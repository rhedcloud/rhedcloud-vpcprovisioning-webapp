package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserActionQueryResultPojo extends SharedObject implements IsSerializable {
	UserActionQueryFilterPojo filterUsed;
	List<UserActionPojo> results = new java.util.ArrayList<UserActionPojo>();

	public UserActionQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public UserActionQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(UserActionQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<UserActionPojo> getResults() {
		return results;
	}

	public void setResults(List<UserActionPojo> results) {
		this.results = results;
	}

}
