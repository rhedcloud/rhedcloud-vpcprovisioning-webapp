package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationView.Presenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.NotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListNotification extends ViewImplBase implements ListNotificationView {
	Presenter presenter;
	private ListDataProvider<NotificationPojo> dataProvider = new ListDataProvider<NotificationPojo>();
	private SingleSelectionModel<NotificationPojo> selectionModel;
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
	    
	    Grid grid = new Grid(1, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
		Anchor assignAnchor = new Anchor("View Status");
		assignAnchor.addStyleName("productAnchor");
		assignAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		assignAnchor.setTitle("View status of selected VPCP");
		assignAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				NotificationPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_FIREWALL_RULE, m, null);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, assignAnchor);

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
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_ACCOUNT);
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
	    	new SingleSelectionModel<NotificationPojo>(NotificationPojo.KEY_PROVIDER);
		notificationListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		NotificationPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected service is: " + m.getNotificationId());
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
				return dateFormat.format(object.getCreateTime());
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<NotificationPojo>() {
			public int compare(NotificationPojo o1, NotificationPojo o2) {
				return o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		notificationListTable.addColumn(createTimeColumn, "Create Time");

		// delete row column
		Column<NotificationPojo, String> deleteRowColumn = new Column<NotificationPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(NotificationPojo object) {
				return "Delete";
			}
		};
		deleteRowColumn.setCellStyleNames("glowing-border");
		notificationListTable.addColumn(deleteRowColumn, "");
		notificationListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
		deleteRowColumn
		.setFieldUpdater(new FieldUpdater<NotificationPojo, String>() {
			@Override
			public void update(int index, final NotificationPojo svc,
					String value) {

				presenter.deleteNotification(svc);
			}
		});

		// edit row column
		Column<NotificationPojo, String> editRowColumn = new Column<NotificationPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(NotificationPojo object) {
				return "View";
			}
		};
		editRowColumn.setCellStyleNames("glowing-border");
		notificationListTable.addColumn(editRowColumn, "");
		notificationListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
		editRowColumn.setFieldUpdater(new FieldUpdater<NotificationPojo, String>() {
			@Override
			public void update(int index, final NotificationPojo svc,
					String value) {
				
				// fire MAINTAIN_VPC event passing the vpc to be maintained
				GWT.log("[DesktopListVpc] editing Notification: " + svc.getNotificationId());
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, svc);
			}
		});
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
