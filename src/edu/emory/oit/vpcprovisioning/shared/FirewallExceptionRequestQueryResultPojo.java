package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRequestQueryResultPojo extends SharedObject implements IsSerializable {
	FirewallExceptionRequestQueryFilterPojo filterUsed;
	List<FirewallExceptionRequestPojo> results;

	public FirewallExceptionRequestQueryResultPojo() {
		
	}

	public FirewallExceptionRequestQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(FirewallExceptionRequestQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<FirewallExceptionRequestPojo> getResults() {
		return results;
	}

	public void setResults(List<FirewallExceptionRequestPojo> results) {
		this.results = results;
	}

}
