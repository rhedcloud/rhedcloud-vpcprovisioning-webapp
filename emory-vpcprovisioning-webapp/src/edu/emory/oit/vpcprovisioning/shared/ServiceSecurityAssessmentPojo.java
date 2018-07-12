package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

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

	public static final ProvidesKey<ServiceSecurityAssessmentPojo> KEY_PROVIDER = new ProvidesKey<ServiceSecurityAssessmentPojo>() {
		@Override
		public Object getKey(ServiceSecurityAssessmentPojo item) {
			return item == null ? null : item.getServiceSecurityAssessmentId();
		}
	};
	String serviceSecurityAssessmentId;
	List<String> serviceIds = new java.util.ArrayList<String>();
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

	public List<String> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(List<String> serviceId) {
		this.serviceIds = serviceId;
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
