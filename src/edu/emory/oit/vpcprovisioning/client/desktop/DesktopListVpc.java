package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopListVpc extends ViewImplBase implements ListVpcView {
	Presenter presenter;
	private ListDataProvider<VpcPojo> dataProvider = new ListDataProvider<VpcPojo>();
	private SingleSelectionModel<VpcPojo> selectionModel;
	List<VpcPojo> vpcList = new java.util.ArrayList<VpcPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);
	List<String> filterTypeItems;

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager vpcListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button registerVpcButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<VpcPojo> vpcListTable = new CellTable<VpcPojo>(15, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField PushButton refreshButton;
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField ListBox filterTypesLB;

//	@UiField Button homeButton;
//	@UiHandler("homeButton")
//	void homeButtonClicked(ClickEvent e) {
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME);
//	}

	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		filterTypesLB.setSelectedIndex(0);
		filterTB.setText("");
		presenter.clearFilter();
	}
	@UiHandler("filterButton") 
	void filterButtonClicked(ClickEvent e) {
		String filterType = filterTypesLB.getSelectedValue();
		String filterValue = filterTB.getText();
		
		if ((filterType != null && filterType.length() > 0) &&
			 (filterValue != null && filterValue.length() > 0)) {
			if (filterType.equalsIgnoreCase(Constants.VPC_FILTER_ACCOUNT_NAME)) {
				presenter.filterByAccountName(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.VPC_FILTER_ACCOUNT_ID)) {
				presenter.filterByAccountId(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.VPC_FILTER_VPC_ID)) {
				presenter.filterByVpcId(filterValue);
			}
			else {
				// invalid filter type...but how?
			}
		}
		else {
			this.showMessageToUser("Please enter a Filter Value AND select a Filter Type");
		}
	}
	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		presenter.refreshList(userLoggedIn);
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	private static DesktopListVpcUiBinder uiBinder = GWT.create(DesktopListVpcUiBinder.class);

	interface DesktopListVpcUiBinder extends UiBinder<Widget, DesktopListVpc> {
	}

	public DesktopListVpc() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);

		registerVpcButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Should go to register vpc here...");
				hidePleaseWaitDialog();
				ActionEvent.fire(presenter.getEventBus(), ActionNames.REGISTER_VPC);
			}
		}, ClickEvent.getType());
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
	    Grid grid = new Grid(3, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
	    String anchorText = "View/Maintain VPC";

		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("View/Maintain selected VPC");
		maintainAnchor.ensureDebugId(anchorText);
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				VpcPojo m = selectionModel.getSelectedObject();
				if (m != null) {
//					if (userLoggedIn.isCentralAdmin() || userLoggedIn.isAdminForAccount(m.getAccountId())) {
						ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPC, m);
//					}
//					else {
//						showMessageToUser("You are not authorized to perform this action for this VPC.");
//					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor deleteAnchor = new Anchor("Delete VPC Metadata");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Remove metadata for the selected VPC");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				VpcPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin()) {
						presenter.deleteVpc(m);
					}
					else {
						showMessageToUser("You are not authorized to perform this action for this vpc.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		Anchor deprovisionAnchor = new Anchor("Deprovision VPC");
		deprovisionAnchor.addStyleName("productAnchor");
		deprovisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deprovisionAnchor.setTitle("Deprovision the selected VPC");
		deprovisionAnchor.ensureDebugId(deprovisionAnchor.getText());
		deprovisionAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				VpcPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin()) {
						showMessageToUser("This functionality is comming soon.");
						// TODO: presenter.deprovisionVpc(m);
					}
					else {
						showMessageToUser("You are not authorized to perform this action for this vpc.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(2, 0, deprovisionAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void clearList() {
		vpcListTable.setVisibleRangeAndClearData(vpcListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setVpcs(List<VpcPojo> vpcs) {
		this.vpcList = vpcs;
		this.initializeVpcListTable();
	    vpcListPager.setDisplay(vpcListTable);
	    topListPager.setDisplay(vpcListTable);
	}
	private Widget initializeVpcListTable() {
		GWT.log("initializing VPC list table...");
		vpcListTable.setTableLayoutFixed(false);
		vpcListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		vpcListTable.setVisibleRange(0, 15);
		
		// create dataprovider
		dataProvider = new ListDataProvider<VpcPojo>();
		dataProvider.addDataDisplay(vpcListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.vpcList);
		
		selectionModel = 
	    	new SingleSelectionModel<VpcPojo>(VpcPojo.KEY_PROVIDER);
		vpcListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		VpcPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected vpc is: " + m.getVpcId());
	    	}
	    });

	    ListHandler<VpcPojo> sortHandler = 
	    	new ListHandler<VpcPojo>(dataProvider.getList());
	    vpcListTable.addColumnSortHandler(sortHandler);

	    if (vpcListTable.getColumnCount() == 0) {
		    initVpcListTableColumns(sortHandler);
	    }
		
		return vpcListTable;
	}
	private void initVpcListTableColumns(ListHandler<VpcPojo> sortHandler) {
		GWT.log("initializing VPC list table columns...");
		
	    Column<VpcPojo, Boolean> checkColumn = new Column<VpcPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(VpcPojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    vpcListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    vpcListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Account id column
		Column<VpcPojo, String> acctIdColumn = 
			new Column<VpcPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(VpcPojo object) {
				return object.getAccountId();
			}
		};
		acctIdColumn.setSortable(true);
		sortHandler.setComparator(acctIdColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getAccountId().compareTo(o2.getAccountId());
			}
		});
		vpcListTable.addColumn(acctIdColumn, "Account ID");
		
		// account name column
		Column<VpcPojo, String> acctNameColumn = 
			new Column<VpcPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(VpcPojo object) {
				return object.getAccountName();
			}
		};
		acctNameColumn.setSortable(true);
		sortHandler.setComparator(acctNameColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				// we really want to sort by the "sequence" part of the name,
				// if one exists...
				String s_seq1 = extractNumberFromString(o1.getAccountName());
				String s_seq2 = extractNumberFromString(o2.getAccountName());
				if (s_seq1 == null || 
					s_seq1.length() == 0 || 
					s_seq2 == null || 
					s_seq2.length() == 0) {
					
					return o1.getAccountName().compareTo(o2.getAccountName());
				}
				else {
					int seq1 = Integer.parseInt(s_seq1);
					int seq2 = Integer.parseInt(s_seq2);
					if (seq1 == seq2) {
						return 0;
					}
					if (seq1 > seq2) {
						return -1;
					}
					return 1;
				}
			}
		});
		vpcListTable.addColumn(acctNameColumn, "Account Name");

		// VPC id column
		Column<VpcPojo, String> vpcIdColumn = 
			new Column<VpcPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(VpcPojo object) {
				return object.getVpcId();
			}
		};
		vpcIdColumn.setSortable(true);
		sortHandler.setComparator(vpcIdColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getVpcId().compareTo(o2.getVpcId());
			}
		});
		vpcListTable.addColumn(vpcIdColumn, "VPC ID");
		
		// VPC region column
		Column<VpcPojo, String> regionColumn = 
			new Column<VpcPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(VpcPojo object) {
				return object.getRegion();
			}
		};
		regionColumn.setSortable(true);
		sortHandler.setComparator(regionColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getRegion().compareTo(o2.getRegion());
			}
		});
		vpcListTable.addColumn(regionColumn, "Region");
		
		// type
		Column<VpcPojo, String> vpcTypeColumn = 
			new Column<VpcPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(VpcPojo object) {
				return object.getType();
			}
		};
		vpcTypeColumn.setSortable(true);
		sortHandler.setComparator(vpcTypeColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getType().compareTo(o2.getType());
			}
		});
		vpcListTable.addColumn(vpcTypeColumn, "VPC Type");
		
		// cidr
		Column<VpcPojo, String> cidrColumn = 
				new Column<VpcPojo, String> (new TextCell()) {
				
				@Override
				public String getValue(VpcPojo object) {
					return object.getCidr();
				}
			};
			cidrColumn.setSortable(true);
			sortHandler.setComparator(cidrColumn, new Comparator<VpcPojo>() {
				public int compare(VpcPojo o1, VpcPojo o2) {
					return o1.getCidr().compareTo(o2.getCidr());
				}
			});
			vpcListTable.addColumn(cidrColumn, "CIDR");
		
		// vpn profile
		Column<VpcPojo, String> vpnProfileColumn = 
				new Column<VpcPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcPojo object) {
				return object.getVpnConnectionProfileId();
			}
		};
		vpnProfileColumn.setSortable(true);
		sortHandler.setComparator(vpnProfileColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getVpnConnectionProfileId().compareTo(o2.getVpnConnectionProfileId());
			}
		});
		vpcListTable.addColumn(vpnProfileColumn, "VPN Profile ID");
		
		// create user
		Column<VpcPojo, String> createUserColumn = 
				new Column<VpcPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(VpcPojo object) {
				return object.getCreateUser();
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getCreateUser().compareTo(o2.getCreateUser());
			}
		});
		createUserColumn.setFieldUpdater(new FieldUpdater<VpcPojo, String>() {
	    	@Override
	    	public void update(int index, VpcPojo object, String value) {
	    		showDirectoryMetaDataForPublicId(object.getCreateUser());
	    	}
	    });
		createUserColumn.setCellStyleNames("tableAnchor");
		vpcListTable.addColumn(createUserColumn, "Create User");
		
		// create time
		Column<VpcPojo, String> createTimeColumn = 
				new Column<VpcPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcPojo object) {
				if (object.getCreateTime() != null) {
					return dateFormat.format(object.getCreateTime());
				}
				else {
					return "Unknown";
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				if (o1.getCreateTime() != null && o2.getCreateTime() != null) {
					return o1.getCreateTime().compareTo(o2.getCreateTime());
				}
				else {
					return 0;
				}
			}
		});
		vpcListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<VpcPojo, String> lastUpdateUserColumn = 
				new Column<VpcPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(VpcPojo object) {
				return object.getUpdateUser();
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getUpdateUser().compareTo(o2.getUpdateUser());
			}
		});
		lastUpdateUserColumn.setFieldUpdater(new FieldUpdater<VpcPojo, String>() {
	    	@Override
	    	public void update(int index, VpcPojo object, String value) {
	    		showDirectoryMetaDataForPublicId(object.getUpdateUser());
	    	}
	    });
		lastUpdateUserColumn.setCellStyleNames("tableAnchor");
		vpcListTable.addColumn(lastUpdateUserColumn, "Update User");
		
		// update time
		Column<VpcPojo, String> updateTimeColumn = 
				new Column<VpcPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcPojo object) {
				if (object.getUpdateTime() != null) {
					return dateFormat.format(object.getUpdateTime());
				}
				else {
					return "Unknown";
				}
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				if (o1.getUpdateTime() != null && o2.getUpdateTime() != null) {
					return o1.getUpdateTime().compareTo(o2.getUpdateTime());
				}
				else {
					return 0;
				}
			}
		});
		vpcListTable.addColumn(updateTimeColumn, "Update Time");

		// delete row column
//		Column<VpcPojo, String> deleteRowColumn = new Column<VpcPojo, String>(
//				new ButtonCell()) {
//			@Override
//			public String getValue(VpcPojo object) {
//				return "Delete";
//			}
//		};
//		deleteRowColumn.setCellStyleNames("glowing-border");
//		vpcListTable.addColumn(deleteRowColumn, "");
//		vpcListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
//		deleteRowColumn
//		.setFieldUpdater(new FieldUpdater<VpcPojo, String>() {
//			@Override
//			public void update(int index, final VpcPojo vpc,
//					String value) {
//
//				if (userLoggedIn.isAdminForAccount(vpc.getAccountId()) ||
//					userLoggedIn.isLitsAdmin()) {
//					
//					GWT.log(userLoggedIn.getEppn() + " is an admin");
//					presenter.deleteVpc(vpc);
//				}
//				else {
//					showMessageToUser("You are not authorized to perform this action for this VPC.");
//				}
//			}
//		});

		// edit row column
//		Column<VpcPojo, String> editRowColumn = new Column<VpcPojo, String>(
//				new ButtonCell()) {
//			@Override
//			public String getValue(final VpcPojo object) {
//				if (userLoggedIn.isAdminForAccount(object.getAccountId()) ||
//						userLoggedIn.isLitsAdmin()) {
//					GWT.log(userLoggedIn.getEppn() + " is an admin");
//					return "Edit";
//				}
//				else {
//					GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
//					return "View";
//				}
//			}
//		};
//		editRowColumn.setCellStyleNames("glowing-border");
//		vpcListTable.addColumn(editRowColumn, "");
//		vpcListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
//		editRowColumn.setFieldUpdater(new FieldUpdater<VpcPojo, String>() {
//			@Override
//			public void update(int index, final VpcPojo vpc,
//					String value) {
//				
//				// fire MAINTAIN_VPC event passing the vpc to be maintained
//				GWT.log("[DesktopListVpc] editing VPC: " + vpc.getVpcId());
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPC, vpc);
//			}
//		});
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@UiField HTML pleaseWaitHTML;
	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		if (pleaseWaitHTML == null || pleaseWaitHTML.length() == 0) {
			this.pleaseWaitHTML.setHTML("Please wait...");
		}
		else {
			this.pleaseWaitHTML.setHTML(pleaseWaitHTML);
		}
		this.pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public void removeVpcFromView(VpcPojo vpc) {
		dataProvider.getList().remove(vpc);
	}

	@Override
	public Widget getStatusMessageSource() {
		return refreshButton;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// enable add button
		registerVpcButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// disable add button
		registerVpcButton.setEnabled(false);
		actionsButton.setEnabled(true);
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
	public void applyCentralAdminMask() {
		registerVpcButton.setEnabled(true);
		actionsButton.setEnabled(true);
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
		registerVpcButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		registerVpcButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
		
		
	}

	@Override
	public void setFilterTypeItems(List<String> filterTypes) {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter filter value");

		this.filterTypeItems = filterTypes;
		filterTypesLB.clear();
		
		filterTypesLB.addItem("-- Select Filter Type --", "");
		if (filterTypeItems != null) {
			for (String filterType : filterTypeItems) {
				filterTypesLB.addItem(filterType, filterType);
			}
		}
	}
}
