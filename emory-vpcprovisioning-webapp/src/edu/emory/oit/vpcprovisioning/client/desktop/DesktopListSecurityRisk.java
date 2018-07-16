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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.resources.client.ClientBundle.Source;
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
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListNotification.MyCellTableResources;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.ListSecurityRiskView;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;

public class DesktopListSecurityRisk extends ViewImplBase implements ListSecurityRiskView {
	Presenter presenter;
	private ListDataProvider<SecurityRiskPojo> dataProvider = new ListDataProvider<SecurityRiskPojo>();
	private SingleSelectionModel<SecurityRiskPojo> selectionModel;
	List<SecurityRiskPojo> pojoList = new java.util.ArrayList<SecurityRiskPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);


	private static DesktopListSecurityRiskUiBinder uiBinder = GWT.create(DesktopListSecurityRiskUiBinder.class);

	interface DesktopListSecurityRiskUiBinder extends UiBinder<Widget, DesktopListSecurityRisk> {
	}

	public DesktopListSecurityRisk() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	/*** FIELDS ***/
	@UiField SimplePager listPager;
	@UiField(provided=true) CellTable<SecurityRiskPojo> listTable = new CellTable<SecurityRiskPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
//	@UiField Button closeOtherFeaturesButton;
	@UiField Button createButton;
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

		Anchor editAnchor = new Anchor("Edit Security Risk");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Security Risk");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				SecurityRiskPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_RISK, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, editAnchor);

		Anchor deleteAnchor = new Anchor("Delete Security Risk");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected Security Risk");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				SecurityRiskPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					presenter.deleteSecurityRisk(m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@UiHandler ("createButton")
	void createSserviceClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_SECURITY_RISK);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		return this.createButton;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		// TODO Auto-generated method stub
		return null;
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
	public void clearList() {
		listTable.setVisibleRangeAndClearData(listTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setSecurityRisks(List<SecurityRiskPojo> securityRisks) {
		GWT.log("view Setting security risks.");
		this.pojoList = securityRisks;
		this.initializeTable();
	    listPager.setDisplay(listTable);
	}
	private Widget initializeTable() {
		GWT.log("initializing security risk list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		listTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<SecurityRiskPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.pojoList);
		
		selectionModel = 
	    	new SingleSelectionModel<SecurityRiskPojo>(SecurityRiskPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		SecurityRiskPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected security risk is: " + m.getSecurityRiskName());
	    	}
	    });

	    ListHandler<SecurityRiskPojo> sortHandler = 
	    	new ListHandler<SecurityRiskPojo>(dataProvider.getList());
	    listTable.addColumnSortHandler(sortHandler);

	    if (listTable.getColumnCount() == 0) {
		    initListTableColumns(sortHandler);
	    }
		
		return listTable;
	}

	private void initListTableColumns(ListHandler<SecurityRiskPojo> sortHandler) {
		GWT.log("initializing Security Risk list table columns...");

		Column<SecurityRiskPojo, Boolean> checkColumn = new Column<SecurityRiskPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(SecurityRiskPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<SecurityRiskPojo, String> nameColumn = 
				new Column<SecurityRiskPojo, String> (new TextCell()) {

			@Override
			public String getValue(SecurityRiskPojo object) {
				return object.getSecurityRiskName();
			}
		};
		nameColumn.setSortable(true);
		nameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(nameColumn, new Comparator<SecurityRiskPojo>() {
			public int compare(SecurityRiskPojo o1, SecurityRiskPojo o2) {
				return o1.getSecurityRiskName().compareTo(o2.getSecurityRiskName());
			}
		});
		listTable.addColumn(nameColumn, "Name");
		
	/*
		SecurityRiskId, 
		ServiceId, 
		SequenceNumber, 
		ServiceRiskName, 
		RiskLevel, 
		Description, 
		AssessorId, 
		AssessmentDatetime, 
		Countermeasure*
	 */
		Column<SecurityRiskPojo, String> descColumn = 
				new Column<SecurityRiskPojo, String> (new TextCell()) {

			@Override
			public String getValue(SecurityRiskPojo object) {
				return object.getDescription();
			}
		};
		descColumn.setSortable(true);
		descColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(descColumn, new Comparator<SecurityRiskPojo>() {
			public int compare(SecurityRiskPojo o1, SecurityRiskPojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
		listTable.addColumn(descColumn, "Description");
		
		Column<SecurityRiskPojo, String> riskLevelColumn = 
				new Column<SecurityRiskPojo, String> (new TextCell()) {

			@Override
			public String getValue(SecurityRiskPojo object) {
				return object.getRiskLevel();
			}
		};
		riskLevelColumn.setSortable(true);
		riskLevelColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(riskLevelColumn, new Comparator<SecurityRiskPojo>() {
			public int compare(SecurityRiskPojo o1, SecurityRiskPojo o2) {
				return o1.getRiskLevel().compareTo(o2.getRiskLevel());
			}
		});
		listTable.addColumn(riskLevelColumn, "Risk Level");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSecurityRiskFromView(SecurityRiskPojo securityRisks) {
		dataProvider.getList().remove(securityRisks);
	}

}
