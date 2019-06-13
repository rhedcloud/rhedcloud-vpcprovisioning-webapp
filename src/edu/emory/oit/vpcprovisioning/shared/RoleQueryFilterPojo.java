package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	String roleDN;

	public RoleQueryFilterPojo() {
		
	}

	public String getRoleDN() {
		return roleDN;
	}

	public void setRoleDN(String roleDN) {
		this.roleDN = roleDN;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
