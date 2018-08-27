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

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningAuthorizationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningAuthorizationQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.BillPojo;
import edu.emory.oit.vpcprovisioning.shared.BillQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.BillQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentStatus;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentStatusPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.PersonInfoSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.RpcException;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserActionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserActionQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserActionQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryResultPojo;

public interface VpcProvisioningServiceAsync {
	// START TEMPORARY
	void testPropertyReload(AsyncCallback<String> callback);
	// END TEMPORARY
	
//	void getLoginURL(AsyncCallback<String> callback);
	void logMessage(String message, AsyncCallback<Void> callback);
	void getEsbServiceStatusURL(AsyncCallback<String> callback);
	void getAccountSeriesText(AsyncCallback<String> callback) throws RpcException;
	void getMyNetIdURL(AsyncCallback<String> callback) throws RpcException;
	
	// ReleaseInfo
	void getReleaseInfo(AsyncCallback<ReleaseInfo> callback);
	
	// Identity services
	void getDirectoryMetaDataForPublicId(String netId, AsyncCallback<DirectoryMetaDataPojo> callback);

	// UserAccount services (user logged in)
	void getUserLoggedIn(AsyncCallback<UserAccountPojo> callback);
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
	void deleteElasticIpSummary(ElasticIpSummaryPojo cidr, AsyncCallback<Void> callback);
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
	void getServiceStatusItems(AsyncCallback<List<String>> callback);

	void getAWSServiceMap(AsyncCallback<HashMap<String, List<AWSServicePojo>>> callback) throws RpcException;
	
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
	
	// FirewallExceptionRequest
	void getFirewallExceptionRequestsForFilter(FirewallExceptionRequestQueryFilterPojo filter, AsyncCallback<FirewallExceptionRequestQueryResultPojo> callback);
	void createFirewallExceptionRequest(FirewallExceptionRequestPojo rule, AsyncCallback<FirewallExceptionRequestPojo> callback);
	void updateFirewallExceptionRequest(FirewallExceptionRequestPojo rule, AsyncCallback<FirewallExceptionRequestPojo> callback);
	void deleteFirewallExceptionRequest(FirewallExceptionRequestPojo rule, AsyncCallback<Void> callback);

	// DirectoryPerson
	void getDirectoryPersonsForFilter(DirectoryPersonQueryFilterPojo filter, AsyncCallback<DirectoryPersonQueryResultPojo> callback);
	
	// FullPerson
	void getFullPersonsForFilter(FullPersonQueryFilterPojo filter, AsyncCallback<FullPersonQueryResultPojo> callback);
	
	// RoleAssignment
	void createRoleAssignmentForPersonInAccount(String publicId, String accountId, String roleName, AsyncCallback<RoleAssignmentPojo> callback);
	void getRoleAssignmentsForFilter(RoleAssignmentQueryFilterPojo filter, AsyncCallback<RoleAssignmentQueryResultPojo> callback);
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

}
