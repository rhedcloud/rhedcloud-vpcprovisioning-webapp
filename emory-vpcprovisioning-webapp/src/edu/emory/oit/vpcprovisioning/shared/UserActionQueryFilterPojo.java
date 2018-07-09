package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserActionQueryFilterPojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT UserActionQuerySpecification (
		Comparison*, 
		QueryLanguage?, 
		UserId, 
		Action)>
	 */
	
	String userId;
	String action;
	
	public UserActionQueryFilterPojo() {
		// TODO Auto-generated constructor stub
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

}
