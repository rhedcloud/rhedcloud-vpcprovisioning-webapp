package edu.emory.oit.vpcprovisioning.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ProvisioningStepPojo extends SharedObject implements IsSerializable, Comparable<ProvisioningStepPojo> {

	String provisioningStepId;
	String provisioningId;
	String stepId;
	String type;
	String description;
	String status;
	String stepResult;
	String actualTime;
	String anticipatedTime;
	HashMap<String, String> properties = new HashMap<String, String>();
	
	public ProvisioningStepPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(ProvisioningStepPojo o) {
//		if (Integer.parseInt(o.getStepId()) > Integer.parseInt(this.getStepId())) {			
//			return 0;
//		}
//		else {
//			return 1;
//		}
		Integer oi = new Integer(o.getStepId());
		Integer ti = new Integer(this.getStepId());
		return ti.compareTo(oi);
		
//		return o.getStepId().compareTo(this.getStepId());
	}

	public String getProvisioningStepId() {
		return provisioningStepId;
	}

	public void setProvisioningStepId(String provisioningStepId) {
		this.provisioningStepId = provisioningStepId;
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStepResult() {
		return stepResult;
	}

	public void setStepResult(String stepResult) {
		this.stepResult = stepResult;
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

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

}
