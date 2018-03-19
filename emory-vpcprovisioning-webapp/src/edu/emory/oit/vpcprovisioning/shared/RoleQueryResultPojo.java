package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleQueryResultPojo extends SharedObject implements IsSerializable {
	RoleQueryFilterPojo filterUsed;
	List<RolePojo> results = new java.util.ArrayList<RolePojo>();

	public RoleQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public RoleQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(RoleQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<RolePojo> getResults() {
		return results;
	}

	public void setResults(List<RolePojo> results) {
		this.results = results;
	}

}
