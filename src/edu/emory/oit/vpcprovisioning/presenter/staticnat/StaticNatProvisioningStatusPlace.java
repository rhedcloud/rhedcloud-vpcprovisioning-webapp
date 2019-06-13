package edu.emory.oit.vpcprovisioning.presenter.staticnat;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;

public class StaticNatProvisioningStatusPlace extends Place {

	/**
	 * The tokenizer for this place.
	 */
	@Prefix("staticNatStatus")
	public static class Tokenizer implements PlaceTokenizer<StaticNatProvisioningStatusPlace> {

		private static final String NO_ID = "staticNatStatus";

		public StaticNatProvisioningStatusPlace getPlace(String token) {
			if (token != null) {
				// TODO: we may need to add a deprovisioningId variable to this place and pass
				if (token.indexOf("DE-") >= 0) {
					String id = token.substring(3, token.length());
					return new StaticNatProvisioningStatusPlace(id, null);
				}
				return new StaticNatProvisioningStatusPlace(token, null);
			}
			else {
				return StaticNatProvisioningStatusPlace.getStaticNatProvisioningStatusPlace();
			}
		}

		public String getToken(StaticNatProvisioningStatusPlace place) {
			String provisioningId=null;
			if (place.getStaticNatDeprovisioning() != null) {
				provisioningId = "DE-" + place.getStaticNatDeprovisioning().getProvisioningId();
			}
			else if (place.getStaticNatProvisioning() != null) {
				provisioningId = place.getStaticNatProvisioning().getProvisioningId();
			}
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static StaticNatProvisioningStatusPlace singleton;

	public static StaticNatProvisioningStatusPlace createStaticNatProvisioningStatusPlace(StaticNatProvisioningSummaryPojo summary) {
		return new StaticNatProvisioningStatusPlace(summary);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static StaticNatProvisioningStatusPlace getStaticNatProvisioningStatusPlace() {
		if (singleton == null) {
			singleton = new StaticNatProvisioningStatusPlace(null);
		}
		return singleton;
	}

	private final String deprovisioningId;
	private final String provisioningId;
	private final StaticNatProvisioningPojo staticNatProvisioning;
	private final StaticNatDeprovisioningPojo staticNatDeprovisioning;
	private final StaticNatProvisioningSummaryPojo summary;

	private StaticNatProvisioningStatusPlace(String provisioningId, StaticNatProvisioningSummaryPojo summary) {
		this.provisioningId = provisioningId;
		this.summary = summary;
		this.staticNatProvisioning = null;
		this.deprovisioningId = null;
		this.staticNatDeprovisioning = null;
	}
	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private StaticNatProvisioningStatusPlace(StaticNatProvisioningSummaryPojo summary) {
		this.summary = summary;
		if (summary.getProvisioned() != null) {
			this.provisioningId = summary.getProvisioned().getProvisioningId();
			this.staticNatProvisioning = summary.getProvisioned();
			this.deprovisioningId = null;
			this.staticNatDeprovisioning = null;
		}
		else {
			this.provisioningId = null;
			this.staticNatProvisioning = null;
			this.deprovisioningId = summary.getDeprovisioned().getProvisioningId();
			this.staticNatDeprovisioning = summary.getDeprovisioned();
		}
	}

	public StaticNatProvisioningPojo getStaticNatProvisioning() {
		return staticNatProvisioning;
	}
	public StaticNatDeprovisioningPojo getStaticNatDeprovisioning() {
		return staticNatDeprovisioning;
	}
	public String getProvisioningId() {
		return provisioningId;
	}
	public String getDeprovisioningId() {
		return deprovisioningId;
	}

	public StaticNatProvisioningSummaryPojo getSummary() {
		return summary;
	}
}
