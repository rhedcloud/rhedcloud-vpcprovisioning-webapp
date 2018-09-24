package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.VpnConnectionProfileListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;

public class ListVpnConnectionProfilePresenter extends PresenterBase implements ListVpnConnectionProfileView.Presenter {
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
	
	VpnConnectionProfileQueryFilterPojo filter;
	VpcPojo vpc;
	VpnConnectionProfileSummaryPojo selectedSummary;
	List<VpnConnectionProfileSummaryPojo> selectedSummaries;
	UserAccountPojo userLoggedIn;

	boolean showStatus = false;
	boolean startTimer = true;
	int deletedCount;
	int totalToDelete;
	StringBuffer deleteErrors;

	public ListVpnConnectionProfilePresenter(ClientFactory clientFactory, boolean clearList, VpnConnectionProfileQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.filter = filter;
		clientFactory.getListVpnConnectionProfileView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListVpnConnectionProfilePresenter(ClientFactory clientFactory, ListVpnConnectionProfilePlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListVpnConnectionProfileView getView() {
		return clientFactory.getListVpnConnectionProfileView();
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
		getView().showPleaseWaitDialog("Retrieving VPN Connection Profiles from the Network OPS service...");
		
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
				clientFactory.getShell().setSubTitle("VPN Connection Profiles");
				getView().initPage();

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);

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
	public void selectVpnConnectionProfile(VpnConnectionProfilePojo selected) {
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
	public VpnConnectionProfileQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpnConnectionProfileQueryFilterPojo filter) {
		this.filter = filter;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteVpnConnectionProfile(VpnConnectionProfileSummaryPojo selected) {
		selectedSummary = selected;
		VpcpConfirm.confirm(
			ListVpnConnectionProfilePresenter.this, 
			"Confirm Delete VPN Connection Profile", 
			"Delete the VPN Connection Profile " + selectedSummary.getProfile().getVpcNetwork() + "?");
	}

	/**
	 * Refresh the CIDR list.
	 */
	public void refreshList(final UserAccountPojo user) {
		getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Retrieving VPN Connection Profiles from the Network OPS service...");
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<VpnConnectionProfileQueryResultPojo> callback = new AsyncCallback<VpnConnectionProfileQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of VPN Connection Profiles.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(VpnConnectionProfileQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " VPN Connection Profiles for " + result.getFilterUsed());
				setVpnConnectionProfileSummaryList(result.getResults());
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

		GWT.log("refreshing VPN Connection Profile list...");
		VpcProvisioningService.Util.getInstance().getVpnConnectionProfilesForFilter(filter, callback);
	}

	private void setVpnConnectionProfileSummaryList(List<VpnConnectionProfileSummaryPojo> list) {
		getView().setVpnConnectionProfileSummaries(list);
		eventBus.fireEventFromSource(new VpnConnectionProfileListUpdateEvent(list), this);
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
			final VpnConnectionProfileSummaryPojo summary = selectedSummaries.get(i);
			final int listCounter = i;
			
			AsyncCallback<VpnConnectionProfilePojo> callback = new AsyncCallback<VpnConnectionProfilePojo>() {
				@Override
				public void onFailure(Throwable caught) {
					deleteErrors.append("There was an exception on the " +
							"server deleting the VPN Connection Profile (id=" + summary.getProfile().getVpnConnectionProfileId() + ").  " +
							"<p>Message from server is: " + caught.getMessage() + "</p>");
					if (!showStatus) {
						deleteErrors.append("\n");
					}
					if (listCounter == totalToDelete - 1) {
						showStatus = true;
					}
				}
	
				@Override
				public void onSuccess(VpnConnectionProfilePojo result) {
					deletedCount++;
					if (listCounter == totalToDelete - 1) {
						showStatus = true;
					}
				}
			};
			VpcProvisioningService.Util.getInstance().deleteVpnConnectionProfile(summary.getProfile(), callback);
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
	}

	@Override
	public void vpcpConfirmCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteVpnConnectionProfiles(List<VpnConnectionProfileSummaryPojo> summaries) {
		selectedSummaries = summaries;
		VpcpConfirm.confirm(
			ListVpnConnectionProfilePresenter.this, 
			"Confirm Delete VPN Connection Profile", 
			"Delete the selected " + selectedSummaries.size() + " VPN Connection Profiles?");
	}

	void showDeleteListStatus() {
		if (deleteErrors.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showStatus(null, deletedCount + " out of " + totalToDelete + " VPN Connection Profile(s) were deleted.");
		}
		else {
			getView().hidePleaseWaitDialog();
			deleteErrors.insert(0, deletedCount + " out of " + totalToDelete + " VPN Connection Profile(s) were deleted.  "
				+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(deleteErrors.toString());
		}
	}

	@Override
	public void setSelectedSummaries(List<VpnConnectionProfileSummaryPojo> summaries) {
		selectedSummaries = summaries;
	}

	@Override
	public void filterByVpcAddress(String vpcAddress) {
		GWT.log("vpn connection profile filtering by vpc address");
		if (vpcAddress == null || vpcAddress.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please enter a VPC Network");
			return;
		}

		getView().showFilteredStatus();
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Filtering list by VPC Network " + vpcAddress + "...");
		
		filter = new VpnConnectionProfileQueryFilterPojo();
		filter.setVpcNetwork(vpcAddress);
		
		refreshList(userLoggedIn);
	}

	@Override
	public void filterByVpnConnectionProfileId(String profileId) {
		GWT.log("vpn connection profile filtering by profile id");
		if (profileId == null || profileId.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please enter a Profile ID");
			return;
		}

		getView().showFilteredStatus();
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Filtering list by Profile ID " + profileId + "...");
		
		filter = new VpnConnectionProfileQueryFilterPojo();
		filter.setVpnConnectionProfileId(profileId);
		
		refreshList(userLoggedIn);
	}

	@Override
	public void clearFilter() {
		this.filter = null;
	}
}
