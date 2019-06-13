package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;

public class MaintainVpcpPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainVpcp")
	public static class Tokenizer implements PlaceTokenizer<MaintainVpcpPlace> {

		private static final String NO_ID = "generateVpcp";

		public MaintainVpcpPlace getPlace(String token) {
			if (token != null) {
				return new MaintainVpcpPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainVpcpPlace.getMaintainVpcpPlace();
			}
		}

		public String getToken(MaintainVpcpPlace place) {
			String provisioningId = place.getProvisioningId();
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainVpcpPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainVpcpPlace createMaintainVpcpPlace(VpcpPojo vpcp) {
		return new MaintainVpcpPlace(vpcp.getProvisioningId(), vpcp);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainVpcpPlace getMaintainVpcpPlace() {
		if (singleton == null) {
			singleton = new MaintainVpcpPlace(null, null);
		}
		return singleton;
	}

	private final VpcpPojo vpcp;
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
	private MaintainVpcpPlace(String provisioningId, VpcpPojo vpcp) {
		this.provisioningId = provisioningId;
		this.vpcp = vpcp;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public VpcpPojo getVpcp() {
		return vpcp;
	}
}
