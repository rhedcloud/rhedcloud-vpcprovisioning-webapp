package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.AccountProvisioningListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListAccountProvisioningPresenter extends PresenterBase implements ListAccountProvisioningView.Presenter {
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
	
	AccountProvisioningQueryFilterPojo filter;
	UserAccountPojo userLoggedIn;
	AccountProvisioningPojo selectedProvisioning;

	public ListAccountProvisioningPresenter(ClientFactory clientFactory, boolean clearList, AccountProvisioningQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListAccountProvisioningView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListAccountProvisioningPresenter(ClientFactory clientFactory, ListAccountProvisioningPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListAccountProvisioningView getView() {
		return clientFactory.getListAccountProvisioningView();
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
				clientFactory.getShell().setSubTitle("VPCPs");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}


				// Request the Vpc list now.
				if (getView().viewAllAccountProvisionings()) {
					// show all of them
					refreshListWithAllAccountProvisionings(user);
				}
				else {
					// only show the default maximum
					refreshListWithMaximumAccountProvisionings(user);
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
		getView().showPleaseWaitDialog("Retrieving Account Provisioning items from the Account Service...");
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<AccountProvisioningQueryResultPojo> callback = new AsyncCallback<AccountProvisioningQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Account Provisioning/Deprovisioning items", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Account Provisioning/Deprovisioning items  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(AccountProvisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " AccountProvisionings for " + result.getFilterUsed());
				setAccountProvisioningSummaryList(result.getResults());
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

		GWT.log("refreshing AccountProvisioning list...");
		VpcProvisioningService.Util.getInstance().getAccountProvisioningSummariesForFilter(filter, callback);
	}

	@Override
	public void refreshListWithMaximumAccountProvisionings(UserAccountPojo user) {
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving the default maximum list of Account Provisioning/Deprovisioning objects from the Network OPs service...");

		filter = new AccountProvisioningQueryFilterPojo();
		filter.setAllObjects(false);
		filter.setDefaultMaxObjects(true);
		
		refreshList(user);
	}

	@Override
	public void refreshListWithAllAccountProvisionings(UserAccountPojo user) {
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving ALL Account Provisioning/Deprovisioning items from the AWS Account service (this could take a while)...");

		filter = new AccountProvisioningQueryFilterPojo();
		filter.setAllObjects(true);
		filter.setDefaultMaxObjects(false);
		
		refreshList(user);
	}

	@Override
	public void filterByDeprovisioningId(boolean includeAllAccountProvisionings, String deprovisioningId) {
		if (deprovisioningId == null || deprovisioningId.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please enter a provisioning id");
			return;
		}

		getView().showFilteredStatus();
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Filtering list by provisioning id " + deprovisioningId + "...");
		
		filter = new AccountProvisioningQueryFilterPojo();
		filter.setAllObjects(false);
		filter.setDefaultMaxObjects(false);
		filter.setDeprovisioningId(deprovisioningId);
		
		refreshList(userLoggedIn);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setAccountProvisioningSummaryList(List<AccountProvisioningSummaryPojo> summaries) {
		getView().setAccountProvisioningSummaries(summaries);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new AccountProvisioningListUpdateEvent(summaries), this);
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
	public void selectAccountProvisioning(AccountProvisioningPojo selected) {
		
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public AccountProvisioningQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(AccountProvisioningQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void vpcpConfirmOkay() {
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Account was NOT deprovisioned");
	}

	@Override
	public void getAccountDeprovisioningConfirmation(List<AccountDeprovisioningRequisitionPojo> requisitions) {
	}

	@Override
	public void getAccountList() {
		AsyncCallback<AccountQueryResultPojo> acct_cb = new AsyncCallback<AccountQueryResultPojo>() {
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
			public void onSuccess(AccountQueryResultPojo accountItems) {
				GWT.log("got " + accountItems.getResults().size() + " accounts.");
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showAccountSelectionList(accountItems.getResults());
			}
		};
		GWT.log("getting accounts");
		getView().showPleaseWaitDialog("Retrieving accounts...");
		VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, acct_cb);
	}

}
