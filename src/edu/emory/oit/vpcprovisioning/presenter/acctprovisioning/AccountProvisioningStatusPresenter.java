package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class AccountProvisioningStatusPresenter extends PresenterBase implements AccountProvisioningStatusView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private AccountProvisioningPojo provisioning;
	private AccountDeprovisioningPojo deprovisioning;
	private AccountProvisioningSummaryPojo summary;
	boolean fromGenerate;
	boolean fromProvisioningList;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new Vpncp.
	 */
	public AccountProvisioningStatusPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.provisioning = null;
		this.deprovisioning = null;
		this.summary = null;
		this.provisioningId = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public AccountProvisioningStatusPresenter(ClientFactory clientFactory, AccountProvisioningSummaryPojo summary) {
		this.isEditing = true;
		this.clientFactory = clientFactory;
		this.summary = summary;
		this.provisioning = summary.getProvisioning();
		this.deprovisioning = summary.getDeprovisioning();
		if (summary.isProvision()) {
			this.provisioningId = provisioning.getProvisioningId();
		}
		else {
			this.provisioningId = deprovisioning.getDeprovisioningId();
		}
		this.fromGenerate = false;
		getView().setPresenter(this);
	}

	public AccountProvisioningStatusPresenter(ClientFactory clientFactory, AccountProvisioningSummaryPojo summary, boolean fromGenerate, boolean fromProvisioningList) {
		this.isEditing = true;
		this.summary = summary;
		this.provisioning = summary.getProvisioning();
		this.deprovisioning = summary.getDeprovisioning();
		if (summary.isProvision()) {
			this.provisioningId = provisioning.getProvisioningId();
		}
		else {
			this.provisioningId = deprovisioning.getDeprovisioningId();
		}
		this.clientFactory = clientFactory;
		this.fromGenerate = fromGenerate;
		this.fromProvisioningList = fromProvisioningList;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().clearProvisioningStatus();
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				GWT.log("Exception Retrieving Vpcs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				clientFactory.getShell().setSubTitle("VPN Connection Status");
				getView().enableButtons();
				getView().setUserLoggedIn(user);
				
				// refresh display with current status
				refreshProvisioningStatusForId(provisioningId);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getVpncpStatusView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private AccountProvisioningStatusView getView() {
		return clientFactory.getAccountProvisioningStatusView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void refreshProvisioningStatusForId(final String provisioningId) {
		AsyncCallback<AccountProvisioningQueryResultPojo> callback = new AsyncCallback<AccountProvisioningQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().stopTimer();
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving Account Provisioning info", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Account Provisioning status information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(AccountProvisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + 
						" record for the filter: " + result.getFilterUsed());
				
				if (result.getResults().size() == 0) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  No Account Provisioning info found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server did not return a result "
							+ "for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
							+ "is an unexpected situation that may need "
							+ "to be addressed by system administrators.");
				}
				else if (result.getResults().size() > 1) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  More than one record found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server returned more than one Account Provisioning/Deprovisioning "
							+ "record for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
							+ "is an unexpected situation that may need "
							+ "to be addressed by system administrators.");
				}
				else {
					// expected behavior
					setProvisioningSummary(result.getResults().get(0));
					if (getProvisioningSummary().isProvision()) {
						setProvisioning(result.getResults().get(0).getProvisioning());
						if (provisioning.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
							getView().stopTimer();
						}
					}
					else {
						setDeprovisioning(result.getResults().get(0).getDeprovisioning());
						if (deprovisioning.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
							getView().stopTimer();
						}
					}
					getView().refreshProvisioningStatusInformation();
	                getView().hidePleaseWaitDialog();
	                getView().hidePleaseWaitPanel();

					// start the timer
	                if (!getView().isTimerRunning()) {
						getView().startTimer(5000);
	                }
				}
			}
		};

		GWT.log("[PRESENTER] refreshing object for provisioning id:  " + provisioningId);
        getView().showPleaseWaitDialog("Retrieving Account provisioning summaries for the provisioning id: " + provisioningId);
		AccountProvisioningQueryFilterPojo filter = new AccountProvisioningQueryFilterPojo();
		filter.setDeprovisioningId(provisioningId);
		VpcProvisioningService.Util.getInstance().getAccountProvisioningSummariesForFilter(filter, callback);
	}

	@Override
	public boolean isFromGenerate() {
		return this.fromGenerate;
	}

	@Override
	public void setFromGenerate(boolean fromGenerate) {
		this.fromGenerate = fromGenerate;
	}

	public AccountProvisioningPojo getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(AccountProvisioningPojo provisioning) {
		this.provisioning = provisioning;
	}

	public AccountDeprovisioningPojo getDeprovisioning() {
		return deprovisioning;
	}

	public void setDeprovisioning(AccountDeprovisioningPojo deprovisioning) {
		this.deprovisioning = deprovisioning;
	}

	public void setProvisioningSummary(AccountProvisioningSummaryPojo summary) {
		this.summary = summary;
	}

	@Override
	public AccountProvisioningSummaryPojo getProvisioningSummary() {
		return summary;
	}

	public boolean isFromProvisioningList() {
		return fromProvisioningList;
	}

	public void setFromProvisioningList(boolean fromProvisioningList) {
		this.fromProvisioningList = fromProvisioningList;
	}
}
