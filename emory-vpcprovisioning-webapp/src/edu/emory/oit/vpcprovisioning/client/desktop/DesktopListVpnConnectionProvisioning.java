package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.ui.HTMLUtils;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpncpPojo;

public class DesktopListVpnConnectionProvisioning extends ViewImplBase implements ListVpnConnectionProvisioningView {
	Presenter presenter;
	private ListDataProvider<VpncpPojo> dataProvider = new ListDataProvider<VpncpPojo>();
	private SingleSelectionModel<VpncpPojo> selectionModel;
	List<VpncpPojo> List = new java.util.ArrayList<VpncpPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager listPager;
//	@UiField Button generateButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<VpncpPojo> listTable = new CellTable<VpncpPojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField PushButton refreshButton;
	@UiField CheckBox viewAllCB;
	@UiField HTML filteredHTML;

	public interface MyCellTableResources extends CellTable.Resources {

		@Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

	private static DesktopListVpnConnectionProvisioningUiBinder uiBinder = GWT
			.create(DesktopListVpnConnectionProvisioningUiBinder.class);

	interface DesktopListVpnConnectionProvisioningUiBinder
			extends UiBinder<Widget, DesktopListVpnConnectionProvisioning> {
	}

	public DesktopListVpnConnectionProvisioning() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);

//		generateButton.addDomHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				GWT.log("Should go to generate  here...");
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPCP);
//			}
//		}, ClickEvent.getType());
	}

	@UiHandler ("filterTB")
	void addEmailTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		presenter.filterByProvisioningId(true, filterTB.getText());
        }
	}
	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		presenter.filterByProvisioningId(true, filterTB.getText());
	}
	
	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllVpnConnectionProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumVpnConnectionProvisionings(userLoggedIn);
		}
		this.hideFilteredStatus();
	}

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllVpnConnectionProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumVpnConnectionProvisionings(userLoggedIn);
		}
	}

	@UiHandler("viewAllCB")
	void viewAllCBClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllVpnConnectionProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumVpnConnectionProvisionings(userLoggedIn);
		}
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid = new Grid(1, 1);
		grid.setCellSpacing(8);
		actionsPopup.add(grid);

		Anchor assignAnchor = new Anchor("View Status");
		assignAnchor.addStyleName("productAnchor");
		assignAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		assignAnchor.setTitle("View status of selected VPCP");
		assignAnchor.ensureDebugId(assignAnchor.getText());
		assignAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				VpncpPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_VPNCP_STATUS, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, assignAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget getStatusMessageSource() {
		return actionsButton;
	}

	@Override
	public void applyAWSAccountAdminMask() {
//		generateButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
//		generateButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
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
	public void setVpnConnectionProvisionings(List<VpncpPojo> s) {
		this.List = s;
		this.initializeVpnConnectionProvisioninglistTable();
		listPager.setDisplay(listTable);
	}

	private Widget initializeVpnConnectionProvisioninglistTable() {
		GWT.log("initializing VPCP list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		listTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<VpncpPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.List);

		selectionModel = 
				new SingleSelectionModel<VpncpPojo>(VpncpPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				VpncpPojo m = selectionModel.getSelectedObject();
				GWT.log("Selected  is: " + m.getProvisioningId());
			}
		});

		ListHandler<VpncpPojo> sortHandler = 
				new ListHandler<VpncpPojo>(dataProvider.getList());
		listTable.addColumnSortHandler(sortHandler);

		if (listTable.getColumnCount() == 0) {
			initVpnConnectionProvisioninglistTableColumns(sortHandler);
		}

		return listTable;
	}

	private void initVpnConnectionProvisioninglistTableColumns(ListHandler<VpncpPojo> sortHandler) {
		GWT.log("initializing VPCP list table columns...");

		Column<VpncpPojo, Boolean> checkColumn = new Column<VpncpPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(VpncpPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// create time
		Column<VpncpPojo, String> createTimeColumn = 
				new Column<VpncpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpncpPojo object) {
				Date createTime = object.getCreateTime();
				return createTime != null ? dateFormat.format(createTime) : "Unknown";
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<VpncpPojo>() {
			public int compare(VpncpPojo o1, VpncpPojo o2) {
				Date c1 = o1.getCreateTime();
				Date c2 = o2.getCreateTime();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
		listTable.addColumn(createTimeColumn, "Create Time");

		// Provisioning id column
		Column<VpncpPojo, String> provIdColumn = 
				new Column<VpncpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpncpPojo object) {
				return object.getProvisioningId();
			}
		};
		provIdColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpncpPojo>() {
			public int compare(VpncpPojo o1, VpncpPojo o2) {
				return o1.getProvisioningId().compareTo(o2.getProvisioningId());
			}
		});
		listTable.addColumn(provIdColumn, "Provisioning ID");

		// Status
		Column<VpncpPojo, String> statusColumn = 
				new Column<VpncpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpncpPojo object) {
				return object.getStatus();
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpncpPojo>() {
			public int compare(VpncpPojo o1, VpncpPojo o2) {
				return o1.getStatus().compareTo(o2.getStatus());
			}
		});
		listTable.addColumn(statusColumn, "Status");

		// Provisioning result
		Column<VpncpPojo, String> resultColumn = 
				new Column<VpncpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpncpPojo object) {
				return object.getProvisioningResult();
			}
		};
		resultColumn.setSortable(true);
		sortHandler.setComparator(resultColumn, new Comparator<VpncpPojo>() {
			public int compare(VpncpPojo o1, VpncpPojo o2) {
				return o1.getProvisioningResult().compareTo(o2.getProvisioningResult());
			}
		});
		listTable.addColumn(resultColumn, "Provisioning Result");

		// Anticipated time
		Column<VpncpPojo, String> anticipatedTimeColumn = 
				new Column<VpncpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpncpPojo object) {
				return object.getAnticipatedTime();
			}
		};
		anticipatedTimeColumn.setSortable(true);
		sortHandler.setComparator(anticipatedTimeColumn, new Comparator<VpncpPojo>() {
			public int compare(VpncpPojo o1, VpncpPojo o2) {
				return o1.getAnticipatedTime().compareTo(o2.getAnticipatedTime());
			}
		});
		listTable.addColumn(anticipatedTimeColumn, "Anticipated Time");

		// Actual time
		Column<VpncpPojo, String> actualTimeColumn = 
				new Column<VpncpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpncpPojo object) {
				return object.getActualTime();
			}
		};
		actualTimeColumn.setSortable(true);
		sortHandler.setComparator(actualTimeColumn, new Comparator<VpncpPojo>() {
			public int compare(VpncpPojo o1, VpncpPojo o2) {
				return o1.getActualTime().compareTo(o2.getActualTime());
			}
		});
		listTable.addColumn(actualTimeColumn, "Actual Time");

		// Provisioning steps progress status
		final SafeHtmlCell stepProgressCell = new SafeHtmlCell();

		Column<VpncpPojo, SafeHtml> stepProgressCol = new Column<VpncpPojo, SafeHtml>(
				stepProgressCell) {

			@Override
			public SafeHtml getValue(VpncpPojo value) {
				SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getTotalStepCount(), value.getCompletedStepCount());
				return sh;
			}
		};		 
		listTable.addColumn(stepProgressCol, "Progress");
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
	public void removeVpnConnectionProvisioningFromView(VpncpPojo vpncp ) {
		dataProvider.getList().remove(vpncp);
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
//		generateButton.setEnabled(true);;
		actionsButton.setEnabled(true);;
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
//		generateButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
//		generateButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
	}

	@Override
	public boolean viewAllVpnConnectionProvisionings() {
		return viewAllCB.getValue();
	}

	@Override
	public void initPage() {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter a provisioning id");
	}

	@Override
	public void showFilteredStatus() {
		filteredHTML.setVisible(true);
	}
	
	@Override
	public void hideFilteredStatus() {
		filteredHTML.setVisible(false);
	}
}
