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
				log.log(Level.SEVERE, "Exception Retrieving VPNs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of VPNs.  " +
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
		getView().showPleaseWaitDialog("Retrieving the default maximum list of VPNP objects from the Network OPs service...");

		filter = new AccountProvisioningQueryFilterPojo();
		filter.setAllObjects(false);
		filter.setDefaultMaxObjects(true);
		
		refreshList(user);
	}

	@Override
	public void refreshListWithAllAccountProvisionings(UserAccountPojo user) {
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving ALL VPCP objects from the AWS Account service (this could take a while)...");

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
//		AsyncCallback<AccountDeprovisioningPojo> callback = new AsyncCallback<AccountDeprovisioningPojo>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				getView().hidePleaseWaitDialog();
//				GWT.log("Exception generating the AccountDeprovisioning", caught);
//				getView().showMessageToUser("There was an exception on the " +
//						"server generating the AccountDeprovisioning.  Message " +
//						"from server is: " + caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(AccountDeprovisioningPojo result) {
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
//				final AccountDeprovisioningPojo vpncdp = result;
//				GWT.log("VPNCDP was generated on the server, showing status page.  "
//						+ "VPNCDP is: " + vpncdp);
//				AccountProvisioningSummaryPojo vpncpSummary = new AccountProvisioningSummaryPojo();
//				vpncpSummary.setDeprovisioning(vpncdp);
//				ActionEvent.fire(eventBus, ActionNames.VPNCDP_GENERATED, vpncpSummary);
//			}
//		};
//		getView().showPleaseWaitDialog("Generating VPC Deprovisioning object...");
//		VpcProvisioningService.Util.getInstance().generateAccountDeprovisioning(selectedProvisioning.getRequisition(), callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN was NOT deprovisioned");
	}

	@Override
	public void getAccountDeprovisioningConfirmation(List<AccountDeprovisioningRequisitionPojo> requisitions) {
		// TODO: Show the terminate account dialog where they have to answser questions
		// and type in the account name or id (see MaintainIncident which is where this is done currently)
		// If they answer the questions and click on the button on that account, the accounts will be
		// deprovisioned there.  This will all be done in the DeprovisionAccountConfirmation place/presenter/view
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

//	@Override
//	public void saveProvisioning(AccountProvisioningPojo pojo) {
//		// update the provisioning object 
//        getView().hidePleaseWaitDialog();
//		getView().showPleaseWaitDialog("Saving VPN Connection Provisioning object...");
//		AsyncCallback<AccountProvisioningPojo> cb = new AsyncCallback<AccountProvisioningPojo>() {
//			@Override
//			public void onFailure(Throwable caught) {
//                getView().hidePleaseWaitPanel();
//                getView().hidePleaseWaitDialog();
//                getView().disableButtons();
//				getView().showMessageToUser("There was an exception on the " +
//						"server updating the VPN Connection Provisioning object.  " +
//						"<p>Message from server is: " + caught.getMessage() + "</p>");
//			}
//
//			@Override
//			public void onSuccess(AccountProvisioningPojo result) {
//		        getView().hidePleaseWaitDialog();
//				// Request the Vpc list now.
//				if (getView().viewAllAccountProvisionings()) {
//					// show all of them
//					refreshListWithAllAccountProvisionings(userLoggedIn);
//				}
//				else {
//					// only show the default maximum
//					refreshListWithMaximumAccountProvisionings(userLoggedIn);
//				}
//			}
//		};
//		VpcProvisioningService.Util.getInstance().updateAccountProvisioning(pojo, cb);
//	}
//
//	@Override
//	public void saveDeprovisioning(AccountDeprovisioningPojo pojo) {
//		// update the deprovisioning object
//        getView().hidePleaseWaitDialog();
//		getView().showPleaseWaitDialog("Saving VPN Connection Deprovisioning object...");
//		AsyncCallback<AccountDeprovisioningPojo> cb = new AsyncCallback<AccountDeprovisioningPojo>() {
//			@Override
//			public void onFailure(Throwable caught) {
//                getView().hidePleaseWaitPanel();
//                getView().hidePleaseWaitDialog();
//                getView().disableButtons();
//				getView().showMessageToUser("There was an exception on the " +
//						"server updating the VPN Connection Deprovisioning object.  " +
//						"<p>Message from server is: " + caught.getMessage() + "</p>");
//			}
//
//			@Override
//			public void onSuccess(AccountDeprovisioningPojo result) {
//		        getView().hidePleaseWaitDialog();
//				// Request the Vpc list now.
//				if (getView().viewAllAccountProvisionings()) {
//					// show all of them
//					refreshListWithAllAccountProvisionings(userLoggedIn);
//				}
//				else {
//					// only show the default maximum
//					refreshListWithMaximumAccountProvisionings(userLoggedIn);
//				}
//			}
//		};
//		VpcProvisioningService.Util.getInstance().updateAccountDeprovisioning(pojo, cb);
//	}

}
