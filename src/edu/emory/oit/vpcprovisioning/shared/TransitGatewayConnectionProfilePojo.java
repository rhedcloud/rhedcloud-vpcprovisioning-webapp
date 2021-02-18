package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfilePojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayConnectionProfilePojo> {

	String transitGatewayConnectionProfileId;
	String cidrId;
	String region;
	String transitGatewayId;
	String cidrRange;
	boolean assigned;
	TransitGatewayConnectionProfilePojo baseline;
	
	public TransitGatewayConnectionProfilePojo() {
	}


	@Override
	public int compareTo(TransitGatewayConnectionProfilePojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTransitGatewayConnectionProfileId() {
		return transitGatewayConnectionProfileId;
	}

	public void setTransitGatewayConnectionProfileId(String transitGatewayConnectionProfileId) {
		this.transitGatewayConnectionProfileId = transitGatewayConnectionProfileId;
	}

	public String getCidrId() {
		return cidrId;
	}

	public void setCidrId(String cidrId) {
		this.cidrId = cidrId;
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

	public String getCidrRange() {
		return cidrRange;
	}

	public void setCidrRange(String cidrRange) {
		this.cidrRange = cidrRange;
	}


	public boolean isAssigned() {
		return assigned;
	}


	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}


	public TransitGatewayConnectionProfilePojo getBaseline() {
		return baseline;
	}


	public void setBaseline(TransitGatewayConnectionProfilePojo baseline) {
		this.baseline = baseline;
	}

}
