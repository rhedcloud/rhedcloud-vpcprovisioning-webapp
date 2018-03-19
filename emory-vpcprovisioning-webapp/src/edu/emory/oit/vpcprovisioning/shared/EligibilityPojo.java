package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class EligibilityPojo extends SharedObject implements IsSerializable {
	/*
	<!ELEMENT Eligibility (Value)>
	<!ATTLIST Eligibility
		type CDATA #REQUIRED
	>
	 */
	String value;
	String type;

	public EligibilityPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
