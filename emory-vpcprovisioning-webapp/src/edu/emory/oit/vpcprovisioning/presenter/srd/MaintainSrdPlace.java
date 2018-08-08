package edu.emory.oit.vpcprovisioning.presenter.srd;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;

public class MaintainSrdPlace extends Place {

	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainSrd")
	public static class Tokenizer implements PlaceTokenizer<MaintainSrdPlace> {

		private static final String NO_ID = "createSrd";

		public MaintainSrdPlace getPlace(String token) {
			if (token != null) {
				return new MaintainSrdPlace(token);
			}
			else {
				// If the ID cannot be parsed, assume we are creating
				return new MaintainSrdPlace(null);
			}
		}

		public String getToken(MaintainSrdPlace place) {
			String srdId = place.getSrdId();
			return (srdId == null) ? NO_ID : srdId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainSrdPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainSrdPlace createMaintainSrdPlace(AccountNotificationPojo accountNotification, SecurityRiskDetectionPojo srd) {
		MaintainSrdPlace p = new MaintainSrdPlace(srd.getSecurityRiskDetectionId());
		p.setAccountNotification(accountNotification);
		p.setSrd(srd);
		return p;
	}
	public static MaintainSrdPlace createMaintainSrdPlace(UserNotificationPojo userNotification, SecurityRiskDetectionPojo srd) {
		MaintainSrdPlace p = new MaintainSrdPlace(srd.getSecurityRiskDetectionId());
		p.setUserNotification(userNotification);
		p.setSrd(srd);
		return p;
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainSrdPlace getMaintainSrdPlace(AccountNotificationPojo accountNotification, SecurityRiskDetectionPojo srd) {
		if (singleton == null) {
			singleton = new MaintainSrdPlace(srd.getSecurityRiskDetectionId());
			singleton.setSrd(srd);
		}
		if (singleton.getAccountNotification() == null) {
			singleton.setAccountNotification(accountNotification);
		}
		return singleton;
	}
	public static MaintainSrdPlace getMaintainSrdPlace(UserNotificationPojo userNotification, SecurityRiskDetectionPojo srd) {
		if (singleton == null) {
			singleton = new MaintainSrdPlace(srd.getSecurityRiskDetectionId());
			singleton.setSrd(srd);
		}
		if (singleton.getUserNotification() == null) {
			singleton.setUserNotification(userNotification);
		}
		return singleton;
	}

	AccountNotificationPojo accountNotification;
	UserNotificationPojo userNotification;
	SecurityRiskDetectionPojo srd;
	String srdId;
	
	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainSrdPlace(String srdId) {
		this.srdId = srdId;
		this.srd = null;
		this.accountNotification = null;
		this.userNotification = null;
	}

	public AccountNotificationPojo getAccountNotification() {
		return accountNotification;
	}

	public void setAccountNotification(AccountNotificationPojo accountNotification) {
		this.accountNotification = accountNotification;
	}

	public UserNotificationPojo getUserNotification() {
		return userNotification;
	}

	public void setUserNotification(UserNotificationPojo userNotification) {
		this.userNotification = userNotification;
	}

	public String getSrdId() {
		return srdId;
	}

	public void setSrdId(String srdId) {
		this.srdId = srdId;
	}
	public SecurityRiskDetectionPojo getSrd() {
		return srd;
	}
	public void setSrd(SecurityRiskDetectionPojo srd) {
		this.srd = srd;
	}
}
