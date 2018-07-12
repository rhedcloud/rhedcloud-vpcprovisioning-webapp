package edu.emory.oit.vpcprovisioning.presenter.home;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountRolePojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class HomePresenter extends PresenterBase implements HomeView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private UserAccountPojo userLoggedIn;
	private boolean finishedDirectoryPersonCache=false;

	public HomePresenter(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		clientFactory.getHomeView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		finishedDirectoryPersonCache = false;
		
		setReleaseInfo(clientFactory);
		
		getView().showPleaseWaitDialog("Retrieving account metadata from the AWS Account Service...");
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your user information.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				
				// cache accounts
//				AsyncCallback<AccountQueryResultPojo> acct_callback = new AsyncCallback<AccountQueryResultPojo>() {
//					@Override
//					public void onFailure(Throwable caught) {
//						GWT.log("Exception Retrieving Accounts", caught);
//					}
//
//					@Override
//					public void onSuccess(AccountQueryResultPojo result) {
//					}
//				};
//				GWT.log("caching Account list...");
//				AccountQueryFilterPojo filter = new AccountQueryFilterPojo();
//				filter.setUserLoggedIn(user);
//				VpcProvisioningService.Util.getInstance().getAccountsForFilter(filter, acct_callback);
				
				// just to prime the pump for directory person results so the first page load
				// on the account details page won't take so long.  this could be done in a number
				// of places but since we have the account ids, we'll do it here (asynchronously)
				AsyncCallback<List<RoleAssignmentSummaryPojo>> callback = new AsyncCallback<List<RoleAssignmentSummaryPojo>>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Exception retrieving RoleAssignments", caught);
					}

					@Override
					public void onSuccess(List<RoleAssignmentSummaryPojo> result) {
						if (finishedDirectoryPersonCache) {
							getView().hideBackgroundWorkNotice();
						}
					}
				};
				GWT.log("caching DirectoryPerson list for all user's role assignments...");
				for (int i=0; i<user.getAccountRoles().size(); i++) {
					AccountRolePojo arp = user.getAccountRoles().get(i);
					if (i == user.getAccountRoles().size() - 1) {
						GWT.log("last RoleAssignment query...i=" + i);
						finishedDirectoryPersonCache = true;
					}
					if (arp.getAccountId() == null) {
						continue;
					}
					VpcProvisioningService.Util.getInstance().getRoleAssignmentsForAccount(arp.getAccountId(), callback);
				}
//				for (AccountRolePojo arp : user.getAccountRoles()) {
//					if (arp.getAccountId() == null) {
//						continue;
//					}
//					VpcProvisioningService.Util.getInstance().getRoleAssignmentsForAccount(arp.getAccountId(), callback);
//				}
				
//				AsyncCallback<Void> log_cb = new AsyncCallback<Void>() {
//					@Override
//					public void onFailure(Throwable caught) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void onSuccess(Void result) {
//						// TODO Auto-generated method stub
//					}
//				};
//				VpcProvisioningService.Util.getInstance().logMessage("starting DirectoryPerson cache", log_cb);

				getView().setUserLoggedIn(user);
				getView().initPage();
				String linkText = userLoggedIn.getPersonalName().toString() + " (" + userLoggedIn.getPublicId() + ")";
				clientFactory.getShell().setUserName(linkText);
				
				AsyncCallback<String> asCallback = new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error getting account series info", caught);
						getView().setAccountSeriesInfo("Error");
					}

					@Override
					public void onSuccess(String result) {
						getView().setAccountSeriesInfo(result);
					}
				};
				VpcProvisioningService.Util.getInstance().getAccountSeriesText(asCallback);

				getView().setAccountRoleList(user.getAccountRoles());
				// account affiliation count needs to be calculated because the accountRoles in the user 
				// could have multilple roles for the same account.  so, i need to go through and get a 
				// distinct account count.
				HashMap<String, Integer> accountMap = new HashMap<String, Integer>();
				int totalAccountCount = 0;
				for (AccountRolePojo arp : user.getAccountRoles()) {
					if (arp.getAccountId() != null) {
						Integer acctCount = accountMap.get(arp.getAccountId());
						if (acctCount != null) {
							acctCount++;
						}
						else {
							acctCount = 1;
							accountMap.put(arp.getAccountId(), acctCount);
						}
					}
				}
				Iterator<String> acctIter = accountMap.keySet().iterator();
				while (acctIter.hasNext()) {
					totalAccountCount += accountMap.get(acctIter.next());
				}
				StringBuffer roleInfoHTML = new StringBuffer("You are affiliated to " + totalAccountCount + " distinct account(s).<br/>");
				roleInfoHTML.append("You have " + user.getAccountRoles().size() + " total roles in these account(s).<br/>");
				int centralAdminCnt = 0;
				int adminCnt = 0;
				int auditorCnt = 0;
				for (AccountRolePojo arp : user.getAccountRoles()) {
					if (user.isAdminForAccount(arp.getAccountId())) {
						adminCnt++;
					}
					else if (user.isAuditorForAccount(arp.getAccountId())) {
						auditorCnt++;
					}
					else if (user.isCentralAdminForAccount(arp.getAccountId())) {
						centralAdminCnt++;
					}
				}
				roleInfoHTML.append("You " + (user.isCentralAdmin() ? "are" : "are not") + " a central administrator, "
						+ "you are the administrator of " + adminCnt + " account(s), "
						+ "and the auditor of " + auditorCnt + " account(s).");
				getView().setRoleInfoHTML(roleInfoHTML.toString());
				
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().setFieldViolations(false);
				getView().setInitialFocus();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	@Override
	public void stop() {
		eventBus = null;
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private HomeView getView() {
		return clientFactory.getHomeView();
	}

	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}

	@Override
	public ClientFactory getClientFactory() {
		return this.clientFactory;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	@Override
	public void viewAccountForId(String accountId) {
		AsyncCallback<AccountQueryResultPojo> callback = new AsyncCallback<AccountQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving account information.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(AccountQueryResultPojo result) {
				getView().hidePleaseWaitPanel();
				if (result != null) {
					if (result.getResults().size() == 1) {
						ActionEvent.fire(getEventBus(), ActionNames.MAINTAIN_ACCOUNT_FROM_HOME, result.getResults().get(0));
					}
					else {
						// error, incorrect number of accounts returned
					}
				}
				else {
					// error
				}
			}
		};
		getView().showPleaseWaitDialog("Retrieving account metadata from the AWS Account Service...");
		AccountQueryFilterPojo filter = new AccountQueryFilterPojo();
		filter.setAccountId(accountId);
		VpcProvisioningService.Util.getInstance().getAccountsForFilter(filter, callback);
	}

	@Override
	public String getDetailedRoleInfoHTML() {
		StringBuffer sbuf = new StringBuffer();
		boolean isFirst=true;
		for (AccountRolePojo arp : userLoggedIn.getAccountRoles()) {
			if (isFirst) {
				isFirst = false;
			}
			else {
				sbuf.append("<br/>");
			}
			sbuf.append("Account: " + (arp.getAccountId() != null ? (arp.getAccountId() + " (" + arp.getAccountName() + ")") : "N/A") + ", Role: " + arp.getRoleName());
		}
		return sbuf.toString();
	}

	@Override
	public String getDetailedDirectoryInfoHTML() {
		AsyncCallback<DirectoryPersonQueryResultPojo> srvrCallback = new AsyncCallback<DirectoryPersonQueryResultPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				GWT.log("Exception retrieving DirectoryPerson information.", caught);
				getView().showDirectoryPersonInfoPopup("Exception retrieving DirectoryPerson info.  "
						+ "Exception: " + caught.getMessage());
			}

			@Override
			public void onSuccess(DirectoryPersonQueryResultPojo result) {
				getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				if (result == null) {
					getView().showDirectoryPersonInfoPopup("NULL DirectoryPerson "
							+ "object returned from server");
				}
				// There may be more than one returned but we'll just take the first one.
				// this is because now we're pulling back EHC people too which may lead 
				// to duplicate records returned for the same public id.  This is okay but 
				// we only need one of them.
//				else if (result.getResults().size() != 1) {
//					getView().showDirectoryPersonInfoPopup("Invalid number of DirectoryPerson objects "
//							+ "returned from server.  Got " + result.getResults().size() + " expected 1");
//				}
				else {
					DirectoryPersonPojo dp = result.getResults().get(0);
					getView().showDirectoryPersonInfoPopup(dp.toString());
				}
			}
			
		};
		DirectoryPersonQueryFilterPojo filter = new DirectoryPersonQueryFilterPojo();
		filter.setKey(userLoggedIn.getPublicId());
		getView().showPleaseWaitDialog("Retrieving Directory meta data from the Directory Service...");
		VpcProvisioningService.Util.getInstance().getDirectoryPersonsForFilter(filter, srvrCallback);
		return null;
	}

	@Override
	public String getDetailedPersonInfoHTML() {
		AsyncCallback<FullPersonQueryResultPojo> srvrCallback = new AsyncCallback<FullPersonQueryResultPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				GWT.log("Exception retrieving FullPerson information.", caught);
				getView().showFullPersonInfoPopup("Exception retrieving FullPerson info.  "
						+ "Exception: " + caught.getMessage());
			}

			@Override
			public void onSuccess(FullPersonQueryResultPojo result) {
				getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				if (result == null) {
					getView().showDirectoryPersonInfoPopup("NULL FirstPerson "
							+ "object returned from server");
				}
				else if (result.getResults().size() != 1) {
					getView().showDirectoryPersonInfoPopup("Invalid number of FirstPerson objects "
							+ "returned from server.  Got " + result.getResults().size() + " expected 1");
				}
				else {
					FullPersonPojo fp = result.getResults().get(0);
					getView().showFullPersonInfoPopup(fp.toString());
				}
			}
			
		};
		FullPersonQueryFilterPojo filter = new FullPersonQueryFilterPojo();
		GWT.log("Getting FullPerson for public id: " + userLoggedIn.getPublicId());
		filter.setPublicId(userLoggedIn.getPublicId());
		getView().showPleaseWaitDialog("Getting person meta data from the Identity Service...");
		VpcProvisioningService.Util.getInstance().getFullPersonsForFilter(filter, srvrCallback);
		return null;
	}

}
