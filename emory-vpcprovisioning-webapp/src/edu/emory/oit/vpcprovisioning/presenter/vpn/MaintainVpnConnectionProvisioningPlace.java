package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;

public class MaintainVpnConnectionProvisioningPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainVpnConnectionProvisioning")
	public static class Tokenizer implements PlaceTokenizer<MaintainVpnConnectionProvisioningPlace> {

		private static final String NO_ID = "generateVpnConnectionProvisioning";

		public MaintainVpnConnectionProvisioningPlace getPlace(String token) {
			if (token != null) {
				return new MaintainVpnConnectionProvisioningPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainVpnConnectionProvisioningPlace.getMaintainVpnConnectionProvisioningPlace();
			}
		}

		public String getToken(MaintainVpnConnectionProvisioningPlace place) {
			String provisioningId = place.getProvisioningId();
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainVpnConnectionProvisioningPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainVpnConnectionProvisioningPlace createMaintainVpnConnectionProvisioningPlace(VpnConnectionProvisioningPojo vpncp) {
		return new MaintainVpnConnectionProvisioningPlace(vpncp.getProvisioningId(), vpncp);
	}

	public static MaintainVpnConnectionProvisioningPlace createMaintainVpnConnectionProvisioningPlace(VpnConnectionProfilePojo profile) {
		return new MaintainVpnConnectionProvisioningPlace(profile);
	}

	public static MaintainVpnConnectionProvisioningPlace createMaintainVpnConnectionProvisioningPlace(VpnConnectionProfilePojo profile, VpnConnectionProfileAssignmentPojo profileAssignment) {
		return new MaintainVpnConnectionProvisioningPlace(profile, profileAssignment);
	}

	public static Place createMaintainVpnConnectionProvisioningPlace(VpnConnectionRequisitionPojo vpncRequisition, VpnConnectionProfileAssignmentPojo assignment) {
		return new MaintainVpnConnectionProvisioningPlace(vpncRequisition, assignment);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainVpnConnectionProvisioningPlace getMaintainVpnConnectionProvisioningPlace() {
		if (singleton == null) {
			singleton = new MaintainVpnConnectionProvisioningPlace();
		}
		return singleton;
	}

	private final VpnConnectionProvisioningPojo vpncp;
	private final VpnConnectionProfilePojo vpnConnectionProfile;
	private final VpnConnectionProfileAssignmentPojo vpnConnectionProfileAssignment;
	private final VpnConnectionRequisitionPojo vpncRequisition;
	private final String provisioningId;
	public String getProvisioningId() {
		return provisioningId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainVpnConnectionProvisioningPlace() {
		this.vpncRequisition = null;
		this.provisioningId = null;
		this.vpncp = null;
		this.vpnConnectionProfile = null;
		this.vpnConnectionProfileAssignment = null;
	}
	private MaintainVpnConnectionProvisioningPlace(String provisioningId, VpnConnectionProvisioningPojo vpncp) {
		this.vpncRequisition = null;
		this.provisioningId = provisioningId;
		this.vpncp = vpncp;
		this.vpnConnectionProfile = null;
		this.vpnConnectionProfileAssignment = null;
	}
	private MaintainVpnConnectionProvisioningPlace(VpnConnectionProfilePojo profile) {
		this.vpncRequisition = null;
		this.provisioningId = null;
		this.vpncp = null;
		this.vpnConnectionProfile = profile;
		this.vpnConnectionProfileAssignment = null;
	}
	private MaintainVpnConnectionProvisioningPlace(VpnConnectionProfilePojo profile, VpnConnectionProfileAssignmentPojo profileAssignment) {
		this.vpncRequisition = null;
		this.provisioningId = null;
		this.vpncp = null;
		this.vpnConnectionProfile = profile;
		this.vpnConnectionProfileAssignment = profileAssignment;
	}
	private MaintainVpnConnectionProvisioningPlace(VpnConnectionRequisitionPojo requisition, VpnConnectionProfileAssignmentPojo assignment) {
		this.vpncRequisition = requisition;
		this.vpnConnectionProfileAssignment = assignment;
		this.provisioningId = null;
		this.vpncp = null;
		this.vpnConnectionProfile = null;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public VpnConnectionProvisioningPojo getVpnConnectionProvisioning() {
		return vpncp;
	}
	public VpnConnectionProfilePojo getVpnConnectionProfile() {
		return vpnConnectionProfile;
	}
	public VpnConnectionProfileAssignmentPojo getVpnConnectionProfileAssignment() {
		return vpnConnectionProfileAssignment;
	}
	public VpnConnectionRequisitionPojo getVpncRequisition() {
		return vpncRequisition;
	}
}
