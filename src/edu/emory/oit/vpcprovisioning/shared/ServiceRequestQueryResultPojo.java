package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceRequestQueryResultPojo extends SharedObject implements IsSerializable {
	ServiceRequestQueryFilterPojo filterUsed;
	List<ServiceRequestPojo> results;

	public ServiceRequestQueryResultPojo() {
		
	}

	public ServiceRequestQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ServiceRequestQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ServiceRequestPojo> getResults() {
		return results;
	}

	public void setResults(List<ServiceRequestPojo> results) {
		this.results = results;
	}


}
