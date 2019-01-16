package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {

	String accountId;
	String vpcId;
	String type;
	String customerAdminUserId;
	UserAccountPojo userLoggedIn;
	boolean excludeVpcsAssignedToVpnConnectionProfiles;
//	String createUser;
//	String lastUpdateUser;

	public VpcQueryFilterPojo() {
		
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustomerAdminUserId() {
		return customerAdminUserId;
	}

	public void setCustomerAdminUserId(String customerNetId) {
		this.customerAdminUserId = customerNetId;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public boolean isExcludeVpcsAssignedToVpnConnectionProfiles() {
		return excludeVpcsAssignedToVpnConnectionProfiles;
	}

	public void setExcludeVpcsAssignedToVpnConnectionProfiles(boolean excludeVpcsAssignedToVpnConnectionProfiles) {
		this.excludeVpcsAssignedToVpnConnectionProfiles = excludeVpcsAssignedToVpnConnectionProfiles;
	}

}
