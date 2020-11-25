package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
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
	Timer timer;

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
					ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_VPN_CONNECTION_PROFILE);
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
		
		
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void applyNetworkAdminMask() {
		
		
	}

	@Override
	public void applyCentralAdminMask() {
		
		
	}

	@Override
	public void applyAWSAccountAdminMask() {
		
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		
		return null;
	}

	@Override
	public void resetFieldStyles() {
		
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		
		return null;
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
//		startTimer = true;
//		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
//			@Override
//			public boolean execute() {
//				if (presenter.getVpncpSummary().isProvision()) {
//					presenter.refreshProvisioningStatusForId(presenter.getVpncp().getProvisioningId());
//					if (presenter.getVpncp().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
//						startTimer = false;
//					}
//					return startTimer;
//				}
//				else {
//					presenter.refreshProvisioningStatusForId(presenter.getVpncdp().getProvisioningId());
//					if (presenter.getVpncdp().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
//						startTimer = false;
//					}
//					return startTimer;
//				}
//			}
//		}, delayMs);
		
		startTimer = true;
		timer = new Timer() {
            @Override
            public void run() {
				if (presenter.getVpncpSummary().isProvision()) {
					presenter.refreshProvisioningStatusForId(presenter.getVpncp().getProvisioningId());
					if (presenter.getVpncp().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						stopTimer();
					}
				}
				else {
					presenter.refreshProvisioningStatusForId(presenter.getVpncdp().getProvisioningId());
					if (presenter.getVpncdp().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						stopTimer();
					}
				}
            }
        };

        // Schedule the timer to close the popup in 3 seconds.
        timer.scheduleRepeating(delayMs);
	}

	@Override
	public void stopTimer() {
		startTimer = false;
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
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
			anticipatedTimeLabel.setText(formatMillisForDisplay(presenter.getVpncp().getAnticipatedTime()));
			actualTimeLabel.setText(formatMillisForDisplay(presenter.getVpncp().getActualTime()));
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
			anticipatedTimeLabel.setText(formatMillisForDisplay(presenter.getVpncdp().getAnticipatedTime()));
			actualTimeLabel.setText(formatMillisForDisplay(presenter.getVpncdp().getActualTime()));
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
		GWT.log("PSP anticipated time is: " + psp.getAnticipatedTime());
		GWT.log("PSP actual time is: " + psp.getActualTime());
		HTML hAnticipatedTime = new HTML(formatMillisForDisplay(psp.getAnticipatedTime()));
		HTML hActualTime = new HTML(formatMillisForDisplay(psp.getActualTime()));
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
			if (psp.getStepResult() == null) {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-success");
			}
			else if (psp.getStepResult().equalsIgnoreCase(Constants.VPCP_STEP_RESULT_SUCCESS)) {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-success");
			}
			else {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-failure");
			}
		}
		else if (psp.getStatus().equalsIgnoreCase(Constants.PROVISIONING_STEP_STATUS_INPROGRESS)) {
			long actualTime = 0;
			String s_actualTime = psp.getActualTime();
			if (s_actualTime != null) {
				actualTime = Long.parseLong(s_actualTime);
			}
			stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-inProgress");
			if (actualTime == 0) {
				if (psp.getProperties().size() > 0) {
					Iterator<String> iter = psp.getProperties().keySet().iterator();
					long startTime=0;
					propertyLoop: while (iter.hasNext()) {
						String key = iter.next();
						if (key.equalsIgnoreCase(Constants.PROVISIONING_STEP_PROP_STARTTIME)) {
							String s_startTime = (String) psp.getProperties().get(key);
							if (s_startTime != null) {
								// if step status is in-progress and property name is "startTime"
								// get the startTime property and calculate running actual time and percent complete
								startTime = Long.parseLong(s_startTime);
							}
							break propertyLoop;
						}
					}
					if (startTime != 0) {
						NumberFormat df2 = NumberFormat.getFormat("#00.##");
						GWT.log("Start time formated: " + new java.util.Date(startTime));
						String s_anticipatedTime = psp.getAnticipatedTime();
						double anticipatedTime = Double.parseDouble(s_anticipatedTime);
						double currentTime = System.currentTimeMillis();
						GWT.log("Current time formated: " + new java.util.Date((long)currentTime));
						double elapsedTime = currentTime - startTime;
						StringBuffer s_elapsedHtml = new StringBuffer();
						s_elapsedHtml.append(formatMillisForDisplay(Double.toString(elapsedTime)) + " ");
						double raw = (elapsedTime / anticipatedTime);
						double pctComplete = new Double(df2.format(raw)).doubleValue() * 100;
						GWT.log("PCT Complete: " + pctComplete);
						s_elapsedHtml.append("(" + pctComplete + "%)");
						HTML hElapsedTime = new HTML(s_elapsedHtml.toString());
						stepsGrid.getColumnFormatter().setWidth(6, "200px");
						stepsGrid.setWidget(gridRow, 6, hElapsedTime);
					}
				}
			}
			else {
				GWT.log("Step is in progress but actual time is greater than zero so this is weird.");
			}
		}
		else {
			applyGridRowFormat(stepsGrid, gridRow);
		}
	}

	private void setProvisioningProgress() {
		if (presenter.getVpncpSummary().isProvision()) {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getVpncp().getTotalStepCount(), presenter.getVpncp().getCompletedSuccessfulCount());
	        progressHTML.setHTML(sh);
		}
		else {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getVpncdp().getTotalStepCount(), presenter.getVpncdp().getCompletedSuccessfulCount());
	        progressHTML.setHTML(sh);
		}
	}

	@Override
	public void clearProvisioningStatus() {
		provisioningIdLabel.setText("");
		provisioningTypeLabel.setText("");
		statusLabel.setText("");
		provisioningResultLabel.setText("");
		anticipatedTimeLabel.setText("");
		actualTimeLabel.setText("");
		stepsPanel.clear();
		setProvisioningProgress();
	}
}
