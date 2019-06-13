package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	String elasticIpId;
	String elasticIpAddress;
	String associatedIpAddress;
	String ownerId;

	public ElasticIpQueryFilterPojo() {
		super();
	}

	public String getElasticIpId() {
		return elasticIpId;
	}

	public void setElasticIpId(String elasticIpId) {
		this.elasticIpId = elasticIpId;
	}

	public String getElasticIpAddress() {
		return elasticIpAddress;
	}

	public void setElasticIpAddress(String ipAddress) {
		this.elasticIpAddress = ipAddress;
	}

	public String getAssociatedIpAddress() {
		return associatedIpAddress;
	}

	public void setAssociatedIpAddress(String associatedIpAddress) {
		this.associatedIpAddress = associatedIpAddress;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
