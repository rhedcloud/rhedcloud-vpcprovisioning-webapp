package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;

public class MaintainElasticIpPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainElasticIp")
	public static class Tokenizer implements PlaceTokenizer<MaintainElasticIpPlace> {

		private static final String NO_ID = "createCidr";

		public MaintainElasticIpPlace getPlace(String token) {
			if (token != null) {
				return new MaintainElasticIpPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainElasticIpPlace.getMaintainElasticIpPlace();
			}
		}

		public String getToken(MaintainElasticIpPlace place) {
			String id = place.getElasticIpId();
			return (id == null) ? NO_ID : id;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainElasticIpPlace singleton;

	/**
	 * Create an instance of {@link MaintainElasticIpPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainElasticIpPlace createMaintainElasticIpPlace(ElasticIpPojo pojo) {
		return new MaintainElasticIpPlace(pojo.getElasticIpId(), pojo);
	}

	/**
	 * Get the singleton instance of the {@link MaintainElasticIpPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainElasticIpPlace getMaintainElasticIpPlace() {
		if (singleton == null) {
			singleton = new MaintainElasticIpPlace(null, null);
		}
		return singleton;
	}

	private final ElasticIpPojo elasticIp;
	private final String elasticIpId;
	public String getElasticIpId() {
		return elasticIpId;
	}

	/**
	 * Construct a new {@link MaintainElasticIpPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainElasticIpPlace(String elasticIpId, ElasticIpPojo pojo) {
		this.elasticIpId = elasticIpId;
		this.elasticIp = pojo;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public ElasticIpPojo getElasticIp() {
		return elasticIp;
	}

}
