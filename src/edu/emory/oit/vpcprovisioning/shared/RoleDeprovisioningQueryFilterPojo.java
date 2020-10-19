package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleDeprovisioningQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	String accountId;
	String provisioningId;
	String deprovisioningId;
	boolean defaultMaxObjects;
	boolean allObjects;
	int maxRows;
	String searchString;
	String type;
	String complianceClass;

	public RoleDeprovisioningQueryFilterPojo() {
		
	}


	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	public String getAccountId() {
		return accountId;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public boolean isDefaultMaxObjects() {
		return defaultMaxObjects;
	}


	public void setDefaultMaxObjects(boolean defaultMaxVpcps) {
		this.defaultMaxObjects = defaultMaxVpcps;
	}


	public boolean isAllObjects() {
		return allObjects;
	}


	public void setAllObjects(boolean allVpcps) {
		this.allObjects = allVpcps;
	}


	public int getMaxRows() {
		return maxRows;
	}


	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}


	public String getSearchString() {
		return searchString;
	}


	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}


	public String getProvisioningId() {
		return provisioningId;
	}


	public void setProvisioningId(String deprovisioningId) {
		this.provisioningId = deprovisioningId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getComplianceClass() {
		return complianceClass;
	}


	public void setComplianceClass(String complianceClass) {
		this.complianceClass = complianceClass;
	}


	public String getDeprovisioningId() {
		return deprovisioningId;
	}


	public void setDeprovisioningId(String deprovisioningId) {
		this.deprovisioningId = deprovisioningId;
	}



}
