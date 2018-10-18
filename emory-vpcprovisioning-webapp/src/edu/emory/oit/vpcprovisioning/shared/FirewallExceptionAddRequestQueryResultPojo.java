package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionAddRequestQueryResultPojo extends SharedObject implements IsSerializable {
	FirewallExceptionAddRequestQueryFilterPojo filterUsed;
	List<FirewallExceptionAddRequestPojo> results;

	public FirewallExceptionAddRequestQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public FirewallExceptionAddRequestQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(FirewallExceptionAddRequestQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<FirewallExceptionAddRequestPojo> getResults() {
		return results;
	}

	public void setResults(List<FirewallExceptionAddRequestPojo> results) {
		this.results = results;
	}

}
