package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ResourceQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	String resourceDN;

	public ResourceQueryFilterPojo() {
		
	}

	public String getResourceDN() {
		return resourceDN;
	}

	public void setResourceDN(String resourceDN) {
		this.resourceDN = resourceDN;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
