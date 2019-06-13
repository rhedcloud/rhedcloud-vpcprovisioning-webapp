package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class BillSummaryPojo extends SharedObject implements IsSerializable {

	String billId;
	String accountId;
	Date billDate;
	String type;
	double accountTotal;
	Date billingPeriodStartDate;
	Date billingPeriodEndDate;

	HashMap<String, LineItemSummaryPojo> lineItemSummaryMap = new HashMap<String, LineItemSummaryPojo>();
	
	public BillSummaryPojo() {
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAccountTotal() {
		return accountTotal;
	}

	public void setAccountTotal(double accountTotal) {
		this.accountTotal = accountTotal;
	}

	public Date getBillingPeriodStartDate() {
		return billingPeriodStartDate;
	}

	public void setBillingPeriodStartDate(Date billingPeriodStartDate) {
		this.billingPeriodStartDate = billingPeriodStartDate;
	}

	public Date getBillingPeriodEndDate() {
		return billingPeriodEndDate;
	}

	public void setBillingPeriodEndDate(Date billingPeriodEndDate) {
		this.billingPeriodEndDate = billingPeriodEndDate;
	}

	public HashMap<String, LineItemSummaryPojo> getLineItemSummaryMap() {
		return lineItemSummaryMap;
	}

	public void setLineItemSummaryMap(HashMap<String, LineItemSummaryPojo> lineItemSummaryMap) {
		this.lineItemSummaryMap = lineItemSummaryMap;
	}

}
