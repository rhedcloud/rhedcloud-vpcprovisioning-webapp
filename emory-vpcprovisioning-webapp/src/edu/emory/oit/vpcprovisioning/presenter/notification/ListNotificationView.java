package edu.emory.oit.vpcprovisioning.presenter.notification;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListNotificationView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectNotification(UserNotificationPojo selected);
		public EventBus getEventBus();
		public UserNotificationQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current Vpc or cancel the creation of a Vpc.
		 */
		void saveNotification(UserNotificationPojo selected);
		void deleteNotification(UserNotificationPojo service);
		public void logMessageOnServer(final String message);
		void refreshListWithUnReadNotificationsForUser(UserAccountPojo user);
		void refreshListWithAllNotificationsForUser(UserAccountPojo user);
		void markAllUnreadNotificationsForUserAsRead(UserAccountPojo user);
		void showSrdForUserNotification(UserNotificationPojo userNotification);
//		void filterBySearchString(boolean includeAllNotifications, String searchString);
		void filterByNotificationId(boolean includeAllNotifications, String notificationId);
		void filterBySubject(boolean includeAllNotifications, String subject);
		void filterByText(boolean includeAllNotifications, String text);
		void filterByReferenceId(boolean includeAllNotifications, String referencId);
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
	void setNotifications(List<UserNotificationPojo> services);
	List<UserNotificationPojo> getNotifications();
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeNotificationFromView(UserNotificationPojo service);
	boolean viewAllNotifications();
	void setLongRunningProcess(boolean isLongRunning);
	boolean isLongRunningProcess();
	void initPage();
	void showFilteredStatus();
	void hideFilteredStatus();
	void setFilterTypeItems(List<String> filterTypes);
}
