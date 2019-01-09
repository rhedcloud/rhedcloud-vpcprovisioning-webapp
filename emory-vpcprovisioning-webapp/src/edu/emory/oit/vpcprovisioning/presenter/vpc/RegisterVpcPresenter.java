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
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class RegisterVpcPresenter extends PresenterBase implements RegisterVpcView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String vpcId;
	private VpcPojo vpc;
	PropertyPojo selectedProperty;

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
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving accounts from AWS Account Service...");
		
		if (vpcId == null) {
			clientFactory.getShell().setSubTitle("Register VPC");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Register VPC");
			startEdit();
		}
		
		AsyncCallback<List<AWSRegionPojo>> regionCB = new AsyncCallback<List<AWSRegionPojo>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<AWSRegionPojo> result) {
				getView().setAwsRegionItems(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getAwsRegionItems(regionCB);

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().disableButtons();
				GWT.log("Exception retrieving the User Logged in.", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().enableButtons();
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().setInitialFocus();
				
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (vpc != null) {
					if (user.isAdminForAccount(vpc.getAccountId())) {
						getView().applyAWSAccountAdminMask();
					}
					else if (user.isAuditorForAccount(vpc.getAccountId())) {
						getView().applyAWSAccountAuditorMask();
					}
				}
				else if (user.isAuditor()) {
					getView().applyAWSAccountAuditorMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles for this page.");
				}

				AsyncCallback<AccountQueryResultPojo> acct_cb = new AsyncCallback<AccountQueryResultPojo>() {
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
						getView().setAccountItems(accountItems.getResults());
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
					}
				};
				GWT.log("getting accounts");
				VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, acct_cb);

				AsyncCallback<List<String>> vpctype_cb = new AsyncCallback<List<String>>() {
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
						// get list of vpc types from the server and add them 
						// to the view
						GWT.log("got vpc type items: " + vpcItems.size());
						getView().setVpcTypeItems(vpcItems);
					}
				};
				GWT.log("getting vpc type items");
				VpcProvisioningService.Util.getInstance().getVpcTypeItems(vpctype_cb);
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
		getView().showPleaseWaitDialog("Registering VPC with the AWS Account service...");
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
				
				// once the VPC has been registered, display a message
				// and take the user to the MaintainCidrAssigmnent page
//				ActionEvent.fire(eventBus, ActionNames.CREATE_CIDR_ASSIGNMENT_AFTER_VPC_REGISTRATION);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPC);

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

	@Override
	public void setSelectedProperty(PropertyPojo prop) {
		this.selectedProperty = prop;
	}

	@Override
	public PropertyPojo getSelectedProperty() {
		return this.selectedProperty;
	}

	@Override
	public void updateProperty(PropertyPojo prop) {
		int index = -1;
		propertyLoop: for (int i=0; i<vpc.getProperties().size(); i++) {
			PropertyPojo tpp = vpc.getProperties().get(i);
			if (tpp.getName().equalsIgnoreCase(prop.getName())) {
				index = i;
				break propertyLoop;
			}
		}
		if (index >= 0) {
			GWT.log("Updating property: " + index);
			vpc.getProperties().remove(index);
			vpc.getProperties().add(prop);
		}
		else {
			GWT.log("Counldn't find a property to update...problem");
		}
	}
}
