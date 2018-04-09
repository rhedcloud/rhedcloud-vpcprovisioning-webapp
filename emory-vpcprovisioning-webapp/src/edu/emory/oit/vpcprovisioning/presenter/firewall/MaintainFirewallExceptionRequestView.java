package edu.emory.oit.vpcprovisioning.presenter.firewall;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainFirewallExceptionRequestView extends Editor<FirewallExceptionRequestPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteFirewallExceptionRequest();

		/**
		 * Create a new account or save the current account based on the values in the
		 * inputs.
		 */
		void saveFirewallExceptionRequest();
		FirewallExceptionRequestPojo getFirewallExceptionRequest();
		public boolean isValidFirewallExceptionRequestName(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w);
		public void logMessageOnServer(final String message);
		public void setVpc(VpcPojo vpc);
		public VpcPojo getVpc();
		public MaintainFirewallExceptionRequestView getView();
		public FirewallRulePojo getFirewallRule();
	}

	/**
	 * Get the driver used to edit tasks in the view.
	 */
	//	  RequestFactoryEditorDriver<TaskProxy, ?> getEditorDriver();

	/**
	 * Specify whether the view is editing an existing account or creating a new
	 * account.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the account is loaded.
	 * 
	 * @param locked true to lock, false to unlock
	 */
	void setLocked(boolean locked);

	/**
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setFirewallExceptionRequestNameViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel();
}
