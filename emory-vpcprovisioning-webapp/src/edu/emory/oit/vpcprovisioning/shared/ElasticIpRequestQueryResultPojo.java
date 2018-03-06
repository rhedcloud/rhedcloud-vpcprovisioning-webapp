package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpRequestQueryResultPojo extends SharedObject implements IsSerializable {
	ElasticIpRequestQueryFilterPojo filterUsed;
	List<ElasticIpRequestPojo> results;

	public ElasticIpRequestQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public ElasticIpRequestQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ElasticIpRequestQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ElasticIpRequestPojo> getResults() {
		return results;
	}

	public void setResults(List<ElasticIpRequestPojo> results) {
		this.results = results;
	}

}
