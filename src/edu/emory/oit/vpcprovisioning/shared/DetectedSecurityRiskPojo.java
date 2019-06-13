package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DetectedSecurityRiskPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT DetectedSecurityRisk (Type, AmazonResourceName, RemediationResult?)>
	 */
	String type;
	String amazonResourceName;
	RemediationResultPojo remediationResult;
	
	public DetectedSecurityRiskPojo() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmazonResourceName() {
		return amazonResourceName;
	}

	public void setAmazonResourceName(String amazonResourceName) {
		this.amazonResourceName = amazonResourceName;
	}

	public RemediationResultPojo getRemediationResult() {
		return remediationResult;
	}

	public void setRemediationResult(RemediationResultPojo remediationResult) {
		this.remediationResult = remediationResult;
	}

}
