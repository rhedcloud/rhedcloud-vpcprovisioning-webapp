package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

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
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainTransitGatewayConnectionProfilePresenter extends PresenterBase implements MaintainTransitGatewayConnectionProfileView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String transitGatewayConnectionProfileId;
	private TransitGatewayConnectionProfilePojo transitGatewayConnectionProfile;
	private MaintainTransitGatewayConnectionProfileView view;
	private UserAccountPojo userLoggedIn;
	int createdCount = 0;
	boolean showStatus = false;
	boolean startTimer = true;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new CIDR.
	 */
	public MaintainTransitGatewayConnectionProfilePresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.transitGatewayConnectionProfile = null;
		this.transitGatewayConnectionProfileId = null;
		this.clientFactory = clientFactory;
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainTransitGatewayConnectionProfilePresenter(ClientFactory clientFactory, TransitGatewayConnectionProfilePojo pojo) {
		this.isEditing = true;
		this.transitGatewayConnectionProfileId = pojo.getTransitGatewayConnectionProfileId();
		this.clientFactory = clientFactory;
		this.transitGatewayConnectionProfile = pojo;
	}

	@Override
	public String mayStop() {
		
		return null;
	}
	
	@Override
	public void start(EventBus eventBus) {
		getView().showPleaseWaitDialog("Transit Gateway Connectino Profile maintenance, please wait...");
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		if (transitGatewayConnectionProfileId == null) {
			clientFactory.getShell().setSubTitle("Create Transit Gateway");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Transit Gateway");
			startEdit();
		}

		AsyncCallback<List<AWSRegionPojo>> regionCB = new AsyncCallback<List<AWSRegionPojo>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(List<AWSRegionPojo> result) {
				getView().setAwsRegionItems(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getAwsRegionItems(regionCB);

		// TODO: get all transit gateways
		AsyncCallback<TransitGatewayQueryResultPojo> envCB = new AsyncCallback<TransitGatewayQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(TransitGatewayQueryResultPojo result) {
				getView().setTransitGatewayItems(result.getResults());
			}
		};
		VpcProvisioningService.Util.getInstance().getTransitGatewaysForFilter(null, envCB);

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				GWT.log("setting user logged in...");
				getView().setUserLoggedIn(user);
				GWT.log("set user logged in...");
				getView().initPage();
				getView().setInitialFocus();
				
				// apply authorization mask
				if (user.isNetworkAdmin()) {
					getView().applyNetworkAdminMask();
				}
				else if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
				
                getView().hidePleaseWaitDialog();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}
	
	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		transitGatewayConnectionProfile = new TransitGatewayConnectionProfilePojo();
	}

	private void startEdit() {
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the cidr is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainTransitGatewayConnectionProfileView().setLocked(false);
	}
	
	@Override
	public void setInitialFocus() {
		
		
	}
	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}
	@Override
	public void saveTransitGatewayConnectionProfile() {
		getView().showPleaseWaitDialog("Saving Transit Gateway info...");
		if (!isFormValid()) {
			return;
		}
		AsyncCallback<TransitGatewayConnectionProfilePojo> callback = new AsyncCallback<TransitGatewayConnectionProfilePojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the TransitGatewayConnectionProfile", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the TransitGatewayConnectionProfile.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(TransitGatewayConnectionProfilePojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.TRANSIT_GATEWAY_CONNECTION_PROFILE_SAVED, result);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createTransitGatewayConnectionProfile(transitGatewayConnectionProfile, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateTransitGatewayConnectionProfile(transitGatewayConnectionProfile, callback);
		}
	}
	private boolean isFormValid() {
		boolean isValid = true;
		
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().hidePleaseWaitPanel();
			getView().showMessageToUser("Please provide data for the required fields.");
			return false;
		}
		else {
			getView().resetFieldStyles();
		}
		return isValid;
	}
	
	void showCreateListStatus(int createdCount, int totalToCreate, StringBuffer errors) {
		if (errors.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showStatus(null, createdCount + " out of " + totalToCreate + " VPN ConnectionProfile(s) were created.");
		}
		else {
			getView().hidePleaseWaitDialog();
			errors.insert(0, createdCount + " out of " + totalToCreate + " VPN ConnectionProfile(s) were created.  "
				+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(errors.toString());
		}
	}
	@Override
	public TransitGatewayConnectionProfilePojo getTransitGatewayConnectionProfile() {
		return this.transitGatewayConnectionProfile;
	}
	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public MaintainTransitGatewayConnectionProfileView getView() {
		if (view == null) {
			view = clientFactory.getMaintainTransitGatewayConnectionProfileView();
			view.setPresenter(this);
		}
		return view;
	}

	public String getTransitGatewayConnectionProfileId() {
		return transitGatewayConnectionProfileId;
	}

	public void setTransitGatewayConnectionProfileId(String transitGatewayConnectionProfileId) {
		this.transitGatewayConnectionProfileId = transitGatewayConnectionProfileId;
	}

	public void setTransitGatewayConnectionProfile(TransitGatewayConnectionProfilePojo transitGatewayConnectionProfile) {
		this.transitGatewayConnectionProfile = transitGatewayConnectionProfile;
	}

}
