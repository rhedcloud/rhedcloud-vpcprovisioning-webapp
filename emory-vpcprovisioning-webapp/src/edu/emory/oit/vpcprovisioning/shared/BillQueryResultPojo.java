package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class BillQueryResultPojo extends SharedObject implements IsSerializable {
	BillQueryFilterPojo filterUsed;
	List<BillPojo> results;

	public BillQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public BillQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(BillQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<BillPojo> getResults() {
		return results;
	}

	public void setResults(List<BillPojo> results) {
		this.results = results;
	}

}
