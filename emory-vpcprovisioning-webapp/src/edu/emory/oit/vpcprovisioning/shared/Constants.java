package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Constants implements IsSerializable {
	public static final String CIDR_ASSIGNMENT = "CidrAssignment";
	public static final String CIDR = "Cidr";
	public static final String USER_ACCOUNT = "UserAccount";
	public static final String NET_ID = "NetId=";
	public static final String SESSION_TIMEOUT = "SessionTimeout";

	// these will need to be specific for this app's functionality
//	public static final String PERMISSION_MAINTAIN_CIDR = "edu.emory.vpcp.maintainCidr";
//	public static final String PERMISSION_MAINTAIN_CIDR_ASSIGNMENT = "edu.emory.vpcp.maintainCidrAssignment";
	// Emory AWS Admins
	public static final String PERMISSION_MAINTAIN_EVERYTHING = "edu.emory.vpcp.maintainEverything";
	// Emory AWS Auditors
	public static final String PERMISSION_VIEW_EVERYTHING = "edu.emory.vpcp.viewEverything";

	public static final String[] PERMISSIONS = new String[] {
//		PERMISSION_MAINTAIN_CIDR,
//		PERMISSION_MAINTAIN_CIDR_ASSIGNMENT,
		PERMISSION_MAINTAIN_EVERYTHING,
		PERMISSION_VIEW_EVERYTHING
	};

	public static final String MOA_VPC_QUERY_SPEC = "VirtualPrivateCloudQuerySpecification.v1_0";
	public static final String MOA_VPC_GENERATE = "GenerateVirtualPrivateCloud.v1_0";
	public static final String MOA_VPC_MAINTAIN = "MaintainVirtualPrivateCloud.v1_0";
	public static final String MOA_VPC_REQUISITION = "VirtualPrivateCloudRequisition.v1_0";
	public static final String MOA_VPCP_QUERY_SPEC = "VirtualPrivateCloudProvisioningQuerySpecification.v1_0";
	public static final String MOA_VPCP_MAINTAIN = "MaintainVirtualPrivateCloudProvisioning.v1_0";
	public static final String MOA_VPCP_GENERATE = "GenerateVirtualPrivateCloudProvisioning.v1_0";
	public static final String MOA_ACCOUNT_QUERY_SPEC = "AccountQuerySpecification.v1_0";
	public static final String MOA_ACCOUNT = "Account.v1_0";
	public static final String MOA_AUTHORIZATION = "Authorization.v2_0";
	public static final String MOA_AUTHORIZATION_QUERY_SPEC = "AuthorizationQuerySpecification.v2_0";
	public static final String MOA_CIDR_QUERY_SPEC = "CidrQuerySpecification.v1_0";
	public static final String MOA_CIDR = "Cidr.v1_0";
	public static final String MOA_CIDR_ASSIGNMENT_QUERY_SPEC = "CidrAssignmentQuerySpecification.v1_0";
	public static final String MOA_CIDR_ASSIGNMENT = "CidrAssignment.v1_0";
	public static final String MOA_FULL_PERSON = "FullPerson.v2_0";
	public static final String MOA_FULL_PERSON_QUERY_SPEC = "FullPersonQuerySpecification.v2_0";
	public static final String MOA_BILL_QUERY_SPEC = "BillQuerySpecification.v1_0";
	public static final String MOA_BILL = "Bill.v1_0";

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
}
