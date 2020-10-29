package edu.emory.oit.vpcprovisioning.presenter.role;

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
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class RoleProvisioningStatusPresenter extends PresenterBase implements RoleProvisioningStatusView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private RoleProvisioningPojo roleProvisioning;
	private RoleDeprovisioningPojo roleDeprovisioning;
	private RoleProvisioningSummaryPojo roleProvisioningSummary;
	boolean fromGenerate;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new RoleProvisioning.
	 */
	public RoleProvisioningStatusPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.roleProvisioning = null;
		this.roleDeprovisioning = null;
		this.roleProvisioningSummary = null;
		this.provisioningId = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public RoleProvisioningStatusPresenter(ClientFactory clientFactory, RoleProvisioningSummaryPojo roleProvisioningSummary) {
		this.isEditing = true;
		this.clientFactory = clientFactory;
		this.roleProvisioningSummary = roleProvisioningSummary;
		this.roleProvisioning = roleProvisioningSummary.getProvisioning();
		this.roleDeprovisioning = roleProvisioningSummary.getDeprovisioning();
		if (roleProvisioningSummary.isProvision()) {
			this.provisioningId = roleProvisioning.getProvisioningId();
		}
		else {
			this.provisioningId = roleDeprovisioning.getDeprovisioningId();
		}
		this.fromGenerate = false;
		getView().setPresenter(this);
	}

	public RoleProvisioningStatusPresenter(ClientFactory clientFactory, RoleProvisioningSummaryPojo roleProvisioningSummary, boolean fromGenerate) {
		this.isEditing = true;
		this.roleProvisioningSummary = roleProvisioningSummary;
		this.roleProvisioning = roleProvisioningSummary.getProvisioning();
		this.roleDeprovisioning = roleProvisioningSummary.getDeprovisioning();
		if (roleProvisioningSummary.isProvision()) {
			this.provisioningId = roleProvisioning.getProvisioningId();
		}
		else {
			this.provisioningId = roleDeprovisioning.getDeprovisioningId();
		}
		this.clientFactory = clientFactory;
		this.fromGenerate = fromGenerate;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().clearProvisioningStatus();
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				GWT.log("Exception Retrieving Vpcs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				clientFactory.getShell().setSubTitle("VPN Connection Status");
				getView().enableButtons();
				getView().setUserLoggedIn(user);
				// TODO: depending on how the scheduler works, we may not need 
				// to refreshRoleProvisioningStatusForId here.  If the scheduler immediately 
				// refreshes the delay and then waits, we should just be able 
				// to start the timer and let it do the refresh.
				
				// refresh display with current status
				refreshProvisioningStatusForId(provisioningId);
				
//				getView().refreshRoleProvisioningStatusInformation();
//                getView().hidePleaseWaitPanel();
//                getView().hidePleaseWaitDialog();
				
				// start the timer
				getView().startTimer(5000);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getRoleProvisioningStatusView().setLocked(false);
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
		ActionEvent.fire(eventBus, ActionNames.VPCP_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteRoleProvisioning() {
		if (roleProvisioning == null) {
			return;
		}

		// TODO Delete the roleProvisioning on server then fire onRoleProvisioningDeleted();
	}

	@Override
	public RoleProvisioningPojo getRoleProvisioning() {
		return this.roleProvisioning;
	}

	private RoleProvisioningStatusView getView() {
		return clientFactory.getRoleProvisioningStatusView();
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

	public void setRoleProvisioning(RoleProvisioningPojo roleProvisioning) {
		this.roleProvisioning = roleProvisioning;
	}

	@Override
	public void refreshProvisioningStatusForId(final String provisioningId) {
		AsyncCallback<RoleProvisioningQueryResultPojo> callback = new AsyncCallback<RoleProvisioningQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().stopTimer();
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving RoleProvisionings", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the VPN Connection status information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(RoleProvisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + 
						" RoleProvisionings for the filter: " + result.getFilterUsed());
				
				if (result.getResults().size() == 0) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  No VPN Connections found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server did not return a result "
							+ "for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
									+ "is an unexpected situation that may need "
									+ "to be addressed by system administrators.");
				}
				else if (result.getResults().size() > 1) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  More than one VPN Connection found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server returned more than one VPN Connection "
							+ "for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
									+ "is an unexpected situation that may need "
									+ "to be addressed by system administrators.");
				}
				else {
					// expected behavior
					setRoleProvisioningSummary(result.getResults().get(0));
					if (getRoleProvisioningSummary().isProvision()) {
						setRoleProvisioning(result.getResults().get(0).getProvisioning());
						if (roleProvisioning.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
							getView().stopTimer();
						}
					}
					else {
						setRoleDeprovisioning(result.getResults().get(0).getDeprovisioning());
						if (roleDeprovisioning.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
							getView().stopTimer();
						}
					}
					getView().refreshProvisioningStatusInformation();
	                getView().hidePleaseWaitDialog();
	                getView().hidePleaseWaitPanel();
				}
			}
		};

		GWT.log("[PRESENTER] refreshing RoleProvisioning object for provisioning id:  " + provisioningId);
        getView().showPleaseWaitDialog("Retrieving VPNCP Summaries for the provisioning id: " + provisioningId);
		RoleProvisioningQueryFilterPojo filter = new RoleProvisioningQueryFilterPojo();
		filter.setProvisioningId(provisioningId);
		VpcProvisioningService.Util.getInstance().getRoleProvisioningSummariesForFilter(filter, callback);
	}

	@Override
	public boolean isFromGenerate() {
		return this.fromGenerate;
	}

	@Override
	public void setFromGenerate(boolean fromGenerate) {
		this.fromGenerate = fromGenerate;
	}

	public RoleDeprovisioningPojo getRoleDeprovisioning() {
		return roleDeprovisioning;
	}

	public void setRoleDeprovisioning(RoleDeprovisioningPojo roleDeprovisioning) {
		this.roleDeprovisioning = roleDeprovisioning;
	}

	public RoleProvisioningSummaryPojo getRoleProvisioningSummary() {
		return roleProvisioningSummary;
	}

	public void setRoleProvisioningSummary(RoleProvisioningSummaryPojo roleProvisioningSummary) {
		this.roleProvisioningSummary = roleProvisioningSummary;
	}

	@Override
	public void addDirectoryPersonInRoleToAccount(DirectoryPersonPojo roleAssignee, AccountPojo account,
			String roleName) {

		AsyncCallback<RoleAssignmentPojo> raCallback = new AsyncCallback<RoleAssignmentPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server creating the role assignment.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final RoleAssignmentPojo roleAssignment) {
				
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();

				// for now, just go to the account maintenance page
				ActionEvent.fire(getEventBus(), ActionNames.MAINTAIN_ACCOUNT, getRoleProvisioningSummary().getAccount());
				
				// TODO: may also go to the role provisioning list page
				// when/if the provisioning type is a deprovision.
				// that will just depend on where we initiate the deprovisioning
				// process from
//				if (getRoleProvisioningSummary().isProvision()) {
//					ActionEvent.fire(getEventBus(), ActionNames.MAINTAIN_ACCOUNT, getRoleProvisioningSummary().getAccount());
//				}
//				else {
//					// TODO: go to wherever we initiate deprovisioning from
//				}
				
			}
		};
		// now, create the role assignment and add the role assignment to the account
		getView().showPleaseWaitDialog("Creating Role Assignment with the IDM service...");
		VpcProvisioningService.Util.getInstance().createRoleAssignmentForPersonInAccount(roleAssignee.getKey(), account.getAccountId(), roleName, raCallback);
	}
}
