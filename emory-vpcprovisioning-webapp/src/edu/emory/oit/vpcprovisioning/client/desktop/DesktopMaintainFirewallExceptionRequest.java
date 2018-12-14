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
import com.google.gwt.user.client.ui.HTML;
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
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionAddRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionAddRequestRequisitionPojo;
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
	
	@UiField VerticalPanel maintainRequestVP;
	@UiField VerticalPanel generateAddRequestVP;
	@UiField VerticalPanel generateAddRequestVP2;
	@UiField VerticalPanel generateRemoveRequestVP;
	
	@UiField TextArea notPatchedJustificationTA;
	@UiField TextArea passwordNotChangedJustificationTA;
	@UiField TextArea notACLedJustificationTA;
	@UiField TextArea notHardenedJustificationTA;
	@UiField CheckBox isTraverseVpnCB;
	@UiField TextBox vpnNameTB;
	@UiField CheckBox isAccessVPCCB;
	
	@UiField Label requestItemNumberLabel;
	@UiField Label requestStateLabel;
	@UiField Label requestItemStateLabel;
	@UiField Label systemIdLabel;
	
	@UiField TextArea requestDetailsTA;

	@UiHandler("isTraverseVpnCB")
	void isTraverseVpnClicked(ClickEvent e) {
		if (isTraverseVpnCB.getValue()) {
			vpnNameTB.setVisible(true);
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	vpnNameTB.setFocus(true);
		        }
		    });
		}
		else {
			vpnNameTB.setVisible(false);
		}
	}
	@UiHandler("isPatchedCB")
	void isPatchedClicked(ClickEvent e) {
		if (!isPatchedCB.getValue()) {
			notPatchedJustificationTA.setVisible(true);
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	notPatchedJustificationTA.setFocus(true);
		        }
		    });
		}
		else {
			notPatchedJustificationTA.setVisible(false);
		}
	}
	@UiHandler("isDefaultPasswordChangedCB")
	void isDefaultPasswordChangedCBClicked(ClickEvent e) {
		if (!isDefaultPasswordChangedCB.getValue()) {
			passwordNotChangedJustificationTA.setVisible(true);
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	passwordNotChangedJustificationTA.setFocus(true);
		        }
		    });
		}
		else {
			passwordNotChangedJustificationTA.setVisible(false);
		}
	}
	@UiHandler("isAppConsoleACLedCB")
	void isAppConsoleACLedCBClicked(ClickEvent e) {
		if (!isAppConsoleACLedCB.getValue()) {
			notACLedJustificationTA.setVisible(true);
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	notACLedJustificationTA.setFocus(true);
		        }
		    });
		}
		else {
			notACLedJustificationTA.setVisible(false);
		}
	}
	@UiHandler("isHardenedCB")
	void isHardenedCBClicked(ClickEvent e) {
		if (!isHardenedCB.getValue()) {
			notHardenedJustificationTA.setVisible(true);
		}
		else {
			notHardenedJustificationTA.setVisible(false);
		}
	}
	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		if (!editing) {
			if (presenter.getAddRequisition() != null) {
				presenter.getAddRequisition().setUserNetId(netIdTB.getText());
				presenter.getAddRequisition().setApplicationName(applicationNameTB.getText());
				presenter.getAddRequisition().setIsSourceOutsideEmory(isSourceOutsideEmoryCB.getValue() ? "Yes" : "No");
				presenter.getAddRequisition().setTimeRule(timeRuleLB.getSelectedValue());
				presenter.getAddRequisition().setValidUntilDate(validUntilDB.getValue());
				presenter.getAddRequisition().setSourceIp(sourceIpAddressesTA.getText());
				presenter.getAddRequisition().setDestinationIp(destinationIpAddressesTA.getText());
				presenter.getAddRequisition().setPorts(portsTA.getText());
				presenter.getAddRequisition().setBusinessReason(businessReasonTA.getText());
				presenter.getAddRequisition().setIsPatched(isPatchedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequisition().setNotPatchedJustification(notPatchedJustificationTA.getText());
				presenter.getAddRequisition().setIsDefaultPasswdChanged(isDefaultPasswordChangedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequisition().setPasswdNotChangedJustification(passwordNotChangedJustificationTA.getText());
				presenter.getAddRequisition().setIsAppConsoleACLed(isAppConsoleACLedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequisition().setNotACLedJustification(notACLedJustificationTA.getText());
				presenter.getAddRequisition().setIsHardened(isHardenedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequisition().setNotHardenedJustification(notHardenedJustificationTA.getText());
				presenter.getAddRequisition().setPatchingPlan(patchingPlanTA.getText());
				// TODO: compliances
				presenter.getAddRequisition().setSensitiveDataDesc(sensitiveDataDescriptionTA.getText());
				presenter.getAddRequisition().setLocalFirewallRules(localFirewallRulesDescriptionTA.getText());
				presenter.getAddRequisition().setIsDefaultDenyZone(isDefaultDenyZoneCB.getValue() ? "Yes" : "No");
				presenter.getAddRequisition().setWillTraverseVPN(isTraverseVpnCB.getValue() ? "Yes" : "No");
				presenter.getAddRequisition().setVpnName(vpnNameTB.getText());
				presenter.getAddRequisition().setAccessAwsVPC(isAccessVPCCB.getValue() ? "Yes" : "No");
				// tags are added to the FirewallExceptionRequest as they're added by the user
				presenter.saveFirewallExceptionRequest();
			}
			else {
				presenter.getRemoveRequisition().setUserNetId(netIdTB.getText());
				FirewallRulePojo fr = presenter.getFirewallRule();
				if (fr != null) {
					GWT.log("adding FirewallRule data to the request details field...");
					StringBuffer sbuf = new StringBuffer();
					sbuf.append(requestDetailsTA.getText() + "\n\n");
					sbuf.append("FirewallRule Details:\n");
					sbuf.append("VSYS=" + (fr.getVsys() != null ? fr.getVsys() : Constants.UNKNOWN) + "\n");
					sbuf.append("Name=" + fr.getName());
					presenter.getRemoveRequisition().setRequestDetails(sbuf.toString());
				}
				else {
					presenter.getRemoveRequisition().setRequestDetails(requestDetailsTA.getText());
				}
				presenter.saveFirewallExceptionRequest();
			}
		}
		else {
			if (presenter.getAddRequest() != null) {
				presenter.getAddRequest().setUserNetId(netIdTB.getText());
				presenter.getAddRequest().setApplicationName(applicationNameTB.getText());
				presenter.getAddRequest().setIsSourceOutsideEmory(isSourceOutsideEmoryCB.getValue() ? "Yes" : "No");
				presenter.getAddRequest().setTimeRule(timeRuleLB.getSelectedValue());
				presenter.getAddRequest().setValidUntilDate(validUntilDB.getValue());
				presenter.getAddRequest().setSourceIp(sourceIpAddressesTA.getText());
				presenter.getAddRequest().setDestinationIp(destinationIpAddressesTA.getText());
				presenter.getAddRequest().setPorts(portsTA.getText());
				presenter.getAddRequest().setBusinessReason(businessReasonTA.getText());
				presenter.getAddRequest().setIsPatched(isPatchedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequest().setNotPatchedJustification(notPatchedJustificationTA.getText());
				presenter.getAddRequest().setIsDefaultPasswdChanged(isDefaultPasswordChangedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequest().setPasswdNotChangedJustification(passwordNotChangedJustificationTA.getText());
				presenter.getAddRequest().setIsAppConsoleACLed(isAppConsoleACLedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequest().setNotACLedJustification(notACLedJustificationTA.getText());
				presenter.getAddRequest().setIsHardened(isHardenedCB.getValue() ? "Yes" : "No");
				presenter.getAddRequest().setNotHardenedJustification(notHardenedJustificationTA.getText());
				presenter.getAddRequest().setPatchingPlan(patchingPlanTA.getText());
				// TODO: compliances
				presenter.getAddRequest().setSensitiveDataDesc(sensitiveDataDescriptionTA.getText());
				presenter.getAddRequest().setLocalFirewallRules(localFirewallRulesDescriptionTA.getText());
				presenter.getAddRequest().setIsDefaultDenyZone(isDefaultDenyZoneCB.getValue() ? "Yes" : "No");
				presenter.getAddRequest().setWillTraverseVPN(isTraverseVpnCB.getValue() ? "Yes" : "No");
				presenter.getAddRequest().setVpnName(vpnNameTB.getText());
				presenter.getAddRequest().setAccessAwsVPC(isAccessVPCCB.getValue() ? "Yes" : "No");
				// tags are added to the FirewallExceptionRequest as they're added by the user
				presenter.saveFirewallExceptionRequest();
			}
			else {
				presenter.getRemoveRequest().setUserNetId(netIdTB.getText());
				presenter.getRemoveRequest().setRequestDetails(requestDetailsTA.getText());
				presenter.saveFirewallExceptionRequest();
			}
		}
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
	        	if (presenter.getAddRequest() != null || presenter.getAddRequisition() != null) {
	        		applicationNameTB.setFocus(true);
	        	}
	        	else {
	        		// set the appropriate focus field for a remove request
	        		requestDetailsTA.setFocus(true);
	        	}
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		okayButton.setEnabled(true);
		netIdTB.setEnabled(true);
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
		isAccessVPCCB.setEnabled(true);
		notPatchedJustificationTA.setEnabled(true);
		passwordNotChangedJustificationTA.setEnabled(true);
		notACLedJustificationTA.setEnabled(true);
		notHardenedJustificationTA.setEnabled(true);
		isTraverseVpnCB.setEnabled(true);
		vpnNameTB.setEnabled(true);
		isAccessVPCCB.setEnabled(true);
		
		// remove request fields
		requestDetailsTA.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		netIdTB.setEnabled(false);
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
		isAccessVPCCB.setEnabled(false);
		notPatchedJustificationTA.setEnabled(false);
		passwordNotChangedJustificationTA.setEnabled(false);
		notACLedJustificationTA.setEnabled(false);
		notHardenedJustificationTA.setEnabled(false);
		isTraverseVpnCB.setEnabled(false);
		vpnNameTB.setEnabled(false);
		isAccessVPCCB.setEnabled(false);

		// remove request fields
		requestDetailsTA.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (!editing) {
			if (presenter.getAddRequisition() != null) {
				FirewallExceptionAddRequestRequisitionPojo addRequisition = presenter.getAddRequisition();
				if (addRequisition.getUserNetId() == null || addRequisition.getUserNetId().length() == 0) {
					fields.add(netIdTB);
				}
				if (addRequisition.getApplicationName() == null || addRequisition.getApplicationName().length() == 0) {
					fields.add(applicationNameTB);
				}
				if (addRequisition.getIsSourceOutsideEmory() == null) {
					fields.add(isSourceOutsideEmoryCB);
				}
				if (addRequisition.getTimeRule() == null) {
					fields.add(timeRuleLB);
				}
				if (addRequisition.getSourceIp() == null || addRequisition.getSourceIp().length() == 0) {
					fields.add(sourceIpAddressesTA);
				}
				if (addRequisition.getDestinationIp() == null || addRequisition.getDestinationIp().length() == 0) {
					fields.add(destinationIpAddressesTA);
				}
				if (addRequisition.getPorts() == null || addRequisition.getPorts().length() == 0) {
					fields.add(portsTA);
				}
				if (addRequisition.getBusinessReason() == null || addRequisition.getBusinessReason().length() == 0) {
					fields.add(businessReasonTA);
				}
				if (!isPatchedCB.getValue()) {
					if (addRequisition.getNotPatchedJustification() == null || addRequisition.getNotPatchedJustification().length() == 0) {
						fields.add(notPatchedJustificationTA);
					}
				}
				if (!isDefaultPasswordChangedCB.getValue()) {
					if (addRequisition.getPasswdNotChangedJustification() == null || addRequisition.getPasswdNotChangedJustification().length() == 0) {
						fields.add(passwordNotChangedJustificationTA);
					}
				}
				if (!isAppConsoleACLedCB.getValue()) {
					if (addRequisition.getNotACLedJustification() == null || addRequisition.getNotACLedJustification().length() == 0) {
						fields.add(notACLedJustificationTA);
					}
				}
				if (!isHardenedCB.getValue()) {
					if (addRequisition.getNotHardenedJustification() == null || addRequisition.getNotHardenedJustification().length() == 0) {
						fields.add(notHardenedJustificationTA);
					}
				}
				if (addRequisition.getPatchingPlan() == null || addRequisition.getPatchingPlan().length() == 0) {
					fields.add(patchingPlanTA);
				}
				if (addRequisition.getSensitiveDataDesc() == null || addRequisition.getSensitiveDataDesc().length() == 0) {
					fields.add(sensitiveDataDescriptionTA);
				}
				if (addRequisition.getLocalFirewallRules() == null || addRequisition.getLocalFirewallRules().length() == 0) {
					fields.add(localFirewallRulesDescriptionTA);
				}
				if (addRequisition.getIsDefaultDenyZone() == null) {
					fields.add(isDefaultDenyZoneCB);
				}
				if (addRequisition.getTags() == null || addRequisition.getTags().size() == 0) {
					fields.add(addTagTF);
				}
				if (isTraverseVpnCB.getValue()) {
					if (addRequisition.getVpnName() == null || addRequisition.getVpnName().length() == 0) {
						fields.add(vpnNameTB);
					}
				}
				if (addRequisition.getAccessAwsVPC() == null) {
					fields.add(isAccessVPCCB);
				}
			}
			else {
				
			}
		}
		else {
			if (presenter.getAddRequest() != null) {
				FirewallExceptionAddRequestPojo addRequest = presenter.getAddRequest();
				if (addRequest.getUserNetId() == null || addRequest.getUserNetId().length() == 0) {
					fields.add(netIdTB);
				}
				if (addRequest.getApplicationName() == null || addRequest.getApplicationName().length() == 0) {
					fields.add(applicationNameTB);
				}
				if (addRequest.getIsSourceOutsideEmory() == null) {
					fields.add(isSourceOutsideEmoryCB);
				}
				if (addRequest.getTimeRule() == null) {
					fields.add(timeRuleLB);
				}
				if (addRequest.getSourceIp() == null || addRequest.getSourceIp().length() == 0) {
					fields.add(sourceIpAddressesTA);
				}
				if (addRequest.getDestinationIp() == null || addRequest.getDestinationIp().length() == 0) {
					fields.add(destinationIpAddressesTA);
				}
				if (addRequest.getPorts() == null || addRequest.getPorts().length() == 0) {
					fields.add(portsTA);
				}
				if (addRequest.getBusinessReason() == null || addRequest.getBusinessReason().length() == 0) {
					fields.add(businessReasonTA);
				}
				if (!isPatchedCB.getValue()) {
					if (addRequest.getNotPatchedJustification() == null || addRequest.getNotPatchedJustification().length() == 0) {
						fields.add(notPatchedJustificationTA);
					}
				}
				if (!isDefaultPasswordChangedCB.getValue()) {
					if (addRequest.getPasswdNotChangedJustification() == null || addRequest.getPasswdNotChangedJustification().length() == 0) {
						fields.add(passwordNotChangedJustificationTA);
					}
				}
				if (!isAppConsoleACLedCB.getValue()) {
					if (addRequest.getNotACLedJustification() == null || addRequest.getNotACLedJustification().length() == 0) {
						fields.add(notACLedJustificationTA);
					}
				}
				if (!isHardenedCB.getValue()) {
					if (addRequest.getNotHardenedJustification() == null || addRequest.getNotHardenedJustification().length() == 0) {
						fields.add(notHardenedJustificationTA);
					}
				}
				if (addRequest.getPatchingPlan() == null || addRequest.getPatchingPlan().length() == 0) {
					fields.add(patchingPlanTA);
				}
				if (addRequest.getSensitiveDataDesc() == null || addRequest.getSensitiveDataDesc().length() == 0) {
					fields.add(sensitiveDataDescriptionTA);
				}
				if (addRequest.getLocalFirewallRules() == null || addRequest.getLocalFirewallRules().length() == 0) {
					fields.add(localFirewallRulesDescriptionTA);
				}
				if (addRequest.getIsDefaultDenyZone() == null) {
					fields.add(isDefaultDenyZoneCB);
				}
				if (addRequest.getTags() == null || addRequest.getTags().size() == 0) {
					fields.add(addTagTF);
				}
				if (isTraverseVpnCB.getValue()) {
					if (addRequest.getVpnName() == null || addRequest.getVpnName().length() == 0) {
						fields.add(vpnNameTB);
					}
				}
				if (addRequest.getAccessAwsVPC() == null) {
					fields.add(isAccessVPCCB);
				}
			}
			else {
				
			}
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(netIdTB);
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
		fields.add(notPatchedJustificationTA);
		fields.add(passwordNotChangedJustificationTA);
		fields.add(notACLedJustificationTA);
		fields.add(notHardenedJustificationTA);
		fields.add(vpnNameTB);
		fields.add(isAccessVPCCB);

		// remove request fields
		fields.add(requestDetailsTA);
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
	public void initDataEntryPanels() {
		if (!editing) {
			if (presenter.getAddRequisition() != null) {
				generateRemoveRequestVP.setVisible(false);
				generateAddRequestVP.setVisible(true);
				generateAddRequestVP2.setVisible(true);
				if (!editing) {
					maintainRequestVP.setVisible(false);
				}
				else {
					maintainRequestVP.setVisible(true);
				}
			}
			else if (presenter.getRemoveRequisition() != null) {
				generateAddRequestVP.setVisible(false);
				generateAddRequestVP2.setVisible(false);
				generateRemoveRequestVP.setVisible(true);
				if (!editing) {
					maintainRequestVP.setVisible(false);
				}
				else {
					maintainRequestVP.setVisible(true);
				}
			}
		}
		else {
			if (presenter.getAddRequest() != null) {
				generateRemoveRequestVP.setVisible(false);
				generateAddRequestVP.setVisible(true);
				generateAddRequestVP2.setVisible(true);
				if (!editing) {
					maintainRequestVP.setVisible(false);
				}
				else {
					maintainRequestVP.setVisible(true);
				}
			}
			else if (presenter.getRemoveRequest() != null) {
				generateAddRequestVP.setVisible(false);
				generateAddRequestVP2.setVisible(false);
				generateRemoveRequestVP.setVisible(true);
				if (!editing) {
					maintainRequestVP.setVisible(false);
				}
				else {
					maintainRequestVP.setVisible(true);
				}
			}
		}
	}

	@Override
	public void initPage() {
		if (!editing) {
			if (presenter.isAddRequest()) {
				FirewallExceptionAddRequestRequisitionPojo addRequisition = presenter.getAddRequisition();
				if (addRequisition != null && addRequisition.getUserNetId() != null) {
					netIdTB.setText(addRequisition.getUserNetId());
				}
				else {
					netIdTB.setText(userLoggedIn.getPrincipal());
				}
				isSourceOutsideEmoryCB.setValue(false);
				isPatchedCB.setValue(true);
				isDefaultPasswordChangedCB.setValue(true);
				isAppConsoleACLedCB.setValue(true);
				isHardenedCB.setValue(true);
				isDefaultDenyZoneCB.setValue(true);
				isAccessVPCCB.setValue(false);
				applicationNameTB.setText("");
				sourceIpAddressesTA.setText("");
				destinationIpAddressesTA.setText("");
				portsTA.setText("");
				businessReasonTA.setText("");
				patchingPlanTA.setText("");
				sensitiveDataDescriptionTA.setText("");
				localFirewallRulesDescriptionTA.setText("");

				notPatchedJustificationTA.setVisible(false);
				passwordNotChangedJustificationTA.setVisible(false);
				notACLedJustificationTA.setVisible(false);
				notHardenedJustificationTA.setVisible(false);
				vpnNameTB.setVisible(false);
				
				notPatchedJustificationTA.setText("");
				notPatchedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason why it's not patched");
				passwordNotChangedJustificationTA.setText("");
				passwordNotChangedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason the password hasn't been changed");
				notACLedJustificationTA.setText("");
				notACLedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason why it's not ACLed");
				notHardenedJustificationTA.setText("");
				notHardenedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason why it's not hardened");
				vpnNameTB.setText("");
				vpnNameTB.getElement().setPropertyString("placeholder", "enter the VPN name");

				FirewallRulePojo fr = presenter.getFirewallRule();
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
				}
			}
			else {
				if (presenter.getRemoveRequisition() != null && 
					presenter.getRemoveRequisition().getUserNetId() != null) {
					
					netIdTB.setText(presenter.getRemoveRequisition().getUserNetId());
				}
				else {
					netIdTB.setText(userLoggedIn.getPrincipal());
				}
				requestDetailsTA.setText("");
			}
		}
		else {
			if (presenter.getAddRequest() != null) {
				okayButton.setEnabled(false);
				netIdTB.setEnabled(false);
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
				isAccessVPCCB.setEnabled(false);
				notPatchedJustificationTA.setEnabled(false);
				passwordNotChangedJustificationTA.setEnabled(false);
				notACLedJustificationTA.setEnabled(false);
				notHardenedJustificationTA.setEnabled(false);
				isTraverseVpnCB.setEnabled(false);
				vpnNameTB.setEnabled(false);
				isAccessVPCCB.setEnabled(false);

				FirewallExceptionAddRequestPojo addRequest = presenter.getAddRequest();
				requestItemNumberLabel.setText(addRequest.getRequestItemNumber());
				requestStateLabel.setText(addRequest.getRequestState());
				requestItemStateLabel.setText(addRequest.getRequestItemState());
				systemIdLabel.setText(addRequest.getSystemId());
				
				if (addRequest.getUserNetId() != null) {
					netIdTB.setText(addRequest.getUserNetId());
				}
				else {
					netIdTB.setText(userLoggedIn.getPrincipal());
				}
				addTagTF.setText("");
				addTagTF.getElement().setPropertyString("placeholder", "enter a tag");
				
				isPatchedCB.setValue(true);
				isDefaultPasswordChangedCB.setValue(true);
				isAppConsoleACLedCB.setValue(true);
				isHardenedCB.setValue(true);
				notPatchedJustificationTA.setText("");
				notPatchedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason why it's not patched");

				passwordNotChangedJustificationTA.setText("");
				passwordNotChangedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason the password hasn't been changed");
				notACLedJustificationTA.setText("");
				notACLedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason why it's not ACLed");
				notHardenedJustificationTA.setText("");
				notHardenedJustificationTA.getElement().setPropertyString("placeholder", "enter a reason why it's not hardened");
				vpnNameTB.setText("");
				vpnNameTB.getElement().setPropertyString("placeholder", "enter the VPN name");
				
				applicationNameTB.setText(addRequest.getApplicationName());
				if (addRequest.getIsSourceOutsideEmory() != null) {
					isSourceOutsideEmoryCB.setValue(addRequest.getIsSourceOutsideEmory().equalsIgnoreCase("Yes") ? true : false);
				}
				if (addRequest.getIsPatched() != null) {
					isPatchedCB.setValue(addRequest.getIsPatched().equalsIgnoreCase("Yes") ? true : false);
					if (!isPatchedCB.getValue()) {
						notPatchedJustificationTA.setVisible(true);
						notPatchedJustificationTA.setText(addRequest.getNotPatchedJustification());
					}
				}
				if (addRequest.getIsDefaultPasswdChanged() != null) {
					isDefaultPasswordChangedCB.setValue(addRequest.getIsDefaultPasswdChanged().equalsIgnoreCase("Yes") ? true : false);
					if (!isDefaultPasswordChangedCB.getValue()) {
						passwordNotChangedJustificationTA.setVisible(true);
						passwordNotChangedJustificationTA.setText(addRequest.getPasswdNotChangedJustification());
					}
				}
				if (addRequest.getIsAppConsoleACLed() != null) {
					isAppConsoleACLedCB.setValue(addRequest.getIsAppConsoleACLed().equalsIgnoreCase("Yes") ? true : false);
					if (!isAppConsoleACLedCB.getValue()) {
						notACLedJustificationTA.setVisible(true);
						notACLedJustificationTA.setText(addRequest.getNotACLedJustification());
					}
				}
				if (addRequest.getIsHardened() != null) {
					isHardenedCB.setValue(addRequest.getIsHardened().equalsIgnoreCase("Yes") ? true : false);
					if (!isHardenedCB.getValue()) {
						notHardenedJustificationTA.setVisible(true);
						notHardenedJustificationTA.setText(addRequest.getNotHardenedJustification());
					}
				}
				if (addRequest.getIsDefaultDenyZone() != null) {
					isDefaultDenyZoneCB.setValue(addRequest.getIsDefaultDenyZone().equalsIgnoreCase("Yes") ? true : false);
				}
				if (addRequest.getAccessAwsVPC() != null) {
					isAccessVPCCB.setValue(addRequest.getAccessAwsVPC().equalsIgnoreCase("Yes") ? true : false);
				}
				
				if (addRequest.getValidUntilDate() != null) {
					validUntilDB.setValue(addRequest.getValidUntilDate());
				}
				sourceIpAddressesTA.setText(addRequest.getSourceIp());
				destinationIpAddressesTA.setText(addRequest.getDestinationIp());
				portsTA.setText(addRequest.getPorts());
				businessReasonTA.setText(addRequest.getBusinessReason());
				patchingPlanTA.setText(addRequest.getPatchingPlan());
				sensitiveDataDescriptionTA.setText(addRequest.getSensitiveDataDesc());
				localFirewallRulesDescriptionTA.setText(addRequest.getLocalFirewallRules());

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
			else {
				// remove request fields
				requestDetailsTA.setEnabled(false);
				requestItemNumberLabel.setText(presenter.getRemoveRequest().getRequestItemNumber());
				requestStateLabel.setText(presenter.getRemoveRequest().getRequestState());
				requestItemStateLabel.setText(presenter.getRemoveRequest().getRequestItemState());
				systemIdLabel.setText(presenter.getRemoveRequest().getSystemId());
				requestDetailsTA.setText(presenter.getRemoveRequest().getRequestDetails());
			}
		}
	    
	    initializeTagsPanel();
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

	private void addTagToFirewallExceptionRequest(String tag) {
		if (tag != null && tag.trim().length() > 0) {
			final String trimmedTag = tag.trim().toLowerCase();
			if (!editing) {
				if (presenter.getAddRequisition() != null) {
					if (presenter.getAddRequisition().getTags().contains(trimmedTag)) {
						showStatus(addTagButton, "That tag is alreay in the list, please enter a unique tag.");
					}
					else {
						presenter.getAddRequisition().getTags().add(trimmedTag);
						addTagToPanel(trimmedTag);
					}
				}
				else {
					if (presenter.getRemoveRequisition().getTags().contains(trimmedTag)) {
						showStatus(addTagButton, "That tag is alreay in the list, please enter a unique tag.");
					}
					else {
						presenter.getRemoveRequisition().getTags().add(trimmedTag);
						addTagToPanel(trimmedTag);
					}
				}
			}
			else {
				if (presenter.getAddRequest() != null) {
					if (presenter.getAddRequest().getTags().contains(trimmedTag)) {
						showStatus(addTagButton, "That tag is alreay in the list, please enter a unique tag.");
					}
					else {
						presenter.getAddRequest().getTags().add(trimmedTag);
						addTagToPanel(trimmedTag);
					}
				}
				else {
					if (presenter.getRemoveRequest().getTags().contains(trimmedTag)) {
						showStatus(addTagButton, "That tag is alreay in the list, please enter a unique tag.");
					}
					else {
						presenter.getRemoveRequest().getTags().add(trimmedTag);
						addTagToPanel(trimmedTag);
					}
				}
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
		if (userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId()) || userLoggedIn.isCentralAdmin()) {
			removeTagButton.setEnabled(true);
		}
		else {
			removeTagButton.setEnabled(false);
		}
		removeTagButton.addStyleName("glowing-border");
		removeTagButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!editing) {
					if (presenter.getAddRequisition() != null) {
						presenter.getAddRequisition().getTags().remove(tag);
					}
					else {
						presenter.getRemoveRequisition().getTags().remove(tag);
					}
				}
				else {
					if (presenter.getAddRequest() != null) {
						presenter.getAddRequest().getTags().remove(tag);
					}
					else {
						presenter.getRemoveRequest().getTags().remove(tag);
					}
				}
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
		if (!editing) {
			if (presenter.getAddRequisition() != null) {
				for (String tag : presenter.getAddRequisition().getTags()) {
					addTagToPanel(tag);
				}
			}
			else {
				for (String tag : presenter.getRemoveRequisition().getTags()) {
					addTagToPanel(tag);
				}
			}
		}
		else {
			if (presenter.getAddRequest() != null) {
				GWT.log("Adding " + presenter.getAddRequest().getTags().size() + " tags to the panel (update).");
				for (String tag : presenter.getAddRequest().getTags()) {
					addTagToPanel(tag);
				}
			}
			else {
				GWT.log("Adding " + presenter.getRemoveRequest().getTags().size() + " tags to the panel (update).");
				for (String tag : presenter.getRemoveRequest().getTags()) {
					addTagToPanel(tag);
				}
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
				if (presenter.getAddRequest() != null) {
					if (presenter.getAddRequest().getCompliance() != null) {
						for (String pojo_type : presenter.getAddRequest().getCompliance()) {
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
		timeRuleLB.addItem("-- Select --", "");
		if (timeRules != null) {
			int i=1;
			for (String type : timeRules) {
				timeRuleLB.addItem(type, type);
				if (presenter.getAddRequest() != null) {
					if (presenter.getAddRequest().getTimeRule() != null) {
						if (presenter.getAddRequest().getTimeRule().equals(type)) {
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
		okayButton.setEnabled(true);
		netIdTB.setEnabled(true);
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
		isAccessVPCCB.setEnabled(true);
		notPatchedJustificationTA.setEnabled(true);
		passwordNotChangedJustificationTA.setEnabled(true);
		notACLedJustificationTA.setEnabled(true);
		notHardenedJustificationTA.setEnabled(true);
		isTraverseVpnCB.setEnabled(true);
		vpnNameTB.setEnabled(true);
		isAccessVPCCB.setEnabled(true);
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
	public void disableButtons() {
	}

	@Override
	public void enableButtons() {
	}

	@Override
	public void applyNetworkAdminMask() {
	}
}
