package edu.emory.oit.vpcprovisioning.presenter.staticnat;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface StaticNatProvisioningStatusView extends Editor<StaticNatProvisioningSummaryPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current Vpcp or cancel the creation of a Vpcp.
		 */
		void deleteStaticNatProvisioning();
		void deleteStaticNatDeprovisioning();

		/**
		 * generate a new Vpcp or save the current Vpcp based on the values in the
		 * inputs.
		 */
		StaticNatProvisioningSummaryPojo getSummary();
		StaticNatProvisioningPojo getProvisioning();
		StaticNatDeprovisioningPojo getDeprovisioning();
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void refreshProvisioningStatusForId(String provisioningId);
		public void refreshDeprovisioningStatusForId(String provisioningId);
		public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w);
		public void logMessageOnServer(final String message);
	}

	/**
	 * Specify whether the view is editing an existing Vpcp or creating a new
	 * Vpcp.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the Vpcp is loaded.
	 * 
	 * @param locked true to lock, false to unlock
	 */
	void setLocked(boolean locked);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void startTimer(int delay);
	void stopTimer();
	void setReleaseInfo(String releaseInfoHTML);
	void refreshProvisioningStatusInformation();


}
