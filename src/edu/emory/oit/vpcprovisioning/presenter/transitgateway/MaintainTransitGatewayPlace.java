package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;

public class MaintainTransitGatewayPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix(Constants.MAINTAIN_TRANSIT_GATEWAY)
	public static class Tokenizer implements PlaceTokenizer<MaintainTransitGatewayPlace> {

		private static final String NO_ID = "createTransitGateway";

		public MaintainTransitGatewayPlace getPlace(String token) {
			if (token != null) {
				return new MaintainTransitGatewayPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a record
				return MaintainTransitGatewayPlace.getMaintainTransitGatewayPlace();
			}
		}

		public String getToken(MaintainTransitGatewayPlace place) {
			String id = place.getTransitGatewayId();
			return (id == null) ? NO_ID : id;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainTransitGatewayPlace singleton;

	/**
	 * Create an instance of {@link MaintainTransitGatewayPlace} associated with the specified record
	 * ID.
	 * 
	 * @param mrn the ID of the record to edit
	 * @param pojo the transit gateway to edit, or null if not available
	 * @return the place
	 */
	public static MaintainTransitGatewayPlace createMaintainTransitGatewayPlace(TransitGatewayPojo pojo) {
		return new MaintainTransitGatewayPlace(pojo.getTransitGatewayId(), pojo);
	}

	/**
	 * Get the singleton instance of the {@link MaintainTransitGatewayPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainTransitGatewayPlace getMaintainTransitGatewayPlace() {
		if (singleton == null) {
			singleton = new MaintainTransitGatewayPlace(null, null);
		}
		return singleton;
	}

	private final TransitGatewayPojo transitGateway;
	private final String transitGatewayId;
	public String getTransitGatewayId() {
		return transitGatewayId;
	}

	/**
	 * Construct a new {@link MaintainTransitGatewayPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainTransitGatewayPlace(String transitGatewayId, TransitGatewayPojo pojo) {
		this.transitGatewayId = transitGatewayId;
		this.transitGateway = pojo;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public TransitGatewayPojo getTransitGateway() {
		return transitGateway;
	}
}
