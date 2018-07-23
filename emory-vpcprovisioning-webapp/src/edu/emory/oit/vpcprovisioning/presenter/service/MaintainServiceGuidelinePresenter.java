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
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainServiceGuidelinePresenter extends PresenterBase implements MaintainServiceGuidelineView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private ServiceSecurityAssessmentPojo assessment;
	private UserAccountPojo userLoggedIn;
	private AWSServicePojo service;
	private ServiceGuidelinePojo serviceGuideline;
	private DirectoryPersonPojo assessorDirectoryPerson;
	private MaintainServiceGuidelineView view;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new service guideline.
	 */
	public MaintainServiceGuidelinePresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.clientFactory = clientFactory;
		this.isEditing = false;
		this.assessment = assessment;
		this.service = service;
		this.serviceGuideline = null;
	}

	/**
	 * For editing an existing guideline.
	 */
	public MaintainServiceGuidelinePresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo guideline) {
		this.clientFactory = clientFactory;
		this.isEditing = true;
		this.assessment = assessment;
		this.service = service;
		this.serviceGuideline = guideline;
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
		getView().showPleaseWaitDialog("Retrieving Service Guideline information...");
		getView().resetFieldStyles();
		
		GWT.log("Maintain Service Guideline: service is: " + service);
		GWT.log("Maintain Service Guideline: assessment is: " + assessment);

		if (serviceGuideline == null) {
			clientFactory.getShell().setSubTitle("Create Service Guideline");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit Service Guideline");
			startEdit();
			// get latest version of the service guideline from the server
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain service guideline: create");
		isEditing = false;
		getView().setEditing(false);
		serviceGuideline = new ServiceGuidelinePojo();
	}

	private void startEdit() {
		GWT.log("Maintain service guideline: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the assessment is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainServiceGuidelineView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private void doCancelServiceGuideline() {
		// go back to the maintain assessment page...
		ActionEvent.fire(eventBus, ActionNames.SERVICE_GUIDELINE_EDITING_CANCELED, assessment);
	}

	private void doDeleteServiceGuideline() {
		if (serviceGuideline == null) {
			return;
		}

		// TODO remove the service guideline from the assessment and save the assessment
		
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
				GWT.log("Exception saving the Security Assessment", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Security Assessment.  Message " +
						"from server is: " + caught.getMessage());
				assessment.getServiceGuidelines().remove(getServiceGuideline());
				ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
			}

			@Override
			public void onSuccess(ServiceSecurityAssessmentPojo result) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
//				ActionEvent.fire(eventBus, ActionNames.SECURITY_ASSESSMENT_SAVED, assessment);
			}
		};
		if (!isEditing) {
			getServiceGuideline().setServiceId(service.getServiceId());
			this.assessment.getServiceGuidelines().add(getServiceGuideline());
		}
		else {
			// TODO: have to find the service guideline, remove it and then re-add this one to the list
		}
		// it's always an update
		VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
	}

	@Override
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return this.assessment;
	}

	public MaintainServiceGuidelineView getView() {
		if (view == null) {
			view = clientFactory.getMaintainServiceGuidelineView();
			view.setPresenter(this);
		}
		return view;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteServiceGuideline(ServiceGuidelinePojo selected) {
		if (isEditing) {
			doDeleteServiceGuideline();
		} else {
			doCancelServiceGuideline();
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
	public ServiceGuidelinePojo getServiceGuideline() {
		return this.serviceGuideline;
	}

	@Override
	public void setAssessorDirectoryPerson(DirectoryPersonPojo pojo) {
		this.assessorDirectoryPerson = pojo;
	}

	public DirectoryPersonPojo getAssessorDirectoryPerson() {
		return assessorDirectoryPerson;
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

	public void setServiceGuideline(ServiceGuidelinePojo serviceGuideline) {
		this.serviceGuideline = serviceGuideline;
	}
}
