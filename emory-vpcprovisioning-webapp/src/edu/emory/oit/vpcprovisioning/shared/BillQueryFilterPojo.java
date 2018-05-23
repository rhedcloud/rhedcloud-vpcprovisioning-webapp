package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class BillQueryFilterPojo extends SharedObject implements IsSerializable {
	/*
<!ELEMENT BillQuerySpecification (Comparison*, QueryLanguage?,PayerAccountId?, LinkedAccountId?, BillDate?, StartDate?, EndDate?, Type?)>
	 */

	String payerAccountId;
	String linkedAccountId;
	Date billDate;
	Date startDate;
	Date endDate;
	String type;
	
	public BillQueryFilterPojo() {
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

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
