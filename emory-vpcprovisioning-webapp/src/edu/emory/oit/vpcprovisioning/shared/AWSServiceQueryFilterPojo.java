package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AWSServiceQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {
	/*
		<!ELEMENT ServiceQuerySpecification (
			Comparison*, 
			QueryLanguage?, 
			ServiceId?, 
			AwsServiceCode?, 
			AwsServiceName?, 
			AwsStatus?, 
			SiteStatus?, 
			Category*, 
			ConsoleCategory*, 
			AwsHipaaEligible?, 
			SiteHipaaEligible?, 
			Tag*)>
	 */
	String serviceId;
	String awsServiceCode;
	String awsServiceName;
	String awsStatus;
	String siteStatus;
	List<String> categories = new java.util.ArrayList<String>();
	List<String> consoleCategories = new java.util.ArrayList<String>();
	String awsHipaaEligible;
	String siteHipaaEligible;
	List<AWSTagPojo> tags = new java.util.ArrayList<AWSTagPojo>(); 
	boolean fuzzyFilter=false;

	public AWSServiceQueryFilterPojo() {
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getAwsServiceCode() {
		return awsServiceCode;
	}

	public void setAwsServiceCode(String serviceCode) {
		this.awsServiceCode = serviceCode;
	}

	public String getAwsStatus() {
		return awsStatus;
	}

	public void setStatus(String status) {
		this.awsStatus = status;
	}

	public String getAwsServiceName() {
		return awsServiceName;
	}

	public void setAwsServiceName(String awsServiceName) {
		this.awsServiceName = awsServiceName;
	}

	public String getSiteStatus() {
		return siteStatus;
	}

	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getConsoleCategories() {
		return consoleCategories;
	}

	public void setConsoleCategories(List<String> consoleCategories) {
		this.consoleCategories = consoleCategories;
	}

	public boolean isAwsHipaaEligible() {
		if (awsHipaaEligible != null) {
			if (awsHipaaEligible.equalsIgnoreCase(Constants.TRUE)) {
				return true;
			}
		}
		return false;
	}

	public String getAwsHipaaEligible() {
		return awsHipaaEligible;
	}

	public void setAwsHipaaEligible(String awsHipaaEligible) {
		this.awsHipaaEligible = awsHipaaEligible;
	}

	public boolean isSiteHipaaEligible() {
		if (siteHipaaEligible != null) {
			if (siteHipaaEligible.equalsIgnoreCase(Constants.TRUE)) {
				return true;
			}
		}
		return false;
	}

	public String getSiteHipaaEligible() {
		return siteHipaaEligible;
	}

	public void setSiteHipaaEligible(String siteHipaaEligible) {
		this.siteHipaaEligible = siteHipaaEligible;
	}

	public List<AWSTagPojo> getTags() {
		return tags;
	}

	public void setTags(List<AWSTagPojo> tags) {
		this.tags = tags;
	}

	public void setAwsStatus(String awsStatus) {
		this.awsStatus = awsStatus;
	}

	public boolean isFuzzyFilter() {
		return fuzzyFilter;
	}

	public void setFuzzyFilter(boolean fuzzyFilter) {
		this.fuzzyFilter = fuzzyFilter;
	}

}
