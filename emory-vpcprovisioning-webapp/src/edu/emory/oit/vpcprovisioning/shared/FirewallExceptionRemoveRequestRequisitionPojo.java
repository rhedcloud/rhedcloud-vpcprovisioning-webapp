package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRemoveRequestRequisitionPojo extends SharedObject implements IsSerializable, Comparable<FirewallExceptionRemoveRequestRequisitionPojo> {
	/*
	<!ELEMENT FirewallExceptionRemoveRequestRequisition (
		UserNetID,  
		RequestDetails)>
	*/ 

	String userNetId;
	String requestDetails;
	List<String> tags = new java.util.ArrayList<String>();
	FirewallExceptionRemoveRequestRequisitionPojo baseline;
	
	public FirewallExceptionRemoveRequestRequisitionPojo() {
		
	}

	@Override
	public int compareTo(FirewallExceptionRemoveRequestRequisitionPojo o) {
		
		return 0;
	}

	public String getUserNetId() {
		return userNetId;
	}

	public void setUserNetId(String userNetId) {
		this.userNetId = userNetId;
	}

	public FirewallExceptionRemoveRequestRequisitionPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(FirewallExceptionRemoveRequestRequisitionPojo baseline) {
		this.baseline = baseline;
	}

	public String getRequestDetails() {
		return requestDetails;
	}

	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
