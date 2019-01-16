package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRuleView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopListFirewallRule extends ViewImplBase implements ListFirewallRuleView {
	Presenter presenter;
	private ListDataProvider<FirewallRulePojo> fw_dataProvider = new ListDataProvider<FirewallRulePojo>();
	private SingleSelectionModel<FirewallRulePojo> fw_selectionModel;
	List<FirewallRulePojo> firewallRuleList = new java.util.ArrayList<FirewallRulePojo>();

	private ListDataProvider<FirewallExceptionRequestSummaryPojo> fwerSummary_dataProvider = new ListDataProvider<FirewallExceptionRequestSummaryPojo>();
	private SingleSelectionModel<FirewallExceptionRequestSummaryPojo> fwerSummary_selectionModel;
	List<FirewallExceptionRequestSummaryPojo> fwerSummaryList = new java.util.ArrayList<FirewallExceptionRequestSummaryPojo>();

	List<VpcPojo> vpcs;
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager firewallRuleListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager firewallExceptionRequestListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button firewallExceptionRequestButton;
	@UiField Button firewallExceptionRequestActionsButton;
	@UiField Button firewallRuleActionsButton;
	@UiField(provided=true) CellTable<FirewallRulePojo> firewallRuleListTable = new CellTable<FirewallRulePojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField(provided=true) CellTable<FirewallExceptionRequestSummaryPojo> firewallExceptionRequestListTable = new CellTable<FirewallExceptionRequestSummaryPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField VerticalPanel firewallRuleListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField TabLayoutPanel firewallRuleTabPanel;
	@UiField PushButton refreshButton;
	@UiField PushButton refreshExceptionsButton;

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		presenter.refreshList(userLoggedIn);
	}

	@UiHandler("refreshExceptionsButton")
	void refreshExceptionsButtonClicked(ClickEvent e) {
		presenter.refreshFirewallExceptionRequestSummaryList(userLoggedIn);
	}

	//	@UiField Button filterButton;
	//	@UiField Button clearFilterButton;
	//	@UiField TextBox filterTB;
	//
	//	@UiHandler("filterButton")
	//	void filterButtonClicked(ClickEvent e) {
	//		// TODO: filter list by account id typed in accountIdTB
	//		presenter.filterByVPCId(filterTB.getText());
	//	}
	//	@UiHandler("clearFilterButton")
	//	void clearFilterButtonClicked(ClickEvent e) {
	//		// clear filter
	//		filterTB.setText("");
	//		presenter.clearFilter();
	//	}

	private static DesktopListFirewallRuleUiBinder uiBinder = GWT.create(DesktopListFirewallRuleUiBinder.class);

	interface DesktopListFirewallRuleUiBinder extends UiBinder<Widget, DesktopListFirewallRule> {
	}

	public DesktopListFirewallRule() {
		initWidget(uiBinder.createAndBindUi(this));

		setRefreshButtonImage(refreshButton);
		setRefreshButtonImage(refreshExceptionsButton);
	}

	public interface MyCellTableResources extends CellTable.Resources {

		@Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

	@UiHandler ("firewallRuleTabPanel") 
	void tabSelected(SelectionEvent<Integer> e) {
		switch (e.getSelectedItem()) {
		case 0:
			presenter.refreshList(userLoggedIn);
			break;
		case 1:
			presenter.refreshFirewallExceptionRequestSummaryList(userLoggedIn);
			break;
		}
	}
	@UiHandler("firewallRuleActionsButton")
	void firewallRuleActionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) && 
				!userLoggedIn.isCentralAdmin()) {

			// user not authorized to perform any actions on selected item...
			showMessageToUser("You are not authorized to perform any actions on Firewall Rules.");
			return;
		}

		Grid grid = new Grid(3, 1);
		grid.setCellSpacing(8);
		actionsPopup.add(grid);

		// anchors for:
		// - view/edit
		// - delete
		String anchorText = "Create Firewall Exception Request Like This";

		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("Create Firewall Exception Request like the selected Firewall Rule.");
		maintainAnchor.ensureDebugId(anchorText);
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) &&
						!userLoggedIn.isCentralAdmin()) {

					showMessageToUser("User are not authorized to perform this action on this item.");
					return;
				}
				FirewallRulePojo m = fw_selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, m, presenter.getVpc(), true);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor createFwerAnchor = new Anchor("Create Firewall Exception Request");
		createFwerAnchor.addStyleName("productAnchor");
		createFwerAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		createFwerAnchor.setTitle("Create a new Firewall Exception Request");
		createFwerAnchor.ensureDebugId(createFwerAnchor.getText());
		createFwerAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) &&
						!userLoggedIn.isCentralAdmin()) {

					showMessageToUser("User are not authorized to perform this action on this item.");
					return;
				}
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, presenter.getVpc(), true);
			}
		});
		grid.setWidget(1, 0, createFwerAnchor);
		actionsPopup.showRelativeTo(firewallRuleActionsButton);

		Anchor deleteAnchor = new Anchor("Remove Firewall Rule");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Remove selected Firewall Rule");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) &&
						!userLoggedIn.isCentralAdmin()) {

					showMessageToUser("User are not authorized to perform this action on this item.");
					return;
				}
				FirewallRulePojo m = fw_selectionModel.getSelectedObject();
				if (m != null) {
					// this should resolve to a FirewallExceptionRemoveRequest.Generate
					// so this will be the MaintainFirewallExceptionRequest view but the object being
					// populated will be a FirewallRuleRemoveExceptionRequestRequisition
					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, presenter.getVpc(), m, false);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(2, 0, deleteAnchor);
		actionsPopup.showRelativeTo(firewallRuleActionsButton);
	}

	@UiHandler("firewallExceptionRequestButton")
	void fer_addButtonClicked(ClickEvent e) {
		GWT.log("Should go to maintain firewallexceptionrequest here...");
		GWT.log("[firewallExceptionRequestButton] presenter.getVpc() is: " + presenter.getVpc());
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, presenter.getVpc(), true);
	}

	@UiHandler("firewallExceptionRequestActionsButton")
	void fer_actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) && 
				!userLoggedIn.isCentralAdmin()) {

			// user not authorized to perform any actions on selected item...
			showMessageToUser("You are not authorized to perform any actions on Firewall Exception Requests.");
			return;
		}
		Grid grid = new Grid(2, 1);
		grid.setCellSpacing(8);
		actionsPopup.add(grid);

		// anchors for:
		// - view/edit
		// - delete
		String anchorText = "View Firewall Exception Request";
		if (userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) ||
				userLoggedIn.isCentralAdmin()) {

			anchorText = "View Firewall Exception Request";
		}

		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("View selected Firewall Exception Request");
		maintainAnchor.ensureDebugId(anchorText);
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) &&
						!userLoggedIn.isCentralAdmin()) {

					showMessageToUser("User are not authorized to perform this action on this item.");
					return;
				}
				FirewallExceptionRequestSummaryPojo m = fwerSummary_selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_FIREWALL_EXCEPTION_REQUEST, m, presenter.getVpc());
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor deleteAnchor = new Anchor("Cancel Firewall Exception Request");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Cancel selected Firewall Exception Request");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) &&
						!userLoggedIn.isCentralAdmin()) {

					showMessageToUser("User are not authorized to perform this action on this item.");
					return;
				}
				FirewallExceptionRequestSummaryPojo m = fwerSummary_selectionModel.getSelectedObject();
				if (m != null) {
					// FirewallExceptionAddRequest.Delete-Request
					presenter.cancelFirewallException(m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		actionsPopup.showRelativeTo(firewallExceptionRequestActionsButton);
	}

	@Override
	public void clearFirewallRuleList() {
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
		GWT.log("initializing Firewall Rule list table...");
		firewallRuleListTable.setTableLayoutFixed(false);
		firewallRuleListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		firewallRuleListTable.setVisibleRange(0, 5);

		// create dataprovider
		fw_dataProvider = new ListDataProvider<FirewallRulePojo>();
		fw_dataProvider.addDataDisplay(firewallRuleListTable);
		fw_dataProvider.getList().clear();
		fw_dataProvider.getList().addAll(this.firewallRuleList);

		fw_selectionModel = 
				new SingleSelectionModel<FirewallRulePojo>(FirewallRulePojo.KEY_PROVIDER);
		firewallRuleListTable.setSelectionModel(fw_selectionModel);

		fw_selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				FirewallRulePojo m = fw_selectionModel.getSelectedObject();
				GWT.log("Selected firewallRule is: " + m.getName());
			}
		});

		ListHandler<FirewallRulePojo> sortHandler = 
				new ListHandler<FirewallRulePojo>(fw_dataProvider.getList());
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

		Column<FirewallRulePojo, Boolean> checkColumn = new Column<FirewallRulePojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(FirewallRulePojo object) {
				// Get the value from the selection model.
				return fw_selectionModel.isSelected(object);
			}
		};
		firewallRuleListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		firewallRuleListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		/*
		Here is how I would suggest we display the rules for users:
			Vsys Number (maybee include the fw name as well; e.g., vsys4 - Device/AdminCore)
			Name, Source, Destination, Application, Service, Action		 
		*/
		Column<FirewallRulePojo, String> vsysColumn = 
				new Column<FirewallRulePojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallRulePojo object) {
				if (object.getVsys() != null) {
					return object.getVsys() + " - " + object.getName();
				}
				return "VSYS Unknown - " + object.getName();
			}
		};
		vsysColumn.setSortable(true);
		vsysColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(vsysColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getVsys().compareTo(o2.getVsys());
			}
		});
		firewallRuleListTable.addColumn(vsysColumn, "VSYS");

		Column<FirewallRulePojo, SafeHtml> sourceColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(FirewallRulePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean isFirst = true;
				for (String s : object.getSources()) {
					if (!isFirst) {
						sbuf.append("<br>");
					}
					else {
						isFirst = false;
					}
					sbuf.append(s);
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		sourceColumn.setSortable(true);
		sourceColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(sourceColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getAction().compareTo(o2.getAction());
			}
		});
		firewallRuleListTable.addColumn(sourceColumn, "Source(s)");

		Column<FirewallRulePojo, SafeHtml> destinationColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(FirewallRulePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean isFirst = true;
				for (String s : object.getDestinations()) {
					if (!isFirst) {
						sbuf.append("<br>");
					}
					else {
						isFirst = false;
					}
					sbuf.append(s);
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		destinationColumn.setSortable(true);
		destinationColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(destinationColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getAction().compareTo(o2.getAction());
			}
		});
		firewallRuleListTable.addColumn(destinationColumn, "Destination(s)");

		Column<FirewallRulePojo, SafeHtml> applicationsColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(FirewallRulePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean isFirst = true;
				for (String s : object.getApplications()) {
					if (!isFirst) {
						sbuf.append("<br>");
					}
					else {
						isFirst = false;
					}
					sbuf.append(s);
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		firewallRuleListTable.addColumn(applicationsColumn, "Application(s)");

		Column<FirewallRulePojo, SafeHtml> serviceColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(FirewallRulePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean isFirst = true;
				for (String s : object.getServices()) {
					if (!isFirst) {
						sbuf.append("<br>");
					}
					else {
						isFirst = false;
					}
					sbuf.append(s);
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		serviceColumn.setSortable(true);
		serviceColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(serviceColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getAction().compareTo(o2.getAction());
			}
		});
		firewallRuleListTable.addColumn(serviceColumn, "Service(s)");

		Column<FirewallRulePojo, SafeHtml> actionColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(FirewallRulePojo object) {
				if (object.getAction() != null) {
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(object.getAction());
				}
				else {
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(Constants.UNKNOWN);
				}
			}
		};
		firewallRuleListTable.addColumn(actionColumn, "Action");

//		Column<FirewallRulePojo, String> descColumn = 
//				new Column<FirewallRulePojo, String> (new TextCell()) {
//
//			@Override
//			public String getValue(FirewallRulePojo object) {
//				return object.getAction();
//			}
//		};
//		descColumn.setSortable(true);
//		descColumn.setCellStyleNames("tableBody");
//		sortHandler.setComparator(descColumn, new Comparator<FirewallRulePojo>() {
//			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
//				return o1.getDescription().compareTo(o2.getDescription());
//			}
//		});
//		firewallRuleListTable.addColumn(descColumn, "Description");
//
//		Column<FirewallRulePojo, SafeHtml> vpcColumn = 
//				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(FirewallRulePojo object) {
//				StringBuffer sbuf = new StringBuffer();
//				boolean isFirst = true;
//				for (String s : object.getTags()) {
//					if (!isFirst) {
//						sbuf.append("<br>");
//					}
//					else {
//						isFirst = false;
//					}
//					sbuf.append(s);
//				}
//				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
//			}
//		};
//		vpcColumn.setSortable(true);
//		vpcColumn.setCellStyleNames("tableBody");
//		sortHandler.setComparator(vpcColumn, new Comparator<FirewallRulePojo>() {
//			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
//				return o1.getAction().compareTo(o2.getAction());
//			}
//		});
//		firewallRuleListTable.addColumn(vpcColumn, "VPC ID");
//
//		Column<FirewallRulePojo, SafeHtml> toColumn = 
//				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(FirewallRulePojo object) {
//				StringBuffer sbuf = new StringBuffer();
//				boolean isFirst = true;
//				for (String s : object.getTos()) {
//					if (!isFirst) {
//						sbuf.append("<br>");
//					}
//					else {
//						isFirst = false;
//					}
//					sbuf.append(s);
//				}
//				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
//			}
//		};
//		firewallRuleListTable.addColumn(toColumn, "To(s)");
//
//		Column<FirewallRulePojo, SafeHtml> fromColumn = 
//				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(FirewallRulePojo object) {
//				StringBuffer sbuf = new StringBuffer();
//				boolean isFirst = true;
//				for (String s : object.getFroms()) {
//					if (!isFirst) {
//						sbuf.append("<br>");
//					}
//					else {
//						isFirst = false;
//					}
//					sbuf.append(s);
//				}
//				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
//			}
//		};
//		firewallRuleListTable.addColumn(fromColumn, "From(s)");
//
//		Column<FirewallRulePojo, SafeHtml> sourceUsersColumn = 
//				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(FirewallRulePojo object) {
//				StringBuffer sbuf = new StringBuffer();
//				boolean isFirst = true;
//				for (String s : object.getSourceUsers()) {
//					if (!isFirst) {
//						sbuf.append("<br>");
//					}
//					else {
//						isFirst = false;
//					}
//					sbuf.append(s);
//				}
//				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
//			}
//		};
//		firewallRuleListTable.addColumn(sourceUsersColumn, "Source User(s)");
//
//		Column<FirewallRulePojo, SafeHtml> categoriesColumn = 
//				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(FirewallRulePojo object) {
//				StringBuffer sbuf = new StringBuffer();
//				boolean isFirst = true;
//				for (String s : object.getCategories()) {
//					if (!isFirst) {
//						sbuf.append("<br>");
//					}
//					else {
//						isFirst = false;
//					}
//					sbuf.append(s);
//				}
//				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
//			}
//		};
//		firewallRuleListTable.addColumn(categoriesColumn, "Category(s)");
//
//		Column<FirewallRulePojo, SafeHtml> hipProfilesColumn = 
//				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(FirewallRulePojo object) {
//				StringBuffer sbuf = new StringBuffer();
//				boolean isFirst = true;
//				for (String s : object.getHipProfiles()) {
//					if (!isFirst) {
//						sbuf.append("<br>");
//					}
//					else {
//						isFirst = false;
//					}
//					sbuf.append(s);
//				}
//				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
//			}
//		};
//		firewallRuleListTable.addColumn(hipProfilesColumn, "HIP Profile(s)");
//
//		Column<FirewallRulePojo, SafeHtml> logSettingColumn = 
//				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(FirewallRulePojo object) {
//				if (object.getLogSetting() != null) {
//					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(object.getLogSetting());
//				}
//				else {
//					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(Constants.UNKNOWN);
//				}
//			}
//		};
//		firewallRuleListTable.addColumn(logSettingColumn, "Log Setting");
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
	public void removeFirewallRuleFromView(FirewallRulePojo firewallRule) {
		fw_dataProvider.getList().remove(firewallRule);
	}

	@Override
	public Widget getStatusMessageSource() {
		return firewallExceptionRequestButton;
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
		GWT.log("userLoggedIn is: " + this.userLoggedIn);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// enable add firewallRule button
		firewallExceptionRequestButton.setEnabled(true);
		firewallExceptionRequestActionsButton.setEnabled(true);
		firewallRuleActionsButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// disable add firewallRule button
		firewallExceptionRequestButton.setEnabled(false);
		firewallExceptionRequestActionsButton.setEnabled(false);
		firewallRuleActionsButton.setEnabled(false);
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
	public void setFirewallExceptionRequestSummaries(List<FirewallExceptionRequestSummaryPojo> summaries) {
		this.fwerSummaryList = summaries;
		this.initializeFirewallExceptionRequestSummaryListTable();
		firewallExceptionRequestListPager.setDisplay(firewallExceptionRequestListTable);
	}

	private Widget initializeFirewallExceptionRequestSummaryListTable() {
		GWT.log("initializing Firewall Rule Exception Request list table...");
		firewallExceptionRequestListTable.setTableLayoutFixed(false);
		firewallExceptionRequestListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		firewallExceptionRequestListTable.setVisibleRange(0, 5);

		// create dataprovider
		fwerSummary_dataProvider = new ListDataProvider<FirewallExceptionRequestSummaryPojo>();
		fwerSummary_dataProvider.addDataDisplay(firewallExceptionRequestListTable);
		fwerSummary_dataProvider.getList().clear();
		fwerSummary_dataProvider.getList().addAll(this.fwerSummaryList);

		fwerSummary_selectionModel = 
				new SingleSelectionModel<FirewallExceptionRequestSummaryPojo>(FirewallExceptionRequestSummaryPojo.KEY_PROVIDER);
		firewallExceptionRequestListTable.setSelectionModel(fwerSummary_selectionModel);

		fwerSummary_selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				FirewallExceptionRequestSummaryPojo m = fwerSummary_selectionModel.getSelectedObject();
			}
		});

		ListHandler<FirewallExceptionRequestSummaryPojo> sortHandler = 
				new ListHandler<FirewallExceptionRequestSummaryPojo>(fwerSummary_dataProvider.getList());
		firewallExceptionRequestListTable.addColumnSortHandler(sortHandler);

		if (firewallExceptionRequestListTable.getColumnCount() == 0) {
			initFirewallExceptionRequestSummaryListTableColumns(sortHandler);
		}

		return firewallExceptionRequestListTable;
	}

	private void initFirewallExceptionRequestSummaryListTableColumns(ListHandler<FirewallExceptionRequestSummaryPojo> sortHandler) {

		GWT.log("initializing Firewall Rule Exception Request list table columns...");
		Column<FirewallExceptionRequestSummaryPojo, Boolean> checkColumn = new Column<FirewallExceptionRequestSummaryPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(FirewallExceptionRequestSummaryPojo object) {
				// Get the value from the selection model.
				return fwerSummary_selectionModel.isSelected(object);
			}
		};
		firewallExceptionRequestListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		firewallExceptionRequestListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<FirewallExceptionRequestSummaryPojo, String> reqType = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return "Firewall Exception ADD Request";
				}
				else if (object.getRemoveRequest() != null) {
					return "Firewall Exception REMOVE Request";
				}
				return Constants.UNKNOWN;
			}
		};
		reqType.setSortable(true);
		reqType.setCellStyleNames("tableBody");
		sortHandler.setComparator(reqType, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return 0;
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return "Add".compareTo("Remove");
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return "Remove".compareTo("Add");
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(reqType, "Request Type");

		Column<FirewallExceptionRequestSummaryPojo, String> reqItemNumberColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return object.getAddRequest().getRequestItemNumber();
				}
				if (object.getRemoveRequest() != null) {
					return object.getRemoveRequest().getRequestItemNumber();
				}
				return Constants.UNKNOWN;
			}
		};
		reqItemNumberColumn.setSortable(true);
		reqItemNumberColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(reqItemNumberColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					if (o1.getAddRequest().getRequestItemNumber() != null && o2.getAddRequest().getRequestItemNumber() != null) {
						return o1.getAddRequest().getRequestItemNumber().compareTo(o2.getAddRequest().getRequestItemNumber());
					}
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					if (o1.getAddRequest().getRequestItemNumber() != null && o2.getRemoveRequest().getRequestItemNumber() != null) {
						return o1.getAddRequest().getRequestItemNumber().compareTo(o2.getRemoveRequest().getRequestItemNumber());
					}
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					if (o1.getRemoveRequest().getRequestItemNumber() != null && o2.getRemoveRequest().getRequestItemNumber() != null) {
						return o1.getRemoveRequest().getRequestItemNumber().compareTo(o2.getRemoveRequest().getRequestItemNumber());
					}
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					if (o1.getRemoveRequest().getRequestItemNumber() != null && o2.getAddRequest().getRequestItemNumber() != null) {
						return o1.getRemoveRequest().getRequestItemNumber().compareTo(o2.getAddRequest().getRequestItemNumber());
					}
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(reqItemNumberColumn, "Request Number");

		Column<FirewallExceptionRequestSummaryPojo, String> reqStateColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return object.getAddRequest().getRequestState();
				}
				if (object.getRemoveRequest() != null) {
					return object.getRemoveRequest().getRequestState();
				}
				return Constants.UNKNOWN;
			}
		};
		reqStateColumn.setSortable(true);
		reqStateColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(reqStateColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return o1.getAddRequest().getRequestState().compareTo(o2.getAddRequest().getRequestState());
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return o1.getAddRequest().getRequestState().compareTo(o2.getRemoveRequest().getRequestState());
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return o1.getRemoveRequest().getRequestState().compareTo(o2.getRemoveRequest().getRequestState());
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return o1.getRemoveRequest().getRequestState().compareTo(o2.getAddRequest().getRequestState());
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(reqStateColumn, "Request State");

		Column<FirewallExceptionRequestSummaryPojo, String> netIdColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return object.getAddRequest().getUserNetId();
				}
				if (object.getRemoveRequest() != null) {
					return object.getRemoveRequest().getUserNetId();
				}
				return Constants.UNKNOWN;
			}
		};
		netIdColumn.setSortable(true);
		netIdColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(netIdColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return o1.getAddRequest().getUserNetId().compareTo(o2.getAddRequest().getUserNetId());
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return o1.getAddRequest().getUserNetId().compareTo(o2.getRemoveRequest().getUserNetId());
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return o1.getRemoveRequest().getUserNetId().compareTo(o2.getRemoveRequest().getUserNetId());
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return o1.getRemoveRequest().getUserNetId().compareTo(o2.getAddRequest().getUserNetId());
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(netIdColumn, "Requestor NetID");

		Column<FirewallExceptionRequestSummaryPojo, String> appNameColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return object.getAddRequest().getApplicationName();
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		appNameColumn.setSortable(true);
		appNameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(appNameColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return o1.getAddRequest().getApplicationName().compareTo(o2.getAddRequest().getApplicationName());
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return 1;
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return -1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(appNameColumn, "Application Name");

		Column<FirewallExceptionRequestSummaryPojo, String> sourceIpColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return object.getAddRequest().getSourceIp();
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		sourceIpColumn.setSortable(true);
		sourceIpColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(sourceIpColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return o1.getAddRequest().getSourceIp().compareTo(o2.getAddRequest().getSourceIp());
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return 1;
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return -1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(sourceIpColumn, "Source IP(s)");

		Column<FirewallExceptionRequestSummaryPojo, String> destIpColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return object.getAddRequest().getDestinationIp();
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		destIpColumn.setSortable(true);
		destIpColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(destIpColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return o1.getAddRequest().getDestinationIp().compareTo(o2.getAddRequest().getDestinationIp());
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return 1;
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return -1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(destIpColumn, "Destination IP(s)");

		Column<FirewallExceptionRequestSummaryPojo, String> portsColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					return object.getAddRequest().getPorts();
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		portsColumn.setSortable(true);
		portsColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(portsColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return o1.getAddRequest().getPorts().compareTo(o2.getAddRequest().getPorts());
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return 1;
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return -1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(portsColumn, "Port(s)");

		Column<FirewallExceptionRequestSummaryPojo, SafeHtml> tagsColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getAddRequest().getTags()) {
						if (!isFirst) {
							sbuf.append("<br>");
						}
						else {
							isFirst = false;
						}
						sbuf.append(s);
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
				}
				if (object.getRemoveRequest() != null) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getRemoveRequest().getTags()) {
						if (!isFirst) {
							sbuf.append("<br>");
						}
						else {
							isFirst = false;
						}
						sbuf.append(s);
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(Constants.UNKNOWN);
			}
		};
		firewallExceptionRequestListTable.addColumn(tagsColumn, "Tag(s)");
		
		Column<FirewallExceptionRequestSummaryPojo, String> traverseVpnColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					if (object.getAddRequest().getWillTraverseVPN() != null) {
						return object.getAddRequest().getWillTraverseVPN();
					}
					else {
						return Constants.UNKNOWN;
					}
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		traverseVpnColumn.setSortable(true);
		traverseVpnColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(traverseVpnColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					if (o1.getAddRequest().getWillTraverseVPN() != null && o2.getAddRequest().getWillTraverseVPN() != null) {
						return o1.getAddRequest().getWillTraverseVPN().compareTo(o2.getAddRequest().getWillTraverseVPN());
					}
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return 1;
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return -1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(traverseVpnColumn, "Traverses VPN");

		Column<FirewallExceptionRequestSummaryPojo, String> vpnNameColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					if (object.getAddRequest().getWillTraverseVPN() != null) {
						if (object.getAddRequest().getWillTraverseVPN().equalsIgnoreCase(Constants.YES)) {
							return object.getAddRequest().getVpnName();
						}
						else {
							return Constants.NOT_APPLICABLE;
						}
					}
					else {
						return Constants.UNKNOWN;
					}
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		vpnNameColumn.setSortable(true);
		vpnNameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(vpnNameColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					if (o1.getAddRequest().getVpnName() != null && o2.getAddRequest().getVpnName() != null) {
						return o1.getAddRequest().getVpnName().compareTo(o2.getAddRequest().getVpnName());
					}
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return 1;
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return -1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(vpnNameColumn, "VPN Name");
		
		Column<FirewallExceptionRequestSummaryPojo, String> accessesVpcColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getAddRequest() != null) {
					if (object.getAddRequest().getAccessAwsVPC() != null) {
						return object.getAddRequest().getAccessAwsVPC();
					}
					else {
						return Constants.UNKNOWN;
					}
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		accessesVpcColumn.setSortable(true);
		accessesVpcColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(accessesVpcColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					if (o1.getAddRequest().getAccessAwsVPC() != null && o2.getAddRequest().getAccessAwsVPC() != null) {
						return o1.getAddRequest().getAccessAwsVPC().compareTo(o2.getAddRequest().getAccessAwsVPC());
					}
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return 1;
				}
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return -1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(accessesVpcColumn, "Accesses AWS VPC");

		Column<FirewallExceptionRequestSummaryPojo, String> requestDetailsColumn = 
				new Column<FirewallExceptionRequestSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestSummaryPojo object) {
				if (object.getRemoveRequest() != null) {
					return object.getRemoveRequest().getRequestDetails();
				}
				return Constants.NOT_APPLICABLE;
			}
		};
		requestDetailsColumn.setSortable(true);
		requestDetailsColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(requestDetailsColumn, new Comparator<FirewallExceptionRequestSummaryPojo>() {
			public int compare(FirewallExceptionRequestSummaryPojo o1, FirewallExceptionRequestSummaryPojo o2) {
				if (o1.getRemoveRequest() != null && o2.getRemoveRequest() != null) {
					return o1.getRemoveRequest().getRequestDetails().compareTo(o2.getRemoveRequest().getRequestDetails());
				}
				if (o1.getAddRequest() != null && o2.getRemoveRequest() != null) {
					return -1;
				}
				if (o1.getAddRequest() != null && o2.getAddRequest() != null) {
					return 0;
				}
				if (o1.getRemoveRequest() != null && o2.getAddRequest() != null) {
					return 1;
				}
				return 0;
			}
		});
		firewallExceptionRequestListTable.addColumn(requestDetailsColumn, "Request Details");
	}

	@Override
	public void clearFirewallRuleExceptionRequestList() {
		firewallExceptionRequestListTable.setVisibleRangeAndClearData(firewallExceptionRequestListTable.getVisibleRange(), true);
	}

	@Override
	public void initPage() {
		if (firewallRuleTabPanel.getSelectedIndex() == 0) {
			presenter.refreshList(userLoggedIn);
		}
		else {
			presenter.refreshFirewallExceptionRequestSummaryList(userLoggedIn);
		}

	}
	@Override
	public void applyCentralAdminMask() {
		

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
		firewallExceptionRequestButton.setEnabled(false);
		firewallExceptionRequestActionsButton.setEnabled(false);
		firewallRuleActionsButton.setEnabled(false);
	}
	@Override
	public void enableButtons() {
		firewallExceptionRequestButton.setEnabled(true);
		firewallExceptionRequestActionsButton.setEnabled(true);
		firewallRuleActionsButton.setEnabled(true);
	}
	@Override
	public void applyNetworkAdminMask() {
		

	}
	@Override
	public void removeFirewallExceptionRequestSummaryFromView(FirewallExceptionRequestSummaryPojo selected) {
		fwerSummary_dataProvider.getList().remove(selected);
	}
}
