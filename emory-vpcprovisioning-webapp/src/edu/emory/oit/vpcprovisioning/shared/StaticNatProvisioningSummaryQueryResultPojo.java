package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class StaticNatProvisioningSummaryQueryResultPojo extends SharedObject implements IsSerializable {
	List<StaticNatProvisioningSummaryPojo> results = new java.util.ArrayList<StaticNatProvisioningSummaryPojo>();
	StaticNatProvisioningQueryFilterPojo provisionedFilterUsed;
	StaticNatDeprovisioningQueryFilterPojo deProvisionedFilterUsed;

	public StaticNatProvisioningSummaryQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<StaticNatProvisioningSummaryPojo> getResults() {
		return results;
	}

	public void setResults(List<StaticNatProvisioningSummaryPojo> results) {
		this.results = results;
	}

	public StaticNatProvisioningQueryFilterPojo getProvisionedFilterUsed() {
		return provisionedFilterUsed;
	}

	public void setProvisionedFilterUsed(StaticNatProvisioningQueryFilterPojo provisionedFilterUsed) {
		this.provisionedFilterUsed = provisionedFilterUsed;
	}

	public StaticNatDeprovisioningQueryFilterPojo getDeProvisionedFilterUsed() {
		return deProvisionedFilterUsed;
	}

	public void setDeProvisionedFilterUsed(StaticNatDeprovisioningQueryFilterPojo deProvisionedFilterUsed) {
		this.deProvisionedFilterUsed = deProvisionedFilterUsed;
	}

}
