package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class SecurityRiskDetectionPojo extends SharedObject implements IsSerializable, Comparable<SecurityRiskDetectionPojo> {

	/*
<!ELEMENT SecurityRiskDetection (SecurityRiskDetectionId, AccountId, SecurityRiskDetector, 
SecurityRiskRemediator, DetectedSecurityRisk*, CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?)>
	 */
	String securityRiskDetectionId;
	String accountId;
	String securityRiskDetector;
	String securityRiskRemediator;
	List<DetectedSecurityRiskPojo> detectedSecurityRisks = new java.util.ArrayList<DetectedSecurityRiskPojo>();
	DetectionResultPojo detectionResult;
	SecurityRiskDetectionPojo baseline;

	public static final ProvidesKey<SecurityRiskDetectionPojo> KEY_PROVIDER = new ProvidesKey<SecurityRiskDetectionPojo>() {
		@Override
		public Object getKey(SecurityRiskDetectionPojo item) {
			return item == null ? null : item.getSecurityRiskDetectionId();
		}
	};
	public SecurityRiskDetectionPojo() {
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
	public List<DetectedSecurityRiskPojo> getDetectedSecurityRisks() {
		return detectedSecurityRisks;
	}
	public void setDetectedSecurityRisks(List<DetectedSecurityRiskPojo> detectedSecurityRisks) {
		this.detectedSecurityRisks = detectedSecurityRisks;
	}
	public SecurityRiskDetectionPojo getBaseline() {
		return baseline;
	}
	public void setBaseline(SecurityRiskDetectionPojo baseline) {
		this.baseline = baseline;
	}
	@Override
	public int compareTo(SecurityRiskDetectionPojo o) {
		
		return 0;
	}
	public DetectionResultPojo getDetectionResult() {
		return detectionResult;
	}
	public void setDetectionResult(DetectionResultPojo detectionResult) {
		this.detectionResult = detectionResult;
	}

}
