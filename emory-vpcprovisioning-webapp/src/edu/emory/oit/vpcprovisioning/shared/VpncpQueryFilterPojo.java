package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class VpncpQueryFilterPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT VpnConnectionProvisioningQuerySpecification (Comparison*, QueryLanguage?, ProvisioningId?, Type?, CreateUser?, LastUpdateUser?)>
	 */
	String provisioningId;
	String type;
	boolean defaultMaxVpncps;
	boolean allVpncps;
	int maxRows;
	String searchString;

	public VpncpQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public boolean isDefaultMaxVpncps() {
		return defaultMaxVpncps;
	}

	public void setDefaultMaxVpncps(boolean defaultMaxVpncps) {
		this.defaultMaxVpncps = defaultMaxVpncps;
	}

	public boolean isAllVpncps() {
		return allVpncps;
	}

	public void setAllVpncps(boolean allVpncps) {
		this.allVpncps = allVpncps;
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
