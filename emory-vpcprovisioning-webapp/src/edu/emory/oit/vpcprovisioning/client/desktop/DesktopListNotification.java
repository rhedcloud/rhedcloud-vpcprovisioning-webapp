package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationView;
import edu.emory.oit.vpcprovisioning.shared.NotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListNotification extends ViewImplBase implements ListNotificationView {
	Presenter presenter;
	private ListDataProvider<NotificationPojo> dataProvider = new ListDataProvider<NotificationPojo>();
	private MultiSelectionModel<NotificationPojo> selectionModel;
	List<NotificationPojo> serviceList = new java.util.ArrayList<NotificationPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	private static DesktopListNotificationUiBinder uiBinder = GWT.create(DesktopListNotificationUiBinder.class);

	interface DesktopListNotificationUiBinder extends UiBinder<Widget, DesktopListNotification> {
	}

	public DesktopListNotification() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DesktopListNotification(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/*** FIELDS ***/
	@UiField SimplePager notificationsListPager;
	@UiField(provided=true) CellTable<NotificationPojo> notificationListTable = new CellTable<NotificationPojo>();
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button closeOtherFeaturesButton;
	@UiField Button createNotificationButton;
	@UiField Button actionsButton;

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
	    Grid grid = new Grid(3, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
		Anchor viewAnchor = new Anchor("View Notification");
		viewAnchor.addStyleName("productAnchor");
		viewAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		viewAnchor.setTitle("View the selected Notification");
		viewAnchor.ensureDebugId(viewAnchor.getText());
		viewAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				if (selectionModel.getSelectedSet().size() > 1) {
					showMessageToUser("Please select one Notification to view");
					return;
				}
				Iterator<NotificationPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				NotificationPojo m = nIter.next();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, viewAnchor);

		Anchor deleteAnchor = new Anchor("Delete Notification(s)");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete selected Notification(s)");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				Iterator<NotificationPojo> nIter = selectionModel.getSelectedSet().iterator();
				while (nIter.hasNext()) {
					NotificationPojo m = nIter.next();
					if (m != null) {
						presenter.deleteNotification(m);
					}
					else {
						showMessageToUser("Please select an item from the list");
					}
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		Anchor markAnchor = new Anchor("Mark as read");
		markAnchor.addStyleName("productAnchor");
		markAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		markAnchor.setTitle("Mark selected Notification(s) as read");
		markAnchor.ensureDebugId(markAnchor.getText());
		markAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				Iterator<NotificationPojo> nIter = selectionModel.getSelectedSet().iterator();
				while (nIter.hasNext()) {
					NotificationPojo m = nIter.next();
					if (m != null) {
						// TODO: update the status of the notification
						m.setViewed(true);
//						presenter.deleteNotification(m);
					}
					else {
						showMessageToUser("Please select an item from the list");
					}
				}
			}
		});
		grid.setWidget(2, 0, markAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@UiHandler ("createNotificationButton")
	void createButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_NOTIFICATION);
	}
	@UiHandler ("closeOtherFeaturesButton")
	void closeOtherFeaturesButtonClicked(ClickEvent e) {
		presenter.getClientFactory().getShell().hideOtherFeaturesPanel();
		presenter.getClientFactory().getShell().showMainTabPanel();
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME);
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		return notificationListTable;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void clearList() {
		notificationListTable.setVisibleRangeAndClearData(notificationListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
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
	public void setNotifications(List<NotificationPojo> services) {
		this.serviceList = services;
		this.initializeNotificationListTable();
		notificationsListPager.setDisplay(notificationListTable);
	}

	@Override
	public void removeNotificationFromView(NotificationPojo service) {
		dataProvider.getList().remove(service);
	}

	private Widget initializeNotificationListTable() {
		GWT.log("initializing service list table...");
		notificationListTable.setTableLayoutFixed(false);
		notificationListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		notificationListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<NotificationPojo>();
		dataProvider.addDataDisplay(notificationListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.serviceList);
		
		selectionModel = 
	    	new MultiSelectionModel<NotificationPojo>(NotificationPojo.KEY_PROVIDER);
		notificationListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
//	    		NotificationPojo m = selectionModel.getSelectedObject();
//	    		GWT.log("Selected service is: " + m.getNotificationId());
	    	}
	    });

	    ListHandler<NotificationPojo> sortHandler = 
	    	new ListHandler<NotificationPojo>(dataProvider.getList());
	    notificationListTable.addColumnSortHandler(sortHandler);

	    if (notificationListTable.getColumnCount() == 0) {
		    initNotificationListTableColumns(sortHandler);
	    }
		
		return notificationListTable;
	}
	private void initNotificationListTableColumns(ListHandler<NotificationPojo> sortHandler) {
		GWT.log("initializing VPC list table columns...");
		
	    Column<NotificationPojo, Boolean> checkColumn = new Column<NotificationPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(NotificationPojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    notificationListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    notificationListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Account id column
		Column<NotificationPojo, String> acctIdColumn = 
			new Column<NotificationPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(NotificationPojo object) {
				return object.getNotificationId();
			}
		};
		acctIdColumn.setSortable(true);
		sortHandler.setComparator(acctIdColumn, new Comparator<NotificationPojo>() {
			public int compare(NotificationPojo o1, NotificationPojo o2) {
				return o1.getNotificationId().compareTo(o2.getNotificationId());
			}
		});
		notificationListTable.addColumn(acctIdColumn, "Notification ID");

		// VPC id column
		Column<NotificationPojo, String> vpcIdColumn = 
			new Column<NotificationPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(NotificationPojo object) {
				return object.getNotificationText();
			}
		};
		vpcIdColumn.setSortable(true);
		sortHandler.setComparator(vpcIdColumn, new Comparator<NotificationPojo>() {
			public int compare(NotificationPojo o1, NotificationPojo o2) {
				return o1.getNotificationText().compareTo(o2.getNotificationText());
			}
		});
		notificationListTable.addColumn(vpcIdColumn, "Text");
		
		// create time
		Column<NotificationPojo, String> createTimeColumn = 
				new Column<NotificationPojo, String> (new TextCell()) {

			@Override
			public String getValue(NotificationPojo object) {
				if (object.getCreateTime() != null) {
					return dateFormat.format(object.getCreateTime());
				}
				else {
					return "No Info";
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<NotificationPojo>() {
			public int compare(NotificationPojo o1, NotificationPojo o2) {
				if (o1.getCreateTime() != null && o2.getCreateTime() != null) {
					return o1.getCreateTime().compareTo(o2.getCreateTime());
				}
				else {
					return 0;
				}
			}
		});
		notificationListTable.addColumn(createTimeColumn, "Create Time");
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
