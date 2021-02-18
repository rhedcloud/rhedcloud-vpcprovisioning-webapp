package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class TransitGatewayConnectionProfileSummaryPojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayConnectionProfileSummaryPojo> {
	TransitGatewayConnectionProfilePojo profile;
	TransitGatewayConnectionProfileAssignmentPojo assignment;
	boolean fromProfilePage=false;
	
	public static final ProvidesKey<TransitGatewayConnectionProfileSummaryPojo> KEY_PROVIDER = new ProvidesKey<TransitGatewayConnectionProfileSummaryPojo>() {
		@Override
		public Object getKey(TransitGatewayConnectionProfileSummaryPojo item) {
			if (item.getProfile() != null) {
				return item.getProfile().getTransitGatewayConnectionProfileId();
			}
			else {
				return item.getAssignment().getTransitGatewayConnectionProfileAssignmentId();
			}
		}
	};

	public TransitGatewayConnectionProfileSummaryPojo() {
		
	}

	public TransitGatewayConnectionProfilePojo getProfile() {
		return profile;
	}

	public void setProfile(TransitGatewayConnectionProfilePojo profile) {
		this.profile = profile;
	}

	public TransitGatewayConnectionProfileAssignmentPojo getAssignment() {
		return assignment;
	}

	public void setAssignment(TransitGatewayConnectionProfileAssignmentPojo assignment) {
		this.assignment = assignment;
	}

	@Override
	public int compareTo(TransitGatewayConnectionProfileSummaryPojo o) {
		int id1 = Integer.parseInt(this.getProfile().getTransitGatewayConnectionProfileId());
		int id2 = Integer.parseInt(o.getProfile().getTransitGatewayConnectionProfileId());
		if (id1 == id2) {
			return 0;
		}
		if (id1 < id2) {
			return -1;
		}
		return 1;
	}

	public boolean isFromProfilePage() {
		return fromProfilePage;
	}

	public void setFromProfilePage(boolean fromProfilePage) {
		this.fromProfilePage = fromProfilePage;
	}

}
