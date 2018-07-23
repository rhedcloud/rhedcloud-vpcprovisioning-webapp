package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class ListServiceGuidelinePlace extends Place {
	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("serviceGuidelineList")
	public static class Tokenizer implements PlaceTokenizer<ListServiceGuidelinePlace> {

		public ListServiceGuidelinePlace getPlace(String token) {
			return new ListServiceGuidelinePlace(true);
		}

		public String getToken(ListServiceGuidelinePlace place) {
			return "";
		}
	}

	private final boolean listStale;
	ServiceSecurityAssessmentPojo assessment;
	AWSServicePojo service;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListServiceGuidelinePlace(boolean listStale) {
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

	public AWSServicePojo getService() {
		return service;
	}

	public void setService(AWSServicePojo service) {
		this.service = service;
	}
}
