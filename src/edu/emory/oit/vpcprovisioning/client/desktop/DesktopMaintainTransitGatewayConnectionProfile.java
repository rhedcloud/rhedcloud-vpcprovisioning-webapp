package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayConnectionProfileView;
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainTransitGatewayConnectionProfile extends ViewImplBase implements MaintainTransitGatewayConnectionProfileView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	boolean editing;
	List<TransitGatewayPojo> transitGateways = new java.util.ArrayList<TransitGatewayPojo>();
	List<AWSRegionPojo> regions = new java.util.ArrayList<AWSRegionPojo>();
//	private final AwsAccountRpcSuggestOracle accountSuggestions = new AwsAccountRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);


	@UiField HTML pleaseWaitHTML;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField ListBox regionLB;
//	@UiField(provided=true) SuggestBox accountIdSB = new SuggestBox(accountSuggestions, new TextBox());
	@UiField ListBox transitGatewayIdLB;
	@UiField TextBox cidrIdTB;
	@UiField TextBox cidrRangeTB;

	private static DesktopMaintainTransitGatewayConnectionProfileUiBinder uiBinder = GWT
			.create(DesktopMaintainTransitGatewayConnectionProfileUiBinder.class);

	interface DesktopMaintainTransitGatewayConnectionProfileUiBinder
			extends UiBinder<Widget, DesktopMaintainTransitGatewayConnectionProfile> {
	}

	public DesktopMaintainTransitGatewayConnectionProfile() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		resetFieldStyles();
		presenter.getTransitGatewayConnectionProfile().setCidrId(cidrIdTB.getText());
		presenter.getTransitGatewayConnectionProfile().setRegion(regionLB.getSelectedValue());
		presenter.getTransitGatewayConnectionProfile().setTransitGatewayId(transitGatewayIdLB.getSelectedValue());
		// validate CIDR range
		presenter.getTransitGatewayConnectionProfile().setCidrRange(cidrRangeTB.getText());
		if (!this.isValidCidr(presenter.getTransitGatewayConnectionProfile().getCidrRange())) {
			List<Widget> fields = new java.util.ArrayList<Widget>();
			fields.add(cidrRangeTB);
			setFieldViolations(true);
			applyStyleToMissingFields(fields);
			hidePleaseWaitDialog();
			hidePleaseWaitPanel();
			showMessageToUser("Invalid CIDR Range.  Please enter a valid CIDR.");
			return;
		}

		presenter.saveTransitGatewayConnectionProfile();
	}
	
	@UiHandler("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_TRANSIT_GATEWAY_CONNECTION_PROFILE);
	}
	
	@Override
	public void hidePleaseWaitPanel() {
		this.pleaseWaitPanel.setVisible(false);
	}

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
	        	cidrIdTB.setFocus(true);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		TransitGatewayConnectionProfilePojo tgwcp = presenter.getTransitGatewayConnectionProfile(); 
		if (tgwcp.getCidrId() == null || tgwcp.getCidrId().length() == 0) {
			fields.add(cidrIdTB);
		}
		if (tgwcp.getRegion() == null || tgwcp.getRegion().length() == 0) {
			fields.add(regionLB);
		}
		if (tgwcp.getTransitGatewayId() == null || tgwcp.getTransitGatewayId().length() == 0) {
			fields.add(transitGatewayIdLB);
		}
		if (tgwcp.getCidrRange() == null || tgwcp.getCidrRange().length() == 0) {
			fields.add(cidrRangeTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(cidrIdTB);
		fields.add(regionLB);
		fields.add(transitGatewayIdLB);
		fields.add(cidrRangeTB);
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
		okayButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		okayButton.setEnabled(true);
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		cidrIdTB.setText("");
		cidrRangeTB.setText("");
		
		if (!editing) {
			cidrIdTB.getElement().setPropertyString("placeholder", "enter CIDR id");
			cidrRangeTB.getElement().setPropertyString("placeholder", "enter CIDR Range");
		}
		else {
			cidrIdTB.setText(presenter.getTransitGatewayConnectionProfile().getCidrId());
			cidrRangeTB.setText(presenter.getTransitGatewayConnectionProfile().getCidrRange());
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransitGatewayItems(List<TransitGatewayPojo> transitGateways) {
		this.transitGateways = transitGateways;

		transitGatewayIdLB.clear();
		if (transitGateways != null) {
			int i=0;
			if (presenter.getTransitGatewayConnectionProfile().getTransitGatewayId() == null) {
				transitGatewayIdLB.addItem("-- Select --", "");
				i = 1;
			}
			
			for (TransitGatewayPojo tgw : transitGateways) {
				String displayValue = tgw.getEnvironment() + " / " +
						tgw.getRegion() + " / " + tgw.getAccountId() + " / " + 
						tgw.getTransitGatewayId();
				transitGatewayIdLB.addItem(displayValue, tgw.getTransitGatewayId());
				if (presenter.getTransitGatewayConnectionProfile() != null) {
					if (presenter.getTransitGatewayConnectionProfile().getTransitGatewayId() != null) {
						if (presenter.getTransitGatewayConnectionProfile().getTransitGatewayId().equals(tgw.getTransitGatewayId())) {
							transitGatewayIdLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}

	@Override
	public void setAwsRegionItems(List<AWSRegionPojo> regionTypes) {
		this.regions = regionTypes;

		regionLB.clear();
		if (regionTypes != null) {
			int i=0;
			if (presenter.getTransitGatewayConnectionProfile().getRegion() == null) {
				regionLB.addItem("-- Select --", "");
				i = 1;
			}
			
			for (AWSRegionPojo region : regionTypes) {
				regionLB.addItem(region.getValue(), region.getCode());
				if (presenter.getTransitGatewayConnectionProfile() != null) {
					if (presenter.getTransitGatewayConnectionProfile().getRegion() != null) {
						if (presenter.getTransitGatewayConnectionProfile().getRegion().equals(region.getCode())) {
							regionLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}

}
