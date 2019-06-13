package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpQueryResultPojo extends SharedObject implements IsSerializable {
	ElasticIpQueryFilterPojo filterUsed;
	List<ElasticIpSummaryPojo> results;

	public ElasticIpQueryResultPojo() {
	}

	public ElasticIpQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ElasticIpQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ElasticIpSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<ElasticIpSummaryPojo> results) {
		this.results = results;
	}

}
