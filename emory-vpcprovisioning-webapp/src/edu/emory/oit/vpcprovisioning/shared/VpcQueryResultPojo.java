package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcQueryResultPojo extends SharedObject implements IsSerializable {
	VpcQueryFilterPojo filterUsed;
	List<VpcPojo> results;

	public VpcQueryResultPojo() {
		
	}

	public VpcQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpcQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<VpcPojo> getResults() {
		return results;
	}

	public void setResults(List<VpcPojo> results) {
		this.results = results;
	}

}
