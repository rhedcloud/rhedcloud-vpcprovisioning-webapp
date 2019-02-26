package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

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
	Date startDate;
	Date endDate;
	boolean useQueryLanguage;
	int maxRows;
	String searchString;

	public AccountNotificationQueryFilterPojo() {
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

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
