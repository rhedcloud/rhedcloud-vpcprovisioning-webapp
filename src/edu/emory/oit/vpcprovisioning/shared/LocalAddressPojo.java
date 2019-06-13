package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class LocalAddressPojo extends SharedObject implements IsSerializable {

	public LocalAddressPojo() {
	}
	String ipAddress;
	String virualRouteForwarding;
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getVirualRouteForwarding() {
		return virualRouteForwarding;
	}
	public void setVirualRouteForwarding(String virualRouteForwarding) {
		this.virualRouteForwarding = virualRouteForwarding;
	}
}
