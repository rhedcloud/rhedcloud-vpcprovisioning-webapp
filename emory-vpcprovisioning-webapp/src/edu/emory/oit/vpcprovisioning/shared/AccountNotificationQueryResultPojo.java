package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountNotificationQueryResultPojo extends SharedObject implements IsSerializable {
	AccountNotificationQueryFilterPojo filterUsed;
	List<AccountNotificationPojo> results;

	public AccountNotificationQueryResultPojo() {
	}

	public AccountNotificationQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(AccountNotificationQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<AccountNotificationPojo> getResults() {
		return results;
	}

	public void setResults(List<AccountNotificationPojo> results) {
		this.results = results;
	}

}
