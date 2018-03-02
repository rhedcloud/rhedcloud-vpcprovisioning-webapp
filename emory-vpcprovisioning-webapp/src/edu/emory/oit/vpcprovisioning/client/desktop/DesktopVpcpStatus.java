package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
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
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ProvisioningStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopVpcpStatus extends ViewImplBase implements VpcpStatusView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	UserAccountPojo userLoggedIn;
	boolean startTimer = true;
	int netIdRowNum = 0;
	int netIdColumnNum = 0;

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button doneButton;
	@UiField Button refreshButton;
	
	@UiField Label provisioningIdLabel;
	@UiField Label vpcpStatusLabel;
	@UiField Label provisioningResultLabel;
	@UiField Label anticipatedTimeLabel;
	@UiField Label actualTimeLabel;
	@UiField Label requestorNetIdLabel;
	@UiField Label ownerNetIdLabel;
	@UiField Label speedTypeLabel;
	@UiField Label vpcTypeLabel;
	@UiField Label complianceClassLabel;
	@UiField Label notifyAdminsLabel;
	
	@UiField VerticalPanel netIdVP;
	@UiField FlexTable netIdTable;
	@UiField HTML progressHTML;
	@UiField VerticalPanel vpcpStepsPanel;

	private static DesktopVpcpStatusUiBinder uiBinder = GWT.create(DesktopVpcpStatusUiBinder.class);

	interface DesktopVpcpStatusUiBinder extends UiBinder<Widget, DesktopVpcpStatus> {
	}

	@UiHandler ("ownerNetIdLabel")
	void ownerNetIdTBMouseOver(MouseOverEvent e) {
		presenter.setDirectoryMetaDataTitleOnWidget(presenter.getVpcp().getVpcRequisition().getAccountOwnerNetId(), ownerNetIdLabel);
	}

	@UiHandler ("requestorNetIdLabel")
	void requestorNetIdTBMouseOver(MouseOverEvent e) {
		presenter.setDirectoryMetaDataTitleOnWidget(presenter.getVpcp().getVpcRequisition().getAuthenticatedRequestorNetId(), requestorNetIdLabel);
	}

	public DesktopVpcpStatus() {
		initWidget(uiBinder.createAndBindUi(this));
		
		doneButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startTimer = false;
				ActionEvent.fire(presenter.getEventBus(), ActionNames.VPCP_EDITING_CANCELED);
			}
		}, ClickEvent.getType());
		
		refreshButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startTimer = false;
				presenter.refreshVpcpStatusForId(presenter.getVpcp().getProvisioningId());
			}
		}, ClickEvent.getType());
		

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
	public void applyEmoryAWSAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
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
	public void setVpcpIdViolation(String message) {
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
		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
			@Override
			public boolean execute() {
				GWT.log("[VIEW] refreshing status information for VPCP: " + presenter.getVpcp().getProvisioningId());
				presenter.refreshVpcpStatusForId(presenter.getVpcp().getProvisioningId());
				// change startTimer to 'false' to stop the timer.  This may
				// happen if we allow the user to click a button to refresh
				
				// check the status of the VPCP and if it's done generating
				// don't keep running the timer.
				if (presenter.getVpcp().getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
					startTimer = false;
				}
				return startTimer;
			}
		}, delayMs);
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void refreshVpcpStatusInformation() {
		GWT.log("[DesktopVpcpStatus.refreshVpcpStatusInformation]");
		netIdRowNum = 0;
		netIdColumnNum = 0;

		// admin net id list...
		initializeNetIdPanel();
		
		provisioningIdLabel.setText(presenter.getVpcp().getProvisioningId());
		vpcpStatusLabel.setText(presenter.getVpcp().getStatus());
		provisioningResultLabel.setText(presenter.getVpcp().getProvisioningResult());
		anticipatedTimeLabel.setText(presenter.getVpcp().getAnticipatedTime());
		actualTimeLabel.setText(presenter.getVpcp().getActualTime());
		requestorNetIdLabel.setText(presenter.getVpcp().getVpcRequisition().getAuthenticatedRequestorNetId());
		ownerNetIdLabel.setText(presenter.getVpcp().getVpcRequisition().getAccountOwnerNetId());
		speedTypeLabel.setText(presenter.getVpcp().getVpcRequisition().getSpeedType());
		vpcTypeLabel.setText(presenter.getVpcp().getVpcRequisition().getType());
		complianceClassLabel.setText(presenter.getVpcp().getVpcRequisition().getComplianceClass());
		notifyAdminsLabel.setText(Boolean.toString(presenter.getVpcp().getVpcRequisition().isNotifyAdmins()));
		
		setVpcpProgress();
		
		refreshProvisioningStepInformation();
	}

	private void refreshProvisioningStepInformation() {
		Grid stepsGrid = new Grid(presenter.getVpcp().getProvisioningSteps().size() + 1, 7);
		stepsGrid.setCellPadding(8);
		vpcpStepsPanel.clear();

		stepsGrid.setWidget(0, 0, new HTML("<b>Step ID</b>"));
		stepsGrid.setWidget(0, 1, new HTML("<b>Type</b>"));
		stepsGrid.setWidget(0, 2, new HTML("<b>Description</b>"));
		stepsGrid.setWidget(0, 3, new HTML("<b>Status</b>"));
		stepsGrid.setWidget(0, 4, new HTML("<b>Actual Time</b>"));
		stepsGrid.setWidget(0, 5, new HTML("<b>Anticipated Time</b>"));
		stepsGrid.setWidget(0, 6, new HTML("<b>Properties</b>"));

		int gridRow = 1;
		for (int i=0; i<presenter.getVpcp().getProvisioningSteps().size(); i++) {
			final ProvisioningStepPojo psp = presenter.getVpcp().getProvisioningSteps().get(i);
			
			HTML hStepId = new HTML(psp.getStepId());
			HTML hType = new HTML(psp.getType());
			HTML hDescription = new HTML(psp.getDescription());
			HTML hStatus = new HTML(psp.getStatus());
			HTML hActualTime = new HTML(psp.getActualTime());
			HTML hAnticipatedTime = new HTML(psp.getAnticipatedTime());
			stepsGrid.setWidget(gridRow, 0, hStepId);
			stepsGrid.setWidget(gridRow, 1, hType);
			stepsGrid.setWidget(gridRow, 2, hDescription);
			stepsGrid.setWidget(gridRow, 3, hStatus);
			stepsGrid.setWidget(gridRow, 4, hActualTime);
			stepsGrid.setWidget(gridRow, 5, hAnticipatedTime);
			if (psp.getProperties().size() > 0) {
				StringBuffer sProps = new StringBuffer();
				@SuppressWarnings("unchecked")
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
				stepsGrid.setWidget(gridRow, 6, hProps);
			}
			else {
				HTML hProps = new HTML("none");
				stepsGrid.setWidget(gridRow, 6, hProps);
			}

			if (psp.getStatus().equalsIgnoreCase(Constants.VPCP_STEP_STATUS_COMPLETED)) {
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
			else {
				applyGridRowFormat(stepsGrid, gridRow);
			}
			gridRow++;
		}
		vpcpStepsPanel.add(stepsGrid);
	}

	private void setVpcpProgress() {
        SafeHtml sh = HTMLUtils.getProgressBarSafeHtml(presenter.getVpcp().getTotalStepCount(), presenter.getVpcp().getCompletedStepCount());
        progressHTML.setHTML(sh);
	}

	private void addNetIdToPanel(final String netId) {
		int numRows = netIdTable.getRowCount();
		final Label netIdLabel = new Label(netId);
		netIdLabel.addStyleName("statusLabel");
		netIdLabel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				presenter.setDirectoryMetaDataTitleOnWidget(netId, netIdLabel);
			}
		});
		if (numRows > 2) {
			if (netIdRowNum > 0) {
				netIdRowNum = 0;
				netIdColumnNum = netIdColumnNum + 1;
			}
			else {
				netIdRowNum ++;
			}
		}
		else {
			netIdRowNum = numRows;
		}
		netIdTable.setWidget(netIdRowNum, netIdColumnNum, netIdLabel);
	}

	void initializeNetIdPanel() {
		netIdTable.removeAllRows();
		if (presenter.getVpcp().getVpcRequisition() != null) {
			GWT.log("Adding " + presenter.getVpcp().getVpcRequisition().getCustomerAdminNetIdList().size() + " net ids to the panel (update).");
			for (String netId : presenter.getVpcp().getVpcRequisition().getCustomerAdminNetIdList()) {
				addNetIdToPanel(netId);
			}
		}
	}

	@Override
	public void stopTimer() {
		startTimer = false;
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
		return doneButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return doneButton;
	}
}
