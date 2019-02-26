package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcpQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
<!ELEMENT VirtualPrivateCloudProvisioningQuerySpecification (Comparison*, QueryLanguage?, ProvisioningId?, Type?, ComplianceClass?, CreateUser?, LastUpdateUser?)>
	 */

	String provisioningId;
	String type;
	String complianceClass;
	boolean defaultMaxVpcps;
	boolean allVpcps;
	int maxRows;
	String searchString;

	public VpcpQueryFilterPojo() {
		
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
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

	public boolean isDefaultMaxVpcps() {
		return defaultMaxVpcps;
	}

	public void setDefaultMaxVpcps(boolean defaultMaxVpcps) {
		this.defaultMaxVpcps = defaultMaxVpcps;
	}

	public boolean isAllVpcps() {
		return allVpcps;
	}

	public void setAllVpcps(boolean allVpcps) {
		this.allVpcps = allVpcps;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
