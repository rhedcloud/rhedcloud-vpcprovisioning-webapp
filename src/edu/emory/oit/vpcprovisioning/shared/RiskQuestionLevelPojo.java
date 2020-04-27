package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RiskQuestionLevelPojo extends SharedObject implements IsSerializable, Comparable<RiskQuestionLevelPojo> {
	// this is the pojo that we'll use to store answers to the risk calculation wizard.
	// it will store a combination of question numbers and answer numbers that will be used
	// to eventually arrive at the overall risk level.  i just don't know what this object
	// needs to look like just yet...
	int questionNumber;
	int riskLevel;
	
	public RiskQuestionLevelPojo() {
		
	}
	@Override
	public int compareTo(RiskQuestionLevelPojo o) {
		return 0;
	}
	public int getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	public int getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(int riskLevel) {
		this.riskLevel = riskLevel;
	}
	
}
