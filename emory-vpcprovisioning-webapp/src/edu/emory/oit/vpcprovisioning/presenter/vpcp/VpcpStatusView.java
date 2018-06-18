package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface VpcpStatusView extends Editor<VpcpPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current Vpcp or cancel the creation of a Vpcp.
		 */
		void deleteVpcp();

		/**
		 * generate a new Vpcp or save the current Vpcp based on the values in the
		 * inputs.
		 */
		void saveVpcp();
		VpcpPojo getVpcp();
		public boolean isValidVpcpId(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void refreshVpcpStatusForId(String provisioningId);
		public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w);
//		public void setSpeedChartStatusForKeyOnWidget(String key, Widget w);
//		public void setSpeedChartStatusForKey(String key, Label label);
		public void logMessageOnServer(final String message);
	}

	/**
	 * Get the driver used to edit tasks in the view.
	 */
	//	  RequestFactoryEditorDriver<TaskProxy, ?> getEditorDriver();

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
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setVpcpIdViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void startTimer(int delay);
	void stopTimer();
	void setReleaseInfo(String releaseInfoHTML);
	void refreshVpcpStatusInformation();
}
