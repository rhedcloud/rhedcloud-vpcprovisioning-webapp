package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class CidrAssignmentPojo extends SharedObject implements IsSerializable, Comparable<CidrAssignmentPojo> {
	String cidrAssignmentId;
	String ownerId;
	String description;
	String purpose;
	CidrPojo cidr;
	CidrAssignmentPojo baseline;
	
	public static final ProvidesKey<CidrAssignmentPojo> KEY_PROVIDER = new ProvidesKey<CidrAssignmentPojo>() {
		@Override
		public Object getKey(CidrAssignmentPojo item) {
			return item == null ? null : item.getCidrAssignmentId();
		}
	};

	public CidrAssignmentPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getCidrAssignmentId() {
		return cidrAssignmentId;
	}

	public void setCidrAssignmentId(String ciderAssignmentId) {
		this.cidrAssignmentId = ciderAssignmentId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public CidrPojo getCidr() {
		return cidr;
	}

	public void setCidr(CidrPojo cidr) {
		this.cidr = cidr;
	}

	@Override
	public int compareTo(CidrAssignmentPojo o) {
		return o.getPurpose().compareTo(this.getPurpose());
	}

	public CidrAssignmentPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(CidrAssignmentPojo baseline) {
		this.baseline = baseline;
	}

	
}
