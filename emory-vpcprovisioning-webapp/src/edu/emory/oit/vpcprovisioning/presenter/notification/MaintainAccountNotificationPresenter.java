package edu.emory.oit.vpcprovisioning.presenter.notification;

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
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainAccountNotificationPresenter extends PresenterBase  implements MaintainAccountNotificationView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String notificationId;
	private AccountNotificationPojo notification;
	private AccountPojo account;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainAccountNotificationPresenter(ClientFactory clientFactory, AccountPojo account) {
		this.isEditing = false;
		this.notification = null;
		this.notificationId = null;
		this.account = account;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainAccountNotificationView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainAccountNotificationPresenter(ClientFactory clientFactory, AccountPojo account, AccountNotificationPojo notification) {
		this.account = account;
		this.isEditing = true;
		this.notificationId = notification.getAccountNotificationId();
		this.clientFactory = clientFactory;
		this.notification = notification;
		clientFactory.getMaintainAccountNotificationView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().showPleaseWaitDialog("Retrieving Notification Details...");
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

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
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().hidePleaseWaitDialog();
				getView().setInitialFocus();
				// apply authorization mask
				// TODO: for now (7/12/2018), this will be view only
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
		GWT.log("Maintain account notification: create");
		isEditing = false;
		getView().setEditing(false);
		notification = new AccountNotificationPojo();
	}

	private void startEdit() {
		GWT.log("Maintain account notification: edit");
		isEditing = true;
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
		ActionEvent.fire(eventBus, ActionNames.ACCOUNT_NOTIFICATION_EDITING_CANCELED);
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
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
		}
		AsyncCallback<AccountNotificationPojo> callback = new AsyncCallback<AccountNotificationPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Notification", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Notification.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(AccountNotificationPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.ACCOUNT_NOTIFICATION_SAVED, notification);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createAccountNotification(notification, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateAccountNotification(notification, callback);
		}
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

	public MaintainAccountNotificationView getView() {
		return clientFactory.getMaintainAccountNotificationView();
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

	@Override
	public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w) {
		AsyncCallback<DirectoryMetaDataPojo> callback = new AsyncCallback<DirectoryMetaDataPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(DirectoryMetaDataPojo result) {
				if (result.getFirstName() == null) {
					result.setFirstName("Unknown");
				}
				if (result.getLastName() == null) {
					result.setLastName("Net ID");
				}
				w.setTitle(result.getFirstName() + " " + result.getLastName() + 
					" - from the Identity Notification.");
			}
		};
		VpcProvisioningService.Util.getInstance().getDirectoryMetaDataForPublicId(netId, callback);
	}

	@Override
	public AccountNotificationPojo getNotification() {
		return notification;
	}

	@Override
	public AccountPojo getAccount() {
		return account;
	}
}
