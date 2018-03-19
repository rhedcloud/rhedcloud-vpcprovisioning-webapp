package edu.emory.oit.vpcprovisioning.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class PhonePojo extends SharedObject implements IsSerializable {
	/*
<!ELEMENT Phone (CountryCode?, AreaCode?, Number, ExtensionNumber?, PhoneCarrier?, Tag*)>
<!ATTLIST Phone
	type CDATA #IMPLIED
>
	 */
	String countryCode;
	String areaCode;
	String number;
	String extensionNumber;
	String phoneCarrier;
	List<String> tags = new ArrayList<String>();
	String type;

	public PhonePojo() {
		// TODO Auto-generated constructor stub
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getPhoneCarrier() {
		return phoneCarrier;
	}

	public void setPhoneCarrier(String phoneCarrier) {
		this.phoneCarrier = phoneCarrier;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
