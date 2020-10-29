package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class RoleProvisioningPojo extends SharedObject implements IsSerializable, Comparable<RoleProvisioningPojo> {

	/*
	<!ELEMENT AccountProvisioning (
		ProvisioningId, 
		AccountProvisioningRequisition, 
		Status, 
		ProvisioningResult?, 
		ActualTime?, 
		AnticipatedTime?, 
		ProvisioningStep*, 
		CreateUser, 
		CreateDatetime, 
		LastUpdateUser?, 
		LastUpdateDatetime?)>
	 */
	String provisioningId;
	RoleProvisioningRequisitionPojo requisition;
	String status;
	String provisioningResult;
	String actualTime;
	String anticipatedTime;
	List<ProvisioningStepPojo> provisioningSteps = new java.util.ArrayList<ProvisioningStepPojo>();	
	RoleProvisioningPojo baseline;
	
	public int getTotalStepCount() {
		return provisioningSteps.size();
	}
	
	public boolean isSuccessful() {
		if (getTotalStepCount() == getCompletedSuccessfullCount()) {
			return true;
		}
		return false;
	}
	public int getCompletedSuccessfullCount() {
		// if status is 'complete' and stepResult is 'success' increment counter
		int cnt = 0;
		for (ProvisioningStepPojo step : provisioningSteps) {
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
	
	public static final ProvidesKey<RoleProvisioningPojo> KEY_PROVIDER = new ProvidesKey<RoleProvisioningPojo>() {
		@Override
		public Object getKey(RoleProvisioningPojo item) {
			return item == null ? null : item.getProvisioningId();
		}
	};

	public RoleProvisioningPojo() {
		
	}

	@Override
	public int compareTo(RoleProvisioningPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provioningId) {
		this.provisioningId = provioningId;
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

	public String getProvisioningResult() {
		return provisioningResult;
	}

	public void setProvisioningResult(String provisioningResult) {
		this.provisioningResult = provisioningResult;
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

	public List<ProvisioningStepPojo> getProvisioningSteps() {
		return provisioningSteps;
	}

	public void setProvisioningSteps(List<ProvisioningStepPojo> provisioningSteps) {
		this.provisioningSteps = provisioningSteps;
	}

	public RoleProvisioningPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(RoleProvisioningPojo baseline) {
		this.baseline = baseline;
	}
}
