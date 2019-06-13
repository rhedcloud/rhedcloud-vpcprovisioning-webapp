package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

@SuppressWarnings("serial")
public class ElasticIpAssignmentSummaryQueryResultPojo extends SharedObject {
	ElasticIpAssignmentSummaryQueryFilterPojo filterUsed;
	List<ElasticIpAssignmentSummaryPojo> results;

	public ElasticIpAssignmentSummaryQueryResultPojo() {
	}

	public ElasticIpAssignmentSummaryQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ElasticIpAssignmentSummaryQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ElasticIpAssignmentSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<ElasticIpAssignmentSummaryPojo> results) {
		this.results = results;
	}

}
