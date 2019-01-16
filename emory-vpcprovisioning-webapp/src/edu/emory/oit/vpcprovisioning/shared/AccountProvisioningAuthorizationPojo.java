package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountProvisioningAuthorizationPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT AccountProvisioningAuthorization (UserId, IsAuthorized, AuthorizedUserDescription, UnauthorizedReason*)>
	 */
	String userId;		// ppid
	boolean authorized;
	String authorizedUserDescription;
	List<String> unauthorizedReasons = new java.util.ArrayList<String>();
	
	public AccountProvisioningAuthorizationPojo() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public String getAuthorizedUserDescription() {
		return authorizedUserDescription;
	}

	public void setAuthorizedUserDescription(String authorizedUserDescription) {
		this.authorizedUserDescription = authorizedUserDescription;
	}

	public List<String> getUnauthorizedReasons() {
		return unauthorizedReasons;
	}

	public void setUnauthorizedReasons(List<String> unauthorizedReasons) {
		this.unauthorizedReasons = unauthorizedReasons;
	}

}
