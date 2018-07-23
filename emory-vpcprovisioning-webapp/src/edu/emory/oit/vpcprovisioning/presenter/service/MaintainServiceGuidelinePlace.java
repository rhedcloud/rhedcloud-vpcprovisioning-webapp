package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class MaintainServiceGuidelinePlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainServiceGuideline")
	public static class Tokenizer implements PlaceTokenizer<MaintainServiceGuidelinePlace> {

		private static final String NO_ID = "createServiceGuideline";

		public MaintainServiceGuidelinePlace getPlace(String token) {
			if (token != null) {
				return new MaintainServiceGuidelinePlace(token, null, null, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainServiceGuidelinePlace.getMaintainServiceGuidelinePlace(null, null);
			}
		}

		public String getToken(MaintainServiceGuidelinePlace place) {
			String serviceGuidelineId = place.getServiceGuidelineId();
			return (serviceGuidelineId == null) ? NO_ID : serviceGuidelineId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainServiceGuidelinePlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainServiceGuidelinePlace createMaintainServiceGuidelinePlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo serviceGuideline) {
		return new MaintainServiceGuidelinePlace(serviceGuideline.getUniqueId(), service, assessment, serviceGuideline);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainServiceGuidelinePlace getMaintainServiceGuidelinePlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		if (singleton == null) {
			singleton = new MaintainServiceGuidelinePlace(null, service, assessment, null);
		}
		return singleton;
	}

	private final ServiceSecurityAssessmentPojo assessment;
	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}
	private final AWSServicePojo service;
	public AWSServicePojo getService() {
		return service;
	}
	private final ServiceGuidelinePojo serviceGuideline;
	private final String serviceGuidelineId;
	public String getServiceGuidelineId() {
		return serviceGuidelineId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainServiceGuidelinePlace(String serviceGuidelineId, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo serviceGuideline) {
		this.serviceGuidelineId = serviceGuidelineId;
		this.serviceGuideline = serviceGuideline;
		this.service = service;
		this.assessment = assessment;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public ServiceGuidelinePojo getServiceGuideline() {
		return serviceGuideline;
	}
}
