package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountProvisioningQueryResultPojo extends SharedObject implements IsSerializable {
	AccountProvisioningQueryFilterPojo filterUsed;
	List<AccountProvisioningSummaryPojo> results;

	public AccountProvisioningQueryResultPojo() {
		
	}

	public AccountProvisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(AccountProvisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<AccountProvisioningSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<AccountProvisioningSummaryPojo> results) {
		this.results = results;
	}

}
