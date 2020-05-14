package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DeprovisionAccountPresenter extends PresenterBase implements DeprovisionAccountView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private UserAccountPojo userLoggedIn;
	private AccountPojo account;
	private AccountDeprovisioningRequisitionPojo requisition;
	private DialogBox accountDeprovisioningDialog;
	
	public DeprovisionAccountPresenter(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public DeprovisionAccountPresenter(ClientFactory clientFactory, AccountDeprovisioningRequisitionPojo req, AccountPojo account) {
		this.clientFactory = clientFactory;
		this.requisition = req;
		this.account = account;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		getView().showPleaseWaitDialog("Retrieving Account Details...");
		setReleaseInfo(clientFactory);
		
		clientFactory.getShell().setSubTitle("Deprovision Account");
		startGenerate();
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Incidents.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	private void startGenerate() {
		GWT.log("Deprovision account(s)");
	}

	@Override
	public void stop() {
		eventBus = null;
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	public DeprovisionAccountView getView() {
		return clientFactory.getDeprovisionAccountView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}


	@Override
	public void vpcpConfirmOkay() {
//		doSave();
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Account " + 
				account.getAccountId() + "/" + account.getAccountName() + " was not terminated.");
	}

	@Override
	public void deprovisionAccount(AccountPojo account) {
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for and/or correct the required fields.");
			return;
		}
		else {
			getView().setFieldViolations(false);
			getView().resetFieldStyles();
		}
		
		// call the deprovision.generate on the server 
		// and transfer user to the provisioning status view...
		if (accountDeprovisioningDialog != null) {
			accountDeprovisioningDialog.hide();
		}
		getView().showPleaseWaitDialog("Initiating the Account Deprovisioning process...");
		
		AsyncCallback<AccountDeprovisioningPojo> cb = new AsyncCallback<AccountDeprovisioningPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server initiating the Account Deprovisioning process.  " +
						"<p>Message from the server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(AccountDeprovisioningPojo result) {
                getView().hidePleaseWaitDialog();
				AccountProvisioningSummaryPojo summary = new AccountProvisioningSummaryPojo();
				summary.setDeprovisioning(result);
				if (!requisition.isFromProvisioningList()) {
					ActionEvent.fire(eventBus, ActionNames.ACCOUNT_DEPROVISIONING_GENERATED, summary);
				}
				else {
					ActionEvent.fire(eventBus, ActionNames.ACCOUNT_DEPROVISIONING_GENERATED_FROM_PROVISIONING_LIST, summary);
				}
			}
		};
		VpcProvisioningService.Util.getInstance().generateAccountDeprovisioning(requisition, cb);
	}

	@Override
	public AccountDeprovisioningRequisitionPojo getRequisition() {
		return requisition;
	}

	@Override
	public void setAccount(AccountPojo account) {
		this.account = account;
	}

	@Override
	public AccountPojo getAccount() {
		return this.account;
	}

	@Override
	public void setAccountDeprovisioningDialog(DialogBox db) {
		this.accountDeprovisioningDialog = db;
	}

}
