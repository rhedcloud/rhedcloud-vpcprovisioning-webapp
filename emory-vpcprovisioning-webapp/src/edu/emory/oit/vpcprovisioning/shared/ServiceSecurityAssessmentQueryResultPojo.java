package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceSecurityAssessmentQueryResultPojo extends SharedObject implements IsSerializable {
	ServiceSecurityAssessmentQueryFilterPojo filterUsed;
	List<ServiceSecurityAssessmentPojo> results = new java.util.ArrayList<ServiceSecurityAssessmentPojo>();

	public ServiceSecurityAssessmentQueryResultPojo() {
		
	}

	public ServiceSecurityAssessmentQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(ServiceSecurityAssessmentQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<ServiceSecurityAssessmentPojo> getResults() {
		return results;
	}

	public void setResults(List<ServiceSecurityAssessmentPojo> results) {
		this.results = results;
	}

}
