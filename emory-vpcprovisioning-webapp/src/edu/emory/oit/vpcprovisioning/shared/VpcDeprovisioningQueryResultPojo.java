package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcDeprovisioningQueryResultPojo extends SharedObject implements IsSerializable {
	VpcpQueryFilterPojo filterUsed;
	List<VpcpPojo> results;

	public VpcDeprovisioningQueryResultPojo() {
		
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
