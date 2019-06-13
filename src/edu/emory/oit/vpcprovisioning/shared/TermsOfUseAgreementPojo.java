package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TermsOfUseAgreementPojo extends SharedObject implements IsSerializable, Comparable<TermsOfUseAgreementPojo> {
	String termsOfUseAgreementId;
	String termsOfUseId;
	String userId;
	String status;
	Date presentedDate;
	Date agreedDate;
	
	public TermsOfUseAgreementPojo() {
		
	}

	public String getTermsOfUseAgreementId() {
		return termsOfUseAgreementId;
	}

	public void setTermsOfUseAgreementId(String termsOfUseAgreementId) {
		this.termsOfUseAgreementId = termsOfUseAgreementId;
	}

	public String getTermsOfUseId() {
		return termsOfUseId;
	}

	public void setTermsOfUseId(String termsOfUseId) {
		this.termsOfUseId = termsOfUseId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPresentedDate() {
		return presentedDate;
	}

	public void setPresentedDate(Date presentedDate) {
		this.presentedDate = presentedDate;
	}

	public Date getAgreedDate() {
		return agreedDate;
	}

	public void setAgreedDate(Date agreedDate) {
		this.agreedDate = agreedDate;
	}

	@Override
	public int compareTo(TermsOfUseAgreementPojo o) {
		
		return 0;
	}

}
