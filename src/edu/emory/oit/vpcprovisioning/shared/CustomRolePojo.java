package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class CustomRolePojo extends SharedObject implements IsSerializable, Comparable<CustomRolePojo> {
	public static final ProvidesKey<CustomRolePojo> KEY_PROVIDER = new ProvidesKey<CustomRolePojo>() {
		@Override
		public Object getKey(CustomRolePojo item) {
			return item == null ? null : item.getCustomRoleId();
		}
	};

	String customRoleId;
	String accountId;
	String roleName;
	String idmRoleName;

	public CustomRolePojo() {
		
	}

	@Override
	public int compareTo(CustomRolePojo o) {
		
		return 0;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCustomRoleId() {
		return customRoleId;
	}

	public void setCustomRoleId(String customRoleId) {
		this.customRoleId = customRoleId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIdmRoleName() {
		return idmRoleName;
	}

	public void setIdmRoleName(String idmRoleName) {
		this.idmRoleName = idmRoleName;
	}

}
