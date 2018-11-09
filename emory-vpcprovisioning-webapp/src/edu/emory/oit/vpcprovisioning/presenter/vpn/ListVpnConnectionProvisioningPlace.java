package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryFilterPojo;

public class ListVpnConnectionProvisioningPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_VPN_CONNECTION)
	public static class Tokenizer implements PlaceTokenizer<ListVpnConnectionProvisioningPlace> {

		public ListVpnConnectionProvisioningPlace getPlace(String token) {
			return new ListVpnConnectionProvisioningPlace(true);
		}

		public String getToken(ListVpnConnectionProvisioningPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	VpnConnectionProvisioningQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListVpnConnectionProvisioningPlace(boolean listStale) {
		GWT.log("List VpnConnectionProvisioning place");
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

	public VpnConnectionProvisioningQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpnConnectionProvisioningQueryFilterPojo filter) {
		this.filter = filter;
	}
}
