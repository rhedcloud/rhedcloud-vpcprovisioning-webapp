package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class VpcPojo extends SharedObject implements IsSerializable, Comparable<VpcPojo> {

	/*
<!ELEMENT VirtualPrivateCloud (VpcId, AccountId, Cidr, VpnProfileId, Type, Purpose, CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?)>	 
	 */
	String accountId;
	String accountName;
	String vpcId;
	String type;
//	String complianceClass;
	List<String> customerAdminNetIdList = new java.util.ArrayList<String>();
	String cidr;
	String vpnConnectionProfileId;
	String purpose;
	String region;
	List<PropertyPojo> properties = new java.util.ArrayList<PropertyPojo>();
	VpcPojo baseline;
	
	public static final ProvidesKey<VpcPojo> KEY_PROVIDER = new ProvidesKey<VpcPojo>() {
		@Override
		public Object getKey(VpcPojo item) {
			return item == null ? null : item.getAccountId() + item.getVpcId();
		}
	};
	public VpcPojo() {
		
	}

	@Override
	public int compareTo(VpcPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
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

//	public List<String> getCustomerAdminNetIdList() {
//		return customerAdminNetIdList;
//	}
//
//	public void setCustomerAdminNetIdList(List<String> customerAdminNetIdList) {
//		this.customerAdminNetIdList = customerAdminNetIdList;
//	}

	public VpcPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(VpcPojo baseline) {
		this.baseline = baseline;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getVpnConnectionProfileId() {
		return vpnConnectionProfileId;
	}

	public void setVpnConnectionProfileId(String vpnConnectionProfileId) {
		this.vpnConnectionProfileId = vpnConnectionProfileId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public List<PropertyPojo> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyPojo> properties) {
		this.properties = properties;
	}

//	public String getComplianceClass() {
//		return complianceClass;
//	}
//
//	public void setComplianceClass(String complianceClass) {
//		this.complianceClass = complianceClass;
//	}

}
