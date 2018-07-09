package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TermsOfUsePojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT TermsOfUse (
			TermsOfUseId?, 
			Revision, 
			EffectiveDate, 
			ExpirationDate?, 
			Text, CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?)>
	 */
	String termsOfUseId;
	String revision;
	Date effectiveDate;
	Date expirationDate;
	String text;
	
	public TermsOfUsePojo() {
		// TODO Auto-generated constructor stub
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
