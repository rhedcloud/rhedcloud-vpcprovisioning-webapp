package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserProfileQueryResultPojo extends SharedObject implements IsSerializable {
	UserProfileQueryFilterPojo filterUsed;
	List<UserProfilePojo> results;

	public UserProfileQueryResultPojo() {
		
	}

	public UserProfileQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(UserProfileQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<UserProfilePojo> getResults() {
		return results;
	}

	public void setResults(List<UserProfilePojo> results) {
		this.results = results;
	}

}
