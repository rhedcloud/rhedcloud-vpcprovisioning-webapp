package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FullPersonQueryResultPojo extends SharedObject implements IsSerializable {
	FullPersonQueryFilterPojo filterUsed;
	List<FullPersonPojo> results = new java.util.ArrayList<FullPersonPojo>();

	public FullPersonQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public FullPersonQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(FullPersonQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<FullPersonPojo> getResults() {
		return results;
	}

	public void setResults(List<FullPersonPojo> results) {
		this.results = results;
	}

}
