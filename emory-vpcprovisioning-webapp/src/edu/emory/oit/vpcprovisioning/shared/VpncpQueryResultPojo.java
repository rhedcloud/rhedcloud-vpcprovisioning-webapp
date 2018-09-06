package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpncpQueryResultPojo extends SharedObject implements IsSerializable {
	List<VpncpPojo> results = new java.util.ArrayList<VpncpPojo>();
	VpncpQueryFilterPojo filterUsed;
	
	public VpncpQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<VpncpPojo> getResults() {
		return results;
	}

	public void setResults(List<VpncpPojo> results) {
		this.results = results;
	}

	public VpncpQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpncpQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
