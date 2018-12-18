package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionQueryFilterPojo extends SharedObject implements IsSerializable {

	String vpcId;
	String vpnId;

	public VpnConnectionQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public String getVpnId() {
		return vpnId;
	}

	public void setVpnId(String vpnId) {
		this.vpnId = vpnId;
	}

}
