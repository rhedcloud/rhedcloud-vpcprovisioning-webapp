package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.common.AwsAccountRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.AwsAccountSuggestion;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.ui.HTMLUtils;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.ListAccountProvisioningView;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListAccountProvisioning extends ViewImplBase implements ListAccountProvisioningView {
	Presenter presenter;
	private ListDataProvider<AccountProvisioningSummaryPojo> dataProvider = new ListDataProvider<AccountProvisioningSummaryPojo>();
	private SingleSelectionModel<AccountProvisioningSummaryPojo> selectionModel;
	List<AccountProvisioningSummaryPojo> provisioningSummaries = new java.util.ArrayList<AccountProvisioningSummaryPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);
	DialogBox accountSelectionDialog = new DialogBox();
	AwsAccountRpcSuggestOracle accountSuggestions;
	AccountPojo selectedAccount;

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button generateButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<AccountProvisioningSummaryPojo> listTable = new CellTable<AccountProvisioningSummaryPojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
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

	private static DesktopListAccountProvisioningUiBinder uiBinder = GWT
			.create(DesktopListAccountProvisioningUiBinder.class);

	interface DesktopListAccountProvisioningUiBinder extends UiBinder<Widget, DesktopListAccountProvisioning> {
	}

	public DesktopListAccountProvisioning() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}
	
	@UiHandler("generateButton")
	void generateButtonClicked(ClickEvent e) {
		// TODO: popup dialog that allows them to select one or more accounts to deprovision.
		presenter.getAccountList();
	}
	@UiHandler ("filterTB")
	void addEmailTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		presenter.filterByDeprovisioningId(true, filterTB.getText());
        }
	}
	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		presenter.filterByDeprovisioningId(true, filterTB.getText());
	}
	
	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllAccountProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumAccountProvisionings(userLoggedIn);
		}
		this.hideFilteredStatus();
	}

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllAccountProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumAccountProvisionings(userLoggedIn);
		}
	}

	@UiHandler("viewAllCB")
	void viewAllCBClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewAllCB.getValue()) {
			presenter.refreshListWithAllAccountProvisionings(userLoggedIn);
		}
		else {
			presenter.refreshListWithMaximumAccountProvisionings(userLoggedIn);
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

		Anchor viewAnchor = new Anchor("View Status");
		viewAnchor.addStyleName("productAnchor");
		viewAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		viewAnchor.setTitle("View status of selected Account Deprovisioning object");
		viewAnchor.ensureDebugId(viewAnchor.getText());
		viewAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_ACCOUNT_PROVISIONING_STATUS, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, viewAnchor);
		
		actionsPopup.showRelativeTo(actionsButton);
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
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		return actionsButton;
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyCentralAdminMask() {
		generateButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		generateButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		generateButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public java.util.List<Widget> getMissingRequiredFields() {
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
	public void disableButtons() {
		generateButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		generateButton.setEnabled(true);
		actionsButton.setEnabled(true);
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
	public void setAccountProvisioningSummaries(java.util.List<AccountProvisioningSummaryPojo> summaries) {
		this.provisioningSummaries = summaries;
		this.initializeListTable();
		listPager.setDisplay(listTable);
		topListPager.setDisplay(listTable);
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAccountProvisioningFromView(AccountProvisioningSummaryPojo summary) {
		dataProvider.getList().remove(summary);
	}

	@Override
	public boolean viewAllAccountProvisionings() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initPage() {
		accountSuggestions = new AwsAccountRpcSuggestOracle(userLoggedIn, Constants.SUGGESTION_TYPE_CONSOLE_FEATURE);
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

	private Widget initializeListTable() {
		GWT.log("initializing Account Provisioning Summary list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		listTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<AccountProvisioningSummaryPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.provisioningSummaries);

		selectionModel = 
				new SingleSelectionModel<AccountProvisioningSummaryPojo>(AccountProvisioningSummaryPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				AccountProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m.isProvision()) {
					AccountProvisioningPojo vpncp = m.getProvisioning();
//					GWT.log("VPN Provisioning info:  " + 
//						"VPC Id: " + vpncp.getRequisition().getOwnerId() + " " +
//						"Profile Id: " + vpncp.getRequisition().getProfile().getVpnConnectionProfileId() + " " +
//						"VPC Network: " + vpncp.getRequisition().getProfile().getVpcNetwork());
				}
			}
		});

		ListHandler<AccountProvisioningSummaryPojo> sortHandler = 
				new ListHandler<AccountProvisioningSummaryPojo>(dataProvider.getList());
		listTable.addColumnSortHandler(sortHandler);

		if (listTable.getColumnCount() == 0) {
			initListTableColumns(sortHandler);
		}

		return listTable;
	}

	private void initListTableColumns(ListHandler<AccountProvisioningSummaryPojo> sortHandler) {
		GWT.log("initializing Account provisioning Summary list table columns...");

		Column<AccountProvisioningSummaryPojo, Boolean> checkColumn = new Column<AccountProvisioningSummaryPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(AccountProvisioningSummaryPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// create time
		Column<AccountProvisioningSummaryPojo, String> createTimeColumn = 
				new Column<AccountProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountProvisioningSummaryPojo object) {
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
		sortHandler.setComparator(createTimeColumn, new Comparator<AccountProvisioningSummaryPojo>() {
			public int compare(AccountProvisioningSummaryPojo o1, AccountProvisioningSummaryPojo o2) {
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
		Column<AccountProvisioningSummaryPojo, String> provTypeColumn = 
				new Column<AccountProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return Constants.VPN_PROVISIONING;
				}
				else {
					return Constants.VPN_DEPROVISIONING;
				}
			}
		};
		provTypeColumn.setSortable(true);
		sortHandler.setComparator(provTypeColumn, new Comparator<AccountProvisioningSummaryPojo>() {
			public int compare(AccountProvisioningSummaryPojo o1, AccountProvisioningSummaryPojo o2) {
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
		Column<AccountProvisioningSummaryPojo, String> provIdColumn = 
				new Column<AccountProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningId();
				}
				else {
					return object.getDeprovisioning().getDeprovisioningId();
				}
			}
		};
		provIdColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<AccountProvisioningSummaryPojo>() {
			public int compare(AccountProvisioningSummaryPojo o1, AccountProvisioningSummaryPojo o2) {
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
		Column<AccountProvisioningSummaryPojo, String> statusColumn = 
				new Column<AccountProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getStatus();
				}
				else {
					return object.getDeprovisioning().getStatus();
				}
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(provIdColumn, new Comparator<AccountProvisioningSummaryPojo>() {
			public int compare(AccountProvisioningSummaryPojo o1, AccountProvisioningSummaryPojo o2) {
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
		Column<AccountProvisioningSummaryPojo, String> resultColumn = 
				new Column<AccountProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return object.getProvisioning().getProvisioningResult();
				}
				else {
					return object.getDeprovisioning().getDeprovisioningResult();
				}
			}
		};
		resultColumn.setSortable(true);
		sortHandler.setComparator(resultColumn, new Comparator<AccountProvisioningSummaryPojo>() {
			public int compare(AccountProvisioningSummaryPojo o1, AccountProvisioningSummaryPojo o2) {
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
		Column<AccountProvisioningSummaryPojo, String> anticipatedTimeColumn = 
				new Column<AccountProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return formatMillisForDisplay(object.getProvisioning().getAnticipatedTime());
				}
				else {
					return formatMillisForDisplay(object.getDeprovisioning().getAnticipatedTime());
				}
			}
		};
		anticipatedTimeColumn.setSortable(true);
		sortHandler.setComparator(anticipatedTimeColumn, new Comparator<AccountProvisioningSummaryPojo>() {
			public int compare(AccountProvisioningSummaryPojo o1, AccountProvisioningSummaryPojo o2) {
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
		Column<AccountProvisioningSummaryPojo, String> actualTimeColumn = 
				new Column<AccountProvisioningSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountProvisioningSummaryPojo object) {
				if (object.isProvision()) {
					return formatMillisForDisplay(object.getProvisioning().getActualTime());
				}
				else {
					return formatMillisForDisplay(object.getDeprovisioning().getActualTime());
				}
			}
		};
		actualTimeColumn.setSortable(true);
		sortHandler.setComparator(actualTimeColumn, new Comparator<AccountProvisioningSummaryPojo>() {
			public int compare(AccountProvisioningSummaryPojo o1, AccountProvisioningSummaryPojo o2) {
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

		Column<AccountProvisioningSummaryPojo, SafeHtml> stepProgressCol = new Column<AccountProvisioningSummaryPojo, SafeHtml>(
				stepProgressCell) {

			@Override
			public SafeHtml getValue(AccountProvisioningSummaryPojo value) {
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
		listTable.addColumn(stepProgressCol, "Progress");
	}

	@Override
	public void showAccountSelectionList(final List<AccountPojo> accounts) {
		// TODO: show a popup that allows them to select one (or more?) accounts to deprovision
		accountSelectionDialog.clear();
		accountSelectionDialog.setText("Select Account(s) to Deprovision");
		accountSelectionDialog.setGlassEnabled(true);
		accountSelectionDialog.setAnimationEnabled(true);
		accountSelectionDialog.center();
		accountSelectionDialog.getElement().getStyle().setBackgroundColor("#f1f1f1");

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(8);
		accountSelectionDialog.setWidget(vp);
		
		Grid grid;
		grid = new Grid(1,2);

		grid.setCellSpacing(8);
		vp.add(grid);
		
		Label l_accts = new Label("Select Account(s):");
		l_accts.addStyleName("label");
		l_accts.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		l_accts.getElement().getStyle().setFontSize(14, Unit.PX);
		l_accts.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
		grid.setWidget(0, 0, l_accts);

//		final ListBox accts_lb = new ListBox();
//		accts_lb.setMultipleSelect(false);
//		accts_lb.addStyleName("listBoxField");
//		accts_lb.addStyleName("glowing-border");
//		accts_lb.getElement().getStyle().setWidth(300, Unit.PX);
//		if (!accts_lb.isMultipleSelect()) {
//			accts_lb.addItem("-- Select --", "");
//		}
//		else {
//			accts_lb.setVisibleItemCount(25);
//		}
//		for (AccountPojo acct : accounts) {
//			accts_lb.addItem(acct.getAccountId() + " - " + acct.getAccountName(), acct.getAccountId());
//		}
//		grid.setWidget(0, 1, accts_lb);
		
		final SuggestBox accountSB = new SuggestBox(accountSuggestions, new TextBox());
		accountSB.setText("");
		accountSB.getElement().setPropertyString("placeholder", "Enter Account ID, Name or Alternate Name (case insensitive)");
		accountSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				AwsAccountSuggestion suggestion = (AwsAccountSuggestion)event.getSelectedItem();
				if (suggestion.getAccount() != null) {
					selectedAccount = suggestion.getAccount();
				}
			}
		});

		accountSB.addStyleName("longField");
		accountSB.addStyleName("glowing-border");
		grid.setWidget(0, 1, accountSB);


		Grid buttonGrid;
		buttonGrid = new Grid(1,2);
		buttonGrid.setCellSpacing(20);
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
				// TODO: get the account and confirm they want to deprovision etc.
				accountSelectionDialog.hide();
//				String acct_id = accts_lb.getSelectedValue();
				AccountDeprovisioningRequisitionPojo req = new AccountDeprovisioningRequisitionPojo();
				req.setAccountId(selectedAccount.getAccountId());
				req.setFromProvisioningList(true);
//				req.setAccountId(acct_id);
//				AccountPojo account = getAccountForId(selectedAccountId, accounts);
				ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_ACCOUNT_DEPROVISIONING_CONFIRMATION, req, selectedAccount);
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
				accountSelectionDialog.hide();
			}
		});
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	accountSB.setFocus(true);
	        }
	    });

		accountSelectionDialog.show();
		accountSelectionDialog.center();
	}
	
	private AccountPojo getAccountForId(String acctId, List<AccountPojo> accounts) {
		for (AccountPojo account : accounts) {
			if (account.getAccountId().equalsIgnoreCase(acctId)) {
				return account;
			}
		}
		return null;
	}
}
