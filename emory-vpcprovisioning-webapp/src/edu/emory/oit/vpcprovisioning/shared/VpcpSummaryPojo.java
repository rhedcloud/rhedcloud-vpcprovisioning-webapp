package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class VpcpSummaryPojo extends SharedObject implements IsSerializable, Comparable<VpcpSummaryPojo> {
	VpcpPojo provisioning;
	VpcDeprovisioningPojo deprovisioning;
	
	public static final ProvidesKey<VpcpSummaryPojo> KEY_PROVIDER = new ProvidesKey<VpcpSummaryPojo>() {
		@Override
		public Object getKey(VpcpSummaryPojo item) {
			if (item.isProvision()) {
				return item.getProvisioning().getProvisioningId();
			}
			else {
				return item.getDeprovisioning().getProvisioningId();
			}
		}
	};

	public VpcpSummaryPojo() {
		
	}

	@Override
	public int compareTo(VpcpSummaryPojo o) {
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

	public VpcpPojo getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(VpcpPojo provisioning) {
		this.provisioning = provisioning;
	}

	public VpcDeprovisioningPojo getDeprovisioning() {
		return deprovisioning;
	}

	public void setDeprovisioning(VpcDeprovisioningPojo deprovisioning) {
		this.deprovisioning = deprovisioning;
	}

	public boolean isProvision() {
		if (this.provisioning != null) {
			return true;
		}
		return false;
	}
}
