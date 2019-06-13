package edu.emory.oit.vpcprovisioning.presenter.tou;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementPojo;

public class MaintainTermsOfUseAgreementPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainTermsOfUseAgreement")
	public static class Tokenizer implements PlaceTokenizer<MaintainTermsOfUseAgreementPlace> {

		private static final String NO_ID = "createTermsOfUseAgreement";

		public MaintainTermsOfUseAgreementPlace getPlace(String token) {
			if (token != null) {
				return new MaintainTermsOfUseAgreementPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainTermsOfUseAgreementPlace.getMaintainTermsOfUseAgreementPlace();
			}
		}

		public String getToken(MaintainTermsOfUseAgreementPlace place) {
			String termsOfUseAgreementId = place.getTermsOfUseAgreementId();
			return (termsOfUseAgreementId == null) ? NO_ID : termsOfUseAgreementId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainTermsOfUseAgreementPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainTermsOfUseAgreementPlace createMaintainTermsOfUseAgreementPlace(TermsOfUseAgreementPojo termsOfUseAgreement) {
		return new MaintainTermsOfUseAgreementPlace(termsOfUseAgreement.getTermsOfUseAgreementId(), termsOfUseAgreement);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainTermsOfUseAgreementPlace getMaintainTermsOfUseAgreementPlace() {
		if (singleton == null) {
			singleton = new MaintainTermsOfUseAgreementPlace(null, null);
		}
		return singleton;
	}

	private final TermsOfUseAgreementPojo termsOfUseAgreement;
	private final String termsOfUseAgreementId;
	public String getTermsOfUseAgreementId() {
		return termsOfUseAgreementId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainTermsOfUseAgreementPlace(String termsOfUseAgreementId, TermsOfUseAgreementPojo termsOfUseAgreement) {
		this.termsOfUseAgreementId = termsOfUseAgreementId;
		this.termsOfUseAgreement = termsOfUseAgreement;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public TermsOfUseAgreementPojo getTermsOfUseAgreement() {
		return termsOfUseAgreement;
	}
}
