package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.VpnConnectionProfileListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
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
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("VPN Connection Profiles");

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
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<VpnConnectionProfileQueryResultPojo> callback = new AsyncCallback<VpnConnectionProfileQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of VPN Connection Profiles.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpnConnectionProfileQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " VPN Connection Profiles for " + result.getFilterUsed());
				setVpnConnectionProfileSummaryList(result.getResults());
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (user.isNetworkAdmin()) {
					getView().applyNetworkAdminMask();
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
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server deleting the VPC metadata.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(Void result) {
				// remove from dataprovider
				getView().removeVpnConnectionProfileSummaryFromView(selectedSummary);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "VPN Connection Profile was deleted.");
			}
		};
		VpcProvisioningService.Util.getInstance().deleteVpnConnectionProfile(selectedSummary.getProfile(), callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteVpnConnectionProfiles(List<VpnConnectionProfileSummaryPojo> summaries) {
		// TODO Auto-generated method stub
		
	}

	void showDeleteListStatus(int createdCount, int totalToCreate, StringBuffer errors) {
		if (errors.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showStatus(null, createdCount + " out of " + totalToCreate + " VPN Connection Profile(s) were created.");
		}
		else {
			getView().hidePleaseWaitDialog();
			errors.insert(0, createdCount + " out of " + totalToCreate + " VPN Connection Profile(s) were created.  "
				+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(errors.toString());
		}
	}

	@Override
	public void setSelectedSummaries(List<VpnConnectionProfileSummaryPojo> summaries) {
		selectedSummaries = summaries;
	}

	@Override
	public void filterByVpcAddress(String vpcAddress) {
		// TODO Auto-generated method stub
		
	}
}
