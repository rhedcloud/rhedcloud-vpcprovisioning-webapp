package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TermsOfUseQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
		Comparison*, 
		QueryLanguage?, 
		TermsOfUseId?, 
		Revision?, 
		EffectiveDate?, 
		ExpirationDate?
	 */
	
	String termsOfUseId;
	String revision;
	Date effectiveDate;
	Date expirationDate;
	boolean effectiveTerms;
	UserAccountPojo userAccount;
	
	public TermsOfUseQueryFilterPojo() {
		
	}

	public String getTermsOfUseId() {
		return termsOfUseId;
	}

	public void setTermsOfUseId(String termsOfUseId) {
		this.termsOfUseId = termsOfUseId;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isEffectiveTerms() {
		return effectiveTerms;
	}

	public void setEffectiveTerms(boolean effectiveTerms) {
		this.effectiveTerms = effectiveTerms;
	}

	public UserAccountPojo getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccountPojo userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
