package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpcRequisitionPojo extends SharedObject implements IsSerializable {

	String accountId;
	String ticketId;
	String authenticatedRequestorNetId;
	String accountOwnerNetId;
	String financialAccountNumber;
	List<String> customerAdminNetIdList = new java.util.ArrayList<String>();
	String type;
	String complianceClass;
	boolean notifyAdmins;
	
	public VpcRequisitionPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getAuthenticatedRequestorNetId() {
		return authenticatedRequestorNetId;
	}

	public void setAuthenticatedRequestorNetId(String authenticatedRequestorNetId) {
		this.authenticatedRequestorNetId = authenticatedRequestorNetId;
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

	public String getAccountOwnerNetId() {
		return accountOwnerNetId;
	}

	public void setAccountOwnerNetId(String accountOwnerNetId) {
		this.accountOwnerNetId = accountOwnerNetId;
	}

	public String getFinancialAccountNumber() {
		return financialAccountNumber;
	}

	public void setFinancialAccountNumber(String financialAccountNumber) {
		this.financialAccountNumber = financialAccountNumber;
	}

	public List<String> getCustomerAdminNetIdList() {
		return customerAdminNetIdList;
	}

	public void setCustomerAdminNetIdList(List<String> customerAdminNetIdList) {
		this.customerAdminNetIdList = customerAdminNetIdList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
