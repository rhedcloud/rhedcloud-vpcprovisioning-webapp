package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonSuggestion;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainServiceControl extends ViewImplBase implements MaintainServiceControlView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	boolean editing;
	PopupPanel actionsPopup = new PopupPanel(true);
	private final DirectoryPersonRpcSuggestOracle assessorSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);
	private final DirectoryPersonRpcSuggestOracle verifierSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);


	private static DesktopMaintainServiceControlUiBinder uiBinder = GWT
			.create(DesktopMaintainServiceControlUiBinder.class);

	interface DesktopMaintainServiceControlUiBinder extends UiBinder<Widget, DesktopMaintainServiceControl> {
	}

	public DesktopMaintainServiceControl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox serviceNameTB;
	@UiField TextBox sequenceNumberTB;
	@UiField TextBox controlNameTB;
	@UiField TextArea controlDescriptionTA;
	@UiField(provided=true) SuggestBox assessorLookupSB = new SuggestBox(assessorSuggestions, new TextBox());
	@UiField DateBox assessmentDB;
	@UiField(provided=true) SuggestBox verifierLookupSB = new SuggestBox(verifierSuggestions, new TextBox());
	@UiField DateBox verificationDB;

	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		if (userLoggedIn.isCentralAdmin()) {
			populateServiceControlWithFormData();
			presenter.saveAssessment();
		}
		else {
			ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), presenter.getSecurityAssessment());
		}
	}
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), presenter.getSecurityAssessment());
	}
	private void populateServiceControlWithFormData() {
		// populate/save service
		presenter.getServiceControl().setServiceId(presenter.getService().getServiceId());
		presenter.getServiceControl().setServiceControlName(controlNameTB.getText());
		presenter.getServiceControl().setDescription(controlDescriptionTA.getText());
		presenter.getServiceControl().setSequenceNumber(Integer.parseInt(sequenceNumberTB.getText()));
		if (presenter.getAssessorDirectoryPerson() != null) {
			presenter.getServiceControl().setAssessorId(presenter.getAssessorDirectoryPerson().getKey());
		}
		presenter.getServiceControl().setAssessmentDate(assessmentDB.getValue());
		if (presenter.getVerifierDirectoryPerson() != null) {
			presenter.getServiceControl().setVerifier(presenter.getVerifierDirectoryPerson().getKey());
		}
		presenter.getServiceControl().setVerificationDate(verificationDB.getValue());
	}

	private void registerHandlers() {
		assessorLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setAssessorDirectoryPerson(dp_suggestion.getDirectoryPerson());
					assessorLookupSB.setTitle(presenter.getAssessorDirectoryPerson().toString());
				}
			}
		});

		verifierLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setVerifierDirectoryPerson(dp_suggestion.getDirectoryPerson());
					verifierLookupSB.setTitle(presenter.getVerifierDirectoryPerson().toString());
				}
			}
		});
	}

	@Override
	public void hidePleaseWaitPanel() {
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	sequenceNumberTB.setFocus(true);
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
		serviceNameTB.setEnabled(true);
		sequenceNumberTB.setEnabled(true);
		controlNameTB.setEnabled(true);
		controlDescriptionTA.setEnabled(true);
		assessorLookupSB.setEnabled(true);
		assessmentDB.setEnabled(true);
		verifierLookupSB.setEnabled(true);
		verificationDB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		serviceNameTB.setEnabled(false);
		sequenceNumberTB.setEnabled(false);
		controlNameTB.setEnabled(false);
		controlDescriptionTA.setEnabled(false);
		assessorLookupSB.setEnabled(false);
		assessmentDB.setEnabled(false);
		verifierLookupSB.setEnabled(false);
		verificationDB.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		serviceNameTB.setEnabled(false);
		sequenceNumberTB.setEnabled(false);
		controlNameTB.setEnabled(false);
		controlDescriptionTA.setEnabled(false);
		assessorLookupSB.setEnabled(false);
		assessmentDB.setEnabled(false);
		verifierLookupSB.setEnabled(false);
		verificationDB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		ServiceControlPojo control = presenter.getServiceControl();
		if (control.getServiceControlName() == null || control.getServiceControlName().length() == 0) {
			fields.add(controlNameTB);
		}
		if (control.getDescription() == null || control.getDescription().length() == 0) {
			fields.add(controlDescriptionTA);
		}
		if (control.getAssessorId() == null|| control.getAssessorId().length() == 0) {
			fields.add(assessorLookupSB);
		}
		if (control.getAssessmentDate() == null) {
			fields.add(assessmentDB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(controlNameTB);
		fields.add(controlDescriptionTA);
		fields.add(assessorLookupSB);
		fields.add(assessmentDB);
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
		GWT.log("DesktopMaintainServiceControl: initPage");
		GWT.log("DesktopMaintainServiceControl:editing=" + this.editing);
		registerHandlers();
		if (editing) {
			ServiceControlPojo srp = presenter.getServiceControl();
			sequenceNumberTB.setText(Integer.toString(srp.getSequenceNumber()));
			controlNameTB.setText(srp.getServiceControlName());
			controlDescriptionTA.setText(srp.getDescription());
			// TODO: this will have to be a lookup to get the name of the person
			assessorLookupSB.setText(srp.getAssessorId());
			verifierLookupSB.setText(srp.getVerifier());
			verificationDB.setValue(srp.getVerificationDate());
			assessmentDB.setValue(srp.getAssessmentDate());
		}
		GWT.log("service name from presenter is: " + presenter.getService().getAwsServiceName());
		serviceNameTB.setText(presenter.getService().getAwsServiceName());
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
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
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}


}
