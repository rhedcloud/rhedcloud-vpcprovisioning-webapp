package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class VpnConnectionProvisioningSummaryPojo extends SharedObject implements IsSerializable, Comparable<VpnConnectionProvisioningSummaryPojo> {
	VpnConnectionProvisioningPojo provisioning;
	VpnConnectionDeprovisioningPojo deprovisioning;
	
	public static final ProvidesKey<VpnConnectionProvisioningSummaryPojo> KEY_PROVIDER = new ProvidesKey<VpnConnectionProvisioningSummaryPojo>() {
		@Override
		public Object getKey(VpnConnectionProvisioningSummaryPojo item) {
			if (item.isProvision()) {
				return item.getProvisioning().getProvisioningId();
			}
			else {
				return item.getDeprovisioning().getProvisioningId();
			}
		}
	};

	public VpnConnectionProvisioningSummaryPojo() {
		
	}

	@Override
	public int compareTo(VpnConnectionProvisioningSummaryPojo o) {
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

	public VpnConnectionProvisioningPojo getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(VpnConnectionProvisioningPojo provisioning) {
		this.provisioning = provisioning;
	}

	public VpnConnectionDeprovisioningPojo getDeprovisioning() {
		return deprovisioning;
	}

	public void setDeprovisioning(VpnConnectionDeprovisioningPojo deprovisioning) {
		this.deprovisioning = deprovisioning;
	}

	public boolean isProvision() {
		if (this.provisioning != null) {
			return true;
		}
		return false;
	}
}
