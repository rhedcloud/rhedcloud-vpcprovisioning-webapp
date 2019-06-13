package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProvisioningQueryResultPojo extends SharedObject implements IsSerializable {
	// TODO: change this to VpnConnectionProvisioningSummaryPojo
	List<VpnConnectionProvisioningSummaryPojo> results = new java.util.ArrayList<VpnConnectionProvisioningSummaryPojo>();
	VpnConnectionProvisioningQueryFilterPojo filterUsed;
	
	public VpnConnectionProvisioningQueryResultPojo() {
		
	}

	public List<VpnConnectionProvisioningSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<VpnConnectionProvisioningSummaryPojo> results) {
		this.results = results;
	}

	public VpnConnectionProvisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpnConnectionProvisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
