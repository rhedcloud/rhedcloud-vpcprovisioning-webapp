package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpAssignmentRequisitionPojo extends SharedObject implements IsSerializable {
	String ownerId;

	public ElasticIpAssignmentRequisitionPojo() {
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
