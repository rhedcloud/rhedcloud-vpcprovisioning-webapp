package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TunnelInterfacePojo extends SharedObject implements IsSerializable {

	public TunnelInterfacePojo() {
	}
	String name;
	String description;
	String virtualRouteForwarding;
	String ipAddress;
	String tcpMaximumSegmentSize;
	String administrativeState;
	String tunnelSource;
	String tunnelMode;
	String tunnelDestination;
	CryptoIpsecProfilePojo cryptoIpsecProfile;
	String ipVirtualReassembly;
	String operationalStatus;
	BgpStatePojo bgpState;
	BgpPrefixesPojo bgpPrefixes;
	String badStateReason;
	
	public String getBadStateReason() {
		return badStateReason;
	}
	public boolean isOperational() {
		// TODO: determine if it's operational and if it isn't, set the badStateReason...
		badStateReason = "bad state reason from the tunnel interface pojo";
		return false;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVirtualRouteForwarding() {
		return virtualRouteForwarding;
	}
	public void setVirtualRouteForwarding(String virtualRouteForwarding) {
		this.virtualRouteForwarding = virtualRouteForwarding;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTcpMaximumSegmentSize() {
		return tcpMaximumSegmentSize;
	}
	public void setTcpMaximumSegmentSize(String tcpMaximumSegmentSize) {
		this.tcpMaximumSegmentSize = tcpMaximumSegmentSize;
	}
	public String getAdministrativeState() {
		return administrativeState;
	}
	public void setAdministrativeState(String administrativeState) {
		this.administrativeState = administrativeState;
	}
	public String getTunnelSource() {
		return tunnelSource;
	}
	public void setTunnelSource(String tunnelSource) {
		this.tunnelSource = tunnelSource;
	}
	public String getTunnelMode() {
		return tunnelMode;
	}
	public void setTunnelMode(String tunnelMode) {
		this.tunnelMode = tunnelMode;
	}
	public String getTunnelDestination() {
		return tunnelDestination;
	}
	public void setTunnelDestination(String tunnelDestination) {
		this.tunnelDestination = tunnelDestination;
	}
	public CryptoIpsecProfilePojo getCryptoIpsecProfile() {
		return cryptoIpsecProfile;
	}
	public void setCryptoIpsecProfile(CryptoIpsecProfilePojo cryptoIpsecProfile) {
		this.cryptoIpsecProfile = cryptoIpsecProfile;
	}
	public String getIpVirtualReassembly() {
		return ipVirtualReassembly;
	}
	public void setIpVirtualReassembly(String ipVirtualReassembly) {
		this.ipVirtualReassembly = ipVirtualReassembly;
	}
	public String getOperationalStatus() {
		return operationalStatus;
	}
	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}
	public BgpStatePojo getBgpState() {
		return bgpState;
	}
	public void setBgpState(BgpStatePojo bgpState) {
		this.bgpState = bgpState;
	}
	public BgpPrefixesPojo getBgpPrefixes() {
		return bgpPrefixes;
	}
	public void setBgpPrefixes(BgpPrefixesPojo bgpPrefixes) {
		this.bgpPrefixes = bgpPrefixes;
	}
}
