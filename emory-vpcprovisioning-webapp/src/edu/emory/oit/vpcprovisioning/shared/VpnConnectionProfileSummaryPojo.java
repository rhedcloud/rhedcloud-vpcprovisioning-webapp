package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class VpnConnectionProfileSummaryPojo extends SharedObject implements IsSerializable, Comparable<VpnConnectionProfileSummaryPojo> {
	VpnConnectionProfilePojo profile;
	VpnConnectionProfileAssignmentPojo assignment;
	
	public static final ProvidesKey<VpnConnectionProfileSummaryPojo> KEY_PROVIDER = new ProvidesKey<VpnConnectionProfileSummaryPojo>() {
		@Override
		public Object getKey(VpnConnectionProfileSummaryPojo item) {
			if (item.getProfile() != null) {
				return item.getProfile().getVpnConnectionProfileId();
			}
			else {
				return item.getAssignment().getVpnConnectionProfileAssignmentId();
			}
		}
	};

	public VpnConnectionProfileSummaryPojo() {
		// TODO Auto-generated constructor stub
	}

	public VpnConnectionProfilePojo getProfile() {
		return profile;
	}

	public void setProfile(VpnConnectionProfilePojo profile) {
		this.profile = profile;
	}

	public VpnConnectionProfileAssignmentPojo getAssignment() {
		return assignment;
	}

	public void setAssignment(VpnConnectionProfileAssignmentPojo assignment) {
		this.assignment = assignment;
	}

	@Override
	public int compareTo(VpnConnectionProfileSummaryPojo o) {
		// TODO: change this appropriately
//		if (this.isProvision() && o.isProvision()) {
//			Date c1 = o.getProvisioning().getCreateTime();
//			Date c2 = this.getProvisioning().getCreateTime();
//			if (c1 == null || c2 == null) {
//				return 0;
//			}
//			return c1.compareTo(c2);
//		}
//		else if (!this.isProvision() && !o.isProvision()) {
//			Date c1 = o.getDeprovisioning().getCreateTime();
//			Date c2 = this.getDeprovisioning().getCreateTime();
//			if (c1 == null || c2 == null) {
//				return 0;
//			}
//			return c1.compareTo(c2);
//		}
//		else if (this.isProvision() && !o.isProvision()) {
//			Date c1 = o.getDeprovisioning().getCreateTime();
//			Date c2 = this.getProvisioning().getCreateTime();
//			if (c1 == null || c2 == null) {
//				return 0;
//			}
//			return c1.compareTo(c2);
//		}
//		else if (!this.isProvision() && o.isProvision()) {
//			Date c1 = o.getProvisioning().getCreateTime();
//			Date c2 = this.getDeprovisioning().getCreateTime();
//			if (c1 == null || c2 == null) {
//				return 0;
//			}
//			return c1.compareTo(c2);
//		}
		return 0;
	}

}
