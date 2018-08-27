package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
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
import com.google.gwt.user.cellview.client.Header;
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
	List<UserNotificationPojo> pojoList = new java.util.ArrayList<UserNotificationPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);
    boolean longRunningProcess;

	@UiField(provided=true) CellTable<UserNotificationPojo> listTable = new CellTable<UserNotificationPojo>(15, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}

	private static DesktopListNotificationUiBinder uiBinder = GWT.create(DesktopListNotificationUiBinder.class);

	interface DesktopListNotificationUiBinder extends UiBinder<Widget, DesktopListNotification> {
	}

	public DesktopListNotification() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
	}

	/*** FIELDS ***/
	@UiField SimplePager listPager;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button closeOtherFeaturesButton;
	@UiField Button actionsButton;
	@UiField CheckBox viewUnReadCB;
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField PushButton refreshButton;
	@UiField HTML filteredHTML;

	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		if (viewUnReadCB.getValue()) {
			presenter.filterBySearchString(false, filterTB.getText());
		}
		else {
			presenter.filterBySearchString(true, filterTB.getText());
		}
	}
	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewUnReadCB.getValue()) {
			presenter.refreshListWithUnReadNotificationsForUser(userLoggedIn);
		}
		else {
			presenter.refreshListWithAllNotificationsForUser(userLoggedIn);
		}
		this.hideFilteredStatus();
	}
	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewUnReadCB.getValue()) {
			presenter.refreshListWithUnReadNotificationsForUser(userLoggedIn);
		}
		else {
			presenter.refreshListWithAllNotificationsForUser(userLoggedIn);
		}
	}
	@UiHandler("viewUnReadCB")
	void viewUnReadCBClicked(ClickEvent e) {
		filterTB.setText("");
		if (viewUnReadCB.getValue()) {
			presenter.refreshListWithUnReadNotificationsForUser(userLoggedIn);
		}
		else {
			presenter.refreshListWithAllNotificationsForUser(userLoggedIn);
		}
	}
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

		Anchor markAnchor = new Anchor("Mark SELECTED Notifications as read");
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
		grid.setWidget(1, 0, markAnchor);

		Anchor markAllAnchor = new Anchor("Mark ALL Notifications as read");
		markAllAnchor.addStyleName("productAnchor");
		markAllAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		markAllAnchor.setTitle("Mark ALL Notification(s) as read");
		markAllAnchor.ensureDebugId(markAllAnchor.getText());
		markAllAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				presenter.markAllUnreadNotificationsForUserAsRead(userLoggedIn);
			}
		});
		grid.setWidget(2, 0, markAllAnchor);

		actionsPopup.showRelativeTo(actionsButton);
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
		return listTable;
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
		listTable.setVisibleRangeAndClearData(listTable.getVisibleRange(), true);
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
	public void setNotifications(List<UserNotificationPojo> pojos) {
		this.pojoList = pojos;
		this.initializeListTable();
		listPager.setDisplay(listTable);
	}

	@Override
	public void removeNotificationFromView(UserNotificationPojo service) {
		dataProvider.getList().remove(service);
	}

	private Widget initializeListTable() {
		GWT.log("initializing service list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		listTable.setVisibleRange(0, 15);
		
		// create dataprovider
		dataProvider = new ListDataProvider<UserNotificationPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.pojoList);
		
		selectionModel = 
	    	new MultiSelectionModel<UserNotificationPojo>(UserNotificationPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
//	    		NotificationPojo m = selectionModel.getSelectedObject();
//	    		GWT.log("Selected service is: " + m.getNotificationId());
	    	}
	    });

	    ListHandler<UserNotificationPojo> sortHandler = 
	    	new ListHandler<UserNotificationPojo>(dataProvider.getList());
	    listTable.addColumnSortHandler(sortHandler);

	    if (listTable.getColumnCount() == 0) {
		    initListTableColumns(sortHandler);
	    }
		
		return listTable;
	}
	
	public class HeaderCheckbox extends CheckboxCell {

//	    private final SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked>Deselect All</>");
//	    private final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\">Select All</>");
	    private final SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked/>");
	    private final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\"/>");

	    public HeaderCheckbox() {
	    }

	    @Override
	    public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
	      if (value != null && value) {
	        sb.append(INPUT_CHECKED);
	      } else {
	        sb.append(INPUT_UNCHECKED);
	      }
	    }
	}

	private void initListTableColumns(ListHandler<UserNotificationPojo> sortHandler) {
		GWT.log("initializing User Notification list table columns...");
		GWT.log("there are " + sortHandler.getList().size() + " user notifications in the list");
		
		Column<UserNotificationPojo, Boolean> checkColumn = new Column<UserNotificationPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(UserNotificationPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		Header<Boolean> selectAllHeader = new Header<Boolean>(new HeaderCheckbox()) {
		    @Override
		    public Boolean getValue() {
		        for (UserNotificationPojo item : listTable.getVisibleItems()) {
		            if (!selectionModel.isSelected(item)) {
		                return false;
		            }
		        }
		        return listTable.getVisibleItems().size() > 0;
		    }
		};
		selectAllHeader.setUpdater(new ValueUpdater<Boolean>() {
		    @Override
		    public void update(Boolean value) {
		        for (UserNotificationPojo object : listTable.getVisibleItems()) {
		            selectionModel.setSelected(object, value);
		        }
		    }
		});
		listTable.addColumn(checkColumn, selectAllHeader);
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Notification id column
		Column<UserNotificationPojo, String> notificationIdColumn = 
			new Column<UserNotificationPojo, String> (new ClickableTextCell()) {
			
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
		notificationIdColumn.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, object);
	    	}
	    });
		notificationIdColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(notificationIdColumn, "ID");

		// Type column
		Column<UserNotificationPojo, String> typeColumn = 
			new Column<UserNotificationPojo, String> (new ClickableTextCell()) {
			
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
		typeColumn.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, object);
	    	}
	    });
		typeColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(typeColumn, "Type");
		
		// Priority column
		Column<UserNotificationPojo, String> priorityColumn = 
			new Column<UserNotificationPojo, String> (new ClickableTextCell()) {
			
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
		priorityColumn.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, object);
	    	}
	    });
		priorityColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(priorityColumn, "Priority");
		
		// Subject column
		Column<UserNotificationPojo, String> subjectColumn = 
			new Column<UserNotificationPojo, String> (new ClickableTextCell()) {
			
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
		subjectColumn.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, object);
	    	}
	    });
		subjectColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(subjectColumn, "Subject");
		
		// FullText column
		Column<UserNotificationPojo, String> fullTextColumn = 
			new Column<UserNotificationPojo, String> (new ClickableTextCell()) {
			
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
		fullTextColumn.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, object);
	    	}
	    });
		fullTextColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(fullTextColumn, "Text");
		
		// Reference id column
		Column<UserNotificationPojo, String> referenceId = 
			new Column<UserNotificationPojo, String> (new ClickableTextCell()) {
			
			@Override
			public String getValue(UserNotificationPojo object) {
				return object.getReferenceId();
			}
		};
		referenceId.setSortable(true);
		sortHandler.setComparator(referenceId, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				return o1.getReferenceId().compareTo(o2.getReferenceId());
			}
		});
		referenceId.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
	    		presenter.showSrdForUserNotification(object);
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.VIEW_SRD_FOR_USER_NOTIFICATION, object);
	    	}
	    });
		referenceId.setCellStyleNames("productAnchor");
		listTable.addColumn(referenceId, "Reference ID");

		// create time
		Column<UserNotificationPojo, String> createTimeColumn = 
				new Column<UserNotificationPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(UserNotificationPojo object) {
				Date createTime = object.getCreateTime();
				return createTime != null ? dateFormat.format(createTime) : "Unknown";
			}
		};
		createTimeColumn.setSortable(true);
		sortHandler.setComparator(createTimeColumn, new Comparator<UserNotificationPojo>() {
			public int compare(UserNotificationPojo o1, UserNotificationPojo o2) {
				GWT.log("user notification create time sort handler...");
				Date c1 = o1.getCreateTime();
				Date c2 = o2.getCreateTime();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
		createTimeColumn.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, object);
	    	}
	    });
		createTimeColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(createTimeColumn, "Create Time");

		// read time
		Column<UserNotificationPojo, String> readTimeColumn = 
				new Column<UserNotificationPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(UserNotificationPojo object) {
				if (object.getReadDateTime() != null) {
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
				Date c1 = o1.getReadDateTime();
				Date c2 = o2.getReadDateTime();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
		readTimeColumn.setFieldUpdater(new FieldUpdater<UserNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, UserNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_NOTIFICATION, object);
	    	}
	    });
		readTimeColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(readTimeColumn, "Read Time");
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

	@Override
	public void disableButtons() {
		actionsButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		actionsButton.setEnabled(true);
	}

	@Override
	public boolean viewAllNotifications() {
		return !viewUnReadCB.getValue();
	}

	@Override
	public void setLongRunningProcess(boolean isLongRunning) {
		longRunningProcess = isLongRunning;
	}

	@Override
	public boolean isLongRunningProcess() {
		return longRunningProcess;
	}

	@Override
	public void initPage() {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter search string");
	}
	@Override
	public List<UserNotificationPojo> getNotifications() {
		return this.pojoList;
	}
	@Override
	public void showFilteredStatus() {
		filteredHTML.setVisible(true);
	}
	@Override
	public void hideFilteredStatus() {
		filteredHTML.setVisible(false);
	}
	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}
}
