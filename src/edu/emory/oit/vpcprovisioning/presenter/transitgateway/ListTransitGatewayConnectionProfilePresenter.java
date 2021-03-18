package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.TransitGatewayConnectionProfileListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileAssignmentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayStatusQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayStatusQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class ListTransitGatewayConnectionProfilePresenter extends PresenterBase implements ListTransitGatewayConnectionProfileView.Presenter {
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
	
	TransitGatewayConnectionProfileQueryFilterPojo filter;
	VpcPojo vpc;
	TransitGatewayConnectionProfileSummaryPojo selectedSummary;
	TransitGatewayConnectionProfileAssignmentPojo selectedAssignment;
	boolean isAssignmentDelete;
	int selectedRowNumber;
	List<TransitGatewayConnectionProfileSummaryPojo> selectedSummaries;
	TransitGatewayConnectionProfileAssignmentRequisitionPojo selectedRequisition;
	String selectedVpcId;
	UserAccountPojo userLoggedIn;
	List<TransitGatewayConnectionProfileSummaryPojo> profileSummaryList = new java.util.ArrayList<TransitGatewayConnectionProfileSummaryPojo>();

	boolean showStatus = false;
	boolean startTimer = true;
	int deletedCount;
	int totalToDelete;
	StringBuffer deleteErrors;
	private boolean editing = false;

	public ListTransitGatewayConnectionProfilePresenter(ClientFactory clientFactory, boolean clearList, TransitGatewayConnectionProfileQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.filter = filter;
		clientFactory.getListTransitGatewayConnectionProfileView().setPresenter(this);
		this.editing = false;
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListTransitGatewayConnectionProfilePresenter(ClientFactory clientFactory, ListTransitGatewayConnectionProfilePlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListTransitGatewayConnectionProfileView getView() {
		return clientFactory.getListTransitGatewayConnectionProfileView();
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
	public void selectTransitGatewayConnectionProfile(TransitGatewayConnectionProfilePojo selected) {
		
		
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public TransitGatewayConnectionProfileQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(TransitGatewayConnectionProfileQueryFilterPojo filter) {
		this.filter = filter;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteTransitGatewayConnectionProfile(TransitGatewayConnectionProfileSummaryPojo selected) {
		selectedSummary = selected;
		VpcpConfirm.confirm(
			ListTransitGatewayConnectionProfilePresenter.this, 
			"Confirm Delete Transit Gateway Connection Profile", 
			"Delete the Transit Gateway Connection Profile " + selectedSummary.getProfile().getCidrId() + "?");
	}

	/**
	 * Refresh the CIDR list.
	 */
	public void refreshList(final UserAccountPojo user) {
		getView().showPleaseWaitDialog("Retrieving Transit Gateway Connection Profiles from the Network OPS service...");
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<TransitGatewayConnectionProfileQueryResultPojo> callback = new AsyncCallback<TransitGatewayConnectionProfileQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Transit Gateway Connection Profiles.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(TransitGatewayConnectionProfileQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Transit Gateway Connection Profiles for " + result.getFilterUsed());
				profileSummaryList = result.getResults();
				setTransitGatewayConnectionProfileSummaryList(profileSummaryList);
				
				int totalProfiles = result.getResults().size();
				int assignedProfiles = 0;
				int unassignedProfiles = 0;
				for (TransitGatewayConnectionProfileSummaryPojo summary : result.getResults()) {
					if (summary.getAssignment() != null) {
						assignedProfiles++;
					}
					else {
						unassignedProfiles++;
					}
				}
				StringBuffer sbuf = new StringBuffer();
				sbuf.append("There are " + totalProfiles + " total VPN Connection Profiles listed below.</br>");
				sbuf.append(assignedProfiles + " profiles are already assigned.</br>");
				sbuf.append(unassignedProfiles + " profiles are unassigned.");
				getView().setProfileSummaryHTML(sbuf.toString());
				
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
		VpcProvisioningService.Util.getInstance().getTransitGatewayConnectionProfilesForFilter(filter, callback);
	}

	private void setTransitGatewayConnectionProfileSummaryList(List<TransitGatewayConnectionProfileSummaryPojo> list) {
		getView().setTransitGatewayConnectionProfileSummaries(list);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new TransitGatewayConnectionProfileListUpdateEvent(list), this);
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
//		getView().showMessageToUser("Coming soon");
//		if (true) {
//			return;
//		}
		if (selectedSummaries != null) {
			// TGW Connection Profile Delete
			showStatus = false;
			deletedCount = 0;
			totalToDelete = selectedSummaries.size();
			deleteErrors = new StringBuffer();
			for (int i=0; i<selectedSummaries.size(); i++) {
				final TransitGatewayConnectionProfileSummaryPojo summary = selectedSummaries.get(i);
				final int listCounter = i;
				
				AsyncCallback<TransitGatewayConnectionProfilePojo> callback = new AsyncCallback<TransitGatewayConnectionProfilePojo>() {
					@Override
					public void onFailure(Throwable caught) {
						deleteErrors.append("There was an exception on the " +
								"server deleting the Transit Gateway Connection Profile (id=" + summary.getProfile().getTransitGatewayConnectionProfileId() + ").  " +
								"<p>Message from server is: " + caught.getMessage() + "</p>");
						if (!showStatus) {
							deleteErrors.append("\n");
						}
						if (listCounter == totalToDelete - 1) {
							showStatus = true;
						}
					}
		
					@Override
					public void onSuccess(TransitGatewayConnectionProfilePojo result) {
						deletedCount++;
						if (listCounter == totalToDelete - 1) {
							showStatus = true;
						}
					}
				};
				VpcProvisioningService.Util.getInstance().deleteTransitGatewayConnectionProfile(summary.getProfile(), callback);
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
		else if (selectedAssignment != null) {
			// TGW profile assignment delete
			getView().showPleaseWaitDialog("Deleting TGW profile assignment VPC " + selectedAssignment.getOwnerId());
			AsyncCallback<TransitGatewayConnectionProfileAssignmentPojo> de_cb = new AsyncCallback<TransitGatewayConnectionProfileAssignmentPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					getView().hidePleaseWaitDialog();
					GWT.log("Exception generating the TransitGatewayConnectionDeprovisioning", caught);
					getView().showMessageToUser("There was an exception on the " +
							"server deleting the TransitGatewayConnectionProfileAssignment.  Message " +
							"from server is: " + caught.getMessage());
				}

				@Override
				public void onSuccess(TransitGatewayConnectionProfileAssignmentPojo result) {
					refreshList(userLoggedIn);
					getView().hidePleaseWaitDialog();
					getView().showMessageToUser("TGW Profile assignment was "
						+ "deleted and the associated profile is now available "
						+ "for use again.");
				}
			};
			VpcProvisioningService.Util.getInstance().deleteTransitGatewayConnectionProfileAssignment(selectedAssignment, de_cb);
		}
	}

	@Override
	public void vpcpConfirmCancel() {
		if (selectedSummaries != null) {
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  TGW Connection Profile was NOT deleted");
		}
		else if (selectedAssignment != null) {
			// is a de-provision
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  TGW Connection Assignment was NOT deleted");
		}
	}

	@Override
	public void deleteTransitGatewayConnectionProfiles(List<TransitGatewayConnectionProfileSummaryPojo> summaries) {
		selectedRequisition = null;
		selectedSummaries = summaries;
		VpcpConfirm.confirm(
			ListTransitGatewayConnectionProfilePresenter.this, 
			"Confirm Delete Transit Gateway Connection Profile", 
			"Delete the selected " + selectedSummaries.size() + " VPN Connection Profiles?");
	}

	void showDeleteListStatus() {
		if (deleteErrors.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showStatus(null, deletedCount + " out of " + totalToDelete + " Transit Gateway Connection Profile(s) were deleted.");
		}
		else {
			getView().hidePleaseWaitDialog();
			deleteErrors.insert(0, deletedCount + " out of " + totalToDelete + " Transit Gateway Connection Profile(s) were deleted.  "
				+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(deleteErrors.toString());
		}
	}

	@Override
	public void setSelectedSummaries(List<TransitGatewayConnectionProfileSummaryPojo> summaries) {
		selectedSummaries = summaries;
	}

	@Override
	public void filterByVpcAddress(String vpcAddress) {
		getView().showMessageToUser("Comming soon");
//		GWT.log("vpn connection profile filtering by vpc address");
//		if (vpcAddress == null || vpcAddress.length() == 0) {
//			getView().hidePleaseWaitDialog();
//			getView().showMessageToUser("Please enter a VPC Network");
//			return;
//		}
//
//		getView().showFilteredStatus();
//        getView().hidePleaseWaitDialog();
//		getView().showPleaseWaitDialog("Filtering list by VPC Network " + vpcAddress + "...");
//		
//		filter = new TransitGatewayConnectionProfileQueryFilterPojo();
//		filter.setVpcNetwork(vpcAddress);
//		
//		refreshList(userLoggedIn);
	}


	@Override
	public void filterByCidrId(String cidrId) {
		getView().showMessageToUser("Comming soon");
	}

	@Override
	public void filterByRegion(String region) {
		getView().showMessageToUser("Comming soon");
	}

	@Override
	public void filterByTransitGatewayId(String tgwId) {
		getView().showMessageToUser("Comming soon");
	}

	@Override
	public void filterByCidrRange(String cidrRange) {
		getView().showMessageToUser("Comming soon");
	}

	@Override
	public void clearFilter() {
		this.filter = null;
	}

//	@Override
//	public void deprovisionTransitGatewayConnection(TransitGatewayConnectionRequisitionPojo vpnConnectionRequisition) {
//		selectedSummaries = new java.util.ArrayList<TransitGatewayConnectionProfileSummaryPojo>();
//		selectedTransitGatewayConnectionRequisition = vpnConnectionRequisition;
//		
//		VpcpConfirm.confirm(
//				ListTransitGatewayConnectionProfilePresenter.this, 
//				"Confirm Deprovision VPN Connection", 
//				"Deprovisiong the VPN Connection " + selectedTransitGatewayConnectionRequisition.getProfile().getVpcNetwork() + "?");
//
//	}

//	@Override
//	public void deprovisionTransitGatewayConnectionForVpcId(String vpcId) {
//		selectedSummary = null;
//		selectedSummaries = null;
//		selectedTransitGatewayConnectionRequisition = null;
//		selectedVpcId = vpcId;
//		selectedAssignment = null;
//
//		VpcpConfirm.confirm(
//				ListTransitGatewayConnectionProfilePresenter.this, 
//				"Confirm Deprovision VPN Connection", 
//				"Deprovisiong the VPN Connection for VPC " + selectedVpcId + "?");
//	}

	@Override
	public void setSelectedAssignment(TransitGatewayConnectionProfileAssignmentPojo assignment) {
		this.selectedAssignment = assignment;
	}

	@Override
	public void deprovisionTransitGatewayConnectionForAssignment(TransitGatewayConnectionProfileAssignmentPojo assignment) {
		selectedSummary = null;
		selectedSummaries = null;
		selectedRequisition = null;
		selectedVpcId = null;
		selectedAssignment = assignment;
		isAssignmentDelete = false;

		VpcpConfirm.confirm(
				ListTransitGatewayConnectionProfilePresenter.this, 
				"Confirm Deprovision VPN Connection", 
				"Deprovisiong the VPN Connection for VPC " + selectedAssignment.getOwnerId() + "?");
	}

	@Override
	public void deleteTransitGatewayConnectionProfileAssignment(final int rowNumber, final TransitGatewayConnectionProfileSummaryPojo summary) {
		selectedSummary = summary;
		selectedSummaries = null;
		selectedRequisition = null;
		selectedVpcId = null;
		selectedAssignment = summary.getAssignment();
		isAssignmentDelete = true;
		selectedRowNumber = rowNumber;
		GWT.log("[deleteTransitGatewayConnectionProfileAssignment] selectedRowNumber is: " + rowNumber);
		VpcpConfirm.confirm(
				ListTransitGatewayConnectionProfilePresenter.this, 
				"Confirm Delete VPN Connection Profile Assignment", 
				"Delete the Profile Assignment from Profile ID " + summary.getProfile().getTransitGatewayConnectionProfileId() + "?");
	}

	@Override
	public void getTransitGatewayStatusForVpc(String vpcId) {
		getView().showPleaseWaitDialog("Retrieving Transit Gateway Information");
		AsyncCallback<TransitGatewayStatusQueryResultPojo> cb = new AsyncCallback<TransitGatewayStatusQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(TransitGatewayStatusQueryResultPojo result) {
				getView().hidePleaseWaitDialog();
				if (result.getResults().size() > 0) {
					getView().showMessageToUser(result.getResults().get(0).toHTML());
				}
				else {
					getView().showMessageToUser("There are no Transit Gateways associated to this profile");
				}
			}
		};
		TransitGatewayStatusQueryFilterPojo filter = new TransitGatewayStatusQueryFilterPojo();
		filter.setVpcId(vpcId);
		VpcProvisioningService.Util.getInstance().getTransitGatewayStatusForFilter(filter, cb);
	}

	@Override
	public void addEmptySummaryToList() {
		this.editing = false;
		TransitGatewayConnectionProfileSummaryPojo summary = new TransitGatewayConnectionProfileSummaryPojo();
		TransitGatewayConnectionProfilePojo profile = new TransitGatewayConnectionProfilePojo();
		int nextProfileId = profileSummaryList.size() + 1;
		profile.setTransitGatewayConnectionProfileId(Integer.toString(nextProfileId));
		summary.setProfile(profile);
		profileSummaryList.add(0, summary);
		setTransitGatewayConnectionProfileSummaryList(profileSummaryList);
	}

	@Override
	public void saveProfile(TransitGatewayConnectionProfilePojo profile) {
		getView().showPleaseWaitDialog("Saving Transit Gateway info...");
//		if (!isFormValid()) {
//			return;
//		}
		AsyncCallback<TransitGatewayConnectionProfilePojo> callback = new AsyncCallback<TransitGatewayConnectionProfilePojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the TransitGatewayConnectionProfilePojo", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the TransitGatewayConnectionProfilePojo.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(TransitGatewayConnectionProfilePojo result) {
				getView().hidePleaseWaitDialog();
//				ActionEvent.fire(eventBus, ActionNames.TRANSIT_GATEWAY_SAVED, result);
			}
		};
		if (!isEditing()) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createTransitGatewayConnectionProfile(profile, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateTransitGatewayConnectionProfile(profile, callback);
		}
	}

	@Override
	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	@Override
	public boolean isEditing() {
		return editing;
	}
}
