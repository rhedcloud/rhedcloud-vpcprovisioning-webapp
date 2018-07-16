package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountNotificationQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {
	/*
		<!ELEMENT AccountNotificationQuerySpecification (
			Comparison*, 
			QueryLanguage?, 
			AccountNotificationId?, 
			AccountId?, 
			Type?, 
			Priority?)>
	 */

	String accountNotificationId;
	String accountId;
	String type;
	String priority;
	
	public AccountNotificationQueryFilterPojo() {
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

}
