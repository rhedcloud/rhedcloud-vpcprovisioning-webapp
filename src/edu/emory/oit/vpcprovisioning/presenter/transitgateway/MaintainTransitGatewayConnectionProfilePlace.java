package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;

public class MaintainTransitGatewayConnectionProfilePlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix(Constants.MAINTAIN_TRANSIT_GATEWAY_CONNECTION_PROFILE)
	public static class Tokenizer implements PlaceTokenizer<MaintainTransitGatewayConnectionProfilePlace> {

		private static final String NO_ID = "createTransitGatewayConnectionProfile";

		public MaintainTransitGatewayConnectionProfilePlace getPlace(String token) {
			if (token != null) {
				return new MaintainTransitGatewayConnectionProfilePlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a record
				return MaintainTransitGatewayConnectionProfilePlace.getMaintainTransitGatewayConnectionProfilePlace();
			}
		}

		public String getToken(MaintainTransitGatewayConnectionProfilePlace place) {
			String id = place.getTransitGatewayConnectionProfileId();
			return (id == null) ? NO_ID : id;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainTransitGatewayConnectionProfilePlace singleton;

	/**
	 * Create an instance of {@link MaintainTransitGatewayConnectionProfilePlace} associated with the specified record
	 * ID.
	 * 
	 * @param mrn the ID of the record to edit
	 * @param pojo the transit gateway to edit, or null if not available
	 * @return the place
	 */
	public static MaintainTransitGatewayConnectionProfilePlace createMaintainTransitGatewayConnectionProfilePlace(TransitGatewayConnectionProfilePojo pojo) {
		return new MaintainTransitGatewayConnectionProfilePlace(pojo.getTransitGatewayConnectionProfileId(), pojo);
	}

	/**
	 * Get the singleton instance of the {@link MaintainTransitGatewayConnectionProfilePlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainTransitGatewayConnectionProfilePlace getMaintainTransitGatewayConnectionProfilePlace() {
		if (singleton == null) {
			singleton = new MaintainTransitGatewayConnectionProfilePlace(null, null);
		}
		return singleton;
	}

	private final TransitGatewayConnectionProfilePojo transitGatewayConnectionProfile;
	private final String transitGatewayConnectionProfileId;
	public String getTransitGatewayConnectionProfileId() {
		return transitGatewayConnectionProfileId;
	}

	/**
	 * Construct a new {@link MaintainTransitGatewayConnectionProfilePlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainTransitGatewayConnectionProfilePlace(String transitGatewayConnectionProfileId, TransitGatewayConnectionProfilePojo pojo) {
		this.transitGatewayConnectionProfileId = transitGatewayConnectionProfileId;
		this.transitGatewayConnectionProfile = pojo;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public TransitGatewayConnectionProfilePojo getTransitGatewayConnectionProfile() {
		return transitGatewayConnectionProfile;
	}
}
