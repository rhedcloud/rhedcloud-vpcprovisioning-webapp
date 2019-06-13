package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionAddRequestQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	/*
<!ELEMENT FirewallExceptionAddRequestQuerySpecification (
	RequestItemNumber?, 
	RequestState?, 
	RequestItemState?, 
	SystemId?, 
	UserNetID?, 
	ApplicationName?, 
	SourceOutsideEmory?, 
	TimeRule?, 
	ValidUntilDate?, 
	SourceIpAddresses?, 
	DestinationIpAddresses?, 
	Ports?, 
	BusinessReason?, 
	Patched?, 
	DefaultPasswdChanged?, 
	AppConsoleACLed?, 
	Hardened?, 
	PatchingPlan?, 
	Compliance*, 
	OtherCompliance?, 
	SensitiveDataDesc?, 
	LocalFirewallRules?, 
	DefaultDenyZone?, 
	Tag*, 
	WillTraverseVPN?, 
	VPNName?, 
	AccessAwsVPC?)>
	 */
	String requestItemNumber;
	String requestState;
	String requestItemState;
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
	String willTraverseVPN;
	String vpnName;
	String accessAwsVPC;

	public FirewallExceptionAddRequestQueryFilterPojo() {
		
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

	public String getWillTraverseVPN() {
		return willTraverseVPN;
	}

	public void setWillTraverseVPN(String willTraverseVPN) {
		this.willTraverseVPN = willTraverseVPN;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getAccessAwsVPC() {
		return accessAwsVPC;
	}

	public void setAccessAwsVPC(String accessAwsVPC) {
		this.accessAwsVPC = accessAwsVPC;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
}
