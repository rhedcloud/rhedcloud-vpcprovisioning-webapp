package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrRequisitionPojo extends SharedObject implements IsSerializable {
	String requestorNetId;
	String purpose;
	String description;
	String bits;

	public CidrRequisitionPojo() {
	}

	public String getRequestorNetId() {
		return requestorNetId;
	}

	public void setRequestorNetId(String requestorNetId) {
		this.requestorNetId = requestorNetId;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBits() {
		return bits;
	}

	public void setBits(String bits) {
		this.bits = bits;
	}

	
}
