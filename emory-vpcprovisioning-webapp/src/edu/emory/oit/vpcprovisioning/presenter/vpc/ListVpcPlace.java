package edu.emory.oit.vpcprovisioning.presenter.vpc;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;

public class ListVpcPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("VpcList")
	public static class Tokenizer implements PlaceTokenizer<ListVpcPlace> {

		public ListVpcPlace getPlace(String token) {
			return new ListVpcPlace(true);
		}

		public String getToken(ListVpcPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	VpcQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListVpcPlace(boolean listStale) {
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

	public VpcQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpcQueryFilterPojo filter) {
		this.filter = filter;
	}
}
