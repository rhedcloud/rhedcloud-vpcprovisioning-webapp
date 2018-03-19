package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DirectoryPersonQueryResultPojo extends SharedObject implements IsSerializable {
	DirectoryPersonQueryFilterPojo filterUsed;
	List<DirectoryPersonPojo> results = new java.util.ArrayList<DirectoryPersonPojo>();

	public DirectoryPersonQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public DirectoryPersonQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(DirectoryPersonQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<DirectoryPersonPojo> getResults() {
		return results;
	}

	public void setResults(List<DirectoryPersonPojo> results) {
		this.results = results;
	}

}
