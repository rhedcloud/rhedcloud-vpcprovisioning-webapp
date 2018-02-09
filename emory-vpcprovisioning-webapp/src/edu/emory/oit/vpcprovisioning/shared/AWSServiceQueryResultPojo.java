package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AWSServiceQueryResultPojo extends SharedObject implements IsSerializable {
	AWSServiceQueryFilterPojo filterUsed;
	List<AWSServicePojo> results;

	public AWSServiceQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public AWSServiceQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(AWSServiceQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<AWSServicePojo> getResults() {
		return results;
	}

	public void setResults(List<AWSServicePojo> results) {
		this.results = results;
	}

}
