package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListAccountProvisioningView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectAccountProvisioning(AccountProvisioningPojo selected);
		public EventBus getEventBus();
		public AccountProvisioningQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		public void logMessageOnServer(final String message);
		void refreshList(final UserAccountPojo user);
		
		void refreshListWithMaximumAccountProvisionings(UserAccountPojo user);
		void refreshListWithAllAccountProvisionings(UserAccountPojo user);
		void filterByDeprovisioningId(boolean includeAllAccountProvisionings, String deprovisioningId);
		void getAccountDeprovisioningConfirmation(List<AccountDeprovisioningRequisitionPojo> requisitions);
		void getAccountList();
//		void saveProvisioning(AccountProvisioningPojo pojo);
//		void saveDeprovisioning(AccountDeprovisioningPojo pojo);
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
	void setAccountProvisioningSummaries(List<AccountProvisioningSummaryPojo> summaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeAccountProvisioningFromView(AccountProvisioningSummaryPojo summary);
	
	boolean viewAllAccountProvisionings();
	void initPage();
	void showFilteredStatus();
	void hideFilteredStatus();
	void showAccountSelectionList(List<AccountPojo> accounts);
}
