package edu.emory.oit.vpcprovisioning.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserAccountPojo implements IsSerializable {
	String eppn;
	String principal;
	private List<String> permissions = new ArrayList<String>();

	public UserAccountPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getEppn() {
		return eppn;
	}

	public void setEppn(String eppn) {
		this.eppn = eppn;
		this.principal = eppn.substring(0, eppn.indexOf("@"));
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public boolean hasPermission(String permission) {
		if (permissions.contains(permission)) {
			return true;
		}
		return false;
	}

	public String getPrincipal() {
		if (principal != null) {
			return principal;
		}
		this.principal = eppn.substring(0, eppn.indexOf("@"));
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
}
