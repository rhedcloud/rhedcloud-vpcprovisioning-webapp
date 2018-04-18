package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class CidrSummaryPojo extends SharedObject implements IsSerializable, Comparable<CidrSummaryPojo> {
	CidrPojo cidr;
	CidrAssignmentSummaryPojo assignmentSummary;

	public static final ProvidesKey<CidrSummaryPojo> KEY_PROVIDER = new ProvidesKey<CidrSummaryPojo>() {
		@Override
		public Object getKey(CidrSummaryPojo item) {
			if (item.getCidr() != null) {
				return item.getCidr().getCidrId();
			}
			else {
				return item.getAssignmentSummary().getCidrAssignment().getCidr().getCidrId();
			}
		}
	};

	public CidrSummaryPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(CidrSummaryPojo o) {
		if (this.getCidr() != null && o.getCidr() != null) {
			if (this.getCidr().getCreateTime() != null && o.getCidr().getCreateTime() != null) {
				return o.getCidr().getCreateTime().compareTo(this.getCidr().getCreateTime());
			}
			else {
				return 0;
			}
		}
		else {
			if (o.getAssignmentSummary() == null) {
				System.out.println("[o] null assignment summary");
				return 0;
			}
			if (o.getAssignmentSummary().getCreateTime() == null) {
				System.out.println("[o] null assignment summary create time");
				return 0;
			}
			if (this.getAssignmentSummary() == null) {
				System.out.println("[this] null assignment summary");
				return 0;
			}
			if (this.getAssignmentSummary().getCreateTime() == null) {
				System.out.println("[this] null assignment summary create time");
				return 0;
			}
			System.out.println("good assignment comparison");
			return o.getAssignmentSummary().getCidrAssignment().getCreateTime().compareTo(this.getAssignmentSummary().getCidrAssignment().getCreateTime());
		}
	}

	public CidrPojo getCidr() {
		return cidr;
	}

	public void setCidr(CidrPojo cidr) {
		this.cidr = cidr;
	}

	public CidrAssignmentSummaryPojo getAssignmentSummary() {
		return assignmentSummary;
	}

	public void setAssignmentSummary(CidrAssignmentSummaryPojo assignmentSummary) {
		this.assignmentSummary = assignmentSummary;
	}

}
