package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CryptoIpsecProfilePojo extends SharedObject implements IsSerializable {

	public CryptoIpsecProfilePojo() {
	}
	String name;
	String description;
	CryptoIpsecTransformSetPojo cryptoIpsecTransformSet;
	String perfectForwardSecrecy;
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
	public CryptoIpsecTransformSetPojo getCryptoIpsecTransformSet() {
		return cryptoIpsecTransformSet;
	}
	public void setCryptoIpsecTransformSet(CryptoIpsecTransformSetPojo cryptoIpsecTransformSet) {
		this.cryptoIpsecTransformSet = cryptoIpsecTransformSet;
	}
	public String getPerfectForwardSecrecy() {
		return perfectForwardSecrecy;
	}
	public void setPerfectForwardSecrecy(String perfectForwardSecrecy) {
		this.perfectForwardSecrecy = perfectForwardSecrecy;
	}
	
}
