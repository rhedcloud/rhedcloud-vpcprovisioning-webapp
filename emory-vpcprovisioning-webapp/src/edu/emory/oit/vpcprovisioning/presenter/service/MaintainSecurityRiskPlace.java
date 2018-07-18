package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class MaintainSecurityRiskPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainSecurityRisk")
	public static class Tokenizer implements PlaceTokenizer<MaintainSecurityRiskPlace> {

		private static final String NO_ID = "createSecurityRisk";

		public MaintainSecurityRiskPlace getPlace(String token) {
			if (token != null) {
				return new MaintainSecurityRiskPlace(token, null, null, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainSecurityRiskPlace.getMaintainSecurityRiskPlace(null, null);
			}
		}

		public String getToken(MaintainSecurityRiskPlace place) {
			String securityRiskId = place.getsecurityRiskId();
			return (securityRiskId == null) ? NO_ID : securityRiskId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainSecurityRiskPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainSecurityRiskPlace createMaintainSecurityRiskPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo securityRisk) {
		return new MaintainSecurityRiskPlace(securityRisk.getSecurityRiskId(), service, assessment, securityRisk);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainSecurityRiskPlace getMaintainSecurityRiskPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		if (singleton == null) {
			singleton = new MaintainSecurityRiskPlace(null, service, assessment, null);
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
	private final SecurityRiskPojo securityRisk;
	public SecurityRiskPojo getsecurityRisk() {
		return securityRisk;
	}
	private final String securityRiskId;
	public String getsecurityRiskId() {
		return securityRiskId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainSecurityRiskPlace(String securityRiskId, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo securityRisk) {
		this.securityRiskId = securityRiskId;
		this.securityRisk = securityRisk;
		this.service = service;
		this.assessment = assessment;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public SecurityRiskPojo getSecurityRisk() {
		return securityRisk;
	}
}
