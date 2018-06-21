package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListAccount.MyCellTableResources;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.centraladmin.ListCentralAdminView;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListCentralAdmin extends ViewImplBase implements ListCentralAdminView {
	Presenter presenter;
	private ListDataProvider<RoleAssignmentSummaryPojo> dataProvider = new ListDataProvider<RoleAssignmentSummaryPojo>();
	private SingleSelectionModel<RoleAssignmentSummaryPojo> selectionModel;
	List<RoleAssignmentSummaryPojo> centralAdminList = new java.util.ArrayList<RoleAssignmentSummaryPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager centralAdminListPager;
//	@UiField Button addCentralAdminButton;
//	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<RoleAssignmentSummaryPojo> centralAdminListTable = new CellTable<RoleAssignmentSummaryPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField VerticalPanel centralAdminListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML introBodyHTML;

//	@UiField Button filterButton;
//	@UiField Button clearFilterButton;
//	@UiField TextBox centralAdminIdTB;

	private static DesktopListCentralAdminUiBinder uiBinder = GWT.create(DesktopListCentralAdminUiBinder.class);

	interface DesktopListCentralAdminUiBinder extends UiBinder<Widget, DesktopListCentralAdmin> {
	}

	public DesktopListCentralAdmin() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DesktopListCentralAdmin(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
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
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// TODO Auto-generated method stub
		
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
		centralAdminListTable.setVisibleRangeAndClearData(centralAdminListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setCentralAdmins(List<RoleAssignmentSummaryPojo> centralAdmins) {
		GWT.log("view Setting centralAdmins.");
		this.centralAdminList = centralAdmins;
		this.initializeCentralAdminListTable();
	    centralAdminListPager.setDisplay(centralAdminListTable);
	}

	private Widget initializeCentralAdminListTable() {
		GWT.log("initializing Central Admin list table...");
		centralAdminListTable.setTableLayoutFixed(false);
		centralAdminListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		centralAdminListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<RoleAssignmentSummaryPojo>();
		dataProvider.addDataDisplay(centralAdminListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.centralAdminList);
		
		selectionModel = 
	    	new SingleSelectionModel<RoleAssignmentSummaryPojo>(RoleAssignmentSummaryPojo.KEY_PROVIDER);
		centralAdminListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		RoleAssignmentSummaryPojo m = selectionModel.getSelectedObject();
//	    		GWT.log("Selected centralAdmin is: " + m.getAccountId());
	    	}
	    });

	    ListHandler<RoleAssignmentSummaryPojo> sortHandler = 
	    	new ListHandler<RoleAssignmentSummaryPojo>(dataProvider.getList());
	    centralAdminListTable.addColumnSortHandler(sortHandler);

	    if (centralAdminListTable.getColumnCount() == 0) {
		    initCentralAdminListTableColumns(sortHandler);
	    }
		
		return centralAdminListTable;
	}

	private void initCentralAdminListTableColumns(ListHandler<RoleAssignmentSummaryPojo> sortHandler) {
		GWT.log("initializing Central Admin list table columns...");
		
		Column<RoleAssignmentSummaryPojo, String> adminNameColumn = 
				new Column<RoleAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleAssignmentSummaryPojo object) {
				return object.getDirectoryPerson().getFullName();
			}
		};
		adminNameColumn.setSortable(true);
		adminNameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(adminNameColumn, new Comparator<RoleAssignmentSummaryPojo>() {
			public int compare(RoleAssignmentSummaryPojo o1, RoleAssignmentSummaryPojo o2) {
				return o1.getDirectoryPerson().getFullName().compareTo(o2.getDirectoryPerson().getFullName());
			}
		});
		centralAdminListTable.addColumn(adminNameColumn, "Admin Name");

		Column<RoleAssignmentSummaryPojo, String> reasonColumn = 
				new Column<RoleAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleAssignmentSummaryPojo object) {
				return object.getRoleAssignment().getReason();
			}
		};
		reasonColumn.setSortable(true);
		reasonColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(reasonColumn, new Comparator<RoleAssignmentSummaryPojo>() {
			public int compare(RoleAssignmentSummaryPojo o1, RoleAssignmentSummaryPojo o2) {
				return o1.getRoleAssignment().getReason().compareTo(o2.getRoleAssignment().getReason());
			}
		});
		centralAdminListTable.addColumn(reasonColumn, "Reason");

//		Column<RoleAssignmentSummaryPojo, String> roleDNColumn = 
//				new Column<RoleAssignmentSummaryPojo, String> (new TextCell()) {
//
//			@Override
//			public String getValue(RoleAssignmentSummaryPojo object) {
//				return object.getRoleAssignment().getRoleDN();
//			}
//		};
//		roleDNColumn.setSortable(true);
//		roleDNColumn.setCellStyleNames("tableBody");
//		sortHandler.setComparator(roleDNColumn, new Comparator<RoleAssignmentSummaryPojo>() {
//			public int compare(RoleAssignmentSummaryPojo o1, RoleAssignmentSummaryPojo o2) {
//				return o1.getRoleAssignment().getRoleDN().compareTo(o2.getRoleAssignment().getRoleDN());
//			}
//		});
//		centralAdminListTable.addColumn(roleDNColumn, "Role DN");
//		centralAdminListTable.setColumnWidth(roleDNColumn, 250, Unit.PX);

//		Column<RoleAssignmentSummaryPojo, SafeHtml> rolesColumn = 
//				new Column<RoleAssignmentSummaryPojo, SafeHtml> (new SafeHtmlCell()) {
//				
//				@Override
//				public SafeHtml getValue(RoleAssignmentSummaryPojo object) {
//					StringBuffer sbuf = new StringBuffer();
//					boolean isFirst = true;
//					for (String s : object.getRoleAssignment().getRoleDNs().getDistinguishedNames()) {
//						if (!isFirst) {
//							sbuf.append("<br>");
//						}
//						else {
//							isFirst = false;
//						}
//						sbuf.append(s);
//					}
//					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
//				}
//		};
//		rolesColumn.setSortable(true);
//		rolesColumn.setCellStyleNames("tableBody");
//		sortHandler.setComparator(rolesColumn, new Comparator<RoleAssignmentSummaryPojo>() {
//			public int compare(RoleAssignmentSummaryPojo o1, RoleAssignmentSummaryPojo o2) {
//				return o1.getAction().compareTo(o2.getAction());
//			}
//		});
//		centralAdminListTable.setColumnWidth(rolesColumn, 250, Unit.PX);
//		centralAdminListTable.addColumn(rolesColumn, "Role(s)");

		Column<RoleAssignmentSummaryPojo, String> effectiveDateColumn = 
				new Column<RoleAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleAssignmentSummaryPojo object) {
				if (object.getRoleAssignment().getEffectiveDate() != null) {
					return dateFormat.format(object.getRoleAssignment().getEffectiveDate());
				}
				else {
					return "Unknown";
				}
			}
		};
//		effectiveDateColumn.setSortable(true);
//		sortHandler.setComparator(effectiveDateColumn, new Comparator<RoleAssignmentSummaryPojo>() {
//			public int compare(RoleAssignmentSummaryPojo o1, RoleAssignmentSummaryPojo o2) {
//				if (o1.getRoleAssignment().getEffectiveDate() != null && o2.getRoleAssignment().getEffectiveDate() != null) {
//					return o1.getRoleAssignment().getEffectiveDate().compareTo(o2.getRoleAssignment().getEffectiveDate());
//				}
//				else {
//					return 0;
//				}
//			}
//		});
		centralAdminListTable.addColumn(effectiveDateColumn, "Effective Date");

		Column<RoleAssignmentSummaryPojo, String> expirationDate = 
				new Column<RoleAssignmentSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(RoleAssignmentSummaryPojo object) {
				if (object.getRoleAssignment().getExpirationDate() != null) {
					return dateFormat.format(object.getRoleAssignment().getExpirationDate());
				}
				else {
					return "Unknown";
				}
			}
		};
//		effectiveDateColumn.setSortable(true);
//		sortHandler.setComparator(effectiveDateColumn, new Comparator<RoleAssignmentSummaryPojo>() {
//			public int compare(RoleAssignmentSummaryPojo o1, RoleAssignmentSummaryPojo o2) {
//				if (o1.getRoleAssignment().getEffectiveDate() != null && o2.getRoleAssignment().getEffectiveDate() != null) {
//					return o1.getRoleAssignment().getEffectiveDate().compareTo(o2.getRoleAssignment().getEffectiveDate());
//				}
//				else {
//					return 0;
//				}
//			}
//		});
		centralAdminListTable.addColumn(expirationDate, "Expiration Date");
	}
	
	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCentralAdminFromView(RoleAssignmentSummaryPojo centralAdmin) {
		dataProvider.getList().remove(centralAdmin);
	}

	@Override
	public void initPage() {
//		centralAdminIdTB.setText("");
//		centralAdminIdTB.getElement().setPropertyString("placeholder", "enter filter text");
	}

	@Override
	public void setMyNetIdURL(String url) {
		String intro = introBodyHTML.
				getHTML().
				replace("MY_NET_ID_URL", url);
		introBodyHTML.setHTML(intro);
	}

}
