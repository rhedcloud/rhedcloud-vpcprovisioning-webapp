package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SupportGroupPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT SupportGroup (Name?)>
	 */
	String name;
	
	public SupportGroupPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
