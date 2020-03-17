package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceFilterPojo extends SharedObject implements IsSerializable {
	String serviceName;
	List<String> resourceNames = new java.util.ArrayList<String>();
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List<String> getResourceNames() {
		return resourceNames;
	}
	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}
	
}
