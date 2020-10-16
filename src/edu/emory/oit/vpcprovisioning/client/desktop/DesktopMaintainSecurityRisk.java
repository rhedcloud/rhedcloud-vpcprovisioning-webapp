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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
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
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceControlPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainSecurityRisk extends ViewImplBase implements MaintainSecurityRiskView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> riskLevelItems;
	boolean editing;
	private boolean firstServiceControlWidget = true;
	private final DirectoryPersonRpcSuggestOracle assessorSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);
	private final DirectoryPersonRpcSuggestOracle personSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);

	private static DesktopMaintainSecurityRiskUiBinder uiBinder = GWT.create(DesktopMaintainSecurityRiskUiBinder.class);

	interface DesktopMaintainSecurityRiskUiBinder extends UiBinder<Widget, DesktopMaintainSecurityRisk> {
	}

	public DesktopMaintainSecurityRisk() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField ListBox riskLevelLB;
	@UiField TextBox serviceNameTB;
	@UiField TextBox sequenceNumberTB;
	@UiField TextBox riskNameTB;
	@UiField TextArea riskDescriptionTA;
	@UiField(provided=true) SuggestBox assessorLookupSB = new SuggestBox(assessorSuggestions, new TextBox());
	@UiField DateBox assessmentDB;
	
	@UiField VerticalPanel serviceControlPanel;
	@UiField Button calculateRiskButton;

	@UiHandler ("calculateRiskButton")
	void calculateRiskButtonClicked(ClickEvent e) {
		// TODO: Phase2:Sprint4
		if (presenter.getSecurityRisk() == null) {
			ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_SECURITY_RISK_CALCULATION, presenter.getService(), presenter.getSecurityAssessment(), this);
		}
		else {
			ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_RISK_CALCULATION, presenter.getService(), presenter.getSecurityAssessment(), presenter.getSecurityRisk(), this);
		}
	}
	
	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		if (userLoggedIn.isCentralAdmin()) {
			populateRiskWithFormData();
			presenter.saveAssessment(true);
		}
		else {
			ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), presenter.getSecurityAssessment());
		}
	}
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), presenter.getSecurityAssessment());
	}
	
	@Override
	public void populateRiskWithFormData() {
		// populate/save service
		presenter.getSecurityRisk().setServiceId(presenter.getService().getServiceId());
		presenter.getSecurityRisk().setRiskLevel(riskLevelLB.getSelectedValue());
		presenter.getSecurityRisk().setSecurityRiskName(riskNameTB.getText());
		presenter.getSecurityRisk().setDescription(riskDescriptionTA.getText());
		if (presenter.getDirectoryPerson() != null) {
			presenter.getSecurityRisk().setAssessorId(presenter.getDirectoryPerson().getKey());
		}
		presenter.getSecurityRisk().setSequenceNumber(Integer.parseInt(sequenceNumberTB.getText()));
		presenter.getSecurityRisk().setAssessmentDate(assessmentDB.getValue());
		
		// countermeasures are added as they're filled out.
	}

	private void registerHandlers() {
		assessorLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
					assessorLookupSB.setTitle(presenter.getDirectoryPerson().toString());
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
	        	riskNameTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void applyCentralAdminMask() {
//		createButton.setEnabled(true);
		riskLevelLB.setEnabled(true);
		serviceNameTB.setEnabled(true);
		sequenceNumberTB.setEnabled(true);
		riskNameTB.setEnabled(true);
		riskDescriptionTA.setEnabled(true);
		assessorLookupSB.setEnabled(true);
		assessmentDB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
//		createButton.setEnabled(false);
		riskLevelLB.setEnabled(false);
		serviceNameTB.setEnabled(false);
		sequenceNumberTB.setEnabled(false);
		riskNameTB.setEnabled(false);
		riskDescriptionTA.setEnabled(false);
		assessorLookupSB.setEnabled(false);
		assessmentDB.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
//		createButton.setEnabled(false);
		riskLevelLB.setEnabled(false);
		serviceNameTB.setEnabled(false);
		sequenceNumberTB.setEnabled(false);
		riskNameTB.setEnabled(false);
		riskDescriptionTA.setEnabled(false);
		assessorLookupSB.setEnabled(false);
		assessmentDB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		
		/*
		ServiceId, 
		SequenceNumber, 
		 */
		List<Widget> fields = new java.util.ArrayList<Widget>();
		SecurityRiskPojo risk = presenter.getSecurityRisk();
		if (risk.getSecurityRiskName() == null || risk.getSecurityRiskName().length() == 0) {
			fields.add(riskNameTB);
		}
		if (risk.getRiskLevel() == null || risk.getRiskLevel().length() == 0) {
			fields.add(riskLevelLB);
		}
		if (risk.getDescription() == null || risk.getDescription().length() == 0) {
			fields.add(riskDescriptionTA);
		}
		if (risk.getAssessorId() == null|| risk.getAssessorId().length() == 0) {
			fields.add(assessorLookupSB);
		}
		if (risk.getAssessmentDate() == null) {
			fields.add(assessmentDB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(riskNameTB);
		fields.add(riskLevelLB);
		fields.add(riskDescriptionTA);
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
		GWT.log("DesktopMaintainSecurityRisk: initPage");
		registerHandlers();

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	    		GWT.log("presenter's service is: " + presenter.getService());
	    		GWT.log("presenter's assessment is: " + presenter.getSecurityAssessment());
	    		GWT.log("presenter's risk is: " + presenter.getSecurityRisk());
	    		final ListServiceControlPresenter sc_presenter = new ListServiceControlPresenter(presenter.getClientFactory(), true, presenter.getService(), presenter.getSecurityAssessment(), presenter.getSecurityRisk());
	    		sc_presenter.setParentPresenter(presenter);
	    		sc_presenter.start(presenter.getEventBus());
	    		GWT.log("refreshing service control panel with service control list presenter...");
	    		GWT.log("serviceControlPanel widget count (1): " + serviceControlPanel.getWidgetCount());
	    		serviceControlPanel.clear();
	    		GWT.log("serviceControlPanel widget count (2): " + serviceControlPanel.getWidgetCount());
	    		serviceControlPanel.add(sc_presenter.asWidget());
	    		GWT.log("serviceControlPanel widget count (3): " + serviceControlPanel.getWidgetCount());
	        }
	    });

		if (editing) {
			SecurityRiskPojo srp = presenter.getSecurityRisk();
			sequenceNumberTB.setText(Integer.toString(srp.getSequenceNumber()));
			riskNameTB.setText(srp.getSecurityRiskName());
			riskDescriptionTA.setText(srp.getDescription());
			assessorLookupSB.setText(srp.getAssessorId());
			assessmentDB.setValue(srp.getAssessmentDate());
		}
		else {
			SecurityRiskPojo srp = presenter.getSecurityRisk();
			sequenceNumberTB.setText(Integer.toString(srp.getSequenceNumber()));
			assessorLookupSB.setText("");
			assessorLookupSB.getElement().setPropertyString("placeholder", "enter name");
		}
		serviceNameTB.setText(presenter.getService().getAwsServiceName());
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

	@Override
	public void setRiskLevelItems(List<String> riskLevels) {
		this.riskLevelItems = riskLevels;
		riskLevelLB.clear();
		riskLevelLB.addItem("-- Select --", "");
		if (riskLevelItems != null) {
			int i=1;
			for (String type : riskLevelItems) {
				riskLevelLB.addItem(type, type);
				if (presenter.getSecurityRisk() != null) {
					if (presenter.getSecurityRisk().getRiskLevel() != null) {
						if (presenter.getSecurityRisk().getRiskLevel().equals(type)) {
							riskLevelLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
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
	public void setWidget(IsWidget w) {
		if (w instanceof ListServiceControlPresenter) {
			GWT.log("Maintain Security Risk, setWidget: Service Controls");
			GWT.log("Widget passed in is: " + w);
			serviceControlPanel.clear();
			serviceControlPanel.add(new HTML("<b>This is just a test</b>"));
			serviceControlPanel.add(w.asWidget());
			GWT.log("serviceControlPanel widget count: " + serviceControlPanel.getWidgetCount());
			return;
		}
	}

	@Override
	public void setRiskLevel(String riskLevel) {
		int itemIndex=0;
		rlLoop: for (int i=0; i<this.riskLevelItems.size(); i++) {
			String rl = riskLevelItems.get(i);
			if (rl.equalsIgnoreCase(riskLevel)) {
				itemIndex = i;
				break rlLoop;
			}
		}
		itemIndex++;	// have to do this because the first row is "-- Select --"
		// this may not do what i want it to...
		GWT.log("maintain security risk] selecting risk level lb index " + itemIndex);
		GWT.log("value we should be selected from list box: " + riskLevelLB.getItemText(itemIndex));
		riskLevelLB.setSelectedIndex(itemIndex);
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	riskDescriptionTA.setFocus(true);
	        }
	    });
	}
}
