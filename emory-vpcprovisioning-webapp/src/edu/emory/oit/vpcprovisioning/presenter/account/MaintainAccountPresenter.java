package edu.emory.oit.vpcprovisioning.presenter.account;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainAccountPresenter implements MaintainAccountView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String accountId;
	private AccountPojo account;
	private String awsAccountsURL = "Cannot retrieve AWS Accounts URL";
	private String awsBillingManagementURL = "Cannot retrieve AWS Billing Management URL";

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainAccountPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.account = null;
		this.accountId = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainAccountView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainAccountPresenter(ClientFactory clientFactory, AccountPojo account) {
		this.isEditing = true;
		this.accountId = account.getAccountId();
		this.clientFactory = clientFactory;
		this.account = account;
		clientFactory.getMaintainAccountView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		
		// TODO: get awsAccountsURL and awsBillingManagementURL in parallel
		AsyncCallback<String> accountsUrlCB = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				GWT.log("accounts URL from server: " + result);
				awsAccountsURL = result;
			}
		};
		VpcProvisioningService.Util.getInstance().getAwsAccountsURL(accountsUrlCB);

		AsyncCallback<String> billingUrlCB = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				GWT.log("billing URL from server: " + result);
				awsBillingManagementURL = result;
			}
		};
		VpcProvisioningService.Util.getInstance().getAwsBillingManagementURL(billingUrlCB);
		
		ReleaseInfo ri = new ReleaseInfo();
		
		clientFactory.getShell().setReleaseInfo(ri.toString());
		if (accountId == null) {
			clientFactory.getShell().setSubTitle("Create Account");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Account");
			startEdit();
		}
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving e-mail types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving e-mail types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().initPage();
						getView().setEmailTypeItems(result);
						getView().hidePleaseWaitDialog();
						getView().setInitialFocus();
						// apply authorization mask
						if (user.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
							getView().applyEmoryAWSAdminMask();
						}
						else if (user.hasPermission(Constants.PERMISSION_VIEW_EVERYTHING)) {
							clientFactory.getShell().setSubTitle("View Account");
							getView().applyEmoryAWSAuditorMask();
						}
						else {
							// ??
						}
						
						getView().setAwsAccountsURL(awsAccountsURL);
						getView().setAwsBillingManagementURL(awsBillingManagementURL);
					}
				};
				VpcProvisioningService.Util.getInstance().getEmailTypeItems(callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain account: create");
		isEditing = false;
		getView().setEditing(false);
		account = new AccountPojo();
	}

	private void startEdit() {
		GWT.log("Maintain account: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the account is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainAccountView().setLocked(false);
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
	public void deleteAccount() {
		if (isEditing) {
			doDeleteAccount();
		} else {
			doCancelAccount();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelAccount() {
		ActionEvent.fire(eventBus, ActionNames.ACCOUNT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteAccount() {
		if (account == null) {
			return;
		}

		// TODO Delete the account on server then fire onAccountDeleted();
	}

	@Override
	public void saveAccount() {
		getView().showPleaseWaitDialog();
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
//			if (true) {
//				// temporary
//				return;
//			}
		}
		AsyncCallback<AccountPojo> callback = new AsyncCallback<AccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Account", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Account.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(AccountPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.ACCOUNT_SAVED, account);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createAccount(account, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateAccount(account, callback);
		}
	}

	@Override
	public AccountPojo getAccount() {
		return this.account;
	}

	@Override
	public boolean isValidAccountId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidAccountName(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private MaintainAccountView getView() {
		return clientFactory.getMaintainAccountView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w) {
		AsyncCallback<DirectoryMetaDataPojo> callback = new AsyncCallback<DirectoryMetaDataPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server exception retrieving directory meta data", caught);
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
					" - from the Identity Service.");
			}
		};
		VpcProvisioningService.Util.getInstance().getDirectoryMetaDataForNetId(netId, callback);
	}

	@Override
	public void setSpeedChartStatusForKeyOnWidget(String key, final Widget w) {
		AsyncCallback<SpeedChartPojo> callback = new AsyncCallback<SpeedChartPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server exception validating speedtype", caught);
				w.setTitle("Server exception validating speedtype");
				getView().setSpeedTypeStatus("Invalid account");
				getView().setSpeedTypeColor(Constants.COLOR_RED);
			}

			@Override
			public void onSuccess(SpeedChartPojo scp) {
				if (scp == null) {
					w.setTitle("Invalid account number, can't validate this number");
					getView().setSpeedTypeStatus("Invalid account");
					getView().setSpeedTypeColor(Constants.COLOR_RED);
				}
				else {
				    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
				    String status = scp.getEuValidityDescription() + 
							"  End date: " + dateFormat.format(scp.getEuProjectEndDate()); 
					w.setTitle(status);
					getView().setSpeedTypeStatus(status);
					getView().setSpeedTypeColor(Constants.COLOR_GREEN);
				}
			}
		};
		if (key != null && key.length() > 0) {
			SpeedChartQueryFilterPojo filter = new SpeedChartQueryFilterPojo();
			filter.getSpeedChartKeys().add(key);
			VpcProvisioningService.Util.getInstance().getSpeedChartForFinancialAccountNumber(key, callback);
		}
		else {
			GWT.log("null key, can't validate yet");
		}
	}

	@Override
	public void setSpeedChartStatusForKey(String key, Label label) {
		// null check / length
		if (key == null || key.length() != 10) {
			label.setText("Invalid length");
			getView().setSpeedTypeColor(Constants.COLOR_RED);
			return;
		}
		// TODO: numeric characters
		
		setSpeedChartStatusForKeyOnWidget(key, getView().getSpeedTypeWidget());
	}
}
