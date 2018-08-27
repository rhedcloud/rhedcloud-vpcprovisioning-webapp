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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainIncident extends ViewImplBase implements MaintainIncidentView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
//	List<String> statusTypes;
	boolean editing;

	@UiField HTML pleaseWaitHTML;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField HorizontalPanel generateTerminateAccountIncidentPanel;
	@UiField VerticalPanel generateCreateServiceAccountIncidentPanel;
	@UiField HorizontalPanel editIncidentPanel;

	// terminate account fields
	@UiField Label tAcctOwnerApprovesLabel;
	@UiField CheckBox tAcctOwnerApprovesCB;
	@UiField Label tAcctReadyLabel;
	@UiField CheckBox tAcctReadyCB;
	@UiField Label tAcctConfirmLabel;
	@UiField CheckBox tAcctConfirmCB;
	
	// create service account fields
	@UiField Label ownerApprovesLabel;
	@UiField CheckBox ownerApprovesCB;
	@UiField Label svcAcctDescriptionLabel;
	@UiField Label svcAcctIamPoliciesLabel;
	@UiField Label svcAcctAlternativeMethodsLabel;
	@UiField TextArea svcAcctDescriptionTA;
	@UiField TextArea svcAcctIamPoliciesTA;
	@UiField TextArea svcAcctAlternativeMethodsTA;

	private static DesktopMaintainIncidentUiBinder uiBinder = GWT.create(DesktopMaintainIncidentUiBinder.class);

	interface DesktopMaintainIncidentUiBinder extends UiBinder<Widget, DesktopMaintainIncident> {
	}

	public DesktopMaintainIncident() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		if (!editing) {
			// generate -> IncidentRequisition
			// TODO: build the description based on the type of incident being generated
			if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT)) {
				StringBuffer sbuf = new StringBuffer();
				sbuf.append(svcAcctDescriptionLabel.getText());
				sbuf.append("\n");
				sbuf.append("Customer Response: " + svcAcctDescriptionTA.getText());
				sbuf.append("-\n\n");
				sbuf.append(svcAcctIamPoliciesLabel.getText());
				sbuf.append("\n");
				sbuf.append("Customer Response: " + svcAcctIamPoliciesTA.getText());
				sbuf.append("-\n\n");
				sbuf.append(svcAcctAlternativeMethodsLabel.getText());
				sbuf.append("\n");
				sbuf.append("Customer Response: " + svcAcctAlternativeMethodsTA.getText());
				sbuf.append("-\n\n");
				sbuf.append(ownerApprovesLabel.getText());
				sbuf.append("\n");
				sbuf.append("Customer Response: " + (ownerApprovesCB.getValue() ? "Yes" : "No"));
				presenter.getIncidentRequisition().setDescription(sbuf.toString());
			}
			else if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT)) {
				// terminate account
				StringBuffer sbuf = new StringBuffer();
				sbuf.append(tAcctOwnerApprovesLabel.getText());
				sbuf.append("\n");
				sbuf.append("Customer Response: " + (tAcctOwnerApprovesCB.getValue() ? "Yes" : "No"));
				sbuf.append("-\n\n");
				sbuf.append(tAcctReadyLabel.getText());
				sbuf.append("\n");
				sbuf.append("Customer Response: " + (tAcctReadyCB.getValue() ? "Yes" : "No"));
				sbuf.append("-\n\n");
				sbuf.append(tAcctConfirmLabel.getText());
				sbuf.append("\n");
				sbuf.append("Customer Response: " + (tAcctConfirmCB.getValue() ? "Yes" : "No"));
				presenter.getIncidentRequisition().setDescription(sbuf.toString());
			}
			presenter.saveIncident();
		}
		else {
			// update -> Incident
		}
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
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
				if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT)) {
					svcAcctDescriptionTA.setFocus(true);
				}
				else if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT)) {
					// terminate account
				}
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return cancelButton;
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
		if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT)) {
			// create service account
			if (svcAcctDescriptionTA.getText() == null || svcAcctDescriptionTA.getText().length() == 0) {
				fields.add(svcAcctDescriptionTA);
			}
			if (svcAcctIamPoliciesTA.getText() == null || svcAcctIamPoliciesTA.getText().length() == 0) {
				fields.add(svcAcctIamPoliciesTA);
			}
			if (svcAcctAlternativeMethodsTA.getText() == null || svcAcctAlternativeMethodsTA.getText().length() == 0) {
				fields.add(svcAcctAlternativeMethodsTA);
			}
			if (!ownerApprovesCB.getValue()) {
				fields.add(ownerApprovesCB);
			}
		}
		else if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT)) {
			// terminate account
			if (!tAcctOwnerApprovesCB.getValue()) {
				fields.add(tAcctOwnerApprovesCB);
			}
			if (!tAcctReadyCB.getValue()) {
				fields.add(tAcctReadyCB);
			}
			if (!tAcctConfirmCB.getValue()) {
				fields.add(tAcctConfirmCB);
			}
		}

		return fields;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		// TODO Auto-generated method stub
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
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIncidentIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIncidentNameViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		if (!editing) {
			if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT)) {
				// create service account
				DirectoryMetaDataPojo dmd = presenter.getAccount().getAccountOwnerDirectoryMetaData();
				String ownerInfo = dmd.getFirstName() + " " + dmd.getLastName();
				String intro = ownerApprovesLabel.
						getText().
						replace("ACCOUNT_OWNER", ownerInfo);
				ownerApprovesLabel.setText(intro);
				
				svcAcctDescriptionTA.getElement().setPropertyString("placeholder", "enter your response");
				svcAcctIamPoliciesTA.getElement().setPropertyString("placeholder", "enter your response");
				svcAcctAlternativeMethodsTA.getElement().setPropertyString("placeholder", "enter your response");
			}
			else if (presenter.getIncidentType().equals(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT)) {
				// terminate account
				DirectoryMetaDataPojo dmd = presenter.getAccount().getAccountOwnerDirectoryMetaData();
				String ownerInfo = dmd.getFirstName() + " " + dmd.getLastName();
				String intro = tAcctOwnerApprovesLabel.
						getText().
						replace("ACCOUNT_OWNER", ownerInfo);
				tAcctOwnerApprovesLabel.setText(intro);
			}
		}
		else {
			// TODO: it's an edit so we'll get whatever incident info exists in the presenter.incident
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showGenerateWidgets() {
		editIncidentPanel.setVisible(false);
		if (presenter.getIncidentType().equalsIgnoreCase(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT)) {
			generateCreateServiceAccountIncidentPanel.setVisible(false);
			generateTerminateAccountIncidentPanel.setVisible(true);
			return;
		}
		if (presenter.getIncidentType().equalsIgnoreCase(Constants.INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT)) {
			generateTerminateAccountIncidentPanel.setVisible(false);
			generateCreateServiceAccountIncidentPanel.setVisible(true);
			return;
		}
	}

	@Override
	public void showEditWidgets() {
		generateCreateServiceAccountIncidentPanel.setVisible(false);
		generateTerminateAccountIncidentPanel.setVisible(false);
		editIncidentPanel.setVisible(true);
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}
}
