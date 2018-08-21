package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ConfigurationItemPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT ConfigurationItem (Name?, SupportGroup?, AmcomEventId?, ServiceOwner?, ServiceOwnerEventId?)>
	 */
	String name;
	SupportGroupPojo supportGroup;
	String amcomEventId;
	ServiceOwnerPojo serviceOwner;
	String serviceOwnerEventId;
	
	public ConfigurationItemPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SupportGroupPojo getSupportGroup() {
		return supportGroup;
	}

	public void setSupportGroup(SupportGroupPojo supportGroup) {
		this.supportGroup = supportGroup;
	}

	public String getAmcomEventId() {
		return amcomEventId;
	}

	public void setAmcomEventId(String amcomEventId) {
		this.amcomEventId = amcomEventId;
	}

	public ServiceOwnerPojo getServiceOwner() {
		return serviceOwner;
	}

	public void setServiceOwner(ServiceOwnerPojo serviceOwner) {
		this.serviceOwner = serviceOwner;
	}

	public String getServiceOwnerEventId() {
		return serviceOwnerEventId;
	}

	public void setServiceOwnerEventId(String serviceOwnerEventId) {
		this.serviceOwnerEventId = serviceOwnerEventId;
	}

}
