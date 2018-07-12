package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountNotificationPojo extends SharedObject implements IsSerializable, Comparable<AccountNotificationPojo> {
	/*
		<!ELEMENT AccountNotification (
			AccountNotificationId?, 
			AccountId, 
			Type, 
			Priority, 
			Subject, 
			Text, 
			ReferenceId?, 
			Annotation*, 
			CreateUser, 
			CreateDatetime, 
			LastUpdateUser?, 
			LastUpdateDatetime?)>
	 */

	String accountNotificationId;
	String accountId;
	String type;
	String priority;
	String subject;
	String text;
	String referenceid;
	List<AnnotationPojo> annotations = new java.util.ArrayList<AnnotationPojo>();
	AccountNotificationPojo baseline;
	
	public AccountNotificationPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getAccountNotificationId() {
		return accountNotificationId;
	}

	public void setAccountNotificationId(String accountNotificationId) {
		this.accountNotificationId = accountNotificationId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getReferenceid() {
		return referenceid;
	}

	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}

	public List<AnnotationPojo> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationPojo> annotations) {
		this.annotations = annotations;
	}

	public AccountNotificationPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(AccountNotificationPojo baseline) {
		this.baseline = baseline;
	}

	@Override
	public int compareTo(AccountNotificationPojo o) {
		return o.getAccountNotificationId().compareTo(this.getAccountNotificationId());
	}

}
