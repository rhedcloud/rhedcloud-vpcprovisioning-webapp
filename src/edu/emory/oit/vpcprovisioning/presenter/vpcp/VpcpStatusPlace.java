package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpcpSummaryPojo;

public class VpcpStatusPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("vpcpStatus")
	public static class Tokenizer implements PlaceTokenizer<VpcpStatusPlace> {

		private static final String NO_ID = "vpcpStatus";

		public VpcpStatusPlace getPlace(String token) {
			if (token != null) {
				return new VpcpStatusPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return VpcpStatusPlace.getVpcpStatusPlace();
			}
		}

		public String getToken(VpcpStatusPlace place) {
			String provisioningId = place.getProvisioningId();
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static VpcpStatusPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static VpcpStatusPlace createVpcpStatusPlace(VpcpSummaryPojo vpcpSummary) {
		if (vpcpSummary.isProvision()) {
			return new VpcpStatusPlace(vpcpSummary.getProvisioning().getProvisioningId(), vpcpSummary);
		}
		else {
			return new VpcpStatusPlace(vpcpSummary.getDeprovisioning().getProvisioningId(), vpcpSummary);
		}
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static VpcpStatusPlace getVpcpStatusPlace() {
		if (singleton == null) {
			singleton = new VpcpStatusPlace(null, null);
		}
		return singleton;
	}

	private final VpcpSummaryPojo vpcpSummary;
//	private final VpcpPojo vpcp;
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
	private VpcpStatusPlace(String provisioningId, VpcpSummaryPojo vpcpSummary) {
		this.provisioningId = provisioningId;
//		this.vpcp = vpcp;
		this.vpcpSummary = vpcpSummary;
	}

//	public VpcpPojo getVpcp() {
//		return vpcp;
//	}

	public VpcpSummaryPojo getVpcpSummary() {
		return vpcpSummary;
	}
}
