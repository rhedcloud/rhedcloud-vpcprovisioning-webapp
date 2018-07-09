package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceTestPlanPojo extends SharedObject implements IsSerializable {
	String serviceId;
	List<ServiceTestRequirementPojo> serviceTestRequirements = new java.util.ArrayList<ServiceTestRequirementPojo>();

	public ServiceTestPlanPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public List<ServiceTestRequirementPojo> getServiceTestRequirements() {
		return serviceTestRequirements;
	}

	public void setServiceTestRequirements(List<ServiceTestRequirementPojo> serviceTestRequirements) {
		this.serviceTestRequirements = serviceTestRequirements;
	}

}
