package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionDeprovisioningQueryResultPojo extends SharedObject implements IsSerializable {
	List<VpnConnectionDeprovisioningPojo> results = new java.util.ArrayList<VpnConnectionDeprovisioningPojo>();
	VpnConnectionDeprovisioningQueryFilterPojo filterUsed;
	
	public VpnConnectionDeprovisioningQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<VpnConnectionDeprovisioningPojo> getResults() {
		return results;
	}

	public void setResults(List<VpnConnectionDeprovisioningPojo> results) {
		this.results = results;
	}

	public VpnConnectionDeprovisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpnConnectionDeprovisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
