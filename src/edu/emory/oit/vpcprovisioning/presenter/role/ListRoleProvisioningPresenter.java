package edu.emory.oit.vpcprovisioning.presenter.role;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.RoleProvisioningListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListRoleProvisioningPresenter extends PresenterBase implements ListRoleProvisioningView.Presenter {
	private static final Logger log = Logger.getLogger(ListVpcPresenter.class.getName());

	/**
	 * A boolean indicating that we should clear the Vpc list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	RoleProvisioningQueryFilterPojo filter;
	UserAccountPojo userLoggedIn;
	RoleProvisioningPojo selectedProvisioning;

	public ListRoleProvisioningPresenter(ClientFactory clientFactory, boolean clearList, RoleProvisioningQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListRoleProvisioningView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListRoleProvisioningPresenter(ClientFactory clientFactory, ListRoleProvisioningPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListRoleProvisioningView getView() {
		return clientFactory.getListRoleProvisioningView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving User Logged In...");
		
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
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().enableButtons();
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Role Provisioning Runs");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}


				// Request the Vpc list now.
				if (getView().viewAllRoleProvisionings()) {
					// show all of them
					refreshListWithAllRoleProvisionings(user);
				}
				else {
					// only show the default maximum
					refreshListWithMaximumRoleProvisionings(user);
				}
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	public void refreshList(final UserAccountPojo user) {
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<RoleProvisioningQueryResultPojo> callback = new AsyncCallback<RoleProvisioningQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving VPNs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of VPNs.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(RoleProvisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " RoleProvisionings for " + result.getFilterUsed());
				setRoleProvisioningSummaryList(result.getResults());
				// apply authorization mask
				if (user.isNetworkAdmin()) {
					getView().applyNetworkAdminMask();
				}
				else if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
			}
		};

		GWT.log("refreshing RoleProvisioning list...");
		VpcProvisioningService.Util.getInstance().getRoleProvisioningSummariesForFilter(filter, callback);
	}

	@Override
	public void refreshListWithMaximumRoleProvisionings(UserAccountPojo user) {
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving the default maximum list of Role Provisioning objects from the AWS Account service...");

		filter = new RoleProvisioningQueryFilterPojo();
		filter.setAllObjects(false);
		filter.setDefaultMaxObjects(true);
		
		refreshList(user);
	}

	@Override
	public void refreshListWithAllRoleProvisionings(UserAccountPojo user) {
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving ALL Role Provisioning objects from the AWS Account service (this could take a while)...");

		filter = new RoleProvisioningQueryFilterPojo();
		filter.setAllObjects(true);
		filter.setDefaultMaxObjects(false);
		
		refreshList(user);
	}

	@Override
	public void filterByProvisioningId(boolean includeAllRoleProvisionings, String provisioningId) {
		if (provisioningId == null || provisioningId.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please enter a provisioning id");
			return;
		}

		getView().showFilteredStatus();
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Filtering list by provisioning id " + provisioningId + "...");
		
		filter = new RoleProvisioningQueryFilterPojo();
		filter.setAllObjects(false);
		filter.setDefaultMaxObjects(false);
		filter.setProvisioningId(provisioningId);
		
		refreshList(userLoggedIn);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setRoleProvisioningSummaryList(List<RoleProvisioningSummaryPojo> summaries) {
		getView().setRoleProvisioningSummaries(summaries);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new RoleProvisioningListUpdateEvent(summaries), this);
		}
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectRoleProvisioning(RoleProvisioningPojo selected) {
		
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public RoleProvisioningQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(RoleProvisioningQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

//	@Override
//	public void deprovisionRole(final RoleProvisioningPojo provisionedRole) {
//		selectedProvisioning = provisionedRole;
//		VpcpConfirm.confirm(
//			ListRoleProvisioningPresenter.this, 
//			"Confirm Deprovision VPN Connection", 
//			"Deprovisiong the VPN Connection " + selectedProvisioning.getRequisition().getProfile().getVpcNetwork() + "?");
//	}

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
//				// if it was a generate, we'll take them to the VPNCP status view
//				// So we won't go directly back
//				// to the list just yet but instead, we'll show them an immediate 
//				// status and give them the opportunity to watch it for a bit
//				// before they go back.  So, we'll only fire the VPCP_SAVED event 
//				// when/if it's an update and not on the generate.  As of right now
//				// we don't think there will be a VPCP update so the update handling 
//				// stuff is just here to maintain consistency and if we ever decide
//				// a VPCP can be updated, we'll already have the flow here.
//				// show VPNCP status page
//				final RoleDeprovisioningPojo vpncdp = result;
//				GWT.log("VPNCDP was generated on the server, showing status page.  "
//						+ "VPNCDP is: " + vpncdp);
//				RoleProvisioningSummaryPojo vpncpSummary = new RoleProvisioningSummaryPojo();
//				vpncpSummary.setDeprovisioning(vpncdp);
//				ActionEvent.fire(eventBus, ActionNames.VPNCDP_GENERATED, vpncpSummary);
//			}
//		};
//		getView().showPleaseWaitDialog("Generating VPC Deprovisioning object...");
//		VpcProvisioningService.Util.getInstance().generateRoleDeprovisioning(selectedProvisioning.getRequisition(), callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN was NOT deprovisioned");
	}

	@Override
	public void deprovisionRole(RoleProvisioningRequisitionPojo requisition) {
		getView().showMessageToUser("Comming soon...");
	}

}
