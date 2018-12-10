package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.PresentsConfirmation;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;

public interface ListVpnConnectionProfileView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void setSelectedSummaries(List<VpnConnectionProfileSummaryPojo> summaries);
		void selectVpnConnectionProfile(VpnConnectionProfilePojo selected);
		public EventBus getEventBus();
		public VpnConnectionProfileQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		void deleteVpnConnectionProfile(VpnConnectionProfileSummaryPojo summaryPojo);
		void deleteVpnConnectionProfiles(List<VpnConnectionProfileSummaryPojo> summaries);
		public void logMessageOnServer(final String message);
		public VpcPojo getVpc();
		void refreshList(final UserAccountPojo user);
		void filterByVpcAddress(String vpcAddress);
		void filterByVpnConnectionProfileId(String profileId);
		void clearFilter();
		void deprovisionVpnConnection(VpnConnectionRequisitionPojo vpnConnectionRequisition);
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
	void setVpnConnectionProfileSummaries(List<VpnConnectionProfileSummaryPojo> elasticIpSummaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeSummaryForVpnConnectionProfileFromView(VpnConnectionProfilePojo vpnConnectionProfile);
	void showFilteredStatus();
	void hideFilteredStatus();
	void initPage();
}
