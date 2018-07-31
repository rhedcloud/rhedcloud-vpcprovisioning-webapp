package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.common.AwsServiceRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.AwsServiceSuggestion;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.ListSecurityRiskPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListSecurityRiskView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceControlPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceControlView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceGuidelinePresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceGuidelineView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceGuidelinePresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceGuidelineView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainSecurityAssessment extends ViewImplBase implements MaintainSecurityAssessmentView {
	Presenter presenter;
	boolean editing;
	UserAccountPojo userLoggedIn;
	private final AwsServiceRpcSuggestOracle serviceSuggestions = new AwsServiceRpcSuggestOracle(Constants.SUGGESTION_TYPE_AWS_SERVICE_CODE_NAME);
	int serviceRowNum = 0;
	int serviceColumnNum = 0;
	int removeButtonColumnNum = 1;
	List<String> statusTypes;

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField VerticalPanel servicesVP;
	@UiField FlexTable servicesTable;
	@UiField Button addServiceButton;
	@UiField Label servicesLabel;
	@UiField ListBox statusLB;
	@UiField(provided=true) SuggestBox serviceLookupSB = new SuggestBox(serviceSuggestions, new TextBox());

	// firewall rules and elasticip tabs
	@UiField TabLayoutPanel assessmentTabPanel;
	@UiField DeckLayoutPanel securityRisksContainer;
	@UiField DeckLayoutPanel serviceControlsContainer;
	@UiField DeckLayoutPanel serviceGuidelinesContainer;
	@UiField DeckLayoutPanel testPlanContainer;

	private boolean firstSecurityRiskWidget = true;
	private boolean firstServiceControlWidget = true;
	private boolean firstServiceGuidelineWidget = true;
	private boolean firstTestPlanWidget = true;

	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		// populate assessment and save
		populateAssessmentWithFormData();
		presenter.saveAssessment();
	}
	@UiHandler("cancelButton") 
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.SECURITY_ASSESSMENT_EDITING_CANCELED, presenter.getService());
	}
	
	@UiHandler("assessmentTabPanel")
	void tabSelected(SelectionEvent<Integer> e) {
		switch (e.getSelectedItem()) {
		case 0:
			GWT.log("need to get Security Risk content.");
			firstSecurityRiskWidget = true;
			securityRisksContainer.clear();
			ListSecurityRiskView listView = presenter.getClientFactory().getListSecurityRiskView();
			MaintainSecurityRiskView maintainView = presenter.getClientFactory().getMaintainSecurityRiskView();
			securityRisksContainer.add(listView);
			securityRisksContainer.add(maintainView);
			securityRisksContainer.setAnimationDuration(500);
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SECURITY_RISK, presenter.getService(), presenter.getSecurityAssessment());
			break;
		case 1:
			GWT.log("need to get Service Control content.");
			firstServiceControlWidget = true;
			serviceControlsContainer.clear();
			ListServiceControlView listView2 = presenter.getClientFactory().getListServiceControlView();
			MaintainServiceControlView maintainView2 = presenter.getClientFactory().getMaintainServiceControlView();
			serviceControlsContainer.add(listView2);
			serviceControlsContainer.add(maintainView2);
			serviceControlsContainer.setAnimationDuration(500);
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE_CONTROL, presenter.getService(), presenter.getSecurityAssessment());
			break;
		case 2:
			GWT.log("need to get Service Guideline content.");
			firstServiceGuidelineWidget = true;
			serviceGuidelinesContainer.clear();
			ListServiceGuidelineView listView3 = presenter.getClientFactory().getListServiceGuidelineView();
			MaintainServiceGuidelineView maintainView3 = presenter.getClientFactory().getMaintainServiceGuidelineView();
			serviceGuidelinesContainer.add(listView3);
			serviceGuidelinesContainer.add(maintainView3);
			serviceGuidelinesContainer.setAnimationDuration(500);
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE_GUIDELINE, presenter.getService(), presenter.getSecurityAssessment());
			break;
		case 3:
			GWT.log("need to get Service Test Plan content.");
			firstTestPlanWidget = true;
			testPlanContainer.clear();
			GWT.log("getting MaintainServiceTestPlan view.");
			MaintainServiceTestPlanView maintainView4 = presenter.getClientFactory().getMaintainServiceTestPlanView();
			GWT.log("got MaintainServiceTestPlan view.");
			testPlanContainer.add(maintainView4);
			testPlanContainer.setAnimationDuration(500);
			if (presenter.getSecurityAssessment().getServiceTestPlan() != null) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SERVICE_TEST_PLAN, presenter.getService(), presenter.getSecurityAssessment(), presenter.getSecurityAssessment().getServiceTestPlan());
				GWT.log("fired MAINTAIN_SERVICE_TEST_PLAN event...");
			}
			else {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_SERVICE_TEST_PLAN, presenter.getService(), presenter.getSecurityAssessment());
				GWT.log("fired CREATE_SERVICE_TEST_PLAN event...");
			}
			break;
		}
	}

	private void populateAssessmentWithFormData() {
		// populate/save service
		presenter.getSecurityAssessment().setStatus(statusLB.getSelectedValue());
		
		// risks, controls, guidelines and test plans are added as they're 
		// added to the page.
		
	}

	// TODO: test this, not sure it will work this way
	@UiHandler ("serviceLookupSB")
	void serviceLookupKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		addRelatedServiceToAssessment(presenter.getRelatedService());
        }
	}
	
	@UiHandler("addServiceButton")
	void addServiceButtonClicked(ClickEvent e) {
		if (serviceLookupSB.getText() == null || serviceLookupSB.getText().trim().length() == 0) {
			showMessageToUser(
					"Alert", 
					"Please enter the valid name of a service you wish to add to this assessment.", 
					serviceLookupSB);
			return;
		}
		addRelatedServiceToAssessment(presenter.getRelatedService());
	}
	
	void addRelatedServiceToAssessment(AWSServicePojo service) {
		if (service != null) {
			String svcId = service.getServiceId();
			if (presenter.getSecurityAssessment().getServiceIds().contains(svcId)) {
				showMessageToUser(
						"Alert", 
						"That service appears to already be related to this assessment, please pick another service.", 
						serviceLookupSB);
				return;
			}
			presenter.getSecurityAssessment().getServiceIds().add(service.getServiceId());
			addServiceToPanel(service);
		}
		else {
			showMessageToUser(
					"Alert", 
					"Please enter the valid name of a service you wish to add to this assessment.", 
					serviceLookupSB);
		}
	}
	
	@Override
	public void addRelatedServiceToView(AWSServicePojo service, String widgetTitle) {
		if (presenter.getSecurityAssessment().getServiceIds().contains(service.getServiceId()) == false) {
			addRelatedServiceToAssessment(service);
		}
		else {
			addServiceToPanel(service);
		}
	}

	void addServiceToPanel(final AWSServicePojo service) {
		final int numRows = servicesTable.getRowCount();
		final Label serviceLabel = new Label(service.getAwsServiceCode() + "/" + service.getAwsServiceName());
		serviceLabel.addStyleName("emailLabel");
		final Button removeServiceButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (this.userLoggedIn.isCentralAdmin()) {
			removeServiceButton.setEnabled(true);
		}
		else {
			removeServiceButton.setEnabled(false);
		}
		removeServiceButton.addStyleName("glowing-border");
		removeServiceButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getSecurityAssessment().getServiceIds().remove(service.getServiceId());
				servicesTable.remove(serviceLabel);
				servicesTable.remove(removeServiceButton);
			}
		});
		serviceLookupSB.setText("");
		servicesTable.setWidget(numRows, 0, serviceLabel);
		servicesTable.setWidget(numRows, 1, removeServiceButton);
	}
	
	private static DesktopMaintainSecurityAssessmentUiBinder uiBinder = GWT
			.create(DesktopMaintainSecurityAssessmentUiBinder.class);

	interface DesktopMaintainSecurityAssessmentUiBinder extends UiBinder<Widget, DesktopMaintainSecurityAssessment> {
	}

	public DesktopMaintainSecurityAssessment() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setInitialFocus() {
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyCentralAdminMask() {
		statusLB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		statusLB.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		statusLB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		ServiceSecurityAssessmentPojo assess = presenter.getSecurityAssessment();
		if (assess.getStatus() == null || assess.getStatus().length() == 0) {
			fields.add(statusLB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
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
		GWT.log("Maintain Assessment, Service is: " + presenter.getService());
		registerHandlers();
		if (presenter.getService() != null) {
			GWT.log("Security Assessments for Service: " + presenter.getService().getAwsServiceName());
		}
		else {
			GWT.log("Service in the presenter is null");
		}
		servicesTable.clear();
		serviceLookupSB.setText("");
		serviceLookupSB.getElement().setPropertyString("placeholder", "enter service name");

		SelectionEvent.fire(assessmentTabPanel, assessmentTabPanel.getSelectedIndex());
		
//		if (assessmentTabPanel.getSelectedIndex() != 0) {
//			assessmentTabPanel.selectTab(0);
//		}
//		else {
//			ListSecurityRiskView listView = presenter.getClientFactory().getListSecurityRiskView();
//			securityRisksContainer.add(listView);
//			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SECURITY_RISK, presenter.getService(), presenter.getSecurityAssessment());
//		}
	}

	private void registerHandlers() {
		serviceLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				AwsServiceSuggestion suggestion = (AwsServiceSuggestion)event.getSelectedItem();
				if (suggestion.getService() != null) {
					presenter.setRelatedService(suggestion.getService());
					serviceLookupSB.setTitle(suggestion.getService().toString());
				}
			}
		});
	}
	
	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setAssessmentStatusItems(List<String> assessmentStatusTypes) {
		this.statusTypes = assessmentStatusTypes;
		statusLB.clear();
		statusLB.addItem("-- Select --");
		if (statusLB != null) {
			int i=1;
			for (String type : statusTypes) {
				statusLB.addItem(type, type);
				if (presenter.getSecurityAssessment() != null) {
					if (presenter.getSecurityAssessment().getStatus() != null) {
						if (presenter.getSecurityAssessment().getStatus().equals(type)) {
							statusLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}
	@Override
	public void setWidget(IsWidget w) {
		if (w instanceof ListSecurityRiskPresenter || w instanceof MaintainSecurityRiskPresenter) {
			GWT.log("Maintain Security Assessment, setWidget: Security Risk");
			securityRisksContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstSecurityRiskWidget) {
				firstSecurityRiskWidget = false;
				securityRisksContainer.animate(0);
			}
			return;
		}

		if (w instanceof ListServiceControlPresenter || w instanceof MaintainServiceControlPresenter) {
			GWT.log("Maintain Security Assessment, setWidget: Service Control");
			serviceControlsContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstServiceControlWidget) {
				firstServiceControlWidget = false;
				serviceControlsContainer.animate(0);
			}
			return;
		}
		
		if (w instanceof ListServiceGuidelinePresenter || w instanceof MaintainServiceGuidelinePresenter) {
			GWT.log("Maintain Security Assessment, setWidget: Service Guideline");
			serviceGuidelinesContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstServiceGuidelineWidget) {
				firstServiceGuidelineWidget = false;
				serviceGuidelinesContainer.animate(0);
			}
			return;
		}
		
		if (w instanceof MaintainServiceTestPlanPresenter) {
			GWT.log("Maintain Security Assessment, setWidget: Test Plan");
			testPlanContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstTestPlanWidget) {
				firstTestPlanWidget = false;
				testPlanContainer.animate(0);
			}
			return;
		}
	}
	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}
}
