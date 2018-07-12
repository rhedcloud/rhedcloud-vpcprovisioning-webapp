package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class ListSecurityRiskPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("securityRiskList")
	public static class Tokenizer implements PlaceTokenizer<ListSecurityRiskPlace> {

		public ListSecurityRiskPlace getPlace(String token) {
			return new ListSecurityRiskPlace(true);
		}

		public String getToken(ListSecurityRiskPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	ServiceSecurityAssessmentPojo assessment;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListSecurityRiskPlace(boolean listStale) {
		this.listStale = listStale;
	}

	/**
	 * Check if the case record list is stale and should be cleared.
	 * 
	 * @return true if stale, false if not
	 */
	public boolean isListStale() {
		return listStale;
	}

	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}

	public void setAssessment(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}
}
