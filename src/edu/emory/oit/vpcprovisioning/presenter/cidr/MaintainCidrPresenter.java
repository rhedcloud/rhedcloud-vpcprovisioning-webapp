package edu.emory.oit.vpcprovisioning.presenter.cidr;

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
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentStatus;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainCidrPresenter extends PresenterBase implements MaintainCidrView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String cidrId;
	private CidrPojo cidr;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new CIDR.
	 */
	public MaintainCidrPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.cidr = null;
		this.cidrId = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainCidrView().setPresenter(this);
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainCidrPresenter(ClientFactory clientFactory, CidrPojo cidr) {
		this.isEditing = true;
		this.cidrId = cidr.getCidrId();
		this.clientFactory = clientFactory;
		this.cidr = cidr;
		clientFactory.getMaintainCidrView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);

		if (cidrId == null) {
			clientFactory.getShell().setSubTitle("Create CIDR");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit CIDR");
			startEdit();
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				// set associated cidr type items
				List<String> associatedCidrTypes = new java.util.ArrayList<String>();
				associatedCidrTypes.add("VpnInsideIpCidr");
				associatedCidrTypes.add("CustomerGatewayIpAddress");

				getView().setAssociatedCidrTypeItems(associatedCidrTypes);
				getView().initPage();
				getView().setInitialFocus();
				
				// if selected CIDR is assigned, disable all fields (except cancel button)
				// an assigned CIDR cannot be edited 
				AsyncCallback<CidrAssignmentStatus> isAssignedCB = new AsyncCallback<CidrAssignmentStatus>() {

					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						getView().showMessageToUser("There was an exception on the " +
								"server determining the Cidr's assignment status.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(CidrAssignmentStatus assignmentStatus) {
						if (assignmentStatus != null && assignmentStatus.isAssigned()) {
							clientFactory.getShell().setSubTitle("View CIDR (assigned to VPC: " + 
								assignmentStatus.getCidrAssignment().getOwnerId() + ")");
							getView().setLocked(true);
						}
						else {
							// apply authorization mask
							if (user.isCentralAdmin()) {
								getView().applyCentralAdminMask();
							}
							else {
								clientFactory.getShell().setSubTitle("View CIDR");
								getView().applyAWSAccountAuditorMask();
							}
						}
					}
					
				};
				VpcProvisioningService.Util.getInstance().getCidrAssignmentStatusForCidr(cidr, isAssignedCB);

			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		cidr = new CidrPojo();
	}

	private void startEdit() {
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the cidr is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainCidrView().setLocked(false);
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
	public void deleteCidr() {
		if (isEditing) {
			doDeleteCidr();
		} else {
			doCancelCidr();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelCidr() {
		ActionEvent.fire(eventBus, ActionNames.CIDR_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteCidr() {
		if (cidr == null) {
			return;
		}

		// TODO Delete the CIDR on the server then fire onCidrDeleted();
	}

	@Override
	public void saveCidr() {
		// save on server
		getView().showPleaseWaitDialog("Saving CIDR...");
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
		AsyncCallback<CidrPojo> callback = new AsyncCallback<CidrPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Cidr", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the CIDR.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(CidrPojo result) {
				
				
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.CIDR_SAVED, result);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createCidr(cidr, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateCidr(cidr, callback);
		}
	}

	@Override
	public CidrPojo getCidr() {
		return this.cidr;
	}

	@Override
	public boolean isValidNetwork(String value) {
		
		return false;
	}

	@Override
	public boolean isValidBits(String value) {
		
		return false;
	}

	private MaintainCidrView getView() {
		return clientFactory.getMaintainCidrView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getCidrId() {
		return cidrId;
	}

	public void setCidrId(String cidrId) {
		this.cidrId = cidrId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setCidr(CidrPojo cidr) {
		this.cidr = cidr;
	}
}
