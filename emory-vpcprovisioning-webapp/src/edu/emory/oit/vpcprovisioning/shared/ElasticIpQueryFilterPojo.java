package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpQueryFilterPojo extends SharedObject implements IsSerializable {
	String elasticIpId;
	String elasticIpAddress;
	String associatedIpAddress;

	public ElasticIpQueryFilterPojo() {
		super();
		// TODO Auto-generated constructor stub
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

}
