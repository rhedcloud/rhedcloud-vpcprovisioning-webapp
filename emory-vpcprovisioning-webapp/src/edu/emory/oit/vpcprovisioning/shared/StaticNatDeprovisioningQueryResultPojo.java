package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class StaticNatDeprovisioningQueryResultPojo extends SharedObject implements IsSerializable {
	List<StaticNatDeprovisioningPojo> results = new java.util.ArrayList<StaticNatDeprovisioningPojo>();
	StaticNatDeprovisioningQueryFilterPojo filterUsed;
	
	public StaticNatDeprovisioningQueryResultPojo() {
		
	}

	public List<StaticNatDeprovisioningPojo> getResults() {
		return results;
	}

	public void setResults(List<StaticNatDeprovisioningPojo> results) {
		this.results = results;
	}

	public StaticNatDeprovisioningQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(StaticNatDeprovisioningQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
