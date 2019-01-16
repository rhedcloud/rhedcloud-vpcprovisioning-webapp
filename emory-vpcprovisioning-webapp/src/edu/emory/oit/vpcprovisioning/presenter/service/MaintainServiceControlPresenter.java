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
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UUID;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainServiceControlPresenter extends PresenterBase implements MaintainServiceControlView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private ServiceSecurityAssessmentPojo assessment;
	private UserAccountPojo userLoggedIn;
	private AWSServicePojo service;
	private ServiceControlPojo serviceControl;
	private String serviceControlId;
	private DirectoryPersonPojo assessorDirectoryPerson;
	private DirectoryPersonPojo verifierDirectoryPerson;
	private MaintainServiceControlView view;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new service control.
	 */
	public MaintainServiceControlPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.isEditing = false;
		this.assessment = assessment;
		this.clientFactory = clientFactory;
		this.service = service;
		this.serviceControl = null;
		this.serviceControlId = null;
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainServiceControlPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceControlPojo control) {
		this.isEditing = true;
		this.clientFactory = clientFactory;
		this.assessment = assessment;
		this.service = service;
		this.serviceControl = control;
		this.serviceControlId = serviceControl.getServiceControlId();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving Service Control information...");
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		
		GWT.log("Maintain Service Control: service is: " + service);
		GWT.log("Maintain Service Control: assessment is: " + assessment);

		if (serviceControlId == null) {
			clientFactory.getShell().setSubTitle("Create Service Control");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit Service Control");
			startEdit();
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
		GWT.log("Maintain service control: create");
		isEditing = false;
		getView().setEditing(false);
		serviceControl = new ServiceControlPojo();
	}

	private void startEdit() {
		GWT.log("Maintain service control: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the assessment is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainServiceControlView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private void doCancelServiceControl() {
		// go back to the maintain assessment page...
		ActionEvent.fire(eventBus, ActionNames.SERVICE_CONTROL_EDITING_CANCELED, assessment);
	}

	private void doDeleteServiceControl() {
		if (serviceControl == null) {
			return;
		}

		// TODO remove the service control from the assessment and save the assessment
		
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
				assessment.getServiceControls().remove(getServiceControl());
				ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
			}

			@Override
			public void onSuccess(ServiceSecurityAssessmentPojo result) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
			}
		};
		if (!isEditing) {
			getServiceControl().setServiceControlId(UUID.uuid());
			getServiceControl().setServiceId(service.getServiceId());
			this.assessment.getServiceControls().add(getServiceControl());
		}
		else {
			// TODO: have to find the service control, remove it and then re-add this one to the list
		}
		// it's always an update
		VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
	}

	@Override
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return this.assessment;
	}

	public MaintainServiceControlView getView() {
		if (view == null) {
			view = clientFactory.getMaintainServiceControlView();
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

	public String getServiceControlId() {
		return serviceControlId;
	}

	public void setServiceControlId(String controlId) {
		this.serviceControlId = controlId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		
		
	}

	@Override
	public void deleteServiceControl(ServiceControlPojo selected) {
		if (isEditing) {
			doDeleteServiceControl();
		} else {
			doCancelServiceControl();
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
	public ServiceControlPojo getServiceControl() {
		return this.serviceControl;
	}

	@Override
	public void setAssessorDirectoryPerson(DirectoryPersonPojo pojo) {
		this.assessorDirectoryPerson = pojo;
	}

	public DirectoryPersonPojo getAssessorDirectoryPerson() {
		return assessorDirectoryPerson;
	}

	public void setVerifierDirectoryPerson(DirectoryPersonPojo pojo) {
		this.verifierDirectoryPerson = pojo;
	}

	public DirectoryPersonPojo getVerifierDirectoryPerson() {
		return verifierDirectoryPerson;
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

	public void setServiceControl(ServiceControlPojo serviceControl) {
		this.serviceControl = serviceControl;
	}
}
