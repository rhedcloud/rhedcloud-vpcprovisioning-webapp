package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfileQueryResultPojo extends SharedObject implements IsSerializable {
	List<VpnConnectionProfilePojo> results = new java.util.ArrayList<VpnConnectionProfilePojo>();
	VpnConnectionProfileQueryResultPojo filterUsed;
	
	public VpnConnectionProfileQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<VpnConnectionProfilePojo> getResults() {
		return results;
	}

	public void setResults(List<VpnConnectionProfilePojo> results) {
		this.results = results;
	}

	public VpnConnectionProfileQueryResultPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpnConnectionProfileQueryResultPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
