package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RiskCalculationPropertiesPojo extends SharedObject implements IsSerializable {
	String stepName;
	int questionNumber;
	int stepNumber;
	String questionText;
	String tableHeading;
	String tableSubHeading;
	List<RiskCalculationAnswerPojo> answers = new java.util.ArrayList<RiskCalculationAnswerPojo>();
	public RiskCalculationPropertiesPojo() {
		
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public List<RiskCalculationAnswerPojo> getAnswers() {
		return answers;
	}
	public void setAnswers(List<RiskCalculationAnswerPojo> answers) {
		this.answers = answers;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public int getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	public int getStepNumber() {
		return stepNumber;
	}
	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}
	public String getTableHeading() {
		return tableHeading;
	}
	public void setTableHeading(String tableHeading) {
		this.tableHeading = tableHeading;
	}
	public String getTableSubHeading() {
		return tableSubHeading;
	}
	public void setTableSubHeading(String tableSubHeading) {
		this.tableSubHeading = tableSubHeading;
	}
	
}
