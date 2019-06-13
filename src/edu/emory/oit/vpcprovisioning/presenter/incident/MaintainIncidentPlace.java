package edu.emory.oit.vpcprovisioning.presenter.incident;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;

public class MaintainIncidentPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainIncident")
	public static class Tokenizer implements PlaceTokenizer<MaintainIncidentPlace> {

		private static final String NO_ID = "generateIncident";

		public MaintainIncidentPlace getPlace(String token) {
			if (token != null) {
				return new MaintainIncidentPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainIncidentPlace.getMaintainIncidentPlace();
			}
		}

		public String getToken(MaintainIncidentPlace place) {
			String number = place.getNumber();
			return (number == null) ? NO_ID : number;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainIncidentPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainIncidentPlace createMaintainIncidentPlace(IncidentPojo incident) {
		return new MaintainIncidentPlace(incident.getNumber(), incident);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainIncidentPlace getMaintainIncidentPlace() {
		if (singleton == null) {
			singleton = new MaintainIncidentPlace(null, null);
		}
		return singleton;
	}

	private final IncidentPojo incident;
	private final String number;
	public String getNumber() {
		return number;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainIncidentPlace(String number, IncidentPojo incident) {
		this.number = number;
		this.incident = incident;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public IncidentPojo getIncident() {
		return incident;
	}
}
