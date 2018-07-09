package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserProfilePojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT UserProfile (
			UserId, 
			Property*, 
			CreateUser, 
			CreateDatetime, 
			LastUpdateUser?, 
			LastUpdateDatetime?, 
			LastLoginDatetime)>
	 */

	String userId;
	List<PropertyPojo> properties = new java.util.ArrayList<PropertyPojo>();

	public UserProfilePojo() {
		// TODO Auto-generated constructor stub
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
	
}
