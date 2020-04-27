package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class CalculateSecurityRiskPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("calculateRisk")
	public static class Tokenizer implements PlaceTokenizer<CalculateSecurityRiskPlace> {

		private static final String NO_ID = "calculateRisk";

		public CalculateSecurityRiskPlace getPlace(String token) {
			if (token != null) {
				return new CalculateSecurityRiskPlace(null, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return CalculateSecurityRiskPlace.getServiceAssessmentReportPlace();
			}
		}

		public String getToken(CalculateSecurityRiskPlace place) {
			return NO_ID;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static CalculateSecurityRiskPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static CalculateSecurityRiskPlace createCalculateRiskPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		return new CalculateSecurityRiskPlace(service, assessment);
	}
	public static CalculateSecurityRiskPlace createCalculateRiskPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk) {
		return new CalculateSecurityRiskPlace(service, assessment, risk);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static CalculateSecurityRiskPlace getServiceAssessmentReportPlace() {
		if (singleton == null) {
			singleton = new CalculateSecurityRiskPlace(null, null);
		}
		return singleton;
	}

	private final AWSServicePojo service;
	private final ServiceSecurityAssessmentPojo assessment;
	private final SecurityRiskPojo risk;
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
	private CalculateSecurityRiskPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.service = service;
		this.assessment = assessment;
		this.risk = null;
		this.securityRiskId = null;
	}
	private CalculateSecurityRiskPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk) {
		this.service = service;
		this.assessment = assessment;
		this.risk = risk;
		this.securityRiskId = risk.getSecurityRiskId();
	}

	public AWSServicePojo getService() {
		return service;
	}
	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}
	public SecurityRiskPojo getRisk() {
		return risk;
	}
}
