package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserActionPojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT UserAction (
			UserActionId?, 
			UserId, 
			Action, 
			Description, 
			Detail?, CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?)>
	 */
	String userActionId;
	String userId;
	String action;
	String description;
	String detail;

	public UserActionPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getUserActionId() {
		return userActionId;
	}

	public void setUserActionId(String userActionId) {
		this.userActionId = userActionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
