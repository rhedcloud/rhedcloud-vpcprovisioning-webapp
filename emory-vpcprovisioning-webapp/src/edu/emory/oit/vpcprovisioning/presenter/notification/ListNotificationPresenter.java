package edu.emory.oit.vpcprovisioning.presenter.notification;

import java.util.Collections;
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
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.NotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

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
	
	NotificationQueryFilterPojo filter;

	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListNotificationPresenter(ClientFactory clientFactory, boolean clearList, NotificationQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
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

		getView().showPleaseWaitDialog();
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				// Add a handler to the 'add' button in the shell.
//				clientFactory.getShell().setAddButtonVisible(true);
//				clientFactory.getShell().setBackButtonVisible(false);
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("AWS Notifications");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				setNotificationList(Collections.<NotificationPojo> emptyList());

				// Request the Vpc list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all Notifications for the current filter being used
		AsyncCallback<NotificationQueryResultPojo> callback = new AsyncCallback<NotificationQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Notifications", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Vpcs.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(NotificationQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Notifications for " + result.getFilterUsed());
				setNotificationList(result.getResults());
				// apply authorization mask
				// TODO: need to determine the Notification structure so we can apply authorization mask appropriately
				if (user.isLitsAdmin()) {
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
		VpcProvisioningService.Util.getInstance().getNotificationsForFilter(filter, callback);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setNotificationList(List<NotificationPojo> notifications) {
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
	public void selectNotification(NotificationPojo selected) {
		// TODO Auto-generated method stub
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public NotificationQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(NotificationQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteNotification(final NotificationPojo notification) {
		if (Window.confirm("Delete the AWS Notification " + notification.getNotificationId() + "?")) {
			getView().showPleaseWaitDialog();
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
			VpcProvisioningService.Util.getInstance().deleteNotification(notification, callback);
		}
	}

}
