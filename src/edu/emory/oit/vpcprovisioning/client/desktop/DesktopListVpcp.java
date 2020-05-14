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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
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
import edu.emory.oit.vpcprovisioning.shared.VpcpSummaryPojo;

public class DesktopListVpcp extends ViewImplBase implements ListVpcpView {
	Presenter presenter;
	private ListDataProvider<VpcpSummaryPojo> dataProvider = new ListDataProvider<VpcpSummaryPojo>();
	private SingleSelectionModel<VpcpSummaryPojo> selectionModel;
	List<VpcpSummaryPojo> summaryList = new java.util.ArrayList<VpcpSummaryPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager vpcpListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button generateVpcButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<VpcpSummaryPojo> vpcpListTable = new CellTable<VpcpSummaryPojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
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
				hidePleaseWaitDialog();
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
				VpcpSummaryPojo m = selectionModel.getSelectedObject();
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
	public void setVpcpSummaries(List<VpcpSummaryPojo> summaries) {
		this.summaryList = summaries;
		this.initializeVpcpListTable();
		vpcpListPager.setDisplay(vpcpListTable);
		topListPager.setDisplay(vpcpListTable);
	}

	private Widget initializeVpcpListTable() {
		GWT.log("initializing VPCP list table...");
		vpcpListTable.setTableLayoutFixed(false);
		vpcpListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		vpcpListTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<VpcpSummaryPojo>();
		dataProvider.addDataDisplay(vpcpListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.summaryList);

		selectionModel = 
				new SingleSelectionModel<VpcpSummaryPojo>(VpcpSummaryPojo.KEY_PROVIDER);
		vpcpListTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				VpcpSummaryPojo m = selectionModel.getSelectedObject();
				if (m.getProvisioning() != null) {
					GWT.log("Selected vpcp is: " + m.getProvisioning().getProvisioningId());
				}
				else if (m.getDeprovisioning() != null) {
					GWT.log("Selected vpcd is: " + m.getDeprovisioning().getProvisioningId());
				}
			}
		});

		ListHandler<VpcpSummaryPojo> sortHandler = 
				new ListHandler<VpcpSummaryPojo>(dataProvider.getList());
		vpcpListTable.addColumnSortHandler(sortHandler);

		if (vpcpListTable.getColumnCount() == 0) {
			initVpcpListTableColumns(sortHandler);
		}

		return vpcpListTable;
	}

	private void initVpcpListTableColumns(ListHandler<VpcpSummaryPojo> sortHandler) {
		GWT.log("initializing VPCP list table columns...");

		Column<VpcpSummaryPojo, Boolean> checkColumn = new Column<VpcpSummaryPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(VpcpSummaryPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		vpcpListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		vpcpListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// create time
		Column<VpcpSummaryPojo, String> createTimeColumn = 
				new Column<VpcpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpSummaryPojo object) {
				if (object.isProvision()) {
					Date createTime = object.getProvisioning().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
				else {
					Date createTime = object.getDeprovisioning().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<VpcpSummaryPojo>() {
			public int compare(VpcpSummaryPojo o1, VpcpSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					Date c1 = o1.getProvisioning().getCreateTime();
					Date c2 = o2.getProvisioning().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					Date c1 = o1.getProvisioning().getCreateTime();
					Date c2 = o2.getDeprovisioning().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					Date c1 = o1.getDeprovisioning().getCreateTime();
					Date c2 = o2.getDeprovisioning().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					Date c1 = o1.getDeprovisioning().getCreateTime();
					Date c2 = o2.getProvisioning().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
				else {
					return 0;
				}
			}
		});
		vpcpListTable.addColumn(createTimeColumn, "Create Time");
		
		// TODO: provisioning type column

		// Provisioning id column
		Column<VpcpSummaryPojo, String> provIdColumn = 
				new Column<VpcpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningId();
				}
				else {
					return object.getDeprovisioning().getProvisioningId();
				}
			}
		};
		provIdColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpcpSummaryPojo>() {
			public int compare(VpcpSummaryPojo o1, VpcpSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getProvisioningId().compareTo(o2.getProvisioning().getProvisioningId());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getProvisioningId().compareTo(o2.getDeprovisioning().getProvisioningId());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getProvisioningId().compareTo(o2.getDeprovisioning().getProvisioningId());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getProvisioningId().compareTo(o2.getProvisioning().getProvisioningId());
				}
				else {
					return 0;
				}
			}
		});
		vpcpListTable.addColumn(provIdColumn, "Provisioning ID");

		// Status
		Column<VpcpSummaryPojo, String> statusColumn = 
				new Column<VpcpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getStatus();
				}
				else {
					return object.getDeprovisioning().getStatus();
				}
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpcpSummaryPojo>() {
			public int compare(VpcpSummaryPojo o1, VpcpSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getStatus().compareTo(o2.getProvisioning().getStatus());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getStatus().compareTo(o2.getDeprovisioning().getStatus());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getStatus().compareTo(o2.getDeprovisioning().getStatus());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getStatus().compareTo(o2.getProvisioning().getStatus());
				}
				else {
					return 0;
				}
			}
		});
		vpcpListTable.addColumn(statusColumn, "Status");

		// VPC region column
		Column<VpcpSummaryPojo, String> regionColumn = 
			new Column<VpcpSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(VpcpSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getVpcRequisition().getRegion();
				}
				else {
					return object.getDeprovisioning().getVpcRequisition().getRegion();
				}
			}
		};
		regionColumn.setSortable(true);
		sortHandler.setComparator(regionColumn, new Comparator<VpcpSummaryPojo>() {
			public int compare(VpcpSummaryPojo o1, VpcpSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getVpcRequisition().getRegion().compareTo(o2.getProvisioning().getVpcRequisition().getRegion());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getVpcRequisition().getRegion().compareTo(o2.getDeprovisioning().getVpcRequisition().getRegion());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getVpcRequisition().getRegion().compareTo(o2.getDeprovisioning().getVpcRequisition().getRegion());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getVpcRequisition().getRegion().compareTo(o2.getProvisioning().getVpcRequisition().getRegion());
				}
				else {
					return 0;
				}
			}
		});
		vpcpListTable.addColumn(regionColumn, "Region");
		
		// Provisioning result
		Column<VpcpSummaryPojo, String> resultColumn = 
				new Column<VpcpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningResult();
				}
				else {
					return object.getDeprovisioning().getProvisioningResult();
				}
			}
		};
		resultColumn.setSortable(true);
		sortHandler.setComparator(resultColumn, new Comparator<VpcpSummaryPojo>() {
			public int compare(VpcpSummaryPojo o1, VpcpSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getProvisioningResult().compareTo(o2.getProvisioning().getProvisioningResult());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getProvisioningResult().compareTo(o2.getDeprovisioning().getProvisioningResult());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getProvisioningResult().compareTo(o2.getDeprovisioning().getProvisioningResult());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getProvisioningResult().compareTo(o2.getProvisioning().getProvisioningResult());
				}
				else {
					return 0;
				}
			}
		});
		vpcpListTable.addColumn(resultColumn, "Provisioning Result");

		// Anticipated time
		Column<VpcpSummaryPojo, String> anticipatedTimeColumn = 
				new Column<VpcpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpSummaryPojo object) {
				if (object.isProvision()) {
					return formatMillisForDisplay(object.getProvisioning().getAnticipatedTime());
				}
				else {
					return formatMillisForDisplay(object.getDeprovisioning().getAnticipatedTime());
				}
			}
		};
		anticipatedTimeColumn.setSortable(true);
		sortHandler.setComparator(anticipatedTimeColumn, new Comparator<VpcpSummaryPojo>() {
			public int compare(VpcpSummaryPojo o1, VpcpSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getAnticipatedTime().compareTo(o2.getProvisioning().getAnticipatedTime());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getAnticipatedTime().compareTo(o2.getDeprovisioning().getAnticipatedTime());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getAnticipatedTime().compareTo(o2.getDeprovisioning().getAnticipatedTime());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getAnticipatedTime().compareTo(o2.getProvisioning().getAnticipatedTime());
				}
				else {
					return 0;
				}
			}
		});
		vpcpListTable.addColumn(anticipatedTimeColumn, "Anticipated Time");

		// Actual time
		Column<VpcpSummaryPojo, String> actualTimeColumn = 
				new Column<VpcpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpcpSummaryPojo object) {
				if (object.isProvision()) {
					return formatMillisForDisplay(object.getProvisioning().getActualTime());
				}
				else {
					return formatMillisForDisplay(object.getDeprovisioning().getActualTime());
				}
			}
		};
		actualTimeColumn.setSortable(true);
		sortHandler.setComparator(actualTimeColumn, new Comparator<VpcpSummaryPojo>() {
			public int compare(VpcpSummaryPojo o1, VpcpSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getActualTime().compareTo(o2.getProvisioning().getActualTime());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getActualTime().compareTo(o2.getDeprovisioning().getActualTime());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getActualTime().compareTo(o2.getDeprovisioning().getActualTime());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getActualTime().compareTo(o2.getProvisioning().getActualTime());
				}
				else {
					return 0;
				}
			}
		});
		vpcpListTable.addColumn(actualTimeColumn, "Actual Time");

		// Provisioning steps progress status
		final SafeHtmlCell stepProgressCell = new SafeHtmlCell();

		Column<VpcpSummaryPojo, SafeHtml> stepProgressCol = new Column<VpcpSummaryPojo, SafeHtml>(
				stepProgressCell) {

			@Override
			public SafeHtml getValue(VpcpSummaryPojo value) {
				if (value.isProvision()) {
					SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getProvisioning().getTotalStepCount(), value.getProvisioning().getCompletedSuccessfulCount());
					return sh;
				}
				else {
					SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getDeprovisioning().getTotalStepCount(), value.getDeprovisioning().getCompletedSuccessfulCount());
					return sh;
				}
			}
		};		 
		vpcpListTable.addColumn(stepProgressCol, "Progress");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		

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
	public void removeVpcpSummaryFromView(VpcpSummaryPojo summary) {
		dataProvider.getList().remove(summary);
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
	public void applyCentralAdminMask() {
		generateVpcButton.setEnabled(true);;
		actionsButton.setEnabled(true);;
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
