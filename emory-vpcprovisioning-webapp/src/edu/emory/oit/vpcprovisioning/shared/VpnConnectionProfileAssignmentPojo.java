package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfileAssignmentPojo extends SharedObject implements IsSerializable {

	String vpnConnectionProfileAssignmentId;
	String vpnConnectionProfileId;
	String ownerId;
	String description;
	String purpose;
	String deleteUser;
	Date deleteTime;
	
	public VpnConnectionProfileAssignmentPojo() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}


}
