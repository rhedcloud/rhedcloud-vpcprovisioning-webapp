package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListElasticIpView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectElasticIp(ElasticIpPojo selected);
		public EventBus getEventBus();
		public ElasticIpQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		void deleteElasticIpSummary(ElasticIpSummaryPojo summary);
		public void logMessageOnServer(final String message);
		public VpcPojo getVpc();
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
	void setElasticIpSummaries(List<ElasticIpSummaryPojo> elasticIpSummaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel();
	void removeElasticIpSummaryFromView(ElasticIpSummaryPojo elasticIpSummary);

}
