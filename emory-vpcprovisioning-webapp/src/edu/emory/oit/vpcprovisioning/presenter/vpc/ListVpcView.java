package edu.emory.oit.vpcprovisioning.presenter.vpc;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListVpcView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectVpc(VpcPojo selected);
		public EventBus getEventBus();
		public VpcQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		void deleteVpc(VpcPojo Vpc);
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
	void setVpcs(List<VpcPojo> Vpcs);
	
	void setReleaseInfo(String releaseInfoHTML);
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel();
	void removeVpcFromView(VpcPojo Vpc);
}
