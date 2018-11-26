package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.TunnelProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileAssignmentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;

public class MaintainVpnConnectionProvisioningPresenter extends PresenterBase implements MaintainVpnConnectionProvisioningView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private VpnConnectionProvisioningPojo vpncp;
	private VpnConnectionRequisitionPojo vpnConnectionRequisition;
	private VpnConnectionProfilePojo vpnConnectionProfile;
	private UserAccountPojo userLoggedIn;
	private DirectoryPersonPojo ownerDirectoryPerson;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new VpnConnectionProvisioning.
	 */
	public MaintainVpnConnectionProvisioningPresenter(ClientFactory clientFactory, VpnConnectionProfilePojo profile) {
		this.isEditing = false;
		this.vpncp = null;
		this.vpnConnectionRequisition = null;
		this.provisioningId = null;
		this.vpnConnectionProfile = profile;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public MaintainVpnConnectionProvisioningPresenter(ClientFactory clientFactory, VpnConnectionProvisioningPojo vpcp) {
		this.isEditing = true;
		this.provisioningId = vpcp.getProvisioningId();
		this.clientFactory = clientFactory;
		this.vpncp = vpcp;
		this.vpnConnectionRequisition = vpcp.getRequisition();
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().showPleaseWaitDialog("Retrieving VPN Profile detail from the Network OPs service...");
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		
		if (provisioningId == null) {
			clientFactory.getShell().setSubTitle("Generate VPNCP");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit VPNCP");
			startEdit();
		}
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your user information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().enableButtons();
				getView().setUserLoggedIn(user);
				userLoggedIn = user;

				// just use the profile that was passed in
				initializeView();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
	
	private void initializeView() {
		getView().initPage();
		getView().setInitialFocus();
		// apply authorization mask
		if (userLoggedIn.isNetworkAdmin()) {
			getView().applyNetworkAdminMask();
		}
		else if (userLoggedIn.isCentralAdmin()) {
			getView().applyCentralAdminMask();
		}
		else if (userLoggedIn.isAuditor()) {
			getView().applyAWSAccountAuditorMask();
		}
		else {
			getView().showMessageToUser("An error has occurred.  The user logged in does not "
					+ "appear to be associated to any valid roles for this page.");
			getView().applyAWSAccountAuditorMask();
		}
		getView().hidePleaseWaitDialog();
		getView().hidePleaseWaitPanel();
	}

	private void startCreate() {
		GWT.log("Maintain vpcp: create/generate");
		isEditing = false;
		getView().setEditing(false);
		vpnConnectionRequisition = new VpnConnectionRequisitionPojo();
		vpnConnectionRequisition.setProfile(vpnConnectionProfile);
	}

	private void startEdit() {
		GWT.log("Maintain vpcp presenter: edit.  VPC: " + getVpnConnectionProvisioning().getProvisioningId());
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the vpcp is loaded.
		getView().setLocked(true);
		getView().setEditing(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainVpnConnectionProvisioningView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void deleteVpnConnectionProvisioning() {
		if (isEditing) {
			doDeleteVpnConnectionProvisioning();
		} else {
			doCancelVpnConnectionProvisioning();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelVpnConnectionProvisioning() {
		ActionEvent.fire(eventBus, ActionNames.VPC_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteVpnConnectionProvisioning() {
		if (vpncp == null) {
			return;
		}

		// TODO Delete the vpcp on server then fire onVpnConnectionProvisioningDeleted();
	}

	@Override
	public void saveVpnConnectionProvisioning() {
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
		}
		
		final AsyncCallback<VpnConnectionProvisioningPojo> vpncpCallback = new AsyncCallback<VpnConnectionProvisioningPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception generating the VpnConnectionProvisioning", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server generating the VpnConnectionProvisioning.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpnConnectionProvisioningPojo result) {
				getView().hidePleaseWaitDialog();
				// if it was a generate, we'll take them to the VPNCP status view
				// So we won't go directly back
				// to the list just yet but instead, we'll show them an immediate 
				// status and give them the opportunity to watch it for a bit
				// before they go back.  So, we'll only fire the VPCP_SAVED event 
				// when/if it's an update and not on the generate.  As of right now
				// we don't think there will be a VPCP update so the update handling 
				// stuff is just here to maintain consistency and if we ever decide
				// a VPCP can be updated, we'll already have the flow here.
				if (!isEditing) {
					// show VPNCP status page
					vpncp = result;
					GWT.log("VPNCP was generated on the server, showing status page.  "
							+ "VPMCP is: " + vpncp);
					ActionEvent.fire(eventBus, ActionNames.VPNCP_GENERATED, vpncp);
				}
				else {
					// go back to the list VPCP page (this will likely never happen)
					ActionEvent.fire(eventBus, ActionNames.VPNCP_SAVED, vpncp);
				}
			}
		};

		if (vpnConnectionRequisition.getProfile() == null) {
			// TODO: vpnProvisioningProfile is null, we need to generate
			// a VpnConnectionProfileAssignment to get the next available
			// profile, then, we'll use that profile in our generate.
			getView().showPleaseWaitDialog("Generating VPN Connection Profile Assignment...");
			
			AsyncCallback<VpnConnectionProfileAssignmentPojo> vcpaCb = new AsyncCallback<VpnConnectionProfileAssignmentPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					getView().hidePleaseWaitDialog();
					GWT.log("Exception generating the VpnConnectionProfileAssignment", caught);
					getView().showMessageToUser("There was an exception on the " +
							"server saving the VpnConnectionProfileAssignment.  Message " +
							"from server is: " + caught.getMessage());
				}

				@Override
				public void onSuccess(final VpnConnectionProfileAssignmentPojo result) {
					// now we have to query for the actual profile passing
					// 	result.getVpnConnectionProfileId();
					// 	result.getOwnerId();
					getView().hidePleaseWaitDialog();
					getView().showPleaseWaitDialog("Retrieving VpnConnectionProfile that was assigned to this VPC...");
					
					AsyncCallback<VpnConnectionProfileQueryResultPojo> vcpaCb = new AsyncCallback<VpnConnectionProfileQueryResultPojo>() {
						@Override
						public void onFailure(Throwable caught) {
							getView().hidePleaseWaitDialog();
							GWT.log("Exception retrieving the VpnConnectionProfileAssignment", caught);
							getView().showMessageToUser("There was an exception on the " +
									"server retrieving the VpnConnectionProfileAssignment.  Message " +
									"from server is: " + caught.getMessage());
						}

						@Override
						public void onSuccess(VpnConnectionProfileQueryResultPojo result) {
							// now we can finally generate the vpn provisioning object
							getView().hidePleaseWaitDialog();
							if (result.getResults().size() == 1) {
								VpnConnectionProfilePojo vcp = result.getResults().get(0).getProfile();
								for (TunnelProfilePojo tpp : vcp.getTunnelProfiles()) {
									GWT.log("tunnel description is: " + tpp.getTunnelDescription());
									String newDesc = tpp.getTunnelDescription().replaceAll(Constants.TUNNEL_AVAILABLE, vpnConnectionRequisition.getOwnerId());
									GWT.log("new tunnel description is: " + newDesc);
									tpp.setTunnelDescription(newDesc);
								}
								vpnConnectionRequisition.setProfile(vcp);
								getView().showPleaseWaitDialog("Generating VPC Provisioning object for "
									+ "connection profile " 
									+ vpnConnectionRequisition.getProfile().getVpnConnectionProfileId() + "...");
								VpcProvisioningService.Util.getInstance().generateVpncp(vpnConnectionRequisition, vpncpCallback);
							}
							else {
								// TODO:  Error
								getView().hidePleaseWaitDialog();
								getView().showMessageToUser("Incorrect number of "
									+ "VpnConnectionProfiles returned in query.  Expected EXACTLY 1 and "
									+ "got " + result.getResults().size());
							}
						}
					};
					VpnConnectionProfileQueryFilterPojo filter = new VpnConnectionProfileQueryFilterPojo();
					filter.setVpnConnectionProfileId(result.getVpnConnectionProfileId());
					VpcProvisioningService.Util.getInstance().getVpnConnectionProfilesForFilter(filter, vcpaCb);
				}
			};
			VpnConnectionProfileAssignmentRequisitionPojo vcpar = new VpnConnectionProfileAssignmentRequisitionPojo();
			vcpar.setOwnerId(vpnConnectionRequisition.getOwnerId());
			VpcProvisioningService.Util.getInstance().generateVpnConnectionProfileAssignment(vcpar, vcpaCb);
		}
		else {
			if (!this.isEditing) {
				// it's a generate
				// TODO: create VpnConnectionProfileAssignment using the profile passed in with the requisition
				AsyncCallback<VpnConnectionProfileAssignmentPojo> vcpaCb = new AsyncCallback<VpnConnectionProfileAssignmentPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception creating the VpnConnectionProfileAssignment", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server saving the VpnConnectionProfileAssignment.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(VpnConnectionProfileAssignmentPojo result) {
						getView().hidePleaseWaitDialog();
						getView().showPleaseWaitDialog("Generating VPC Provisioning object...");
						VpcProvisioningService.Util.getInstance().generateVpncp(vpnConnectionRequisition, vpncpCallback);
					}
				};
				getView().showPleaseWaitDialog("Creating the VPN Connection Profile Assignment object...");
				VpnConnectionProfileAssignmentPojo vcpa = new VpnConnectionProfileAssignmentPojo();
				vcpa.setVpnConnectionProfileId(vpnConnectionRequisition.getProfile().getVpnConnectionProfileId());
				vcpa.setOwnerId(vpnConnectionRequisition.getOwnerId());
				// TODO: description and purpose are required fields but we're not collecting that data yet...
				vcpa.setDescription("Unknown description");
				vcpa.setPurpose("Unknown purpose");
				VpcProvisioningService.Util.getInstance().createVpnConnectionProfileAssignment(vcpa, vcpaCb);
			}
			else {
				// it's an update
//				VpcProvisioningService.Util.getInstance().updateVpncp(vpcp, callback);
			}
		}
	}

	@Override
	public VpnConnectionProvisioningPojo getVpnConnectionProvisioning() {
		return this.vpncp;
	}

	@Override
	public boolean isValidVpnConnectionProvisioningId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private MaintainVpnConnectionProvisioningView getView() {
		return clientFactory.getMaintainVpnConnectionProvisioningView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setVpnConnectionProvisioning(VpnConnectionProvisioningPojo vpcp) {
		this.vpncp = vpcp;
	}

	@Override
	public VpnConnectionRequisitionPojo getVpnConnectionRequisition() {
		return vpnConnectionRequisition;
	}

	public void setVpnConnectionRequisition(VpnConnectionRequisitionPojo vpcRequisition) {
		this.vpnConnectionRequisition = vpcRequisition;
	}

	public VpnConnectionProfilePojo getVpnConnectionProfile() {
		return vpnConnectionProfile;
	}

	public void setVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile) {
		this.vpnConnectionProfile = vpnConnectionProfile;
	}
	@Override
	public void setOwnerDirectoryPerson(DirectoryPersonPojo pojo) {
		this.ownerDirectoryPerson = pojo;
	}

	@Override
	public DirectoryPersonPojo getOwnerDirectoryPerson() {
		return this.ownerDirectoryPerson;
	}
}
