package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class RoleDeprovisioningPojo extends SharedObject implements IsSerializable, Comparable<RoleDeprovisioningPojo> {

	/*
	<!ELEMENT AccountDeprovisioning (
		DeprovisioningId, 
		AccountDeprovisioningRequisition, 
		Status, 
		DeprovisioningResult?, 
		ActualTime?, 
		AnticipatedTime?, 
		DeprovisioningStep*, 
		CreateUser, 
		CreateDatetime, 
		LastUpdateUser?, 
		LastUpdateDatetime?)>
	 */
	String deProvisioningId;
	RoleProvisioningRequisitionPojo requisition;
	String status;
	String deprovisioningResult;
	String actualTime;
	String anticipatedTime;
	List<ProvisioningStepPojo> deprovisioningSteps = new java.util.ArrayList<ProvisioningStepPojo>();	
	RoleDeprovisioningPojo baseline;
	
	public int getTotalStepCount() {
		return deprovisioningSteps.size();
	}
	
	public int getCompletedSuccessfulCount() {
		// if status is 'complete' and stepResult is 'success' increment counter
		int cnt = 0;
		for (ProvisioningStepPojo step : deprovisioningSteps) {
			if (step.getStatus() != null) {
				if (step.getStatus().equalsIgnoreCase(Constants.PROVISIONING_STEP_STATUS_COMPLETED)) {
					if (step.getStepResult() != null) {
						if (step.getStepResult().equalsIgnoreCase(Constants.VPCP_STEP_RESULT_SUCCESS)) {
							cnt++;
						}
					}
				}
			}
		}
		return cnt;
	}
	
	public static final ProvidesKey<RoleDeprovisioningPojo> KEY_PROVIDER = new ProvidesKey<RoleDeprovisioningPojo>() {
		@Override
		public Object getKey(RoleDeprovisioningPojo item) {
			return item == null ? null : item.getDeprovisioningId();
		}
	};

	public RoleDeprovisioningPojo() {
		
	}

	@Override
	public int compareTo(RoleDeprovisioningPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

	public String getDeprovisioningId() {
		return deProvisioningId;
	}

	public void setDeprovisioningId(String deProvisioningId) {
		this.deProvisioningId = deProvisioningId;
	}

	public RoleProvisioningRequisitionPojo getRequisition() {
		return requisition;
	}

	public void setRequisition(RoleProvisioningRequisitionPojo requisition) {
		this.requisition = requisition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeprovisioningResult() {
		return deprovisioningResult;
	}

	public void setDeprovisioningResult(String provisioningResult) {
		this.deprovisioningResult = provisioningResult;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getAnticipatedTime() {
		return anticipatedTime;
	}

	public void setAnticipatedTime(String anticipatedTime) {
		this.anticipatedTime = anticipatedTime;
	}

	public List<ProvisioningStepPojo> getDeprovisioningSteps() {
		return deprovisioningSteps;
	}

	public void setDeprovisioningSteps(List<ProvisioningStepPojo> provisioningSteps) {
		this.deprovisioningSteps = provisioningSteps;
	}

	public RoleDeprovisioningPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(RoleDeprovisioningPojo baseline) {
		this.baseline = baseline;
	}

}
