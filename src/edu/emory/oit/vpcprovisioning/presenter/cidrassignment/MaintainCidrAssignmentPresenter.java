package edu.emory.oit.vpcprovisioning.presenter.cidrassignment;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class MaintainCidrAssignmentPresenter extends PresenterBase implements MaintainCidrAssignmentView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String assignmentId;
	private CidrPojo cidr;
	private CidrAssignmentSummaryPojo cidrAssignmentSummary;
	private boolean isRegisteringVpc=false;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new case cidr assignment
	 */
	public MaintainCidrAssignmentPresenter(ClientFactory clientFactory) {
		this.assignmentId = null;
		this.cidrAssignmentSummary = null;
		this.cidr = null;
		this.clientFactory = clientFactory;
		this.isRegisteringVpc = false;
		clientFactory.getMaintainCidrAssignmentView().setPresenter(this);
	}

	/**
	 * For creating a new case cidr assignment after VPC registration
	 */
	public MaintainCidrAssignmentPresenter(ClientFactory clientFactory, boolean isRegisteringVpc, CidrPojo cidr) {
		this.assignmentId = null;
		this.cidrAssignmentSummary = null;
		this.cidr = cidr;
		this.clientFactory = clientFactory;
		this.isRegisteringVpc = isRegisteringVpc;
		clientFactory.getMaintainCidrAssignmentView().setPresenter(this);
	}
	/**
	 * For editing an existing cidr assignment.
	 */
	public MaintainCidrAssignmentPresenter(ClientFactory clientFactory, CidrPojo cidr, CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		/*
		 * TODO surely we can find a way to show the read-only values while waiting
		 * for the async fetch
		 */
		this.assignmentId = cidrAssignmentSummary.getCidrAssignment().getCidrAssignmentId();
		this.clientFactory = clientFactory;
		this.cidrAssignmentSummary = cidrAssignmentSummary;
		this.cidr = cidr;
		this.isRegisteringVpc = false;
		clientFactory.getMaintainCidrAssignmentView().setPresenter(this);
	}
	
	/**
	 * constructor for adding assignment to selected cidr (from cidr list view) 
	 */
	public MaintainCidrAssignmentPresenter(ClientFactory clientFactory, CidrPojo cidr) {
		this.clientFactory = clientFactory;
		this.isRegisteringVpc = false;
		this.cidr = cidr;
		clientFactory.getMaintainCidrAssignmentView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		
		if (assignmentId == null) {
			clientFactory.getShell().setSubTitle("Create CIDR Assignment");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit CIDR Assignment");
			startEdit();
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Central Admins you're associated to.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				// get all accounts and populate list box with them
				AsyncCallback<AccountQueryResultPojo> callback = new AsyncCallback<AccountQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
						GWT.log("Exception retrieving AWS accounts", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving a list of AWS Accounts.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(AccountQueryResultPojo result) {
						GWT.log("presenter, got " + result.getResults().size() + " accounts back from server");
						getView().setAccountItems(result.getResults());
						getView().initPage();
						getView().setInitialFocus();
						// apply authorization mask
						if (user.isCentralAdmin()) {
							getView().applyCentralAdminMask();
						}
						else if (cidrAssignmentSummary != null) {
							if (cidrAssignmentSummary.getAccount() != null && user.isAdminForAccount(cidrAssignmentSummary.getAccount().getAccountId())) {
								getView().applyAWSAccountAdminMask();
							}
							else if (cidrAssignmentSummary.getAccount() != null && user.isAuditorForAccount(cidrAssignmentSummary.getAccount().getAccountId())) {
								getView().applyAWSAccountAuditorMask();
							}
							else {
								getView().showMessageToUser("An error has occurred.  The user logged in does not "
										+ "appear to be associated to any valid roles for this page.");
								getView().applyAWSAccountAuditorMask();
								// TODO: need to not show them this page??
							}
						}
						else {
							
						}
						
						// do this so the CIDR we're working with will be added to the list of 
						// available CIDRs and we don't have to go get the entire list of 
						// all unassigned CIDRs
						if (cidr != null) {
							List<CidrPojo> cidrs = new java.util.ArrayList<CidrPojo>();
							cidrs.add(cidr);
							getView().setCidrItems(cidrs);
						}
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
					}
				};
				AccountQueryFilterPojo acct_filter = new AccountQueryFilterPojo();
				acct_filter.setUserLoggedIn(user);
				VpcProvisioningService.Util.getInstance().getAccountsForFilter(acct_filter, callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		cidrAssignmentSummary = new CidrAssignmentSummaryPojo();
		cidrAssignmentSummary.setCidrAssignment(new CidrAssignmentPojo());
	}

	private void startEdit() {
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the cidr assignment is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainCidrAssignmentView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void deleteCidrAssignment() {
		if (isEditing) {
			doDeleteCidrAssignment();
		} else {
			doCancelCidrAssignment();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelCidrAssignment() {
		ActionEvent.fire(eventBus, ActionNames.CIDR_ASSIGNMENT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteCidrAssignment() {
		if (cidrAssignmentSummary == null) {
			return;
		}

		// TODO Delete the CIDR on the server then fire onCidrDeleted();
	}

	@Override
	public void saveCidrAssignment() {
		getView().showPleaseWaitDialog("Saving CIDR Assignment...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
		}
		AsyncCallback<CidrAssignmentPojo> callback = new AsyncCallback<CidrAssignmentPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Cidr", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the CIDR.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(CidrAssignmentPojo result) {
				getView().hidePleaseWaitDialog();
				if (!isRegisteringVpc) {
					ActionEvent.fire(eventBus, ActionNames.CIDR_ASSIGNMENT_SAVED, result);
				}
				else {
					ActionEvent.fire(eventBus, ActionNames.CIDR_ASSIGNMENT_SAVED_AFTER_VPC_REGISTRATION, result);
				}
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createCidrAssignment(cidrAssignmentSummary.getCidrAssignment(), callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateCidrAssignment(cidrAssignmentSummary.getCidrAssignment(), callback);
		}
	}

	@Override
	public boolean isValidCidr(CidrPojo cidr) {
		
		return false;
	}

	public MaintainCidrAssignmentView getView() {
		return clientFactory.getMaintainCidrAssignmentView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void getVpcsForAccount(String accountId) {
		// get vpcs for the accountid passed in
		getView().showPleaseWaitDialog("Retrieving VPCs for the Account...");
		AsyncCallback<List<VpcPojo>> callback = new AsyncCallback<List<VpcPojo>>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception retrieving AWS accounts", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving a list of AWS Accounts.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<VpcPojo> results) {
				getView().hidePleaseWaitDialog();
				getView().setVpcItems(results);
			}
		};
		VpcProvisioningService.Util.getInstance().getVpcsForAccount(accountId, callback);
	}

	@Override
	public void getUnassigedCidrs() {
		// get all unassigned cidrs
		getView().showPleaseWaitDialog("Retrieving unassigned CIDRs...");
		AsyncCallback<List<CidrPojo>> callback = new AsyncCallback<List<CidrPojo>>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception retrieving AWS accounts", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving a list of AWS Accounts.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<CidrPojo> results) {
				getView().hidePleaseWaitDialog();
				getView().setCidrItems(results);
			}
		};
		VpcProvisioningService.Util.getInstance().getUnassignedCidrs(callback);
	}

	@Override
	public CidrAssignmentSummaryPojo getCidrAssignmentSummary() {
		return this.cidrAssignmentSummary;
	}

	public void setCidrAssignmentSummary(CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		this.cidrAssignmentSummary = cidrAssignmentSummary;
	}

	@Override
	public boolean isRegisteringVpc() {
		return isRegisteringVpc;
	}

	@Override
	public void setRegisteringVpc(boolean isRegistering) {
		isRegisteringVpc = isRegistering;		
	}

	public CidrPojo getCidr() {
		return cidr;
	}

	public void setCidr(CidrPojo cidr) {
		this.cidr = cidr;
	}
}
