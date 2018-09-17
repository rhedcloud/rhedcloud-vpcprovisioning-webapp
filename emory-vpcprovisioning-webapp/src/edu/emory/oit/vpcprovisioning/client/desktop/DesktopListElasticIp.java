package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListElasticIp extends ViewImplBase implements ListElasticIpView {
	Presenter presenter;
	private ListDataProvider<ElasticIpSummaryPojo> dataProvider = new ListDataProvider<ElasticIpSummaryPojo>();
	private MultiSelectionModel<ElasticIpSummaryPojo> selectionModel;
	List<ElasticIpSummaryPojo> elasticIpList = new java.util.ArrayList<ElasticIpSummaryPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager elasticIpListPager;
	@UiField Button createElasticIpButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<ElasticIpSummaryPojo> elasticIpListTable = new CellTable<ElasticIpSummaryPojo>(15, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField HorizontalPanel pleaseWaitPanel;

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	private static DesktopListElasticIpUiBinder uiBinder = GWT.create(DesktopListElasticIpUiBinder.class);

	interface DesktopListElasticIpUiBinder extends UiBinder<Widget, DesktopListElasticIp> {
	}

	public DesktopListElasticIp() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("createElasticIpButton")
	void createIpButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_ELASTIC_IP);
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

		Anchor releaseAddressesAnchor = new Anchor("Delete Address(es)");
		releaseAddressesAnchor.addStyleName("productAnchor");
		releaseAddressesAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		releaseAddressesAnchor.setTitle("Delete selected address(es)");
		releaseAddressesAnchor.ensureDebugId(releaseAddressesAnchor.getText());
		releaseAddressesAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select one or more item(s) from the list");
					return;
				}
				
				// TODO: presenter.deleteElasticIps(ipsToDelete);
				
				Iterator<ElasticIpSummaryPojo> nIter = selectionModel.getSelectedSet().iterator();
				while (nIter.hasNext()) {
					ElasticIpSummaryPojo m = nIter.next();
					if (m != null) {
						// remove the elastic ip if it's NOT assigned
						if (m.getElasticIpAssignment() != null) {
							showMessageToUser("You cannot delete an Elastic IP that has an assignment associated to it.");
						}
						else {
							if (userLoggedIn.isNetworkAdmin()) {
								presenter.deleteElasticIp(m);
							}
							else {
								showMessageToUser("You are not authorized to perform this action.");
							}
						}
					}
					else {
						showMessageToUser("Please select one or more item(s) from the list");
					}
				}
			}
		});
		grid.setWidget(0, 0, releaseAddressesAnchor);

		actionsPopup.showRelativeTo(actionsButton);
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
	public void applyAWSAccountAdminMask() {
		actionsButton.setEnabled(false);
		createElasticIpButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		actionsButton.setEnabled(false);
		createElasticIpButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
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
	public void setElasticIpSummaries(List<ElasticIpSummaryPojo> elasticIps) {
		this.elasticIpList = elasticIps;
		this.initializeElasticIpListTable();
	    elasticIpListPager.setDisplay(elasticIpListTable);
	}
	private Widget initializeElasticIpListTable() {
		GWT.log("initializing ElasticIP list table...");
		elasticIpListTable.setTableLayoutFixed(false);
		elasticIpListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		elasticIpListTable.setVisibleRange(0, 15);
		
		// create dataprovider
		dataProvider = new ListDataProvider<ElasticIpSummaryPojo>();
		dataProvider.addDataDisplay(elasticIpListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.elasticIpList);
		
		selectionModel = 
	    	new MultiSelectionModel<ElasticIpSummaryPojo>(ElasticIpSummaryPojo.KEY_PROVIDER);
		elasticIpListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
//	    		ElasticIpSummaryPojo m = selectionModel.getSelectedObject();
//	    		GWT.log("Selected elsticIp is: " + m.getClass().getName());
	    	}
	    });

	    ListHandler<ElasticIpSummaryPojo> sortHandler = 
	    	new ListHandler<ElasticIpSummaryPojo>(dataProvider.getList());
	    elasticIpListTable.addColumnSortHandler(sortHandler);

	    if (elasticIpListTable.getColumnCount() == 0) {
		    initElasticIpListTableColumns(sortHandler);
	    }
		
		return elasticIpListTable;
	}
	private void initElasticIpListTableColumns(ListHandler<ElasticIpSummaryPojo> sortHandler) {
		GWT.log("initializing ElasticIpSummary list table columns...");
		
	    Column<ElasticIpSummaryPojo, Boolean> checkColumn = new Column<ElasticIpSummaryPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(ElasticIpSummaryPojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    elasticIpListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    elasticIpListTable.setColumnWidth(checkColumn, 40, Unit.PX);

	    // Elastic IP column
		Column<ElasticIpSummaryPojo, String> elasticIpColumn = 
			new Column<ElasticIpSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				return object.getElasticIp().getElasticIpAddress();
			}
		};
		elasticIpColumn.setSortable(true);
		sortHandler.setComparator(elasticIpColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				return o1.getElasticIp().getElasticIpAddress().compareTo(o2.getElasticIp().getElasticIpAddress());
			}
		});
		elasticIpListTable.addColumn(elasticIpColumn, "Elastic IP");

		// VPC id column
		Column<ElasticIpSummaryPojo, String> associatedIpColumn = 
			new Column<ElasticIpSummaryPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				return object.getElasticIp().getAssociatedIpAddress();
			}
		};
		associatedIpColumn.setSortable(true);
		sortHandler.setComparator(associatedIpColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				return o1.getElasticIp().getAssociatedIpAddress().compareTo(o2.getElasticIp().getAssociatedIpAddress());
			}
		});
		elasticIpListTable.addColumn(associatedIpColumn, "Associated Private IP");
		
		Column<ElasticIpSummaryPojo, SafeHtml> assignmentStatusColumn = 
			new Column<ElasticIpSummaryPojo, SafeHtml> (new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIpAssignment() == null) {
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("Unassigned");
				}
				else {
					// TODO: more content here
					String s =
						"<b>Assigned </b>to VPC: " + object.getElasticIpAssignment().getOwnerId();
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(s);
				}
			}
		};
		assignmentStatusColumn.setSortable(true);
		sortHandler.setComparator(assignmentStatusColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				return o1.getElasticIp() == null ? 0 : 1;
			}
		});
		elasticIpListTable.addColumn(assignmentStatusColumn, "Assignment Status");

		// create user
		Column<ElasticIpSummaryPojo, String> createUserColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					return object.getElasticIp().getCreateUser();
				}
				else {
					return object.getElasticIpAssignment().getCreateUser();
				}
			}
		};
		createUserColumn.setSortable(true);
		sortHandler.setComparator(createUserColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					return o1.getElasticIp().getCreateUser().compareTo(o2.getElasticIp().getCreateUser());
				}
				else {
					return o1.getElasticIpAssignment().getCreateUser().
							compareTo(o2.getElasticIpAssignment().getCreateUser());
				}
			}
		});
		elasticIpListTable.addColumn(createUserColumn, "Create User");
		
		// create time
		Column<ElasticIpSummaryPojo, String> createTimeColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					Date createTime = object.getElasticIp().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
				else {
					Date createTime = object.getElasticIpAssignment().getCreateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					Date c1 = o1.getElasticIp().getCreateTime();
					Date c2 = o2.getElasticIp().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
				else {
					Date c1 = o1.getElasticIpAssignment().getCreateTime();
					Date c2 = o2.getElasticIpAssignment().getCreateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
			}
		});
		elasticIpListTable.addColumn(createTimeColumn, "Create Time");

		// last update user
		Column<ElasticIpSummaryPojo, String> lastUpdateUserColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					return object.getElasticIp().getUpdateUser();
				}
				else {
					return object.getElasticIpAssignment().getUpdateUser();
				}
			}
		};
		lastUpdateUserColumn.setSortable(true);
		sortHandler.setComparator(lastUpdateUserColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					return o1.getElasticIp().getUpdateUser().compareTo(o2.getElasticIp().getUpdateUser());
				}
				else {
					return o1.getElasticIpAssignment().getUpdateUser().
							compareTo(o2.getElasticIpAssignment().getUpdateUser());
				}
			}
		});
		elasticIpListTable.addColumn(lastUpdateUserColumn, "Update User");
		
		// update time
		Column<ElasticIpSummaryPojo, String> updateTimeColumn = 
				new Column<ElasticIpSummaryPojo, String> (new TextCell()) {

			@Override
			public String getValue(ElasticIpSummaryPojo object) {
				if (object.getElasticIp() != null) {
					Date createTime = object.getElasticIp().getUpdateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
				else {
					Date createTime = object.getElasticIpAssignment().getUpdateTime();
					return createTime != null ? dateFormat.format(createTime) : "Unknown";
				}
			}
		};
		updateTimeColumn.setSortable(true);
		sortHandler.setComparator(updateTimeColumn, new Comparator<ElasticIpSummaryPojo>() {
			public int compare(ElasticIpSummaryPojo o1, ElasticIpSummaryPojo o2) {
				if (o1.getElasticIp() != null) {
					Date c1 = o1.getElasticIp().getUpdateTime();
					Date c2 = o2.getElasticIp().getUpdateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
				else {
					Date c1 = o1.getElasticIpAssignment().getUpdateTime();
					Date c2 = o2.getElasticIpAssignment().getUpdateTime();
					if (c1 == null || c2 == null) {
						return 0;
					}
					return c1.compareTo(c2);
				}
			}
		});
		elasticIpListTable.addColumn(updateTimeColumn, "Update Time");
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
	public void removeElasticIpSummaryFromView(ElasticIpSummaryPojo summary) {
		dataProvider.getList().remove(summary);
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
		actionsButton.setEnabled(true);
		createElasticIpButton.setEnabled(false);
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
		createElasticIpButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		createElasticIpButton.setEnabled(true);
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
		actionsButton.setEnabled(true);
		createElasticIpButton.setEnabled(true);
	}
}
