package edu.emory.oit.vpcprovisioning.presenter.vpn;

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
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;

public class MaintainVpnConnectionProfileAssignmentPresenter extends PresenterBase implements MaintainVpnConnectionProfileAssignmentView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String vpnConnectionProfileAssignmentId;
	private VpnConnectionProfilePojo vpnConnectionProfile;
	private VpnConnectionProfileSummaryPojo vpnConnectionProfileSummary;
	private VpnConnectionProfileAssignmentPojo vpnConnectionProfileAssignment;
	private MaintainVpnConnectionProfileAssignmentView view;
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
	public MaintainVpnConnectionProfileAssignmentPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpnConnectionProfileAssignment = null;
		this.vpnConnectionProfileAssignmentId = null;
		this.clientFactory = clientFactory;
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainVpnConnectionProfileAssignmentPresenter(ClientFactory clientFactory, VpnConnectionProfileSummaryPojo summary) {
		this.isEditing = true;
		this.vpnConnectionProfileSummary = summary;
		this.vpnConnectionProfile = summary.getProfile();
		this.vpnConnectionProfileAssignment = summary.getAssignment();
		this.vpnConnectionProfileAssignmentId = this.vpnConnectionProfileAssignment.getVpnConnectionProfileAssignmentId();
		this.clientFactory = clientFactory;
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void start(EventBus eventBus) {
		getView().showPleaseWaitDialog("Retrieving VPN Connection ProfileAssignment details...");
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		if (vpnConnectionProfileAssignmentId == null) {
			clientFactory.getShell().setSubTitle("Create VPN Connection ProfileAssignment");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit VPN Connection ProfileAssignment");
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
				getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
	
	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		vpnConnectionProfileAssignment = new VpnConnectionProfileAssignmentPojo();
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
		clientFactory.getMaintainVpnConnectionProfileAssignmentView().setLocked(false);
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
	public void deleteVpnConnectionProfileAssignment() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void saveVpnConnectionProfileAssignment() {
		getView().showPleaseWaitDialog("Saving VPN Connection ProfileAssignment...");
		if (!isFormValid()) {
			return;
		}
		AsyncCallback<VpnConnectionProfileAssignmentPojo> callback = new AsyncCallback<VpnConnectionProfileAssignmentPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the VpnConnectionProfileAssignment", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the VpnConnectionProfileAssignment.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpnConnectionProfileAssignmentPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.VPN_CONNECTION_PROFILE_SAVED, result);
			}
		};
		if (!this.isEditing) {
			// it's a create
//			VpcProvisioningService.Util.getInstance().createVpnConnectionProfileAssignment(vpnConnectionProfileAssignment, callback);
		}
		else {
			// it's an update
//			VpcProvisioningService.Util.getInstance().updateVpnConnectionProfileAssignment(vpnConnectionProfileAssignment, callback);
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
			getView().showStatus(null, createdCount + " out of " + totalToCreate + " VPN ConnectionProfileAssignment(s) were created.");
		}
		else {
			getView().hidePleaseWaitDialog();
			errors.insert(0, createdCount + " out of " + totalToCreate + " VPN ConnectionProfileAssignment(s) were created.  "
				+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(errors.toString());
		}
	}
	@Override
	public VpnConnectionProfileAssignmentPojo getVpnConnectionProfileAssignment() {
		return this.vpnConnectionProfileAssignment;
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

	public MaintainVpnConnectionProfileAssignmentView getView() {
		if (view == null) {
			view = clientFactory.getMaintainVpnConnectionProfileAssignmentView();
			view.setPresenter(this);
		}
		return view;
	}

	public String getVpnConnectionProfileAssignmentId() {
		return vpnConnectionProfileAssignmentId;
	}

	public void setVpnConnectionProfileAssignmentId(String vpnConnectionProfileAssignmentId) {
		this.vpnConnectionProfileAssignmentId = vpnConnectionProfileAssignmentId;
	}

	public void setVpnConnectionProfileAssignment(VpnConnectionProfileAssignmentPojo vpnConnectionProfileAssignment) {
		this.vpnConnectionProfileAssignment = vpnConnectionProfileAssignment;
	}

	@Override
	public VpnConnectionProfilePojo getVpnConnectionProfile() {
		return this.vpnConnectionProfile;
	}

	@Override
	public VpnConnectionProfileSummaryPojo getVpnConnectonProfileSummary() {
		return this.vpnConnectionProfileSummary;
	}
}
