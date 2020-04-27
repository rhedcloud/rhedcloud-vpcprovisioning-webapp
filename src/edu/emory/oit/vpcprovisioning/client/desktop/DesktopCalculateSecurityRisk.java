package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.CalculateSecurityRiskView;
import edu.emory.oit.vpcprovisioning.shared.RiskCalculationAnswerPojo;
import edu.emory.oit.vpcprovisioning.shared.RiskCalculationPropertiesPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopCalculateSecurityRisk extends ViewImplBase implements CalculateSecurityRiskView {
	Presenter presenter;
	boolean locked;
	UserAccountPojo userLoggedIn;
    List<DockPanel> wizardPanels = new java.util.ArrayList<DockPanel>();
	int panelNumber=0;
    int numberOfDockPanels = 0;
    int panelCounter = 0;
    boolean firstPrevClick=true;
    boolean firstNextClick=true;
    boolean editing;
    List<RiskCalculationPropertiesPojo> rcps = new java.util.ArrayList<RiskCalculationPropertiesPojo>();

    @UiField DeckLayoutPanel wizardContainer;
    @UiField Button prevButton;
    @UiField Button nextButton;
    @UiField Button okayButton;
    @UiField Button cancelButton;

	private static DesktopCalculateSecurityRiskUiBinder uiBinder = GWT
			.create(DesktopCalculateSecurityRiskUiBinder.class);

	interface DesktopCalculateSecurityRiskUiBinder extends UiBinder<Widget, DesktopCalculateSecurityRisk> {
	}

	public DesktopCalculateSecurityRisk() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void createWizardTablePanel(RiskCalculationPropertiesPojo rcp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createWizardQuestionPanel(final RiskCalculationPropertiesPojo rcp) {
		DockPanel dp = createDock(rcp.getQuestionText());

		// add answers table
		if (rcp.getAnswers().size() > 0) {
			VerticalPanel answersPanel = new VerticalPanel();
			answersPanel.setSpacing(8);
			dp.add(answersPanel, DockPanel.NORTH);
			HTML dpContent = new HTML("Pick the best answer for: " + rcp.getStepName() + ", Question: " + rcp.getQuestionNumber());
			answersPanel.add(dpContent);
			answersPanel.setCellHorizontalAlignment(dpContent, HasHorizontalAlignment.ALIGN_LEFT);
			
			for (final RiskCalculationAnswerPojo rca : rcp.getAnswers()) {
				RadioButton answerRB = new RadioButton("answers", rca.getAnswerText());
				// trying to increase the margin so the text will be further away from the
				// radio button itself but it doesn't seem to be working.
//				answerRB.getElement().getStyle().setMarginRight(20, Unit.PX);
//				answerRB.getElement().getStyle().setMarginLeft(20, Unit.PX);
				answerRB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						// set the risk level on the RiskLevelCalculationPojo we're maintaining throughout this wizard
						presenter.getRiskLevelCalculation().updateLevelForQuestion(rcp.getQuestionNumber(), rca.getRiskLevel());
						if (rcp.getStepNumber() == 4) {
							presenter.getRiskLevelCalculation().setLevelOfImpact(rca.getRiskLevel());
						}
					}
				});
				answersPanel.add(answerRB);
				answersPanel.setCellHorizontalAlignment(answerRB, HasHorizontalAlignment.ALIGN_LEFT);
			}
		}
		else {
			VerticalPanel resultsPanel = new VerticalPanel();
			resultsPanel.setSpacing(8);
			dp.add(resultsPanel, DockPanel.NORTH);
			HTML dpContent = new HTML(rcp.getTableHeading());
			resultsPanel.add(dpContent);
		}
	
		wizardPanels.add(dp);
		wizardContainer.add(dp);
	}
	
	@Override
	public void setInitialWizardPanel() {
		DockPanel dock = wizardPanels.get(0);
		wizardContainer.setWidget(dock);
		wizardContainer.animate(0);
		panelCounter = 0;
		numberOfDockPanels = wizardPanels.size();
		prevButton.setEnabled(false);
	}

	private DockPanel createDock(String title) {
		DockPanel dock = new DockPanel();
		dock.setSpacing(4);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dock.setVerticalAlignment(DockPanel.ALIGN_TOP);
		dock.setHeight("100%");
		dock.setWidth("100%");

		if (title != null) {
			HTML text = new HTML(title);
			text.getElement().getStyle().setFontSize(14, Unit.PX);
			text.getElement().getStyle().setColor("#002878");
			text.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			text.getElement().getStyle().setTextAlign(TextAlign.LEFT);
			dock.add(text, DockPanel.NORTH);
		}
		else {
			HTML text = new HTML();
			text.getElement().getStyle().setFontSize(14, Unit.PX);
			text.getElement().getStyle().setColor("#002878");
			text.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			text.getElement().getStyle().setTextAlign(TextAlign.LEFT);
			dock.add(text, DockPanel.NORTH);
		}
		
		return dock;
	}

	@UiHandler ("prevButton")
	void prevButtonClicked(ClickEvent e) {
		GWT.log("[prev:1] panelCounter is: " + panelCounter);
		nextButton.setEnabled(true);
		if (panelCounter <= 1) {
			// we're going to the first panel.  can't click prev.
			prevButton.setEnabled(false);
			panelCounter = 0;
		}
		else {
			prevButton.setEnabled(true);
			if (!firstPrevClick) {
				panelCounter--;
			}
		}
		firstPrevClick = false;
		GWT.log("[prev:3] panelCounter is: " + panelCounter);
		RiskCalculationPropertiesPojo rcp = this.rcps.get(panelCounter);
		GWT.log("Previous RCP stepNumber is: " + rcp.getStepNumber());
		DockPanel dock = wizardPanels.get(panelCounter);
		wizardContainer.setWidget(dock);
		wizardContainer.animate(700);
	}

	@UiHandler ("nextButton")
	void nextButtonClicked(ClickEvent e) {
		GWT.log("[next:1] panelCounter is: " + panelCounter);
		prevButton.setEnabled(true);
		RiskCalculationPropertiesPojo currentRcp = this.rcps.get(panelCounter);
		if (currentRcp.getQuestionNumber() != 0) {
			int selectedValue = presenter.getRiskLevelCalculation().getRiskLevelForQuestion(currentRcp.getQuestionNumber());
			if (selectedValue == 0) {
				showMessageToUser("Please select the best answer for this question.");
				return;
			}
		}
		
		if (panelCounter >= numberOfDockPanels - 2) {
			// we're going to the last panel.  can't click next.
			nextButton.setEnabled(false);
			panelCounter = numberOfDockPanels - 1;
		}
		else {
			nextButton.setEnabled(true);
			panelCounter++;
		}
		firstPrevClick = false;
		GWT.log("[next:3] panelCounter is: " + panelCounter);
		RiskCalculationPropertiesPojo nextRcp = this.rcps.get(panelCounter);
		GWT.log("Next RCP stepNumber is: " + nextRcp.getStepNumber());
		DockPanel dock;
		if (nextRcp.getStepNumber() == 3) {
			dock = wizardPanels.get(panelCounter);
			wizardContainer.remove(dock);
			wizardPanels.remove(dock);
			
			dock = createDock(null);
			VerticalPanel resultsPanel = new VerticalPanel();
			resultsPanel.setSpacing(8);
			dock.add(resultsPanel, DockPanel.CENTER);
			
			int step1answer1 = presenter.getRiskLevelCalculation().getRiskLevelForQuestion(1);
			int step1answer2 = presenter.getRiskLevelCalculation().getRiskLevelForQuestion(2);
			int step1RiskLevel = 0;
			if (step1answer1 > step1answer2) {
				step1RiskLevel = step1answer1;
				resultsPanel.add(new HTML("Step 1 Risk Level (from question 1): " + step1RiskLevel));
			}
			else {
				step1RiskLevel = step1answer2;
				resultsPanel.add(new HTML("Step 1 Risk Level (from question 2): " + step1RiskLevel));
			}
			int step2RiskLevel = presenter.getRiskLevelCalculation().getRiskLevelForQuestion(3);
			resultsPanel.add(new HTML("Step 2 Risk Level (from question 3): " + step2RiskLevel));
			
			// overall likelihood
			if (step1RiskLevel > step2RiskLevel) {
				presenter.getRiskLevelCalculation().setOverallLikelihood(step1RiskLevel);
			}
			else {
				presenter.getRiskLevelCalculation().setOverallLikelihood(step2RiskLevel);
			}

			VerticalPanel tablePanel = new VerticalPanel();
			resultsPanel.add(tablePanel);
			
			HTML tableHeading = new HTML(nextRcp.getTableHeading());
			tableHeading.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			tableHeading.getElement().getStyle().setBackgroundColor("lightgrey");
			tablePanel.add(tableHeading);
			
			Grid table = new Grid(6,6);
			table.setWidth("100%");
			tablePanel.add(table);
			table.setCellSpacing(0);
			table.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getElement().getStyle().setBorderWidth(1, Unit.PX);
			
			// headings
			table.setWidget(1, 0, new HTML("<b>Critical</b>"));
			table.getCellFormatter().getElement(1, 0).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(1, 0).getStyle().setBorderWidth(1, Unit.PX);
			
			table.setWidget(2, 0, new HTML("<b>High</b>"));
			table.getCellFormatter().getElement(2, 0).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(2, 0).getStyle().setBorderWidth(1, Unit.PX);

			table.setWidget(3, 0, new HTML("<b>Medium</b>"));
			table.getCellFormatter().getElement(3, 0).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(3, 0).getStyle().setBorderWidth(1, Unit.PX);

			table.setWidget(4, 0, new HTML("<b>Low</b>"));
			table.getCellFormatter().getElement(4, 0).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(4, 0).getStyle().setBorderWidth(1, Unit.PX);

			table.setWidget(5, 0, new HTML("<b>Very Low</b>"));
			table.getCellFormatter().getElement(5, 0).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(5, 0).getStyle().setBorderWidth(1, Unit.PX);

			table.setWidget(0, 5, new HTML("<b>Critical</b>"));
			table.getCellFormatter().getElement(0, 5).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(0, 5).getStyle().setBorderWidth(1, Unit.PX);

			table.setWidget(0, 4, new HTML("<b>High</b>"));
			table.getCellFormatter().getElement(0, 4).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(0, 4).getStyle().setBorderWidth(1, Unit.PX);
			
			table.setWidget(0, 3, new HTML("<b>Medium</b>"));
			table.getCellFormatter().getElement(0, 3).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(0, 3).getStyle().setBorderWidth(1, Unit.PX);

			table.setWidget(0, 2, new HTML("<b>Low</b>"));
			table.getCellFormatter().getElement(0, 2).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(0, 2).getStyle().setBorderWidth(1, Unit.PX);

			table.setWidget(0, 1, new HTML("<b>Very Low</b>"));
			table.getCellFormatter().getElement(0, 1).getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getCellFormatter().getElement(0, 1).getStyle().setBorderWidth(1, Unit.PX);

			wizardPanels.add(panelCounter, dock);
			wizardContainer.insert(dock, panelCounter);
		}
		else if (nextRcp.getStepNumber() == 5) {
			dock = wizardPanels.get(panelCounter);
			wizardContainer.remove(dock);
			
			dock = createDock(nextRcp.getQuestionText());
			VerticalPanel resultsPanel = new VerticalPanel();
			resultsPanel.setSpacing(8);
			dock.add(resultsPanel, DockPanel.NORTH);
			HTML dpContent = new HTML(nextRcp.getTableHeading());
			resultsPanel.add(dpContent);
			resultsPanel.add(new HTML("I-2 Table"));
			
			wizardPanels.add(panelCounter, dock);
			wizardContainer.insert(dock, panelCounter);
		}
		else {
			dock = wizardPanels.get(panelCounter);
		}
		wizardContainer.setWidget(dock);
		wizardContainer.animate(700);
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub
		
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
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setRiskCalculationProperties(List<RiskCalculationPropertiesPojo> rcps) {
		this.rcps = rcps;
	}
}
