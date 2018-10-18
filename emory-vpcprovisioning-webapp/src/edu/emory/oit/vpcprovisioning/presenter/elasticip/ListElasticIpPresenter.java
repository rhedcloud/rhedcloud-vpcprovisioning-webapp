package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ElasticIpListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class ListElasticIpPresenter extends PresenterBase implements ListElasticIpView.Presenter {
	private static final Logger log = Logger.getLogger(ListElasticIpPresenter.class.getName());
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
	
	ElasticIpQueryFilterPojo filter;
	VpcPojo vpc;
	ElasticIpSummaryPojo selectedSummary;
	List<ElasticIpSummaryPojo> selectedSummaries;
	UserAccountPojo userLoggedIn;

	boolean showStatus = false;
	boolean startTimer = true;
	int deletedCount;
	int totalToDelete;
	StringBuffer deleteErrors;

	public ListElasticIpPresenter(ClientFactory clientFactory, boolean clearList, ElasticIpQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.filter = filter;
		clientFactory.getListElasticIpView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListElasticIpPresenter(ClientFactory clientFactory, ListElasticIpPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListElasticIpView getView() {
		return clientFactory.getListElasticIpView();
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving Elastic IPs from the Elastic IP service...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Elastic IPs");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(user);

				// Request the Vpc list now.
				refreshList(user);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
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
	public void selectElasticIp(ElasticIpPojo selected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public ElasticIpQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(ElasticIpQueryFilterPojo filter) {
		this.filter = filter;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteElasticIp(ElasticIpSummaryPojo selected) {
		selectedSummary = selected;
		VpcpConfirm.confirm(
			ListElasticIpPresenter.this, 
			"Confirm Delete Elastic IP", 
			"Delete the Elastic IP " + selectedSummary.getElasticIp().getElasticIpAddress() + "?");
	}

	/**
	 * Refresh the CIDR list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<ElasticIpQueryResultPojo> callback = new AsyncCallback<ElasticIpQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
				log.log(Level.SEVERE, "Exception Retrieving Elastic IPs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Elastic IPs.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(ElasticIpQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " ElasticIPs for " + result.getFilterUsed());
				setElasticIpSummaryList(result.getResults());
				// apply authorization mask
				if (user.isNetworkAdmin()) {
					getView().applyNetworkAdminMask();
				}
				else if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
			}
		};

		GWT.log("refreshing ElasticIP list...");
		VpcProvisioningService.Util.getInstance().getElasticIpsForFilter(filter, callback);
	}

	private void setElasticIpSummaryList(List<ElasticIpSummaryPojo> list) {
		getView().setElasticIpSummaries(list);
		eventBus.fireEventFromSource(new ElasticIpListUpdateEvent(list), this);
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	@Override
	public void vpcpConfirmOkay() {
		showStatus = false;
		deletedCount = 0;
		totalToDelete = selectedSummaries.size();
		deleteErrors = new StringBuffer();
		for (int i=0; i<selectedSummaries.size(); i++) {
			final ElasticIpSummaryPojo summary = selectedSummaries.get(i);
			final int listCounter = i;

			AsyncCallback<ElasticIpPojo> callback = new AsyncCallback<ElasticIpPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					deleteErrors.append("There was an exception on the " +
							"server deleting the ElasticIP (" + summary.getElasticIp().getElasticIpAddress() + ").  " +
							"<p>Message from server is: " + caught.getMessage() + "</p>");
					if (!showStatus) {
						deleteErrors.append("\n");
					}
					if (listCounter == totalToDelete - 1) {
						showStatus = true;
					}
				}
	
				@Override
				public void onSuccess(ElasticIpPojo result) {
					deletedCount++;
					if (listCounter == totalToDelete - 1) {
						showStatus = true;
					}
				}
			};
			VpcProvisioningService.Util.getInstance().deleteElasticIp(summary.getElasticIp(), callback);
		}
		if (!showStatus) {
			// wait for all the creates to finish processing
			int delayMs = 500;
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
				@Override
				public boolean execute() {
					if (showStatus) {
						startTimer = false;
						showDeleteListStatus();
					}
					return startTimer;
				}
			}, delayMs);
		}
		else {
			showDeleteListStatus();
		}
		refreshList(userLoggedIn);
	}

	@Override
	public void vpcpConfirmCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteElasticIps(List<ElasticIpSummaryPojo> summaries) {
		selectedSummaries = summaries;
		VpcpConfirm.confirm(
			ListElasticIpPresenter.this, 
			"Confirm Delete Elastic IP", 
			"Delete the selected " + selectedSummaries.size() + " Elastic IPs?");
	}

	void showDeleteListStatus() {
		if (deleteErrors.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showStatus(null, deletedCount + " out of " + totalToDelete + " ElasticIP(s) were deleted.");
		}
		else {
			getView().hidePleaseWaitDialog();
			deleteErrors.insert(0, deletedCount + " out of " + totalToDelete + " ElasticIP(s) were deleted.  "
				+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(deleteErrors.toString());
		}
	}

	@Override
	public void setSelectedSummaries(List<ElasticIpSummaryPojo> summaries) {
		selectedSummaries = summaries;
	}
}
