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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentView;
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
	@UiField HorizontalPanel generateIncidentPanel;
	@UiField HorizontalPanel editIncidentPanel;
	
	@UiField TextBox shortDescriptionTB;
	@UiField TextArea descriptionTA;
	@UiField TextBox urgencyTB;
	@UiField TextBox impactTB;
	@UiField TextBox businessServiceTB;
	@UiField TextBox categoryTB;
	@UiField TextBox subCategoryTB;
	@UiField TextBox recordTypeTB;
	@UiField TextBox contactTypeTB;
	@UiField TextBox callerIdTB;
	@UiField TextBox cmdbCiTB;
	@UiField TextBox assignmentGroupTB;

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
			presenter.getIncidentRequisition().setShortDescription(shortDescriptionTB.getText());
			presenter.getIncidentRequisition().setDescription(descriptionTA.getText());
			presenter.getIncidentRequisition().setCallerId(callerIdTB.getText());
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
	        	descriptionTA.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
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
			// TODO: it's a generate, so we'll get whatever requisition info exists in the presenter
			shortDescriptionTB.setText(presenter.getIncidentRequisition().getShortDescription());
			descriptionTA.getElement().setPropertyString("placeholder", "enter full description [optional]");
			urgencyTB.setText(presenter.getIncidentRequisition().getUrgency());
			impactTB.setText(presenter.getIncidentRequisition().getImpact());
			businessServiceTB.setText(presenter.getIncidentRequisition().getBusinessService());
			categoryTB.setText(presenter.getIncidentRequisition().getCategory());
			subCategoryTB.setText(presenter.getIncidentRequisition().getSubCategory());
			recordTypeTB.setText(presenter.getIncidentRequisition().getRecordType());
			contactTypeTB.setText(presenter.getIncidentRequisition().getContactType());
			callerIdTB.setText(userLoggedIn.getPrincipal());
			cmdbCiTB.setText(presenter.getIncidentRequisition().getCmdbCi());
			assignmentGroupTB.setText(presenter.getIncidentRequisition().getAssignmentGroup());
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
		generateIncidentPanel.setVisible(true);
		editIncidentPanel.setVisible(false);
	}

	@Override
	public void showEditWidgets() {
		generateIncidentPanel.setVisible(false);
		editIncidentPanel.setVisible(true);
	}

}
