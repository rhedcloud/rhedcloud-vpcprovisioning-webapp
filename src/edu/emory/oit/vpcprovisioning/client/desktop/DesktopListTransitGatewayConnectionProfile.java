package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
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

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.ListTransitGatewayConnectionProfileView;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListTransitGatewayConnectionProfile extends ViewImplBase implements ListTransitGatewayConnectionProfileView {
	Presenter presenter;
	private ListDataProvider<TransitGatewayConnectionProfileSummaryPojo> dataProvider = new ListDataProvider<TransitGatewayConnectionProfileSummaryPojo>();
	private MultiSelectionModel<TransitGatewayConnectionProfileSummaryPojo> selectionModel;
	List<TransitGatewayConnectionProfileSummaryPojo> profileList = new java.util.ArrayList<TransitGatewayConnectionProfileSummaryPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button createButton;
	@UiField Button actionsButton;
	@UiField(provided = true)
	CellTable<TransitGatewayConnectionProfileSummaryPojo> listTable = new CellTable<TransitGatewayConnectionProfileSummaryPojo>(15,
			(CellTable.Resources) GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML pleaseWaitHTML;
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField PushButton refreshButton;
	@UiField HTML filteredHTML;
	@UiField HTML profileSummaryHTML;

	public interface MyCellTableResources extends CellTable.Resources {

		@Source({ CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

	private static DesktopListTransitGatewayConnectionProfileUiBinder uiBinder = GWT
			.create(DesktopListTransitGatewayConnectionProfileUiBinder.class);

	interface DesktopListTransitGatewayConnectionProfileUiBinder
			extends UiBinder<Widget, DesktopListTransitGatewayConnectionProfile> {
	}

	public DesktopListTransitGatewayConnectionProfile() {
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
		// TODO: complete filter functionality
		String filterText = filterTB.getText();
		if (filterText != null) {
			if (filterText.toLowerCase().indexOf("vpc-") >= 0) {
				presenter.filterByVpcAddress(filterText);
			}
			else {
//				presenter.filterByVpnConnectionProfileId(filterTB.getText());
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

	@UiHandler("createButton")
	void createButtonClicked(ClickEvent e) {
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_TRANSIT_GATEWAY_CONNECTION_PROFILE);
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid = new Grid(4, 1);
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
				Iterator<TransitGatewayConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				TransitGatewayConnectionProfileSummaryPojo m = nIter.next();
				if (m != null) {
					// TODO: implement maintenance page(s)
//					getAppShell().addBreadCrumb("Maintain Transit Gatway Profile", ActionNames.MAINTAIN_TRANSIT_GATEWAY_CONNECTION_PROFILE, m.getProfile());
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_TRANSIT_GATEWAY_CONNECTION_PROFILE, m.getProfile());
					showMessageToUser("Coming soon");
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

				List<TransitGatewayConnectionProfileSummaryPojo> profilesToDelete = new java.util.ArrayList<TransitGatewayConnectionProfileSummaryPojo>();
				boolean hasErrors = false;

				Iterator<TransitGatewayConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				profileLoop: while (nIter.hasNext()) {
					TransitGatewayConnectionProfileSummaryPojo m = nIter.next();
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
						presenter.deleteTransitGatewayConnectionProfiles(profilesToDelete);
					}
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

//		Anchor provisionAnchor = new Anchor("Provision Transit Gateway Connection");
//		provisionAnchor.addStyleName("productAnchor");
//		provisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
//		provisionAnchor.setTitle("Provision selected profile");
//		provisionAnchor.ensureDebugId(provisionAnchor.getText());
//		provisionAnchor.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				actionsPopup.hide();
//				if (selectionModel.getSelectedSet().size() == 0) {
//					showMessageToUser("Please select an item from the list");
//					return;
//				}
//				if (selectionModel.getSelectedSet().size() > 1) {
//					showMessageToUser("Please select one Profile to provision");
//					return;
//				}
//				Iterator<TransitGatewayConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
//				
//				TransitGatewayConnectionProfileSummaryPojo m = nIter.next();
//				if (m != null) {
//					if (userLoggedIn.isNetworkAdmin()) {
//						showMessageToUser("Coming soon");
//						if (m.getAssignment() != null) {
//							// (re-provision) if it's already assigned, just do a TransitGatewayConnectionProfileAssignment.Generate 
//							// again using that assignment.  i.e., don't create a VpnConnectionProfileAssignment
//							
//							// TODO: just add a method to the presenter instead of linking off to provisioning status?
////							ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_TRANSIT_GATEWAY_CONNECTION_PROFILE_ASSIGNMENT, m);
//						}
//						else {
//							// (initial provision) create the assignment and then generate the provisioning request
//
//							// TODO: just add a method to the presenter instead of linking off to provisioning status?
////							ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_TRANSIT_GATEWAY_CONNECTION_PROFILE_ASSIGNMENT, m.getProfile());
//						}
//					}
//					else {
//						showMessageToUser("You are not authorized to perform this action.");
//					}
//				}
//				else {
//					showMessageToUser("Please select an item from the list");
//				}
//			}
//		});
//		grid.setWidget(2, 0, provisionAnchor);

//		Anchor deprovisionAnchor = new Anchor("De-Provisiong Transit Gateway Connection");
//		deprovisionAnchor.addStyleName("productAnchor");
//		deprovisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
//		deprovisionAnchor.setTitle("De-Provision selected profile(es)");
//		deprovisionAnchor.ensureDebugId(deprovisionAnchor.getText());
//		deprovisionAnchor.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				actionsPopup.hide();
//				if (selectionModel.getSelectedSet().size() == 0) {
//					showMessageToUser("Please select one or more item(s) from the list");
//					return;
//				}
//
//				Iterator<TransitGatewayConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
//				while (nIter.hasNext()) {
//					TransitGatewayConnectionProfileSummaryPojo m = nIter.next();
//					if (m != null) {
//						if (userLoggedIn.isNetworkAdmin()) {
//							if (m.getAssignment() == null) {
//								showMessageToUser("It does not appear that this profile is currently "
//									+ "assigned to a VPC.  In order to de-provision a Transit Gateway connection, the profile "
//									+ "must be assigned to a VPC.  Please select a profile that is "
//									+ "assigned to a VPC and try again.");
//								return;
//							}
//
//							presenter.deprovisionTransitGatewayConnectionForAssignment(m.getAssignment());
//						}
//						else {
//							showMessageToUser("You are not authorized to perform this action.");
//						}
//					} 
//					else {
//						showMessageToUser("Please select one or more item(s) from the list");
//					}
//				}
//			}
//		});
//		grid.setWidget(3, 0, deprovisionAnchor);
		
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
					showMessageToUser("Please select one Profile Assignment to delete");
					return;
				}
				Iterator<TransitGatewayConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				TransitGatewayConnectionProfileSummaryPojo m = nIter.next();
				if (m != null) {
					if (m.getAssignment() != null) {
						summaryLoop: for (TransitGatewayConnectionProfileSummaryPojo summary : profileList) {
							if (summary.equals(m)) {
								break summaryLoop;
							}
						}
						
						// NOTE:  this assumes profile ids are numbers AND sequential AND the table is 
						// sorted by profile id so we can refresh a single row later
						// This is the only way I can get the row number of the assignment
						// being deleted as far as i can tell.  So, if we ever break any of those
						// assumptions this won't work as intended.
						// This is all being done in an effort to just refresh the selected
						// row instead of refreshing the entire table.  It's complicated by
						// the fact that we're using a multi-select selection model in this case.
						// as of 4/9/2020, refreshing a single row really doesn't appear to work
						int row = 0;
						String profileId = m.getProfile().getTransitGatewayConnectionProfileId();
						try {
							row = Integer.parseInt(profileId) - 1;
						}
						catch (Exception e) {
							GWT.log("couldn't parse the profile id as an integer.  refreshing row 0");
							row = 0;
						}
						GWT.log("Refreshing row: " + row);
						presenter.deleteTransitGatewayConnectionProfileAssignment(row, m);
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
		grid.setWidget(2, 0, deleteAssignmentAnchor);
		
		Anchor vpnStatusAnchor = new Anchor("Show Transit Gateway Connection Status");
		vpnStatusAnchor.addStyleName("productAnchor");
		vpnStatusAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		vpnStatusAnchor.setTitle("Show Transit Gateway Connection Status");
		vpnStatusAnchor.ensureDebugId(vpnStatusAnchor.getText());
		vpnStatusAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				if (selectionModel.getSelectedSet().size() > 1) {
					showMessageToUser("Please select one Profile");
					return;
				}
				Iterator<TransitGatewayConnectionProfileSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				TransitGatewayConnectionProfileSummaryPojo m = nIter.next();
				if (m != null) {
					if (m.getAssignment() != null) {
						summaryLoop: for (TransitGatewayConnectionProfileSummaryPojo summary : profileList) {
							if (summary.equals(m)) {
								break summaryLoop;
							}
						}
						presenter.getTransitGatewayStatusForVpc(m.getAssignment().getOwnerId());
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
		grid.setWidget(3, 0, vpnStatusAnchor);

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
		// TODO Auto-generated method stub
		
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
	public void setTransitGatewayConnectionProfileSummaries(
			List<TransitGatewayConnectionProfileSummaryPojo> summaries) {

		this.profileList = summaries;
		this.initializeListTable();
		listPager.setDisplay(listTable);
		topListPager.setDisplay(listTable);
	}

	private void initializeListTable() {
		GWT.log("initializing Transit Gateway connection profile list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		listTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<TransitGatewayConnectionProfileSummaryPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.profileList);

		selectionModel = new MultiSelectionModel<TransitGatewayConnectionProfileSummaryPojo>(
				TransitGatewayConnectionProfileSummaryPojo.KEY_PROVIDER);
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

		ListHandler<TransitGatewayConnectionProfileSummaryPojo> sortHandler = 
				new ListHandler<TransitGatewayConnectionProfileSummaryPojo>(
				dataProvider.getList());
		listTable.addColumnSortHandler(sortHandler);

		if (listTable.getColumnCount() == 0) {
			initListTableColumns(sortHandler);
		}
	}

	private void initListTableColumns(ListHandler<TransitGatewayConnectionProfileSummaryPojo> sortHandler) {
		GWT.log("initializing TransitGatewayConnectionProfileSummary list table columns...");

		/*
		<!ELEMENT TransitGatewayConnectionProfile (
			TransitGatewayConnectionProfileId?, 
			CidrId, 
			Region, 
			TransitGatewayId, 
			CidrRange)>

		<!ELEMENT TransitGatewayConnectionProfileAssignment (
			TransitGatewayConnectionProfileAssignmentId?, 
			TransitGatewayConnectionProfileId, 
			OwnerId, 
			CreateUser, 
			CreateDatetime, 
			LastUpdateUser?, 
			LastUpdateDatetime?)>
		*/

		Column<TransitGatewayConnectionProfileSummaryPojo, Boolean> checkColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// profile id column
		Column<TransitGatewayConnectionProfileSummaryPojo, String> elasticIpColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				return object.getProfile().getTransitGatewayConnectionProfileId();
			}
		};
		elasticIpColumn.setSortable(true);
		sortHandler.setComparator(elasticIpColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				int id1 = Integer.parseInt(o1.getProfile().getTransitGatewayConnectionProfileId());
				int id2 = Integer.parseInt(o2.getProfile().getTransitGatewayConnectionProfileId());
				if (id1 == id2) {
					return 0;
				}
				if (id1 < id2) {
					return -1;
				}
				return 1;
			}
		});
		listTable.addColumn(elasticIpColumn, "Profile ID");

		// CIDR id column
		Column<TransitGatewayConnectionProfileSummaryPojo, String> cidrIdColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				return object.getProfile().getCidrId();
			}
		};
		cidrIdColumn.setSortable(true);
		sortHandler.setComparator(cidrIdColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				return o1.getProfile().getCidrId().compareTo(o2.getProfile().getCidrId());
			}
		});
		listTable.addColumn(cidrIdColumn, "CIDR ID");

		// region column
		Column<TransitGatewayConnectionProfileSummaryPojo, String> regionColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				return object.getProfile().getRegion();
			}
		};
		regionColumn.setSortable(true);
		sortHandler.setComparator(regionColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				return o1.getProfile().getRegion().compareTo(o2.getProfile().getRegion());
			}
		});
		listTable.addColumn(regionColumn, "Region");

		// transit gateway id column (link)
		Column<TransitGatewayConnectionProfileSummaryPojo, String> transitGatewayIdColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				return object.getProfile().getTransitGatewayId();
			}
		};
		transitGatewayIdColumn.setSortable(true);
		sortHandler.setComparator(transitGatewayIdColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				return o1.getProfile().getTransitGatewayId().compareTo(o2.getProfile().getTransitGatewayId());
			}
		});
		transitGatewayIdColumn.setFieldUpdater(new FieldUpdater<TransitGatewayConnectionProfileSummaryPojo, String>() {
	    	@Override
	    	public void update(int index, TransitGatewayConnectionProfileSummaryPojo object, String value) {
    			// TODO: go to transit gateway maintenance page for this id
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPN_CONNECTION_PROFILE_ASSIGNMENT, object);
	    	}
	    });
		transitGatewayIdColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(transitGatewayIdColumn, "Transit Gateway ID");

		// CIDR Range column
		Column<TransitGatewayConnectionProfileSummaryPojo, String> cidrRangeColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				return object.getProfile().getCidrRange();
			}
		};
		cidrRangeColumn.setSortable(true);
		sortHandler.setComparator(cidrRangeColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				return o1.getProfile().getCidrRange().compareTo(o2.getProfile().getCidrRange());
			}
		});
		listTable.addColumn(cidrRangeColumn, "CIDR Range");

		Column<TransitGatewayConnectionProfileSummaryPojo, String> assignmentStatusColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				if (object.getAssignment() == null) {
					return "Unassigned";
				} else {
					String s = "Assigned to VPC:  " + object.getAssignment().getOwnerId();
					return s;
				}
			}
		};
		assignmentStatusColumn.setSortable(true);
		sortHandler.setComparator(assignmentStatusColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				if (o1.getAssignment() == null && o2.getAssignment() == null) {
					return 0;
				}
				if (o1.getAssignment() != null && o2.getAssignment() != null) {
					return 0;
				}
				if (o1.getAssignment() != null && o2.getAssignment() == null) {
					return 1;
				}
				return -1;
			}
		});
		assignmentStatusColumn.setFieldUpdater(new FieldUpdater<TransitGatewayConnectionProfileSummaryPojo, String>() {
	    	@Override
	    	public void update(int index, TransitGatewayConnectionProfileSummaryPojo object, String value) {
	    		if (object.getAssignment() != null) {
	    			// TODO: show some assignment details???
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPN_CONNECTION_PROFILE_ASSIGNMENT, object);
	    		}
	    		else {
	    			showMessageToUser("No assignment to view.");
	    		}
	    	}
	    });
		assignmentStatusColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(assignmentStatusColumn, "Assignment Status");

		// create user
		Column<TransitGatewayConnectionProfileSummaryPojo, String> createUserColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				if (object.getAssignment() != null) {
					return object.getAssignment().getCreateUser() != null ? object.getAssignment().getCreateUser() : "Unknown";
				} else {
					return object.getProfile().getCreateUser() != null ? object.getProfile().getCreateUser() : "Unknown";
				}
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				if (o1.getAssignment() != null) {
					return o1.getAssignment().getCreateUser().compareTo(o2.getAssignment().getCreateUser());
				} else {
					return o1.getProfile().getCreateUser().compareTo(o2.getProfile().getCreateUser());
				}
			}
		});
		createUserColumn.setFieldUpdater(new FieldUpdater<TransitGatewayConnectionProfileSummaryPojo, String>() {
	    	@Override
	    	public void update(int index, TransitGatewayConnectionProfileSummaryPojo object, String value) {
				if (object.getAssignment() != null) {
					showDirectoryMetaDataForPublicId(object.getAssignment().getCreateUser());
				} else {
					showDirectoryMetaDataForPublicId(object.getProfile().getCreateUser());
				}
	    	}
	    });
		createUserColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(createUserColumn, "Create User");

		// create time
		Column<TransitGatewayConnectionProfileSummaryPojo, String> createTimeColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
				new TextCell()) {

			@Override
			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
				if (object.getAssignment() != null) {
					Date createTime = object.getAssignment().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				} else {
					Date createTime = object.getProfile().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
				if (o1.getAssignment() != null) {
					Date c1 = o1.getAssignment().getCreateTime();
					Date c2 = o2.getAssignment().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				} else {
					Date c1 = o1.getProfile().getCreateTime();
					Date c2 = o2.getProfile().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
			}
		});
		listTable.addColumn(createTimeColumn, "Create Time");

		// last update user
//		Column<TransitGatewayConnectionProfileSummaryPojo, String> lastUpdateUserColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
//				new ClickableTextCell()) {
//
//			@Override
//			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
//				if (object.getAssignment() != null) {
//					return object.getAssignment().getUpdateUser() != null ? object.getAssignment().getUpdateUser() : "Unknown";
//				} else {
//					return object.getProfile().getUpdateUser() != null ? object.getProfile().getUpdateUser() : "Unknown";
//				}
//			}
//		};
//		lastUpdateUserColumn.setSortable(true);
//		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
//			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
//				if (o1.getAssignment() != null) {
//					return o1.getAssignment().getUpdateUser().compareTo(o2.getAssignment().getUpdateUser());
//				} else {
//					return o1.getProfile().getUpdateUser().compareTo(o2.getProfile().getUpdateUser());
//				}
//			}
//		});
//		lastUpdateUserColumn.setFieldUpdater(new FieldUpdater<TransitGatewayConnectionProfileSummaryPojo, String>() {
//	    	@Override
//	    	public void update(int index, TransitGatewayConnectionProfileSummaryPojo object, String value) {
//	    		showDirectoryMetaDataForPublicId(object.getCreateUser());
//	    	}
//	    });
//		lastUpdateUserColumn.setCellStyleNames("tableAnchor");
//		listTable.addColumn(lastUpdateUserColumn, "Update User");

		// update time
//		Column<TransitGatewayConnectionProfileSummaryPojo, String> updateTimeColumn = new Column<TransitGatewayConnectionProfileSummaryPojo, String>(
//				new TextCell()) {
//
//			@Override
//			public String getValue(TransitGatewayConnectionProfileSummaryPojo object) {
//				if (object.getAssignment() != null) {
//					Date createTime = object.getAssignment().getUpdateTime();
//					return createTime != null ? dateFormat.format(createTime) : "Unknown";
//				} else {
//					Date createTime = object.getProfile().getUpdateTime();
//					return createTime != null ? dateFormat.format(createTime) : "Unknown";
//				}
//			}
//		};
//		updateTimeColumn.setSortable(true);
//		sortHandler.setComparator(updateTimeColumn, new Comparator<TransitGatewayConnectionProfileSummaryPojo>() {
//			public int compare(TransitGatewayConnectionProfileSummaryPojo o1, TransitGatewayConnectionProfileSummaryPojo o2) {
//				if (o1.getProfile() != null) {
//					Date c1 = o1.getProfile().getUpdateTime();
//					Date c2 = o2.getProfile().getUpdateTime();
//					if (c1 == null || c2 == null) {
//						return 0;
//					}
//					return c1.compareTo(c2);
//				} else {
//					Date c1 = o1.getAssignment().getUpdateTime();
//					Date c2 = o2.getAssignment().getUpdateTime();
//					if (c1 == null || c2 == null) {
//						return 0;
//					}
//					return c1.compareTo(c2);
//				}
//			}
//		});
//		listTable.addColumn(updateTimeColumn, "Update Time");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSummaryForTransitGatewayConnectionProfileFromView(TransitGatewayConnectionProfilePojo profile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showFilteredStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideFilteredStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshTableRow(int rowNumber, TransitGatewayConnectionProfileSummaryPojo summary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProfileSummaryHTML(String summaryHTML) {
		// TODO Auto-generated method stub
		
	}

}
