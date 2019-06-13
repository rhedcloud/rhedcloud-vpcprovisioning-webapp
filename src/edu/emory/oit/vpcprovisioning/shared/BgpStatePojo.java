package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class BgpStatePojo extends SharedObject implements IsSerializable {
	String status;
	String uptime;
	String neighborId;
	public String getStatus() {
		return status;
	}
	public BgpStatePojo() {
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public String getNeighborId() {
		return neighborId;
	}
	public void setNeighborId(String neighborId) {
		this.neighborId = neighborId;
	}
}
