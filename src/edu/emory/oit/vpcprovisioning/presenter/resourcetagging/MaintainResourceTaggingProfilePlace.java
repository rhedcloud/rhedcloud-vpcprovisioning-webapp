package edu.emory.oit.vpcprovisioning.presenter.resourcetagging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;

public class MaintainResourceTaggingProfilePlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainRtp")
	public static class Tokenizer implements PlaceTokenizer<MaintainResourceTaggingProfilePlace> {

		private static final String NO_ID = "createRtp";

		public MaintainResourceTaggingProfilePlace getPlace(String token) {
			if (token != null) {
				GWT.log("maintain RTP place...got a token: " + token);
				return new MaintainResourceTaggingProfilePlace(false, token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainResourceTaggingProfilePlace.getMaintainResourceTaggingProfilePlace();
			}
		}

		public String getToken(MaintainResourceTaggingProfilePlace place) {
			String profileId = place.getResourceTaggingProfileId();
			return (profileId == null) ? NO_ID : profileId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainResourceTaggingProfilePlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainResourceTaggingProfilePlace createMaintainResourceTaggingProfilePlace(boolean newRevision, ResourceTaggingProfilePojo rtp) {
		return new MaintainResourceTaggingProfilePlace(newRevision, rtp.getProfileId(), rtp);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainResourceTaggingProfilePlace getMaintainResourceTaggingProfilePlace() {
		if (singleton == null) {
			singleton = new MaintainResourceTaggingProfilePlace(false, null, null);
		}
		return singleton;
	}

	private final ResourceTaggingProfilePojo rtp;
	private final String resourceTaggingProfileId;
	private final boolean newRevision;
	
	public boolean isNewRevision() {
		return newRevision;
	}

	public String getResourceTaggingProfileId() {
		return resourceTaggingProfileId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainResourceTaggingProfilePlace(boolean newRevision, String resourceTaggingProfileId, ResourceTaggingProfilePojo rtp) {
		this.resourceTaggingProfileId = resourceTaggingProfileId;
		this.rtp = rtp;
		this.newRevision = newRevision;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public ResourceTaggingProfilePojo getResourceTaggingProfile() {
		return rtp;
	}
}
