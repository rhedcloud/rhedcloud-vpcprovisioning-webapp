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
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainServicePresenter extends PresenterBase implements MaintainServiceView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String serviceId;
	private AWSServicePojo account;
	private String awsServicesURL = "Cannot retrieve AWS Services URL";
	private String awsBillingManagementURL = "Cannot retrieve AWS Billing Management URL";

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainServicePresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.account = null;
		this.serviceId = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainServiceView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainServicePresenter(ClientFactory clientFactory, AWSServicePojo account) {
		this.isEditing = true;
		this.serviceId = account.getServiceId();
		this.clientFactory = clientFactory;
		this.account = account;
		clientFactory.getMaintainServiceView().setPresenter(this);
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving e-mail types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving e-mail types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().initPage();
						getView().setServiceStatusItems(result);
						getView().hidePleaseWaitDialog();
						getView().setInitialFocus();
						// apply authorization mask
						// TODO: need to determine the Service structure so we can apply authorization mask appropriately
						if (user.isLitsAdmin()) {
							getView().applyAWSAccountAdminMask();
						}
						else {
							getView().applyAWSAccountAuditorMask();
						}
					}
				};
				VpcProvisioningService.Util.getInstance().getServiceStatusItems(callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain service: create");
		isEditing = false;
		getView().setEditing(false);
		account = new AWSServicePojo();
	}

	private void startEdit() {
		GWT.log("Maintain service: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the account is loaded.
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
		if (account == null) {
			return;
		}

		// TODO Delete the account on server then fire onServiceDeleted();
	}

	@Override
	public void saveService() {
		getView().showPleaseWaitDialog();
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
			public void onSuccess(AWSServicePojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.ACCOUNT_SAVED, account);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createService(account, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateService(account, callback);
		}
	}

	@Override
	public AWSServicePojo getService() {
		return this.account;
	}

	@Override
	public boolean isValidServiceId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidServiceName(String value) {
		// TODO Auto-generated method stub
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

	public void setService(AWSServicePojo account) {
		this.account = account;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w) {
		AsyncCallback<DirectoryMetaDataPojo> callback = new AsyncCallback<DirectoryMetaDataPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(DirectoryMetaDataPojo result) {
				if (result.getFirstName() == null) {
					result.setFirstName("Unknown");
				}
				if (result.getLastName() == null) {
					result.setLastName("Net ID");
				}
				w.setTitle(result.getFirstName() + " " + result.getLastName() + 
					" - from the Identity Service.");
			}
		};
		VpcProvisioningService.Util.getInstance().getDirectoryMetaDataForNetId(netId, callback);
	}
}
