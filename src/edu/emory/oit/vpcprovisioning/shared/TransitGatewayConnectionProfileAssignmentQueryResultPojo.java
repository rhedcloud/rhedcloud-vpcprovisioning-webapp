package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfileAssignmentQueryResultPojo extends SharedObject implements IsSerializable {
	
	List<TransitGatewayConnectionProfileAssignmentPojo> results = new java.util.ArrayList<TransitGatewayConnectionProfileAssignmentPojo>();
	TransitGatewayConnectionProfileAssignmentQueryFilterPojo filterUsed;
	
	
	public TransitGatewayConnectionProfileAssignmentQueryResultPojo() {
	}
	
	public List<TransitGatewayConnectionProfileAssignmentPojo> getResults() {
		return results;
	}
	public void setResults(List<TransitGatewayConnectionProfileAssignmentPojo> results) {
		this.results = results;
	}
	public TransitGatewayConnectionProfileAssignmentQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}
	public void setFilterUsed(TransitGatewayConnectionProfileAssignmentQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
