package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
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
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
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
	List<String> filterTypeItems;
	boolean isCimpInstance=false;
    
	private ListDataProvider<AccountNotificationPojo> dataProvider = new ListDataProvider<AccountNotificationPojo>();
	private MultiSelectionModel<AccountNotificationPojo> selectionModel;
	List<AccountNotificationPojo> pojoList = new java.util.ArrayList<AccountNotificationPojo>();
	PopupPanel actionsPopup = new PopupPanel(true);

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	@UiField VerticalPanel notificationListPanel;
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) CellTable<AccountNotificationPojo> listTable = new CellTable<AccountNotificationPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button billSummaryButton;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox accountIdTB;
	@UiField TextBox accountNameTB;
	@UiField TextBox alternateNameTB;
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
	@UiField PushButton refreshButton;
	
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField ListBox filterTypesLB;
	@UiField HTML filteredHTML;

	// AWS Account associated properties
	@UiField VerticalPanel propertiesVP;
	@UiField TextBox propertyKeyTF;
	@UiField TextBox propertyValueTF;
	@UiField Button addPropertyButton;
	@UiField FlexTable propertiesTable;
	
	@UiField Label speedTypeLabel;
	
	@UiHandler ("addPropertyButton")
	void addElasticIpButtonClick(ClickEvent e) {
		propertyKeyTF.setEnabled(true);
		if (addPropertyButton.getText().equalsIgnoreCase("Add")) {
			addProperty();
		}
		else {
			// update existing property
			PropertyPojo property = createPropertyFromFormData();
			presenter.updateProperty(property);
			initializeAccountPropertiesPanel();
		}
		propertyKeyTF.setText("");
		propertyValueTF.setText("");
	}
	
	private void initializeAccountPropertiesPanel() {
		addPropertyButton.setText("Add");
		propertyKeyTF.setEnabled(true);
		propertiesTable.removeAllRows();
		for (PropertyPojo prop : presenter.getAccount().getProperties()) {
			this.addPropertyToPanel(prop);
		}
	}

	private void addPropertyToPanel(final PropertyPojo prop) {
		final int numRows = propertiesTable.getRowCount();
		
		final Label keyLabel = new Label(prop.getName());
		keyLabel.addStyleName("emailLabel");
		
		final Label valueLabel = new Label(prop.getValue());
		valueLabel.addStyleName("emailLabel");
		
		final PushButton editButton = new PushButton();
		editButton.setTitle("Edit this Property.");
		Image editImage = new Image("images/edit_icon.png");
		editImage.setWidth("30px");
		editImage.setHeight("30px");
		editButton.getUpFace().setImage(editImage);
		if (this.userLoggedIn.isCentralAdmin()) {
			editButton.setEnabled(true);
		}
		else {
			editButton.setEnabled(false);
		}
		editButton.addStyleName("glowing-border");
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.setSelectedProperty(prop);
				propertyKeyTF.setText(prop.getName());
				propertyKeyTF.setEnabled(false);
				propertyValueTF.setText(prop.getValue());
				addPropertyButton.setText("Update");
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
			        public void execute () {
			        	propertyValueTF.setFocus(true);
			        }
			    });
			}
		});
		
		final PushButton removeButton = new PushButton();
		removeButton.setTitle("Remove this Property.");
		Image removeImage = new Image("images/delete_icon.png");
		removeImage.setWidth("30px");
		removeImage.setHeight("30px");
		removeButton.getUpFace().setImage(removeImage);
		// disable buttons if userLoggedIn is NOT a central admin
		if (this.userLoggedIn.isCentralAdmin()) {
			removeButton.setEnabled(true);
		}
		else {
			removeButton.setEnabled(false);
		}
		removeButton.addStyleName("glowing-border");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getAccount().getProperties().remove(prop);
				initPage();
			}
		});
		
		propertiesTable.setWidget(numRows, 0, keyLabel);
		propertiesTable.setWidget(numRows, 1, new Label("="));
		propertiesTable.setWidget(numRows, 2, valueLabel);
		propertiesTable.setWidget(numRows, 3, editButton);
		propertiesTable.setWidget(numRows, 4, removeButton);
	}

	private void addProperty() {
		List<Widget> fields = getMissingPropertyFields();
		if (fields != null && fields.size() > 0) {
			setFieldViolations(true);
			applyStyleToMissingFields(fields);
			showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			setFieldViolations(false);
			resetFieldStyles();
		}
		PropertyPojo property = createPropertyFromFormData();
		presenter.getAccount().getProperties().add(property);
		addPropertyToPanel(property);
		this.resetFieldStyles();
	}

	private PropertyPojo createPropertyFromFormData() {
		PropertyPojo prop = new PropertyPojo();
		prop.setName(propertyKeyTF.getText());
		prop.setValue(propertyValueTF.getText());
		return prop;
	}

	private List<Widget> getMissingPropertyFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (propertyKeyTF.getText() == null || propertyKeyTF.getText().length() == 0) {
			fields.add(propertyKeyTF);
		}
		if (propertyValueTF.getText() == null || propertyValueTF.getText().length() == 0) {
			fields.add(propertyValueTF);
		}
		return fields;
	}

	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		String filterType = filterTypesLB.getSelectedValue();
		String filterValue = filterTB.getText();
		
		if ((filterType != null && filterType.length() > 0) &&
			 (filterValue != null && filterValue.length() > 0)) {
			
			if (filterType.equalsIgnoreCase(Constants.USR_NOT_FILTER_SUBJECT)) {
				presenter.filterBySubject(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.USR_NOT_FILTER_REF_ID)) {
				presenter.filterByReferenceId(filterValue);
			}
			else {
				// invalid filter type...but how?
			}
		}
		else {
			this.showMessageToUser("Please enter a Filter Value AND select a Filter Type");
		}
	}
	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		filterTB.setText("");
		presenter.setAccountNotificationFilter(null);
		presenter.refreshAccountNotificationList(userLoggedIn);
		this.hideFilteredStatus();
	}

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		presenter.refreshAccountNotificationList(userLoggedIn);
	}

	@UiHandler ("speedTypeTB")
	void speedTypeBlur(BlurEvent e) {
		// check CIMP status.  If we are a CIMP instance, we don't need to confirm speed type
		if (isCimpInstance) {
			return;
		}
		presenter.setSpeedChartStatusForKey(speedTypeTB.getText(), speedTypeHTML, true);
	}
	@UiHandler ("speedTypeTB")
	void speedTypeMouseOver(MouseOverEvent e) {
		// check CIMP status.  If we are a CIMP instance, we don't need to confirm speed type
		if (isCimpInstance) {
			return;
		}
		String acct = speedTypeTB.getText();
		presenter.setSpeedChartStatusForKeyOnWidget(acct, speedTypeTB, false);
	}
	@UiHandler ("speedTypeTB")
	void speedTypeKeyDown(KeyDownEvent e) {
		// check CIMP status.  If we are a CIMP instance, we don't need to confirm speed type
		if (isCimpInstance) {
			return;
		}
		this.setSpeedTypeConfirmed(false);
		int keyCode = e.getNativeKeyCode();
		char ccode = (char)keyCode;

		if (keyCode == KeyCodes.KEY_BACKSPACE) {
			if (speedTypeBeingTyped != null) {
				if (speedTypeBeingTyped.length() > 0) {
					speedTypeBeingTyped = speedTypeBeingTyped.substring(0, speedTypeBeingTyped.length() - 1);
				}
				presenter.setSpeedChartStatusForKey(speedTypeBeingTyped, speedTypeHTML, false);
				return;
			}
		}
		
		if (!isValidKey(keyCode)) {
			GWT.log("[speedTypeKeyPressed] invalid key: " + keyCode);
			return;
		}
		else {
			if (speedTypeBeingTyped == null) {
				speedTypeBeingTyped = "";
			}
			speedTypeBeingTyped += String.valueOf(ccode);
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
							showMessageToUser("You must select a role to assign this person to.");
						}
					}
					else {
						showMessageToUser("You must select a role to assign this person to.");
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
		presenter.getAccount().setAccountId(accountIdTB.getText());
		presenter.getAccount().setAccountName(accountNameTB.getText());
		String alternateName = alternateNameTB.getText();
		if (alternateName != null && alternateName.trim().length() > 0) {
			if (!isAlphaNumeric(alternateName)) {
				setFieldViolations(true);
				List<Widget> fields = new java.util.ArrayList<Widget>();
				fields.add(alternateNameTB);
				applyStyleToMissingFields(fields);
				showMessageToUser("Alternate name can only consist of numbers and letters.  "
					+ "Special characters are not allowed.  Please enter a valid alternate name.");
				return;
			}
		}
		setFieldViolations(false);
		resetFieldStyles();
		presenter.getAccount().setAlternateName(alternateName);
		presenter.getAccount().setComplianceClass(complianceClassLB.getSelectedValue());
		presenter.getAccount().setPasswordLocation(passwordLocationTB.getText());
		presenter.getAccount().setSpeedType(speedTypeTB.getText());
		// emails are added as they're added in the interface
		
		// check CIMP status.  If we are a CIMP instance, we don't need to confirm speed type
		if (isCimpInstance) {
			presenter.saveAccount();
		}
		else {
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
	}

	private static DesktopMaintainAccountUiBinder uiBinder = GWT.create(DesktopMaintainAccountUiBinder.class);

	interface DesktopMaintainAccountUiBinder extends UiBinder<Widget, DesktopMaintainAccount> {
	}

	public DesktopMaintainAccount() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
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
		if (this.userLoggedIn.isCentralAdmin()) {
				
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
		if (isEditing) {
			// account id is immutable when/if the account is being edited
			accountIdTB.setEnabled(false);
		}
	}

	@Override
	public void setLocked(boolean locked) {
		
		
	}

	@Override
	public void setAccountIdViolation(String message) {
		
		
	}

	@Override
	public void setAccountNameViolation(String message) {
		
		
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
		alternateNameTB.setText("");
		ownerIdSB.setText("");
		passwordLocationTB.setText("");
		speedTypeTB.setText("");
		addEmailTF.setText("");
		addEmailTypeLB.setSelectedIndex(0);

		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter search string");

		addEmailTF.setText("");
		addEmailTF.getElement().setPropertyString("placeholder", "enter e-mail");
		addEmailTypeLB.getElement().setPropertyString("placeholder", "select e-mail type");

		addPropertyButton.setText("Add");
		propertyKeyTF.setText("");
		propertyKeyTF.getElement().setPropertyString("placeholder", "enter property key");
		propertyValueTF.setText("");
		propertyValueTF.getElement().setPropertyString("placeholder", "enter property value");

		directoryLookupSB.setText("");
		directoryLookupSB.getElement().setPropertyString("placeholder", "enter name");

		// populate fields if appropriate
		if (presenter.getAccount() != null) {
			accountIdTB.setText(presenter.getAccount().getAccountId());
			// account id can't be changed
			accountNameTB.setText(presenter.getAccount().getAccountName());
			alternateNameTB.setText(presenter.getAccount().getAlternateName());
			if (presenter.getAccount().getAccountOwnerDirectoryMetaData() != null) {
				ownerIdSB.setText(presenter.getAccount().getAccountOwnerDirectoryMetaData().getFirstName() + 
						" " + presenter.getAccount().getAccountOwnerDirectoryMetaData().getLastName());
				presenter.setDirectoryMetaDataTitleOnWidget(presenter.getAccount().getAccountOwnerDirectoryMetaData().getPublicId(), ownerIdSB);
			}
			else {
				// enter placeholder text on ownerIdSB
				ownerIdSB.getElement().setPropertyString("placeholder", "enter name");
			}
			// TODO: add a static text object to show person's name from the meta data pojo
			passwordLocationTB.setText(presenter.getAccount().getPasswordLocation());
			speedTypeTB.setText(presenter.getAccount().getSpeedType());
			// check CIMP status.  If we are a CIMP instance, we don't need to confirm speed type
			if (!isCimpInstance) {
				presenter.setSpeedChartStatusForKey(presenter.getAccount().getSpeedType(), speedTypeHTML, false);
			}
		}
		
		registerHandlers();
		
		// populate associated emails if appropriate
		initializeEmailPanel();
		
		// populate properties panel
		initializeAccountPropertiesPanel();
		
		// populate admin net id fields if appropriate
		adminTable.clear();
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
	        	if (!editing) {
		        	accountIdTB.setFocus(true);
	        	}
	        	else {
	        		accountNameTB.setFocus(true);
	        	}
	        }
	    });
	}
	@Override
	public Widget getStatusMessageSource() {
		
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
		GWT.log("Applying account admin mask...");
		okayButton.setEnabled(true);
		accountIdTB.setEnabled(false);
		accountNameTB.setEnabled(false);
		alternateNameTB.setEnabled(true);
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
		propertyKeyTF.setEnabled(false);
		propertyValueTF.setEnabled(false);
		addPropertyButton.setEnabled(false);
	}
	@Override
	public void applyAWSAccountAuditorMask() {
		GWT.log("Applying account auditor mask...");
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		accountNameTB.setEnabled(false);
		alternateNameTB.setEnabled(false);
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
		propertyKeyTF.setEnabled(false);
		propertyValueTF.setEnabled(false);
		addPropertyButton.setEnabled(false);
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
		
		
	}
	@Override
	public void setComplianceClassItems(List<String> complianceClassTypes) {
		this.complianceClassTypes = complianceClassTypes;
		complianceClassLB.clear();
		complianceClassLB.addItem("-- Select --", "");
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
		GWT.log("Applying central admin mask...");
		okayButton.setEnabled(true);
		if (!editing) {
			// account id is immutable when/if the account is being edited
			// so, only enable the account id field if it's a new account
			accountIdTB.setEnabled(true);
		}
		accountNameTB.setEnabled(true);
		alternateNameTB.setEnabled(true);
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
		propertyKeyTF.setEnabled(true);
		propertyValueTF.setEnabled(true);
		addPropertyButton.setEnabled(true);
	}
	@Override
	public void vpcpPromptOkay(String valueEntered) {
		
		
	}
	@Override
	public void vpcpPromptCancel() {
		
		
	}
	@Override
	public void vpcpConfirmOkay() {
		
		
	}
	@Override
	public void vpcpConfirmCancel() {
		
		
	}
	@Override
	public void enableAdminMaintenance() {
		GWT.log("enabling admin maintenance...");
		addAdminButton.setEnabled(true);
		directoryLookupSB.setEnabled(true);
	}
	
	@Override
	public void disableAdminMaintenance() {
		GWT.log("disabling admin maintenance...");
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
		waitForNotificationsDialog.center();
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
		typeColumn.setCellStyleNames("tableAnchor");
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
	    priorityColumn.setCellStyleNames("tableAnchor");
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
		subjectColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(subjectColumn, "Subject");

		// Reference id column
		Column<AccountNotificationPojo, String> referenceId = 
			new Column<AccountNotificationPojo, String> (new ClickableTextCell()) {
			
			@Override
			public String getValue(AccountNotificationPojo object) {
				return object.getReferenceid();
			}
		};
		referenceId.setSortable(true);
		sortHandler.setComparator(referenceId, new Comparator<AccountNotificationPojo>() {
			public int compare(AccountNotificationPojo o1, AccountNotificationPojo o2) {
				return o1.getReferenceid().compareTo(o2.getReferenceid());
			}
		});
		referenceId.setFieldUpdater(new FieldUpdater<AccountNotificationPojo, String>() {
	    	@Override
	    	public void update(int index, AccountNotificationPojo object, String value) {
	    		if (object != null && object.getReferenceid() != null && object.getReferenceid().length() > 0) {
		    		presenter.showSrdForAccountNotification(object);
	    		}
	    		else {
	    			showMessageToUser("Notification doesn't have a reference id to link to.");
	    		}
	    	}
	    });
		referenceId.setCellStyleNames("tableAnchor");
		listTable.addColumn(referenceId, "Reference ID");

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
	    createTime.setCellStyleNames("tableAnchor");
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
	    updateTime.setCellStyleNames("tableAnchor");
		listTable.addColumn(updateTime, "Update Time");
	}
	@Override
	public void disableButtons() {
		
		
	}
	@Override
	public void enableButtons() {
		
		
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
		
		
	}

	@Override
	public void setFilterTypeItems(List<String> filterTypes) {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter filter value");

		this.filterTypeItems = filterTypes;
		filterTypesLB.clear();
		
		filterTypesLB.addItem("-- Select Filter Type --", "");
		if (filterTypeItems != null) {
			for (String filterType : filterTypeItems) {
				filterTypesLB.addItem(filterType, filterType);
			}
		}
	}

	@Override
	public void setCimpInstance(boolean isCimpInstance) {
		this.isCimpInstance = isCimpInstance;
	}

	@Override
	public void setFinancialAccountFieldLabel(String label) {
		speedTypeLabel.setText(label);
	}
}
