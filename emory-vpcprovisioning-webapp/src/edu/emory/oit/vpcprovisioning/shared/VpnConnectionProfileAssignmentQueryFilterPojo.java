package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfileAssignmentQueryFilterPojo extends SharedObject implements IsSerializable {
	String vpnConnectionProfileId;
	String vpnConnectionProfileAssignmentId;
	String ownerId;

	public VpnConnectionProfileAssignmentQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getVpnConnectionProfileId() {
		return vpnConnectionProfileId;
	}

	public void setVpnConnectionProfileId(String vpnConnectionProfileId) {
		this.vpnConnectionProfileId = vpnConnectionProfileId;
	}

	public String getVpnConnectionProfileAssignmentId() {
		return vpnConnectionProfileAssignmentId;
	}

	public void setVpnConnectionProfileAssignmentId(String vpnConnectionProfileAssignmentId) {
		this.vpnConnectionProfileAssignmentId = vpnConnectionProfileAssignmentId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
