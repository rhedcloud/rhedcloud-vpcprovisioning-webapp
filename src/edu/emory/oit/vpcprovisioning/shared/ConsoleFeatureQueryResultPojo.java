package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ConsoleFeatureQueryResultPojo extends SharedObject implements IsSerializable {
	ConsoleFeatureQueryFilterPojo filterUsed;
	List<ConsoleFeaturePojo> results = new java.util.ArrayList<ConsoleFeaturePojo>();

	public ConsoleFeatureQueryResultPojo() {
	}

	public ConsoleFeatureQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ConsoleFeatureQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ConsoleFeaturePojo> getResults() {
		return results;
	}

	public void setResults(List<ConsoleFeaturePojo> results) {
		this.results = results;
	}

}
