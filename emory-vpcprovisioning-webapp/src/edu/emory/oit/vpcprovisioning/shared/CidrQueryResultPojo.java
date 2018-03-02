package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrQueryResultPojo extends SharedObject implements IsSerializable {
	CidrQueryFilterPojo filterUsed;
	// unassigned cidrs
//	List<CidrPojo> results = new java.util.ArrayList<CidrPojo>();
	// cidr assignments
//	List<CidrAssignmentSummaryPojo> assignmentSummaries = new java.util.ArrayList<CidrAssignmentSummaryPojo>();
	List<CidrSummaryPojo> results = new java.util.ArrayList<CidrSummaryPojo>();

	public CidrQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public CidrQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(CidrQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<CidrSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<CidrSummaryPojo> results) {
		this.results = results;
	}

//	public List<CidrAssignmentSummaryPojo> getAssignmentSummaries() {
//		return assignmentSummaries;
//	}
//
//	public void setAssignmentSummaries(List<CidrAssignmentSummaryPojo> assignmentSummaries) {
//		this.assignmentSummaries = assignmentSummaries;
//	}

}
