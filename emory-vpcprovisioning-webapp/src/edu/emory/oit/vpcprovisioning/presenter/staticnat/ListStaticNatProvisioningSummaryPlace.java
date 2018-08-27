package edu.emory.oit.vpcprovisioning.presenter.staticnat;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryQueryFilterPojo;

public class ListStaticNatProvisioningSummaryPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_STATIC_NAT)
	public static class Tokenizer implements PlaceTokenizer<ListStaticNatProvisioningSummaryPlace> {

		public ListStaticNatProvisioningSummaryPlace getPlace(String token) {
			return new ListStaticNatProvisioningSummaryPlace(true);
		}

		public String getToken(ListStaticNatProvisioningSummaryPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	StaticNatProvisioningSummaryQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListStaticNatProvisioningSummaryPlace(boolean listStale) {
		GWT.log("StaticNatProvisioning place");
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

	public StaticNatProvisioningSummaryQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(StaticNatProvisioningSummaryQueryFilterPojo filter) {
		this.filter = filter;
	}
}
