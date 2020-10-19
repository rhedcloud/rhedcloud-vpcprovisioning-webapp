package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleProvisioningRequisitionPojo extends SharedObject implements IsSerializable {

	String accountId;
	String requestorId;
	boolean fromProvisioningList;
	
	public RoleProvisioningRequisitionPojo() {
		
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public boolean isFromProvisioningList() {
		return fromProvisioningList;
	}

	public void setFromProvisioningList(boolean fromProvisioningList) {
		this.fromProvisioningList = fromProvisioningList;
	}

	public void setRequestorId(String key) {
		requestorId = key;
	}

	public String getRequestorId() {
		return requestorId;
	}

}
