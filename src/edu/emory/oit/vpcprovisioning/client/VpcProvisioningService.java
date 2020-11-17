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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.emory.oit.vpcprovisioning.shared.*;

@RemoteServiceRelativePath("VpcProvisioningService")
public interface VpcProvisioningService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static VpcProvisioningServiceAsync instance;
		public static VpcProvisioningServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(VpcProvisioningService.class);
			}
			return instance;
		}
	}
	
	
	// START TEMPORARY
	String testPropertyReload() throws RpcException;
	// END TEMPORARY
	
//	String getLoginURL() throws RpcException;

	String getEsbServiceStatusURL() throws RpcException;
	String getAccountSeriesText() throws RpcException;
	String getMyNetIdURL() throws RpcException;
	PropertiesPojo getPropertiesForIncidentOfType(String incidentType) throws RpcException;

	// ReleaseInfo
	ReleaseInfo getReleaseInfo() throws RpcException;
	
	// logging
	void logMessage(String message) throws RpcException;
	
	// Identity services
	DirectoryMetaDataPojo getDirectoryMetaDataForPublicId(String netId) throws RpcException;
	String getFullNameForPublicId(String ppid) throws RpcException;

	// UserAccount services (user logged in)
	UserAccountPojo getUserLoggedIn() throws RpcException;
	UserAccountPojo getUserLoggedIn(boolean refreshRoles) throws RpcException;
	void invalidateSessionForUser(String userId);
	boolean isSessionValidForUser(UserAccountPojo user) throws RpcException;
	String getClientInfoForUser(UserAccountPojo user);
	String getClientInfo();
	
	// Account
	List<AccountPojo>getAccountsForUserLoggedIn() throws RpcException;
	AccountQueryResultPojo getAccountsForFilter(AccountQueryFilterPojo filter) throws RpcException;
	AccountPojo createAccount(AccountPojo account) throws RpcException;
	AccountPojo updateAccount(AccountPojo account) throws RpcException;
	void deleteAccount(AccountPojo account) throws RpcException;
	List<String> getEmailTypeItems();
	String getAwsAccountsURL() throws RpcException;
	String getAwsBillingManagementURL() throws RpcException;
	AccountPojo getAccountById(String accountId) throws RpcException;

	
	// CIDR
	List<CidrPojo>getCidrsForUserLoggedIn() throws RpcException;
	CidrQueryResultPojo getCidrsForFilter(CidrQueryFilterPojo filter) throws RpcException;
	CidrPojo createCidr(CidrPojo cidr) throws RpcException;
	CidrPojo updateCidr(CidrPojo cidr) throws RpcException;
	void deleteCidrSummary(CidrSummaryPojo cidrSummary) throws RpcException;
	boolean isCidrAssigned(CidrPojo cidr) throws RpcException;
	CidrAssignmentStatus getCidrAssignmentStatusForCidr(CidrPojo cidr) throws RpcException;

	// CidrAssignment
	List<CidrAssignmentPojo>getCidrAssignmentsForUserLoggedIn() throws RpcException;
	CidrAssignmentQueryResultPojo getCidrAssignmentsForFilter(CidrAssignmentQueryFilterPojo filter) throws RpcException;
	CidrAssignmentPojo createCidrAssignment(CidrAssignmentPojo cidrAssignment) throws RpcException;
	CidrAssignmentPojo updateCidrAssignment(CidrAssignmentPojo cidrAssignment) throws RpcException;
	void deleteCidrAssignment(CidrAssignmentPojo cidr) throws RpcException;
	List<VpcPojo> getVpcsForAccount(String accountId) throws RpcException;
	List<CidrPojo> getUnassignedCidrs() throws RpcException;
	
	// CidrAssignmentSummary
	CidrAssignmentSummaryQueryResultPojo getCidrAssignmentSummariesForFilter(CidrAssignmentSummaryQueryFilterPojo filter) throws RpcException;
	
	// VPC
	List<VpcPojo>getVpcsForUserLoggedIn() throws RpcException;
	VpcQueryResultPojo getVpcsForFilter(VpcQueryFilterPojo filter) throws RpcException;
//	VpcPojo generateVpc(VpcRequisitionPojo vpcRequisition) throws RpcException;
	VpcPojo updateVpc(VpcPojo vpc) throws RpcException;
	VpcPojo registerVpc(VpcPojo vpc) throws RpcException;
	void deleteVpc(VpcPojo vpc) throws RpcException;
	List<String> getVpcTypeItems() throws RpcException;
	
	// VPCP
	VpcpQueryResultPojo getVpcpSummariesForFilter(VpcpQueryFilterPojo filter) throws RpcException;
	void deleteVpcp(VpcpPojo vpc) throws RpcException;
	VpcpPojo generateVpcp(VpcRequisitionPojo vpcRequisition) throws RpcException;
	VpcpPojo updateVpcp(VpcpPojo vpc) throws RpcException;
	List<String> getComplianceClassItems() throws RpcException;
	
	// Elastic IP
	ElasticIpQueryResultPojo getElasticIpsForFilter(ElasticIpQueryFilterPojo filter) throws RpcException;
	ElasticIpPojo createElasticIp(ElasticIpPojo elasticIp) throws RpcException;
	ElasticIpPojo deleteElasticIp(ElasticIpPojo elasticIp) throws RpcException;
	ElasticIpPojo updateElasticIp(ElasticIpPojo vpc) throws RpcException;
	boolean isElasticIpAssigned(ElasticIpPojo elasticIp) throws RpcException;
	ElasticIpAssignmentStatusPojo getElasticIpAssignmentStatusForElasticIp(ElasticIpPojo elasticIp) throws RpcException;
	
	// ElasticIpAssignment
	List<ElasticIpAssignmentPojo>getElasticIpAssignmentsForUserLoggedIn() throws RpcException;
	ElasticIpAssignmentQueryResultPojo getElasticIpAssignmentsForFilter(ElasticIpAssignmentQueryFilterPojo filter) throws RpcException;
	ElasticIpAssignmentPojo generateElasticIpAssignment(ElasticIpAssignmentRequisitionPojo elasticIpAssignment) throws RpcException;
	ElasticIpAssignmentPojo updateElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment) throws RpcException;
	void deleteElasticIpAssignment(ElasticIpAssignmentPojo cidr) throws RpcException;
	List<ElasticIpPojo> getUnassignedElasticIps() throws RpcException;
	
	// ElasticIpAssignmentSummary
	ElasticIpAssignmentSummaryQueryResultPojo getElasticIpAssignmentSummariesForFilter(ElasticIpAssignmentSummaryQueryFilterPojo filter) throws RpcException;
	
	// Bill and LineItem methods
	BillQueryResultPojo getBillsForFilter(BillQueryFilterPojo filter) throws RpcException;
	List<BillPojo> getCachedBillsForAccount(String accountId) throws RpcException;
	void refreshMasterBillData() throws RpcException;

	// AWS Services
	AWSServiceQueryResultPojo getServicesForFilter(AWSServiceQueryFilterPojo filter) throws RpcException;
	AWSServicePojo createService(AWSServicePojo service) throws RpcException;
	AWSServicePojo updateService(AWSServicePojo service) throws RpcException;
	void deleteService(AWSServicePojo service) throws RpcException;
	List<String> getAwsServiceStatusItems();
	List<String> getSiteServiceStatusItems();

	// UserNotifications
	UserNotificationQueryResultPojo getUserNotificationsForFilter(UserNotificationQueryFilterPojo filter) throws RpcException;
	UserNotificationPojo createUserNotification(UserNotificationPojo notification) throws RpcException;
	UserNotificationPojo updateUserNotification(UserNotificationPojo notification) throws RpcException;
	void deleteUserNotification(UserNotificationPojo notification) throws RpcException;
	boolean userHasUnreadNotifications(UserAccountPojo user) throws RpcException;
	int getNotificationCheckIntervalMillis() throws RpcException;
	void markAllUnreadNotificationsForUserAsRead(UserAccountPojo user) throws RpcException;
	
	// AccountNotifications
	AccountNotificationQueryResultPojo getAccountNotificationsForFilter(AccountNotificationQueryFilterPojo filter) throws RpcException;
	AccountNotificationPojo createAccountNotification(AccountNotificationPojo notification) throws RpcException;
	AccountNotificationPojo updateAccountNotification(AccountNotificationPojo notification) throws RpcException;
	void deleteAccountNotification(AccountNotificationPojo notification) throws RpcException;
	
	// Speed Chart
	SpeedChartQueryResultPojo getSpeedChartsForFilter(SpeedChartQueryFilterPojo filter) throws RpcException;
	SpeedChartPojo getSpeedChartForFinancialAccountNumber(String accountNumber) throws RpcException;

	// FirewallRule
	FirewallRuleQueryResultPojo getFirewallRulesForFilter(FirewallRuleQueryFilterPojo filter) throws RpcException;
	FirewallRulePojo createFirewallRule(FirewallRulePojo rule) throws RpcException;
	FirewallRulePojo updateFirewallRule(FirewallRulePojo rule) throws RpcException;
	void deleteFirewallRule(FirewallRulePojo rule) throws RpcException;
	
	// FirewallExceptionAddRequest
	FirewallExceptionAddRequestQueryResultPojo getFirewallExceptionAddRequestsForFilter(FirewallExceptionAddRequestQueryFilterPojo filter) throws RpcException;
	FirewallExceptionAddRequestPojo generateFirewallExceptionAddRequest(FirewallExceptionAddRequestRequisitionPojo rule) throws RpcException;
	FirewallExceptionAddRequestPojo updateFirewallExceptionAddRequest(FirewallExceptionAddRequestPojo rule) throws RpcException;
	void deleteFirewallExceptionAddRequest(FirewallExceptionAddRequestPojo rule) throws RpcException;

	// FirewallExceptionRemoveRequest
	FirewallExceptionRemoveRequestQueryResultPojo getFirewallExceptionRemoveRequestsForFilter(FirewallExceptionRemoveRequestQueryFilterPojo filter) throws RpcException;
	FirewallExceptionRemoveRequestPojo generateFirewallExceptionRemoveRequest(FirewallExceptionRemoveRequestRequisitionPojo rule) throws RpcException;
	FirewallExceptionRemoveRequestPojo updateFirewallExceptionRemoveRequest(FirewallExceptionRemoveRequestPojo rule) throws RpcException;
	void deleteFirewallExceptionRemoveRequest(FirewallExceptionRemoveRequestPojo rule) throws RpcException;

	List<String> getFirewallExceptionRequestComplianceClassItems() throws RpcException;
	FirewallExceptionRequestSummaryQueryResultPojo getFirewallExceptionRequestSummariesForFilter(FirewallExceptionRequestSummaryQueryFilterPojo filter) throws RpcException;

	// caching methods
	CidrPojo storeCidrInCacheForUser(String eppn, CidrPojo cidr);
	CidrPojo getCidrFromCacheForUser(String eppn);
	CidrAssignmentPojo storeCidrAssignmentInCacheForUser(String eppn, CidrAssignmentPojo cidrAssignment);
	CidrAssignmentPojo getCidrAssignmentFromCacheForUser(String eppn);
	
	// DirectoryPerson
	DirectoryPersonQueryResultPojo getDirectoryPersonsForFilter(DirectoryPersonQueryFilterPojo filter) throws RpcException;
	
	// FullPerson
	FullPersonQueryResultPojo getFullPersonsForFilter(FullPersonQueryFilterPojo filter) throws RpcException;
	
	// RoleAssignments
	RoleAssignmentQueryResultPojo getRoleAssignmentsForFilter(RoleAssignmentQueryFilterPojo filter) throws RpcException;
	RoleAssignmentPojo createRoleAssignmentForPersonInAccount(String publicId, String accountId, String roleName) throws RpcException;
	List<RoleAssignmentSummaryPojo> getRoleAssignmentsForAccounts(List<String> accountIds) throws RpcException;
	List<RoleAssignmentSummaryPojo> getRoleAssignmentsForAccount(String accountId) throws RpcException;
	void removeRoleAssignmentFromAccount(String accountId, RoleAssignmentPojo roleAssignment) throws RpcException;
	List<RoleAssignmentSummaryPojo> getCentralAdmins() throws RpcException;
	
	AWSServiceSummaryPojo getAWSServiceMap() throws RpcException;

	// user profile
	UserProfileQueryResultPojo getUserProfilesForFilter(UserProfileQueryFilterPojo filter) throws RpcException;
	UserProfilePojo createUserProfile(UserProfilePojo notification) throws RpcException;
	UserProfilePojo updateUserProfile(UserProfilePojo notification) throws RpcException;
	void deleteUserProfile(UserProfilePojo notification) throws RpcException;
	
	// user actions
	UserActionQueryResultPojo getUserActionsForFilter(UserActionQueryFilterPojo filter) throws RpcException;
	UserActionPojo createUserAction(UserActionPojo notification) throws RpcException;
	
	// terms of use
	TermsOfUseQueryResultPojo getTermsOfUseForFilter(TermsOfUseQueryFilterPojo filter) throws RpcException;
	TermsOfUseAgreementQueryResultPojo getTermsOfUseAgreementsForFilter(TermsOfUseAgreementQueryFilterPojo filter) throws RpcException;
	TermsOfUseAgreementPojo createTermsOfUseAgreement(TermsOfUseAgreementPojo notification) throws RpcException;
	TermsOfUseSummaryPojo getTermsOfUseSummaryForUser(UserAccountPojo user) throws RpcException;

	// security assessments
	ServiceSecurityAssessmentQueryResultPojo getSecurityAssessmentsForFilter(ServiceSecurityAssessmentQueryFilterPojo filter) throws RpcException;
	ServiceSecurityAssessmentPojo createSecurityAssessment(ServiceSecurityAssessmentPojo assessment) throws RpcException;
	ServiceSecurityAssessmentPojo updateSecurityAssessment(ServiceSecurityAssessmentPojo assessment) throws RpcException;
	void deleteSecurityAssessment(ServiceSecurityAssessmentPojo service) throws RpcException;
	List<String> getAssessmentStatusTypeItems();
	List<String> getRiskLevelTypeItems();
	List<String> getCounterMeasureStatusTypeItems();
	SecurityRiskDetectionQueryResultPojo getSecurityRiskDetectionsForFilter(SecurityRiskDetectionQueryFilterPojo filter) throws RpcException;
	SecurityAssessmentSummaryQueryResultPojo getSecurityAssessmentSummariesForFilter(SecurityAssessmentSummaryQueryFilterPojo filter) throws RpcException;
	List<String> getServiceControlTypeItems();
	List<String> getServiceControlImplementationTypeItems();

	// AccountProvisioningAuthorization
	AccountProvisioningAuthorizationQueryResultPojo getAccountProvisioningAuthorizationsForFilter(AccountProvisioningAuthorizationQueryFilterPojo filter) throws RpcException;
	PersonInfoSummaryPojo getPersonInfoSummaryForPublicId(String publicId) throws RpcException;
	
	// Incident
	IncidentQueryResultPojo getIncidentsForFilter(IncidentQueryFilterPojo filter) throws RpcException;
	void deleteIncident(IncidentPojo incident) throws RpcException;
	IncidentPojo generateIncident(IncidentRequisitionPojo incidentRequisition) throws RpcException;
	IncidentPojo updateIncident(IncidentPojo indident) throws RpcException;

	// StaticNatProvisioningSummary (provisioned and de-provisioned static nats)
	StaticNatProvisioningSummaryQueryResultPojo getStaticNatProvisioningSummariesForFilter(StaticNatProvisioningSummaryQueryFilterPojo filter) throws RpcException;
	StaticNatProvisioningQueryResultPojo getStaticNatProvisioningsForFilter(StaticNatProvisioningQueryFilterPojo filter) throws RpcException;
	StaticNatDeprovisioningQueryResultPojo getStaticNatDeprovisioningsForFilter(StaticNatDeprovisioningQueryFilterPojo filter) throws RpcException;
	
	// VpnConnectionProfile and VpnConnectionProfileAssignment
	VpnConnectionProfileQueryResultPojo getVpnConnectionProfilesForFilter(VpnConnectionProfileQueryFilterPojo filter) throws RpcException;
	VpnConnectionProfilePojo createVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile) throws RpcException;
	VpnConnectionProfilePojo deleteVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile) throws RpcException;
	VpnConnectionProfilePojo updateVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile) throws RpcException;
	VpnConnectionProfileAssignmentQueryResultPojo getVpnConnectionProfileAssignmentsForFilter(VpnConnectionProfileAssignmentQueryFilterPojo filter) throws RpcException;
	VpnConnectionQueryResultPojo getVpnConnectionsForFilter(VpnConnectionQueryFilterPojo filter) throws RpcException;

	// VPNCP
	VpnConnectionProvisioningQueryResultPojo getVpncpSummariesForFilter(VpnConnectionProvisioningQueryFilterPojo filter) throws RpcException;
	VpnConnectionProvisioningPojo generateVpncp(VpnConnectionRequisitionPojo vpncpRequisition) throws RpcException;
	VpnConnectionProvisioningPojo updateVpnConnectionProvisioning(VpnConnectionProvisioningPojo pojo) throws RpcException;
//	VpnConnectionDeprovisioningPojo generateVpnConnectionDeprovisioning(VpnConnectionRequisitionPojo requisition) throws RpcException;
//	VpnConnectionDeprovisioningPojo generateVpnConnectionDeprovisioning(VpnConnectionPojo vpnConnection) throws RpcException;
	VpnConnectionDeprovisioningPojo generateVpnConnectionDeprovisioning(VpnConnectionProfileAssignmentPojo assignment) throws RpcException;
	VpnConnectionDeprovisioningPojo updateVpnConnectionDeprovisioning(VpnConnectionDeprovisioningPojo pojo) throws RpcException;

	VpnConnectionProfileAssignmentPojo generateVpnConnectionProfileAssignment(VpnConnectionProfileAssignmentRequisitionPojo requisition) throws RpcException;
	VpnConnectionProfileAssignmentPojo createVpnConnectionProfileAssignment(VpnConnectionProfileAssignmentPojo profileAssignment) throws RpcException;
	VpnConnectionProfileAssignmentPojo deleteVpnConnectionProfileAssignment(VpnConnectionProfileAssignmentPojo vpnConnectionProfileAssignment) throws RpcException;
	
	// TKI Client Download
//	AmazonS3AccessWrapperPojo getTkiClientS3AccessWrapper() throws RpcException;
	List<AWSRegionPojo> getAwsRegionItems();

	// console features
	ConsoleFeatureQueryResultPojo getConsoleFeaturesForFilter(ConsoleFeatureQueryFilterPojo filter) throws RpcException;
	ConsoleFeatureQueryResultPojo getCachedConsoleFeaturesForUserLoggedIn() throws RpcException;
	void saveConsoleFeatureInCacheForUser(ConsoleFeaturePojo service, UserAccountPojo user) throws RpcException;
	long getCurrentSystemTime();
	
	// Emory CIMP Specific methods
	boolean isCimpInstance() throws RpcException;
	String getFinancialAccountFieldLabel() throws RpcException;
	
	// Resource Tagging Profile
	ResourceTaggingProfileQueryResultPojo getResourceTaggingProfilesForFilter(ResourceTaggingProfileQueryFilterPojo filter) throws RpcException;
	ResourceTaggingProfilePojo createResourceTaggingProfile(ResourceTaggingProfilePojo account) throws RpcException;
	ResourceTaggingProfilePojo updateResourceTaggingProfile(ResourceTaggingProfilePojo account) throws RpcException;
	void updateResourceTaggingProfiles(List<ResourceTaggingProfilePojo> profile) throws RpcException;
	void deleteResourceTaggingProfile(boolean deleteAllMatchingProfiles, ResourceTaggingProfilePojo account) throws RpcException;

	PropertiesPojo getPropertiesForMenu(String menuId) throws RpcException;
	List<PropertyPojo> getUserProfileProperties() throws RpcException;
	List<AccountSpeedChartPojo> getFinancialAccountsForUser(UserAccountPojo user) throws RpcException;
	boolean isUserAssociatedToBadSpeedTypes(UserAccountPojo user) throws RpcException;
	RiskCalculationPropertiesPojo getPropertiesForRiskCalculation(String calculationName) throws RpcException;
	List<RiskCalculationPropertiesPojo> getRiskCalculationProperties() throws RpcException;

	SecurityRiskDetectionPojo generateSrd(SecurityRiskDetectionRequisitionPojo srdRequisition) throws RpcException;
	List<SecurityRiskDetectionPojo> generateSrds(List<SecurityRiskDetectionRequisitionPojo> srdRequisitions) throws RpcException;
	
	// Account Provisioning/Deprovisioning
	AccountProvisioningQueryResultPojo getAccountProvisioningSummariesForFilter(AccountProvisioningQueryFilterPojo filter) throws RpcException;
	AccountDeprovisioningPojo generateAccountDeprovisioning(AccountDeprovisioningRequisitionPojo requisition) throws RpcException;

	PropertiesPojo getSiteSpecificTextProperties() throws RpcException;

	// Role Provisioning/Deprovisioning
	RoleProvisioningQueryResultPojo getRoleProvisioningSummariesForFilter(RoleProvisioningQueryFilterPojo filter) throws RpcException;
	RoleProvisioningPojo generateRoleProvisioning(RoleProvisioningRequisitionPojo requisition) throws RpcException;
	RoleDeprovisioningPojo generateRoleDeprovisioning(RoleDeprovisioningRequisitionPojo requisition) throws RpcException;
	
	// CustomRoles
	CustomRoleQueryResultPojo getCustomRolesForFilter(CustomRoleQueryFilterPojo filter) throws RpcException;
	String getAwsConsoleURL() throws RpcException;
	boolean isExistingCustomRoleInAccount(String accountId, String roleName) throws RpcException;

}
