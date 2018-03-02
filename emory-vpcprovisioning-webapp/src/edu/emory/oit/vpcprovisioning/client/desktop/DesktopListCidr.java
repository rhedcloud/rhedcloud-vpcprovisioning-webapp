package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
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
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrView;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListCidr extends ViewImplBase implements ListCidrView {
	Presenter presenter;
	private ListDataProvider<CidrSummaryPojo> cidrDataProvider = new ListDataProvider<CidrSummaryPojo>();
	private SingleSelectionModel<CidrSummaryPojo> cidrSelectionModel;
//	private MultiSelectionModel<CidrPojo> cidrMultiSelectionModel;
	List<CidrSummaryPojo> cidrSummaryList = new java.util.ArrayList<CidrSummaryPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager cidrListPager;
	@UiField Button addCidrButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<CidrSummaryPojo> cidrListTable = new CellTable<CidrSummaryPojo>();
	@UiField VerticalPanel cidrListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListCidrUiBinder uiBinder = GWT.create(DesktopListCidrUiBinder.class);

	interface DesktopListCidrUiBinder extends UiBinder<Widget, DesktopListCidr> {
	}

	public DesktopListCidr() {
		initWidget(uiBinder.createAndBindUi(this));
		
		addCidrButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_CIDR);
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
	    
		Anchor assignAnchor = new Anchor("Assign CIDR(s)");
		assignAnchor.addStyleName("productAnchor");
		assignAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		assignAnchor.setTitle("Assign selected CIDR");
		assignAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
					if (m.getCidr() != null) {
						ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_CIDR_ASSIGNMENT, m.getCidr(), null);
					}
					else {
						showMessageToUser("Please select an UNASSIGNED CIDR from the list");
					}
				}
				else {
					showMessageToUser("Please select a CIDR from the list");
				}
			}
		});
		grid.setWidget(0, 0, assignAnchor);
		
		Anchor unassignAnchor = new Anchor("Unassign CIDR(s)");
		unassignAnchor.addStyleName("productAnchor");
		unassignAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		unassignAnchor.setTitle("Unassign selected CIDR");
		unassignAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
				if (m != null) {
					if (m.getAssignmentSummary() != null) {
						showMessageToUser("Will un-assign CIDR assignment: " + m.getAssignmentSummary().getCidrAssignment().getCidrAssignmentId());
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
		grid.setWidget(1, 0, unassignAnchor);
		
		Anchor editAssignmentAnchor = new Anchor("Edit CIDR assignment");
		editAssignmentAnchor.addStyleName("productAnchor");
		editAssignmentAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAssignmentAnchor.setTitle("Unassign selected CIDR");
		editAssignmentAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
				if (m != null) {
					if (m.getAssignmentSummary() != null) {
						showMessageToUser("Will edit CIDR assignment: " + m.getAssignmentSummary().getCidrAssignment().getCidrAssignmentId());
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
		grid.setWidget(2, 0, editAssignmentAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void clearList() {
		cidrListTable.setVisibleRangeAndClearData(cidrListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setCidrSummaries(List<CidrSummaryPojo> cidrSummaries) {
		this.cidrSummaryList = cidrSummaries;
		this.initializeCidrListTable();
	    cidrListPager.setDisplay(cidrListTable);
	}
	private Widget initializeCidrListTable() {
		GWT.log("initializing CIDR list table...");
		cidrListTable.setTableLayoutFixed(false);
		cidrListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		cidrListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		cidrDataProvider = new ListDataProvider<CidrSummaryPojo>();
		cidrDataProvider.addDataDisplay(cidrListTable);
		cidrDataProvider.getList().clear();
		cidrDataProvider.getList().addAll(this.cidrSummaryList);
		
//	    cidrMultiSelectionModel = new MultiSelectionModel<CidrPojo>(CidrPojo.KEY_PROVIDER);
		cidrSelectionModel = new SingleSelectionModel<CidrSummaryPojo>(CidrSummaryPojo.KEY_PROVIDER);
		cidrListTable.setSelectionModel(cidrSelectionModel);
	    
		cidrSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
//	    		Set<CidrPojo> ms = cidrMultiSelectionModel.getSelectedSet();
//	    		GWT.log("there are " + ms.size() + " cidrs selected");
//	    		GWT.log("Selected cidr is: " + m.getCidrId());
	    	}
	    });
	    
	    ListHandler<CidrSummaryPojo> sortHandler = 
	    	new ListHandler<CidrSummaryPojo>(cidrDataProvider.getList());
	    cidrListTable.addColumnSortHandler(sortHandler);

	    if (cidrListTable.getColumnCount() == 0) {
		    initCidrListTableColumns(sortHandler);
	    }
		
		return cidrListTable;
	}
	private void initCidrListTableColumns(ListHandler<CidrSummaryPojo> sortHandler) {
		GWT.log("initializing CIDR list table columns...");
		// CIDR network column
		Column<CidrSummaryPojo, String> networkColumn = 
			new Column<CidrSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(CidrSummaryPojo object) {
				if (object.getCidr() != null) {
					return object.getCidr().getNetwork();
				}
				else {
					return object.getAssignmentSummary().getCidrAssignment().getCidr().getNetwork();
				}
			}
		};
		networkColumn.setSortable(true);
		sortHandler.setComparator(networkColumn, new Comparator<CidrSummaryPojo>() {
			public int compare(CidrSummaryPojo o1, CidrSummaryPojo o2) {
				if (o1.getCidr() != null) {
					return o1.getCidr().getNetwork().compareTo(o2.getCidr().getNetwork());
				}
				else {
					return o1.getAssignmentSummary().getCidrAssignment().getCidr().getNetwork().compareTo(
							o2.getAssignmentSummary().getCidrAssignment().getCidr().getNetwork());
				}
			}
		});
		cidrListTable.addColumn(networkColumn, "Network");
		
		// cidr bits
		Column<CidrSummaryPojo, String> bitsColumn = 
			new Column<CidrSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(CidrSummaryPojo object) {
				if (object.getCidr() != null) {
					return object.getCidr().getBits();
				}
				else {
					return object.getAssignmentSummary().getCidrAssignment().getCidr().getBits();
				}
			}
		};
		bitsColumn.setSortable(true);
		sortHandler.setComparator(bitsColumn, new Comparator<CidrSummaryPojo>() {
			public int compare(CidrSummaryPojo o1, CidrSummaryPojo o2) {
				if (o1.getCidr() != null) {
					return o1.getCidr().getBits().compareTo(o2.getCidr().getBits());
				}
				else {
					return o1.getAssignmentSummary().getCidrAssignment().getCidr().getBits().compareTo(
							o2.getAssignmentSummary().getCidrAssignment().getCidr().getBits());
				}
			}
		});
		cidrListTable.addColumn(bitsColumn, "Bits");
		
		// TODO: cidr assignement stuff if present
		Column<CidrSummaryPojo, SafeHtml> assignmentStatusColumn = 
				new Column<CidrSummaryPojo, SafeHtml> (new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CidrSummaryPojo object) {
					if (object.getCidr() != null) {
						return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("Unassigned");
					}
					else {
						String s =
							"<b>Assigned</b><br>" + 
							"Account: " + object.getAssignmentSummary().getAccount().getAccountName() + "<br>" +
							"VPC: " + object.getAssignmentSummary().getVpc().getVpcId();
						return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(s);
					}
				}
			};
			assignmentStatusColumn.setSortable(true);
			sortHandler.setComparator(assignmentStatusColumn, new Comparator<CidrSummaryPojo>() {
				public int compare(CidrSummaryPojo o1, CidrSummaryPojo o2) {
					return o1.getCidr() == null ? 0 : 1;
				}
			});
			cidrListTable.addColumn(assignmentStatusColumn, "Assignment Status");

		// create user
		Column<CidrSummaryPojo, String> createUserColumn = 
				new Column<CidrSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrSummaryPojo object) {
				if (object.getCidr() != null) {
					return object.getCidr().getCreateUser();
				}
				else {
					return object.getAssignmentSummary().getCidrAssignment().getCreateUser();
				}
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<CidrSummaryPojo>() {
			public int compare(CidrSummaryPojo o1, CidrSummaryPojo o2) {
				if (o1.getCidr() != null) {
					return o1.getCidr().getCreateUser().compareTo(o2.getCidr().getCreateUser());
				}
				else {
					return o1.getAssignmentSummary().getCidrAssignment().getCreateUser().
							compareTo(o2.getAssignmentSummary().getCidrAssignment().
									getCreateUser());
				}
			}
		});
		cidrListTable.addColumn(createUserColumn, "Create User");

		// create time
		Column<CidrSummaryPojo, String> createTimeColumn = 
				new Column<CidrSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrSummaryPojo object) {
				if (object.getCidr() != null) {
					return dateFormat.format(object.getCidr().getCreateTime());
				}
				else {
					return dateFormat.format(object.getAssignmentSummary().getCidrAssignment().getCreateTime());
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<CidrSummaryPojo>() {
			public int compare(CidrSummaryPojo o1, CidrSummaryPojo o2) {
				if (o1.getCidr() != null) {
					return o1.getCidr().getCreateTime().compareTo(o2.getCidr().getCreateTime());
				}
				else {
					return o1.getAssignmentSummary().getCidrAssignment().getCreateTime().
							compareTo(o2.getAssignmentSummary().getCidrAssignment().getCreateTime());
				}
			}
		});
		cidrListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<CidrSummaryPojo, String> lastUpdateUserColumn = 
				new Column<CidrSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrSummaryPojo object) {
				if (object.getCidr() != null) {
					return object.getCidr().getUpdateUser();
				}
				else {
					return object.getAssignmentSummary().getCidrAssignment().getUpdateUser();
				}
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<CidrSummaryPojo>() {
			public int compare(CidrSummaryPojo o1, CidrSummaryPojo o2) {
				if (o1.getCidr() != null) {
					return o1.getCidr().getUpdateUser().compareTo(o2.getCidr().getUpdateUser());
				}
				else {
					return o1.getAssignmentSummary().getCidrAssignment().getUpdateUser().
							compareTo(o2.getAssignmentSummary().getCidrAssignment().
									getUpdateUser());
				}
			}
		});
		cidrListTable.addColumn(lastUpdateUserColumn, "Update User");

		// update time
		Column<CidrSummaryPojo, String> updateTimeColumn = 
				new Column<CidrSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrSummaryPojo object) {
				if (object.getCidr() != null) {
					return dateFormat.format(object.getCidr().getUpdateTime());
				}
				else {
					return dateFormat.format(object.getAssignmentSummary().getCidrAssignment().getUpdateTime());
				}
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<CidrSummaryPojo>() {
			public int compare(CidrSummaryPojo o1, CidrSummaryPojo o2) {
				if (o1.getCidr() != null) {
					return o1.getCidr().getUpdateTime().compareTo(o2.getCidr().getUpdateTime());
				}
				else {
					return o1.getAssignmentSummary().getCidrAssignment().getUpdateTime().
							compareTo(o2.getAssignmentSummary().getCidrAssignment().getUpdateTime());
				}
			}
		});
		cidrListTable.addColumn(updateTimeColumn, "Update Time");

		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			GWT.log(userLoggedIn.getEppn() + " is an admin");
			// delete row column
			Column<CidrSummaryPojo, String> deleteRowColumn = new Column<CidrSummaryPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(CidrSummaryPojo object) {
					return "Delete";
				}
			};
			deleteRowColumn.setCellStyleNames("glowing-border");
			cidrListTable.addColumn(deleteRowColumn, "");
			cidrListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
			deleteRowColumn
			.setFieldUpdater(new FieldUpdater<CidrSummaryPojo, String>() {
				@Override
				public void update(int index, final CidrSummaryPojo cidr,
						String value) {
					
					// Called when the user clicks the button
					// confirm action
					presenter.deleteCidrSummary(cidr);
				}
			});
		}

		// edit row column
		Column<CidrSummaryPojo, String> editRowColumn = new Column<CidrSummaryPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(CidrSummaryPojo object) {
				if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
					GWT.log(userLoggedIn.getEppn() + " is an admin");
					return "Edit";
				}
				else {
					GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
					return "View";
				}
			}
		};
		editRowColumn.setCellStyleNames("actionButton");
		cidrListTable.addColumn(editRowColumn, "");
		cidrListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
		editRowColumn.setFieldUpdater(new FieldUpdater<CidrSummaryPojo, String>() {
			@Override
			public void update(int index, final CidrSummaryPojo cidr,
					String value) {
				
				// fire MAINTAIN_CIDR event passing the cidr to be maintained
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_CIDR, cidr);
			}
		});
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
	public Widget getStatusMessageSource() {
		return cidrListTable;
	}

	@Override
	public void removeCidrSummaryFromView(CidrSummaryPojo cidrSummary) {
		cidrDataProvider.getList().remove(cidrSummary);
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// enable add button
		addCidrButton.setEnabled(true);
		// enable Delete button in table (handled in init...ListTableColumns)
		// change text of button to Edit (handled in init...ListTableColumns)
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// disable add button
		addCidrButton.setEnabled(false);
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
