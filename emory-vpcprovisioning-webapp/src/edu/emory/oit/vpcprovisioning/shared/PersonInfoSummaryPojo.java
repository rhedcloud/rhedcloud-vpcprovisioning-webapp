package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class PersonInfoSummaryPojo extends SharedObject implements IsSerializable {
	FullPersonPojo fullPerson;
	AccountProvisioningAuthorizationPojo accountProvisioningAuthorization;
	
	public PersonInfoSummaryPojo() {
		// TODO Auto-generated constructor stub
	}

	public FullPersonPojo getFullPerson() {
		return fullPerson;
	}

	public void setFullPerson(FullPersonPojo fullPerson) {
		this.fullPerson = fullPerson;
	}

	public AccountProvisioningAuthorizationPojo getAccountProvisioningAuthorization() {
		return accountProvisioningAuthorization;
	}

	public void setAccountProvisioningAuthorization(AccountProvisioningAuthorizationPojo accountProvisioningAuthorization) {
		this.accountProvisioningAuthorization = accountProvisioningAuthorization;
	}

}
