package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class StaticNatDeprovisioningPojo extends SharedObject implements IsSerializable, Comparable<StaticNatDeprovisioningPojo> {
	
	String provisioningId;
	StaticNatPojo staticNat;
	String status;
	String provisioningResult;
	String actualTime;
	String anticipatedTime;
	List<ProvisioningStepPojo> provisioningSteps = new java.util.ArrayList<ProvisioningStepPojo>();	
	StaticNatDeprovisioningPojo baseline;
	
	public StaticNatDeprovisioningPojo() {
		// TODO Auto-generated constructor stub
	}

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

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
	}

	public StaticNatPojo getStaticNat() {
		return staticNat;
	}

	public void setStaticNat(StaticNatPojo staticNat) {
		this.staticNat = staticNat;
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

	public StaticNatDeprovisioningPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(StaticNatDeprovisioningPojo baseline) {
		this.baseline = baseline;
	}

	@Override
	public int compareTo(StaticNatDeprovisioningPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

}
