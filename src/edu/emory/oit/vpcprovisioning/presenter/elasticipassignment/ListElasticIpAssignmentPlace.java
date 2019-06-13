package edu.emory.oit.vpcprovisioning.presenter.elasticipassignment;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ListElasticIpAssignmentPlace extends Place {
	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("elasticIpAssignmentList")
	public static class Tokenizer implements PlaceTokenizer<ListElasticIpAssignmentPlace> {

		public ListElasticIpAssignmentPlace getPlace(String token) {
			return new ListElasticIpAssignmentPlace(true);
		}

		public String getToken(ListElasticIpAssignmentPlace place) {
			return "";
		}
	}

	private final boolean listStale;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListElasticIpAssignmentPlace(boolean listStale) {
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

}
