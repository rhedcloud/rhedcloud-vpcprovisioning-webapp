package edu.emory.oit.vpcprovisioning.presenter.elasticipassignment;

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
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainElasticIpAssignmentPresenter extends PresenterBase implements MaintainElasticIpAssignmentView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String assignmentId;
	private ElasticIpAssignmentSummaryPojo summary;
	private boolean isRegisteringVpc=false;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new elastic ip assignment
	 */
	public MaintainElasticIpAssignmentPresenter(ClientFactory clientFactory) {
		this.assignmentId = null;
		this.summary = null;
		this.clientFactory = clientFactory;
		this.isRegisteringVpc = false;
		clientFactory.getMaintainElasticIpAssignmentView().setPresenter(this);
	}
	/**
	 * For creating a new elastic ip assignment after VPC registration
	 */
	public MaintainElasticIpAssignmentPresenter(ClientFactory clientFactory, boolean isRegisteringVpc) {
		this.assignmentId = null;
		this.summary = null;
		this.clientFactory = clientFactory;
		this.isRegisteringVpc = isRegisteringVpc;
		clientFactory.getMaintainElasticIpAssignmentView().setPresenter(this);
	}
	/**
	 * For editing an existing elastic ip assignment
	 */
	public MaintainElasticIpAssignmentPresenter(ClientFactory clientFactory, ElasticIpAssignmentSummaryPojo summary) {
		this.assignmentId = summary.getElasticIpAssignment().getAssignmentId();
		this.clientFactory = clientFactory;
		this.summary = summary;
		this.isRegisteringVpc = false;
		clientFactory.getMaintainElasticIpAssignmentView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		
		if (assignmentId == null) {
			clientFactory.getShell().setSubTitle("Create Elastic IP Assignment");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Elastic IP Assignment");
			startEdit();
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
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
						getView().setAccountItems(result.getResults());
						getView().initPage();
						getView().setInitialFocus();
						// apply authorization mask
						if (user.isLitsAdmin()) {
							getView().applyCentralAdminMask();
						}
						else if (summary != null && user.isAdminForAccount(summary.getAccount().getAccountId())) {
							getView().applyAWSAccountAdminMask();
						}
						else if (summary != null && user.isAuditorForAccount(summary.getAccount().getAccountId())) {
							getView().applyAWSAccountAuditorMask();
						}
						else {
							getView().applyAWSAccountAuditorMask();
							getView().showMessageToUser("An error has occurred.  The user logged in does not "
									+ "appear to be associated to any valid roles for this page.");
						}
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
					}
				};
				VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		summary = new ElasticIpAssignmentSummaryPojo();
		summary.setElasticIpAssignment(new ElasticIpAssignmentPojo());
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
		clientFactory.getMaintainElasticIpAssignmentView().setLocked(false);
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
	public void deleteElasticIpAssignment() {
		if (isEditing) {
			doDeleteElasticIpAssignment();
		} else {
			doCancelElasticIpAssignment();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelElasticIpAssignment() {
		ActionEvent.fire(eventBus, ActionNames.ELASTIC_IP_ASSIGNMENT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteElasticIpAssignment() {
		if (summary == null) {
			return;
		}

		// TODO Delete the Elastic IP Assignemnt on the server then fire onElasticIpAssignmentDeleted();
	}

	@Override
	public void saveElasticIpAssignment() {
		getView().showPleaseWaitDialog("Saving Elastic IP Assignment...");
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
		AsyncCallback<ElasticIpAssignmentPojo> callback = new AsyncCallback<ElasticIpAssignmentPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the ElasticIP Assignment", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the ElasticIP Assignment.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ElasticIpAssignmentPojo result) {
				getView().hidePleaseWaitDialog();
				if (!isRegisteringVpc) {
					ActionEvent.fire(eventBus, ActionNames.ELASTIC_IP_ASSIGNMENT_SAVED, null, result);
				}
				else {
					// TODO:  not sure if this will be needed or not...will depend on provisioning flow
//					ActionEvent.fire(eventBus, ActionNames.ELASTIC_IP_ASSIGNMENT_SAVED_AFTER_VPC_REGISTRATION, result);
				}
			}
		};
		if (!this.isEditing) {
			// it's a create
//			VpcProvisioningService.Util.getInstance().generateElasticIpAssignment(summary.getElasticIpAssignment(), callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateElasticIpAssignment(summary.getElasticIpAssignment(), callback);
		}
	}

	@Override
	public ElasticIpAssignmentSummaryPojo getElasticIpAssignmentSummary() {
		return this.summary;
	}

	@Override
	public boolean isValidElasticIp(ElasticIpPojo value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}

	@Override
	public ClientFactory getClientFactory() {
		return this.clientFactory;
	}

	@Override
	public void getVpcsForAccount(String accountId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUnassigedElasticIps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRegisteringVpc() {
		return this.isRegisteringVpc;
	}

	@Override
	public void setRegisteringVpc(boolean isRegistering) {
		this.isRegisteringVpc = isRegistering;
	}

	private MaintainElasticIpAssignmentView getView() {
		return clientFactory.getMaintainElasticIpAssignmentView();
	}
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public ElasticIpAssignmentSummaryPojo getSummary() {
		return summary;
	}
	public void setSummary(ElasticIpAssignmentSummaryPojo summary) {
		this.summary = summary;
	}
	public boolean isEditing() {
		return isEditing;
	}
	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

}
