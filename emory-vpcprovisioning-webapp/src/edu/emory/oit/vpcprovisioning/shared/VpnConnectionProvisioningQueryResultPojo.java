package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProvisioningQueryResultPojo extends SharedObject implements IsSerializable {
	List<VpnConnectionProvisioningPojo> results = new java.util.ArrayList<VpnConnectionProvisioningPojo>();
	VpnConnectionProvisioningQueryFilterPojo filterUsed;
	
	public VpnConnectionProvisioningQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<VpnConnectionProvisioningPojo> getResults() {
		return results;
	}

	public void setResults(List<VpnConnectionProvisioningPojo> results) {
		this.results = results;
	}

	public VpnConnectionProvisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpnConnectionProvisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
