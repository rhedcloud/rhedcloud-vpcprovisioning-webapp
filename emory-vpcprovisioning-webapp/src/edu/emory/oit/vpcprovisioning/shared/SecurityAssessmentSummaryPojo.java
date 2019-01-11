package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SecurityAssessmentSummaryPojo extends SharedObject implements IsSerializable, Comparable<SecurityAssessmentSummaryPojo> {
	AWSServicePojo service;
	ServiceSecurityAssessmentPojo assessment;
	
	public SecurityAssessmentSummaryPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(SecurityAssessmentSummaryPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

	public AWSServicePojo getService() {
		return service;
	}

	public void setService(AWSServicePojo service) {
		this.service = service;
	}

	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}

	public void setAssessment(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}

}
