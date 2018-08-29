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
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;

public class DesktopListVpcp extends ViewImplBase implements ListVpcpView {
	Presenter presenter;
	private ListDataProvider<VpcpPojo> dataProvider = new ListDataProvider<VpcpPojo>();
	private SingleSelectionModel<VpcpPojo> selectionModel;
	List<VpcpPojo> vpcpList = new java.util.ArrayList<VpcpPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager vpcpListPager;
	@UiField Button generateVpcButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<VpcpPojo> vpcpListTable = new CellTable<VpcpPojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
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
	private static DesktopListVpcpUiBinder uiBinder = GWT.create(DesktopListVpcpUiBinder.class);

	interface DesktopListVpcpUiBinder extends UiBinder<Widget, DesktopListVpcp> {
	}

	public DesktopListVpcp() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);

		generateVpcButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Should go to generate vpcp here...");
				ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPCP);
			}
		}, ClickEvent.getType());
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
			presenter.refreshListWithAllVpcps(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumVpcps(userLoggedIn);
		}
		this.hideFilteredStatus();
	}

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllVpcps(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumVpcps(userLoggedIn);
		}
	}

	@UiHandler("viewAllCB")
	void viewAllCBClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllVpcps(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumVpcps(userLoggedIn);
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
				VpcpPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_VPCP_STATUS, m);
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
		generateVpcButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		generateVpcButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void clearList() {
		vpcpListTable.setVisibleRangeAndClearData(vpcpListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setVpcps(List<VpcpPojo> vpcps) {
		this.vpcpList = vpcps;
		this.initializeVpcpListTable();
		vpcpListPager.setDisplay(vpcpListTable);
	}

	private Widget initializeVpcpListTable() {
		GWT.log("initializing VPCP list table...");
		vpcpListTable.setTableLayoutFixed(false);
		vpcpListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		vpcpListTable.setVisibleRange(0, 20);

		// create dataprovider
		dataProvider = new ListDataProvider<VpcpPojo>();
		dataProvider.addDataDisplay(vpcpListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.vpcpList);

		selectionModel = 
				new SingleSelectionModel<VpcpPojo>(VpcpPojo.KEY_PROVIDER);
		vpcpListTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				VpcpPojo m = selectionModel.getSelectedObject();
				GWT.log("Selected vpcp is: " + m.getProvisioningId());
			}
		});

		ListHandler<VpcpPojo> sortHandler = 
				new ListHandler<VpcpPojo>(dataProvider.getList());
		vpcpListTable.addColumnSortHandler(sortHandler);

		if (vpcpListTable.getColumnCount() == 0) {
			initVpcpListTableColumns(sortHandler);
		}

		return vpcpListTable;
	}

	private void initVpcpListTableColumns(ListHandler<VpcpPojo> sortHandler) {
		GWT.log("initializing VPCP list table columns...");

		Column<VpcpPojo, Boolean> checkColumn = new Column<VpcpPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(VpcpPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		vpcpListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		vpcpListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// create time
		Column<VpcpPojo, String> createTimeColumn = 
				new Column<VpcpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpPojo object) {
				Date createTime = object.getCreateTime();
				return createTime != null ? dateFormat.format(createTime) : "Unknown";
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<VpcpPojo>() {
			public int compare(VpcpPojo o1, VpcpPojo o2) {
				Date c1 = o1.getCreateTime();
				Date c2 = o2.getCreateTime();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
		vpcpListTable.addColumn(createTimeColumn, "Create Time");

		// Provisioning id column
		Column<VpcpPojo, String> provIdColumn = 
				new Column<VpcpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpPojo object) {
				return object.getProvisioningId();
			}
		};
		provIdColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpcpPojo>() {
			public int compare(VpcpPojo o1, VpcpPojo o2) {
				return o1.getProvisioningId().compareTo(o2.getProvisioningId());
			}
		});
		vpcpListTable.addColumn(provIdColumn, "Provisioning ID");

		// Status
		Column<VpcpPojo, String> statusColumn = 
				new Column<VpcpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpPojo object) {
				return object.getStatus();
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpcpPojo>() {
			public int compare(VpcpPojo o1, VpcpPojo o2) {
				return o1.getStatus().compareTo(o2.getStatus());
			}
		});
		vpcpListTable.addColumn(statusColumn, "Status");

		// Provisioning result
		Column<VpcpPojo, String> resultColumn = 
				new Column<VpcpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpPojo object) {
				return object.getProvisioningResult();
			}
		};
		resultColumn.setSortable(true);
		sortHandler.setComparator(resultColumn, new Comparator<VpcpPojo>() {
			public int compare(VpcpPojo o1, VpcpPojo o2) {
				return o1.getProvisioningResult().compareTo(o2.getProvisioningResult());
			}
		});
		vpcpListTable.addColumn(resultColumn, "Provisioning Result");

		// Anticipated time
		Column<VpcpPojo, String> anticipatedTimeColumn = 
				new Column<VpcpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpPojo object) {
				return object.getAnticipatedTime();
			}
		};
		anticipatedTimeColumn.setSortable(true);
		sortHandler.setComparator(anticipatedTimeColumn, new Comparator<VpcpPojo>() {
			public int compare(VpcpPojo o1, VpcpPojo o2) {
				return o1.getAnticipatedTime().compareTo(o2.getAnticipatedTime());
			}
		});
		vpcpListTable.addColumn(anticipatedTimeColumn, "Anticipated Time");

		// Actual time
		Column<VpcpPojo, String> actualTimeColumn = 
				new Column<VpcpPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpPojo object) {
				return object.getActualTime();
			}
		};
		actualTimeColumn.setSortable(true);
		sortHandler.setComparator(actualTimeColumn, new Comparator<VpcpPojo>() {
			public int compare(VpcpPojo o1, VpcpPojo o2) {
				return o1.getActualTime().compareTo(o2.getActualTime());
			}
		});
		vpcpListTable.addColumn(actualTimeColumn, "Actual Time");

		// Provisioning steps progress status
		final SafeHtmlCell stepProgressCell = new SafeHtmlCell();

		Column<VpcpPojo, SafeHtml> stepProgressCol = new Column<VpcpPojo, SafeHtml>(
				stepProgressCell) {

			@Override
			public SafeHtml getValue(VpcpPojo value) {
				SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getTotalStepCount(), value.getCompletedStepCount());
				return sh;
			}
		};		 
		vpcpListTable.addColumn(stepProgressCol, "Progress");
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
	public void removeVpcpFromView(VpcpPojo vpcp) {
		dataProvider.getList().remove(vpcp);
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
		generateVpcButton.setEnabled(true);;
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
		generateVpcButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		generateVpcButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
	}

	@Override
	public boolean viewAllVpcps() {
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
