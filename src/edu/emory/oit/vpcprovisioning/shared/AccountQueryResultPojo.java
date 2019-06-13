package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountQueryResultPojo extends SharedObject implements IsSerializable {
	AccountQueryFilterPojo filterUsed;
	List<AccountPojo> results;

	public AccountQueryResultPojo() {
	}

	public AccountQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(AccountQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<AccountPojo> getResults() {
		return results;
	}

	public void setResults(List<AccountPojo> results) {
		this.results = results;
	}

}
