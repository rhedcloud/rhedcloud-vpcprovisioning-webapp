package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallRuleQueryResultPojo extends SharedObject implements IsSerializable {
	FirewallRuleQueryFilterPojo filterUsed;
	List<FirewallRulePojo> results;

	public FirewallRuleQueryResultPojo() {
		
	}

	public FirewallRuleQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(FirewallRuleQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<FirewallRulePojo> getResults() {
		return results;
	}

	public void setResults(List<FirewallRulePojo> results) {
		this.results = results;
	}

}
