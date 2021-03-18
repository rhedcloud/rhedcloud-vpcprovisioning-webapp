package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListTransitGatewayConnectionProfileView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void setSelectedSummaries(List<TransitGatewayConnectionProfileSummaryPojo> summaries);
		void selectTransitGatewayConnectionProfile(TransitGatewayConnectionProfilePojo selected);
		public EventBus getEventBus();
		public TransitGatewayConnectionProfileQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		void deleteTransitGatewayConnectionProfile(TransitGatewayConnectionProfileSummaryPojo summaryPojo);
		void deleteTransitGatewayConnectionProfiles(List<TransitGatewayConnectionProfileSummaryPojo> summaries);
		public void logMessageOnServer(final String message);
		public VpcPojo getVpc();
		void refreshList(final UserAccountPojo user);
		
		void filterByVpcAddress(String vpcAddress);
		void filterByCidrId(String cidrId);
		void filterByRegion(String region);
		void filterByTransitGatewayId(String tgwId);
		void filterByCidrRange(String cidrRange);
		
		void clearFilter();
		
		void deprovisionTransitGatewayConnectionForAssignment(TransitGatewayConnectionProfileAssignmentPojo assignment);
		void setSelectedAssignment(TransitGatewayConnectionProfileAssignmentPojo assignment);
		void deleteTransitGatewayConnectionProfileAssignment(int rowNumber, TransitGatewayConnectionProfileSummaryPojo summary);
		void getTransitGatewayStatusForVpc(String vpcId);
		
		// seeing if we can maintain profiles right from this page
		void addEmptySummaryToList();
		void saveProfile(TransitGatewayConnectionProfilePojo profile);
		void setEditing(boolean editing);
		boolean isEditing();
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
	void setTransitGatewayConnectionProfileSummaries(List<TransitGatewayConnectionProfileSummaryPojo> summaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeSummaryForTransitGatewayConnectionProfileFromView(TransitGatewayConnectionProfilePojo profile);
	void showFilteredStatus();
	void hideFilteredStatus();
	void initPage();
	void refreshTableRow(int rowNumber, TransitGatewayConnectionProfileSummaryPojo summary);
	void setProfileSummaryHTML(String summaryHTML);
	void showMessageToUser(String title, String message, Focusable postFocus);
}
