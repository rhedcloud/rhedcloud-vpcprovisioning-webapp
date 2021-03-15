package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayStatusQueryResultPojo extends SharedObject implements IsSerializable {
	
	List<TransitGatewayStatusPojo> results = new java.util.ArrayList<TransitGatewayStatusPojo>();
	TransitGatewayStatusQueryFilterPojo filterUsed;
	
	
	public TransitGatewayStatusQueryResultPojo() {
	}
	
	public List<TransitGatewayStatusPojo> getResults() {
		return results;
	}
	public void setResults(List<TransitGatewayStatusPojo> results) {
		this.results = results;
	}
	public TransitGatewayStatusQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}
	public void setFilterUsed(TransitGatewayStatusQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
