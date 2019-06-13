package edu.emory.oit.vpcprovisioning.presenter.cidr;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

/**
 * Used to create, update and delete a Cidr
 * @author jtjacks
 *
 */
public interface MaintainCidrView extends Editor<CidrPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current cidr or cancel the creation of a cidr.
		 */
		void deleteCidr();

		/**
		 * Create a new cidr or save the current cidr based on the values in the
		 * inputs.
		 */
		void saveCidr();
		CidrPojo getCidr();
		public boolean isValidNetwork(String value);
		public boolean isValidBits(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
	}

	/**
	 * Get the driver used to edit tasks in the view.
	 */
	//	  RequestFactoryEditorDriver<TaskProxy, ?> getEditorDriver();

	/**
	 * Specify whether the view is editing an existing cidr or creating a new
	 * cidr.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the cidr is loaded.
	 * 
	 * @param locked true to lock, false to unlock
	 */
	void setLocked(boolean locked);

	/**
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setNetworkViolation(String message);
	void setBitsViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void setAssociatedCidrTypeItems(List<String> associatedCidrTypes);
}
