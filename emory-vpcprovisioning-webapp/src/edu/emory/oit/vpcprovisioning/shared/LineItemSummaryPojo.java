package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class LineItemSummaryPojo extends SharedObject implements IsSerializable {
	
	String productCode;
	String productName;
	double lineItemTotal;
	List<LineItemPojo> lineItems = new java.util.ArrayList<LineItemPojo>();

	public LineItemSummaryPojo() {
		
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

	public double getLineItemTotal() {
		return lineItemTotal;
	}

	public void setLineItemTotal(double lineItemTotal) {
		this.lineItemTotal = lineItemTotal;
	}

	public List<LineItemPojo> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItemPojo> lineItems) {
		this.lineItems = lineItems;
	}

}
