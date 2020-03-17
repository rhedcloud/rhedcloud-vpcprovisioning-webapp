package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ManagedTagPojo extends SharedObject implements IsSerializable {
	/*
<!ELEMENT ManagedTag (ManagedTagId?,TagName, TagValue?, ServiceFilter?, AccountMetadataFilter?)>
<!ATTLIST ManagedTag description CDATA #IMPLIED>
	 */

	String managedTagId;
	String tagName;
	String tagValue;
	String description;
	ServiceFilterPojo serviceFilter;
	AccountMetadataFilterPojo accountMetadataFilter;
	
	public String getManagedTagId() {
		return managedTagId;
	}
	public void setManagedTagId(String managedTagId) {
		this.managedTagId = managedTagId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ServiceFilterPojo getServiceFilter() {
		return serviceFilter;
	}
	public void setServiceFilter(ServiceFilterPojo serviceFilter) {
		this.serviceFilter = serviceFilter;
	}
	public AccountMetadataFilterPojo getAccountMetadataFilter() {
		return accountMetadataFilter;
	}
	public void setAccountMetadataFilter(AccountMetadataFilterPojo accountMetadataFilter) {
		this.accountMetadataFilter = accountMetadataFilter;
	}
}
