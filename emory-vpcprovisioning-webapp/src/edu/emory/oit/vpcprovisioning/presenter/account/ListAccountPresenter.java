package edu.emory.oit.vpcprovisioning.presenter.account;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.AccountListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListAccountPresenter extends PresenterBase implements ListAccountView.Presenter {
	private static final Logger log = Logger.getLogger(ListAccountPresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the account list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the account list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	AccountQueryFilterPojo filter;
	AccountPojo account;

	/**
	 * The refresh timer used to periodically refresh the account list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListAccountPresenter(ClientFactory clientFactory, boolean clearList, AccountQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListAccountView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListAccountPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListAccountPresenter(ClientFactory clientFactory, ListAccountPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListAccountView getView() {
		return clientFactory.getListAccountView();
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving accounts from the AWS Account Service...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				log.log(Level.SEVERE, "Exception Retrieving Accounts", caught);
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Accounts you're associated to.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Accounts");
				clientFactory.getShell().setUserName(userLoggedIn.getEppn());

				// Clear the account list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				getView().initPage();
				setAccountList(Collections.<AccountPojo> emptyList());

				// Request the account list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the Account list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all accounts for the current filter being used
		AsyncCallback<AccountQueryResultPojo> callback = new AsyncCallback<AccountQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Accounts", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of accounts.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(AccountQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " accounts for " + result.getFilterUsed());
				setAccountList(result.getResults());
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					boolean isAdmin=false;
					boolean isAuditor=false;
					acctLoop: for (AccountPojo acct : result.getResults()) {
						// if they're an admin for any of the accounts, they are an admin for this page
						if (user.isAdminForAccount(acct.getAccountId())) {
							isAdmin = true;
							break acctLoop;
						}
						if (user.isAuditorForAccount(acct.getAccountId())) {
							isAuditor = true;
						}
					}
					if (isAdmin) {
						getView().applyAWSAccountAdminMask();
					}
					else if (isAuditor) {
						getView().applyAWSAccountAuditorMask();
					}
					else {
						getView().showMessageToUser("An error has occurred.  The user logged in does not "
								+ "appear to be associated to any valid roles for this account.");
						getView().applyAWSAccountAuditorMask();
						// TODO: need to not show them the list of accounts???
					}
				}
				
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing Account list...");
		if (filter == null) {
			filter = new AccountQueryFilterPojo();
		}
		filter.setUserLoggedIn(user);
		VpcProvisioningService.Util.getInstance().getAccountsForFilter(filter, callback);
	}

	/**
	 * Set the list of accounts.
	 */
	private void setAccountList(List<AccountPojo> accounts) {
		getView().setAccounts(accounts);
		eventBus.fireEventFromSource(new AccountListUpdateEvent(accounts), this);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
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
	public void selectAccount(AccountPojo selected) {
		this.account = selected;
		// TODO fire view/edit account action maybe
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public AccountQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(AccountQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteAccount(final AccountPojo account) {
		if (Window.confirm("Delete the AWS Account " + account.getAccountId() + "/" + account.getAccountName() + "?")) {
			getView().showPleaseWaitDialog("Deleting account");
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					getView().showMessageToUser("There was an exception on the " +
							"server deleting the Account.  Message " +
							"from server is: " + caught.getMessage());
					getView().hidePleaseWaitDialog();
				}

				@Override
				public void onSuccess(Void result) {
					// remove from dataprovider
					getView().removeAccountFromView(account);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "Account was deleted.");
					
					// TODO fire list accounts event...
				}
			};
			VpcProvisioningService.Util.getInstance().deleteAccount(account, callback);
		}
	}

	@Override
	public void filterByAccountId(String accountId) {
		getView().showPleaseWaitDialog("Filtering accounts");
		filter = new AccountQueryFilterPojo();
		filter.setAccountId(accountId);
		this.getUserAndRefreshList();
	}

	@Override
	public void clearFilter() {
		getView().showPleaseWaitDialog("Clearing filter");
		filter = null;
		this.getUserAndRefreshList();
	}
	private void getUserAndRefreshList() {
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				getView().setUserLoggedIn(result);
				refreshList(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
}
