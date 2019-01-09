package edu.emory.oit.vpcprovisioning.presenter.staticnat;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class StaticNatProvisioningStatusPresenter extends PresenterBase implements StaticNatProvisioningStatusView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private String deprovisioningId;
	private StaticNatProvisioningPojo snp;
	private StaticNatDeprovisioningPojo sndp;
	private StaticNatProvisioningSummaryPojo summary;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new StaticNatProvisioning.
	 */
	public StaticNatProvisioningStatusPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.snp = null;
		this.sndp = null;
		this.provisioningId = null;
		this.deprovisioningId = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public StaticNatProvisioningStatusPresenter(ClientFactory clientFactory, StaticNatProvisioningSummaryPojo summary) {
		this.isEditing = true;
		this.summary = summary;
		if (summary.getProvisioned() != null) {
			this.sndp = null;
			this.deprovisioningId = null;
			this.snp = summary.getProvisioned();
			this.provisioningId = summary.getProvisioned().getProvisioningId();
		}
		else {
			this.snp = null;
			this.provisioningId = null;
			this.sndp = summary.getDeprovisioned();
			this.deprovisioningId = summary.getDeprovisioned().getProvisioningId();
		}
		this.clientFactory = clientFactory;
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
				clientFactory.getShell().setSubTitle("VPCP Status");
				getView().enableButtons();
				getView().setUserLoggedIn(user);
				// TODO: depending on how the scheduler works, we may not need 
				// to refreshStaticNatProvisioningStatusForId here.  If the scheduler immediately 
				// refreshes the delay and then waits, we should just be able 
				// to start the timer and let it do the refresh.
				
				// refresh display with current status
				if (provisioningId != null) {
					refreshProvisioningStatusForId(provisioningId);
				}
				else {
					refreshDeprovisioningStatusForId(deprovisioningId);
				}
				
//				getView().refreshStaticNatProvisioningStatusInformation();
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
		clientFactory.getStaticNatProvisioningStatusView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private StaticNatProvisioningStatusView getView() {
		return clientFactory.getStaticNatProvisioningStatusView();
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

	public void setProvisioning(StaticNatProvisioningPojo vpcp) {
		this.snp = vpcp;
	}
	public void setDeprovisioning(StaticNatDeprovisioningPojo vpcp) {
		this.sndp = vpcp;
	}

	@Override
	public void refreshDeprovisioningStatusForId(final String provisioningId) {
		AsyncCallback<StaticNatDeprovisioningQueryResultPojo> callback = new AsyncCallback<StaticNatDeprovisioningQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().stopTimer();
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving StaticNatDeprovisionings", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the StaticNatDeprovisioning status information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(StaticNatDeprovisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + 
						" StaticNatDeprovisionings for the filter: " + result.getFilterUsed());
				
				if (result.getResults().size() == 0) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  No StaticNatDeprovisioning objects found for provisioningId " + provisioningId);
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
					GWT.log("Something weird.  More than one StaticNatDeprovisioning object found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server returned more than one VStaticNatDeprovisioning object "
							+ "for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
									+ "is an unexpected situation that may need "
									+ "to be addressed by system administrators.");
				}
				else {
					// expected behavior
					setDeprovisioning(result.getResults().get(0));
					if (sndp.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						getView().stopTimer();
					}
					getView().refreshProvisioningStatusInformation();
	                getView().hidePleaseWaitDialog();
	                getView().hidePleaseWaitPanel();
				}
			}
		};

		GWT.log("[PRESENTER] refreshing StaticNatDeprovisioning object for provisioning id:  " + provisioningId);
        getView().showPleaseWaitDialog("Retrieving StaticNatDeprovisioning object for the provisioning id: " + provisioningId);
		StaticNatDeprovisioningQueryFilterPojo filter = new StaticNatDeprovisioningQueryFilterPojo();
		filter.setProvisioningId(provisioningId);
		VpcProvisioningService.Util.getInstance().getStaticNatDeprovisioningsForFilter(filter, callback);
	}

	@Override
	public void refreshProvisioningStatusForId(final String provisioningId) {
		AsyncCallback<StaticNatProvisioningQueryResultPojo> callback = new AsyncCallback<StaticNatProvisioningQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().stopTimer();
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving StaticNatProvisionings", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the StaticNatProvisioning status information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(StaticNatProvisioningQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + 
						" StaticNatProvisionings for the filter: " + result.getFilterUsed());
				
				if (result.getResults().size() == 0) {
					getView().stopTimer();
	                getView().hidePleaseWaitPanel();
	                getView().hidePleaseWaitDialog();
					GWT.log("Something weird.  No StaticNatProvisioning objects found for provisioningId " + provisioningId);
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
					GWT.log("Something weird.  More than one StaticNatProvisioning objects found for provisioningId " + provisioningId);
					getView().showMessageToUser("An unexpected situation has "
							+ "occurred.  The server returned more than one StaticNatProvisioning object "
							+ "for the query specification used "
							+ "(provisioningId=" + provisioningId + ").  This "
									+ "is an unexpected situation that may need "
									+ "to be addressed by system administrators.");
				}
				else {
					// expected behavior
					setProvisioning(result.getResults().get(0));
					if (snp.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						getView().stopTimer();
					}
					getView().refreshProvisioningStatusInformation();
	                getView().hidePleaseWaitDialog();
	                getView().hidePleaseWaitPanel();
				}
			}
		};

		GWT.log("[PRESENTER] refreshing StaticNatProvisioning object for provisioning id:  " + provisioningId);
        getView().showPleaseWaitDialog("Retrieving StaticNatProvisioning object for the provisioning id: " + provisioningId);
		StaticNatProvisioningQueryFilterPojo filter = new StaticNatProvisioningQueryFilterPojo();
		filter.setProvisioningId(provisioningId);
		VpcProvisioningService.Util.getInstance().getStaticNatProvisioningsForFilter(filter, callback);
	}

	@Override
	public void deleteStaticNatProvisioning() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteStaticNatDeprovisioning() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StaticNatProvisioningSummaryPojo getSummary() {
		return this.summary;
	}

	@Override
	public StaticNatProvisioningPojo getProvisioning() {
		return this.snp;
	}

	@Override
	public StaticNatDeprovisioningPojo getDeprovisioning() {
		return this.sndp;
	}
}
