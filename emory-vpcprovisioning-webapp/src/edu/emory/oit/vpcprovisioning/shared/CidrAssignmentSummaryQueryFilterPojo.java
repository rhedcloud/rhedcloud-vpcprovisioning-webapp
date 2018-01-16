package edu.emory.oit.vpcprovisioning.shared;

@SuppressWarnings("serial")
public class CidrAssignmentSummaryQueryFilterPojo extends SharedObject {
	String cidrAssignmentId;
	String ownerId;

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
}
