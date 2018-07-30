package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SecurityRiskDetectionQueryResultPojo extends SharedObject implements IsSerializable {
	List<SecurityRiskDetectionPojo> results;
	SecurityRiskDetectionQueryFilterPojo filterUsed;
	
	public SecurityRiskDetectionQueryResultPojo() {
	}

	public List<SecurityRiskDetectionPojo> getResults() {
		return results;
	}

	public void setResults(List<SecurityRiskDetectionPojo> results) {
		this.results = results;
	}

	public SecurityRiskDetectionQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(SecurityRiskDetectionQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

}
