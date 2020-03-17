package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
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
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceGuidelineView;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListServiceGuideline extends ViewImplBase implements ListServiceGuidelineView {
	Presenter presenter;
	private ListDataProvider<ServiceGuidelinePojo> dataProvider = new ListDataProvider<ServiceGuidelinePojo>();
	private SingleSelectionModel<ServiceGuidelinePojo> selectionModel;
	List<ServiceGuidelinePojo> pojoList = new java.util.ArrayList<ServiceGuidelinePojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);


	private static DesktopListServiceGuidelineUiBinder uiBinder = GWT.create(DesktopListServiceGuidelineUiBinder.class);

	interface DesktopListServiceGuidelineUiBinder extends UiBinder<Widget, DesktopListServiceGuideline> {
	}

	public DesktopListServiceGuideline() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) CellTable<ServiceGuidelinePojo> listTable = new CellTable<ServiceGuidelinePojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button createButton;
	@UiField Button actionsButton;

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

		Anchor editAnchor = new Anchor("View/Maintain Service Control");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Service Control");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ServiceGuidelinePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SERVICE_GUIDELINE, presenter.getService(), presenter.getAssessment(), m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, editAnchor);

		if (userLoggedIn.isCentralAdmin()) {
			Anchor deleteAnchor = new Anchor("Delete Service Control");
			deleteAnchor.addStyleName("productAnchor");
			deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
			deleteAnchor.setTitle("Delete selected Service Control");
			deleteAnchor.ensureDebugId(deleteAnchor.getText());
			deleteAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					actionsPopup.hide();
					ServiceGuidelinePojo m = selectionModel.getSelectedObject();
					if (m != null) {
						presenter.deleteServiceGuideline(m);
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

	@UiHandler ("createButton")
	void createServiceGuidelineClicked(ClickEvent e) {
		if (presenter.getAssessment() == null || presenter.getAssessment().getStatus() == null) {
			this.showMessageToUser("Please select an Assessment Status from the list above and "
					+ "save the Assessment before adding a Service Guideline");
			return;
		}
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_SERVICE_GUIDELINE, presenter.getService(), presenter.getAssessment());
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
		
		
	}

	@Override
	public Widget getStatusMessageSource() {
		return this.actionsButton;
	}

	@Override
	public void applyCentralAdminMask() {
		createButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		createButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		createButton.setEnabled(false);
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
	public void clearList() {
		listTable.setVisibleRangeAndClearData(listTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setServiceGuidelines(List<ServiceGuidelinePojo> serviceControls) {
		GWT.log("view Setting service controls.");
		this.pojoList = serviceControls;
		this.initializeTable();
	    listPager.setDisplay(listTable);
	}
	private Widget initializeTable() {
		GWT.log("initializing service control list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		listTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<ServiceGuidelinePojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.pojoList);
		
		selectionModel = 
	    	new SingleSelectionModel<ServiceGuidelinePojo>(ServiceGuidelinePojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		ServiceGuidelinePojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected service control is: " + m.getServiceGuidelineName());
	    	}
	    });

	    ListHandler<ServiceGuidelinePojo> sortHandler = 
	    	new ListHandler<ServiceGuidelinePojo>(dataProvider.getList());
	    listTable.addColumnSortHandler(sortHandler);

	    if (listTable.getColumnCount() == 0) {
		    initListTableColumns(sortHandler);
	    }
		
		return listTable;
	}

	private void initListTableColumns(ListHandler<ServiceGuidelinePojo> sortHandler) {
		GWT.log("initializing Service Control list table columns...");

		Column<ServiceGuidelinePojo, Boolean> checkColumn = new Column<ServiceGuidelinePojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(ServiceGuidelinePojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<ServiceGuidelinePojo, String> sequenceNumber = 
				new Column<ServiceGuidelinePojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceGuidelinePojo object) {
				return Integer.toString(object.getSequenceNumber());
			}
		};
		sequenceNumber.setSortable(true);
		sequenceNumber.setCellStyleNames("tableBody");
		sortHandler.setComparator(sequenceNumber, new Comparator<ServiceGuidelinePojo>() {
			public int compare(ServiceGuidelinePojo o1, ServiceGuidelinePojo o2) {
				int seq1 = o1.getSequenceNumber();
				int seq2 = o2.getSequenceNumber();
				if (seq1 == seq2) {
					return 0;
				}
				if (seq1 > seq2) {
					return -1;
				}
				return 1;
			}
		});
		listTable.addColumn(sequenceNumber, "Sequence Number");

		Column<ServiceGuidelinePojo, String> nameColumn = 
				new Column<ServiceGuidelinePojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceGuidelinePojo object) {
				return object.getServiceGuidelineName();
			}
		};
		nameColumn.setSortable(true);
		nameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(nameColumn, new Comparator<ServiceGuidelinePojo>() {
			public int compare(ServiceGuidelinePojo o1, ServiceGuidelinePojo o2) {
				return o1.getServiceGuidelineName().compareTo(o2.getServiceGuidelineName());
			}
		});
		listTable.addColumn(nameColumn, "Name");
		
		Column<ServiceGuidelinePojo, String> descColumn = 
				new Column<ServiceGuidelinePojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceGuidelinePojo object) {
				return object.getDescription();
			}
		};
		descColumn.setSortable(true);
		descColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(descColumn, new Comparator<ServiceGuidelinePojo>() {
			public int compare(ServiceGuidelinePojo o1, ServiceGuidelinePojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
		listTable.addColumn(descColumn, "Description");
		
		Column<ServiceGuidelinePojo, String> assessorId = 
				new Column<ServiceGuidelinePojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceGuidelinePojo object) {
				// TODO: should this be their name?
				return object.getAssessorId();
			}
		};
		assessorId.setSortable(true);
		assessorId.setCellStyleNames("tableBody");
		sortHandler.setComparator(assessorId, new Comparator<ServiceGuidelinePojo>() {
			public int compare(ServiceGuidelinePojo o1, ServiceGuidelinePojo o2) {
				return o1.getAssessorId().compareTo(o2.getAssessorId());
			}
		});
		listTable.addColumn(assessorId, "Assessor");
		
		Column<ServiceGuidelinePojo, String> assessmentDate = 
				new Column<ServiceGuidelinePojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceGuidelinePojo object) {
				Date d1 = object.getAssessmentDate();
				return d1 != null ? dateFormat.format(d1) : "Unknown";
			}
		};
		assessmentDate.setSortable(true);
		sortHandler.setComparator(assessmentDate, new Comparator<ServiceGuidelinePojo>() {
			public int compare(ServiceGuidelinePojo o1, ServiceGuidelinePojo o2) {
				GWT.log("user notification create time sort handler...");
				Date c1 = o1.getAssessmentDate();
				Date c2 = o2.getAssessmentDate();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
		listTable.addColumn(assessmentDate, "Assessment Date");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

	@Override
	public void removeServiceGuidelineFromView(ServiceGuidelinePojo serviceControls) {
		dataProvider.getList().remove(serviceControls);
	}

	@Override
	public void disableButtons() {
		createButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		createButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
		
		
	}
}
