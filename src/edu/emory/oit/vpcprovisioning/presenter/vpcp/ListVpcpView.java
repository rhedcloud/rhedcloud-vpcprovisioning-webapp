package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListVpcpView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		void selectVpcp(VpcpPojo selected);
		public EventBus getEventBus();
		public VpcpQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		void deleteVpcp(VpcpPojo vpcp);
		public void logMessageOnServer(final String message);
		void refreshList(final UserAccountPojo user);
		
		void refreshListWithMaximumVpcps(UserAccountPojo user);
		void refreshListWithAllVpcps(UserAccountPojo user);
		void filterByProvisioningId(boolean includeAllVpcps, String provisioningId);
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
	void setVpcpSummaries(List<VpcpSummaryPojo> vpcpSummaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeVpcpSummaryFromView(VpcpSummaryPojo summary);
	
	boolean viewAllVpcps();
	void initPage();
	void showFilteredStatus();
	void hideFilteredStatus();
}
