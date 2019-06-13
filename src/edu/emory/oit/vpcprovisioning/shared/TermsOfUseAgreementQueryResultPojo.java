package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TermsOfUseAgreementQueryResultPojo extends SharedObject implements IsSerializable {
	TermsOfUseAgreementQueryFilterPojo filterUsed;
	List<TermsOfUseAgreementPojo> results = new java.util.ArrayList<TermsOfUseAgreementPojo>();

	public TermsOfUseAgreementQueryResultPojo() {
		
	}

	public TermsOfUseAgreementQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}

	public void setFilterUsed(TermsOfUseAgreementQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}

	public List<TermsOfUseAgreementPojo> getResults() {
		return results;
	}

	public void setResults(List<TermsOfUseAgreementPojo> results) {
		this.results = results;
	}

}
