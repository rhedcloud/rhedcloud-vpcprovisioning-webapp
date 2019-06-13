package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class MaintainServiceTestPlanPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainServiceTestPlan")
	public static class Tokenizer implements PlaceTokenizer<MaintainServiceTestPlanPlace> {

		private static final String NO_ID = "createServiceTestPlan";

		public MaintainServiceTestPlanPlace getPlace(String token) {
			if (token != null) {
				return new MaintainServiceTestPlanPlace(null, null, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a test plan.
				return MaintainServiceTestPlanPlace.getMaintainServiceTestPlanPlace(null, null);
			}
		}

		public String getToken(MaintainServiceTestPlanPlace place) {
			if (place.getServiceTestPlan() == null) {
				return NO_ID;
			}
			String uniqueId = place.getServiceTestPlan().getServiceId();
			return (uniqueId == null) ? NO_ID : uniqueId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainServiceTestPlanPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainServiceTestPlanPlace createMaintainServiceTestPlanPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo serviceTestPlan) {
		return new MaintainServiceTestPlanPlace(service, assessment, serviceTestPlan);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainServiceTestPlanPlace getMaintainServiceTestPlanPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		if (singleton == null) {
			singleton = new MaintainServiceTestPlanPlace(service, assessment, null);
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
	private final ServiceTestPlanPojo serviceTestPlan;

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainServiceTestPlanPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo serviceTestPlan) {
		this.serviceTestPlan = serviceTestPlan;
		this.service = service;
		this.assessment = assessment;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public ServiceTestPlanPojo getServiceTestPlan() {
		return serviceTestPlan;
	}
}
