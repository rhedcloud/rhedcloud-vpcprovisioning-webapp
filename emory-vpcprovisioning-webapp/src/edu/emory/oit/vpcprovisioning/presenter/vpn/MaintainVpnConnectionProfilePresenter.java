package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;

public class MaintainVpnConnectionProfilePresenter extends PresenterBase implements MaintainVpnConnectionProfileView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String vpnConnectionProfileId;
	private VpnConnectionProfilePojo vpnConnectionProfile;
	private MaintainVpnConnectionProfileView view;
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
	public MaintainVpnConnectionProfilePresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpnConnectionProfile = null;
		this.vpnConnectionProfileId = null;
		this.clientFactory = clientFactory;
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainVpnConnectionProfilePresenter(ClientFactory clientFactory, VpnConnectionProfilePojo pojo) {
		this.isEditing = true;
		this.vpnConnectionProfileId = pojo.getVpnConnectionProfileId();
		this.clientFactory = clientFactory;
		this.vpnConnectionProfile = pojo;
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void start(EventBus eventBus) {
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		if (vpnConnectionProfileId == null) {
			clientFactory.getShell().setSubTitle("Create VPN Connection Profile");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit VPN Connection Profile");
			startEdit();
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().setInitialFocus();
				
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (user.isNetworkAdmin()) {
					getView().applyNetworkAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
	
	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		vpnConnectionProfile = new VpnConnectionProfilePojo();
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
		clientFactory.getMaintainVpnConnectionProfileView().setLocked(false);
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
	public void deleteVpnConnectionProfile() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createVpnConnectionProfiles(List<VpnConnectionProfilePojo> ips) {
		createdCount = 0;
		showStatus = false;

		if (!isFormValid()) {
			return;
		}
		final int totalToCreate = ips.size();
		
		final StringBuffer errors = new StringBuffer();
		for (int i=0; i<ips.size(); i++) {
			final VpnConnectionProfilePojo profile = ips.get(i);
			profile.setCreateTime(new Date());
			profile.setCreateUser(userLoggedIn.getPublicId());
			final int listCounter = i;
			
			AsyncCallback<VpnConnectionProfilePojo> callback = new AsyncCallback<VpnConnectionProfilePojo>() {
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Exception saving the VPN ConnectionProfile: " + profile.getVpcNetwork(), caught);
					errors.append("There was an exception on the " +
							"server saving the VPN ConnectionProfile (" + profile.getVpcNetwork() + ").  " +
							"Message from server is: " + caught.getMessage());
					if (!showStatus) {
						errors.append("\n");
					}
					if (listCounter == totalToCreate - 1) {
						showStatus = true;
					}
				}

				@Override
				public void onSuccess(VpnConnectionProfilePojo result) {
					createdCount++;
					if (listCounter == totalToCreate - 1) {
						showStatus = true;
					}
				}
			};

			GWT.log("[MaintainVpnConnectionProfilePresenter] creating VPN Connection Profile: " + profile.getVpcNetwork());
			VpcProvisioningService.Util.getInstance().createVpnConnectionProfile(profile, callback);
		}
		if (!showStatus) {
			// wait for all the creates to finish processing
			int delayMs = 500;
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
				@Override
				public boolean execute() {
					if (showStatus) {
						startTimer = false;
						showCreateListStatus(createdCount, totalToCreate, errors);
					}
					return startTimer;
				}
			}, delayMs);
		}
		else {
			showCreateListStatus(createdCount, totalToCreate, errors);
		}
	}
	@Override
	public void saveVpnConnectionProfile() {
		getView().showPleaseWaitDialog("Saving VPN Connection Profile...");
		if (!isFormValid()) {
			return;
		}
		AsyncCallback<VpnConnectionProfilePojo> callback = new AsyncCallback<VpnConnectionProfilePojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the VpnConnectionProfile", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the VpnConnectionProfile.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpnConnectionProfilePojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.VPN_CONNECTION_PROFILE_SAVED, result);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createVpnConnectionProfile(vpnConnectionProfile, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateVpnConnectionProfile(vpnConnectionProfile, callback);
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
	public VpnConnectionProfilePojo getVpnConnectionProfile() {
		return this.vpnConnectionProfile;
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

	public MaintainVpnConnectionProfileView getView() {
		if (view == null) {
			view = clientFactory.getMaintainVpnConnectionProfileView();
			view.setPresenter(this);
		}
		return view;
	}

	public String getVpnConnectionProfileId() {
		return vpnConnectionProfileId;
	}

	public void setVpnConnectionProfileId(String vpnConnectionProfileId) {
		this.vpnConnectionProfileId = vpnConnectionProfileId;
	}

	public void setVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile) {
		this.vpnConnectionProfile = vpnConnectionProfile;
	}
}
