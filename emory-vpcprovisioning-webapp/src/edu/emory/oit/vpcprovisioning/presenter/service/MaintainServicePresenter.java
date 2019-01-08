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
import edu.emory.oit.vpcprovisioning.client.event.AssessmentListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainServicePresenter extends PresenterBase implements MaintainServiceView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String serviceId;
	private AWSServicePojo service;
	private ServiceSecurityAssessmentPojo selectedSSA;
	private UserAccountPojo userLoggedIn;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;
	private final boolean clearList;

	/**
	 * For creating a new service.
	 */
	public MaintainServicePresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.service = null;
		this.serviceId = null;
		this.clientFactory = clientFactory;
		this.clearList = true;
		clientFactory.getMaintainServiceView().setPresenter(this);
	}

	/**
	 * For editing an existing service.
	 */
	public MaintainServicePresenter(ClientFactory clientFactory, AWSServicePojo service) {
		this.isEditing = true;
		this.serviceId = service.getServiceId();
		this.clientFactory = clientFactory;
		this.service = service;
		this.clearList = true;
		clientFactory.getMaintainServiceView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().showPleaseWaitDialog("Retrieving Service details, please wait...");
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		if (serviceId == null) {
			clientFactory.getShell().setSubTitle("Create Service");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Service");
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
				AsyncCallback<List<String>> awsServiceStatusItemsCB = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving AWS service status types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving AWS service status types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().setAwsServiceStatusItems(result);
					}
				};
				VpcProvisioningService.Util.getInstance().getAwsServiceStatusItems(awsServiceStatusItemsCB);

				AsyncCallback<List<String>> siteServiceStatusItemsCB = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving site service status types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving site service status types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().setSiteServiceStatusItems(result);
					}
				};
				VpcProvisioningService.Util.getInstance().getSiteServiceStatusItems(siteServiceStatusItemsCB);

				getView().setUserLoggedIn(user);
				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearAssessmentList();
				}
				// Request the service assessment list now.
				refreshList(user);
				getView().initPage();
				getView().setInitialFocus();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all security assessments for the current filter being used
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
				setAssessmentList(result.getResults());
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};

		if (isEditing) {
			GWT.log("refreshing security assessment list...");
			ServiceSecurityAssessmentQueryFilterPojo filter = new ServiceSecurityAssessmentQueryFilterPojo();
			filter.setServiceId(this.serviceId);
			VpcProvisioningService.Util.getInstance().getSecurityAssessmentsForFilter(filter, callback);
		}
		else {
			setAssessmentList(Collections.<ServiceSecurityAssessmentPojo> emptyList());
		}
	}

	private void setAssessmentList(List<ServiceSecurityAssessmentPojo> services) {
		getView().setAssessments(services);
		eventBus.fireEventFromSource(new AssessmentListUpdateEvent(services), this);
	}

	private void startCreate() {
		GWT.log("Maintain service: create");
		isEditing = false;
		getView().setEditing(false);
		service = new AWSServicePojo();
	}

	private void startEdit() {
		GWT.log("Maintain service: edit");
		isEditing = true;
		getView().setEditing(true);
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainServiceView().setLocked(false);
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
	public void deleteService() {
		if (isEditing) {
			doDeleteService();
		} else {
			doCancelService();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelService() {
		ActionEvent.fire(eventBus, ActionNames.ACCOUNT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteService() {
		if (service == null) {
			return;
		}

		// TODO Delete the service on server then fire onServiceDeleted();
	}

	@Override
	public void saveService(final boolean listServices) {
		getView().showPleaseWaitDialog("Saving AWS Service...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
		}
		AsyncCallback<AWSServicePojo> callback = new AsyncCallback<AWSServicePojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Service", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Service.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final AWSServicePojo result) {
				GWT.log("MaintainServicePresenter.saveService - service was saved");
				getView().hidePleaseWaitDialog();
				// only go back to service list if true
				// otherwise we're adding an assessment so go to that view
				if (listServices) {
					ActionEvent.fire(eventBus, ActionNames.SERVICE_SAVED, service);
				}
				else {
					getView().showPleaseWaitDialog("Creating assessment...");
					GWT.log("Creating assessment for " + result.getAwsServiceCode() + "/" + result.getAwsServiceName());
					// TODO: just create an assessment with skeleton data and refresh the assessments list
					AsyncCallback<ServiceSecurityAssessmentPojo> callback = new AsyncCallback<ServiceSecurityAssessmentPojo>() {
						@Override
						public void onFailure(Throwable caught) {
							getView().hidePleaseWaitDialog();
							getView().hidePleaseWaitPanel();
							GWT.log("Exception saving the Security Assessment", caught);
							getView().showMessageToUser("There was an exception on the " +
									"server saving the Security Assessment.  Message " +
									"from server is: " + caught.getMessage());
						}

						@Override
						public void onSuccess(ServiceSecurityAssessmentPojo result) {
							AsyncCallback<AWSServiceQueryResultPojo> svcCb = new AsyncCallback<AWSServiceQueryResultPojo>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									getView().hidePleaseWaitDialog();
									getView().hidePleaseWaitPanel();
								}

								@Override
								public void onSuccess(AWSServiceQueryResultPojo result) {
									service = result.getResults().get(0);
									refreshList(userLoggedIn);
									getView().hidePleaseWaitDialog();
									getView().hidePleaseWaitPanel();
								}
							};
							// TODO: get the service again so we have the latest version
							AWSServiceQueryFilterPojo svcFilter = new AWSServiceQueryFilterPojo();
							svcFilter.setServiceId(serviceId);
							VpcProvisioningService.Util.getInstance().getServicesForFilter(svcFilter, svcCb);
						}
					};
					ServiceSecurityAssessmentPojo assessment = new ServiceSecurityAssessmentPojo();
					assessment.getServiceIds().add(result.getServiceId());
					assessment.setStatus("In Progress");
					VpcProvisioningService.Util.getInstance().createSecurityAssessment(assessment, callback);

//					ActionEvent.fire(eventBus, ActionNames.CREATE_SECURITY_ASSESSMENT, result);
				}
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createService(service, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateService(service, callback);
		}
	}

	@Override
	public AWSServicePojo getService() {
		return this.service;
	}

	@Override
	public boolean isValidServiceId(String value) {
		return false;
	}

	@Override
	public boolean isValidServiceName(String value) {
		return false;
	}

	private MaintainServiceView getView() {
		return clientFactory.getMaintainServiceView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setService(AWSServicePojo service) {
		this.service = service;
	}

	@Override
	public void deleteSecurityAssessment(final ServiceSecurityAssessmentPojo selected) {
		selectedSSA = selected;
		VpcpConfirm.confirm(
				MaintainServicePresenter.this, 
				"Confirm Delete Security Assessment", 
				"Delete the Security Assessment " + selectedSSA.getServiceSecurityAssessmentId() + "?");
	}

	@Override
	public void vpcpConfirmOkay() {
		getView().showPleaseWaitDialog("Deleting Security Assessment " + 
			selectedSSA.getServiceSecurityAssessmentId() + "...");
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server deleting the Security Assessment.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(Void result) {
				// remove from dataprovider
				getView().removeAssessmentFromView(selectedSSA);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "Security Assessment " + 
					selectedSSA.getServiceSecurityAssessmentId() + " was deleted.");
			}
		};
		VpcProvisioningService.Util.getInstance().deleteSecurityAssessment(selectedSSA, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Security Assessment " + 
				selectedSSA.getServiceSecurityAssessmentId() + " was not deleted.");
	}
}
