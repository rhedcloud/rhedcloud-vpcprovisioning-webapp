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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrView;
import edu.emory.oit.vpcprovisioning.shared.AssociatedCidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
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

	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;

	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		// TODO: filter list by account id typed in accountIdTB
		presenter.filterByVPCId(filterTB.getText());
	}
	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		// clear filter
		filterTB.setText("");
		presenter.clearFilter();
	}

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
	    
	    Grid grid = new Grid(2, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
		Anchor editCidrAnchor = new Anchor("Edit CIDR");
		editCidrAnchor.addStyleName("productAnchor");
		editCidrAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editCidrAnchor.setTitle("Edit selected CIDR");
		editCidrAnchor.ensureDebugId(editCidrAnchor.getText());
		editCidrAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
					if (m.getCidr() != null) {
//						ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_CIDR_ASSIGNMENT, m.getCidr(), null);
						ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_CIDR, m.getCidr(), null);
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
		grid.setWidget(0, 0, editCidrAnchor);
		
		Anchor deleteAnchor = new Anchor("Delete CIDR");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected CIDR");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
				if (m != null) {
//					if (m.getAssignmentSummary() != null) {
//						showMessageToUser("Will un-assign CIDR assignment: " + m.getAssignmentSummary().getCidrAssignment().getCidrAssignmentId());
						// TODO: CidrAssignment.Delete-Request ????
						// TODO: confirm delete
						presenter.deleteCidrSummary(m);
//					}
//					else {
//						showMessageToUser("Please select an ASSIGNED CIDR from the list");
//					}
				}
				else {
					showMessageToUser("Please select a CIDR from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);
		
//		Anchor editAssignmentAnchor = new Anchor("Edit CIDR assignment");
//		editAssignmentAnchor.addStyleName("productAnchor");
//		editAssignmentAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
//		editAssignmentAnchor.setTitle("Edit selected CIDR");
//		editAssignmentAnchor.ensureDebugId(editAssignmentAnchor.getText());
//		editAssignmentAnchor.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				actionsPopup.hide();
//				CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
//				if (m != null) {
//					if (m.getAssignmentSummary() != null) {
////						showMessageToUser("Will edit CIDR assignment: " + m.getAssignmentSummary().getCidrAssignment().getCidrAssignmentId());
//						ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_CIDR_ASSIGNMENT, m);
//					}
//					else {
//						showMessageToUser("Please select an ASSIGNED CIDR from the list");
//					}
//				}
//				else {
//					showMessageToUser("Please select a CIDR from the list");
//				}
//			}
//		});
//		grid.setWidget(2, 0, editAssignmentAnchor);

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
		cidrListTable.setTableLayoutFixed(false);
		cidrListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		cidrListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		cidrDataProvider = new ListDataProvider<CidrSummaryPojo>();
		cidrDataProvider.addDataDisplay(cidrListTable);
		cidrDataProvider.getList().clear();
		cidrDataProvider.getList().addAll(this.cidrSummaryList);
		
		cidrSelectionModel = new SingleSelectionModel<CidrSummaryPojo>(CidrSummaryPojo.KEY_PROVIDER);
		cidrListTable.setSelectionModel(cidrSelectionModel);
	    
		cidrSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		CidrSummaryPojo m = cidrSelectionModel.getSelectedObject();
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
	    Column<CidrSummaryPojo, Boolean> checkColumn = new Column<CidrSummaryPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(CidrSummaryPojo object) {
		        // Get the value from the selection model.
		        return cidrSelectionModel.isSelected(object);
		      }
		    };
		    cidrListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    cidrListTable.setColumnWidth(checkColumn, 40, Unit.PX);

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

		// associated cidr(s)
		Column<CidrSummaryPojo, SafeHtml> associatedCidrsColumn = 
				new Column<CidrSummaryPojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(CidrSummaryPojo object) {
				StringBuffer acps = new StringBuffer();
				int cntr = 1;
				CidrPojo cidr = null;
				if (object.getCidr() != null) {
					cidr = object.getCidr();
				}
				else {
					// get them from the CidrAssignment.Cidr object
					cidr = object.getAssignmentSummary().getCidrAssignment().getCidr();
				}
				if (cidr.getAssociatedCidrs().size() > 0) {
					for (AssociatedCidrPojo acp : cidr.getAssociatedCidrs()) {
						if (cntr == cidr.getAssociatedCidrs().size()) {
							acps.append(acp.getType() + "-" + acp.getNetwork() + "/" + acp.getBits());
						}
						else {
							cntr++;
							acps.append(acp.getType() + "-" + acp.getNetwork() + "/" + acp.getBits() + "</br>");
						}
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(acps.toString());
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("No Associated CIDRs");
			}
		};
		cidrListTable.addColumn(associatedCidrsColumn, "Associated CIDR(s)");
		
		// Property(s)
		Column<CidrSummaryPojo, SafeHtml> propertiesColumn = 
				new Column<CidrSummaryPojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(CidrSummaryPojo object) {
				StringBuffer acps = new StringBuffer();
				int cntr = 1;
				CidrPojo cidr = null;
				if (object.getCidr() != null) {
					cidr = object.getCidr();
				}
				else {
					// get them from the CidrAssignment.Cidr object
					cidr = object.getAssignmentSummary().getCidrAssignment().getCidr();
				}
				if (cidr.getProperties().size() > 0) {
					for (PropertyPojo prop : cidr.getProperties()) {
						if (cntr == cidr.getAssociatedCidrs().size()) {
							acps.append(prop.getName() + "=" + prop.getValue());
						}
						else {
							cntr++;
							acps.append(prop.getName() + "=" + prop.getValue() + "</br>");
						}
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(acps.toString());
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("No Properties");
			}
		};
		cidrListTable.addColumn(propertiesColumn, "Property(s)");
			
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
					if (object.getCidr().getCreateTime() != null) {
						return dateFormat.format(object.getCidr().getCreateTime());
					}
					else {
						return "Unknown";
					}
				}
				else {
					if (object.getAssignmentSummary().getCidrAssignment().getCreateTime() != null) {
						return dateFormat.format(object.getAssignmentSummary().getCidrAssignment().getCreateTime());
					}
					else {
						return "Unknown";
					}
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
					if (object.getCidr().getUpdateTime() != null) {
						return dateFormat.format(object.getCidr().getUpdateTime());
					}
					else {
						return "Uknown";
					}
				}
				else {
					if (object.getAssignmentSummary().getCidrAssignment().getUpdateTime() != null) {
						return dateFormat.format(object.getAssignmentSummary().getCidrAssignment().getUpdateTime());
					}
					else {
						return "Unknown";
					}
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
	public Widget getStatusMessageSource() {
		return cidrListTable;
	}

	@Override
	public void removeCidrSummaryFromView(CidrSummaryPojo cidrSummary) {
		cidrDataProvider.getList().remove(cidrSummary);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// enable add button
		addCidrButton.setEnabled(true);
		actionsButton.setEnabled(true);
		filterButton.setEnabled(true);
		clearFilterButton.setEnabled(true);
		filterTB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// disable add button
		addCidrButton.setEnabled(false);
		actionsButton.setEnabled(false);
		filterButton.setEnabled(false);
		clearFilterButton.setEnabled(false);
		filterTB.setEnabled(false);
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

	@Override
	public void initPage() {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter VPC id");
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
