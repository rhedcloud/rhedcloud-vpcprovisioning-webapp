package edu.emory.oit.vpcprovisioning.presenter.role;

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
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainRoleProvisioningPresenter extends PresenterBase implements MaintainRoleProvisioningView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private RoleProvisioningPojo roleProvisioning;
	private RoleProvisioningRequisitionPojo roleRequisition;
	private UserAccountPojo userLoggedIn;
	private DirectoryPersonPojo ownerDirectoryPerson;
	private AccountPojo account;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;
	private boolean isRegen;

	/**
	 * For provisioning a Role
	 */
	public MaintainRoleProvisioningPresenter(ClientFactory clientFactory2,
			RoleProvisioningRequisitionPojo vpncRequisition, AccountPojo account) {

		this.isEditing = false;
		this.isRegen = false;
		this.roleProvisioning = null;
		this.roleRequisition = vpncRequisition;
		this.provisioningId = null;
		this.account = account;
		this.clientFactory = clientFactory2;
		getView().setPresenter(this);
	}
	
	/**
	 * For editing an existing provisioning (N/A)??
	 */
	public MaintainRoleProvisioningPresenter(ClientFactory clientFactory2,
			RoleProvisioningPojo vrp) {

		this.isEditing = true;
		this.isRegen = true;
		this.roleProvisioning = vrp;
		this.roleRequisition = vrp.getRequisition();
		this.provisioningId = vrp.getProvisioningId();
		this.clientFactory = clientFactory2;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().showPleaseWaitDialog("Please wait...");
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		setReleaseInfo(clientFactory);
		
		GWT.log("[MaintainRoleProvisioningPresenter.start] provisioning new role for account: " + getRoleProvisioningRequisition().getAccountId());
		if (provisioningId == null) {
			if (isRegen) {
				getView().setHeading("Re-Provision a Role");
			}
			else {
				getView().setHeading("Provision a new Role");
			}
			clientFactory.getShell().setSubTitle("Generate RoleProvisioning");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit RoleProvisioning");
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
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
        getView().hidePleaseWaitPanel();
        getView().hidePleaseWaitDialog();
	}

	private void startCreate() {
		GWT.log("Maintain role provisioning: create/generate");
		isEditing = false;
		getView().setEditing(false);
		if (isRegen) {
			GWT.log("[MaintainRoleProvisioningPresenter] it is a RE-PROVISION");
			roleRequisition = new RoleProvisioningRequisitionPojo();
			roleRequisition.setAccountId(this.getAccount().getAccountId());
		}
		else {
			GWT.log("[MaintainRoleProvisioningPresenter] it is a PROVISION");
			roleRequisition = new RoleProvisioningRequisitionPojo();
			roleRequisition.setAccountId(this.getAccount().getAccountId());
		}
	}

	private void startEdit() {
		GWT.log("Maintain role provisioning presenter: edit.  "
			+ "RoleProvisioning: " + getRoleProvisioning().getProvisioningId());
		isEditing = true;
		// Lock the display until the vpcp is loaded.
		getView().setLocked(true);
		getView().setEditing(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainRoleProvisioningView().setLocked(false);
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
	public void deleteRoleProvisioning() {
		if (isEditing) {
			doDeleteRoleProvisioning();
		} else {
			doCancelRoleProvisioning();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelRoleProvisioning() {
		ActionEvent.fire(eventBus, ActionNames.ROLE_PROVISIONING_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteRoleProvisioning() {
		if (roleProvisioning == null) {
			return;
		}

		// Delete the vpcp on server then fire onRoleProvisioningDeleted();
	}

	@Override
	public void saveRoleProvisioning() {
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
		
		final AsyncCallback<RoleProvisioningPojo> roleProvisioningCallback = new AsyncCallback<RoleProvisioningPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception generating the RoleProvisioning", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server generating the RoleProvisioning.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(RoleProvisioningPojo result) {
				getView().hidePleaseWaitDialog();
				// if it was a generate, we'll take them to the RoleProvisioning status view
				// So we won't go directly back
				// to the list just yet but instead, we'll show them an immediate 
				// status and give them the opportunity to watch it for a bit
				// before they go back.  So, we'll only fire the VPCP_SAVED event 
				// when/if it's an update and not on the generate.  As of right now
				// we don't think there will be a VPCP update so the update handling 
				// stuff is just here to maintain consistency and if we ever decide
				// a VPCP can be updated, we'll already have the flow here.
				if (!isEditing) {
					// show RoleProvisioning status page
					roleProvisioning = result;
					GWT.log("RoleProvisioning was generated on the server, showing status page.  "
							+ "RoleProvisioning object is: " + roleProvisioning);
					RoleProvisioningSummaryPojo summary = new RoleProvisioningSummaryPojo();
					summary.setProvisioning(roleProvisioning);
					summary.setAccount(getAccount());
					ActionEvent.fire(eventBus, ActionNames.ROLE_PROVISIONING_GENERATED, summary);
				}
				else {
					// go back to the list VPCP page (this will likely never happen)
					ActionEvent.fire(eventBus, ActionNames.ROLE_PROVISIONING_SAVED, roleProvisioning);
				}
			}
		};

		if (!this.isEditing) {
			// it's a generate
			VpcProvisioningService.Util.getInstance().generateRoleProvisioning(roleRequisition, roleProvisioningCallback);
		}
		else {
			// it's an update
//				VpcProvisioningService.Util.getInstance().updateVpncp(vpcp, callback);
		}
	}

	@Override
	public RoleProvisioningPojo getRoleProvisioning() {
		return this.roleProvisioning;
	}

	@Override
	public boolean isValidRoleProvisioningId(String value) {
		
		return false;
	}

	private MaintainRoleProvisioningView getView() {
		return clientFactory.getMaintainRoleProvisioningView();
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

	public void setRoleProvisioning(RoleProvisioningPojo vpcp) {
		this.roleProvisioning = vpcp;
	}

	@Override
	public RoleProvisioningRequisitionPojo getRoleProvisioningRequisition() {
		return roleRequisition;
	}

	public void setRoleProvisioningRequisition(RoleProvisioningRequisitionPojo vpcRequisition) {
		this.roleRequisition = vpcRequisition;
	}

	@Override
	public void setOwnerDirectoryPerson(DirectoryPersonPojo pojo) {
		this.ownerDirectoryPerson = pojo;
	}

	@Override
	public DirectoryPersonPojo getOwnerDirectoryPerson() {
		return this.ownerDirectoryPerson;
	}

	@Override
	public void vpcpConfirmOkay() {
//		AsyncCallback<RoleDeprovisioningPojo> callback = new AsyncCallback<RoleDeprovisioningPojo>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				getView().hidePleaseWaitDialog();
//				GWT.log("Exception generating the RoleDeprovisioning", caught);
//				getView().showMessageToUser("There was an exception on the " +
//						"server generating the RoleDeprovisioning.  Message " +
//						"from server is: " + caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(RoleDeprovisioningPojo result) {
//				getView().hidePleaseWaitDialog();
//				// if it was a generate, we'll take them to the RoleProvisioning status view
//				// So we won't go directly back
//				// to the list just yet but instead, we'll show them an immediate 
//				// status and give them the opportunity to watch it for a bit
//				// before they go back.  So, we'll only fire the VPCP_SAVED event 
//				// when/if it's an update and not on the generate.  As of right now
//				// we don't think there will be a VPCP update so the update handling 
//				// stuff is just here to maintain consistency and if we ever decide
//				// a VPCP can be updated, we'll already have the flow here.
//				// show RoleProvisioning status page
//				final RoleDeprovisioningPojo vpncdp = result;
//				GWT.log("VPNCDP was generated on the server, showing status page.  "
//						+ "VPNCDP is: " + vpncdp);
//				RoleProvisioningSummaryPojo roleProvisioningSummary = new RoleProvisioningSummaryPojo();
//				roleProvisioningSummary.setDeprovisioning(vpncdp);
//				ActionEvent.fire(eventBus, ActionNames.VPNCDP_GENERATED, roleProvisioningSummary);
//			}
//		};
//		getView().showPleaseWaitDialog("Generating VPC Deprovisioning object...");
//		VpcProvisioningService.Util.getInstance().generateRoleDeprovisioning(roleRequisition, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN was NOT deprovisioned");
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

}
