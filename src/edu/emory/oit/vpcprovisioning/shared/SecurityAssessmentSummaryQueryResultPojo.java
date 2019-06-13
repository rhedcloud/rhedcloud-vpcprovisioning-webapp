package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SecurityAssessmentSummaryQueryResultPojo extends SharedObject implements IsSerializable {
	SecurityAssessmentSummaryQueryFilterPojo filterUsed;
	List<SecurityAssessmentSummaryPojo> results = new java.util.ArrayList<SecurityAssessmentSummaryPojo>();
	AWSServiceSummaryPojo serviceSummary;
	
	public SecurityAssessmentSummaryQueryResultPojo() {
		
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

	public AWSServiceSummaryPojo getServiceSummary() {
		return serviceSummary;
	}

	public void setServiceSummary(AWSServiceSummaryPojo serviceSummary) {
		this.serviceSummary = serviceSummary;
	}

}
