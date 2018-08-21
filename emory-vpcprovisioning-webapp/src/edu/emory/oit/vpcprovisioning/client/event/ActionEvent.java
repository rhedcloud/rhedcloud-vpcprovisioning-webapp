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

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CounterMeasurePojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;
import edu.emory.oit.vpcprovisioning.shared.QueryFilter;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;


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
	private CidrPojo cidr;
	private CidrAssignmentSummaryPojo cidrAssignmentSummary;
	private CidrAssignmentPojo cidrAssignment;
	private AccountPojo account;
	private VpcPojo vpc;
	private VpcpPojo vpcp;
	private ElasticIpPojo elasticIp;
	private ElasticIpAssignmentPojo elasticIpAssignment;
	private ElasticIpAssignmentSummaryPojo elasticIpAssignmentSummary;
	private AWSServicePojo awsService;
	private UserNotificationPojo notification;
	private FirewallRulePojo firewallRule;
	private FirewallExceptionRequestPojo firewallExceptionRequest;
	private CidrSummaryPojo cidrSummary;
	ServiceSecurityAssessmentPojo securityAssessment;
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
	private Place nextPlace;

	public CidrPojo getCidr() {
		return cidr;
	}
	
	public CidrAssignmentPojo getCidrAssignment() {
		return cidrAssignment;
	}

	public static void fire(EventBus eventBus, String sourceName) {
		GWT.log("Firing event: " + sourceName);
		GWT.log("ActionEvent: EventBus passed in is is: " + eventBus);
		eventBus.fireEventFromSource(new ActionEvent(), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrAssignmentPojo cidrAssignment, Place nextPlace) {
		eventBus.fireEventFromSource(new ActionEvent(cidrAssignment, nextPlace), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrPojo cidr, CidrAssignmentPojo cidrAssignment, Place nextPlace) {
		eventBus.fireEventFromSource(new ActionEvent(cidr, cidrAssignment, nextPlace), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, IncidentPojo incident) {
		eventBus.fireEventFromSource(new ActionEvent(incident), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrPojo cidr) {
		eventBus.fireEventFromSource(new ActionEvent(cidr), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, CidrSummaryPojo cidrSummary) {
		eventBus.fireEventFromSource(new ActionEvent(cidrSummary), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ElasticIpPojo elasticIp) {
		eventBus.fireEventFromSource(new ActionEvent(elasticIp), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, VpcPojo vpc, ElasticIpAssignmentPojo elasticIpAssignment) {
		eventBus.fireEventFromSource(new ActionEvent(vpc, elasticIpAssignment), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, ElasticIpPojo elasticIp, ElasticIpAssignmentPojo elasticIpAssignment, Place nextPlace) {
		eventBus.fireEventFromSource(new ActionEvent(elasticIp, elasticIpAssignment, nextPlace), sourceName);
	}


   	public static void fire(EventBus eventBus, String sourceName, CidrPojo cidr, CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		eventBus.fireEventFromSource(new ActionEvent(cidr, cidrAssignmentSummary), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, CidrAssignmentPojo cidrAssignment) {
		eventBus.fireEventFromSource(new ActionEvent(cidrAssignment), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AccountPojo account) {
		eventBus.fireEventFromSource(new ActionEvent(account), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service) {
		eventBus.fireEventFromSource(new ActionEvent(service), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, VpcPojo vpc) {
		eventBus.fireEventFromSource(new ActionEvent(vpc), sourceName);
	}
	
	public static void fire(EventBus eventBus, String sourceName, VpcpPojo vpcp) {
		eventBus.fireEventFromSource(new ActionEvent(vpcp), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, UserNotificationPojo notification) {
		eventBus.fireEventFromSource(new ActionEvent(notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, SecurityRiskDetectionPojo srd, UserNotificationPojo notification) {
		eventBus.fireEventFromSource(new ActionEvent(srd, notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, SecurityRiskDetectionPojo srd, AccountNotificationPojo notification) {
		eventBus.fireEventFromSource(new ActionEvent(srd, notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, FirewallRulePojo rule) {
		eventBus.fireEventFromSource(new ActionEvent(rule), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, FirewallExceptionRequestPojo rule) {
		eventBus.fireEventFromSource(new ActionEvent(rule), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, FirewallExceptionRequestPojo rule, VpcPojo vpc) {
		eventBus.fireEventFromSource(new ActionEvent(rule, vpc), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, FirewallRulePojo rule, VpcPojo vpc) {
		eventBus.fireEventFromSource(new ActionEvent(rule, vpc), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ServiceSecurityAssessmentPojo m) {
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, SecurityRiskPojo m) {
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ServiceControlPojo m) {
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ServiceGuidelinePojo m) {
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, ServiceTestPlanPojo m) {
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AccountNotificationPojo m) {
		eventBus.fireEventFromSource(new ActionEvent(m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AccountPojo account,
			AccountNotificationPojo notification) {

		eventBus.fireEventFromSource(new ActionEvent(account, notification), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, QueryFilter filter) {
		eventBus.fireEventFromSource(new ActionEvent(filter), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName,
			ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo securityRisk,
			CounterMeasurePojo object) {
		
		eventBus.fireEventFromSource(new ActionEvent(assessment, securityRisk, object), sourceName);
	}
	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo securityAssessment2) {
		
		eventBus.fireEventFromSource(new ActionEvent(service, securityAssessment2), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo m) {
		
		eventBus.fireEventFromSource(new ActionEvent(service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, ServiceControlPojo m) {
		
		eventBus.fireEventFromSource(new ActionEvent(service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo m) {
		
		eventBus.fireEventFromSource(new ActionEvent(service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, AWSServicePojo service,
			ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo m) {
		
		eventBus.fireEventFromSource(new ActionEvent(service, assessment, m), sourceName);
	}

	public static void fire(EventBus eventBus, String sourceName, TermsOfUseAgreementPojo toua) {
		eventBus.fireEventFromSource(new ActionEvent(toua), sourceName);
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

	public ActionEvent(FirewallExceptionRequestPojo rule, VpcPojo vpc) {
		this.firewallExceptionRequest = rule;
		this.vpc = vpc;
	}

	public ActionEvent(FirewallExceptionRequestPojo rule) {
		this.firewallExceptionRequest = rule;
	}

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
	
	public ActionEvent(VpcPojo vpc) {
		this.vpc = vpc;
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

	public ActionEvent(FirewallExceptionRequestPojo rule, Place nextPlace) {
		this.firewallExceptionRequest = rule;
		this.setNextPlace(nextPlace);
	}

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

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo securityAssessment2) {
		this.awsService = service;
		this.securityAssessment = securityAssessment2;
	}

	public ActionEvent(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo m) {
		this.awsService = service;
		this.securityAssessment = assessment;
		this.securityRisk = m;
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

	public FirewallExceptionRequestPojo getFirewallExceptionRequest() {
		return firewallExceptionRequest;
	}

	public void setFirewallExceptionRequest(FirewallExceptionRequestPojo firewallExceptionRequest) {
		this.firewallExceptionRequest = firewallExceptionRequest;
	}

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
}
