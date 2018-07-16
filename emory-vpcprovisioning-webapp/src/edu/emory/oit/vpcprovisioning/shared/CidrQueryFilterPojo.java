package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	String network;
	int bits;
	String requestorNetId;
	String purpose;
	String description;
	UserAccountPojo userLoggedIn;
	
	public CidrQueryFilterPojo() {
	}

	public int getBits() {
		return bits;
	}

	public void setBits(int bits) {
		this.bits = bits;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getRequestorNetId() {
		return requestorNetId;
	}

	public void setRequestorNetId(String requestorNetId) {
		this.requestorNetId = requestorNetId;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

}
