package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountProvisioningAuthorizationQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {

	/*
<!ELEMENT AccountProvisioningAuthorizationQuerySpecification (UserId)>
	 */
	String userId;		// ppid

	public AccountProvisioningAuthorizationQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
