package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrAssignmentStatus extends SharedObject implements IsSerializable {
	boolean assigned;
	CidrAssignmentPojo cidrAssignment;

	public CidrAssignmentStatus() {
		// TODO Auto-generated constructor stub
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public CidrAssignmentPojo getCidrAssignment() {
		return cidrAssignment;
	}

	public void setCidrAssignment(CidrAssignmentPojo cidrAssignment) {
		this.cidrAssignment = cidrAssignment;
	}

}
