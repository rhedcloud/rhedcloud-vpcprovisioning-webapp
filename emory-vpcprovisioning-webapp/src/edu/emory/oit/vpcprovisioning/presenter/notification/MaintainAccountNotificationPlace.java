package edu.emory.oit.vpcprovisioning.presenter.notification;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;

public class MaintainAccountNotificationPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainAcctNotification")
	public static class Tokenizer implements PlaceTokenizer<MaintainAccountNotificationPlace> {

		private static final String NO_ID = "createAcctNotification";

		public MaintainAccountNotificationPlace getPlace(String token) {
			if (token != null) {
				return new MaintainAccountNotificationPlace(null, token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainAccountNotificationPlace.getMaintainAccountNotificationPlace(null);
			}
		}

		public String getToken(MaintainAccountNotificationPlace place) {
			String notificationId = place.getNotificationId();
			return (notificationId == null) ? NO_ID : notificationId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainAccountNotificationPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainAccountNotificationPlace createMaintainAccountNotificationPlace(AccountPojo account, AccountNotificationPojo notification) {
		return new MaintainAccountNotificationPlace(account, notification.getAccountNotificationId(), notification);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainAccountNotificationPlace getMaintainAccountNotificationPlace(AccountPojo account) {
		if (singleton == null) {
			singleton = new MaintainAccountNotificationPlace(account, null, null);
		}
		return singleton;
	}

	private final AccountNotificationPojo notification;
	private final AccountPojo account;
	private final String notificationId;
	public String getNotificationId() {
		return notificationId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainAccountNotificationPlace(AccountPojo account, String notificationId, AccountNotificationPojo notification) {
		this.notificationId = notificationId;
		this.notification = notification;
		this.account = account;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public AccountNotificationPojo getNotification() {
		return notification;
	}
	public AccountPojo getAccount() {
		return account;
	}
}
