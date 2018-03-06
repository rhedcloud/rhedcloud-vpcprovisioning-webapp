package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallRuleExceptionRequestQueryResultPojo extends SharedObject implements IsSerializable {
	FirewallRuleExceptionRequestQueryFilterPojo filterUsed;
	List<FirewallRuleExceptionRequestPojo> results;

	public FirewallRuleExceptionRequestQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public FirewallRuleExceptionRequestQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(FirewallRuleExceptionRequestQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<FirewallRuleExceptionRequestPojo> getResults() {
		return results;
	}

	public void setResults(List<FirewallRuleExceptionRequestPojo> results) {
		this.results = results;
	}

}
