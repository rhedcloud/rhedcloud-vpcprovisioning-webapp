package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class AccountPojo extends SharedObject implements IsSerializable, Comparable<AccountPojo> {
	String accountId;
	String passwordLocation;
	String accountName;
	List<EmailPojo> emailList = new java.util.ArrayList<EmailPojo>();
//	String accountOwnerNetId;
	DirectoryMetaDataPojo accountOwnerDirectoryMetaData;
	String speedType;
//	List<String> customerAdminNetIdList = new java.util.ArrayList<String>();
//	List<RoleAssignmentPojo> roleAssignments = new ArrayList<RoleAssignmentPojo>();
	String complianceClass;
	List<String> sensitiveDataList = new java.util.ArrayList<String>();
	List<PropertyPojo> properties = new java.util.ArrayList<PropertyPojo>();
	AccountPojo baseline;
	
	public static final ProvidesKey<AccountPojo> KEY_PROVIDER = new ProvidesKey<AccountPojo>() {
		@Override
		public Object getKey(AccountPojo item) {
			return item == null ? null : item.getAccountId();
		}
	};
	public AccountPojo() {
	}

	@Override
	public int compareTo(AccountPojo o) {
		
		return 0;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public List<EmailPojo> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<EmailPojo> emailList) {
		this.emailList = emailList;
	}

//	public String getAccountOwnerNetId() {
//		return accountOwnerNetId;
//	}

//	public void setAccountOwnerNetId(String accountOwnerNetId) {
//		this.accountOwnerNetId = accountOwnerNetId;
//	}

	public String getSpeedType() {
		return speedType;
	}

	public void setSpeedType(String speedType) {
		this.speedType = speedType;
	}

	public String getPasswordLocation() {
		return passwordLocation;
	}

	public void setPasswordLocation(String passwordLocation) {
		this.passwordLocation = passwordLocation;
	}

	public AccountPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(AccountPojo baseline) {
		this.baseline = baseline;
	}

//	public List<String> getCustomerAdminNetIdList() {
//		return customerAdminNetIdList;
//	}
//
//	public void setCustomerAdminNetIdList(List<String> customerAdminNetIdList) {
//		this.customerAdminNetIdList = customerAdminNetIdList;
//	}

	public boolean containsEmail(EmailPojo email) {
		for (EmailPojo pojo : this.emailList) {
			if (pojo.getEmailAddress().equalsIgnoreCase(email.getEmailAddress()) && 
				pojo.getType().equalsIgnoreCase(email.getType())) {
				
				return true;
			}
		}
		return false;
	}

	public DirectoryMetaDataPojo getAccountOwnerDirectoryMetaData() {
		return accountOwnerDirectoryMetaData;
	}

	public void setAccountOwnerDirectoryMetaData(DirectoryMetaDataPojo accountOwnerDirectoryMetaData) {
		this.accountOwnerDirectoryMetaData = accountOwnerDirectoryMetaData;
	}

//	public List<RoleAssignmentPojo> getRoleAssignments() {
//		return roleAssignments;
//	}
//
//	public void setRoleAssignments(List<RoleAssignmentPojo> roleAssignments) {
//		this.roleAssignments = roleAssignments;
//	}

	public String getComplianceClass() {
		return complianceClass;
	}

	public void setComplianceClass(String complianceClass) {
		this.complianceClass = complianceClass;
	}

	public List<String> getSensitiveDataList() {
		return sensitiveDataList;
	}

	public void setSensitiveDataList(List<String> sensitiveDataList) {
		this.sensitiveDataList = sensitiveDataList;
	}

	public List<PropertyPojo> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyPojo> properties) {
		this.properties = properties;
	}
}
