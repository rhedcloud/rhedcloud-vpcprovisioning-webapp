package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class EntitlementPojo extends SharedObject implements IsSerializable, Comparable<EntitlementPojo> {
	String entitlementDN;
	String entitlementGuid;
	String entitlementApplication;

	public EntitlementPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(EntitlementPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getEntitlementDN() {
		return entitlementDN;
	}

	public void setEntitlementDN(String entitlementDN) {
		this.entitlementDN = entitlementDN;
	}

	public String getEntitlementGuid() {
		return entitlementGuid;
	}

	public void setEntitlementGuid(String entitlementGuid) {
		this.entitlementGuid = entitlementGuid;
	}

	public String getEntitlementApplication() {
		return entitlementApplication;
	}

	public void setEntitlementApplication(String entitlementApplication) {
		this.entitlementApplication = entitlementApplication;
	}

}
