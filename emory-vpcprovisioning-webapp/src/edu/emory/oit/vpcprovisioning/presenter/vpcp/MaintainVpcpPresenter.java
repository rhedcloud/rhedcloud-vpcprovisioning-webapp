package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;

public class MaintainVpcpPresenter implements MaintainVpcpView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private VpcpPojo vpcp;
	private VpcRequisitionPojo vpcRequisition;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new Vpcp.
	 */
	public MaintainVpcpPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpcp = null;
		this.vpcRequisition = null;
		this.provisioningId = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainVpcpView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public MaintainVpcpPresenter(ClientFactory clientFactory, VpcpPojo vpcp) {
		this.isEditing = true;
		this.provisioningId = vpcp.getProvisioningId();
		this.clientFactory = clientFactory;
		this.vpcp = vpcp;
		this.vpcRequisition = vpcp.getVpcRequisition();
		clientFactory.getMaintainVpcpView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		ReleaseInfo ri = new ReleaseInfo();
		clientFactory.getShell().setReleaseInfo(ri.toString());
		
		if (provisioningId == null) {
			clientFactory.getShell().setSubTitle("Generate VPCP");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit VPCP");
			startEdit();
		}
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving VPC types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving VPC types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(final List<String> vpcItems) {
						GWT.log("got vpc type items: " + vpcItems.size());
						AsyncCallback<AccountQueryResultPojo> callback = new AsyncCallback<AccountQueryResultPojo>() {
							@Override
							public void onFailure(Throwable caught) {
								getView().hidePleaseWaitDialog();
								getView().hidePleaseWaitPanel();
								GWT.log("Exception retrieving AWS accounts", caught);
								getView().showMessageToUser("There was an exception on the " +
										"server retrieving a list of AWS Accounts.  Message " +
										"from server is: " + caught.getMessage());
							}

							@Override
							public void onSuccess(final AccountQueryResultPojo accountItems) {
								GWT.log("got " + accountItems.getResults().size() + " accounts.");
								AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
									@Override
									public void onFailure(Throwable caught) {
										getView().hidePleaseWaitDialog();
										getView().hidePleaseWaitPanel();
										GWT.log("Exception retrieving Compliance Class types", caught);
										getView().showMessageToUser("There was an exception on the " +
												"server retrieving a list of Compliance Class types.  Message " +
												"from server is: " + caught.getMessage());
									}

									@Override
									public void onSuccess(List<String> complianceClassTypes) {
										getView().setComplianceClassItems(complianceClassTypes);
										getView().setVpcTypeItems(vpcItems);
										getView().setAccountItems(accountItems.getResults());
										getView().initPage();
										getView().setInitialFocus();
										// apply authorization mask
										if (user.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
											getView().applyEmoryAWSAdminMask();
										}
										else if (user.hasPermission(Constants.PERMISSION_VIEW_EVERYTHING)) {
											getView().applyEmoryAWSAuditorMask();
										}
										else {
											// ??
										}
										getView().hidePleaseWaitDialog();
										getView().hidePleaseWaitPanel();
									}
								};
								GWT.log("getting comliance class types");
								VpcProvisioningService.Util.getInstance().getComplianceClassItems(callback);
							}
						};
						GWT.log("getting accounts");
						VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, callback);
					}
				};
				GWT.log("getting vpc type items");
				VpcProvisioningService.Util.getInstance().getVpcTypeItems(callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain vpcp: create/generate");
		isEditing = false;
		getView().setEditing(false);
		vpcRequisition = new VpcRequisitionPojo();
	}

	private void startEdit() {
		GWT.log("Maintain vpcp presenter: edit.  VPC: " + getVpcp().getProvisioningId());
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the vpcp is loaded.
		getView().setLocked(true);
		getView().setEditing(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainVpcpView().setLocked(false);
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
	public void deleteVpcp() {
		if (isEditing) {
			doDeleteVpcp();
		} else {
			doCancelVpcp();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelVpcp() {
		ActionEvent.fire(eventBus, ActionNames.VPC_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteVpcp() {
		if (vpcp == null) {
			return;
		}

		// TODO Delete the vpcp on server then fire onVpcpDeleted();
	}

	@Override
	public void saveVpcp() {
		getView().showPleaseWaitDialog();
		AsyncCallback<VpcpPojo> callback = new AsyncCallback<VpcpPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Vpcp", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Vpcp.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpcpPojo result) {
				getView().hidePleaseWaitDialog();
				// if it was a generate, we'll take them to the VPCP status view
				// So we won't go directly back
				// to the list just yet but instead, we'll show them an immediate 
				// status and give them the opportunity to watch it for a bit
				// before they go back.  So, we'll only fire the VPCP_SAVED event 
				// when/if it's an update and not on the generate.  As of right now
				// we don't think there will be a VPCP update so the update handling 
				// stuff is just here to maintain consistency and if we ever decide
				// a VPCP can be updated, we'll already have the flow here.
				if (!isEditing) {
					// show VPCP status page
//					getVpcpForId(result.getProvisioningId());
					vpcp = result;
					GWT.log("VPCP was generated on the server, showing status page.  VPCP is: " + vpcp);
					ActionEvent.fire(eventBus, ActionNames.VPCP_GENERATED, vpcp);
				}
				else {
					// go back to the list VPCP page (this will likely never happen)
					ActionEvent.fire(eventBus, ActionNames.VPCP_SAVED, vpcp);
				}
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().generateVpcp(vpcRequisition, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateVpcp(vpcp, callback);
		}
	}

	@Override
	public VpcpPojo getVpcp() {
		return this.vpcp;
	}

	@Override
	public boolean isValidVpcpId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private MaintainVpcpView getView() {
		return clientFactory.getMaintainVpcpView();
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

	public void setVpcp(VpcpPojo vpcp) {
		this.vpcp = vpcp;
	}

	@Override
	public VpcRequisitionPojo getVpcRequisition() {
		return vpcRequisition;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w) {
		AsyncCallback<DirectoryMetaDataPojo> callback = new AsyncCallback<DirectoryMetaDataPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(DirectoryMetaDataPojo result) {
				if (result.getFirstName() == null) {
					result.setFirstName("Unknown");
				}
				if (result.getLastName() == null) {
					result.setLastName("Net ID");
				}
				w.setTitle(result.getFirstName() + " " + result.getLastName() + 
					" - from the Identity Service.");
			}
		};
		VpcProvisioningService.Util.getInstance().getDirectoryMetaDataForNetId(netId, callback);
	}

//	@Override
//	public void getVpcpForId(String provisioningId) {
//		AsyncCallback<VpcpQueryResultPojo> callback = new AsyncCallback<VpcpQueryResultPojo>() {
//			@Override
//			public void onFailure(Throwable caught) {
//                getView().hidePleaseWaitPanel();
//                getView().hidePleaseWaitDialog();
//				GWT.log("Exception Retrieving Vpcs", caught);
//				getView().showMessageToUser("There was an exception on the " +
//						"server retrieving your list of Vpcs.  " +
//						"Message from server is: " + caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(VpcpQueryResultPojo result) {
//				GWT.log("Got " + result.getResults().size() + " Vpcps for " + result.getFilterUsed());
//				// TODO: error checking here (must be 1 and only 1 vpcps returned)
//				
//				// TODO: fire VPCP_GENERATED even which will show the VpcpStatusView
////				setVpcp(result.getResults().get(0));
////				getView().showVpcpStatusInformation();
//                getView().hidePleaseWaitDialog();
//                getView().hidePleaseWaitPanel();
//			}
//		};
//
//		GWT.log("refreshing Vpcp object for provisioning id:  " + provisioningId);
//		VpcpQueryFilterPojo filter = new VpcpQueryFilterPojo();
//		filter.setProvisioningId(provisioningId);
//		VpcProvisioningService.Util.getInstance().getVpcpsForFilter(filter, callback);
//	}
}
