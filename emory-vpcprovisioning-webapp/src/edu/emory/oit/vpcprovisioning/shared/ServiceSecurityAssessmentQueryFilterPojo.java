package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceSecurityAssessmentQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
ServiceSecurityAssessmentQuerySpecification (Comparison*, QueryLanguage?, SecurityRiskId?, ServiceId?, RiskLevel?)>
	 */

	String assessmentId;
	String serviceId;
	
	public ServiceSecurityAssessmentQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "Assessment Id: " + assessmentId + ", Service ID: " + serviceId;
	}

}
