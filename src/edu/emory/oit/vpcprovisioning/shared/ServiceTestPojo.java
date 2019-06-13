package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ServiceTestPojo extends SharedObject implements IsSerializable, Comparable<ServiceTestPojo> {
	/*
<!ELEMENT ServiceTest (ServiceTestId?, SequenceNumber, Description, ServiceTestStep*, ServiceTestExpectedResult)>
	 */

	String serviceTestId;
	int sequenceNumber;
	String description;
	List<ServiceTestStepPojo> serviceTestSteps = new java.util.ArrayList<ServiceTestStepPojo>();
	String serviceTestExpectedResult;
	
	public static final ProvidesKey<ServiceTestPojo> KEY_PROVIDER = new ProvidesKey<ServiceTestPojo>() {
		@Override
		public Object getKey(ServiceTestPojo item) {
			return item == null ? null : item.getSequenceNumber();
		}
	};
	public ServiceTestPojo() {
		
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

	public String getServiceTestExpectedResult() {
		return serviceTestExpectedResult;
	}

	public void setServiceTestExpectedResult(String serviceTestExpectedResult) {
		this.serviceTestExpectedResult = serviceTestExpectedResult;
	}

	public List<ServiceTestStepPojo> getServiceTestSteps() {
		return serviceTestSteps;
	}

	public void setServiceTestSteps(List<ServiceTestStepPojo> serviceTestSteps) {
		this.serviceTestSteps = serviceTestSteps;
	}

	@Override
	public String toString() {
		return "Test: " + Integer.toString(sequenceNumber) + "-" + description + "  Expected to: " + serviceTestExpectedResult;
	}

	public boolean hasStep(int sequenceNumber) {
		for (ServiceTestStepPojo str : serviceTestSteps) {
			if (str.getSequenceNumber() == sequenceNumber) {
				return true;
			}
		}
		return false;
	}
	public void removeServiceTestStep(ServiceTestStepPojo sts) {
		serviceTestSteps.remove(sts);
	}

	@Override
	public int compareTo(ServiceTestPojo o) {
		if (o.getSequenceNumber() == sequenceNumber) {
			return 0;
		}
		else if (sequenceNumber > o.getSequenceNumber()) {
			return 1;
		}
		return -1;
	}
}
