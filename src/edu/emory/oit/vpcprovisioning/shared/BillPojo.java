package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class BillPojo extends SharedObject implements IsSerializable, Comparable<BillPojo> {

	String billId;
	String payerAccountId;
	Date billDate;
	String type;
	List<LineItemPojo> lineItems = new java.util.ArrayList<LineItemPojo>();
	double accountTotal;
	Date billingPeriodStartDate;
	Date billingPeriodEndDate;
	
	public BillPojo() {
	}

	@Override
	public int compareTo(BillPojo o) {
		
		return 0;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getPayerAccountId() {
		return payerAccountId;
	}

	public void setPayerAccountId(String payerAccountId) {
		this.payerAccountId = payerAccountId;
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

	public List<LineItemPojo> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItemPojo> lineItems) {
		this.lineItems = lineItems;
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

}
