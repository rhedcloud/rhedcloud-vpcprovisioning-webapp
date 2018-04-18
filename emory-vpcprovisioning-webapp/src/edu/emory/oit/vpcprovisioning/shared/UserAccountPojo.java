package edu.emory.oit.vpcprovisioning.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserAccountPojo implements IsSerializable {
	String eppn;
	String principal;
	private List<String> permissions = new ArrayList<String>();
	String publicId;
	List<AccountRolePojo> accountRoles = new ArrayList<AccountRolePojo>();
	PersonalNamePojo personalName = new PersonalNamePojo();

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

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public List<AccountRolePojo> getAccountRoles() {
		return accountRoles;
	}

	public void setAccountRoles(List<AccountRolePojo> accountRoles) {
		this.accountRoles = accountRoles;
	}
	
	public boolean isAuditorForAccount(String accountId) {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getAccountId() != null && 
					arp.getAccountId().equalsIgnoreCase(accountId)) {
				if (arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_EMORY_AUDITOR)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isAdminForAccount(String accountId) {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getAccountId() != null && 
					arp.getAccountId().equalsIgnoreCase(accountId)) {
				if (arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_EMORY_AWS_ADMIN)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isLitsAdmin() {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getRoleName() != null && 
					arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMIN)) {
				return true;
			}
		}
		return false;
	}
	public boolean isAuditor() {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getRoleName() != null && 
					arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_EMORY_AUDITOR)) {
				return true;
			}
		}
		return false;
	}

	public PersonalNamePojo getPersonalName() {
		return personalName;
	}

	public void setPersonalName(PersonalNamePojo personalName) {
		this.personalName = personalName;
	}
}
