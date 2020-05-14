package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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

import edu.emory.oit.vpcprovisioning.client.common.Notification;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.client.ui.HTMLUtils;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.AccountProvisioningStatusView;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ProvisioningStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopAccountProvisioningStatus extends ViewImplBase implements AccountProvisioningStatusView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	UserAccountPojo userLoggedIn;
	Timer timer;

	boolean startTimer = false;

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


	private static DesktopAccountProvisioningStatusUiBinder uiBinder = GWT
			.create(DesktopAccountProvisioningStatusUiBinder.class);

	interface DesktopAccountProvisioningStatusUiBinder extends UiBinder<Widget, DesktopAccountProvisioningStatus> {
	}

	public DesktopAccountProvisioningStatus() {
		initWidget(uiBinder.createAndBindUi(this));
	
		doneButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopTimer();
				if (presenter.isFromGenerate()) {
					// need to account for account deprovisions from multiple places:
					// if it was deprovisioned from the Account provisioning list, 
					// we need to go back to that page (GO_HOME_ACCOUNT_PROVISIONING)
					// if it was deprovisioned from the account list, we need to go to GO_HOME_ACCOUNT
					if (!presenter.getProvisioningSummary().isProvision()) {
						if (presenter.isFromProvisioningList()) {
							ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_ACCOUNT_PROVISIONING);
							
						}
						else {
							ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_ACCOUNT);
						}
					}
				}
				else {
					// if it's a status check, we also need to go to GO_HOME_ACCOUNT_PROVISIONING
					ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_ACCOUNT_PROVISIONING);
				}
			}
		}, ClickEvent.getType());
		
		refreshButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopTimer();
				if (presenter.getProvisioningSummary().isProvision()) {
					presenter.refreshProvisioningStatusForId(presenter.getProvisioning().getProvisioningId());
				}
				else {
					// refresh the deprovisioing object
					presenter.refreshProvisioningStatusForId(presenter.getDeprovisioning().getDeprovisioningId());
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void startTimer(int delayMs) {
		GWT.log("[VIEW] starting timer...");
		startTimer = true;
		timer = new Timer() {
            @Override
            public void run() {
				if (presenter.getProvisioningSummary().isProvision()) {
					presenter.refreshProvisioningStatusForId(presenter.getProvisioning().getProvisioningId());
					if (presenter.getProvisioning().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						stopTimer();
					}
				}
				else {
					presenter.refreshProvisioningStatusForId(presenter.getDeprovisioning().getDeprovisioningId());
					if (presenter.getDeprovisioning().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshProvisioningStatusInformation() {
		GWT.log("[DesktopAccountProvisioningStatus.refreshProvisioningStatusInformation]");

		if (presenter.getProvisioningSummary().isProvision()) {
			provisioningIdLabel.setText(presenter.getProvisioning().getProvisioningId());
			provisioningTypeLabel.setText(Constants.VPN_PROVISIONING);
			statusLabel.setText(presenter.getProvisioning().getStatus());
			if (presenter.getProvisioning().getProvisioningResult() == null) {
				provisioningResultLabel.setText(Constants.NOT_APPLICABLE);
			}
			else {
				provisioningResultLabel.setText(presenter.getProvisioning().getProvisioningResult());
			}
			anticipatedTimeLabel.setText(formatMillisForDisplay(presenter.getProvisioning().getAnticipatedTime()));
			actualTimeLabel.setText(formatMillisForDisplay(presenter.getProvisioning().getActualTime()));
		}
		else {
			provisioningIdLabel.setText(presenter.getDeprovisioning().getDeprovisioningId());
			provisioningTypeLabel.setText(Constants.VPN_DEPROVISIONING);
			statusLabel.setText(presenter.getDeprovisioning().getStatus());
			if (presenter.getDeprovisioning().getDeprovisioningResult() == null) {
				provisioningResultLabel.setText(Constants.NOT_APPLICABLE);
			}
			else {
				provisioningResultLabel.setText(presenter.getDeprovisioning().getDeprovisioningResult());
			}
			anticipatedTimeLabel.setText(formatMillisForDisplay(presenter.getDeprovisioning().getAnticipatedTime()));
			actualTimeLabel.setText(formatMillisForDisplay(presenter.getDeprovisioning().getActualTime()));
		}
		
		setProvisioningProgress();
		
		refreshProvisioningStepInformation();
	}

	private void refreshProvisioningStepInformation() {
		Grid stepsGrid = null;
		if (presenter.getProvisioningSummary().isProvision()) {
			stepsGrid = new Grid(presenter.getProvisioning().getProvisioningSteps().size() + 1, 8);
		}
		else {
			stepsGrid = new Grid(presenter.getDeprovisioning().getDeprovisioningSteps().size() + 1, 8);
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
		if (presenter.getProvisioningSummary().isProvision()) {
			for (int i=0; i<presenter.getProvisioning().getProvisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getProvisioning().getProvisioningSteps().get(i);
				addStepToGrid(gridRow, stepsGrid, psp);
				gridRow++;
			}
		}
		else {
			for (int i=0; i<presenter.getDeprovisioning().getDeprovisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getDeprovisioning().getDeprovisioningSteps().get(i);
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

	private void setProvisioningProgress() {
		if (presenter.getProvisioningSummary().isProvision()) {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getProvisioning().getTotalStepCount(), presenter.getProvisioning().getCompletedSuccessfulCount());
	        progressHTML.setHTML(sh);
		}
		else {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getDeprovisioning().getTotalStepCount(), presenter.getDeprovisioning().getCompletedSuccessfulCount());
	        progressHTML.setHTML(sh);
		}
	}

	@Override
	public boolean isTimerRunning() {
		return this.startTimer;
	}

}
