package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePresenter;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRuleView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainVpc extends ViewImplBase implements MaintainVpcView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	List<String> vpcTypes;
	UserAccountPojo userLoggedIn;
	String speedTypeBeingTyped=null;
	
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	
	// used for maintaining vpc
	@UiField Grid maintainVpcGrid;
	@UiField TextBox accountIdTB;
	@UiField TextBox accountNameTB;
	@UiField TextBox vpcIdTB;
	@UiField ListBox vpcTypeLB;
	@UiField TextBox cidrTB;
	@UiField TextBox vpnProfileIdTB;
	@UiField TextArea purposeTA;
	
	// used when generating vpc
	@UiField Grid generateVpcGrid;
	@UiField TextBox vpcReqOwnerNetIdTB;
	@UiField TextBox vpcReqAccountIdTB;
	@UiField TextBox vpcReqSpeedTypeTB;
	@UiField ListBox vpcReqTypeLB;
	@UiField TextBox vpcReqCidrTB;
	@UiField Label speedTypeLabel;
	
	// firewall rules and elasticip tabs
	@UiField TabLayoutPanel vpcTabPanel;
	@UiField DeckLayoutPanel firewallContainer;
//	@UiField DeckLayoutPanel cidrAssignmentContainer;
	@UiField DeckLayoutPanel elasticIpAssignmentContainer;

//	private boolean firstCidrWidget = true;
	private boolean firstElasticIpWidget = true;
	private boolean firstFirewallWidget = true;

	private static DesktopMaintainVpcUiBinder uiBinder = GWT.create(DesktopMaintainVpcUiBinder.class);

	interface DesktopMaintainVpcUiBinder extends UiBinder<Widget, DesktopMaintainVpc> {
	}

	public DesktopMaintainVpc() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.log("maintain VPC view init...");
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.VPC_EDITING_CANCELED);
			}
		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (editing) {
					presenter.getVpc().setAccountId(accountIdTB.getText());
					presenter.getVpc().setVpcId(vpcIdTB.getText());
					presenter.getVpc().setType(vpcTypeLB.getSelectedValue());
					presenter.getVpc().setCidr(cidrTB.getText());
					presenter.getVpc().setVpnConnectionProfileId(vpnProfileIdTB.getText());
					presenter.getVpc().setPurpose(purposeTA.getText());
					// admin net ids are added as they're added in the interface
				}
				else {
					presenter.getVpcRequisition().setAccountId(vpcReqAccountIdTB.getText());
					presenter.getVpcRequisition().setAccountOwnerUserId(vpcReqOwnerNetIdTB.getText());
					presenter.getVpcRequisition().setSpeedType(vpcReqSpeedTypeTB.getText());
					presenter.getVpcRequisition().setType(vpcReqTypeLB.getSelectedValue());
					// admin net ids are added as they're added in the interface
				}
				presenter.saveVpc();
			}
		}, ClickEvent.getType());
	}

	@UiHandler ("vpcReqSpeedTypeTB")
	void speedTypeMouseOver(MouseOverEvent e) {
		String acct = vpcReqSpeedTypeTB.getText();
		presenter.setSpeedChartStatusForKeyOnWidget(acct, vpcReqSpeedTypeTB);
	}
	@UiHandler ("vpcReqSpeedTypeTB")
	void speedTypeKeyPressed(KeyPressEvent e) {
		GWT.log("SpeedType key pressed...");
		int keyCode = e.getNativeEvent().getKeyCode();
		char ccode = e.getCharCode();

		if (keyCode == KeyCodes.KEY_BACKSPACE) {
			if (speedTypeBeingTyped.length() > 0) {
				speedTypeBeingTyped = speedTypeBeingTyped.substring(0, speedTypeBeingTyped.length() - 1);
			}
			presenter.setSpeedChartStatusForKey(speedTypeBeingTyped, speedTypeLabel);
			return;
		}
		
		if (keyCode == KeyCodes.KEY_TAB) {
			presenter.setSpeedChartStatusForKey(vpcReqSpeedTypeTB.getText(), speedTypeLabel);
			return;
		}

		if (!isValidKey(keyCode)) {
			return;
		}
		else {
			speedTypeBeingTyped += ccode;
		}

		presenter.setSpeedChartStatusForKey(speedTypeBeingTyped, speedTypeLabel);
	}
	
	@UiHandler ("vpcTabPanel") 
	void tabSelected(SelectionEvent<Integer> e) {
		switch (e.getSelectedItem()) {
		case 0:
			GWT.log("need to get Firewall Maintentenance content.");
			firstFirewallWidget = true;
			firewallContainer.clear();
			ListFirewallRuleView listFwView = presenter.getClientFactory().getListFirewallRuleView();
//			MaintainFirewallView maintainFwView = clientFactory.getMaintainFirewallView();
			firewallContainer.add(listFwView);
//			firewallContentContainer.add(maintainFwView);
			firewallContainer.setAnimationDuration(500);
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_FIREWALL_RULE, presenter.getVpc());
			break;
//		case 1:
//			GWT.log("need to get CIDR Assignment Maintentenance content.");
//			firstCidrWidget = true;
//			cidrAssignmentContainer.clear();
//			ListCidrAssignmentView listCidrView = presenter.getClientFactory().getListCidrAssignmentView();
//			MaintainCidrAssignmentView maintainCidrView = presenter.getClientFactory().getMaintainCidrAssignmentView();
//			cidrAssignmentContainer.add(listCidrView);
//			cidrAssignmentContainer.add(maintainCidrView);
//			cidrAssignmentContainer.setAnimationDuration(500);
//			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_CIDR_ASSIGNMENT, presenter.getVpc());
//			break;
		case 1:
			GWT.log("need to get Elastic IP Maintentenance content.");
			firstElasticIpWidget = true;
			elasticIpAssignmentContainer.clear();
			ListElasticIpAssignmentView listEipView = presenter.getClientFactory().getListElasticIpAssignmentView();
			MaintainElasticIpAssignmentView maintainEipView = presenter.getClientFactory().getMaintainElasticIpAssignmentView();
			elasticIpAssignmentContainer.add(listEipView);
			elasticIpAssignmentContainer.add(maintainEipView);
			elasticIpAssignmentContainer.setAnimationDuration(500);
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_ELASTIC_IP_ASSIGNMENT, presenter.getVpc());
			break;
		}
	}

	@Override
	public void setWidget(IsWidget w) {
		GWT.log("Maintain VPC, setWidget");
//		if (w instanceof ListCidrAssignmentPresenter || w instanceof MaintainCidrAssignmentPresenter) {
//			GWT.log("Maintain VPC, setWidget: cidr assignment");
//			cidrAssignmentContainer.setWidget(w);
//			// Do not animate the first time we show a widget.
//			if (firstCidrWidget) {
//				firstCidrWidget = false;
//				cidrAssignmentContainer.animate(0);
//			}
//			return;
//		}

		if (w instanceof ListElasticIpAssignmentPresenter || w instanceof MaintainElasticIpAssignmentPresenter) {
			GWT.log("Maintain VPC, setWidget: elastic IP");
			elasticIpAssignmentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstElasticIpWidget) {
				firstElasticIpWidget = false;
				elasticIpAssignmentContainer.animate(0);
			}
			return;
		}

		if (w instanceof ListFirewallRulePresenter) {
			GWT.log("Maintain VPC, setWidget: firewall");
			firewallContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstFirewallWidget) {
				firstFirewallWidget = false;
				firewallContainer.animate(0);
			}
				return;
		}
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
	public void initPage() {
		this.setFieldViolations(false);
		if (editing) {
			GWT.log("maintain VPC view initPage.  editing");
//			// hide generate grid, show maintain grid
//			generateVpcGrid.setVisible(false);
//			maintainVpcGrid.setVisible(true);
			// clear the page
			accountIdTB.setText("");
			accountNameTB.setText("");
			vpcIdTB.setText("");
			vpcTypeLB.setSelectedIndex(0);
			cidrTB.setText("");
			purposeTA.setText("");
			vpnProfileIdTB.setText("");

			if (presenter.getVpc() != null) {
				GWT.log("maintain VPC view initPage.  VPC: " + presenter.getVpc().getVpcId());
				accountIdTB.setText(presenter.getVpc().getAccountId());
				accountNameTB.setText(presenter.getVpc().getAccountName());
				vpcIdTB.setText(presenter.getVpc().getVpcId());
				cidrTB.setText(presenter.getVpc().getCidr());
				purposeTA.setText(presenter.getVpc().getPurpose());
				vpnProfileIdTB.setText(presenter.getVpc().getVpnConnectionProfileId());
			}
			
			ListFirewallRuleView listAccountView = presenter.getClientFactory().getListFirewallRuleView();
			firewallContainer.add(listAccountView);
			firewallContainer.setAnimationDuration(500);
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_FIREWALL_RULE, presenter.getVpc());
		}
		else {
			GWT.log("maintain VPC view initPage.  create");
			// hide maintain grid, show generate grid
//			maintainVpcGrid.setVisible(false);
//			generateVpcGrid.setVisible(true);
			vpcReqOwnerNetIdTB.setText("");
			vpcReqAccountIdTB.setText("");
			vpcReqSpeedTypeTB.setText("");
			vpcReqTypeLB.setSelectedIndex(0);
			cidrTB.setText("");
			purposeTA.setText("");
			vpnProfileIdTB.setText("");
		}
		
		// populate admin net id fields if appropriate
//		initializeNetIdPanel();
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
		if (editing) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	accountIdTB.setFocus(true);
		        }
		    });
		}
		else {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	vpcReqAccountIdTB.setFocus(true);
		        }
		    });
		}
	}
	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setVpcIdViolation(String message) {
		// TODO Auto-generated method stub
		
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
					if (presenter.getVpc() != null) {
						if (presenter.getVpc().getType() != null) {
							if (presenter.getVpc().getType().equals(type)) {
								vpcTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
		else {
			vpcReqTypeLB.clear();
			if (vpcTypes != null) {
				int i=1;
				for (String type : vpcTypes) {
					vpcReqTypeLB.addItem("Type: " + type, type);
					if (presenter.getVpcRequisition() != null) {
						if (presenter.getVpcRequisition().getType() != null) {
							if (presenter.getVpcRequisition().getType().equals(type)) {
								vpcReqTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
	}

	@Override
	public void applyAWSAccountAdminMask() {
		accountIdTB.setEnabled(false);
		accountNameTB.setEnabled(false);
		vpcIdTB.setEnabled(false);
		vpcTypeLB.setEnabled(false);
		cidrTB.setEnabled(false);
		vpnProfileIdTB.setEnabled(false);
		purposeTA.setEnabled(true);
		okayButton.setEnabled(true);
		
		vpcReqOwnerNetIdTB.setEnabled(false);
		vpcReqAccountIdTB.setEnabled(false);
		vpcReqSpeedTypeTB.setEnabled(false);
		vpcReqTypeLB.setEnabled(false);
		vpcReqCidrTB.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		accountIdTB.setEnabled(false);
		accountNameTB.setEnabled(false);
		vpcIdTB.setEnabled(false);
		vpcTypeLB.setEnabled(false);
		cidrTB.setEnabled(false);
		vpnProfileIdTB.setEnabled(false);
		purposeTA.setEnabled(false);
		okayButton.setEnabled(false);
		
		vpcReqOwnerNetIdTB.setEnabled(false);
		vpcReqAccountIdTB.setEnabled(false);
		vpcReqSpeedTypeTB.setEnabled(false);
		vpcReqTypeLB.setEnabled(false);
		vpcReqCidrTB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setSpeedTypeStatus(String status) {
		speedTypeLabel.setText(status);
	}

	@Override
	public void setSpeedTypeColor(String color) {
		speedTypeLabel.getElement().getStyle().setColor(color);
	}

	@Override
	public Widget getSpeedTypeWidget() {
		return vpcReqSpeedTypeTB;
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
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return okayButton;
	}

	@Override
	public void applyCentralAdminMask() {
		accountIdTB.setEnabled(true);
		accountNameTB.setEnabled(false);
		vpcIdTB.setEnabled(true);
		vpcTypeLB.setEnabled(true);
		cidrTB.setEnabled(true);
		vpnProfileIdTB.setEnabled(true);
		purposeTA.setEnabled(true);
		okayButton.setEnabled(true);
		
		vpcReqOwnerNetIdTB.setEnabled(true);
		vpcReqAccountIdTB.setEnabled(true);
		vpcReqSpeedTypeTB.setEnabled(true);
		vpcReqTypeLB.setEnabled(true);
		vpcReqCidrTB.setEnabled(true);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initDataEntryPanels() {
		if (editing) {
			// hide generate grid, show maintain grid
			generateVpcGrid.setVisible(false);
			maintainVpcGrid.setVisible(true);
		}
		else {
			// hide maintain grid, show generate grid
			maintainVpcGrid.setVisible(false);
			generateVpcGrid.setVisible(true);
		}
	}
}
