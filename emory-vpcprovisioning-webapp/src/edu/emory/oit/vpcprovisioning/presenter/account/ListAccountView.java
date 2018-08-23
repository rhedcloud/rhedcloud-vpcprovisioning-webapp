package edu.emory.oit.vpcprovisioning.presenter.account;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.presenter.PresentsConfirmation;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public interface ListAccountView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectAccount(AccountPojo selected);
		public EventBus getEventBus();
		public AccountQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteAccount(AccountPojo account);
		public void logMessageOnServer(final String message);
		
		void filterByAccountId(String accountId);
		void clearFilter();
		void refreshList(final UserAccountPojo user);
	}

	/**
	 * Clear the list of case records.
	 */
	void clearList();

	/**
	 * Sets the new presenter, and calls {@link Presenter#stop()} on the previous
	 * one.
	 */
	void setPresenter(Presenter presenter);

	/**
	 * Set the list of caseRecords to display.
	 * 
	 * @param cidrs the list of caseRecords
	 */
	void setAccounts(List<AccountPojo> accounts);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeAccountFromView(AccountPojo account);
	void initPage();
}
