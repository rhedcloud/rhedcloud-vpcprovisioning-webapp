package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.shared.RemoteVpnConnectionInfoPojo;
import edu.emory.oit.vpcprovisioning.shared.TunnelProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;

public class DesktopMaintainVpnConnectionProvisioning extends ViewImplBase implements MaintainVpnConnectionProvisioningView {
	Presenter presenter;
	boolean editing;
	boolean locked;
//	boolean deprovision;
	boolean reprovision;
	UserAccountPojo userLoggedIn;
	private ListDataProvider<TunnelProfilePojo> tunnelProfileDataProvider = new ListDataProvider<TunnelProfilePojo>();
	private SingleSelectionModel<TunnelProfilePojo> tunnelProfileSelectionModel;
	private ListDataProvider<RemoteVpnConnectionInfoPojo> remoteVpnConnectionDataProvider = new ListDataProvider<RemoteVpnConnectionInfoPojo>();
	private SingleSelectionModel<RemoteVpnConnectionInfoPojo> remoteVpnConnectionSelectionModel;
	List<VpcPojo> vpcItems = new java.util.ArrayList<VpcPojo>();

	private static DesktopMaintainVpnConnectionProvisioningUiBinder uiBinder = GWT
			.create(DesktopMaintainVpnConnectionProvisioningUiBinder.class);

	interface DesktopMaintainVpnConnectionProvisioningUiBinder
			extends UiBinder<Widget, DesktopMaintainVpnConnectionProvisioning> {
	}

	public DesktopMaintainVpnConnectionProvisioning() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface MyCellTableResources extends CellTable.Resources {

		@Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

	@UiField TextBox vpcTB;
	@UiField ListBox vpcLB;
//	@UiField TextBox remoteVpnIpAddressTB;
//	@UiField TextArea presharedKeyTA;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField(provided=true) CellTable<TunnelProfilePojo> tunnelProfileTable = new CellTable<TunnelProfilePojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField(provided=true) CellTable<RemoteVpnConnectionInfoPojo> remoteVpnConnectionTable = new CellTable<RemoteVpnConnectionInfoPojo>(5, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField Label headerLabel;
//	@UiField TextBox remoteVpnConnectionIdTB;
	
	@UiHandler ("vpcLB")
	void vpcLbChanged(ChangeEvent e) {
		if (vpcLB.getSelectedIndex() == 0) {
			presenter.setSelectedVpc(null);
		}
		else {
			presenter.setSelectedVpc(vpcItems.get(vpcLB.getSelectedIndex() - 1));
			GWT.log("selected vpc is: " + presenter.getSelectedVpc().getVpcId());
		}
	}
	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		// populate requisition object, generate VPNCP or VPNCDP
		if (presenter.getSelectedVpc() != null) {
			presenter.getVpnConnectionRequisition().setOwnerId(presenter.getSelectedVpc().getVpcId());
		}
		else {
			presenter.getVpnConnectionRequisition().setOwnerId(null);
		}
//		presenter.getVpnConnectionRequisition().setRemoteVpnIpAddress(remoteVpnIpAddressTB.getText());
//		if (!this.isValidIp(presenter.getVpnConnectionRequisition().getRemoteVpnIpAddress())) {
//			showMessageToUser("Invalid Remote VPN IP address.  Please enter a valid IP address.");
//			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
//		        public void execute () {
//		        	remoteVpnIpAddressTB.setFocus(true);
//		        }
//		    });
//			return;
//		}
//		presenter.getVpnConnectionRequisition().setPresharedKey(presharedKeyTA.getText());
//		if (!this.deprovision) {
			// provision generate/update (provsion or re-provision)
			presenter.saveVpnConnectionProvisioning();
//		}
//		else {
//			// do a de-provision generate
//			presenter.saveVpnConnectionDeprovisioning();
//		}
	}
	
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_VPN_CONNECTION_PROFILE);
	}

	@Override
	public void hidePleaseWaitPanel() {
		
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		
		
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	vpcLB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return vpcLB;
	}

	@Override
	public void applyNetworkAdminMask() {
//		if (deprovision || reprovision) {
		if (reprovision) {
			// if this is a de-provision or a re-provision, the VPC id can't be changed
			vpcLB.setEnabled(false);
		}
		else {
			vpcLB.setEnabled(true);
		}
//		remoteVpnIpAddressTB.setEnabled(true);
//		presharedKeyTA.setEnabled(true);
		okayButton.setEnabled(true);
	}

	@Override
	public void applyCentralAdminMask() {
		vpcLB.setEnabled(false);
//		remoteVpnIpAddressTB.setEnabled(false);
//		presharedKeyTA.setEnabled(false);
		okayButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		vpcLB.setEnabled(false);
//		remoteVpnIpAddressTB.setEnabled(false);
//		presharedKeyTA.setEnabled(false);
		okayButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		vpcLB.setEnabled(false);
//		remoteVpnIpAddressTB.setEnabled(false);
//		presharedKeyTA.setEnabled(false);
		okayButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		VpnConnectionRequisitionPojo req = presenter.getVpnConnectionRequisition();
		if (req.getOwnerId() == null || req.getOwnerId().length() == 0) {
			fields.add(vpcLB);
		}
//		if (req.getRemoteVpnIpAddress() == null || req.getRemoteVpnIpAddress().length() == 0) {
//			fields.add(remoteVpnIpAddressTB);
//		}
//		if (req.getPresharedKey() == null || req.getPresharedKey().length() == 0) {
//			fields.add(presharedKeyTA);
//		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(vpcLB);
//		fields.add(remoteVpnIpAddressTB);
//		fields.add(presharedKeyTA);
		this.resetFieldStyles(fields);
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return okayButton;
	}

	@Override
	public void vpcpPromptOkay(String valueEntered) {
		
		
	}

	@Override
	public void vpcpPromptCancel() {
		
		
	}

	@Override
	public void vpcpConfirmOkay() {
		
		
	}

	@Override
	public void vpcpConfirmCancel() {
		
		
	}

	@Override
	public void disableButtons() {
		
		
	}

	@Override
	public void enableButtons() {
		
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		
		
	}

	@Override
	public void setVpnConnectionProvisioningIdViolation(String message) {
		
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		this.setFieldViolations(false);
		this.registerHandlers();

		if (presenter.getVpnConnectionRequisition().getProfile() != null) {
			vpcTB.setText(presenter.getVpnConnectionRequisition().getProfile().getVpcNetwork());
			this.initializeTunnelProfileListTable();
		}
		this.initializeRemoteVpnConnectionListTable();

//		remoteVpnIpAddressTB.setText("");
//		presharedKeyTA.setText("");
	}

	private void registerHandlers() {
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
	}

	private Widget initializeRemoteVpnConnectionListTable() {
		GWT.log("initializing Remote VPN Connection info list table...");
		remoteVpnConnectionTable.setTableLayoutFixed(false);
		remoteVpnConnectionTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		remoteVpnConnectionTable.setVisibleRange(0, 15);

		// create dataprovider
		remoteVpnConnectionDataProvider = new ListDataProvider<RemoteVpnConnectionInfoPojo>();
		remoteVpnConnectionDataProvider.addDataDisplay(remoteVpnConnectionTable);
		remoteVpnConnectionDataProvider.getList().clear();
		remoteVpnConnectionDataProvider.getList().addAll(presenter.getVpnConnectionRequisition().getRemoteVpnConnectionInfo());

		remoteVpnConnectionSelectionModel = 
				new SingleSelectionModel<RemoteVpnConnectionInfoPojo>(RemoteVpnConnectionInfoPojo.KEY_PROVIDER);
		remoteVpnConnectionTable.setSelectionModel(remoteVpnConnectionSelectionModel);

		remoteVpnConnectionSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				RemoteVpnConnectionInfoPojo m = remoteVpnConnectionSelectionModel.getSelectedObject();
			}
		});

		ListHandler<RemoteVpnConnectionInfoPojo> sortHandler = 
				new ListHandler<RemoteVpnConnectionInfoPojo>(remoteVpnConnectionDataProvider.getList());
		remoteVpnConnectionTable.addColumnSortHandler(sortHandler);

		if (remoteVpnConnectionTable.getColumnCount() == 0) {
			initRemoteVpnConnectionTableColumns(sortHandler);
		}

		return remoteVpnConnectionTable;
	}

	private void initRemoteVpnConnectionTableColumns(ListHandler<RemoteVpnConnectionInfoPojo> sortHandler) {
		// vpn number
		Column<RemoteVpnConnectionInfoPojo, String> vpnNumberColumn = 
				new Column<RemoteVpnConnectionInfoPojo, String> (new TextCell()) {

			@Override
			public String getValue(RemoteVpnConnectionInfoPojo object) {
				return Integer.toString(object.getVpnConnectionNumber());
			}
		};
		vpnNumberColumn.setSortable(true);
		sortHandler.setComparator(vpnNumberColumn, new Comparator<RemoteVpnConnectionInfoPojo>() {
			public int compare(RemoteVpnConnectionInfoPojo o1, RemoteVpnConnectionInfoPojo o2) {
				if (o1.getVpnConnectionNumber() > o2.getVpnConnectionNumber()) {
					return 1;
				}
				else if (o1.getVpnConnectionNumber() < o2.getVpnConnectionNumber()) {
					return -1;
				}
				return 0;
			}
		});
		remoteVpnConnectionTable.addColumn(vpnNumberColumn, "VPN Connection Number");
		
//		contactList.setColumnWidth(textInputColumn, 16.0, Unit.EM);
		// connection id column
		Column<RemoteVpnConnectionInfoPojo, String> vpnConnectionIdColumn = 
				new Column<RemoteVpnConnectionInfoPojo, String> (new TextInputCell()) {

			@Override
			public String getValue(RemoteVpnConnectionInfoPojo object) {
				return object.getRemoteVpnConnectionId();
			}
			@Override
			public void render(Context context, RemoteVpnConnectionInfoPojo object, SafeHtmlBuilder sb) {
				final SafeHtml INPUT_TEXT_DISABLED = 
					SafeHtmlUtils.fromSafeConstant("<input type=\"text\" tabindex=\"-1\" "
						+ "disabled=\"disabled\" value=\"" + object.getRemoteVpnConnectionId() + "\"/>");
				sb.append(INPUT_TEXT_DISABLED);
			}
		};
		vpnConnectionIdColumn.setFieldUpdater(new FieldUpdater<RemoteVpnConnectionInfoPojo, String>() {
	    	@Override
	    	public void update(int index, RemoteVpnConnectionInfoPojo object, String value) {
	    		object.setRemoteVpnConnectionId(value);
//				pendingChanges.add(new LastNameChange(object, value));
	    	}
	    });
		vpnConnectionIdColumn.setSortable(true);
		sortHandler.setComparator(vpnConnectionIdColumn, new Comparator<RemoteVpnConnectionInfoPojo>() {
			public int compare(RemoteVpnConnectionInfoPojo o1, RemoteVpnConnectionInfoPojo o2) {
				return o1.getRemoteVpnConnectionId().compareTo(o2.getRemoteVpnConnectionId());
			}
		});
		remoteVpnConnectionTable.addColumn(vpnConnectionIdColumn, "Remote VPN Connection ID");

		// tunnel id column
		Column<RemoteVpnConnectionInfoPojo, String> tunnelIdColumn = 
				new Column<RemoteVpnConnectionInfoPojo, String> (new TextInputCell()) {

			@Override
			public String getValue(RemoteVpnConnectionInfoPojo object) {
				return object.getRemoteVpnTunnels().get(0).getLocalTunnelId();
				// TODO: have to check the vpn connection number and get the associated tunnel
//				if (object.getVpnConnectionNumber() == 1) {
//					return object.getRemoteVpnTunnels().get(0).getLocalTunnelId();
//				}
//				else {
//				}
			}
		};
		tunnelIdColumn.setFieldUpdater(new FieldUpdater<RemoteVpnConnectionInfoPojo, String>() {
	    	@Override
	    	public void update(int index, RemoteVpnConnectionInfoPojo object, String value) {
	    		object.getRemoteVpnTunnels().get(0).setLocalTunnelId(value);
//				pendingChanges.add(new LastNameChange(object, value));
	    	}
	    });
		tunnelIdColumn.setSortable(true);
		sortHandler.setComparator(tunnelIdColumn, new Comparator<RemoteVpnConnectionInfoPojo>() {
			public int compare(RemoteVpnConnectionInfoPojo o1, RemoteVpnConnectionInfoPojo o2) {
				return o1.getRemoteVpnTunnels().get(0).getLocalTunnelId().compareTo(o2.getRemoteVpnTunnels().get(0).getLocalTunnelId());
			}
		});
		remoteVpnConnectionTable.addColumn(tunnelIdColumn, "Local Tunnel ID");
		
		// remote vpn ip addres column
		Column<RemoteVpnConnectionInfoPojo, String> remoteVpnIpAddressColumn = 
				new Column<RemoteVpnConnectionInfoPojo, String> (new TextInputCell()) {

			@Override
			public String getValue(RemoteVpnConnectionInfoPojo object) {
				return object.getRemoteVpnTunnels().get(0).getRemoteVpnIpAddress();
				// TODO: have to check the vpn connection number and get the associated tunnel
//				if (object.getVpnConnectionNumber() == 1) {
//					return object.getRemoteVpnTunnels().get(0).getLocalTunnelId();
//				}
//				else {
//				}
			}
		};
		remoteVpnIpAddressColumn.setFieldUpdater(new FieldUpdater<RemoteVpnConnectionInfoPojo, String>() {
	    	@Override
	    	public void update(int index, RemoteVpnConnectionInfoPojo object, String value) {
	    		object.getRemoteVpnTunnels().get(0).setRemoteVpnIpAddress(value);
//				pendingChanges.add(new LastNameChange(object, value));
	    	}
	    });
		remoteVpnIpAddressColumn.setSortable(true);
		sortHandler.setComparator(remoteVpnIpAddressColumn, new Comparator<RemoteVpnConnectionInfoPojo>() {
			public int compare(RemoteVpnConnectionInfoPojo o1, RemoteVpnConnectionInfoPojo o2) {
				return o1.getRemoteVpnTunnels().get(0).getRemoteVpnIpAddress().compareTo(o2.getRemoteVpnTunnels().get(0).getRemoteVpnIpAddress());
			}
		});
		remoteVpnConnectionTable.addColumn(remoteVpnIpAddressColumn, "Remote VPN IP Address");
		
		// preshared key column
		Column<RemoteVpnConnectionInfoPojo, String> presharedKeyColumn = 
				new Column<RemoteVpnConnectionInfoPojo, String> (new TextInputCell()) {

			@Override
			public String getValue(RemoteVpnConnectionInfoPojo object) {
				return object.getRemoteVpnTunnels().get(0).getPresharedKey();
				// TODO: have to check the vpn connection number and get the associated tunnel
//				if (object.getVpnConnectionNumber() == 1) {
//					return object.getRemoteVpnTunnels().get(0).getLocalTunnelId();
//				}
//				else {
//				}
			}
		};
		presharedKeyColumn.setFieldUpdater(new FieldUpdater<RemoteVpnConnectionInfoPojo, String>() {
	    	@Override
	    	public void update(int index, RemoteVpnConnectionInfoPojo object, String value) {
	    		object.getRemoteVpnTunnels().get(0).setPresharedKey(value);
//				pendingChanges.add(new LastNameChange(object, value));
	    	}
	    });
		presharedKeyColumn.setSortable(true);
		sortHandler.setComparator(presharedKeyColumn, new Comparator<RemoteVpnConnectionInfoPojo>() {
			public int compare(RemoteVpnConnectionInfoPojo o1, RemoteVpnConnectionInfoPojo o2) {
				return o1.getRemoteVpnTunnels().get(0).getPresharedKey().compareTo(o2.getRemoteVpnTunnels().get(0).getPresharedKey());
			}
		});
		remoteVpnConnectionTable.addColumn(presharedKeyColumn, "Pre-Shared Key");
		
		// VPN Inside IP CIDR key column
		Column<RemoteVpnConnectionInfoPojo, String> vpnInsideIpCidrColumn = 
				new Column<RemoteVpnConnectionInfoPojo, String> (new TextInputCell()) {

			@Override
			public String getValue(RemoteVpnConnectionInfoPojo object) {
				return object.getRemoteVpnTunnels().get(0).getVpnInsideCidr();
				// TODO: have to check the vpn connection number and get the associated tunnel
//				if (object.getVpnConnectionNumber() == 1) {
//					return object.getRemoteVpnTunnels().get(0).getLocalTunnelId();
//				}
//				else {
//				}
			}
			@Override
			public void render(Context context, RemoteVpnConnectionInfoPojo object, SafeHtmlBuilder sb) {
				final SafeHtml INPUT_TEXT_DISABLED = 
					SafeHtmlUtils.fromSafeConstant("<input type=\"text\" tabindex=\"-1\" "
						+ "disabled=\"disabled\" value=\"" + object.getRemoteVpnTunnels().get(0).getVpnInsideCidr() + "\"/>");
				sb.append(INPUT_TEXT_DISABLED);
			}
		};
		vpnInsideIpCidrColumn.setFieldUpdater(new FieldUpdater<RemoteVpnConnectionInfoPojo, String>() {
	    	@Override
	    	public void update(int index, RemoteVpnConnectionInfoPojo object, String value) {
	    		object.getRemoteVpnTunnels().get(0).setVpnInsideCidr(value);
//				pendingChanges.add(new LastNameChange(object, value));
	    	}
	    });
		vpnInsideIpCidrColumn.setSortable(true);
		sortHandler.setComparator(vpnInsideIpCidrColumn, new Comparator<RemoteVpnConnectionInfoPojo>() {
			public int compare(RemoteVpnConnectionInfoPojo o1, RemoteVpnConnectionInfoPojo o2) {
				return o1.getRemoteVpnTunnels().get(0).getVpnInsideCidr().compareTo(o2.getRemoteVpnTunnels().get(0).getVpnInsideCidr());
			}
		});
		remoteVpnConnectionTable.addColumn(vpnInsideIpCidrColumn, "VPN Inside IP CIDR");
	}
	
	private Widget initializeTunnelProfileListTable() {
		GWT.log("initializing Tunnel Profile list table...");
		tunnelProfileTable.setTableLayoutFixed(false);
		tunnelProfileTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		tunnelProfileTable.setVisibleRange(0, 15);

		// create dataprovider
		tunnelProfileDataProvider = new ListDataProvider<TunnelProfilePojo>();
		tunnelProfileDataProvider.addDataDisplay(tunnelProfileTable);
		tunnelProfileDataProvider.getList().clear();
		tunnelProfileDataProvider.getList().addAll(presenter.getVpnConnectionRequisition().getProfile().getTunnelProfiles());

		tunnelProfileSelectionModel = 
				new SingleSelectionModel<TunnelProfilePojo>(TunnelProfilePojo.KEY_PROVIDER);
		tunnelProfileTable.setSelectionModel(tunnelProfileSelectionModel);

		tunnelProfileSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				TunnelProfilePojo m = tunnelProfileSelectionModel.getSelectedObject();
			}
		});

		ListHandler<TunnelProfilePojo> sortHandler = 
				new ListHandler<TunnelProfilePojo>(tunnelProfileDataProvider.getList());
		tunnelProfileTable.addColumnSortHandler(sortHandler);

		if (tunnelProfileTable.getColumnCount() == 0) {
			initTunnelProfileListTableColumns(sortHandler);
		}

		return tunnelProfileTable;
	}
	
	private void initTunnelProfileListTableColumns(ListHandler<TunnelProfilePojo> sortHandler) {
		GWT.log("initializing TunnelProfile list table columns...");

		// tunnel id column
		Column<TunnelProfilePojo, String> provIdColumn = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getTunnelId();
			}
		};
		provIdColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getTunnelId().compareTo(o2.getTunnelId());
			}
		});
		tunnelProfileTable.addColumn(provIdColumn, "Tunnel ID");

		Column<TunnelProfilePojo, String> column2 = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getCryptoKeyringName();
			}
		};
		column2.setSortable(true);
		sortHandler.setComparator(column2, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getCryptoKeyringName().compareTo(o2.getCryptoKeyringName());
			}
		});
		tunnelProfileTable.addColumn(column2, "Crypto Keyring");
		
		Column<TunnelProfilePojo, String> column3 = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getIsakampProfileName();
			}
		};
		column3.setSortable(true);
		sortHandler.setComparator(column3, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getIsakampProfileName().compareTo(o2.getIsakampProfileName());
			}
		});
		tunnelProfileTable.addColumn(column3, "ISAKAMP Profile");

		Column<TunnelProfilePojo, String> column4 = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getIpsecProfileName();
			}
		};
		column4.setSortable(true);
		sortHandler.setComparator(column4, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getIpsecProfileName().compareTo(o2.getIpsecProfileName());
			}
		});
		tunnelProfileTable.addColumn(column4, "IPSEC Profile");

		Column<TunnelProfilePojo, String> column5 = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getIpsecTransformSetName();
			}
		};
		column5.setSortable(true);
		sortHandler.setComparator(column5, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getIpsecTransformSetName().compareTo(o2.getIpsecTransformSetName());
			}
		});
		tunnelProfileTable.addColumn(column5, "IPSEC Transform Set");

		Column<TunnelProfilePojo, String> column6 = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getCustomerGatewayIp();
			}
		};
		column6.setSortable(true);
		sortHandler.setComparator(column6, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getCustomerGatewayIp().compareTo(o2.getCustomerGatewayIp());
			}
		});
		tunnelProfileTable.addColumn(column6, "Customer Gateway IP");

		Column<TunnelProfilePojo, String> column7 = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getVpnInsideIpCidr1();
			}
		};
		column7.setSortable(true);
		sortHandler.setComparator(column7, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getVpnInsideIpCidr1().compareTo(o2.getVpnInsideIpCidr1());
			}
		});
		tunnelProfileTable.addColumn(column7, "VPN Inside CIDR 1");

		Column<TunnelProfilePojo, String> column8 = 
				new Column<TunnelProfilePojo, String> (new TextCell()) {

			@Override
			public String getValue(TunnelProfilePojo object) {
				return object.getVpnInsideIpCidr2();
			}
		};
		column8.setSortable(true);
		sortHandler.setComparator(column8, new Comparator<TunnelProfilePojo>() {
			public int compare(TunnelProfilePojo o1, TunnelProfilePojo o2) {
				return o1.getVpnInsideIpCidr2().compareTo(o2.getVpnInsideIpCidr2());
			}
		});
		tunnelProfileTable.addColumn(column8, "VPN Inside CIDR 2");
	}
	@Override
	public void setVpcItems(List<VpcPojo> vpcs) {
		this.vpcItems = vpcs;
		
		vpcLB.clear();
//		if (deprovision || reprovision) {
		if (reprovision) {
			// de-provision OR re-provision
			if (vpcItems != null) {
				for (VpcPojo vpc : vpcItems) {
					vpcLB.addItem(vpc.getVpcId() + " - " + vpc.getAccountName());
				}
				vpcLB.setSelectedIndex(0);
				vpcLB.setEnabled(false);
			}
		}
		else {
			vpcLB.addItem("-- Select --", "");
			if (vpcItems != null) {
				for (VpcPojo vpc : vpcItems) {
					vpcLB.addItem(vpc.getVpcId() + " - " + vpc.getAccountName());
				}
			}
		}
	}
//	@Override
//	public void setDeprovisioning(boolean isDeprovision) {
//		this.deprovision = isDeprovision;
//	}
	@Override
	public void setReprovisioning(boolean isReprovision) {
		this.reprovision = isReprovision;
	}
	@Override
	public void setHeading(String heading) {
		headerLabel.setText(heading);
	}
}
