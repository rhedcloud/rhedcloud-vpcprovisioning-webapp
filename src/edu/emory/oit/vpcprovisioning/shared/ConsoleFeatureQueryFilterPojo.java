package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ConsoleFeatureQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {
	String searchKey;
	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	UserAccountPojo userLoggedIn;
	
	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	boolean fuzzyFilter=false;

	public ConsoleFeatureQueryFilterPojo() {
	}

	public boolean isFuzzyFilter() {
		return fuzzyFilter;
	}

	public void setFuzzyFilter(boolean fuzzyFilter) {
		this.fuzzyFilter = fuzzyFilter;
	}

	@Override
	public boolean isEmpty() {
		if (searchKey != null && searchKey.length() > 0) {
			return false;
		}
		if (userLoggedIn != null) {
			return false;
		}
		return true;
	}

}
