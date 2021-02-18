package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.TransitGatewayListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class ListTransitGatewayPresenter extends PresenterBase implements ListTransitGatewayView.Presenter {
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
	
	TransitGatewayQueryFilterPojo filter;
	VpcPojo vpc;
	List<TransitGatewayPojo> selectedTransitGateways = new java.util.ArrayList<TransitGatewayPojo>();
	TransitGatewayPojo selectedTransitGateway;
	boolean isAssignmentDelete;
	int selectedRowNumber;
//	VpnConnectionRequisitionPojo selectedVpnConnectionRequisition;
	String selectedVpcId;
	UserAccountPojo userLoggedIn;

	boolean showStatus = false;
	boolean startTimer = true;
	int deletedCount;
	int totalToDelete;
	StringBuffer deleteErrors;

	public ListTransitGatewayPresenter(ClientFactory clientFactory, boolean clearList, TransitGatewayQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.filter = filter;
		clientFactory.getListTransitGatewayView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListTransitGatewayPresenter(ClientFactory clientFactory, ListTransitGatewayPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListTransitGatewayView getView() {
		return clientFactory.getListTransitGatewayView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving User Logged In...");
		
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectTransitGateway(TransitGatewayPojo selected) {
		
		this.selectedTransitGateway = selected;
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public TransitGatewayQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(TransitGatewayQueryFilterPojo filter) {
		this.filter = filter;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

//	@Override
//	public void deleteTransitGateway(TransitGatewayPojo selected) {
//		selectedTransitGateway = selected;
//		VpcpConfirm.confirm(
//			ListTransitGatewayPresenter.this, 
//			"Confirm Delete VPN Connection Profile", 
//			"Delete the VPN Connection Profile " + selectedSummary.getProfile().getVpcNetwork() + "?");
//	}

	/**
	 * Refresh the transit gateway list.
	 */
	public void refreshList(final UserAccountPojo user) {
		getView().showPleaseWaitDialog("Retrieving Transit Gateway metadata from the Network OPS service...");
		AsyncCallback<TransitGatewayQueryResultPojo> callback = new AsyncCallback<TransitGatewayQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of VPN Connection Profiles.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(TransitGatewayQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " VPN Connection Profiles for " + result.getFilterUsed());
				setTransitGatewayList(result.getResults());
				
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
		VpcProvisioningService.Util.getInstance().getTransitGatewaysForFilter(filter, callback);
	}

	private void setTransitGatewayList(List<TransitGatewayPojo> list) {
		getView().setTransitGateways(list);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new TransitGatewayListUpdateEvent(list), this);
		}
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	@Override
	public void vpcpConfirmOkay() {
	}

	@Override
	public void vpcpConfirmCancel() {
//		if (selectedSummaries != null) {
//			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN Connection Profile was NOT deleted");
//		}
//		else if (selectedVpcId != null){
//			// is a de-provision
//			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN Connection was NOT de-provisioned");
//		}
//		else if (selectedAssignment != null) {
//			// is a de-provision
//			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN Connection was NOT de-provisioned");
//		}
	}

	@Override
	public void deleteTransitGateways(List<TransitGatewayPojo> pojos) {
		selectedTransitGateways = pojos;
		VpcpConfirm.confirm(
			ListTransitGatewayPresenter.this, 
			"Confirm Delete Transit Gateway", 
			"Delete the selected " + pojos.size() + " Transit Gateways?");
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
	public void filterByEnvironment(String environment) {
		if (environment == null || environment.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please enter an Environment");
			return;
		}

		getView().showFilteredStatus();
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Filtering list by Environment " + environment + "...");
		
		filter = new TransitGatewayQueryFilterPojo();
		filter.setEnvironment(environment);
		
		refreshList(userLoggedIn);
	}

	@Override
	public void filterByRegion(String region) {
		if (region == null || region.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please enter a Region");
			return;
		}

		getView().showFilteredStatus();
        getView().hidePleaseWaitDialog();
		getView().showPleaseWaitDialog("Filtering list by region " + region + "...");
		
		filter = new TransitGatewayQueryFilterPojo();
		filter.setRegion(region);
		
		refreshList(userLoggedIn);
	}

	@Override
	public void clearFilter() {
		this.filter = null;
	}

	@Override
	public void filterByTransitGatewayId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filterByAssociationRouteTableId(String id) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void deprovisionVpnConnection(VpnConnectionRequisitionPojo vpnConnectionRequisition) {
//		selectedSummaries = new java.util.ArrayList<TransitGatewaySummaryPojo>();
//		selectedVpnConnectionRequisition = vpnConnectionRequisition;
//		
//		VpcpConfirm.confirm(
//				ListTransitGatewayPresenter.this, 
//				"Confirm Deprovision VPN Connection", 
//				"Deprovisiong the VPN Connection " + selectedVpnConnectionRequisition.getProfile().getVpcNetwork() + "?");
//
//	}

//	@Override
//	public void deprovisionVpnConnectionForVpcId(String vpcId) {
//		selectedSummary = null;
//		selectedSummaries = null;
//		selectedVpnConnectionRequisition = null;
//		selectedVpcId = vpcId;
//		selectedAssignment = null;
//
//		VpcpConfirm.confirm(
//				ListTransitGatewayPresenter.this, 
//				"Confirm Deprovision VPN Connection", 
//				"Deprovisiong the VPN Connection for VPC " + selectedVpcId + "?");
//	}

}
