package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DirectoryMetaDataPojo extends SharedObject implements IsSerializable {
	String netId;
	String firstName;
	String lastName;
	String emailAddress;
	String ppid;

	public DirectoryMetaDataPojo() {
	}

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPublicId() {
		return ppid;
	}

	public void setPublicId(String ppid) {
		this.ppid = ppid;
	}

}
