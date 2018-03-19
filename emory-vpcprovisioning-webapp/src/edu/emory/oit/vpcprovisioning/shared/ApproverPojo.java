package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ApproverPojo extends SharedObject implements IsSerializable {
	//ApproverDN,Sequence
	String approverDN;
	String sequence;

	public ApproverPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getApproverDN() {
		return approverDN;
	}

	public void setApproverDN(String approverDN) {
		this.approverDN = approverDN;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

}
