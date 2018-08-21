package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class IncidentQueryResultPojo extends SharedObject implements IsSerializable {
	List<IncidentPojo> results;
	IncidentQueryFilterPojo filterUsed;
	
	public IncidentQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<IncidentPojo> getResults() {
		return results;
	}

	public void setResults(List<IncidentPojo> results) {
		this.results = results;
	}

	public IncidentQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(IncidentQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
