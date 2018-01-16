package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpAssignmentStatusPojo extends SharedObject implements IsSerializable {
	boolean assigned;
	ElasticIpAssignmentPojo elasticIpAssignment;
	
	public ElasticIpAssignmentStatusPojo() {
		super();
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public ElasticIpAssignmentPojo getElasticIpAssignment() {
		return elasticIpAssignment;
	}

	public void setElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment) {
		this.elasticIpAssignment = elasticIpAssignment;
	}

}
