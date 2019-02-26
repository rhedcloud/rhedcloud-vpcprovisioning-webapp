package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {
	String accountId;
	String accountName;
	// TODO: change to a list of emails (and query spec)?
	EmailPojo email;
	String accountOwnerId;
	String speedType;
	String createUser;
	String lastUpdateUser;
	UserAccountPojo userLoggedIn;
	boolean fuzzyFilter=false;

	public AccountQueryFilterPojo() {
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public EmailPojo getEmail() {
		return email;
	}

	public void setEmail(EmailPojo email) {
		this.email = email;
	}

	public String getAccountOwnerId() {
		return accountOwnerId;
	}

	public void setAccountOwnerId(String accountOwnerNetId) {
		this.accountOwnerId = accountOwnerNetId;
	}

	public String getSpeedType() {
		return speedType;
	}

	public void setSpeedType(String speedType) {
		this.speedType = speedType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public boolean isFuzzyFilter() {
		return fuzzyFilter;
	}

	public void setFuzzyFilter(boolean fuzzyFilter) {
		this.fuzzyFilter = fuzzyFilter;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
