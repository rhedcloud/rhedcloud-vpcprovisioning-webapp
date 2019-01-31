package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface VpncpStatusView extends Editor<VpnConnectionProvisioningPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current Vpncp or cancel the creation of a Vpncp.
		 */
		void deleteVpncp();

		/**
		 * generate a new Vpncp or save the current Vpncp based on the values in the
		 * inputs.
		 */
		VpnConnectionProvisioningPojo getVpncp();
		VpnConnectionDeprovisioningPojo getVpncdp();
		VpnConnectionProvisioningSummaryPojo getVpncpSummary();
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void refreshProvisioningStatusForId(String provisioningId);
		public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w);
		public void logMessageOnServer(final String message);
		public boolean isFromGenerate();
		public void setFromGenerate(boolean fromGenerate);
	}

	/**
	 * Get the driver used to edit tasks in the view.
	 */
	//	  RequestFactoryEditorDriver<TaskProxy, ?> getEditorDriver();

	/**
	 * Specify whether the view is editing an existing Vpncp or creating a new
	 * Vpncp.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the Vpncp is loaded.
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
	void clearProvisioningStatus();
}
