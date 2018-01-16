package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CidrQueryResultPojo extends SharedObject implements IsSerializable {
	CidrQueryFilterPojo filterUsed;
	List<CidrPojo> results;

	public CidrQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public CidrQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(CidrQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<CidrPojo> getResults() {
		return results;
	}

	public void setResults(List<CidrPojo> results) {
		this.results = results;
	}

}
