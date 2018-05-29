package edu.emory.oit.vpcprovisioning.presenter.vpc;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;

public class MaintainVpcPresenter extends PresenterBase implements MaintainVpcView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String vpcId;
	private VpcPojo vpc;
	private VpcRequisitionPojo vpcRequisition;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new VPC.
	 */
	public MaintainVpcPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpc = null;
		this.vpcId = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainVpcView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public MaintainVpcPresenter(ClientFactory clientFactory, VpcPojo vpc) {
		this.isEditing = true;
		this.vpcId = vpc.getVpcId();
		this.clientFactory = clientFactory;
		this.vpc = vpc;
		clientFactory.getMaintainVpcView().setPresenter(this);
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
		if (vpcId == null) {
			clientFactory.getShell().setSubTitle("Generate VPC");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit VPC");
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
						getView().hidePleaseWaitPanel();
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving VPC types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving VPC types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						getView().initPage();
						getView().setVpcTypeItems(result);
						getView().setInitialFocus();
						// apply authorization mask
						if (user.isLitsAdmin()) {
							getView().applyCentralAdminMask();
						}
						else if (user.isAdminForAccount(vpc.getAccountId())) {
							getView().applyAWSAccountAdminMask();
						}
						else if (user.isAuditorForAccount(vpc.getAccountId())) {
							clientFactory.getShell().setSubTitle("View VPC");
							getView().applyAWSAccountAuditorMask();
						}
						else {
							getView().applyAWSAccountAuditorMask();
							getView().showMessageToUser("An error has occurred.  The user logged in does not "
									+ "appear to be associated to any valid roles for this page.");
							// TODO: need to not show them the item???
						}
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
					}
				};
				VpcProvisioningService.Util.getInstance().getVpcTypeItems(callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain vpc: create/generate");
		isEditing = false;
		getView().setEditing(false);
		vpcRequisition = new VpcRequisitionPojo();
	}

	private void startEdit() {
		GWT.log("Maintain vpc presenter: edit.  VPC: " + getVpc().getVpcId());
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the vpc is loaded.
		getView().setLocked(true);
		getView().setEditing(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainVpcView().setLocked(false);
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
		ActionEvent.fire(eventBus, ActionNames.VPC_EDITING_CANCELED);
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
	public void saveVpc() {
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
				ActionEvent.fire(eventBus, ActionNames.VPC_SAVED, vpc);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().generateVpc(vpcRequisition, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateVpc(vpc, callback);
		}
	}

	@Override
	public VpcPojo getVpc() {
		return this.vpc;
	}

	@Override
	public boolean isValidVpcId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private MaintainVpcView getView() {
		return clientFactory.getMaintainVpcView();
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
	public VpcRequisitionPojo getVpcRequisition() {
		return vpcRequisition;
	}

	@Override
	public void setSpeedChartStatusForKeyOnWidget(String key, final Widget w) {
		AsyncCallback<SpeedChartPojo> callback = new AsyncCallback<SpeedChartPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server exception validating speedtype", caught);
				w.setTitle("Server exception validating speedtype");
				getView().setSpeedTypeStatus("Invalid account");
				getView().setSpeedTypeColor(Constants.COLOR_RED);
			}

			@Override
			public void onSuccess(SpeedChartPojo scp) {
				if (scp == null) {
					w.setTitle("Invalid account number, can't validate this number");
					getView().setSpeedTypeStatus("Invalid account");
					getView().setSpeedTypeColor(Constants.COLOR_RED);
				}
				else {
				    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
				    String status = scp.getEuValidityDescription() + 
							"  End date: " + dateFormat.format(scp.getEuProjectEndDate()); 
					w.setTitle(status);
					getView().setSpeedTypeStatus(status);
					getView().setSpeedTypeColor(Constants.COLOR_GREEN);
				}
			}
		};
		if (key != null && key.length() > 0) {
			SpeedChartQueryFilterPojo filter = new SpeedChartQueryFilterPojo();
			filter.getSpeedChartKeys().add(key);
			VpcProvisioningService.Util.getInstance().getSpeedChartForFinancialAccountNumber(key, callback);
		}
		else {
			GWT.log("null key, can't validate yet");
		}
	}

	@Override
	public void setSpeedChartStatusForKey(String key, Label label) {
		// null check / length
		if (key == null || key.length() != 10) {
			label.setText("Invalid length");
			getView().setSpeedTypeColor(Constants.COLOR_RED);
			return;
		}
		// TODO: numeric characters
		
		setSpeedChartStatusForKeyOnWidget(key, getView().getSpeedTypeWidget());
	}
}
