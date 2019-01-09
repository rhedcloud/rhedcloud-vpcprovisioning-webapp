package edu.emory.oit.vpcprovisioning.presenter.bill;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.BillPojo;
import edu.emory.oit.vpcprovisioning.shared.BillQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.BillSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.LineItemPojo;
import edu.emory.oit.vpcprovisioning.shared.LineItemSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class BillSummaryPresenter extends PresenterBase implements BillSummaryView.Presenter {
	private static final Logger log = Logger.getLogger(BillSummaryPresenter.class.getName());
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private BillPojo bill;
	private AccountPojo account;

	public BillSummaryPresenter(ClientFactory clientFactory, AccountPojo account) {
		this.clientFactory = clientFactory;
		this.account = account;
		clientFactory.getBillSummaryView().setPresenter(this);
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
		GWT.log("BillSummary Presenter...");
		GWT.log("[presenter] Selected account is: " + this.account.getAccountId());
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving Vpcs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				clientFactory.getShell().setSubTitle("Bill Summary");
				getView().setUserLoggedIn(user);

				// get all accounts so they can be added to drop down
				AsyncCallback<AccountQueryResultPojo> accoutCallback = new AsyncCallback<AccountQueryResultPojo>() {
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
					public void onSuccess(AccountQueryResultPojo result) {
						GWT.log("Got " + result.getResults().size() + " accounts for " + result.getFilterUsed());
						getView().setAccountItems(result.getResults());
						selectAccount(account);
						// apply authorization mask
						if (user.isCentralAdmin()) {
							getView().applyCentralAdminMask();
						}
						else if (account != null && user.isAdminForAccount(account.getAccountId())) {
							getView().applyAWSAccountAdminMask();
						}
						else if (account != null && user.isAuditorForAccount(account.getAccountId())) {
							getView().applyAWSAccountAuditorMask();
						}
						else {
							getView().showMessageToUser("An error has occurred.  The user logged in does not "
									+ "appear to be associated to any valid roles for this account.");
							getView().applyAWSAccountAuditorMask();
							// TODO: need to not show them the bill summary???
						}
					}
				};

				GWT.log("[BillSummaryPresenter] refreshing Account list...");
				VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, accoutCallback);
				
				// refresh bill information for account passed in.
//				AsyncCallback<List<BillPojo>> billCallback = new AsyncCallback<List<BillPojo>>() {
//					@Override
//					public void onFailure(Throwable caught) {
//		                getView().hidePleaseWaitPanel();
//		                getView().hidePleaseWaitDialog();
//						log.log(Level.SEVERE, "Exception Retrieving Bill information", caught);
//						getView().showMessageToUser("There was an exception on the " +
//								"server retrieving the list of bills for the selected account.  " +
//								"Message from server is: " + caught.getMessage());
//					}
//
//					@Override
//					public void onSuccess(List<BillPojo> result) {
//						if (result != null) {
//							GWT.log("Got " + result.size() + " bills for account: " + account.getAccountId());
//							getView().setBillItems(result);
//						}
//						else {
//							GWT.log("NULL list of bills returned for account " + account.getAccountId() + " this is an issue.");
//						}
//		                getView().hidePleaseWaitPanel();
//		                getView().hidePleaseWaitDialog();
//					}
//				};
//				GWT.log("refreshing bill list for account: " + account.getAccountId());
//				VpcProvisioningService.Util.getInstance().getCachedBillsForAccount(account.getAccountId(), billCallback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getVpcpStatusView().setLocked(false);
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
	public void selectBill(BillPojo selected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectAccount(AccountPojo selected) {
		// refresh bill information for account passed in.
		getView().showPleaseWaitDialog("Loading Bill information");
		getView().setBillItems(new java.util.ArrayList<BillPojo>());
		setAccount(selected);
		AsyncCallback<List<BillPojo>> billCallback = new AsyncCallback<List<BillPojo>>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Bill information", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the list of bills for the selected account.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(List<BillPojo> result) {
				if (result != null) {
					GWT.log("Got " + result.size() + " bills for account: " + account.getAccountId());
					getView().setBillItems(result);
				}
				else {
					GWT.log("NULL list of bills returned for account " + account.getAccountId() + " this is an issue.");
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};
		GWT.log("refreshing bill list for account: " + account.getAccountId());
		VpcProvisioningService.Util.getInstance().getCachedBillsForAccount(selected.getAccountId(), billCallback);
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public BillQueryFilterPojo getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public BillPojo getBill() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBill(BillPojo bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AccountPojo getAccount() {
		return account;
	}

	@Override
	public void setAccount(AccountPojo account) {
		this.account = account;
	}

	private BillSummaryView getView() {
		return clientFactory.getBillSummaryView();
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public BillSummaryPojo generateBillSummaryForBill(BillPojo bill) {
		BillSummaryPojo billSummary = new BillSummaryPojo();
		billSummary.setBillId(bill.getBillId());
		billSummary.setAccountId(bill.getPayerAccountId());
		
		for (LineItemPojo lineItem : bill.getLineItems()) {
			LineItemSummaryPojo liSummary = billSummary.getLineItemSummaryMap().get(lineItem.getProductCode());
			if (liSummary != null) {
				// it's an existing summary
				liSummary.setLineItemTotal(liSummary.getLineItemTotal() + lineItem.getTotalCost());
				liSummary.getLineItems().add(lineItem);
			}
			else {
				// it's a new summary
				liSummary = new LineItemSummaryPojo();
				liSummary.setProductCode(lineItem.getProductCode());
				liSummary.setProductName(lineItem.getProductName());
				liSummary.setLineItemTotal(lineItem.getTotalCost());
				liSummary.getLineItems().add(lineItem);
				billSummary.getLineItemSummaryMap().put(liSummary.getProductCode(), liSummary);
			}
		}
		
		return billSummary;
	}
}
