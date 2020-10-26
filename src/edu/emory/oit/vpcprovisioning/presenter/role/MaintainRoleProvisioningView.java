package edu.emory.oit.vpcprovisioning.presenter.role;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface MaintainRoleProvisioningView extends Editor<RoleProvisioningPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Delete the current RoleProvisioning or cancel the creation of a RoleProvisioning.
		 */
		void deleteRoleProvisioning();

		/**
		 * generate a new RoleProvisioning or save the current RoleProvisioning based on the values in the
		 * inputs.
		 */
		void saveRoleProvisioning();
		RoleProvisioningPojo getRoleProvisioning();
		public void setRoleProvisioningRequisition(RoleProvisioningRequisitionPojo requisition);
		public RoleProvisioningRequisitionPojo getRoleProvisioningRequisition();
		public boolean isValidRoleProvisioningId(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
		public void setOwnerDirectoryPerson(DirectoryPersonPojo pojo);
		public DirectoryPersonPojo getOwnerDirectoryPerson();
		public void setAccount(AccountPojo account);
		public AccountPojo getAccount();
	}

	/**
	 * Specify whether the view is editing an existing RoleProvisioning or creating a new
	 * RoleProvisioning.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the RoleProvisioning is loaded.
	 * 
	 * @param locked true to lock, false to unlock
	 */
	void setLocked(boolean locked);

	/**
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setRoleProvisioningIdViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	public void setHeading(String heading);
}
