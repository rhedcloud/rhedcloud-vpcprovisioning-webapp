package edu.emory.oit.vpcprovisioning.presenter.firewall;

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
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class MaintainFirewallExceptionRequestPresenter extends PresenterBase implements MaintainFirewallExceptionRequestView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String systemId;
	private FirewallExceptionRequestPojo firewallExceptionRequest;
	private FirewallRulePojo firewallRule;
	VpcPojo vpc;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new CIDR.
	 */
	public MaintainFirewallExceptionRequestPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.firewallExceptionRequest = null;
		this.systemId = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainFirewallExceptionRequestView().setPresenter(this);
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainFirewallExceptionRequestPresenter(ClientFactory clientFactory, FirewallExceptionRequestPojo firewallExceptionRequest) {
		this.isEditing = true;
		this.systemId = firewallExceptionRequest.getSystemId();
		this.clientFactory = clientFactory;
		this.firewallExceptionRequest = firewallExceptionRequest;
		clientFactory.getMaintainFirewallExceptionRequestView().setPresenter(this);
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
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		if (systemId == null) {
			clientFactory.getShell().setSubTitle("Create Firewall Exception Request");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Firewall Exception Request");
			startEdit();
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				if (!isEditing) {
					firewallExceptionRequest.setUserNetId(user.getPrincipal());
				}

				getView().setUserLoggedIn(user);
				List<String> complianceClassTypes = new java.util.ArrayList<String>();
				complianceClassTypes.add("ePHI");
				complianceClassTypes.add("FERPA");
				complianceClassTypes.add("FISMA");
				complianceClassTypes.add("HIPAA");
				complianceClassTypes.add("PCI");

				getView().setComplianceClassItems(complianceClassTypes);
				
				List<String> timeRules = new java.util.ArrayList<String>();
				timeRules.add(Constants.TIME_RULE_INDEFINITELY);
				timeRules.add(Constants.TIME_RULE_SPECIFIC_DATE);
				getView().setTimeRuleItems(timeRules);
				
				getView().initPage();
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (user.isAdminForAccount(getVpc().getAccountId())) {
			    	getView().applyAWSAccountAdminMask();
			    }
			    else {
			    	getView().applyAWSAccountAuditorMask();
			    }

				getView().setInitialFocus();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		firewallExceptionRequest = new FirewallExceptionRequestPojo();
		firewallExceptionRequest.getTags().add(this.getVpc().getVpcId());
	}

	private void startEdit() {
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the firewallExceptionRequest is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainFirewallExceptionRequestView().setLocked(false);
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
	public void deleteFirewallExceptionRequest() {
		if (isEditing) {
			doDeleteFirewallExceptionRequest();
		} else {
			doCancelFirewallExceptionRequest();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelFirewallExceptionRequest() {
		ActionEvent.fire(eventBus, ActionNames.CIDR_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteFirewallExceptionRequest() {
		if (firewallExceptionRequest == null) {
			return;
		}

		// TODO Delete the CIDR on the server then fire onFirewallExceptionRequestDeleted();
	}

	@Override
	public void saveFirewallExceptionRequest() {
		// save on server
		getView().showPleaseWaitDialog("Saving firewall exception request...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().setFieldViolations(false);
			getView().resetFieldStyles();
		}
		AsyncCallback<FirewallExceptionRequestPojo> callback = new AsyncCallback<FirewallExceptionRequestPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the FirewallExceptionRequest", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Firewall Exception Request.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(FirewallExceptionRequestPojo result) {
				// TODO Auto-generated method stub
				
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.FIREWALL_EXCEPTION_REQUEST_SAVED, result, vpc);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createFirewallExceptionRequest(firewallExceptionRequest, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateFirewallExceptionRequest(firewallExceptionRequest, callback);
		}
	}

	@Override
	public FirewallExceptionRequestPojo getFirewallExceptionRequest() {
		return this.firewallExceptionRequest;
	}

	public MaintainFirewallExceptionRequestView getView() {
		return clientFactory.getMaintainFirewallExceptionRequestView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getFirewallExceptionRequestId() {
		return systemId;
	}

	public void setFirewallExceptionRequestId(String name) {
		this.systemId = name;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setFirewallExceptionRequest(FirewallExceptionRequestPojo firewallExceptionRequest) {
		this.firewallExceptionRequest = firewallExceptionRequest;
	}

	@Override
	public boolean isValidFirewallExceptionRequestName(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		// TODO Auto-generated method stub
		
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	public FirewallRulePojo getFirewallRule() {
		return firewallRule;
	}

	public void setFirewallRule(FirewallRulePojo firewallRule) {
		this.firewallRule = firewallRule;
	}
}
