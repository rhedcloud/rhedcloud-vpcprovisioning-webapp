package edu.emory.oit.vpcprovisioning.presenter.finacct;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.FinancialAccountListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountSpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListFinancialAccountsPresenter extends PresenterBase implements ListFinancialAccountsView.Presenter {
	private static final Logger log = Logger.getLogger(ListFinancialAccountsPresenter.class.getName());
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
	
	private UserAccountPojo userLoggedIn;
	SpeedChartPojo speedType;
	AccountQueryFilterPojo filter;
//	AccountSpeedChartPojo accountSpeedChart;
//	AccountSpeedChartPojo selectedAccount;
//	AccountSpeedChartPojo validatedAccount;
	private List<AccountSpeedChartPojo> fullAccountList = new java.util.ArrayList<AccountSpeedChartPojo>();
	private boolean showBadFinAcctsHTML;

	/**
	 * The refresh timer used to periodically refresh the account list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListFinancialAccountsPresenter(ClientFactory clientFactory, boolean clearList, boolean showBadFinAcctsHTML, AccountQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.showBadFinAcctsHTML = showBadFinAcctsHTML;
		clientFactory.getListFinancialAccountsView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListFinancialAccountsPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListFinancialAccountsPresenter(ClientFactory clientFactory, ListFinancialAccountsPlace place) {
		this(clientFactory, place.isListStale(), place.isShowBadFinAcctsHTML(), place.getFilter());
	}

	private ListFinancialAccountsView getView() {
		return clientFactory.getListFinancialAccountsView();
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
		getView().showPleaseWaitDialog("Retrieving User Logged In...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				log.log(Level.SEVERE, "Exception Retrieving Accounts", caught);
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Accounts you're associated to.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				if (showBadFinAcctsHTML) {
					getView().showBadFinancialAccountsHTML();
				}
				else {
					getView().hideBadFinancialAccountsHTML();
				}
				getView().enableButtons();
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Accounts");

				// Clear the account list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				getView().initPage();

				List<String> filterTypeItems = new java.util.ArrayList<String>();
				filterTypeItems.add(Constants.FILTER_ACCT_ID);
				filterTypeItems.add(Constants.FILTER_ACCT_NAME);
				filterTypeItems.add(Constants.FILTER_ACCT_ALT_NAME);
				getView().setFilterTypeItems(filterTypeItems);

				// Request the account list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	/**
	 * Refresh the Account list.
	 */
	public void refreshList(final UserAccountPojo user) {
		// use RPC to get all accounts for the current filter being used
		getView().showPleaseWaitDialog("Retrieving bad financial accounts from the AWS Account Service...");
		AsyncCallback<List<AccountSpeedChartPojo>> callback = new AsyncCallback<List<AccountSpeedChartPojo>>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Accounts", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of accounts.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(List<AccountSpeedChartPojo> result) {
				GWT.log("Got " + result.size() + " account speed charts");
				setAccountList(result);
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					boolean isAdmin=false;
					boolean isAuditor=false;
					acctLoop: for (AccountSpeedChartPojo acct : result) {
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
						if (result.size() > 0) {
						}
						// just means no rows were returned.
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
		VpcProvisioningService.Util.getInstance().getFinancialAccountsForUser(user, callback);
	}

	/**
	 * Set the list of accounts.
	 */
	private void setAccountList(List<AccountSpeedChartPojo> accounts) {
		getView().setAccounts(accounts);
		if (filter == null || filter.isFuzzyFilter() == false) {
			fullAccountList = accounts;
		}
		if (eventBus != null) {
			eventBus.fireEventFromSource(new FinancialAccountListUpdateEvent(accounts), this);
		}
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

//	@Override
//	public void selectAccount(AccountSpeedChartPojo selected) {
//		this.selectedAccount = selected;
//		// TODO fire view/edit account action maybe
//	}

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
	public void filterByAccountId(String accountId) {
		getView().showPleaseWaitDialog("Filtering accounts");
		filter = new AccountQueryFilterPojo();
		filter.setAccountId(accountId);
//		this.getUserAndRefreshList();

		// just try filtering from out full list
		List<AccountSpeedChartPojo> filteredList = new java.util.ArrayList<AccountSpeedChartPojo>();
		GWT.log("checking " + fullAccountList.size() + " Accounts for a match of " + filter.getAccountId());
		for (AccountSpeedChartPojo pojo : fullAccountList) {
			if (filter.getAccountId() != null && filter.getAccountId().length() > 0) {
				if (pojo.getAccountId().toLowerCase().indexOf(filter.getAccountId().toLowerCase()) >= 0) {
					GWT.log("found an account with a name that matches " + filter.getAccountId());
					filteredList.add(pojo);
				}
			}
		}
		getUserAndRefreshList(filteredList);
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
				
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				userLoggedIn = result;
				getView().setUserLoggedIn(result);
				refreshList(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	@Override
	public void vpcpConfirmOkay() {
//		getView().showPleaseWaitDialog("Deleting Account Metadata for " + 
//			selectedAccount.getAccountId() + "/" + selectedAccount.getAccountName() + "...");
//		
//		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				getView().showMessageToUser("There was an exception on the " +
//						"server deleting the Account metadata.  Message " +
//						"from server is: " + caught.getMessage());
//				getView().hidePleaseWaitDialog();
//			}
//
//			@Override
//			public void onSuccess(Void result) {
//				// remove from dataprovider
////				getView().removeAccountFromView(selectedAccount);
//				getView().hidePleaseWaitDialog();
//				// status message
//				getView().showStatus(getView().getStatusMessageSource(), "Account metadata was deleted.");
//			}
//		};
////		VpcProvisioningService.Util.getInstance().deleteAccount(selectedAccount, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
//		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Account metadata for " + 
//				selectedAccount.getAccountId() + "/" + selectedAccount.getAccountName() + " was not deleted.");
	}

	@Override
	public void filterByAccountName(String name) {
		getView().showPleaseWaitDialog("Filtering accounts");
		filter = new AccountQueryFilterPojo();
		filter.setFuzzyFilter(true);
		filter.setAccountName(name);
//		this.getUserAndRefreshList();
		
		// just try filtering from out full list
		List<AccountSpeedChartPojo> filteredList = new java.util.ArrayList<AccountSpeedChartPojo>();
		GWT.log("checking " + fullAccountList.size() + " Accounts for a match of " + filter.getAccountName());
		for (AccountSpeedChartPojo pojo : fullAccountList) {
			if (filter.getAccountName() != null && filter.getAccountName().length() > 0) {
				if (pojo.getAccountName().toLowerCase().indexOf(filter.getAccountName().toLowerCase()) >= 0) {
					GWT.log("found an account with a name that matches " + filter.getAccountName());
					filteredList.add(pojo);
				}
			}
		}
		getUserAndRefreshList(filteredList);
	}

	@Override
	public void filterByAlternateAccountName(String name) {
		getView().showPleaseWaitDialog("Filtering accounts");
		filter = new AccountQueryFilterPojo();
		filter.setFuzzyFilter(true);
		filter.setAlternateAccountName(name);
//		this.getUserAndRefreshList();

		// just try filtering from out full list
		List<AccountSpeedChartPojo> filteredList = new java.util.ArrayList<AccountSpeedChartPojo>();
		GWT.log("checking " + fullAccountList.size() + " Accounts for a match of " + filter.getAlternateAccountName());
		for (AccountSpeedChartPojo pojo : fullAccountList) {
			if (filter.getAlternateAccountName() != null && filter.getAlternateAccountName().length() > 0) {
				if (pojo.getAlternateName().toLowerCase().indexOf(filter.getAlternateAccountName().toLowerCase()) >= 0) {
					GWT.log("found an account with a name that matches " + filter.getAlternateAccountName());
					filteredList.add(pojo);
				}
			}
		}
		getUserAndRefreshList(filteredList);
	}

	private void getUserAndRefreshList(final List<AccountSpeedChartPojo> filteredList) {
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				userLoggedIn = result;
				getView().setUserLoggedIn(result);
				setAccountList(filteredList);
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	@Override
	public void updateAccounts(List<AccountSpeedChartPojo> selected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccount(AccountSpeedChartPojo selected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSpeedChartStatusForKeyOnWidget(final AccountSpeedChartPojo selected, final String key, final Widget w, final HTML statusHTML, final boolean confirmSpeedType) {
		GWT.log("[setSpeedChartStatusForKeyOnWidget] validating speed type: " + key);
		AsyncCallback<SpeedChartPojo> callback = new AsyncCallback<SpeedChartPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server exception validating speedtype", caught);
				w.setTitle("Server exception validating speedtype");
				w.getElement().getStyle().setColor(Constants.COLOR_RED);
				statusHTML.setHTML("<b>Server exception validating speedtype</b>");
			}

			@Override
			public void onSuccess(SpeedChartPojo scp) {
				if (scp == null) {
					w.setTitle("Invalid account number (" + key + "), can't validate this number");
					w.getElement().getStyle().setBackgroundColor("#efbebe");
					w.getElement().getStyle().setColor(Constants.COLOR_RED);
					statusHTML.setHTML("<b>Invalid account</b>");
					getView().setFieldViolations(true);
				}
				else {
					GWT.log("[setSpeedChartStatusForKeyOnWidget] got a speed chart.");
					speedType = scp;
					String deptId = scp.getDepartmentId();
					String deptDesc = scp.getDepartmentDescription();
					String desc = scp.getDescription();
				    String euValidityDesc = scp.getEuValidityDescription();
				    String statusDescString = euValidityDesc + "\n" + 
				    		deptId + " | " + deptDesc + "\n" +
				    		desc;
				    String statusDescHTML = "<b>" + euValidityDesc + "<br>" + 
				    		deptId + " | " + deptDesc + "<br>" +
				    		desc + "<b>";
					w.setTitle(statusDescString);
					statusHTML.setHTML(statusDescHTML);
					GWT.log("[setSpeedChartStatusForKeyOnWidget] set speed type status html.");
					
					if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_VALID)) {
						w.getElement().getStyle().setColor(Constants.COLOR_GREEN);
						w.getElement().getStyle().setBackgroundColor(null);
						statusHTML.getElement().getStyle().setColor(Constants.COLOR_GREEN);
					}
					else if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_INVALID)) {
						w.getElement().getStyle().setColor(Constants.COLOR_RED);
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_INVALID_FIELD);
						statusHTML.getElement().getStyle().setColor(Constants.COLOR_RED);
						getView().setFieldViolations(true);
					}
					else {
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_FIELD_WARNING);
						statusHTML.getElement().getStyle().setColor(Constants.COLOR_FIELD_WARNING);
					}
					
					getView().setFieldViolations(false);
					if (confirmSpeedType) {
						if (didConfirmSpeedType(selected, key)) {
							// this is where we will update the account with 
							// the new speed type and update the corresponding row
							GWT.log("[green] update account " + selected.getAccountId() + " with new speed type of: " + key);
							
							getView().showPleaseWaitDialog("Saving account...");
							
							AsyncCallback<AccountPojo> acctCb = new AsyncCallback<AccountPojo>() {
								@Override
								public void onFailure(Throwable caught) {
									getView().hidePleaseWaitDialog();
									getView().hidePleaseWaitPanel();
								}

								@Override
								public void onSuccess(AccountPojo result) {
									AsyncCallback<AccountPojo> callback = new AsyncCallback<AccountPojo>() {
										@Override
										public void onFailure(Throwable caught) {
											getView().hidePleaseWaitDialog();
											getView().hidePleaseWaitPanel();
											GWT.log("Exception saving the Account", caught);
											getView().updateAccountStatus(selected, false, "the error message");
											getView().showMessageToUser("There was an exception on the " +
													"server saving the Account.  Message " +
													"from server is: " + caught.getMessage());
										}

										@Override
										public void onSuccess(AccountPojo result) {
											getView().hidePleaseWaitDialog();
											getView().hidePleaseWaitPanel();
											getView().updateAccountStatus(selected, true, null);
										}
									};
									// it's an update
									result.setSpeedType(key);
									VpcProvisioningService.Util.getInstance().updateAccount(result, callback);
								}
							};
							VpcProvisioningService.Util.getInstance().getAccountById(selected.getAccountId(), acctCb);
						}
					}
				}
			}
		};
		if (key != null && key.length() > 0) {
			SpeedChartQueryFilterPojo filter = new SpeedChartQueryFilterPojo();
			filter.getSpeedChartKeys().add(key);
			VpcProvisioningService.Util.getInstance().getSpeedChartForFinancialAccountNumber(key, callback);
		}
		else {
			GWT.log("null key, can't validate yet");
		}
	}

	@Override
	public void setSpeedChartStatusForKey(final AccountSpeedChartPojo selected, String key, HTML statusHTML, boolean confirmSpeedType) {
		GWT.log("[setSpeedChartStatusForKey] validating speed type: " + key);
		// null check / length
		if (key != null && key.length() > 0 && key.length() != 10) {
			statusHTML.setHTML("<b>Invalid Length</b>");
			statusHTML.getElement().getStyle().setColor(Constants.COLOR_RED);
			getView().setFieldViolations(true);
			return;
		}
		// TODO: numeric characters
		
		setSpeedChartStatusForKeyOnWidget(selected, key, getView().getSpeedTypeWidget(), statusHTML, confirmSpeedType);
	}

	@Override
	public boolean didConfirmSpeedType(final AccountSpeedChartPojo selected, String key) {
		if (selected != null && 
			selected.getSpeedType() != null && 
			selected.getAccountId() != null) {
			
			boolean confirmed = Window.confirm("Are you sure you want to use this SpeedType (" + key + ")?  "
					+ "NOTE:  Using an invalid SpeedType is a violation of " + 
					getView().getAppShell().getSiteName() + "'s Terms of Use.");
			if (confirmed) {
				// log that the user acknowldged the speed type (on the server)
				DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM-dd-yyyy HH:mm:ss:SSS zzz");
				String msg = "User " + this.userLoggedIn.getPublicId() + " acknowledged "
						+ "the SpeedType " + selected.getSpeedType() 
						+ " for account " + selected.getAccountId() 
						+ " (" + selected.getAccountName() + ") "  
						+ "is the correct SpeedType for this account at: " + new Date();
				this.logMessageOnServer(msg);
//				getView().showMessageToUser("Logged " + msg);
				getView().setSpeedTypeConfirmed(true);
				return true;
			}
		}
		// user decided they didn't want to use this speed type, or the account hasn't been 
		// entered yet
		getView().setSpeedTypeConfirmed(false);
		return false;
	}

//	@Override
//	public void setSelectedAccountSpeedChart(AccountSpeedChartPojo selected) {
//		this.selectedAccount = selected;
//		GWT.log("selected AccountSpeedChart is now: " + selected.getAccountId());
//	}
//
//	@Override
//	public AccountSpeedChartPojo getSelectedaccountSpeedChart() {
//		return this.selectedAccount;
//	}
}
