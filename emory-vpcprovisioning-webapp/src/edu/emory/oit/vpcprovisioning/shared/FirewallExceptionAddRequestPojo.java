package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class FirewallExceptionAddRequestPojo extends SharedObject implements IsSerializable, Comparable<FirewallExceptionAddRequestPojo> {
	/*
<!ELEMENT FirewallExceptionAddRequest (
	RequestItemNumber,  
	RequestState, 
	RequestItemState, 
	SystemId, 
	UserNetID, 
	ApplicationName, 
	SourceOutsideEmory, 
	TimeRule, 
	ValidUntilDate?, 
	SourceIpAddresses, 
	DestinationIpAddresses, 
	Ports, 
	BusinessReason, 
	Patched, 
	NotPatchedJustification?, 
	DefaultPasswdChanged, 
	PasswdNotChangedJustification?, 
	AppConsoleACLed, 
	NotACLedJustification?, 
	Hardened, 
	NotHardenedJustification?, 
	PatchingPlan, 
	Compliance*, 
	OtherCompliance?, 
	SensitiveDataDesc, 
	LocalFirewallRules, 
	DefaultDenyZone, 
	Tag+, 
	WillTraverseVPN, 
	VPNName?, 
	AccessAwsVPC)> 
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
	String notPatchedJustification;
	String isDefaultPasswdChanged;
	String passwdNotChangedJustification;
	String isAppConsoleACLed;
	String notACLedJustification;
	String isHardened;
	String notHardenedJustification;
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
	FirewallExceptionAddRequestPojo baseline;
	
	public static final ProvidesKey<FirewallExceptionAddRequestPojo> KEY_PROVIDER = new ProvidesKey<FirewallExceptionAddRequestPojo>() {
		@Override
		public Object getKey(FirewallExceptionAddRequestPojo item) {
			return item == null ? null : item.getSystemId();
		}
	};

	public FirewallExceptionAddRequestPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(FirewallExceptionAddRequestPojo o) {
		// TODO Auto-generated method stub
		return 0;
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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

	public FirewallExceptionAddRequestPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(FirewallExceptionAddRequestPojo baseline) {
		this.baseline = baseline;
	}

	public String getNotPatchedJustification() {
		return notPatchedJustification;
	}

	public void setNotPatchedJustification(String notPatchedJustification) {
		this.notPatchedJustification = notPatchedJustification;
	}

	public String getPasswdNotChangedJustification() {
		return passwdNotChangedJustification;
	}

	public void setPasswdNotChangedJustification(String passwdNotChangedJustification) {
		this.passwdNotChangedJustification = passwdNotChangedJustification;
	}

	public String getNotACLedJustification() {
		return notACLedJustification;
	}

	public void setNotACLedJustification(String notACLedJustification) {
		this.notACLedJustification = notACLedJustification;
	}

	public String getNotHardenedJustification() {
		return notHardenedJustification;
	}

	public void setNotHardenedJustification(String notHardenedJustification) {
		this.notHardenedJustification = notHardenedJustification;
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
}
