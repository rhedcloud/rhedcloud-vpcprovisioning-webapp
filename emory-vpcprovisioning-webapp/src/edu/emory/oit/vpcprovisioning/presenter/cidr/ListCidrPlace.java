package edu.emory.oit.vpcprovisioning.presenter.cidr;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.CidrQueryFilterPojo;

public class ListCidrPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("cidrList")
	public static class Tokenizer implements PlaceTokenizer<ListCidrPlace> {

		public ListCidrPlace getPlace(String token) {
			return new ListCidrPlace(true);
		}

		public String getToken(ListCidrPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	CidrQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListCidrPlace(boolean listStale) {
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

	public CidrQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(CidrQueryFilterPojo filter) {
		this.filter = filter;
	}

}
