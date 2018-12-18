package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.shared.TunnelProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;

public class DesktopMaintainVpnConnectionProvisioning extends ViewImplBase implements MaintainVpnConnectionProvisioningView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	boolean deprovision;
	boolean reprovision;
	UserAccountPojo userLoggedIn;
//	private final VpcRpcSuggestOracle vpcSuggestions = new VpcRpcSuggestOracle(Constants.SUGGESTION_TYPE_VPC_ID);
	private ListDataProvider<TunnelProfilePojo> dataProvider = new ListDataProvider<TunnelProfilePojo>();
	private SingleSelectionModel<TunnelProfilePojo> selectionModel;
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
//	@UiField(provided=true) SuggestBox vpcIdSB = new SuggestBox(vpcSuggestions, new TextBox());
//	@UiField TextBox ownerIdTB;
	@UiField ListBox vpcLB;
	@UiField TextBox remoteVpnIpAddressTB;
	@UiField TextArea presharedKeyTA;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField(provided=true) CellTable<TunnelProfilePojo> tunnelProfileTable = new CellTable<TunnelProfilePojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	
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
		presenter.getVpnConnectionRequisition().setRemoteVpnIpAddress(remoteVpnIpAddressTB.getText());
		if (!this.isValidIp(presenter.getVpnConnectionRequisition().getRemoteVpnIpAddress())) {
			showMessageToUser("Invalid Remote VPN IP address.  Please enter a valid IP address.");
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	remoteVpnIpAddressTB.setFocus(true);
		        }
		    });
			return;
		}
		presenter.getVpnConnectionRequisition().setPresharedKey(presharedKeyTA.getText());
		if (!this.deprovision) {
			// provision generate/update (provsion or re-provision)
			presenter.saveVpnConnectionProvisioning();
		}
		else {
			// do a de-provision generate
			presenter.saveVpnConnectionDeprovisioning();
		}
	}
	
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_VPN_CONNECTION_PROFILE);
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub
		
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
		if (deprovision || reprovision) {
			// if this is a de-provision or a re-provision, the VPC id can't be changed
			vpcLB.setEnabled(false);
		}
		else {
			vpcLB.setEnabled(true);
		}
		remoteVpnIpAddressTB.setEnabled(true);
		presharedKeyTA.setEnabled(true);
		okayButton.setEnabled(true);
	}

	@Override
	public void applyCentralAdminMask() {
		vpcLB.setEnabled(false);
		remoteVpnIpAddressTB.setEnabled(false);
		presharedKeyTA.setEnabled(false);
		okayButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		vpcLB.setEnabled(false);
		remoteVpnIpAddressTB.setEnabled(false);
		presharedKeyTA.setEnabled(false);
		okayButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		vpcLB.setEnabled(false);
		remoteVpnIpAddressTB.setEnabled(false);
		presharedKeyTA.setEnabled(false);
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
		if (req.getRemoteVpnIpAddress() == null || req.getRemoteVpnIpAddress().length() == 0) {
			fields.add(remoteVpnIpAddressTB);
		}
		if (req.getPresharedKey() == null || req.getPresharedKey().length() == 0) {
			fields.add(presharedKeyTA);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(vpcLB);
		fields.add(remoteVpnIpAddressTB);
		fields.add(presharedKeyTA);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpPromptCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmOkay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVpnConnectionProvisioningIdViolation(String message) {
		// TODO Auto-generated method stub
		
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

//		ownerLookupSB.setText("");
//		ownerLookupSB.getElement().setPropertyString("placeholder", "enter name");
		remoteVpnIpAddressTB.setText("");
		presharedKeyTA.setText("");
//		vpcIdSB.setText("");
//		vpcIdSB.getElement().setPropertyString("placeholder", "enter VPC id");
	}

	private void registerHandlers() {
//		ownerLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
//			@Override
//			public void onSelection(SelectionEvent<Suggestion> event) {
//				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
//				if (dp_suggestion.getDirectoryPerson() != null) {
//					presenter.setOwnerDirectoryPerson(dp_suggestion.getDirectoryPerson());
//					ownerLookupSB.setTitle(presenter.getOwnerDirectoryPerson().toString());
//				}
//			}
//		});
		
//		vpcIdSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
//			@Override
//			public void onSelection(SelectionEvent<Suggestion> event) {
//				VpcSuggestion suggestion = (VpcSuggestion)event.getSelectedItem();
//				if (suggestion.getVpc() != null) {
//					presenter.setSelectedVpc(suggestion.getVpc());
//					vpcIdSB.setTitle(presenter.getSelectedVpc().toString());
//				}
//			}
//		});
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	private Widget initializeTunnelProfileListTable() {
		GWT.log("initializing Tunnel Profile list table...");
		tunnelProfileTable.setTableLayoutFixed(false);
		tunnelProfileTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		tunnelProfileTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<TunnelProfilePojo>();
		dataProvider.addDataDisplay(tunnelProfileTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(presenter.getVpnConnectionRequisition().getProfile().getTunnelProfiles());

		selectionModel = 
				new SingleSelectionModel<TunnelProfilePojo>(TunnelProfilePojo.KEY_PROVIDER);
		tunnelProfileTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				TunnelProfilePojo m = selectionModel.getSelectedObject();
			}
		});

		ListHandler<TunnelProfilePojo> sortHandler = 
				new ListHandler<TunnelProfilePojo>(dataProvider.getList());
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
		if (deprovision || reprovision) {
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
	@Override
	public void setDeprovisioning(boolean isDeprovision) {
		this.deprovision = isDeprovision;
	}
	@Override
	public void setReprovisioning(boolean isReprovision) {
		this.reprovision = isReprovision;
	}
}
