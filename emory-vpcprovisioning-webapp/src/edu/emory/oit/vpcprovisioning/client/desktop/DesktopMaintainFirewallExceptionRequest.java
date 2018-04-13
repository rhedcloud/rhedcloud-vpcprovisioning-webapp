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
		presenter.getFirewallExceptionRequest().setIsDefaultDenyZone(isHardenedCB.getValue() ? "Yes" : "No");
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
	        	netIdTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		/*
SystemId?, ServiceNow internal id to uniquely identify a record in the custom table. Not required in create. Required for update/delete.  wsdl: sys_id
UserNetID, Form: requestor userID; wsdl: u_requested_for
ApplicationName,   Form: For what application is this firewall exception needed.  wsdl: u_common_name
IsSourceOutsideEmory,  Form:Is the source outside of Emory's network and the destination inside?.  Valid values: Yes, No.  wsdl: u_internet_access
TimeRule,  how long the firewall exception will be valid.  Valid values: Indefinitely, SpecificDate.  wsdl: u_how_long
ValidUntilDate?,  if ValidUntil is SpecificDate, this field holds the date string, such as 2018-11-14. wsdl: u_specific_date 
SourceIpAddresses,  Source IP addresses AND fully qualified domain names of the system(s) that need to access the destination(s).  wsdl: u_ip_address 
DestinationIpAddresses, Destination IP address(es) AND fully qualified domain name(s) of the system(s) that needs to be accessible through the firewall.  wsdl: u_ip_address2 
Ports,  Destination ports, applications, or services that need to be accessible. Indicate TCP or UDP for each, or ICMP type, along with the underlying protocol or application.  wsdl: u_ports 
BusinessReason, - Describe the business justification for this exception.  wsdl: u_business_reason 
IsPatched, - Are Server/Service/Application components fully patched at the time of this request?  valie values: Yes, No.  wsdl: u_patch_server 
IsDefaultPasswdChanged, - Have all default application or server passwords been changed? valid values: Yes, No.  wsdl: u_app_pswd 
IsAppConsoleACLed, - Have all administrative/management consoles for the application, as well as any underlying technology administrative/management consoles (such as ColdFusion, Jboss, Tomcat, phpmyadmin) been properly ACL'd. valid values: Yes, No. wsdl: u_mgmt_consol 
IsHardened, - Have hardening guidelines for the application/service and underlying technologies been followed and implemented? Valid values: Yes, No.  wsdl:  u_guidelines 
PatchingPlan, - Describe your plan for keeping this application/service and underlying technologies patched in a timely manner.  wsdl: u_plan 
Compliance*, - Is this application/service in scope of any of the following compliance regimes? valid values: FERPA, FISMA, HIPAA, PCI, Other, Unsure. wsdl: u_comp_1 to u_comp_6 
OtherCompliance?, - compliance description when the Compliance value is "Other".  wsdl: u_other_compliance 
SensitiveDataDesc, - Describe any sensitive data that will be stored or processed on this system.  wsdl: u_sensitivity 
LocalFirewallRules, - Describe any local (host-based) firewall rules that are currently configured on the system.  wsdl: u_firewall_rules 
IsDefaultDenyZone, - Could this system be placed into a subnet that blocks all outbound Internet traffic by default.  valid values: Yes, No.  wsdl: u_subnet 
Tag+, - tags for this request.  wsdl: u_tag
		 */
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
//		this.resetFieldStyles();
//		this.setFieldViolations(false);
		if (presenter.getFirewallExceptionRequest() != null && presenter.getFirewallExceptionRequest().getUserNetId() != null) {
			netIdTB.setText(presenter.getFirewallExceptionRequest().getUserNetId());
		}
		else {
			netIdTB.setText(userLoggedIn.getPrincipal());
		}
		addTagTF.setText("");
		addTagTF.getElement().setPropertyString("placeholder", "enter a tag");
		
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
}
