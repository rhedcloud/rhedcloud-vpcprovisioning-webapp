package edu.emory.oit.vpcprovisioning.presenter.role;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryFilterPojo;

public class ListRoleProvisioningPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_ROLE_PROVISIONING)
	public static class Tokenizer implements PlaceTokenizer<ListRoleProvisioningPlace> {

		public ListRoleProvisioningPlace getPlace(String token) {
			return new ListRoleProvisioningPlace(true);
		}

		public String getToken(ListRoleProvisioningPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	RoleProvisioningQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListRoleProvisioningPlace(boolean listStale) {
		GWT.log("Role Provisioning place");
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

	public RoleProvisioningQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(RoleProvisioningQueryFilterPojo filter) {
		this.filter = filter;
	}
}
