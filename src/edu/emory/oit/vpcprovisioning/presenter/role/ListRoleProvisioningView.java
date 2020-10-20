package edu.emory.oit.vpcprovisioning.presenter.role;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListRoleProvisioningView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectRoleProvisioning(RoleProvisioningPojo selected);
		public EventBus getEventBus();
		public RoleProvisioningQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
		void refreshList(final UserAccountPojo user);
		void refreshListWithMaximumRoleProvisionings(UserAccountPojo user);
		void refreshListWithAllRoleProvisionings(UserAccountPojo user);
		void filterByProvisioningId(boolean includeAllRoleProvisionings, String provisioningId);
		void deprovisionRole(RoleProvisioningRequisitionPojo requisition);
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
	void setRoleProvisioningSummaries(List<RoleProvisioningSummaryPojo> summaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeRoleProvisioningFromView(RoleProvisioningSummaryPojo vpcp);
	
	boolean viewAllRoleProvisionings();
	void initPage();
	void showFilteredStatus();
	void hideFilteredStatus();
}
