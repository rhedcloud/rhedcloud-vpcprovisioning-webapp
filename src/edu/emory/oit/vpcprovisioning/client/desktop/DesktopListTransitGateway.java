package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.ListTransitGatewayView;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListTransitGateway extends ViewImplBase implements ListTransitGatewayView {
	Presenter presenter;
	private ListDataProvider<TransitGatewayPojo> dataProvider = new ListDataProvider<TransitGatewayPojo>();
	private SingleSelectionModel<TransitGatewayPojo> selectionModel;
	List<TransitGatewayPojo> transitGatewayList = new java.util.ArrayList<TransitGatewayPojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField(provided = true)
	SimplePager transitGatewayListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided = true)
	SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField
	Button createTransitGatewayButton;
	@UiField
	Button actionsButton;
	@UiField(provided = true)
	CellTable<TransitGatewayPojo> transitGatewayListTable = new CellTable<TransitGatewayPojo>(15,
			(CellTable.Resources) GWT.create(MyCellTableResources.class));
	@UiField
	HorizontalPanel pleaseWaitPanel;
	@UiField
	HTML pleaseWaitHTML;

	public interface MyCellTableResources extends CellTable.Resources {

		@Source({ CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

	private static DesktopListTransitGatewayUiBinder uiBinder = GWT.create(DesktopListTransitGatewayUiBinder.class);

	interface DesktopListTransitGatewayUiBinder extends UiBinder<Widget, DesktopListTransitGateway> {
	}

	public DesktopListTransitGateway() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		showMessageToUser("Comming soon");
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		if (pleaseWaitHTML == null || pleaseWaitHTML.length() == 0) {
			this.pleaseWaitHTML.setHTML("Please wait...");
		} else {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyNetworkAdminMask() {
		actionsButton.setEnabled(true);
		createTransitGatewayButton.setEnabled(true);
	}

	@Override
	public void applyCentralAdminMask() {
		actionsButton.setEnabled(true);
		createTransitGatewayButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		actionsButton.setEnabled(false);
		createTransitGatewayButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		actionsButton.setEnabled(false);
		createTransitGatewayButton.setEnabled(false);
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
		createTransitGatewayButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		createTransitGatewayButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void clearList() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setTransitGateways(List<TransitGatewayPojo> pojos) {
		this.transitGatewayList = pojos;
		this.initializeListTable();
		transitGatewayListPager.setDisplay(transitGatewayListTable);
		topListPager.setDisplay(transitGatewayListTable);
	}

	private void initializeListTable() {
		GWT.log("initializing Transit Gateway list table...");
		transitGatewayListTable.setTableLayoutFixed(false);
		transitGatewayListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		transitGatewayListTable.setVisibleRange(0, 15);

		// create dataprovider
		dataProvider = new ListDataProvider<TransitGatewayPojo>();
		dataProvider.addDataDisplay(transitGatewayListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.transitGatewayList);

		selectionModel = new SingleSelectionModel<TransitGatewayPojo>(TransitGatewayPojo.KEY_PROVIDER);
		transitGatewayListTable.setSelectionModel(selectionModel);

//	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//	    	@Override
//	    	public void onSelectionChange(SelectionChangeEvent event) {
//	    	}
//	    });

		ListHandler<TransitGatewayPojo> sortHandler = new ListHandler<TransitGatewayPojo>(dataProvider.getList());
		transitGatewayListTable.addColumnSortHandler(sortHandler);

		if (transitGatewayListTable.getColumnCount() == 0) {
			initListTableColumns(sortHandler);
		}
	}

	private void initListTableColumns(ListHandler<TransitGatewayPojo> sortHandler) {
		Column<TransitGatewayPojo, Boolean> checkColumn = new Column<TransitGatewayPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(TransitGatewayPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		transitGatewayListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		transitGatewayListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// environment column
		Column<TransitGatewayPojo, String> environmentColumn = new Column<TransitGatewayPojo, String>(new TextCell()) {

			@Override
			public String getValue(TransitGatewayPojo object) {
				return object.getEnvironment();
			}
		};
		environmentColumn.setSortable(true);
		sortHandler.setComparator(environmentColumn, new Comparator<TransitGatewayPojo>() {
			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
				return o1.getEnvironment().compareTo(o2.getEnvironment());
			}
		});
		transitGatewayListTable.addColumn(environmentColumn, "Environment");

		// Region column
		Column<TransitGatewayPojo, String> vpcIdColumn = new Column<TransitGatewayPojo, String>(new TextCell()) {

			@Override
			public String getValue(TransitGatewayPojo object) {
				return object.getRegion();
			}
		};
		vpcIdColumn.setSortable(true);
		sortHandler.setComparator(vpcIdColumn, new Comparator<TransitGatewayPojo>() {
			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
				return o1.getRegion().compareTo(o2.getRegion());
			}
		});
		transitGatewayListTable.addColumn(vpcIdColumn, "Region");

		// Account id column
		Column<TransitGatewayPojo, String> acctIdColumn = new Column<TransitGatewayPojo, String>(new TextCell()) {

			@Override
			public String getValue(TransitGatewayPojo object) {
				return object.getAccountId();
			}
		};
		acctIdColumn.setSortable(true);
		sortHandler.setComparator(acctIdColumn, new Comparator<TransitGatewayPojo>() {
			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
				return o1.getAccountId().compareTo(o2.getAccountId());
			}
		});
		transitGatewayListTable.addColumn(acctIdColumn, "Account ID");

		// Transit Gateway ID column
		Column<TransitGatewayPojo, String> regionColumn = new Column<TransitGatewayPojo, String>(new TextCell()) {

			@Override
			public String getValue(TransitGatewayPojo object) {
				return object.getTransitGatewayId();
			}
		};
		regionColumn.setSortable(true);
		sortHandler.setComparator(regionColumn, new Comparator<TransitGatewayPojo>() {
			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
				return o1.getTransitGatewayId().compareTo(o2.getTransitGatewayId());
			}
		});
		transitGatewayListTable.addColumn(regionColumn, "Transit Gateway ID");

		// association route table id
		Column<TransitGatewayPojo, SafeHtml> assocRouteColumn = new Column<TransitGatewayPojo, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(TransitGatewayPojo object) {
				List<TransitGatewayProfilePojo> profiles = object.getProfiles();
				if (profiles.size() > 0) {
					StringBuffer sbuf = new StringBuffer();
					for (TransitGatewayProfilePojo profile : profiles) {
						sbuf.append("<p>");
						sbuf.append(profile.getAssociationRouteTableId());
						sbuf.append("</p>");
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
				} else {
					String s = "No Associated Route Table Info";
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(s);
				}
			}
		};
		assocRouteColumn.setSortable(true);
		sortHandler.setComparator(assocRouteColumn, new Comparator<TransitGatewayPojo>() {
			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
				return o1.getProfiles().size() == 0 ? 0 : 1;
			}
		});
		transitGatewayListTable.addColumn(assocRouteColumn, "Association Route Table ID(s)");

		// propagation route table id
		Column<TransitGatewayPojo, SafeHtml> propRouteColumn = new Column<TransitGatewayPojo, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(TransitGatewayPojo object) {
				List<TransitGatewayProfilePojo> profiles = object.getProfiles();
				if (profiles.size() > 0) {
					StringBuffer sbuf = new StringBuffer();
					for (TransitGatewayProfilePojo profile : profiles) {
						for (String propRoute : profile.getPropagationRouteTableIds()) {
							sbuf.append("<p>");
							sbuf.append(propRoute);
							sbuf.append("</p>");
						}
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
				} else {
					String s = "No Propagation Route Table Info";
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(s);
				}
			}
		};
		propRouteColumn.setSortable(true);
		sortHandler.setComparator(propRouteColumn, new Comparator<TransitGatewayPojo>() {
			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
				return o1.getProfiles().size() == 0 ? 0 : 1;
			}
		});
		transitGatewayListTable.addColumn(propRouteColumn, "Propagation Route Table ID(s)");

		// create user
//		Column<TransitGatewayPojo, String> createUserColumn = new Column<TransitGatewayPojo, String>(
//				new ClickableTextCell()) {
//
//			@Override
//			public String getValue(TransitGatewayPojo object) {
//				return object.getCreateUser();
//			}
//		};
//		createUserColumn.setSortable(true);
//		sortHandler.setComparator(createUserColumn, new Comparator<TransitGatewayPojo>() {
//			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
//				return o1.getCreateUser().compareTo(o2.getCreateUser());
//			}
//		});
//		createUserColumn.setFieldUpdater(new FieldUpdater<TransitGatewayPojo, String>() {
//			@Override
//			public void update(int index, TransitGatewayPojo object, String value) {
//				showDirectoryMetaDataForPublicId(object.getCreateUser());
//			}
//		});
//		createUserColumn.setCellStyleNames("tableAnchor");
//		transitGatewayListTable.addColumn(createUserColumn, "Create User");

		// create time
//		Column<TransitGatewayPojo, String> createTimeColumn = new Column<TransitGatewayPojo, String>(new TextCell()) {
//
//			@Override
//			public String getValue(TransitGatewayPojo object) {
//				if (object.getCreateTime() != null) {
//					return dateFormat.format(object.getCreateTime());
//				} else {
//					return "Unknown";
//				}
//			}
//		};
//		createTimeColumn.setSortable(true);
//		sortHandler.setComparator(createTimeColumn, new Comparator<TransitGatewayPojo>() {
//			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
//				if (o1.getCreateTime() != null && o2.getCreateTime() != null) {
//					return o1.getCreateTime().compareTo(o2.getCreateTime());
//				} else {
//					return 0;
//				}
//			}
//		});
//		transitGatewayListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
//		Column<TransitGatewayPojo, String> lastUpdateUserColumn = new Column<TransitGatewayPojo, String>(
//				new ClickableTextCell()) {
//
//			@Override
//			public String getValue(TransitGatewayPojo object) {
//				return object.getUpdateUser();
//			}
//		};
//		lastUpdateUserColumn.setSortable(true);
//		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<TransitGatewayPojo>() {
//			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
//				return o1.getUpdateUser().compareTo(o2.getUpdateUser());
//			}
//		});
//		lastUpdateUserColumn.setFieldUpdater(new FieldUpdater<TransitGatewayPojo, String>() {
//			@Override
//			public void update(int index, TransitGatewayPojo object, String value) {
//				showDirectoryMetaDataForPublicId(object.getUpdateUser());
//			}
//		});
//		lastUpdateUserColumn.setCellStyleNames("tableAnchor");
//		transitGatewayListTable.addColumn(lastUpdateUserColumn, "Update User");

		// update time
//		Column<TransitGatewayPojo, String> updateTimeColumn = new Column<TransitGatewayPojo, String>(new TextCell()) {
//
//			@Override
//			public String getValue(TransitGatewayPojo object) {
//				if (object.getUpdateTime() != null) {
//					return dateFormat.format(object.getUpdateTime());
//				} else {
//					return "Unknown";
//				}
//			}
//		};
//		updateTimeColumn.setSortable(true);
//		sortHandler.setComparator(updateTimeColumn, new Comparator<TransitGatewayPojo>() {
//			public int compare(TransitGatewayPojo o1, TransitGatewayPojo o2) {
//				if (o1.getUpdateTime() != null && o2.getUpdateTime() != null) {
//					return o1.getUpdateTime().compareTo(o2.getUpdateTime());
//				} else {
//					return 0;
//				}
//			}
//		});
//		transitGatewayListTable.addColumn(updateTimeColumn, "Update Time");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTransitGatewayFromView(TransitGatewayPojo tgw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showFilteredStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideFilteredStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshTableRow(int rowNumber, TransitGatewayPojo tgw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSummaryHTML(String summaryHTML) {
		// TODO Auto-generated method stub

	}

}
