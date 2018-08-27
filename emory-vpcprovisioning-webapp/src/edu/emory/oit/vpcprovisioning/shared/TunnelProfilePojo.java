package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TunnelProfilePojo extends SharedObject implements IsSerializable {
	String tunnelId;
	String cryptoKeyringName;
	String isakampProfileName;
	String ipsecTransformSetName;
	String ipsecProfileName;
	String tunnelDescription;
	String customerGatewayIp;
	String vpnInsideIpCidr1;
	String vpnInsideIpCidr2;
	
	public TunnelProfilePojo() {
		// TODO Auto-generated constructor stub
	}

	public String getTunnelId() {
		return tunnelId;
	}

	public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}

	public String getCryptoKeyringName() {
		return cryptoKeyringName;
	}

	public void setCryptoKeyringName(String cryptoKeyringName) {
		this.cryptoKeyringName = cryptoKeyringName;
	}

	public String getIsakampProfileName() {
		return isakampProfileName;
	}

	public void setIsakampProfileName(String isakampProfileName) {
		this.isakampProfileName = isakampProfileName;
	}

	public String getIpsecTransformSetName() {
		return ipsecTransformSetName;
	}

	public void setIpsecTransformSetName(String ipsecTransformSetName) {
		this.ipsecTransformSetName = ipsecTransformSetName;
	}

	public String getIpsecProfileName() {
		return ipsecProfileName;
	}

	public void setIpsecProfileName(String ipsecProfileName) {
		this.ipsecProfileName = ipsecProfileName;
	}

	public String getTunnelDescription() {
		return tunnelDescription;
	}

	public void setTunnelDescription(String tunnelDescription) {
		this.tunnelDescription = tunnelDescription;
	}

	public String getCustomerGatewayIp() {
		return customerGatewayIp;
	}

	public void setCustomerGatewayIp(String customerGatewayIp) {
		this.customerGatewayIp = customerGatewayIp;
	}

	public String getVpnInsideIpCidr1() {
		return vpnInsideIpCidr1;
	}

	public void setVpnInsideIpCidr1(String vpnInsideIpCidr1) {
		this.vpnInsideIpCidr1 = vpnInsideIpCidr1;
	}

	public String getVpnInsideIpCidr2() {
		return vpnInsideIpCidr2;
	}

	public void setVpnInsideIpCidr2(String vpnInsideIpCidr2) {
		this.vpnInsideIpCidr2 = vpnInsideIpCidr2;
	}

}
