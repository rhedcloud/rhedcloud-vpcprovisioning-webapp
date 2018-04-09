package edu.emory.oit.vpcprovisioning.presenter.firewall;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;

public class MaintainFirewallExceptionRequestPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainFirewallExceptionRequest")
	public static class Tokenizer implements PlaceTokenizer<MaintainFirewallExceptionRequestPlace> {

		private static final String NO_ID = "createFirewallExceptionRequest";

		public MaintainFirewallExceptionRequestPlace getPlace(String token) {
			if (token != null) {
				return new MaintainFirewallExceptionRequestPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainFirewallExceptionRequestPlace.getMaintainFirewallExceptionRequestPlace();
			}
		}

		public String getToken(MaintainFirewallExceptionRequestPlace place) {
			String name = place.getFirewallExceptionRequestId();
			return (name == null) ? NO_ID : name;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainFirewallExceptionRequestPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainFirewallExceptionRequestPlace createMaintainFirewallExceptionRequestPlace(FirewallExceptionRequestPojo firewallExceptionRequest) {
		return new MaintainFirewallExceptionRequestPlace(firewallExceptionRequest.getSystemId(), firewallExceptionRequest);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainFirewallExceptionRequestPlace getMaintainFirewallExceptionRequestPlace() {
		if (singleton == null) {
			singleton = new MaintainFirewallExceptionRequestPlace(null, null);
		}
		return singleton;
	}

	private final FirewallExceptionRequestPojo firewallExceptionRequest;
	private final String systemId;
	public String getFirewallExceptionRequestId() {
		return systemId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainFirewallExceptionRequestPlace(String systemId, FirewallExceptionRequestPojo firewallExceptionRequest) {
		this.systemId = systemId;
		this.firewallExceptionRequest = firewallExceptionRequest;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public FirewallExceptionRequestPojo getFirewallExceptionRequest() {
		return firewallExceptionRequest;
	}
}
