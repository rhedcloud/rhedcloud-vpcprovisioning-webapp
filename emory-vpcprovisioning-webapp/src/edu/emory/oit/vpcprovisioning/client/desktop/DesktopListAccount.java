package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
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
import edu.emory.oit.vpcprovisioning.shared.Constants;
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
	@UiField(provided=true) CellTable<AccountPojo> accountListTable = new CellTable<AccountPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField VerticalPanel accountListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox accountIdTB;

	private static DesktopListAccountUiBinder uiBinder = GWT.create(DesktopListAccountUiBinder.class);

	interface DesktopListAccountUiBinder extends UiBinder<Widget, DesktopListAccount> {
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	 }
	
	public DesktopListAccount() {
		initWidget(uiBinder.createAndBindUi(this));
		
//		refreshPropertyButton.addDomHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				AsyncCallback<String> callback = new AsyncCallback<String>() {
//					@Override
//					public void onFailure(Throwable caught) {
//						showMessageToUser("There was an exception on the " +
//								"server refreshing the property.  " +
//								"Message from server is: " + caught.getMessage());
//					}
//
//					@Override
//					public void onSuccess(String result) {
//						showStatus(refreshPropertyButton, "refreshableProperty is now:" + result);
//					}
//				};
//				GWT.log("testing property refresh.");
//				VpcProvisioningService.Util.getInstance().testPropertyReload(callback);
//			}
//		}, ClickEvent.getType());

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
	    
	    Grid grid = new Grid(3, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
	    // anchors for:
	    // - view/edit
	    // - delete
	    // - view bill summaries?
	    String anchorText = "View Account";
		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			anchorText = "Maintain Account";
		}

		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("View/Maintain selected Account");
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					// just use a popup here and not try to show the "normal" CidrAssignment
					// maintenance view.  This is handled in the AppBootstrapper when the events are registered.
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_FIREWALL_RULE, m, null);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);

		Anchor deleteAnchor = new Anchor("Delete Account");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Remove selected Account");
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
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
		grid.setWidget(1, 0, deleteAnchor);

		Anchor billSummaryAnchor = new Anchor("View Bill Summary");
		billSummaryAnchor.addStyleName("productAnchor");
		billSummaryAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		billSummaryAnchor.setTitle("View bill summary for selected Account");
		billSummaryAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				AccountPojo m = selectionModel.getSelectedObject();
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
		grid.setWidget(2, 0, billSummaryAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	@Override
	public void showPleaseWaitDialog() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void hidePleaseWaitDialog() {
		pleaseWaitPanel.setVisible(false);
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
		accountListTable.setVisibleRange(0, 5);
		
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
		
		// owner net id
		Column<AccountPojo, String> ownerNetIdColumn = 
			new Column<AccountPojo, String> (new TextCell()) {
			
			@Override
			public String getValue(AccountPojo object) {
				return object.getAccountOwnerDirectoryMetaData().getNetId() + 
						" (" + object.getAccountOwnerDirectoryMetaData().getFirstName() + 
						" " + object.getAccountOwnerDirectoryMetaData().getLastName() + 
						")";
			}
		};
		ownerNetIdColumn.setSortable(true);
		ownerNetIdColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(ownerNetIdColumn, new Comparator<AccountPojo>() {
			public int compare(AccountPojo o1, AccountPojo o2) {
				return o1.getAccountOwnerDirectoryMetaData().getNetId().compareTo(o2.getAccountOwnerDirectoryMetaData().getNetId());
			}
		});
		accountListTable.addColumn(ownerNetIdColumn, "Owner's NetID");
		
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
							emails.append(email.getEmail() + "/" + email.getType());
						}
						else {
							cntr++;
							emails.append(email.getEmail() + "/" + email.getType() + "</br>");
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
		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			GWT.log(userLoggedIn.getEppn() + " is an admin");
			Column<AccountPojo, String> viewBillsColumn = new Column<AccountPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(AccountPojo object) {
					return "Bills";
				}
			};
			accountListTable.addColumn(viewBillsColumn, "");
			accountListTable.setColumnWidth(viewBillsColumn, 50.0, Unit.PX);
			viewBillsColumn
			.setFieldUpdater(new FieldUpdater<AccountPojo, String>() {
				@Override
				public void update(int index, final AccountPojo account,
						String value) {

					// show billing information for this account
					ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_BILL_SUMMARY_FOR_ACCOUNT, account);
				}
			});
		}
		else {
			GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
		}

		if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			GWT.log(userLoggedIn.getEppn() + " is an admin");
			// delete row column
			Column<AccountPojo, String> deleteRowColumn = new Column<AccountPojo, String>(
					new ButtonCell()) {
				@Override
				public String getValue(AccountPojo object) {
					return "Delete";
				}
			};
			accountListTable.addColumn(deleteRowColumn, "");
			accountListTable.setColumnWidth(deleteRowColumn, 50.0, Unit.PX);
			deleteRowColumn
			.setFieldUpdater(new FieldUpdater<AccountPojo, String>() {
				@Override
				public void update(int index, final AccountPojo account,
						String value) {

					presenter.deleteAccount(account);
					
				}
			});
		}
		else {
			GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
		}

		// view/edit row column
		Column<AccountPojo, String> editRowColumn = new Column<AccountPojo, String>(
				new ButtonCell()) {
			@Override
			public String getValue(AccountPojo object) {
				if (userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
					GWT.log(userLoggedIn.getEppn() + " is an admin");
					return "Edit";
				}
				else {
					GWT.log(userLoggedIn.getEppn() + " is NOT an admin");
					return "View";
				}
			}
		};
		accountListTable.addColumn(editRowColumn, "");
		accountListTable.setColumnWidth(editRowColumn, 50.0, Unit.PX);
		editRowColumn.setFieldUpdater(new FieldUpdater<AccountPojo, String>() {
			@Override
			public void update(int index, final AccountPojo account,
					String value) {
				
				// fire MAINTAIN_ACCOUNT event passing the account to be maintained
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT, account);
				// TODO: add a maintainAccount method to the presenter so this is done there
				// and not here.  This way, the mobile views will also get this 
				// functionality even though they present the list of accounts 
				// differently
			}
		});
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
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAccountFromView(AccountPojo account) {
		dataProvider.getList().remove(account);
	}

	@Override
	public Widget getStatusMessageSource() {
		return addAccountButton;
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
		GWT.log("userLoggedIn is: " + this.userLoggedIn);
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// enable add account button
		addAccountButton.setEnabled(true);
		// enable Delete button in table (handled in initAccountListTableColumns)
		// change text of button to Edit (handled in initAccountListTableColumns)
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// disable add account button
		addAccountButton.setEnabled(false);
		// disable Delete button in table (handled in initAccountListTableColumns)
		// change text of button to View (handled in initAccountListTableColumns)
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
}
