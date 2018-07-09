package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceTestStepPojo extends SharedObject implements IsSerializable {
	/*
ServiceTestStepId, ServiceTestId, SequenceNumber, Description
	 */

	String serviceTestStepId;
	String serviceTestId;
	int sequenceNumber;
	String description;
	
	public ServiceTestStepPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getServiceTestStepId() {
		return serviceTestStepId;
	}

	public void setServiceTestStepId(String serviceTestStepId) {
		this.serviceTestStepId = serviceTestStepId;
	}

	public String getServiceTestId() {
		return serviceTestId;
	}

	public void setServiceTestId(String serviceTestId) {
		this.serviceTestId = serviceTestId;
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

}
