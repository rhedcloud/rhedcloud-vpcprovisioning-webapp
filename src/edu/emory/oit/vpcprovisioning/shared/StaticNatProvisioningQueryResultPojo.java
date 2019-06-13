package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class StaticNatProvisioningQueryResultPojo extends SharedObject implements IsSerializable {
	List<StaticNatProvisioningPojo> results = new java.util.ArrayList<StaticNatProvisioningPojo>();
	StaticNatProvisioningQueryFilterPojo filterUsed;
	
	public StaticNatProvisioningQueryResultPojo() {
		
	}

	public List<StaticNatProvisioningPojo> getResults() {
		return results;
	}

	public void setResults(List<StaticNatProvisioningPojo> results) {
		this.results = results;
	}

	public StaticNatProvisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(StaticNatProvisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
