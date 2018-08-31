package edu.emory.oit.vpcprovisioning.shared;

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
		// TODO Auto-generated method stub
		return 0;
	}

}
