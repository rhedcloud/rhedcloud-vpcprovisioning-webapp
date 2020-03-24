package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class AWSServicePojo extends SharedObject implements IsSerializable, Comparable<AWSServicePojo> {
	/*
	<!ELEMENT Service (
		ServiceId?, 
		AwsServiceCode, 
		AwsServiceName, 
		AlternateServiceName?, 
		CombinedServiceName?, 
		AwsStatus, 
		SiteStatus, 
		AwsServiceLandingPageUrl?, 
		SiteServiceLandingPageUrl?, 
		Description?, 
		Category*, 
		ConsoleCategory*, 
		AwsHipaaEligible?, 
		SiteHipaaEligible?, 
		Tag*, 
		CreateUser, 
		CreateDatetime, 
		LastUpdateUser?, 
		LastUpdateDatetime?)>
	 */
	String serviceId;
	String awsServiceCode;
	String awsServiceName;
	String alternateServiceName;
	String combinedServiceName;
	String awsStatus;
	String siteStatus;
	String awsLandingPageUrl;
	String siteLandingPageUrl;
	String description;
	List<String> awsCategories = new java.util.ArrayList<String>();
	List<String> consoleCategories = new java.util.ArrayList<String>();
	String awsHipaaEligible;
	String siteHipaaEligible;
	List<AWSTagPojo> tags = new java.util.ArrayList<AWSTagPojo>(); 
	AWSServicePojo baseline;
	boolean skeleton;

	public static final ProvidesKey<AWSServicePojo> KEY_PROVIDER = new ProvidesKey<AWSServicePojo>() {
		@Override
		public Object getKey(AWSServicePojo item) {
			return item == null ? null : item.getAwsServiceName();
		}
	};

	public AWSServicePojo() {
	}


	public List<String> getAwsCategories() {
		return awsCategories;
	}


	public void setAwsCategories(List<String> categories) {
		this.awsCategories = categories;
	}


	public String getAwsServiceName() {
		return awsServiceName;
	}


	public void setAwsServiceName(String name) {
		this.awsServiceName = name;
	}


	public String getAwsServiceCode() {
		return awsServiceCode;
	}


	public void setAwsServiceCode(String code) {
		this.awsServiceCode = code;
	}


	public String getAwsStatus() {
		return awsStatus;
	}


	public void setAwsStatus(String status) {
		this.awsStatus = status;
	}


	public String getServiceId() {
		return serviceId;
	}


	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	public String getAwsLandingPageUrl() {
		return awsLandingPageUrl;
	}


	public void setAwsLandingPageUrl(String landingPage) {
		this.awsLandingPageUrl = landingPage;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public List<AWSTagPojo> getTags() {
		return tags;
	}


	public void setTags(List<AWSTagPojo> tags) {
		this.tags = tags;
	}


	public List<String> getConsoleCategories() {
		return consoleCategories;
	}


	public void setConsoleCategories(List<String> consoleCategories) {
		this.consoleCategories = consoleCategories;
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
		return this.siteHipaaEligible;
	}
	public void setSiteHipaaEligible(String emoryHipaaEligible) {
		this.siteHipaaEligible = emoryHipaaEligible;
	}


	public AWSServicePojo getBaseline() {
		return baseline;
	}


	public void setBaseline(AWSServicePojo baseline) {
		this.baseline = baseline;
	}


	public String getAlternateServiceName() {
		return alternateServiceName;
	}


	public void setAlternateServiceName(String alternateServiceName) {
		this.alternateServiceName = alternateServiceName;
	}


	public String getCombinedServiceName() {
		return combinedServiceName;
	}


	public void setCombinedServiceName(String combinedServiceName) {
		this.combinedServiceName = combinedServiceName;
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
		return this.awsHipaaEligible;
	}
	public void setAwsHipaaEligible(String awsHipaaEligible) {
		this.awsHipaaEligible = awsHipaaEligible;
	}


	public boolean hasTag(AWSTagPojo tag) {
		for (AWSTagPojo t : tags) {
			if (t.getKey().equalsIgnoreCase(tag.getKey())) {
				return true;
			}
		}
		return false;
	}


	public boolean isSkeleton() {
		if (alternateServiceName != null && alternateServiceName.length() > 0) {
			return false;
		}
		if (combinedServiceName != null && combinedServiceName.length() > 0) {
			return false;
		}
//		if (landingPageURL != null && landingPageURL.length() > 0) {
//			return false;
//		}
		if (description != null && description.length() > 0) {
			return false;
		}
		if (consoleCategories.size() > 0) {
			return false;
		}
		if (tags.size() > 0) {
			return false;
		}
		return true;
	}


	public void setSkeleton(boolean skeleton) {
		this.skeleton = skeleton;
	}


	public String getSiteStatus() {
		return siteStatus;
	}


	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}


	public String getSiteLandingPageUrl() {
		return siteLandingPageUrl;
	}


	public void setSiteLandingPageUrl(String siteLandingPageUrl) {
		this.siteLandingPageUrl = siteLandingPageUrl;
	}


	@Override
	public int compareTo(AWSServicePojo o) {
		String c1 = this.getCombinedServiceName();
		String c2 = o.getCombinedServiceName();
		String a1 = this.getAlternateServiceName();
		String a2 = o.getAlternateServiceName();
		String aws1 = this.getAwsServiceName();
		String aws2 = o.getAwsServiceName();
		
		if (c1 != null && c2 != null) {
			return c1.compareTo(c2);
		}
		if (c1 != null && a2 != null) {
			return c1.compareTo(a2);
		}
		if (c1 != null && aws2 != null) {
			return c1.compareTo(aws2);
		}
		if (a1 != null && c2 != null) {
			return a1.compareTo(c2);
		}
		if (a1 != null && a2 != null) {
			return a1.compareTo(a2);
		}
		if (a1 != null && aws2 != null) {
			return a1.compareTo(aws2);
		}
		if (aws1 != null && c2 != null) {
			return aws1.compareTo(c2);
		}
		if (aws1 != null && a2 != null) {
			return aws1.compareTo(a2);
		}
		if (aws1 != null && aws2 != null) {
			return aws1.compareTo(aws2);
		}

		return 0;
	}

	public boolean isBlockedPendingReview() {
		if (siteStatus == null) {
			return true;
		}
		if (siteStatus.equalsIgnoreCase("blocked pending review")) {
			return true;
		}
		return false;
	}
	public boolean isBlocked() {
		if (siteStatus == null) {
			return true;
		}
		if (siteStatus.equalsIgnoreCase("blocked")) {
			return true;
		}
		return false;
	}
	public boolean isAvailableStandard() {
		if (siteStatus == null) {
			return false;
		}
		if (siteStatus.equalsIgnoreCase("available")) {
			return true;
		}
		return false;
	}
	public boolean isAvailableWithCountermeasuresStandard() {
		if (siteStatus == null) {
			return false;
		}
		if (siteStatus.equalsIgnoreCase("available with countermeasures")) {
			return true;
		}
		return false;
	}
	public boolean isAvailableHIPAA() {
		if (siteStatus == null) {
			return false;
		}
		if (siteStatus.equalsIgnoreCase("available")) {
			if (siteHipaaEligible != null) {
				boolean b = Boolean.parseBoolean(siteHipaaEligible);
				return b;
			}
			return false;
		}
		return false;
	}
	public boolean isAvailableWithCountermeasuresHIPAA() {
		if (siteStatus == null) {
			return false;
		}
		if (siteStatus.equalsIgnoreCase("available with countermeasures")) {
			if (siteHipaaEligible != null) {
				boolean b = Boolean.parseBoolean(siteHipaaEligible);
				return b;
			}
			return false;
		}
		return false;
	}
}
