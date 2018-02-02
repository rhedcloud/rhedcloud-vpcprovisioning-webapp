package edu.emory.oit.vpcprovisioning.presenter.elasticipassignment;

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ElasticIpAssignmentSummaryListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListElasticIpAssignmentPresenter implements ListElasticIpAssignmentView.Presenter {

	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the case record list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;

	ElasticIpAssignmentSummaryQueryFilterPojo filter;
	ElasticIpAssignmentSummaryPojo summary;

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
		this.eventBus = eventBus;

        getView().showPleaseWaitPanel();

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
//				if (!PresenterBase.isTimeoutException(getView(), caught)) {
//					getView().showMessageToUser("There was an exception on the " +
//							"server retrieving information about the user logged " +
//							"in.  Message from server is: " + caught.getMessage());
//				}
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				// Add a handler to the 'add' button in the shell.
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Elastic IP Assignments");
				ReleaseInfo ri = new ReleaseInfo();
				clientFactory.getShell().setReleaseInfo(ri.toString());

				// Clear the account list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				GWT.log("presenter, emptying Elastic IP Assignment list.");
				setElasticIpAssignmentSummaryList(Collections.<ElasticIpAssignmentSummaryPojo> emptyList());

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
	private void setElasticIpAssignmentSummaryList(List<ElasticIpAssignmentSummaryPojo> summaries) {
		getView().setElasticIpAssignmentSummaries(summaries);
		GWT.log("back to presenter, firing Elastic IP Assignemt list update event...");
		eventBus.fireEventFromSource(new ElasticIpAssignmentSummaryListUpdateEvent(summaries), this);
	}
	
	/**
	 * Refresh the CIDR assignment list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all accounts for the current filter being used
		AsyncCallback<ElasticIpAssignmentSummaryQueryResultPojo> callback = new AsyncCallback<ElasticIpAssignmentSummaryQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of ElasticIpAssignmentSummaries.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ElasticIpAssignmentSummaryQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " ElasticIpAssignmentSummaries for " + result.getFilterUsed());
				GWT.log("presenter, initializing Elastic IP Assignment list with " + result.getResults().size() + " Elastic IP Assignments.");
				setElasticIpAssignmentSummaryList(result.getResults());
				// apply authorization mask
				GWT.log("back to presenter, applying authorization masks...");
				if (user.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
					getView().applyEmoryAWSAdminMask();
				}
				else if (user.hasPermission(Constants.PERMISSION_VIEW_EVERYTHING)) {
					getView().applyEmoryAWSAuditorMask();
				}
				else {
					// ??
				}
				GWT.log("back to presenter, masks applied...");
                getView().hidePleaseWaitPanel();
				GWT.log("back to presenter, please wait hidden...");
			}
		};

		GWT.log("refreshing Elastic IP AssignmentSummary list...");
		// getCidrAssignmentSummariesForFilter
		VpcProvisioningService.Util.getInstance().getElasticIpAssignmentSummariesForFilter(filter, callback);
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
	public void selectElasticIpAssignmentSummary(ElasticIpAssignmentSummaryPojo selected) {
		this.summary = selected;
	}

	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}

	@Override
	public ElasticIpAssignmentSummaryQueryFilterPojo getFilter() {
		return this.filter;
	}

	@Override
	public ClientFactory getClientFactory() {
		return this.clientFactory;
	}

	@Override
	public void deleteElasticIpAssignment(ElasticIpAssignmentSummaryPojo selected) {
		if (Window.confirm("Delete the Elastic IP Assignment " + 
				summary.getElasticIpAssignment().getAssignmentId() + "/" + 
				summary.getElasticIpAssignment().getPurpose() + "?")) {
			
			getView().showPleaseWaitDialog();
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					getView().showMessageToUser("There was an exception on the " +
							"server deleting the CidrAssignment.  Message " +
							"from server is: " + caught.getMessage());
					getView().hidePleaseWaitDialog();
				}

				@Override
				public void onSuccess(Void result) {
					// remove from dataprovider
					getView().removeElasticIpAssignmentSummaryFromView(summary);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "Elastic IP Assignment was deleted.");
					
					// TODO fire list accounts event...
				}
			};
			VpcProvisioningService.Util.getInstance().deleteElasticIpAssignment(summary.getElasticIpAssignment(), callback);
		}
	}

}
