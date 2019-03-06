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
import edu.emory.oit.vpcprovisioning.client.event.ServiceGuidelineListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListServiceGuidelinePresenter extends PresenterBase implements ListServiceGuidelineView.Presenter {

	/**
	 * A boolean indicating that we should clear the Vpc list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	private ServiceSecurityAssessmentPojo assessment;
	private AWSServicePojo service;
	private UserAccountPojo userLoggedIn;
	private ServiceGuidelinePojo selectedServiceGuideline;
	
	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListServiceGuidelinePresenter(ClientFactory clientFactory, boolean clearList, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
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
	public ListServiceGuidelinePresenter(ClientFactory clientFactory, ListServiceGuidelinePlace place) {
		this(clientFactory, place.isListStale(), place.getService(), place.getAssessment());
	}

	private ListServiceGuidelineView getView() {
		return clientFactory.getListServiceGuidelineView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().applyAWSAccountAuditorMask();
		getView().showPleaseWaitDialog("Retrieving service guidelines...");
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
				clientFactory.getShell().setSubTitle(" ServiceGuidelines");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
//				setServiceGuidelineList(Collections.<ServiceGuidelinePojo> emptyList());

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
		setServiceGuidelineList(assessment.getServiceGuidelines());
        getView().hidePleaseWaitDialog();
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setServiceGuidelineList(List<ServiceGuidelinePojo> serviceGuidelines) {
		getView().setServiceGuidelines(serviceGuidelines);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new ServiceGuidelineListUpdateEvent(serviceGuidelines), this);
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
	public void selectServiceGuideline(ServiceGuidelinePojo selected) {
		
		
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
	public void deleteServiceGuideline(final ServiceGuidelinePojo serviceGuideline) {
		selectedServiceGuideline = serviceGuideline;
		VpcpConfirm.confirm(
				ListServiceGuidelinePresenter.this, 
				"Confirm Delete Service Guideline", 
				"Delete the Service Guideline " + selectedServiceGuideline.getServiceGuidelineName() + "?");
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
		getView().showPleaseWaitDialog("Deleting service guideline " + selectedServiceGuideline.getServiceGuidelineName() + "...");
		assessment.getServiceGuidelines().remove(selectedServiceGuideline);
		// re-sequence guidelines
		int i=1;
		for (ServiceGuidelinePojo pojo : assessment.getServiceGuidelines()) {
			pojo.setSequenceNumber(i);
			i++;
		}
		AsyncCallback<ServiceSecurityAssessmentPojo> callback = new AsyncCallback<ServiceSecurityAssessmentPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server deleting the ServiceGuideline.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(ServiceSecurityAssessmentPojo result) {
				// remove from dataprovider
				getView().removeServiceGuidelineFromView(selectedServiceGuideline);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "Service Guideline " + 
						selectedServiceGuideline.getServiceGuidelineName() + " was deleted.");
				ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, service, assessment);
			}
		};
		VpcProvisioningService.Util.getInstance().updateSecurityAssessment(assessment, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Service Guideline " + 
				selectedServiceGuideline.getServiceGuidelineName() + " was not deleted.");
	}

}
