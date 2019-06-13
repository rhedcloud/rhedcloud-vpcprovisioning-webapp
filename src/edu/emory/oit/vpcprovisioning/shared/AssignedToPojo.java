package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AssignedToPojo extends SharedObject implements IsSerializable {
	String publicId;
	PersonalNamePojo personalName;
	
	public AssignedToPojo() {
		
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
