package edu.emory.oit.vpcprovisioning.presenter.cidrassignment;

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.CidrAssignmentSummaryListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class ListCidrAssignmentPresenter extends PresenterBase implements ListCidrAssignmentView.Presenter {
	/**
	 * The delay in milliseconds between calls to refresh the case record list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the case record list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;

	CidrAssignmentSummaryQueryFilterPojo filter;
	CidrAssignmentSummaryPojo cidrAssignmentSummary;
	VpcPojo vpc;

	/**
	 * The refresh timer used to periodically refresh the case record list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListCidrAssignmentPresenter(ClientFactory clientFactory, boolean clearList) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListCidrAssignmentView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListCidrAssignmentPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListCidrAssignmentPresenter(ClientFactory clientFactory, ListCidrAssignmentPlace place) {
		this(clientFactory, place.isListStale());
	}

	private ListCidrAssignmentView getView() {
		return clientFactory.getListCidrAssignmentView();
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
        getView().showPleaseWaitPanel("Retrieving CIDR Assignments");

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
//				clientFactory.getShell().setAddButtonVisible(true);
//				clientFactory.getShell().setBackButtonVisible(false);
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("CidrAssignments");

				// Clear the account list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				GWT.log("presenter, emptying CidrAssignment list.");
//				setCidrAssignmentSummaryList(Collections.<CidrAssignmentSummaryPojo> emptyList());

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
	private void setCidrAssignmentSummaryList(List<CidrAssignmentSummaryPojo> cidrAssignmentSummaries) {
		getView().setCidrAssignmentSummaries(cidrAssignmentSummaries);
		GWT.log("back to presenter, firing CidrAssignemt list update event...");
		eventBus.fireEventFromSource(new CidrAssignmentSummaryListUpdateEvent(cidrAssignmentSummaries), this);
	}


	/**
	 * Refresh the CIDR assignment list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all accounts for the current filter being used
		AsyncCallback<CidrAssignmentSummaryQueryResultPojo> callback = new AsyncCallback<CidrAssignmentSummaryQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of CidrAssignmentSummaries.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(CidrAssignmentSummaryQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " CidrAssignmentSummaries for " + result.getFilterUsed());
				GWT.log("presenter, initializing CidrAssignment list with " + result.getResults().size() + " CidrAssignments.");
				setCidrAssignmentSummaryList(result.getResults());
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
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles for this page.");
					getView().applyAWSAccountAuditorMask();
					// TODO: need to not show them this page??
				}
				GWT.log("back to presenter, masks applied...");
                getView().hidePleaseWaitPanel();
				GWT.log("back to presenter, please wait hidden...");
			}
		};

		GWT.log("refreshing CidrAssignmentSummary list...");
		// getCidrAssignmentSummariesForFilter
		VpcProvisioningService.Util.getInstance().getCidrAssignmentSummariesForFilter(filter, callback);
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
	public void selectCidrAssignmentSummary(CidrAssignmentSummaryPojo selected) {
		this.cidrAssignmentSummary = selected;
		// TODO fire view CidrAssignment even maybe...
	}

	@Override
	public void deleteCidrAssignment(final CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		if (Window.confirm("Delete the CidrAssignment " + 
				cidrAssignmentSummary.getCidrAssignment().getCidrAssignmentId() + "/" + 
				cidrAssignmentSummary.getCidrAssignment().getPurpose() + "?")) {
			
			getView().showPleaseWaitDialog("Deleting CIDR Assignment");
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
					getView().removeCidrAssignmentSummaryFromView(cidrAssignmentSummary);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "CidrAssignment was deleted.");
					
					// TODO fire list accounts event...
				}
			};
			VpcProvisioningService.Util.getInstance().deleteCidrAssignment(cidrAssignmentSummary.getCidrAssignment(), callback);
		}
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public CidrAssignmentSummaryQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(CidrAssignmentSummaryQueryFilterPojo filter) {
		this.filter = filter;
	}

	public CidrAssignmentSummaryPojo getCidrAssignmentSummary() {
		return cidrAssignmentSummary;
	}

	public void setCidrAssignmentSummary(CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		this.cidrAssignmentSummary = cidrAssignmentSummary;
	}

	public boolean isClearList() {
		return clearList;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}
}
