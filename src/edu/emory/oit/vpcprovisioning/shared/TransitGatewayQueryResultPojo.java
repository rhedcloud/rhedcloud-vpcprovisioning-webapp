package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayQueryResultPojo extends SharedObject implements IsSerializable {
	
	List<TransitGatewayPojo> results = new java.util.ArrayList<TransitGatewayPojo>();
	TransitGatewayQueryFilterPojo filterUsed;
	
	
	public TransitGatewayQueryResultPojo() {
	}
	
	public List<TransitGatewayPojo> getResults() {
		return results;
	}
	public void setResults(List<TransitGatewayPojo> results) {
		this.results = results;
	}
	public TransitGatewayQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}
	public void setFilterUsed(TransitGatewayQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
