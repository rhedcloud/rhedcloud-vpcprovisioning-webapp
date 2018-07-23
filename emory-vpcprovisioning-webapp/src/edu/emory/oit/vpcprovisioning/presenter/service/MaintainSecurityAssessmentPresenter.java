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
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainSecurityAssessmentPresenter extends PresenterBase implements MaintainSecurityAssessmentView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String assessmentId;
	private ServiceSecurityAssessmentPojo assessment;
	private UserAccountPojo userLoggedIn;
	private AWSServicePojo service;
	private AWSServicePojo relatedService;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainSecurityAssessmentPresenter(ClientFactory clientFactory, AWSServicePojo service) {
		this.isEditing = false;
		this.assessment = null;
		this.assessmentId = null;
		this.clientFactory = clientFactory;
		this.service = service;
		clientFactory.getMaintainSecurityAssessmentView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainSecurityAssessmentPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.isEditing = true;
		this.assessmentId = assessment.getServiceSecurityAssessmentId();
		this.clientFactory = clientFactory;
		this.assessment = assessment;
		this.service = service;
		clientFactory.getMaintainSecurityAssessmentView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		setReleaseInfo(clientFactory);
		getView().showPleaseWaitPanel("Retrieving Security Assessment information...");
		
		GWT.log("Maintain Assessment: service is: " + service);

		if (assessmentId == null) {
			clientFactory.getShell().setSubTitle("Create Assessment");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit Assessment");
			startEdit();
			// get latest version of the assessment from the server
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
//			getView().setSecurityRisks(assessment.getSecurityRisks());
//			// TODO: controls, guidelines and test plans
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
				
				// get all the services that are related to the assessment and add 
				// them to the view
				for (String id : assessment.getServiceIds()) {
					AsyncCallback<AWSServiceQueryResultPojo> svc_callback = new AsyncCallback<AWSServiceQueryResultPojo>() {
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onSuccess(AWSServiceQueryResultPojo result) {
							// TODO: add more info to the widget title (last param)
							// so when they hover over it, they can see more detail about the
							// service.
							getView().addRelatedServiceToView(result.getResults().get(0), "");
						}
					};
					AWSServiceQueryFilterPojo filter = new AWSServiceQueryFilterPojo();
					filter.setServiceId(id);
					VpcProvisioningService.Util.getInstance().getServicesForFilter(filter, svc_callback);
				}

				AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
						GWT.log("Exception retrieving status types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving status types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().setAssessmentStatusItems(result);
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
				// assessment status items here
				VpcProvisioningService.Util.getInstance().getAssessmentStatusTypeItems(callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain assessment: create");
		isEditing = false;
		getView().setEditing(false);
		assessment = new ServiceSecurityAssessmentPojo();
		assessment.getServiceIds().add(service.getServiceId());
	}

	private void startEdit() {
		GWT.log("Maintain assessment: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the assessment is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainSecurityAssessmentView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelAssessment() {
		ActionEvent.fire(eventBus, ActionNames.SECURITY_ASSESSMENT_EDITING_CANCELED, assessment);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteAssessment() {
		if (assessment == null) {
			return;
		}

		// TODO Delete the assessment on server then fire onAccountDeleted();
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
				ActionEvent.fire(eventBus, ActionNames.SECURITY_ASSESSMENT_SAVED, service, assessment);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createSecurityAssessment(assessment, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
		}
	}

	@Override
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return this.assessment;
	}

	public MaintainSecurityAssessmentView getView() {
		return clientFactory.getMaintainSecurityAssessmentView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getAccountId() {
		return assessmentId;
	}

	public void setAccountId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setAccount(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSecurityAssessment(ServiceSecurityAssessmentPojo selected) {
		if (isEditing) {
			doDeleteAssessment();
		} else {
			doCancelAssessment();
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
	public void setRelatedService(AWSServicePojo service) {
		this.relatedService = service;
	}

	@Override
	public AWSServicePojo getRelatedService() {
		return relatedService;
	}

}
