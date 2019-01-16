package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountProvisioningAuthorizationQueryResultPojo extends SharedObject implements IsSerializable {
	AccountProvisioningAuthorizationQueryFilterPojo filterUsed;
	List<AccountProvisioningAuthorizationPojo> results = new java.util.ArrayList<AccountProvisioningAuthorizationPojo>();
	
	public AccountProvisioningAuthorizationQueryResultPojo() {
		
	}

	public AccountProvisioningAuthorizationQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(AccountProvisioningAuthorizationQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<AccountProvisioningAuthorizationPojo> getResults() {
		return results;
	}

	public void setResults(List<AccountProvisioningAuthorizationPojo> results) {
		this.results = results;
	}

}
