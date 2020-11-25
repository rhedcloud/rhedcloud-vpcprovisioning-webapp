package edu.emory.oit.vpcprovisioning.presenter.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;

public class RoleProvisioningStatusPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("roleProvisioningStatus")
	public static class Tokenizer implements PlaceTokenizer<RoleProvisioningStatusPlace> {

		private static final String NO_ID = "roleProvisioningStatus";

		public RoleProvisioningStatusPlace getPlace(String token) {
			if (token != null) {
				return new RoleProvisioningStatusPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return RoleProvisioningStatusPlace.getRoleProvisioningStatusPlace();
			}
		}

		public String getToken(RoleProvisioningStatusPlace place) {
			String provisioningId = place.getProvisioningId();
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static RoleProvisioningStatusPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static RoleProvisioningStatusPlace createRoleProvisioningStatusPlace(RoleProvisioningSummaryPojo roleProvisioningSummary) {
		if (roleProvisioningSummary.getProvisioning() == null) {
			roleProvisioningSummary.setProvisioning(new RoleProvisioningPojo());
			roleProvisioningSummary.getProvisioning().setProvisioningId(roleProvisioningSummary.getDeprovisioning().getDeprovisioningId());
		}
		if (roleProvisioningSummary.isProvision()) {
			return new RoleProvisioningStatusPlace(roleProvisioningSummary.getProvisioning().getProvisioningId(), roleProvisioningSummary);
		}
		else {
			return new RoleProvisioningStatusPlace(roleProvisioningSummary.getDeprovisioning().getDeprovisioningId(), roleProvisioningSummary);
		}
	}

	public static RoleProvisioningStatusPlace createRoleProvisioningStatusPlaceFromGenerate(RoleProvisioningSummaryPojo roleProvisioningSummary) {
		if (roleProvisioningSummary.isProvision()) {
			return new RoleProvisioningStatusPlace(roleProvisioningSummary.getProvisioning().getProvisioningId(), roleProvisioningSummary, true);
		}
		else {
			return new RoleProvisioningStatusPlace(roleProvisioningSummary.getDeprovisioning().getDeprovisioningId(), roleProvisioningSummary, true);
		}
	}
	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static RoleProvisioningStatusPlace getRoleProvisioningStatusPlace() {
		if (singleton == null) {
			singleton = new RoleProvisioningStatusPlace(null, null);
		}
		return singleton;
	}

	private final RoleProvisioningSummaryPojo roleProvisioningSummary;
	private final String provisioningId;
	private final boolean fromGenerate;
	public String getProvisioningId() {
		return provisioningId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private RoleProvisioningStatusPlace(String provisioningId, RoleProvisioningSummaryPojo roleProvisioningSummary) {
		this.provisioningId = provisioningId;
		this.roleProvisioningSummary = roleProvisioningSummary;
		this.fromGenerate = false;
	}

	private RoleProvisioningStatusPlace(String provisioningId, RoleProvisioningSummaryPojo roleProvisioningSummary, boolean fromGenerate) {
		this.provisioningId = provisioningId;
		this.roleProvisioningSummary = roleProvisioningSummary;
		this.fromGenerate = fromGenerate;
	}
	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public RoleProvisioningSummaryPojo getRoleProvisioningSummary() {
		return roleProvisioningSummary;
	}
	public boolean isFromGenerate() {
		return this.fromGenerate;
	}
}
