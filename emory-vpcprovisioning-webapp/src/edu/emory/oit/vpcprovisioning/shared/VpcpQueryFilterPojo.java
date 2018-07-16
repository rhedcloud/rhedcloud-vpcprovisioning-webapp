package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcpQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
<!ELEMENT VirtualPrivateCloudProvisioningQuerySpecification (Comparison*, QueryLanguage?, ProvisioningId?, Type?, ComplianceClass?, CreateUser?, LastUpdateUser?)>
	 */

	// TODO: Comparison and QueryLanguage
	String provisioningId;
	String type;
	String complianceClass;
	
	public VpcpQueryFilterPojo() {
		// TODO Auto-generated constructor stub
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

}
