package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class AWSServicePojo extends SharedObject implements IsSerializable {
	/*
	<!ELEMENT Service (
		ServiceId, 
		ServiceCode, 
		ServiceName, 
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
	String code;
	String name;
	String status;
	String landingPageURL;
	String description;
	List<String> categories = new java.util.ArrayList<String>();
	List<String> consoleCategories = new java.util.ArrayList<String>();

	boolean awsHipaaEligible;
	boolean emoryHipaaEligible;
	List<AWSTagPojo> tags = new java.util.ArrayList<AWSTagPojo>(); 
	AWSServicePojo baseline;

	public static final ProvidesKey<AWSServicePojo> KEY_PROVIDER = new ProvidesKey<AWSServicePojo>() {
		@Override
		public Object getKey(AWSServicePojo item) {
			return item == null ? null : item.getName();
		}
	};

	public AWSServicePojo() {
	}


	public List<String> getCategories() {
		return categories;
	}


	public void setCategories(List<String> categories) {
		this.categories = categories;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
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

	public boolean isAWSHipaaEligible() {
		return awsHipaaEligible;
	}


	public void setAWSHipaaEligible(boolean hipaaEligible) {
		this.awsHipaaEligible = hipaaEligible;
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

}
