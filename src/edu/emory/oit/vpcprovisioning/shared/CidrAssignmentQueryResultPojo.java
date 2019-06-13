package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrAssignmentQueryResultPojo extends SharedObject implements IsSerializable {
	CidrAssignmentQueryFilterPojo filterUsed;
	List<CidrAssignmentPojo> results;

	public CidrAssignmentQueryResultPojo() {
	}

	public CidrAssignmentQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(CidrAssignmentQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<CidrAssignmentPojo> getResults() {
		return results;
	}

	public void setResults(List<CidrAssignmentPojo> results) {
		this.results = results;
	}

}
