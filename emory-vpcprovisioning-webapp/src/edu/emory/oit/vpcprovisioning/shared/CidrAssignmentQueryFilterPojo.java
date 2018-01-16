package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrAssignmentQueryFilterPojo extends SharedObject implements IsSerializable {
	String cidrAssignmentId;
	String ownerId;
	
	public CidrAssignmentQueryFilterPojo() {
		// TODO Auto-generated constructor stub
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
