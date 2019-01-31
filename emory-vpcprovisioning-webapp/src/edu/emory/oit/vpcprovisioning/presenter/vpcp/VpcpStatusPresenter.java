package edu.emory.oit.vpcprovisioning.presenter.vpcp;

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
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryResultPojo;

public class VpcpStatusPresenter extends PresenterBase implements VpcpStatusView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private VpcpPojo vpcp;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new Vpcp.
	 */
	public VpcpStatusPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpcp = null;
		this.provisioningId = null;
		this.clientFactory = clientFactory;
		clientFactory.getVpcpStatusView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public VpcpStatusPresenter(ClientFactory clientFactory, VpcpPojo vpcp) {
		this.isEditing = true;
		this.provisioningId = vpcp.getProvisioningId();
		this.clientFactory = clientFactory;
		this.vpcp = vpcp;
		clientFactory.getVpcpStatusView().setPresenter(this);
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
				clientFactory.getShell().setSubTitle("VPCP Status");
				getView().enableButtons();
				getView().setUserLoggedIn(user);
				// TODO: depending on how the scheduler works, we may not need 
				// to refreshVpcpStatusForId here.  If the scheduler immediately 
				// refreshes the delay and then waits, we should just be able 
				// to start the timer and let it do the refresh.
				
				// refresh display with current status
				refreshVpcpStatusForId(provisioningId);
				
//				getView().refreshVpcpStatusInformation();
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
		clientFactory.getVpcpStatusView().setLocked(false);
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
	public void deleteVpcp() {
		if (isEditing) {
			doDeleteVpcp();
		} else {
			doCancelVpcp();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelVpcp() {
		ActionEvent.fire(eventBus, ActionNames.VPCP_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteVpcp() {
		if (vpcp == null) {
			return;
		}

		// TODO Delete the vpcp on server then fire onVpcpDeleted();
	}

	@Override
	public void saveVpcp() {
//		getView().showPleaseWaitDialog();
	}

	@Override
	public VpcpPojo getVpcp() {
		return this.vpcp;
	}

	@Override
	public boolean isValidVpcpId(String value) {
		
		return false;
	}

	private VpcpStatusView getView() {
		return clientFactory.getVpcpStatusView();
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

	public void setVpcp(VpcpPojo vpcp) {
		this.vpcp = vpcp;
	}

	@Override
	public void refreshVpcpStatusForId(final String provisioningId) {
		AsyncCallback<VpcpQueryResultPojo> callback = new AsyncCallback<VpcpQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().stopTimer();
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving Vpcps", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the VPCP status information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(VpcpQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + 
						" Vpcps for the filter: " + result.getFilterUsed());
				
				if (result.getResults().size() == 0) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  No VPCPs found for provisioningId " + provisioningId);
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
					GWT.log("Something weird.  More than one VPCP found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server returned more than one VPCP "
							+ "for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
									+ "is an unexpected situation that may need "
									+ "to be addressed by system administrators.");
				}
				else {
					// expected behavior
					setVpcp(result.getResults().get(0));
					if (vpcp.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						getView().stopTimer();
					}
					getView().refreshVpcpStatusInformation();
	                getView().hidePleaseWaitDialog();
	                getView().hidePleaseWaitPanel();
				}
			}
		};

		GWT.log("[PRESENTER] refreshing Vpcp object for provisioning id:  " + provisioningId);
        getView().showPleaseWaitDialog("Retrieving VPCs for the provisioning id: " + provisioningId);
		VpcpQueryFilterPojo filter = new VpcpQueryFilterPojo();
		filter.setProvisioningId(provisioningId);
		VpcProvisioningService.Util.getInstance().getVpcpsForFilter(filter, callback);
	}

	@Override
	public void setSpeedChartStatusForKeyOnWidget(final String speedChartNumber, final Widget w) {
		AsyncCallback<SpeedChartPojo> callback = new AsyncCallback<SpeedChartPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server exception validating speedtype", caught);
				w.setTitle("Server exception validating speedtype");
			}

			@Override
			public void onSuccess(SpeedChartPojo scp) {
				if (scp == null) {
					w.setTitle("Invalid account number (" + speedChartNumber + "), can't validate this number");
					w.getElement().getStyle().setBackgroundColor("#efbebe");
				}
				else {
					String deptId = scp.getDepartmentId();
					String deptDesc = scp.getDepartmentDescription();
					String desc = scp.getDescription();
				    String euValidityDesc = scp.getEuValidityDescription();
				    String statusDescString = euValidityDesc + "\n" + 
				    		deptId + " | " + deptDesc + "\n" +
				    		desc;
					w.setTitle(statusDescString);
					if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_VALID)) {
						w.getElement().getStyle().setBackgroundColor(null);
					}
					else if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_INVALID)) {
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_INVALID_FIELD);
					}
					else {
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_FIELD_WARNING);
					}
				}
			}
		};
		if (speedChartNumber != null && speedChartNumber.length() > 0) {
			VpcProvisioningService.Util.getInstance().getSpeedChartForFinancialAccountNumber(speedChartNumber, callback);
		}
		else {
			GWT.log("null key, can't validate yet");
		}
	}
}
