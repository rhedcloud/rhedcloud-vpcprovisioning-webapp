package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileView;
import edu.emory.oit.vpcprovisioning.shared.TunnelProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainVpnConnectionProfile extends ViewImplBase implements MaintainVpnConnectionProfileView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;

	private static DesktopMaintainVpnConnectionProfileUiBinder uiBinder = GWT
			.create(DesktopMaintainVpnConnectionProfileUiBinder.class);

	interface DesktopMaintainVpnConnectionProfileUiBinder
			extends UiBinder<Widget, DesktopMaintainVpnConnectionProfile> {
	}

	public DesktopMaintainVpnConnectionProfile() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox tunnelDescTB;
	@UiField TextBox cryptoKeyringTB;
	@UiField TextBox isakampProfileTB;
	@UiField TextBox ipsecProfileTB;
	@UiField TextBox ipsecTransformSetTB;
	@UiField TextBox customerGatewayIpTB;
	@UiField TextBox vpnInsideCidr1TB;
	@UiField TextBox vpnInsideCidr2TB;
	@UiField Button addTunnelButton;
	@UiField FlexTable tunnelTable;
	@UiField TextBox vpcTB;
//	@UiField TextBox profileIdTB;

	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.getVpnConnectionProfile().setVpcNetwork(vpcTB.getText());
//		presenter.getVpnConnectionProfile().setVpnConnectionProfileId(profileIdTB.getText());
		if (presenter.getVpnConnectionProfile().getTunnelProfiles().size() != 2) {
			showMessageToUser("You must add exactly two (2) Tunnel Profiles to the VPN Connection Profile.");
			setFieldViolations(true);
			return;
		}
		// tunnels are added to the profile as they're added
		presenter.saveVpnConnectionProfile();
	}
	@UiHandler ("addTunnelButton")
	void addElasticIpButtonClick(ClickEvent e) {
		if (addTunnelButton.getText().equalsIgnoreCase("Add")) {
			addTunnel();
		}
		else {
			// update existing tunnel
			TunnelProfilePojo tunnel = createTunnelFromFormData();
			presenter.updateTunnel(tunnel);
			initPage();
		}
	}
	private TunnelProfilePojo createTunnelFromFormData() {
		TunnelProfilePojo tunnel = new TunnelProfilePojo();
		if (presenter.getSelectedTunnel() != null) {
			tunnel.setTunnelId(presenter.getSelectedTunnel().getTunnelId());
		}
		tunnel.setTunnelDescription(tunnelDescTB.getText());
		tunnel.setCryptoKeyringName(cryptoKeyringTB.getText());
		tunnel.setIsakampProfileName(isakampProfileTB.getText());
		tunnel.setIpsecProfileName(ipsecProfileTB.getText());
		tunnel.setIpsecTransformSetName(ipsecTransformSetTB.getText());
		tunnel.setCustomerGatewayIp(customerGatewayIpTB.getText());
		tunnel.setVpnInsideIpCidr1(vpnInsideCidr1TB.getText());
		tunnel.setVpnInsideIpCidr2(vpnInsideCidr2TB.getText());
		
		return tunnel;
	}
	private void clearTunnelFields() {
		vpnInsideCidr2TB.setText("");
		vpnInsideCidr1TB.setText("");
		customerGatewayIpTB.setText("");
		ipsecTransformSetTB.setText("");
		ipsecProfileTB.setText("");
		tunnelDescTB.setText("");
		cryptoKeyringTB.setText("");
		isakampProfileTB.setText("");
	}
	private void addTunnel() {
		List<Widget> fields = getMissingTunnelFields();
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
		TunnelProfilePojo tunnel = createTunnelFromFormData();
		presenter.getVpnConnectionProfile().getTunnelProfiles().add(tunnel);
		addTunnelToPanel(tunnel);
		this.resetFieldStyles();
		this.setInitialFocus();
	}
	private List<Widget> getMissingTunnelFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (presenter.getVpnConnectionProfile().getTunnelProfiles().size() == 2) {
			return fields;
		}
		if (vpnInsideCidr2TB.getText() == null || vpnInsideCidr2TB.getText().length() == 0) {
			fields.add(vpnInsideCidr2TB);
		}
		if (vpnInsideCidr1TB.getText() == null || vpnInsideCidr1TB.getText().length() == 0) {
			fields.add(vpnInsideCidr1TB);
		}
		if (customerGatewayIpTB.getText() == null || customerGatewayIpTB.getText().length() == 0) {
			fields.add(customerGatewayIpTB);
		}
		if (ipsecTransformSetTB.getText() == null || ipsecTransformSetTB.getText().length() == 0) {
			fields.add(ipsecTransformSetTB);
		}
		if (ipsecProfileTB.getText() == null || ipsecProfileTB.getText().length() == 0) {
			fields.add(ipsecProfileTB);
		}
		if (tunnelDescTB.getText() == null || tunnelDescTB.getText().length() == 0) {
			fields.add(tunnelDescTB);
		}
		if (cryptoKeyringTB.getText() == null || cryptoKeyringTB.getText().length() == 0) {
			fields.add(cryptoKeyringTB);
		}
		if (isakampProfileTB.getText() == null || isakampProfileTB.getText().length() == 0) {
			fields.add(isakampProfileTB);
		}
		return fields;
	}
	private void addTunnelToPanel(final TunnelProfilePojo tunnel) {
		final int numRows = tunnelTable.getRowCount();
		final Label descLabel = new Label(tunnel.getTunnelDescription());
		descLabel.addStyleName("emailLabel");
		descLabel.setTitle(tunnel.toString());
		
		final PushButton editButton = new PushButton();
		editButton.setTitle("Edit this Tunnel Profile.");
		Image editImage = new Image("images/edit_icon.png");
		editImage.setWidth("30px");
		editImage.setHeight("30px");
		editButton.getUpFace().setImage(editImage);
		// disable buttons if userLoggedIn is NOT a network admin
		// TODO: network admin
		if (this.userLoggedIn.isNetworkAdmin()  || userLoggedIn.isCentralAdmin()) {
			editButton.setEnabled(true);
		}
		else {
			editButton.setEnabled(false);
		}
		editButton.addStyleName("glowing-border");
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.setSelectedTunnel(tunnel);
				vpnInsideCidr2TB.setText(tunnel.getVpnInsideIpCidr2());
				vpnInsideCidr1TB.setText(tunnel.getVpnInsideIpCidr1());
				customerGatewayIpTB.setText(tunnel.getCustomerGatewayIp());
				ipsecTransformSetTB.setText(tunnel.getIpsecTransformSetName());
				ipsecProfileTB.setText(tunnel.getIpsecProfileName());
				tunnelDescTB.setText(tunnel.getTunnelDescription());
				cryptoKeyringTB.setText(tunnel.getCryptoKeyringName());
				isakampProfileTB.setText(tunnel.getIsakampProfileName());
				addTunnelButton.setText("Update");
			}
		});
		
		final PushButton removeTunnelButton = new PushButton();
		removeTunnelButton.setTitle("Remove this Tunnel Profile.");
		Image removeImage = new Image("images/delete_icon.png");
		removeImage.setWidth("30px");
		removeImage.setHeight("30px");
		removeTunnelButton.getUpFace().setImage(removeImage);
		// disable buttons if userLoggedIn is NOT a network admin
		// TODO: network admin
		if (this.userLoggedIn.isNetworkAdmin()) {
			removeTunnelButton.setEnabled(true);
		}
		else {
			removeTunnelButton.setEnabled(false);
		}
		removeTunnelButton.addStyleName("glowing-border");
		removeTunnelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				tunnelTable.remove(descLabel);
//				tunnelTable.remove(removeTunnelButton);
				presenter.getVpnConnectionProfile().getTunnelProfiles().remove(tunnel);
				initPage();
			}
		});
		
		tunnelTable.setWidget(numRows, 0, descLabel);
		tunnelTable.setWidget(numRows, 1, editButton);
		tunnelTable.setWidget(numRows, 2, removeTunnelButton);
	}

	void initializeTunnelProfilePanel() {
		tunnelTable.removeAllRows();
		for (TunnelProfilePojo tunnel : presenter.getVpnConnectionProfile().getTunnelProfiles()) {
			this.addTunnelToPanel(tunnel);
		}
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	vpcTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyNetworkAdminMask() {
		vpcTB.setEnabled(true);
		okayButton.setEnabled(true);
		addTunnelButton.setEnabled(true);
	}

	@Override
	public void applyCentralAdminMask() {
		vpcTB.setEnabled(false);
		okayButton.setEnabled(false);
		addTunnelButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		vpcTB.setEnabled(false);
		okayButton.setEnabled(false);
		addTunnelButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		vpcTB.setEnabled(false);
		okayButton.setEnabled(false);
		addTunnelButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (vpcTB.getText() == null || vpcTB.getText().length() == 0) {
			fields.add(vpcTB);
		}
//		if (profileIdTB.getText() == null || profileIdTB.getText().length() == 0) {
//			fields.add(profileIdTB);
//		}
		fields.addAll(getMissingTunnelFields());
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
//		fields.add(profileIdTB);
		fields.add(vpcTB);
		fields.add(vpnInsideCidr2TB);
		fields.add(vpnInsideCidr1TB);
		fields.add(customerGatewayIpTB);
		fields.add(ipsecTransformSetTB);
		fields.add(ipsecProfileTB);
		fields.add(tunnelDescTB);
		fields.add(cryptoKeyringTB);
		fields.add(isakampProfileTB);
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
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		clearTunnelFields();
		presenter.setSelectedTunnel(null);
		addTunnelButton.setText("Add");
		vpcTB.setText(presenter.getVpnConnectionProfile().getVpcNetwork());
		this.initializeTunnelProfilePanel();
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

}
