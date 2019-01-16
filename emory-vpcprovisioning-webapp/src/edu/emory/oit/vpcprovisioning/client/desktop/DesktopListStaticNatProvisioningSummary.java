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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.ui.HTMLUtils;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.ListStaticNatProvisioningSummaryView;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListStaticNatProvisioningSummary extends ViewImplBase implements ListStaticNatProvisioningSummaryView {
	Presenter presenter;
	private ListDataProvider<StaticNatProvisioningSummaryPojo> dataProvider = new ListDataProvider<StaticNatProvisioningSummaryPojo>();
	private SingleSelectionModel<StaticNatProvisioningSummaryPojo> selectionModel;
	List<StaticNatProvisioningSummaryPojo> summaryList = new java.util.ArrayList<StaticNatProvisioningSummaryPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<StaticNatProvisioningSummaryPojo> listTable = new CellTable<StaticNatProvisioningSummaryPojo>(20, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML pleaseWaitHTML;
	@UiField PushButton refreshButton;

	public interface MyCellTableResources extends CellTable.Resources {
		@Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}
	
	private static DesktopListStaticNatProvisioningSummaryUiBinder uiBinder = GWT
			.create(DesktopListStaticNatProvisioningSummaryUiBinder.class);

	interface DesktopListStaticNatProvisioningSummaryUiBinder
			extends UiBinder<Widget, DesktopListStaticNatProvisioningSummary> {
	}

	public DesktopListStaticNatProvisioningSummary() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
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
		assignAnchor.setTitle("View status of selected row");
		assignAnchor.ensureDebugId(assignAnchor.getText());
		assignAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				StaticNatProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (m.getProvisioned() != null) {
						GWT.log("Firing SHOW_STATIC_NAT_STATUS for a StaticNatProvisioning object.");
						ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_STATIC_NAT_STATUS, m);
					}
					else if (m.getDeprovisioned() != null) {
						GWT.log("Firing SHOW_STATIC_NAT_STATUS for a StaticNatDeprovisioning object.");
						ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_STATIC_NAT_STATUS, m);
					}
					else {
						showMessageToUser("There appears to be a data issue with the selected "
							+ "object.  No Static NAT objects were found.");
					}
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
	public void hidePleaseWaitPanel() {
		this.pleaseWaitPanel.setVisible(false);
	}

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
		
		
	}

	@Override
	public Widget getStatusMessageSource() {
		return actionsButton;
	}

	@Override
	public void applyCentralAdminMask() {
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		actionsButton.setEnabled(true);
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
	public void clearList() {
		
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setProvisioningSummaries(List<StaticNatProvisioningSummaryPojo> summaries) {
		this.summaryList = summaries;
		this.initializeListTable();
		listPager.setDisplay(listTable);
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

	@Override
	public void removeStaticNatProvisioningSummaryFromView(StaticNatProvisioningSummaryPojo summary) {
		dataProvider.getList().remove(summary);
	}
	
	private Widget initializeListTable() {
		GWT.log("initializing Static Nat Provisioning Summary table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		listTable.setVisibleRange(0, 20);

		// create dataprovider
		dataProvider = new ListDataProvider<StaticNatProvisioningSummaryPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.summaryList);

		selectionModel = 
				new SingleSelectionModel<StaticNatProvisioningSummaryPojo>(StaticNatProvisioningSummaryPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				StaticNatProvisioningSummaryPojo m = selectionModel.getSelectedObject();
				GWT.log("The summary is a " + (m.getProvisioned() == null ? "De-Provision" : "Provision") + " Summary");
			}
		});

		ListHandler<StaticNatProvisioningSummaryPojo> sortHandler = 
				new ListHandler<StaticNatProvisioningSummaryPojo>(dataProvider.getList());
		listTable.addColumnSortHandler(sortHandler);

		if (listTable.getColumnCount() == 0) {
			initListTableColumns(sortHandler);
		}

		return listTable;
	}

	private void initListTableColumns(ListHandler<StaticNatProvisioningSummaryPojo> sortHandler) {
			GWT.log("initializing Static Nat Summary table columns...");

			Column<StaticNatProvisioningSummaryPojo, Boolean> checkColumn = new Column<StaticNatProvisioningSummaryPojo, Boolean>(
					new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(StaticNatProvisioningSummaryPojo object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};
			listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
			listTable.setColumnWidth(checkColumn, 40, Unit.PX);

			// create time
			Column<StaticNatProvisioningSummaryPojo, String> createTimeColumn = 
					new Column<StaticNatProvisioningSummaryPojo, String> (new TextCell()) {

				@Override
				public String getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						Date createTime = object.getProvisioned().getCreateTime();
						return createTime != null ? dateFormat.format(createTime) : "Unknown";
					}
					else {
						Date createTime = object.getDeprovisioned().getCreateTime();
						return createTime != null ? dateFormat.format(createTime) : "Unknown";
					}
						
				}
			};
			createTimeColumn.setSortable(true);
			sortHandler.setComparator(createTimeColumn, new Comparator<StaticNatProvisioningSummaryPojo>() {
				public int compare(StaticNatProvisioningSummaryPojo o1, StaticNatProvisioningSummaryPojo o2) {
					if (o1.getProvisioned() != null && o2.getProvisioned() != null) {
						Date c1 = o1.getProvisioned().getCreateTime();
						Date c2 = o2.getProvisioned().getCreateTime();
						if (c1 == null || c2 == null) {
							return 0;
						}
						return c1.compareTo(c2);
					}
					else if (o1.getDeprovisioned() != null && o2.getDeprovisioned() != null) {
						Date c1 = o1.getDeprovisioned().getCreateTime();
						Date c2 = o2.getDeprovisioned().getCreateTime();
						if (c1 == null || c2 == null) {
							return 0;
						}
						return c1.compareTo(c2);
					}
					else if (o1.getProvisioned() != null && o2.getDeprovisioned() != null) {
						Date c1 = o1.getProvisioned().getCreateTime();
						Date c2 = o2.getDeprovisioned().getCreateTime();
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

			// Provisioning id column
			Column<StaticNatProvisioningSummaryPojo, String> provTypeColumn = 
					new Column<StaticNatProvisioningSummaryPojo, String> (new TextCell()) {

				@Override
				public String getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						return "Provision";
					}
					else if (object.getDeprovisioned() != null) {
						return "Deprovision";
					}
					else {
						return "Error";
					}
				}
			};
			provTypeColumn.setSortable(true);
			sortHandler.setComparator(provTypeColumn, new Comparator<StaticNatProvisioningSummaryPojo>() {
				public int compare(StaticNatProvisioningSummaryPojo o1, StaticNatProvisioningSummaryPojo o2) {
					if (o1.getProvisioned() != null) {
						return "Provisioned".compareTo("Deprovisioned");
					}
					else {
						return "Deprovisioned".compareTo("Provisioned");
					}
				}
			});
			listTable.addColumn(provTypeColumn, "Provisioning Type");

			// Provisioning id column
			Column<StaticNatProvisioningSummaryPojo, String> provIdColumn = 
					new Column<StaticNatProvisioningSummaryPojo, String> (new TextCell()) {

				@Override
				public String getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						return object.getProvisioned().getProvisioningId();
					}
					else if (object.getDeprovisioned() != null) {
						return object.getDeprovisioned().getProvisioningId();
					}
					else {
						return "Error";
					}
				}
			};
			provIdColumn.setSortable(true);
			sortHandler.setComparator(provIdColumn, new Comparator<StaticNatProvisioningSummaryPojo>() {
				public int compare(StaticNatProvisioningSummaryPojo o1, StaticNatProvisioningSummaryPojo o2) {
					if (o1.getProvisioned() != null && o2.getProvisioned() != null) {
						return o1.getProvisioned().getProvisioningId().compareTo(o2.getProvisioned().getProvisioningId());
					}
					else if (o1.getDeprovisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getDeprovisioned().getProvisioningId().compareTo(o2.getDeprovisioned().getProvisioningId());
					}
					else if (o1.getProvisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getProvisioned().getProvisioningId().compareTo(o2.getDeprovisioned().getProvisioningId());
					}
					else {
						return 0;
					}
				}
			});
			listTable.addColumn(provIdColumn, "Provisioning ID");

			// Status
			Column<StaticNatProvisioningSummaryPojo, String> statusColumn = 
					new Column<StaticNatProvisioningSummaryPojo, String> (new TextCell()) {

				@Override
				public String getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						return object.getProvisioned().getStatus();
					}
					else if (object.getDeprovisioned() != null) {
						return object.getDeprovisioned().getStatus();
					}
					else {
						return "Error";
					}
				}
			};
			statusColumn.setSortable(true);
			sortHandler.setComparator(provIdColumn, new Comparator<StaticNatProvisioningSummaryPojo>() {
				public int compare(StaticNatProvisioningSummaryPojo o1, StaticNatProvisioningSummaryPojo o2) {
					if (o1.getProvisioned() != null && o2.getProvisioned() != null) {
						return o1.getProvisioned().getStatus().compareTo(o2.getProvisioned().getStatus());
					}
					else if (o1.getDeprovisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getDeprovisioned().getStatus().compareTo(o2.getDeprovisioned().getStatus());
					}
					else if (o1.getProvisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getProvisioned().getStatus().compareTo(o2.getDeprovisioned().getStatus());
					}
					else {
						return 0;
					}
				}
			});
			listTable.addColumn(statusColumn, "Status");

			// Provisioning result
			Column<StaticNatProvisioningSummaryPojo, String> resultColumn = 
					new Column<StaticNatProvisioningSummaryPojo, String> (new TextCell()) {

				@Override
				public String getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						return object.getProvisioned().getProvisioningResult();
					}
					else if (object.getDeprovisioned() != null) {
						return object.getDeprovisioned().getProvisioningResult();
					}
					else {
						return "Error";
					}
				}
			};
			resultColumn.setSortable(true);
			sortHandler.setComparator(resultColumn, new Comparator<StaticNatProvisioningSummaryPojo>() {
				public int compare(StaticNatProvisioningSummaryPojo o1, StaticNatProvisioningSummaryPojo o2) {
					if (o1.getProvisioned() != null && o2.getProvisioned() != null) {
						return o1.getProvisioned().getProvisioningResult().compareTo(o2.getProvisioned().getProvisioningResult());
					}
					else if (o1.getDeprovisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getDeprovisioned().getProvisioningResult().compareTo(o2.getDeprovisioned().getProvisioningResult());
					}
					else if (o1.getProvisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getProvisioned().getProvisioningResult().compareTo(o2.getDeprovisioned().getProvisioningResult());
					}
					else {
						return 0;
					}
				}
			});
			listTable.addColumn(resultColumn, "Provisioning Result");

			// Anticipated time
			Column<StaticNatProvisioningSummaryPojo, String> anticipatedTimeColumn = 
					new Column<StaticNatProvisioningSummaryPojo, String> (new TextCell()) {

				@Override
				public String getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						return object.getProvisioned().getAnticipatedTime();
					}
					else if (object.getDeprovisioned() != null) {
						return object.getDeprovisioned().getAnticipatedTime();
					}
					else {
						return "Error";
					}
				}
			};
			anticipatedTimeColumn.setSortable(true);
			sortHandler.setComparator(anticipatedTimeColumn, new Comparator<StaticNatProvisioningSummaryPojo>() {
				public int compare(StaticNatProvisioningSummaryPojo o1, StaticNatProvisioningSummaryPojo o2) {
					if (o1.getProvisioned() != null && o2.getProvisioned() != null) {
						return o1.getProvisioned().getAnticipatedTime().compareTo(o2.getProvisioned().getAnticipatedTime());
					}
					else if (o1.getDeprovisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getDeprovisioned().getAnticipatedTime().compareTo(o2.getDeprovisioned().getAnticipatedTime());
					}
					else if (o1.getProvisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getProvisioned().getAnticipatedTime().compareTo(o2.getDeprovisioned().getAnticipatedTime());
					}
					else {
						return 0;
					}
				}
			});
			listTable.addColumn(anticipatedTimeColumn, "Anticipated Time");

			// Actual time
			Column<StaticNatProvisioningSummaryPojo, String> actualTimeColumn = 
					new Column<StaticNatProvisioningSummaryPojo, String> (new TextCell()) {

				@Override
				public String getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						return object.getProvisioned().getActualTime();
					}
					else if (object.getDeprovisioned() != null) {
						return object.getDeprovisioned().getActualTime();
					}
					else {
						return "Error";
					}
				}
			};
			actualTimeColumn.setSortable(true);
			sortHandler.setComparator(actualTimeColumn, new Comparator<StaticNatProvisioningSummaryPojo>() {
				public int compare(StaticNatProvisioningSummaryPojo o1, StaticNatProvisioningSummaryPojo o2) {
					if (o1.getProvisioned() != null && o2.getProvisioned() != null) {
						return o1.getProvisioned().getActualTime().compareTo(o2.getProvisioned().getActualTime());
					}
					else if (o1.getDeprovisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getDeprovisioned().getActualTime().compareTo(o2.getDeprovisioned().getActualTime());
					}
					else if (o1.getProvisioned() != null && o2.getDeprovisioned() != null) {
						return o1.getProvisioned().getActualTime().compareTo(o2.getDeprovisioned().getActualTime());
					}
					else {
						return 0;
					}
				}
			});
			listTable.addColumn(actualTimeColumn, "Actual Time");

			// Provisioning steps progress status
			final SafeHtmlCell stepProgressCell = new SafeHtmlCell();

			Column<StaticNatProvisioningSummaryPojo, SafeHtml> stepProgressCol = new Column<StaticNatProvisioningSummaryPojo, SafeHtml>(
					stepProgressCell) {

				@Override
				public SafeHtml getValue(StaticNatProvisioningSummaryPojo object) {
					if (object.getProvisioned() != null) {
						SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(
							object.getProvisioned().getTotalStepCount(), 
							object.getProvisioned().getCompletedStepCount());
						return sh;
					}
					else if (object.getDeprovisioned() != null) {
						SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(
								object.getDeprovisioned().getTotalStepCount(), 
								object.getDeprovisioned().getCompletedStepCount());
							return sh;
					}
					else {
				        SafeHtmlBuilder sb = new SafeHtmlBuilder();
				        sb.appendHtmlConstant("Error");
				        return sb.toSafeHtml();
					}
				}
			};		 
			listTable.addColumn(stepProgressCol, "Progress");
	}

	@Override
	public void applyNetworkAdminMask() {
		actionsButton.setEnabled(true);
	}
}
