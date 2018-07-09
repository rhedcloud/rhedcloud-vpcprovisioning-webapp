package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceSecurityAssessmentPojo extends SharedObject implements IsSerializable {
	/*
		ServiceSecurityAssessmentId?, 
		ServiceId+, 
		Status, 
		SecurityRisk*, 
		ServiceControl*, 
		ServiceGuideline*, 
		ServiceTestPlan?
	 */

	String serviceSecurityAssessmentId;
	List<String> serviceId;
	String status;
	List<SecurityRiskPojo> securityRisks = new java.util.ArrayList<SecurityRiskPojo>();
	List<ServiceControlPojo> serviceControls = new java.util.ArrayList<ServiceControlPojo>();
	List<ServiceGuidelinePojo> serviceGuidelines = new java.util.ArrayList<ServiceGuidelinePojo>();
	ServiceTestPlanPojo serviceTestPlan;
	
	public ServiceSecurityAssessmentPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getServiceSecurityAssessmentId() {
		return serviceSecurityAssessmentId;
	}

	public void setServiceSecurityAssessmentId(String serviceSecurityAssessmentId) {
		this.serviceSecurityAssessmentId = serviceSecurityAssessmentId;
	}

	public List<String> getServiceId() {
		return serviceId;
	}

	public void setServiceId(List<String> serviceId) {
		this.serviceId = serviceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SecurityRiskPojo> getSecurityRisks() {
		return securityRisks;
	}

	public void setSecurityRisks(List<SecurityRiskPojo> securityRisks) {
		this.securityRisks = securityRisks;
	}

	public List<ServiceControlPojo> getServiceControls() {
		return serviceControls;
	}

	public void setServiceControls(List<ServiceControlPojo> serviceControls) {
		this.serviceControls = serviceControls;
	}

	public List<ServiceGuidelinePojo> getServiceGuidelines() {
		return serviceGuidelines;
	}

	public void setServiceGuidelines(List<ServiceGuidelinePojo> serviceGuidelines) {
		this.serviceGuidelines = serviceGuidelines;
	}

	public ServiceTestPlanPojo getServiceTestPlan() {
		return serviceTestPlan;
	}

	public void setServiceTestPlan(ServiceTestPlanPojo serviceTestPlan) {
		this.serviceTestPlan = serviceTestPlan;
	}

}
