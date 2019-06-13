package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ElasticIpSummaryPojo extends SharedObject implements IsSerializable, Comparable<ElasticIpSummaryPojo> {
	ElasticIpPojo elasticIp;
	ElasticIpAssignmentPojo elasticIpAssignment;

	public ElasticIpSummaryPojo() {
	}

	public static final ProvidesKey<ElasticIpSummaryPojo> KEY_PROVIDER = new ProvidesKey<ElasticIpSummaryPojo>() {
		@Override
		public Object getKey(ElasticIpSummaryPojo item) {
			if (item.getElasticIp() != null) {
				return item.getElasticIp().getElasticIpId();
			}
			else {
				return item.getElasticIpAssignment().getElasticIp().getElasticIpId();
			}
		}
	};

	@Override
	public int compareTo(ElasticIpSummaryPojo o) {
		return 0;
	}

	public ElasticIpPojo getElasticIp() {
		return elasticIp;
	}

	public void setElasticIp(ElasticIpPojo elasticIp) {
		this.elasticIp = elasticIp;
	}

	public ElasticIpAssignmentPojo getElasticIpAssignment() {
		return elasticIpAssignment;
	}

	public void setElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment) {
		this.elasticIpAssignment = elasticIpAssignment;
	}

}
