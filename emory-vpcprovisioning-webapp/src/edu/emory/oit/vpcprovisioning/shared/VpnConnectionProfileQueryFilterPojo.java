package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionProfileQueryFilterPojo extends SharedObject implements IsSerializable {

	String vpnConnectionProfileId;
	String vpcNetwork;

	public VpnConnectionProfileQueryFilterPojo() {
		
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

}
