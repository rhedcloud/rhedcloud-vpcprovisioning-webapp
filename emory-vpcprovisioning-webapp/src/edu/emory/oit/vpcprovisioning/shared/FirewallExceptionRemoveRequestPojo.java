package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRemoveRequestPojo extends SharedObject implements IsSerializable, Comparable<FirewallExceptionRemoveRequestPojo> {
	/*
<!ELEMENT FirewallExceptionRemoveRequest (
	RequestItemNumber,  
	RequestState, 
	RequestItemState, 
	SystemId, 
	UserNetID, 
	RequestDetails)> 
	 */
	String requestItemNumber;
	String requestState;
	String requestItemState;
	String systemId;
	String userNetId;
	String requestDetails;
	List<String> tags = new java.util.ArrayList<String>();
	FirewallExceptionRemoveRequestPojo baseline;
	
	public FirewallExceptionRemoveRequestPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(FirewallExceptionRemoveRequestPojo o) {
		// TODO Auto-generated method stub
		return 0;
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

	public FirewallExceptionRemoveRequestPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(FirewallExceptionRemoveRequestPojo baseline) {
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
