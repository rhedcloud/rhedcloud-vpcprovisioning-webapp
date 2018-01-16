package edu.emory.oit.vpcprovisioning.presenter.account;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountPojo;

public class MaintainAccountPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainAccount")
	public static class Tokenizer implements PlaceTokenizer<MaintainAccountPlace> {

		private static final String NO_ID = "createAccount";

		public MaintainAccountPlace getPlace(String token) {
			if (token != null) {
				return new MaintainAccountPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainAccountPlace.getMaintainAccountPlace();
			}
		}

		public String getToken(MaintainAccountPlace place) {
			String accountId = place.getAccountId();
			return (accountId == null) ? NO_ID : accountId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainAccountPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainAccountPlace createMaintainAccountPlace(AccountPojo account) {
		return new MaintainAccountPlace(account.getAccountId(), account);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainAccountPlace getMaintainAccountPlace() {
		if (singleton == null) {
			singleton = new MaintainAccountPlace(null, null);
		}
		return singleton;
	}

	private final AccountPojo account;
	private final String accountId;
	public String getAccountId() {
		return accountId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainAccountPlace(String accountId, AccountPojo account) {
		this.accountId = accountId;
		this.account = account;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public AccountPojo getAccount() {
		return account;
	}
}
