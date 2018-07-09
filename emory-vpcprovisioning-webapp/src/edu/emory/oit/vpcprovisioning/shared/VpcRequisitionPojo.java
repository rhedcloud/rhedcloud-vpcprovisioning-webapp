package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcRequisitionPojo extends SharedObject implements IsSerializable {

	String accountId;
	String ticketId;
	String authenticatedRequestorUserId;
	String accountOwnerUserId;
	String speedType;
	List<String> customerAdminUserIdList = new java.util.ArrayList<String>();
	String type;
	String complianceClass;
	String purpose;
	boolean notifyAdmins;
	List<String> sensitiveDataList = new java.util.ArrayList<String>();
	
	public VpcRequisitionPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getAuthenticatedRequestorUserId() {
		return authenticatedRequestorUserId;
	}

	public void setAuthenticatedRequestorUserId(String authenticatedRequestorUserId) {
		this.authenticatedRequestorUserId = authenticatedRequestorUserId;
	}

	public String getComplianceClass() {
		return complianceClass;
	}

	public void setComplianceClass(String complianceClass) {
		this.complianceClass = complianceClass;
	}

	public boolean isNotifyAdmins() {
		return notifyAdmins;
	}

	public void setNotifyAdmins(boolean notifyAdmins) {
		this.notifyAdmins = notifyAdmins;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountOwnerUserId() {
		return accountOwnerUserId;
	}

	public void setAccountOwnerUserId(String accountOwnerUserId) {
		this.accountOwnerUserId = accountOwnerUserId;
	}

	public String getSpeedType() {
		return speedType;
	}

	public void setSpeedType(String speedType) {
		this.speedType = speedType;
	}

	public List<String> getCustomerAdminUserIdList() {
		return customerAdminUserIdList;
	}

	public void setCustomerAdminUserIdList(List<String> customerAdminUserIdList) {
		this.customerAdminUserIdList = customerAdminUserIdList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public List<String> getSensitiveDataList() {
		return sensitiveDataList;
	}

	public void setSensitiveDataList(List<String> sensitiveDataList) {
		this.sensitiveDataList = sensitiveDataList;
	}

}
