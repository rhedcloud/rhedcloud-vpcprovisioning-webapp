package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TermsOfUseAgreementQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	String termsOfUseAgreementId;
	String termsOfUseId;
	String userId;
	UserAccountPojo userAccount;
	
	public TermsOfUseAgreementQueryFilterPojo() {
		// TODO Auto-generated constructor stub
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

	public UserAccountPojo getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccountPojo userAccount) {
		this.userAccount = userAccount;
	}

}
