package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleAssignmentQueryFilterPojo extends SharedObject implements IsSerializable {
	//RoleDN?, IdentityType?, DirectAssignOnly?, UserDN?
	String roleDN;
	String identityType;
	boolean directAssignOnly;
	String userDN;

	public RoleAssignmentQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getRoleDN() {
		return roleDN;
	}

	public void setRoleDN(String roleDN) {
		this.roleDN = roleDN;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getUserDN() {
		return userDN;
	}

	public void setUserDN(String userDN) {
		this.userDN = userDN;
	}

	public boolean isDirectAssignOnly() {
		return directAssignOnly;
	}

	public void setDirectAssignOnly(boolean directAssignOnly) {
		this.directAssignOnly = directAssignOnly;
	}

}
