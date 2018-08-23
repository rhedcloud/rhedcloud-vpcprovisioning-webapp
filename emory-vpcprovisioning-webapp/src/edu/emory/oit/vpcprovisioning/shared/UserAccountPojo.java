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
	boolean superUser=false;
	boolean hasValidTermsOfUseAgreement;
	String authUserIdForHALS;

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
	
	public boolean isNetworkAdmin() {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getRoleName() != null && 
				(arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_EMORY_NETWORK_ADMIN))) {
				
				return true;
			}
		}
		return false;
	}
	public boolean isAuditorForAccount(String accountId) {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getAccountId() != null && 
					arp.getAccountId().equalsIgnoreCase(accountId)) {
				if (arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_RHEDCLOUD_AUDITOR)) {
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
				if (arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_RHEDCLOUD_AWS_ADMIN)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isEmoryAwsAdmin() {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getRoleName() != null && 
					arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_RHEDCLOUD_AWS_ADMIN)) {
				return true;
			}
		}
		return false;
	}
	public boolean isCentralAdminForAccount(String accountId) {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getAccountId() != null && 
					arp.getAccountId().equalsIgnoreCase(accountId)) {
				if (arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_RHEDCLOUD_AWS_CENTRAL_ADMIN)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isCentralAdmin() {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getRoleName() != null && 
				(arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_RHEDCLOUD_AWS_CENTRAL_ADMIN) ||
				 arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS))) {
				
				return true;
			}
		}
		return isSuperUser();
	}
	public boolean isAuditor() {
		for (AccountRolePojo arp : accountRoles) {
			if (arp.getRoleName() != null && 
					arp.getRoleName().equalsIgnoreCase(Constants.ROLE_NAME_RHEDCLOUD_AUDITOR)) {
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

	public boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}

	@Override
	public String toString() {
		return "EPPN: " + eppn + 
				", Principal: " + principal + 
				", Permissions: " + permissions +
				", PublicID: " + publicId +
				", AccountRoles: " + accountRoles + 
				", Personal Name: " + personalName +
				", isSuperUser: " + superUser;
	}

	public void addAccountRole(AccountRolePojo arp) {
		boolean roleExists = false;
		arpLoop: for (AccountRolePojo l_arp : accountRoles) {
			if (l_arp.getAccountId() != null) {
				if (l_arp.getAccountId().equalsIgnoreCase(arp.getAccountId()) && l_arp.getRoleName().equalsIgnoreCase(arp.getRoleName())) {
					roleExists = true;
					break arpLoop;
				}
			}
			else if (l_arp.getRoleName().equalsIgnoreCase(arp.getRoleName())) {
				roleExists = true;
				break arpLoop;
			}

		}
		if (!roleExists) {
			accountRoles.add(arp);
		}
	}

	public boolean hasValidTermsOfUseAgreement() {
		return hasValidTermsOfUseAgreement;
	}

	public void setHasValidTermsOfUseAgreement(boolean hasValidTermsOfUseAgreement) {
		this.hasValidTermsOfUseAgreement = hasValidTermsOfUseAgreement;
	}

	public String getAuthUserIdForHALS() {
		return authUserIdForHALS;
	}

	public void setAuthUserIdForHALS(String authUserIdForHALS) {
		this.authUserIdForHALS = authUserIdForHALS;
	}
}
