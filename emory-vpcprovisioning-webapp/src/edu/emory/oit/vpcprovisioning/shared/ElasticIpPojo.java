package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ElasticIpPojo extends SharedObject implements IsSerializable, Comparable<ElasticIpPojo> {
	String elasticIpId;

	public static final ProvidesKey<ElasticIpPojo> KEY_PROVIDER = new ProvidesKey<ElasticIpPojo>() {
		@Override
		public Object getKey(ElasticIpPojo item) {
			return null;
//			return item == null ? null : item.getAccountId();
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

}
