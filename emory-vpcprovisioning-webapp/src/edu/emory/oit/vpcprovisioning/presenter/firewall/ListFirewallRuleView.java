package edu.emory.oit.vpcprovisioning.presenter.firewall;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface ListFirewallRuleView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectFirewallRule(FirewallRulePojo selected);
		public EventBus getEventBus();
		public FirewallRuleQueryFilterPojo getFirewallRuleFilter();
		public FirewallExceptionRequestQueryFilterPojo getFirewallRuleExceptionRequestFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current firewallRule or cancel the creation of a firewallRule.
		 */
		void deleteFirewallRule(FirewallRulePojo firewallRule);
		public void logMessageOnServer(final String message);
		public void refreshFirewallRuleList(final UserAccountPojo user);
		public void refreshFirewallRuleExceptionRequestList(final UserAccountPojo user);
		
		void filterByVPCId(String vpcId);
		void clearFilter();
		public VpcQueryResultPojo getVpcsForFilter(VpcQueryFilterPojo filter);
		public VpcPojo getVpc();
	}

	/**
	 * Clear the list of case records.
	 */
	void clearFirewallRuleList();
	void clearFirewallRuleExceptionRequestList();

	/**
	 * Sets the new presenter, and calls {@link Presenter#stop()} on the previous
	 * one.
	 */
	void setPresenter(Presenter presenter);

	/**
	 * Set the list of caseRecords to display.
	 * 
	 * @param cidrs the list of caseRecords
	 */
	void setFirewallRules(List<FirewallRulePojo> firewallRules);
	void setFirewallRuleRequests(List<FirewallExceptionRequestPojo> firewallRequests);
	
	void setReleaseInfo(String releaseInfoHTML);
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel();
	void removeFirewallRuleFromView(FirewallRulePojo firewallRule);
//	void setVpcItems(List<VpcPojo> vpcs);
	void initPage();


}
