package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleDeprovisioningQueryResultPojo extends SharedObject implements IsSerializable {
	RoleDeprovisioningQueryFilterPojo filterUsed;
	List<RoleProvisioningSummaryPojo> results;

	public RoleDeprovisioningQueryResultPojo() {
		
	}

	public RoleDeprovisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(RoleDeprovisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<RoleProvisioningSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<RoleProvisioningSummaryPojo> results) {
		this.results = results;
	}

}
