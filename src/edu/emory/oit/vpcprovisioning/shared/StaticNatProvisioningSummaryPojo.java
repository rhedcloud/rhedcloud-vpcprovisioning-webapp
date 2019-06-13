package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class StaticNatProvisioningSummaryPojo extends SharedObject implements IsSerializable, Comparable<StaticNatProvisioningSummaryPojo> {
	StaticNatProvisioningPojo provisioned;
	StaticNatDeprovisioningPojo deProvisioned;

	public StaticNatProvisioningSummaryPojo() {
		
	}

	public static final ProvidesKey<StaticNatProvisioningSummaryPojo> KEY_PROVIDER = new ProvidesKey<StaticNatProvisioningSummaryPojo>() {
		@Override
		public Object getKey(StaticNatProvisioningSummaryPojo item) {
			if (item != null) {
				if (item.getProvisioned() != null) {
					return item.getProvisioned().getProvisioningId();
				}
				else if (item.getDeprovisioned() != null) {
					return item.getDeprovisioned().getProvisioningId();
				}
				else {
					return null;
				}
			}
			else {
				return null;
			}
		}
	};

	public StaticNatProvisioningPojo getProvisioned() {
		return provisioned;
	}

	public void setProvisioned(StaticNatProvisioningPojo provisionined) {
		this.provisioned = provisionined;
	}

	public StaticNatDeprovisioningPojo getDeprovisioned() {
		return deProvisioned;
	}

	public void setDeprovisioned(StaticNatDeprovisioningPojo deProvisioned) {
		this.deProvisioned = deProvisioned;
	}

	@Override
	public int compareTo(StaticNatProvisioningSummaryPojo o) {
		if (this.provisioned != null && o.getProvisioned() != null) {
			Date c1 = o.getProvisioned().getCreateTime();
			Date c2 = this.getProvisioned().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		else if (this.deProvisioned != null && o.getDeprovisioned() != null) {
			Date c1 = o.getDeprovisioned().getCreateTime();
			Date c2 = this.getDeprovisioned().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		else if (this.provisioned != null && o.getDeprovisioned() != null) {
			Date c1 = o.getDeprovisioned().getCreateTime();
			Date c2 = this.getProvisioned().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		else if (this.deProvisioned != null && o.getProvisioned() != null) {
			Date c1 = o.getProvisioned().getCreateTime();
			Date c2 = this.getDeprovisioned().getCreateTime();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		return 0;
	}

}
