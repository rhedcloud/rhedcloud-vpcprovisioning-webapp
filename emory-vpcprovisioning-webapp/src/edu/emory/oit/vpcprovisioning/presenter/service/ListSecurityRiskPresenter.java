package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.SecurityRiskListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListSecurityRiskPresenter extends PresenterBase implements ListSecurityRiskView.Presenter {
	private static final Logger log = Logger.getLogger(ListSecurityRiskPresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the Vpc list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the Vpc list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	private ServiceSecurityAssessmentPojo assessment;
	private AWSServicePojo service;
	private UserAccountPojo userLoggedIn;
	SecurityRiskPojo selectedSecurityRisk;
	
	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListSecurityRiskPresenter(ClientFactory clientFactory, boolean clearList, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.service = service;
		this.assessment = assessment;
		clientFactory.getListSecurityRiskView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListSecurityRiskPresenter(ClientFactory clientFactory, ListSecurityRiskPlace place) {
		this(clientFactory, place.isListStale(), place.getService(), place.getAssessment());
	}

	private ListSecurityRiskView getView() {
		return clientFactory.getListSecurityRiskView();
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().applyAWSAccountAuditorMask();
		getView().showPleaseWaitDialog("Retrieving security risks...");
		this.eventBus = eventBus;
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {
				getView().enableButtons();
				// Add a handler to the 'add' button in the shell.
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle(" SecurityRisks");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
//				setSecurityRiskList(Collections.<SecurityRiskPojo> emptyList());

				// Request the service list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	public void refreshList(final UserAccountPojo user) {
		setSecurityRiskList(assessment.getSecurityRisks());
        getView().hidePleaseWaitDialog();
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setSecurityRiskList(List<SecurityRiskPojo> securityRisks) {
		getView().setSecurityRisks(securityRisks);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new SecurityRiskListUpdateEvent(securityRisks), this);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectSecurityRisk(SecurityRiskPojo selected) {
		// TODO Auto-generated method stub
		
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
	public void deleteSecurityRisk(final SecurityRiskPojo securityRisk) {
		selectedSecurityRisk = securityRisk;
		VpcpConfirm.confirm(
			ListSecurityRiskPresenter.this, 
			"Confirm Delete Security Risk", 
			"Delete the Security Risk " + selectedSecurityRisk.getSecurityRiskName() + "?");
	}

	@Override
	public void setAssessment(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}

	@Override
	public ServiceSecurityAssessmentPojo getAssessment() {
		return this.assessment;
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public AWSServicePojo getService() {
		return service;
	}

	public void setService(AWSServicePojo service) {
		this.service = service;
	}

	@Override
	public void vpcpConfirmOkay() {
		getView().showPleaseWaitDialog("Deleting security risk...");
		assessment.getSecurityRisks().remove(selectedSecurityRisk);
		AsyncCallback<ServiceSecurityAssessmentPojo> callback = new AsyncCallback<ServiceSecurityAssessmentPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server deleting the SecurityRisk.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(ServiceSecurityAssessmentPojo result) {
				// remove from dataprovider
				getView().removeSecurityRiskFromView(selectedSecurityRisk);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "Security Risk " + 
						selectedSecurityRisk.getSecurityRiskName() + " was deleted.");
				ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
			}
		};
		VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  VPC " + 
				selectedSecurityRisk.getSecurityRiskName() + " was not deleted.");
	}
}
