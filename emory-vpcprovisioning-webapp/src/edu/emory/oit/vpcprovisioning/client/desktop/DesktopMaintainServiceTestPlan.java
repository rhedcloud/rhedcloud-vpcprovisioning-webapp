package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanView;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestRequirementPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainServiceTestPlan extends ViewImplBase implements MaintainServiceTestPlanView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	boolean editing;
	PopupPanel actionsPopup = new PopupPanel(true);
	ServiceTestPlanTreeViewModel model;
	List<String> testExpectedResultItems = new java.util.ArrayList<String>();
	static final String SAVE = "Save";
	static final String ADD = "Add";

	ListDataProvider<ServiceTestRequirementPojo> requirementDataProvider = 
			new ListDataProvider<ServiceTestRequirementPojo>(new ArrayList<ServiceTestRequirementPojo>());
	
	SingleSelectionModel<ServiceTestRequirementPojo> reqSelectionModel = 
			new SingleSelectionModel<ServiceTestRequirementPojo>(ServiceTestRequirementPojo.KEY_PROVIDER);
	SingleSelectionModel<ServiceTestPojo> testSelectionModel = 
			new SingleSelectionModel<ServiceTestPojo>(ServiceTestPojo.KEY_PROVIDER);
	SingleSelectionModel<ServiceTestStepPojo> stepSelectionModel = 
			new SingleSelectionModel<ServiceTestStepPojo>(ServiceTestStepPojo.KEY_PROVIDER);

//	@UiField Button okayButton;
//	@UiField Button cancelButton;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField (provided=true) CellBrowser cellBrowser;
	@UiField VerticalPanel requirementPanel;
	@UiField VerticalPanel testPanel;
	@UiField VerticalPanel testStepPanel;
	@UiField TextBox reqSequenceNumberTB;
	@UiField TextArea reqDescriptionTA;
	@UiField TextBox testSequenceNumberTB;
	@UiField TextArea testDescriptionTA;
	@UiField ListBox testExpectedResultLB;
	@UiField TextBox stepSequenceNumberTB;
	@UiField TextArea stepDescriptionTA;
	
	@UiField Button addReqButton;
	@UiField Button removeReqButton;
	@UiField Button saveReqButton;
	
	@UiField Button addTestButton;
	@UiField Button removeTestButton;
	@UiField Button saveTestButton;

	@UiField Button addStepButton;
	@UiField Button removeStepButton;
	@UiField Button saveStepButton;
	
	@UiField Label testRequirementForTestLabel;
	@UiField Label testRequirementForStepLabel;
	@UiField Label testForStepLabel;

	private static DesktopMaintainServiceTestPlanUiBinder uiBinder = GWT
			.create(DesktopMaintainServiceTestPlanUiBinder.class);

	interface DesktopMaintainServiceTestPlanUiBinder extends UiBinder<Widget, DesktopMaintainServiceTestPlan> {
	}

	public DesktopMaintainServiceTestPlan() {
		initializeCellBrowser();
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
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	reqDescriptionTA.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return addReqButton;
	}

	@Override
	public void applyCentralAdminMask() {
		addReqButton.setEnabled(true);
		removeReqButton.setEnabled(true);
		saveReqButton.setEnabled(true);
		addTestButton.setEnabled(true);
		removeTestButton.setEnabled(true);
		saveTestButton.setEnabled(true);
		addStepButton.setEnabled(true);
		removeStepButton.setEnabled(true);
		saveStepButton.setEnabled(true);
		
		reqSequenceNumberTB.setEnabled(true);
		reqDescriptionTA.setEnabled(true);
		testSequenceNumberTB.setEnabled(true);
		testDescriptionTA.setEnabled(true);
		testExpectedResultLB.setEnabled(true);
		stepSequenceNumberTB.setEnabled(true);
		stepDescriptionTA.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		addReqButton.setEnabled(false);
		removeReqButton.setEnabled(false);
		saveReqButton.setEnabled(false);
		addTestButton.setEnabled(false);
		removeTestButton.setEnabled(false);
		saveTestButton.setEnabled(false);
		addStepButton.setEnabled(false);
		removeStepButton.setEnabled(false);
		saveStepButton.setEnabled(false);
		
		reqSequenceNumberTB.setEnabled(false);
		reqDescriptionTA.setEnabled(false);
		testSequenceNumberTB.setEnabled(false);
		testDescriptionTA.setEnabled(false);
		testExpectedResultLB.setEnabled(false);
		stepSequenceNumberTB.setEnabled(false);
		stepDescriptionTA.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		addReqButton.setEnabled(false);
		removeReqButton.setEnabled(false);
		saveReqButton.setEnabled(false);
		addTestButton.setEnabled(false);
		removeTestButton.setEnabled(false);
		saveTestButton.setEnabled(false);
		addStepButton.setEnabled(false);
		removeStepButton.setEnabled(false);
		saveStepButton.setEnabled(false);
		
		reqSequenceNumberTB.setEnabled(false);
		reqDescriptionTA.setEnabled(false);
		testSequenceNumberTB.setEnabled(false);
		testDescriptionTA.setEnabled(false);
		testExpectedResultLB.setEnabled(false);
		stepSequenceNumberTB.setEnabled(false);
		stepDescriptionTA.setEnabled(false);
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
		addReqButton.setEnabled(false);
		removeReqButton.setEnabled(false);
		saveReqButton.setEnabled(false);
		addTestButton.setEnabled(false);
		removeTestButton.setEnabled(false);
		saveTestButton.setEnabled(false);
		addStepButton.setEnabled(false);
		removeStepButton.setEnabled(false);
		saveStepButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		addReqButton.setEnabled(true);
		removeReqButton.setEnabled(true);
		saveReqButton.setEnabled(true);
		addTestButton.setEnabled(true);
		removeTestButton.setEnabled(true);
		saveTestButton.setEnabled(true);
		addStepButton.setEnabled(true);
		removeStepButton.setEnabled(true);
		saveStepButton.setEnabled(true);
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
		GWT.log("MaintainServiceTestPlan.initPage()");
		
		model.setPresenter((MaintainServiceTestPlanPresenter)presenter);
		removeReqButton.setVisible(false);
		addTestButton.setVisible(false);
		removeTestButton.setVisible(false);
		addStepButton.setVisible(false);
		
		removeStepButton.setVisible(false);
		requirementPanel.setVisible(false);
		testPanel.setVisible(false);
		testStepPanel.setVisible(false);
		
		if (model != null) {
			refreshDataProvider();
		}
		else {
			GWT.log("[MaintainServiceTestPlan] model is null.  Can't initialize it");
		}
	}

	private void hidePanels() {
		testPanel.setVisible(false);
		testStepPanel.setVisible(false);
		requirementPanel.setVisible(false);
	}
	
	private void hideButtons() {
		addTestButton.setVisible(false);
		removeTestButton.setVisible(false);
		addStepButton.setVisible(false);
		removeStepButton.setVisible(false);
		removeReqButton.setVisible(false);
	}
	
	@UiHandler ("saveReqButton")
	void saveReqButtonClicked(ClickEvent e) {
		// TODO: add vs. update
		if (saveReqButton.getText().equals(ADD)) {
			GWT.log("Add new requirement.");
		}
		else {
			GWT.log("Save selected requirement.");
		}
		ServiceTestPlanPojo stp = presenter.getServiceTestPlan();
		ServiceTestRequirementPojo str = new ServiceTestRequirementPojo();
		str.setSequenceNumber(Integer.parseInt(reqSequenceNumberTB.getText()));
		str.setDescription(reqDescriptionTA.getText());
		if (stp.hasRequirement(str.getSequenceNumber())) {
			showMessageToUser("A requirement with this sequence number already "
					+ "exists.  Please use a different sequence number.");
			return;
		}
		stp.getServiceTestRequirements().add(str);

		removeReqButton.setVisible(false);
		addTestButton.setVisible(false);
		removeTestButton.setVisible(false);
		addStepButton.setVisible(false);
		removeStepButton.setVisible(false);
		
		presenter.getSecurityAssessment().setServiceTestPlan(stp);
		presenter.saveAssessment();
		saveReqButton.setText(SAVE);
	}
	@UiHandler ("removeReqButton")
	void removeReqButtonClicked(ClickEvent e) {
		ServiceTestPlanPojo stp = presenter.getServiceTestPlan();
		stp.removeServiceRequirement(presenter.getSelectedTestRequirement());
		presenter.setSelectedTestRequirement(null);
		presenter.saveAssessment();
	}

	@UiHandler ("saveTestButton")
	void saveTestButtonClicked(ClickEvent e) {
		// TODO: add vs. update
		if (saveTestButton.getText().equals(ADD)) {
			GWT.log("Add new test.");
		}
		else {
			GWT.log("Save selected test.");
		}
		ServiceTestPojo st = new ServiceTestPojo();
		st.setSequenceNumber(Integer.parseInt(testSequenceNumberTB.getText()));
		st.setDescription(testDescriptionTA.getText());
		st.setServiceTestExpectedResult(testExpectedResultLB.getSelectedValue());

		if (presenter.getSelectedTestRequirement().hasTest(st.getSequenceNumber())) {
			showMessageToUser("A test for the selected requirement with this sequence number already "
					+ "exists.  Please use a different sequence number.");
			return;
		}
		presenter.getSelectedTestRequirement().getServiceTests().add(st);
		
		removeTestButton.setVisible(false);
		addStepButton.setVisible(false);
		removeStepButton.setVisible(false);
		
		// fire selection to show the new test
//		GWT.log("firing a selection change event on the requirements selection model (trying)");
//		GWT.log("presenters selected requirement (before clear) is: " + presenter.getSelectedTestRequirement());
//		GWT.log("selected requirement (before clear) is: " + reqSelectionModel.getSelectedObject());
//		reqSelectionModel.clear();
//		GWT.log("selected requirement (after clear) is: " + reqSelectionModel.getSelectedObject());
//		GWT.log("presenters selected requirement (after clear) is: " + presenter.getSelectedTestRequirement());
//		reqSelectionModel.setSelected(reqSelectionModel.getSelectedObject(), false);
//		GWT.log("selected requirement (after setting to false) is: " + reqSelectionModel.getSelectedObject());
//		GWT.log("presenters selected requirement (after setting it to false) is: " + presenter.getSelectedTestRequirement());
//		reqSelectionModel.setSelected(reqSelectionModel.getSelectedObject(), true);
//		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), reqSelectionModel);
		
		presenter.saveAssessment();
		saveTestButton.setText(SAVE);
//		model.getNodeInfo(st);
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), reqSelectionModel);
	}
	@UiHandler ("removeTestButton")
	void removeTestButtonClicked(ClickEvent e) {
		ServiceTestRequirementPojo str = presenter.getSelectedTestRequirement();
		str.removeServiceTest(presenter.getSelectedTest());
		presenter.setSelectedTest(null);
		presenter.saveAssessment();
	}

	@UiHandler ("saveStepButton")
	void saveStepButtonClicked(ClickEvent e) {
		// TODO: add vs. update
		if (saveReqButton.getText().equals(ADD)) {
			GWT.log("Add new step.");
		}
		else {
			GWT.log("Save selected step.");
		}
		ServiceTestStepPojo str = new ServiceTestStepPojo();
		str.setSequenceNumber(Integer.parseInt(stepSequenceNumberTB.getText()));
		str.setDescription(stepDescriptionTA.getText());
		str.setServiceTestId(presenter.getSelectedTest().getServiceTestId());
		
		if (presenter.getSelectedTest().hasStep(str.getSequenceNumber())) {
			showMessageToUser("A test step for the selected test with this sequence number already "
					+ "exists.  Please use a different sequence number.");
			return;
		}
		presenter.getSelectedTest().getServiceTestSteps().add(str);
		
		removeStepButton.setVisible(false);
		
		// fire selection to show the new test step
//		testSelectionModel.setSelected(presenter.getSelectedTest(), true);

		presenter.saveAssessment();
		saveStepButton.setText(SAVE);
	}

	@UiHandler ("removeStepButton")
	void removeStepButtonClicked(ClickEvent e) {
		ServiceTestPojo str = presenter.getSelectedTest();
		str.removeServiceTestStep(presenter.getSelectedTestStep());
		presenter.setSelectedTestStep(null);
		presenter.saveAssessment();
	}

	@UiHandler ("addReqButton")
	void addReqButtonClicked(ClickEvent e) {
		saveReqButton.setText(ADD);
		testPanel.setVisible(false);
		testStepPanel.setVisible(false);
		requirementPanel.setVisible(true);

		removeReqButton.setVisible(false);
		addTestButton.setVisible(false);
		removeTestButton.setVisible(false);
		addStepButton.setVisible(false);
		removeStepButton.setVisible(false);
		
		ServiceTestPlanPojo stp = presenter.getServiceTestPlan();
		reqSequenceNumberTB.setText(Integer.toString(stp.getServiceTestRequirements().size() + 1));
		reqDescriptionTA.setText("");
		reqDescriptionTA.getElement().setPropertyString("placeholder", "description");
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	reqDescriptionTA.setFocus(true);
	        }
	    });
	}

	@UiHandler ("addTestButton")
	void addTestButtonClicked(ClickEvent e) {
		saveTestButton.setText(ADD);
		testStepPanel.setVisible(false);
		requirementPanel.setVisible(false);
		testPanel.setVisible(true);
		
		addTestButton.setVisible(true);
		removeTestButton.setVisible(false);
		addStepButton.setVisible(false);
		removeStepButton.setVisible(false);
		
		if (presenter.getSelectedTestRequirement() == null) {
			this.showMessageToUser("[add test ] no selected test requirement.");
			return;
		}
		testRequirementForTestLabel.setText(Integer.toString(
				presenter.getSelectedTestRequirement().getSequenceNumber()));
		
		testSequenceNumberTB.setText(Integer.toString(
				presenter.getSelectedTestRequirement().getServiceTests().size() + 1));
		testDescriptionTA.setText("");
		testDescriptionTA.getElement().setPropertyString("placeholder", "description");
		testExpectedResultLB.setSelectedIndex(0);
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	testDescriptionTA.setFocus(true);
	        }
	    });
	}

	@UiHandler ("addStepButton")
	void addStepButtonClicked(ClickEvent e) {
		saveStepButton.setText(ADD);
		testPanel.setVisible(false);
		requirementPanel.setVisible(false);
		testStepPanel.setVisible(true);
		
		removeStepButton.setVisible(false);
		
		if (presenter.getSelectedTestRequirement() == null) {
			this.showMessageToUser("[add step] no selected test requirement.");
			return;
		}
		testRequirementForStepLabel.setText(Integer.toString(presenter.getSelectedTestRequirement().getSequenceNumber()));
		
		testForStepLabel.setText(Integer.toString(presenter.getSelectedTest().getSequenceNumber()));
		stepSequenceNumberTB.setText(Integer.toString(
				presenter.getSelectedTest().getServiceTestSteps().size() + 1));
		stepDescriptionTA.setText("");
		stepDescriptionTA.getElement().setPropertyString("placeholder", "description");
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	stepDescriptionTA.setFocus(true);
	        }
	    });
	}

	private void initializeCellBrowser() {
		reqSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				requirementSelected();
			}
		});

		testSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				testSelected();
			}
		});

		stepSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				stepSelected();
			}
		});

		model = new ServiceTestPlanTreeViewModel(
				requirementDataProvider,
				reqSelectionModel, 
				testSelectionModel, 
				stepSelectionModel);
		CellBrowser.Builder<Object> builder = new CellBrowser.Builder<Object>(model, null);
		cellBrowser = builder.build();
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
	}

	@Override
	public void setTestExpectedResultItems(List<String> items) {
		this.testExpectedResultItems = items;
		testExpectedResultLB.clear();
		testExpectedResultLB.addItem("-- Select --", "");
		if (testExpectedResultLB != null) {
			for (String type : testExpectedResultItems) {
				testExpectedResultLB.addItem(type, type);
			}
		}
	}

	@Override
	public void refreshDataProvider() {
		GWT.log("refreshing data provider...test " + 
			"requirement count is: " + 
			presenter.getServiceTestPlan().getServiceTestRequirements().size());
		requirementDataProvider.setList(
				presenter.getServiceTestPlan().getServiceTestRequirements());
		requirementDataProvider.refresh();
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requirementSelected() {
//		ServiceTestRequirementPojo selected = reqSelectionModel.getSelectedObject();
		ServiceTestRequirementPojo selectedFromModel = reqSelectionModel.getSelectedObject();
		GWT.log("REQ:selectedFromModel is: " + selectedFromModel);
		ServiceTestRequirementPojo selected = presenter.getSelectedTestRequirement();
		reqSelectionModel.setSelected(selected, true);
		GWT.log("[REQ:onSelectionChange] requirment selected: " + selected);
		presenter.setSelectedTestRequirement(selected);
		saveReqButton.setText(SAVE);

		if (selected == null) {
			hidePanels();
			hideButtons();
		}
		else {
			testPanel.setVisible(false);
			testStepPanel.setVisible(false);
			requirementPanel.setVisible(true);
			reqSequenceNumberTB.setText(Integer.toString(selected.getSequenceNumber()));
			reqDescriptionTA.setText(selected.getDescription());
		}

		addTestButton.setVisible(true);
		removeTestButton.setVisible(false);
		addStepButton.setVisible(false);
		removeStepButton.setVisible(false);
		addReqButton.setVisible(true);
		removeReqButton.setVisible(true);
	}

	@Override
	public void testSelected() {
//		ServiceTestPojo selected = testSelectionModel.getSelectedObject();
		ServiceTestPojo selectedFromModel = testSelectionModel.getSelectedObject();
		GWT.log("TEST:selectedFromModel is: " + selectedFromModel);
		ServiceTestPojo selected = presenter.getSelectedTest();
		testSelectionModel.setSelected(selected, true);
		GWT.log("[TEST:onSelectionChange] test selected: " + selected);
		presenter.setSelectedTest(selected);
		saveTestButton.setText(SAVE);

		if (selected == null) {
			hidePanels();
			hideButtons();
		}
		else {
			if (presenter.getSelectedTestRequirement() == null) {
				GWT.log("[TEST:onSelectionChange] no selected test requirement.");
				hidePanels();
				hideButtons();
				return;
			}
			testStepPanel.setVisible(false);
			requirementPanel.setVisible(false);
			testPanel.setVisible(true);
			testSequenceNumberTB.setText(Integer.toString(selected.getSequenceNumber()));
			testDescriptionTA.setText(selected.getDescription());
			testRequirementForTestLabel.setText(Integer.toString(presenter.getSelectedTestRequirement().getSequenceNumber()));
			testExpectedResultLB.setSelectedIndex(0);
			int i=1;
			erLoop: for (String type : testExpectedResultItems) {
				if (selected.getServiceTestExpectedResult() != null) {
					if (selected.getServiceTestExpectedResult().equalsIgnoreCase(type)) {
						testExpectedResultLB.setSelectedIndex(i);
						break erLoop;
					}
				}
				i++;
			}
			addTestButton.setVisible(true);
			removeTestButton.setVisible(true);
			addStepButton.setVisible(true);
			removeStepButton.setVisible(false);
		}
	}

	@Override
	public void stepSelected() {
//		ServiceTestStepPojo selected = stepSelectionModel.getSelectedObject();
		ServiceTestStepPojo selectedFromModel = stepSelectionModel.getSelectedObject();
		GWT.log("STEP:selectedFromModel is: " + selectedFromModel);
		ServiceTestStepPojo selected = presenter.getSelectedTestStep();
		stepSelectionModel.setSelected(selected, true);
		GWT.log("[STEP: onSelectionChange] step selected: " + selected);
		presenter.setSelectedTestStep(selected);
		saveStepButton.setText(SAVE);

		if (selected == null) {
			hidePanels();
			hideButtons();
		}
		else {
			if (presenter.getSelectedTestRequirement() == null) {
				GWT.log("[STEP: onSelectionChange]  no selected test requirement.");
				hidePanels();
				hideButtons();
				return;
			}
			if (presenter.getSelectedTest() == null) {
				GWT.log("[STEP: onSelectionChange]  no selected test.");
				addStepButton.setVisible(false);
				removeStepButton.setVisible(false);
				requirementPanel.setVisible(true);
				testPanel.setVisible(false);
				testStepPanel.setVisible(false);
			}
			else {
				requirementPanel.setVisible(false);
				testPanel.setVisible(false);
				testStepPanel.setVisible(true);
				stepSequenceNumberTB.setText(Integer.toString(selected.getSequenceNumber()));
				stepDescriptionTA.setText(selected.getDescription());
				testRequirementForStepLabel.setText(Integer.toString(presenter.getSelectedTestRequirement().getSequenceNumber()));
				testForStepLabel.setText(Integer.toString(presenter.getSelectedTest().getSequenceNumber()));
				addStepButton.setVisible(true);
				removeStepButton.setVisible(true);
			}
		}

	}
}
