package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CryptoIsakmpProfilePojo extends SharedObject implements IsSerializable {

	public CryptoIsakmpProfilePojo() {
	}
	String name;
	String description;
	String virtualRouteForwarding;
	CryptoKeyringPojo cryptoKeyRing;
	MatchIdentityPojo matchIdentity;
	LocalAddressPojo localAddress;
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
	public CryptoKeyringPojo getCryptoKeyring() {
		return cryptoKeyRing;
	}
	public void setCryptoKeyring(CryptoKeyringPojo cryptoKeyRing) {
		this.cryptoKeyRing = cryptoKeyRing;
	}
	public MatchIdentityPojo getMatchIdentity() {
		return matchIdentity;
	}
	public void setMatchIdentity(MatchIdentityPojo matchIdentity) {
		this.matchIdentity = matchIdentity;
	}
	public LocalAddressPojo getLocalAddress() {
		return localAddress;
	}
	public void setLocalAddress(LocalAddressPojo localAddress) {
		this.localAddress = localAddress;
	}
}
