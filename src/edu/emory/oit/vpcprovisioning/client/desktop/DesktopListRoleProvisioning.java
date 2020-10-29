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
import edu.emory.oit.vpcprovisioning.presenter.role.ListRoleProvisioningView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListRoleProvisioning extends ViewImplBase implements ListRoleProvisioningView {
	Presenter presenter;
	private ListDataProvider<RoleProvisioningSummaryPojo> dataProvider = new ListDataProvider<RoleProvisioningSummaryPojo>();
	private SingleSelectionModel<RoleProvisioningSummaryPojo> selectionModel;
	List<RoleProvisioningSummaryPojo> List = new java.util.ArrayList<RoleProvisioningSummaryPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
//	@UiField Button generateButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<RoleProvisioningSummaryPojo> listTable = new CellTable<RoleProvisioningSummaryPojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
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


	private static DesktopListRoleProvisioningUiBinder uiBinder = GWT.create(DesktopListRoleProvisioningUiBinder.class);

	interface DesktopListRoleProvisioningUiBinder extends UiBinder<Widget, DesktopListRoleProvisioning> {
	}

	public DesktopListRoleProvisioning() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
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
			presenter.refreshListWithAllRoleProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumRoleProvisionings(userLoggedIn);
		}
		this.hideFilteredStatus();
	}

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllRoleProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumRoleProvisionings(userLoggedIn);
		}
	}

	@UiHandler("viewAllCB")
	void viewAllCBClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllRoleProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumRoleProvisionings(userLoggedIn);
		}
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid = new Grid(2, 1);
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
				RoleProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					getAppShell().addBreadCrumb("Show ROLE_PROVISIONING Status", ActionNames.SHOW_ROLE_PROVISIONING_STATUS, m);
					ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_ROLE_PROVISIONING_STATUS, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, assignAnchor);

//		Anchor generateRoleAnchor = new Anchor("Generate Role");
//		generateRoleAnchor.addStyleName("productAnchor");
//		generateRoleAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
//		generateRoleAnchor.setTitle("Generate a new custom Role");
//		generateRoleAnchor.ensureDebugId(generateRoleAnchor.getText());
//		generateRoleAnchor.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				actionsPopup.hide();
//				showMessageToUser("Comming soon...");
////				getAppShell().addBreadCrumb("Show GENERATE_ROLE", ActionNames.GENERATE_ROLE);
////				ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_ROLE, m);
//			}
//		});
//		grid.setWidget(1, 0, generateRoleAnchor);

		Anchor deprovisionAnchor = new Anchor("De-Provisiong Role");
		deprovisionAnchor.addStyleName("productAnchor");
		deprovisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deprovisionAnchor.setTitle("De-Provision selected item");
		deprovisionAnchor.ensureDebugId(deprovisionAnchor.getText());
		deprovisionAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				RoleProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin() || 
						userLoggedIn.isAdminForAccount(m.getProvisioning().getRequisition().getAccountId())) {
						if (!m.isProvision()) {
							showMessageToUser("You cannot de-provision Role that is NOT in a provisioned status.");
							return;
						}
						presenter.deprovisionRole(m.getProvisioning().getRequisition());
					}
					else {
						showMessageToUser("You are not authorized to perform this action.");
					}
				} 
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deprovisionAnchor);
		
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
		actionsButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
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
	public void setRoleProvisioningSummaries(List<RoleProvisioningSummaryPojo> s) {
		this.List = s;
		this.initializeRoleProvisioninglistTable();
		listPager.setDisplay(listTable);
		topListPager.setDisplay(listTable);
	}

	private Widget initializeRoleProvisioninglistTable() {
		GWT.log("initializing VPN Connection Provisioning Summary list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		listTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<RoleProvisioningSummaryPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.List);

		selectionModel = 
				new SingleSelectionModel<RoleProvisioningSummaryPojo>(RoleProvisioningSummaryPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				RoleProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m.isProvision()) {
					RoleProvisioningPojo roleProvisioning = m.getProvisioning();
				}
			}
		});

		ListHandler<RoleProvisioningSummaryPojo> sortHandler = 
				new ListHandler<RoleProvisioningSummaryPojo>(dataProvider.getList());
		listTable.addColumnSortHandler(sortHandler);

		if (listTable.getColumnCount() == 0) {
			initRoleProvisioninglistTableColumns(sortHandler);
		}

		return listTable;
	}

	private void initRoleProvisioninglistTableColumns(ListHandler<RoleProvisioningSummaryPojo> sortHandler) {
		GWT.log("initializing ROLE_PROVISIONING Summary list table columns...");

		Column<RoleProvisioningSummaryPojo, Boolean> checkColumn = new Column<RoleProvisioningSummaryPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(RoleProvisioningSummaryPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// create time
		Column<RoleProvisioningSummaryPojo, String> createTimeColumn = 
				new Column<RoleProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleProvisioningSummaryPojo object) {
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
		sortHandler.setComparator(createTimeColumn, new Comparator<RoleProvisioningSummaryPojo>() {
			public int compare(RoleProvisioningSummaryPojo o1, RoleProvisioningSummaryPojo o2) {
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
		listTable.addColumn(createTimeColumn, "Create Time");

		// Provisioning type column
		Column<RoleProvisioningSummaryPojo, String> provTypeColumn = 
				new Column<RoleProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return Constants.VPN_PROVISIONING;
				}
				else {
					return Constants.VPN_DEPROVISIONING;
				}
			}
		};
		provTypeColumn.setSortable(true);
		sortHandler.setComparator(provTypeColumn, new Comparator<RoleProvisioningSummaryPojo>() {
			public int compare(RoleProvisioningSummaryPojo o1, RoleProvisioningSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return 0;
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return Constants.VPN_PROVISIONING.compareTo(Constants.VPN_DEPROVISIONING);
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return 0;
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return Constants.VPN_DEPROVISIONING.compareTo(Constants.VPN_PROVISIONING);
				}
				else {
					return 0;
				}
			}
		});
		listTable.addColumn(provTypeColumn, "Provisioning Type");

		// Provisioning id column
		Column<RoleProvisioningSummaryPojo, String> provIdColumn = 
				new Column<RoleProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningId();
				}
				else {
					return object.getDeprovisioning().getDeprovisioningId();
				}
			}
		};
		provIdColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<RoleProvisioningSummaryPojo>() {
			public int compare(RoleProvisioningSummaryPojo o1, RoleProvisioningSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getProvisioningId().compareTo(o2.getProvisioning().getProvisioningId());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getProvisioningId().compareTo(o2.getDeprovisioning().getDeprovisioningId());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getDeprovisioningId().compareTo(o2.getDeprovisioning().getDeprovisioningId());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getDeprovisioningId().compareTo(o2.getProvisioning().getProvisioningId());
				}
				else {
					return 0;
				}
			}
		});
		listTable.addColumn(provIdColumn, "Provisioning ID");

		// Status
		Column<RoleProvisioningSummaryPojo, String> statusColumn = 
				new Column<RoleProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getStatus();
				}
				else {
					return object.getDeprovisioning().getStatus();
				}
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<RoleProvisioningSummaryPojo>() {
			public int compare(RoleProvisioningSummaryPojo o1, RoleProvisioningSummaryPojo o2) {
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
		listTable.addColumn(statusColumn, "Status");

		// Provisioning result
		Column<RoleProvisioningSummaryPojo, String> resultColumn = 
				new Column<RoleProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningResult();
				}
				else {
					return object.getDeprovisioning().getDeprovisioningResult();
				}
			}
		};
		resultColumn.setSortable(true);
		sortHandler.setComparator(resultColumn, new Comparator<RoleProvisioningSummaryPojo>() {
			public int compare(RoleProvisioningSummaryPojo o1, RoleProvisioningSummaryPojo o2) {
				if (o1.isProvision() && o2.isProvision()) {
					return o1.getProvisioning().getProvisioningResult().compareTo(o2.getProvisioning().getProvisioningResult());
				}
				else if (o1.isProvision() && !o2.isProvision()) {
					return o1.getProvisioning().getProvisioningResult().compareTo(o2.getDeprovisioning().getDeprovisioningResult());
				}
				else if (!o1.isProvision() && !o2.isProvision()) {
					return o1.getDeprovisioning().getDeprovisioningResult().compareTo(o2.getDeprovisioning().getDeprovisioningResult());
				}
				else if (!o1.isProvision() && o2.isProvision()) {
					return o1.getDeprovisioning().getDeprovisioningResult().compareTo(o2.getProvisioning().getProvisioningResult());
				}
				else {
					return 0;
				}
			}
		});
		listTable.addColumn(resultColumn, "Provisioning Result");

		// Anticipated time
		Column<RoleProvisioningSummaryPojo, String> anticipatedTimeColumn = 
				new Column<RoleProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return formatMillisForDisplay(object.getProvisioning().getAnticipatedTime());
				}
				else {
					return formatMillisForDisplay(object.getDeprovisioning().getAnticipatedTime());
				}
			}
		};
		anticipatedTimeColumn.setSortable(true);
		sortHandler.setComparator(anticipatedTimeColumn, new Comparator<RoleProvisioningSummaryPojo>() {
			public int compare(RoleProvisioningSummaryPojo o1, RoleProvisioningSummaryPojo o2) {
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
		listTable.addColumn(anticipatedTimeColumn, "Anticipated Time");

		// Actual time
		Column<RoleProvisioningSummaryPojo, String> actualTimeColumn = 
				new Column<RoleProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return formatMillisForDisplay(object.getProvisioning().getActualTime());
				}
				else {
					return formatMillisForDisplay(object.getDeprovisioning().getActualTime());
				}
			}
		};
		actualTimeColumn.setSortable(true);
		sortHandler.setComparator(actualTimeColumn, new Comparator<RoleProvisioningSummaryPojo>() {
			public int compare(RoleProvisioningSummaryPojo o1, RoleProvisioningSummaryPojo o2) {
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
		listTable.addColumn(actualTimeColumn, "Actual Time");

		// Provisioning steps progress status
		final SafeHtmlCell stepProgressCell = new SafeHtmlCell();

		Column<RoleProvisioningSummaryPojo, SafeHtml> stepProgressCol = new Column<RoleProvisioningSummaryPojo, SafeHtml>(
				stepProgressCell) {

			@Override
			public SafeHtml getValue(RoleProvisioningSummaryPojo value) {
				if (value.isProvision()) {
					SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getProvisioning().getTotalStepCount(), value.getProvisioning().getCompletedSuccessfullCount());
					return sh;
				}
				else {
					SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getDeprovisioning().getTotalStepCount(), value.getDeprovisioning().getCompletedSuccessfulCount());
					return sh;
				}
			}
		};		 
		listTable.addColumn(stepProgressCol, "Progress");
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
	public void removeRoleProvisioningFromView(RoleProvisioningSummaryPojo vpncp ) {
		dataProvider.getList().remove(vpncp);
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
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
		actionsButton.setEnabled(true);
	}

	@Override
	public boolean viewAllRoleProvisionings() {
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
