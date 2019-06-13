package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class IdentityTypeDnMapPojo extends SharedObject implements IsSerializable {
	//DistinguishedName*, IdentityType
	List<String> distinguishedNames = new java.util.ArrayList<String>();
	String identityType;

	public IdentityTypeDnMapPojo() {
		
	}

	public List<String> getDistinguishedNames() {
		return distinguishedNames;
	}

	public void setDistinguishedNames(List<String> distinguishedNames) {
		this.distinguishedNames = distinguishedNames;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

}
