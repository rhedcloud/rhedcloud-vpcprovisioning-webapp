package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcpQueryResultPojo extends SharedObject implements IsSerializable {
	VpcpQueryFilterPojo filterUsed;
	List<VpcpPojo> results;

	public VpcpQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public VpcpQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpcpQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<VpcpPojo> getResults() {
		return results;
	}

	public void setResults(List<VpcpPojo> results) {
		this.results = results;
	}

}
