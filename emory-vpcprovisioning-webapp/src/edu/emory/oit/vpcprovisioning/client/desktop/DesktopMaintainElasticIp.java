package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpView;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainElasticIp extends ViewImplBase implements MaintainElasticIpView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<ElasticIpPojo> ipsToCreate = new java.util.ArrayList<ElasticIpPojo>();

	private static DesktopMaintainElasticIpUiBinder uiBinder = GWT.create(DesktopMaintainElasticIpUiBinder.class);

	interface DesktopMaintainElasticIpUiBinder extends UiBinder<Widget, DesktopMaintainElasticIp> {
	}

	public DesktopMaintainElasticIp() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button okayButton;
	@UiField Button cancelButton;
	
	@UiField TextBox addElasticIpTF;
	@UiField Button addElasticIpButton;
	@UiField FlexTable elasticIpTable;
	

	@UiHandler ("addElasticIpTF")
	void addElasticIpTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		addIp();
        }
	}
	@UiHandler ("addElasticIpButton")
	void addElasticIpButtonClick(ClickEvent e) {
		addIp();
	}
	private void addIp() {
		String ip = addElasticIpTF.getText();
		if (ip == null || ip.length() == 0) {
			this.setInitialFocus();
			showMessageToUser("Please enter a valid IP address.");
			return;
		}
		if (!this.isValidIp(ip)) {
			this.setInitialFocus();
			showMessageToUser("Invalid IP address.  Please enter a valid IP address.");
			return;
		}
		addElasticIpToPanel(addElasticIpTF.getText());
		addElasticIpTF.setText("");
		this.resetFieldStyles();
		this.setInitialFocus();
	}
	private void addElasticIpToPanel(final String ipAddress) {
		// TODO: presenter.isValidIpAddress(ipAddress);
		final ElasticIpPojo eip = new ElasticIpPojo();
		eip.setElasticIpAddress(ipAddress);
		ipsToCreate.add(eip);
		final int numRows = elasticIpTable.getRowCount();
		final Label ipLabel = new Label(ipAddress);
		ipLabel.addStyleName("emailLabel");
		final Button removeIpButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		// TODO: network admin
		if (this.userLoggedIn.isCentralAdmin()) {
			removeIpButton.setEnabled(true);
		}
		else {
			removeIpButton.setEnabled(false);
		}
		removeIpButton.addStyleName("glowing-border");
		removeIpButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ipsToCreate.remove(eip);
				elasticIpTable.remove(ipLabel);
				elasticIpTable.remove(removeIpButton);
			}
		});
		addElasticIpTF.setText("");
		elasticIpTable.setWidget(numRows, 0, ipLabel);
		elasticIpTable.setWidget(numRows, 1, removeIpButton);
	}

	void initializeElasticIpPanel() {
		elasticIpTable.removeAllRows();
		ipsToCreate = new java.util.ArrayList<ElasticIpPojo>();
	}
	
	@UiHandler("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.ELASTIC_IP_EDITING_CANCELED);
	}
	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.createElasticIps(ipsToCreate);
	}
	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	addElasticIpTF.setFocus(true);
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
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setEditing(boolean isEditing) {
		
	}

	@Override
	public void setLocked(boolean locked) {
		okayButton.setEnabled(false);
	}

	@Override
	public void setNetworkViolation(String message) {
		
		
	}

	@Override
	public void setBitsViolation(String message) {
		
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		initializeElasticIpPanel();
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
	}

	@Override
	public void hidePleaseWaitPanel() {
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (ipsToCreate.size() == 0) {
			this.setFieldViolations(true);
			fields.add(addElasticIpTF);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(addElasticIpTF);
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
	public void applyCentralAdminMask() {
		okayButton.setEnabled(true);
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
