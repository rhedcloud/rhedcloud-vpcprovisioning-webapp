package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfileAssignmentRequisitionPojo extends SharedObject implements IsSerializable {

	String ownerId;		// vpc id
	
	public VpnConnectionProfileAssignmentRequisitionPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
