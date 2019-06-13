package edu.emory.oit.vpcprovisioning.presenter.srd;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;

public class MaintainSrdPresenter extends PresenterBase implements MaintainSrdView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String srdId;
	private SecurityRiskDetectionPojo srd;
	private AccountNotificationPojo accountNotification;
	private UserNotificationPojo userNotification;
	private UserAccountPojo userLoggedIn;
	private boolean isEditing;

	/**
	 * For editing an existing SRD via AccountNotification.
	 */
	public MaintainSrdPresenter(ClientFactory clientFactory, SecurityRiskDetectionPojo srd, AccountNotificationPojo accountNotification) {
		this.isEditing = true;
		this.srdId = srd.getSecurityRiskDetectionId();
		this.clientFactory = clientFactory;
		this.srd = srd;
		this.accountNotification = accountNotification;
		this.userNotification = null;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing SRD via UserNotification.
	 */
	public MaintainSrdPresenter(ClientFactory clientFactory, SecurityRiskDetectionPojo srd, UserNotificationPojo userNotification) {
		this.isEditing = true;
		this.srdId = srd.getSecurityRiskDetectionId();
		this.clientFactory = clientFactory;
		this.srd = srd;
		this.accountNotification = null;
		this.userNotification = userNotification;
		getView().setPresenter(this);
	}

	public MaintainSrdView getView() {
		return clientFactory.getMaintainSrdView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getSrdId() {
		return srdId;
	}

	public void setSrdId(String srdId) {
		this.srdId = srdId;
	}

	public SecurityRiskDetectionPojo getSrd() {
		return srd;
	}

	public void setSrd(SecurityRiskDetectionPojo srd) {
		this.srd = srd;
	}

	public AccountNotificationPojo getAccountNotification() {
		return accountNotification;
	}

	public void setAccountNotification(AccountNotificationPojo accountNotification) {
		this.accountNotification = accountNotification;
	}

	public UserNotificationPojo getUserNotification() {
		return userNotification;
	}

	public void setUserNotification(UserNotificationPojo userNotification) {
		this.userNotification = userNotification;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving Security Risk Detection information...");
		
		if (srdId == null) {
			clientFactory.getShell().setSubTitle("Create SRD");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit SRD");
			startEdit();
			// get latest version of the srd from the server
//			AsyncCallback<ServiceSecurityAssessmentQueryResultPojo> callback = new AsyncCallback<ServiceSecurityAssessmentQueryResultPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//	                getView().hidePleaseWaitPanel();
//	                getView().hidePleaseWaitDialog();
//					GWT.log("Exception Retrieving Security Assessments", caught);
//					getView().showMessageToUser("There was an exception on the " +
//							"server retrieving the list of Security Assessments associated to this Service.  " +
//							"Message from server is: " + caught.getMessage());
//				}
//
//				@Override
//				public void onSuccess(ServiceSecurityAssessmentQueryResultPojo result) {
//					GWT.log("Got " + result.getResults().size() + " Security Assessments for " + result.getFilterUsed());
//					for (ServiceSecurityAssessmentPojo ssa : result.getResults()) {
//						if (ssa.getServiceSecurityAssessmentId().equals(assessment.getServiceSecurityAssessmentId())) {
//							assessment = ssa;
//							break;
//						}
//					}
//				}
//			};
//
//			GWT.log("refreshing security assessment...");
//			ServiceSecurityAssessmentQueryFilterPojo filter = new ServiceSecurityAssessmentQueryFilterPojo();
//			filter.setServiceId(service.getServiceId());
//			VpcProvisioningService.Util.getInstance().getSecurityAssessmentsForFilter(filter, callback);
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
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain SRD: create");
		isEditing = false;
		getView().setEditing(false);
		srd = new SecurityRiskDetectionPojo();
	}

	private void startEdit() {
		GWT.log("Maintain SRD: view/edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the assessment is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainSrdView().setLocked(false);
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
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		
		
	}

}
