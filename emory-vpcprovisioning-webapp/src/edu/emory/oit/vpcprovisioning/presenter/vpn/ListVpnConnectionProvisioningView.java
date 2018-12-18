package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListVpnConnectionProvisioningView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectVpnConnectionProvisioning(VpnConnectionProvisioningPojo selected);
		public EventBus getEventBus();
		public VpnConnectionProvisioningQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		public void logMessageOnServer(final String message);
		void refreshList(final UserAccountPojo user);
		
		void refreshListWithMaximumVpnConnectionProvisionings(UserAccountPojo user);
		void refreshListWithAllVpnConnectionProvisionings(UserAccountPojo user);
		void filterByProvisioningId(boolean includeAllVpnConnectionProvisionings, String provisioningId);
		void deprovisionVpnConnection(VpnConnectionProvisioningPojo provisionedVpnConnection);
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
	void setVpnConnectionProvisioningSummaries(List<VpnConnectionProvisioningSummaryPojo> summaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeVpnConnectionProvisioningFromView(VpnConnectionProvisioningPojo vpcp);
	
	boolean viewAllVpnConnectionProvisionings();
	void initPage();
	void showFilteredStatus();
	void hideFilteredStatus();
}
