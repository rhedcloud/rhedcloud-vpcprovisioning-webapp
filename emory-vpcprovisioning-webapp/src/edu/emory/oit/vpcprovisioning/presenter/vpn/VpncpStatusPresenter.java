package edu.emory.oit.vpcprovisioning.presenter.vpn;

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
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;

public class VpncpStatusPresenter extends PresenterBase implements VpncpStatusView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private VpnConnectionProvisioningPojo vpncp;
	private VpnConnectionDeprovisioningPojo vpncdp;
	private VpnConnectionProvisioningSummaryPojo vpncpSummary;
	private VpnConnectionProfilePojo profile;
	boolean fromGenerate;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new Vpncp.
	 */
	public VpncpStatusPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpncp = null;
		this.vpncdp = null;
		this.vpncpSummary = null;
		this.provisioningId = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public VpncpStatusPresenter(ClientFactory clientFactory, VpnConnectionProvisioningSummaryPojo vpncpSummary) {
		this.isEditing = true;
		this.clientFactory = clientFactory;
		this.vpncpSummary = vpncpSummary;
		this.vpncp = vpncpSummary.getProvisioning();
		this.vpncdp = vpncpSummary.getDeprovisioning();
		if (vpncpSummary.isProvision()) {
			this.provisioningId = vpncp.getProvisioningId();
		}
		else {
			this.provisioningId = vpncdp.getProvisioningId();
		}
		this.fromGenerate = false;
		getView().setPresenter(this);
	}

	public VpncpStatusPresenter(ClientFactory clientFactory, VpnConnectionProvisioningSummaryPojo vpncpSummary, boolean fromGenerate) {
		this.isEditing = true;
		this.vpncpSummary = vpncpSummary;
		this.vpncp = vpncpSummary.getProvisioning();
		this.vpncdp = vpncpSummary.getDeprovisioning();
		if (vpncpSummary.isProvision()) {
			this.provisioningId = vpncp.getProvisioningId();
		}
		else {
			this.provisioningId = vpncdp.getProvisioningId();
		}
		this.clientFactory = clientFactory;
		this.fromGenerate = fromGenerate;
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
				// TODO: depending on how the scheduler works, we may not need 
				// to refreshVpncpStatusForId here.  If the scheduler immediately 
				// refreshes the delay and then waits, we should just be able 
				// to start the timer and let it do the refresh.
				
				// refresh display with current status
				refreshProvisioningStatusForId(provisioningId);
				
//				getView().refreshVpncpStatusInformation();
//                getView().hidePleaseWaitPanel();
//                getView().hidePleaseWaitDialog();
				
				// start the timer
				getView().startTimer(5000);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
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

	@Override
	public void deleteVpncp() {
		if (isEditing) {
			doDeleteVpncp();
		} else {
			doCancelVpncp();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelVpncp() {
		ActionEvent.fire(eventBus, ActionNames.VPCP_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteVpncp() {
		if (vpncp == null) {
			return;
		}

		// TODO Delete the vpncp on server then fire onVpncpDeleted();
	}

	@Override
	public VpnConnectionProvisioningPojo getVpncp() {
		return this.vpncp;
	}

	private VpncpStatusView getView() {
		return clientFactory.getVpncpStatusView();
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

	public void setVpncp(VpnConnectionProvisioningPojo vpncp) {
		this.vpncp = vpncp;
	}

	@Override
	public void refreshProvisioningStatusForId(final String provisioningId) {
		AsyncCallback<VpnConnectionProvisioningQueryResultPojo> callback = new AsyncCallback<VpnConnectionProvisioningQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().stopTimer();
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving Vpncps", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the VPN Connection status information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(VpnConnectionProvisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + 
						" Vpncps for the filter: " + result.getFilterUsed());
				
				if (result.getResults().size() == 0) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  No VPN Connections found for provisioningId " + provisioningId);
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
					GWT.log("Something weird.  More than one VPN Connection found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server returned more than one VPN Connection "
							+ "for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
									+ "is an unexpected situation that may need "
									+ "to be addressed by system administrators.");
				}
				else {
					// expected behavior
					setVpncpSummary(result.getResults().get(0));
					if (getVpncpSummary().isProvision()) {
						setVpncp(result.getResults().get(0).getProvisioning());
						if (vpncp.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
							getView().stopTimer();
						}
					}
					else {
						setVpncdp(result.getResults().get(0).getDeprovisioning());
						if (vpncdp.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
							getView().stopTimer();
						}
					}
					getView().refreshProvisioningStatusInformation();
	                getView().hidePleaseWaitDialog();
	                getView().hidePleaseWaitPanel();
				}
			}
		};

		GWT.log("[PRESENTER] refreshing Vpncp object for provisioning id:  " + provisioningId);
        getView().showPleaseWaitDialog("Retrieving VPNCP Summaries for the provisioning id: " + provisioningId);
		VpnConnectionProvisioningQueryFilterPojo filter = new VpnConnectionProvisioningQueryFilterPojo();
		filter.setProvisioningId(provisioningId);
		VpcProvisioningService.Util.getInstance().getVpncpSummariesForFilter(filter, callback);
	}

	public VpnConnectionProfilePojo getProfile() {
		return profile;
	}

	public void setProfile(VpnConnectionProfilePojo profile) {
		this.profile = profile;
	}

	@Override
	public boolean isFromGenerate() {
		return this.fromGenerate;
	}

	@Override
	public void setFromGenerate(boolean fromGenerate) {
		this.fromGenerate = fromGenerate;
	}

	public VpnConnectionDeprovisioningPojo getVpncdp() {
		return vpncdp;
	}

	public void setVpncdp(VpnConnectionDeprovisioningPojo vpncdp) {
		this.vpncdp = vpncdp;
	}

	public VpnConnectionProvisioningSummaryPojo getVpncpSummary() {
		return vpncpSummary;
	}

	public void setVpncpSummary(VpnConnectionProvisioningSummaryPojo vpncpSummary) {
		this.vpncpSummary = vpncpSummary;
	}
}
