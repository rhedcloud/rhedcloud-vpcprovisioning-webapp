/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package edu.emory.oit.vpcprovisioning.client.event;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CounterMeasurePojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionAddRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRemoveRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;
import edu.emory.oit.vpcprovisioning.shared.QueryFilter;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;


/**
 * Fired when the user wants the app to do something. Action events are fired
 * with a string name, and handlers monitor the event bus for them based on that
 * name.
 */
public class ActionEvent extends Event<ActionEvent.Handler> {

	/**
	 * Implemented by objects that handle {@link ActionEvent}.
	 */
	public interface Handler {
		void onAction(ActionEvent event);
	}

	/**
	 * The event type.
	 */
	private static final Type<ActionEvent.Handler> TYPE = new Type<ActionEvent.Handler>();
	private UserAccountPojo userLoggedIn;
	private CidrPojo cidr;
	private CidrAssignmentSummaryPojo cidrAssignmentSummary;
	private CidrAssignmentPojo cidrAssignment;
	private AccountPojo account;
	private VpcPojo vpc;
	private VpcpPojo vpcp;
	private VpcpSummaryPojo vpcpSummary;
	private ElasticIpPojo elasticIp;
	private ElasticIpAssignmentPojo elasticIpAssignment;
	private ElasticIpAssignmentSummaryPojo elasticIpAssignmentSummary;
	private AWSServicePojo awsService;
	private UserNotificationPojo notification;
	private FirewallRulePojo firewallRule;
	private CidrSummaryPojo cidrSummary;
	private ServiceSecurityAssessmentPojo securityAssessment;
	private SecurityRiskPojo securityRisk;
	private CounterMeasurePojo counterMeasure;
	private ServiceControlPojo serviceControl;
	private ServiceGuidelinePojo serviceGuideline;
	private ServiceTestPlanPojo testPlan;
	private AccountNotificationPojo accountNotification;
	private SecurityRiskDetectionPojo srd;
	private QueryFilter filter;
	private TermsOfUseAgreementPojo termsOfUseAgreement;
	private IncidentPojo incident;
	private StaticNatProvisioningSummaryPojo staticNatProvisioningSummary;
	private StaticNatProvisioningPojo staticNatProvisioning;
	private StaticNatDeprovisioningPojo staticNatDeprovisioning;
	private VpnConnectionProfilePojo vpnConnectionProfile;
	private VpnConnectionProfileAssignmentPojo vpnConnectonProfileAssignment;
	private VpnConnectionProfileSummaryPojo vpnConnectionProfileSummary;
	private VpnConnectionProvisioningSummaryPojo vpncpSummary;
	private VpnConnectionProvisioningPojo vpncp;
	private VpnConnectionDeprovisioningPojo vpncdp;
	private VpnConnectionRequisitionPojo vpncRequisition;
	private FirewallExceptionAddRequestPojo fwea_request;
	private FirewallExceptionRemoveRequestPojo fwer_request;
	private FirewallExceptionRequestSummaryPojo fwer_summary;
	private Place nextPlace;
	private boolean firewallExceptionAddRequest;
	private List<AWSServicePojo> servicesToAssess = new java.util.ArrayList<AWSServicePojo>();
	private boolean newSecurityRiskWindow=false;
	private ResourceTaggingProfilePojo resourceTaggingProfile;
	private AccountProvisioningSummaryPojo accountProvisioningSummary;
	private AccountDeprovisioningRequisitionPojo acctDeprovisioningRequisition;
	private boolean showBadFinAcctsHTML;
	private Widget actionSourceWidget;
	private MaintainSecurityRiskView maintainSecurityRiskView;
	private RoleProvisioningSummaryPojo roleProvisioningSummary;

	public ResourceTaggingProfilePojo getResourceTaggingProfile() {
		return resourceTaggingProfile;
	}

	public void setResourceTaggingProfile(ResourceTaggingProfilePojo resourceTaggingProfile) {
		this.resourceTaggingProfile = resourceTaggingProfile;
	}

	public CidrPojo getCidr() {
		return cidr;
	}
	
	public CidrAssignmentPojo getCidrAssignment() {
		return cidrAssignment;
	}

	public static void fire(EventBus eventBus, String sourceName) {
		if (eventBus == null) return;
		GWT.log("Firing event: " + sourceName);
		GWT.log("ActionEvent: EventBus passed in is is: " + eventBus);
		eventBus.fireEventFromSource(new ActionEvent(), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrAssignmentPojo cidrAssignment, Place nextPlace) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(cidrAssignment, nextPlace), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrPojo cidr, CidrAssignmentPojo cidrAssignment, Place nextPlace) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(cidr, cidrAssignment, nextPlace), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, IncidentPojo incident) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(incident), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrPojo cidr) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(cidr), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrSummaryPojo cidrSummary) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(cidrSummary), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ElasticIpPojo elasticIp) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(elasticIp), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, VpcPojo vpc, ElasticIpAssignmentPojo elasticIpAssignment) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpc, elasticIpAssignment), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, ElasticIpPojo elasticIp, ElasticIpAssignmentPojo elasticIpAssignment, Place nextPlace) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(elasticIp, elasticIpAssignment, nextPlace), sourceName);
	}


   	public static void fire(EventBus eventBus, String sourceName, CidrPojo cidr, CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(cidr, cidrAssignmentSummary), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, CidrAssignmentPojo cidrAssignment) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(cidrAssignment), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ResourceTaggingProfilePojo rtp) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(rtp), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AccountPojo account) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(account), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpcPojo vpc) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpc), sourceName);
	}
	
	public static void fire(EventBus eventBus, String sourceName, VpcPojo vpc2, boolean b) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpc2, b), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, VpcPojo vpc, FirewallRulePojo rule,
			boolean b) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpc, rule, b), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpcpPojo vpcp) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpcp), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpcpSummaryPojo vpcpSummary) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpcpSummary), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, StaticNatProvisioningSummaryPojo snp) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(snp), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, StaticNatProvisioningPojo snp) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(snp), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, StaticNatDeprovisioningPojo sndp) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(sndp), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, UserNotificationPojo notification) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, SecurityRiskDetectionPojo srd, UserNotificationPojo notification) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(srd, notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, SecurityRiskDetectionPojo srd, AccountNotificationPojo notification) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(srd, notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, FirewallRulePojo rule) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(rule), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName,
			FirewallExceptionAddRequestPojo result, VpcPojo vpc2) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(result, vpc2), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName,
			FirewallExceptionRemoveRequestPojo result, VpcPojo vpc2) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(result, vpc2), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName,
			FirewallExceptionRequestSummaryPojo m, VpcPojo vpc) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m, vpc), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, FirewallRulePojo rule, VpcPojo vpc) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(rule, vpc), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, FirewallRulePojo m, VpcPojo vpc2,
			boolean b) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m, vpc2, b), sourceName);
	}


	public static void fire(EventBus eventBus, String sourceName,
			List<AWSServicePojo> serviceList, ServiceSecurityAssessmentPojo assessment) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(serviceList, assessment), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, ServiceSecurityAssessmentPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, SecurityRiskPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ServiceControlPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ServiceGuidelinePojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ServiceTestPlanPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AccountNotificationPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AccountPojo account,
			AccountNotificationPojo notification) {

		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(account, notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, QueryFilter filter) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(filter), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo securityAssessment, SecurityRiskPojo risk) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service, securityAssessment, risk), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo securityAssessment, SecurityRiskPojo risk, MaintainSecurityRiskView view) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service, securityAssessment, risk, view), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName,
			ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo securityRisk,
			CounterMeasurePojo object) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(assessment, securityRisk, object), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo securityAssessment2) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service, securityAssessment2), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo securityAssessment2, MaintainSecurityRiskView view) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service, securityAssessment2, view), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, boolean newWindow, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo m) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(newWindow, service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, ServiceControlPojo m) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, boolean newWindow, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk, ServiceControlPojo m) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(newWindow, service, assessment, risk, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo m) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo m) {
		
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, TermsOfUseAgreementPojo toua) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(toua), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, UserAccountPojo userLoggedIn) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(userLoggedIn), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpnConnectionProfilePojo profile) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(profile), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName,
			VpnConnectionRequisitionPojo vpnConnectionRequisition, VpnConnectionProfileAssignmentPojo assignment) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpnConnectionRequisition, assignment), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpnConnectionProfileSummaryPojo profileSummary) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(profileSummary), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpnConnectionProfileAssignmentPojo profileAssignment) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(profileAssignment), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpnConnectionProvisioningPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpnConnectionProvisioningSummaryPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpnConnectionDeprovisioningPojo vpncdp) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(vpncdp), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, List<AWSServicePojo> serviceList) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(serviceList), sourceName);
	}


	public static HandlerRegistration register(EventBus eventBus, String sourceName, Handler handler) {
		return eventBus.addHandlerToSource(TYPE, sourceName, handler);
	}

	/**
	 * Protected contructor to encourage the use of
	 * {@link #fire(EventBus, String)}.
	 */
	protected ActionEvent() {
	}

	public ActionEvent(FirewallRulePojo rule, VpcPojo vpc) {
		this.firewallRule = rule;
		this.vpc = vpc;
	}
	public ActionEvent(FirewallRulePojo m, VpcPojo vpc2, boolean b) {
		this.firewallRule = m;
		this.vpc = vpc2;
		this.firewallExceptionAddRequest = b;
	}


//	public ActionEvent(FirewallExceptionRequestPojo rule, VpcPojo vpc) {
//		this.firewallExceptionRequest = rule;
//		this.vpc = vpc;
//	}

	public ActionEvent(FirewallExceptionRequestSummaryPojo m, VpcPojo vpc2) {
		this.fwea_request = m.getAddRequest();
		this.fwer_request = m.getRemoveRequest();
		this.fwer_summary = m;
		this.vpc = vpc2;
	}

//	public ActionEvent(FirewallExceptionRequestPojo rule) {
//		this.firewallExceptionRequest = rule;
//	}

	public ActionEvent(FirewallRulePojo rule) {
		this.firewallRule = rule;
	}

	public ActionEvent(UserNotificationPojo notification) {
		this.notification = notification;
	}

	public ActionEvent(AWSServicePojo service) {
		this.awsService = service;
	}

	public ActionEvent(VpcpPojo vpcp) {
		this.vpcp = vpcp;
	}
	
	public ActionEvent(VpcpSummaryPojo vpcpSummary) {
		this.vpcpSummary = vpcpSummary;
	}
	
	public ActionEvent(VpcPojo vpc) {
		this.vpc = vpc;
	}
	
	public ActionEvent(ResourceTaggingProfilePojo rtp) {
		this.resourceTaggingProfile = rtp;
	}

	public ActionEvent(AccountPojo account) {
		this.account = account;
	}
	
	public ActionEvent(ElasticIpPojo pojo) {
		this.elasticIp = pojo;
	}
	
	public ActionEvent(VpcPojo vpc, ElasticIpAssignmentPojo pojo) {
		this.vpc = vpc;
		this.elasticIpAssignment = pojo;
	}
	
	public ActionEvent(CidrSummaryPojo cidrSummary) {
		this.cidrSummary = cidrSummary;
		if (cidrSummary.getCidr() != null) {
			this.cidr = cidrSummary.getCidr();
		}
		else {
			this.cidr = cidrSummary.getAssignmentSummary().getCidrAssignment().getCidr();
		}
	}
	
	public ActionEvent(CidrPojo cidr) {
		this.cidr = cidr;
	}
	
	public ActionEvent(CidrPojo cidr, CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		this.cidrAssignmentSummary = cidrAssignmentSummary;
		this.cidr = cidr;
	}
	
	public ActionEvent(CidrAssignmentPojo cidrAssignment) {
		this.cidrAssignment = cidrAssignment;
	}
	
	public ActionEvent(AccountPojo account, Place nextPlace) {
		this.account = account;
		this.setNextPlace(nextPlace);
	}

	public ActionEvent(ResourceTaggingProfilePojo rtp, Place nextPlace) {
		this.resourceTaggingProfile = rtp;
		this.setNextPlace(nextPlace);
	}

	public ActionEvent(CidrPojo cidr, CidrAssignmentSummaryPojo cidrAssignmentSummary, Place nextPlace) {
		this.cidrAssignmentSummary = cidrAssignmentSummary;
		this.cidr = cidr;
		this.setNextPlace(nextPlace);
	}

	public ActionEvent(CidrAssignmentPojo cidrAssignment, Place nextPlace) {
		this.cidrAssignment = cidrAssignment;
		this.setNextPlace(nextPlace);
	}

	public ActionEvent(CidrPojo cidr, CidrAssignmentPojo cidrAssignment, Place nextPlace) {
		this.cidr = cidr;
		this.cidrAssignment = cidrAssignment;
		this.setNextPlace(nextPlace);
	}

	public ActionEvent(ElasticIpPojo elasticIp, ElasticIpAssignmentPojo elasticIpAssignment, Place nextPlace) {
		this.elasticIp = elasticIp;
		this.elasticIpAssignment = elasticIpAssignment;
		this.setNextPlace(nextPlace);
	}

	public ActionEvent(ElasticIpAssignmentSummaryPojo summary, Place nextPlace) {
		this.elasticIpAssignmentSummary = summary;
		this.setNextPlace(nextPlace);
	}

	public ActionEvent(FirewallRulePojo rule, Place nextPlace) {
		this.firewallRule = rule;
		this.setNextPlace(nextPlace);
	}

//	public ActionEvent(FirewallExceptionRequestPojo rule, Place nextPlace) {
//		this.firewallExceptionRequest = rule;
//		this.setNextPlace(nextPlace);
//	}

	public ActionEvent(ServiceSecurityAssessmentPojo m) {
		this.securityAssessment = m;
	}

	public ActionEvent(SecurityRiskPojo m) {
		this.securityRisk = m;
	}

	public ActionEvent(ServiceControlPojo m) {
		this.serviceControl = m;
	}

	public ActionEvent(ServiceGuidelinePojo m) {
		this.serviceGuideline = m;
	}

	public ActionEvent(ServiceTestPlanPojo m) {
		this.testPlan = m;
	}

	public ActionEvent(AccountNotificationPojo m) {
		this.accountNotification = m;
	}

	public ActionEvent(AccountPojo account2, AccountNotificationPojo notification2) {
		this.account = account2;
		this.accountNotification = notification2;
	}

	public ActionEvent(QueryFilter filter) {
		this.filter = filter;
	}

	public ActionEvent(ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo securityRisk2,
			CounterMeasurePojo object) {

		this.securityAssessment = assessment;
		this.securityRisk = securityRisk2;
		this.counterMeasure = object;
	}

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo securityAssessment, SecurityRiskPojo risk) {
		this.awsService = service;
		this.securityAssessment = securityAssessment;
		this.securityRisk = risk;
	}

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo securityAssessment, SecurityRiskPojo risk, MaintainSecurityRiskView view) {
		this.awsService = service;
		this.securityAssessment = securityAssessment;
		this.securityRisk = risk;
		this.maintainSecurityRiskView = view;
	}

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo securityAssessment2) {
		this.awsService = service;
		this.securityAssessment = securityAssessment2;
	}
	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo securityAssessment2, MaintainSecurityRiskView view) {
		this.awsService = service;
		this.securityAssessment = securityAssessment2;
		this.maintainSecurityRiskView = view;
	}

	public ActionEvent(boolean newWindow, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo m) {
		this.awsService = service;
		this.securityAssessment = assessment;
		this.securityRisk = m;
		this.newSecurityRiskWindow = newWindow;
	}

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceControlPojo m) {
		this.awsService = service;
		this.securityAssessment = assessment;
		this.serviceControl = m;
	}

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo m) {
		this.awsService = service;
		this.securityAssessment = assessment;
		this.serviceGuideline = m;
	}

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo m) {
		this.awsService = service;
		this.securityAssessment = assessment;
		this.testPlan = m;
	}

	public ActionEvent(SecurityRiskDetectionPojo srd2, UserNotificationPojo notification2) {
		this.srd = srd2;
		this.notification = notification2;
	}

	public ActionEvent(SecurityRiskDetectionPojo srd2, AccountNotificationPojo notification2) {
		this.srd = srd2;
		this.accountNotification = notification2;
	}

	public ActionEvent(TermsOfUseAgreementPojo toua) {
		this.termsOfUseAgreement = toua;
	}

	public ActionEvent(IncidentPojo incident2) {
		this.incident = incident2;
	}

	public ActionEvent(StaticNatProvisioningPojo snp) {
		this.staticNatProvisioning = snp;
	}

	public ActionEvent(StaticNatDeprovisioningPojo sndp) {
		this.staticNatDeprovisioning = sndp;
	}

	public ActionEvent(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public ActionEvent(VpnConnectionProfilePojo profile) {
		this.vpnConnectionProfile = profile;
	}

	public ActionEvent(VpnConnectionProfileAssignmentPojo profileAssignment) {
		this.vpnConnectonProfileAssignment = profileAssignment;
	}

	public ActionEvent(StaticNatProvisioningSummaryPojo snp) {
		this.staticNatProvisioningSummary = snp;
	}

	public ActionEvent(VpnConnectionProfileSummaryPojo profileSummary) {
		this.vpnConnectionProfileSummary = profileSummary;
		this.vpnConnectionProfile = profileSummary.getProfile();
		this.vpnConnectonProfileAssignment = profileSummary.getAssignment();
	}

	public ActionEvent(VpnConnectionProvisioningPojo m) {
		this.vpncpSummary = new VpnConnectionProvisioningSummaryPojo();
		this.vpncpSummary.setProvisioning(m);
		this.vpncp = m;
	}
	public ActionEvent(VpnConnectionProvisioningSummaryPojo m) {
		this.vpncpSummary = m;
		this.vpncp = m.getProvisioning();
		this.vpncdp = m.getDeprovisioning();
	}

	public ActionEvent(VpnConnectionDeprovisioningPojo vpncdp2) {
		this.vpncpSummary = new VpnConnectionProvisioningSummaryPojo();
		this.vpncpSummary.setDeprovisioning(vpncdp2);
		this.vpncdp = vpncdp2;
	}

	public ActionEvent(FirewallExceptionAddRequestPojo result, VpcPojo vpc2) {
		this.fwea_request = result;
		this.vpc = vpc2;
	}

	public ActionEvent(FirewallExceptionRemoveRequestPojo result, VpcPojo vpc2) {
		this.fwer_request = result;
		this.vpc = vpc2;
	}

	public ActionEvent(VpcPojo vpc2, boolean b) {
		this.vpc = vpc2;
		this.firewallExceptionAddRequest = b;
	}

	public ActionEvent(VpcPojo vpc2, FirewallRulePojo rule, boolean b) {
		this.vpc = vpc2;
		this.firewallRule = rule;
		this.firewallExceptionAddRequest = b;
	}

	public ActionEvent(VpnConnectionRequisitionPojo vpnConnectionRequisition, VpnConnectionProfileAssignmentPojo assignment) {
		this.vpnConnectonProfileAssignment = assignment;
		this.vpncRequisition = vpnConnectionRequisition;
	}

	public ActionEvent(List<AWSServicePojo> serviceList) {
		servicesToAssess = serviceList;
	}

	public ActionEvent(List<AWSServicePojo> serviceList, ServiceSecurityAssessmentPojo assessment) {
		servicesToAssess = serviceList;
		securityAssessment = assessment;
	}

	public ActionEvent(boolean newWindow, AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk,
			ServiceControlPojo m) {
		
		this.awsService = service;
		this.securityAssessment = assessment;
		this.securityRisk = risk;
		this.serviceControl = m;
		this.newSecurityRiskWindow = newWindow;
	}

	public ActionEvent(AccountProvisioningSummaryPojo summary) {
		this.accountProvisioningSummary = summary;
	}

	public ActionEvent(Widget actionSourceWidget, AccountDeprovisioningRequisitionPojo requisition, AccountPojo account) {
		this.acctDeprovisioningRequisition = requisition;
		this.account = account;
		this.actionSourceWidget = actionSourceWidget;
	}

	public ActionEvent(AccountDeprovisioningRequisitionPojo requisition, AccountPojo account) {
		this.acctDeprovisioningRequisition = requisition;
		this.account = account;
	}

	public ActionEvent(UserAccountPojo userLoggedIn2, boolean showBadFinAcctsHTML) {
		this.userLoggedIn = userLoggedIn2;
		this.setShowBadFinAcctsHTML(showBadFinAcctsHTML);
	}

	public ActionEvent(RoleProvisioningSummaryPojo m) {
		this.roleProvisioningSummary = m;
	}

	@Override
	public final Type<ActionEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ActionEvent.Handler handler) {
		handler.onAction(this);
	}

	public Place getNextPlace() {
		return nextPlace;
	}

	public void setNextPlace(Place nextPlace) {
		this.nextPlace = nextPlace;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

	public VpcpPojo getVpcp() {
		return vpcp;
	}

	public void setVpcp(VpcpPojo vpcp) {
		this.vpcp = vpcp;
	}

	public CidrAssignmentSummaryPojo getCidrAssignmentSummary() {
		return cidrAssignmentSummary;
	}

	public void setCidrAssignmentSummary(CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		this.cidrAssignmentSummary = cidrAssignmentSummary;
	}

	public ElasticIpPojo getElasticIp() {
		return elasticIp;
	}

	public void setElasticIp(ElasticIpPojo elasticIp) {
		this.elasticIp = elasticIp;
	}

	public ElasticIpAssignmentPojo getElasticIpAssignment() {
		return elasticIpAssignment;
	}

	public void setElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment) {
		this.elasticIpAssignment = elasticIpAssignment;
	}

	public ElasticIpAssignmentSummaryPojo getElasticIpAssignmentSummary() {
		return elasticIpAssignmentSummary;
	}

	public void setElasticIpAssignmentSummary(ElasticIpAssignmentSummaryPojo elasticIpAssignmentSummary) {
		this.elasticIpAssignmentSummary = elasticIpAssignmentSummary;
	}

	public AWSServicePojo getAwsService() {
		return awsService;
	}

	public void setAwsService(AWSServicePojo awsService) {
		this.awsService = awsService;
	}

	public UserNotificationPojo getNotification() {
		return notification;
	}

	public void setNotification(UserNotificationPojo notification) {
		this.notification = notification;
	}

	public FirewallRulePojo getFirewallRule() {
		return firewallRule;
	}

	public void setFirewallRule(FirewallRulePojo firewallRule) {
		this.firewallRule = firewallRule;
	}

	public CidrSummaryPojo getCidrSummary() {
		return cidrSummary;
	}

	public void setCidrSummary(CidrSummaryPojo cidrSummary) {
		this.cidrSummary = cidrSummary;
	}

//	public FirewallExceptionRequestPojo getFirewallExceptionRequest() {
//		return firewallExceptionRequest;
//	}
//
//	public void setFirewallExceptionRequest(FirewallExceptionRequestPojo firewallExceptionRequest) {
//		this.firewallExceptionRequest = firewallExceptionRequest;
//	}

	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return securityAssessment;
	}

	public void setSecurityAssessment(ServiceSecurityAssessmentPojo securityAssessment) {
		this.securityAssessment = securityAssessment;
	}

	public SecurityRiskPojo getSecurityRisk() {
		return securityRisk;
	}

	public void setSecurityRisk(SecurityRiskPojo securityRisk) {
		this.securityRisk = securityRisk;
	}

	public ServiceControlPojo getServiceControl() {
		return serviceControl;
	}

	public void setServiceControl(ServiceControlPojo serviceControl) {
		this.serviceControl = serviceControl;
	}

	public ServiceGuidelinePojo getServiceGuideline() {
		return serviceGuideline;
	}

	public void setServiceGuideline(ServiceGuidelinePojo serviceGuideline) {
		this.serviceGuideline = serviceGuideline;
	}

	public ServiceTestPlanPojo getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(ServiceTestPlanPojo testPlan) {
		this.testPlan = testPlan;
	}

	public AccountNotificationPojo getAccountNotification() {
		return accountNotification;
	}

	public void setAccountNotification(AccountNotificationPojo accountNotification) {
		this.accountNotification = accountNotification;
	}

	public QueryFilter getFilter() {
		return filter;
	}

	public void setFilter(QueryFilter filter) {
		this.filter = filter;
	}

	public CounterMeasurePojo getCounterMeasure() {
		return counterMeasure;
	}

	public void setCounterMeasure(CounterMeasurePojo counterMeasure) {
		this.counterMeasure = counterMeasure;
	}

	public SecurityRiskDetectionPojo getSrd() {
		return srd;
	}

	public void setSrd(SecurityRiskDetectionPojo srd) {
		this.srd = srd;
	}

	public TermsOfUseAgreementPojo getTermsOfUseAgreement() {
		return termsOfUseAgreement;
	}

	public void setTermsOfUseAgreement(TermsOfUseAgreementPojo termsOfUseAgreement) {
		this.termsOfUseAgreement = termsOfUseAgreement;
	}

	public IncidentPojo getIncident() {
		return incident;
	}

	public void setIncident(IncidentPojo incident) {
		this.incident = incident;
	}

	public StaticNatProvisioningPojo getStaticNatProvisioning() {
		return staticNatProvisioning;
	}

	public void setStaticNatProvisioning(StaticNatProvisioningPojo staticNatProvisioning) {
		this.staticNatProvisioning = staticNatProvisioning;
	}

	public StaticNatDeprovisioningPojo getStaticNatDeprovisioning() {
		return staticNatDeprovisioning;
	}

	public void setStaticNatDeprovisioning(StaticNatDeprovisioningPojo staticNatDeprovisioning) {
		this.staticNatDeprovisioning = staticNatDeprovisioning;
	}

	public UserAccountPojo getUserLoggedIn() {
		return this.userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public VpnConnectionProfilePojo getVpnConnectionProfile() {
		return vpnConnectionProfile;
	}

	public void setVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile) {
		this.vpnConnectionProfile = vpnConnectionProfile;
	}

	public VpnConnectionProfileAssignmentPojo getVpnConnectonProfileAssignment() {
		return vpnConnectonProfileAssignment;
	}

	public void setVpnConnectonProfileAssignment(VpnConnectionProfileAssignmentPojo vpnConnectonProfileAssignment) {
		this.vpnConnectonProfileAssignment = vpnConnectonProfileAssignment;
	}

	public StaticNatProvisioningSummaryPojo getStaticNatProvisioningSummary() {
		return staticNatProvisioningSummary;
	}

	public void setStaticNatProvisioningSummary(StaticNatProvisioningSummaryPojo staticNatProvisioningSummary) {
		this.staticNatProvisioningSummary = staticNatProvisioningSummary;
	}

	public VpnConnectionProfileSummaryPojo getVpnConnectionProfileSummary() {
		return vpnConnectionProfileSummary;
	}

	public void setVpnConnectionProfileSummary(VpnConnectionProfileSummaryPojo vpnConnectionProfileSummary) {
		this.vpnConnectionProfileSummary = vpnConnectionProfileSummary;
	}

	public VpnConnectionProvisioningPojo getVpncp() {
		return vpncp;
	}

	public void setVpncp(VpnConnectionProvisioningPojo vpncp) {
		this.vpncp = vpncp;
	}

	public FirewallExceptionRequestSummaryPojo getFwer_summary() {
		return fwer_summary;
	}

	public void setFwer_summary(FirewallExceptionRequestSummaryPojo fwer_summary) {
		this.fwer_summary = fwer_summary;
	}

	public FirewallExceptionAddRequestPojo getFwea_request() {
		return fwea_request;
	}

	public void setFwea_request(FirewallExceptionAddRequestPojo fwea_request) {
		this.fwea_request = fwea_request;
	}

	public FirewallExceptionRemoveRequestPojo getFwer_request() {
		return fwer_request;
	}

	public void setFwer_request(FirewallExceptionRemoveRequestPojo fwer_request) {
		this.fwer_request = fwer_request;
	}

	public boolean isFirewallExceptionAddRequest() {
		return firewallExceptionAddRequest;
	}

	public void setFirewallExceptionAddRequest(boolean firewallExceptionAddRequest) {
		this.firewallExceptionAddRequest = firewallExceptionAddRequest;
	}

	public VpnConnectionDeprovisioningPojo getVpncdp() {
		return vpncdp;
	}

	public void setVpncdp(VpnConnectionDeprovisioningPojo vpncdp) {
		this.vpncdp = vpncdp;
	}

	public VpnConnectionProvisioningSummaryPojo getVpncpSummary() {
		return vpncpSummary;
	}

	public void setVpncpSummary(VpnConnectionProvisioningSummaryPojo vpncpSummary) {
		this.vpncpSummary = vpncpSummary;
	}

	public VpnConnectionRequisitionPojo getVpncRequisition() {
		return vpncRequisition;
	}

	public void setVpncRequisition(VpnConnectionRequisitionPojo vpncRequisition) {
		this.vpncRequisition = vpncRequisition;
	}

	public List<AWSServicePojo> getServicesToAssess() {
		return servicesToAssess;
	}

	public void setServicesToAssess(List<AWSServicePojo> servicesToAssess) {
		this.servicesToAssess = servicesToAssess;
	}

	public boolean isNewSecurityRiskWindow() {
		return newSecurityRiskWindow;
	}

	public void setNewSecurityRiskWindow(boolean newSecurityRiskWindow) {
		this.newSecurityRiskWindow = newSecurityRiskWindow;
	}

	public VpcpSummaryPojo getVpcpSummary() {
		return vpcpSummary;
	}

	public void setVpcpSummary(VpcpSummaryPojo vpcpSummary) {
		this.vpcpSummary = vpcpSummary;
	}

	public static void fire(EventBus eventBus, String sourceName, AccountProvisioningSummaryPojo summary) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(summary), sourceName);
	}

	public AccountProvisioningSummaryPojo getAccountProvisioningSummary() {
		return accountProvisioningSummary;
	}

	public void setAccountProvisioningSummary(AccountProvisioningSummaryPojo accountProvisioningSummary) {
		this.accountProvisioningSummary = accountProvisioningSummary;
	}

	public static void fire(EventBus eventBus, String sourceName,
			Widget actionSourceWidget, AccountDeprovisioningRequisitionPojo requisition, AccountPojo account) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(actionSourceWidget, requisition, account), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName,
			AccountDeprovisioningRequisitionPojo requisition, AccountPojo account) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(requisition, account), sourceName);
	}
	
	public AccountDeprovisioningRequisitionPojo getAcctDeprovisioningRequisition() {
		return acctDeprovisioningRequisition;
	}

	public void setAcctDeprovisioningRequisition(AccountDeprovisioningRequisitionPojo acctDeprovisioningRequisition) {
		this.acctDeprovisioningRequisition = acctDeprovisioningRequisition;
	}

	public Widget getActionSourceWidget() {
		return actionSourceWidget;
	}

	public void setActionSourceWidget(Widget actionSourceWidget) {
		this.actionSourceWidget = actionSourceWidget;
	}

	public static void fire(EventBus eventBus, String goHomeFinancialAccounts, UserAccountPojo userLoggedIn2,
			boolean showBadFinAcctsHTML) {

		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(userLoggedIn2, showBadFinAcctsHTML), goHomeFinancialAccounts);
	}

	public boolean isShowBadFinAcctsHTML() {
		return showBadFinAcctsHTML;
	}

	public void setShowBadFinAcctsHTML(boolean showBadFinAcctsHTML) {
		this.showBadFinAcctsHTML = showBadFinAcctsHTML;
	}

	public MaintainSecurityRiskView getMaintainSecurityRiskView() {
		return maintainSecurityRiskView;
	}

	public void setMaintainSecurityRiskView(MaintainSecurityRiskView maintainSecurityRiskView) {
		this.maintainSecurityRiskView = maintainSecurityRiskView;
	}

	public static void fire(EventBus eventBus, String showRoleProvisioningStatus, RoleProvisioningSummaryPojo m) {
		if (eventBus == null) return;
		eventBus.fireEventFromSource(new ActionEvent(m), showRoleProvisioningStatus);
	}

	public RoleProvisioningSummaryPojo getRoleProvisioningSummary() {
		return roleProvisioningSummary;
	}

	public void setRoleProvisioningSummary(RoleProvisioningSummaryPojo roleProvisioningSummary) {
		this.roleProvisioningSummary = roleProvisioningSummary;
	}

}
