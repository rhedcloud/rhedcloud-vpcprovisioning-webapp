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

/**
 * Names of the {@link ActionEvent}s used in this app.
 */
public interface ActionNames {
	final String MAINTAIN_VPCP = "maintainVpcp";
	final String GENERATE_VPCP = "generateVpcp";
	final String VPCP_EDITING_CANCELED = "vpcpEditingCanceled";
	final String GENERATE_VPC = "generateVpc";
	
	final String CREATE_FIREWALL_EXCEPTION_REQUEST = "createFirewallExceptionRequest";
	final String MAINTAIN_FIREWALL_EXCEPTION_REQUEST = "maintainFirewallExceptionRequest";
	final String MAINTAIN_NOTIFICATION = "viewNotification";
	final String CREATE_NOTIFICATION = "createNotification";
	final String MAINTAIN_SERVICE = "maintainSvc";
	final String CREATE_SERVICE = "createSvc";
	final String REGISTER_VPC = "registerVpc";
	final String MAINTAIN_VPC = "maintainVpc";
	final String CREATE_ACCOUNT = "createAccount";
	final String MAINTAIN_ACCOUNT = "maintainAccount";
	final String MAINTAIN_ACCOUNT_FROM_HOME = "maintainAccountFromHome";
	final String CREATE_CIDR = "createCidr";
	final String MAINTAIN_CIDR = "maintainCidr";
	final String MAINTAIN_CIDR_ASSIGNMENT = "maintainCidrAssignment";
	final String CREATE_CIDR_ASSIGNMENT = "createCidrAssignment";
	final String CREATE_CIDR_ASSIGNMENT_AFTER_VPC_REGISTRATION = "createCidrAssignmentAfterVpcReg";
	final String CREATE_ELASTIC_IP = "createElasticIp";
	final String MAINTAIN_ELASTIC_IP = "maintainElasticIp";
	final String CREATE_ELASTIC_IP_ASSIGNMENT = "createElasticIpAssignment";
	final String MAINTAIN_ELASTIC_IP_ASSIGNMENT = "maintainElasticIpAssignment";
	final String CREATE_RTP = "createRtp";
	final String MAINTAIN_RTP = "maintainRtp";

	final String FIREWALL_EXCEPTION_REQUEST_EDITING_CANCELED = "firewallExceptionRequestEditingCanceled";
	final String VPC_EDITING_CANCELED = "vpcEditingCanceled";
	final String VPC_REGISTRATION_CANCELED = "vpcRegistrationCanceled";
	final String ACCOUNT_EDITING_CANCELED = "accountEditingCanceled";
	final String CIDR_EDITING_CANCELED = "cidrEditingCanceled";
	final String CIDR_ASSIGNMENT_EDITING_CANCELED = "cidrAssignmentEditingCanceled";
	final String CIDR_ASSIGNMENT_EDITING_CANCELED_AFTER_VPC_REGISTRATION = "cidrAssignmentEditingCanceledAfterVpcReg";
	final String ELASTIC_IP_EDITING_CANCELED = "elasticIpEditingCanceled";
	final String ELASTIC_IP_ASSIGNMENT_EDITING_CANCELED = "elasticIpAssignmentEditingCanceled";
	final String SERVICE_EDITING_CANCELED = "svcEditingCanceled";
	final String NOTIFICATION_EDITING_CANCELED = "notificationEditingCanceled";
	final String RTP_EDITING_CANCELED = "rtpEditingCanceled";

	final String GO_HOME = "goHome";
	final String GO_HOME_FIREWALL_RULE = "goHomeFirewallRule";
	final String GO_BACK_FIREWALL_RULE = "goBackFirewallRule";
	final String GO_HOME_STATIC_NAT_PROVISIONING_SUMMARY = "goHomeSnps";
	final String GO_HOME_VPCP = "goHomeVpcp";
	final String GO_BACK_VPCP = "goBackVpcp";
	final String GO_HOME_VPC = "goHomeVpc";
	final String GO_BACK_VPC = "goBackVpc";
	final String GO_HOME_SERVICE = "goHomeService";
	final String GO_BACK_SERVICE = "goBackService";
	final String GO_HOME_ACCOUNT = "goHomeAccount";
	final String GO_BACK_ACCOUNT = "goBackAccount";
	final String GO_HOME_CIDR = "goHomeCidr";
	final String GO_BACK_CIDR = "goBackCidr";
	final String GO_HOME_CIDR_ASSIGNMENT = "goHomeCidrAssignment";
	final String GO_BACK_CIDR_ASSIGNMENT = "goBackCidrAssignment";
	final String GO_HOME_ELASTIC_IP = "goHomeElasticIp";
	final String GO_BACK_ELASTIC_IP = "goBackElasticIp";
	final String GO_HOME_ELASTIC_IP_ASSIGNMENT = "goHomeElasticIpAssignment";
	final String GO_BACK_ELASTIC_IP_ASSIGNMENT = "goBackElasticIpAssignment";
	final String GO_HOME_NOTIFICATION = "goHomeNotification";
	final String GO_HOME_RTP = "goHomeResourceTagging";

	final String VPC_CACHED = "vpcCached";
	final String ACCOUNT_CACHED = "accountCached";
	final String CIDR_CACHED = "cidrCached";
	final String CIDR_ASSIGNMENT_CACHED = "cidrAssignmentCached";
	final String ELASTIC_IP_CACHED = "elasticIpCached";
	final String ELASTIC_IP_ASSIGNMENT_CACHED = "elasticIpAssignmentCached";
	final String RTP_CACHED = "rtpCached";
	
	final String FIREWALL_EXCEPTION_REQUEST_DELETED = "firewallExceptionRequestDeleted";
	final String VPC_DELETED = "vpcDeleted";
	final String ACCOUNT_DELETED = "accountDeleted";
	final String CIDR_DELETED = "cidrDeleted";
	final String CIDR_ASSIGNMENT_DELETED = "cidrAssignmentDeleted";
	final String ELASTIC_IP_DELETED = "elasticIpDeleted";
	final String ELASTIC_IP_ASSIGNMENT_DELETED = "elasticIpAssignmentDeleted";
	final String RTP_DELETED = "rtpDeleted";
	
	final String FIREWALL_EXCEPTION_REQUEST_SAVED = "firewallExceptionRequestSaved";
	final String VPCP_GENERATED = "vpcpGenerated";
	final String VPCP_SAVED = "vpcpSaved";
	final String VPC_SAVED = "vpcSaved";
	final String ACCOUNT_SAVED = "accountSaved";
	final String CIDR_SAVED = "cidrSaved";
	final String CIDR_ASSIGNMENT_SAVED = "cidrAssignmentSaved";
	final String CIDR_ASSIGNMENT_SAVED_AFTER_VPC_REGISTRATION = "cidrAssignmentSavedAfterVpcReg";
	final String ELASTIC_IP_SAVED = "elasticIpSaved";
	final String ELASTIC_IP_ASSIGNMENT_SAVED = "elasticIpAssignmentSaved";
	final String SERVICE_SAVED = "svcSaved";
	final String NOTIFICATION_SAVED = "notificationSaved";
	final String RTP_SAVED = "rtpSaved";

	final String INAUTHENTIC_CLIENT = "inauthenticClient";

	final String CREATE_USER_PASSCODE = "createUserPasscode";
	final String CONFIRM_USER_PASSCODE = "confirmUserPasscode";
	final String CONFIRM_PASSCODE_CREATE_CASE = "confirmPasscodeCreateCase";
	final String CONFIRM_PASSCODE_CACHE_CASE = "confirmPasscodeCacheCase";
	final String PIN_CONFIRMED = "pinConfirmed";
	final String SHOW_VPCP_STATUS = "showVpcpStatus";
	final String SHOW_STATIC_NAT_STATUS = "showStaticNatStatus";
//	final String SHOW_STATIC_NAT_PROVISIONING_STATUS = "showSnpStatus";
//	final String SHOW_STATIC_NAT_DEPROVISIONING_STATUS = "showSndpStatus";
	final String SHOW_BILL_SUMMARY_FOR_ACCOUNT = "showBillSummaryForAccount";
	final String GO_HOME_CENTRAL_ADMIN = "goHomeCentralAdmin";
	
	final String MAINTAIN_SECURITY_ASSESSMENT = "maintainSecurityAssessment";
	final String CREATE_SECURITY_ASSESSMENT = "createSecurityAssessment";
	final String SECURITY_ASSESSMENT_EDITING_CANCELED = "securityAssessmentEditingCancelled";
	final String SECURITY_ASSESSMENT_SAVED = "securityAssessmentSaved";
	
	final String GO_HOME_SECURITY_RISK = "goHomeSecurityRisk";
	final String MAINTAIN_SECURITY_RISK = "maintainSecurityRisk";
	final String CREATE_SECURITY_RISK = "createSecurityRisk";
	final String SECURITY_RISK_EDITING_CANCELED = "securityRiskEditingCancelled";
	final String SECURITY_RISK_SAVED = "securityRiskSaved";

	final String GO_HOME_SERVICE_CONTROL = "goHomeServiceControl";
	final String MAINTAIN_SERVICE_CONTROL = "maintainServiceControl";
	final String CREATE_SERVICE_CONTROL = "createServiceControl";
	final String SERVICE_CONTROL_EDITING_CANCELED = "serviceControlEditingCancelled";
	final String SERVICE_CONTROL_SAVED = "serviceControlSaved";
	
	final String GO_HOME_SERVICE_GUIDELINE = "goHomeServiceGuideline";
	final String MAINTAIN_SERVICE_GUIDELINE = "maintainServiceGuideline";
	final String CREATE_SERVICE_GUIDELINE = "createServiceGuideline";
	final String SERVICE_GUIDELINE_EDITING_CANCELED = "ServiceGuidelineEditingCancelled";
	final String SERVICE_GUIDELINE_SAVED = "ServiceGuidelineSaved";
	
	final String GO_HOME_TEST_PLAN = "goHomeTestPlan";
	final String MAINTAIN_TEST_PLAN = "maintainTestPlan";
	final String CREATE_TEST_PLAN = "createTestPlan";
	final String TEST_PLAN_EDITING_CANCELED = "testPlanEditingCancelled";
	final String TEST_PLAN_SAVED = "testPlanSaved";
	
	final String MAINTAIN_ACCOUNT_NOTIFICATION = "maintainAccountNotification";
	final String ACCOUNT_NOTIFICATION_EDITING_CANCELED = "acctNotificationEditingCancelled";
	final String ACCOUNT_NOTIFICATION_SAVED = "acctNotificationSaved";
	final String MAINTAIN_COUNTER_MEASURE = "maintainCounterMeasure";

	final String SERVICE_TEST_PLAN_EDITING_CANCELED = "serviceTestPlanEditingCancelled";;
	final String MAINTAIN_SERVICE_TEST_PLAN = "maintainServiceTestPlan";
	final String CREATE_SERVICE_TEST_PLAN = "createServiceTestPlan";
	final String SERVICE_TEST_PLAN_SAVED = "ServiceTestPlanSaved";
	final String VIEW_SRD_FOR_USER_NOTIFICATION = "viewUserSrd";
	final String VIEW_SRD_FOR_ACCOUNT_NOTIFICATION = "viewAccountSrd";
	final String MAINTAIN_TERMS_OF_USE_AGREEMENT = "maintainTermsOfUseAgreement";
	final String CREATE_TERMS_OF_USE_AGREEMENT = "createTermsOfUseAgreement";
	final String TERMS_OF_USE_AGREEMENT_SAVED = "termsOfUseAgreementSaved";

	final String GENERATE_INCIDENT = "generateIncident";
	final String MAINTAIN_INCIDENT = "maintainIncident";
	final String INCIDENT_SAVED = "incidentSaved";
	final String INCIDENT_EDITING_CANCELED = "incidentEditingCanceled";
	final String VIEW_INCIDENT_STATUS = "viewIncidentStatus";
	final String INCIDENT_TERMINATE_ACCOUNT = "terminateAccount";
	final String INCIDENT_CREATE_SERVICE_ACCOUNT = "createServiceAccount";
	final String CREATE_USER_NOTIFICATION = "createUserNotification";
	final String CREATE_ACCOUNT_NOTIFICATION = "createAccountNotification";
	
	final String CREATE_VPN_CONNECTION_PROFILE = "createVpnConnectionProfile";
	final String MAINTAIN_VPN_CONNECTION_PROFILE = "maintainVpnConnectionProfile";
	final String GO_HOME_VPN_CONNECTION_PROFILE = "goHomeVpnConnectionProfile";
	final String VPN_CONNECTION_PROFILE_EDITING_CANCELED = "vpnConnectionProfileCanceled";
	final String VPN_CONNECTION_PROFILE_SAVED = "vpnConnectionProfileSaved";
	final String MAINTAIN_VPN_CONNECTION_PROFILE_ASSIGNMENT = "maintainVpnConnectionProfileAssignment";
	final String SHOW_VPNCP_STATUS = "showVpncpStatus";
	final String GO_HOME_VPNCP = "goHomeVpncp";
	final String VPNCP_GENERATED = "vpncpGenerated";
	final String VPNCP_SAVED = "vpncpSaved";
	final String GENERATE_VPN_CONNECTION_PROVISIONING = "generateVpnConnectionProvisioning";
	final String VPNCDP_GENERATED = "vpncdpGenerated";
	final String GENERATE_VPN_CONNECTION_DEPROVISIONING = "generateVpnConnectionDeprovisioning";
	final String VIEW_SERVICE_SECURITY_ASSESSMENT_REPORT = "viewSecurityAssessmentReport";
	final String CREATE_RTP_REVISION = "createRtpRevision";
	
	final String CREATE_SECURITY_RISK_CALCULATION = "createSecurityRiskCalculation";
	final String MAINTAIN_SECURITY_RISK_CALCULATION = "maintainSecurityRiskCalculation";

	final String SHOW_ACCOUNT_DEPROVISIONING_CONFIRMATION = "showAccountDeprovisioningConfirmation";
	final String SHOW_ACCOUNT_PROVISIONING_STATUS = "showAccountProvisioningStatus";
	final String GO_HOME_ACCOUNT_PROVISIONING = "goHomeAccountProvisioning";
	final String ACCOUNT_PROVISIONING_GENERATED = "accountProvisioningGenerated";
	final String ACCOUNT_PROVISIONING_SAVED = "AccountProvisioningSaved";
	final String GENERATE_ACCOUNT_PROVISIONING = "generateAccountProvisioning";
	final String ACCOUNT_DEPROVISIONING_GENERATED = "accountDeprovisioningGenerated";
	final String GENERATE_ACCOUNT_DEPROVISIONING = "generateAccountDeprovisioning";
	final String ACCOUNT_DEPROVISIONING_GENERATED_FROM_PROVISIONING_LIST = "accountDeprovisioningGeneratedFromProvisioningList";
}
