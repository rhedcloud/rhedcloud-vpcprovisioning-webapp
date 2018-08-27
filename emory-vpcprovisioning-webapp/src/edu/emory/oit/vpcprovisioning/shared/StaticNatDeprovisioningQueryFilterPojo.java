package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class StaticNatDeprovisioningQueryFilterPojo extends SharedObject implements IsSerializable {

	String provisioningId;
	String type;

	public StaticNatDeprovisioningQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
