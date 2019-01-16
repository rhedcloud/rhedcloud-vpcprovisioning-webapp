package edu.emory.oit.vpcprovisioning.presenter.firewall;

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
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.FirewallExceptionRequestListUpdateEvent;
import edu.emory.oit.vpcprovisioning.client.event.FirewallRuleListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionAddRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRemoveRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;

public class ListFirewallRulePresenter extends PresenterBase implements ListFirewallRuleView.Presenter {
	private static final Logger log = Logger.getLogger(ListFirewallRulePresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the firewallRule list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the firewallRule list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	FirewallRuleQueryFilterPojo fw_filter;
	FirewallExceptionRequestSummaryQueryFilterPojo fwer_filter;
	FirewallRulePojo firewallRule;
	VpcPojo vpc;
	FirewallExceptionAddRequestPojo selectedAddRequest;
	FirewallExceptionRemoveRequestPojo selectedRemoveRequest;
	FirewallExceptionRequestSummaryPojo selectedExceptionRequestSummary;

	/**
	 * The refresh timer used to periodically refresh the firewallRule list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListFirewallRulePresenter(ClientFactory clientFactory, boolean clearList, FirewallRuleQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListFirewallRuleView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListFirewallRulePresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListFirewallRulePresenter(ClientFactory clientFactory, ListFirewallRulePlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListFirewallRuleView getView() {
		return clientFactory.getListFirewallRuleView();
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
		getView().showPleaseWaitDialog("Retrieving firewall rules from the Firewall service...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Central Admins you're associated to.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {
				getView().enableButtons();
				// Add a handler to the 'add' button in the shell.
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("FirewallRules");

				// Clear the firewallRule list and display it.
				if (clearList) {
					getView().clearFirewallRuleList();
					getView().clearFirewallRuleExceptionRequestList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				getView().initPage();
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the Firewall Rule list.
	 */
	@Override
	public void refreshList(final UserAccountPojo user) {
		getView().showPleaseWaitDialog("Retrieving firewall rules from the Firewall service...");
		// use RPC to get all firewallRules for the current filter being used
		AsyncCallback<FirewallRuleQueryResultPojo> callback = new AsyncCallback<FirewallRuleQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving FirewallRules", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the list of Firewall Rules for this VPC.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(FirewallRuleQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " firewallRules for " + result.getFilterUsed());
				setFirewallRuleList(result.getResults());
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
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles for this page.");
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing FirewallRule list...");
		if (fw_filter == null) {
			fw_filter = new FirewallRuleQueryFilterPojo();
			fw_filter.getTags().add(vpc.getVpcId());
		}
		getView().showPleaseWaitDialog("Retrieving firewall rules from the Firewall service...");
		VpcProvisioningService.Util.getInstance().getFirewallRulesForFilter(fw_filter, callback);
	}

	@Override
	public void refreshFirewallExceptionRequestSummaryList(final UserAccountPojo user) {
		AsyncCallback<FirewallExceptionRequestSummaryQueryResultPojo> callback = new AsyncCallback<FirewallExceptionRequestSummaryQueryResultPojo>() {

			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving FirewallExceptionRequests", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of firewall exception requests.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(FirewallExceptionRequestSummaryQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " firewall exception requests for " + vpc.getVpcId());
				setFirewallExceptionRequestList(result.getResults());
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
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles for this page.");
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};
		GWT.log("refreshing FirewallExceptionRequest list...");
		if (fwer_filter == null) {
			fwer_filter = new FirewallExceptionRequestSummaryQueryFilterPojo();
			fwer_filter.setVpcId(vpc.getVpcId());
		}
		getView().showPleaseWaitDialog("Retrieving firewall exception requests from service now...");
		VpcProvisioningService.Util.getInstance().getFirewallExceptionRequestSummariesForFilter(fwer_filter, callback);
	}

	/**
	 * Set the list of firewallRules.
	 */
	private void setFirewallRuleList(List<FirewallRulePojo> firewallRules) {
		getView().setFirewallRules(firewallRules);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new FirewallRuleListUpdateEvent(firewallRules), this);
		}
	}

	private void setFirewallExceptionRequestList(List<FirewallExceptionRequestSummaryPojo> summaries) {
		getView().setFirewallExceptionRequestSummaries(summaries);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new FirewallExceptionRequestListUpdateEvent(summaries), this);
		}
	}

	@Override
	public void stop() {
		
		
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
	public void selectFirewallRule(FirewallRulePojo selected) {
		this.firewallRule = selected;
		// TODO fire view/edit firewallRule action maybe
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public FirewallRuleQueryFilterPojo getFirewallRuleFilter() {
		return fw_filter;
	}

	public void setFilter(FirewallRuleQueryFilterPojo filter) {
		this.fw_filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteFirewallRule(final FirewallRulePojo firewallRule) {
		if (Window.confirm("Delete the AWS FirewallRule " + firewallRule.getName() + "?")) {
			getView().showPleaseWaitDialog("Deleting firewall rule...");
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					getView().showMessageToUser("There was an exception on the " +
							"server deleting the FirewallRule.  Message " +
							"from server is: " + caught.getMessage());
					getView().hidePleaseWaitDialog();
				}

				@Override
				public void onSuccess(Void result) {
					// remove from dataprovider
					getView().removeFirewallRuleFromView(firewallRule);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "FirewallRule was deleted.");
					
					// TODO fire list firewallRules event...
				}
			};
			VpcProvisioningService.Util.getInstance().deleteFirewallRule(firewallRule, callback);
		}
	}

	@Override
	public FirewallExceptionRequestSummaryQueryFilterPojo getFirewallRuleExceptionRequestSummaryFilter() {
		return fwer_filter;
	}

	@Override
	public void filterByVPCId(String vpcId) {
		getView().showPleaseWaitDialog("Filtering firewall rules...");
		fw_filter = new FirewallRuleQueryFilterPojo();
		fw_filter.getTags().add(vpcId);
		this.getUserAndRefreshList();
	}

	@Override
	public void clearFilter() {
		getView().showPleaseWaitDialog("Clearing filter...");
		fw_filter = null;
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	@Override
	public VpcQueryResultPojo getVpcsForFilter(VpcQueryFilterPojo filter) {
		
		return null;
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	@Override
	public void cancelFirewallException(FirewallExceptionRequestSummaryPojo summary) {
		selectedExceptionRequestSummary = summary;
		selectedAddRequest = summary.getAddRequest();
		selectedRemoveRequest = summary.getRemoveRequest();
		String title = null;
		String message = null;
		if (selectedAddRequest != null) {
			if (!selectedAddRequest.getRequestState().equalsIgnoreCase(Constants.REQUEST_STATE_OPEN)) {
				getView().showMessageToUser("The selected Firewall Exception Request cannot be cancelled.  "
					+ "Please select a request that's in an " + Constants.REQUEST_STATE_OPEN + " state.");
				return;
			}
			title = "Confirm Cancel Firewall Exception ADD Request";
			message = "Cancel the Firewall Exception ADD Request: " + selectedAddRequest.getRequestItemNumber() + "?";  
			VpcpConfirm.confirm(
					ListFirewallRulePresenter.this, 
					title, 
					message);
		}
		else {
			if (!selectedRemoveRequest.getRequestState().equalsIgnoreCase(Constants.REQUEST_STATE_OPEN)) {
				getView().showMessageToUser("The selected Firewall Exception Request cannot be cancelled.  "
					+ "Please select a request that's in an " + Constants.REQUEST_STATE_OPEN + " state.");
				return;
			}
			title = "Confirm Delete Firewall Exception REMOVE Request";
			message = "Camce; the Firewall Exception REMOVE Request: " + selectedRemoveRequest.getRequestItemNumber() + "?";  
			VpcpConfirm.confirm(
					ListFirewallRulePresenter.this, 
					title, 
					message);
		}
	}

	@Override
	public void vpcpConfirmOkay() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server cancelling the Firewall Exception Request.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(Void result) {
				// remove from dataprovider
//				getView().removeFirewallExceptionRequestSummaryFromView(selectedExceptionRequestSummary);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "Firewall exceptoin request was cancelled.");
				ActionEvent.fire(getEventBus(), ActionNames.GO_HOME_FIREWALL_RULE, vpc);
			}
		};
		if (selectedAddRequest != null) {
			VpcProvisioningService.Util.getInstance().deleteFirewallExceptionAddRequest(selectedAddRequest, callback);
		}
		else {
			VpcProvisioningService.Util.getInstance().deleteFirewallExceptionRemoveRequest(selectedRemoveRequest, callback);
		}
	}

	@Override
	public void vpcpConfirmCancel() {
		if (selectedAddRequest != null) {
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Firewall Exception ADD Request " + 
					selectedAddRequest.getRequestItemNumber() + " was not cancelled.");
		}
		else {
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Firewall Exception REMOVE Request " + 
					selectedRemoveRequest.getRequestItemNumber() + " was not cancelled.");
		}
	}
}
