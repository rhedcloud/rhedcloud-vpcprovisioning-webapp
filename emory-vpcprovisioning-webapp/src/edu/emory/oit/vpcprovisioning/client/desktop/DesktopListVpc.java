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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
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

	/*** FIELDS ***/
	@UiField SimplePager vpcListPager;
//	@UiField Button addVpcButton;
	@UiField Button registerVpcButton;
	@UiField(provided=true) CellTable<VpcPojo> vpcListTable = new CellTable<VpcPojo>();
//	@UiField VerticalPanel vpcListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListVpcUiBinder uiBinder = GWT.create(DesktopListVpcUiBinder.class);

	interface DesktopListVpcUiBinder extends UiBinder<Widget, DesktopListVpc> {
	}

	public DesktopListVpc() {
		initWidget(uiBinder.createAndBindUi(this));
		
//		addVpcButton.addDomHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				GWT.log("Should go to maintain vpc here...");
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPC);
//			}
//		}, ClickEvent.getType());

		registerVpcButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Should go to register vpc here...");
				ActionEvent.fire(presenter.getEventBus(), ActionNames.REGISTER_VPC);
			}
		}, ClickEvent.getType());
	}

	@Override
	public void showMessageToUser(String message) {
		Window.alert(message);
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
	}
	private Widget initializeVpcListTable() {
		GWT.log("initializing VPC list table...");
		vpcListTable.setTableLayoutFixed(false);
		vpcListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		vpcListTable.setVisibleRange(0, 5);
		
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
		
		// compliance class
		Column<VpcPojo, String> complianceClassColumn = 
			new Column<VpcPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(VpcPojo object) {
				return object.getComplianceClass();
			}
		};
		complianceClassColumn.setSortable(true);
		sortHandler.setComparator(complianceClassColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getComplianceClass().compareTo(o2.getComplianceClass());
			}
		});
		vpcListTable.addColumn(complianceClassColumn, "Compliance Class");
		
		// create user
		Column<VpcPojo, String> createUserColumn = 
				new Column<VpcPojo, String> (new TextCell()) {

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
		vpcListTable.addColumn(createUserColumn, "Create User");
		
		// create time
		Column<VpcPojo, String> createTimeColumn = 
				new Column<VpcPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcPojo object) {
				return dateFormat.format(object.getCreateTime());
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		vpcListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<VpcPojo, String> lastUpdateUserColumn = 
				new Column<VpcPojo, String> (new TextCell()) {

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
		vpcListTable.addColumn(lastUpdateUserColumn, "Update User");
		
		// update time
		Column<VpcPojo, String> updateTimeColumn = 
				new Column<VpcPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcPojo object) {
				return dateFormat.format(object.getUpdateTime());
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<VpcPojo>() {
			public int compare(VpcPojo o1, VpcPojo o2) {
				return o1.getUpdateTime().compareTo(o2.getUpdateTime());
			}
		});
		vpcListTable.addColumn(updateTimeColumn, "Update Time");

		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			GWT.log(userLoggedIn.getEppn() + " is an admin");
			// delete row column
			Column<VpcPojo, String> deleteRowColumn = new Column<VpcPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(VpcPojo object) {
					return "Delete";
				}
			};
			deleteRowColumn.setCellStyleNames("glowing-border");
			vpcListTable.addColumn(deleteRowColumn, "");
			vpcListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
			deleteRowColumn
			.setFieldUpdater(new FieldUpdater<VpcPojo, String>() {
				@Override
				public void update(int index, final VpcPojo vpc,
						String value) {
	
					presenter.deleteVpc(vpc);
				}
			});
		}

		// edit row column
		Column<VpcPojo, String> editRowColumn = new Column<VpcPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(VpcPojo object) {
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
		vpcListTable.addColumn(editRowColumn, "");
		vpcListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
		editRowColumn.setFieldUpdater(new FieldUpdater<VpcPojo, String>() {
			@Override
			public void update(int index, final VpcPojo vpc,
					String value) {
				
				// fire MAINTAIN_VPC event passing the vpc to be maintained
				GWT.log("[DesktopListVpc] editing VPC: " + vpc.getVpcId());
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_VPC, vpc);
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
	public void removeVpcFromView(VpcPojo vpc) {
		dataProvider.getList().remove(vpc);
	}

	@Override
	public Widget getStatusMessageSource() {
		return vpcListTable;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// enable add button
//		addVpcButton.setEnabled(true);
		registerVpcButton.setEnabled(true);
		// enable Delete button in table (handled in init...ListTableColumns)
		// change text of button to Edit (handled in init...ListTableColumns)
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// disable add button
//		addVpcButton.setEnabled(false);
		registerVpcButton.setEnabled(false);
		// disable Delete button in table (handled in init...ListTableColumns)
		// change text of button to Edit (handled in init...ListTableColumns)
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}
}
