package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.BillPojo;
import edu.emory.oit.vpcprovisioning.shared.BillSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.LineItemPojo;
import edu.emory.oit.vpcprovisioning.shared.LineItemSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopBillSummary extends ViewImplBase implements BillSummaryView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<BillPojo> billsForAccount = new java.util.ArrayList<BillPojo>();
	List<AccountPojo> accounts = new java.util.ArrayList<AccountPojo>();

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField ListBox accountLB;
	@UiField ListBox billLB;	
	@UiField VerticalPanel billSummaryPanel;
	
	@UiHandler ("accountLB")
	void accountSelected(ChangeEvent e) {
		// refresh the bill drop down with bills for the selected account
		String accountId = accountLB.getSelectedValue();
		AccountPojo selected = null;
		if (accountId != null) {
			acctLoop: for (AccountPojo acct : accounts) {
				if (acct.getAccountId().equals(accountId)) {
					selected = acct;
					break acctLoop;
				}
			}
		}
		if (selected != null) {
			presenter.selectAccount(selected);
		}
	}
	
	@UiHandler ("billLB")
	void billSelected(ChangeEvent e) {
		showPleaseWaitDialog();
		billSummaryPanel.setVisible(false);
		String billId = billLB.getSelectedValue();
		BillPojo selectedBill = null;
		if (billId != null) {
			GWT.log("Selected bill id is: " + billId);
			billLoop: for (BillPojo bill : billsForAccount) {
				if (bill.getBillId().equals(billId)) {
					GWT.log("found a matching bill with " + bill.getLineItems().size() + " LineItems in it.");
					selectedBill = bill;
					break billLoop;
				}
			}
		}
		if (selectedBill == null) {
			GWT.log("could not find a matching bill for bill id: " + billId);
			// show message and return;
			return;
		}
		
		
		Tree billSummaryTree = createBillSummaryTree(selectedBill);
		billSummaryPanel.clear();
		billSummaryPanel.add(billSummaryTree);
		billSummaryPanel.setVisible(true);
    	hidePleaseWaitDialog();

    	// TEMPORARY just to make sure things are displaying correctly
//		Timer timer = new Timer()  {
//            @Override
//            public void run()	{
//				Tree billSummaryTree = createBillSummaryTree();
//				billSummaryPanel.clear();
//				billSummaryPanel.add(billSummaryTree);
//				billSummaryPanel.setVisible(true);
//            	hidePleaseWaitDialog();
//            }
//        };
//
//        timer.schedule(5000);
	}

	private Tree createBillSummaryTree(BillPojo selectedBill) {
		BillSummaryPojo billSummary = presenter.generateBillSummaryForBill(selectedBill);

		Tree billSummaryTree = new Tree();
		Iterator<String> lineItemKeys = billSummary.getLineItemSummaryMap().keySet().iterator();
		while (lineItemKeys.hasNext()) {
			String productCode = lineItemKeys.next();
			LineItemSummaryPojo lis = billSummary.getLineItemSummaryMap().get(productCode);
			NumberFormat fmt = NumberFormat.getCurrencyFormat();
			String formatted = fmt.format(lis.getLineItemTotal());
			TreeItem liTreeItem = billSummaryTree.addTextItem(lis.getProductName() + " - " + formatted);
			// TODO add more details to the liTreeItem...
			for (LineItemPojo lip : lis.getLineItems()) {
				addLineItemSection(liTreeItem, lip);
			}
		}
		return billSummaryTree;
	}
	
	private void addLineItemSection(TreeItem parent, LineItemPojo lineItem) {
		NumberFormat fmt = NumberFormat.getCurrencyFormat();
		String formatted = fmt.format(lineItem.getTotalCost());
		String desc = lineItem.getItemDescription() + " - " + formatted;
		parent.addTextItem(desc);
	}
	
	private static DesktopBillSummaryUiBinder uiBinder = GWT.create(DesktopBillSummaryUiBinder.class);

	interface DesktopBillSummaryUiBinder extends UiBinder<Widget, DesktopBillSummary> {
	}

	public DesktopBillSummary() {
		initWidget(uiBinder.createAndBindUi(this));
		billSummaryPanel.setVisible(false);
		GWT.log("DesktopBillSummary...");
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		accountLB.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		accountLB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setBillItems(List<BillPojo> bills) {
		GWT.log("adding " + bills.size() + " bills to the drop down");
		billSummaryPanel.setVisible(false);
		this.billsForAccount = bills;
		billLB.clear();
		billLB.addItem("-- Select Bill --");
		if (billsForAccount != null) {
			for (int i=0; i<billsForAccount.size(); i++) {
				BillPojo bill = billsForAccount.get(i);
				GWT.log("Adding bill (" + i +"): " + bill.getBillId());
				billLB.addItem(bill.getBillingPeriodStartDate() + " - " + 
						bill.getBillingPeriodEndDate(), bill.getBillId());
			}
		}
	}

	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		this.accounts = accounts;
		accountLB.clear();
		if (accounts != null) {
			for (int i=0; i<accounts.size(); i++) {
				AccountPojo account = accounts.get(i);
				accountLB.addItem(account.getAccountId() + "/" + 
						account.getAccountName(), account.getAccountId());

				if (presenter.getAccount() != null) {
					if (account.getAccountId().equalsIgnoreCase(presenter.getAccount().getAccountId())) {
						accountLB.setSelectedIndex(i);
					}
				}
			}
		}
	}
}
