package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.VpncpListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;

public class ListVpnConnectionProvisioningPresenter extends PresenterBase implements ListVpnConnectionProvisioningView.Presenter {
	private static final Logger log = Logger.getLogger(ListVpcPresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the Vpc list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the Vpc list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	VpnConnectionProvisioningQueryFilterPojo filter;
	UserAccountPojo userLoggedIn;
	VpnConnectionProvisioningPojo selectedProvisioning;

	public ListVpnConnectionProvisioningPresenter(ClientFactory clientFactory, boolean clearList, VpnConnectionProvisioningQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListVpnConnectionProvisioningView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListVpnConnectionProvisioningPresenter(ClientFactory clientFactory, ListVpnConnectionProvisioningPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListVpnConnectionProvisioningView getView() {
		return clientFactory.getListVpnConnectionProvisioningView();
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
		getView().showPleaseWaitPanel("Retrieving VPN Provisioning items...please wait");
		getView().showPleaseWaitDialog("Retrieving VPN Provisioning items from the Network OPs Service...");
		
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
				clientFactory.getShell().setSubTitle("VPCPs");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}


				// Request the Vpc list now.
				if (getView().viewAllVpnConnectionProvisionings()) {
					// show all of them
					refreshListWithAllVpnConnectionProvisionings(user);
				}
				else {
					// only show the default maximum
					refreshListWithMaximumVpnConnectionProvisionings(user);
				}
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	public void refreshList(final UserAccountPojo user) {
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<VpnConnectionProvisioningQueryResultPojo> callback = new AsyncCallback<VpnConnectionProvisioningQueryResultPojo>() {
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
			public void onSuccess(VpnConnectionProvisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " VpnConnectionProvisionings for " + result.getFilterUsed());
				setVpnConnectionProvisioningSummaryList(result.getResults());
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

		GWT.log("refreshing VpnConnectionProvisioning list...");
		VpcProvisioningService.Util.getInstance().getVpncpSummariesForFilter(filter, callback);
	}

	@Override
	public void refreshListWithMaximumVpnConnectionProvisionings(UserAccountPojo user) {
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving the default maximum list of VPNP objects from the Network OPs service...");

		filter = new VpnConnectionProvisioningQueryFilterPojo();
		filter.setAllVpncps(false);
		filter.setDefaultMaxVpncps(true);
		
		refreshList(user);
	}

	@Override
	public void refreshListWithAllVpnConnectionProvisionings(UserAccountPojo user) {
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving ALL VPCP objects from the AWS Account service (this could take a while)...");

		filter = new VpnConnectionProvisioningQueryFilterPojo();
		filter.setAllVpncps(true);
		filter.setDefaultMaxVpncps(false);
		
		refreshList(user);
	}

	@Override
	public void filterByProvisioningId(boolean includeAllVpnConnectionProvisionings, String provisioningId) {
		if (provisioningId == null || provisioningId.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please enter a provisioning id");
			return;
		}

		getView().showFilteredStatus();
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Filtering list by provisioning id " + provisioningId + "...");
		
		filter = new VpnConnectionProvisioningQueryFilterPojo();
		filter.setAllVpncps(false);
		filter.setDefaultMaxVpncps(false);
		filter.setProvisioningId(provisioningId);
		
		refreshList(userLoggedIn);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setVpnConnectionProvisioningSummaryList(List<VpnConnectionProvisioningSummaryPojo> summaries) {
		getView().setVpnConnectionProvisioningSummaries(summaries);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new VpncpListUpdateEvent(summaries), this);
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
	public void selectVpnConnectionProvisioning(VpnConnectionProvisioningPojo selected) {
		
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public VpnConnectionProvisioningQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpnConnectionProvisioningQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deprovisionVpnConnection(final VpnConnectionProvisioningPojo provisionedVpnConnection) {
		selectedProvisioning = provisionedVpnConnection;
		VpcpConfirm.confirm(
			ListVpnConnectionProvisioningPresenter.this, 
			"Confirm Deprovision VPN Connection", 
			"Deprovisiong the VPN Connection " + selectedProvisioning.getRequisition().getProfile().getVpcNetwork() + "?");
	}

	@Override
	public void vpcpConfirmOkay() {
		AsyncCallback<VpnConnectionDeprovisioningPojo> callback = new AsyncCallback<VpnConnectionDeprovisioningPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception generating the VpnConnectionDeprovisioning", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server generating the VpnConnectionDeprovisioning.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpnConnectionDeprovisioningPojo result) {
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
				// show VPNCP status page
				final VpnConnectionDeprovisioningPojo vpncdp = result;
				GWT.log("VPNCDP was generated on the server, showing status page.  "
						+ "VPNCDP is: " + vpncdp);
				VpnConnectionProvisioningSummaryPojo vpncpSummary = new VpnConnectionProvisioningSummaryPojo();
				vpncpSummary.setDeprovisioning(vpncdp);
				ActionEvent.fire(eventBus, ActionNames.VPNCDP_GENERATED, vpncpSummary);
			}
		};
		getView().showPleaseWaitDialog("Generating VPC Deprovisioning object...");
		VpcProvisioningService.Util.getInstance().generateVpnConnectionDeprovisioning(selectedProvisioning.getRequisition(), callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN was NOT deprovisioned");
	}

}
