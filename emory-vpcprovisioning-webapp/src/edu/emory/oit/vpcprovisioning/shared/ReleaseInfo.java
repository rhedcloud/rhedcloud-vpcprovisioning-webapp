package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReleaseInfo implements IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String version = "1.0";
	String build = "20";
	String productName = "VPC Provisioning App";
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ReleaseInfo() {
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String toString() {
		return 
				"Version: " + version + "  Build: " + build;
	}
}
