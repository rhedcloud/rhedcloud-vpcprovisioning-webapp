package edu.emory.oit.vpcprovisioning.presenter.centraladmin;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListCentralAdminView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectCentralAdmin(RoleAssignmentSummaryPojo selected);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteCentralAdmin(RoleAssignmentSummaryPojo account);
		public void logMessageOnServer(final String message);
		
//		void filterByAccountId(String accountId);
//		void clearFilter();
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
	void setCentralAdmins(List<RoleAssignmentSummaryPojo> centralAdmins);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeCentralAdminFromView(RoleAssignmentSummaryPojo centralAdmin);
	void initPage();
	void setMyNetIdURL(String url);
}
