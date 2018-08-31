package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;

public class MaintainVpnConnectionProfilePlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainVpnConnectionProfile")
	public static class Tokenizer implements PlaceTokenizer<MaintainVpnConnectionProfilePlace> {

		private static final String NO_ID = "createVpnConnectionProfile";

		public MaintainVpnConnectionProfilePlace getPlace(String token) {
			if (token != null) {
				return new MaintainVpnConnectionProfilePlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainVpnConnectionProfilePlace.getMaintainVpnConnectionProfilePlace();
			}
		}

		public String getToken(MaintainVpnConnectionProfilePlace place) {
			String id = place.getVpnConnectionProfileId();
			return (id == null) ? NO_ID : id;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainVpnConnectionProfilePlace singleton;

	/**
	 * Create an instance of {@link MaintainVpnConnectionProfilePlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainVpnConnectionProfilePlace createMaintainVpnConnectionProfilePlace(VpnConnectionProfilePojo pojo) {
		return new MaintainVpnConnectionProfilePlace(pojo.getVpnConnectionProfileId(), pojo);
	}

	/**
	 * Get the singleton instance of the {@link MaintainVpnConnectionProfilePlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainVpnConnectionProfilePlace getMaintainVpnConnectionProfilePlace() {
		if (singleton == null) {
			singleton = new MaintainVpnConnectionProfilePlace(null, null);
		}
		return singleton;
	}

	private final VpnConnectionProfilePojo vpnConnectionProfile;
	private final String vpnConnectionProfileId;
	public String getVpnConnectionProfileId() {
		return vpnConnectionProfileId;
	}

	/**
	 * Construct a new {@link MaintainVpnConnectionProfilePlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainVpnConnectionProfilePlace(String vpnConnectionProfileId, VpnConnectionProfilePojo pojo) {
		this.vpnConnectionProfileId = vpnConnectionProfileId;
		this.vpnConnectionProfile = pojo;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public VpnConnectionProfilePojo getVpnConnectionProfile() {
		return vpnConnectionProfile;
	}
}
