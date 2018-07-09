package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserNotificationQueryResultPojo extends SharedObject implements IsSerializable {
	UserNotificationQueryFilterPojo filterUsed;
	List<UserNotificationPojo> results;

	public UserNotificationQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public UserNotificationQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(UserNotificationQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<UserNotificationPojo> getResults() {
		return results;
	}

	public void setResults(List<UserNotificationPojo> results) {
		this.results = results;
	}

}
