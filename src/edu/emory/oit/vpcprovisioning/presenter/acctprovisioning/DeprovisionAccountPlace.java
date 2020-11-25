package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountPojo;

public class DeprovisionAccountPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("deprovisionAccount")
	public static class Tokenizer implements PlaceTokenizer<DeprovisionAccountPlace> {

		private static final String NO_ID = "deprovisionAccount";

		public DeprovisionAccountPlace getPlace(String token) {
			if (token != null) {
				List<AccountPojo> accounts = new java.util.ArrayList<AccountPojo>();
				AccountPojo account = new AccountPojo();
				account.setAccountId(token);
				accounts.add(account);
				return new DeprovisionAccountPlace(accounts);
			}
			else {
				return DeprovisionAccountPlace.getMaintainIncidentPlace();
			}
		}

		public String getToken(DeprovisionAccountPlace place) {
			String number = "";
			return (number == null) ? NO_ID : number;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static DeprovisionAccountPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static DeprovisionAccountPlace createMaintainIncidentPlace(List<AccountPojo> accounts) {
		return new DeprovisionAccountPlace(accounts);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static DeprovisionAccountPlace getMaintainIncidentPlace() {
		if (singleton == null) {
			singleton = new DeprovisionAccountPlace(null);
		}
		return singleton;
	}

	private final List<AccountPojo> accounts;

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private DeprovisionAccountPlace(List<AccountPojo> accounts) {
		this.accounts = accounts;
	}

	public List<AccountPojo> getAccounts() {
		return accounts;
	}
}
