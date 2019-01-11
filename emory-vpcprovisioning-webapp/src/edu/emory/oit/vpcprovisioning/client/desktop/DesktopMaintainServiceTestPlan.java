package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanView;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestRequirementPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainServiceTestPlan extends ViewImplBase implements MaintainServiceTestPlanView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	boolean editing;
//	PopupPanel actionsPopup = new PopupPanel(true);
	List<String> testExpectedResultItems = new java.util.ArrayList<String>();
	static final String SAVE = "Save";
	static final String ADD = "Add";
	List<ServiceTestRequirementPojo> requirementList = new java.util.ArrayList<ServiceTestRequirementPojo>();

	DialogBox reqmtPopup = new DialogBox();
	PopupPanel reqmtActionsPopup = new PopupPanel(true);
	DialogBox testPopup = new DialogBox();
	PopupPanel testActionsPopup = new PopupPanel(true);
	DialogBox stepPopup = new DialogBox(true);
	PopupPanel stepActionsPopup = new PopupPanel(true);

	ListDataProvider<ServiceTestRequirementPojo> requirementDataProvider = 
			new ListDataProvider<ServiceTestRequirementPojo>(new ArrayList<ServiceTestRequirementPojo>());
	ListDataProvider<ServiceTestPojo> testDataProvider = 
			new ListDataProvider<ServiceTestPojo>(new ArrayList<ServiceTestPojo>());
	ListDataProvider<ServiceTestStepPojo> stepDataProvider = 
			new ListDataProvider<ServiceTestStepPojo>(new ArrayList<ServiceTestStepPojo>());
	
	SingleSelectionModel<ServiceTestRequirementPojo> reqmtSelectionModel = 
			new SingleSelectionModel<ServiceTestRequirementPojo>(ServiceTestRequirementPojo.KEY_PROVIDER);
	SingleSelectionModel<ServiceTestPojo> testSelectionModel = 
			new SingleSelectionModel<ServiceTestPojo>(ServiceTestPojo.KEY_PROVIDER);
	SingleSelectionModel<ServiceTestStepPojo> stepSelectionModel = 
			new SingleSelectionModel<ServiceTestStepPojo>(ServiceTestStepPojo.KEY_PROVIDER);

//	@UiField Button okayButton;
//	@UiField Button cancelButton;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField(provided=true) SimplePager reqmtListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager testListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager stepListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) CellTable<ServiceTestRequirementPojo> reqmtListTable = new CellTable<ServiceTestRequirementPojo>(5, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField(provided=true) CellTable<ServiceTestPojo> testListTable = new CellTable<ServiceTestPojo>(5, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField(provided=true) CellTable<ServiceTestStepPojo> stepListTable = new CellTable<ServiceTestStepPojo>(5, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField Button createReqmtButton;
	@UiField Button reqmtActionsButton;
	@UiField Button createTestButton;
	@UiField Button testActionsButton;
	@UiField Button createStepButton;
	@UiField Button stepActionsButton;

	private static DesktopMaintainServiceTestPlanUiBinder uiBinder = GWT
			.create(DesktopMaintainServiceTestPlanUiBinder.class);

	interface DesktopMaintainServiceTestPlanUiBinder extends UiBinder<Widget, DesktopMaintainServiceTestPlan> {
	}

	public DesktopMaintainServiceTestPlan() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}

	@UiHandler("reqmtActionsButton")
	void reqmtActionsButtonClicked(ClickEvent e) {
		reqmtActionsPopup.clear();
		reqmtActionsPopup.setAutoHideEnabled(true);
		reqmtActionsPopup.setAnimationEnabled(true);
		reqmtActionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid;
		grid = new Grid(2,1);

		grid.setCellSpacing(8);
		reqmtActionsPopup.add(grid);

		Anchor editAnchor = new Anchor("View/Maintain Requirement");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Requirement");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reqmtActionsPopup.hide();
				ServiceTestRequirementPojo m = reqmtSelectionModel.getSelectedObject();
				if (m == null) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				// view/maintain requirement (via requirement popup)
				presenter.maintainRequirement(m);
			}
		});
		grid.setWidget(0, 0, editAnchor);

		Anchor deleteAnchor = new Anchor("Delete Requirement");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected Requirement");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reqmtActionsPopup.hide();
				ServiceTestRequirementPojo m = reqmtSelectionModel.getSelectedObject();
				if (m == null) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				// delete requirement and refresh list (via presenter)
				presenter.deleteRequirement(m);
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		reqmtActionsPopup.showRelativeTo(reqmtActionsButton);
	}

	@UiHandler ("createReqmtButton")
	void createReqmtClicked(ClickEvent e) {
		presenter.createRequirement();
	}

	@UiHandler("testActionsButton")
	void testActionsButtonClicked(ClickEvent e) {
		testActionsPopup.clear();
		testActionsPopup.setAutoHideEnabled(true);
		testActionsPopup.setAnimationEnabled(true);
		testActionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid;
		grid = new Grid(2,1);

		grid.setCellSpacing(8);
		testActionsPopup.add(grid);

		Anchor editAnchor = new Anchor("View/Maintain Test");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Test");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				testActionsPopup.hide();
				ServiceTestPojo m = testSelectionModel.getSelectedObject();
				if (m == null) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				// view/maintain requirement (via requirement popup)
				presenter.maintainTest(m);
			}
		});
		grid.setWidget(0, 0, editAnchor);

		Anchor deleteAnchor = new Anchor("Delete Test");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected Test");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				testActionsPopup.hide();
				ServiceTestPojo m = testSelectionModel.getSelectedObject();
				if (m == null) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				// delete test and refresh list (via presenter)
				presenter.deleteTest(m);
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		testActionsPopup.showRelativeTo(testActionsButton);
	}

	@UiHandler ("createTestButton")
	void createTestClicked(ClickEvent e) {
		presenter.createTest();
	}

	@UiHandler("stepActionsButton")
	void stepActionsButtonClicked(ClickEvent e) {
		stepActionsPopup.clear();
		stepActionsPopup.setAutoHideEnabled(true);
		stepActionsPopup.setAnimationEnabled(true);
		stepActionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid;
		grid = new Grid(2,1);

		grid.setCellSpacing(8);
		stepActionsPopup.add(grid);

		Anchor editAnchor = new Anchor("View/Maintain Test Step");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Test Step");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stepActionsPopup.hide();
				ServiceTestStepPojo m = stepSelectionModel.getSelectedObject();
				if (m == null) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				// view/maintain step (via step popup)
				presenter.maintainStep(m);
			}
		});
		grid.setWidget(0, 0, editAnchor);

		Anchor deleteAnchor = new Anchor("Delete Test Step");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected Test Step");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stepActionsPopup.hide();
				ServiceTestStepPojo m = stepSelectionModel.getSelectedObject();
				if (m == null) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				// delete step and refresh list (via presenter)
				presenter.deleteStep(m);
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		stepActionsPopup.showRelativeTo(stepActionsButton);
	}

	@UiHandler ("createStepButton")
	void createStepClicked(ClickEvent e) {
		presenter.createStep();
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return null;
	}

	@Override
	public void applyCentralAdminMask() {
		createReqmtButton.setEnabled(true);
		reqmtActionsButton.setEnabled(true);
		createTestButton.setEnabled(true);
		testActionsButton.setEnabled(true);
		createStepButton.setEnabled(true);
		stepActionsButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		createReqmtButton.setEnabled(false);
		reqmtActionsButton.setEnabled(false);
		createTestButton.setEnabled(false);
		testActionsButton.setEnabled(false);
		createStepButton.setEnabled(false);
		stepActionsButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		createReqmtButton.setEnabled(false);
		reqmtActionsButton.setEnabled(false);
		createTestButton.setEnabled(false);
		testActionsButton.setEnabled(false);
		createStepButton.setEnabled(false);
		stepActionsButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		return null;
	}

	@Override
	public void resetFieldStyles() {
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
	public void vpcpPromptOkay(String valueEntered) {
	}

	@Override
	public void vpcpPromptCancel() {
	}

	@Override
	public void vpcpConfirmOkay() {
	}

	@Override
	public void vpcpConfirmCancel() {
	}

	@Override
	public void disableButtons() {
	}

	@Override
	public void enableButtons() {
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		GWT.log("MaintainServiceTestPlan.initPage()");
		
//		refreshDataProvider();
	}

	private void hidePanels() {
	}
	
	private void hideButtons() {
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
	}

	@Override
	public void setTestExpectedResultItems(List<String> items) {
		this.testExpectedResultItems = items;
//		testExpectedResultLB.clear();
//		testExpectedResultLB.addItem("-- Select --", "");
//		if (testExpectedResultLB != null) {
//			for (String type : testExpectedResultItems) {
//				testExpectedResultLB.addItem(type, type);
//			}
//		}
	}

	@Override
	public void refreshDataProvider() {
		GWT.log("refreshing data provider...test " + 
			"requirement count is: " + 
		presenter.getServiceTestPlan().getServiceTestRequirements().size());
		requirementDataProvider.setList(presenter.getServiceTestPlan().getServiceTestRequirements());
		requirementDataProvider.refresh();
		if (presenter.getServiceTestPlan().getServiceTestRequirements().size() > 0) {
			
			ServiceTestRequirementPojo str = presenter.getServiceTestPlan().getServiceTestRequirements().get(0);
			reqmtSelectionModel.setSelected(str, true);
			SelectionChangeEvent.fire(reqmtSelectionModel);
		}
		else {
			GWT.log("[refreshDataProvider] can't get a test requirement from the current assessment.");
		}
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requirementSelected() {
		ServiceTestRequirementPojo selected = reqmtSelectionModel.getSelectedObject();
		GWT.log("[REQ:onSelectionChange] requirment selected: " + selected);
		presenter.setSelectedTestRequirement(selected);
	}

	@Override
	public void testSelected() {
		ServiceTestPojo selected = testSelectionModel.getSelectedObject();
		GWT.log("[TEST:onSelectionChange] test selected: " + selected);
		presenter.setSelectedTest(selected);
	}

	@Override
	public void stepSelected() {
		ServiceTestStepPojo selected = stepSelectionModel.getSelectedObject();
		GWT.log("[STEP: onSelectionChange] step selected: " + selected);
		presenter.setSelectedTestStep(selected);
	}

	@Override
	public void setRequirements(List<ServiceTestRequirementPojo> requirements) {
		GWT.log("[DesktopMaintainServiceTestPlanView.setRequirements] there are " + requirements.size() + " requirements in this test plan.");
		this.requirementList = requirements;
		this.initializeRequirementListTable();
		reqmtListPager.setDisplay(reqmtListTable);
		presenter.setSelectedTestRequirement(null);
		presenter.refreshTestList(userLoggedIn);
	}

	private Widget initializeRequirementListTable() {
		GWT.log("initializing requirements list table...");
		reqmtListTable.setTableLayoutFixed(false);
		reqmtListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		reqmtListTable.setVisibleRange(0, 5);

		// create dataprovider
		requirementDataProvider = new ListDataProvider<ServiceTestRequirementPojo>();
		requirementDataProvider.addDataDisplay(reqmtListTable);
		requirementDataProvider.getList().clear();
		requirementDataProvider.getList().addAll(this.requirementList);

		reqmtSelectionModel = 
				new SingleSelectionModel<ServiceTestRequirementPojo>(ServiceTestRequirementPojo.KEY_PROVIDER);

		reqmtSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ServiceTestRequirementPojo m = reqmtSelectionModel.getSelectedObject();
				// refresh tests table
				presenter.setSelectedTestRequirement(m);
				presenter.refreshTestList(userLoggedIn);
			}
		});
		reqmtListTable.setSelectionModel(reqmtSelectionModel);

		ListHandler<ServiceTestRequirementPojo> sortHandler = 
				new ListHandler<ServiceTestRequirementPojo>(requirementDataProvider.getList());
		reqmtListTable.addColumnSortHandler(sortHandler);

		if (reqmtListTable.getColumnCount() == 0) {
			initRequirementListTableColumns(sortHandler);
		}
		
		return reqmtListTable;
	}
	
	private void initRequirementListTableColumns(ListHandler<ServiceTestRequirementPojo> sortHandler) {
		GWT.log("initializing requirement list table columns...");

		Column<ServiceTestRequirementPojo, Boolean> checkColumn = new Column<ServiceTestRequirementPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(ServiceTestRequirementPojo object) {
				// Get the value from the selection model.
				return reqmtSelectionModel.isSelected(object);
			}
		};
		reqmtListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		reqmtListTable.setColumnWidth(checkColumn, 40, Unit.PX);
		
		// sequence column
		Column<ServiceTestRequirementPojo, String> sequenceColumn = 
				new Column<ServiceTestRequirementPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceTestRequirementPojo object) {
				return Integer.toString(object.getSequenceNumber());
			}
		};
		sequenceColumn.setSortable(true);
		sortHandler.setComparator(sequenceColumn, new Comparator<ServiceTestRequirementPojo>() {
			public int compare(ServiceTestRequirementPojo o1, ServiceTestRequirementPojo o2) {
				if (o1.getSequenceNumber() == o2.getSequenceNumber()) {
					return 0;
				}
				if (o1.getSequenceNumber() > o2.getSequenceNumber()) {
					return 1;
				}
				return -1;
			}
		});
//		sequenceColumn.setFieldUpdater(new FieldUpdater<ServiceTestRequirementPojo, String>() {
//	    	@Override
//	    	public void update(int index, ServiceTestRequirementPojo object, String value) {
//	    		presenter.maintainRequirement(object);
//	    	}
//	    });
//		sequenceColumn.setCellStyleNames("tableAnchor");
		reqmtListTable.addColumn(sequenceColumn, "Sequence Number");
		
		// description column
		Column<ServiceTestRequirementPojo, String> descColumn = 
				new Column<ServiceTestRequirementPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceTestRequirementPojo object) {
				return object.getDescription();
			}
		};
		descColumn.setSortable(true);
		sortHandler.setComparator(descColumn, new Comparator<ServiceTestRequirementPojo>() {
			public int compare(ServiceTestRequirementPojo o1, ServiceTestRequirementPojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
//		descColumn.setFieldUpdater(new FieldUpdater<ServiceTestRequirementPojo, String>() {
//	    	@Override
//	    	public void update(int index, ServiceTestRequirementPojo object, String value) {
//	    		presenter.maintainRequirement(object);
//	    	}
//	    });
//		descColumn.setCellStyleNames("tableAnchor");
		reqmtListTable.addColumn(descColumn, "Description");
	}

	@Override
	public void setTests(List<ServiceTestPojo> tests) {
		GWT.log("[DesktopMaintainServiceTestPlanView.setSteps] there are " + tests.size() + " tests in the selected requirement.");
		this.initializeTestListTable();
		testListPager.setDisplay(testListTable);
		presenter.setSelectedTest(null);
		presenter.refreshStepList(userLoggedIn);
	}

	private Widget initializeTestListTable() {
		GWT.log("initializing test list table...");
		testListTable.setTableLayoutFixed(false);
		testListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		testListTable.setVisibleRange(0, 5);

		// create dataprovider
		testDataProvider = new ListDataProvider<ServiceTestPojo>();
		testDataProvider.addDataDisplay(testListTable);
		testDataProvider.getList().clear();
		if (presenter.getSelectedTestRequirement() != null) {
			testDataProvider.getList().addAll(presenter.getSelectedTestRequirement().getServiceTests());
		}

		testSelectionModel = 
				new SingleSelectionModel<ServiceTestPojo>(ServiceTestPojo.KEY_PROVIDER);

		testSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ServiceTestPojo m = testSelectionModel.getSelectedObject();
				// refresh tests table
				presenter.setSelectedTest(m);
				presenter.refreshStepList(userLoggedIn);
			}
		});
		testListTable.setSelectionModel(testSelectionModel);

		ListHandler<ServiceTestPojo> sortHandler = 
				new ListHandler<ServiceTestPojo>(testDataProvider.getList());
		testListTable.addColumnSortHandler(sortHandler);

		if (testListTable.getColumnCount() == 0) {
			initTestListTableColumns(sortHandler);
		}
		
		return testListTable;
	}

	private void initTestListTableColumns(ListHandler<ServiceTestPojo> sortHandler) {
		GWT.log("initializing test list table columns...");

		Column<ServiceTestPojo, Boolean> checkColumn = new Column<ServiceTestPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(ServiceTestPojo object) {
				// Get the value from the selection model.
				return testSelectionModel.isSelected(object);
			}
		};
		testListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		testListTable.setColumnWidth(checkColumn, 40, Unit.PX);
		
		// sequence column
		Column<ServiceTestPojo, String> sequenceColumn = 
				new Column<ServiceTestPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceTestPojo object) {
				return Integer.toString(object.getSequenceNumber());
			}
		};
		sequenceColumn.setSortable(true);
		sortHandler.setComparator(sequenceColumn, new Comparator<ServiceTestPojo>() {
			public int compare(ServiceTestPojo o1, ServiceTestPojo o2) {
				if (o1.getSequenceNumber() == o2.getSequenceNumber()) {
					return 0;
				}
				if (o1.getSequenceNumber() > o2.getSequenceNumber()) {
					return 1;
				}
				return -1;
			}
		});
//		sequenceColumn.setFieldUpdater(new FieldUpdater<ServiceTestPojo, String>() {
//	    	@Override
//	    	public void update(int index, ServiceTestPojo object, String value) {
//	    		presenter.maintainTest(object);
//	    	}
//	    });
//		sequenceColumn.setCellStyleNames("tableAnchor");
		testListTable.addColumn(sequenceColumn, "Sequence Number");
		
		// description column
		Column<ServiceTestPojo, String> descColumn = 
				new Column<ServiceTestPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceTestPojo object) {
				return object.getDescription();
			}
		};
		descColumn.setSortable(true);
		sortHandler.setComparator(descColumn, new Comparator<ServiceTestPojo>() {
			public int compare(ServiceTestPojo o1, ServiceTestPojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
//		descColumn.setFieldUpdater(new FieldUpdater<ServiceTestPojo, String>() {
//	    	@Override
//	    	public void update(int index, ServiceTestPojo object, String value) {
//	    		presenter.maintainTest(object);
//	    	}
//	    });
//		descColumn.setCellStyleNames("tableAnchor");
		testListTable.addColumn(descColumn, "Description");

		// expected result column
		Column<ServiceTestPojo, String> expectedResultColumn = 
				new Column<ServiceTestPojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceTestPojo object) {
				return object.getServiceTestExpectedResult();
			}
		};
		expectedResultColumn.setSortable(true);
		sortHandler.setComparator(expectedResultColumn, new Comparator<ServiceTestPojo>() {
			public int compare(ServiceTestPojo o1, ServiceTestPojo o2) {
				return o1.getServiceTestExpectedResult().compareTo(o2.getServiceTestExpectedResult());
			}
		});
		testListTable.addColumn(expectedResultColumn, "Expected Result");
	}

	@Override
	public void setSteps(List<ServiceTestStepPojo> steps) {
		GWT.log("[DesktopMaintainServiceTestPlanView.setSteps] there are " + steps.size() + " steps in the selected test.");
		this.initializeTestStepListTable();
		stepListPager.setDisplay(stepListTable);
		presenter.setSelectedTestStep(null);
	}

	private Widget initializeTestStepListTable() {
		GWT.log("initializing test step list table...");
		stepListTable.setTableLayoutFixed(false);
		stepListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		stepListTable.setVisibleRange(0, 5);

		// create dataprovider
		stepDataProvider = new ListDataProvider<ServiceTestStepPojo>();
		stepDataProvider.addDataDisplay(stepListTable);
		stepDataProvider.getList().clear();
		if (presenter.getSelectedTest() != null) {
			stepDataProvider.getList().addAll(presenter.getSelectedTest().getServiceTestSteps());
		}

		stepSelectionModel = 
				new SingleSelectionModel<ServiceTestStepPojo>(ServiceTestStepPojo.KEY_PROVIDER);

		stepSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ServiceTestStepPojo m = stepSelectionModel.getSelectedObject();
				// refresh tests table
				presenter.setSelectedTestStep(m);
			}
		});
		stepListTable.setSelectionModel(stepSelectionModel);

		ListHandler<ServiceTestStepPojo> sortHandler = 
				new ListHandler<ServiceTestStepPojo>(stepDataProvider.getList());
		stepListTable.addColumnSortHandler(sortHandler);

		if (stepListTable.getColumnCount() == 0) {
			initStepListTableColumns(sortHandler);
		}
		
		return stepListTable;
	}

	private void initStepListTableColumns(ListHandler<ServiceTestStepPojo> sortHandler) {
		GWT.log("initializing step list table columns...");

		Column<ServiceTestStepPojo, Boolean> checkColumn = new Column<ServiceTestStepPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(ServiceTestStepPojo object) {
				// Get the value from the selection model.
				return stepSelectionModel.isSelected(object);
			}
		};
		stepListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		stepListTable.setColumnWidth(checkColumn, 40, Unit.PX);
		
		// sequence column
		Column<ServiceTestStepPojo, String> sequenceColumn = 
				new Column<ServiceTestStepPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceTestStepPojo object) {
				return Integer.toString(object.getSequenceNumber());
			}
		};
		sequenceColumn.setSortable(true);
		sortHandler.setComparator(sequenceColumn, new Comparator<ServiceTestStepPojo>() {
			public int compare(ServiceTestStepPojo o1, ServiceTestStepPojo o2) {
				if (o1.getSequenceNumber() == o2.getSequenceNumber()) {
					return 0;
				}
				if (o1.getSequenceNumber() > o2.getSequenceNumber()) {
					return 1;
				}
				return -1;
			}
		});
//		sequenceColumn.setFieldUpdater(new FieldUpdater<ServiceTestStepPojo, String>() {
//	    	@Override
//	    	public void update(int index, ServiceTestStepPojo object, String value) {
//	    		presenter.maintainStep(object);
//	    	}
//	    });
//		sequenceColumn.setCellStyleNames("tableAnchor");
		stepListTable.addColumn(sequenceColumn, "Sequence Number");
		
		// description column
		Column<ServiceTestStepPojo, String> descColumn = 
				new Column<ServiceTestStepPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceTestStepPojo object) {
				return object.getDescription();
			}
		};
		descColumn.setSortable(true);
		sortHandler.setComparator(descColumn, new Comparator<ServiceTestStepPojo>() {
			public int compare(ServiceTestStepPojo o1, ServiceTestStepPojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
//		descColumn.setFieldUpdater(new FieldUpdater<ServiceTestStepPojo, String>() {
//	    	@Override
//	    	public void update(int index, ServiceTestStepPojo object, String value) {
//	    		presenter.maintainStep(object);
//	    	}
//	    });
//		descColumn.setCellStyleNames("tableAnchor");
		stepListTable.addColumn(descColumn, "Description");
	}

	@Override
	public void showRequirementMaintenanceDialog(final boolean isEdit, final ServiceTestRequirementPojo selected) {
		reqmtPopup.clear();
		if (isEdit) {
			reqmtPopup.setText("View/Maintain Test Requirement");
		}
		else {
			reqmtPopup.setText("Create Test Requirement");
		}
		reqmtPopup.setGlassEnabled(true);
		reqmtPopup.setAnimationEnabled(true);
		reqmtPopup.center();
		reqmtPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(12);
		reqmtPopup.setWidget(vp);
		
		Grid grid;
		grid = new Grid(2,2);

		grid.setCellSpacing(8);
		vp.add(grid);
		
		Label l_seq = new Label("Sequence Number:");
		l_seq.addStyleName("label");
		grid.setWidget(0, 0, l_seq);
		
		final TextBox tb_seq = new TextBox();
		tb_seq.addStyleName("field");
		tb_seq.addStyleName("glowing-border");
		tb_seq.getElement().setPropertyString("placeholder", "enter numeric sequence number");
		tb_seq.setText(Integer.toString(selected.getSequenceNumber()));
		grid.setWidget(0, 1, tb_seq);
		
		Label l_desc = new Label("Description:");
		l_desc.addStyleName("label");
		grid.setWidget(1, 0, l_desc);

		final TextArea ta_desc = new TextArea();
		ta_desc.addStyleName("field");
		ta_desc.addStyleName("glowing-border");
		ta_desc.getElement().setPropertyString("placeholder", "enter requirement description");
		ta_desc.setText(selected.getDescription());
		grid.setWidget(1, 1, ta_desc);
		
		Grid buttonGrid;
		buttonGrid = new Grid(1,2);
		buttonGrid.setCellSpacing(12);
		vp.add(buttonGrid);
		vp.setCellHorizontalAlignment(buttonGrid, HasHorizontalAlignment.ALIGN_CENTER);
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.addStyleName("glowing-border");
		okayButton.setWidth("105px");
		buttonGrid.setWidget(0, 0, okayButton);
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selected.setSequenceNumber(Integer.parseInt(tb_seq.getText()));
				selected.setDescription(ta_desc.getText());

				// required fields.
				List<Widget> fields = new java.util.ArrayList<Widget>();
				if (selected.getSequenceNumber() == 0) {
					fields.add(tb_seq);
				}
				if (selected.getDescription() == null || selected.getDescription().length() == 0) {
					fields.add(ta_desc);
				}
				if (fields != null && fields.size() > 0) {
					setFieldViolations(true);
					applyStyleToMissingFields(fields);
					showMessageToUser("Please provide data for the required fields.");
					return;
				}

				// if create, add requirement to current test plan and save assessment
				// otherwise just save the assessment?
				if (!isEdit) {
					presenter.getSecurityAssessment().getServiceTestPlan().getServiceTestRequirements().add(selected);
				}
				presenter.saveAssessment();
				reqmtPopup.hide();
			}
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("normalButton");
		cancelButton.addStyleName("glowing-border");
		cancelButton.setWidth("105px");
		buttonGrid.setWidget(0, 1, cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reqmtPopup.hide();
			}
		});
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	ta_desc.setFocus(true);
	        }
	    });

		reqmtPopup.show();
		reqmtPopup.center();
	}

	@Override
	public void showTestMaintenanceDialog(final boolean isEdit, final ServiceTestPojo selected) {
		testPopup.clear();
		if (isEdit) {
			testPopup.setText("View/Maintain Test");
		}
		else {
			testPopup.setText("Create Test");
		}
		testPopup.setGlassEnabled(true);
		testPopup.setAnimationEnabled(true);
		testPopup.center();
		testPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(12);
		testPopup.setWidget(vp);

		Grid grid;
		grid = new Grid(3,2);

		grid.setCellSpacing(8);
		vp.add(grid);
		
		Label l_seq = new Label("Sequence Number:");
		l_seq.addStyleName("label");
		grid.setWidget(0, 0, l_seq);
		
		final TextBox tb_seq = new TextBox();
		tb_seq.addStyleName("field");
		tb_seq.addStyleName("glowing-border");
		tb_seq.getElement().setPropertyString("placeholder", "enter numeric sequence number");
		tb_seq.setText(Integer.toString(selected.getSequenceNumber()));
		grid.setWidget(0, 1, tb_seq);
		
		Label l_desc = new Label("Description:");
		l_desc.addStyleName("label");
		grid.setWidget(1, 0, l_desc);

		final TextArea ta_desc = new TextArea();
		ta_desc.addStyleName("field");
		ta_desc.addStyleName("glowing-border");
		ta_desc.getElement().setPropertyString("placeholder", "enter requirement description");
		ta_desc.setText(selected.getDescription());
		grid.setWidget(1, 1, ta_desc);
		
		Label l_expectedResult = new Label("Expected Result:");
		l_expectedResult.addStyleName("label");
		grid.setWidget(2, 0, l_expectedResult);
		
		final ListBox lb_expectedResult = new ListBox();
		lb_expectedResult.addStyleName("listBoxField");
		lb_expectedResult.addStyleName("glowing-border");
		grid.setWidget(2, 1, lb_expectedResult);
		if (!isEdit) {
			lb_expectedResult.addItem("-- Select --", "");
		}
		if (testExpectedResultItems != null) {
			int i=0;
			for (String status : testExpectedResultItems) {
				lb_expectedResult.addItem(status, status);
				if (isEdit) {
					if (selected.getServiceTestExpectedResult() != null) {
						GWT.log("Comparing " + selected.getServiceTestExpectedResult() + " to " + status);
						if (selected.getServiceTestExpectedResult().equalsIgnoreCase(status)) {
							lb_expectedResult.setSelectedIndex(i);
						}
					}
					else {
						GWT.log("expected result is null");
					}
				}
				else {
					GWT.log("It's a create???");
				}
				i++;
			}
		}

		Grid buttonGrid;
		buttonGrid = new Grid(1,2);
		buttonGrid.setCellSpacing(12);
		vp.add(buttonGrid);
		vp.setCellHorizontalAlignment(buttonGrid, HasHorizontalAlignment.ALIGN_CENTER);
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.addStyleName("glowing-border");
		okayButton.setWidth("105px");
		buttonGrid.setWidget(0, 0, okayButton);
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selected.setSequenceNumber(Integer.parseInt(tb_seq.getText()));
				selected.setDescription(ta_desc.getText());
				selected.setServiceTestExpectedResult(lb_expectedResult.getSelectedValue());
				
				// required fields.
				List<Widget> fields = new java.util.ArrayList<Widget>();
				if (selected.getSequenceNumber() == 0) {
					fields.add(tb_seq);
				}
				if (selected.getDescription() == null || selected.getDescription().length() == 0) {
					fields.add(ta_desc);
				}
				if (selected.getServiceTestExpectedResult() == null || selected.getServiceTestExpectedResult().length() == 0) {
					fields.add(lb_expectedResult);
				}
				if (fields != null && fields.size() > 0) {
					setFieldViolations(true);
					applyStyleToMissingFields(fields);
					showMessageToUser("Please provide data for the required fields.");
					return;
				}

				// if create, add test to selected requirement and save assessment
				// otherwise just save the assessment?
				if (!isEdit) {
					presenter.getSelectedTestRequirement().getServiceTests().add(selected);
				}
				presenter.saveAssessment();

				testPopup.hide();
			}
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("normalButton");
		cancelButton.addStyleName("glowing-border");
		cancelButton.setWidth("105px");
		buttonGrid.setWidget(0, 1, cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				testPopup.hide();
			}
		});
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	ta_desc.setFocus(true);
	        }
	    });

		testPopup.show();
		testPopup.center();
	}

	@Override
	public void showStepMaintenanceDialog(final boolean isEdit, final ServiceTestStepPojo selected) {
		stepPopup.clear();
		if (isEdit) {
			stepPopup.setText("View/Maintain Step");
		}
		else {
			stepPopup.setText("Create Step");
		}
		stepPopup.setGlassEnabled(true);
		stepPopup.setAnimationEnabled(true);
		stepPopup.center();
		stepPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(12);
		stepPopup.setWidget(vp);
		
		Grid grid;
		grid = new Grid(2,2);

		grid.setCellSpacing(8);
		vp.add(grid);
		
		Label l_seq = new Label("Sequence Number:");
		l_seq.addStyleName("label");
		grid.setWidget(0, 0, l_seq);
		
		final TextBox tb_seq = new TextBox();
		tb_seq.addStyleName("field");
		tb_seq.addStyleName("glowing-border");
		tb_seq.getElement().setPropertyString("placeholder", "enter numeric sequence number");
		tb_seq.setText(Integer.toString(selected.getSequenceNumber()));
		grid.setWidget(0, 1, tb_seq);
		
		Label l_desc = new Label("Description:");
		l_desc.addStyleName("label");
		grid.setWidget(1, 0, l_desc);

		final TextArea ta_desc = new TextArea();
		ta_desc.addStyleName("field");
		ta_desc.addStyleName("glowing-border");
		ta_desc.getElement().setPropertyString("placeholder", "enter requirement description");
		ta_desc.setText(selected.getDescription());
		grid.setWidget(1, 1, ta_desc);
		
		Grid buttonGrid;
		buttonGrid = new Grid(1,2);
		buttonGrid.setCellSpacing(12);
		vp.add(buttonGrid);
		vp.setCellHorizontalAlignment(buttonGrid, HasHorizontalAlignment.ALIGN_CENTER);
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.addStyleName("glowing-border");
		okayButton.setWidth("105px");
		buttonGrid.setWidget(0, 0, okayButton);
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selected.setServiceTestId(presenter.getSelectedTest().getServiceTestId());
				selected.setSequenceNumber(Integer.parseInt(tb_seq.getText()));
				selected.setDescription(ta_desc.getText());
				
				// required fields.
				List<Widget> fields = new java.util.ArrayList<Widget>();
				if (selected.getSequenceNumber() == 0) {
					fields.add(tb_seq);
				}
				if (selected.getDescription() == null || selected.getDescription().length() == 0) {
					fields.add(ta_desc);
				}
				if (fields != null && fields.size() > 0) {
					setFieldViolations(true);
					applyStyleToMissingFields(fields);
					showMessageToUser("Please provide data for the required fields.");
					return;
				}

				// if create, add step to selected test and save assessment
				// otherwise just save the assessment?
				if (!isEdit) {
					presenter.getSelectedTest().getServiceTestSteps().add(selected);
				}
				presenter.saveAssessment();
				
				stepPopup.hide();
			}
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("normalButton");
		cancelButton.addStyleName("glowing-border");
		cancelButton.setWidth("105px");
		buttonGrid.setWidget(0, 1, cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stepPopup.hide();
			}
		});
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	ta_desc.setFocus(true);
	        }
	    });

		stepPopup.show();
		stepPopup.center();
	}
}
