package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfileQueryFilterPojo extends SharedObject implements IsSerializable {

	String transitGatewayConnectionProfileId;
	String region;
	String transitGatewayId;
	
	public TransitGatewayConnectionProfileQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getTransitGatewayConnectionProfileId() {
		return transitGatewayConnectionProfileId;
	}

	public void setTransitGatewayConnectionProfileId(String transitGatewayConnectionProfileId) {
		this.transitGatewayConnectionProfileId = transitGatewayConnectionProfileId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTransitGatewayId() {
		return transitGatewayId;
	}

	public void setTransitGatewayId(String transitGatewayId) {
		this.transitGatewayId = transitGatewayId;
	}

}
