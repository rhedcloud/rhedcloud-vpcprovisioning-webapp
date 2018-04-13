package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
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
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentView;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListCidrAssignment extends ViewImplBase implements ListCidrAssignmentView {
	Presenter presenter;
	private ListDataProvider<CidrAssignmentSummaryPojo> dataProvider = new ListDataProvider<CidrAssignmentSummaryPojo>();
	private SingleSelectionModel<CidrAssignmentSummaryPojo> selectionModel;
	List<CidrAssignmentSummaryPojo> cidrAssignmentSummaryList = new java.util.ArrayList<CidrAssignmentSummaryPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager cidrAssignmentSummaryListPager;
	@UiField Button addCidrAssignmentButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<CidrAssignmentSummaryPojo> cidrAssignmentSummaryListTable = new CellTable<CidrAssignmentSummaryPojo>();
	@UiField VerticalPanel cidrAssignmentSummaryListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListCidrAssignmentUiBinder uiBinder = GWT.create(DesktopListCidrAssignmentUiBinder.class);

	interface DesktopListCidrAssignmentUiBinder extends UiBinder<Widget, DesktopListCidrAssignment> {
	}

	public DesktopListCidrAssignment() {
		initWidget(uiBinder.createAndBindUi(this));
		
		addCidrAssignmentButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_CIDR_ASSIGNMENT);
			}
		}, ClickEvent.getType());
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
	    Grid grid = new Grid(2, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
//		Anchor assignAnchor = new Anchor("Assign CIDR(s)");
//		assignAnchor.addStyleName("productAnchor");
//		assignAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
//		assignAnchor.setTitle("Assign selected CIDR");
//		assignAnchor.ensureDebugId(assignAnchor.getText());
//		assignAnchor.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				actionsPopup.hide();
//				CidrAssignmentSummaryPojo m = selectionModel.getSelectedObject();
//				if (m != null) {
//					// just use a popup here and not try to show the "normal" CidrAssignment
//					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//					if (m.getCidrAssignment() != null) {
////						ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_CIDR_ASSIGNMENT, m.getCidr(), null);
//					}
//					else {
//						showMessageToUser("Please select an UNASSIGNED CIDR from the list");
//					}
//				}
//				else {
//					showMessageToUser("Please select a CIDR Assignment from the list");
//				}
//			}
//		});
//		grid.setWidget(0, 0, assignAnchor);
		
		Anchor unassignAnchor = new Anchor("Unassign CIDR(s)");
		unassignAnchor.addStyleName("productAnchor");
		unassignAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		unassignAnchor.setTitle("Unassign selected CIDR");
		unassignAnchor.ensureDebugId(unassignAnchor.getText());
		unassignAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CidrAssignmentSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (m.getCidrAssignment() != null) {
						showMessageToUser("Will un-assign CIDR assignment: " + m.getCidrAssignment().getCidrAssignmentId());
						// TODO: CidrAssignment.Delete-Request ????
//						presenter.deleteCidrAssignment(m);
					}
					else {
						showMessageToUser("Please select an ASSIGNED CIDR from the list");
					}
				}
				else {
					showMessageToUser("Please select a CIDR from the list");
				}
			}
		});
		grid.setWidget(0, 0, unassignAnchor);
		
		Anchor editAssignmentAnchor = new Anchor("Edit CIDR assignment");
		editAssignmentAnchor.addStyleName("productAnchor");
		editAssignmentAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAssignmentAnchor.setTitle("Edit selected CIDR");
		editAssignmentAnchor.ensureDebugId(editAssignmentAnchor.getText());
		editAssignmentAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CidrAssignmentSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (m.getCidrAssignment() != null) {
						showMessageToUser("Will edit CIDR assignment: " + m.getCidrAssignment().getCidrAssignmentId());
//						ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_CIDR_ASSIGNMENT, m);
					}
					else {
						showMessageToUser("Please select an ASSIGNED CIDR from the list");
					}
				}
				else {
					showMessageToUser("Please select a CIDR from the list");
				}
			}
		});
		grid.setWidget(1, 0, editAssignmentAnchor);

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
		cidrAssignmentSummaryListTable.setVisibleRangeAndClearData(cidrAssignmentSummaryListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setCidrAssignmentSummaries(List<CidrAssignmentSummaryPojo> cidrAssignments) {
		this.cidrAssignmentSummaryList = cidrAssignments;
		GWT.log("initializing cidr assignment table");
		this.initializeCidrAssignmentSummaryListTable();
		GWT.log("cidr assignment table initialized");
	    cidrAssignmentSummaryListPager.setDisplay(cidrAssignmentSummaryListTable);
	}

	private Widget initializeCidrAssignmentSummaryListTable() {
		GWT.log("initializing CIDR_ASSIGNMENT list table...");
		cidrAssignmentSummaryListTable.setTableLayoutFixed(false);
		cidrAssignmentSummaryListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		cidrAssignmentSummaryListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<CidrAssignmentSummaryPojo>();
		dataProvider.addDataDisplay(cidrAssignmentSummaryListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.cidrAssignmentSummaryList);
		
		selectionModel = 
	    	new SingleSelectionModel<CidrAssignmentSummaryPojo>(CidrAssignmentSummaryPojo.KEY_PROVIDER);
		cidrAssignmentSummaryListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		CidrAssignmentSummaryPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected CidrAssignmentSummary is: " + m.getCidrAssignment().getCidrAssignmentId());
	    	}
	    });

	    ListHandler<CidrAssignmentSummaryPojo> sortHandler = 
	    	new ListHandler<CidrAssignmentSummaryPojo>(dataProvider.getList());
	    cidrAssignmentSummaryListTable.addColumnSortHandler(sortHandler);

	    if (cidrAssignmentSummaryListTable.getColumnCount() == 0) {
		    initcidrAssignmentSummaryListTableColumns(sortHandler);
	    }
		
		return cidrAssignmentSummaryListTable;
	}
	private void initcidrAssignmentSummaryListTableColumns(ListHandler<CidrAssignmentSummaryPojo> sortHandler) {
		GWT.log("initializing CIDR_ASSIGNMENT list table columns...");
	    Column<CidrAssignmentSummaryPojo, Boolean> checkColumn = new Column<CidrAssignmentSummaryPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(CidrAssignmentSummaryPojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    cidrAssignmentSummaryListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    cidrAssignmentSummaryListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		/*
<!ELEMENT CidrAssignment (CidrAssignmentId?, OwnerId, Decription, Purpose, Cidr,CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?) >
		 */
		// CIDR column
		Column<CidrAssignmentSummaryPojo, String> cidrColumn = 
			new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				return object.getCidrAssignment().getCidr().toString();
			}
		};
		cidrColumn.setSortable(true);
		sortHandler.setComparator(cidrColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				return o1.getCidrAssignment().getCidrAssignmentId().compareTo(o2.getCidrAssignment().getCidrAssignmentId());
			}
		});
		cidrAssignmentSummaryListTable.addColumn(cidrColumn, "CIDR");
		
		// vpc id
		Column<CidrAssignmentSummaryPojo, String> vpcIdColumn = 
			new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				if (object.getVpc() == null) {
					GWT.log("null vpc, for CidrAssignmentSummary " + 
							object.getCidrAssignment().getOwnerId() + 
							" 'data issue'");
					return "Data Issue - null vpc (" + object.getCidrAssignment().getOwnerId() +")";
				}
				else {
					return object.getVpc().getVpcId();
				}
			}
		};
		vpcIdColumn.setSortable(true);
		sortHandler.setComparator(vpcIdColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				if (o1.getVpc() != null && o2.getVpc() != null) {
					return o1.getVpc().getVpcId().compareTo(o2.getVpc().getVpcId());
				}
				GWT.log("null vpc for CidrAssignmentSummary " + 
						o1.getCidrAssignment().getOwnerId() + 
						", returning 0");
				return 0;
			}
		});
		cidrAssignmentSummaryListTable.addColumn(vpcIdColumn, "Owner ID");
		
		// AWS Account
		Column<CidrAssignmentSummaryPojo, String> awsColumn = 
			new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				if (object.getAccount() == null) {
					GWT.log("null account, for CidrAssignmentSummary " + 
							object.getCidrAssignment().getOwnerId() + 
							" 'data issue'");
					return "Data Issue - null account (" + object.getCidrAssignment().getOwnerId() + ")";
				}
				else {
					return object.getAccount().getAccountName();
				}
			}
		};
		awsColumn.setSortable(true);
		sortHandler.setComparator(awsColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				return o1.getAccount().getAccountName().compareTo(o2.getAccount().getAccountName());
			}
		});
		cidrAssignmentSummaryListTable.addColumn(awsColumn, "AWS Account");
		
		// AWS Account OwnerId
		Column<CidrAssignmentSummaryPojo, String> acctOwnerColumn = 
				new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				if (object.getAccount() == null) {
					GWT.log("null account, for CidrAssignmentSummary " + 
							object.getCidrAssignment().getOwnerId() + 
							" 'data issue'");
					return "Data Issue - null account (" + object.getCidrAssignment().getOwnerId() + ")";
				}
				else {
					return object.getAccount().getAccountOwnerDirectoryMetaData().getNetId() + 
							" (" + object.getAccount().getAccountOwnerDirectoryMetaData().getFirstName() + 
							" " + object.getAccount().getAccountOwnerDirectoryMetaData().getLastName() + 
							")";
				}
			}
		};
		acctOwnerColumn.setSortable(true);
		sortHandler.setComparator(acctOwnerColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				if (o1.getAccount() != null && o2.getAccount() != null) {
					return o1.getAccount().getAccountOwnerDirectoryMetaData().getNetId().compareTo(o2.getAccount().getAccountOwnerDirectoryMetaData().getNetId());
				}
				GWT.log("null account for CidrAssignmentSummary " + 
						o1.getCidrAssignment().getOwnerId() + 
						", returning 0");
				return 0;
			}
		});
		cidrAssignmentSummaryListTable.addColumn(acctOwnerColumn, "Account Owner NetId");

		// create user
		Column<CidrAssignmentSummaryPojo, String> createUserColumn = 
				new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				return object.getCidrAssignment().getCreateUser();
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				return o1.getCidrAssignment().getCreateUser().compareTo(o2.getCidrAssignment().getCreateUser());
			}
		});
		cidrAssignmentSummaryListTable.addColumn(createUserColumn, "Create User");

		// create time
		Column<CidrAssignmentSummaryPojo, String> createTimeColumn = 
				new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				return dateFormat.format(object.getCidrAssignment().getCreateTime());
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				return o1.getCidrAssignment().getCreateTime().compareTo(o2.getCidrAssignment().getCreateTime());
			}
		});
		cidrAssignmentSummaryListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<CidrAssignmentSummaryPojo, String> lastUpdateUserColumn = 
				new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				return object.getCidrAssignment().getUpdateUser();
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				return o1.getCidrAssignment().getUpdateUser().compareTo(o2.getCidrAssignment().getUpdateUser());
			}
		});
		cidrAssignmentSummaryListTable.addColumn(lastUpdateUserColumn, "Update User");

		// update time
		Column<CidrAssignmentSummaryPojo, String> updateTimeColumn = 
				new Column<CidrAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				return dateFormat.format(object.getCidrAssignment().getUpdateTime());
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<CidrAssignmentSummaryPojo>() {
			public int compare(CidrAssignmentSummaryPojo o1, CidrAssignmentSummaryPojo o2) {
				return o1.getCidrAssignment().getUpdateTime().compareTo(o2.getCidrAssignment().getUpdateTime());
			}
		});
		cidrAssignmentSummaryListTable.addColumn(updateTimeColumn, "Update Time");

		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING_FOR_ACCOUNT)) {
			GWT.log(userLoggedIn.getEppn() + " is an admin (delete)");
			GWT.log("delete row column");
			// delete row column
			Column<CidrAssignmentSummaryPojo, String> deleteRowColumn = new Column<CidrAssignmentSummaryPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(CidrAssignmentSummaryPojo object) {
					return "Delete";
				}
			};
			deleteRowColumn.setCellStyleNames("glowing-border");
			cidrAssignmentSummaryListTable.addColumn(deleteRowColumn, "");
			cidrAssignmentSummaryListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
			GWT.log("delete row column field updater");
			deleteRowColumn
			.setFieldUpdater(new FieldUpdater<CidrAssignmentSummaryPojo, String>() {
				@Override
				public void update(int index, final CidrAssignmentSummaryPojo cidrAssignmentSummary,
						String value) {
	
					presenter.deleteCidrAssignment(cidrAssignmentSummary);
				}
			});
		}
		else {
			GWT.log(userLoggedIn.getEppn() + " is NOT an admin (delete)");
		}

		// view row column
		GWT.log("view/edit row column");
		Column<CidrAssignmentSummaryPojo, String> editRowColumn = new Column<CidrAssignmentSummaryPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(CidrAssignmentSummaryPojo object) {
				if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING_FOR_ACCOUNT)) {
					GWT.log(userLoggedIn.getEppn() + " is an admin (edit)");
					return "Edit";
				}
				else {
					GWT.log(userLoggedIn.getEppn() + " is NOT an admin (edit)");
					return "View";
				}
			}
		};
		GWT.log("view/edit row field updater");
		editRowColumn.setCellStyleNames("glowing-border");
		cidrAssignmentSummaryListTable.addColumn(editRowColumn, "");
		cidrAssignmentSummaryListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
		editRowColumn.setFieldUpdater(new FieldUpdater<CidrAssignmentSummaryPojo, String>() {
			@Override
			public void update(int index, final CidrAssignmentSummaryPojo cidrAssignmentSummary,
					String value) {
				
				// fire MAINTAIN_CIDR_ASSIGNMENT event passing the CidrAssignment to be maintained
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_CIDR_ASSIGNMENT, null, cidrAssignmentSummary);
			}
		});
	}
	
	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
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
	}

	@Override
	public Widget getStatusMessageSource() {
		return cidrAssignmentSummaryListTable;
	}

	@Override
	public void removeCidrAssignmentSummaryFromView(CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		dataProvider.getList().remove(cidrAssignmentSummary);
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// enable add button
		addCidrAssignmentButton.setEnabled(true);
		// enable Delete button in table (handled in init...ListTableColumns)
		// change text of button to Edit (handled in init...ListTableColumns)
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// disable add button
		addCidrAssignmentButton.setEnabled(false);
		// disable Delete button in table (handled in init...ListTableColumns)
		// change text of button to Edit (handled in init...ListTableColumns)
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
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
