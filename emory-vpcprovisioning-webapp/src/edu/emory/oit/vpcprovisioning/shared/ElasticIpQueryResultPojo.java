package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpQueryResultPojo extends SharedObject implements IsSerializable {
	ElasticIpQueryFilterPojo filterUsed;
	List<ElasticIpPojo> results;

	public ElasticIpQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public ElasticIpQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ElasticIpQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ElasticIpPojo> getResults() {
		return results;
	}

	public void setResults(List<ElasticIpPojo> results) {
		this.results = results;
	}

}
