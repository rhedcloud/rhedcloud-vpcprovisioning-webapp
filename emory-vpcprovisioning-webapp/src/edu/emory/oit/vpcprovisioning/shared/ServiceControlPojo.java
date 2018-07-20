package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ServiceControlPojo extends SharedObject implements IsSerializable {
	/*
ServiceId, ServiceControlId, SequenceNumber, ServiceControlName, Description, AssessorId, AssessmentDatetime, Verifier?, VerificationDatetime?
	 */

	public static final ProvidesKey<ServiceControlPojo> KEY_PROVIDER = new ProvidesKey<ServiceControlPojo>() {
		@Override
		public Object getKey(ServiceControlPojo item) {
			return item == null ? null : item.getServiceControlName();
		}
	};
	String serviceId;
	String serviceControlId;
	int sequenceNumber;
	String serviceControlName;
	String description;
	String assessorId;
	Date assessmentDate;
	String verifier;
	Date verificationDate;
	
	public ServiceControlPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceControlId() {
		return serviceControlId;
	}

	public void setServiceControlId(String serviceControlId) {
		this.serviceControlId = serviceControlId;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getServiceControlName() {
		return serviceControlName;
	}

	public void setServiceControlName(String serviceControlName) {
		this.serviceControlName = serviceControlName;
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

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public Date getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}

}
