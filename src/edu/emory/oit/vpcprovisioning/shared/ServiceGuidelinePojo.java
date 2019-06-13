package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ServiceGuidelinePojo extends SharedObject implements IsSerializable, Comparable<ServiceGuidelinePojo> {
	/*
ServiceId, SequenceNumber, ServiceGuidelineName, Description, AssessorId, AssessmentDatetime
	 */

	public static final ProvidesKey<ServiceGuidelinePojo> KEY_PROVIDER = new ProvidesKey<ServiceGuidelinePojo>() {
		@Override
		public Object getKey(ServiceGuidelinePojo item) {
			return item == null ? null : item.getServiceGuidelineName();
		}
	};
	String serviceId;
	int sequenceNumber;
	String serviceGuidelineName;
	String description;
	String assessorId;
	Date assessmentDate;
	
	public ServiceGuidelinePojo() {
		
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getServiceGuidelineName() {
		return serviceGuidelineName;
	}

	public void setServiceGuidelineName(String serviceGuidelineName) {
		this.serviceGuidelineName = serviceGuidelineName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssessorId() {
		return assessorId;
	}

	public void setAssessorId(String assessorId) {
		this.assessorId = assessorId;
	}

	public Date getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(Date assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	@Override
	public int compareTo(ServiceGuidelinePojo o) {
		if (o.getSequenceNumber() == sequenceNumber) {
			return 0;
		}
		else if (sequenceNumber > o.getSequenceNumber()) {
			return 1;
		}
		return -1;
	}

}
