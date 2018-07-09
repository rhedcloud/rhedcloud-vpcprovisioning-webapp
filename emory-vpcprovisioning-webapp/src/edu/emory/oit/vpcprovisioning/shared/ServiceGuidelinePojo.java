package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceGuidelinePojo extends SharedObject implements IsSerializable {
	/*
ServiceId, SequenceNumber, ServiceGuidelineName, Description, AssessorId, AssessmentDatetime
	 */

	String serviceId;
	int sequenceNumber;
	String serviceGuidelineName;
	String description;
	String assessorId;
	Date assessmentDate;
	
	public ServiceGuidelinePojo() {
		// TODO Auto-generated constructor stub
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

}
