package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class VpnConnectionProvisioningPojo extends SharedObject implements IsSerializable, Comparable<VpnConnectionProvisioningPojo> {
	
	String provisioningId;
	VpnConnectionRequisitionPojo requisition;
	String status;
	String provisioningResult;
	String actualTime;
	String anticipatedTime;
	List<ProvisioningStepPojo> provisioningSteps = new java.util.ArrayList<ProvisioningStepPojo>();	
	VpnConnectionProvisioningPojo baseline;
	
	public int getTotalStepCount() {
		return provisioningSteps.size();
	}
	
	public int getSuccessfullStepCount() {
		// if result is 'success' increment counter
		int successfulStepCount = 0;
		for (ProvisioningStepPojo step : provisioningSteps) {
			if (step.getStepResult() != null) {
				if (step.getStepResult().equalsIgnoreCase(Constants.VPCP_STEP_RESULT_SUCCESS)) {
					successfulStepCount++;
				}
			}
		}
		return successfulStepCount;
	}
	public int getCompletedStepCount() {
		// if status is 'complete' increment counter
		int completeStepCount = 0;
		for (ProvisioningStepPojo step : provisioningSteps) {
			if (step.getStatus() != null) {
				if (step.getStatus().equalsIgnoreCase(Constants.PROVISIONING_STEP_STATUS_COMPLETED)) {
					
					completeStepCount++;
				}
			}
		}
		return completeStepCount;
	}
	
	public static final ProvidesKey<VpnConnectionProvisioningPojo> KEY_PROVIDER = new ProvidesKey<VpnConnectionProvisioningPojo>() {
		@Override
		public Object getKey(VpnConnectionProvisioningPojo item) {
			return item == null ? null : item.getProvisioningId();
		}
	};

	@Override
	public int compareTo(VpnConnectionProvisioningPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

	public VpnConnectionProvisioningPojo() {
		
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
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

	public VpnConnectionProvisioningPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(VpnConnectionProvisioningPojo baseline) {
		this.baseline = baseline;
	}

	public VpnConnectionRequisitionPojo getRequisition() {
		return requisition;
	}

	public void setRequisition(VpnConnectionRequisitionPojo requisition) {
		this.requisition = requisition;
	}

}
