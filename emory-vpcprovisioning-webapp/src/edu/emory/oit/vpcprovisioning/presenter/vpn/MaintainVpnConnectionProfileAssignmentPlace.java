package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;

public class MaintainVpnConnectionProfileAssignmentPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainVpnConnectionProfileAssignment")
	public static class Tokenizer implements PlaceTokenizer<MaintainVpnConnectionProfileAssignmentPlace> {

		private static final String NO_ID = "createVpnConnectionProfileAssignment";

		public MaintainVpnConnectionProfileAssignmentPlace getPlace(String token) {
			if (token != null) {
				return new MaintainVpnConnectionProfileAssignmentPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainVpnConnectionProfileAssignmentPlace.getMaintainVpnConnectionProfileAssignmentPlace();
			}
		}

		public String getToken(MaintainVpnConnectionProfileAssignmentPlace place) {
			String id = place.getVpnConnectionProfileAssignmentId();
			return (id == null) ? NO_ID : id;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainVpnConnectionProfileAssignmentPlace singleton;

	/**
	 * Create an instance of {@link MaintainVpnConnectionProfileAssignmentPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainVpnConnectionProfileAssignmentPlace createMaintainVpnConnectionProfileAssignmentPlace(VpnConnectionProfileSummaryPojo pojo) {
		return new MaintainVpnConnectionProfileAssignmentPlace(pojo.getAssignment().getVpnConnectionProfileAssignmentId(), pojo);
	}

	/**
	 * Get the singleton instance of the {@link MaintainVpnConnectionProfileAssignmentPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainVpnConnectionProfileAssignmentPlace getMaintainVpnConnectionProfileAssignmentPlace() {
		if (singleton == null) {
			singleton = new MaintainVpnConnectionProfileAssignmentPlace(null, null);
		}
		return singleton;
	}

	private final VpnConnectionProfileSummaryPojo vpnConnectionProfileSummary;
	private final String vpnConnectionProfileAssignmentId;
	public String getVpnConnectionProfileAssignmentId() {
		return vpnConnectionProfileAssignmentId;
	}

	/**
	 * Construct a new {@link MaintainVpnConnectionProfileAssignmentPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainVpnConnectionProfileAssignmentPlace(String vpnConnectionProfileAssignmentId, VpnConnectionProfileSummaryPojo pojo) {
		this.vpnConnectionProfileAssignmentId = vpnConnectionProfileAssignmentId;
		this.vpnConnectionProfileSummary = pojo;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public VpnConnectionProfileSummaryPojo getVpnConnectionProfileSummary() {
		return vpnConnectionProfileSummary;
	}
}
