package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProfileView;
import edu.emory.oit.vpcprovisioning.shared.TunnelProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;

public class DesktopListVpnConnectionProfile extends ViewImplBase implements ListVpnConnectionProfileView {
	Presenter presenter;
	private ListDataProvider<VpnConnectionProfileSummaryPojo> dataProvider = new ListDataProvider<VpnConnectionProfileSummaryPojo>();
	private MultiSelectionModel<VpnConnectionProfileSummaryPojo> selectionModel;
	List<VpnConnectionProfileSummaryPojo> profileList = new java.util.ArrayList<VpnConnectionProfileSummaryPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button createButton;
	@UiField Button actionsButton;
	@UiField(provided = true)
	CellTable<VpnConnectionProfileSummaryPojo> listTable = new CellTable<VpnConnectionProfileSummaryPojo>(15,
			(CellTable.Resources) GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML pleaseWaitHTML;
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField PushButton refreshButton;
	@UiField HTML filteredHTML;


	public interface MyCellTableResources extends CellTable.Resources {

		@Source({ CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

	private static DesktopListVpnConnectionProfileUiBinder uiBinder = GWT
			.create(DesktopListVpnConnectionProfileUiBinder.class);

	interface DesktopListVpnConnectionProfileUiBinder extends UiBinder<Widget, DesktopListVpnConnectionProfile> {
	}

	public DesktopListVpnConnectionProfile() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
	}

	@UiHandler ("filterTB")
	void addEmailTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		filterList();
        }
	}
	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		filterList();
	}
	
	void filterList() {
		String filterText = filterTB.getText();
		if (filterText != null) {
			if (filterText.toLowerCase().indexOf("vpc-") >= 0) {
				presenter.filterByVpcAddress(filterText);
			}
			else {
				presenter.filterByVpnConnectionProfileId(filterTB.getText());
			}
		}
	}
	
	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		filterTB.setText("");
		presenter.clearFilter();
		presenter.refreshList(userLoggedIn);
		this.hideFilteredStatus();
	}

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		filterTB.setText("");
		presenter.refreshList(userLoggedIn);
	}

//	@UiHandler("assignButton")
//	void assignButtonClicked(ClickEvent e) {
//		showMessageToUser("This feature is not yet implemented.");
////		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_VPN_CONNECTION_PROFILE);
//	}

	@UiHandler("createButton")
	void createButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_VPN_CONNECTION_PROFILE);
	}
	
//	@UiHandler("provisionButton")
//	void provisionButtonClicked(ClickEvent e) {
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPN_CONNECTION_PROVISIONING);
//	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid = new Grid(5, 1);
		grid.setCellSpacing(8);
		actionsPopup.add(grid);

		Anchor maintainAnchor = new Anchor("View/Maintain Profile");
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("View/maintain selected profile");
		maintainAnchor.ensureDebugId(maintainAnchor.getText());
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				if (selectionModel.getSelectedSet().size() > 1) {
					showMessageToUser("Please select one Profile to view");
					return;
				}
				Iterator<VpnConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				VpnConnectionProfileSummaryPojo m = nIter.next();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPN_CONNECTION_PROFILE, m.getProfile());
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor deleteAnchor = new Anchor("Delete Profile(es)");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected profile(es)");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select one or more item(s) from the list");
					return;
				}

				List<VpnConnectionProfileSummaryPojo> profilesToDelete = new java.util.ArrayList<VpnConnectionProfileSummaryPojo>();
				boolean hasErrors = false;

				Iterator<VpnConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				profileLoop: while (nIter.hasNext()) {
					VpnConnectionProfileSummaryPojo m = nIter.next();
					if (m != null) {
						// remove the elastic ip if it's NOT assigned
						if (m.getAssignment() != null) {
							showMessageToUser("You cannot delete a profile that has an assignment associated to it.");
							hasErrors = true;
							break profileLoop;
						} 
						else {
							if (userLoggedIn.isNetworkAdmin()) {
								profilesToDelete.add(m);
							}
							else {
								showMessageToUser("You are not authorized to perform this action.");
								hasErrors = true;
								break profileLoop;
							}
						}
					} 
					else {
						showMessageToUser("Please select one or more item(s) from the list");
						hasErrors = true;
						break profileLoop;
					}
					
					if (!hasErrors && profilesToDelete.size() > 0) {
						presenter.deleteVpnConnectionProfiles(profilesToDelete);
					}
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		Anchor provisionAnchor = new Anchor("Provision VPN Connection");
		provisionAnchor.addStyleName("productAnchor");
		provisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		provisionAnchor.setTitle("Provision selected profile");
		provisionAnchor.ensureDebugId(provisionAnchor.getText());
		provisionAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				if (selectionModel.getSelectedSet().size() > 1) {
					showMessageToUser("Please select one Profile to provision");
					return;
				}
				Iterator<VpnConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				VpnConnectionProfileSummaryPojo m = nIter.next();
				if (m != null) {
					if (userLoggedIn.isNetworkAdmin()) {
						if (m.getAssignment() != null) {
							// (re-provision) if it's already assigned, just do a VpnConnectionProvisioning.Generate 
							// again using that assignment.  i.e., don't create a VpnConnectionProfileAssignment
							ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPN_CONNECTION_PROVISIONING, m);
						}
						else {
							// (initial provision) create the assignment and then generate the provisioning request
							ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPN_CONNECTION_PROVISIONING, m.getProfile());
						}
					}
					else {
						showMessageToUser("You are not authorized to perform this action.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(2, 0, provisionAnchor);

		Anchor deprovisionAnchor = new Anchor("De-Provisiong VPN Connection");
		deprovisionAnchor.addStyleName("productAnchor");
		deprovisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deprovisionAnchor.setTitle("De-Provision selected profile(es)");
		deprovisionAnchor.ensureDebugId(deprovisionAnchor.getText());
		deprovisionAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select one or more item(s) from the list");
					return;
				}

				Iterator<VpnConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				while (nIter.hasNext()) {
					VpnConnectionProfileSummaryPojo m = nIter.next();
					if (m != null) {
						if (userLoggedIn.isNetworkAdmin()) {
							if (m.getAssignment() == null) {
								showMessageToUser("It does not appear that this profile is currently "
									+ "assigned to a VPC.  In order to de-provision a VPN, the profile "
									+ "must be assigned to a VPC.  Please select a profile that is "
									+ "assigned to a VPC and try again.");
								return;
							}

//							VpnConnectionRequisitionPojo vpnConnectionRequisition = new VpnConnectionRequisitionPojo();
//							vpnConnectionRequisition.setProfile(m.getProfile());
//							vpnConnectionRequisition.setOwnerId(m.getAssignment().getOwnerId());
//
//							// maybe we just need to go to the VPNCP maintenance page and collect 
//							// the vpnipaddress and shared key, passing the requisition
//							ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPN_CONNECTION_DEPROVISIONING, vpnConnectionRequisition, m.getAssignment());
							
//							presenter.deprovisionVpnConnectionForVpcId(m.getAssignment().getOwnerId());
							presenter.deprovisionVpnConnectionForAssignment(m.getAssignment());
						}
						else {
							showMessageToUser("You are not authorized to perform this action.");
						}
					} 
					else {
						showMessageToUser("Please select one or more item(s) from the list");
					}
				}
			}
		});
		grid.setWidget(3, 0, deprovisionAnchor);
		
		Anchor deleteAssignmentAnchor = new Anchor("Delete Profile Assignment");
		deleteAssignmentAnchor.addStyleName("productAnchor");
		deleteAssignmentAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAssignmentAnchor.setTitle("Delete selected profile assignment");
		deleteAssignmentAnchor.ensureDebugId(deleteAssignmentAnchor.getText());
		deleteAssignmentAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				if (selectionModel.getSelectedSet().size() > 1) {
					showMessageToUser("Please select one Profile to view");
					return;
				}
				Iterator<VpnConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				VpnConnectionProfileSummaryPojo m = nIter.next();
				if (m != null) {
					if (m.getAssignment() != null) {
//						int i=0;
//						summaryLoop: for (VpnConnectionProfileSummaryPojo summary : profileList) {
//							if (summary.equals(m)) {
//								break summaryLoop;
//							}
//							i++;
//						}
//						presenter.deleteVpnConnectionProfileAssignment(i, m);
						showMessageToUser("This feature is coming soon.");
					}
					else {
						showMessageToUser("The selected profile does not appear to be assigned.  "
							+ "Please select a profile that has is assigned to a VPC.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(4, 0, deleteAssignmentAnchor);
		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		if (pleaseWaitHTML == null || pleaseWaitHTML.length() == 0) {
			this.pleaseWaitHTML.setHTML("Please wait...");
		} else {
			this.pleaseWaitHTML.setHTML(pleaseWaitHTML);
		}
		this.pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setInitialFocus() {
		

	}

	@Override
	public Widget getStatusMessageSource() {
		return actionsButton;
	}

	@Override
	public void applyNetworkAdminMask() {
		createButton.setEnabled(true);
		actionsButton.setEnabled(true);
//		assignButton.setEnabled(true);
	}

	@Override
	public void applyCentralAdminMask() {
		createButton.setEnabled(false);
		actionsButton.setEnabled(true);
//		assignButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		createButton.setEnabled(false);
		actionsButton.setEnabled(true);
//		assignButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		createButton.setEnabled(false);
		actionsButton.setEnabled(true);
//		assignButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		
		return null;
	}

	@Override
	public void resetFieldStyles() {
		

	}

	@Override
	public HasClickHandlers getCancelWidget() {
		
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		
		return null;
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
	public void clearList() {
		

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setVpnConnectionProfileSummaries(List<VpnConnectionProfileSummaryPojo> summaries) {
		this.profileList = summaries;
		this.initializeListTable();
		listPager.setDisplay(listTable);
		topListPager.setDisplay(listTable);
	}

	private void initializeListTable() {
		GWT.log("initializing VpnConnectionPforile list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		listTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<VpnConnectionProfileSummaryPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.profileList);

		selectionModel = new MultiSelectionModel<VpnConnectionProfileSummaryPojo>(
				VpnConnectionProfileSummaryPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
//				Iterator<VpnConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
//				VpnConnectionProfileSummaryPojo m = nIter.next();
//				if (m != null) {
//					int i=0;
//					summaryLoop: for (VpnConnectionProfileSummaryPojo summary : profileList) {
//						if (summary.equals(m)) {
//							break summaryLoop;
//						}
//						i++;
//					}
//					GWT.log("Selected row number is: " + i);
//				}
			}
		});

		ListHandler<VpnConnectionProfileSummaryPojo> sortHandler = new ListHandler<VpnConnectionProfileSummaryPojo>(
				dataProvider.getList());
		listTable.addColumnSortHandler(sortHandler);

		if (listTable.getColumnCount() == 0) {
			initListTableColumns(sortHandler);
		}
	}

	private void initListTableColumns(ListHandler<VpnConnectionProfileSummaryPojo> sortHandler) {
		GWT.log("initializing VpnConnectionProfileSummary list table columns...");

		Column<VpnConnectionProfileSummaryPojo, Boolean> checkColumn = new Column<VpnConnectionProfileSummaryPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(VpnConnectionProfileSummaryPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Account id column
		Column<VpnConnectionProfileSummaryPojo, String> elasticIpColumn = new Column<VpnConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(VpnConnectionProfileSummaryPojo object) {
				return object.getProfile().getVpnConnectionProfileId();
			}
		};
		elasticIpColumn.setSortable(true);
		sortHandler.setComparator(elasticIpColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				return o1.getProfile().getVpnConnectionProfileId()
						.compareTo(o2.getProfile().getVpnConnectionProfileId());
			}
		});
		listTable.addColumn(elasticIpColumn, "Profile ID");

		// VPC id column
		Column<VpnConnectionProfileSummaryPojo, String> associatedIpColumn = new Column<VpnConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(VpnConnectionProfileSummaryPojo object) {
				return object.getProfile().getVpcNetwork();
			}
		};
		associatedIpColumn.setSortable(true);
		sortHandler.setComparator(associatedIpColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				return o1.getProfile().getVpcNetwork().compareTo(o2.getProfile().getVpcNetwork());
			}
		});
		listTable.addColumn(associatedIpColumn, "VPC Network");

		Column<VpnConnectionProfileSummaryPojo, SafeHtml> tunnelProfileColumn = new Column<VpnConnectionProfileSummaryPojo, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(VpnConnectionProfileSummaryPojo object) {
				if (object.getProfile().getTunnelProfiles().size() > 0) {
					StringBuffer sbuf = new StringBuffer();
					/*
						<p>Collapsible Set:</p>
						<button class="collapsible">Open Section 1</button>
						<div class="content">
						  <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
						</div>
						<button class="collapsible">Open Section 2</button>
						<div class="content">
						  <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
						</div>
						<button class="collapsible">Open Section 3</button>
						<div class="content">
						  <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
						</div>
					 */
					for (int i=0; i<object.getProfile().getTunnelProfiles().size(); i++) {
						TunnelProfilePojo tpp = object.getProfile().getTunnelProfiles().get(i);
//						sbuf.append("<button class=\"collapsible\">" + tpp.getTunnelId() + "</button>");
//						sbuf.append("<div class=\"content\">");
						sbuf.append("<p>");
						sbuf.append("<b>Tunnel Id: </b>" + tpp.getTunnelId() + "</br>");
						sbuf.append("<b>Tunnel Description: </b>" + tpp.getTunnelDescription() + "</br>");
//						sbuf.append("<b>Crypto Keyring: </b>" + tpp.getCryptoKeyringName() + "</br>");
//						sbuf.append("<b>ISAKAMP Profile: </b>" + tpp.getIsakampProfileName() + "</br>");
//						sbuf.append("<b>IPSEC Profile: </b>" + tpp.getIpsecProfileName() + "</br>");
//						sbuf.append("<b>IPSEC Transform Set: </b>" + tpp.getIpsecTransformSetName() + "</br>");
//						sbuf.append("<b>Customer Gateway IP: </b>" + tpp.getCustomerGatewayIp() + "</br>");
//						sbuf.append("<b>VPN Inside CIDR 1: </b>" + tpp.getVpnInsideIpCidr1() + "</br>");
//						sbuf.append("<b>VPN Inside CIDR 2: </b>" + tpp.getVpnInsideIpCidr2() + "</br>");
						sbuf.append("</p>");
//						sbuf.append("</div>");
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
				}
				else {
					String s = "No Tunnel Profile Info";
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(s);
				}
			}
		};
		tunnelProfileColumn.setSortable(true);
		sortHandler.setComparator(tunnelProfileColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				return o1.getProfile() == null ? 0 : 1;
			}
		});
		listTable.addColumn(tunnelProfileColumn, "Tunnel Profiles");

		Column<VpnConnectionProfileSummaryPojo, String> assignmentStatusColumn = new Column<VpnConnectionProfileSummaryPojo, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(VpnConnectionProfileSummaryPojo object) {
				if (object.getAssignment() == null) {
					return "Unassigned";
				} else {
					String s = "Assigned to VPC:  " + object.getAssignment().getOwnerId();
					return s;
				}
			}
		};
		assignmentStatusColumn.setSortable(true);
		sortHandler.setComparator(assignmentStatusColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				return o1.getProfile() == null ? 0 : 1;
			}
		});
		assignmentStatusColumn.setFieldUpdater(new FieldUpdater<VpnConnectionProfileSummaryPojo, String>() {
	    	@Override
	    	public void update(int index, VpnConnectionProfileSummaryPojo object, String value) {
	    		if (object.getAssignment() != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPN_CONNECTION_PROFILE_ASSIGNMENT, object);
	    		}
	    		else {
	    			showMessageToUser("No assignment to view.");
	    		}
	    	}
	    });
		assignmentStatusColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(assignmentStatusColumn, "Assignment Status");

		// create user
		Column<VpnConnectionProfileSummaryPojo, String> createUserColumn = new Column<VpnConnectionProfileSummaryPojo, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(VpnConnectionProfileSummaryPojo object) {
				if (object.getProfile() != null) {
					return object.getProfile().getCreateUser();
				} else {
					return object.getAssignment().getCreateUser();
				}
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				if (o1.getProfile() != null) {
					return o1.getProfile().getCreateUser().compareTo(o2.getProfile().getCreateUser());
				} else {
					return o1.getAssignment().getCreateUser().compareTo(o2.getAssignment().getCreateUser());
				}
			}
		});
		createUserColumn.setFieldUpdater(new FieldUpdater<VpnConnectionProfileSummaryPojo, String>() {
	    	@Override
	    	public void update(int index, VpnConnectionProfileSummaryPojo object, String value) {
				if (object.getProfile() != null) {
					showDirectoryMetaDataForPublicId(object.getProfile().getCreateUser());
				} else {
					showDirectoryMetaDataForPublicId(object.getAssignment().getCreateUser());
				}
	    	}
	    });
		createUserColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(createUserColumn, "Create User");

		// create time
		Column<VpnConnectionProfileSummaryPojo, String> createTimeColumn = new Column<VpnConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(VpnConnectionProfileSummaryPojo object) {
				if (object.getProfile() != null) {
					Date createTime = object.getProfile().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				} else {
					Date createTime = object.getAssignment().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				if (o1.getProfile() != null) {
					Date c1 = o1.getProfile().getCreateTime();
					Date c2 = o2.getProfile().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				} else {
					Date c1 = o1.getAssignment().getCreateTime();
					Date c2 = o2.getAssignment().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
			}
		});
		listTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<VpnConnectionProfileSummaryPojo, String> lastUpdateUserColumn = new Column<VpnConnectionProfileSummaryPojo, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(VpnConnectionProfileSummaryPojo object) {
				if (object.getProfile() != null) {
					return object.getProfile().getUpdateUser();
				} else {
					return object.getAssignment().getUpdateUser();
				}
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				if (o1.getProfile() != null) {
					return o1.getProfile().getUpdateUser().compareTo(o2.getProfile().getUpdateUser());
				} else {
					return o1.getAssignment().getUpdateUser().compareTo(o2.getAssignment().getUpdateUser());
				}
			}
		});
		lastUpdateUserColumn.setFieldUpdater(new FieldUpdater<VpnConnectionProfileSummaryPojo, String>() {
	    	@Override
	    	public void update(int index, VpnConnectionProfileSummaryPojo object, String value) {
	    		showDirectoryMetaDataForPublicId(object.getCreateUser());
	    	}
	    });
		lastUpdateUserColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(lastUpdateUserColumn, "Update User");

		// update time
		Column<VpnConnectionProfileSummaryPojo, String> updateTimeColumn = new Column<VpnConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(VpnConnectionProfileSummaryPojo object) {
				if (object.getProfile() != null) {
					Date createTime = object.getProfile().getUpdateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				} else {
					Date createTime = object.getAssignment().getUpdateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<VpnConnectionProfileSummaryPojo>() {
			public int compare(VpnConnectionProfileSummaryPojo o1, VpnConnectionProfileSummaryPojo o2) {
				if (o1.getProfile() != null) {
					Date c1 = o1.getProfile().getUpdateTime();
					Date c2 = o2.getProfile().getUpdateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				} else {
					Date c1 = o1.getAssignment().getUpdateTime();
					Date c2 = o2.getAssignment().getUpdateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
			}
		});
		listTable.addColumn(updateTimeColumn, "Update Time");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		

	}

	@Override
	public void removeSummaryForVpnConnectionProfileFromView(VpnConnectionProfilePojo vpnConnectionProfile) {
		for (VpnConnectionProfileSummaryPojo summary : dataProvider.getList()) {
			if (summary.getProfile().equals(vpnConnectionProfile)) {
				dataProvider.getList().remove(summary);
			}
		}
	}

	@Override
	public void showFilteredStatus() {
		filteredHTML.setVisible(true);
	}

	@Override
	public void hideFilteredStatus() {
		filteredHTML.setVisible(false);
	}

	@Override
	public void initPage() {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter a profile id OR a VPC Network");
	}

	@Override
	public void refreshTableRow(int rowNumber, VpnConnectionProfileSummaryPojo summary) {
		profileList.set(rowNumber, summary);
		listTable.redrawRow(rowNumber);
	}

}
