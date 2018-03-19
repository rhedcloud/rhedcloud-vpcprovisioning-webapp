package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ResourceRequisitionPojo extends SharedObject implements IsSerializable {
	//ResourceName, ResourceDescription, Entitlement
	String resourceName;
	String resourceDescription;
	EntitlementPojo entitlement;

	public ResourceRequisitionPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceDescription() {
		return resourceDescription;
	}

	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	public EntitlementPojo getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(EntitlementPojo entitlement) {
		this.entitlement = entitlement;
	}

}
