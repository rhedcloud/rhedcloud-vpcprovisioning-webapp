package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrAssignmentQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {
	String cidrAssignmentId;
	String ownerId;
	UserAccountPojo userLoggedIn;
	
	public CidrAssignmentQueryFilterPojo() {
	}

	public String getCidrAssignmentId() {
		return cidrAssignmentId;
	}

	public void setCidrAssignmentId(String cidrAssignmentId) {
		this.cidrAssignmentId = cidrAssignmentId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

}
