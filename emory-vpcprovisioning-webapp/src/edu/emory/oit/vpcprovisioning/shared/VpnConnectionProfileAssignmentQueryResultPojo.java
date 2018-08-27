package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfileAssignmentQueryResultPojo extends SharedObject implements IsSerializable {
	List<VpnConnectionProfileAssignmentPojo> results = new java.util.ArrayList<VpnConnectionProfileAssignmentPojo>();
	VpnConnectionProfileAssignmentQueryResultPojo filterUsed;
	
	public VpnConnectionProfileAssignmentQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<VpnConnectionProfileAssignmentPojo> getResults() {
		return results;
	}

	public void setResults(List<VpnConnectionProfileAssignmentPojo> results) {
		this.results = results;
	}

	public VpnConnectionProfileAssignmentQueryResultPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(VpnConnectionProfileAssignmentQueryResultPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
