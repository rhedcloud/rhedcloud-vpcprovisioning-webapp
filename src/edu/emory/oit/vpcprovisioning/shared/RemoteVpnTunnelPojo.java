package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RemoteVpnTunnelPojo extends SharedObject implements IsSerializable {

	/*
		<!ELEMENT RemoteVpnTunnel (LocalTunnelId, RemoteVpnIpAddress, PresharedKey, VpnInsideIpCidr)>
	 */
	String localTunnelId;
	String remoteVpnIpAddress;
	String presharedKey;
	String vpnInsideCidr;
	
	public RemoteVpnTunnelPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getLocalTunnelId() {
		return localTunnelId;
	}

	public void setLocalTunnelId(String localTunnelId) {
		this.localTunnelId = localTunnelId;
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

	public String getVpnInsideCidr() {
		return vpnInsideCidr;
	}

	public void setVpnInsideCidr(String vpnInsideCidr) {
		this.vpnInsideCidr = vpnInsideCidr;
	}

}
