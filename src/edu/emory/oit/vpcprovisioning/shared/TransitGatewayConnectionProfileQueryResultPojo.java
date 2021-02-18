package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfileQueryResultPojo extends SharedObject implements IsSerializable {
	
	List<TransitGatewayConnectionProfileSummaryPojo> results = new java.util.ArrayList<TransitGatewayConnectionProfileSummaryPojo>();
	TransitGatewayConnectionProfileQueryFilterPojo filterUsed;
	
	
	public TransitGatewayConnectionProfileQueryResultPojo() {
	}
	
	public List<TransitGatewayConnectionProfileSummaryPojo> getResults() {
		return results;
	}
	public void setResults(List<TransitGatewayConnectionProfileSummaryPojo> results) {
		this.results = results;
	}
	public TransitGatewayConnectionProfileQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}
	public void setFilterUsed(TransitGatewayConnectionProfileQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
