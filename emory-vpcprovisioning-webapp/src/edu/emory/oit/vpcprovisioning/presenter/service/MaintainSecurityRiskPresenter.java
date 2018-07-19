package edu.emory.oit.vpcprovisioning.presenter.service;

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
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainSecurityRiskPresenter extends PresenterBase implements MaintainSecurityRiskView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private ServiceSecurityAssessmentPojo assessment;
	private UserAccountPojo userLoggedIn;
	private AWSServicePojo service;
	private SecurityRiskPojo securityRisk;
	private String securityRiskId;
	private DirectoryPersonPojo directoryPerson;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainSecurityRiskPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.isEditing = false;
		this.assessment = assessment;
		this.clientFactory = clientFactory;
		this.service = service;
		this.securityRisk = null;
		this.securityRiskId = null;
		clientFactory.getMaintainSecurityRiskView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainSecurityRiskPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk) {
		this.isEditing = true;
		this.clientFactory = clientFactory;
		this.assessment = assessment;
		this.service = service;
		this.securityRisk = risk;
		this.securityRiskId = securityRisk.getSecurityRiskId();
		clientFactory.getMaintainSecurityRiskView().setPresenter(this);
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
		getView().showPleaseWaitDialog("Retrieving Security Risk information...");
		
		GWT.log("Maintain Security Risk: service is: " + service);
		GWT.log("Maintain Security Risk: assessment is: " + assessment);

		if (securityRiskId == null) {
			clientFactory.getShell().setSubTitle("Create Security Risk");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit Security Risk");
			startEdit();
			// get latest version of the security risk from the server
//			AsyncCallback<ServiceSecurityAssessmentPojo> acct_cb = new AsyncCallback<ServiceSecurityAssessmentPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//					getView().hidePleaseWaitDialog();
//					getView().hidePleaseWaitPanel();
//					GWT.log("Exception retrieving assessment details", caught);
//					getView().showMessageToUser("There was an exception on the " +
//							"server retrieving the details for this assessment.  Message " +
//							"from server is: " + caught.getMessage());
//				}
//
//				@Override
//				public void onSuccess(ServiceSecurityAssessmentPojo result) {
//					assessment = result;
//				}
//			};
//			ServiceSecurityAssessmentQueryFilterPojo filter = new ServiceSecurityAssessmentQueryFilterPojo();
//			VpcProvisioningService.Util.getInstance().getAccountById(assessmentId, acct_cb);
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				GWT.log("Exception retrieving user logged in", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				getView().setCounterMeasures(securityRisk.getCouterMeasures());
				
				AsyncCallback<List<String>> rl_callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
						GWT.log("Exception retrieving risk level types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving status types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().setRiskLevelItems(result);
						getView().initPage();
						getView().setFieldViolations(false);
						getView().setInitialFocus();
						
						// apply authorization mask
						if (user.isCentralAdmin()) {
							getView().applyCentralAdminMask();
						}
						else {
							getView().applyAWSAccountAuditorMask();
						}
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
					}
				};
				// risk level types
				VpcProvisioningService.Util.getInstance().getRiskLevelTypeItems(rl_callback);

				AsyncCallback<List<String>> cm_status_callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
						GWT.log("Exception retrieving counter measure statuses", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving counter measure statuses.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().setCounterMeasureStatusItems(result);
					}
				};
				// counter measure status types
				VpcProvisioningService.Util.getInstance().getCounterMeasureStatusTypeItems(cm_status_callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain security risk: create");
		isEditing = false;
		getView().setEditing(false);
		securityRisk = new SecurityRiskPojo();
	}

	private void startEdit() {
		GWT.log("Maintain security risk: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the assessment is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainSecurityRiskView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private void doCancelSecurityRisk() {
		// go back to the maintain assessment page...
		ActionEvent.fire(eventBus, ActionNames.SECURITY_RISK_EDITING_CANCELED, assessment);
	}

	private void doDeleteSecurityRisk() {
		if (securityRisk == null) {
			return;
		}

		// TODO remove the security risk from the assessment and save the assessment
		
	}

	@Override
	public void saveAssessment() {
		getView().showPleaseWaitDialog("Saving assessment...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().hidePleaseWaitPanel();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().setFieldViolations(false);
			getView().resetFieldStyles();
		}
		AsyncCallback<ServiceSecurityAssessmentPojo> callback = new AsyncCallback<ServiceSecurityAssessmentPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				GWT.log("Exception saving the Account", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Account.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ServiceSecurityAssessmentPojo result) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				ActionEvent.fire(eventBus, ActionNames.SECURITY_ASSESSMENT_SAVED, assessment);
			}
		};
		// it's always an update
		if (!isEditing) {
			this.assessment.getSecurityRisks().add(getSecurityRisk());
		}
		else {
			// TODO: have to find the security risk, remove it and then re-add this one to the list
		}
		VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
	}

	@Override
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return this.assessment;
	}

	public MaintainSecurityRiskView getView() {
		return clientFactory.getMaintainSecurityRiskView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getSecurityRiskId() {
		return securityRiskId;
	}

	public void setSecurityRiskId(String riskId) {
		this.securityRiskId = riskId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSecurityRisk(SecurityRiskPojo selected) {
		if (isEditing) {
			doDeleteSecurityRisk();
		} else {
			doCancelSecurityRisk();
		}
	}

	@Override
	public AWSServicePojo getService() {
		return this.service;
	}

	@Override
	public void setService(AWSServicePojo service) {
		this.service = service;
	}

	@Override
	public void setSecurityAssessment(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}

	@Override
	public SecurityRiskPojo getSecurityRisk() {
		return this.securityRisk;
	}

	@Override
	public void setDirectoryPerson(DirectoryPersonPojo pojo) {
		this.directoryPerson = pojo;
	}

	public DirectoryPersonPojo getDirectoryPerson() {
		return directoryPerson;
	}
}
