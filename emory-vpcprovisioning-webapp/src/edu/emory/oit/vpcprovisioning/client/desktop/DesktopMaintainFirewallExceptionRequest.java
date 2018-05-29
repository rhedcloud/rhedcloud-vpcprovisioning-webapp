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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.firewall.MaintainFirewallExceptionRequestView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainFirewallExceptionRequest extends ViewImplBase implements MaintainFirewallExceptionRequestView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	boolean editing;
	List<String> complianceClassTypes;
	List<String> timeRules;

	private static DesktopMaintainFirewallRuleUiBinder uiBinder = GWT.create(DesktopMaintainFirewallRuleUiBinder.class);

	interface DesktopMaintainFirewallRuleUiBinder extends UiBinder<Widget, DesktopMaintainFirewallExceptionRequest> {
	}

	public DesktopMaintainFirewallExceptionRequest() {
		initWidget(uiBinder.createAndBindUi(this));
		
		timeRuleLB.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String rule = timeRuleLB.getSelectedValue();
				GWT.log("selected timeRule is: " + rule);
				if (rule.trim().equalsIgnoreCase(Constants.TIME_RULE_INDEFINITELY)) {
					hideValidUntilDB();
				}
				else {
					showValidUntilDB();
				}
			}
		});
	}

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	
	@UiField TextBox netIdTB;
	@UiField TextBox technicalContactTB;
	@UiField TextBox applicationNameTB;
	@UiField CheckBox isSourceOutsideEmoryCB;
	@UiField ListBox timeRuleLB;
	@UiField DateBox validUntilDB;
	@UiField Label validUntilLabel;
	@UiField TextArea sourceIpAddressesTA;
	@UiField TextArea destinationIpAddressesTA;
	@UiField TextArea portsTA;
	@UiField TextArea businessReasonTA;
	@UiField CheckBox isPatchedCB;
	@UiField CheckBox isDefaultPasswordChangedCB;
	@UiField CheckBox isAppConsoleACLedCB;
	@UiField CheckBox isHardenedCB;
	@UiField TextArea patchingPlanTA;
	@UiField ListBox complianceClassLB;
	@UiField TextArea sensitiveDataDescriptionTA;
	@UiField TextArea localFirewallRulesDescriptionTA;
	@UiField CheckBox isDefaultDenyZoneCB;

	int tagRowNum = 0;
	int tagColumnNum = 0;
	int removeTagButtonColumnNum = 1;
	@UiField TextBox addTagTF;
	@UiField VerticalPanel tagsVP;
	@UiField FlexTable tagsTable;
	@UiField Button addTagButton;

	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.getFirewallExceptionRequest().setUserNetId(netIdTB.getText());
		presenter.getFirewallExceptionRequest().setTechnicalContact(technicalContactTB.getText());
		presenter.getFirewallExceptionRequest().setApplicationName(applicationNameTB.getText());
		presenter.getFirewallExceptionRequest().setIsSourceOutsideEmory(isSourceOutsideEmoryCB.getValue() ? "Yes" : "No");
		presenter.getFirewallExceptionRequest().setTimeRule(timeRuleLB.getSelectedValue());
		presenter.getFirewallExceptionRequest().setValidUntilDate(validUntilDB.getValue());
		presenter.getFirewallExceptionRequest().setSourceIp(sourceIpAddressesTA.getText());
		presenter.getFirewallExceptionRequest().setDestinationIp(destinationIpAddressesTA.getText());
		presenter.getFirewallExceptionRequest().setPorts(portsTA.getText());
		presenter.getFirewallExceptionRequest().setBusinessReason(businessReasonTA.getText());
		presenter.getFirewallExceptionRequest().setIsPatched(isPatchedCB.getValue() ? "Yes" : "No");
		presenter.getFirewallExceptionRequest().setIsDefaultPasswdChanged(isDefaultPasswordChangedCB.getValue() ? "Yes" : "No");
		presenter.getFirewallExceptionRequest().setIsAppConsoleACLed(isAppConsoleACLedCB.getValue() ? "Yes" : "No");
		presenter.getFirewallExceptionRequest().setIsHardened(isHardenedCB.getValue() ? "Yes" : "No");
		presenter.getFirewallExceptionRequest().setPatchingPlan(patchingPlanTA.getText());
		// TODO: compliances
		presenter.getFirewallExceptionRequest().setSensitiveDataDesc(sensitiveDataDescriptionTA.getText());
		presenter.getFirewallExceptionRequest().setLocalFirewallRules(localFirewallRulesDescriptionTA.getText());
		presenter.getFirewallExceptionRequest().setIsDefaultDenyZone(isDefaultDenyZoneCB.getValue() ? "Yes" : "No");
		// tags are added to the FirewallExceptionRequest as they're added by the user
		presenter.saveFirewallExceptionRequest();
	}
	
	@UiHandler ("addTagTF")
	void addUserTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
        	addTagToFirewallExceptionRequest(addTagTF.getText());
        }
	}
	@UiHandler ("addTagButton")
	void addUserButtonClick(ClickEvent e) {
		addTagToFirewallExceptionRequest(addTagTF.getText());
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	technicalContactTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		okayButton.setEnabled(true);
		netIdTB.setEnabled(true);
		technicalContactTB.setEnabled(true);
		applicationNameTB.setEnabled(true);
		isSourceOutsideEmoryCB.setEnabled(true);
		timeRuleLB.setEnabled(true);
		validUntilDB.setEnabled(true);
		sourceIpAddressesTA.setEnabled(true);
		destinationIpAddressesTA.setEnabled(true);
		portsTA.setEnabled(true);
		businessReasonTA.setEnabled(true);
		isPatchedCB.setEnabled(true);
		isDefaultPasswordChangedCB.setEnabled(true);
		isAppConsoleACLedCB.setEnabled(true);
		isHardenedCB.setEnabled(true);
		patchingPlanTA.setEnabled(true);
		complianceClassLB.setEnabled(true);
		sensitiveDataDescriptionTA.setEnabled(true);
		localFirewallRulesDescriptionTA.setEnabled(true);
		isDefaultDenyZoneCB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		netIdTB.setEnabled(false);
		technicalContactTB.setEnabled(false);
		applicationNameTB.setEnabled(false);
		isSourceOutsideEmoryCB.setEnabled(false);
		timeRuleLB.setEnabled(false);
		validUntilDB.setEnabled(false);
		sourceIpAddressesTA.setEnabled(false);
		destinationIpAddressesTA.setEnabled(false);
		portsTA.setEnabled(false);
		businessReasonTA.setEnabled(false);
		isPatchedCB.setEnabled(false);
		isDefaultPasswordChangedCB.setEnabled(false);
		isAppConsoleACLedCB.setEnabled(false);
		isHardenedCB.setEnabled(false);
		patchingPlanTA.setEnabled(false);
		complianceClassLB.setEnabled(false);
		sensitiveDataDescriptionTA.setEnabled(false);
		localFirewallRulesDescriptionTA.setEnabled(false);
		isDefaultDenyZoneCB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		FirewallExceptionRequestPojo fer = presenter.getFirewallExceptionRequest();
		if (fer.getUserNetId() == null || fer.getUserNetId().length() == 0) {
			fields.add(netIdTB);
		}
		if (fer.getTechnicalContact() == null || fer.getTechnicalContact().length() == 0) {
			fields.add(technicalContactTB);
		}
		if (fer.getApplicationName() == null || fer.getApplicationName().length() == 0) {
			fields.add(applicationNameTB);
		}
		if (fer.getIsSourceOutsideEmory() == null) {
			fields.add(isSourceOutsideEmoryCB);
		}
		if (fer.getTimeRule() == null) {
			fields.add(timeRuleLB);
		}
		if (fer.getSourceIp() == null || fer.getSourceIp().length() == 0) {
			fields.add(sourceIpAddressesTA);
		}
		if (fer.getDestinationIp() == null || fer.getDestinationIp().length() == 0) {
			fields.add(destinationIpAddressesTA);
		}
		if (fer.getPorts() == null || fer.getPorts().length() == 0) {
			fields.add(portsTA);
		}
		if (fer.getBusinessReason() == null || fer.getBusinessReason().length() == 0) {
			fields.add(businessReasonTA);
		}
		if (fer.getIsPatched() == null) {
			fields.add(isPatchedCB);
		}
		if (fer.getIsDefaultPasswdChanged() == null) {
			fields.add(isDefaultPasswordChangedCB);
		}
		if (fer.getIsAppConsoleACLed() == null) {
			fields.add(isAppConsoleACLedCB);
		}
		if (fer.getIsHardened() == null) {
			fields.add(isHardenedCB);
		}
		if (fer.getPatchingPlan() == null || fer.getPatchingPlan().length() == 0) {
			fields.add(patchingPlanTA);
		}
		if (fer.getSensitiveDataDesc() == null || fer.getSensitiveDataDesc().length() == 0) {
			fields.add(sensitiveDataDescriptionTA);
		}
		if (fer.getLocalFirewallRules() == null || fer.getLocalFirewallRules().length() == 0) {
			fields.add(localFirewallRulesDescriptionTA);
		}
		if (fer.getIsDefaultDenyZone() == null) {
			fields.add(isDefaultDenyZoneCB);
		}
		if (fer.getTags() == null || fer.getTags().size() == 0) {
			fields.add(addTagTF);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(netIdTB);
		fields.add(technicalContactTB);
		fields.add(applicationNameTB);
		fields.add(complianceClassLB);
		fields.add(isSourceOutsideEmoryCB);
		fields.add(timeRuleLB);
		fields.add(sourceIpAddressesTA);
		fields.add(destinationIpAddressesTA);
		fields.add(portsTA);
		fields.add(businessReasonTA);
		fields.add(isPatchedCB);
		fields.add(isDefaultPasswordChangedCB);
		fields.add(isAppConsoleACLedCB);
		fields.add(isHardenedCB);
		fields.add(patchingPlanTA);
		fields.add(sensitiveDataDescriptionTA);
		fields.add(localFirewallRulesDescriptionTA);
		fields.add(isDefaultDenyZoneCB);
		fields.add(addTagTF);
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
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFirewallExceptionRequestNameViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		FirewallRulePojo fr = presenter.getFirewallRule();
		FirewallExceptionRequestPojo fer = presenter.getFirewallExceptionRequest();
		if (fer != null && fer.getUserNetId() != null) {
			netIdTB.setText(fer.getUserNetId());
		}
		else {
			netIdTB.setText(userLoggedIn.getPrincipal());
		}
		addTagTF.setText("");
		addTagTF.getElement().setPropertyString("placeholder", "enter a tag");
		
		
		// initialize all fields with data from the presenter's FirewallExceptionRequest
		// or from the presenter's firewall rule if it exists
		if (fr != null) {
			applicationNameTB.setText(fr.getName());
			StringBuffer sbuf = new StringBuffer();
			boolean isFirst = true;
			for (String s : fr.getSources()) {
				if (!isFirst) {
					sbuf.append("\n");
				}
				else {
					isFirst = false;
				}
				sbuf.append(s);
			}
			sourceIpAddressesTA.setText(sbuf.toString());
			
			sbuf = new StringBuffer();
			isFirst = true;
			for (String s : fr.getDestinations()) {
				if (!isFirst) {
					sbuf.append("\n");
				}
				else {
					isFirst = false;
				}
				sbuf.append(s);
			}
			destinationIpAddressesTA.setText(sbuf.toString());
			
			sbuf = new StringBuffer();
			isFirst = true;
			for (String s : fr.getServices()) {
				if (!isFirst) {
					sbuf.append("\n");
				}
				else {
					isFirst = false;
				}
				sbuf.append(s);
			}
			portsTA.setText(sbuf.toString());
			
			isSourceOutsideEmoryCB.setValue(false);
			isPatchedCB.setValue(true);
			isDefaultPasswordChangedCB.setValue(true);
			isAppConsoleACLedCB.setValue(true);
			isHardenedCB.setValue(true);
			isDefaultDenyZoneCB.setValue(true);
		}
		else {
			technicalContactTB.setText(fer.getTechnicalContact());
			applicationNameTB.setText(fer.getApplicationName());
			if (fer.getIsSourceOutsideEmory() != null) {
				isSourceOutsideEmoryCB.setValue(fer.getIsSourceOutsideEmory().equalsIgnoreCase("Yes") ? true : false);
			}
			if (fer.getIsPatched() != null) {
				isPatchedCB.setValue(fer.getIsPatched().equalsIgnoreCase("Yes") ? true : false);
			}
			if (fer.getIsDefaultPasswdChanged() != null) {
				isDefaultPasswordChangedCB.setValue(fer.getIsDefaultPasswdChanged().equalsIgnoreCase("Yes") ? true : false);
			}
			if (fer.getIsAppConsoleACLed() != null) {
				isAppConsoleACLedCB.setValue(fer.getIsAppConsoleACLed().equalsIgnoreCase("Yes") ? true : false);
			}
			if (fer.getIsHardened() != null) {
				isHardenedCB.setValue(fer.getIsHardened().equalsIgnoreCase("Yes") ? true : false);
			}
			if (fer.getIsDefaultDenyZone() != null) {
				isDefaultDenyZoneCB.setValue(fer.getIsDefaultDenyZone().equalsIgnoreCase("Yes") ? true : false);
			}
			
			if (fer.getValidUntilDate() != null) {
				validUntilDB.setValue(presenter.getFirewallExceptionRequest().getValidUntilDate());
			}
			sourceIpAddressesTA.setText(fer.getSourceIp());
			destinationIpAddressesTA.setText(fer.getDestinationIp());
			portsTA.setText(fer.getPorts());
			businessReasonTA.setText(fer.getBusinessReason());
			patchingPlanTA.setText(fer.getPatchingPlan());
			sensitiveDataDescriptionTA.setText(fer.getSensitiveDataDesc());
			localFirewallRulesDescriptionTA.setText(fer.getLocalFirewallRules());

			DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
		    validUntilDB.setFormat(new DateBox.DefaultFormat(dateFormat));
		    validUntilDB.getDatePicker().setYearArrowsVisible(true);
		    
		    String timeRule = timeRuleLB.getSelectedValue();
		    if (timeRule != null) {
				if (timeRule.trim().equalsIgnoreCase(Constants.TIME_RULE_INDEFINITELY)) {
					hideValidUntilDB();
				}
				else {
					showValidUntilDB();
				}
		    }
		    else {
		    	hideValidUntilDB();
		    }
		}
	    
	    initializeTagsPanel();
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

	private void addTagToFirewallExceptionRequest(String tag) {
		if (tag != null && tag.trim().length() > 0) {
			final String trimmedTag = tag.trim().toLowerCase();
			if (presenter.getFirewallExceptionRequest().getTags().contains(trimmedTag)) {
				showStatus(addTagButton, "That tag is alreay in the list, please enter a unique tag.");
			}
			else {
				presenter.getFirewallExceptionRequest().getTags().add(trimmedTag);
				addTagToPanel(trimmedTag);
			}
		}
		else {
			showStatus(addTagButton, "Please enter a valid tag.");
		}
	}

	private void addTagToPanel(final String tag) {
		int numRows = tagsTable.getRowCount();
		final Label tagLabel = new Label(tag);
		tagLabel.addStyleName("emailLabel");
		tagLabel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				presenter.setDirectoryMetaDataTitleOnWidget(tag, tagLabel);
			}
		});
		final Button removeTagButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) || userLoggedIn.isLitsAdmin()) {
			removeTagButton.setEnabled(true);
		}
		else {
			removeTagButton.setEnabled(false);
		}
		removeTagButton.addStyleName("glowing-border");
		removeTagButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getFirewallExceptionRequest().getTags().remove(tag);
				tagsTable.remove(tagLabel);
				tagsTable.remove(removeTagButton);
			}
		});
		addTagTF.setText("");
		if (numRows > 6) {
			if (tagRowNum > 5) {
				tagRowNum = 0;
				tagColumnNum = tagColumnNum + 2;
				removeTagButtonColumnNum = removeTagButtonColumnNum + 2;
			}
			else {
				tagRowNum ++;
			}
		}
		else {
			tagRowNum = numRows;
		}
		tagsTable.setWidget(tagRowNum, tagColumnNum, tagLabel);
		tagsTable.setWidget(tagRowNum, removeTagButtonColumnNum, removeTagButton);
	}

	void initializeTagsPanel() {
		tagsTable.removeAllRows();
		if (presenter.getFirewallExceptionRequest() != null) {
			GWT.log("Adding " + presenter.getFirewallExceptionRequest().getTags().size() + " tags to the panel (update).");
			for (String tag : presenter.getFirewallExceptionRequest().getTags()) {
				addTagToPanel(tag);
			}
		}
	}

	@Override
	public void setComplianceClassItems(List<String> complianceClassTypes) {
		this.complianceClassTypes = complianceClassTypes;
		complianceClassLB.clear();
		if (complianceClassTypes != null) {
			int i=0;
			for (String type : complianceClassTypes) {
				complianceClassLB.addItem(type, type);
				if (presenter.getFirewallExceptionRequest() != null) {
					if (presenter.getFirewallExceptionRequest().getCompliance() != null) {
						for (String pojo_type : presenter.getFirewallExceptionRequest().getCompliance()) {
							if (pojo_type.equals(type)) {
								complianceClassLB.setItemSelected(i, true);
							}
						}
					}
				}
				i++;
			}
		}
	}

	@Override
	public void setTimeRuleItems(List<String> timeRules) {
		this.timeRules = timeRules;
		timeRuleLB.clear();
		timeRuleLB.addItem("-- Select --");
		if (timeRules != null) {
			int i=1;
			for (String type : timeRules) {
				timeRuleLB.addItem(type, type);
				if (presenter.getFirewallExceptionRequest() != null) {
					if (presenter.getFirewallExceptionRequest().getTimeRule() != null) {
						if (presenter.getFirewallExceptionRequest().getTimeRule().equals(type)) {
							timeRuleLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}
	
	private void showValidUntilDB() {
		validUntilLabel.setVisible(true);
		validUntilDB.setVisible(true);
	}
	private void hideValidUntilDB() {
		validUntilLabel.setVisible(false);
		validUntilDB.setVisible(false);
	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
	}
}
