package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceSecurityAssessmentQueryFilterPojo extends SharedObject implements IsSerializable {
	/*
ServiceSecurityAssessmentQuerySpecification (Comparison*, QueryLanguage?, SecurityRiskId?, ServiceId?, RiskLevel?)>
	 */

	String securityRiskId;
	String serviceId;
	String riskLevel;
	
	public ServiceSecurityAssessmentQueryFilterPojo() {
		// TODO Auto-generated constructor stub
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

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

}
