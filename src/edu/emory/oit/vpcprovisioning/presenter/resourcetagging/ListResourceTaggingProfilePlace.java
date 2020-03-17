package edu.emory.oit.vpcprovisioning.presenter.resourcetagging;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryFilterPojo;

public class ListResourceTaggingProfilePlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_RESOURCE_TAGGING_PROFILE)
	public static class Tokenizer implements PlaceTokenizer<ListResourceTaggingProfilePlace> {

		public ListResourceTaggingProfilePlace getPlace(String token) {
			return new ListResourceTaggingProfilePlace(true);
		}

		public String getToken(ListResourceTaggingProfilePlace place) {
			return "";
		}
	}

	private final boolean listStale;
	ResourceTaggingProfileQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListResourceTaggingProfilePlace(boolean listStale) {
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

	public ResourceTaggingProfileQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(ResourceTaggingProfileQueryFilterPojo filter) {
		this.filter = filter;
	}


}
