package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ServiceSecurityAssessmentPojo extends SharedObject implements IsSerializable, Comparable<ServiceSecurityAssessmentPojo> {
	/*
	<!ELEMENT ServiceSecurityAssessment (
		ServiceSecurityAssessmentId?, 
		ServiceId+, 
		Status, 
		SecurityRisk*, 
		ServiceTestPlan?)>
	 */

	public static final ProvidesKey<ServiceSecurityAssessmentPojo> KEY_PROVIDER = new ProvidesKey<ServiceSecurityAssessmentPojo>() {
		@Override
		public Object getKey(ServiceSecurityAssessmentPojo item) {
			return item == null ? null : item.getServiceSecurityAssessmentId();
		}
	};
	String serviceSecurityAssessmentId;
	// ids of services that share this assessment
	List<String> serviceIds = new java.util.ArrayList<String>();
	List<String> serviceNames = new java.util.ArrayList<String>();
	String status;
	List<SecurityRiskPojo> securityRisks = new java.util.ArrayList<SecurityRiskPojo>();
//	List<ServiceControlPojo> serviceControls = new java.util.ArrayList<ServiceControlPojo>();
//	List<ServiceGuidelinePojo> serviceGuidelines = new java.util.ArrayList<ServiceGuidelinePojo>();
	ServiceTestPlanPojo serviceTestPlan;
	ServiceSecurityAssessmentPojo baseline;
	
	public ServiceSecurityAssessmentPojo() {
		
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

//	public List<ServiceControlPojo> getServiceControls() {
//		return serviceControls;
//	}
//
//	public void setServiceControls(List<ServiceControlPojo> serviceControls) {
//		this.serviceControls = serviceControls;
//	}
//
//	public List<ServiceGuidelinePojo> getServiceGuidelines() {
//		return serviceGuidelines;
//	}
//
//	public void setServiceGuidelines(List<ServiceGuidelinePojo> serviceGuidelines) {
//		this.serviceGuidelines = serviceGuidelines;
//	}

	public ServiceTestPlanPojo getServiceTestPlan() {
		return serviceTestPlan;
	}

	public void setServiceTestPlan(ServiceTestPlanPojo serviceTestPlan) {
		this.serviceTestPlan = serviceTestPlan;
	}

	public ServiceSecurityAssessmentPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(ServiceSecurityAssessmentPojo baseline) {
		this.baseline = baseline;
	}

	@Override
	public int compareTo(ServiceSecurityAssessmentPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

	public List<String> getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

}
