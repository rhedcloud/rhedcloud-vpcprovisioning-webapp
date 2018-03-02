package edu.emory.oit.vpcprovisioning.shared;

@SuppressWarnings("serial")
public class CidrAssignmentSummaryQueryFilterPojo extends SharedObject {
	String cidrAssignmentId;
	String ownerId;
	CidrPojo cidr;

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
}
