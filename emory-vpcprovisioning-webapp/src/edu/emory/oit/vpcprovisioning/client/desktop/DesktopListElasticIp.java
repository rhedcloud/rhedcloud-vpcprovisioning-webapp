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
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListElasticIp extends ViewImplBase implements ListElasticIpView {
	Presenter presenter;
	private ListDataProvider<ElasticIpPojo> dataProvider = new ListDataProvider<ElasticIpPojo>();
	private SingleSelectionModel<ElasticIpPojo> selectionModel;
	List<ElasticIpPojo> elasticIpList = new java.util.ArrayList<ElasticIpPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager elasticIpListPager;
	@UiField Button allocateAddressButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<ElasticIpPojo> elasticIpListTable = new CellTable<ElasticIpPojo>();
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListElasticIpUiBinder uiBinder = GWT.create(DesktopListElasticIpUiBinder.class);

	interface DesktopListElasticIpUiBinder extends UiBinder<Widget, DesktopListElasticIp> {
	}

	public DesktopListElasticIp() {
		initWidget(uiBinder.createAndBindUi(this));
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
		releaseAddressesAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ElasticIpPojo m = selectionModel.getSelectedObject();
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
		disassociateAddressesAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ElasticIpPojo m = selectionModel.getSelectedObject();
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
	public void applyEmoryAWSAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// TODO Auto-generated method stub
		
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
	public void setElasticIps(List<ElasticIpPojo> elasticIps) {
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
		dataProvider = new ListDataProvider<ElasticIpPojo>();
		dataProvider.addDataDisplay(elasticIpListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.elasticIpList);
		
		selectionModel = 
	    	new SingleSelectionModel<ElasticIpPojo>(ElasticIpPojo.KEY_PROVIDER);
		elasticIpListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		ElasticIpPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected elsticIp is: " + m.getClass().getName());
	    	}
	    });

	    ListHandler<ElasticIpPojo> sortHandler = 
	    	new ListHandler<ElasticIpPojo>(dataProvider.getList());
	    elasticIpListTable.addColumnSortHandler(sortHandler);

	    if (elasticIpListTable.getColumnCount() == 0) {
		    initElasticIpListTableColumns(sortHandler);
	    }
		
		return elasticIpListTable;
	}
	private void initElasticIpListTableColumns(ListHandler<ElasticIpPojo> sortHandler) {
		GWT.log("initializing VPC list table columns...");
		// Account id column
		Column<ElasticIpPojo, String> acctIdColumn = 
			new Column<ElasticIpPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpPojo object) {
				return object.getElasticIp();
			}
		};
		acctIdColumn.setSortable(true);
		sortHandler.setComparator(acctIdColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getElasticIp().compareTo(o2.getElasticIp());
			}
		});
		elasticIpListTable.addColumn(acctIdColumn, "Elastic IP");

		// VPC id column
		Column<ElasticIpPojo, String> vpcIdColumn = 
			new Column<ElasticIpPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpPojo object) {
				return object.getAllocationId();
			}
		};
		vpcIdColumn.setSortable(true);
		sortHandler.setComparator(vpcIdColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getAllocationId().compareTo(o2.getAllocationId());
			}
		});
		elasticIpListTable.addColumn(vpcIdColumn, "Allocation ID");
		
		// type
		Column<ElasticIpPojo, String> vpcTypeColumn = 
			new Column<ElasticIpPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpPojo object) {
				return object.getInstance();
			}
		};
		vpcTypeColumn.setSortable(true);
		sortHandler.setComparator(vpcTypeColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getInstance().compareTo(o2.getInstance());
			}
		});
		elasticIpListTable.addColumn(vpcTypeColumn, "Instance");
		
		// compliance class
		Column<ElasticIpPojo, String> complianceClassColumn = 
			new Column<ElasticIpPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpPojo object) {
				return object.getPrivateIpAddress();
			}
		};
		complianceClassColumn.setSortable(true);
		sortHandler.setComparator(complianceClassColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getPrivateIpAddress().compareTo(o2.getPrivateIpAddress());
			}
		});
		elasticIpListTable.addColumn(complianceClassColumn, "Private IP");
		
		// create user
		Column<ElasticIpPojo, String> createUserColumn = 
				new Column<ElasticIpPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpPojo object) {
				return object.getCreateUser();
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getCreateUser().compareTo(o2.getCreateUser());
			}
		});
		elasticIpListTable.addColumn(createUserColumn, "Create User");
		
		// create time
		Column<ElasticIpPojo, String> createTimeColumn = 
				new Column<ElasticIpPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpPojo object) {
				return dateFormat.format(object.getCreateTime());
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		elasticIpListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<ElasticIpPojo, String> lastUpdateUserColumn = 
				new Column<ElasticIpPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpPojo object) {
				return object.getUpdateUser();
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getUpdateUser().compareTo(o2.getUpdateUser());
			}
		});
		elasticIpListTable.addColumn(lastUpdateUserColumn, "Update User");
		
		// update time
		Column<ElasticIpPojo, String> updateTimeColumn = 
				new Column<ElasticIpPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpPojo object) {
				return dateFormat.format(object.getUpdateTime());
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<ElasticIpPojo>() {
			public int compare(ElasticIpPojo o1, ElasticIpPojo o2) {
				return o1.getUpdateTime().compareTo(o2.getUpdateTime());
			}
		});
		elasticIpListTable.addColumn(updateTimeColumn, "Update Time");

		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			GWT.log(userLoggedIn.getEppn() + " is an admin");
			// delete row column
			Column<ElasticIpPojo, String> deleteRowColumn = new Column<ElasticIpPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(ElasticIpPojo object) {
					return "Delete";
				}
			};
			deleteRowColumn.setCellStyleNames("glowing-border");
			elasticIpListTable.addColumn(deleteRowColumn, "");
			elasticIpListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
			deleteRowColumn
			.setFieldUpdater(new FieldUpdater<ElasticIpPojo, String>() {
				@Override
				public void update(int index, final ElasticIpPojo vpc,
						String value) {
	
//					presenter.deleteVpc(vpc);
				}
			});
		}

		// edit row column
		Column<ElasticIpPojo, String> editRowColumn = new Column<ElasticIpPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(ElasticIpPojo object) {
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
		editRowColumn.setCellStyleNames("glowing-border");
		elasticIpListTable.addColumn(editRowColumn, "");
		elasticIpListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
		editRowColumn.setFieldUpdater(new FieldUpdater<ElasticIpPojo, String>() {
			@Override
			public void update(int index, final ElasticIpPojo vpc,
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

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void removeElasticIpFromView(ElasticIpPojo elasticIp) {
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
}
