package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfileAssignmentQueryFilterPojo extends SharedObject implements IsSerializable {

	String transitGatewayConnectionProfileAssignmentId;
	String transitGatewayConnectionProfileId;
	String ownerId;
	
	public TransitGatewayConnectionProfileAssignmentQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getTransitGatewayConnectionProfileAssignmentId() {
		return transitGatewayConnectionProfileAssignmentId;
	}

	public void setTransitGatewayConnectionProfileAssignmentId(String transitGatewayConnectionProfileAssignmentId) {
		this.transitGatewayConnectionProfileAssignmentId = transitGatewayConnectionProfileAssignmentId;
	}

	public String getTransitGatewayConnectionProfileId() {
		return transitGatewayConnectionProfileId;
	}

	public void setTransitGatewayConnectionProfileId(String transitGatewayConnectionProfileId) {
		this.transitGatewayConnectionProfileId = transitGatewayConnectionProfileId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
