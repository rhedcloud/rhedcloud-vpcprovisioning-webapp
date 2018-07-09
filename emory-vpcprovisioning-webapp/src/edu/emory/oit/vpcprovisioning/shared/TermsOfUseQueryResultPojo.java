package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TermsOfUseQueryResultPojo extends SharedObject implements IsSerializable {
	TermsOfUseQueryFilterPojo filterUsed;
	List<TermsOfUsePojo> results = new java.util.ArrayList<TermsOfUsePojo>();

	public TermsOfUseQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

	public TermsOfUseQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(TermsOfUseQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<TermsOfUsePojo> getResults() {
		return results;
	}

	public void setResults(List<TermsOfUsePojo> results) {
		this.results = results;
	}

}
