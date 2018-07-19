package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class AWSServicePojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT Service (
			ServiceId?, 
			AwsServiceCode, 
			AwsServiceName, 
			AlternateServiceName?, 
			CombinedServiceName?, 
			Status, 
			ServiceLandingPageUrl, 
			Description?, 
			Category*, 
			ConsoleCategory*, 
			AwsHipaaEligible, 
			EmoryHipaaEligible, 
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
	String status;
	String landingPageURL;
	String description;
	List<String> awsCategories = new java.util.ArrayList<String>();
	List<String> consoleCategories = new java.util.ArrayList<String>();
	boolean awsHipaaEligible;
	boolean emoryHipaaEligible;
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


	public List<String> getCategories() {
		return awsCategories;
	}


	public void setCategories(List<String> categories) {
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


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getServiceId() {
		return serviceId;
	}


	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	public String getLandingPageURL() {
		return landingPageURL;
	}


	public void setLandingPageURL(String landingPage) {
		this.landingPageURL = landingPage;
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


	public boolean isEmoryHipaaEligible() {
		return emoryHipaaEligible;
	}


	public void setEmoryHipaaEligible(boolean emoryHipaaEligible) {
		this.emoryHipaaEligible = emoryHipaaEligible;
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
		return awsHipaaEligible;
	}


	public void setAwsHipaaEligible(boolean awsHipaaEligible) {
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

}
