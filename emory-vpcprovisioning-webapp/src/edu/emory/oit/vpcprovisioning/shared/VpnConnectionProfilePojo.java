package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfilePojo extends SharedObject implements IsSerializable {

	String vpnConnectionProfileId;
	String vpcNetwork;
	List<TunnelProfilePojo> tunnelProfiles = new java.util.ArrayList<TunnelProfilePojo>();
	
	public VpnConnectionProfilePojo() {
		// TODO Auto-generated constructor stub
	}

	public String getVpnConnectionProfileId() {
		return vpnConnectionProfileId;
	}

	public void setVpnConnectionProfileId(String vpnConnectionProfileId) {
		this.vpnConnectionProfileId = vpnConnectionProfileId;
	}

	public String getVpcNetwork() {
		return vpcNetwork;
	}

	public void setVpcNetwork(String vpcNetwork) {
		this.vpcNetwork = vpcNetwork;
	}

	public List<TunnelProfilePojo> getTunnelProfiles() {
		return tunnelProfiles;
	}

	public void setTunnelProfiles(List<TunnelProfilePojo> tunnelProfiles) {
		this.tunnelProfiles = tunnelProfiles;
	}

}
