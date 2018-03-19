package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RolePojo extends SharedObject implements IsSerializable, Comparable<RolePojo> {
	
	String roleDN;
	String roleName;
	String roleDescription;
	List<ResourcePojo> resources = new java.util.ArrayList<ResourcePojo>();

	public RolePojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(RolePojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getRoleDN() {
		return roleDN;
	}

	public void setRoleDN(String roleDN) {
		this.roleDN = roleDN;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public List<ResourcePojo> getResources() {
		return resources;
	}

	public void setResources(List<ResourcePojo> resources) {
		this.resources = resources;
	}

}
