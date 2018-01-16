package edu.emory.oit.vpcprovisioning.presenter.elasticipassignment;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;

public class MaintainElasticIpAssignmentPlace extends Place {

	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainElasticIpAssignment")
	public static class Tokenizer implements PlaceTokenizer<MaintainElasticIpAssignmentPlace> {

		private static final String NO_ID = "createElasticIpAssignment";

		public MaintainElasticIpAssignmentPlace getPlace(String token) {
			if (token != null) {
				return new MaintainElasticIpAssignmentPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainElasticIpAssignmentPlace.getMaintainElasticIpAssignmentPlace();
			}
		}

		public String getToken(MaintainElasticIpAssignmentPlace place) {
			String assignmentId = place.getAssignmentId();
			return (assignmentId == null) ? NO_ID : assignmentId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainElasticIpAssignmentPlace singleton;
//	private boolean registeringVpc;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainElasticIpAssignmentPlace createMaintainElasticIpAssignmentPlace(String assignmentId, ElasticIpAssignmentSummaryPojo elasticIpAssignmentSummary) {
		return new MaintainElasticIpAssignmentPlace(assignmentId, elasticIpAssignmentSummary);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainElasticIpAssignmentPlace getMaintainElasticIpAssignmentPlace() {
		if (singleton == null) {
			singleton = new MaintainElasticIpAssignmentPlace(null, null);
			singleton.assignmentId = null;
		}
		return singleton;
	}

	private MaintainElasticIpAssignmentPlace(String assignmentId) {
		this.assignmentId = assignmentId;
		this.elasticIpAssignmentSummary = null;
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
//	public static MaintainElasticIpAssignmentPlace getMaintainElasticIpAssignmentPlace(boolean registeringVpc) {
//		if (singleton == null) {
//			singleton = new MaintainElasticIpAssignmentPlace(null, null);
//			singleton.setRegisteringVpc(registeringVpc);
//		}
//		return singleton;
//	}

	private final ElasticIpAssignmentSummaryPojo elasticIpAssignmentSummary;
	private String assignmentId;
	public String getAssignmentId() {
		return assignmentId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainElasticIpAssignmentPlace(String assignmentId, ElasticIpAssignmentSummaryPojo elasticIpAssignmentSummary) {
		this.assignmentId = assignmentId;
		this.elasticIpAssignmentSummary = elasticIpAssignmentSummary;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public ElasticIpAssignmentSummaryPojo getElasticIpAssignmentSummary() {
		return elasticIpAssignmentSummary;
	}

//	public boolean isRegisteringVpc() {
//		return registeringVpc;
//	}
//
//	public void setRegisteringVpc(boolean registeringVpc) {
//		this.registeringVpc = registeringVpc;
//	}
}
