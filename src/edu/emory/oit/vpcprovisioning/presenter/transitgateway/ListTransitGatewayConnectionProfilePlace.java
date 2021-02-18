package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryFilterPojo;

public class ListTransitGatewayConnectionProfilePlace extends Place {

	public ListTransitGatewayConnectionProfilePlace(boolean listStale) {
		this.listStale = listStale;
	}

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_TRANSIT_GATEWAY_CONNECTION_PROFILE)
	public static class Tokenizer implements PlaceTokenizer<ListTransitGatewayConnectionProfilePlace> {

		public ListTransitGatewayConnectionProfilePlace getPlace(String token) {
			return new ListTransitGatewayConnectionProfilePlace(true);
		}

		public String getToken(ListTransitGatewayConnectionProfilePlace place) {
			return "";
		}
	}

	private final boolean listStale;
	TransitGatewayConnectionProfileQueryFilterPojo filter;

	/**
	 * Check if the case record list is stale and should be cleared.
	 * 
	 * @return true if stale, false if not
	 */
	public boolean isListStale() {
		return listStale;
	}

	public TransitGatewayConnectionProfileQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(TransitGatewayConnectionProfileQueryFilterPojo filter) {
		this.filter = filter;
	}
}
