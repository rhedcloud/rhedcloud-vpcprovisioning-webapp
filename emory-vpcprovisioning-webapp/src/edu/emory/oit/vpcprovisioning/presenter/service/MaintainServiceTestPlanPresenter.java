package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.ServiceTestListUpdateEvent;
import edu.emory.oit.vpcprovisioning.client.event.ServiceTestRequirementListUpdateEvent;
import edu.emory.oit.vpcprovisioning.client.event.ServiceTestStepListUpdateEvent;
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
	private ServiceTestRequirementPojo selectedRequirement;
	private ServiceTestPojo selectedTest;
	private ServiceTestStepPojo selectedStep;
	boolean isDeletingRequirement=false;
	boolean isDeletingTest=false;
	boolean isDeletingStep=false;

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
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		setReleaseInfo(clientFactory);
		getView().applyAWSAccountAuditorMask();
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
				refreshRequirementList(userLoggedIn);
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain service testPlan: create");
		isEditing = false;
		getView().setEditing(false);
		serviceTestPlan = new ServiceTestPlanPojo();
		getSecurityAssessment().setServiceTestPlan(serviceTestPlan);
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
	public void saveAssessment(final boolean addAnother) {
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
								"<p>Message from server is: " + caught.getMessage() + "</p>");
					}

					@Override
					public void onSuccess(ServiceSecurityAssessmentQueryResultPojo result) {
						GWT.log("Got " + result.getResults().size() + " Security Assessments for " + result.getFilterUsed());
						assessmentLoop: for (ServiceSecurityAssessmentPojo ssa : result.getResults()) {
							if (ssa.getServiceSecurityAssessmentId().equals(assessment.getServiceSecurityAssessmentId())) {
								GWT.log("Re-hydrating assessment and test plan...");
								assessment = ssa;
								serviceTestPlan = assessment.getServiceTestPlan();
								break assessmentLoop;
							}
						}
						if (addAnother) {
							// if boolean passed in is true, don't fire the MAINTAIN_SECURITY_ASSESSMENT event, 
							// instead, just re-hydrate the selected requirement, test, step from the fresh assessment 
							// and re-show the appropriate requirement, test, step dialog
							if (selectedRequirement != null) {
								// find the matching requirement in the fresh assessment and set selectedRequirement to that
								reqmtLoop: for (ServiceTestRequirementPojo reqmt : assessment.getServiceTestPlan().getServiceTestRequirements()) {
									if (reqmt.getServiceTestRequirementId().equals(selectedRequirement.getServiceTestRequirementId())) {
										GWT.log("Re-hydrating selected requirement and creating a new test...");
										selectedRequirement = reqmt;
										break reqmtLoop;
									}
								}
								if (selectedTest != null) {
									// find the matching test in the selectedRequirement and set selectedTest to that
									// adding another test step
									testLoop: for (ServiceTestPojo test : selectedRequirement.getServiceTests()) {
										if (test.getServiceTestId().equals(selectedTest.getServiceTestId())) {
											GWT.log("Re-hydrating selected test and creating a new step...");
											selectedTest = test;
											break testLoop;
										}
									}
									getView().hidePleaseWaitDialog();
									createStep();
								}
								else {
									// adding another test
									GWT.log("Creating a new test...");
					                getView().hidePleaseWaitDialog();
									createTest();
								}
							}
							else {
								// adding another requirement
								GWT.log("Creating a new requirement...");
				                getView().hidePleaseWaitDialog();
								createRequirement();
							}
						}
						else {
							ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
						}
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
	}

	@Override
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return this.assessment;
	}

	public MaintainServiceTestPlanView getView() {
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

	@Override
	public void requirementSelected() {
		getView().requirementSelected();
	}

	@Override
	public void testSelected() {
		getView().testSelected();
	}

	@Override
	public void stepSelected() {
		getView().stepSelected();
	}

	@Override
	public void refreshRequirementList(UserAccountPojo user) {
		if (serviceTestPlan != null) {
			setRequirementList(this.serviceTestPlan.getServiceTestRequirements());
		}
	}

	private void setRequirementList(List<ServiceTestRequirementPojo> requirements) {
		getView().setRequirements(requirements);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new ServiceTestRequirementListUpdateEvent(requirements), this);
		}
	}

	@Override
	public void refreshTestList(UserAccountPojo user) {
		if (selectedRequirement != null) {
			getView().setTests(selectedRequirement.getServiceTests());
			if (eventBus != null) {
				eventBus.fireEventFromSource(new ServiceTestListUpdateEvent(selectedRequirement.getServiceTests()), this);
			}
		}
		else {
			getView().setTests(Collections.<ServiceTestPojo> emptyList());
			if (eventBus != null) {
				eventBus.fireEventFromSource(new ServiceTestListUpdateEvent(Collections.<ServiceTestPojo> emptyList()), this);
			}
		}
	}

	@Override
	public void refreshStepList(UserAccountPojo user) {
		if (selectedTest != null) {
			getView().setSteps(selectedTest.getServiceTestSteps());
			if (eventBus != null) {
				eventBus.fireEventFromSource(new ServiceTestStepListUpdateEvent(selectedTest.getServiceTestSteps()), this);
			}
		}
		else {
			getView().setSteps(Collections.<ServiceTestStepPojo> emptyList());
			if (eventBus != null) {
				eventBus.fireEventFromSource(new ServiceTestStepListUpdateEvent(Collections.<ServiceTestStepPojo> emptyList()), this);
			}
		}
	}

	@Override
	public void vpcpConfirmOkay() {
		if (isDeletingRequirement) {
			serviceTestPlan.getServiceTestRequirements().remove(selectedRequirement);
			refreshRequirementList(userLoggedIn);
		}
		else if (isDeletingTest) {
			selectedRequirement.getServiceTests().remove(selectedTest);
			refreshTestList(userLoggedIn);
		}
		else {
			selectedTest.getServiceTestSteps().remove(selectedStep);
			refreshStepList(userLoggedIn);
		}
		// save the assessment
		getAssessment().setServiceTestPlan(getServiceTestPlan());
		saveAssessment(false);
	}

	@Override
	public void vpcpConfirmCancel() {
		if (isDeletingRequirement) {
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Requirement " + 
					selectedRequirement.getDescription() + 
					" (Sequence: " + selectedRequirement.getSequenceNumber() + ") was not deleted.");
		}
		else if (isDeletingTest) {
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Test " + 
					selectedTest.getDescription() + 
					" (Sequence: " + selectedTest.getSequenceNumber() + ") was not deleted.");
		}
		else {
			getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Step " + 
					selectedStep.getDescription() + 
					" (Sequence: " + selectedStep.getSequenceNumber() + ") was not deleted.");
		}
	}

	@Override
	public void deleteRequirement(ServiceTestRequirementPojo selected) {
		isDeletingRequirement=true;
		isDeletingTest=false;
		isDeletingStep=false;
		selectedRequirement = selected;
		VpcpConfirm.confirm(
				MaintainServiceTestPlanPresenter.this, 
			"Confirm Delete Service Test Requirement", 
			"Delete the Service Test Requirement " + selectedRequirement.getDescription() + 
				" (Sequence: " + selectedRequirement.getSequenceNumber() + ")" + "?");
	}

	@Override
	public void deleteTest(ServiceTestPojo selected) {
		isDeletingRequirement=false;
		isDeletingTest=true;
		isDeletingStep=false;
		selectedTest = selected;
		VpcpConfirm.confirm(
				MaintainServiceTestPlanPresenter.this, 
			"Confirm Delete Service Test", 
			"Delete the Service Test " + selectedTest.getDescription() + 
				" (Sequence: " + selectedTest.getSequenceNumber() + ")" + "?");
	}

	@Override
	public void deleteStep(ServiceTestStepPojo selected) {
		isDeletingRequirement=false;
		isDeletingTest=false;
		isDeletingStep=true;
		selectedStep = selected;
		VpcpConfirm.confirm(
				MaintainServiceTestPlanPresenter.this, 
			"Confirm Delete Service Test Step", 
			"Delete the Service Test Step " + selectedStep.getDescription() + 
				" (Sequence: " + selectedStep.getSequenceNumber() + ")" + "?");
	}

	@Override
	public void createRequirement() {
		ServiceTestRequirementPojo req = new ServiceTestRequirementPojo();
		req.setSequenceNumber(serviceTestPlan.getServiceTestRequirements().size() + 1);
		getView().showRequirementMaintenanceDialog(false, req);
	}

	@Override
	public void maintainRequirement(ServiceTestRequirementPojo selected) {
		selectedRequirement = selected;
		getView().showRequirementMaintenanceDialog(true, selected);
	}

	@Override
	public void createTest() {
		if (selectedRequirement == null) {
			getView().showMessageToUser("Please select a test requirement from the list before adding a test.");
			return;
		}
		ServiceTestPojo test = new ServiceTestPojo();
		test.setSequenceNumber(selectedRequirement.getServiceTests().size() + 1);
		getView().showTestMaintenanceDialog(false, test);
	}

	@Override
	public void maintainTest(ServiceTestPojo selected) {
		selectedTest = selected;
		getView().showTestMaintenanceDialog(true, selected);
	}

	@Override
	public void createStep() {
		if (selectedTest == null) {
			getView().showMessageToUser("Please select a test from the list before adding a test step.");
			return;
		}
		ServiceTestStepPojo step = new ServiceTestStepPojo();
		step.setSequenceNumber(selectedTest.getServiceTestSteps().size() + 1);
		getView().showStepMaintenanceDialog(false, step);
	}

	@Override
	public void maintainStep(ServiceTestStepPojo selected) {
		selectedStep = selected;
		getView().showStepMaintenanceDialog(true, selected);
	}

	@Override
	public void cancelMaintenance() {
		// just so we refresh the test plan.  this is important when/if user selects
		// cancel but were in the middle of an "add another" workflow but changed their minds
		ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
	}

}
