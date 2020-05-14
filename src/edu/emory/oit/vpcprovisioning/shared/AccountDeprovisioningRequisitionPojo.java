package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountDeprovisioningRequisitionPojo extends SharedObject implements IsSerializable {

	String accountId;
	boolean fromProvisioningList;
	
	public AccountDeprovisioningRequisitionPojo() {
		
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

}
