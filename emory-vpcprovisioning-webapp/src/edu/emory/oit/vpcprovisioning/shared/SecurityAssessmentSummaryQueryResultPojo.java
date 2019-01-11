package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SecurityAssessmentSummaryQueryResultPojo extends SharedObject implements IsSerializable {
	SecurityAssessmentSummaryQueryFilterPojo filterUsed;
	List<SecurityAssessmentSummaryPojo> results = new java.util.ArrayList<SecurityAssessmentSummaryPojo>();

	public SecurityAssessmentSummaryQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public SecurityAssessmentSummaryQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(SecurityAssessmentSummaryQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<SecurityAssessmentSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<SecurityAssessmentSummaryPojo> results) {
		this.results = results;
	}

}
