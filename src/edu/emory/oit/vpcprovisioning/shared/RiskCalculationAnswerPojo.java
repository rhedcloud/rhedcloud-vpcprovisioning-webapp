package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RiskCalculationAnswerPojo extends SharedObject implements IsSerializable, Comparable<RiskCalculationAnswerPojo> {
	String answerText;
	int riskLevel;
	public RiskCalculationAnswerPojo() {
		
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public int getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(int riskLevel) {
		this.riskLevel = riskLevel;
	}
	@Override
	public int compareTo(RiskCalculationAnswerPojo o) {
		if (riskLevel > o.getRiskLevel()) {
			return -1;
		}
		else if (riskLevel < o.getRiskLevel()) {
			return 1;
		}
		return 0;
	}
	
}
