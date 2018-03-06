package edu.emory.oit.vpcprovisioning.presenter.cidr;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListCidrView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectCidr(CidrPojo selected);
		public EventBus getEventBus();
		public CidrQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		void deleteCidrSummary(CidrSummaryPojo cidrSummary);
		public void logMessageOnServer(final String message);
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
	void setCidrSummaries(List<CidrSummaryPojo> cidrSummaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel();
	void removeCidrSummaryFromView(CidrSummaryPojo cidrSummary);
}
