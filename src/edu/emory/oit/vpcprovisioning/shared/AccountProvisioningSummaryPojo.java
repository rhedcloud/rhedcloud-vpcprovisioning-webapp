package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class AccountProvisioningSummaryPojo extends SharedObject implements IsSerializable, Comparable<AccountProvisioningSummaryPojo> {
	AccountProvisioningPojo provisioning;
	AccountDeprovisioningPojo deprovisioning;
	AccountPojo account;
	
	public static final ProvidesKey<AccountProvisioningSummaryPojo> KEY_PROVIDER = new ProvidesKey<AccountProvisioningSummaryPojo>() {
		@Override
		public Object getKey(AccountProvisioningSummaryPojo item) {
			if (item.isProvision()) {
				return item.getProvisioning().getProvisioningId();
			}
			else {
				return item.getDeprovisioning().getDeprovisioningId();
			}
		}
	};

	public AccountProvisioningSummaryPojo() {
		
	}

	@Override
	public int compareTo(AccountProvisioningSummaryPojo o) {
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

	public AccountProvisioningPojo getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(AccountProvisioningPojo provisioning) {
		this.provisioning = provisioning;
	}

	public AccountDeprovisioningPojo getDeprovisioning() {
		return deprovisioning;
	}

	public void setDeprovisioning(AccountDeprovisioningPojo deprovisioning) {
		this.deprovisioning = deprovisioning;
	}

	public boolean isProvision() {
		if (this.provisioning != null) {
			return true;
		}
		return false;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}
}
