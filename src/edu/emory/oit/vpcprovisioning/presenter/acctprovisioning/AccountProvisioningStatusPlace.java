package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;

public class AccountProvisioningStatusPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("accountProvisioningStatus")
	public static class Tokenizer implements PlaceTokenizer<AccountProvisioningStatusPlace> {

		private static final String NO_ID = "accountProvisioningStatus";

		public AccountProvisioningStatusPlace getPlace(String token) {
			if (token != null) {
				return new AccountProvisioningStatusPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return AccountProvisioningStatusPlace.getVpncpStatusPlace();
			}
		}

		public String getToken(AccountProvisioningStatusPlace place) {
			String provisioningId = place.getProvisioningId();
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static AccountProvisioningStatusPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static AccountProvisioningStatusPlace createAccountProvisioningStatusPlace(AccountProvisioningSummaryPojo summary) {
		if (summary.isProvision()) {
			return new AccountProvisioningStatusPlace(summary.getProvisioning().getProvisioningId(), summary);
		}
		else {
			return new AccountProvisioningStatusPlace(summary.getDeprovisioning().getDeprovisioningId(), summary);
		}
	}

	public static AccountProvisioningStatusPlace createAccountProvisioningStatusPlaceFromGenerate(AccountProvisioningSummaryPojo summary, boolean fromProvisioningList) {
		if (summary.isProvision()) {
			return new AccountProvisioningStatusPlace(summary.getProvisioning().getProvisioningId(), summary, true, fromProvisioningList);
		}
		else {
			return new AccountProvisioningStatusPlace(summary.getDeprovisioning().getDeprovisioningId(), summary, true, fromProvisioningList);
		}
	}
	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static AccountProvisioningStatusPlace getVpncpStatusPlace() {
		if (singleton == null) {
			singleton = new AccountProvisioningStatusPlace(null, null);
		}
		return singleton;
	}

	private final AccountProvisioningSummaryPojo summary;
	private final String provisioningId;
	private final boolean fromGenerate;
	private final boolean fromProvisioningList;
	public String getProvisioningId() {
		return provisioningId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private AccountProvisioningStatusPlace(String provisioningId, AccountProvisioningSummaryPojo summary) {
		this.provisioningId = provisioningId;
		this.summary = summary;
		this.fromGenerate = false;
		this.fromProvisioningList = false;
	}

	private AccountProvisioningStatusPlace(String provisioningId, AccountProvisioningSummaryPojo summary, boolean fromGenerate, boolean fromProvisioningList) {
		this.provisioningId = provisioningId;
		this.summary = summary;
		this.fromGenerate = fromGenerate;
		this.fromProvisioningList = fromProvisioningList;
	}
	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public AccountProvisioningSummaryPojo getAccountProvisioningSummary() {
		return summary;
	}
	public boolean isFromGenerate() {
		return this.fromGenerate;
	}

	public boolean isFromProvisioningList() {
		return fromProvisioningList;
	}
}
