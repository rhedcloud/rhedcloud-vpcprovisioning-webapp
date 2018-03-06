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
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainFirewallRulePresenter extends PresenterBase implements MaintainFirewallRuleView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String name;
	private FirewallRulePojo firewallRule;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new CIDR.
	 */
	public MaintainFirewallRulePresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.firewallRule = null;
		this.name = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainFirewallRuleView().setPresenter(this);
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainFirewallRulePresenter(ClientFactory clientFactory, FirewallRulePojo firewallRule) {
		this.isEditing = true;
		this.name = firewallRule.getName();
		this.clientFactory = clientFactory;
		this.firewallRule = firewallRule;
		clientFactory.getMaintainFirewallRuleView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		ReleaseInfo ri = new ReleaseInfo();
		clientFactory.getShell().setReleaseInfo(ri.toString());
		if (name == null) {
			clientFactory.getShell().setSubTitle("Create CIDR");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit CIDR");
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
				getView().initPage();
				getView().setInitialFocus();
				
				// TODO: get firewall rules for filter
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		firewallRule = new FirewallRulePojo();
	}

	private void startEdit() {
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the firewallRule is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainFirewallRuleView().setLocked(false);
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
	public void deleteFirewallRule() {
		if (isEditing) {
			doDeleteFirewallRule();
		} else {
			doCancelFirewallRule();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelFirewallRule() {
		ActionEvent.fire(eventBus, ActionNames.CIDR_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteFirewallRule() {
		if (firewallRule == null) {
			return;
		}

		// TODO Delete the CIDR on the server then fire onFirewallRuleDeleted();
	}

	@Override
	public void saveFirewallRule() {
		// save on server
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
		AsyncCallback<FirewallRulePojo> callback = new AsyncCallback<FirewallRulePojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the FirewallRule", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the CIDR.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(FirewallRulePojo result) {
				// TODO Auto-generated method stub
				
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.CIDR_SAVED, result);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createFirewallRule(firewallRule, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateFirewallRule(firewallRule, callback);
		}
	}

	@Override
	public FirewallRulePojo getFirewallRule() {
		return this.firewallRule;
	}

	private MaintainFirewallRuleView getView() {
		return clientFactory.getMaintainFirewallRuleView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getFirewallRuleId() {
		return name;
	}

	public void setFirewallRuleId(String name) {
		this.name = name;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setFirewallRule(FirewallRulePojo firewallRule) {
		this.firewallRule = firewallRule;
	}

	@Override
	public boolean isValidFirewallRuleName(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w) {
		// TODO Auto-generated method stub
		
	}
}
