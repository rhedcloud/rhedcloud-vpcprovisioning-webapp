package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRequestSummaryQueryResultPojo extends SharedObject implements IsSerializable {
	List<FirewallExceptionRequestSummaryPojo> results = new java.util.ArrayList<FirewallExceptionRequestSummaryPojo>();
	FirewallExceptionAddRequestQueryFilterPojo addRequestFilterUsed;
	FirewallExceptionRemoveRequestQueryFilterPojo removeRequestFilterUsed;

	public FirewallExceptionRequestSummaryQueryResultPojo() {
		
	}

	public List<FirewallExceptionRequestSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<FirewallExceptionRequestSummaryPojo> results) {
		this.results = results;
	}

	public FirewallExceptionAddRequestQueryFilterPojo getAddRequestFilterUsed() {
		return addRequestFilterUsed;
	}

	public void setAddRequestFilterUsed(FirewallExceptionAddRequestQueryFilterPojo addRequestFilterUsed) {
		this.addRequestFilterUsed = addRequestFilterUsed;
	}

	public FirewallExceptionRemoveRequestQueryFilterPojo getRemoveRequestFilterUsed() {
		return removeRequestFilterUsed;
	}

	public void setRemoveRequestFilterUsed(FirewallExceptionRemoveRequestQueryFilterPojo removeRequestFilterUsed) {
		this.removeRequestFilterUsed = removeRequestFilterUsed;
	}

}
