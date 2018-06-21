package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListElasticIp extends ViewImplBase implements ListElasticIpView {
	Presenter presenter;
	private ListDataProvider<ElasticIpSummaryPojo> dataProvider = new ListDataProvider<ElasticIpSummaryPojo>();
	private SingleSelectionModel<ElasticIpSummaryPojo> selectionModel;
	List<ElasticIpSummaryPojo> elasticIpList = new java.util.ArrayList<ElasticIpSummaryPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager elasticIpListPager;
	@UiField Button allocateAddressButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<ElasticIpSummaryPojo> elasticIpListTable = new CellTable<ElasticIpSummaryPojo>();
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListElasticIpUiBinder uiBinder = GWT.create(DesktopListElasticIpUiBinder.class);

	interface DesktopListElasticIpUiBinder extends UiBinder<Widget, DesktopListElasticIp> {
	}

	public DesktopListElasticIp() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("allocateAddressButton")
	void allocateAddressButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_ELASTIC_IP);
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

		Anchor releaseAddressesAnchor = new Anchor("Release Addresses");
		releaseAddressesAnchor.addStyleName("productAnchor");
		releaseAddressesAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		releaseAddressesAnchor.setTitle("Release selected addresses");
		releaseAddressesAnchor.ensureDebugId(releaseAddressesAnchor.getText());
		releaseAddressesAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ElasticIpSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					showMessageToUser("Will release address");
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, releaseAddressesAnchor);
		
		Anchor associateAddressesAnchor = new Anchor("Associate Addresses");
		associateAddressesAnchor.addStyleName("productAnchor");
		associateAddressesAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		associateAddressesAnchor.setTitle("Associate selected addresses");
		associateAddressesAnchor.ensureDebugId(associateAddressesAnchor.getText());
		associateAddressesAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE);
			}
		});
		associateAddressesAnchor.setEnabled(false);
		grid.setWidget(1, 0, associateAddressesAnchor);
		
		Anchor disassociateAddressesAnchor = new Anchor("Disassociate Addresses");
		disassociateAddressesAnchor.addStyleName("productAnchor");
		disassociateAddressesAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		disassociateAddressesAnchor.setTitle("Disassociate selected addresses");
		disassociateAddressesAnchor.ensureDebugId(disassociateAddressesAnchor.getText());
		disassociateAddressesAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ElasticIpSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					showMessageToUser("Will associate address");
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(2, 0, disassociateAddressesAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		actionsButton.setEnabled(true);
		allocateAddressButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		actionsButton.setEnabled(false);
		allocateAddressButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void clearList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setElasticIpSummaries(List<ElasticIpSummaryPojo> elasticIps) {
		this.elasticIpList = elasticIps;
		this.initializeElasticIpListTable();
	    elasticIpListPager.setDisplay(elasticIpListTable);
	}
	private Widget initializeElasticIpListTable() {
		GWT.log("initializing ElasticIP list table...");
		elasticIpListTable.setTableLayoutFixed(false);
		elasticIpListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		elasticIpListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<ElasticIpSummaryPojo>();
		dataProvider.addDataDisplay(elasticIpListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.elasticIpList);
		
		selectionModel = 
	    	new SingleSelectionModel<ElasticIpSummaryPojo>(ElasticIpSummaryPojo.KEY_PROVIDER);
		elasticIpListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		ElasticIpSummaryPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected elsticIp is: " + m.getClass().getName());
	    	}
	    });

	    ListHandler<ElasticIpSummaryPojo> sortHandler = 
	    	new ListHandler<ElasticIpSummaryPojo>(dataProvider.getList());
	    elasticIpListTable.addColumnSortHandler(sortHandler);

	    if (elasticIpListTable.getColumnCount() == 0) {
		    initElasticIpListTableColumns(sortHandler);
	    }
		
		return elasticIpListTable;
	}
	private void initElasticIpListTableColumns(ListHandler<ElasticIpSummaryPojo> sortHandler) {
		GWT.log("initializing ElasticIpSummary list table columns...");
		
	    Column<ElasticIpSummaryPojo, Boolean> checkColumn = new Column<ElasticIpSummaryPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(ElasticIpSummaryPojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    elasticIpListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    elasticIpListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		    // Account id column
		Column<ElasticIpSummaryPojo, String> elasticIpColumn = 
			new Column<ElasticIpSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				return object.getElasticIp().getElasticIpAddress();
			}
		};
		elasticIpColumn.setSortable(true);
		sortHandler.setComparator(elasticIpColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				return o1.getElasticIp().compareTo(o2.getElasticIp());
			}
		});
		elasticIpListTable.addColumn(elasticIpColumn, "Elastic IP");

		// VPC id column
		Column<ElasticIpSummaryPojo, String> associatedIpColumn = 
			new Column<ElasticIpSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				return object.getElasticIp().getAssociatedIpAddress();
			}
		};
		associatedIpColumn.setSortable(true);
		sortHandler.setComparator(associatedIpColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				return o1.getElasticIp().getAssociatedIpAddress().compareTo(o2.getElasticIp().getAssociatedIpAddress());
			}
		});
		elasticIpListTable.addColumn(associatedIpColumn, "Associated Private IP");
		
		Column<ElasticIpSummaryPojo, SafeHtml> assignmentStatusColumn = 
			new Column<ElasticIpSummaryPojo, SafeHtml> (new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("Unassigned");
				}
				else {
					// TODO: more content here
					String s =
						"<b>Assigned</b><br>";
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(s);
				}
			}
		};
		assignmentStatusColumn.setSortable(true);
		sortHandler.setComparator(assignmentStatusColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				return o1.getElasticIp() == null ? 0 : 1;
			}
		});
		elasticIpListTable.addColumn(assignmentStatusColumn, "Assignment Status");

		// create user
		Column<ElasticIpSummaryPojo, String> createUserColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					return object.getElasticIp().getCreateUser();
				}
				else {
					return object.getElasticIpAssignment().getCreateUser();
				}
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					return o1.getElasticIp().getCreateUser().compareTo(o2.getElasticIp().getCreateUser());
				}
				else {
					return o1.getElasticIpAssignment().getCreateUser().
							compareTo(o2.getElasticIpAssignment().getCreateUser());
				}
			}
		});
		elasticIpListTable.addColumn(createUserColumn, "Create User");
		
		// create time
		Column<ElasticIpSummaryPojo, String> createTimeColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					return dateFormat.format(object.getElasticIp().getCreateTime());
				}
				else {
					return dateFormat.format(object.getElasticIpAssignment().getCreateTime());
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					return o1.getElasticIp().getCreateTime().compareTo(o2.getElasticIp().getCreateTime());
				}
				else {
					return o1.getElasticIpAssignment().getCreateTime().
							compareTo(o2.getElasticIpAssignment().getCreateTime());
				}
			}
		});
		elasticIpListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<ElasticIpSummaryPojo, String> lastUpdateUserColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					return object.getElasticIp().getUpdateUser();
				}
				else {
					return object.getElasticIpAssignment().getUpdateUser();
				}
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					return o1.getElasticIp().getUpdateUser().compareTo(o2.getElasticIp().getUpdateUser());
				}
				else {
					return o1.getElasticIpAssignment().getUpdateUser().
							compareTo(o2.getElasticIpAssignment().getUpdateUser());
				}
			}
		});
		elasticIpListTable.addColumn(lastUpdateUserColumn, "Update User");
		
		// update time
		Column<ElasticIpSummaryPojo, String> updateTimeColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					return dateFormat.format(object.getElasticIp().getUpdateTime());
				}
				else {
					return dateFormat.format(object.getElasticIpAssignment().getUpdateTime());
				}
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					return o1.getElasticIp().getUpdateTime().compareTo(o2.getElasticIp().getUpdateTime());
				}
				else {
					return o1.getElasticIpAssignment().getUpdateTime().
							compareTo(o2.getElasticIpAssignment().getUpdateTime());
				}
			}
		});
		elasticIpListTable.addColumn(updateTimeColumn, "Update Time");

		if (userLoggedIn.isCentralAdmin()) {
			GWT.log(userLoggedIn.getEppn() + " is an admin");
			// delete row column
			Column<ElasticIpSummaryPojo, String> deleteRowColumn = new Column<ElasticIpSummaryPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(ElasticIpSummaryPojo object) {
					return "Delete";
				}
			};
			deleteRowColumn.setCellStyleNames("glowing-border");
			elasticIpListTable.addColumn(deleteRowColumn, "");
			elasticIpListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
			deleteRowColumn
			.setFieldUpdater(new FieldUpdater<ElasticIpSummaryPojo, String>() {
				@Override
				public void update(int index, final ElasticIpSummaryPojo vpc,
						String value) {
	
//					presenter.deleteVpc(vpc);
				}
			});
		}

		// edit row column
		Column<ElasticIpSummaryPojo, String> editRowColumn = new Column<ElasticIpSummaryPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (userLoggedIn.isCentralAdmin()) {
					GWT.log(userLoggedIn.getEppn() + " is an admin");
					return "Edit";
				}
				else {
					GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
					return "View";
				}
			}
		};
		editRowColumn.setCellStyleNames("glowing-border");
		elasticIpListTable.addColumn(editRowColumn, "");
		elasticIpListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
		editRowColumn.setFieldUpdater(new FieldUpdater<ElasticIpSummaryPojo, String>() {
			@Override
			public void update(int index, final ElasticIpSummaryPojo vpc,
					String value) {
				
				// fire MAINTAIN_VPC event passing the vpc to be maintained
				GWT.log("[DesktopListVpc] editing ElasticIP: " + vpc.getElasticIp());
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPC, vpc);
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
	public void removeElasticIpSummaryFromView(ElasticIpSummaryPojo elasticIp) {
		// TODO Auto-generated method stub
		
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
