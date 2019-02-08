package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.event.ServiceControlListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListServiceControlPresenter extends PresenterBase implements ListServiceControlView.Presenter {

	/**
	 * A boolean indicating that we should clear the Vpc list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	private ServiceSecurityAssessmentPojo assessment;
	private AWSServicePojo service;
	private UserAccountPojo userLoggedIn;
	private ServiceControlPojo selectedServiceControl;
	
	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListServiceControlPresenter(ClientFactory clientFactory, boolean clearList, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.service = service;
		this.assessment = assessment;
		getView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListServiceControlPresenter(ClientFactory clientFactory, ListServiceControlPlace place) {
		this(clientFactory, place.isListStale(), place.getService(), place.getAssessment());
	}

	private ListServiceControlView getView() {
		return clientFactory.getListServiceControlView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().applyAWSAccountAuditorMask();
		getView().showPleaseWaitDialog("Retrieving service controls...");
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
				clientFactory.getShell().setSubTitle(" ServiceControls");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
//				setServiceControlList(Collections.<ServiceControlPojo> emptyList());

				// Request the service list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	private void refreshList(final UserAccountPojo user) {
		setServiceControlList(assessment.getServiceControls());
        getView().hidePleaseWaitDialog();
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setServiceControlList(List<ServiceControlPojo> serviceControls) {
		getView().setServiceControls(serviceControls);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new ServiceControlListUpdateEvent(serviceControls), this);
		}
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectServiceControl(ServiceControlPojo selected) {
		
		
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
	public void deleteServiceControl(final ServiceControlPojo serviceControl) {
		selectedServiceControl = serviceControl;
		VpcpConfirm.confirm(
			ListServiceControlPresenter.this, 
			"Confirm Delete Service Control", 
			"Delete the Service Control " + selectedServiceControl.getServiceControlName() + "?");
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
		getView().showPleaseWaitDialog("Deleting service control " + selectedServiceControl.getServiceControlName() + "...");
		assessment.getServiceControls().remove(selectedServiceControl);
		AsyncCallback<ServiceSecurityAssessmentPojo> callback = new AsyncCallback<ServiceSecurityAssessmentPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server deleting the ServiceControl.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(ServiceSecurityAssessmentPojo result) {
				// remove from dataprovider
				getView().removeServiceControlFromView(selectedServiceControl);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "Service Control " + 
						selectedServiceControl.getServiceControlName() + " was deleted.");
				ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
			}
		};
		VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Service Control " + 
				selectedServiceControl.getServiceControlName() + " was not deleted.");
	}
}
