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
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.TransitGatewayConnectionProfileListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileAssignmentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
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
		getView().showMessageToUser("Coming soon");
		if (true) {
			return;
		}
		if (selectedSummaries != null) {
			// VPN Connection Profile Delete
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
		else if (selectedRequisition != null){
			// NOTE:  this isn't used any more and we shouldn't get here
			// is a VPN Connection de-provision (generate)
//			AsyncCallback<TransitGatewayConnectionDeprovisioningPojo> callback = new AsyncCallback<TransitGatewayConnectionDeprovisioningPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//					getView().hidePleaseWaitDialog();
//					GWT.log("Exception generating the TransitGatewayConnectionDeprovisioning", caught);
//					getView().showMessageToUser("There was an exception on the " +
//							"server generating the TransitGatewayConnectionDeprovisioning.  Message " +
//							"from server is: " + caught.getMessage());
//				}
//
//				@Override
//				public void onSuccess(TransitGatewayConnectionDeprovisioningPojo result) {
//					getView().hidePleaseWaitDialog();
//					// it was a generate, we'll take them to the VPNCP status view
//					final TransitGatewayConnectionDeprovisioningPojo vpncdp = result;
//					GWT.log("VPNCDP was generated on the server, showing status page.  "
//							+ "VPNCDP is: " + vpncdp);
//					TransitGatewayConnectionProvisioningSummaryPojo vpncpSummary = new TransitGatewayConnectionProvisioningSummaryPojo();
//					vpncpSummary.setDeprovisioning(vpncdp);
//					ActionEvent.fire(eventBus, ActionNames.VPNCDP_GENERATED, vpncpSummary);
//				}
//			};
//			getView().showPleaseWaitDialog("Generating VPC Deprovisioning object...");
//			VpcProvisioningService.Util.getInstance().generateTransitGatewayConnectionDeprovisioning(selectedTransitGatewayConnectionRequisition, callback);
		}
		else if (selectedVpcId != null) {
			// NOTE:  this isn't used any more and we shouldn't get here
//			getView().showPleaseWaitDialog("Deprovisioning VPN Connection for VPC " + selectedVpcId);
//			// VPN deprovision for a given vpcid
//			// do a TransitGatewayConnection.Query for the given vpc
//			// do a TransitGatewayConnectionDeprovisioning.Generate passing the TransitGatewayConnection that was returned.
//			
//			AsyncCallback<TransitGatewayConnectionQueryResultPojo> cb = new AsyncCallback<TransitGatewayConnectionQueryResultPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//					getView().hidePleaseWaitDialog();
//					getView().showMessageToUser("There was an exception on the " +
//							"server retrieving the VPN Connection for VPC ID " + 
//							selectedVpcId + ".  Message " +
//							"from server is: " + caught.getMessage());
//				}
//
//				@Override
//				public void onSuccess(TransitGatewayConnectionQueryResultPojo result) {
//					if (result.getResults().size() == 0) {
//						getView().hidePleaseWaitDialog();
//						getView().showMessageToUser("Could not find a VPN Connection for the VPC "
//								+ "id " + selectedVpcId + ".  Processing cannot continue.");
//						return;
//					}
//					TransitGatewayConnectionPojo vpn = result.getResults().get(0);
//					AsyncCallback<TransitGatewayConnectionDeprovisioningPojo> de_cb = new AsyncCallback<TransitGatewayConnectionDeprovisioningPojo>() {
//						@Override
//						public void onFailure(Throwable caught) {
//							getView().hidePleaseWaitDialog();
//							GWT.log("Exception generating the TransitGatewayConnectionDeprovisioning", caught);
//							getView().showMessageToUser("There was an exception on the " +
//									"server generating the TransitGatewayConnectionDeprovisioning.  Message " +
//									"from server is: " + caught.getMessage());
//						}
//
//						@Override
//						public void onSuccess(TransitGatewayConnectionDeprovisioningPojo result) {
//							// go to the status page
//							getView().hidePleaseWaitDialog();
//							TransitGatewayConnectionProvisioningSummaryPojo vpncpSummary = new TransitGatewayConnectionProvisioningSummaryPojo();
//							vpncpSummary.setDeprovisioning(result);
//							ActionEvent.fire(eventBus, ActionNames.VPNCDP_GENERATED, vpncpSummary);
//						}
//					};
//					VpcProvisioningService.Util.getInstance().generateTransitGatewayConnectionDeprovisioning(vpn, de_cb);
//				}
//			};
//			TransitGatewayConnectionQueryFilterPojo filter = new TransitGatewayConnectionQueryFilterPojo();
//			filter.setVpcId(selectedVpcId);
//			VpcProvisioningService.Util.getInstance().getTransitGatewayConnectionsForFilter(filter, cb);
		}
		else if (selectedAssignment != null) {
			// TODO:  deprovision the transit gateway assignment
//			getView().showPleaseWaitDialog("Deprovisioning VPN Connection for VPC " + selectedAssignment.getOwnerId());
//			AsyncCallback<TransitGatewayConnectionDeprovisioningPojo> de_cb = new AsyncCallback<TransitGatewayConnectionDeprovisioningPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//					getView().hidePleaseWaitDialog();
//					GWT.log("Exception generating the TransitGatewayConnectionDeprovisioning", caught);
//					getView().showMessageToUser("There was an exception on the " +
//							"server generating the TransitGatewayConnectionDeprovisioning.  Message " +
//							"from server is: " + caught.getMessage());
//				}
//
//				@Override
//				public void onSuccess(TransitGatewayConnectionDeprovisioningPojo result) {
//					// go to the status page
//					getView().hidePleaseWaitDialog();
//					TransitGatewayConnectionProvisioningSummaryPojo vpncpSummary = new TransitGatewayConnectionProvisioningSummaryPojo();
//					vpncpSummary.setDeprovisioning(result);
//					ActionEvent.fire(eventBus, ActionNames.VPNCDP_GENERATED, vpncpSummary);
//				}
//			};
//			VpcProvisioningService.Util.getInstance().generateTransitGatewayConnectionDeprovisioning(selectedAssignment, de_cb);
		}
		else if (selectedSummary != null && isAssignmentDelete) {
			// query for a TransitGatewayConnection by assignment.ownerId
			// if a TransitGatewayConnection does not exist, delete the assignment
			// else show a message to the user

			// uncomment this just to test the row refresh
//			getView().showPleaseWaitDialog("Retrieving the VPN Connection Profile: " + selectedSummary.getProfile().getTransitGatewayConnectionProfileId());
//			GWT.log("[vpcpConfirmOkay] telling view to refresh rowNumber: " + selectedRowNumber);
//			selectedSummary.setAssignment(null);
//    		getView().refreshTableRow(selectedRowNumber, selectedSummary);
//          getView().hidePleaseWaitDialog();

//            AsyncCallback<TransitGatewayConnectionQueryResultPojo> vpn_cb = new AsyncCallback<TransitGatewayConnectionQueryResultPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//					getView().hidePleaseWaitDialog();
//					getView().showMessageToUser("There was an exception on the " +
//							"server checking for a TransitGatewayConnection.  Message " +
//							"from server is: " + caught.getMessage());
//				}
//
//				@Override
//				public void onSuccess(TransitGatewayConnectionQueryResultPojo result) {
//					if (result.getResults().size() == 0) {
//						// it is safe to delete the assignment
//						AsyncCallback<TransitGatewayConnectionProfileAssignmentPojo> delete_assignment_cb = new AsyncCallback<TransitGatewayConnectionProfileAssignmentPojo>() {
//							@Override
//							public void onFailure(Throwable caught) {
//								getView().hidePleaseWaitDialog();
//								getView().showMessageToUser("There was an exception on the " +
//										"server deleting the TransitGatewayConnectionProfileAssignment.  Message " +
//										"from server is: " + caught.getMessage());
//							}
//
//							@Override
//							public void onSuccess(TransitGatewayConnectionProfileAssignmentPojo result) {
//								// now get the summary for this profile again
//								AsyncCallback<TransitGatewayConnectionProfileQueryResultPojo> vpn_summary_cb = new AsyncCallback<TransitGatewayConnectionProfileQueryResultPojo>() {
//									@Override
//									public void onFailure(Throwable caught) {
//						                getView().hidePleaseWaitDialog();
//						                getView().hidePleaseWaitPanel();
//										getView().showMessageToUser("There was an exception on the " +
//												"server retrieving the VPN Connection Profile.  " +
//												"<p>Message from server is: " + caught.getMessage() + "</p>");
//									}
//
//									@Override
//									public void onSuccess(TransitGatewayConnectionProfileQueryResultPojo result) {
//										TransitGatewayConnectionProfileSummaryPojo summary = result.getResults().get(0);
//										GWT.log("[vpcpConfirmOkay] telling view to refresh rowNumber: " 
//											+ selectedRowNumber 
//											+ " for profile id: " 
//											+ summary.getProfile().getTransitGatewayConnectionProfileId());
//						                getView().hidePleaseWaitDialog();
//						                getView().hidePleaseWaitPanel();
//						        		getView().refreshTableRow(selectedRowNumber, summary);
//									}
//								};
//
//								TransitGatewayConnectionProfileQueryFilterPojo vcp_filter = new TransitGatewayConnectionProfileQueryFilterPojo();
//								vcp_filter.setTransitGatewayConnectionProfileId(selectedSummary.getProfile().getTransitGatewayConnectionProfileId());
//				                getView().hidePleaseWaitDialog();
//								getView().showPleaseWaitDialog("Retrieving the VPN Connection Profile: " + vcp_filter.getTransitGatewayConnectionProfileId());
//								VpcProvisioningService.Util.getInstance().getTransitGatewayConnectionProfilesForFilter(vcp_filter, vpn_summary_cb);
//							}
//						};
//		                getView().hidePleaseWaitDialog();
//						getView().showPleaseWaitDialog("Deleting the VPN Connection Profile Assignment for VPC " + selectedSummary.getAssignment().getOwnerId());
//						VpcProvisioningService.Util.getInstance().deleteTransitGatewayConnectionProfileAssignment(selectedSummary.getAssignment(), delete_assignment_cb);
//					}
//					else {
//						// show message to user
//		                getView().hidePleaseWaitDialog();
//						getView().showMessageToUser("This profile appears to have a VPN Connection associated "
//							+ "to it.  Therefore, the assignment cannot be deleted until that connection "
//							+ "has been de-provisioned.  If you still want to remove this assignment, please "
//							+ "deprovision the VPN connection first.");
//					}
//				}
//			};
//			getView().showPleaseWaitDialog("Checking for a TransitGatewayConnection associated to VPC " + selectedSummary.getAssignment().getOwnerId());
//			TransitGatewayConnectionProfileQueryFilterPojo vpn_filter = new TransitGatewayConnectionProfileQueryFilterPojo();
//			vpn_filter.setVpcId(selectedSummary.getAssignment().getOwnerId());
//			vpn_filter.set
//			VpcProvisioningService.Util.getInstance().getTransitGatewayConnectionsForFilter(vpn_filter, vpn_cb);
		}
	}

	@Override
	public void vpcpConfirmCancel() {
		if (selectedSummaries != null) {
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN Connection Profile was NOT deleted");
		}
		else if (selectedVpcId != null){
			// is a de-provision
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN Connection was NOT de-provisioned");
		}
		else if (selectedAssignment != null) {
			// is a de-provision
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPN Connection was NOT de-provisioned");
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
		selectedAssignment = null;
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
		getView().showMessageToUser("Comming soon");
//		AsyncCallback<TransitGatewayConnectionQueryResultPojo> vpn_cb = new AsyncCallback<TransitGatewayConnectionQueryResultPojo>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				getView().hidePleaseWaitDialog();
//				getView().showMessageToUser("There was an exception on the " +
//						"server checking for a TransitGatewayConnection.  Message " +
//						"from server is: " + caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(TransitGatewayConnectionQueryResultPojo result) {
//                getView().hidePleaseWaitDialog();
//				if (result.getResults().size() == 0) {
//					getView().showMessageToUser("A VPN Connection DOES NOT exist for the selected profile/assignment");
//				}
//				else {
//					// show message to user
//					TransitGatewayConnectionPojo vpn1=null;
//					TransitGatewayConnectionPojo vpn2=null;
//					if (result.getResults().size() == 2) {
//						vpn1 = result.getResults().get(0);
//						vpn2 = result.getResults().get(1);
//					}
//					else if (result.getResults().size() == 1) {
//						vpn1 = result.getResults().get(0);
//					}
//					
//					// get the tunnel status from the not null vpn1 and/or vpn2 objects
//					boolean tunnel1Good=false;
//					boolean tunnel2Good=false;
//					if (vpn1 != null) {
//						if (vpn1.getTunnelInterfaces().size() > 0) {
//							tunnel1Good = vpn1.getTunnelInterfaces().get(0).isOperational();
//						}
//					}
//					if (vpn2 != null) {
//						if (vpn2.getTunnelInterfaces().size() > 0) {
//							tunnel2Good = vpn2.getTunnelInterfaces().get(0).isOperational();
//						}
//					}
//					
//					String tunnel1Html;
//					if (tunnel1Good) {
//						tunnel1Html = 
//								"<table cellspacing=\"8\"><tr><td>"
//								+ "Tunnel 1 is: Operational"
//								+ "</td><td>"
//								+ "<img src=\"images/green_circle_icon.png\" width=\"16\" height=\"16\"/>"
//								+ "</td></tr></table";
//					}
//					else {
//						tunnel1Html = 
//								"<table cellspacing=\"8\"><tr><td>"
//								+ "Tunnel 1 is: Not Operational"
//								+ "</td><td>"
//								+ "<img src=\"images/red_circle_icon.jpg\" width=\"16\" height=\"16\"/>"
//								+ "</td></tr></table";
//					}
//							 
//					String tunnel2Html;
//					if (tunnel2Good) {
//						tunnel2Html =
//								"<table cellspacing=\"8\"><tr><td>"
//								+ "Tunnel 2 is: Operational"
//								+ "</td><td>"
//								+ "<img src=\"images/green_circle_icon.png\" width=\"16\" height=\"16\"/>"
//								+ "</td></tr></table";
//					}
//					else {
//						tunnel2Html = 
//								"<table cellspacing=\"8\"><tr><td>"
//								+ "Tunnel 2 is: Not Operational"
//								+ "</td><td>"
//								+ "<img src=\"images/red_circle_icon.jpg\" width=\"16\" height=\"16\"/>"
//								+ "</td></tr></table";
//					}
//					
//					getView().showMessageToUser(
//							"A VPN Connection DOES exist for the selected profile/assignment.  "
//							+ "<p>"
//							+ tunnel1Html
//							+ tunnel2Html
//							+ "</p>"
//							+ "<p>For more information, please visit the VPC Details page for the VPC "
//							+ "associated to this profile</p>");
//				}
//			}
//		};
//		getView().showPleaseWaitDialog("Checking for a TransitGatewayConnection associated to VPC " + vpcId);
//		TransitGatewayConnectionQueryFilterPojo vpn_filter = new TransitGatewayConnectionQueryFilterPojo();
//		vpn_filter.setVpcId(vpcId);
//		VpcProvisioningService.Util.getInstance().getTransitGatewayConnectionsForFilter(vpn_filter, vpn_cb);
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
