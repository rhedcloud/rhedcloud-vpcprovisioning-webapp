package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfileAssignmentRequisitionPojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayConnectionProfileAssignmentRequisitionPojo> {

	/*
	<!ELEMENT TransitGatewayConnectionProfileAssignmentRequisition (
		Region, 
		TransitGatewayId, 
		OwnerId)>
	 */
	String region;
	String transitGatewayId;
	String ownerId;

	public TransitGatewayConnectionProfileAssignmentRequisitionPojo() {
	}

	@Override
	public int compareTo(TransitGatewayConnectionProfileAssignmentRequisitionPojo o) {
		// TODO Auto-generated method stub
		return 0;
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
