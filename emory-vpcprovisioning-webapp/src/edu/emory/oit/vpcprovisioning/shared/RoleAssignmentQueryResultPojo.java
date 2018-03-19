package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleAssignmentQueryResultPojo extends SharedObject implements IsSerializable {
	RoleAssignmentQueryFilterPojo filterUsed;
	List<RoleAssignmentPojo> results = new java.util.ArrayList<RoleAssignmentPojo>();

	public RoleAssignmentQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public RoleAssignmentQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(RoleAssignmentQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<RoleAssignmentPojo> getResults() {
		return results;
	}

	public void setResults(List<RoleAssignmentPojo> results) {
		this.results = results;
	}

}
