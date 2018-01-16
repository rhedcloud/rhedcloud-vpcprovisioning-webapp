package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ElasticIpAssignmentSummaryPojo extends SharedObject implements IsSerializable, Comparable<ElasticIpAssignmentSummaryPojo> {
	ElasticIpAssignmentPojo elasticIpAssignment;
	VpcPojo vpc;
	AccountPojo account;

	public ElasticIpAssignmentSummaryPojo() {
	}

	public static final ProvidesKey<ElasticIpAssignmentSummaryPojo> KEY_PROVIDER = new ProvidesKey<ElasticIpAssignmentSummaryPojo>() {
		@Override
		public Object getKey(ElasticIpAssignmentSummaryPojo item) {
			return item == null ? null : item.getElasticIpAssignment().getAssignmentId();
		}
	};

	@Override
	public int compareTo(ElasticIpAssignmentSummaryPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ElasticIpAssignmentPojo getElasticIpAssignment() {
		return elasticIpAssignment;
	}

	public void setElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment) {
		this.elasticIpAssignment = elasticIpAssignment;
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

}
