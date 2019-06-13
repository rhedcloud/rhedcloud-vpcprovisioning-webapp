package edu.emory.oit.vpcprovisioning.presenter.cidr;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.CidrPojo;

public class MaintainCidrPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainCidr")
	public static class Tokenizer implements PlaceTokenizer<MaintainCidrPlace> {

		private static final String NO_ID = "createCidr";

		public MaintainCidrPlace getPlace(String token) {
			if (token != null) {
				return new MaintainCidrPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainCidrPlace.getMaintainCidrPlace();
			}
		}

		public String getToken(MaintainCidrPlace place) {
			String cidrId = place.getCidrId();
			return (cidrId == null) ? NO_ID : cidrId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainCidrPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainCidrPlace createMaintainCidrPlace(CidrPojo cidr) {
		return new MaintainCidrPlace(cidr.getCidrId(), cidr);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainCidrPlace getMaintainCidrPlace() {
		if (singleton == null) {
			singleton = new MaintainCidrPlace(null, null);
		}
		return singleton;
	}

	private final CidrPojo cidr;
	private final String cidrId;
	public String getCidrId() {
		return cidrId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainCidrPlace(String cidrId, CidrPojo cidr) {
		this.cidrId = cidrId;
		this.cidr = cidr;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public CidrPojo getCidr() {
		return cidr;
	}
}
