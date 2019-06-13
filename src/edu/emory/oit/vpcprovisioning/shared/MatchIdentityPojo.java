package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class MatchIdentityPojo extends SharedObject implements IsSerializable {

	public MatchIdentityPojo() {
	}
	String ipAddress;
	String netMask;
	String virtualRouteForwarding;
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getNetMask() {
		return netMask;
	}
	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}
	public String getVirtualRouteForwarding() {
		return virtualRouteForwarding;
	}
	public void setVirtualRouteForwarding(String virtualRouteForwarding) {
		this.virtualRouteForwarding = virtualRouteForwarding;
	}
}
