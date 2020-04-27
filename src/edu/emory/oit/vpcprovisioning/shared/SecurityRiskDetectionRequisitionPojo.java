package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SecurityRiskDetectionRequisitionPojo extends SharedObject implements IsSerializable {

	String accountId;
	String securityRiskDetector;
	String securityRiskRemediator;
	String rtpNamespace;
	String rtpProfileName;
	String rtpRevision;

	public SecurityRiskDetectionRequisitionPojo() {
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
	public String getRtpNamespace() {
		return rtpNamespace;
	}
	public void setRtpNamespace(String rtpNamespace) {
		this.rtpNamespace = rtpNamespace;
	}
	public String getRtpProfileName() {
		return rtpProfileName;
	}
	public void setRtpProfileName(String rtpProfileName) {
		this.rtpProfileName = rtpProfileName;
	}
	public String getRtpRevision() {
		return rtpRevision;
	}
	public void setRtpRevision(String rtpRevision) {
		this.rtpRevision = rtpRevision;
	}
}
