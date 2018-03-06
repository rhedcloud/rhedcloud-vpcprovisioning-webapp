package edu.emory.oit.vpcprovisioning.presenter.firewall;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;

public class MaintainFirewallRulePlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainFirewallRule")
	public static class Tokenizer implements PlaceTokenizer<MaintainFirewallRulePlace> {

		private static final String NO_ID = "createFirewallRule";

		public MaintainFirewallRulePlace getPlace(String token) {
			if (token != null) {
				return new MaintainFirewallRulePlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainFirewallRulePlace.getMaintainFirewallRulePlace();
			}
		}

		public String getToken(MaintainFirewallRulePlace place) {
			String name = place.getFirewallRuleId();
			return (name == null) ? NO_ID : name;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainFirewallRulePlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainFirewallRulePlace createMaintainFirewallRulePlace(FirewallRulePojo firewallRule) {
		return new MaintainFirewallRulePlace(firewallRule.getName(), firewallRule);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainFirewallRulePlace getMaintainFirewallRulePlace() {
		if (singleton == null) {
			singleton = new MaintainFirewallRulePlace(null, null);
		}
		return singleton;
	}

	private final FirewallRulePojo firewallRule;
	private final String name;
	public String getFirewallRuleId() {
		return name;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainFirewallRulePlace(String name, FirewallRulePojo firewallRule) {
		this.name = name;
		this.firewallRule = firewallRule;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public FirewallRulePojo getFirewallRule() {
		return firewallRule;
	}
}
