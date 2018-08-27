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
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.EmailPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListAccount extends ViewImplBase implements ListAccountView {
	Presenter presenter;
	private ListDataProvider<AccountPojo> dataProvider = new ListDataProvider<AccountPojo>();
	private SingleSelectionModel<AccountPojo> selectionModel;
	List<AccountPojo> accountList = new java.util.ArrayList<AccountPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);

	/*** FIELDS ***/
	@UiField SimplePager accountListPager;
	@UiField Button addAccountButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<AccountPojo> accountListTable = new CellTable<AccountPojo>(15, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField VerticalPanel accountListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox accountIdTB;
	@UiField PushButton refreshButton;

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		presenter.refreshList(userLoggedIn);
	}

	private static DesktopListAccountUiBinder uiBinder = GWT.create(DesktopListAccountUiBinder.class);

	interface DesktopListAccountUiBinder extends UiBinder<Widget, DesktopListAccount> {
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	 }
	
	public DesktopListAccount() {
		initWidget(uiBinder.createAndBindUi(this));

		setRefreshButtonImage(refreshButton);
		
		addAccountButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Should go to maintain account here...");
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_ACCOUNT);
			}
		}, ClickEvent.getType());
	}

	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		// filter list by account id typed in accountIdTB
		presenter.filterByAccountId(accountIdTB.getText());
	}
	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		// clear filter
		accountIdTB.setText("");
		presenter.clearFilter();
	}
	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    Grid grid = new Grid(6, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
	    // anchors for:
	    // - view/edit
	    // - delete
	    // - view bill summaries?
	    String anchorText = "View/Maintain Account";

		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("View/Maintain selected Account");
		maintainAnchor.ensureDebugId(anchorText);
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT, m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor deleteAnchor = new Anchor("Delete Account Metadata");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete the metadata for the selected account.  NOTE:  this is different than the 'Terminate Account' action as this only deletes the account metadata and does NOT remove the account from AWS.");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin()) {
						presenter.deleteAccount(m);
					}
					else {
						showMessageToUser("You are not authorized to perform this function for this account.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, deleteAnchor);

		Anchor billSummaryAnchor = new Anchor("View Bill Summary");
		billSummaryAnchor.addStyleName("productAnchor");
		billSummaryAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		billSummaryAnchor.setTitle("View bill summary for selected Account");
		billSummaryAnchor.ensureDebugId(billSummaryAnchor.getText());
		billSummaryAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
					if (userLoggedIn.isCentralAdmin() || userLoggedIn.isAdminForAccount(m.getAccountId())) {
						// show billing information for this account
						ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_BILL_SUMMARY_FOR_ACCOUNT, m);
					}
					else {
						showMessageToUser("You are not authorized to perform this function for this account.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(2, 0, billSummaryAnchor);

		Anchor createAccountNotificationAnchor = new Anchor("Create Account Notification");
		createAccountNotificationAnchor.addStyleName("productAnchor");
		createAccountNotificationAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		createAccountNotificationAnchor.setTitle("Create Account Notification");
		createAccountNotificationAnchor.ensureDebugId(createAccountNotificationAnchor.getText());
		createAccountNotificationAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin()) {
						// dialog for creating a service account
						ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_ACCOUNT_NOTIFICATION, m);
					}
					else {
						showMessageToUser("You are not authorized to perform this function for this account.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(3, 0, createAccountNotificationAnchor);

		Anchor createServiceAccountAnchor = new Anchor("Create Service Account");
		createServiceAccountAnchor.addStyleName("productAnchor");
		createServiceAccountAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		createServiceAccountAnchor.setTitle("Create Service Account");
		createServiceAccountAnchor.ensureDebugId(createServiceAccountAnchor.getText());
		createServiceAccountAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin() || userLoggedIn.isAdminForAccount(m.getAccountId())) {
						// dialog for creating a service account
						ActionEvent.fire(presenter.getEventBus(), ActionNames.INCIDENT_CREATE_SERVICE_ACCOUNT, m);
					}
					else {
						showMessageToUser("You are not authorized to perform this function for this account.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(4, 0, createServiceAccountAnchor);

		Anchor terminateAnchor = new Anchor("Terminate Account");
		terminateAnchor.addStyleName("productAnchor");
		terminateAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		terminateAnchor.setTitle("Terminate selected Account");
		terminateAnchor.ensureDebugId(terminateAnchor.getText());
		terminateAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin() || userLoggedIn.isAdminForAccount(m.getAccountId())) {
						// dialog for terminating account
						ActionEvent.fire(presenter.getEventBus(), ActionNames.INCIDENT_TERMINATE_ACCOUNT, m);
					}
					else {
						showMessageToUser("You are not authorized to perform this function for this account.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(5, 0, terminateAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void clearList() {
		accountListTable.setVisibleRangeAndClearData(accountListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setAccounts(List<AccountPojo> accounts) {
		GWT.log("view Setting accounts.");
		this.accountList = accounts;
		this.initializeAccountListTable();
	    accountListPager.setDisplay(accountListTable);
	}
	private Widget initializeAccountListTable() {
		GWT.log("initializing ACCOUNT list table...");
		accountListTable.setTableLayoutFixed(false);
		accountListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		accountListTable.setVisibleRange(0, 15);
		
		// create dataprovider
		dataProvider = new ListDataProvider<AccountPojo>();
		dataProvider.addDataDisplay(accountListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.accountList);
		
		selectionModel = 
	    	new SingleSelectionModel<AccountPojo>(AccountPojo.KEY_PROVIDER);
		accountListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		AccountPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected account is: " + m.getAccountId());
	    	}
	    });

	    ListHandler<AccountPojo> sortHandler = 
	    	new ListHandler<AccountPojo>(dataProvider.getList());
	    accountListTable.addColumnSortHandler(sortHandler);

	    if (accountListTable.getColumnCount() == 0) {
		    initAccountListTableColumns(sortHandler);

			// trying to add a dynamic title to an individual cell within the table.
		    // the following works UNTIL there are more than 5 rows and the 
		    // pager is used.  So, when the range changes, it gets confused
		    // no solution yet
//			accountListTable.addCellPreviewHandler(new Handler<AccountPojo>() {
//				@Override
//				public void onCellPreview(CellPreviewEvent<AccountPojo> event) {
//					if ("mouseover".equals(event.getNativeEvent().getType())) {
//						DirectoryMetaDataPojo dmd = event.getValue().getAccountOwnerDirectoryMetaData();
//						accountListTable.getRowElement(event.getIndex()).getCells().
//							getItem(event.getColumn()).setTitle(
//								dmd.getFirstName() + " " + 
//								dmd.getLastName() + 
//								" - From the IdentityService.");
//					}
//				}
//			});
			
	    }
		
		return accountListTable;
	}
	
	private void initAccountListTableColumns(ListHandler<AccountPojo> sortHandler) {
		GWT.log("initializing ACCOUNT list table columns...");
		
		// Checkbox column. This table will uses a checkbox column for selection.
	    // Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
	    // mouse selection.
	    Column<AccountPojo, Boolean> checkColumn = new Column<AccountPojo, Boolean>(
	        new CheckboxCell(true, false)) {
	      @Override
	      public Boolean getValue(AccountPojo object) {
	        // Get the value from the selection model.
	        return selectionModel.isSelected(object);
	      }
	    };
	    accountListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
	    accountListTable.setColumnWidth(checkColumn, 40, Unit.PX);
	    
		// ACCOUNT id column
		Column<AccountPojo, String> acctIdColumn = 
			new Column<AccountPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AccountPojo object) {
				return object.getAccountId();
			}
		};
		acctIdColumn.setSortable(true);
		acctIdColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(acctIdColumn, new Comparator<AccountPojo>() {
			public int compare(AccountPojo o1, AccountPojo o2) {
				return o1.getAccountId().compareTo(o2.getAccountId());
			}
		});
		accountListTable.addColumn(acctIdColumn, "Account ID");
		
		// account name
		Column<AccountPojo, String> acctNameColumn = 
			new Column<AccountPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AccountPojo object) {
				return object.getAccountName();
			}
		};
		acctNameColumn.setSortable(true);
		acctNameColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(acctNameColumn, new Comparator<AccountPojo>() {
			public int compare(AccountPojo o1, AccountPojo o2) {
				return o1.getAccountName().compareTo(o2.getAccountName());
			}
		});
		accountListTable.addColumn(acctNameColumn, "Account Name");
		
		// account owner
		Column<AccountPojo, String> ownerColumn = 
			new Column<AccountPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AccountPojo object) {
				return object.getAccountOwnerDirectoryMetaData().getFirstName() + 
						" " + object.getAccountOwnerDirectoryMetaData().getLastName();
			}
		};
		ownerColumn.setSortable(true);
		ownerColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(ownerColumn, new Comparator<AccountPojo>() {
			public int compare(AccountPojo o1, AccountPojo o2) {
				return o1.getAccountOwnerDirectoryMetaData().getNetId().compareTo(o2.getAccountOwnerDirectoryMetaData().getNetId());
			}
		});
		accountListTable.addColumn(ownerColumn, "Account Owner");
		
		// compliance class
		Column<AccountPojo, String> complianceClassColumn = 
			new Column<AccountPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AccountPojo object) {
				return object.getComplianceClass();
			}
		};
		complianceClassColumn.setSortable(true);
		complianceClassColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(complianceClassColumn, new Comparator<AccountPojo>() {
			public int compare(AccountPojo o1, AccountPojo o2) {
				return o1.getComplianceClass().compareTo(o2.getComplianceClass());
			}
		});
		accountListTable.addColumn(complianceClassColumn, "Compliance Class");
		
		// password location
		Column<AccountPojo, String> pwLocColumn = 
				new Column<AccountPojo, String> (new TextCell()) {

			@Override
			public String getValue(AccountPojo object) {
				return object.getPasswordLocation();
			}
		};
		pwLocColumn.setSortable(true);
		pwLocColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(pwLocColumn, new Comparator<AccountPojo>() {
			public int compare(AccountPojo o1, AccountPojo o2) {
				return o1.getPasswordLocation().compareTo(o2.getPasswordLocation());
			}
		});
		accountListTable.addColumn(pwLocColumn, "Password Location");
		
		// email addresses
		Column<AccountPojo, SafeHtml> emailColumn = 
				new Column<AccountPojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(AccountPojo object) {
				StringBuffer emails = new StringBuffer();
				int cntr = 1;
				if (object.getEmailList().size() > 0) {
					for (EmailPojo email : object.getEmailList()) {
						if (cntr == object.getEmailList().size()) {
							emails.append(email.getEmailAddress() + "/" + email.getType());
						}
						else {
							cntr++;
							emails.append(email.getEmailAddress() + "/" + email.getType() + "</br>");
						}
					}
					return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(emails.toString());
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("No e-mail addresses");
			}
		};
		emailColumn.setSortable(true);
		emailColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(emailColumn, new Comparator<AccountPojo>() {
			public int compare(AccountPojo o1, AccountPojo o2) {
				return o1.getPasswordLocation().compareTo(o2.getPasswordLocation());
			}
		});
		accountListTable.addColumn(emailColumn, "Associated e-mails");

		// TODO financial account number
		
		// TODO create user/time?
		
		// TODO last update user/time?
		
		// button to view billing information for this account
//		Column<AccountPojo, String> viewBillsColumn = new Column<AccountPojo, String>(
//				new ButtonCell()) {
//			@Override
//			public String getValue(AccountPojo object) {
//				return "Bills";
//			}
//		};
//		accountListTable.addColumn(viewBillsColumn, "");
//		accountListTable.setColumnWidth(viewBillsColumn, 50.0, Unit.PX);
//		viewBillsColumn
//		.setFieldUpdater(new FieldUpdater<AccountPojo, String>() {
//			@Override
//			public void update(int index, final AccountPojo account,
//					String value) {
//
//				if (userLoggedIn.isLitsAdmin() || userLoggedIn.isAdminForAccount(account.getAccountId())) {
//					// show billing information for this account
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_BILL_SUMMARY_FOR_ACCOUNT, account);
//				}
//				else {
//					showMessageToUser("You are not authorized to perform this function for this account.");
//				}
//			}
//		});

//		// delete row column
//		Column<AccountPojo, String> deleteRowColumn = new Column<AccountPojo, String>(
//				new ButtonCell()) {
//			@Override
//			public String getValue(AccountPojo object) {
//				return "Delete";
//			}
//		};
//		accountListTable.addColumn(deleteRowColumn, "");
//		accountListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
//		deleteRowColumn
//		.setFieldUpdater(new FieldUpdater<AccountPojo, String>() {
//			@Override
//			public void update(int index, final AccountPojo account,
//					String value) {
//
//				if (userLoggedIn.isLitsAdmin() || userLoggedIn.isAdminForAccount(account.getAccountId())) {
//					presenter.deleteAccount(account);
//				}
//				else {
//					showMessageToUser("You are not authorized to perform this function for this account.");
//				}
//			}
//		});

		// view/edit row column
//		Column<AccountPojo, String> editRowColumn = new Column<AccountPojo, String>(
//				new ButtonCell()) {
//			@Override
//			public String getValue(AccountPojo object) {
//				if (userLoggedIn.isLitsAdmin() || userLoggedIn.isAdminForAccount(object.getAccountId())) {
//					GWT.log(userLoggedIn.getEppn() + " is an admin");
//					return "Edit";
//				}
//				else {
//					GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
//					return "View";
//				}
//			}
//		};
//		accountListTable.addColumn(editRowColumn, "");
//		accountListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
//		editRowColumn.setFieldUpdater(new FieldUpdater<AccountPojo, String>() {
//			@Override
//			public void update(int index, final AccountPojo account,
//					String value) {
//				
//				// fire MAINTAIN_ACCOUNT event passing the account to be maintained
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT, account);
//				// TODO: add a maintainAccount method to the presenter so this is done there
//				// and not here.  This way, the mobile views will also get this 
//				// functionality even though they present the list of accounts 
//				// differently
//			}
//		});
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
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAccountFromView(AccountPojo account) {
		dataProvider.getList().remove(account);
	}

	@Override
	public Widget getStatusMessageSource() {
		return refreshButton;
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
		GWT.log("userLoggedIn is: " + this.userLoggedIn);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		addAccountButton.setEnabled(true);
		actionsButton.setEnabled(true);
		filterButton.setEnabled(true);
		clearFilterButton.setEnabled(true);
		accountIdTB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// disable add account button
		addAccountButton.setEnabled(false);
		actionsButton.setEnabled(false);
		filterButton.setEnabled(false);
		clearFilterButton.setEnabled(false);
		accountIdTB.setEnabled(false);
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
	public void initPage() {
		accountIdTB.setText("");
		accountIdTB.getElement().setPropertyString("placeholder", "enter account id");
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
		filterButton.setEnabled(false);
		clearFilterButton.setEnabled(false);
		actionsButton.setEnabled(false);
		addAccountButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		filterButton.setEnabled(true);
		clearFilterButton.setEnabled(true);
		actionsButton.setEnabled(true);
		addAccountButton.setEnabled(true);
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}
}
