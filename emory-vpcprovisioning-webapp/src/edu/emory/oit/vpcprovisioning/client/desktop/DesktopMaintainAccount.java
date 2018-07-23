package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonSuggestion;
import edu.emory.oit.vpcprovisioning.client.common.RoleSelectionPopup;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountView;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.EmailPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainAccount extends ViewImplBase implements MaintainAccountView {
	Presenter presenter;
	List<String> emailTypes;
	List<String> complianceClassTypes;
	UserAccountPojo userLoggedIn;
	boolean editing;
	int adminRowNum = 0;
	int adminColumnNum = 0;
	int removeButtonColumnNum = 1;
	String speedTypeBeingTyped=null;
	boolean speedTypeConfirmed = false;
	private final DirectoryPersonRpcSuggestOracle personSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);
	private final DirectoryPersonRpcSuggestOracle ownerIdSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);
	PopupPanel adminPleaseWaitDialog;
	PopupPanel waitForNotificationsDialog;
    
	private ListDataProvider<AccountNotificationPojo> dataProvider = new ListDataProvider<AccountNotificationPojo>();
	private MultiSelectionModel<AccountNotificationPojo> selectionModel;
	List<AccountNotificationPojo> pojoList = new java.util.ArrayList<AccountNotificationPojo>();
	PopupPanel actionsPopup = new PopupPanel(true);

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	@UiField VerticalPanel notificationListPanel;
	@UiField SimplePager listPager;
	@UiField(provided=true) CellTable<AccountNotificationPojo> listTable = new CellTable<AccountNotificationPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button billSummaryButton;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox accountIdTB;
	@UiField TextBox accountNameTB;
	@UiField TextBox passwordLocationTB;
	@UiField TextBox speedTypeTB;

	// AWS Account associated emails
	@UiField VerticalPanel emailsVP;
	@UiField TextBox addEmailTF;
	@UiField ListBox addEmailTypeLB;
	@UiField Button addEmailButton;
	@UiField FlexTable emailTable;
	
	// AWS Account Administrator (net ids)
	@UiField VerticalPanel adminVP;
	@UiField FlexTable adminTable;
	@UiField Button addAdminButton;
	@UiField Label adminLabel;
	
	@UiField HTML speedTypeHTML;
	@UiField ListBox complianceClassLB;

	@UiField(provided=true) SuggestBox directoryLookupSB = new SuggestBox(personSuggestions, new TextBox());
	@UiField(provided=true) SuggestBox ownerIdSB = new SuggestBox(ownerIdSuggestions, new TextBox());
	@UiField VerticalPanel sensitiveDataPanel;

	@UiHandler ("speedTypeTB")
	void speedTypeMouseOver(MouseOverEvent e) {
		String acct = speedTypeTB.getText();
		presenter.setSpeedChartStatusForKeyOnWidget(acct, speedTypeTB, false);
	}
	@UiHandler ("speedTypeTB")
	void speedTypeKeyPressed(KeyPressEvent e) {
		this.setSpeedTypeConfirmed(false);
		GWT.log("SpeedType key pressed...");
		int keyCode = e.getNativeEvent().getKeyCode();
		char ccode = e.getCharCode();

		if (keyCode == KeyCodes.KEY_BACKSPACE) {
			if (speedTypeBeingTyped.length() > 0) {
				speedTypeBeingTyped = speedTypeBeingTyped.substring(0, speedTypeBeingTyped.length() - 1);
			}
			presenter.setSpeedChartStatusForKey(speedTypeBeingTyped, speedTypeHTML, false);
			return;
		}
		
		if (keyCode == KeyCodes.KEY_TAB) {
			presenter.setSpeedChartStatusForKey(speedTypeTB.getText(), speedTypeHTML, true);
			return;
		}

		if (!isValidKey(keyCode)) {
			return;
		}
		else {
			speedTypeBeingTyped += ccode;
		}

		presenter.setSpeedChartStatusForKey(speedTypeBeingTyped, speedTypeHTML, false);
	}
	
	@UiHandler ("billSummaryButton")
	void billSummaryButtonClick(ClickEvent e) {
		// show billing information for this account
		ActionEvent.fire(presenter.getEventBus(), ActionNames.SHOW_BILL_SUMMARY_FOR_ACCOUNT, presenter.getAccount());
	}
	
//	@UiHandler ("ownerIdSB")
//	void ownerIdSBMouseOver(MouseOverEvent e) {
//		DirectoryMetaDataPojo dmd = presenter.getAccount().getAccountOwnerDirectoryMetaData();
//		if (dmd != null) {
//			ownerIdSB.setTitle(dmd.getFirstName() + " " + 
//					dmd.getLastName() + 
//					" - From the IdentityService.");
//		}
//		else {
//			ownerIdSB.setTitle("Owner ID");
//		}
//	}
	@UiHandler ("addAdminButton")
	void addAdminButtonClick(ClickEvent e) {
		if (accountIdTB.getText() == null || accountIdTB.getText().trim().length() == 0) {
			showMessageToUser("Please enter a valid account ID.");
			return;
		}
		if (directoryLookupSB.getText() == null || directoryLookupSB.getText().trim().length() == 0) {
			showMessageToUser("Please enter the name of a person you want to assign a role to for this account.");
			return;
		}
		// present a dialog where user must select a role
		// then pass that role to the add method
		final RoleSelectionPopup rsp = new RoleSelectionPopup();
		rsp.setAssigneeName(directoryLookupSB.getText());
		rsp.initPanel();
		rsp.showRelativeTo(addAdminButton);
		rsp.addCloseHandler(new CloseHandler<PopupPanel>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onClose(CloseEvent event) {
				GWT.log("selected role is: " + rsp.getSelectedRoleName());
				if (!rsp.isCanceled()) {
					if (rsp.isRoleSelected()) {
						if (rsp.getSelectedRoleName() != null) {
							addDirectoryPersonInRoleToAccount(rsp.getSelectedRoleName());
						}
						else {
							Window.alert("You must select a role to assign this person to.");
						}
					}
					else {
						Window.alert("You must select a role to assign this person to.");
					}
				}
			}
		});
		
	}

	@UiHandler ("addEmailTF")
	void addEmailTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		addEmailToAccount(addEmailTF.getText(), addEmailTypeLB.getSelectedValue());
        }
	}
	@UiHandler ("addEmailButton")
	void addEmailButtonClick(ClickEvent e) {
		addEmailToAccount(addEmailTF.getText(), addEmailTypeLB.getSelectedValue());
	}
	@UiHandler ("okayButton")
	void okayButtonClick(ClickEvent e) {
		if (this.hasFieldViolations() == false) {
			presenter.getAccount().setAccountId(accountIdTB.getText());
			presenter.getAccount().setAccountName(accountNameTB.getText());
//			if (presenter.getAccount().getAccountOwnerDirectoryMetaData() == null) {
//				presenter.getAccount().setAccountOwnerDirectoryMetaData(new DirectoryMetaDataPojo());
//			}
//			presenter.getAccount().getAccountOwnerDirectoryMetaData().setNetId(ownerIdTB.getText());
			presenter.getAccount().setComplianceClass(complianceClassLB.getSelectedValue());
			presenter.getAccount().setPasswordLocation(passwordLocationTB.getText());
			presenter.getAccount().setSpeedType(speedTypeTB.getText());
			// emails are added as they're added in the interface
			
			if (this.isSpeedTypeConfirmed()) {
				presenter.saveAccount();
			}
			else {
				if (presenter.didConfirmSpeedType()) {
					presenter.saveAccount();
				}
				else {
					this.showMessageToUser("SpeedType has not been confirmed.  Can't save the account "
							+ "until the SpeedType is confirmed.");
					GWT.log("SpeedType has not been confirmed. Can't save account until SpeedType is confirmed.");
				}
			}
		}
		else {
			showMessageToUser("Please correct any field violations.");
		}
	}

	private static DesktopMaintainAccountUiBinder uiBinder = GWT.create(DesktopMaintainAccountUiBinder.class);

	interface DesktopMaintainAccountUiBinder extends UiBinder<Widget, DesktopMaintainAccount> {
	}

	public DesktopMaintainAccount() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.log("maintain account view init...");
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.ACCOUNT_EDITING_CANCELED);
			}
		}, ClickEvent.getType());
	}

	/*
	 * Role/Roleassignment helper methods
	 */
	private void addDirectoryPersonInRoleToAccount(String roleName) {
		// get fullperson for current directory person
		// get net id from fullperson
		// create role assignment
		presenter.getAccount().setAccountId(accountIdTB.getText());
		presenter.addDirectoryPersonInRoleToAccount(roleName);
	}
	
	/*
	 * Admin helper methods
	 */
	
	@Override
	public void addRoleAssignment(final int ra_summaryIndex, String name, final String netId, String roleName, String widgetTitle) {
//		int numRows = adminTable.getRowCount();
		final Label nameLabel = new Label(name + " (" + netId + ")/" + roleName);
		nameLabel.setTitle(widgetTitle);
		nameLabel.addStyleName("emailLabel");
//		nameLabel.addMouseOverHandler(new MouseOverHandler() {
//			@Override
//			public void onMouseOver(MouseOverEvent event) {
//				presenter.setDirectoryMetaDataTitleOnWidget(netId, nameLabel);
//			}
//		});
		final Button removeAdminButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (this.userLoggedIn.isAdminForAccount(presenter.getAccount().getAccountId()) ||
			this.userLoggedIn.isCentralAdmin()) {
			
			removeAdminButton.setEnabled(true);
		}
		else {
			removeAdminButton.setEnabled(false);
		}
		removeAdminButton.addStyleName("glowing-border");
		removeAdminButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// RoleAssignment.Delete
				presenter.removeRoleAssignmentFromAccount(presenter.getAccount().getAccountId(), 
						presenter.getRoleAssignmentSummaries().get(ra_summaryIndex));
				// TODO: move this to the presenter, after the assignment has actually been deleted
				adminTable.remove(nameLabel);
				adminTable.remove(removeAdminButton);
			}
		});
		directoryLookupSB.setText("");
		adminTable.setWidget(adminRowNum, adminColumnNum, nameLabel);
		adminTable.setWidget(adminRowNum, removeButtonColumnNum, removeAdminButton);
		adminRowNum++;
	}

	/*
	 *	associated email helper methods
	 */
	private void addEmailToAccount(String email, String type) {
		if (email != null && email.trim().length() > 0) {
			if (type != null && type.trim().length() > 0) {
				final String trimmedEmail = email.trim().toLowerCase();
				final String trimmedEmailType = type.trim().toLowerCase();
				EmailPojo emailPojo = new EmailPojo();
				emailPojo.setEmailAddress(trimmedEmail);
				emailPojo.setType(trimmedEmailType);
				if (presenter.getAccount().containsEmail(emailPojo)) {
					showStatus(addEmailButton, "That e-mail is already in the list, please enter a unique e-mail address.");
				}
				else {
					presenter.getAccount().getEmailList().add(emailPojo);
					addEmailToEmailPanel(emailPojo);
				}
			}
			else {
				showStatus(addEmailButton, "Please enter a valid e-mail address type.");
			}
		}
		else {
			showStatus(addEmailButton, "Please enter a valid e-mail address.");
		}
	}
	
	private void addEmailToEmailPanel(final EmailPojo email) {
		final int numRows = emailTable.getRowCount();
		final Label emailLabel = new Label(email.getEmailAddress() + "/" + email.getType());
		emailLabel.addStyleName("emailLabel");
		final Button removeEmailButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (this.userLoggedIn.isAdminForAccount(presenter.getAccount().getAccountId()) ||
			this.userLoggedIn.isCentralAdmin()) {
				
			removeEmailButton.setEnabled(true);
		}
		else {
			removeEmailButton.setEnabled(false);
		}
		removeEmailButton.addStyleName("glowing-border");
		removeEmailButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getAccount().getEmailList().remove(email);
				emailTable.remove(emailLabel);
				emailTable.remove(removeEmailButton);
			}
		});
		addEmailTF.setText("");
		addEmailTypeLB.setSelectedIndex(0);
		emailTable.setWidget(numRows, 0, emailLabel);
		emailTable.setWidget(numRows, 1, removeEmailButton);
	}

	void initializeEmailPanel() {
		emailTable.removeAllRows();
		if (presenter.getAccount() != null) {
			GWT.log("Adding " + presenter.getAccount().getEmailList().size() + " e-mails to the email panel.");
			for (EmailPojo emailPojo : presenter.getAccount().getEmailList()) {
				addEmailToEmailPanel(emailPojo);
			}
		}
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccountIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccountNameViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		// clear the page
		this.setFieldViolations(false);
		speedTypeBeingTyped = "";
		speedTypeHTML.setHTML("");
		adminRowNum = 0;
		adminColumnNum = 0;
		removeButtonColumnNum = 1;
		accountIdTB.setText("");
		accountNameTB.setText("");
		ownerIdSB.setText("");
		passwordLocationTB.setText("");
		speedTypeTB.setText("");
		addEmailTF.setText("");
		addEmailTypeLB.setSelectedIndex(0);

//		addNetIdTF.setText("");
//		addNetIdTF.getElement().setPropertyString("placeholder", "enter net id");

		addEmailTF.setText("");
		addEmailTF.getElement().setPropertyString("placeholder", "enter e-mail");
		addEmailTypeLB.getElement().setPropertyString("placeholder", "select e-mail type");

		directoryLookupSB.setText("");
		directoryLookupSB.getElement().setPropertyString("placeholder", "enter name");

		// populate fields if appropriate
		if (presenter.getAccount() != null) {
			accountIdTB.setText(presenter.getAccount().getAccountId());
			// account id can't be changed
			accountNameTB.setText(presenter.getAccount().getAccountName());
			if (presenter.getAccount().getAccountOwnerDirectoryMetaData() != null) {
				ownerIdSB.setText(presenter.getAccount().getAccountOwnerDirectoryMetaData().getFirstName() + 
						" " + presenter.getAccount().getAccountOwnerDirectoryMetaData().getLastName());
			}
			else {
				// enter placeholder text on ownerIdSB
				ownerIdSB.getElement().setPropertyString("placeholder", "enter name");
			}
			// TODO: add a static text object to show person's name from the meta data pojo
			passwordLocationTB.setText(presenter.getAccount().getPasswordLocation());
			speedTypeTB.setText(presenter.getAccount().getSpeedType());
			presenter.setSpeedChartStatusForKey(presenter.getAccount().getSpeedType(), speedTypeHTML, false);
		}
		
		registerHandlers();
		
		// populate associated emails if appropriate
		initializeEmailPanel();
		
		// populate sensitive data panel
		
		// populate admin net id fields if appropriate
		adminTable.clear();
//		initializeNetIdPanel();
	}
	
	private void registerHandlers() {
		directoryLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
					directoryLookupSB.setTitle(presenter.getDirectoryPerson().toString());
				}
			}
		});
		
		ownerIdSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					if (presenter.getAccount().getAccountOwnerDirectoryMetaData() == null) {
						presenter.getAccount().setAccountOwnerDirectoryMetaData(new DirectoryMetaDataPojo());
					}
					
					//TODO: set the ownerIdSB text to their name
					
					//set the PPID on AccountOwnerDirectoryMetaData.
					presenter.getAccount().getAccountOwnerDirectoryMetaData().
						setPublicId(dp_suggestion.getDirectoryPerson().getKey());
					
					// basically, we want to present their name but store their PPID
					
//					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
//					directoryLookupSB.setTitle(presenter.getDirectoryPerson().toString());
				}
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
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	accountIdTB.setFocus(true);
	        }
	    });
	}
	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setEmailTypeItems(List<String> emailTypes) {
		this.emailTypes = emailTypes;
		addEmailTypeLB.clear();
		if (emailTypes != null) {
			for (String emailType : emailTypes) {
				addEmailTypeLB.addItem(emailType, emailType);
			}
		}
	}
	@Override
	public void applyAWSAccountAdminMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		accountNameTB.setEnabled(false);
		ownerIdSB.setEnabled(false);
		if (editing) {
			if (presenter.getAccount() != null) {
				if (userLoggedIn.getPublicId().equalsIgnoreCase(presenter.getAccount().getAccountOwnerDirectoryMetaData().getPublicId())) {
					ownerIdSB.setEnabled(true);
				}
			}
		}
		else {
			// it's a create
			ownerIdSB.setEnabled(true);
		}
		passwordLocationTB.setEnabled(false);
		speedTypeTB.setEnabled(true);
		addEmailTF.setEnabled(false);
		addEmailTypeLB.setEnabled(false);
		addEmailButton.setEnabled(false);
		directoryLookupSB.setEnabled(true);
		addAdminButton.setEnabled(true);
		complianceClassLB.setEnabled(false);
		billSummaryButton.setVisible(false);
	}
	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		accountNameTB.setEnabled(false);
		ownerIdSB.setEnabled(false);
		passwordLocationTB.setEnabled(false);
		speedTypeTB.setEnabled(false);
		addEmailTF.setEnabled(false);
		addEmailTypeLB.setEnabled(false);
		addEmailButton.setEnabled(false);
		directoryLookupSB.setEnabled(false);
		addAdminButton.setEnabled(false);
		complianceClassLB.setEnabled(false);
		billSummaryButton.setVisible(false);
	}
	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}
	@Override
	public void setAwsAccountsURL(String awsAccountsURL) {
//		String acctInfo = accountInfoHTML.getHTML();
//		accountInfoHTML.setHTML(acctInfo.replaceAll("AWS_ACCOUNTS_URL", awsAccountsURL));
	}
	@Override
	public void setAwsBillingManagementURL(String awsBillingManagementURL) {
//		String acctInfo = accountInfoHTML.getHTML();
//		accountInfoHTML.setHTML(acctInfo.replaceAll("AWS_BILLING_MANAGEMENT_URL", awsBillingManagementURL));
	}
	@Override
	public void setSpeedTypeStatus(String status) {
		speedTypeHTML.setHTML(status);
	}
	@Override
	public void setSpeedTypeColor(String color) {
		speedTypeHTML.getElement().getStyle().setColor(color);
	}
	@Override
	public Widget getSpeedTypeWidget() {
		return speedTypeTB;
	}
	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		AccountPojo acct = presenter.getAccount(); 
		if (acct.getAccountId() == null || acct.getAccountId().length() == 0) {
			fields.add(accountIdTB);
		}
		if (acct.getAccountName() == null || acct.getAccountName().length() == 0) {
			fields.add(accountNameTB);
		}
		if (acct.getPasswordLocation() == null || acct.getPasswordLocation().length() == 0) {
			fields.add(passwordLocationTB);
		}
		if (acct.getEmailList() == null|| acct.getEmailList().size() == 0) {
			fields.add(addEmailTF);
		}
		if (acct.getComplianceClass() == null || acct.getComplianceClass().length() == 0) {
			fields.add(complianceClassLB);
		}
		return fields;
	}
	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(accountIdTB);
		fields.add(accountNameTB);
		fields.add(complianceClassLB);
		fields.add(passwordLocationTB);
		fields.add(addEmailTF);
		this.resetFieldStyles(fields);
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return okayButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return cancelButton;
	}
	@Override
	public void setSpeedTypeConfirmed(boolean confirmed) {
		this.speedTypeConfirmed = confirmed;
	}
	@Override
	public boolean isSpeedTypeConfirmed() {
		return this.speedTypeConfirmed;
	}
	@Override
	public void setRoleAssignmentSummaries(List<RoleAssignmentSummaryPojo> summaries) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setComplianceClassItems(List<String> complianceClassTypes) {
		this.complianceClassTypes = complianceClassTypes;
		complianceClassLB.clear();
		complianceClassLB.addItem("-- Select --");
		if (complianceClassLB != null) {
			int i=1;
			for (String type : complianceClassTypes) {
				complianceClassLB.addItem(type, type);
				if (presenter.getAccount() != null) {
					if (presenter.getAccount().getComplianceClass() != null) {
						if (presenter.getAccount().getComplianceClass().equals(type)) {
							complianceClassLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}
	@Override
	public void showPleaseWaitDialog(String pleaseWaitHTML) {
		adminPleaseWaitDialog = new PopupPanel(true);

		VerticalPanel vp = new VerticalPanel();
		Image img = new Image();
		img.setUrl("images/ajax-loader.gif");
		vp.add(img);
		HTML h = new HTML(pleaseWaitHTML);
		vp.add(h);
		vp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_CENTER);

		adminPleaseWaitDialog.setWidget(vp);
//		adminPleaseWaitDialog.center();
		adminPleaseWaitDialog.setPopupPosition(
				adminLabel.getOffsetWidth() / 2, 
				adminLabel.getAbsoluteTop() + 75);
		adminPleaseWaitDialog.show();
	}
	@Override
	public void hidePleaseWaitDialog() {
		super.hidePleaseWaitDialog();
		if (adminPleaseWaitDialog != null) {
			adminPleaseWaitDialog.hide();
		}
	}
	@Override
	public void applyCentralAdminMask() {
		okayButton.setEnabled(true);
		accountIdTB.setEnabled(true);
		accountNameTB.setEnabled(true);
		ownerIdSB.setEnabled(true);
		passwordLocationTB.setEnabled(true);
		speedTypeTB.setEnabled(true);
		addEmailTF.setEnabled(true);
		addEmailTypeLB.setEnabled(true);
		addEmailButton.setEnabled(true);
		directoryLookupSB.setEnabled(true);
		addAdminButton.setEnabled(true);
		complianceClassLB.setEnabled(true);
		billSummaryButton.setVisible(true);
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
	public void enableAdminMaintenance() {
		addAdminButton.setEnabled(true);
		directoryLookupSB.setEnabled(true);
	}
	
	@Override
	public void disableAdminMaintenance() {
		addAdminButton.setEnabled(false);
		directoryLookupSB.setEnabled(false);
	}
	
	@Override
	public void showWaitForNotificationsDialog(String message) {
		waitForNotificationsDialog = new PopupPanel(true);

		VerticalPanel vp = new VerticalPanel();
		Image img = new Image();
		img.setUrl("images/ajax-loader.gif");
		vp.add(img);
		HTML h = new HTML(message);
		vp.add(h);
		vp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_CENTER);

		waitForNotificationsDialog.setWidget(vp);
		waitForNotificationsDialog.setPopupPosition(
				notificationListPanel.getAbsoluteLeft() + 25, 
				notificationListPanel.getAbsoluteTop() + 25);
		waitForNotificationsDialog.show();
	}
	@Override
	public void hidWaitForNotificationsDialog() {
		if (waitForNotificationsDialog != null) {
			waitForNotificationsDialog.hide();
		}
	}
	@Override
	public void setAccountNotifications(List<AccountNotificationPojo> pojos) {
		this.pojoList = pojos;
		this.initializeListTable();
		listPager.setDisplay(listTable);
	}
	@Override
	public void removeAccountNotificationFromView(AccountNotificationPojo pojo) {
		dataProvider.getList().remove(pojo);
	}
	
	private Widget initializeListTable() {
		GWT.log("initializing account notification list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		listTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<AccountNotificationPojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.pojoList);
		
		selectionModel = 
	    	new MultiSelectionModel<AccountNotificationPojo>(AccountNotificationPojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
//	    		NotificationPojo m = selectionModel.getSelectedObject();
//	    		GWT.log("Selected service is: " + m.getNotificationId());
	    	}
	    });

	    ListHandler<AccountNotificationPojo> sortHandler = 
	    	new ListHandler<AccountNotificationPojo>(dataProvider.getList());
	    listTable.addColumnSortHandler(sortHandler);

	    if (listTable.getColumnCount() == 0) {
		    initListTableColumns(sortHandler);
	    }
		
		return listTable;
	}
	private void initListTableColumns(ListHandler<AccountNotificationPojo> sortHandler) {
		GWT.log("initializing account notification list table columns...");
		GWT.log("there are " + sortHandler.getList().size() + " user notifications in the list");

		// Notification id column
		Column<AccountNotificationPojo, String> typeColumn = 
				new Column<AccountNotificationPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(AccountNotificationPojo object) {
				return object.getType();
			}
		};
		typeColumn.setSortable(true);
		sortHandler.setComparator(typeColumn, new Comparator<AccountNotificationPojo>() {
			public int compare(AccountNotificationPojo o1, AccountNotificationPojo o2) {
				return o1.getType().compareTo(o2.getType());
			}
		});
		typeColumn.setFieldUpdater(new FieldUpdater<AccountNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, AccountNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT_NOTIFICATION, presenter.getAccount(), object);
	    	}
	    });
		typeColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(typeColumn, "Type");
		
		Column<AccountNotificationPojo, String> priorityColumn = 
				new Column<AccountNotificationPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(AccountNotificationPojo object) {
				return object.getPriority();
			}
		};
		priorityColumn.setSortable(true);
		sortHandler.setComparator(priorityColumn, new Comparator<AccountNotificationPojo>() {
			public int compare(AccountNotificationPojo o1, AccountNotificationPojo o2) {
				return o1.getPriority().compareTo(o2.getPriority());
			}
		});
	    priorityColumn.setFieldUpdater(new FieldUpdater<AccountNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, AccountNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT_NOTIFICATION, presenter.getAccount(), object);
	    	}
	    });
	    priorityColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(priorityColumn, "Priority");
		
		Column<AccountNotificationPojo, String> subjectColumn = 
				new Column<AccountNotificationPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(AccountNotificationPojo object) {
				return object.getSubject();
			}
	    };
	    subjectColumn.setFieldUpdater(new FieldUpdater<AccountNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, AccountNotificationPojo object, String value) {
	    		GWT.log("value updater for subject column");
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT_NOTIFICATION, presenter.getAccount(), object);
	    	}
	    });
		subjectColumn.setSortable(true);
		sortHandler.setComparator(subjectColumn, new Comparator<AccountNotificationPojo>() {
			public int compare(AccountNotificationPojo o1, AccountNotificationPojo o2) {
				return o1.getSubject().compareTo(o2.getSubject());
			}
		});
		subjectColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(subjectColumn, "Subject");

		Column<AccountNotificationPojo, String> createTime = 
				new Column<AccountNotificationPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(AccountNotificationPojo object) {
				Date createTime = object.getCreateTime();
				return createTime != null ? dateFormat.format(createTime) : "Unknown";
			}
		};
		createTime.setSortable(true);
		sortHandler.setComparator(createTime, new Comparator<AccountNotificationPojo>() {
			public int compare(AccountNotificationPojo o1, AccountNotificationPojo o2) {
				GWT.log("account notification create time sort handler...");
				Date c1 = o1.getCreateTime();
				Date c2 = o2.getCreateTime();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
	    createTime.setFieldUpdater(new FieldUpdater<AccountNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, AccountNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT_NOTIFICATION, presenter.getAccount(), object);
	    	}
	    });
	    createTime.setCellStyleNames("productAnchor");
		listTable.addColumn(createTime, "Create Time");
		
		Column<AccountNotificationPojo, String> updateTime = 
				new Column<AccountNotificationPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(AccountNotificationPojo object) {
				Date updateTime = object.getUpdateTime();
				return updateTime != null ? dateFormat.format(updateTime) : "Unknown";
			}
		};
		updateTime.setSortable(true);
		sortHandler.setComparator(updateTime, new Comparator<AccountNotificationPojo>() {
			public int compare(AccountNotificationPojo o1, AccountNotificationPojo o2) {
				Date c1 = o1.getUpdateTime();
				Date c2 = o2.getUpdateTime();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
	    updateTime.setFieldUpdater(new FieldUpdater<AccountNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, AccountNotificationPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT_NOTIFICATION, presenter.getAccount(), object);
	    	}
	    });
	    updateTime.setCellStyleNames("productAnchor");
		listTable.addColumn(updateTime, "Update Time");
	}
	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}
}
