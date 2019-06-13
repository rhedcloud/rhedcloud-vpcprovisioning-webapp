package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TermsOfUseSummaryPojo extends SharedObject implements IsSerializable {
	UserAccountPojo user;
	List<TermsOfUseAgreementPojo> termsOfUseAgreements = new java.util.ArrayList<TermsOfUseAgreementPojo>();
	TermsOfUsePojo latestTerms;
	boolean hasValidTermsOfUseAgreement;
	
	public TermsOfUseSummaryPojo() {
		
	}

	public UserAccountPojo getUser() {
		return user;
	}

	public void setUser(UserAccountPojo user) {
		this.user = user;
	}

	public TermsOfUsePojo getLatestTerms() {
		return latestTerms;
	}

	public void setLatestTerms(TermsOfUsePojo latestTerms) {
		this.latestTerms = latestTerms;
	}

	public boolean hasValidTermsOfUseAgreement() {
		return hasValidTermsOfUseAgreement;
	}

	public void setHasValidTermsOfUseAgreement(boolean hasValidTermsOfUseAgreement) {
		this.hasValidTermsOfUseAgreement = hasValidTermsOfUseAgreement;
	}

	public List<TermsOfUseAgreementPojo> getTermsOfUseAgreements() {
		return termsOfUseAgreements;
	}

	public void setTermsOfUseAgreements(List<TermsOfUseAgreementPojo> termsOfUseAgreements) {
		this.termsOfUseAgreements = termsOfUseAgreements;
	}

}
