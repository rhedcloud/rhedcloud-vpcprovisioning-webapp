package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionQueryResultPojo extends SharedObject implements IsSerializable {
	List<VpnConnectionPojo> results = new java.util.ArrayList<VpnConnectionPojo>();
	VpnConnectionQueryFilterPojo filterUsed;
	
	public VpnConnectionQueryResultPojo() {
		
	}

	public List<VpnConnectionPojo> getResults() {
		return results;
	}

	public void setResults(List<VpnConnectionPojo> results) {
		this.results = results;
	}

	public VpnConnectionQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpnConnectionQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
