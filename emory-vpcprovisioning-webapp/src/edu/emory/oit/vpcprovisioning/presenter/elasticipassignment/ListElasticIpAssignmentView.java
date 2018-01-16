package edu.emory.oit.vpcprovisioning.presenter.elasticipassignment;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListElasticIpAssignmentView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectElasticIpAssignmentSummary(ElasticIpAssignmentSummaryPojo selected);
		public EventBus getEventBus();
		public ElasticIpAssignmentSummaryQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteElasticIpAssignment(ElasticIpAssignmentSummaryPojo selected);
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
	 * @param caseRecords the list of caseRecords
	 */
	void setElasticIpAssignmentSummaries(List<ElasticIpAssignmentSummaryPojo> summaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel();
	void removeElasticIpAssignmentSummaryFromView(ElasticIpAssignmentSummaryPojo summary);


}
