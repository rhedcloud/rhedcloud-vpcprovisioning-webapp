package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpAssignmentQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
//	<!ELEMENT ElasticIpAssignmentQuerySpecification (Comparison*, QueryLanguage?,ElasticIpAssignmentId?, OwnerId?,ElasticIpId?) >

	String assignmentId;
	String ownerId;		// NOTE: this is the VPC ID
	String elasticIpId;
	String elasticIpAddress;

	public ElasticIpAssignmentQueryFilterPojo() {
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
