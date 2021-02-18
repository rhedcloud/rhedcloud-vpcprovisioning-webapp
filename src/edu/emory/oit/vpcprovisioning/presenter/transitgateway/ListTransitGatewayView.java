package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListTransitGatewayView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
//		void setSelectedSummaries(List<TransitGatewaySummaryPojo> summaries);
		void selectTransitGateway(TransitGatewayPojo selected);
		public EventBus getEventBus();
		public TransitGatewayQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
//		void deleteTransitGateway(TransitGatewaySummaryPojo summaryPojo);
		void deleteTransitGateways(List<TransitGatewayPojo> pojos);
		public void logMessageOnServer(final String message);
//		public VpcPojo getVpc();
		void refreshList(final UserAccountPojo user);
		void filterByEnvironment(String environment);
		void filterByRegion(String region);
		void filterByTransitGatewayId(String id);
		void filterByAssociationRouteTableId(String id);
		void clearFilter();
//		void deprovisionVpnConnection(VpnConnectionRequisitionPojo vpnConnectionRequisition);
//		void deprovisionVpnConnectionForVpcId(String vpcId);
//		void deprovisionVpnConnectionForAssignment(TransitGatewayAssignmentPojo assignment);
//		void setSelectedAssignment(TransitGatewayAssignmentPojo assignment);
//		void deleteTransitGatewayAssignment(int rowNumber, TransitGatewaySummaryPojo summary);
//		void getTransitGatewayStatusForVpc(String vpcId);
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
	void setTransitGateways(List<TransitGatewayPojo> pojos);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeTransitGatewayFromView(TransitGatewayPojo tgw);
	void showFilteredStatus();
	void hideFilteredStatus();
	void initPage();
	void refreshTableRow(int rowNumber, TransitGatewayPojo tgw);
	void setSummaryHTML(String summaryHTML);
}
