package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.RiskCalculationPropertiesPojo;
import edu.emory.oit.vpcprovisioning.shared.RiskLevelCalculationPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class CalculateSecurityRiskPresenter extends PresenterBase implements CalculateSecurityRiskView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private AWSServicePojo service;
	private UserAccountPojo userLoggedIn;
	private ServiceSecurityAssessmentPojo assessment;
	private SecurityRiskPojo risk;
	private String securityRiskId;
	private boolean isEditing;
	private RiskLevelCalculationPojo rlcp;
	private MaintainSecurityRiskView maintainSecurityRiskView;

	/**
	 * For creating a new service.
	 */
	public CalculateSecurityRiskPresenter(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing service.
	 */
	public CalculateSecurityRiskPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.clientFactory = clientFactory;
		this.service = service;
		this.assessment = assessment;
		getView().setPresenter(this);
	}

	public CalculateSecurityRiskPresenter(ClientFactory clientFactory, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk) {
		this.clientFactory = clientFactory;
		this.service = service;
		this.assessment = assessment;
		this.risk = risk;
		this.securityRiskId = risk.getSecurityRiskId();
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().clear();
//		getView().showPleaseWaitDialog("Generating Service Assessment Report, please wait (potential long running task)...");
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);

		if (securityRiskId == null) {
			clientFactory.getShell().setSubTitle("Create Security Risk");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit Security Risk");
			startEdit();
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception determining user logged in", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server determining the user logged in.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				// Request the service assessment list now.
				
				AsyncCallback<List<RiskCalculationPropertiesPojo>> rcps = new AsyncCallback<List<RiskCalculationPropertiesPojo>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(List<RiskCalculationPropertiesPojo> result) {
						getView().setRiskCalculationProperties(result);
						getView().initPage();
						for (RiskCalculationPropertiesPojo rcp : result) {
							getView().createWizardQuestionPanel(rcp);
						}
						getView().setInitialWizardPanel();
						
						getView().setInitialFocus();
					}
				};
				VpcProvisioningService.Util.getInstance().getRiskCalculationProperties(rcps);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	private void startCreate() {
		GWT.log("Calculate security risk: create");
		risk = new SecurityRiskPojo();
		rlcp = new RiskLevelCalculationPojo();
		risk.setSequenceNumber(assessment.getSecurityRisks().size() + 1);
	}

	private void startEdit() {
		GWT.log("calculate security risk: edit");
		isEditing = true;
		rlcp = new RiskLevelCalculationPojo();
		getView().setEditing(true);
	}

	@Override
	public void stop() {
		eventBus = null;
//		clientFactory.getServiceAssessmentReportView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	public CalculateSecurityRiskView getView() {
		return clientFactory.getCalculateSecurityRiskView();
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

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}

	public void setAssessment(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}

	@Override
	public AWSServicePojo getService() {
		return service;
	}

	@Override
	public SecurityRiskPojo getRisk() {
		return risk;
	}

	@Override
	public RiskLevelCalculationPojo getRiskLevelCalculation() {
		return this.rlcp;
	}

	public MaintainSecurityRiskView getMaintainSecurityRiskView() {
		return maintainSecurityRiskView;
	}

	public void setMaintainSecurityRiskView(MaintainSecurityRiskView maintainSecurityRiskView) {
		this.maintainSecurityRiskView = maintainSecurityRiskView;
	}
}
