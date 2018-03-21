package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleAssignmentPojo extends SharedObject implements IsSerializable, Comparable<RoleAssignmentPojo> {
	// RoleAssignmentActionType?, RoleAssignmentType?, CorrelationId?, CauseIdentities?, EffectiveDatetime?, 
	// ExpirationDatetime?,IdentityDN?,OriginatorDN?,Reason?,RoleDNs?,SodJustification*,ExplicitIdentityDNs?, 
	// RoleDN?
	
	String actionType;
	String type;
	String correlationId;
	Date effectiveDate;
	Date expirationDate;
	String identityDN;
	String originatorDN;
	String reason;
	RoleDNsPojo roleDNs;
	CauseIdentitiesPojo causeIdentities;
	List<SodJustificationPojo> sodJustifications = new java.util.ArrayList<SodJustificationPojo>();
	ExplicitIdentityDNsPojo explicityIdentitiyDNs;

	public RoleAssignmentPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(RoleAssignmentPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getIdentityDN() {
		return identityDN;
	}

	public void setIdentityDN(String identityDN) {
		this.identityDN = identityDN;
	}

	public String getOriginatorDN() {
		return originatorDN;
	}

	public void setOriginatorDN(String originatorDN) {
		this.originatorDN = originatorDN;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public RoleDNsPojo getRoleDNs() {
		return roleDNs;
	}

	public void setRoleDN(RoleDNsPojo roleDNs) {
		this.roleDNs = roleDNs;
	}

	public CauseIdentitiesPojo getCauseIdentities() {
		return causeIdentities;
	}

	public void setCauseIdentities(CauseIdentitiesPojo causeIdentities) {
		this.causeIdentities = causeIdentities;
	}

	public List<SodJustificationPojo> getSodJustifications() {
		return sodJustifications;
	}

	public void setSodJustifications(List<SodJustificationPojo> sodJustifications) {
		this.sodJustifications = sodJustifications;
	}

	public ExplicitIdentityDNsPojo getExplicityIdentitiyDNs() {
		return explicityIdentitiyDNs;
	}

	public void setExplicityIdentitiyDNs(ExplicitIdentityDNsPojo explicityIdentitiyDNs) {
		this.explicityIdentitiyDNs = explicityIdentitiyDNs;
	}

	public void setRoleDNs(RoleDNsPojo roleDNs) {
		this.roleDNs = roleDNs;
	}

}
