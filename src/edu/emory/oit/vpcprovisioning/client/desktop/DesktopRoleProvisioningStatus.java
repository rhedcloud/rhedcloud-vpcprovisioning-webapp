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
import edu.emory.oit.vpcprovisioning.presenter.role.RoleProvisioningStatusView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.ProvisioningStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopRoleProvisioningStatus extends ViewImplBase implements RoleProvisioningStatusView {
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


	private static DesktopRoleProvisioningStatusUiBinder uiBinder = GWT
			.create(DesktopRoleProvisioningStatusUiBinder.class);

	interface DesktopRoleProvisioningStatusUiBinder extends UiBinder<Widget, DesktopRoleProvisioningStatus> {
	}

	public DesktopRoleProvisioningStatus() {
		initWidget(uiBinder.createAndBindUi(this));
		doneButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopTimer();
				GWT.log("RoleProvisioningStatus: presenter's account is " + presenter.getAccount());
				if (presenter.getRoleProvisioningSummary().isProvision()) {
					if (!presenter.getRoleProvisioning().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						// this starts a timer that will keep checking the 
						// status and show a dialog when/if it completes
						ActionEvent.fire(presenter.getEventBus(), ActionNames.CHECK_ROLE_PROVISIONING_STATUS, presenter.getRoleProvisioningSummary());
					}
				}
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT, presenter.getAccount());
			}
		}, ClickEvent.getType());
		
		refreshButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startTimer = false;
				if (presenter.getRoleProvisioningSummary().isProvision()) {
					presenter.refreshProvisioningStatusForId(presenter.getRoleProvisioning().getProvisioningId());
				}
				else {
					// refresh the deprovisioing object
					presenter.refreshProvisioningStatusForId(presenter.getRoleDeprovisioning().getDeprovisioningId());
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
		GWT.log("[RoleProvisioningStatus VIEW] starting timer...");
		
		startTimer = true;
		timer = new Timer() {
            @Override
            public void run() {
				if (presenter.getRoleProvisioningSummary().isProvision()) {
					presenter.refreshProvisioningStatusForId(presenter.getRoleProvisioning().getProvisioningId());
					if (presenter.getRoleProvisioning().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						GWT.log("[DesktopRoleProvisioningStatus.startTimer.run] provisioning is complete, time to stop the timer...");
						stopTimer();

						// TODO: if the provisioning run was successful and 
						// there is an assignee in the summary object, do a 
						// RoleAssignment.Generate?
						// IDEALLY, this will all happen as part of the provisioning
						// process.  So, I think it will be better to try and 
						// handle that there instead of here.
//						if (presenter.getRoleProvisioning().isSuccessful() && 
//							presenter.getRoleProvisioningSummary().getAssignee() != null) {
//							
//							DirectoryPersonPojo roleAssignee = presenter.getRoleProvisioningSummary().getAssignee();
//							AccountPojo account = presenter.getRoleProvisioningSummary().getAccount();
//							String customRoleName = presenter.getRoleProvisioningSummary().getProvisioning().getRequisition().getCustomRoleName();
//							presenter.addDirectoryPersonInRoleToAccount(roleAssignee, account, customRoleName);
//						}
					}
				}
				else {
					presenter.refreshProvisioningStatusForId(presenter.getRoleDeprovisioning().getDeprovisioningId());
					if (presenter.getRoleDeprovisioning().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						GWT.log("[DesktopRoleProvisioningStatus.startTimer.run] de-provisioning is complete, time to stop the timer...");
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
		GWT.log("[RoleProvisioningStatus VIEW] stopping timer...");
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
		GWT.log("[DesktopRolProvisioningStatus.refreshProvisioningStatusInformation]");

		if (presenter.getRoleProvisioningSummary().isProvision()) {
			provisioningIdLabel.setText(presenter.getRoleProvisioning().getProvisioningId());
			provisioningTypeLabel.setText(Constants.VPN_PROVISIONING);
			statusLabel.setText(presenter.getRoleProvisioning().getStatus());
			if (presenter.getRoleProvisioning().getProvisioningResult() == null) {
				provisioningResultLabel.setText(Constants.NOT_APPLICABLE);
			}
			else {
				provisioningResultLabel.setText(presenter.getRoleProvisioning().getProvisioningResult());
			}
			anticipatedTimeLabel.setText(formatMillisForDisplay(presenter.getRoleProvisioning().getAnticipatedTime()));
			actualTimeLabel.setText(formatMillisForDisplay(presenter.getRoleProvisioning().getActualTime()));
		}
		else {
			provisioningIdLabel.setText(presenter.getRoleDeprovisioning().getDeprovisioningId());
			provisioningTypeLabel.setText(Constants.VPN_DEPROVISIONING);
			statusLabel.setText(presenter.getRoleDeprovisioning().getStatus());
			if (presenter.getRoleDeprovisioning().getDeprovisioningResult() == null) {
				provisioningResultLabel.setText(Constants.NOT_APPLICABLE);
			}
			else {
				provisioningResultLabel.setText(presenter.getRoleDeprovisioning().getDeprovisioningResult());
			}
			anticipatedTimeLabel.setText(formatMillisForDisplay(presenter.getRoleDeprovisioning().getAnticipatedTime()));
			actualTimeLabel.setText(formatMillisForDisplay(presenter.getRoleDeprovisioning().getActualTime()));
		}
		
		setProvisioningProgress();
		
		refreshProvisioningStepInformation();
	}

	private void refreshProvisioningStepInformation() {
		Grid stepsGrid = null;
		if (presenter.getRoleProvisioningSummary().isProvision()) {
			stepsGrid = new Grid(presenter.getRoleProvisioning().getProvisioningSteps().size() + 1, 8);
		}
		else {
			stepsGrid = new Grid(presenter.getRoleDeprovisioning().getDeprovisioningSteps().size() + 1, 8);
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
		if (presenter.getRoleProvisioningSummary().isProvision()) {
			for (int i=0; i<presenter.getRoleProvisioning().getProvisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getRoleProvisioning().getProvisioningSteps().get(i);
				addStepToGrid(gridRow, stepsGrid, psp);
				gridRow++;
			}
		}
		else {
			for (int i=0; i<presenter.getRoleDeprovisioning().getDeprovisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getRoleDeprovisioning().getDeprovisioningSteps().get(i);
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
						String s_anticipatedTime = psp.getAnticipatedTime();
						double anticipatedTime = Double.parseDouble(s_anticipatedTime);
						double currentTime = System.currentTimeMillis();
						double elapsedTime = currentTime - startTime;
						StringBuffer s_elapsedHtml = new StringBuffer();
						s_elapsedHtml.append(formatMillisForDisplay(Double.toString(elapsedTime)) + " ");
						double raw = (elapsedTime / anticipatedTime);
						double pctComplete = new Double(df2.format(raw)).doubleValue() * 100;
						GWT.log("Step: " + psp.getStepId() + " PCT Complete: " + pctComplete);
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
		if (presenter.getRoleProvisioningSummary().isProvision()) {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getRoleProvisioning().getTotalStepCount(), presenter.getRoleProvisioning().getCompletedSuccessfullCount());
	        progressHTML.setHTML(sh);
		}
		else {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getRoleDeprovisioning().getTotalStepCount(), presenter.getRoleDeprovisioning().getCompletedSuccessfulCount());
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
