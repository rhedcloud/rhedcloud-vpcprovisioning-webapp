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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListService extends ViewImplBase implements ListServiceView {
	Presenter presenter;
	private ListDataProvider<AWSServicePojo> dataProvider = new ListDataProvider<AWSServicePojo>();
	private SingleSelectionModel<AWSServicePojo> selectionModel;
	List<AWSServicePojo> serviceList = new java.util.ArrayList<AWSServicePojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	private static DesktopListServiceUiBinder uiBinder = GWT.create(DesktopListServiceUiBinder.class);

	interface DesktopListServiceUiBinder extends UiBinder<Widget, DesktopListService> {
	}

	public DesktopListService() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.log("List services desktop view implementation...");
	}

	/*** FIELDS ***/
	@UiField SimplePager serviceListPager;
	@UiField(provided=true) CellTable<AWSServicePojo> serviceListTable = new CellTable<AWSServicePojo>();
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button closeOtherFeaturesButton;
	@UiField Button createServiceButton;
	@UiField Button actionsButton;

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
	    Grid grid = new Grid(2, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
		Anchor editAnchor = new Anchor("Edit Service");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Service");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AWSServicePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SERVICE, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, editAnchor);

		Anchor deleteAnchor = new Anchor("Delete Service");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected Service");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AWSServicePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					presenter.deleteService(m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@UiHandler ("closeOtherFeaturesButton")
	void closeOtherFeaturesButtonClicked(ClickEvent e) {
		presenter.getClientFactory().getShell().hideOtherFeaturesPanel();
		presenter.getClientFactory().getShell().showMainTabPanel();
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME);
	}
	@UiHandler ("createServiceButton")
	void createSserviceClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_SERVICE);
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		return serviceListTable;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		createServiceButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		createServiceButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void clearList() {
		serviceListTable.setVisibleRangeAndClearData(serviceListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
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
	public void setServices(List<AWSServicePojo> services) {
		this.serviceList = services;
		this.initializeServiceListTable();
		serviceListPager.setDisplay(serviceListTable);
	}

	@Override
	public void removeServiceFromView(AWSServicePojo service) {
		dataProvider.getList().remove(service);
	}

	private Widget initializeServiceListTable() {
		GWT.log("initializing service list table...");
		serviceListTable.setTableLayoutFixed(false);
		serviceListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		serviceListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<AWSServicePojo>();
		dataProvider.addDataDisplay(serviceListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.serviceList);
		
		selectionModel = 
	    	new SingleSelectionModel<AWSServicePojo>(AWSServicePojo.KEY_PROVIDER);
		serviceListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		AWSServicePojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected service is: " + m.getServiceId());
	    	}
	    });

	    ListHandler<AWSServicePojo> sortHandler = 
	    	new ListHandler<AWSServicePojo>(dataProvider.getList());
	    serviceListTable.addColumnSortHandler(sortHandler);

	    if (serviceListTable.getColumnCount() == 0) {
		    initServiceListTableColumns(sortHandler);
	    }
		
		return serviceListTable;
	}
	private void initServiceListTableColumns(ListHandler<AWSServicePojo> sortHandler) {
		GWT.log("initializing VPC list table columns...");
		
	    Column<AWSServicePojo, Boolean> checkColumn = new Column<AWSServicePojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(AWSServicePojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    serviceListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    serviceListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Account id column
		Column<AWSServicePojo, String> acctIdColumn = 
			new Column<AWSServicePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AWSServicePojo object) {
				return object.getConsoleCategories().get(0);
			}
		};
		acctIdColumn.setSortable(true);
		sortHandler.setComparator(acctIdColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getConsoleCategories().get(0).compareTo(o2.getConsoleCategories().get(0));
			}
		});
		serviceListTable.addColumn(acctIdColumn, "Category");

		// VPC id column
		Column<AWSServicePojo, String> vpcIdColumn = 
			new Column<AWSServicePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AWSServicePojo object) {
				return object.getCode();
			}
		};
		vpcIdColumn.setSortable(true);
		sortHandler.setComparator(vpcIdColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});
		serviceListTable.addColumn(vpcIdColumn, "Code");
		
		// type
		Column<AWSServicePojo, String> vpcTypeColumn = 
			new Column<AWSServicePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AWSServicePojo object) {
				return object.getName();
			}
		};
		vpcTypeColumn.setSortable(true);
		sortHandler.setComparator(vpcTypeColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		serviceListTable.addColumn(vpcTypeColumn, "Name");
		
		// compliance class
		Column<AWSServicePojo, String> complianceClassColumn = 
			new Column<AWSServicePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AWSServicePojo object) {
				return object.getStatus();
			}
		};
		complianceClassColumn.setSortable(true);
		sortHandler.setComparator(complianceClassColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getStatus().compareTo(o2.getStatus());
			}
		});
		serviceListTable.addColumn(complianceClassColumn, "Status");
		
		Column<AWSServicePojo, String> descColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {
				
				@Override
				public String getValue(AWSServicePojo object) {
					return object.getDescription();
				}
			};
			descColumn.setSortable(true);
			sortHandler.setComparator(descColumn, new Comparator<AWSServicePojo>() {
				public int compare(AWSServicePojo o1, AWSServicePojo o2) {
					return o1.getDescription().compareTo(o2.getDescription());
				}
			});
			serviceListTable.addColumn(descColumn, "Description");

		// create user
		Column<AWSServicePojo, String> createUserColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getCreateUser();
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getCreateUser().compareTo(o2.getCreateUser());
			}
		});
		serviceListTable.addColumn(createUserColumn, "Create User");
		
		// create time
		Column<AWSServicePojo, String> createTimeColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return dateFormat.format(object.getCreateTime());
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		serviceListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<AWSServicePojo, String> lastUpdateUserColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getUpdateUser();
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getUpdateUser().compareTo(o2.getUpdateUser());
			}
		});
		serviceListTable.addColumn(lastUpdateUserColumn, "Update User");
		
		// update time
		Column<AWSServicePojo, String> updateTimeColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return dateFormat.format(object.getUpdateTime());
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getUpdateTime().compareTo(o2.getUpdateTime());
			}
		});
		serviceListTable.addColumn(updateTimeColumn, "Update Time");
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
