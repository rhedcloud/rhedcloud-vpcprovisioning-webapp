package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AWSServicePojo implements IsSerializable {
	List<AWSServiceCategoryPojo> categories = new java.util.ArrayList<AWSServiceCategoryPojo>();
	String name;
	String code;
	String status;
	

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

}
