package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CustomRoleQueryResultPojo extends SharedObject implements IsSerializable {
	CustomRoleQueryFilterPojo filterUsed;
	List<CustomRolePojo> results = new java.util.ArrayList<CustomRolePojo>();

	public CustomRoleQueryResultPojo() {
		
	}

	public CustomRoleQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(CustomRoleQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<CustomRolePojo> getResults() {
		return results;
	}

	public void setResults(List<CustomRolePojo> results) {
		this.results = results;
	}

}
