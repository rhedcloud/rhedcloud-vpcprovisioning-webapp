package edu.emory.oit.vpcprovisioning.presenter.finacct;

import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountSpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListFinancialAccountsView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
//		void selectAccount(AccountSpeedChartPojo selected);
		void updateAccounts(List<AccountSpeedChartPojo> selected);
		void updateAccount(AccountSpeedChartPojo selected);
		public EventBus getEventBus();
		public AccountQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
		void filterByAccountId(String accountId);
		void filterByAccountName(String name);
		void filterByAlternateAccountName(String name);
		void clearFilter();
		void refreshList(final UserAccountPojo user);
		
		public void setSpeedChartStatusForKeyOnWidget(AccountSpeedChartPojo selected, String key, Widget w, HTML statusHTML, boolean confirmSpeedType);
		public void setSpeedChartStatusForKey(AccountSpeedChartPojo selected, String key, HTML statusHTML, boolean confirmSpeedType);
		public boolean didConfirmSpeedType(AccountSpeedChartPojo selected, String key);
//		public void setSelectedAccountSpeedChart(AccountSpeedChartPojo selected);
//		public AccountSpeedChartPojo getSelectedaccountSpeedChart();
	}

	void clearList();

	/**
	 * Sets the new presenter, and calls {@link Presenter#stop()} on the previous
	 * one.
	 */
	void setPresenter(Presenter presenter);

	void setAccounts(List<AccountSpeedChartPojo> accounts);
	void setReleaseInfo(String releaseInfoHTML);
	void removeAccountFromView(AccountSpeedChartPojo account);
	void initPage();
	void setFilterTypeItems(List<String> filterTypes);
	Widget getSpeedTypeWidget();
	void setSpeedTypeConfirmed(boolean confirmed);
	boolean isSpeedTypeConfirmed();
	void hideBadFinancialAccountsHTML();
	void showBadFinancialAccountsHTML();
	void updateAccountStatus(AccountSpeedChartPojo account, boolean success, String errorMessage);
}
