package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ServiceTestStepPojo extends SharedObject implements IsSerializable, Comparable<ServiceTestStepPojo> {
	/*
<!ELEMENT ServiceTestStep (ServiceTestStepId?, ServiceTestId, SequenceNumber, Description)>
	 */

	String serviceTestStepId;
	String serviceTestId;
	int sequenceNumber;
	String description;
	
	public static final ProvidesKey<ServiceTestStepPojo> KEY_PROVIDER = new ProvidesKey<ServiceTestStepPojo>() {
		@Override
		public Object getKey(ServiceTestStepPojo item) {
			return item == null ? null : item.getSequenceNumber();
		}
	};
	public ServiceTestStepPojo() {
		
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

	@Override
	public String toString() {
		return "Step: " + Integer.toString(sequenceNumber) + "-" + description;
	}

	@Override
	public int compareTo(ServiceTestStepPojo o) {
		if (o.getSequenceNumber() == sequenceNumber) {
			return 0;
		}
		else if (sequenceNumber > o.getSequenceNumber()) {
			return 1;
		}
		return -1;
	}
}
