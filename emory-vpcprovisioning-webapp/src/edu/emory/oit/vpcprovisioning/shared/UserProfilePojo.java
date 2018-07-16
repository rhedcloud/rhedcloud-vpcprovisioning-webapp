package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class UserProfilePojo extends SharedObject implements IsSerializable, Comparable<UserProfilePojo> {
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
	Date lastLoginTime;
	UserProfilePojo baseline;

	public static final ProvidesKey<UserProfilePojo> KEY_PROVIDER = new ProvidesKey<UserProfilePojo>() {
		@Override
		public Object getKey(UserProfilePojo item) {
			return item == null ? null : item.getUserId();
		}
	};
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

	public UserProfilePojo getBaseline() {
		return baseline;
	}

	public void setBaseline(UserProfilePojo baseline) {
		this.baseline = baseline;
	}

	@Override
	public int compareTo(UserProfilePojo o) {
		return 0;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void updateProperty(String name, String value) {
		for (PropertyPojo prop : getProperties()) {
			if (prop.getName().equalsIgnoreCase(name)) {
				GWT.log("setting " + name + " to " + value);
				prop.setValue(value);
			}
		}
	}
}
