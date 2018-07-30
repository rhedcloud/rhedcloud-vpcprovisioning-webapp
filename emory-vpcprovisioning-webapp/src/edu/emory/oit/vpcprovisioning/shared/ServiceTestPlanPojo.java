package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceTestPlanPojo extends SharedObject implements IsSerializable {
	/*
<!ELEMENT ServiceTestPlan (ServiceId, ServiceTestRequirement*)>
	 */
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

	public boolean hasRequirement(int sequenceNumber) {
		for (ServiceTestRequirementPojo str : serviceTestRequirements) {
			if (str.getSequenceNumber() == sequenceNumber) {
				return true;
			}
		}
		return false;
	}
	public void removeServiceRequirement(ServiceTestRequirementPojo req) { 
		serviceTestRequirements.remove(req);
//		reqLoop: for (ServiceTestRequirementPojo str : serviceTestRequirements) {
//			if (str.getSequenceNumber() == req.getSequenceNumber()) {
//				serviceTestRequirements.remove(str);
//				break reqLoop;
//			}
//		}
	}
}
