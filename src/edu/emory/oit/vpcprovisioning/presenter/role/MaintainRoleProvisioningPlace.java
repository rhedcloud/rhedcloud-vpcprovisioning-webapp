package edu.emory.oit.vpcprovisioning.presenter.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningRequisitionPojo;

public class MaintainRoleProvisioningPlace extends Place {
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("maintainRoleProvisioning")
	public static class Tokenizer implements PlaceTokenizer<MaintainRoleProvisioningPlace> {

		private static final String NO_ID = "generateRoleProvisioning";

		public MaintainRoleProvisioningPlace getPlace(String token) {
			if (token != null) {
				return new MaintainRoleProvisioningPlace(token, null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return MaintainRoleProvisioningPlace.getMaintainRoleProvisioningPlace();
			}
		}

		public String getToken(MaintainRoleProvisioningPlace place) {
			String provisioningId = place.getProvisioningId();
			return (provisioningId == null) ? NO_ID : provisioningId;
		}
	}

	/**
	 * The singleton instance of this place used for creation.
	 */
	private static MaintainRoleProvisioningPlace singleton;

	/**
	 * Create an instance of {@link AddCaseRecordPlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static MaintainRoleProvisioningPlace createMaintainRoleProvisioningPlace(RoleProvisioningPojo roleProvisioningp) {
		return new MaintainRoleProvisioningPlace(roleProvisioningp.getProvisioningId(), roleProvisioningp);
	}

	public static Place createMaintainRoleProvisioningPlace(RoleProvisioningRequisitionPojo roleProvisioningRequisition) {
		return new MaintainRoleProvisioningPlace(roleProvisioningRequisition);
	}

	public static Place createMaintainRoleProvisioningPlace() {
		return new MaintainRoleProvisioningPlace();
	}

	public static Place createMaintainRoleProvisioningPlace(DirectoryPersonPojo person, AccountPojo account) {
		return new MaintainRoleProvisioningPlace(person, account);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static MaintainRoleProvisioningPlace getMaintainRoleProvisioningPlace() {
		if (singleton == null) {
			singleton = new MaintainRoleProvisioningPlace();
		}
		return singleton;
	}

	private final DirectoryPersonPojo assignee;
	private final AccountPojo account;
	private final RoleProvisioningPojo roleProvisioningp;
	private final RoleProvisioningRequisitionPojo roleProvisioningRequisition;
	private final String provisioningId;
	public String getProvisioningId() {
		return provisioningId;
	}

	/**
	 * Construct a new {@link AddCaseRecordPlace} for the specified caseRecord id.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 */
	private MaintainRoleProvisioningPlace(DirectoryPersonPojo directoryPerson, AccountPojo account) {
		this.roleProvisioningRequisition = new RoleProvisioningRequisitionPojo();
		this.roleProvisioningRequisition.setAccountId(account.getAccountId());
		this.provisioningId = null;
		this.roleProvisioningp = null;
		this.account = account;
		this.assignee = directoryPerson;
	}
	private MaintainRoleProvisioningPlace() {
		this.roleProvisioningRequisition = null;
		this.provisioningId = null;
		this.roleProvisioningp = null;
		this.account = null;
		this.assignee = null;
	}
	private MaintainRoleProvisioningPlace(String provisioningId, RoleProvisioningPojo roleProvisioningp) {
		this.roleProvisioningRequisition = null;
		this.provisioningId = provisioningId;
		this.roleProvisioningp = roleProvisioningp;
		this.account = null;
		this.assignee = null;
	}
	private MaintainRoleProvisioningPlace(RoleProvisioningRequisitionPojo requisition) {
		this.roleProvisioningRequisition = requisition;
		this.provisioningId = null;
		this.roleProvisioningp = null;
		this.account = null;
		this.assignee = null;
	}

	/**
	 * Get the caseRecord to edit.
	 * 
	 * @return the caseRecord to edit, or null if not available
	 */
	public RoleProvisioningPojo getRoleProvisioning() {
		return roleProvisioningp;
	}
	public RoleProvisioningRequisitionPojo getRoleProvisioningRequisition() {
		return roleProvisioningRequisition;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public DirectoryPersonPojo getAssignee() {
		return assignee;
	}
}
