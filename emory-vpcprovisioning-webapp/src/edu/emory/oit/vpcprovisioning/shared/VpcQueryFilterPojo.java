package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcQueryFilterPojo extends SharedObject implements IsSerializable {

	String accountId;
	String vpcId;
	String type;
	String customerAdminNetId;
	UserAccountPojo userLoggedIn;
//	String createUser;
//	String lastUpdateUser;

	public VpcQueryFilterPojo() {
		// TODO Auto-generated constructor stub
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

	public String getCustomerAdminNetId() {
		return customerAdminNetId;
	}

	public void setCustomerAdminNetId(String customerNetId) {
		this.customerAdminNetId = customerNetId;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

}
