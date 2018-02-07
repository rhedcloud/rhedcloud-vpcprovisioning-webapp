package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ElasticIpPojo extends SharedObject implements IsSerializable, Comparable<ElasticIpPojo> {
	String elasticIpId;
	String elasticIp;
	String allocationId;
	String instance;
	String privateIpAddress;
	String scope;
	String associationId;
	String networkInterfaceId;

	public static final ProvidesKey<ElasticIpPojo> KEY_PROVIDER = new ProvidesKey<ElasticIpPojo>() {
		@Override
		public Object getKey(ElasticIpPojo item) {
			return item == null ? null : item.getElasticIp();
		}
	};
	public ElasticIpPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(ElasticIpPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getElasticIpId() {
		return elasticIpId;
	}

	public void setElasticIpId(String elasticIpId) {
		this.elasticIpId = elasticIpId;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAssociationId() {
		return associationId;
	}

	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}

	public String getNetworkInterfaceId() {
		return networkInterfaceId;
	}

	public void setNetworkInterfaceId(String networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}

	public String getElasticIp() {
		return elasticIp;
	}

	public void setElasticIp(String elasticIp) {
		this.elasticIp = elasticIp;
	}

}
