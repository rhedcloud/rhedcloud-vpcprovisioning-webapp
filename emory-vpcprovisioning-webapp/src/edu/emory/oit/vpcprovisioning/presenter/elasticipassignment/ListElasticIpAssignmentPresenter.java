package edu.emory.oit.vpcprovisioning.presenter.elasticipassignment;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.ElasticIpAssignmentListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class ListElasticIpAssignmentPresenter extends PresenterBase implements ListElasticIpAssignmentView.Presenter {

	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the case record list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;

	ElasticIpAssignmentQueryFilterPojo filter;
	ElasticIpAssignmentPojo elasticIpAssignment;
	VpcPojo vpc;

	public ListElasticIpAssignmentPresenter(ClientFactory clientFactory, boolean clearList) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListElasticIpAssignmentView().setPresenter(this);
	}

	public ListElasticIpAssignmentPresenter(ClientFactory clientFactory, ListElasticIpAssignmentPlace place) {
		this(clientFactory, place.isListStale());
	}

	private ListElasticIpAssignmentView getView() {
		return clientFactory.getListElasticIpAssignmentView();
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
        getView().showPleaseWaitDialog("Retrieving Elastic IP Assignments");

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Central Admins you're associated to.  " +
						"Message from server is: " + caught.getMessage());
//				if (!PresenterBase.isTimeoutException(getView(), caught)) {
//					getView().showMessageToUser("There was an exception on the " +
//							"server retrieving information about the user logged " +
//							"in.  Message from server is: " + caught.getMessage());
//				}
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {
				getView().enableButtons();
				// Add a handler to the 'add' button in the shell.
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Elastic IP Assignments");

				// Clear the account list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
//				setElasticIpAssignmentList(Collections.<ElasticIpAssignmentPojo> emptyList());

				// Request the cidr assignment list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Set the list of CidrAssignments
	 */
	private void setElasticIpAssignmentList(List<ElasticIpAssignmentPojo> summaries) {
		getView().setElasticIpAssignments(summaries);
		GWT.log("back to presenter, firing Elastic IP Assignemt list update event...");
		eventBus.fireEventFromSource(new ElasticIpAssignmentListUpdateEvent(summaries), this);
	}
	
	/**
	 * Refresh the CIDR assignment list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all accounts for the current filter being used
		AsyncCallback<ElasticIpAssignmentQueryResultPojo> callback = new AsyncCallback<ElasticIpAssignmentQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of ElasticIpAssignmentSummaries.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ElasticIpAssignmentQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " ElasticIpAssignments for VPC: " + vpc.getVpcId());
				GWT.log("presenter, initializing Elastic IP Assignment list with " + result.getResults().size() + " Elastic IP Assignments.");
				setElasticIpAssignmentList(result.getResults());
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (vpc != null && user.isAdminForAccount(vpc.getAccountId())) {
					getView().applyAWSAccountAdminMask();
				}
				else if (vpc != null && user.isAuditorForAccount(vpc.getAccountId())) {
					getView().applyAWSAccountAuditorMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing Elastic IP AssignmentSummary list...");
		// getCidrAssignmentSummariesForFilter
		if (filter == null) {
			filter = new ElasticIpAssignmentQueryFilterPojo();
		}
//		if (vpc != null) {
//			
//		}
		filter.setOwnerId(vpc.getVpcId());
		VpcProvisioningService.Util.getInstance().getElasticIpAssignmentsForFilter(filter, callback);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
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
	public void selectElasticIpAssignment(ElasticIpAssignmentPojo selected) {
		this.elasticIpAssignment = selected;
	}

	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}

	@Override
	public ElasticIpAssignmentQueryFilterPojo getFilter() {
		return this.filter;
	}

	@Override
	public ClientFactory getClientFactory() {
		return this.clientFactory;
	}

	@Override
	public void deleteElasticIpAssignment(final ElasticIpAssignmentPojo selected) {
		if (Window.confirm("Delete the Elastic IP Assignment " + 
				selected.getAssignmentId() + "/" + 
				selected.getPurpose() + "?")) {
			
			getView().showPleaseWaitDialog("Deleting Elastic IP Assignment");
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					getView().showMessageToUser("There was an exception on the " +
							"server deleting the Elastic IP Assignment.  Message " +
							"from server is: " + caught.getMessage());
					getView().hidePleaseWaitDialog();
				}

				@Override
				public void onSuccess(Void result) {
					// remove from dataprovider
					getView().removeElasticIpAssignmentFromView(selected);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "Elastic IP Assignment was deleted.");
					
					// fire list elastic ip assignment event...
//					ActionEvent.fire(eventBus, ActionNames.GO_HOME_ELASTIC_IP_ASSIGNMENT, vpc);
				}
			};
			VpcProvisioningService.Util.getInstance().deleteElasticIpAssignment(selected, callback);
		}
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	@Override
	public void generateElasticIpAssignment() {
        getView().showPleaseWaitPanel("Generating Elastic IP Assignment");

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				getView().setUserLoggedIn(userLoggedIn);
				
				AsyncCallback<ElasticIpAssignmentPojo> callback = new AsyncCallback<ElasticIpAssignmentPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().showMessageToUser("There was an exception on the " +
								"server generating the Elastic IP Assignment.  Message " +
								"from server is: " + caught.getMessage());
		                getView().hidePleaseWaitPanel();
					}

					@Override
					public void onSuccess(ElasticIpAssignmentPojo result) {
						refreshList(userLoggedIn);
					}
				};
				ElasticIpAssignmentRequisitionPojo req = new ElasticIpAssignmentRequisitionPojo();
				req.setOwnerId(vpc.getVpcId());
				VpcProvisioningService.Util.getInstance().generateElasticIpAssignment(req, callback);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	@Override
	public void saveElasticIpAssignment(final ElasticIpAssignmentPojo selected) {
		getView().showPleaseWaitDialog("Saving Elastic IP Assignment");
		if (selected == null) {
			getView().showMessageToUser("Please select an item from the list.");
			return;
		}
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
		}
		AsyncCallback<ElasticIpAssignmentPojo> callback = new AsyncCallback<ElasticIpAssignmentPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the ElasticIP Assignment", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the ElasticIP Assignment.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ElasticIpAssignmentPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ELASTIC_IP_ASSIGNMENT, vpc);
			}
		};
		// it's an update
		VpcProvisioningService.Util.getInstance().updateElasticIpAssignment(selected, callback);
	}

}
