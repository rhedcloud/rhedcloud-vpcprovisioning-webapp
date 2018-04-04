package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRequestQueryFilterPojo extends SharedObject implements IsSerializable {
	String systemId;
	String userNetId;
	String applicationName;
	String isSourceOutsideEmory;
	String timeRule;
	Date validUntilDate;
	String sourceIp;
	String destinationIp;
	String ports;
	String businessReason;
	String isPatched;
	String isDefaultPasswdChanged;
	String isAppConsoleACLed;
	String isHardened;
	String patchingPlan;
	List<String> compliance = new java.util.ArrayList<String>();
	String otherCompliance;
	String sensitiveDataDesc;
	String localFirewallRules;
	String isDefaultDenyZone;
	List<String> tags = new java.util.ArrayList<String>();
	String requestNumber;
	String requestState;
	String requestItemNumber;
	String requestItemState;
	/*
	(SystemId?, UserNetID?, ApplicationName?, 
	IsSourceOutsideEmory?, TimeRule?, ValidUntilDate?, SourceIpAddresses?, DestinationIpAddresses?, 
	Ports?, BusinessReason?, IsPatched?, IsDefaultPasswdChanged?, IsAppConsoleACLed?, IsHardened?, 
	PatchingPlan?, Compliance*, OtherCompliance?, SensitiveDataDesc?, LocalFirewallRules?, 
	IsDefaultDenyZone?, Tag*, RequestNumber?,  RequestState?, RequestItemNumber?, RequestItemState?, 
	CreateDatetime?, UpdateDatetime?)>
	 */

	public FirewallExceptionRequestQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUserNetId() {
		return userNetId;
	}

	public void setUserNetId(String userNetId) {
		this.userNetId = userNetId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getIsSourceOutsideEmory() {
		return isSourceOutsideEmory;
	}

	public void setIsSourceOutsideEmory(String isSourceOutsideEmory) {
		this.isSourceOutsideEmory = isSourceOutsideEmory;
	}

	public String getTimeRule() {
		return timeRule;
	}

	public void setTimeRule(String timeRule) {
		this.timeRule = timeRule;
	}

	public Date getValidUntilDate() {
		return validUntilDate;
	}

	public void setValidUntilDate(Date validUntilDate) {
		this.validUntilDate = validUntilDate;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getDestinationIp() {
		return destinationIp;
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getBusinessReason() {
		return businessReason;
	}

	public void setBusinessReason(String businessReason) {
		this.businessReason = businessReason;
	}

	public String getIsPatched() {
		return isPatched;
	}

	public void setIsPatched(String isPatched) {
		this.isPatched = isPatched;
	}

	public String getIsDefaultPasswdChanged() {
		return isDefaultPasswdChanged;
	}

	public void setIsDefaultPasswdChanged(String isDefaultPasswdChanged) {
		this.isDefaultPasswdChanged = isDefaultPasswdChanged;
	}

	public String getIsAppConsoleACLed() {
		return isAppConsoleACLed;
	}

	public void setIsAppConsoleACLed(String isAppConsoleACLed) {
		this.isAppConsoleACLed = isAppConsoleACLed;
	}

	public String getIsHardened() {
		return isHardened;
	}

	public void setIsHardened(String isHardened) {
		this.isHardened = isHardened;
	}

	public String getPatchingPlan() {
		return patchingPlan;
	}

	public void setPatchingPlan(String patchingPlan) {
		this.patchingPlan = patchingPlan;
	}

	public List<String> getCompliance() {
		return compliance;
	}

	public void setCompliance(List<String> compliance) {
		this.compliance = compliance;
	}

	public String getOtherCompliance() {
		return otherCompliance;
	}

	public void setOtherCompliance(String otherCompliance) {
		this.otherCompliance = otherCompliance;
	}

	public String getSensitiveDataDesc() {
		return sensitiveDataDesc;
	}

	public void setSensitiveDataDesc(String sensitiveDataDesc) {
		this.sensitiveDataDesc = sensitiveDataDesc;
	}

	public String getLocalFirewallRules() {
		return localFirewallRules;
	}

	public void setLocalFirewallRules(String localFirewallRules) {
		this.localFirewallRules = localFirewallRules;
	}

	public String getIsDefaultDenyZone() {
		return isDefaultDenyZone;
	}

	public void setIsDefaultDenyZone(String isDefaultDenyZone) {
		this.isDefaultDenyZone = isDefaultDenyZone;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public String getRequestState() {
		return requestState;
	}

	public void setRequestState(String requestState) {
		this.requestState = requestState;
	}

	public String getRequestItemNumber() {
		return requestItemNumber;
	}

	public void setRequestItemNumber(String requestItemNumber) {
		this.requestItemNumber = requestItemNumber;
	}

	public String getRequestItemState() {
		return requestItemState;
	}

	public void setRequestItemState(String requestItemState) {
		this.requestItemState = requestItemState;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
