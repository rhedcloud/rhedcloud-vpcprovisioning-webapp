package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ElasticIpPojo extends SharedObject implements IsSerializable, Comparable<ElasticIpPojo> {
	String elasticIpId;
	String elasticIpAddress;
	String associatedIpAddress;
	ElasticIpPojo baseline;

	public static final ProvidesKey<ElasticIpPojo> KEY_PROVIDER = new ProvidesKey<ElasticIpPojo>() {
		@Override
		public Object getKey(ElasticIpPojo item) {
			return item == null ? null : item.getElasticIpId();
		}
	};
	public ElasticIpPojo() {
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

	public String getElasticIpAddress() {
		return elasticIpAddress;
	}

	public void setElasticIpAddress(String elasticIpAddress) {
		this.elasticIpAddress = elasticIpAddress;
	}

	public String getAssociatedIpAddress() {
		return associatedIpAddress;
	}

	public void setAssociatedIpAddress(String associatedIpAddress) {
		this.associatedIpAddress = associatedIpAddress;
	}

	public ElasticIpPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(ElasticIpPojo baseline) {
		this.baseline = baseline;
	}


}
