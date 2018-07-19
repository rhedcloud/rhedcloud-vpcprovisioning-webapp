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
import com.google.gwt.user.cellview.client.RowStyles;
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
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListService extends ViewImplBase implements ListServiceView {
	Presenter presenter;
	private ListDataProvider<AWSServicePojo> dataProvider = new ListDataProvider<AWSServicePojo>();
	private SingleSelectionModel<AWSServicePojo> selectionModel;
	List<AWSServicePojo> serviceList = new java.util.ArrayList<AWSServicePojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);
	boolean skeletonOnly=true;

	private static DesktopListServiceUiBinder uiBinder = GWT.create(DesktopListServiceUiBinder.class);

	interface DesktopListServiceUiBinder extends UiBinder<Widget, DesktopListService> {
	}

	public DesktopListService() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.log("List services desktop view implementation...");
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	/*** FIELDS ***/
	@UiField SimplePager serviceListPager;
	@UiField(provided=true) CellTable<AWSServicePojo> serviceListTable = new CellTable<AWSServicePojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
//	@UiField Button closeOtherFeaturesButton;
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

//	@UiHandler ("closeOtherFeaturesButton")
//	void closeOtherFeaturesButtonClicked(ClickEvent e) {
//		presenter.getClientFactory().getShell().hideOtherFeaturesPanel();
//		presenter.getClientFactory().getShell().showMainTabPanel();
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME);
//	}
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
		createServiceButton.setEnabled(false);
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
		skeletonOnly=true;
		serviceListTable.setTableLayoutFixed(false);
		serviceListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		serviceListTable.setVisibleRange(0, 10);

		// create dataprovider
		dataProvider = new ListDataProvider<AWSServicePojo>();
		dataProvider.addDataDisplay(serviceListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.serviceList);

		selectionModel = 
				new SingleSelectionModel<AWSServicePojo>(AWSServicePojo.KEY_PROVIDER);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				AWSServicePojo m = selectionModel.getSelectedObject();
				GWT.log("Selected service is: " + m.getServiceId());

				// set RowStyles
				serviceListTable.setRowStyles(new RowStyles<AWSServicePojo>() {
					@Override
					public String getStyleNames(AWSServicePojo row, int rowIndex) {
						if (row.isSkeleton()) {
							GWT.log("skeleton service: " + row.getAwsServiceName());
							return "skeletonService";
						}
						else {
							GWT.log("normal row: " + row.getAwsServiceName());
							return null;
						}
					}
				});
			}
		});
		serviceListTable.setSelectionModel(selectionModel);

		ListHandler<AWSServicePojo> sortHandler = 
				new ListHandler<AWSServicePojo>(dataProvider.getList());
		serviceListTable.addColumnSortHandler(sortHandler);

		if (serviceListTable.getColumnCount() == 0) {
			initServiceListTableColumns(sortHandler);
		}
		
		// set RowStyles
		serviceListTable.setRowStyles(new RowStyles<AWSServicePojo>() {
			@Override
			public String getStyleNames(AWSServicePojo row, int rowIndex) {
				if (row.isSkeleton()) {
					GWT.log("skeleton service: " + row.getAwsServiceName());
					return "skeletonService";
				}
				else {
					GWT.log("normal row: " + row.getAwsServiceName());
					return null;
				}
			}
		});

		return serviceListTable;
	}

	private void initServiceListTableColumns(ListHandler<AWSServicePojo> sortHandler) {
		GWT.log("initializing Service list table columns...");

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

		// aws category column
		Column<AWSServicePojo, String> awsCategoryColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				if (object.getCategories().size() > 0) {
					return "aws categories";
				}
				else {
					return "None Yet";
				}
				// TODO: make a list of categories;
				// return object.getCategories().get(0);
			}
			
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			    	GWT.log("skeleton service..." + object.getAwsServiceName());
//			        return "skeletonService";
//			    }
//			    else {
//			    	GWT.log("non-skeleton service..." + object.getAwsServiceName());
//			    	return "tableData";
//			    }
//			}
		};
		serviceListTable.addColumn(awsCategoryColumn, "AWS Category");

		// AWS Code column
		Column<AWSServicePojo, String> awsCodeColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getAwsServiceCode();
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		awsCodeColumn.setSortable(true);
		sortHandler.setComparator(awsCodeColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getAwsServiceCode().compareTo(o2.getAwsServiceCode());
			}
		});
		serviceListTable.addColumn(awsCodeColumn, "AWS Code");

		// aws name
		Column<AWSServicePojo, String> awsNameColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getAwsServiceName();
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		awsNameColumn.setSortable(true);
		sortHandler.setComparator(awsNameColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getAwsServiceName().compareTo(o2.getAwsServiceName());
			}
		});
		serviceListTable.addColumn(awsNameColumn, "AWS Name");

		// status
		Column<AWSServicePojo, String> statusColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getStatus();
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(statusColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getStatus().compareTo(o2.getStatus());
			}
		});
		serviceListTable.addColumn(statusColumn, "Status");

		// description
		Column<AWSServicePojo, String> descColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getDescription();
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		descColumn.setSortable(true);
		sortHandler.setComparator(descColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
		serviceListTable.addColumn(descColumn, "Description");

		// aws hipaa eligible
		Column<AWSServicePojo, String> awsHipaaEligibleColumn = new Column<AWSServicePojo, String>(
				new TextCell()) {
			@Override
			public String getValue(AWSServicePojo object) {
				return (object.isAwsHipaaEligible() ? "Yes" : "No");
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		serviceListTable.addColumn(awsHipaaEligibleColumn, "AWS HIPAA Eligible");
		serviceListTable.setColumnWidth(awsHipaaEligibleColumn, 40, Unit.PX);

		// emory category column
		Column<AWSServicePojo, String> emoryCategoryColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				if (object.getConsoleCategories().size() > 0) {
					return "console categories";
				}
				else {
					return "None Yet";
				}
				// TODO: make a list of categories;
				// return object.getCategories().get(0);
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		serviceListTable.addColumn(emoryCategoryColumn, "Emory Category");

		// emory hipaa eligible
		Column<AWSServicePojo, String> emoryHipaaEligibleColumn = new Column<AWSServicePojo, String>(
				new TextCell()) {
			@Override
			public String getValue(AWSServicePojo object) {
				return (object.isEmoryHipaaEligible() ? "Yes" : "No");
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		serviceListTable.addColumn(emoryHipaaEligibleColumn, "Emory HIPAA Eligible");
		serviceListTable.setColumnWidth(emoryHipaaEligibleColumn, 40, Unit.PX);

		// create user
		Column<AWSServicePojo, String> createUserColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getCreateUser();
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
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
				if (object.getCreateTime() != null) {
					return dateFormat.format(object.getCreateTime());
				}
				else {
					return "Unknown";
				}
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				if (o1.getCreateTime() != null) {
					return o1.getCreateTime().compareTo(o2.getCreateTime());
				}
				else {
					return 0;
				}
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
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
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
				if (object.getUpdateTime() != null) {
					return dateFormat.format(object.getUpdateTime());
				}
				else {
					return "Uknown";
				}
			}
//			@Override
//		    public String getCellStyleNames(Context context, AWSServicePojo object) {
//			    if (object.isSkeleton()) {
//			        return "skeletonService";
//			    }
//			    else {
//			    	return "tableData";
//			    }
//			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				if (o1.getUpdateTime() != null) {
					return o1.getUpdateTime().compareTo(o2.getUpdateTime());
				}
				else {
					return 0;
				}
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
		createServiceButton.setEnabled(true);
		actionsButton.setEnabled(true);
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
