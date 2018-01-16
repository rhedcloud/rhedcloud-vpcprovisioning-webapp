package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryFilterPojo;

public class ListElasticIpPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("ElasticIpList")
	public static class Tokenizer implements PlaceTokenizer<ListElasticIpPlace> {

		public ListElasticIpPlace getPlace(String token) {
			return new ListElasticIpPlace(true);
		}

		public String getToken(ListElasticIpPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	ElasticIpQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListElasticIpPlace(boolean listStale) {
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

	public ElasticIpQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(ElasticIpQueryFilterPojo filter) {
		this.filter = filter;
	}


}
