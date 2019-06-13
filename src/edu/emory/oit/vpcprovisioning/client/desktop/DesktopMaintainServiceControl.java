package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	List<String> controlTypeItems;
	List<String> implementationTypeItems;

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
	@UiField ListBox controlTypeLB;
	@UiField ListBox implementationTypeLB;

	// Documenation URLs associated to this service control
	@UiField VerticalPanel urlsVP;
	@UiField TextBox urlTF;
	@UiField Button addUrlButton;
	@UiField FlexTable urlTable;

	@UiHandler ("addUrlButton")
	void addElasticIpButtonClick(ClickEvent e) {
		urlTF.setEnabled(true);
		if (addUrlButton.getText().equalsIgnoreCase("Add")) {
			addUrl(urlTF.getText());
		}
		else {
			// update existing url
//			PropertyPojo property = createPropertyFromFormData();
//			presenter.updateProperty(property);
//			initializeAccountPropertiesPanel();
		}
		urlTF.setText("");
	}
	
	private void initializeDocumentationUrlPanel() {
		addUrlButton.setText("Add");
		urlTF.setEnabled(true);
		urlTable.removeAllRows();
		for (String url : presenter.getServiceControl().getDocumentationUrls()) {
			this.addUrlToPanel(url);
		}
	}

	private void addUrlToPanel(final String url) {
		final int numRows = urlTable.getRowCount();
		
		final Anchor docUrlAnchor = new Anchor(url);
		docUrlAnchor.addStyleName("emailLabel");
		docUrlAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open(url, "_blank", "");
			}
		});

//		final PushButton editButton = new PushButton();
//		editButton.setTitle("Edit this URL.");
//		Image editImage = new Image("images/edit_icon.png");
//		editImage.setWidth("30px");
//		editImage.setHeight("30px");
//		editButton.getUpFace().setImage(editImage);
//		if (this.userLoggedIn.isCentralAdmin()) {
//			editButton.setEnabled(true);
//		}
//		else {
//			editButton.setEnabled(false);
//		}
//		editButton.addStyleName("glowing-border");
//		editButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				presenter.setSelectedUrl(url);
//				urlTF.setText(url);
//				urlTF.setEnabled(false);
//				addUrlButton.setText("Update");
//				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
//			        public void execute () {
//			        	urlTF.setFocus(true);
//			        }
//			    });
//			}
//		});
		
		final PushButton removeButton = new PushButton();
		removeButton.setTitle("Remove this URL.");
		Image removeImage = new Image("images/delete_icon.png");
		removeImage.setWidth("30px");
		removeImage.setHeight("30px");
		removeButton.getUpFace().setImage(removeImage);
		// disable buttons if userLoggedIn is NOT a central admin
		if (this.userLoggedIn.isCentralAdmin()) {
			removeButton.setEnabled(true);
		}
		else {
			removeButton.setEnabled(false);
		}
		removeButton.addStyleName("glowing-border");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getServiceControl().getDocumentationUrls().remove(url);
				initPage();
			}
		});
		
		urlTable.setWidget(numRows, 0, docUrlAnchor);
//		urlTable.setWidget(numRows, 1, editButton);
		urlTable.setWidget(numRows, 1, removeButton);
	}

	private void addUrl(String url) {
		List<Widget> fields = getMissingUrlFields();
		if (fields != null && fields.size() > 0) {
			setFieldViolations(true);
			applyStyleToMissingFields(fields);
			showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			setFieldViolations(false);
			resetFieldStyles();
		}
		presenter.getServiceControl().getDocumentationUrls().add(url);
		addUrlToPanel(url);
		this.resetFieldStyles();
	}

	private List<Widget> getMissingUrlFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (urlTF.getText() == null || urlTF.getText().length() == 0) {
			fields.add(urlTF);
		}
		return fields;
	}

	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		if (userLoggedIn.isCentralAdmin()) {
			populateServiceControlWithFormData();
			presenter.saveAssessment();
		}
		else {
			// TODO: need to go to back to the maintain security risk view and refresh the service
			// control list.
			// so, close this window and tell the security risk view to refresh that list
			ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_RISK, false, presenter.getService(), presenter.getSecurityAssessment(), presenter.getRisk());
		}
	}
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		// TODO: need to go to back to the maintain security risk view and refresh the service
		// control list.
		// so, close this window and tell the security risk view to refresh that list
		ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_RISK, false, presenter.getService(), presenter.getSecurityAssessment(), presenter.getRisk());
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
		GWT.log("verification date (widget);" + verificationDB.getValue());
		if (verificationDB.getValue() != null) {
			presenter.getServiceControl().setVerificationDate(verificationDB.getValue());
		}
		presenter.getServiceControl().setControlType(controlTypeLB.getSelectedValue());
		presenter.getServiceControl().setImplementationType(implementationTypeLB.getSelectedValue());
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
		
		
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	controlNameTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		
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
		implementationTypeLB.setEnabled(true);
		controlTypeLB.setEnabled(true);
		urlTF.setEnabled(true);
		addUrlButton.setEnabled(true);
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
		implementationTypeLB.setEnabled(false);
		controlTypeLB.setEnabled(false);
		urlTF.setEnabled(false);
		addUrlButton.setEnabled(false);
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
		implementationTypeLB.setEnabled(false);
		controlTypeLB.setEnabled(false);
		urlTF.setEnabled(false);
		addUrlButton.setEnabled(false);
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
		if (control.getControlType() == null || control.getControlType().length() == 0) {
			fields.add(controlTypeLB);
		}
		if (control.getImplementationType() == null || control.getImplementationType().length() == 0) {
			fields.add(implementationTypeLB);
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
		fields.add(controlTypeLB);
		fields.add(implementationTypeLB);
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
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		
		
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
			assessorLookupSB.setText(srp.getAssessorId());
			verifierLookupSB.setText(srp.getVerifier());
			verificationDB.setValue(srp.getVerificationDate());
			assessmentDB.setValue(srp.getAssessmentDate());
		}
		else {
			ServiceControlPojo srp = presenter.getServiceControl();
			sequenceNumberTB.setText(Integer.toString(srp.getSequenceNumber()));
			assessorLookupSB.setText("");
			assessorLookupSB.getElement().setPropertyString("placeholder", "enter name");
			verifierLookupSB.setText("");
			verifierLookupSB.getElement().setPropertyString("placeholder", "enter name");
		}
		GWT.log("service name from presenter is: " + presenter.getService().getAwsServiceName());
		serviceNameTB.setText(presenter.getService().getAwsServiceName());
		// populate documenation url panel
		initializeDocumentationUrlPanel();
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
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
	@Override
	public void setServiceControlTypeItems(List<String> controlTypes) {
		this.controlTypeItems = controlTypes;
		controlTypeLB.clear();
		controlTypeLB.addItem("-- Select --", "");
		if (controlTypeItems != null) {
			int i=1;
			for (String type : controlTypeItems) {
				controlTypeLB.addItem(type, type);
				if (presenter.getServiceControl() != null) {
					if (presenter.getServiceControl().getControlType() != null) {
						if (presenter.getServiceControl().getControlType().equals(type)) {
							controlTypeLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}
	@Override
	public void setServiceControlImplementationTypeItems(List<String> implTypes) {
		this.implementationTypeItems = implTypes;
		implementationTypeLB.clear();
		implementationTypeLB.addItem("-- Select --", "");
		if (implementationTypeItems != null) {
			int i=1;
			for (String type : implementationTypeItems) {
				implementationTypeLB.addItem(type, type);
				if (presenter.getServiceControl() != null) {
					if (presenter.getServiceControl().getImplementationType() != null) {
						if (presenter.getServiceControl().getImplementationType().equals(type)) {
							implementationTypeLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}


}
