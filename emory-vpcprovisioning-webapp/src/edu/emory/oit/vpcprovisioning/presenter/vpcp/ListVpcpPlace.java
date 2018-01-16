package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;

public class ListVpcpPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("VpcpList")
	public static class Tokenizer implements PlaceTokenizer<ListVpcpPlace> {

		public ListVpcpPlace getPlace(String token) {
			return new ListVpcpPlace(true);
		}

		public String getToken(ListVpcpPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	VpcpQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListVpcpPlace(boolean listStale) {
		GWT.log("VPCP place");
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

	public VpcpQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpcpQueryFilterPojo filter) {
		this.filter = filter;
	}
}
