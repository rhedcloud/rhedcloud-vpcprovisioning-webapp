package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class SecurityRiskPojo extends SharedObject implements IsSerializable, Comparable<SecurityRiskPojo> {
	/*
		<!ELEMENT SecurityRisk (
			SecurityRiskId?, 
			ServiceId, 
			SequenceNumber, 
			ServiceRiskName, 
			RiskLevel, 
			Description, 
			AssessorId, 
			AssessmentDatetime, 
			ServiceControl*)>
	 */

	public static final ProvidesKey<SecurityRiskPojo> KEY_PROVIDER = new ProvidesKey<SecurityRiskPojo>() {
		@Override
		public Object getKey(SecurityRiskPojo item) {
			return item == null ? null : item.getSecurityRiskName();
		}
	};
	String securityRiskId;
	String serviceId;
	int sequenceNumber;
	String securityRiskName;
	String riskLevel;
	String description;
	String assessorId;
	Date assessmentDate;
	List<ServiceControlPojo> serviceControls = new java.util.ArrayList<ServiceControlPojo>();
	
	public SecurityRiskPojo() {
		
	}

	public String getSecurityRiskId() {
		return securityRiskId;
	}

	public void setSecurityRiskId(String securityRiskId) {
		this.securityRiskId = securityRiskId;
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

	public String getSecurityRiskName() {
		return securityRiskName;
	}

	public void setSecurityRiskName(String securityRiskName) {
		this.securityRiskName = securityRiskName;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
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

	public List<ServiceControlPojo> getServiceControls() {
		return serviceControls;
	}

	public void setServiceControls(List<ServiceControlPojo> serviceControls) {
		this.serviceControls = serviceControls;
	}

	@Override
	public int compareTo(SecurityRiskPojo o) {
		if (o.getSequenceNumber() == sequenceNumber) {
			return 0;
		}
		else if (sequenceNumber > o.getSequenceNumber()) {
			return 1;
		}
		return -1;
	}

}
