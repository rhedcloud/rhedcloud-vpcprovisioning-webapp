package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RiskLevelCalculationPojo extends SharedObject implements IsSerializable, Comparable<RiskLevelCalculationPojo> {
	// this is the pojo that we'll use to store answers to the risk calculation wizard.
	// it will store a combination of question numbers and answer numbers that will be used
	// to eventually arrive at the overall risk level.  i just don't know what this object
	// needs to look like just yet...

	List<RiskQuestionLevelPojo> questionLevels = new java.util.ArrayList<RiskQuestionLevelPojo>();
	int overallLikelihood;
	int levelOfImpact;
	public RiskLevelCalculationPojo() {
		
	}
	@Override
	public int compareTo(RiskLevelCalculationPojo o) {
		return 0;
	}
	public List<RiskQuestionLevelPojo> getQuestionLevels() {
		return questionLevels;
	}
	public void setQuestionLevels(List<RiskQuestionLevelPojo> questionLevels) {
		this.questionLevels = questionLevels;
	}
	public void updateLevelForQuestion(int questionNumber, int riskLevel) {
		for (RiskQuestionLevelPojo rql : questionLevels) {
			if (rql.getQuestionNumber() == questionNumber) {
				rql.setRiskLevel(riskLevel);
				return;
			}
		}
		RiskQuestionLevelPojo rql = new RiskQuestionLevelPojo();
		rql.setQuestionNumber(questionNumber);
		rql.setRiskLevel(riskLevel);
		questionLevels.add(rql);
	}
	public int getRiskLevelForQuestion(int questionNumber) {
		for (RiskQuestionLevelPojo rql : questionLevels) {
			if (rql.getQuestionNumber() == questionNumber) {
				return rql.getRiskLevel();
			}
		}
		return 0;
	}
	public int getOverallLikelihood() {
		return overallLikelihood;
	}
	public void setOverallLikelihood(int overallLikelihood) {
		this.overallLikelihood = overallLikelihood;
	}
	public int getLevelOfImpact() {
		return levelOfImpact;
	}
	public void setLevelOfImpact(int levelOfImpact) {
		this.levelOfImpact = levelOfImpact;
	}
}
