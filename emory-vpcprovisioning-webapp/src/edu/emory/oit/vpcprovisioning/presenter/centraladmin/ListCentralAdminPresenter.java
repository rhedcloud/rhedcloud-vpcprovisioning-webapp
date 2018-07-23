package edu.emory.oit.vpcprovisioning.presenter.centraladmin;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.CentralAdminListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListCentralAdminPresenter extends PresenterBase implements ListCentralAdminView.Presenter {

	private static final Logger log = Logger.getLogger(ListCentralAdminPresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the account list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the account list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	RoleAssignmentQueryFilterPojo filter;
	RoleAssignmentSummaryPojo centralAdmin;

	/**
	 * The refresh timer used to periodically refresh the account list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListCentralAdminPresenter(ClientFactory clientFactory, boolean clearList, RoleAssignmentQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListCentralAdminView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListCentralAdminPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListCentralAdminPresenter(ClientFactory clientFactory, ListCentralAdminPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListCentralAdminView getView() {
		return clientFactory.getListCentralAdminView();
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
		getView().showPleaseWaitDialog("Retrieving Central Administrators from the IDM Service...");
		
		AsyncCallback<String> myNetIdCallback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().setMyNetIdURL("Exception getting MyNETId URL from Server: " + caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				getView().setMyNetIdURL(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getMyNetIdURL(myNetIdCallback);
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				log.log(Level.SEVERE, "Exception Retrieving Central Admins", caught);
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Central Admins you're associated to.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {
				getView().enableButtons();
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Central Admins");

				// Clear the account list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				getView().initPage();
//				setCentralAdminList(Collections.<RoleAssignmentSummaryPojo> emptyList());

				// Request the account list now.
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
		// use RPC to get all accounts for the current filter being used
		AsyncCallback<List<RoleAssignmentSummaryPojo>> callback = new AsyncCallback<List<RoleAssignmentSummaryPojo>>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Central Admins", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of accounts.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<RoleAssignmentSummaryPojo> result) {
				GWT.log("Got " + result.size() + " central admins back from server.");
				setCentralAdminList(result);
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles required to view this data.");
					getView().applyAWSAccountAuditorMask();
					// TODO: need to not show them the list of accounts???
				}
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing Account list...");
		if (filter == null) {
			filter = new RoleAssignmentQueryFilterPojo();
		}
//		filter.setUserLoggedIn(user);
		VpcProvisioningService.Util.getInstance().getCentralAdmins(callback);
	}

	/**
	 * Set the list of accounts.
	 */
	private void setCentralAdminList(List<RoleAssignmentSummaryPojo> centralAdmins) {
		getView().setCentralAdmins(centralAdmins);
		eventBus.fireEventFromSource(new CentralAdminListUpdateEvent(centralAdmins), this);
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
	public void selectCentralAdmin(RoleAssignmentSummaryPojo selected) {
		this.centralAdmin = selected;
		// TODO fire view/edit account action maybe
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public RoleAssignmentQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(RoleAssignmentQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteCentralAdmin(final RoleAssignmentSummaryPojo centralAdmin) {
//		if (Window.confirm("Delete the Central Admin " + "?")) {
//			getView().showPleaseWaitDialog("Deleting central admin");
//			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
//
//				@Override
//				public void onFailure(Throwable caught) {
//					getView().showMessageToUser("There was an exception on the " +
//							"server deleting the central administrator.  Message " +
//							"from server is: " + caught.getMessage());
//					getView().hidePleaseWaitDialog();
//				}
//
//				@Override
//				public void onSuccess(Void result) {
//					// remove from dataprovider
//					getView().removeCentralAdminFromView(centralAdmin);
//					getView().hidePleaseWaitDialog();
//					// status message
//					getView().showStatus(getView().getStatusMessageSource(), "Account was deleted.");
//					
//					// TODO fire list accounts event...
//				}
//			};
//			VpcProvisioningService.Util.getInstance().deleteCentralAdmin(centralAdmin, callback);
//		}
	}

//	@Override
//	public void filterByAccountId(String accountId) {
//		getView().showPleaseWaitDialog("Filtering accounts");
//		filter = new RoleAssignmentQueryFilterPojo();
//		filter.setAccountId(accountId);
//		this.getUserAndRefreshList();
//	}
//
//	@Override
//	public void clearFilter() {
//		getView().showPleaseWaitDialog("Clearing filter");
//		filter = null;
//		this.getUserAndRefreshList();
//	}
	
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
