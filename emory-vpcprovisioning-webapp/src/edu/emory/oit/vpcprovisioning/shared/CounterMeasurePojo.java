package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CounterMeasurePojo extends SharedObject implements IsSerializable {
	/*
		SecurityRiskId, Status, Description, Verifier?, VerificationDatetime?
	 */

	String securityRiskId;
	String status;
	String description;
	String verifier;
	Date verificationDate;
	
	public CounterMeasurePojo() {
		// TODO Auto-generated constructor stub
	}

	public String getSecurityRiskId() {
		return securityRiskId;
	}

	public void setSecurityRiskId(String securityRiskId) {
		this.securityRiskId = securityRiskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public Date getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}

}
