package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleProvisioningQueryResultPojo extends SharedObject implements IsSerializable {
	RoleProvisioningQueryFilterPojo filterUsed;
	List<RoleProvisioningSummaryPojo> results;

	public RoleProvisioningQueryResultPojo() {
		
	}

	public RoleProvisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(RoleProvisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<RoleProvisioningSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<RoleProvisioningSummaryPojo> results) {
		this.results = results;
	}

}
