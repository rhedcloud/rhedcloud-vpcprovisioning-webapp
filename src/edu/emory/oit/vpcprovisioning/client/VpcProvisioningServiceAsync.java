/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package edu.emory.oit.vpcprovisioning.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.emory.oit.vpcprovisioning.shared.*;

public interface VpcProvisioningServiceAsync {
	// START TEMPORARY
	void testPropertyReload(AsyncCallback<String> callback);
	// END TEMPORARY
	
//	void getLoginURL(AsyncCallback<String> callback);
	void logMessage(String message, AsyncCallback<Void> callback);
	void getEsbServiceStatusURL(AsyncCallback<String> callback);
	void getAccountSeriesText(AsyncCallback<String> callback) throws RpcException;
	void getMyNetIdURL(AsyncCallback<String> callback) throws RpcException;
	void getPropertiesForIncidentOfType(String incidentType, AsyncCallback<PropertiesPojo> callback) throws RpcException;
	
	// ReleaseInfo
	void getReleaseInfo(AsyncCallback<ReleaseInfo> callback);
	
	// Identity services
	void getDirectoryMetaDataForPublicId(String netId, AsyncCallback<DirectoryMetaDataPojo> callback);
	void getFullNameForPublicId(String ppid, AsyncCallback<String> callback);

	// UserAccount services (user logged in)
	void getUserLoggedIn(AsyncCallback<UserAccountPojo> callback);
	void getUserLoggedIn(boolean refreshRoles, AsyncCallback<UserAccountPojo> callback);
	void invalidateSessionForUser(String userId, AsyncCallback<Void> callback);
	void isSessionValidForUser(UserAccountPojo user, AsyncCallback<Boolean> callback);
	void getClientInfoForUser(UserAccountPojo user, AsyncCallback<String> callback);
	void getClientInfo(AsyncCallback<String> callback);
	
	// Account
	void getAccountsForUserLoggedIn(AsyncCallback<List<AccountPojo>> callback);
	void getAccountsForFilter(AccountQueryFilterPojo filter, AsyncCallback<AccountQueryResultPojo> callback);
	void createAccount(AccountPojo account, AsyncCallback<AccountPojo> callback);
	void updateAccount(AccountPojo account, AsyncCallback<AccountPojo> callback);
	void deleteAccount(AccountPojo account, AsyncCallback<Void> callback);
	void getEmailTypeItems(AsyncCallback<List<String>> callback);
	void getAwsAccountsURL(AsyncCallback<String> callback);
	void getAwsBillingManagementURL(AsyncCallback<String> callback);
	void getAccountById(String accountId, AsyncCallback<AccountPojo> callback);
	
	// CIDR
	void getCidrsForUserLoggedIn(AsyncCallback<List<CidrPojo>> callback);
	void getCidrsForFilter(CidrQueryFilterPojo filter, AsyncCallback<CidrQueryResultPojo> callback);
	void createCidr(CidrPojo cidr, AsyncCallback<CidrPojo> callback);
	void updateCidr(CidrPojo cidr, AsyncCallback<CidrPojo> callback);
	void deleteCidrSummary(CidrSummaryPojo cidr, AsyncCallback<Void> callback);
	void isCidrAssigned(CidrPojo cidr, AsyncCallback<Boolean> callback);
	void getCidrAssignmentStatusForCidr(CidrPojo cidr, AsyncCallback<CidrAssignmentStatus> callback);

	// CidrAssignment
	void getCidrAssignmentsForUserLoggedIn(AsyncCallback<List<CidrAssignmentPojo>> callback);
	void getCidrAssignmentsForFilter(CidrAssignmentQueryFilterPojo filter, AsyncCallback<CidrAssignmentQueryResultPojo> callback);
	void createCidrAssignment(CidrAssignmentPojo cidrAssignment, AsyncCallback<CidrAssignmentPojo> callback);
	void updateCidrAssignment(CidrAssignmentPojo cidrAssignment, AsyncCallback<CidrAssignmentPojo> callback);
	void deleteCidrAssignment(CidrAssignmentPojo cidr, AsyncCallback<Void> callback);
	void getVpcsForAccount(String accountId, AsyncCallback<List<VpcPojo>> callback);
	void getUnassignedCidrs(AsyncCallback<List<CidrPojo>> callback);
	
	// CidrAssignmentSummary
	void getCidrAssignmentSummariesForFilter(CidrAssignmentSummaryQueryFilterPojo filter, AsyncCallback<CidrAssignmentSummaryQueryResultPojo> callback);
	
	// VPC
	void getVpcsForUserLoggedIn(AsyncCallback<List<VpcPojo>> callback);
	void getVpcsForFilter(VpcQueryFilterPojo filter, AsyncCallback<VpcQueryResultPojo> callback);
//	void generateVpc(VpcRequisitionPojo vpcRequisition, AsyncCallback<VpcPojo> callback);
	void updateVpc(VpcPojo vpc, AsyncCallback<VpcPojo> callback);
	void registerVpc(VpcPojo vpc, AsyncCallback<VpcPojo> callback);
	void deleteVpc(VpcPojo vpc, AsyncCallback<Void> callback);
	void getVpcTypeItems(AsyncCallback<List<String>> callback);
	
	// VPCP
	void getVpcpsForFilter(VpcpQueryFilterPojo filter, AsyncCallback<VpcpQueryResultPojo> callback);
	void deleteVpcp(VpcpPojo vpc, AsyncCallback<Void> callback);
	void generateVpcp(VpcRequisitionPojo vpcRequisition, AsyncCallback<VpcpPojo> callback);
	void updateVpcp(VpcpPojo vpc, AsyncCallback<VpcpPojo> callback);
	void getComplianceClassItems(AsyncCallback<List<String>> callback);
	
	// Bill and LineItem methods
	void getBillsForFilter(BillQueryFilterPojo filter, AsyncCallback<BillQueryResultPojo> callback);
	void getCachedBillsForAccount(String accountId, AsyncCallback<List<BillPojo>> callback);
	void refreshMasterBillData(AsyncCallback<Void> callback);


	// caching methods
	void storeCidrInCacheForUser(String eppn, CidrPojo cidr, AsyncCallback<CidrPojo> callback);
	void getCidrFromCacheForUser(String eppn, AsyncCallback<CidrPojo> callback);
	void storeCidrAssignmentInCacheForUser(String eppn, CidrAssignmentPojo cidrAssignment, AsyncCallback<CidrAssignmentPojo> callback);
	void getCidrAssignmentFromCacheForUser(String eppn, AsyncCallback<CidrAssignmentPojo> callback);

	// Elastic IP
	void getElasticIpsForFilter(ElasticIpQueryFilterPojo filter, AsyncCallback<ElasticIpQueryResultPojo> callback);
	void createElasticIp(ElasticIpPojo cidr, AsyncCallback<ElasticIpPojo> callback);
	void updateElasticIp(ElasticIpPojo cidr, AsyncCallback<ElasticIpPojo> callback);
	void deleteElasticIp(ElasticIpPojo cidr, AsyncCallback<ElasticIpPojo> callback);
	void isElasticIpAssigned(ElasticIpPojo cidr, AsyncCallback<Boolean> callback);
	void getElasticIpAssignmentStatusForElasticIp(ElasticIpPojo cidr, AsyncCallback<ElasticIpAssignmentStatusPojo> callback);

	// ElasticIpAssignment
	void getElasticIpAssignmentsForUserLoggedIn(AsyncCallback<List<ElasticIpAssignmentPojo>> callback);
	void getElasticIpAssignmentsForFilter(ElasticIpAssignmentQueryFilterPojo filter, AsyncCallback<ElasticIpAssignmentQueryResultPojo> callback);
	void generateElasticIpAssignment(ElasticIpAssignmentRequisitionPojo elasticIpAssignment, AsyncCallback<ElasticIpAssignmentPojo> callback);
	void updateElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment, AsyncCallback<ElasticIpAssignmentPojo> callback);
	void deleteElasticIpAssignment(ElasticIpAssignmentPojo cidr, AsyncCallback<Void> callback);
	void getUnassignedElasticIps(AsyncCallback<List<ElasticIpPojo>> callback);
	
	// ElasticIpAssignmentSummary
	void getElasticIpAssignmentSummariesForFilter(ElasticIpAssignmentSummaryQueryFilterPojo filter, AsyncCallback<ElasticIpAssignmentSummaryQueryResultPojo> callback);
	
	// services
	void getServicesForFilter(AWSServiceQueryFilterPojo filter, AsyncCallback<AWSServiceQueryResultPojo> callback);
	void createService(AWSServicePojo service, AsyncCallback<AWSServicePojo> callback);
	void updateService(AWSServicePojo service, AsyncCallback<AWSServicePojo> callback);
	void deleteService(AWSServicePojo service, AsyncCallback<Void> callback);
	void getAwsServiceStatusItems(AsyncCallback<List<String>> callback);
	void getSiteServiceStatusItems(AsyncCallback<List<String>> callback);

	void getAWSServiceMap(AsyncCallback<AWSServiceSummaryPojo> callback) throws RpcException;
	
	// SpeedChart
	void getSpeedChartsForFilter(SpeedChartQueryFilterPojo filter, AsyncCallback<SpeedChartQueryResultPojo> callback);
	void getSpeedChartForFinancialAccountNumber(String accountNumber, AsyncCallback<SpeedChartPojo> callback);
	
	// user notifications
	void getUserNotificationsForFilter(UserNotificationQueryFilterPojo filter, AsyncCallback<UserNotificationQueryResultPojo> callback);
	void createUserNotification(UserNotificationPojo notification, AsyncCallback<UserNotificationPojo> callback);
	void updateUserNotification(UserNotificationPojo notification, AsyncCallback<UserNotificationPojo> callback);
	void deleteUserNotification(UserNotificationPojo notification, AsyncCallback<Void> callback);
	void userHasUnreadNotifications(UserAccountPojo user, AsyncCallback<Boolean> callback);
	void getNotificationCheckIntervalMillis(AsyncCallback<Integer> callback);
	void markAllUnreadNotificationsForUserAsRead(UserAccountPojo user, AsyncCallback<Void> callback);

	// account notifications
	void getAccountNotificationsForFilter(AccountNotificationQueryFilterPojo filter, AsyncCallback<AccountNotificationQueryResultPojo> callback);
	void createAccountNotification(AccountNotificationPojo notification, AsyncCallback<AccountNotificationPojo> callback);
	void updateAccountNotification(AccountNotificationPojo notification, AsyncCallback<AccountNotificationPojo> callback);
	void deleteAccountNotification(AccountNotificationPojo notification, AsyncCallback<Void> callback);
	
	// FirewallRule
	void getFirewallRulesForFilter(FirewallRuleQueryFilterPojo filter, AsyncCallback<FirewallRuleQueryResultPojo> callback);
	void createFirewallRule(FirewallRulePojo rule, AsyncCallback<FirewallRulePojo> callback);
	void updateFirewallRule(FirewallRulePojo rule, AsyncCallback<FirewallRulePojo> callback);
	void deleteFirewallRule(FirewallRulePojo rule, AsyncCallback<Void> callback);
	
	// FirewallExceptionAddRequest
	void getFirewallExceptionAddRequestsForFilter(FirewallExceptionAddRequestQueryFilterPojo filter, AsyncCallback<FirewallExceptionAddRequestQueryResultPojo> callback);
	void generateFirewallExceptionAddRequest(FirewallExceptionAddRequestRequisitionPojo rule, AsyncCallback<FirewallExceptionAddRequestPojo> callback);
	void updateFirewallExceptionAddRequest(FirewallExceptionAddRequestPojo rule, AsyncCallback<FirewallExceptionAddRequestPojo> callback);
	void deleteFirewallExceptionAddRequest(FirewallExceptionAddRequestPojo rule, AsyncCallback<Void> callback);

	// FirewallExceptionRemoveRequest
	void getFirewallExceptionRemoveRequestsForFilter(FirewallExceptionRemoveRequestQueryFilterPojo filter, AsyncCallback<FirewallExceptionRemoveRequestQueryResultPojo> callback);
	void generateFirewallExceptionRemoveRequest(FirewallExceptionRemoveRequestRequisitionPojo rule, AsyncCallback<FirewallExceptionRemoveRequestPojo> callback);
	void updateFirewallExceptionRemoveRequest(FirewallExceptionRemoveRequestPojo rule, AsyncCallback<FirewallExceptionRemoveRequestPojo> callback);
	void deleteFirewallExceptionRemoveRequest(FirewallExceptionRemoveRequestPojo rule, AsyncCallback<Void> callback);

	void getFirewallExceptionRequestComplianceClassItems(AsyncCallback<List<String>> callback);
	void getFirewallExceptionRequestSummariesForFilter(FirewallExceptionRequestSummaryQueryFilterPojo filter, AsyncCallback<FirewallExceptionRequestSummaryQueryResultPojo> callback);

	// DirectoryPerson
	void getDirectoryPersonsForFilter(DirectoryPersonQueryFilterPojo filter, AsyncCallback<DirectoryPersonQueryResultPojo> callback);
	
	// FullPerson
	void getFullPersonsForFilter(FullPersonQueryFilterPojo filter, AsyncCallback<FullPersonQueryResultPojo> callback);
	
	// RoleAssignment
	void createRoleAssignmentForPersonInAccount(String publicId, String accountId, String roleName, AsyncCallback<RoleAssignmentPojo> callback);
	void getRoleAssignmentsForFilter(RoleAssignmentQueryFilterPojo filter, AsyncCallback<RoleAssignmentQueryResultPojo> callback);
	void getRoleAssignmentsForAccounts(List<String> accountIds, AsyncCallback<List<RoleAssignmentSummaryPojo>> callback) throws RpcException;
	void getRoleAssignmentsForAccount(String accountId, AsyncCallback<List<RoleAssignmentSummaryPojo>> callback) throws RpcException;
	void removeRoleAssignmentFromAccount(String accountId, RoleAssignmentPojo roleAssignment, AsyncCallback<Void> callback) throws RpcException;
	void getCentralAdmins(AsyncCallback<List<RoleAssignmentSummaryPojo>> callback) throws RpcException;

	// user profile
	void getUserProfilesForFilter(UserProfileQueryFilterPojo filter, AsyncCallback<UserProfileQueryResultPojo> callback);
	void createUserProfile(UserProfilePojo profile, AsyncCallback<UserProfilePojo> callback);
	void updateUserProfile(UserProfilePojo profile, AsyncCallback<UserProfilePojo> callback);
	void deleteUserProfile(UserProfilePojo profile, AsyncCallback<Void> callback);

	
	// user actions
	void getUserActionsForFilter(UserActionQueryFilterPojo filter, AsyncCallback<UserActionQueryResultPojo> callback);
	void createUserAction(UserActionPojo profile, AsyncCallback<UserActionPojo> callback);
	
	// terms of use
	void getTermsOfUseForFilter(TermsOfUseQueryFilterPojo filter, AsyncCallback<TermsOfUseQueryResultPojo> callback);
	void getTermsOfUseAgreementsForFilter(TermsOfUseAgreementQueryFilterPojo filter, AsyncCallback<TermsOfUseAgreementQueryResultPojo> callback);
	void createTermsOfUseAgreement(TermsOfUseAgreementPojo profile, AsyncCallback<TermsOfUseAgreementPojo> callback);
	void getTermsOfUseSummaryForUser(UserAccountPojo user, AsyncCallback<TermsOfUseSummaryPojo> callback);

	void getSecurityAssessmentsForFilter(ServiceSecurityAssessmentQueryFilterPojo filter, AsyncCallback<ServiceSecurityAssessmentQueryResultPojo> callback);
	void createSecurityAssessment(ServiceSecurityAssessmentPojo service, AsyncCallback<ServiceSecurityAssessmentPojo> callback);
	void updateSecurityAssessment(ServiceSecurityAssessmentPojo service, AsyncCallback<ServiceSecurityAssessmentPojo> callback);
	void deleteSecurityAssessment(ServiceSecurityAssessmentPojo service, AsyncCallback<Void> callback);
	void getAssessmentStatusTypeItems(AsyncCallback<List<String>> callback);
	void getRiskLevelTypeItems(AsyncCallback<List<String>> callback);
	void getCounterMeasureStatusTypeItems(AsyncCallback<List<String>> callback);
	void getSecurityRiskDetectionsForFilter(SecurityRiskDetectionQueryFilterPojo filter, AsyncCallback<SecurityRiskDetectionQueryResultPojo> callback);
	void getSecurityAssessmentSummariesForFilter(SecurityAssessmentSummaryQueryFilterPojo filter, AsyncCallback<SecurityAssessmentSummaryQueryResultPojo> callback);
	void getServiceControlTypeItems(AsyncCallback<List<String>> callback);
	void getServiceControlImplementationTypeItems(AsyncCallback<List<String>> callback);

	void getAccountProvisioningAuthorizationsForFilter(AccountProvisioningAuthorizationQueryFilterPojo filter, AsyncCallback<AccountProvisioningAuthorizationQueryResultPojo> callback);
	void getPersonInfoSummaryForPublicId(String publicId, AsyncCallback<PersonInfoSummaryPojo> callback);
	
	// incident
	void getIncidentsForFilter(IncidentQueryFilterPojo filter, AsyncCallback<IncidentQueryResultPojo> callback);
	void deleteIncident(IncidentPojo incident, AsyncCallback<Void> callback);
	void generateIncident(IncidentRequisitionPojo incidentRequisition, AsyncCallback<IncidentPojo> callback);
	void updateIncident(IncidentPojo incident, AsyncCallback<IncidentPojo> callback);

	// static nat provisioning summaries
	void getStaticNatProvisioningSummariesForFilter(StaticNatProvisioningSummaryQueryFilterPojo filter, AsyncCallback<StaticNatProvisioningSummaryQueryResultPojo> callback);
	void getStaticNatProvisioningsForFilter(StaticNatProvisioningQueryFilterPojo filter, AsyncCallback<StaticNatProvisioningQueryResultPojo> callback);
	void getStaticNatDeprovisioningsForFilter(StaticNatDeprovisioningQueryFilterPojo filter, AsyncCallback<StaticNatDeprovisioningQueryResultPojo> callback);

	// vpn connection profiles and assignments
	void getVpnConnectionProfilesForFilter(VpnConnectionProfileQueryFilterPojo filter, AsyncCallback<VpnConnectionProfileQueryResultPojo> callback);
	void createVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile, AsyncCallback<VpnConnectionProfilePojo> callback);
	void updateVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile, AsyncCallback<VpnConnectionProfilePojo> callback);
	void deleteVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile, AsyncCallback<VpnConnectionProfilePojo> callback);
	void getVpnConnectionProfileAssignmentsForFilter(VpnConnectionProfileAssignmentQueryFilterPojo filter, AsyncCallback<VpnConnectionProfileAssignmentQueryResultPojo> callback);
	void getVpnConnectionsForFilter(VpnConnectionQueryFilterPojo filter, AsyncCallback<VpnConnectionQueryResultPojo> callback);

	// vpncp
	void getVpncpSummariesForFilter(VpnConnectionProvisioningQueryFilterPojo filter, AsyncCallback<VpnConnectionProvisioningQueryResultPojo> callback);
	void generateVpncp(VpnConnectionRequisitionPojo requisition, AsyncCallback<VpnConnectionProvisioningPojo> callback);
	void updateVpnConnectionProvisioning(VpnConnectionProvisioningPojo pojo, AsyncCallback<VpnConnectionProvisioningPojo> callback);
//	void generateVpnConnectionDeprovisioning(VpnConnectionRequisitionPojo requisition, AsyncCallback<VpnConnectionDeprovisioningPojo> callback);
//	void generateVpnConnectionDeprovisioning(VpnConnectionPojo vpnConnection, AsyncCallback<VpnConnectionDeprovisioningPojo> callback);
	void generateVpnConnectionDeprovisioning(VpnConnectionProfileAssignmentPojo assignment, AsyncCallback<VpnConnectionDeprovisioningPojo> callback);
	void updateVpnConnectionDeprovisioning(VpnConnectionDeprovisioningPojo pojo, AsyncCallback<VpnConnectionDeprovisioningPojo> callback);
	void generateVpnConnectionProfileAssignment(VpnConnectionProfileAssignmentRequisitionPojo requisition, AsyncCallback<VpnConnectionProfileAssignmentPojo> callback);
	void createVpnConnectionProfileAssignment(VpnConnectionProfileAssignmentPojo profileAssignment, AsyncCallback<VpnConnectionProfileAssignmentPojo> callback);
	void deleteVpnConnectionProfileAssignment(VpnConnectionProfileAssignmentPojo vpnConnectionProfileAssignment, AsyncCallback<VpnConnectionProfileAssignmentPojo> callback);

//	void getTkiClientS3AccessWrapper(AsyncCallback<AmazonS3AccessWrapperPojo> callback);
	void getAwsRegionItems(AsyncCallback<List<AWSRegionPojo>> callback);

	void getCurrentSystemTime(AsyncCallback<Long> callback);
	
	// Emory CIMP Specific methods
	void isCimpInstance(AsyncCallback<Boolean> callback);
	void getFinancialAccountFieldLabel(AsyncCallback<String> callback);

}
