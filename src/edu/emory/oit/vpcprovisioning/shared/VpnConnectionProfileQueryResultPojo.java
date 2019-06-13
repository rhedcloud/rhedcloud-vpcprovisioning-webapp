package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfileQueryResultPojo extends SharedObject implements IsSerializable {
	List<VpnConnectionProfileSummaryPojo> results = new java.util.ArrayList<VpnConnectionProfileSummaryPojo>();
	VpnConnectionProfileQueryFilterPojo filterUsed;
	
	public VpnConnectionProfileQueryResultPojo() {
		
	}

	public List<VpnConnectionProfileSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<VpnConnectionProfileSummaryPojo> results) {
		this.results = results;
	}

	public VpnConnectionProfileQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpnConnectionProfileQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
