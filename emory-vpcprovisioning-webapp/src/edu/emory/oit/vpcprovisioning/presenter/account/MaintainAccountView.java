package edu.emory.oit.vpcprovisioning.presenter.account;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainAccountView extends Editor<AccountPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteAccount();

		/**
		 * Create a new account or save the current account based on the values in the
		 * inputs.
		 */
		void saveAccount();
		AccountPojo getAccount();
		public boolean isValidAccountId(String value);
		public boolean isValidAccountName(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w);
		public void setSpeedChartStatusForKeyOnWidget(String key, Widget w, boolean confirmSpeedType);
		public void setSpeedChartStatusForKey(String key, Label label, boolean confirmSpeedType);
		public boolean didConfirmSpeedType();
		public SpeedChartPojo getSpeedType();
		public void logMessageOnServer(final String message);
		public void setDirectoryPerson(DirectoryPersonPojo pojo);
		public DirectoryPersonPojo getDirectoryPerson();
		public void addDirectoryPersonInRoleToAccount(String roleName);
		public void getRoleAssignmentsForAccount();
		public List<RoleAssignmentSummaryPojo> getRoleAssignmentSummaries();
		public void removeRoleAssignmentFromAccount(String accountId, RoleAssignmentSummaryPojo roleAssignmentSummary);
		public MaintainAccountView getView();

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
	void setAccountIdViolation(String message);
	void setAccountNameViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void setEmailTypeItems(List<String> emailTypes);
	void setAwsAccountsURL(String awsAccountsURL);
	void setAwsBillingManagementURL(String awsBillingManagementURL);
	void setSpeedTypeStatus(String status);
	void setSpeedTypeColor(String color);
	Widget getSpeedTypeWidget();
	void setSpeedTypeConfirmed(boolean confirmed);
	boolean isSpeedTypeConfirmed();
	void addRoleAssignment(int index, String name, String netId, String roleName, String widgetTitle);
	void setRoleAssignmentSummaries(List<RoleAssignmentSummaryPojo> summaries);
	void setComplianceClassItems(List<String> complianceClassTypes);
	void enableAdminMaintenance();
	void disableAdminMaintenance();
	void addAccountNotification(AccountNotificationPojo accountNotification);
	void clearAccountNotificationList();
}
