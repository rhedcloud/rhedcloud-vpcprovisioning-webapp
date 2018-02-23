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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrView;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListCidr extends ViewImplBase implements ListCidrView {
	Presenter presenter;
	private ListDataProvider<CidrPojo> cidrDataProvider = new ListDataProvider<CidrPojo>();
	private SingleSelectionModel<CidrPojo> cidrSelectionModel;
	List<CidrPojo> cidrList = new java.util.ArrayList<CidrPojo>();
	UserAccountPojo userLoggedIn;

	/*** FIELDS ***/
	@UiField SimplePager cidrListPager;
	@UiField Button addCidrButton;
	@UiField(provided=true) CellTable<CidrPojo> cidrListTable = new CellTable<CidrPojo>();
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
		cidrListTable.setVisibleRangeAndClearData(cidrListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setCidrs(List<CidrPojo> cidrs) {
		this.cidrList = cidrs;
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
		cidrDataProvider = new ListDataProvider<CidrPojo>();
		cidrDataProvider.addDataDisplay(cidrListTable);
		cidrDataProvider.getList().clear();
		cidrDataProvider.getList().addAll(this.cidrList);
		
		cidrSelectionModel = 
	    	new SingleSelectionModel<CidrPojo>(CidrPojo.KEY_PROVIDER);
		cidrListTable.setSelectionModel(cidrSelectionModel);
	    
	    cidrSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		CidrPojo m = cidrSelectionModel.getSelectedObject();
	    		GWT.log("Selected cidr is: " + m.getCidrId());
	    	}
	    });

	    ListHandler<CidrPojo> sortHandler = 
	    	new ListHandler<CidrPojo>(cidrDataProvider.getList());
	    cidrListTable.addColumnSortHandler(sortHandler);

	    if (cidrListTable.getColumnCount() == 0) {
		    initCidrListTableColumns(sortHandler);
	    }
		
		return cidrListTable;
	}
	private void initCidrListTableColumns(ListHandler<CidrPojo> sortHandler) {
		GWT.log("initializing CIDR list table columns...");
		// CIDR network column
		Column<CidrPojo, String> networkColumn = 
			new Column<CidrPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(CidrPojo object) {
				return object.getNetwork();
			}
		};
		networkColumn.setSortable(true);
		sortHandler.setComparator(networkColumn, new Comparator<CidrPojo>() {
			public int compare(CidrPojo o1, CidrPojo o2) {
				return o1.getNetwork().compareTo(o2.getNetwork());
			}
		});
		cidrListTable.addColumn(networkColumn, "Network");
		
		// cidr bits
		Column<CidrPojo, String> bitsColumn = 
			new Column<CidrPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(CidrPojo object) {
				return object.getBits();
			}
		};
		bitsColumn.setSortable(true);
		sortHandler.setComparator(bitsColumn, new Comparator<CidrPojo>() {
			public int compare(CidrPojo o1, CidrPojo o2) {
				return o1.getBits().compareTo(o2.getBits());
			}
		});
		cidrListTable.addColumn(bitsColumn, "Bits");

		// create user
		Column<CidrPojo, String> createUserColumn = 
				new Column<CidrPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrPojo object) {
				return object.getCreateUser();
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<CidrPojo>() {
			public int compare(CidrPojo o1, CidrPojo o2) {
				return o1.getCreateUser().compareTo(o2.getCreateUser());
			}
		});
		cidrListTable.addColumn(createUserColumn, "Create User");

		// create time
		Column<CidrPojo, String> createTimeColumn = 
				new Column<CidrPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrPojo object) {
				return dateFormat.format(object.getCreateTime());
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<CidrPojo>() {
			public int compare(CidrPojo o1, CidrPojo o2) {
				return o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		cidrListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<CidrPojo, String> lastUpdateUserColumn = 
				new Column<CidrPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrPojo object) {
				return object.getUpdateUser();
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<CidrPojo>() {
			public int compare(CidrPojo o1, CidrPojo o2) {
				return o1.getUpdateUser().compareTo(o2.getUpdateUser());
			}
		});
		cidrListTable.addColumn(lastUpdateUserColumn, "Update User");

		// update time
		Column<CidrPojo, String> updateTimeColumn = 
				new Column<CidrPojo, String> (new TextCell()) {

			@Override
			public String getValue(CidrPojo object) {
				return dateFormat.format(object.getUpdateTime());
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<CidrPojo>() {
			public int compare(CidrPojo o1, CidrPojo o2) {
				return o1.getUpdateTime().compareTo(o2.getUpdateTime());
			}
		});
		cidrListTable.addColumn(updateTimeColumn, "Update Time");

		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			GWT.log(userLoggedIn.getEppn() + " is an admin");
			// delete row column
			Column<CidrPojo, String> deleteRowColumn = new Column<CidrPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(CidrPojo object) {
					return "Delete";
				}
			};
			deleteRowColumn.setCellStyleNames("glowing-border");
			cidrListTable.addColumn(deleteRowColumn, "");
			cidrListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
			deleteRowColumn
			.setFieldUpdater(new FieldUpdater<CidrPojo, String>() {
				@Override
				public void update(int index, final CidrPojo cidr,
						String value) {
					
					// Called when the user clicks the button
					// confirm action
					presenter.deleteCidr(cidr);
				}
			});
		}

		// edit row column
		Column<CidrPojo, String> editRowColumn = new Column<CidrPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(CidrPojo object) {
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
		editRowColumn.setFieldUpdater(new FieldUpdater<CidrPojo, String>() {
			@Override
			public void update(int index, final CidrPojo cidr,
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
	public void removeCidrFromView(CidrPojo cidr) {
		cidrDataProvider.getList().remove(cidr);
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

}
