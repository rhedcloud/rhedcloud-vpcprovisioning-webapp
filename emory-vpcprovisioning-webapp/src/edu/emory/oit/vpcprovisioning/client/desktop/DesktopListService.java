package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
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
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListService extends ViewImplBase implements ListServiceView {
	Presenter presenter;
	private ListDataProvider<AWSServicePojo> dataProvider = new ListDataProvider<AWSServicePojo>();
	private SingleSelectionModel<AWSServicePojo> selectionModel;
	List<AWSServicePojo> serviceList = new java.util.ArrayList<AWSServicePojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);
	boolean skeletonOnly=true;
	List<String> filterTypeItems;

	private static DesktopListServiceUiBinder uiBinder = GWT.create(DesktopListServiceUiBinder.class);

	interface DesktopListServiceUiBinder extends UiBinder<Widget, DesktopListService> {
	}

	public DesktopListService() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	/*** FIELDS ***/
	@UiField(provided=true) SimplePager serviceListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) CellTable<AWSServicePojo> serviceListTable = new CellTable<AWSServicePojo>(15, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button createServiceButton;
	@UiField Button actionsButton;
	@UiField PushButton refreshButton;
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField ListBox filterTypesLB;

	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		filterTB.setText("");
		presenter.clearFilter();
	}
	@UiHandler("filterButton") 
	void filterButtonClicked(ClickEvent e) {
		String filterType = filterTypesLB.getSelectedValue();
		String filterValue = filterTB.getText();
		
		if ((filterType != null && filterType.length() > 0) &&
			 (filterValue != null && filterValue.length() > 0)) {
			if (filterType.equalsIgnoreCase(Constants.SVC_FILTER_AWS_HIPAA_STATUS)) {
				presenter.filterByAwsHipaaStatus(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.SVC_FILTER_AWS_NAME)) {
				presenter.filterByAwsServiceName(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.SVC_FILTER_AWS_STATUS)) {
				presenter.filterByAwsStatus(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.SVC_FILTER_CONSOLE_CATEGORY)) {
				presenter.filterByConsoleCategories(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.SVC_FILTER_SITE_HIPAA_STATUS)) {
				presenter.filterBySiteHipaaStatus(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.SVC_FILTER_SITE_STATUS)) {
				presenter.filterBySiteStatus(filterValue);
			}
			else {
				// invalid filter type...but how?
			}
		}
		else {
			this.showMessageToUser("Please enter a Filter Value AND select a Filter Type");
		}
	}
	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		presenter.refreshList(userLoggedIn);
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid;
		if (userLoggedIn.isCentralAdmin()) {
			grid = new Grid(2, 1);
		}
		else {
			grid = new Grid(1,1);
		}

		grid.setCellSpacing(8);
		actionsPopup.add(grid);

		Anchor editAnchor = new Anchor("View/Maintain Service");
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

		if (userLoggedIn.isCentralAdmin()) {
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
		}

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
		return refreshButton;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		createServiceButton.setEnabled(false);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		createServiceButton.setEnabled(false);
		actionsButton.setEnabled(true);
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
		serviceListTable.setVisibleRange(0, 15);

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
		serviceListTable.redraw();

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
//		Column<AWSServicePojo, SafeHtml> awsCategoryColumn = 
//				new Column<AWSServicePojo, SafeHtml> (new SafeHtmlCell()) {
//
//			@Override
//			public SafeHtml getValue(AWSServicePojo object) {
//				StringBuffer categories = new StringBuffer();
//				int cntr = 1;
//				if (object.getAwsCategories().size() > 0) {
//					for (String category : object.getAwsCategories()) {
//						if (cntr == object.getAwsCategories().size()) {
//							categories.append(category);
//							
//						}
//						else {
//							cntr++;
//							categories.append(category + "</br>");
//						}
//					}
//					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(categories.toString());
//				}
//				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("No categories yet");
//			}
//		};
//		serviceListTable.addColumn(awsCategoryColumn, "AWS Category(ies)");

		// AWS Code column
		Column<AWSServicePojo, String> awsCodeColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getAwsServiceCode();
			}
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
				return object.getAwsStatus();
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(statusColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getAwsStatus().compareTo(o2.getAwsStatus());
			}
		});
		serviceListTable.addColumn(statusColumn, "AWS Status");

		// description
		Column<AWSServicePojo, String> descColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				String desc = object.getDescription();
				if (desc != null && desc.length() > 50) {
					return desc.substring(0, 49) + "...";
				}
				else {
					return desc;
				}
			}
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
		};
		serviceListTable.addColumn(awsHipaaEligibleColumn, "AWS HIPAA Eligible");
		serviceListTable.setColumnWidth(awsHipaaEligibleColumn, 40, Unit.PX);

		// emory category column
		Column<AWSServicePojo, SafeHtml> emoryCategoryColumn = 
				new Column<AWSServicePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(AWSServicePojo object) {
				StringBuffer categories = new StringBuffer();
				int cntr = 1;
				if (object.getConsoleCategories().size() > 0) {
					for (String category : object.getConsoleCategories()) {
						if (cntr == object.getConsoleCategories().size()) {
							categories.append(category);
							
						}
						else {
							cntr++;
							categories.append(category + "</br>");
						}
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(categories.toString());
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("No console categories yet");
			}
		};
		serviceListTable.addColumn(emoryCategoryColumn, "Emory Category(ies)");
		
		// emory status
		Column<AWSServicePojo, String> emoryStatusColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getSiteStatus();
			}
		};
		emoryStatusColumn.setSortable(true);
		sortHandler.setComparator(emoryStatusColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getSiteStatus().compareTo(o2.getSiteStatus());
			}
		});
		serviceListTable.addColumn(emoryStatusColumn, "Emory Status");

		// emory hipaa eligible
		Column<AWSServicePojo, String> emoryHipaaEligibleColumn = new Column<AWSServicePojo, String>(
				new TextCell()) {
			@Override
			public String getValue(AWSServicePojo object) {
				return (object.isSiteHipaaEligible() ? "Yes" : "No");
			}
		};
		serviceListTable.addColumn(emoryHipaaEligibleColumn, "Emory HIPAA Eligible");
		serviceListTable.setColumnWidth(emoryHipaaEligibleColumn, 40, Unit.PX);

		// combined name
		Column<AWSServicePojo, String> combinedNameColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getCombinedServiceName();
			}
		};
		combinedNameColumn.setSortable(true);
		sortHandler.setComparator(combinedNameColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getCombinedServiceName().compareTo(o2.getCombinedServiceName());
			}
		});
		serviceListTable.addColumn(combinedNameColumn, "Combined Name");

		// combined name
		Column<AWSServicePojo, String> alternateNameColumn = 
				new Column<AWSServicePojo, String> (new TextCell()) {

			@Override
			public String getValue(AWSServicePojo object) {
				return object.getAlternateServiceName();
			}
		};
		alternateNameColumn.setSortable(true);
		sortHandler.setComparator(alternateNameColumn, new Comparator<AWSServicePojo>() {
			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
				return o1.getAlternateServiceName().compareTo(o2.getAlternateServiceName());
			}
		});
		serviceListTable.addColumn(alternateNameColumn, "Alternate Name");

		// create user
		Column<AWSServicePojo, String> createUserColumn = 
				new Column<AWSServicePojo, String> (new ClickableTextCell()) {

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
		createUserColumn.setFieldUpdater(new FieldUpdater<AWSServicePojo, String>() {
	    	@Override
	    	public void update(int index, AWSServicePojo object, String value) {
	    		GWT.log("showing directory data for: " + object.getCreateUser());
	    		showDirectoryMetaDataForPublicId(object.getCreateUser());
	    	}
	    });
		createUserColumn.setCellStyleNames("tableAnchor");
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
//		Column<AWSServicePojo, String> lastUpdateUserColumn = 
//				new Column<AWSServicePojo, String> (new TextCell()) {
//
//			@Override
//			public String getValue(AWSServicePojo object) {
//				return object.getUpdateUser();
//			}
//		};
//		lastUpdateUserColumn.setSortable(true);
//		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<AWSServicePojo>() {
//			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
//				return o1.getUpdateUser().compareTo(o2.getUpdateUser());
//			}
//		});
//		serviceListTable.addColumn(lastUpdateUserColumn, "Update User");

		// update time
//		Column<AWSServicePojo, String> updateTimeColumn = 
//				new Column<AWSServicePojo, String> (new TextCell()) {
//
//			@Override
//			public String getValue(AWSServicePojo object) {
//				if (object.getUpdateTime() != null) {
//					return dateFormat.format(object.getUpdateTime());
//				}
//				else {
//					return "Uknown";
//				}
//			}
//		};
//		updateTimeColumn.setSortable(true);
//		sortHandler.setComparator(updateTimeColumn, new Comparator<AWSServicePojo>() {
//			public int compare(AWSServicePojo o1, AWSServicePojo o2) {
//				if (o1.getUpdateTime() != null) {
//					return o1.getUpdateTime().compareTo(o2.getUpdateTime());
//				}
//				else {
//					return 0;
//				}
//			}
//		});
//		serviceListTable.addColumn(updateTimeColumn, "Update Time");
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

	@Override
	public void disableButtons() {
		createServiceButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		createServiceButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setFilterTypeItems(List<String> filterTypes) {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter filter value");

		this.filterTypeItems = filterTypes;
		filterTypesLB.clear();
		
		filterTypesLB.addItem("-- Select Filter Type --", "");
		if (filterTypeItems != null) {
			for (String filterType : filterTypeItems) {
				filterTypesLB.addItem(filterType, filterType);
			}
		}
	}
}
