package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfileAssignmentPojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayConnectionProfileAssignmentPojo> {

	String transitGatewayConnectionProfileAssignmentId;
	String transitGatewayConnectionProfileId;
	String ownerId;
	TransitGatewayConnectionProfileAssignmentPojo baseline;
	
	public TransitGatewayConnectionProfileAssignmentPojo() {
	}

	@Override
	public int compareTo(TransitGatewayConnectionProfileAssignmentPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTransitGatewayConnectionProfileId() {
		return transitGatewayConnectionProfileId;
	}

	public void setTransitGatewayConnectionProfileId(String transitGatewayConnectionProfileId) {
		this.transitGatewayConnectionProfileId = transitGatewayConnectionProfileId;
	}

	public String getTransitGatewayConnectionProfileAssignmentId() {
		return transitGatewayConnectionProfileAssignmentId;
	}

	public void setTransitGatewayConnectionProfileAssignmentId(String transitGatewayConnectionProfileAssignmentId) {
		this.transitGatewayConnectionProfileAssignmentId = transitGatewayConnectionProfileAssignmentId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public TransitGatewayConnectionProfileAssignmentPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(TransitGatewayConnectionProfileAssignmentPojo baseline) {
		this.baseline = baseline;
	}
}
