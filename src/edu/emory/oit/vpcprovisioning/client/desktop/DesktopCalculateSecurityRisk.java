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
import com.google.gwt.user.client.ui.HorizontalPanel;
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
    String overallRiskLevelText = null;

	private static DesktopCalculateSecurityRiskUiBinder uiBinder = GWT
			.create(DesktopCalculateSecurityRiskUiBinder.class);

	interface DesktopCalculateSecurityRiskUiBinder extends UiBinder<Widget, DesktopCalculateSecurityRisk> {
	}

	public DesktopCalculateSecurityRisk() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		GWT.log("[calculate risk dialog] overall risk is: " + overallRiskLevelText);
		presenter.getMaintainSecurityRiskView().setRiskLevel(overallRiskLevelText);
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
			// OVERALL LIKELIHOOD TABLE
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
				resultsPanel.add(new HTML("Step 1 Risk Level (from question 1): " + this.getRiskLevelForInt(step1RiskLevel)));
			}
			else {
				step1RiskLevel = step1answer2;
				resultsPanel.add(new HTML("Step 1 Risk Level (from question 2): " + this.getRiskLevelForInt(step1RiskLevel)));
			}
			int step2RiskLevel = presenter.getRiskLevelCalculation().getRiskLevelForQuestion(3);
			resultsPanel.add(new HTML("Step 2 Risk Level (from question 3): " + this.getRiskLevelForInt(step2RiskLevel)));
			
			VerticalPanel tablePanel = new VerticalPanel();
			tablePanel.setSpacing(4);
			resultsPanel.add(tablePanel);
			
			HTML tableHeading = new HTML(nextRcp.getTableHeading());
			tableHeading.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			tableHeading.getElement().getStyle().setBackgroundColor("lightgrey");
			tablePanel.add(tableHeading);
			
			Grid table = new Grid(6,6);
			table.setWidth("100%");
			tablePanel.add(table);
			table.setCellSpacing(0);
			table.setCellPadding(6);
			table.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			table.getElement().getStyle().setBorderWidth(1, Unit.PX);
			
			// very low = 1
			// low      = 2
			// medium   = 3
			// high     = 4
			// critical = 5
			
			// Row 1, columns 0 - 5
			this.addCellToTable(table, 1, 0, "<b>Critical</b>", "#e5e8e8");
			this.addCellToTable(table, 1, 1, "Low", null);
			this.addCellToTable(table, 1, 2, "Medium", null);
			this.addCellToTable(table, 1, 3, "High", null);
			this.addCellToTable(table, 1, 4, "Critical", null);
			this.addCellToTable(table, 1, 5, "Critical", null);

			// Row 2, columns 0 - 5
			this.addCellToTable(table, 2, 0, "<b>High</b>", "#e5e8e8");
			this.addCellToTable(table, 2, 1, "Low", null);
			this.addCellToTable(table, 2, 2, "Medium", null);
			this.addCellToTable(table, 2, 3, "Medium", null);
			this.addCellToTable(table, 2, 4, "High", null);
			this.addCellToTable(table, 2, 5, "Critical", null);

			// Row 3, columns 0 - 5
			this.addCellToTable(table, 3, 0, "<b>Medium</b>", "#e5e8e8");
			this.addCellToTable(table, 3, 1, "Low", null);
			this.addCellToTable(table, 3, 2, "Low", null);
			this.addCellToTable(table, 3, 3, "Medium", null);
			this.addCellToTable(table, 3, 4, "Medium", null);
			this.addCellToTable(table, 3, 5, "High", null);

			// Row 4, columns 0-5
			this.addCellToTable(table, 4, 0, "<b>Low</b>", "#e5e8e8");
			this.addCellToTable(table, 4, 1, "Very Low", null);
			this.addCellToTable(table, 4, 2, "Low", null);
			this.addCellToTable(table, 4, 3, "Low", null);
			this.addCellToTable(table, 4, 4, "Medium", null);
			this.addCellToTable(table, 4, 5, "Medium", null);

			// Row 5 columns 0-5
			this.addCellToTable(table, 5, 0, "<b>Very Low</b>", "#e5e8e8");
			this.addCellToTable(table, 5, 1, "Very Low", null);
			this.addCellToTable(table, 5, 2, "Very Low", null);
			this.addCellToTable(table, 5, 3, "Low", null);
			this.addCellToTable(table, 5, 4, "Low", null);
			this.addCellToTable(table, 5, 5, "Low", null);

			// row headings
			this.addCellToTable(table, 0, 5, "<b>Critical</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 4, "<b>High</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 3, "<b>Medium</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 2, "<b>Low</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 1, "<b>Very Low</b>", "#e5e8e8");

			// highlight the appropriate cell
			int row = 0;
			switch (step1RiskLevel) {
			case 1:
				row = 5;
				break;
			case 2:
				row = 4;
				break;
			case 3:
				row = 3;
				break;
			case 4:
				row = 2;
				break;
			case 5:
				row = 1;
				break;
			}
			int column = step2RiskLevel;
			GWT.log("row: " + row + " column: " + column);
			table.getCellFormatter().getElement(row, column).getStyle().setBackgroundColor("#f1948a");

			// overall likelihood
			HTML olhHTML = (HTML)table.getWidget(row, column);
			String olhText = olhHTML.getText();
			GWT.log("Overall likelihood text is: " + olhText);
			GWT.log("Overall likelihood as int : " + this.getOverallLikelihoodAsInt(olhText));
			// using olhText, derive a numeric value from that
			presenter.getRiskLevelCalculation().setOverallLikelihood(this.getOverallLikelihoodAsInt(olhText));

			wizardPanels.add(panelCounter, dock);
			wizardContainer.insert(dock, panelCounter);
		}
		else if (nextRcp.getStepNumber() == 5) {
			// FINAL RISK CALULATION TABLE
			dock = wizardPanels.get(panelCounter);
			wizardContainer.remove(dock);
			
			// tow to highlight
			int likelihood = presenter.getRiskLevelCalculation().getOverallLikelihood();
			// column to highlight
			int impact = presenter.getRiskLevelCalculation().getRiskLevelForQuestion(4);

			dock = createDock(null);
			VerticalPanel resultsPanel = new VerticalPanel();
			resultsPanel.setSpacing(4);
			dock.add(resultsPanel, DockPanel.NORTH);
			
			resultsPanel.add(new HTML("Overall likelihood: " + this.getRiskLevelForInt(likelihood)));
			resultsPanel.add(new HTML("Level of impact   : " + this.getRiskLevelForInt(impact)));

			HTML dpContent = new HTML(nextRcp.getTableHeading());
			resultsPanel.add(dpContent);
			resultsPanel.setCellHorizontalAlignment(dpContent, HasHorizontalAlignment.ALIGN_CENTER);
			HTML dpSubHeading = new HTML(nextRcp.getTableSubHeading());
			resultsPanel.add(dpSubHeading);
			resultsPanel.setCellHorizontalAlignment(dpSubHeading, HasHorizontalAlignment.ALIGN_CENTER);

			// TODO: build the table using overallLikelihood and step4Answer to 
			// highlight the appropriate cell and derive the appropriate
			// overall risk level using the text->number from the cell
			// that gets highlighted

			HorizontalPanel headerPanel = new HorizontalPanel();
			headerPanel.setWidth("96%");
			headerPanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			headerPanel.getElement().getStyle().setBorderWidth(1, Unit.PX);
			headerPanel.getElement().getStyle().setBackgroundColor("#e5e8e8");
			resultsPanel.add(headerPanel);
			
			HTML likelihoodHeader = new HTML("<b>Likelihood</b>");
			HTML impactHeader = new HTML("<b>Level of Impact</b>");
			headerPanel.add(likelihoodHeader);
			headerPanel.setCellHorizontalAlignment(likelihoodHeader, HasHorizontalAlignment.ALIGN_LEFT);
			headerPanel.add(impactHeader);
			headerPanel.setCellHorizontalAlignment(impactHeader, HasHorizontalAlignment.ALIGN_CENTER);
			
			Grid table = new Grid(7,7);
			resultsPanel.add(table);
			table.setCellSpacing(0);
			table.setCellPadding(6);

			// blank cell
			this.addCellToTable(table, 0, 0, "", "#e5e8e8");

			// column headings (row 0, columns 1-5)
			this.addCellToTable(table, 0, 1, "<b>Very Low</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 2, "<b>Low</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 3, "<b>Medium</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 4, "<b>High</b>", "#e5e8e8");
			this.addCellToTable(table, 0, 5, "<b>Critical</b>", "#e5e8e8");

			// Row headings, column 0, row 1-5
			this.addCellToTable(table, 1, 0, "<b>Critical</b>", "#e5e8e8");
			this.addCellToTable(table, 2, 0, "<b>High</b>", "#e5e8e8");
			this.addCellToTable(table, 3, 0, "<b>Medium</b>", "#e5e8e8");
			this.addCellToTable(table, 4, 0, "<b>Low</b>", "#e5e8e8");
			this.addCellToTable(table, 5, 0, "<b>Very Low</b>", "#e5e8e8");
			
			// row 1, columns 1 - 5
			this.addCellToTable(table, 1, 1, "<b>Very Low</b>", null);
			this.addCellToTable(table, 1, 2, "<b>Low</b>", null);
			this.addCellToTable(table, 1, 3, "<b>Medium</b>", null);
			this.addCellToTable(table, 1, 4, "<b>High</b>", null);
			this.addCellToTable(table, 1, 5, "<b>Critical</b>", null);

			// row 2, columns 1 - 5
			this.addCellToTable(table, 2, 1, "<b>Very Low</b>", null);
			this.addCellToTable(table, 2, 2, "<b>Low</b>", null);
			this.addCellToTable(table, 2, 3, "<b>Medium</b>", null);
			this.addCellToTable(table, 2, 4, "<b>High</b>", null);
			this.addCellToTable(table, 2, 5, "<b>Critical</b>", null);

			// row 3, columns 1 - 5
			this.addCellToTable(table, 3, 1, "<b>Very Low</b>", null);
			this.addCellToTable(table, 3, 2, "<b>Low</b>", null);
			this.addCellToTable(table, 3, 3, "<b>Medium</b>", null);
			this.addCellToTable(table, 3, 4, "<b>Medium</b>", null);
			this.addCellToTable(table, 3, 5, "<b>High</b>", null);

			// row 4, columns 1 - 5
			this.addCellToTable(table, 4, 1, "<b>Very Low</b>", null);
			this.addCellToTable(table, 4, 2, "<b>Low</b>", null);
			this.addCellToTable(table, 4, 3, "<b>Low</b>", null);
			this.addCellToTable(table, 4, 4, "<b>Low</b>", null);
			this.addCellToTable(table, 4, 5, "<b>Medium</b>", null);

			// row 5, columns 1 - 5
			this.addCellToTable(table, 5, 1, "<b>Very Low</b>", null);
			this.addCellToTable(table, 5, 2, "<b>Very Low</b>", null);
			this.addCellToTable(table, 5, 3, "<b>Very Low</b>", null);
			this.addCellToTable(table, 5, 4, "<b>Low</b>", null);
			this.addCellToTable(table, 5, 5, "<b>Low</b>", null);

			// highlight the appropriate cell
			int row = 0;
			switch (likelihood) {
			case 1:
				row = 5;
				break;
			case 2:
				row = 4;
				break;
			case 3:
				row = 3;
				break;
			case 4:
				row = 2;
				break;
			case 5:
				row = 1;
				break;
			}
			table.getCellFormatter().getElement(row, impact).getStyle().setBackgroundColor("#f1948a");
			HTML overallRiskLevelHTML = (HTML)table.getWidget(row, impact);
			overallRiskLevelText = overallRiskLevelHTML.getText();
			if (presenter.getRisk() != null) {
				GWT.log("Overall risk level for the " + 
					presenter.getRisk().getSecurityRiskName() + " security risk"
					+ "in the " + presenter.getService().getAwsServiceName() + " is: " + 
					overallRiskLevelText);
			}
			else {
				GWT.log("Overall risk level for the 'null' security risk is: " + 
					overallRiskLevelText);
			}
			
			// TODO: how to get the risk level back to the security risk object/view

			wizardPanels.add(panelCounter, dock);
			wizardContainer.insert(dock, panelCounter);
		}
		else {
			dock = wizardPanels.get(panelCounter);
		}
		wizardContainer.setWidget(dock);
		wizardContainer.animate(700);
	}
	
	private void addCellToTable(Grid table, int row, int column, String htmlText, String backgroundColor) {
		table.setWidget(row, column, new HTML(htmlText));
		table.getCellFormatter().getElement(row, column).getStyle().setBorderStyle(BorderStyle.SOLID);
		table.getCellFormatter().getElement(row, column).getStyle().setBorderWidth(1, Unit.PX);
		
		if (backgroundColor != null) {
			table.getCellFormatter().getElement(row, column).getStyle().setBackgroundColor(backgroundColor);
		}
	}
	
	private String getRiskLevelForInt(int level) {
		switch (level) {
		case 1:
			return "Very Low";
		case 2:
			return "Low";
		case 3:
			return "Medium";
		case 4:
			return "High";
		case 5:
			return "Critical";
		}
		return "Unknown";
	}
	private int getOverallRiskLevelAsInt(String overallRiskText) {
		return 0;
	}
	
	private int getOverallLikelihoodAsInt(String olhText) {
		if (olhText.equalsIgnoreCase("very low")) {
			return 1;
		}
		if (olhText.equalsIgnoreCase("low")) {
			return 2;
		}
		if (olhText.equalsIgnoreCase("medium")) {
			return 3;
		}
		if (olhText.equalsIgnoreCase("high")) {
			return 4;
		}
		if (olhText.equalsIgnoreCase("critical")) {
			return 5;
		}
		return 0;
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
		overallRiskLevelText = "Medium";	// default value
		panelNumber=0;
	    numberOfDockPanels = 0;
	    panelCounter = 0;
	    firstPrevClick=true;
	    firstNextClick=true;
		nextButton.setEnabled(true);
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
