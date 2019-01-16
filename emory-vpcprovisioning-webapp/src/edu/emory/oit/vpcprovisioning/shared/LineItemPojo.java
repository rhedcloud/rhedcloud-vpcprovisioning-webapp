package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class LineItemPojo extends SharedObject implements IsSerializable, Comparable<LineItemPojo> {

	String invoiceId;
	String payerAccountId;
	String linkedAccountId;
	String recordType;
	String recordId;
	Date billingPeriodStartDatetime;
	Date billingPeriodEndDatetime;
	Date invoiceDatetime;
	String payerAccountName;
	String linkedAccountName;
	String taxationAddress;
	String payerPONumber;
	String productCode;
	String productName;
	String sellerOfRecord;
	String usageType;
	String operation;
	String rateId;
	String itemDescription;
	Date usageStartDatetime;
	Date usageEndDatetime;
	double usageQuantity;
	double blendedRate;
	String currencyCode;
	double costBeforeTax;
	double credits;
	double taxAmount;
	String taxType;
	double totalCost;
	
	
	public static final ProvidesKey<LineItemPojo> KEY_PROVIDER = new ProvidesKey<LineItemPojo>() {
		@Override
		public Object getKey(LineItemPojo item) {
			return item == null ? null : item.getInvoiceId();
		}
	};
	public LineItemPojo() {
		
	}

	@Override
	public int compareTo(LineItemPojo o) {
		
		return 0;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getPayerAccountId() {
		return payerAccountId;
	}

	public void setPayerAccountId(String payerAccountId) {
		this.payerAccountId = payerAccountId;
	}

	public String getLinkedAccountId() {
		return linkedAccountId;
	}

	public void setLinkedAccountId(String linkedAccountId) {
		this.linkedAccountId = linkedAccountId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Date getBillingPeriodStartDatetime() {
		return billingPeriodStartDatetime;
	}

	public void setBillingPeriodStartDatetime(Date billingPeriodStartDatetime) {
		this.billingPeriodStartDatetime = billingPeriodStartDatetime;
	}

	public Date getBillingPeriodEndDatetime() {
		return billingPeriodEndDatetime;
	}

	public void setBillingPeriodEndDatetime(Date billingPeriodEndDatetime) {
		this.billingPeriodEndDatetime = billingPeriodEndDatetime;
	}

	public Date getInvoiceDatetime() {
		return invoiceDatetime;
	}

	public void setInvoiceDatetime(Date invoiceDatetime) {
		this.invoiceDatetime = invoiceDatetime;
	}

	public String getPayerAccountName() {
		return payerAccountName;
	}

	public void setPayerAccountName(String payerAccountName) {
		this.payerAccountName = payerAccountName;
	}

	public String getLinkedAccountName() {
		return linkedAccountName;
	}

	public void setLinkedAccountName(String linkedAccountName) {
		this.linkedAccountName = linkedAccountName;
	}

	public String getTaxationAddress() {
		return taxationAddress;
	}

	public void setTaxationAddress(String taxationAddress) {
		this.taxationAddress = taxationAddress;
	}

	public String getPayerPONumber() {
		return payerPONumber;
	}

	public void setPayerPONumber(String payerPONumber) {
		this.payerPONumber = payerPONumber;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSellerOfRecord() {
		return sellerOfRecord;
	}

	public void setSellerOfRecord(String sellerOfRecord) {
		this.sellerOfRecord = sellerOfRecord;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRateId() {
		return rateId;
	}

	public void setRateId(String rateId) {
		this.rateId = rateId;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Date getUsageStartDatetime() {
		return usageStartDatetime;
	}

	public void setUsageStartDatetime(Date usageStartDatetime) {
		this.usageStartDatetime = usageStartDatetime;
	}

	public Date getUsageEndDatetime() {
		return usageEndDatetime;
	}

	public void setUsageEndDatetime(Date usageEndDatetime) {
		this.usageEndDatetime = usageEndDatetime;
	}

	public double getUsageQuantity() {
		return usageQuantity;
	}

	public void setUsageQuantity(double usageQuantity) {
		this.usageQuantity = usageQuantity;
	}

	public double getBlendedRate() {
		return blendedRate;
	}

	public void setBlendedRate(double blendedRate) {
		this.blendedRate = blendedRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getCostBeforeTax() {
		return costBeforeTax;
	}

	public void setCostBeforeTax(double costBeforeTax) {
		this.costBeforeTax = costBeforeTax;
	}

	public double getCredits() {
		return credits;
	}

	public void setCredits(double credits) {
		this.credits = credits;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

}
