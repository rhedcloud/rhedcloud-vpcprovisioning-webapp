package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryFilterPojo;

public class ListTransitGatewayPlace extends Place {

	public ListTransitGatewayPlace(boolean listStale) {
		this.listStale = listStale;
	}

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_TRANSIT_GATEWAY)
	public static class Tokenizer implements PlaceTokenizer<ListTransitGatewayPlace> {

		public ListTransitGatewayPlace getPlace(String token) {
			return new ListTransitGatewayPlace(true);
		}

		public String getToken(ListTransitGatewayPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	TransitGatewayQueryFilterPojo filter;

	/**
	 * Check if the case record list is stale and should be cleared.
	 * 
	 * @return true if stale, false if not
	 */
	public boolean isListStale() {
		return listStale;
	}

	public TransitGatewayQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(TransitGatewayQueryFilterPojo filter) {
		this.filter = filter;
	}
}
