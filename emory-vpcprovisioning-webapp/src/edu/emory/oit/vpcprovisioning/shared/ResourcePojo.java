package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ResourcePojo extends SharedObject implements IsSerializable, Comparable<ResourcePojo> {
	
	String resourceDN;
	String resourceName;
	String resourceDescription;
	EntitlementPojo entitlement;

	public ResourcePojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(ResourcePojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getResourceDN() {
		return resourceDN;
	}

	public void setResourceDN(String resourceDN) {
		this.resourceDN = resourceDN;
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
