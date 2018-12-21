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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
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
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;

public class DesktopListVpnConnectionProvisioning extends ViewImplBase implements ListVpnConnectionProvisioningView {
	Presenter presenter;
	private ListDataProvider<VpnConnectionProvisioningSummaryPojo> dataProvider = new ListDataProvider<VpnConnectionProvisioningSummaryPojo>();
	private SingleSelectionModel<VpnConnectionProvisioningSummaryPojo> selectionModel;
	List<VpnConnectionProvisioningSummaryPojo> List = new java.util.ArrayList<VpnConnectionProvisioningSummaryPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
//	@UiField Button generateButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<VpnConnectionProvisioningSummaryPojo> listTable = new CellTable<VpnConnectionProvisioningSummaryPojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
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
				VpnConnectionProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_VPNCP_STATUS, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, assignAnchor);

//		Anchor deprovisionAnchor = new Anchor("De-Provisiong VPN Connection");
//		deprovisionAnchor.addStyleName("productAnchor");
//		deprovisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
//		deprovisionAnchor.setTitle("De-Provision selected VPN Connection");
//		deprovisionAnchor.ensureDebugId(deprovisionAnchor.getText());
//		deprovisionAnchor.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				actionsPopup.hide();
//				VpnConnectionProvisioningSummaryPojo m = selectionModel.getSelectedObject();
//				if (m != null) {
//					if (userLoggedIn.isNetworkAdmin()) {
//						if (!m.isProvision()) {
//							showMessageToUser("You cannot de-provision a VPN that is NOT in a provisioned status.");
//							return;
//						}
//						presenter.deprovisionVpnConnection(m.getProvisioning());
//					}
//					else {
//						showMessageToUser("You are not authorized to perform this action.");
//					}
//				} 
//				else {
//					showMessageToUser("Please select an item from the list");
//				}
//			}
//		});
//		grid.setWidget(1, 0, deprovisionAnchor);
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
	public void setVpnConnectionProvisioningSummaries(List<VpnConnectionProvisioningSummaryPojo> s) {
		this.List = s;
		this.initializeVpnConnectionProvisioninglistTable();
		listPager.setDisplay(listTable);
	}

	private Widget initializeVpnConnectionProvisioninglistTable() {
		GWT.log("initializing VPN Connection Provisioning Summary list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		listTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<VpnConnectionProvisioningSummaryPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.List);

		selectionModel = 
				new SingleSelectionModel<VpnConnectionProvisioningSummaryPojo>(VpnConnectionProvisioningSummaryPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				VpnConnectionProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m.isProvision()) {
					VpnConnectionProvisioningPojo vpncp = m.getProvisioning();
					GWT.log("VPN Provisioning info:  " + 
						"VPC Id: " + vpncp.getRequisition().getOwnerId() + " " +
						"Profile Id: " + vpncp.getRequisition().getProfile().getVpnConnectionProfileId() + " " +
						"VPC Network: " + vpncp.getRequisition().getProfile().getVpcNetwork());
				}
			}
		});

		ListHandler<VpnConnectionProvisioningSummaryPojo> sortHandler = 
				new ListHandler<VpnConnectionProvisioningSummaryPojo>(dataProvider.getList());
		listTable.addColumnSortHandler(sortHandler);

		if (listTable.getColumnCount() == 0) {
			initVpnConnectionProvisioninglistTableColumns(sortHandler);
		}

		return listTable;
	}

	private void initVpnConnectionProvisioninglistTableColumns(ListHandler<VpnConnectionProvisioningSummaryPojo> sortHandler) {
		GWT.log("initializing VPNCP Summary list table columns...");

		Column<VpnConnectionProvisioningSummaryPojo, Boolean> checkColumn = new Column<VpnConnectionProvisioningSummaryPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(VpnConnectionProvisioningSummaryPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// create time
		Column<VpnConnectionProvisioningSummaryPojo, String> createTimeColumn = 
				new Column<VpnConnectionProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpnConnectionProvisioningSummaryPojo object) {
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
		sortHandler.setComparator(createTimeColumn, new Comparator<VpnConnectionProvisioningSummaryPojo>() {
			public int compare(VpnConnectionProvisioningSummaryPojo o1, VpnConnectionProvisioningSummaryPojo o2) {
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
		Column<VpnConnectionProvisioningSummaryPojo, String> provTypeColumn = 
				new Column<VpnConnectionProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpnConnectionProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return Constants.VPN_PROVISIONING;
				}
				else {
					return Constants.VPN_DEPROVISIONING;
				}
			}
		};
		provTypeColumn.setSortable(true);
		sortHandler.setComparator(provTypeColumn, new Comparator<VpnConnectionProvisioningSummaryPojo>() {
			public int compare(VpnConnectionProvisioningSummaryPojo o1, VpnConnectionProvisioningSummaryPojo o2) {
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
		Column<VpnConnectionProvisioningSummaryPojo, String> provIdColumn = 
				new Column<VpnConnectionProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpnConnectionProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningId();
				}
				else {
					return object.getDeprovisioning().getProvisioningId();
				}
			}
		};
		provIdColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpnConnectionProvisioningSummaryPojo>() {
			public int compare(VpnConnectionProvisioningSummaryPojo o1, VpnConnectionProvisioningSummaryPojo o2) {
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
		listTable.addColumn(provIdColumn, "Provisioning ID");

		// Status
		Column<VpnConnectionProvisioningSummaryPojo, String> statusColumn = 
				new Column<VpnConnectionProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpnConnectionProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getStatus();
				}
				else {
					return object.getDeprovisioning().getStatus();
				}
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<VpnConnectionProvisioningSummaryPojo>() {
			public int compare(VpnConnectionProvisioningSummaryPojo o1, VpnConnectionProvisioningSummaryPojo o2) {
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
		Column<VpnConnectionProvisioningSummaryPojo, String> resultColumn = 
				new Column<VpnConnectionProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpnConnectionProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningResult();
				}
				else {
					return object.getDeprovisioning().getProvisioningResult();
				}
			}
		};
		resultColumn.setSortable(true);
		sortHandler.setComparator(resultColumn, new Comparator<VpnConnectionProvisioningSummaryPojo>() {
			public int compare(VpnConnectionProvisioningSummaryPojo o1, VpnConnectionProvisioningSummaryPojo o2) {
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
		listTable.addColumn(resultColumn, "Provisioning Result");

		// Anticipated time
		Column<VpnConnectionProvisioningSummaryPojo, String> anticipatedTimeColumn = 
				new Column<VpnConnectionProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpnConnectionProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getAnticipatedTime();
				}
				else {
					return object.getDeprovisioning().getAnticipatedTime();
				}
			}
		};
		anticipatedTimeColumn.setSortable(true);
		sortHandler.setComparator(anticipatedTimeColumn, new Comparator<VpnConnectionProvisioningSummaryPojo>() {
			public int compare(VpnConnectionProvisioningSummaryPojo o1, VpnConnectionProvisioningSummaryPojo o2) {
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
		Column<VpnConnectionProvisioningSummaryPojo, String> actualTimeColumn = 
				new Column<VpnConnectionProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(VpnConnectionProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getActualTime();
				}
				else {
					return object.getDeprovisioning().getActualTime();
				}
			}
		};
		actualTimeColumn.setSortable(true);
		sortHandler.setComparator(actualTimeColumn, new Comparator<VpnConnectionProvisioningSummaryPojo>() {
			public int compare(VpnConnectionProvisioningSummaryPojo o1, VpnConnectionProvisioningSummaryPojo o2) {
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

		Column<VpnConnectionProvisioningSummaryPojo, SafeHtml> stepProgressCol = new Column<VpnConnectionProvisioningSummaryPojo, SafeHtml>(
				stepProgressCell) {

			@Override
			public SafeHtml getValue(VpnConnectionProvisioningSummaryPojo value) {
				if (value.isProvision()) {
					SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getProvisioning().getTotalStepCount(), value.getProvisioning().getCompletedStepCount());
					return sh;
				}
				else {
					SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(value.getDeprovisioning().getTotalStepCount(), value.getDeprovisioning().getCompletedStepCount());
					return sh;
				}
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
	public void removeVpnConnectionProvisioningFromView(VpnConnectionProvisioningPojo vpncp ) {
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
		actionsButton.setEnabled(true);
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
