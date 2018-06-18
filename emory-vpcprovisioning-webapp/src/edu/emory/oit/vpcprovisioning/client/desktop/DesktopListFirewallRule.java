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
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
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
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopListFirewallRule extends ViewImplBase implements ListFirewallRuleView {
	Presenter presenter;
	private ListDataProvider<FirewallRulePojo> fw_dataProvider = new ListDataProvider<FirewallRulePojo>();
	private SingleSelectionModel<FirewallRulePojo> fw_selectionModel;
	List<FirewallRulePojo> firewallRuleList = new java.util.ArrayList<FirewallRulePojo>();

	private ListDataProvider<FirewallExceptionRequestPojo> fwer_dataProvider = new ListDataProvider<FirewallExceptionRequestPojo>();
	private SingleSelectionModel<FirewallExceptionRequestPojo> fwer_selectionModel;
	List<FirewallExceptionRequestPojo> fwerRuleList = new java.util.ArrayList<FirewallExceptionRequestPojo>();

	List<VpcPojo> vpcs;
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager firewallRuleListPager;
	@UiField SimplePager firewallExceptionRequestListPager;
	@UiField Button firewallExceptionRequestButton;
	@UiField Button firewallExceptionRequestActionsButton;
	@UiField Button firewallRuleActionsButton;
	@UiField(provided=true) CellTable<FirewallRulePojo> firewallRuleListTable = new CellTable<FirewallRulePojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField(provided=true) CellTable<FirewallExceptionRequestPojo> firewallExceptionRequestListTable = new CellTable<FirewallExceptionRequestPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField VerticalPanel firewallRuleListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField TabLayoutPanel firewallRuleTabPanel;

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

//		firewallExceptionRequestButton.addDomHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				GWT.log("Should go to maintain firewallexceptionrequest here...");
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST);
//			}
//		}, ClickEvent.getType());
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	 }
	
	@UiHandler ("firewallRuleTabPanel") 
	void tabSelected(SelectionEvent<Integer> e) {
		switch (e.getSelectedItem()) {
			case 0:
				presenter.refreshFirewallRuleList(userLoggedIn);
				break;
			case 1:
				presenter.refreshFirewallRuleExceptionRequestList(userLoggedIn);
				break;
		}
	}
	@UiHandler("firewallRuleActionsButton")
	void firewallRuleActionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
		if (userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) || 
				userLoggedIn.isLitsAdmin()) {

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
					FirewallRulePojo m = fw_selectionModel.getSelectedObject();
					if (m != null) {
						ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, m, presenter.getVpc());
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
					FirewallRulePojo m = null;
					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, m, presenter.getVpc());
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
					FirewallRulePojo m = fw_selectionModel.getSelectedObject();
					if (m != null) {
						// just use a popup here and not try to show the "normal" CidrAssignment
						// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//						ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_RULE, m, null);
					}
					else {
						showMessageToUser("Please select an item from the list");
					}
				}
			});
			grid.setWidget(2, 0, deleteAnchor);
			actionsPopup.showRelativeTo(firewallRuleActionsButton);
		}
		else {
			// user not authorized to perform any actions on selected item...
			showMessageToUser("You are not authorized to perform any actions on Firewall Rules.");
		}
	}
	
	@UiHandler("firewallExceptionRequestButton")
	void fer_addButtonClicked(ClickEvent e) {
		GWT.log("Should go to maintain firewallexceptionrequest here...");
		GWT.log("[firewallExceptionRequestButton] presenter.getVpc() is: " + presenter.getVpc());
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, presenter.getVpc());
	}
	
	@UiHandler("firewallExceptionRequestActionsButton")
	void fer_actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
	    Grid grid = new Grid(2, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
	    // anchors for:
	    // - view/edit
	    // - delete
	    String anchorText = "View Firewall Exception Request";
	    if (userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) ||
	    	userLoggedIn.isLitsAdmin()) {
	    	
			anchorText = "Maintain Firewall Exception Request";
	    }

		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("View/edit selected Firewall Exception Request");
		maintainAnchor.ensureDebugId(anchorText);
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				FirewallExceptionRequestPojo m = fwer_selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_FIREWALL_EXCEPTION_REQUEST, m, presenter.getVpc());
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor deleteAnchor = new Anchor("Remove Firewall Exception Request");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Remove selected Firewall Exception Request");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
			    if (!userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) &&
				    	!userLoggedIn.isLitsAdmin()) {
			    	
			    	showMessageToUser("User are not authorized to perform this action on this item.");
			    	return;
			    }
				FirewallExceptionRequestPojo m = fwer_selectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_RULE, m, null);
					// TODO: this may actually be a "create" in that we're creating a SN request to remove
					// the firewall exception request
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

//		Anchor historyAnchor = new Anchor("View your Firewall Rule History");
//		historyAnchor.addStyleName("productAnchor");
//		historyAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
//		historyAnchor.setTitle("View firewall rule history");
//		historyAnchor.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				actionsPopup.hide();
//				FirewallRulePojo m = fw_selectionModel.getSelectedObject();
//				if (m != null) {
//					// just use a popup here and not try to show the "normal" CidrAssignment
//					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
////					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_RULE, m, null);
//				}
//				else {
//					showMessageToUser("Please select an item from the list");
//				}
//			}
//		});
//		grid.setWidget(2, 0, historyAnchor);

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

		Column<FirewallRulePojo, SafeHtml> vpcColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getTags()) {
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
		vpcColumn.setSortable(true);
		vpcColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(vpcColumn, new Comparator<FirewallRulePojo>() {
			public int compare(FirewallRulePojo o1, FirewallRulePojo o2) {
				return o1.getAction().compareTo(o2.getAction());
			}
		});
		firewallRuleListTable.addColumn(vpcColumn, "VPC ID");

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

		Column<FirewallRulePojo, SafeHtml> toColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getTos()) {
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
		firewallRuleListTable.addColumn(toColumn, "To(s)");

		Column<FirewallRulePojo, SafeHtml> fromColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getFroms()) {
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
		firewallRuleListTable.addColumn(fromColumn, "From(s)");

		Column<FirewallRulePojo, SafeHtml> sourceUsersColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getSourceUsers()) {
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
		firewallRuleListTable.addColumn(sourceUsersColumn, "Source User(s)");
		
		Column<FirewallRulePojo, SafeHtml> categoriesColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getCategories()) {
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
		firewallRuleListTable.addColumn(categoriesColumn, "Category(s)");
		
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
		
		Column<FirewallRulePojo, SafeHtml> hipProfilesColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getHipProfiles()) {
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
		firewallRuleListTable.addColumn(hipProfilesColumn, "HIP Profile(s)");

		Column<FirewallRulePojo, SafeHtml> actionColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(object.getAction());
				}
		};
		firewallRuleListTable.addColumn(actionColumn, "Action");

		Column<FirewallRulePojo, SafeHtml> logSettingColumn = 
				new Column<FirewallRulePojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallRulePojo object) {
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(object.getLogSetting());
				}
		};
		firewallRuleListTable.addColumn(logSettingColumn, "Log Setting");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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

	@Override
	public void setFirewallRuleRequests(List<FirewallExceptionRequestPojo> firewallRequests) {
		this.fwerRuleList = firewallRequests;
		this.initializeFirewallRuleRequestListTable();
	    firewallExceptionRequestListPager.setDisplay(firewallExceptionRequestListTable);
	}

	private Widget initializeFirewallRuleRequestListTable() {
		GWT.log("initializing Firewall Rule Exception Request list table...");
		firewallExceptionRequestListTable.setTableLayoutFixed(false);
		firewallExceptionRequestListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		firewallExceptionRequestListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		fwer_dataProvider = new ListDataProvider<FirewallExceptionRequestPojo>();
		fwer_dataProvider.addDataDisplay(firewallExceptionRequestListTable);
		fwer_dataProvider.getList().clear();
		fwer_dataProvider.getList().addAll(this.fwerRuleList);
		
		fwer_selectionModel = 
	    	new SingleSelectionModel<FirewallExceptionRequestPojo>(FirewallExceptionRequestPojo.KEY_PROVIDER);
		firewallExceptionRequestListTable.setSelectionModel(fwer_selectionModel);
	    
	    fwer_selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		FirewallExceptionRequestPojo m = fwer_selectionModel.getSelectedObject();
	    		GWT.log("Selected firewallException (app Name) is: " + m.getApplicationName());
	    	}
	    });
	    
	    ListHandler<FirewallExceptionRequestPojo> sortHandler = 
	    	new ListHandler<FirewallExceptionRequestPojo>(fwer_dataProvider.getList());
	    firewallExceptionRequestListTable.addColumnSortHandler(sortHandler);

	    if (firewallExceptionRequestListTable.getColumnCount() == 0) {
		    initFirewallRuleRequestListTableColumns(sortHandler);
	    }
		
		return firewallExceptionRequestListTable;
	}

	private void initFirewallRuleRequestListTableColumns(ListHandler<FirewallExceptionRequestPojo> sortHandler) {
		/*
             <UserNetID>gwang28</UserNetID>
             <ApplicationName>JBossEAP</ApplicationName>
             <IsSourceOutsideEmory>No</IsSourceOutsideEmory>
             <TimeRule>SpecificDate</TimeRule>
             <ValidUntilDate>2020-12-31</ValidUntilDate>
             <SourceIpAddresses>10.231.51.207</SourceIpAddresses>
             <DestinationIpAddresses>23.20.53.144</DestinationIpAddresses>
             <Ports>443</Ports>
             <BusinessReason>access EC2 instances in AWS</BusinessReason>
             <IsPatched>Yes</IsPatched>
             <IsDefaultPasswdChanged>Yes</IsDefaultPasswdChanged>
             <IsAppConsoleACLed>Yes</IsAppConsoleACLed>
             <IsHardened>Yes</IsHardened>
             <PatchingPlan>quarterly patching by LITS</PatchingPlan>
             <Compliance>HIPAA</Compliance> 
             <Compliance>Other</Compliance>
             <OtherCompliance>SOX</OtherCompliance>
             <SensitiveDataDesc>financial information</SensitiveDataDesc>
             <LocalFirewallRules></LocalFirewallRules>
             <IsDefaultDenyZone>Yes</IsDefaultDenyZone>
             <Tag>vpc-2f09a348</Tag>
             <Tag>SOM</Tag>
             <Tag>Emory</Tag>
		 */
	    Column<FirewallExceptionRequestPojo, Boolean> checkColumn = new Column<FirewallExceptionRequestPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(FirewallExceptionRequestPojo object) {
		        // Get the value from the selection model.
		        return fwer_selectionModel.isSelected(object);
		      }
		    };
		    firewallExceptionRequestListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    firewallExceptionRequestListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<FirewallExceptionRequestPojo, String> reqNumberColumn = 
				new Column<FirewallExceptionRequestPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestPojo object) {
				return object.getRequestNumber();
			}
		};
		reqNumberColumn.setSortable(true);
		reqNumberColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(reqNumberColumn, new Comparator<FirewallExceptionRequestPojo>() {
			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
				return o1.getRequestNumber().compareTo(o2.getRequestNumber());
			}
		});
		firewallExceptionRequestListTable.addColumn(reqNumberColumn, "Request Number");

		Column<FirewallExceptionRequestPojo, String> reqStateColumn = 
				new Column<FirewallExceptionRequestPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestPojo object) {
				return object.getRequestState();
			}
		};
		reqStateColumn.setSortable(true);
		reqStateColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(reqStateColumn, new Comparator<FirewallExceptionRequestPojo>() {
			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
				return o1.getRequestState().compareTo(o2.getRequestState());
			}
		});
		firewallExceptionRequestListTable.addColumn(reqStateColumn, "Request State");

		Column<FirewallExceptionRequestPojo, String> netIdColumn = 
				new Column<FirewallExceptionRequestPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestPojo object) {
				return object.getUserNetId();
			}
		};
		netIdColumn.setSortable(true);
		netIdColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(netIdColumn, new Comparator<FirewallExceptionRequestPojo>() {
			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
				return o1.getUserNetId().compareTo(o2.getUserNetId());
			}
		});
		firewallExceptionRequestListTable.addColumn(netIdColumn, "Requestor NetID");

		Column<FirewallExceptionRequestPojo, String> appNameColumn = 
				new Column<FirewallExceptionRequestPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestPojo object) {
				return object.getApplicationName();
			}
		};
		appNameColumn.setSortable(true);
		appNameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(appNameColumn, new Comparator<FirewallExceptionRequestPojo>() {
			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
				return o1.getApplicationName().compareTo(o2.getApplicationName());
			}
		});
		firewallExceptionRequestListTable.addColumn(appNameColumn, "Application Name");

		Column<FirewallExceptionRequestPojo, String> sourceIpColumn = 
				new Column<FirewallExceptionRequestPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestPojo object) {
				return object.getSourceIp();
			}
		};
		sourceIpColumn.setSortable(true);
		sourceIpColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(sourceIpColumn, new Comparator<FirewallExceptionRequestPojo>() {
			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
				return o1.getSourceIp().compareTo(o2.getSourceIp());
			}
		});
		firewallExceptionRequestListTable.addColumn(sourceIpColumn, "Source IP(s)");

		Column<FirewallExceptionRequestPojo, String> destIpColumn = 
				new Column<FirewallExceptionRequestPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestPojo object) {
				return object.getDestinationIp();
			}
		};
		destIpColumn.setSortable(true);
		destIpColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(destIpColumn, new Comparator<FirewallExceptionRequestPojo>() {
			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
				return o1.getDestinationIp().compareTo(o2.getDestinationIp());
			}
		});
		firewallExceptionRequestListTable.addColumn(destIpColumn, "Destination IP(s)");

		Column<FirewallExceptionRequestPojo, String> portsColumn = 
				new Column<FirewallExceptionRequestPojo, String> (new TextCell()) {

			@Override
			public String getValue(FirewallExceptionRequestPojo object) {
				return object.getPorts();
			}
		};
		portsColumn.setSortable(true);
		portsColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(portsColumn, new Comparator<FirewallExceptionRequestPojo>() {
			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
				return o1.getPorts().compareTo(o2.getPorts());
			}
		});
		firewallExceptionRequestListTable.addColumn(portsColumn, "Port(s)");

		Column<FirewallExceptionRequestPojo, SafeHtml> tagsColumn = 
				new Column<FirewallExceptionRequestPojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(FirewallExceptionRequestPojo object) {
					StringBuffer sbuf = new StringBuffer();
					boolean isFirst = true;
					for (String s : object.getTags()) {
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
//		tagsColumn.setSortable(true);
//		tagsColumn.setCellStyleNames("tableBody");
//		sortHandler.setComparator(tagsColumn, new Comparator<FirewallExceptionRequestPojo>() {
//			public int compare(FirewallExceptionRequestPojo o1, FirewallExceptionRequestPojo o2) {
//				return o1.getTags().compareTo(o2.getTags());
//			}
//		});
		firewallExceptionRequestListTable.addColumn(tagsColumn, "Tag(s)");
	}
	
	@Override
	public void clearFirewallRuleExceptionRequestList() {
		firewallExceptionRequestListTable.setVisibleRangeAndClearData(firewallExceptionRequestListTable.getVisibleRange(), true);
	}

	@Override
	public void initPage() {
//		filterTB.setText("");
//		filterTB.getElement().setPropertyString("placeholder", "enter VPC id");
	}
	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
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
}
