package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ResourceQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	String resourceDN;

	public ResourceQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getResourceDN() {
		return resourceDN;
	}

	public void setResourceDN(String resourceDN) {
		this.resourceDN = resourceDN;
	}

}
