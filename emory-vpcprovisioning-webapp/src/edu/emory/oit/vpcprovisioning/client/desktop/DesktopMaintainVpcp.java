package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonSuggestion;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;

public class DesktopMaintainVpcp  extends ViewImplBase implements MaintainVpcpView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	List<String> complianceClassTypes;
	List<String> vpcTypes;
	List<AccountPojo> accounts;
	UserAccountPojo userLoggedIn;
	int netIdRowNum = 0;
	int netIdColumnNum = 0;
	int removeButtonColumnNum = 1;
	String speedTypeBeingTyped=null;
	boolean speedTypeConfirmed = false;
	private final DirectoryPersonRpcSuggestOracle personSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);

	private static DesktopMaintainVpcpUiBinder uiBinder = GWT.create(DesktopMaintainVpcpUiBinder.class);

	interface DesktopMaintainVpcpUiBinder extends UiBinder<Widget, DesktopMaintainVpcp> {
	}

	@UiField
	Button okayButton;
	Button cancelButton;
	@UiField HorizontalPanel pleaseWaitPanel;

	// used for maintaining vpc (irrelevant for now i think)
	@UiField Grid maintainVpcpGrid;
	@UiField TextBox provisioningIdTB;
	@UiField ListBox vpcTypeLB;
	
	// used when generating vpc
	@UiField VerticalPanel generateVpcpPanel;
	@UiField TextBox vpcpReqTicketIdTB;
	@UiField TextBox vpcpReqRequestorNetIdTB;
	@UiField TextBox vpcpReqOwnerNetIdTB;
	@UiField TextBox vpcpReqSpeedTypeTB;
	@UiField ListBox vpcpReqTypeLB;
	@UiField ListBox vpcpReqComplianceClassLB;
	@UiField CheckBox fismaCB;
	@UiField CheckBox pciCB;
	@UiField CheckBox vpcpReqNotifyAdminsCB;
	@UiField ListBox accountLB;
	@UiField CaptionPanel accountCP;
	@UiField HTML speedTypeHTML;
	@UiField TextArea vpcpReqPurposeTA;

	@UiField VerticalPanel adminVP;
	@UiField(provided=true) SuggestBox directoryLookupSB = new SuggestBox(personSuggestions, new TextBox());
	@UiField Button addAdminButton;
	@UiField FlexTable adminTable;

	@UiHandler ("vpcpReqSpeedTypeTB")
	void speedTypeMouseOver(MouseOverEvent e) {
		String acct = vpcpReqSpeedTypeTB.getText();
		presenter.setSpeedChartStatusForKeyOnWidget(acct, vpcpReqSpeedTypeTB, false);
	}
	@UiHandler ("vpcpReqSpeedTypeTB")
	void speedTypeKeyPressed(KeyPressEvent e) {
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
			presenter.setSpeedChartStatusForKey(vpcpReqSpeedTypeTB.getText(), speedTypeHTML, true);
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

	@UiHandler ("directoryLookupSB")
	void directoryLookupSBKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		presenter.addAdminDirectoryPersonToVpcp();
        }
	}
	@UiHandler ("addAdminButton")
	void addAdminButtonClick(ClickEvent e) {
		presenter.addAdminDirectoryPersonToVpcp();
	}
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		GWT.log("VPCP generation canceled...");
		ActionEvent.fire(presenter.getEventBus(), ActionNames.VPCP_EDITING_CANCELED);
	}

	public DesktopMaintainVpcp() {
		initWidget(uiBinder.createAndBindUi(this));

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (fismaCB.getValue() || pciCB.getValue()) {
					showMessageToUser("Please contact LITS Security.");
					return;
				}
				// populate vpcrequisition that will be used as seed data
				// for the vpcp.generate
				presenter.getVpcRequisition().setTicketId(vpcpReqTicketIdTB.getText());
				presenter.getVpcRequisition().setAuthenticatedRequestorNetId(vpcpReqRequestorNetIdTB.getText());
				presenter.getVpcRequisition().setAccountOwnerNetId(vpcpReqOwnerNetIdTB.getText());
				presenter.getVpcRequisition().setSpeedType(vpcpReqSpeedTypeTB.getText());
				presenter.getVpcRequisition().setComplianceClass(vpcpReqComplianceClassLB.getSelectedValue());
				presenter.getVpcRequisition().setNotifyAdmins(vpcpReqNotifyAdminsCB.getValue());
				presenter.getVpcRequisition().setAccountId(accountLB.getSelectedValue());
				presenter.getVpcRequisition().setType(vpcpReqTypeLB.getSelectedValue());
				presenter.getVpcRequisition().setPurpose(vpcpReqPurposeTA.getText());
				// customer admin net id list is already maintained as they add/remove them
				
				presenter.saveVpcp();
				GWT.log("VPCP saved...");
			}
		}, ClickEvent.getType());
		
		accountLB.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = accountLB.getSelectedIndex() - 1;
				accountCP.clear();
				if (index >= 0) {
					AccountPojo acct = accounts.get(index);
					presenter.setSelectedAccount(acct);
					GWT.log("selected account is: " + acct.getAccountName());
					accountCP.add(new HTML("Account Name: " + acct.getAccountName()));
				}
			}
		});
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
	}

	@Override
	public void setInitialFocus() {
		if (editing) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	provisioningIdTB.setFocus(true);
		        }
		    });
		}
		else {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	accountLB.setFocus(true);
		        }
		    });
		}
	}

	/*
	 * Admin net id helper methods
	 */
//	private void addNetIdToVpcp(String netId) {
//		if (netId != null && netId.trim().length() > 0) {
//			final String trimmedNetId = netId.trim().toLowerCase();
//			if (presenter.getVpcRequisition().getCustomerAdminNetIdList().contains(trimmedNetId)) {
//				showStatus(addNetIdButton, "That net id is alreay in the list, please enter a unique net id.");
//			}
//			else {
//				presenter.getVpcRequisition().getCustomerAdminNetIdList().add(trimmedNetId);
//				addNetIdToPanel(trimmedNetId);
//			}
//		}
//		else {
//			showStatus(addNetIdButton, "Please enter a valid net id.");
//		}
//	}
	private void addNetIdToPanel(final String netId) {
		int numRows = adminTable.getRowCount();
		final Label netIdLabel = new Label(netId);
		netIdLabel.addStyleName("emailLabel");
		netIdLabel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				presenter.setDirectoryMetaDataTitleOnWidget(netId, netIdLabel);
			}
		});
		final Button removeNetIdButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin		
		if (userLoggedIn.isAdminForAccount(presenter.getVpcRequisition().getAccountId()) || 
			userLoggedIn.isCentralAdmin()) {
			
			removeNetIdButton.setEnabled(true);
		}
		else {
			removeNetIdButton.setEnabled(false);
		}
		removeNetIdButton.addStyleName("glowing-border");
		removeNetIdButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getVpcRequisition().getCustomerAdminNetIdList().remove(netId);
				adminTable.remove(netIdLabel);
				adminTable.remove(removeNetIdButton);
			}
		});
		directoryLookupSB.setText("");
		if (numRows > 6) {
			if (netIdRowNum > 5) {
				netIdRowNum = 0;
				netIdColumnNum = netIdColumnNum + 2;
				removeButtonColumnNum = removeButtonColumnNum + 2;
			}
			else {
				netIdRowNum ++;
			}
		}
		else {
			netIdRowNum = numRows;
		}
		adminTable.setWidget(netIdRowNum, netIdColumnNum, netIdLabel);
		adminTable.setWidget(netIdRowNum, removeButtonColumnNum, removeNetIdButton);
	}

	void initializeNetIdPanel() {
		adminTable.removeAllRows();
		if (presenter.getVpcRequisition() != null) {
			GWT.log("Adding " + presenter.getVpcRequisition().getCustomerAdminNetIdList().size() + " net ids to the panel (update).");
			for (String netId : presenter.getVpcRequisition().getCustomerAdminNetIdList()) {
				addNetIdToPanel(netId);
			}
		}
	}


	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		okayButton.setEnabled(true);
		provisioningIdTB.setEnabled(true);
		vpcTypeLB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		provisioningIdTB.setEnabled(false);
		vpcTypeLB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setVpcpIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initPage() {
		this.setFieldViolations(false);
		this.registerHandlers();
		directoryLookupSB.setText("");
		directoryLookupSB.getElement().setPropertyString("placeholder", "enter name");
		if (editing) {
			GWT.log("maintain VPCP view initPage.  editing");
			// hide generate grid, show maintain grid
			generateVpcpPanel.setVisible(false);
			maintainVpcpGrid.setVisible(true);
			// clear the page
			provisioningIdTB.setText("");
			vpcTypeLB.setSelectedIndex(0);

			if (presenter.getVpcp() != null) {
				GWT.log("maintain VPCP view initPage.  VPCP: " + presenter.getVpcp().getProvisioningId());
				provisioningIdTB.setText(presenter.getVpcp().getProvisioningId());
			}
		}
		else {
			GWT.log("maintain VPCP view initPage.  generate");
			// hide maintain grid, show generate grid
			maintainVpcpGrid.setVisible(false);
			generateVpcpPanel.setVisible(true);
			vpcpReqOwnerNetIdTB.setText("");
			vpcpReqSpeedTypeTB.setText("");
			vpcpReqTicketIdTB.setText("");
			vpcpReqRequestorNetIdTB.setText("");
			vpcpReqPurposeTA.setText("");
			
			vpcpReqTypeLB.setSelectedIndex(0);
			accountLB.setSelectedIndex(0);
			vpcpReqComplianceClassLB.setSelectedIndex(0);
			vpcpReqNotifyAdminsCB.setValue(false);
			accountCP.clear();
			
			// populate admin net id fields if appropriate
			initializeNetIdPanel();
		}
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
	public void setVpcTypeItems(List<String> vpcTypes) {
		this.vpcTypes = vpcTypes;
		if (editing) {
			vpcTypeLB.clear();
			if (vpcTypes != null) {
				int i=1;
				for (String type : vpcTypes) {
					vpcTypeLB.addItem("Type: " + type, type);
					if (presenter.getVpcp() != null) {
						if (presenter.getVpcp().getVpcRequisition().getType() != null) {
							if (presenter.getVpcp().getVpcRequisition().getType().equals(type)) {
								vpcTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
		else {
			vpcpReqTypeLB.clear();
			vpcpReqTypeLB.addItem("-- Select --");
			if (vpcTypes != null) {
				int i=1;
				for (String type : vpcTypes) {
					vpcpReqTypeLB.addItem("Type: " + type, type);
					if (presenter.getVpcRequisition() != null) {
						if (presenter.getVpcRequisition().getType() != null) {
							if (presenter.getVpcRequisition().getType().equals(type)) {
								vpcpReqTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
	}
	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		this.accounts = accounts;
		accountLB.clear();
		accountLB.addItem("-- Select --");
		if (accounts != null) {
			for (AccountPojo account : accounts) {
				accountLB.addItem(account.getAccountId(), account.getAccountId());
			}
		}
	}
	@Override
	public void setComplianceClassItems(List<String> complianceClassTypes) {
		this.complianceClassTypes = complianceClassTypes;
		vpcpReqComplianceClassLB.clear();
		vpcpReqComplianceClassLB.addItem("-- Select --");
		if (complianceClassTypes != null) {
			for (String type : complianceClassTypes) {
				vpcpReqComplianceClassLB.addItem(type, type);
			}
		}
		
	}

	@Override
	public Widget getSpeedTypeWidget() {
		return vpcpReqSpeedTypeTB;
	}
	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		VpcRequisitionPojo vpcr = presenter.getVpcRequisition();
		if (vpcr.getAuthenticatedRequestorNetId() == null || vpcr.getAuthenticatedRequestorNetId().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpcpReqRequestorNetIdTB);
		}
		if (vpcr.getAccountOwnerNetId() == null || vpcr.getAccountOwnerNetId().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpcpReqOwnerNetIdTB);
		}
		if (vpcr.getSpeedType() == null || vpcr.getSpeedType().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpcpReqSpeedTypeTB);
		}
		if (vpcr.getCustomerAdminNetIdList() == null || vpcr.getCustomerAdminNetIdList().size() == 0) {
			this.setFieldViolations(true);
			fields.add(directoryLookupSB);
		}
		if (vpcr.getType() == null || vpcr.getType().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpcTypeLB);
		}
		if (vpcr.getComplianceClass() == null || vpcr.getComplianceClass().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpcpReqComplianceClassLB);
		}
		return fields;
	}
	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(vpcpReqRequestorNetIdTB);
		fields.add(vpcpReqOwnerNetIdTB);
		fields.add(vpcpReqSpeedTypeTB);
		fields.add(directoryLookupSB);
		fields.add(vpcTypeLB);
		fields.add(vpcpReqComplianceClassLB);
		this.resetFieldStyles(fields);
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return okayButton;
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
	public void setSpeedTypeConfirmed(boolean confirmed) {
		this.speedTypeConfirmed = confirmed;
	}
	@Override
	public boolean isSpeedTypeConfirmed() {
		return this.speedTypeConfirmed;
	}
	@Override
	public void addAdminNetId(String netId) {
		this.addNetIdToPanel(netId);
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
