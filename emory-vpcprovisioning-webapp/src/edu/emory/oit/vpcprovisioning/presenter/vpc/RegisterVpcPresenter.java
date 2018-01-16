package edu.emory.oit.vpcprovisioning.presenter.vpc;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class RegisterVpcPresenter implements RegisterVpcView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String vpcId;
	private VpcPojo vpc;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new VPC.
	 */
	public RegisterVpcPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpc = null;
		this.vpcId = null;
		this.clientFactory = clientFactory;
		clientFactory.getRegisterVpcView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public RegisterVpcPresenter(ClientFactory clientFactory, VpcPojo vpc) {
		this.isEditing = true;
		this.vpcId = vpc.getVpcId();
		this.clientFactory = clientFactory;
		this.vpc = vpc;
		clientFactory.getRegisterVpcView().setPresenter(this);
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
		
		if (vpcId == null) {
			clientFactory.getShell().setSubTitle("Register VPC");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Register VPC");
			startEdit();
		}
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception retrieving userLoggedIn", caught);
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				GWT.log("got user logged in: " + user.getEppn());
				getView().setUserLoggedIn(user);
				AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving VPC types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving VPC types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(final List<String> vpcItems) {
						// get list of accounts from server and add them 
						// to the view
						GWT.log("got vpc type items: " + vpcItems.size());
						AsyncCallback<AccountQueryResultPojo> callback = new AsyncCallback<AccountQueryResultPojo>() {
							@Override
							public void onFailure(Throwable caught) {
								getView().hidePleaseWaitDialog();
								getView().hidePleaseWaitPanel();
								GWT.log("Exception retrieving AWS accounts", caught);
								getView().showMessageToUser("There was an exception on the " +
										"server retrieving a list of AWS Accounts.  Message " +
										"from server is: " + caught.getMessage());
							}

							@Override
							public void onSuccess(AccountQueryResultPojo accountItems) {
								GWT.log("got " + accountItems.getResults().size() + " accounts.");
								getView().setVpcTypeItems(vpcItems);
								getView().setAccountItems(accountItems.getResults());
								getView().initPage();
								getView().setInitialFocus();
								// apply authorization mask
								if (user.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
									getView().applyEmoryAWSAdminMask();
								}
								else if (user.hasPermission(Constants.PERMISSION_VIEW_EVERYTHING)) {
									getView().applyEmoryAWSAuditorMask();
								}
								else {
									// ??
								}
								getView().hidePleaseWaitDialog();
								getView().hidePleaseWaitPanel();
							}
						};
						GWT.log("getting accounts");
						VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, callback);
					}
				};
				GWT.log("getting vpc type items");
				VpcProvisioningService.Util.getInstance().getVpcTypeItems(callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Register vpc:  startCreate");
		isEditing = false;
//		getView().setEditing(false);
	}

	private void startEdit() {
		GWT.log("Register vpc presenter: edit.  VPC: " + getVpc().getVpcId());
		isEditing = true;
//		getView().setEditing(true);
		// Lock the display until the vpc is loaded.
		getView().setLocked(true);
//		getView().setEditing(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getRegisterVpcView().setLocked(false);
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
	public void deleteVpc() {
		if (isEditing) {
			doDeleteVpc();
		} else {
			doCancelVpc();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelVpc() {
		ActionEvent.fire(eventBus, ActionNames.VPC_REGISTRATION_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteVpc() {
		if (vpc == null) {
			return;
		}

		// TODO Delete the vpc on server then fire onVpcDeleted();
	}

	@Override
	public void registerVpc() {
		getView().showPleaseWaitDialog();
		AsyncCallback<VpcPojo> callback = new AsyncCallback<VpcPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Vpc", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Vpc.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpcPojo result) {
				getView().hidePleaseWaitDialog();
//				ActionEvent.fire(eventBus, ActionNames.VPC_SAVED, vpc);
				
				// TODO: once the VPC has been registered, display a message
				// and take the user to the MaintainCidrAssigmnent page
				ActionEvent.fire(eventBus, ActionNames.CREATE_CIDR_ASSIGNMENT_AFTER_VPC_REGISTRATION);

			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().registerVpc(vpc, callback);
		}
		else {
			// error condition
		}
	}

	@Override
	public VpcPojo getVpc() {
		if (this.vpc == null) {
			this.vpc = new VpcPojo();
		}
		return this.vpc;
	}

	@Override
	public boolean isValidVpcId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private RegisterVpcView getView() {
		return clientFactory.getRegisterVpcView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}
}
