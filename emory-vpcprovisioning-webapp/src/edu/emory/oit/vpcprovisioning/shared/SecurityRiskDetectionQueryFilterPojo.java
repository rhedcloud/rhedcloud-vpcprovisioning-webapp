package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SecurityRiskDetectionQueryFilterPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT SecurityRiskDetectionQuerySpecification (Comparison*, QueryLanguage?, 
SecurityRiskDetectionId?, AccountId?, SecurityRiskDetector, 
SecurityRiskRemediator, DetectedSecurityRisk*)>
	 */
	String securityRiskDetectionId;
	String accountId;
	String securityRiskDetector;
	String securityRiskRemediator;
	
	public SecurityRiskDetectionQueryFilterPojo() {
	}

	public String getSecurityRiskDetectionId() {
		return securityRiskDetectionId;
	}

	public void setSecurityRiskDetectionId(String securityRiskDetectionId) {
		this.securityRiskDetectionId = securityRiskDetectionId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getSecurityRiskDetector() {
		return securityRiskDetector;
	}

	public void setSecurityRiskDetector(String securityRiskDetector) {
		this.securityRiskDetector = securityRiskDetector;
	}

	public String getSecurityRiskRemediator() {
		return securityRiskRemediator;
	}

	public void setSecurityRiskRemediator(String securityRiskRemediator) {
		this.securityRiskRemediator = securityRiskRemediator;
	}

}
