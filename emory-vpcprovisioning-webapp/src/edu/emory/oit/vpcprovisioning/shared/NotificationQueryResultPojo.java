package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class NotificationQueryResultPojo extends SharedObject implements IsSerializable {
	NotificationQueryFilterPojo filterUsed;
	List<NotificationPojo> results;

	public NotificationQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public NotificationQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(NotificationQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<NotificationPojo> getResults() {
		return results;
	}

	public void setResults(List<NotificationPojo> results) {
		this.results = results;
	}

}
