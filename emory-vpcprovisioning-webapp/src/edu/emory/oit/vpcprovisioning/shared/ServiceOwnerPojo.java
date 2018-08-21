package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ServiceOwnerPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT ServiceOwner (PublicId?, PersonalName?)>
	 */
	String publicId;
	PersonalNamePojo personalName;
	
	public ServiceOwnerPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public PersonalNamePojo getPersonalName() {
		return personalName;
	}

	public void setPersonalName(PersonalNamePojo personalName) {
		this.personalName = personalName;
	}

}
