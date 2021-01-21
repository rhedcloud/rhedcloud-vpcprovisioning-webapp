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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionPojo;

public class DesktopMaintainVpc extends ViewImplBase implements MaintainVpcView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	List<String> vpcTypes;
	List<AWSRegionPojo> regionTypes;
	UserAccountPojo userLoggedIn;
	String speedTypeBeingTyped=null;
	Tree vpn_tree;
	PopupPanel vpnPleaseWaitDialog;
	
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
	@UiField ListBox regionLB;
	@UiField ListBox reqRegionLB;

	// VPN connection info
	@UiField PushButton expandButton;
	@UiField PushButton collapseButton;
	@UiField PushButton refreshButton;
	@UiField VerticalPanel vpnInfoOuterPanel;
	@UiField ScrollPanel vpnInfoPanel;
	@UiField Image tunnel1StatusImage;
	@UiField Image tunnel2StatusImage;
	@UiField TextArea operationalSummaryTA;
	@UiField Grid vpnInfoHeaderGrid;
	@UiField Grid operationalStatusGrid;
	
	// VPC associated properties
	@UiField VerticalPanel propertiesVP;
	@UiField TextBox propertyKeyTF;
	@UiField TextBox propertyValueTF;
	@UiField Button addPropertyButton;
	@UiField FlexTable propertiesTable;

//	private boolean firstCidrWidget = true;
	private boolean firstElasticIpWidget = true;
	private boolean firstFirewallWidget = true;

	private static DesktopMaintainVpcUiBinder uiBinder = GWT.create(DesktopMaintainVpcUiBinder.class);

	interface DesktopMaintainVpcUiBinder extends UiBinder<Widget, DesktopMaintainVpc> {
	}

	public DesktopMaintainVpc() {
		initWidget(uiBinder.createAndBindUi(this));
		
		// 01/21/2021:  hide the elastic ip assignments tab
		if (vpcTabPanel.getTabWidget(1) != null) {
			vpcTabPanel.getTabWidget(1).setVisible(false);
		}
		
		setRefreshButtonImage(refreshButton);
		Image expandImg = new Image("images/expand.png");
		expandImg.setWidth("30px");
		expandImg.setHeight("30px");
		expandButton.getUpFace().setImage(expandImg);

		Image collapseImg = new Image("images/collapse.png");
		collapseImg.setWidth("30px");
		collapseImg.setHeight("30px");
		collapseButton.getUpFace().setImage(collapseImg);
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
					presenter.getVpc().setRegion(regionLB.getSelectedValue());
					// admin net ids are added as they're added in the interface
				}
				else {
					presenter.getVpcRequisition().setAccountId(vpcReqAccountIdTB.getText());
					presenter.getVpcRequisition().setAccountOwnerUserId(vpcReqOwnerNetIdTB.getText());
					presenter.getVpcRequisition().setSpeedType(vpcReqSpeedTypeTB.getText());
					presenter.getVpcRequisition().setType(vpcReqTypeLB.getSelectedValue());
					presenter.getVpcRequisition().setRegion(reqRegionLB.getSelectedValue());
					// admin net ids are added as they're added in the interface
				}
				presenter.saveVpc();
			}
		}, ClickEvent.getType());
	}

	@UiHandler ("addPropertyButton")
	void addPropertyButtonClick(ClickEvent e) {
		propertyKeyTF.setEnabled(true);
		if (addPropertyButton.getText().equalsIgnoreCase("Add")) {
			addProperty();
		}
		else {
			// update existing property
			PropertyPojo property = createPropertyFromFormData();
			presenter.updateProperty(property);
			initializeVpcPropertiesPanel();
		}
		propertyKeyTF.setText("");
		propertyValueTF.setText("");
	}
	
	private void initializeVpcPropertiesPanel() {
		addPropertyButton.setText("Add");
		propertyKeyTF.setEnabled(true);
		propertiesTable.removeAllRows();
		for (PropertyPojo prop : presenter.getVpc().getProperties()) {
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
		if (this.userLoggedIn.isCentralAdmin() || 
			this.userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId())) {
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
		// disable buttons if userLoggedIn is NOT a network admin
		if (this.userLoggedIn.isCentralAdmin()  || 
			this.userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId())) {
			removeButton.setEnabled(true);
		}
		else {
			removeButton.setEnabled(false);
		}
		removeButton.addStyleName("glowing-border");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getVpc().getProperties().remove(prop);
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
		presenter.getVpc().getProperties().add(property);
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

	@UiHandler("expandButton")
	void expandButtonClicked(ClickEvent e) {
		if (vpn_tree == null) {
			return;
		}
		int ti_count = vpn_tree.getItemCount();
		for (int i=0; i<ti_count; i++) {
			TreeItem ti = vpn_tree.getItem(i);
			ti.setState(true, false);
		}
	}

	@UiHandler("collapseButton")
	void collapseButtonClicked(ClickEvent e) {
		if (vpn_tree == null) {
			return;
		}
		int ti_count = vpn_tree.getItemCount();
		for (int i=0; i<ti_count; i++) {
			TreeItem ti = vpn_tree.getItem(i);
			ti.setState(false, false);
		}
	}

	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		presenter.refreshVpnConnectionInfo();
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
			firewallContainer.add(listFwView);
			firewallContainer.setAnimationDuration(500);
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_FIREWALL_RULE, presenter.getVpc());
			break;
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
		if (w instanceof ListElasticIpAssignmentPresenter || 
			w instanceof MaintainElasticIpAssignmentPresenter) {
			
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
		
		// 01/21/2021:  hide the elastic ip assignments tab
		if (vpcTabPanel.getTabWidget(1) != null) {
			vpcTabPanel.getTabWidget(1).setVisible(false);
		}
		
		if (editing) {
			GWT.log("maintain VPC view initPage.  editing");
			
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
			
			if (vpcTabPanel.getSelectedIndex() == 0) {
				firewallContainer.clear();
				ListFirewallRuleView listAccountView = presenter.getClientFactory().getListFirewallRuleView();
				firewallContainer.add(listAccountView);
				firewallContainer.setAnimationDuration(500);
				ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_FIREWALL_RULE, presenter.getVpc());
			}
			else {
				elasticIpAssignmentContainer.clear();
				ListElasticIpAssignmentView listEipView = presenter.getClientFactory().getListElasticIpAssignmentView();
				MaintainElasticIpAssignmentView maintainEipView = presenter.getClientFactory().getMaintainElasticIpAssignmentView();
				elasticIpAssignmentContainer.add(listEipView);
				elasticIpAssignmentContainer.add(maintainEipView);
				elasticIpAssignmentContainer.setAnimationDuration(500);
				ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_ELASTIC_IP_ASSIGNMENT, presenter.getVpc());
			}
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
		
		// populate properties panel
		initializeVpcPropertiesPanel();
		
		// populate admin net id fields if appropriate
//		initializeNetIdPanel();
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
		
		return null;
	}
	@Override
	public void setVpcIdViolation(String message) {
		
		
	}
	@Override
	public void setVpcTypeItems(List<String> vpcTypes) {
		this.vpcTypes = vpcTypes;
		if (editing) {
			vpcTypeLB.clear();
			if (vpcTypes != null) {
				int i=0;
				for (String type : vpcTypes) {
					vpcTypeLB.addItem("Type: " + type, type);
					if (presenter.getVpc() != null) {
						if (presenter.getVpc().getType() != null) {
							GWT.log("comparing vpc type: '" + presenter.getVpc().getType() + "' to: '" + type + "'");
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
			vpcReqTypeLB.addItem("-- Select --", "");
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
		propertyKeyTF.setEnabled(true);
		propertyValueTF.setEnabled(true);
		addPropertyButton.setEnabled(true);
		regionLB.setEnabled(false);
		reqRegionLB.setEnabled(false);
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
		propertyKeyTF.setEnabled(false);
		propertyValueTF.setEnabled(false);
		addPropertyButton.setEnabled(false);
		regionLB.setEnabled(false);
		reqRegionLB.setEnabled(false);
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
		List<Widget> fields = new java.util.ArrayList<Widget>();
		/*
		<!ELEMENT VirtualPrivateCloud (
			VpcId, 
			AccountId, 
			Cidr, 
			VpnConnectionProfileId, 
			Region, 
			Type, 
			Purpose, CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?)>
		 */
		VpcPojo vpc = presenter.getVpc();
		if (vpc.getVpcId() == null || vpc.getVpcId().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpcIdTB);
		}
		if (vpc.getAccountId() == null || vpc.getAccountId().length() == 0) {
			this.setFieldViolations(true);
			fields.add(accountIdTB);
		}
		if (vpc.getCidr() == null || vpc.getCidr().length() == 0) {
			this.setFieldViolations(true);
			fields.add(cidrTB);
		}
		if (vpc.getVpnConnectionProfileId() == null || vpc.getVpnConnectionProfileId().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpnProfileIdTB);
		}
		if (vpc.getRegion() == null || vpc.getRegion().length() == 0) {
			this.setFieldViolations(true);
			fields.add(regionLB);
		}
		if (vpc.getType() == null || vpc.getType().length() == 0) {
			this.setFieldViolations(true);
			fields.add(vpcTypeLB);
		}
		if (vpc.getPurpose() == null || vpc.getPurpose().length() == 0) {
			this.setFieldViolations(true);
			fields.add(purposeTA);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		
		
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

		propertyKeyTF.setEnabled(true);
		propertyValueTF.setEnabled(true);
		addPropertyButton.setEnabled(true);
		
		regionLB.setEnabled(true);
		reqRegionLB.setEnabled(true);
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

	@Override
	public void setAwsRegionItems(List<AWSRegionPojo> regionTypes) {
		this.regionTypes = regionTypes;

		if (editing) {
			regionLB.clear();
			if (regionTypes != null) {
				int i=0;
				if (presenter.getVpc().getRegion() == null) {
					regionLB.addItem("-- Select --", "");
					i = 1;
				}
				
				for (AWSRegionPojo region : regionTypes) {
					regionLB.addItem(region.getValue(), region.getCode());
					if (presenter.getVpc() != null) {
						if (presenter.getVpc().getRegion() != null) {
							if (presenter.getVpc().getRegion().equals(region.getCode())) {
								regionLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
		else {
			reqRegionLB.clear();
			if (regionTypes != null) {
				int i=1;
				reqRegionLB.addItem("-- Select --", "");
				for (AWSRegionPojo region : regionTypes) {
					regionLB.addItem(region.getValue(), region.getCode());
					if (presenter.getVpcRequisition() != null) {
						if (presenter.getVpcRequisition().getRegion() != null) {
							if (presenter.getVpcRequisition().getRegion().equals(region.getCode())) {
								reqRegionLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
	}

	@Override
	public void refreshVpnConnectionInfo(VpnConnectionPojo vpnConnection) {
		if (vpnConnection != null) {
			// update operational status info (images and summary)
			Tree vpnInfoTree = createVpnInfoTree(vpnConnection);
			vpnInfoPanel.setWidget(vpnInfoTree);
			operationalStatusGrid.setVisible(true);
		}
		else {
			operationalStatusGrid.setVisible(false);
			setVpnInfo("<b>No VPN Information available</b>");
		}
		// start timer to disable the refreshButton and re-enable it 
		// after 1 minute or so
		refreshButton.setEnabled(false);
		refreshButton.setTitle("Waiting to allow a refresh...");
		final Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshButton.setEnabled(true);
				refreshButton.setTitle("Refresh VPN Connection Info");
			}
		};
		refreshTimer.schedule(60000);
	}

	@Override
	public void showVpnConnectionPleaseWaitDialog(String message) {
		if (vpnPleaseWaitDialog == null) {
			vpnPleaseWaitDialog = new PopupPanel(false);
		}
		else {
			vpnPleaseWaitDialog.clear();
		}
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().getStyle().setBackgroundColor("#f1f1f1");
		Image img = new Image();
		img.setUrl("images/ajax-loader.gif");
		vp.add(img);
		HTML h = new HTML(message);
		vp.add(h);
		vp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_CENTER);
		vpnPleaseWaitDialog.setWidget(vp);
		vpnPleaseWaitDialog.center();
		vpnPleaseWaitDialog.show();
//		vpnPleaseWaitDialog.showRelativeTo(vpnInfoOuterPanel);
	}

	@Override
	public void hideVpnConnectionPleaseWaitDialog() {
		if (vpnPleaseWaitDialog != null) {
			vpnPleaseWaitDialog.hide();
		}
	}

	@Override
	public void setOperationalStatusSummary(String string) {
		operationalSummaryTA.setText(string);
	}

	@Override
	public void setTunnel2StatusBad(String reason) {
		tunnel2StatusImage.setUrl("images/red_circle_icon.jpg");
		tunnel2StatusImage.setTitle(reason);
	}

	@Override
	public void setTunnel2StatusGood() {
		tunnel2StatusImage.setUrl("images/green_circle_icon.png");
		tunnel2StatusImage.setTitle("Tunnel is good");
	}

	@Override
	public void setTunnel1StatusBad(String reason) {
		tunnel1StatusImage.setUrl("images/red_circle_icon.jpg");
		tunnel1StatusImage.setTitle(reason);
	}

	@Override
	public void setTunnel1StatusGood() {
		tunnel1StatusImage.setUrl("images/green_circle_icon.png");
		tunnel1StatusImage.setTitle("Tunnel is good");
	}

	@Override
	public void setVpnRefreshing() {
		setVpnInfo("<b>Refreshing...</b>");
		operationalStatusGrid.setVisible(false);
	}

	@Override
	public void setVpnInfo(String vpnInfoHtml) {
		vpnInfoPanel.setWidget(new HTML(vpnInfoHtml));
	}
}
