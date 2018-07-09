package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
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
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;

public class DesktopListNotification extends ViewImplBase implements ListNotificationView {
	Presenter presenter;
	private ListDataProvider<UserNotificationPojo> dataProvider = new ListDataProvider<UserNotificationPojo>();
	private MultiSelectionModel<UserNotificationPojo> selectionModel;
	List<UserNotificationPojo> serviceList = new java.util.ArrayList<UserNotificationPojo>();
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
	@UiField(provided=true) CellTable<UserNotificationPojo> notificationListTable = new CellTable<UserNotificationPojo>();
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button closeOtherFeaturesButton;
//	@UiField Button createNotificationButton;
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
				Iterator<UserNotificationPojo> nIter = selectionModel.getSelectedSet().iterator();
				
				UserNotificationPojo m = nIter.next();
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
				Iterator<UserNotificationPojo> nIter = selectionModel.getSelectedSet().iterator();
				while (nIter.hasNext()) {
					UserNotificationPojo m = nIter.next();
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
					showMessageToUser("Please select one or more item(s) from the list");
					return;
				}
				Iterator<UserNotificationPojo> nIter = selectionModel.getSelectedSet().iterator();
				while (nIter.hasNext()) {
					UserNotificationPojo m = nIter.next();
					if (m != null) {
						// update the status of the notification
						m.setRead(true);
						m.setReadDateTime(new Date());
						presenter.saveNotification(m);
					}
					else {
						showMessageToUser("Please select one or more item(s) from the list");
					}
				}
			}
		});
		grid.setWidget(2, 0, markAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

//	@UiHandler ("createNotificationButton")
//	void createButtonClicked(ClickEvent e) {
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_NOTIFICATION);
//	}
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
	public void applyAWSAccountAdminMask() {
//		createNotificationButton.setEnabled(true);;
		actionsButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
//		createNotificationButton.setEnabled(false);;
		actionsButton.setEnabled(false);
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
	public void setNotifications(List<UserNotificationPojo> services) {
		this.serviceList = services;
		this.initializeNotificationListTable();
		notificationsListPager.setDisplay(notificationListTable);
	}

	@Override
	public void removeNotificationFromView(UserNotificationPojo service) {
		dataProvider.getList().remove(service);
	}

	private Widget initializeNotificationListTable() {
		GWT.log("initializing service list table...");
		notificationListTable.setTableLayoutFixed(false);
		notificationListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		notificationListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<UserNotificationPojo>();
		dataProvider.addDataDisplay(notificationListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.serviceList);
		
		selectionModel = 
	    	new MultiSelectionModel<UserNotificationPojo>(UserNotificationPojo.KEY_PROVIDER);
		notificationListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
//	    		NotificationPojo m = selectionModel.getSelectedObject();
//	    		GWT.log("Selected service is: " + m.getNotificationId());
	    	}
	    });

	    ListHandler<UserNotificationPojo> sortHandler = 
	    	new ListHandler<UserNotificationPojo>(dataProvider.getList());
	    notificationListTable.addColumnSortHandler(sortHandler);

	    if (notificationListTable.getColumnCount() == 0) {
		    initNotificationListTableColumns(sortHandler);
	    }
		
		return notificationListTable;
	}
	private void initNotificationListTableColumns(ListHandler<UserNotificationPojo> sortHandler) {
		GWT.log("initializing VPC list table columns...");
		
	    Column<UserNotificationPojo, Boolean> checkColumn = new Column<UserNotificationPojo, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(UserNotificationPojo object) {
		        // Get the value from the selection model.
		        return selectionModel.isSelected(object);
		      }
		    };
		    notificationListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		    notificationListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Notification id column
		Column<UserNotificationPojo, String> notificationIdColumn = 
			new Column<UserNotificationPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(UserNotificationPojo object) {
				return object.getUserNotificationId();
			}
		};
		notificationIdColumn.setSortable(true);
		sortHandler.setComparator(notificationIdColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				return o1.getUserNotificationId().compareTo(o2.getUserNotificationId());
			}
		});
		notificationListTable.addColumn(notificationIdColumn, "Notification ID");

		// Type column
		Column<UserNotificationPojo, String> typeColumn = 
			new Column<UserNotificationPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(UserNotificationPojo object) {
				return object.getType();
			}
		};
		typeColumn.setSortable(true);
		sortHandler.setComparator(typeColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				return o1.getType().compareTo(o2.getType());
			}
		});
		notificationListTable.addColumn(typeColumn, "Type");
		
		// Priority column
		Column<UserNotificationPojo, String> priorityColumn = 
			new Column<UserNotificationPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(UserNotificationPojo object) {
				return object.getPriority();
			}
		};
		priorityColumn.setSortable(true);
		sortHandler.setComparator(priorityColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				return o1.getPriority().compareTo(o2.getPriority());
			}
		});
		notificationListTable.addColumn(priorityColumn, "Priority");
		
		// Subject column
		Column<UserNotificationPojo, String> subjectColumn = 
			new Column<UserNotificationPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(UserNotificationPojo object) {
				return object.getSubject();
			}
		};
		subjectColumn.setSortable(true);
		sortHandler.setComparator(subjectColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				return o1.getSubject().compareTo(o2.getSubject());
			}
		});
		notificationListTable.addColumn(subjectColumn, "Subject");
		
		// FullText column
		Column<UserNotificationPojo, String> fullTextColumn = 
			new Column<UserNotificationPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(UserNotificationPojo object) {
				return object.getText();
			}
		};
		fullTextColumn.setSortable(true);
		sortHandler.setComparator(fullTextColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				return o1.getText().compareTo(o2.getText());
			}
		});
		notificationListTable.addColumn(fullTextColumn, "Text");
		
		// create time
		Column<UserNotificationPojo, String> createTimeColumn = 
				new Column<UserNotificationPojo, String> (new TextCell()) {

			@Override
			public String getValue(UserNotificationPojo object) {
				if (object.getCreateTime() != null) {
					return dateFormat.format(object.getCreateTime());
				}
				else {
					return "No Info";
				}
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				if (o1.getCreateTime() != null && o2.getCreateTime() != null) {
					return o1.getCreateTime().compareTo(o2.getCreateTime());
				}
				else {
					return 0;
				}
			}
		});
		notificationListTable.addColumn(createTimeColumn, "Create Time");

		// read time
		Column<UserNotificationPojo, String> readTimeColumn = 
				new Column<UserNotificationPojo, String> (new TextCell()) {

			@Override
			public String getValue(UserNotificationPojo object) {
				if (object.getCreateTime() != null) {
					return dateFormat.format(object.getReadDateTime());
				}
				else {
					return "No Info";
				}
			}
		};
		readTimeColumn.setSortable(true);
		sortHandler.setComparator(readTimeColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				if (o1.getReadDateTime() != null && o2.getReadDateTime() != null) {
					return o1.getReadDateTime().compareTo(o2.getReadDateTime());
				}
				else {
					return 0;
				}
			}
		});
		notificationListTable.addColumn(readTimeColumn, "Read Time");
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
		// TODO Auto-generated method stub
		
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
}
