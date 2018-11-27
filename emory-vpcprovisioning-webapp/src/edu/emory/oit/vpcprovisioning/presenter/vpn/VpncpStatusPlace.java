package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;

public class VpncpStatusPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("vpncpStatus")
	public static class Tokenizer implements PlaceTokenizer<VpncpStatusPlace> {

		private static final String NO_ID = "vpncpStatus";

		public VpncpStatusPlace getPlace(String token) {
			if (token != null) {
				return new VpncpStatusPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return VpncpStatusPlace.getVpncpStatusPlace();
			}
		}

		public String getToken(VpncpStatusPlace place) {
			String provisioningId = place.getProvisioningId();
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static VpncpStatusPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static VpncpStatusPlace createVpncpStatusPlace(VpnConnectionProvisioningPojo vpncp) {
		return new VpncpStatusPlace(vpncp.getProvisioningId(), vpncp);
	}

	public static VpncpStatusPlace createVpncpStatusPlaceFromGenerate(VpnConnectionProvisioningPojo vpncp) {
		return new VpncpStatusPlace(vpncp.getProvisioningId(), vpncp, true);
	}
	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static VpncpStatusPlace getVpncpStatusPlace() {
		if (singleton == null) {
			singleton = new VpncpStatusPlace(null, null);
		}
		return singleton;
	}

	private final VpnConnectionProvisioningPojo vpncp;
	private final String provisioningId;
	private final boolean fromGenerate;
	public String getProvisioningId() {
		return provisioningId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private VpncpStatusPlace(String provisioningId, VpnConnectionProvisioningPojo vpncp) {
		this.provisioningId = provisioningId;
		this.vpncp = vpncp;
		this.fromGenerate = false;
	}

	private VpncpStatusPlace(String provisioningId, VpnConnectionProvisioningPojo vpncp, boolean fromGenerate) {
		this.provisioningId = provisioningId;
		this.vpncp = vpncp;
		this.fromGenerate = fromGenerate;
	}
	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public VpnConnectionProvisioningPojo getVpncp() {
		return vpncp;
	}
	public boolean isFromGenerate() {
		return this.fromGenerate;
	}
}
