package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class MaintainServiceControlPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainServiceControl")
	public static class Tokenizer implements PlaceTokenizer<MaintainServiceControlPlace> {

		private static final String NO_ID = "createServiceControl";

		public MaintainServiceControlPlace getPlace(String token) {
			if (token != null) {
				return new MaintainServiceControlPlace(token, null, null, null, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainServiceControlPlace.getMaintainServiceControlPlace(null, null, null);
			}
		}

		public String getToken(MaintainServiceControlPlace place) {
			String serviceControlId = place.getServiceControlId();
			return (serviceControlId == null) ? NO_ID : serviceControlId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainServiceControlPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainServiceControlPlace createMaintainServiceControlPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk, ServiceControlPojo serviceControl) {
		return new MaintainServiceControlPlace(serviceControl.getServiceControlId(), service, assessment, risk, serviceControl);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainServiceControlPlace getMaintainServiceControlPlace(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk) {
		if (singleton == null) {
			singleton = new MaintainServiceControlPlace(null, service, assessment, risk, null);
		}
		return singleton;
	}

	private final SecurityRiskPojo risk;
	private final ServiceSecurityAssessmentPojo assessment;
	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}
	private final AWSServicePojo service;
	public AWSServicePojo getService() {
		return service;
	}
	private final ServiceControlPojo serviceControl;
	private final String serviceControlId;
	public String getServiceControlId() {
		return serviceControlId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainServiceControlPlace(String serviceControlId, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk, ServiceControlPojo serviceControl) {
		this.serviceControlId = serviceControlId;
		this.serviceControl = serviceControl;
		this.service = service;
		this.assessment = assessment;
		this.risk = risk;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public ServiceControlPojo getServiceControl() {
		return serviceControl;
	}

	public SecurityRiskPojo getRisk() {
		return risk;
	}
}
