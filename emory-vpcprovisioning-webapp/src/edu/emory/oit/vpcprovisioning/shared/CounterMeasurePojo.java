package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class CounterMeasurePojo extends SharedObject implements IsSerializable, Comparable<CounterMeasurePojo> {
	/*
		SecurityRiskId, Status, Description, Verifier?, VerificationDatetime?
	 */

	public static final ProvidesKey<CounterMeasurePojo> KEY_PROVIDER = new ProvidesKey<CounterMeasurePojo>() {
		@Override
		public Object getKey(CounterMeasurePojo item) {
			return item == null ? null : item.getSecurityRiskId();
		}
	};
	String securityRiskId;
	String status;
	String description;
	String verifier;
	Date verificationDate;
	
	public CounterMeasurePojo() {
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

	@Override
	public int compareTo(CounterMeasurePojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
