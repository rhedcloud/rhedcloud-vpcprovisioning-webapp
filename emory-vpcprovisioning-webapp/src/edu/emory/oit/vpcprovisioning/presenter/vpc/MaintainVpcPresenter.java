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
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TunnelInterfacePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionQueryResultPojo;

public class MaintainVpcPresenter extends PresenterBase implements MaintainVpcView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String vpcId;
	private VpcPojo vpc;
	private VpcRequisitionPojo vpcRequisition;
	private VpnConnectionPojo vpnConnection;
	PropertyPojo selectedProperty;

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
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		getView().showPleaseWaitDialog("Retrieving VPC details...");
		setReleaseInfo(clientFactory);
		if (vpcId == null) {
			clientFactory.getShell().setSubTitle("Generate VPC");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit VPC");
			startEdit();
		}
		getView().initDataEntryPanels();
		
		if (isEditing) {
			refreshVpnConnectionInfo();
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
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the VPCs you're associated to.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().enableButtons();
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
						if (user.isCentralAdmin()) {
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
		getView().showPleaseWaitDialog("Saving VPC...");
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
//			VpcProvisioningService.Util.getInstance().generateVpc(vpcRequisition, callback);
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

	public VpnConnectionPojo getVpnConnection() {
		return vpnConnection;
	}

	public void setVpnConnection(VpnConnectionPojo vpnConnection) {
		this.vpnConnection = vpnConnection;
	}

	@Override
	public void refreshVpnConnectionInfo() {
		AsyncCallback<VpnConnectionQueryResultPojo> vpnConnectionCB = new AsyncCallback<VpnConnectionQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hideVpnConnectionPleaseWaitDialog();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the VPN Connection information for this VPC.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(VpnConnectionQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " VPN Connections back.");
				if (result.getResults().size() > 0) {
					vpnConnection = result.getResults().get(0);
				}
				else {
					vpnConnection = null;
				}
				if (vpnConnection != null) {
					if (vpnConnection.getTunnelInterfaces().size() == 2) {
						TunnelInterfacePojo t1 = vpnConnection.getTunnelInterfaces().get(0);
						TunnelInterfacePojo t2 = vpnConnection.getTunnelInterfaces().get(1);
						if (t1.isOperational()) {
							getView().setTunnel1StatusGood();
						}
						else {
							getView().setTunnel1StatusBad(t1.getBadStateReasons().toString());
						}
						if (t2.isOperational()) {
							getView().setTunnel2StatusGood();
						}
						else {
							getView().setTunnel2StatusBad(t2.getBadStateReasons().toString());
						}
						if (t1.isOperational() && t2.isOperational()) {
							getView().setOperationalStatusSummary("Both tunnels are operational so this VPN "
									+ "Connection should be in working order.");
						}
						else if (t1.isOperational()) {
							getView().setOperationalStatusSummary("Tunnel 1 is operational so this VPN "
									+ "Connection should be in working order.");
						}
						else if (t2.isOperational()) {
							getView().setOperationalStatusSummary("Tunnel 2 is operational so this VPN "
									+ "Connection should be in working order.");
						}
						else {
							getView().setOperationalStatusSummary("Neither tunnel interface is operatonal "
									+ "at this time so this VPN Connection will NOT work based on it's current status.  "
									+ "You can refresh the status and this may change as the VPN Connection "
									+ "is built which can take some time.\n\nHover your mouse over the "
									+ "red circles above for more details.");
						}
					}
					else {
						// TODO: something goofy with the tunnel interfaces
					}
				}
				getView().refreshVpnConnectionInfo(vpnConnection);
				getView().hideVpnConnectionPleaseWaitDialog();
			}
		};
		getView().showVpnConnectionPleaseWaitDialog("Refreshing VPN Connection info...");
		getView().setVpnRefreshing();
		VpnConnectionQueryFilterPojo vpnFilter = new VpnConnectionQueryFilterPojo();
		vpnFilter.setVpcId(getVpcId());
		VpcProvisioningService.Util.getInstance().getVpnConnectionsForFilter(vpnFilter, vpnConnectionCB);
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
