package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CryptoKeyringPojo extends SharedObject implements IsSerializable {
	/*
	<!ELEMENT CryptoKeyring (Name, Description, LocalAddress, PresharedKey)>
	 */

	String name;
	public CryptoKeyringPojo() {
	}
	String description;
	LocalAddressPojo localAddress;
	String presharedKey;
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
	public LocalAddressPojo getLocalAddress() {
		return localAddress;
	}
	public void setLocalAddress(LocalAddressPojo localAddress) {
		this.localAddress = localAddress;
	}
	public String getPresharedKey() {
		return presharedKey;
	}
	public void setPresharedKey(String presharedKey) {
		this.presharedKey = presharedKey;
	}
}
