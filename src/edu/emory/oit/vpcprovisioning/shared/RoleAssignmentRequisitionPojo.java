package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleAssignmentRequisitionPojo extends SharedObject implements IsSerializable {
	//RoleAssignmentActionType?, RoleAssignmentType?,IdentityDN?,Reason?,RoleDNs?
	String actionType;
	String type;
	String identityDN;
	String reason;
	RoleDNsPojo roleDNs; 

	public RoleAssignmentRequisitionPojo() {
		
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentityDN() {
		return identityDN;
	}

	public void setIdentityDN(String identityDN) {
		this.identityDN = identityDN;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public RoleDNsPojo getRoleDNs() {
		return roleDNs;
	}

	public void setRoleDNs(RoleDNsPojo roleDNs) {
		this.roleDNs = roleDNs;
	}

}
