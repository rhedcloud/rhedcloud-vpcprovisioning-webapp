package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AWSServicePojo extends SharedObject implements IsSerializable {
	List<AWSServiceCategoryPojo> categories = new java.util.ArrayList<AWSServiceCategoryPojo>();
	String name;
	String code;
	String status;
	
	/*
			<ServiceId>EC2</ServiceId>
			<ServiceCode>EC2</ServiceCode>
			<ServiceName>Elastic Compute Cloud</ServiceName>
			<Status>Fully Available</Status>
			<ServiceLandingPageUrl>https://aws.amazon.com/ec2/</ServiceLandingPageUrl>
			<Description>Amazon Elastic Compute Cloud (Amazon EC2) is a web service that provides secure, resizable compute capacity in the cloud.</Description>
			<Category>Compute</Category>
			<ConsoleCategory>Compute</ConsoleCategory>
			<HippaEligible>true</HippaEligible>
			<Tag>
				<Key>VerificationDate</Key>
				<Value>12/13/2016</Value>
			</Tag>
			<Tag>
				<Key>Verifier</Key>
				<Value>Steve Wheat</Value>
			</Tag>
	 */

	String serviceId;
	String landingPage;
	String description;
	boolean hipaaEligible;
	List<AWSTagPojo> tags = new java.util.ArrayList<AWSTagPojo>(); 
	List<String> consoleCategories = new java.util.ArrayList<String>();

	public AWSServicePojo() {
		// TODO Auto-generated constructor stub
	}


	public List<AWSServiceCategoryPojo> getCategories() {
		return categories;
	}


	public void setCategories(List<AWSServiceCategoryPojo> categories) {
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


	public String getLandingPage() {
		return landingPage;
	}


	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isHipaaEligible() {
		return hipaaEligible;
	}


	public void setHipaaEligible(boolean hipaaEligible) {
		this.hipaaEligible = hipaaEligible;
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

}
