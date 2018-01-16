package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpAssignmentQueryResultPojo extends SharedObject implements IsSerializable {
	ElasticIpAssignmentQueryFilterPojo filterUsed;
	List<ElasticIpAssignmentPojo> results;

	public ElasticIpAssignmentQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public ElasticIpAssignmentQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ElasticIpAssignmentQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ElasticIpAssignmentPojo> getResults() {
		return results;
	}

	public void setResults(List<ElasticIpAssignmentPojo> results) {
		this.results = results;
	}

}
