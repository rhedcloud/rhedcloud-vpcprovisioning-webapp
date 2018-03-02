package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRuleView;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListFirewallRule extends ViewImplBase implements ListFirewallRuleView {
	Presenter presenter;
	private ListDataProvider<FirewallRulePojo> dataProvider = new ListDataProvider<FirewallRulePojo>();
	private SingleSelectionModel<FirewallRulePojo> selectionModel;
	List<FirewallRulePojo> firewallRuleList = new java.util.ArrayList<FirewallRulePojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager firewallRuleListPager;
	@UiField Button addFirewallRuleButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<FirewallRulePojo> firewallRuleListTable = new CellTable<FirewallRulePojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField VerticalPanel firewallRuleListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListFirewallRuleUiBinder uiBinder = GWT.create(DesktopListFirewallRuleUiBinder.class);

	interface DesktopListFirewallRuleUiBinder extends UiBinder<Widget, DesktopListFirewallRule> {
	}

	public DesktopListFirewallRule() {
		initWidget(uiBinder.createAndBindUi(this));

		addFirewallRuleButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Should go to maintain firewallRule here...");
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_RULE);
			}
		}, ClickEvent.getType());
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
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
	    
	    // anchors for:
	    // - view/edit
	    // - delete
	    // - show history/status?
	    String anchorText = "View Firewall Rule";
		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			anchorText = "Maintain Firewall Rule";
		}

		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("Remove selected Firewall Rule");
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				FirewallRulePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_FIREWALL_RULE, m, null);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor deleteAnchor = new Anchor("Remove Firewall Rule");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Remove selected Firewall Rule");
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				FirewallRulePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_RULE, m, null);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		Anchor historyAnchor = new Anchor("View your Firewall Rule History");
		historyAnchor.addStyleName("productAnchor");
		historyAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		historyAnchor.setTitle("View firewall rule history");
		historyAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				FirewallRulePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_RULE, m, null);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(2, 0, historyAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void showPleaseWaitDialog() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void hidePleaseWaitDialog() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void clearList() {
		firewallRuleListTable.setVisibleRangeAndClearData(firewallRuleListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setFirewallRules(List<FirewallRulePojo> firewallRules) {
		GWT.log("view Setting firewallRules.");
		this.firewallRuleList = firewallRules;
		this.initializeFirewallRuleListTable();
	    firewallRuleListPager.setDisplay(firewallRuleListTable);
	}
	private Widget initializeFirewallRuleListTable() {
		GWT.log("initializing ACCOUNT list table...");
		firewallRuleListTable.setTableLayoutFixed(false);
		firewallRuleListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		firewallRuleListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<FirewallRulePojo>();
		dataProvider.addDataDisplay(firewallRuleListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.firewallRuleList);
		
		selectionModel = 
	    	new SingleSelectionModel<FirewallRulePojo>(FirewallRulePojo.KEY_PROVIDER);
		firewallRuleListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		FirewallRulePojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected firewallRule is: " + m.getName());
	    	}
	    });

	    ListHandler<FirewallRulePojo> sortHandler = 
	    	new ListHandler<FirewallRulePojo>(dataProvider.getList());
	    firewallRuleListTable.addColumnSortHandler(sortHandler);

	    if (firewallRuleListTable.getColumnCount() == 0) {
		    initFirewallRuleListTableColumns(sortHandler);

			// trying to add a dynamic title to an individual cell within the table.
		    // the following works UNTIL there are more than 5 rows and the 
		    // pager is used.  So, when the range changes, it gets confused
		    // no solution yet
//			firewallRuleListTable.addCellPreviewHandler(new Handler<FirewallRulePojo>() {
//				@Override
//				public void onCellPreview(CellPreviewEvent<FirewallRulePojo> event) {
//					if ("mouseover".equals(event.getNativeEvent().getType())) {
//						DirectoryMetaDataPojo dmd = event.getValue().getFirewallRuleOwnerDirectoryMetaData();
//						firewallRuleListTable.getRowElement(event.getIndex()).getCells().
//							getItem(event.getColumn()).setTitle(
//								dmd.getFirstName() + " " + 
//								dmd.getLastName() + 
//								" - From the IdentityService.");
//					}
//				}
//			});
			
	    }
		
		return firewallRuleListTable;
	}
	private void initFirewallRuleListTableColumns(ListHandler<FirewallRulePojo> sortHandler) {
		GWT.log("initializing Firewall Rule list table columns...");

		/*
		<!ELEMENT FirewallRule (Name?, ProfileSetting?, To?, From?, Source?, Destination?, 
		SourceUser?, Category?, Application?, Service?, HipProfiles?, Action?, Description?, 
		LogSetting?, Tag?)>
		
		<!ELEMENT ProfileSetting (Group)>
		<!ELEMENT Group (Member+)>
		<!ELEMENT To (Member+)>
		<!ELEMENT From (Member+)>
		<!ELEMENT Source (Member+)>
		<!ELEMENT Destination (Member+)>
		<!ELEMENT SourceUser (Member+)>
		<!ELEMENT Category (Member+)>
		<!ELEMENT Application (Member+)>
		<!ELEMENT Service (Member+)>
		<!ELEMENT HipProfiles (Member+)>
		<!ELEMENT Tag (Member+)>
		<!ELEMENT FirewallRuleQuerySpecification (Tag?)>
		
		 */

		Column<FirewallRulePojo, String> nameColumn = 
			new Column<FirewallRulePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(FirewallRulePojo object) {
				return object.getName();
			}
		};
		nameColumn.setSortable(true);
		nameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(nameColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		firewallRuleListTable.addColumn(nameColumn, "Name");
		
		Column<FirewallRulePojo, String> actionColumn = 
				new Column<FirewallRulePojo, String> (new TextCell()) {
				
				@Override
				public String getValue(FirewallRulePojo object) {
					return object.getAction();
				}
		};
		actionColumn.setSortable(true);
		actionColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(actionColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getAction().compareTo(o2.getAction());
			}
		});
		firewallRuleListTable.addColumn(actionColumn, "Action");

		Column<FirewallRulePojo, String> descColumn = 
				new Column<FirewallRulePojo, String> (new TextCell()) {
				
				@Override
				public String getValue(FirewallRulePojo object) {
					return object.getAction();
				}
		};
		descColumn.setSortable(true);
		descColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(descColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
		firewallRuleListTable.addColumn(descColumn, "Description");

//		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
//			GWT.log(userLoggedIn.getEppn() + " is an admin");
//			// delete row column
//			Column<FirewallRulePojo, String> deleteRowColumn = new Column<FirewallRulePojo, String>(
//					new ButtonCell()) {
//				@Override
//				public String getValue(FirewallRulePojo object) {
//					return "Delete";
//				}
//			};
//			firewallRuleListTable.addColumn(deleteRowColumn, "");
//			firewallRuleListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
//			deleteRowColumn
//			.setFieldUpdater(new FieldUpdater<FirewallRulePojo, String>() {
//				@Override
//				public void update(int index, final FirewallRulePojo firewallRule,
//						String value) {
//
//					presenter.deleteFirewallRule(firewallRule);
//					
//				}
//			});
//		}
//		else {
//			GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
//		}
//
//		// view/edit row column
//		Column<FirewallRulePojo, String> editRowColumn = new Column<FirewallRulePojo, String>(
//				new ButtonCell()) {
//			@Override
//			public String getValue(FirewallRulePojo object) {
//				if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
//					GWT.log(userLoggedIn.getEppn() + " is an admin");
//					return "Edit";
//				}
//				else {
//					GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
//					return "View";
//				}
//			}
//		};
//		firewallRuleListTable.addColumn(editRowColumn, "");
//		firewallRuleListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
//		editRowColumn.setFieldUpdater(new FieldUpdater<FirewallRulePojo, String>() {
//			@Override
//			public void update(int index, final FirewallRulePojo firewallRule,
//					String value) {
//				
//				// fire MAINTAIN_ACCOUNT event passing the firewallRule to be maintained
////				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_FIREWALL_RULE, firewallRule);
//				// TODO: add a maintainFirewallRule method to the presenter so this is done there
//				// and not here.  This way, the mobile views will also get this 
//				// functionality even though they present the list of firewallRules 
//				// differently
//			}
//		});
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFirewallRuleFromView(FirewallRulePojo firewallRule) {
		dataProvider.getList().remove(firewallRule);
	}

	@Override
	public Widget getStatusMessageSource() {
		return addFirewallRuleButton;
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
		GWT.log("userLoggedIn is: " + this.userLoggedIn);
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// enable add firewallRule button
		addFirewallRuleButton.setEnabled(true);
		// enable Delete button in table (handled in initFirewallRuleListTableColumns)
		// change text of button to Edit (handled in initFirewallRuleListTableColumns)
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// disable add firewallRule button
		addFirewallRuleButton.setEnabled(false);
		// disable Delete button in table (handled in initFirewallRuleListTableColumns)
		// change text of button to View (handled in initFirewallRuleListTableColumns)
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public HasClickHandlers getCancelWidget() {
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return null;
	}
}
