package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountMetadataFilterPojo extends SharedObject implements IsSerializable {
	String propertyName;
	List<String> propertyValues = new java.util.ArrayList<String>();
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public List<String> getPropertyValues() {
		return propertyValues;
	}
	public void setPropertyValues(List<String> propertyValues) {
		this.propertyValues = propertyValues;
	}

}
