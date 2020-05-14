package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryFilterPojo;

public class ListAccountProvisioningPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_ACCOUNT_PROVISIONING)
	public static class Tokenizer implements PlaceTokenizer<ListAccountProvisioningPlace> {

		public ListAccountProvisioningPlace getPlace(String token) {
			return new ListAccountProvisioningPlace(true);
		}

		public String getToken(ListAccountProvisioningPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	AccountProvisioningQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListAccountProvisioningPlace(boolean listStale) {
		GWT.log("List AccountProvisioning place");
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

	public AccountProvisioningQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(AccountProvisioningQueryFilterPojo filter) {
		this.filter = filter;
	}
}
