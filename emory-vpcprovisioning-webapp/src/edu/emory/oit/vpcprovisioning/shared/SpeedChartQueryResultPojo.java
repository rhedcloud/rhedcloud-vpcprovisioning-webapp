package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SpeedChartQueryResultPojo extends SharedObject implements IsSerializable {
	SpeedChartQueryFilterPojo filterUsed;
	List<SpeedChartPojo> results;

	public SpeedChartQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public SpeedChartQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(SpeedChartQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<SpeedChartPojo> getResults() {
		return results;
	}

	public void setResults(List<SpeedChartPojo> results) {
		this.results = results;
	}

}
