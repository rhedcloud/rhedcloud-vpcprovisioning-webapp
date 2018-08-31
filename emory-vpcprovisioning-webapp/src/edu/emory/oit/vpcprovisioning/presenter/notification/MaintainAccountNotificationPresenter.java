package edu.emory.oit.vpcprovisioning.presenter.notification;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
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
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainAccountNotificationPresenter extends PresenterBase  implements MaintainAccountNotificationView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String notificationId;
	private AccountNotificationPojo notification;
	private AccountPojo account;
	private UserAccountPojo userLoggedIn;
	int createdCount = 0;
	boolean showStatus = false;
	boolean startTimer = true;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT notification for an unknown account.
	 */
	public MaintainAccountNotificationPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.notification = null;
		this.notificationId = null;
		this.account = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}
	
	/**
	 * For creating a new ACCOUNT notification for a given account.
	 */
	public MaintainAccountNotificationPresenter(ClientFactory clientFactory, AccountPojo account) {
		this.isEditing = false;
		this.notification = null;
		this.notificationId = null;
		this.account = account;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT notification.
	 */
	public MaintainAccountNotificationPresenter(ClientFactory clientFactory, AccountPojo account, AccountNotificationPojo notification) {
		this.account = account;
		this.isEditing = true;
		this.notificationId = notification.getAccountNotificationId();
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
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				if (!isEditing) {
					notification.setCreateTime(new Date());
					notification.setCreateUser(userLoggedIn.getPublicId());
				}
				getView().setUserLoggedIn(user);
				
				List<String> priorities = new java.util.ArrayList<String>();
				priorities.add("Low");
				priorities.add("Medium");
				priorities.add("High");
				getView().setPriorityItems(priorities);
				
				if (!isEditing && account == null) {
					AsyncCallback<AccountQueryResultPojo> acct_cb = new AsyncCallback<AccountQueryResultPojo>() {
						@Override
						public void onFailure(Throwable caught) {
							getView().hidePleaseWaitDialog();
							getView().hidePleaseWaitPanel();
							GWT.log("Exception retrieving AWS accounts", caught);
							getView().showMessageToUser("There was an exception on the " +
									"server retrieving a list of AWS Accounts.  Message " +
									"from server is: " + caught.getMessage());
						}

						@Override
						public void onSuccess(AccountQueryResultPojo accountItems) {
							GWT.log("got " + accountItems.getResults().size() + " accounts.");
							getView().setAccountItems(accountItems.getResults());
							getView().initPage();
							getView().setInitialFocus();
							// apply authorization mask
							if (user.isCentralAdmin()) {
								getView().applyCentralAdminMask();
							}
							else {
								getView().applyAWSAccountAuditorMask();
							}
							getView().hidePleaseWaitDialog();
							getView().hidePleaseWaitPanel();
						}
					};
					GWT.log("getting accounts");
					VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, acct_cb);
				}
				else {
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
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain account notification: create");
		isEditing = false;
		notification = new AccountNotificationPojo();
		notification.setType(Constants.NOTIFICATION_TYPE_CENTRAL_ADMIN);
		if (this.account == null) {
			getView().showAccountListBox();
		}
		else {
			notification.setAccountId(account.getAccountId());
			getView().hideAccountListBox();
		}
		getView().setEditing(false);
	}

	private void startEdit() {
		GWT.log("Maintain account notification: edit");
		isEditing = true;
		getView().hideAccountListBox();
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
		getView().showPleaseWaitDialog("Saving Notification(s)...");
		if (!isFormValid()) {
			return;
		}
		AsyncCallback<AccountNotificationPojo> callback = new AsyncCallback<AccountNotificationPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Notification", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Notification for account (" + notification.getAccountId() + ").  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(AccountNotificationPojo result) {
				getView().hidePleaseWaitDialog();
				getView().showStatus(null, "Notification was saved");
			}
		};
		if (!this.isEditing) {
			// it's a create
			GWT.log("[MaintainAccountNotificationPresenter] creating notification for account id: " + notification.getAccountId());
			VpcProvisioningService.Util.getInstance().createAccountNotification(notification, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateAccountNotification(notification, callback);
		}
	}

	@Override
	public void createNotifications(List<AccountNotificationPojo> notifications) {
		createdCount = 0;
		showStatus = false;

		if (!isFormValid()) {
			return;
		}
		final int totalToCreate = notifications.size();
		
		final StringBuffer errors = new StringBuffer();
		for (int i=0; i<notifications.size(); i++) {
			final AccountNotificationPojo not = notifications.get(i);
			not.setCreateTime(new Date());
			not.setCreateUser(userLoggedIn.getPublicId());
			final int listCounter = i;
			
			AsyncCallback<AccountNotificationPojo> callback = new AsyncCallback<AccountNotificationPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Exception saving the Notification for account: " + not.getAccountId(), caught);
					errors.append("There was an exception on the " +
							"server saving the Notification for account (" + not.getAccountId() + ").  " +
							"<p>Message from server is: " + caught.getMessage() + "</p>");
					if (!showStatus) {
						errors.append("\n");
					}
					if (listCounter == totalToCreate - 1) {
						showStatus = true;
					}
				}

				@Override
				public void onSuccess(AccountNotificationPojo result) {
					createdCount++;
					if (listCounter == totalToCreate - 1) {
						showStatus = true;
					}
				}
			};

			GWT.log("[MaintainAccountNotificationPresenter] creating notification for account id: " + not.getAccountId());
			VpcProvisioningService.Util.getInstance().createAccountNotification(not, callback);
		}
		if (!showStatus) {
			// wait for all the creates to finish processing
			int delayMs = 500;
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
				@Override
				public boolean execute() {
					if (showStatus) {
						startTimer = false;
						showCreateNotificationsStatus(createdCount, totalToCreate, errors);
					}
					return startTimer;
				}
			}, delayMs);
		}
		else {
			showCreateNotificationsStatus(createdCount, totalToCreate, errors);
		}
	}

	void showCreateNotificationsStatus(int createdCount, int totalToCreate, StringBuffer errors) {
		if (errors.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showStatus(null, createdCount + " out of " + totalToCreate + " Notification(s) were created.");
		}
		else {
			getView().hidePleaseWaitDialog();
			errors.insert(0, createdCount + " out of " + totalToCreate + " Notification(s) were created.  "
					+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(errors.toString());
		}
	}
	boolean isFormValid() {
		boolean isValid = true;
		
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().hidePleaseWaitPanel();
			getView().showMessageToUser("Please provide data for the required fields.");
			return false;
		}
		else {
			getView().resetFieldStyles();
		}
		return isValid;
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
	public AccountNotificationPojo getNotification() {
		return notification;
	}

	@Override
	public AccountPojo getAccount() {
		return account;
	}

	@Override
	public void showSrdForAccountNotification(final AccountNotificationPojo selected) {
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
					ActionEvent.fire(getEventBus(), ActionNames.VIEW_SRD_FOR_ACCOUNT_NOTIFICATION, srd, selected);
				}
				else {
					// TODO: error - no srd found
				}
			}
			
		};
		SecurityRiskDetectionQueryFilterPojo filter = new SecurityRiskDetectionQueryFilterPojo();
		filter.setSecurityRiskDetectionId(selected.getReferenceid());
		VpcProvisioningService.Util.getInstance().getSecurityRiskDetectionsForFilter(filter, cb);
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public void setNotification(AccountNotificationPojo notification) {
		this.notification = notification;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}
}
