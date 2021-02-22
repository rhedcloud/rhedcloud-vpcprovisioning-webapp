package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayProfilePojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayProfilePojo> {

	String tgwProfileId;
	String transitGatewayId;
	String associationRouteTableId;
	List<String> propagationRouteTableIds = new java.util.ArrayList<String>();
	
	public TransitGatewayProfilePojo() {
	}

	@Override
	public int compareTo(TransitGatewayProfilePojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTgwProfileId() {
		return tgwProfileId;
	}

	public void setTgwProfileId(String tgwProfileId) {
		this.tgwProfileId = tgwProfileId;
	}

	public String getTransitGatewayId() {
		return transitGatewayId;
	}

	public void setTransitGatewayId(String transitGatewayId) {
		this.transitGatewayId = transitGatewayId;
	}

	public String getAssociationRouteTableId() {
		return associationRouteTableId;
	}

	public void setAssociationRouteTableId(String associationRouteTableId) {
		this.associationRouteTableId = associationRouteTableId;
	}

	public List<String> getPropagationRouteTableIds() {
		return propagationRouteTableIds;
	}

	public void setPropagationRouteTableIds(List<String> propagationRouteTableIds) {
		this.propagationRouteTableIds = propagationRouteTableIds;
	}

	public void removePropagationRouteTableId(String propRouteId) {
		idLoop: for (String pri : propagationRouteTableIds) {
			if (pri.equalsIgnoreCase(propRouteId)) {
				propagationRouteTableIds.remove(pri);
				break idLoop;
			}
		}
	}

	public void updatePropagationRouteTableId(String oldPropRouteId, String newPropRouteId) {
		idLoop: for (String pri : propagationRouteTableIds) {
			if (pri.equalsIgnoreCase(oldPropRouteId)) {
				removePropagationRouteTableId(oldPropRouteId);
				break idLoop;
			}
		}
		propagationRouteTableIds.add(newPropRouteId);
		// remove any blank values
		for (String pri : propagationRouteTableIds) {
			if (pri == null || pri.length() == 0) {
				removePropagationRouteTableId(pri);
			}
		}
	}

	public void addPropagationRouteTableId(String id) {
		propagationRouteTableIds.add(id);
	}
}
