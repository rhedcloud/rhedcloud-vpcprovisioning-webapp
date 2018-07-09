package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceTestPojo extends SharedObject implements IsSerializable {
	/*
ServiceTestId, SequenceNumber, Description, ServiceTestStep*, ServiceTestExpectedResult
	 */

	String serviceTestId;
	int sequenceNumber;
	String description;
	ServiceTestStepPojo serviceTestStep;
	String serviceTestExpectedResult;
	
	public ServiceTestPojo() {
		// TODO Auto-generated constructor stub
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

	public ServiceTestStepPojo getServiceTestStep() {
		return serviceTestStep;
	}

	public void setServiceTestStep(ServiceTestStepPojo serviceTestStep) {
		this.serviceTestStep = serviceTestStep;
	}

	public String getServiceTestExpectedResult() {
		return serviceTestExpectedResult;
	}

	public void setServiceTestExpectedResult(String serviceTestExpectedResult) {
		this.serviceTestExpectedResult = serviceTestExpectedResult;
	}

}
