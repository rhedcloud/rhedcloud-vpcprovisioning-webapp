package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;

public class MaintainServicePlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainService")
	public static class Tokenizer implements PlaceTokenizer<MaintainServicePlace> {

		private static final String NO_ID = "createService";

		public MaintainServicePlace getPlace(String token) {
			if (token != null) {
				return new MaintainServicePlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainServicePlace.getMaintainServicePlace();
			}
		}

		public String getToken(MaintainServicePlace place) {
			String serviceId = place.getServiceId();
			return (serviceId == null) ? NO_ID : serviceId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainServicePlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainServicePlace createMaintainServicePlace(AWSServicePojo service) {
		return new MaintainServicePlace(service.getServiceId(), service);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainServicePlace getMaintainServicePlace() {
		if (singleton == null) {
			singleton = new MaintainServicePlace(null, null);
		}
		return singleton;
	}

	private final AWSServicePojo service;
	private final String serviceId;
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainServicePlace(String serviceId, AWSServicePojo service) {
		this.serviceId = serviceId;
		this.service = service;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public AWSServicePojo getService() {
		return service;
	}
}
