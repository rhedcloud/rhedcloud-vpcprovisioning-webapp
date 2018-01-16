package edu.emory.oit.vpcprovisioning.presenter.vpc;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class MaintainVpcPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainVpc")
	public static class Tokenizer implements PlaceTokenizer<MaintainVpcPlace> {

		private static final String NO_ID = "createVpc";

		public MaintainVpcPlace getPlace(String token) {
			if (token != null) {
				return new MaintainVpcPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainVpcPlace.getMaintainVpcPlace();
			}
		}

		public String getToken(MaintainVpcPlace place) {
			String vpcId = place.getVpcId();
			return (vpcId == null) ? NO_ID : vpcId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainVpcPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainVpcPlace createMaintainVpcPlace(VpcPojo vpc) {
		return new MaintainVpcPlace(vpc.getVpcId(), vpc);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainVpcPlace getMaintainVpcPlace() {
		if (singleton == null) {
			singleton = new MaintainVpcPlace(null, null);
		}
		return singleton;
	}

	private final VpcPojo vpc;
	private final String vpcId;
	public String getVpcId() {
		return vpcId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainVpcPlace(String vpcId, VpcPojo vpc) {
		this.vpcId = vpcId;
		this.vpc = vpc;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public VpcPojo getVpc() {
		return vpc;
	}
}
