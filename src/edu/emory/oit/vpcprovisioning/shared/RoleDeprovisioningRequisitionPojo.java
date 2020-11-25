package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleDeprovisioningRequisitionPojo extends SharedObject implements IsSerializable {

	String accountId;
	String requestorId;
	boolean fromProvisioningList;
	String customRoleName;
	
	public RoleDeprovisioningRequisitionPojo() {
		
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

	public void setCustomRoleName(String roleName) {
		this.customRoleName = roleName;
	}

	public String getCustomRoleName() {
		return customRoleName;
	}

}
