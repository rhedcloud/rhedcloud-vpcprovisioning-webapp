package edu.emory.oit.vpcprovisioning.presenter.firewall;

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
import edu.emory.oit.vpcprovisioning.client.event.FirewallRuleListUpdateEvent;
import edu.emory.oit.vpcprovisioning.client.event.FirewallRuleRequestListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleExceptionRequestQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

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
	FirewallRuleExceptionRequestQueryFilterPojo fwer_filter;
	FirewallRulePojo firewallRule;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		getView().showPleaseWaitDialog();
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				// Add a handler to the 'add' button in the shell.
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("FirewallRules");
				clientFactory.getShell().setUserName(userLoggedIn.getEppn());
				ReleaseInfo ri = new ReleaseInfo();
				clientFactory.getShell().setReleaseInfo(ri.toString());

				// Clear the firewallRule list and display it.
				if (clearList) {
					getView().clearFirewallRuleList();
					getView().clearFirewallRuleExceptionRequestList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				setFirewallRuleList(Collections.<FirewallRulePojo> emptyList());
				setFirewallRuleExceptionRequestList(Collections.<FirewallRuleExceptionRequestPojo> emptyList());
				getView().initPage();

				// Request the firewallRule list now.
				refreshFirewallRuleList(userLoggedIn);
				refreshFirewallRuleExceptionRequestList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	@Override
	public void refreshFirewallRuleList(final UserAccountPojo user) {
		// use RPC to get all firewallRules for the current filter being used
		AsyncCallback<FirewallRuleQueryResultPojo> callback = new AsyncCallback<FirewallRuleQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				log.log(Level.SEVERE, "Exception Retrieving FirewallRules", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of firewallRules.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(FirewallRuleQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " firewallRules for " + result.getFilterUsed());
				setFirewallRuleList(result.getResults());
				// apply authorization mask
				if (user.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
					getView().applyEmoryAWSAdminMask();
				}
				else if (user.hasPermission(Constants.PERMISSION_VIEW_EVERYTHING)) {
					getView().applyEmoryAWSAuditorMask();
				}
				else {
					// ??
				}
                getView().hidePleaseWaitPanel();
			}
		};

		GWT.log("refreshing FirewallRule list...");
		VpcProvisioningService.Util.getInstance().getFirewallRulesForFilter(fw_filter, callback);
	}

	/**
	 * Set the list of firewallRules.
	 */
	private void setFirewallRuleList(List<FirewallRulePojo> firewallRules) {
		getView().setFirewallRules(firewallRules);
		eventBus.fireEventFromSource(new FirewallRuleListUpdateEvent(firewallRules), this);
	}

	private void setFirewallRuleExceptionRequestList(List<FirewallRuleExceptionRequestPojo> firewallRules) {
		getView().setFirewallRuleRequests(firewallRules);
		eventBus.fireEventFromSource(new FirewallRuleRequestListUpdateEvent(firewallRules), this);
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
			getView().showPleaseWaitDialog();
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
	public void refreshFirewallRuleExceptionRequestList(final UserAccountPojo user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FirewallRuleExceptionRequestQueryFilterPojo getFirewallRuleExceptionRequestFilter() {
		return fwer_filter;
	}

	@Override
	public void filterByVPCId(String vpcId) {
		getView().showPleaseWaitDialog();
		fw_filter = new FirewallRuleQueryFilterPojo();
		fw_filter.getTags().add(vpcId);
		this.getUserAndRefreshList();
	}

	@Override
	public void clearFilter() {
		getView().showPleaseWaitDialog();
		fw_filter = null;
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
				refreshFirewallRuleList(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
}
