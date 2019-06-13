package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryFilterPojo;

public class ListVpnConnectionProfilePlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_VPN_CONNECTION_PROFILE)
	public static class Tokenizer implements PlaceTokenizer<ListVpnConnectionProfilePlace> {

		public ListVpnConnectionProfilePlace getPlace(String token) {
			return new ListVpnConnectionProfilePlace(true);
		}

		public String getToken(ListVpnConnectionProfilePlace place) {
			return "";
		}
	}

	private final boolean listStale;
	VpnConnectionProfileQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListVpnConnectionProfilePlace(boolean listStale) {
		GWT.log("List VpnConnectionProfile place");
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

	public VpnConnectionProfileQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpnConnectionProfileQueryFilterPojo filter) {
		this.filter = filter;
	}
}
