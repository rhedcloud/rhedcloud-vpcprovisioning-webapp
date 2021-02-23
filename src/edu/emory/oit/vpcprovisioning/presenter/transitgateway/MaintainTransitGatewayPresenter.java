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
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.shared.TunnelProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainTransitGatewayPresenter extends PresenterBase implements MaintainTransitGatewayView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String transitGatewayId;
	private TransitGatewayPojo transitGateway;
	private MaintainTransitGatewayView view;
	private UserAccountPojo userLoggedIn;
	int createdCount = 0;
	boolean showStatus = false;
	boolean startTimer = true;
	TunnelProfilePojo selectedTunnel;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new CIDR.
	 */
	public MaintainTransitGatewayPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.transitGateway = null;
		this.transitGatewayId = null;
		this.clientFactory = clientFactory;
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainTransitGatewayPresenter(ClientFactory clientFactory, TransitGatewayPojo pojo) {
		this.isEditing = true;
		this.transitGatewayId = pojo.getTransitGatewayId();
		this.clientFactory = clientFactory;
		this.transitGateway = pojo;
	}

	@Override
	public String mayStop() {
		
		return null;
	}
	
	@Override
	public void start(EventBus eventBus) {
		getView().showPleaseWaitDialog("Transit Gateway maintenance, please wait...");
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		if (transitGatewayId == null) {
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

		AsyncCallback<List<String>> envCB = new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(List<String> result) {
				getView().setEnvironmentItems(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getEnvironmentItems(envCB);

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
				getView().setUserLoggedIn(user);
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
		transitGateway = new TransitGatewayPojo();
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
		clientFactory.getMaintainTransitGatewayView().setLocked(false);
	}
	
	@Override
	public void setInitialFocus() {
		
		
	}
	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}
	@Override
	public void saveTransitGateway() {
		getView().showPleaseWaitDialog("Saving Transit Gateway info...");
		if (!isFormValid()) {
			return;
		}
		AsyncCallback<TransitGatewayPojo> callback = new AsyncCallback<TransitGatewayPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the TransitGateway", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the TransitGateway.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(TransitGatewayPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.TRANSIT_GATEWAY_SAVED, result);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createTransitGateway(transitGateway, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateTransitGateway(transitGateway, callback);
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
	public TransitGatewayPojo getTransitGateway() {
		return this.transitGateway;
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

	public MaintainTransitGatewayView getView() {
		if (view == null) {
			view = clientFactory.getMaintainTransitGatewayView();
			view.setPresenter(this);
		}
		return view;
	}

	public String getTransitGatewayId() {
		return transitGatewayId;
	}

	public void setTransitGatewayId(String transitGatewayId) {
		this.transitGatewayId = transitGatewayId;
	}

	public void setTransitGateway(TransitGatewayPojo transitGateway) {
		this.transitGateway = transitGateway;
	}

}
