package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRemoveRequestQueryResultPojo extends SharedObject implements IsSerializable {
	FirewallExceptionRemoveRequestQueryFilterPojo filterUsed;
	List<FirewallExceptionRemoveRequestPojo> results;

	public FirewallExceptionRemoveRequestQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public FirewallExceptionRemoveRequestQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(FirewallExceptionRemoveRequestQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<FirewallExceptionRemoveRequestPojo> getResults() {
		return results;
	}

	public void setResults(List<FirewallExceptionRemoveRequestPojo> results) {
		this.results = results;
	}

}
