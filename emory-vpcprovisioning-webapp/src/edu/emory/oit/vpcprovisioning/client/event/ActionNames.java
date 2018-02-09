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
	
	final String MAINTAIN_SERVICE = "maintainSvc";
	final String CREATE_SERVICE = "createSvc";
	final String REGISTER_VPC = "registerVpc";
	final String MAINTAIN_VPC = "maintainVpc";
	final String CREATE_ACCOUNT = "createAccount";
	final String MAINTAIN_ACCOUNT = "maintainAccount";
	final String CREATE_CIDR = "createCidr";
	final String MAINTAIN_CIDR = "maintainCidr";
	final String MAINTAIN_CIDR_ASSIGNMENT = "maintainCidrAssignment";
	final String CREATE_CIDR_ASSIGNMENT = "createCidrAssignment";
	final String CREATE_CIDR_ASSIGNMENT_AFTER_VPC_REGISTRATION = "createCidrAssignmentAfterVpcReg";
	final String CREATE_ELASTIC_IP = "createElasticIp";
	final String MAINTAIN_ELASTIC_IP = "maintainElasticIp";
	final String CREATE_ELASTIC_IP_ASSIGNMENT = "createElasticIpAssignment";
	final String MAINTAIN_ELASTIC_IP_ASSIGNMENT = "maintainElasticIpAssignment";

	final String VPC_EDITING_CANCELED = "vpcEditingCanceled";
	final String VPC_REGISTRATION_CANCELED = "vpcRegistrationCanceled";
	final String ACCOUNT_EDITING_CANCELED = "accountEditingCanceled";
	final String CIDR_EDITING_CANCELED = "cidrEditingCanceled";
	final String CIDR_ASSIGNMENT_EDITING_CANCELED = "cidrAssignmentEditingCanceled";
	final String CIDR_ASSIGNMENT_EDITING_CANCELED_AFTER_VPC_REGISTRATION = "cidrAssignmentEditingCanceledAfterVpcReg";
	final String ELASTIC_IP_EDITING_CANCELED = "elasticIpEditingCanceled";
	final String ELASTIC_IP_ASSIGNMENT_EDITING_CANCELED = "elasticIpAssignmentEditingCanceled";

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

	final String VPC_CACHED = "vpcCached";
	final String ACCOUNT_CACHED = "accountCached";
	final String CIDR_CACHED = "cidrCached";
	final String CIDR_ASSIGNMENT_CACHED = "cidrAssignmentCached";
	final String ELASTIC_IP_CACHED = "elasticIpCached";
	final String ELASTIC_IP_ASSIGNMENT_CACHED = "elasticIpAssignmentCached";
	
	final String VPC_DELETED = "vpcDeleted";
	final String ACCOUNT_DELETED = "accountDeleted";
	final String CIDR_DELETED = "cidrDeleted";
	final String CIDR_ASSIGNMENT_DELETED = "cidrAssignmentDeleted";
	final String ELASTIC_IP_DELETED = "elasticIpDeleted";
	final String ELASTIC_IP_ASSIGNMENT_DELETED = "elasticIpAssignmentDeleted";
	
	final String VPCP_GENERATED = "vpcpGenerated";
	final String VPCP_SAVED = "vpcpSaved";
	final String VPC_SAVED = "vpcSaved";
	final String ACCOUNT_SAVED = "accountSaved";
	final String CIDR_SAVED = "cidrSaved";
	final String CIDR_ASSIGNMENT_SAVED = "cidrAssignmentSaved";
	final String CIDR_ASSIGNMENT_SAVED_AFTER_VPC_REGISTRATION = "cidrAssignmentSavedAfterVpcReg";
	final String ELASTIC_IP_SAVED = "elasticIpSaved";
	final String ELASTIC_IP_ASSIGNMENT_SAVED = "elasticIpAssignmentSaved";

	final String INAUTHENTIC_CLIENT = "inauthenticClient";

	final String CREATE_USER_PASSCODE = "createUserPasscode";
	final String CONFIRM_USER_PASSCODE = "confirmUserPasscode";
	final String CONFIRM_PASSCODE_CREATE_CASE = "confirmPasscodeCreateCase";
	final String CONFIRM_PASSCODE_CACHE_CASE = "confirmPasscodeCacheCase";
	final String PIN_CONFIRMED = "pinConfirmed";
	final String SHOW_VPCP_STATUS = "showVpcpStatus";
	final String SHOW_BILL_SUMMARY_FOR_ACCOUNT = "showBillSummaryForAccount";
}
