package edu.emory.oit.vpcprovisioning.presenter.bill;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.BillPojo;
import edu.emory.oit.vpcprovisioning.shared.BillQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.BillSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface BillSummaryView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		// get all line items for this bill
		void selectBill(BillPojo selected);
		// get all bills for this account
		// TODO: there will be more here depending on the interfaces needed
		void selectAccount(AccountPojo selected);
		public EventBus getEventBus();
		public BillQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		BillPojo getBill();
		void setBill(BillPojo bill);
		AccountPojo getAccount();
		void setAccount(AccountPojo account);
		
		public BillSummaryPojo generateBillSummaryForBill(BillPojo bill);
		public void logMessageOnServer(final String message);
	}

	/**
	 * Sets the new presenter, and calls {@link Presenter#stop()} on the previous
	 * one.
	 */
	void setPresenter(Presenter presenter);

	void setReleaseInfo(String releaseInfoHTML);
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel();
	// populate drop downs
	void setBillItems(List<BillPojo> bills);
	void setAccountItems(List<AccountPojo> accounts);
}
