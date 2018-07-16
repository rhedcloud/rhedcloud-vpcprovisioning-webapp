package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrAssignmentSummaryQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {
	String cidrAssignmentId;
	String ownerId;
	CidrPojo cidr;
	UserAccountPojo userLoggedIn;

	public CidrAssignmentSummaryQueryFilterPojo() {
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

	public CidrPojo getCidr() {
		return cidr;
	}

	public void setCidr(CidrPojo cidr) {
		this.cidr = cidr;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}
}
