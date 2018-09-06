package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class VpncpPojo extends SharedObject implements IsSerializable, Comparable<VpncpPojo> {
	
	String provisioningId;
	VpncpRequisitionPojo requisition;
	String status;
	String provisioningResult;
	String actualTime;
	String anticipatedTime;
	List<ProvisioningStepPojo> provisioningSteps = new java.util.ArrayList<ProvisioningStepPojo>();	
	VpncpPojo baseline;
	
	public int getTotalStepCount() {
		return provisioningSteps.size();
	}
	
	public int getCompletedStepCount() {
		// if status is 'complete' and stepResult is 'success' increment counter
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
	
	public static final ProvidesKey<VpncpPojo> KEY_PROVIDER = new ProvidesKey<VpncpPojo>() {
		@Override
		public Object getKey(VpncpPojo item) {
			return item == null ? null : item.getProvisioningId();
		}
	};

	@Override
	public int compareTo(VpncpPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

	public VpncpPojo() {
		// TODO Auto-generated constructor stub
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

	public VpncpPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(VpncpPojo baseline) {
		this.baseline = baseline;
	}

	public VpncpRequisitionPojo getRequisition() {
		return requisition;
	}

	public void setRequisition(VpncpRequisitionPojo requisition) {
		this.requisition = requisition;
	}

}
