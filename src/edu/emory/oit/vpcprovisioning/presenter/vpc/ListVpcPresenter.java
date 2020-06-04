package edu.emory.oit.vpcprovisioning.presenter.vpc;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.VpcListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;

public class ListVpcPresenter extends PresenterBase implements ListVpcView.Presenter {
	private static final Logger log = Logger.getLogger(ListVpcPresenter.class.getName());
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
	
	VpcQueryFilterPojo filter;
	VpcPojo selectedVpc;
	private List<VpcPojo> fullVpcList = new java.util.ArrayList<VpcPojo>();

	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListVpcPresenter(ClientFactory clientFactory, boolean clearList, VpcQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListVpcView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListVpcPresenter(ClientFactory clientFactory, ListVpcPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListVpcView getView() {
		return clientFactory.getListVpcView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving User Logged In...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {
				getView().enableButtons();
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("VPCs");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);

				List<String> filterTypeItems = new java.util.ArrayList<String>();
				filterTypeItems.add(Constants.VPC_FILTER_ACCOUNT_ID);
				filterTypeItems.add(Constants.VPC_FILTER_ACCOUNT_NAME);
				filterTypeItems.add(Constants.VPC_FILTER_VPC_ID);
				getView().setFilterTypeItems(filterTypeItems);

				// Request the Vpc list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	public void refreshList(final UserAccountPojo user) {
		getView().showPleaseWaitDialog("Retrieving VPCs from the AWS Account Service...");
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<VpcQueryResultPojo> callback = new AsyncCallback<VpcQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Vpcs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Vpcs.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(VpcQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Vpcs for " + result.getFilterUsed());
				setVpcList(result.getResults());
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (user.isEmoryAwsAdmin()) {
					getView().applyAWSAccountAdminMask();
				}
				else if (user.isAuditor()) {
					getView().applyAWSAccountAuditorMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles for this page.");
					// TODO: need to not show them the list of items???
				}
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing Vpc list...");
		if (filter == null) {
			filter = new VpcQueryFilterPojo();
		}
		filter.setUserLoggedIn(user);
		VpcProvisioningService.Util.getInstance().getVpcsForFilter(filter, callback);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setVpcList(List<VpcPojo> vpcs) {
		getView().setVpcs(vpcs);
		if (filter == null || filter.isFuzzyFilter() == false) {
			fullVpcList = vpcs;
		}
		if (eventBus != null) {
			eventBus.fireEventFromSource(new VpcListUpdateEvent(vpcs), this);
		}
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
	public void selectVpc(VpcPojo selected) {
		
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public VpcQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpcQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteVpc(final VpcPojo vpc) {
		selectedVpc = vpc;
		VpcpConfirm.confirm(
			ListVpcPresenter.this, 
			"Confirm Delete VPC", 
			"Delete the AWS Vpc " + vpc.getAccountId() + "/" + vpc.getVpcId() + "?  "
				+ "<b>NOTE: this VPC will be removed from AWS.</b>");
	}

	@Override
	public void vpcpConfirmOkay() {
		getView().showPleaseWaitDialog("Deleting VPC " + selectedVpc.getVpcId() + "...");
		
		// TEMP
//		Timer t = new Timer() {
//            @Override
//            public void run() {
//				getView().hidePleaseWaitDialog();
//				getView().showStatus(getView().getStatusMessageSource(), "VPC was deleted (not really).");
//            }
//        };
//        t.schedule(3000);
        // end temp
        
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
				getView().removeVpcFromView(selectedVpc);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "VPC metadata was deleted.");
			}
		};
		VpcProvisioningService.Util.getInstance().deleteVpc(selectedVpc, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPC " + 
			selectedVpc.getVpcId() + " was not deleted.");
	}

	@Override
	public void filterByAccountName(String acctName) {
		getView().showPleaseWaitDialog("Filtering VPCs by account name...");
		filter = new VpcQueryFilterPojo();
		filter.setAccountName(acctName);
		filter.setFuzzyFilter(true);
//		this.getUserAndRefreshList();
		
		// just try filtering from out full list
		List<VpcPojo> filteredList = new java.util.ArrayList<VpcPojo>();
		GWT.log("checking " + fullVpcList.size() + " VPCs for a match of " + filter.getAccountName());
		for (VpcPojo vpc : fullVpcList) {
			if (filter.getAccountName() != null && filter.getAccountName().length() > 0) {
				if (vpc.getAccountName().toLowerCase().indexOf(filter.getAccountName().toLowerCase()) >= 0) {
					GWT.log("found an account with a name that matches " + filter.getAccountName());
					filteredList.add(vpc);
				}
			}
		}
		getUserAndRefreshList(filteredList);
	}

	@Override
	public void filterByVpcId(String vpcId) {
		getView().showPleaseWaitDialog("Filtering VPCs by VPC ID...");
		filter = new VpcQueryFilterPojo();
		filter.setVpcId(vpcId);
		filter.setFuzzyFilter(true);
//		this.getUserAndRefreshList();

		List<VpcPojo> filteredList = new java.util.ArrayList<VpcPojo>();
		GWT.log("checking " + fullVpcList.size() + " VPCs for a match of " + filter.getVpcId());
		for (VpcPojo vpc : fullVpcList) {
			if (filter.getVpcId() != null && filter.getVpcId().length() > 0) {
				if (vpc.getVpcId().toLowerCase().indexOf(filter.getVpcId().toLowerCase()) >= 0) {
					GWT.log("found an account with a name that matches " + filter.getVpcId());
					filteredList.add(vpc);
				}
			}
		}
		getUserAndRefreshList(filteredList);
	}

	@Override
	public void filterByAccountId(String acctId) {
		getView().showPleaseWaitDialog("Filtering VPCs by Account ID...");
		filter = new VpcQueryFilterPojo();
		filter.setAccountId(acctId);
		filter.setFuzzyFilter(true);
//		this.getUserAndRefreshList();

		List<VpcPojo> filteredList = new java.util.ArrayList<VpcPojo>();
		GWT.log("checking " + fullVpcList.size() + " VPCs for a match of " + filter.getVpcId());
		for (VpcPojo vpc : fullVpcList) {
			if (filter.getAccountId() != null && filter.getAccountId().length() > 0) {
				if (vpc.getAccountId().toLowerCase().indexOf(filter.getAccountId().toLowerCase()) >= 0) {
					GWT.log("found an account with a name that matches " + filter.getAccountId());
					filteredList.add(vpc);
				}
			}
		}
		getUserAndRefreshList(filteredList);
	}

	@Override
	public void clearFilter() {
		getView().showPleaseWaitDialog("Clearing filter...");
		filter = null;
		this.getUserAndRefreshList();
	}

	private void getUserAndRefreshList() {
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				getView().setUserLoggedIn(result);
				refreshList(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}
	private void getUserAndRefreshList(final List<VpcPojo> filteredList) {
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				getView().setUserLoggedIn(result);
				setVpcList(filteredList);
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}
}
