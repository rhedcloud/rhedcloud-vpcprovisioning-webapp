package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ServiceTestRequirementPojo extends SharedObject implements IsSerializable, Comparable<ServiceTestRequirementPojo> {
	/*
<!ELEMENT ServiceTestRequirement (ServiceTestRequirementId?, SequenceNumber, Description, ServiceTest*)>
	 */

	String serviceTestRequirementId;
	int sequenceNumber;
	String description;
	List<ServiceTestPojo> serviceTests = new java.util.ArrayList<ServiceTestPojo>();
	
	public static final ProvidesKey<ServiceTestRequirementPojo> KEY_PROVIDER = new ProvidesKey<ServiceTestRequirementPojo>() {
		@Override
		public Object getKey(ServiceTestRequirementPojo item) {
			return item == null ? null : item.getSequenceNumber();
		}
	};
	public ServiceTestRequirementPojo() {
		
	}

	public String getServiceTestRequirementId() {
		return serviceTestRequirementId;
	}

	public void setServiceTestRequirementId(String serviceTestRequirementId) {
		this.serviceTestRequirementId = serviceTestRequirementId;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ServiceTestPojo> getServiceTests() {
		return serviceTests;
	}

	public void setServiceTests(List<ServiceTestPojo> serviceTests) {
		this.serviceTests = serviceTests;
	}

	@Override
	public String toString() {
		return "Requirement: " + Integer.toString(sequenceNumber) + "-" + description;
	}

	public boolean hasTest(int sequenceNumber) {
		for (ServiceTestPojo str : serviceTests) {
			if (str.getSequenceNumber() == sequenceNumber) {
				return true;
			}
		}
		return false;
	}
	public void removeServiceTest(ServiceTestPojo st) {
		serviceTests.remove(st);
	}

	@Override
	public int compareTo(ServiceTestRequirementPojo o) {
		if (o.getSequenceNumber() == sequenceNumber) {
			return 0;
		}
		else if (sequenceNumber > o.getSequenceNumber()) {
			return 1;
		}
		return -1;
	}
}
