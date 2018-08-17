package edu.emory.oit.vpcprovisioning.presenter.service;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;

public class ListServicePlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_SERVICES)
	public static class Tokenizer implements PlaceTokenizer<ListServicePlace> {

		public ListServicePlace getPlace(String token) {
			return new ListServicePlace(true);
		}

		public String getToken(ListServicePlace place) {
			return "";
		}
	}

	private final boolean listStale;
	AWSServiceQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListServicePlace(boolean listStale) {
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

	public AWSServiceQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(AWSServiceQueryFilterPojo filter) {
		this.filter = filter;
	}
}
