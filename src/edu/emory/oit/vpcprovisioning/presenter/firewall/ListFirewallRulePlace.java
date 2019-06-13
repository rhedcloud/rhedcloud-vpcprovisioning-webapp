package edu.emory.oit.vpcprovisioning.presenter.firewall;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryFilterPojo;

public class ListFirewallRulePlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("firewallRuleList")
	public static class Tokenizer implements PlaceTokenizer<ListFirewallRulePlace> {

		public ListFirewallRulePlace getPlace(String token) {
			return new ListFirewallRulePlace(true);
		}

		public String getToken(ListFirewallRulePlace place) {
			return "";
		}
	}

	private final boolean listStale;
	FirewallRuleQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListFirewallRulePlace(boolean listStale) {
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

	public FirewallRuleQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(FirewallRuleQueryFilterPojo filter) {
		this.filter = filter;
	}
}
