package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

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
	Date startDate;
	Date endDate;
	boolean useQueryLanguage;
	int maxRows;
	String searchString;
	UserAccountPojo userLoggedIn;
	
	public UserNotificationQueryFilterPojo() {
		
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isUseQueryLanguage() {
		return useQueryLanguage;
	}

	public void setUseQueryLanguage(boolean useQueryLanguage) {
		this.useQueryLanguage = useQueryLanguage;
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
