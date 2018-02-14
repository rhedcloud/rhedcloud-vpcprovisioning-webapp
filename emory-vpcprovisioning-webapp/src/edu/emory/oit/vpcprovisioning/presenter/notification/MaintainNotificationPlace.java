package edu.emory.oit.vpcprovisioning.presenter.notification;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.NotificationPojo;

public class MaintainNotificationPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainNotification")
	public static class Tokenizer implements PlaceTokenizer<MaintainNotificationPlace> {

		private static final String NO_ID = "createNotification";

		public MaintainNotificationPlace getPlace(String token) {
			if (token != null) {
				return new MaintainNotificationPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainNotificationPlace.getMaintainNotificationPlace();
			}
		}

		public String getToken(MaintainNotificationPlace place) {
			String notificationId = place.getNotificationId();
			return (notificationId == null) ? NO_ID : notificationId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainNotificationPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainNotificationPlace createMaintainNotificationPlace(NotificationPojo notification) {
		return new MaintainNotificationPlace(notification.getNotificationId(), notification);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainNotificationPlace getMaintainNotificationPlace() {
		if (singleton == null) {
			singleton = new MaintainNotificationPlace(null, null);
		}
		return singleton;
	}

	private final NotificationPojo notification;
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
	private MaintainNotificationPlace(String notificationId, NotificationPojo notification) {
		this.notificationId = notificationId;
		this.notification = notification;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public NotificationPojo getNotification() {
		return notification;
	}

}
