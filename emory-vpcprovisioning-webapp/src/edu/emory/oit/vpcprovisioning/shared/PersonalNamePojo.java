package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class PersonalNamePojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT PersonalName (LastName, FirstName, MiddleName?, NameSuffix?, Prefix?, Suffix?, CompositeName?)>
		<!ATTLIST PersonalName
			type CDATA #REQUIRED
			order (east | west) #IMPLIED
			noFirstName (true | false) #IMPLIED
		>
	 */
	String lastName;
	String firstName;
	String middleName;
	String nameSuffix;
	String prefix;
	String suffix;
	String compositeName;

	public PersonalNamePojo() {
		// TODO Auto-generated constructor stub
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNameSuffix() {
		return nameSuffix;
	}

	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getCompositeName() {
		return compositeName;
	}

	public void setCompositeName(String compositeName) {
		this.compositeName = compositeName;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

}
