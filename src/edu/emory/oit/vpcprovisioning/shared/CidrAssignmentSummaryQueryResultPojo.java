package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

@SuppressWarnings("serial")
public class CidrAssignmentSummaryQueryResultPojo extends SharedObject {
	CidrAssignmentSummaryQueryFilterPojo filterUsed;
	List<CidrAssignmentSummaryPojo> results;

	public CidrAssignmentSummaryQueryResultPojo() {
	}

	public CidrAssignmentSummaryQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(CidrAssignmentSummaryQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<CidrAssignmentSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<CidrAssignmentSummaryPojo> results) {
		this.results = results;
	}

}
