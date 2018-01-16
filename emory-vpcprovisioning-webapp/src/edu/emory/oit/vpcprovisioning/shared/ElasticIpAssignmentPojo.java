package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpAssignmentPojo extends SharedObject implements IsSerializable, Comparable<ElasticIpAssignmentPojo> {
	String assignmentId;
	String ownerId;
	String description;
	String purpose;
	ElasticIpPojo elasticIp;
	ElasticIpAssignmentPojo baseline;
	
	public ElasticIpAssignmentPojo() {
		super();
	}

	@Override
	public int compareTo(ElasticIpAssignmentPojo o) {
		// TODO Auto-generated method stub
		return 0;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public ElasticIpPojo getElasticIp() {
		return elasticIp;
	}

	public void setElasticIp(ElasticIpPojo elasticIp) {
		this.elasticIp = elasticIp;
	}

	public ElasticIpAssignmentPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(ElasticIpAssignmentPojo baseline) {
		this.baseline = baseline;
	}

}
