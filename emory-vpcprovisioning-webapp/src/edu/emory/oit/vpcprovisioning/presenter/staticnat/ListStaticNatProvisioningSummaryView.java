package edu.emory.oit.vpcprovisioning.presenter.staticnat;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListStaticNatProvisioningSummaryView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectStaticNatProvisioningSummary(StaticNatProvisioningSummaryPojo selected);
		public EventBus getEventBus();
		public StaticNatProvisioningQueryFilterPojo getStaticNatProvisioningFilter();
		public StaticNatDeprovisioningQueryFilterPojo getStaticNatDeprovisioningFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		void deleteStaticNatProvisioningSummary(StaticNatProvisioningSummaryPojo vpcp);
		public void logMessageOnServer(final String message);
		void refreshList(final UserAccountPojo user);
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

	void setProvisioningSummaries(List<StaticNatProvisioningSummaryPojo> summaries);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeStaticNatProvisioningSummaryFromView(StaticNatProvisioningSummaryPojo vpcp);
}
