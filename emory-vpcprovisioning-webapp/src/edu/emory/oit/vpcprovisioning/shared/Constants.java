package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Constants implements IsSerializable {
	public final static long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
	public final static long MILLIS_PER_12HR = 12 * 60 * 60 * 1000;
	public final static long MILLIS_PER_6HR = 6 * 60 * 60 * 1000;
	public final static long MILLIS_PER_3HR = 3 * 60 * 60 * 1000;
	public final static long MILLIS_PER_HR = 60 * 60 * 1000;
	public final static long MILLIS_PER_HOUR = 60 * 60 * 1000;
	public static final String CIDR_ASSIGNMENT = "CidrAssignment";
	public static final String CIDR = "Cidr";
	public static final String USER_ACCOUNT = "UserAccount";
	public static final String NET_ID = "NetId=";
	public static final String SESSION_TIMEOUT = "SessionTimeout";

	// these will need to be specific for this app's functionality
	
	public static final String ROLE_NAME_RHEDCLOUD_AWS_CENTRAL_ADMIN = "RHEDcloudCentralAdministratorRole";
	public static final String ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS = "RGR_AwsCentralAdministrators";
	public static final String ROLE_NAME_RHEDCLOUD_AWS_ADMIN = "RHEDcloudAdministratorRole";
	public static final String ROLE_NAME_RHEDCLOUD_AUDITOR = "RHEDcloudAuditorRole";
	public static final String STATIC_TEXT_ADMINISTRATOR = "Administrator";
	public static final String STATIC_TEXT_AUDITOR = "Auditor";
	public static final String ROLE_NAME_EMORY_NETWORK_ADMIN = "NetworkAdmin";

	public static final String[] ACCOUNT_ROLE_NAMES = new String[] {
			ROLE_NAME_RHEDCLOUD_AWS_ADMIN,
			ROLE_NAME_RHEDCLOUD_AUDITOR
		};


	public static final String TIME_RULE_INDEFINITELY = "Indefinitely";
	public static final String TIME_RULE_SPECIFIC_DATE = "Specific Date";
	public static final String COLOR_INVALID_FIELD = "#efbebe";
	public static final String COLOR_FIELD_WARNING = "#ffa07a";
	public static final String COLOR_RED = "red";
	public static final String COLOR_GREEN = "green";
	public static final String COLOR_ORANGE = "orange";
	public static final String MOA_SPEEDCHART_QUERY_SPEC = "SPEEDCHART_QUERY.v1_0";
	public static final String MOA_SPEEDCHART = "SPEEDCHART.v1_0";
	public static final String MOA_VPC_QUERY_SPEC = "VirtualPrivateCloudQuerySpecification.v1_0";
//	public static final String MOA_VPC_GENERATE = "GenerateVirtualPrivateCloud.v1_0";
	public static final String MOA_VPC_MAINTAIN = "MaintainVirtualPrivateCloud.v1_0";
	public static final String MOA_VPC_REQUISITION = "VirtualPrivateCloudRequisition.v1_0";
	public static final String MOA_VPCP_QUERY_SPEC = "VirtualPrivateCloudProvisioningQuerySpecification.v1_0";
	public static final String MOA_VPCP_MAINTAIN = "MaintainVirtualPrivateCloudProvisioning.v1_0";
	public static final String MOA_VPCP_GENERATE = "GenerateVirtualPrivateCloudProvisioning.v1_0";
	public static final String MOA_ACCOUNT_QUERY_SPEC = "AccountQuerySpecification.v1_0";
	public static final String MOA_ACCOUNT = "Account.v1_0";
//	public static final String MOA_AUTHORIZATION = "Authorization.v2_0";
//	public static final String MOA_AUTHORIZATION_QUERY_SPEC = "AuthorizationQuerySpecification.v2_0";
	public static final String MOA_CIDR_QUERY_SPEC = "CidrQuerySpecification.v1_0";
	public static final String MOA_CIDR = "Cidr.v1_0";
	public static final String MOA_CIDR_ASSIGNMENT_QUERY_SPEC = "CidrAssignmentQuerySpecification.v1_0";
	public static final String MOA_CIDR_ASSIGNMENT = "CidrAssignment.v1_0";
	public static final String MOA_FULL_PERSON = "FullPerson.v2_0";
	public static final String MOA_FULL_PERSON_QUERY_SPEC = "FullPersonQuerySpecification.v2_0";
	public static final String MOA_BILL_QUERY_SPEC = "BillQuerySpecification.v1_0";
	public static final String MOA_BILL = "Bill.v1_0";
	public static final String MOA_FIREWALL_RULE_QUERY_SPEC = "FirewallRuleQuerySpecification.v1_0";
	public static final String MOA_FIREWALL_RULE = "FirewallRule.v1_0";
	public static final String MOA_DIRECTORY_PERSON_QUERY_SPEC = "DirectoryPersonQuerySpecification.v1_0";
	public static final String MOA_DIRECTORY_PERSON = "DirectoryPerson.v1_0";
	public static final String MOA_ROLE_ASSIGNMENT = "RoleAssignment.v1_0";
	public static final String MOA_ROLE_ASSIGNMENT_REQUISITION = "RoleAssignmentRequisition.v1_0";
	public static final String MOA_ROLE_ASSIGNMENT_QUERY_SPEC = "RoleAssignmentQuerySpecification.v1_0";
	public static final String MOA_ELASTIC_IP_ASSIGNMENT_QUERY_SPEC = "ElasticIpAssignmentQuerySpecification.v1_0";
	public static final String MOA_ELASTIC_IP_ASSIGNMENT = "ElasticIpAssignment.v1_0";
	public static final String MOA_ELASTIC_IP_ASSIGNMENT_GENERATE = "ElasticIpAssignment.Generate.v1_0";
	public static final String MOA_ELASTIC_IP_QUERY_SPEC = "ElasticIpQuerySpecification.v1_0";
	public static final String MOA_ELASTIC_IP = "ElasticIp.v1_0";
	public static final String MOA_ELASTIC_IP_REQUISITION = "ElasticIpRequisition.v1_0";
	public static final String MOA_FIREWALL_EXCEPTION_REQUEST_QUERY_SPEC = "FirewallExceptionRequestQuerySpecification.v1_0";
	public static final String MOA_FIREWALL_EXCEPTION_REQUEST = "FirewallExceptionRequest.v1_0";
	public static final String MOA_SERVICE = "Service.v1_0";
	public static final String MOA_SERVICE_QUERY_SPEC = "ServiceQuerySpecification.v1_0";

    public static final String STYLE_INFO_POPUP_MESSAGE = "informationalPopupMessage";
    
	public static final String VPCP_STATUS_PENDING = "pending";
	public static final String VPCP_STATUS_COMPLETED = "completed";
	public static final String VPCP_RESULT_SUCCESS = "success";
	public static final String VPCP_RESULT_FAILURE = "failure";

	public static final String PROVISIONING_STEP_STATUS_ROLLED_BACK = "rolled back";
	public static final String PROVISIONING_STEP_STATUS_COMPLETED = "completed";
	public static final String VPCP_STEP_STATUS_PENDING = "pending";
	public static final String VPCP_STEP_RESULT_SUCCESS = "success";
	public static final String VPCP_STEP_RESULT_FAILURE = "failure";
	
	public static final String LINEITEM_RECORD_TYPE_PAYER = "PayerLineItem";
	public static final String LINEITEM_RECORD_TYPE_LINKED = "LinkedLineItem";
	
	public static final String SPEED_TYPE_VALID = "Y";
	public static final String SPEED_TYPE_WARNING = "W";
	public static final String SPEED_TYPE_INVALID = "N";
	
	public static final String SUGGESTION_TYPE_DIRECTORY_PERSON_NAME = "DirectoryPerson-Name";
	public static final String SUGGESTION_TYPE_AWS_SERVICE_CODE_NAME = "AWS-Service-Code-Name";
	
	public static final String REPLACEMENT_VAR_AWS_ACCOUNT_NUMBER = "AWS_ACCOUNT_NUMBER";
	public static final String REPLACEMENT_VAR_EMORY_ROLE_NAME = "EMORY_ROLE_NAME";
	public static final String REPLACEMENT_VAR_PUBLIC_ID = "PUBLIC_ID";
	public static final String DIRECTORY_PERSON = "DirectoryPerson";
	public static final String ACCOUNT = "Account";
	public static final String MOA_ACCOUNT_NOTIFICATION_QUERY_SPEC = "AccountNotificationQuerySpecification.v1_0";
	public static final String MOA_ACCOUNT_NOTIFICATION = "AccountNotification.v1_0";
	public static final String MOA_USER_NOTIFICATION_QUERY_SPEC = "UserNotificationQuerySpecification.v1_0";
	public static final String MOA_USER_NOTIFICATION = "UserNotification.v1_0";
	public static final String MOA_USER_PROFILE_QUERY_SPEC = "UserProfileQuerySpecification.v1_0";
	public static final String MOA_USER_PROFILE = "UserProfile.v1_0";
	
	public static final String PROFILE_SETTING_RECEIVE_NOTIFICATIONS = "send e-mail notifications in addition to in-app notifications";
	public static final String MOA_SVC_SECURITY_ASSESSMENT = "ServiceSecurityAssessment.v1_0";
	public static final String MOA_SVC_SECURITY_ASSESSMENT_QUERY_SPEC = "ServiceSecurityAssessmentQuerySpecification.v1_0";
	public static final String MOA_USER_ACTION = "UserAction.v1_0";
	public static final String MOA_SECURITY_RISK_DETECTION_QUERY_SPEC = "SecurityRiskDetectionQuerySpecification.v1_0";
	public static final String MOA_SECURITY_RISK_DETECTION = "SecurityRiskDetection.v1_0";
	public static final String MOA_TERMS_OF_USE = "TermsOfUse.v1_0";
	public static final String MOA_TERMS_OF_USE_QUERY_SPECIFICATION = "TermsOfUseQuerySpecification.v1_0";
	public static final String MOA_TERMS_OF_USE_AGREEMENT = "TermsOfUseAgreement.v1_0";
	public static final String MOA_TERMS_OF_USE_AGREEMENT_QUERY_SPECIFICATION = "TermsOfUseAgreementQuerySpecification.v1_0";
	
	public static final String LIST_ACCOUNT = "listAccount";
	public static final String LIST_VPC = "listVpc";
	public static final String LIST_VPCP = "listVpcp";
	public static final String LIST_SERVICES = "listServices";
	public static final String LIST_CENTRAL_ADMIN = "listCentralAdmin";
	public static final String MOA_ACCOUNT_PROVISIONING_AUTHORIZATION_QUERY_SPECIFICATION = "AccountProvisioningAuthorizationQuerySpecification.v1_0";
	public static final String MOA_ACCOUNT_PROVISIONING_AUTHORIZATION = "AccountProvisioningAuthorization.v1_0";
	public static final String MOA_INCIDENT_GENERATE = "GenerateIncident.v2_0";
	public static final String MOA_INCIDENT_MAINTAIN = "MaintainIncident.v2_0";
	public static final String MOA_INCIDENT_QUERY_SPEC = "IncidentQuerySpecification.v2_0";
	public static final String MOA_INCIDENT_REQUISITION = "IncidentRequisition.v2_0";
	
	public static final String INCIDENT_TYPE_TERMINATE_ACCOUNT = "terminateAccount";
	public static final String INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT = "createServiceAccount";
	public static final String LIST_STATIC_NAT = "listStaticNat";
	public static final String MOA_STATIC_NAT_PROVISIONING = "StaticNatProvisioning.v1_0";
	public static final String MOA_STATIC_NAT_PROVISIONING_QUERY_SPEC = "StaticNatProvisioningQuerySpecification.v1_0";
	public static final String MOA_STATIC_NAT_DEPROVISIONING = "StaticNatDeprovisioning.v1_0";
	public static final String MOA_STATIC_NAT_DEPROVISIONING_QUERY_SPEC = "StaticNatDeprovisioningQuerySpecification.v1_0";
	
	public static final String NOTIFICATION_TYPE_CENTRAL_ADMIN = "Central Admin Initiated";
	public static final String LIST_ELASTIC_IP = "listElasticIp";
	public static final String LIST_VPN_CONNECTION = "listVpnConnection";
	public static final String LIST_VPN_CONNECTION_PROFILE = "listVpnConnectionProfile";
	public static final String MOA_VPN_CONNECTION_PROFILE_QUERY_SPEC = "VpnConnectionProfileQuerySpecification.v1_0";
	public static final String MOA_VPN_CONNECTION_PROFILE = "VpnConnectionProfile.v1_0";
	public static final String MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT_QUERY_SPEC = "VpnConnectionProfileAssignmentQuerySpecification.v1_0";
	public static final String MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT = "VpnConnectionProfileAssignment.v1_0";
	public static final String MOA_VPCNP_QUERY_SPEC = "VpnConnectionProvisioningQuerySpecification.v1_0";
	public static final String MOA_VPCNP = "VpnConnectionProvisioning.v1_0";
}
