package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserNotificationQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
		<!ELEMENT UserNotificationQuerySpecification (
			Comparison*, 
			QueryLanguage?, 
			UserNotificationId?, 
			AccountNotificationId?, 
			AccountId?, 
			UserId?, 
			Type?, 
			Priority?, 
			Read?)>
	 */

	String userNotificationId;
	String accountNotificationId;
	String accountId;
	String userId;
	String type;
	String priority;
	boolean read;
	String readStr;
	
	public UserNotificationQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getUserNotificationId() {
		return userNotificationId;
	}

	public void setUserNotificationId(String userNotificationId) {
		this.userNotificationId = userNotificationId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean viewed) {
		if (viewed) {
			this.readStr = "true";
		}
		else {
			this.readStr = "false";
		}
		this.read = viewed;
	}

	public String getReadStr() {
		return readStr;
	}

	public void setReadStr(String readStr) {
		this.readStr = readStr;
	}

}
