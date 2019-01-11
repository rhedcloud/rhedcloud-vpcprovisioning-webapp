package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;

public class ServiceAssessmentReportPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("serviceAssessmentReport")
	public static class Tokenizer implements PlaceTokenizer<ServiceAssessmentReportPlace> {

		private static final String NO_ID = "assessService";

		public ServiceAssessmentReportPlace getPlace(String token) {
			if (token != null) {
				return new ServiceAssessmentReportPlace(null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return ServiceAssessmentReportPlace.getServiceAssessmentReportPlace();
			}
		}

		public String getToken(ServiceAssessmentReportPlace place) {
			return NO_ID;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static ServiceAssessmentReportPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static ServiceAssessmentReportPlace createServiceAssessmentReportPlace(List<AWSServicePojo> services) {
		return new ServiceAssessmentReportPlace(services);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static ServiceAssessmentReportPlace getServiceAssessmentReportPlace() {
		if (singleton == null) {
			singleton = new ServiceAssessmentReportPlace(null);
		}
		return singleton;
	}

	private final List<AWSServicePojo> serviceList;

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private ServiceAssessmentReportPlace(List<AWSServicePojo> services) {
		this.serviceList = services;
	}

	public List<AWSServicePojo> getServiceList() {
		return serviceList;
	}
}
