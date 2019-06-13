package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpnConnectionPojo extends SharedObject implements IsSerializable, Comparable<VpnConnectionPojo> {

	/*
	<!ELEMENT VpnConnection (
		VpnId, 
		VpcId, 
		CryptoKeyring, 
		CryptoIsakmpProfile, 
		CryptoIpsecTransformSet, 
		CryptoIpsecProfile, 
		TunnelInterface+)>
	 */
	String vpnId;
	String vpcId;
	CryptoKeyringPojo cryptoKeyring;
	CryptoIsakmpProfilePojo cryptoIsakmpProfile;
	CryptoIpsecTransformSetPojo cryptoIpsedTransformSet;
	CryptoIpsecProfilePojo cryptoIpsecProfile;
	List<TunnelInterfacePojo> tunnelInterfaces = new java.util.ArrayList<TunnelInterfacePojo>();
	
	VpnConnectionPojo baseline;
	
	public VpnConnectionPojo() {
		
	}

	public String getVpnId() {
		return vpnId;
	}

	public void setVpnId(String vpnId) {
		this.vpnId = vpnId;
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public CryptoKeyringPojo getCryptoKeyring() {
		return cryptoKeyring;
	}

	public void setCryptoKeyring(CryptoKeyringPojo cryptoKeyring) {
		this.cryptoKeyring = cryptoKeyring;
	}

	public CryptoIsakmpProfilePojo getCryptoIsakmpProfile() {
		return cryptoIsakmpProfile;
	}

	public void setCryptoIsakmpProfile(CryptoIsakmpProfilePojo cryptoIsakmpProfile) {
		this.cryptoIsakmpProfile = cryptoIsakmpProfile;
	}

	public CryptoIpsecTransformSetPojo getCryptoIpsedTransformSet() {
		return cryptoIpsedTransformSet;
	}

	public void setCryptoIpsedTransformSet(CryptoIpsecTransformSetPojo cryptoIpsedTransformSet) {
		this.cryptoIpsedTransformSet = cryptoIpsedTransformSet;
	}

	public CryptoIpsecProfilePojo getCryptoIpsecProfile() {
		return cryptoIpsecProfile;
	}

	public void setCryptoIpsecProfile(CryptoIpsecProfilePojo cryptoIpsecProfile) {
		this.cryptoIpsecProfile = cryptoIpsecProfile;
	}

	public List<TunnelInterfacePojo> getTunnelInterfaces() {
		return tunnelInterfaces;
	}

	public void setTunnelInterfaces(List<TunnelInterfacePojo> tunnelInterfaces) {
		this.tunnelInterfaces = tunnelInterfaces;
	}

	public VpnConnectionPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(VpnConnectionPojo baseline) {
		this.baseline = baseline;
	}

	@Override
	public int compareTo(VpnConnectionPojo o) {
		return vpnId.compareTo(o.getVpnId());
	}
}
