package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;


public class MaintainSecurityAssessmentPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainSecurityAssessment")
	public static class Tokenizer implements PlaceTokenizer<MaintainSecurityAssessmentPlace> {

		private static final String NO_ID = "createSecurityAssessment";

		public MaintainSecurityAssessmentPlace getPlace(String token) {
			if (token != null) {
				return new MaintainSecurityAssessmentPlace(token, null, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainSecurityAssessmentPlace.getMaintainSecurityAssessmentPlace(null);
			}
		}

		public String getToken(MaintainSecurityAssessmentPlace place) {
			String assessmentId = place.getAssessmentId();
			return (assessmentId == null) ? NO_ID : assessmentId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainSecurityAssessmentPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainSecurityAssessmentPlace createMaintainSecurityAssessmentPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		return new MaintainSecurityAssessmentPlace(assessment.getServiceSecurityAssessmentId(), service, assessment);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainSecurityAssessmentPlace getMaintainSecurityAssessmentPlace(AWSServicePojo service) {
		if (singleton == null) {
			singleton = new MaintainSecurityAssessmentPlace(null, service, null);
		}
		return singleton;
	}

	private final AWSServicePojo service;
	public AWSServicePojo getService() {
		return service;
	}
	private final ServiceSecurityAssessmentPojo assessment;
	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}
	private final String assessmentId;
	public String getAssessmentId() {
		return assessmentId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainSecurityAssessmentPlace(String assessmentId, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.assessmentId = assessmentId;
		this.assessment = assessment;
		this.service = service;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return assessment;
	}
}
