package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class CidrAssignmentSummaryPojo extends SharedObject implements IsSerializable, Comparable<CidrAssignmentSummaryPojo> {
	CidrAssignmentPojo cidrAssignment;
	VpcPojo vpc;
	AccountPojo account;

	public CidrAssignmentSummaryPojo() {
	}

	public static final ProvidesKey<CidrAssignmentSummaryPojo> KEY_PROVIDER = new ProvidesKey<CidrAssignmentSummaryPojo>() {
		@Override
		public Object getKey(CidrAssignmentSummaryPojo item) {
			return item == null ? null : item.getCidrAssignment().getCidrAssignmentId();
		}
	};

	@Override
	public int compareTo(CidrAssignmentSummaryPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public CidrAssignmentPojo getCidrAssignment() {
		return cidrAssignment;
	}

	public void setCidrAssignment(CidrAssignmentPojo cidrAssignment) {
		this.cidrAssignment = cidrAssignment;
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

}
