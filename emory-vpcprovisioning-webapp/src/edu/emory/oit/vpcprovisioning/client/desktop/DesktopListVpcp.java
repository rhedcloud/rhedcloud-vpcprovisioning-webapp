package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
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
	@UiField(provided=true) CellTable<VpcpPojo> vpcpListTable = new CellTable<VpcpPojo>();
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListVpcpUiBinder uiBinder = GWT.create(DesktopListVpcpUiBinder.class);

	interface DesktopListVpcpUiBinder extends UiBinder<Widget, DesktopListVpcp> {
	}

	public DesktopListVpcp() {
		initWidget(uiBinder.createAndBindUi(this));
		
		generateVpcButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Should go to generate vpcp here...");
				ActionEvent.fire(presenter.getEventBus(), ActionNames.GENERATE_VPCP);
			}
		}, ClickEvent.getType());
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
		return vpcpListTable;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		generateVpcButton.setEnabled(true);
		actionsButton.setEnabled(true);
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
		vpcpListTable.setVisibleRange(0, 5);
		
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

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
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
}
