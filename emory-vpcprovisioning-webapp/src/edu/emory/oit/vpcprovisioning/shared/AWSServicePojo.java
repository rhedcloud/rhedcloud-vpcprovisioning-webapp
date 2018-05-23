package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class AWSServicePojo extends SharedObject implements IsSerializable {
	List<AWSServiceCategoryPojo> categories = new java.util.ArrayList<AWSServiceCategoryPojo>();
	String name;
	String code;
	String status;
	
	String serviceId;
	String landingPage;
	String description;
	boolean hipaaEligible;
	List<AWSTagPojo> tags = new java.util.ArrayList<AWSTagPojo>(); 
	List<String> consoleCategories = new java.util.ArrayList<String>();

	public static final ProvidesKey<AWSServicePojo> KEY_PROVIDER = new ProvidesKey<AWSServicePojo>() {
		@Override
		public Object getKey(AWSServicePojo item) {
			return item == null ? null : item.getName();
		}
	};

	public AWSServicePojo() {
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
