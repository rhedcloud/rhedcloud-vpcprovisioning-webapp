package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class StaticNatProvisioningSummaryQueryFilterPojo extends SharedObject implements IsSerializable {

	StaticNatProvisioningQueryFilterPojo provisionedFilter;
	StaticNatDeprovisioningQueryFilterPojo deProvisionedFilter;

	public StaticNatProvisioningSummaryQueryFilterPojo() {
		
	}

	public StaticNatProvisioningQueryFilterPojo getProvisionedFilter() {
		return provisionedFilter;
	}

	public void setProvisionedFilter(StaticNatProvisioningQueryFilterPojo provisionedFilter) {
		this.provisionedFilter = provisionedFilter;
	}

	public StaticNatDeprovisioningQueryFilterPojo getDeProvisionedFilter() {
		return deProvisionedFilter;
	}

	public void setDeProvisionedFilter(StaticNatDeprovisioningQueryFilterPojo deProvisionedFilter) {
		this.deProvisionedFilter = deProvisionedFilter;
	}

}
