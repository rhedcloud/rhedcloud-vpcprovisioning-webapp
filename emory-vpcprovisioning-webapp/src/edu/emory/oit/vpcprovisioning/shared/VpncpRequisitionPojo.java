package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpncpRequisitionPojo extends SharedObject implements IsSerializable {

	String ownerId;
	String remoteVpnIpAddress;
	String presharedKey;
	VpnConnectionProfilePojo profile;
	
	public VpncpRequisitionPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getRemoteVpnIpAddress() {
		return remoteVpnIpAddress;
	}

	public void setRemoteVpnIpAddress(String remoteVpnIpAddress) {
		this.remoteVpnIpAddress = remoteVpnIpAddress;
	}

	public String getPresharedKey() {
		return presharedKey;
	}

	public void setPresharedKey(String presharedKey) {
		this.presharedKey = presharedKey;
	}

	public VpnConnectionProfilePojo getProfile() {
		return profile;
	}

	public void setProfile(VpnConnectionProfilePojo profile) {
		this.profile = profile;
	}

}
