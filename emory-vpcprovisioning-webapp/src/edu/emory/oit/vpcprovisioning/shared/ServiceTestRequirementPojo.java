package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceTestRequirementPojo extends SharedObject implements IsSerializable {
	/*
ServiceTestRequirementId, SequenceNumber, Description, ServiceTest*
	 */

	String serviceTestRequirementId;
	int sequenceNumber;
	String description;
	List<ServiceTestPojo> serviceTests = new java.util.ArrayList<ServiceTestPojo>();
	
	public ServiceTestRequirementPojo() {
		// TODO Auto-generated constructor stub
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

}
