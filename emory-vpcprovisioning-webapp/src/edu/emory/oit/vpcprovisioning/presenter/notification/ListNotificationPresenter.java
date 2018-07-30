package edu.emory.oit.vpcprovisioning.presenter.notification;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		GWT.log("List notifications presenter...");
		this.eventBus = eventBus;
		setReleaseInfo(clientFactory);
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		getView().showPleaseWaitDialog("Retrieving Notifications from the AWS Account service...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Notifications.  " +
						"Message from server is: " + caught.getMessage());
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
//				setNotificationList(Collections.<UserNotificationPojo> emptyList());

				// Request the Vpc list now.
				refreshList(user);
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
		AsyncCallback<UserNotificationQueryResultPojo> callback = new AsyncCallback<UserNotificationQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Notifications", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Notifications.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(UserNotificationQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Notifications for " + result.getFilterUsed());
				setNotificationList(result.getResults());
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
			}
		};

		GWT.log("refreshing Notifications list...");
		VpcProvisioningService.Util.getInstance().getUserNotificationsForFilter(filter, callback);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setNotificationList(List<UserNotificationPojo> notifications) {
		getView().setNotifications(notifications);
		eventBus.fireEventFromSource(new NotificationListUpdateEvent(notifications), this);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectNotification(UserNotificationPojo selected) {
		// TODO Auto-generated method stub
		
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
		if (Window.confirm("Delete the AWS Notification " + notification.getUserNotificationId() + "?")) {
			getView().showPleaseWaitDialog("Deleting Notification...");
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					getView().showMessageToUser("There was an exception on the " +
							"server deleting the Notification.  Message " +
							"from server is: " + caught.getMessage());
					getView().hidePleaseWaitDialog();
				}

				@Override
				public void onSuccess(Void result) {
					// remove from dataprovider
					getView().removeNotificationFromView(notification);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "Notification was deleted.");
					ActionEvent.fire(eventBus, ActionNames.GO_HOME_NOTIFICATION);
				}
			};
			VpcProvisioningService.Util.getInstance().deleteUserNotification(notification, callback);
		}
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

}
