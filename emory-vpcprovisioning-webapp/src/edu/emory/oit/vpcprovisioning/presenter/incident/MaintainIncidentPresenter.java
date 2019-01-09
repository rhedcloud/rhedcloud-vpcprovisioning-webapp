package edu.emory.oit.vpcprovisioning.presenter.incident;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainIncidentPresenter extends PresenterBase implements MaintainIncidentView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private IncidentPojo incident;
	private IncidentRequisitionPojo incidentRequisition;
	private String incidentNumber;
	private UserAccountPojo userLoggedIn;
	private AccountPojo account;
	private DialogBox incidentDialog;
	
	private String urgency;
	private String impact;
	private String businessService;
	private String category;
	private String subCategory;
	private String recordType;
	private String contactType;
	private String cmdbCi;
	private String assignmentGroup;
	private String shortDescription;
	private String incidentType;


	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainIncidentPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.incidentRequisition = null;
		this.incident = null;
		this.incidentNumber = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainIncidentPresenter(ClientFactory clientFactory, IncidentPojo incident) {
		this.isEditing = true;
		this.incidentNumber = incident.getNumber();
		this.clientFactory = clientFactory;
		this.incident = incident;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		getView().showPleaseWaitDialog("Retrieving Incident Details...");
		setReleaseInfo(clientFactory);
		
		if (incidentNumber == null) {
			clientFactory.getShell().setSubTitle("Generate Incident");
			startGenerate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Incident");
			startEdit();
		}
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Incidents.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().hidePleaseWaitDialog();
				getView().setInitialFocus();
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startGenerate() {
		GWT.log("Maintain incident: generate");
		isEditing = false;
		getView().setEditing(false);
		getView().showGenerateWidgets();
		incidentRequisition = new IncidentRequisitionPojo();
		incidentRequisition.setShortDescription(shortDescription);
		incidentRequisition.setUrgency(urgency);
		incidentRequisition.setImpact(impact);
		incidentRequisition.setBusinessService(businessService);
		incidentRequisition.setCategory(category);
		incidentRequisition.setSubCategory(subCategory);
		incidentRequisition.setRecordType(recordType);
		incidentRequisition.setContactType(contactType);
		incidentRequisition.setCmdbCi(cmdbCi);
		incidentRequisition.setAssignmentGroup(assignmentGroup);
	}

	private void startEdit() {
		GWT.log("Maintain incident: edit");
		isEditing = true;
		getView().setEditing(true);
		getView().showEditWidgets();
		// Lock the display until the incident is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainIncidentView().setLocked(false);
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
	public void deleteIncident() {
		if (isEditing) {
			doDeleteIncident();
		} else {
			doCancelIncident();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelIncident() {
		ActionEvent.fire(eventBus, ActionNames.ACCOUNT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteIncident() {
		if (incident == null) {
			return;
		}

		// TODO Delete the incident on server then fire onIncidentDeleted();
	}

	@Override
	public void saveIncident() {
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().setFieldViolations(false);
			getView().resetFieldStyles();
		}

		if (incidentType.equals(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT)) {
			VpcpConfirm.confirm(
					MaintainIncidentPresenter.this, 
					"Confirm Terminate Account", 
					"Terminating an account is a permanent act.  When an account is deleted, it will "
					+ "no longer be accessible.  Are you sure you want to terminate this account?");
		}
		else {
			doSave();
		}
	}
	
	private void doSave() {
		AsyncCallback<IncidentPojo> callback = new AsyncCallback<IncidentPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Incident", caught);
				getView().showMessageToUser("The incident was NOT saved successfully.  "
					+ "Message from the server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(IncidentPojo result) {
				getView().hidePleaseWaitDialog();
				GWT.log("IncidentPojo is: " + result);
				getView().showMessageToUser("An Incident has been created for you.  "
					+ "The incident number for your reference is: <b>" + result.getNumber() 
					+ "</b>  The incident has been assigned to the group: <b>" + result.getAssignmentGroup()
					+ "</b> and is in the following state:  <b>" + result.getIncidentState() + "</b>");
				incidentDialog.hide();
			}
		};
		if (!isEditing) {
			getView().showPleaseWaitDialog("Generating an Incident...");
			incidentRequisition.setCallerId(userLoggedIn.getPrincipal());
			GWT.log(incidentRequisition.getDescription());
			VpcProvisioningService.Util.getInstance().generateIncident(incidentRequisition, callback);
		}
		else {
			getView().showPleaseWaitDialog("Saving the Incident...");
			VpcProvisioningService.Util.getInstance().updateIncident(incident, callback);
		}
	}

	@Override
	public IncidentPojo getIncident() {
		return this.incident;
	}

	public MaintainIncidentView getView() {
		return clientFactory.getMaintainIncidentView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getNumber() {
		return incidentNumber;
	}

	public void setNumber(String number) {
		this.incidentNumber = number;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setIncident(IncidentPojo incident) {
		this.incident = incident;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	@Override
	public boolean isValidIncidentNumber(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IncidentRequisitionPojo getIncidentRequisition() {
		return incidentRequisition;
	}

	@Override
	public void setAccount(AccountPojo account) {
		this.account = account;
	}

	@Override
	public AccountPojo getAccount() {
		return this.account;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public void setBusinessService(String businessService) {
		this.businessService = businessService;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public void setCmdbCi(String cmdbCi) {
		this.cmdbCi = cmdbCi;
	}

	public void setAssignmentGroup(String assignmentGroup) {
		this.assignmentGroup = assignmentGroup;
	}

	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@Override
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	@Override
	public String getIncidentType() {
		return this.incidentType;
	}

	@Override
	public void vpcpConfirmOkay() {
		doSave();
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Account " + 
				account.getAccountId() + "/" + account.getAccountName() + " was not terminated.");
	}

	@Override
	public void setIncidentDialog(DialogBox incidentDialog) {
		this.incidentDialog = incidentDialog;
	}

	@Override
	public DialogBox getIncidentDialog() {
		return this.incidentDialog;
	}

}
