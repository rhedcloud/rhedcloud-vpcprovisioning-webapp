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
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionAddRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionAddRequestRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRemoveRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRemoveRequestRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class MaintainFirewallExceptionRequestPresenter extends PresenterBase implements MaintainFirewallExceptionRequestView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String systemId;
	private FirewallExceptionRequestSummaryPojo summary;
	private FirewallExceptionAddRequestPojo addRequest;
	private FirewallExceptionAddRequestRequisitionPojo addRequisition;
	private FirewallExceptionRemoveRequestPojo removeRequest;
	private FirewallExceptionRemoveRequestRequisitionPojo removeRequisition;
	private FirewallRulePojo firewallRule;
	VpcPojo vpc;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;
	private boolean isAdd;
	private boolean isRemove;

	/**
	 * For creating a new firewall exception request
	 */
	// TODO: need to tell the presenter if we're creating an addException request or a removeException request
	public MaintainFirewallExceptionRequestPresenter(ClientFactory clientFactory, boolean isAdd) {
		this.isAdd = isAdd;
		if (isAdd) {
			this.isRemove = false;
		}
		else {
			this.isRemove = true;
		}
		this.isEditing = false;
		this.systemId = null;
		this.clientFactory = clientFactory;
		this.summary = null;
		clientFactory.getMaintainFirewallExceptionRequestView().setPresenter(this);
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainFirewallExceptionRequestPresenter(ClientFactory clientFactory, FirewallExceptionRequestSummaryPojo summary) {
		this.isEditing = true;
		if (summary.getAddRequest() != null) {
			this.isAdd = true;
			this.isRemove = false;
			this.systemId = summary.getAddRequest().getSystemId();
			this.addRequest = summary.getAddRequest();
		}
		else {
			this.isRemove = true;
			this.isAdd = false;
			this.systemId = summary.getRemoveRequest().getSystemId();
			this.removeRequest = summary.getRemoveRequest();
		}
		this.clientFactory = clientFactory;
		this.summary = summary;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		setReleaseInfo(clientFactory);

		if (systemId == null) {
			clientFactory.getShell().setSubTitle("Create Firewall Exception Request");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Firewall Exception Request");
			startEdit();
		}

		getView().initDataEntryPanels();
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Central Admins you're associated to.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				if (!isEditing) {
					if (isAdd) {
						addRequisition.setUserNetId(user.getPrincipal());
					}
					else {
						removeRequisition.setUserNetId(user.getPrincipal());
					}
				}

				getView().setUserLoggedIn(user);
				if (isAdd) {
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
				}
				
				getView().initPage();
				if (!isEditing) {
					if (user.isCentralAdmin()) {
						getView().applyCentralAdminMask();
					}
					else if (user.isAdminForAccount(getVpc().getAccountId())) {
				    	getView().applyAWSAccountAdminMask();
				    }
				    else {
				    	getView().applyAWSAccountAuditorMask();
				    }
				}

				getView().setInitialFocus();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		if (isAdd) {
			addRequisition = new FirewallExceptionAddRequestRequisitionPojo();
			addRequisition.getTags().add(this.getVpc().getVpcId());
		}
		else {
			removeRequisition = new FirewallExceptionRemoveRequestRequisitionPojo();
			removeRequisition.getTags().add(this.getVpc().getVpcId());
		}
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
		
		AsyncCallback<FirewallExceptionAddRequestPojo> addCb = new AsyncCallback<FirewallExceptionAddRequestPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the FirewallExceptionAddRequest", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Firewall Exception Add Request.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(FirewallExceptionAddRequestPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.FIREWALL_EXCEPTION_REQUEST_SAVED, result, vpc);
			}
		};
		AsyncCallback<FirewallExceptionRemoveRequestPojo> removeCb = new AsyncCallback<FirewallExceptionRemoveRequestPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the FirewallExceptionRemoveRequest", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Firewall Exception Remove Request.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(FirewallExceptionRemoveRequestPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.FIREWALL_EXCEPTION_REQUEST_SAVED, result, vpc);
			}
		};
		
		if (!this.isEditing) {
			// it's a create
			if (isAdd) {
				VpcProvisioningService.Util.getInstance().generateFirewallExceptionAddRequest(addRequisition, addCb);
			}
			else {
				VpcProvisioningService.Util.getInstance().generateFirewallExceptionRemoveRequest(removeRequisition, removeCb);
			}
		}
		else {
			// it's an update
			if (isAdd) {
				VpcProvisioningService.Util.getInstance().updateFirewallExceptionAddRequest(addRequest, addCb);
			}
			else {
				VpcProvisioningService.Util.getInstance().updateFirewallExceptionRemoveRequest(removeRequest, removeCb);
			}
		}
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

	@Override
	public FirewallExceptionRequestSummaryPojo getSummary() {
		return summary;
	}

	public FirewallExceptionAddRequestPojo getAddRequest() {
		return addRequest;
	}

	public void setAddRequest(FirewallExceptionAddRequestPojo addRequest) {
		this.addRequest = addRequest;
	}

	public FirewallExceptionRemoveRequestPojo getRemoveRequest() {
		return removeRequest;
	}

	public void setRemoveRequest(FirewallExceptionRemoveRequestPojo removeRequest) {
		this.removeRequest = removeRequest;
	}

	public void setSummary(FirewallExceptionRequestSummaryPojo summary) {
		this.summary = summary;
	}

	public FirewallExceptionAddRequestRequisitionPojo getAddRequisition() {
		return addRequisition;
	}

	public void setAddRequisition(FirewallExceptionAddRequestRequisitionPojo addRequisition) {
		this.addRequisition = addRequisition;
	}

	public FirewallExceptionRemoveRequestRequisitionPojo getRemoveRequisition() {
		return removeRequisition;
	}

	public void setRemoveRequisition(FirewallExceptionRemoveRequestRequisitionPojo removeRequisition) {
		this.removeRequisition = removeRequisition;
	}

	@Override
	public boolean isAddRequest() {
		return this.isAdd;
	}
}
