package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class SecurityRiskPojo extends SharedObject implements IsSerializable {
	/*
		SecurityRiskId, 
		ServiceId, 
		SequenceNumber, 
		ServiceRiskName, 
		RiskLevel, 
		Description, 
		AssessorId, 
		AssessmentDatetime, 
		Countermeasure*
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
	List<CounterMeasurePojo> couterMeasures = new java.util.ArrayList<CounterMeasurePojo>();
	
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

	public List<CounterMeasurePojo> getCouterMeasures() {
		return couterMeasures;
	}

	public void setCouterMeasures(List<CounterMeasurePojo> couterMeasures) {
		this.couterMeasures = couterMeasures;
	}

}
