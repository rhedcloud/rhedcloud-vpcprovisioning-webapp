package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class RoleProvisioningSummaryPojo extends SharedObject implements IsSerializable, Comparable<RoleProvisioningSummaryPojo> {
	RoleProvisioningPojo provisioning;
	RoleDeprovisioningPojo deprovisioning;
	RolePojo role;
	AccountPojo account;
	DirectoryPersonPojo assignee;
	
	public static final ProvidesKey<RoleProvisioningSummaryPojo> KEY_PROVIDER = new ProvidesKey<RoleProvisioningSummaryPojo>() {
		@Override
		public Object getKey(RoleProvisioningSummaryPojo item) {
			if (item.isProvision()) {
				return item.getProvisioning().getProvisioningId();
			}
			else {
				return item.getDeprovisioning().getDeprovisioningId();
			}
		}
	};

	public RoleProvisioningSummaryPojo() {
		
	}

	@Override
	public int compareTo(RoleProvisioningSummaryPojo o) {
		if (this.isProvision() && o.isProvision()) {
			Date c1 = o.getProvisioning().getCreateTime();
			Date c2 = this.getProvisioning().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		else if (!this.isProvision() && !o.isProvision()) {
			Date c1 = o.getDeprovisioning().getCreateTime();
			Date c2 = this.getDeprovisioning().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		else if (this.isProvision() && !o.isProvision()) {
			Date c1 = o.getDeprovisioning().getCreateTime();
			Date c2 = this.getProvisioning().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		else if (!this.isProvision() && o.isProvision()) {
			Date c1 = o.getProvisioning().getCreateTime();
			Date c2 = this.getDeprovisioning().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		return 0;
	}

	public RoleProvisioningPojo getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(RoleProvisioningPojo provisioning) {
		this.provisioning = provisioning;
	}

	public RoleDeprovisioningPojo getDeprovisioning() {
		return deprovisioning;
	}

	public void setDeprovisioning(RoleDeprovisioningPojo deprovisioning) {
		this.deprovisioning = deprovisioning;
	}

	public boolean isProvision() {
		if (this.provisioning != null) {
			return true;
		}
		return false;
	}

	public RolePojo getRole() {
		return role;
	}

	public void setRole(RolePojo role) {
		this.role = role;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

	public DirectoryPersonPojo getAssignee() {
		return assignee;
	}

	public void setAssignee(DirectoryPersonPojo assignee) {
		this.assignee = assignee;
	}
}
