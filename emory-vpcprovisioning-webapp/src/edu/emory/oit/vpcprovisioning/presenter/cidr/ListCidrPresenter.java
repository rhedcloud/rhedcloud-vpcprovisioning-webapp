package edu.emory.oit.vpcprovisioning.presenter.cidr;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.CidrListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentStatus;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class ListCidrPresenter extends PresenterBase implements ListCidrView.Presenter {
	private static final Logger log = Logger.getLogger(ListCidrPresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the CIDR list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes
	
	/**
	 * A boolean indicating that we should clear the CIDR list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	CidrQueryFilterPojo filter;
	List<CidrSummaryPojo> cidrSummaryList = new java.util.ArrayList<CidrSummaryPojo>();

	/**
	 * The refresh timer used to periodically refresh the CIDR list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListCidrPresenter(ClientFactory clientFactory, boolean clearList, CidrQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListCidrView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListCidrPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListCidrPresenter(ClientFactory clientFactory, ListCidrPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListCidrView getView() {
		return clientFactory.getListCidrView();
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

		getView().showPleaseWaitDialog();
		getView().showPleaseWaitPanel();

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
//				if (!PresenterBase.isTimeoutException(getView(), caught)) {
//					log.log(Level.SEVERE, 
//							"Exception getting user logged in on server", 
//							caught);
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
				clientFactory.getShell().setSubTitle("CIDRs");

				// Clear the CIDR list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				getView().initPage();

				setCidrSummaryList(Collections.<CidrSummaryPojo> emptyList());

				// Request the CIDR list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all cidrs for the current filter being used
		AsyncCallback<CidrQueryResultPojo> callback = new AsyncCallback<CidrQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Case Records", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of CIDRs.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(CidrQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " CIDRs for " + result.getFilterUsed());
				setCidrSummaryList(result.getResults());
				// apply authorization mask
				if (user.isLitsAdmin()) {
					getView().applyEmoryAWSAdminMask();
				}
				else {
					getView().applyEmoryAWSAuditorMask();
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles for this page.");
					// TODO: need to not show them the list of items???
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing CIDR list...");
		// TODO: setup filter
//		if (filter == null) {
//			filter = new CidrQueryFilterPojo();
//		}
//		if (!user.isLitsAdmin()) {
//			filter.setUserLoggedIn(user);
//		}
		VpcProvisioningService.Util.getInstance().getCidrsForFilter(filter, callback);
	}

	/**
	 * Set the list of CIDRs.
	 */
	private void setCidrSummaryList(List<CidrSummaryPojo> cidrSummaries) {
		this.cidrSummaryList = cidrSummaries;
		getView().setCidrSummaries(cidrSummaries);
		eventBus.fireEventFromSource(new CidrListUpdateEvent(cidrSummaries), this);
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
	public void selectCidr(CidrPojo selected) {
		// TODO Auto-generated method stub
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public CidrQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(CidrQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteCidrSummary(final CidrSummaryPojo cidrSummary) {
		// check to see if the selected cidr is already assigned.
		// if it is, display an error.  An assigned CIDR cannot be deleted
		
		AsyncCallback<CidrAssignmentStatus> isAssignedCB = new AsyncCallback<CidrAssignmentStatus>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().showMessageToUser("There was an exception on the " +
						"server determining the Cidr's assignment status.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(CidrAssignmentStatus assignmentStatus) {
				if (assignmentStatus != null && assignmentStatus.isAssigned()) {
					getView().hidePleaseWaitDialog();
					// this locks the view for some reason
//					getView().showMessageToUser("This CIDR is assigned to a VPC (" + 
//							assignmentStatus.getCidrAssignment().getOwnerId() + ").  "
//							+ "CIDRs that are assigned to a VPC cannot be deleted.");
					// this does not lock the view
					getView().showStatus(getView().getStatusMessageSource(), 
							"This CIDR is assigned to a VPC (" + 
							assignmentStatus.getCidrAssignment().getOwnerId() + ").  "
							+ "CIDRs that are assigned to a VPC cannot be deleted.");
				}
				else {
					// confirm and delete
					if (Window.confirm("Delete the CIDR Assignment for " + cidrSummary.getCidr().getNetwork() + 
						"/" + cidrSummary.getCidr().getBits() + "?")) {
						
						getView().showPleaseWaitDialog();
						AsyncCallback<Void> callback = new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								getView().hidePleaseWaitDialog();
								getView().showMessageToUser("There was an exception on the " +
										"server deleting the CidrAsignment.  Message " +
										"from server is: " + caught.getMessage());
							}

							@Override
							public void onSuccess(Void result) {
								// remove from dataprovider
								getView().removeCidrSummaryFromView(cidrSummary);
								getView().hidePleaseWaitDialog();
								// status message
								getView().showStatus(getView().getStatusMessageSource(), "Cidr Assignment was deleted.");

								// TODO fire list cidrs event...
							}
						};
						VpcProvisioningService.Util.getInstance().deleteCidrSummary(cidrSummary, callback);
					}
				}
			}
			
		};
		VpcProvisioningService.Util.getInstance().getCidrAssignmentStatusForCidr(cidrSummary.getCidr(), isAssignedCB);
	}

	@Override
	public void filterByVPCId(String vpcId) {
		getView().showPleaseWaitDialog();
		List<CidrSummaryPojo> filteredList = new java.util.ArrayList<CidrSummaryPojo>();
		for (CidrSummaryPojo pojo : this.cidrSummaryList) {
			if (pojo.getAssignmentSummary() != null) {
				VpcPojo vpc = pojo.getAssignmentSummary().getVpc();
				if (vpc.getVpcId().equalsIgnoreCase(vpcId)) {
					filteredList.add(pojo);
				}
			}
		}
		getView().setCidrSummaries(filteredList);
		eventBus.fireEventFromSource(new CidrListUpdateEvent(filteredList), this);
        getView().hidePleaseWaitPanel();
        getView().hidePleaseWaitDialog();
	}

	@Override
	public void clearFilter() {
		getView().showPleaseWaitDialog();
		filter = null;
		this.getUserAndRefreshList();
	}

	private void getUserAndRefreshList() {
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				getView().setUserLoggedIn(result);
				refreshList(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
}
