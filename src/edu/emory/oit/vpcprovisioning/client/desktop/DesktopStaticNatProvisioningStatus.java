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
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ProvisioningStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopStaticNatProvisioningStatus extends ViewImplBase implements StaticNatProvisioningStatusView {
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
	@UiField Label statusLabel;
	@UiField Label provisioningResultLabel;
	@UiField Label anticipatedTimeLabel;
	@UiField Label actualTimeLabel;
	@UiField Label publicIpLabel;
	@UiField Label privateIpLabel;
	
	@UiField HTML progressHTML;
	@UiField VerticalPanel stepsPanel;

	private static DesktopStaticNatProvisioningStatusUiBinder uiBinder = GWT
			.create(DesktopStaticNatProvisioningStatusUiBinder.class);

	interface DesktopStaticNatProvisioningStatusUiBinder extends UiBinder<Widget, DesktopStaticNatProvisioningStatus> {
	}

	public DesktopStaticNatProvisioningStatus() {
		initWidget(uiBinder.createAndBindUi(this));
		doneButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopTimer();
				ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_STATIC_NAT_PROVISIONING_SUMMARY);
			}
		}, ClickEvent.getType());
		
		refreshButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startTimer = false;
				if (presenter.getProvisioning() != null) {
					presenter.refreshProvisioningStatusForId(presenter.getProvisioning().getProvisioningId());
				}
				else {
					presenter.refreshDeprovisioningStatusForId(presenter.getDeprovisioning().getProvisioningId());
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
		startTimer = true;
		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
			@Override
			public boolean execute() {
				if (presenter.getProvisioning() != null) {
					GWT.log("[VIEW] refreshing status information for StaticNatProvisioning: " + presenter.getProvisioning().getProvisioningId());
					presenter.refreshProvisioningStatusForId(presenter.getProvisioning().getProvisioningId());
					if (presenter.getProvisioning().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
						startTimer = false;
					}
					return startTimer;
				}
				else {
					GWT.log("[VIEW] refreshing status information for StaticNatDeprovisioning: " + presenter.getDeprovisioning().getProvisioningId());
					presenter.refreshDeprovisioningStatusForId(presenter.getDeprovisioning().getProvisioningId());
					if (presenter.getDeprovisioning().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
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
		
		
	}

	@Override
	public void refreshProvisioningStatusInformation() {
		GWT.log("[DesktopVpcpStatus.refreshVpcpStatusInformation]");

		if (presenter.getProvisioning() != null) {
			provisioningIdLabel.setText(presenter.getProvisioning().getProvisioningId());
			statusLabel.setText(presenter.getProvisioning().getStatus());
			provisioningResultLabel.setText(presenter.getProvisioning().getProvisioningResult());
			anticipatedTimeLabel.setText(presenter.getProvisioning().getAnticipatedTime());
			actualTimeLabel.setText(presenter.getProvisioning().getActualTime());
			if (presenter.getProvisioning().getStaticNat() != null) {
				publicIpLabel.setText(presenter.getProvisioning().getStaticNat().getPublicIp());
				privateIpLabel.setText(presenter.getProvisioning().getStaticNat().getPrivateIp());
			}
			else {
				publicIpLabel.setText("TBD");
				privateIpLabel.setText("TBD");
			}
		}
		else {
			provisioningIdLabel.setText(presenter.getDeprovisioning().getProvisioningId());
			statusLabel.setText(presenter.getDeprovisioning().getStatus());
			provisioningResultLabel.setText(presenter.getDeprovisioning().getProvisioningResult());
			anticipatedTimeLabel.setText(presenter.getDeprovisioning().getAnticipatedTime());
			actualTimeLabel.setText(presenter.getDeprovisioning().getActualTime());
			if (presenter.getDeprovisioning().getStaticNat() != null) {
				publicIpLabel.setText(presenter.getDeprovisioning().getStaticNat().getPublicIp());
				privateIpLabel.setText(presenter.getDeprovisioning().getStaticNat().getPrivateIp());
			}
			else {
				publicIpLabel.setText("TBD");
				privateIpLabel.setText("TBD");
			}
		}
		
		setProvisioningProgress();
		
		refreshProvisioningStepInformation();
	}

	private void refreshProvisioningStepInformation() {
		Grid stepsGrid = null;
		if (presenter.getProvisioning() != null) {
			stepsGrid = new Grid(presenter.getProvisioning().getProvisioningSteps().size() + 1, 8);
		}
		else {
			stepsGrid = new Grid(presenter.getDeprovisioning().getProvisioningSteps().size() + 1, 8);
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
		if (presenter.getProvisioning() != null) {
			for (int i=0; i<presenter.getProvisioning().getProvisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getProvisioning().getProvisioningSteps().get(i);
				addStepToGrid(gridRow, stepsGrid, psp);
				gridRow++;
			}
		}
		else {
			for (int i=0; i<presenter.getDeprovisioning().getProvisioningSteps().size(); i++) {
				final ProvisioningStepPojo psp = presenter.getDeprovisioning().getProvisioningSteps().get(i);
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
			if (psp.getStepResult() == null) {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-success");
			}
			else if (psp.getStepResult().trim().equalsIgnoreCase(Constants.VPCP_STEP_RESULT_SUCCESS)) {
				stepsGrid.getRowFormatter().addStyleName(gridRow, "pspGridRow-success");
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
		if (presenter.getProvisioning() != null) {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getProvisioning().getTotalStepCount(), presenter.getProvisioning().getCompletedStepCount());
	        progressHTML.setHTML(sh);
		}
		else {
	        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getDeprovisioning().getTotalStepCount(), presenter.getDeprovisioning().getCompletedStepCount());
	        progressHTML.setHTML(sh);
		}
	}
}
