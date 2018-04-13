package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListElasticIpAssignment extends ViewImplBase implements ListElasticIpAssignmentView {
	Presenter presenter;
	private ListDataProvider<ElasticIpAssignmentPojo> dataProvider = new ListDataProvider<ElasticIpAssignmentPojo>();
	private SingleSelectionModel<ElasticIpAssignmentPojo> selectionModel;
	List<ElasticIpAssignmentPojo> elasticIpAssignmentList = new java.util.ArrayList<ElasticIpAssignmentPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager elasticIpAssignmentListPager;
	@UiField(provided=true) CellTable<ElasticIpAssignmentPojo> elasticIpAssignmentListTable = new CellTable<ElasticIpAssignmentPojo>();
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button allocateAddressButton;
	@UiField Button actionsButton;

	@UiHandler("allocateAddressButton")
	void allocateAddressButtonClicked(ClickEvent e) {
		presenter.generateElasticIpAssignment();
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
				ElasticIpAssignmentPojo m = selectionModel.getSelectedObject();
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
				ElasticIpAssignmentPojo m = selectionModel.getSelectedObject();
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

	private static DesktopListElasticIpAssignmentUiBinder uiBinder = GWT
			.create(DesktopListElasticIpAssignmentUiBinder.class);

	interface DesktopListElasticIpAssignmentUiBinder extends UiBinder<Widget, DesktopListElasticIpAssignment> {
	}

	public DesktopListElasticIpAssignment() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInitialFocus() {
	}

	@Override
	public Widget getStatusMessageSource() {
		return elasticIpAssignmentListTable;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		allocateAddressButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		allocateAddressButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void clearList() {
		elasticIpAssignmentListTable.setVisibleRangeAndClearData(elasticIpAssignmentListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setElasticIpAssignments(List<ElasticIpAssignmentPojo> summaries) {
		this.elasticIpAssignmentList = summaries;
		GWT.log("initializing elastic IP assignment table");
		this.initializeElasticIpAssignmentListTable();
		GWT.log("elastic IP assignment table initialized");
	    elasticIpAssignmentListPager.setDisplay(elasticIpAssignmentListTable);
	}

	private Widget initializeElasticIpAssignmentListTable() {
		GWT.log("initializing ELASTIC_IP_ASSIGNMENT list table columns...");

		elasticIpAssignmentListTable.setTableLayoutFixed(false);
		elasticIpAssignmentListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		elasticIpAssignmentListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<ElasticIpAssignmentPojo>();
		dataProvider.addDataDisplay(elasticIpAssignmentListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.elasticIpAssignmentList);
		
		selectionModel = 
	    	new SingleSelectionModel<ElasticIpAssignmentPojo>(ElasticIpAssignmentPojo.KEY_PROVIDER);
		elasticIpAssignmentListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		ElasticIpAssignmentPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected ElasticIpAssignment is: " + m.getAssignmentId());
	    	}
	    });

	    ListHandler<ElasticIpAssignmentPojo> sortHandler = 
	    	new ListHandler<ElasticIpAssignmentPojo>(dataProvider.getList());
	    elasticIpAssignmentListTable.addColumnSortHandler(sortHandler);

	    if (elasticIpAssignmentListTable.getColumnCount() == 0) {
		    initelasticIpAssignmentListTableColumns(sortHandler);
	    }
		
		return elasticIpAssignmentListTable;
	}

	private void initelasticIpAssignmentListTableColumns(
			ListHandler<ElasticIpAssignmentPojo> sortHandler) {

		GWT.log("initializing ELASTIC_IP_ASSIGNMENT list table columns...");

	    Column<ElasticIpAssignmentPojo, Boolean> checkColumn = new Column<ElasticIpAssignmentPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(ElasticIpAssignmentPojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    elasticIpAssignmentListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    elasticIpAssignmentListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<ElasticIpAssignmentPojo, String> vpcColumn = 
			new Column<ElasticIpAssignmentPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpAssignmentPojo object) {
				return object.getElasticIp().getElasticIpAddress();
			}
		};
		vpcColumn.setSortable(true);
		sortHandler.setComparator(vpcColumn, new Comparator<ElasticIpAssignmentPojo>() {
			public int compare(ElasticIpAssignmentPojo o1, ElasticIpAssignmentPojo o2) {
				return o1.getElasticIp().getElasticIpAddress().compareTo(o2.getElasticIp().getElasticIpAddress());
			}
		});
		elasticIpAssignmentListTable.addColumn(vpcColumn, "Elastic IP");

		Column<ElasticIpAssignmentPojo, String> associatedIpColumn = 
				new Column<ElasticIpAssignmentPojo, String> (new TextCell()) {
				
				@Override
				public String getValue(ElasticIpAssignmentPojo object) {
					// TODO: this will come from the assignment object and not the ElasticIp object
					return object.getElasticIp().getAssociatedIpAddress();
				}
			};
			associatedIpColumn.setSortable(true);
			sortHandler.setComparator(associatedIpColumn, new Comparator<ElasticIpAssignmentPojo>() {
				public int compare(ElasticIpAssignmentPojo o1, ElasticIpAssignmentPojo o2) {
					return o1.getElasticIp().getAssociatedIpAddress().compareTo(o2.getElasticIp().getAssociatedIpAddress());
				}
			});
			elasticIpAssignmentListTable.addColumn(associatedIpColumn, "Associated IP");

		Column<ElasticIpAssignmentPojo, String> purposeColumn = 
				new Column<ElasticIpAssignmentPojo, String>(new TextCell()) {

					@Override
					public String getValue(ElasticIpAssignmentPojo object) {
						return object.getPurpose();
					}
				};
		purposeColumn.setSortable(true);
		sortHandler.setComparator(purposeColumn, new Comparator<ElasticIpAssignmentPojo>() {
			public int compare(ElasticIpAssignmentPojo o1, ElasticIpAssignmentPojo o2) {
				return o1.getPurpose().compareTo(o2.getPurpose());
			}
		});
		elasticIpAssignmentListTable.addColumn(purposeColumn, "Purpose");
		
		Column<ElasticIpAssignmentPojo, String> descriptionColumn = 
				new Column<ElasticIpAssignmentPojo, String>(new TextCell()) {

					@Override
					public String getValue(ElasticIpAssignmentPojo object) {
						return object.getDescription();
					}
				};
		descriptionColumn.setSortable(true);
		sortHandler.setComparator(descriptionColumn, new Comparator<ElasticIpAssignmentPojo>() {
			public int compare(ElasticIpAssignmentPojo o1, ElasticIpAssignmentPojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
		elasticIpAssignmentListTable.addColumn(descriptionColumn, "Description");
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
	public void removeElasticIpAssignmentFromView(ElasticIpAssignmentPojo summary) {
		dataProvider.getList().remove(summary);
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
