package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.ui.HTMLUtils;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ProvisioningStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopVpncpStatus extends ViewImplBase implements VpncpStatusView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	UserAccountPojo userLoggedIn;

	boolean startTimer = true;

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML pleaseWaitHTML;
	@UiField Button doneButton;
	@UiField Button refreshButton;
	
	@UiField Label provisioningIdLabel;
	@UiField Label provisioningTypeLabel;
	@UiField Label statusLabel;
	@UiField Label provisioningResultLabel;
	@UiField Label anticipatedTimeLabel;
	@UiField Label actualTimeLabel;
	
	@UiField HTML progressHTML;
	@UiField VerticalPanel stepsPanel;


	private static DesktopVpncpStatusUiBinder uiBinder = GWT.create(DesktopVpncpStatusUiBinder.class);

	interface DesktopVpncpStatusUiBinder extends UiBinder<Widget, DesktopVpncpStatus> {
	}

	public DesktopVpncpStatus() {
		initWidget(uiBinder.createAndBindUi(this));
		doneButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopTimer();
				if (presenter.isFromGenerate()) {
					// need to account for vpn connection deprovision generates here too
					// if it's from a generate BUT is a deprovision, we'll want to go to GO_HOME_VPNCP
					// because that's where VPNs are deprovisioned from
					if (presenter.getVpncpSummary().isProvision()) {
						ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_VPN_CONNECTION_PROFILE);
					}
					else {
						ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_VPNCP);
					}
				}
				else {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_VPNCP);
				}
			}
		}, ClickEvent.getType());
		
		refreshButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startTimer = false;
				if (presenter.getVpncpSummary().isProvision()) {
					presenter.refreshProvisioningStatusForId(presenter.getVpncp().getProvisioningId());
				}
				else {
					// refresh the deprovisioing object
					presenter.refreshProvisioningStatusForId(presenter.getVpncdp().getProvisioningId());
				}
			}
		}, ClickEvent.getType());
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		// TODO Auto-generated method stub
		return null;
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
	public void startTimer(int delayMs) {
		GWT.log("[VIEW] starting timer...");
		startTimer = true;
		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
			@Override
			public boolean execute() {
				if (presenter.getVpncpSummary().isProvision()) {
					presenter.refreshProvisioningStatusForId(presenter.getVpncp().getProvisioningId());
					if (presenter.getVpncp().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						startTimer = false;
					}
					return startTimer;
				}
				else {
					presenter.refreshProvisioningStatusForId(presenter.getVpncdp().getProvisioningId());
					if (presenter.getVpncdp().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						startTimer = false;
					}
					return startTimer;
				}
			}
		}, delayMs);
	}

	@Override
	public void stopTimer() {
		startTimer = false;
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshProvisioningStatusInformation() {
		GWT.log("[DesktopVpcpStatus.refreshVpcpStatusInformation]");

		if (presenter.getVpncpSummary().isProvision()) {
			provisioningIdLabel.setText(presenter.getVpncp().getProvisioningId());
			provisioningTypeLabel.setText(Constants.VPN_PROVISIONING);
			statusLabel.setText(presenter.getVpncp().getStatus());
			if (presenter.getVpncp().getProvisioningResult() == null) {
				provisioningResultLabel.setText(Constants.NOT_APPLICABLE);
			}
			else {
				provisioningResultLabel.setText(presenter.getVpncp().getProvisioningResult());
			}
			anticipatedTimeLabel.setText(presenter.getVpncp().getAnticipatedTime());
			actualTimeLabel.setText(presenter.getVpncp().getActualTime());
		}
		else {
			provisioningIdLabel.setText(presenter.getVpncdp().getProvisioningId());
			provisioningTypeLabel.setText(Constants.VPN_DEPROVISIONING);
			statusLabel.setText(presenter.getVpncdp().getStatus());
			if (presenter.getVpncdp().getProvisioningResult() == null) {
				provisioningResultLabel.setText(Constants.NOT_APPLICABLE);
			}
			else {
				provisioningResultLabel.setText(presenter.getVpncdp().getProvisioningResult());
			}
			anticipatedTimeLabel.setText(presenter.getVpncdp().getAnticipatedTime());
			actualTimeLabel.setText(presenter.getVpncdp().getActualTime());
		}
		
		setProvisioningProgress();
		
		refreshProvisioningStepInformation();
	}

	private void refreshProvisioningStepInformation() {
		Grid stepsGrid = null;
		if (presenter.getVpncpSummary().isProvision()) {
			stepsGrid = new Grid(presenter.getVpncp().getProvisioningSteps().size() + 1, 8);
		}
		else {
			stepsGrid = new Grid(presenter.getVpncdp().getProvisioningSteps().size() + 1, 8);
		}
		stepsGrid.setCellPadding(8);
		stepsPanel.clear();

		stepsGrid.setWidget(0, 0, new HTML("<b>Step ID</b>"));
		stepsGrid.setWidget(0, 1, new HTML("<b>Type</b>"));
		stepsGrid.setWidget(0, 2, new HTML("<b>Description</b>"));
		stepsGrid.setWidget(0, 3, new HTML("<b>Status</b>"));
		stepsGrid.setWidget(0, 4, new HTML("<b>Result</b>"));
		stepsGrid.setWidget(0, 5, new HTML("<b>Anticipated Time</b>"));
		stepsGrid.setWidget(0, 6, new HTML("<b>Actual Time</b>"));
		stepsGrid.setWidget(0, 7, new HTML("<b>Properties</b>"));

		int gridRow = 1;
		if (presenter.getVpncpSummary().isProvision()) {
			for (int i=0; i<presenter.getVpncp().getProvisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getVpncp().getProvisioningSteps().get(i);
				addStepToGrid(gridRow, stepsGrid, psp);
				gridRow++;
			}
		}
		else {
			for (int i=0; i<presenter.getVpncdp().getProvisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getVpncdp().getProvisioningSteps().get(i);
				addStepToGrid(gridRow, stepsGrid, psp);
				gridRow++;
			}
		}
		stepsPanel.add(stepsGrid);
	}
	
	private void addStepToGrid(int gridRow, Grid stepsGrid, ProvisioningStepPojo psp) {
		HTML hStepId = new HTML(psp.getStepId());
		HTML hType = new HTML(psp.getType());
		HTML hDescription = new HTML(psp.getDescription());
		HTML hStatus = new HTML(psp.getStatus());
		HTML hResult = new HTML(psp.getStepResult());
		HTML hAnticipatedTime = new HTML(psp.getAnticipatedTime());
		HTML hActualTime = new HTML(psp.getActualTime());
		stepsGrid.setWidget(gridRow, 0, hStepId);
		stepsGrid.setWidget(gridRow, 1, hType);
		stepsGrid.setWidget(gridRow, 2, hDescription);
		stepsGrid.setWidget(gridRow, 3, hStatus);
		stepsGrid.setWidget(gridRow, 4, hResult);
		stepsGrid.setWidget(gridRow, 5, hAnticipatedTime);
		stepsGrid.setWidget(gridRow, 6, hActualTime);
		if (psp.getProperties().size() > 0) {
			StringBuffer sProps = new StringBuffer();
			Iterator<String> iter = psp.getProperties().keySet().iterator();
			boolean firstKey = true;
			while (iter.hasNext()) {
				String key = iter.next();
				String value = (String) psp.getProperties().get(key);
				if (firstKey) {
					firstKey = false;
					sProps.append(key + "=" + value);
				}
				else {
					sProps.append("<br>" + key + "=" + value);
				}
			}
			HTML hProps = new HTML(sProps.toString());
			stepsGrid.setWidget(gridRow, 7, hProps);
		}
		else {
			HTML hProps = new HTML("none");
			stepsGrid.setWidget(gridRow, 7, hProps);
		}

		if (psp.getStatus().equalsIgnoreCase(Constants.PROVISIONING_STEP_STATUS_COMPLETED)) {
			if (psp.getStepResult() == null || 
				psp.getStepResult().equalsIgnoreCase(Constants.VPCP_STEP_RESULT_SUCCESS)) {
				
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-success");
			}
			else {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-failure");
			}
		}
		else if (psp.getStatus().equalsIgnoreCase(Constants.PROVISIONING_STEP_STATUS_ROLLED_BACK)) {
			if (psp.getStepResult() == null) {
				applyGridRowFormat(stepsGrid, gridRow);
			}
			else {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-failure");
			}
		}
		else if (psp.getStatus().equalsIgnoreCase(Constants.PROVISIONING_STEP_STATUS_PENDING)) {
			if (psp.getStepResult() == null) {
				applyGridRowFormat(stepsGrid, gridRow);
			}
			else {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-failure");
			}
		}
		else {
			applyGridRowFormat(stepsGrid, gridRow);
		}
	}

	private void setProvisioningProgress() {
		if (presenter.getVpncpSummary().isProvision()) {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getVpncp().getTotalStepCount(), presenter.getVpncp().getCompletedStepCount());
	        progressHTML.setHTML(sh);
		}
		else {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getVpncdp().getTotalStepCount(), presenter.getVpncdp().getCompletedStepCount());
	        progressHTML.setHTML(sh);
		}
	}
}
