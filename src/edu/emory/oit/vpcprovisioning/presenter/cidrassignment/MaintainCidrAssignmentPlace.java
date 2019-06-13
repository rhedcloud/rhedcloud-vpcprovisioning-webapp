package edu.emory.oit.vpcprovisioning.presenter.cidrassignment;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;

public class MaintainCidrAssignmentPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainCidrAssignment")
	public static class Tokenizer implements PlaceTokenizer<MaintainCidrAssignmentPlace> {

		private static final String NO_ID = "createCidrAssignment";

		public MaintainCidrAssignmentPlace getPlace(String token) {
			if (token != null) {
				return new MaintainCidrAssignmentPlace(token, null, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainCidrAssignmentPlace.getMaintainCidrAssignmentPlace();
			}
		}

		public String getToken(MaintainCidrAssignmentPlace place) {
			String assignmentId = place.getAssignmentId();
			return (assignmentId == null) ? NO_ID : assignmentId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainCidrAssignmentPlace singleton;
	private boolean registeringVpc;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
//	public static MaintainCidrAssignmentPlace createMaintainCidrAssignmentPlace(String assignmentId, CidrAssignmentSummaryPojo cidrAssignmentSummary) {
//		return new MaintainCidrAssignmentPlace(assignmentId, null, cidrAssignmentSummary);
//	}
	public static MaintainCidrAssignmentPlace createMaintainCidrAssignmentPlace(String assignmentId, CidrPojo cidr, CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		return new MaintainCidrAssignmentPlace(assignmentId, cidr, cidrAssignmentSummary);
	}

	public static MaintainCidrAssignmentPlace getMaintainCidrAssignmentPlace(CidrPojo cidr) {
		if (singleton == null) {
			singleton = new MaintainCidrAssignmentPlace(null, cidr, null);
			singleton.setRegisteringVpc(false);
		}
		return singleton;
	}
	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainCidrAssignmentPlace getMaintainCidrAssignmentPlace() {
		if (singleton == null) {
			singleton = new MaintainCidrAssignmentPlace(null, null, null);
			singleton.setRegisteringVpc(false);
		}
		return singleton;
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainCidrAssignmentPlace getMaintainCidrAssignmentPlace(boolean registeringVpc) {
		if (singleton == null) {
			singleton = new MaintainCidrAssignmentPlace(null, null, null);
			singleton.setRegisteringVpc(registeringVpc);
		}
		return singleton;
	}

	private final CidrPojo cidr;
	private final CidrAssignmentSummaryPojo cidrAssignmentSummary;
	private final String assignmentId;
	public String getAssignmentId() {
		return assignmentId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainCidrAssignmentPlace(String assignmentId, CidrPojo cidr, CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		this.assignmentId = assignmentId;
		this.cidrAssignmentSummary = cidrAssignmentSummary;
		this.cidr = cidr;
	}

	private MaintainCidrAssignmentPlace(CidrPojo cidr) {
		this.assignmentId = null;
		this.cidrAssignmentSummary = null;
		this.cidr = cidr;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public CidrAssignmentSummaryPojo getCidrAssignmentSummary() {
		return cidrAssignmentSummary;
	}

	public boolean isRegisteringVpc() {
		return registeringVpc;
	}

	public void setRegisteringVpc(boolean registeringVpc) {
		this.registeringVpc = registeringVpc;
	}

	public CidrPojo getCidr() {
		return cidr;
	}
}
