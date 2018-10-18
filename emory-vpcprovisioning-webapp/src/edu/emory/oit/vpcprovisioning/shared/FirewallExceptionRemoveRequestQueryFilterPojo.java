package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRemoveRequestQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
<!ELEMENT FirewallExceptionRemoveRequestQuerySpecification (
	RequestItemNumber?,  
	RequestState?, 
	RequestItemState?, 
	SystemId?, 
	UserNetID?, 
	RequestDetails?)>
	 */
	String requestState;
	String requestItemNumber;
	String requestItemState;
	String systemId;
	String userNetId;
	String requestDetails;
	List<String> tags = new java.util.ArrayList<String>();

	public FirewallExceptionRemoveRequestQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUserNetId() {
		return userNetId;
	}

	public void setUserNetId(String userNetId) {
		this.userNetId = userNetId;
	}

	public String getRequestState() {
		return requestState;
	}

	public void setRequestState(String requestState) {
		this.requestState = requestState;
	}

	public String getRequestItemNumber() {
		return requestItemNumber;
	}

	public void setRequestItemNumber(String requestItemNumber) {
		this.requestItemNumber = requestItemNumber;
	}

	public String getRequestItemState() {
		return requestItemState;
	}

	public void setRequestItemState(String requestItemState) {
		this.requestItemState = requestItemState;
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
