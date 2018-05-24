package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Constants implements IsSerializable {
	public static final String CIDR_ASSIGNMENT = "CidrAssignment";
	public static final String CIDR = "Cidr";
	public static final String USER_ACCOUNT = "UserAccount";
	public static final String NET_ID = "NetId=";
	public static final String SESSION_TIMEOUT = "SessionTimeout";

	// these will need to be specific for this app's functionality
	
	public static final String ROLE_NAME_RHEDCLOUD_AWS_CENTRAL_ADMIN = "RHEDcloudCentralAdministratorRole";
	public static final String ROLE_NAME_RHEDCLOUD_AWS_ADMIN = "RHEDcloudAdministratorRole";
	public static final String ROLE_NAME_RHEDCLOUD_AUDITOR = "RHEDcloudAuditorRole";
	public static final String STATIC_TEXT_ADMINISTRATOR = "Administrator";
	public static final String STATIC_TEXT_AUDITOR = "Auditor";
	
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
	public static final String MOA_VPC_GENERATE = "GenerateVirtualPrivateCloud.v1_0";
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
	public static final String MOA_ELASTIC_IP_QUERY_SPEC = "ElasticIpQuerySpecification.v1_0";
	public static final String MOA_ELASTIC_IP = "ElasticIp.v1_0";
	public static final String MOA_ELASTIC_IP_REQUISITION = "ElasticIpRequisition.v1_0";
	public static final String MOA_FIREWALL_EXCEPTION_REQUEST_QUERY_SPEC = "FirewallExceptionRequestQuerySpecification.v1_0";
	public static final String MOA_FIREWALL_EXCEPTION_REQUEST = "FirewallExceptionRequest.v1_0";

    public static final String STYLE_INFO_POPUP_MESSAGE = "informationalPopupMessage";
    
	public static final String VPCP_STATUS_PENDING = "pending";
	public static final String VPCP_STATUS_COMPLETED = "completed";
	public static final String VPCP_RESULT_SUCCESS = "success";
	public static final String VPCP_RESULT_FAILURE = "failure";

	public static final String VPCP_STEP_STATUS_COMPLETED = "completed";
	public static final String VPCP_STEP_STATUS_PENDING = "pending";
	public static final String VPCP_STEP_RESULT_SUCCESS = "success";
	public static final String VPCP_STEP_RESULT_FAILURE = "failure";
	
	public static final String LINEITEM_RECORD_TYPE_PAYER = "PayerLineItem";
	public static final String LINEITEM_RECORD_TYPE_LINKED = "LinkedLineItem";
	
	public static final String SPEED_TYPE_VALID = "Y";
	public static final String SPEED_TYPE_WARNING = "W";
	public static final String SPEED_TYPE_INVALID = "N";
	
	public static final String SUGGESTION_TYPE_DIRECTORY_PERSON_NAME = "DirectoryPerson-Name";
	
	public static final String REPLACEMENT_VAR_AWS_ACCOUNT_NUMBER = "AWS_ACCOUNT_NUMBER";
	public static final String REPLACEMENT_VAR_EMORY_ROLE_NAME = "EMORY_ROLE_NAME";
	public static final String REPLACEMENT_VAR_PUBLIC_ID = "PUBLIC_ID";
}
