package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcpQueryResultPojo extends SharedObject implements IsSerializable {
	VpcpQueryFilterPojo filterUsed;
	List<VpcpSummaryPojo> results;

	public VpcpQueryResultPojo() {
		
	}

	public VpcpQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpcpQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<VpcpSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<VpcpSummaryPojo> results) {
		this.results = results;
	}

}
