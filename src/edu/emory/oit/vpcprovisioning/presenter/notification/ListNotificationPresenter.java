package edu.emory.oit.vpcprovisioning.presenter.notification;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.NotificationListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationQueryResultPojo;

public class ListNotificationPresenter extends PresenterBase implements ListNotificationView.Presenter {
	private static final Logger log = Logger.getLogger(ListNotificationPresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the Vpc list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the Vpc list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	UserNotificationQueryFilterPojo filter;
	UserAccountPojo userLoggedIn;

	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListNotificationPresenter(ClientFactory clientFactory, boolean clearList, UserNotificationQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.filter = filter;
		clientFactory.getListNotificationView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListNotificationPresenter(ClientFactory clientFactory, ListNotificationPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListNotificationView getView() {
		return clientFactory.getListNotificationView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		GWT.log("List notifications presenter...");
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		setReleaseInfo(clientFactory);
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		getView().hideFilteredStatus();

		getView().showPleaseWaitDialog("Retrieving User Logged In...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Notifications.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().enableButtons();
				// Add a handler to the 'add' button in the shell.
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("User Notifications");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(user);
				userLoggedIn = user;

				List<String> filterTypeItems = new java.util.ArrayList<String>();
				filterTypeItems.add(Constants.USR_NOT_FILTER_NOTIFICATION_ID);
				filterTypeItems.add(Constants.USR_NOT_FILTER_SUBJECT);
				filterTypeItems.add(Constants.USR_NOT_FILTER_TEXT);
				filterTypeItems.add(Constants.USR_NOT_FILTER_REF_ID);
				getView().setFilterTypeItems(filterTypeItems);

				// Request the Vpc list now.
				if (getView().viewAllNotifications()) {
					refreshListWithAllNotificationsForUser(user);
				}
				else {
					refreshListWithUnReadNotificationsForUser(user);
				}
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the Notification list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all Notifications for the current filter being used
		getView().showPleaseWaitDialog("Retrieving Notifications from the AWS Account service...");
		AsyncCallback<UserNotificationQueryResultPojo> callback = new AsyncCallback<UserNotificationQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Notifications", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Notifications.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(UserNotificationQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Notifications for " + result.getFilterUsed());
				setNotificationList(result.getResults());
				getView().initPage();
				// apply authorization mask
				// TODO: need to determine the Notification structure so we can apply authorization mask appropriately
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                if (getView().isLongRunningProcess()) {
            		getView().showPleaseWaitPanel("Please wait...");
            		getView().showPleaseWaitDialog("** STILL ** Marking all un-read notifications to read.  Depending on the "
            				+ "number of notifications, this could take a while...Please Wait");
                }
			}
		};

		GWT.log("refreshing Notifications list...");
		VpcProvisioningService.Util.getInstance().getUserNotificationsForFilter(filter, callback);
	}


	@Override
	public void refreshListWithUnReadNotificationsForUser(UserAccountPojo user) {
		getView().showPleaseWaitDialog("Retrieving Un-Read Notifications from the AWS Account service...");
		filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(user.getPublicId());
		filter.setReadStr("false");
		filter.setRead(false);
		filter.setUseQueryLanguage(true);
		filter.setMaxRows(200);
		refreshList(user);
	}

	@Override
	public void refreshListWithAllNotificationsForUser(UserAccountPojo user) {
		getView().showPleaseWaitDialog("Retrieving Notifications from the AWS Account service...");

		filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(user.getPublicId());
		filter.setReadStr(null);
		filter.setUseQueryLanguage(true);
		filter.setMaxRows(200);
		
		refreshList(user);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setNotificationList(List<UserNotificationPojo> notifications) {
		getView().setNotifications(notifications);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new NotificationListUpdateEvent(notifications), this);
		}
	}

//	@Override
//	public void filterBySearchString(boolean includeAllNotifications, String searchString) {
//		getView().showPleaseWaitDialog("Filtering notifications...");
//		
//		if (searchString == null || searchString.length() == 0) {
//			getView().hidePleaseWaitDialog();
//			getView().showMessageToUser("Please enter a search string");
//			return;
//		}
//
//		getView().showFilteredStatus();
//		if (includeAllNotifications) {
//			filter = new UserNotificationQueryFilterPojo();
//			filter.setUserId(userLoggedIn.getPublicId());
//			filter.setReadStr(null);
//			filter.setUseQueryLanguage(true);
//			filter.setMaxRows(200);
//			filter.setSearchString(searchString);
//		}
//		else {
//			filter = new UserNotificationQueryFilterPojo();
//			filter.setUserId(userLoggedIn.getPublicId());
//			filter.setReadStr("false");
//			filter.setRead(false);
//			filter.setUseQueryLanguage(true);
//			filter.setMaxRows(200);
//			filter.setSearchString(searchString);
//		}
//		refreshList(userLoggedIn);
//	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectNotification(UserNotificationPojo selected) {
		
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public UserNotificationQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(UserNotificationQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteNotification(final UserNotificationPojo notification) {
//		if (Window.confirm("Delete the AWS Notification " + notification.getUserNotificationId() + "?")) {
//			getView().showPleaseWaitDialog("Deleting Notification...");
//			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
//
//				@Override
//				public void onFailure(Throwable caught) {
//					getView().showMessageToUser("There was an exception on the " +
//							"server deleting the Notification.  Message " +
//							"from server is: " + caught.getMessage());
//					getView().hidePleaseWaitDialog();
//				}
//
//				@Override
//				public void onSuccess(Void result) {
//					// remove from dataprovider
//					getView().removeNotificationFromView(notification);
//					getView().hidePleaseWaitDialog();
//					// status message
//					getView().showStatus(getView().getStatusMessageSource(), "Notification was deleted.");
//					ActionEvent.fire(eventBus, ActionNames.GO_HOME_NOTIFICATION);
//				}
//			};
//			VpcProvisioningService.Util.getInstance().deleteUserNotification(notification, callback);
//		}
	}

	@Override
	public void saveNotification(final UserNotificationPojo selected) {
		getView().showPleaseWaitDialog("Saving Notification...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
		}
		AsyncCallback<UserNotificationPojo> callback = new AsyncCallback<UserNotificationPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Notification", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Notification.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(UserNotificationPojo result) {
				getView().hidePleaseWaitDialog();
				if (filter == null) {
					filter = new UserNotificationQueryFilterPojo();
					filter.setUserId(userLoggedIn.getPublicId());
				}
				ActionEvent.fire(eventBus, ActionNames.NOTIFICATION_SAVED, filter);
			}
		};
		// it's an update
		VpcProvisioningService.Util.getInstance().updateUserNotification(selected, callback);
	}

	@Override
	public void markAllUnreadNotificationsForUserAsRead(UserAccountPojo user) {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().setLongRunningProcess(false);
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server marking all un-read notifications as read.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().setLongRunningProcess(false);
				if (filter == null) {
					filter = new UserNotificationQueryFilterPojo();
					filter.setUserId(userLoggedIn.getPublicId());
				}
				ActionEvent.fire(eventBus, ActionNames.NOTIFICATION_SAVED, filter);
			}
			
		};
		getView().setLongRunningProcess(true);
		getView().showPleaseWaitPanel("Please wait...");
		getView().showPleaseWaitDialog("Marking all un-read notifications to read.  Depending on the "
				+ "number of notifications, this could take a while...Please Wait");
		VpcProvisioningService.Util.getInstance().markAllUnreadNotificationsForUserAsRead(user, callback);
	}

	@Override
	public void showSrdForUserNotification(final UserNotificationPojo userNotification) {
		// get the SRD associated to the notification and pass it
		getView().showPleaseWaitDialog("Retrieving Security Risk Detection from the SRD service...");
		AsyncCallback<SecurityRiskDetectionQueryResultPojo> cb = new AsyncCallback<SecurityRiskDetectionQueryResultPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Security Risk Detection.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(SecurityRiskDetectionQueryResultPojo result) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				if (result.getResults().size() > 0) {
					SecurityRiskDetectionPojo srd = result.getResults().get(0);
					ActionEvent.fire(getEventBus(), ActionNames.VIEW_SRD_FOR_USER_NOTIFICATION, srd, userNotification);
				}
				else {
					// TODO: error - no srd found
				}
			}
			
		};
		SecurityRiskDetectionQueryFilterPojo filter = new SecurityRiskDetectionQueryFilterPojo();
		filter.setSecurityRiskDetectionId(userNotification.getReferenceId());
		VpcProvisioningService.Util.getInstance().getSecurityRiskDetectionsForFilter(filter, cb);
	}

	@Override
	public void filterByNotificationId(boolean includeAllNotifications, String notificationId) {
		getView().showPleaseWaitDialog("Filtering notifications by Notification Id");
		getView().showFilteredStatus();
		filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(userLoggedIn.getPublicId());
		filter.setUseQueryLanguage(true);
		filter.setMaxRows(200);
		filter.setFuzzyFilter(true);
		filter.setUserNotificationId(notificationId);
		if (includeAllNotifications) {
			filter.setReadStr(null);
		}
		else {
			filter.setReadStr("false");
			filter.setRead(false);
		}
		this.refreshList(userLoggedIn);
	}

	@Override
	public void filterBySubject(boolean includeAllNotifications, String subject) {
		getView().showPleaseWaitDialog("Filtering notifications by Subject");
		getView().showFilteredStatus();
		filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(userLoggedIn.getPublicId());
		filter.setUseQueryLanguage(true);
		filter.setMaxRows(200);
		filter.setFuzzyFilter(true);
		filter.setSubject(subject);
		if (includeAllNotifications) {
			filter.setReadStr(null);
		}
		else {
			filter.setReadStr("false");
			filter.setRead(false);
		}
		this.refreshList(userLoggedIn);
	}

	@Override
	public void filterByText(boolean includeAllNotifications, String text) {
		getView().showPleaseWaitDialog("Filtering notifications by Text");
		getView().showFilteredStatus();
		filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(userLoggedIn.getPublicId());
		filter.setFuzzyFilter(true);
		filter.setText(text);
		filter.setUseQueryLanguage(true);
		filter.setMaxRows(200);
		if (includeAllNotifications) {
			filter.setReadStr(null);
		}
		else {
			filter.setReadStr("false");
			filter.setRead(false);
		}
		this.refreshList(userLoggedIn);
	}

	@Override
	public void filterByReferenceId(boolean includeAllNotifications, String referencId) {
		getView().showPleaseWaitDialog("Filtering notifications by Reference Id");
		getView().showFilteredStatus();
		filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(userLoggedIn.getPublicId());
		filter.setFuzzyFilter(true);
		filter.setReferenceId(referencId);
		filter.setUseQueryLanguage(true);
		filter.setMaxRows(200);
		if (includeAllNotifications) {
			filter.setReadStr(null);
		}
		else {
			filter.setReadStr("false");
			filter.setRead(false);
		}
		this.refreshList(userLoggedIn);
	}

}
