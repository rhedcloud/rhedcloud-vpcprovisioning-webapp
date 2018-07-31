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
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestRequirementPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainServiceTestPlanPresenter extends PresenterBase implements MaintainServiceTestPlanView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private ServiceSecurityAssessmentPojo assessment;
	private UserAccountPojo userLoggedIn;
	private AWSServicePojo service;
	private ServiceTestPlanPojo serviceTestPlan;
	private MaintainServiceTestPlanView view;
	private ServiceTestRequirementPojo selectedRequirement;
	private ServiceTestPojo selectedTest;
	private ServiceTestStepPojo selectedStep;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new service testPlan.
	 */
	public MaintainServiceTestPlanPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.isEditing = false;
		this.assessment = assessment;
		this.clientFactory = clientFactory;
		this.service = service;
		this.serviceTestPlan = null;
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainServiceTestPlanPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo testPlan) {
		this.isEditing = true;
		this.clientFactory = clientFactory;
		this.assessment = assessment;
		this.service = service;
		this.serviceTestPlan = testPlan;
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
		getView().showPleaseWaitDialog("Retrieving Service Test Plan information...");
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		
		GWT.log("Maintain Service Test Plan: service is: " + service);
		GWT.log("Maintain Service Test Plan: assessment is: " + assessment);

		if (serviceTestPlan == null) {
			clientFactory.getShell().setSubTitle("Create Service Test Plan");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit Service Test Plan");
			startEdit();
			// get latest version of the service testPlan from the server
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
				getView().setFieldViolations(false);
				List<String> expectedResults = new java.util.ArrayList<String>();
				expectedResults.add("Pass");
				expectedResults.add("Fail");
				getView().setTestExpectedResultItems(expectedResults);
				getView().initPage();
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain service testPlan: create");
		isEditing = false;
		getView().setEditing(false);
		serviceTestPlan = new ServiceTestPlanPojo();
	}

	private void startEdit() {
		GWT.log("Maintain service testPlan: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the assessment is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainServiceTestPlanView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private void doCancelServiceTestPlan() {
		// go back to the maintain assessment page...
		ActionEvent.fire(eventBus, ActionNames.SERVICE_TEST_PLAN_EDITING_CANCELED, assessment);
	}

	private void doDeleteServiceTestPlan() {
		if (serviceTestPlan == null) {
			return;
		}

		// TODO remove the service testPlan from the assessment and save the assessment
		
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
				GWT.log("Exception saving the Security Assessment Test Plan", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Security Assessment Test Plan.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ServiceSecurityAssessmentPojo result) {
				// TODO: also need to refresh the assessment associated to the security assessment page
				// not sure how to do that
				
				AsyncCallback<ServiceSecurityAssessmentQueryResultPojo> callback = new AsyncCallback<ServiceSecurityAssessmentQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
		                getView().hidePleaseWaitPanel();
		                getView().hidePleaseWaitDialog();
						GWT.log("Exception Retrieving Services", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving the list of Security Assessments associated to this Service.  " +
								"Message from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(ServiceSecurityAssessmentQueryResultPojo result) {
						GWT.log("Got " + result.getResults().size() + " Security Assessments for " + result.getFilterUsed());
						for (ServiceSecurityAssessmentPojo ssa : result.getResults()) {
							if (ssa.getServiceSecurityAssessmentId().equals(assessment.getServiceSecurityAssessmentId())) {
								assessment = ssa;
								break;
							}
						}
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
						getView().refreshDataProvider();
//						getView().showStatus(getView().getStatusMessageSource(), "Test Plan was saved.");
						ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
					}
				};

				GWT.log("refreshing security assessment...");
				ServiceSecurityAssessmentQueryFilterPojo filter = new ServiceSecurityAssessmentQueryFilterPojo();
				filter.setServiceId(service.getServiceId());
				VpcProvisioningService.Util.getInstance().getSecurityAssessmentsForFilter(filter, callback);
			}
		};
		this.assessment.setServiceTestPlan(this.serviceTestPlan);
		if (!isEditing) {
			getServiceTestPlan().setServiceId(service.getServiceId());
		}
		else {
			// TODO: have to find the service testPlan, remove it and then re-add this one to the list
		}
		// it's always an update
		VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
		// TODO: tempoarary until i get the update logic working correctly
//		getView().refreshDataProvider();
//		getView().showStatus(getView().getStatusMessageSource(), "Test Plan was saved.");
//		getView().hidePleaseWaitDialog();
	}

	@Override
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return this.assessment;
	}

	public MaintainServiceTestPlanView getView() {
//		if (view == null) {
//			view = clientFactory.getMaintainServiceTestPlanView();
//			view.setPresenter(this);
//		}
//		return view;
		
		MaintainServiceTestPlanView v = clientFactory.getMaintainServiceTestPlanView();
		v.setPresenter(this);
		return v;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteServiceTestPlan(ServiceTestPlanPojo selected) {
		if (isEditing) {
			doDeleteServiceTestPlan();
		} else {
			doCancelServiceTestPlan();
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
	public ServiceTestPlanPojo getServiceTestPlan() {
		return this.serviceTestPlan;
	}

	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}

	public void setAssessment(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public void setServiceTestPlan(ServiceTestPlanPojo serviceTestPlan) {
		this.serviceTestPlan = serviceTestPlan;
	}

	@Override
	public ClientFactory getClientFactory() {
		return this.clientFactory;
	}

	@Override
	public void setSelectedTestRequirement(ServiceTestRequirementPojo selected) {
		this.selectedRequirement = selected;
	}

	@Override
	public ServiceTestRequirementPojo getSelectedTestRequirement() {
		return this.selectedRequirement;
	}

	@Override
	public void setSelectedTest(ServiceTestPojo selected) {
		this.selectedTest = selected;
	}

	@Override
	public ServiceTestPojo getSelectedTest() {
		return this.selectedTest;
	}

	@Override
	public void setSelectedTestStep(ServiceTestStepPojo selected) {
		this.selectedStep = selected;
	}

	@Override
	public ServiceTestStepPojo getSelectedTestStep() {
		return this.selectedStep;
	}

}
