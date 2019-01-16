package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserProfileQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
		<!ELEMENT UserProfileQuerySpecification (UserId?, Property*)>	 
	*/

	String userId;
	UserAccountPojo userAccount;
	List<PropertyPojo> properties = new java.util.ArrayList<PropertyPojo>();
	
	public UserProfileQueryFilterPojo() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<PropertyPojo> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyPojo> properties) {
		this.properties = properties;
	}

	public UserAccountPojo getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccountPojo userAccount) {
		this.userAccount = userAccount;
	}

}
