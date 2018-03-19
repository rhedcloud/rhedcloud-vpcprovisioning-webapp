package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ResourceQueryResultPojo extends SharedObject implements IsSerializable {
	ResourceQueryFilterPojo filterUsed;
	List<ResourcePojo> results = new java.util.ArrayList<ResourcePojo>();

	public ResourceQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public ResourceQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ResourceQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ResourcePojo> getResults() {
		return results;
	}

	public void setResults(List<ResourcePojo> results) {
		this.results = results;
	}

}
