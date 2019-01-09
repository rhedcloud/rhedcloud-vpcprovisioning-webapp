package edu.emory.oit.vpcprovisioning.presenter.notification;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationQueryFilterPojo;

public class MaintainNotificationPresenter extends PresenterBase implements MaintainNotificationView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String notificationId;
	private UserNotificationPojo notification;
	private UserAccountPojo userLoggedIn;
	private DirectoryPersonPojo directoryPerson;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainNotificationPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.notification = null;
		this.notificationId = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainNotificationPresenter(ClientFactory clientFactory, UserNotificationPojo notification) {
		this.isEditing = true;
		this.notificationId = notification.getUserNotificationId();
		this.clientFactory = clientFactory;
		this.notification = notification;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		getView().showPleaseWaitDialog("Retrieving Notification Details...");
		setReleaseInfo(clientFactory);
		
		if (notificationId == null) {
			clientFactory.getShell().setSubTitle("Create Notification");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Notification");
			startEdit();
		}
		
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
				userLoggedIn = user;
				if (!isEditing) {
					notification.setCreateTime(new Date());
					notification.setCreateUser(userLoggedIn.getPublicId());
				}
				List<String> priorities = new java.util.ArrayList<String>();
				priorities.add("Low");
				priorities.add("Medium");
				priorities.add("High");
				getView().setPriorityItems(priorities);
				

				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().hidePleaseWaitDialog();
				getView().setInitialFocus();
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain notification: create");
		isEditing = false;
		notification = new UserNotificationPojo();
		notification.setType(Constants.NOTIFICATION_TYPE_CENTRAL_ADMIN);
		getView().showCreateNotificationPanel();
		getView().showCancelButton();
		getView().setEditing(false);
	}

	private void startEdit() {
		GWT.log("Maintain notification: edit");
		isEditing = true;
		getView().hideCreateNotificationPanel();
		getView().hidCancelButton();
		getView().setEditing(true);
		// Lock the display until the notification is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainNotificationView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void deleteNotification() {
		if (isEditing) {
			doDeleteNotification();
		} else {
			doCancelNotification();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelNotification() {
		ActionEvent.fire(eventBus, ActionNames.ACCOUNT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteNotification() {
		if (notification == null) {
			return;
		}

		// TODO Delete the notification on server then fire onNotificationDeleted();
	}

	@Override
	public void saveNotification() {
		getView().showPleaseWaitDialog("Saving Notification...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().hidePleaseWaitPanel();
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
				if (!isEditing) {
					getView().hidePleaseWaitDialog();
					getView().showStatus(null, "Notification was saved");
				}
				else {
					UserNotificationQueryFilterPojo filter = new UserNotificationQueryFilterPojo();
					filter.setUserId(userLoggedIn.getPublicId());
					ActionEvent.fire(getEventBus(), ActionNames.NOTIFICATION_SAVED, filter);
				}
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createUserNotification(notification, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateUserNotification(notification, callback);
		}
	}

	@Override
	public UserNotificationPojo getNotification() {
		return this.notification;
	}

	@Override
	public boolean isValidNotificationId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidNotificationName(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	public MaintainNotificationView getView() {
		return clientFactory.getMaintainNotificationView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setNotification(UserNotificationPojo notification) {
		this.notification = notification;
	}

	@Override
	public void showSrdForUserNotification(final UserNotificationPojo userNotification) {
		getView().showPleaseWaitDialog("Retrieving Security Risk Detection from the SRD service...");
		// get the SRD associated to the notification and pass it
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
	public void setDirectoryPerson(DirectoryPersonPojo pojo) {
		directoryPerson = pojo;
	}

	@Override
	public DirectoryPersonPojo getDirectoryPerson() {
		return directoryPerson;
	}

}
