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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
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
import edu.emory.oit.vpcprovisioning.shared.FullPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.RpcException;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryResultPojo;

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

	// ReleaseInfo
	ReleaseInfo getReleaseInfo() throws RpcException;
	
	// logging
	void logMessage(String message) throws RpcException;
	
	// Identity services
	DirectoryMetaDataPojo getDirectoryMetaDataForPublicId(String netId) throws RpcException;

	// UserAccount services (user logged in)
	UserAccountPojo getUserLoggedIn() throws RpcException;
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
	VpcPojo generateVpc(VpcRequisitionPojo vpcRequisition) throws RpcException;
	VpcPojo updateVpc(VpcPojo vpc) throws RpcException;
	VpcPojo registerVpc(VpcPojo vpc) throws RpcException;
	void deleteVpc(VpcPojo vpc) throws RpcException;
	List<String> getVpcTypeItems() throws RpcException;
	
	// VPCP
	VpcpQueryResultPojo getVpcpsForFilter(VpcpQueryFilterPojo filter) throws RpcException;
	void deleteVpcp(VpcpPojo vpc) throws RpcException;
	VpcpPojo generateVpcp(VpcRequisitionPojo vpcRequisition) throws RpcException;
	VpcpPojo updateVpcp(VpcpPojo vpc) throws RpcException;
	List<String> getComplianceClassItems() throws RpcException;
	
	// Elastic IP
	ElasticIpQueryResultPojo getElasticIpsForFilter(ElasticIpQueryFilterPojo filter) throws RpcException;
	ElasticIpPojo createElasticIp(ElasticIpPojo elasticIp) throws RpcException;
	void deleteElasticIpSummary(ElasticIpSummaryPojo elasticIpSummary) throws RpcException;
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
	List<String> getServiceStatusItems();

	// Notifications
	NotificationQueryResultPojo getNotificationsForFilter(NotificationQueryFilterPojo filter) throws RpcException;
	NotificationPojo createNotification(NotificationPojo notification) throws RpcException;
	NotificationPojo updateNotification(NotificationPojo notification) throws RpcException;
	void deleteNotification(NotificationPojo notification) throws RpcException;
	
	// Speed Chart
	SpeedChartQueryResultPojo getSpeedChartsForFilter(SpeedChartQueryFilterPojo filter) throws RpcException;
	SpeedChartPojo getSpeedChartForFinancialAccountNumber(String accountNumber) throws RpcException;

	// FirewallRule
	FirewallRuleQueryResultPojo getFirewallRulesForFilter(FirewallRuleQueryFilterPojo filter) throws RpcException;
	FirewallRulePojo createFirewallRule(FirewallRulePojo rule) throws RpcException;
	FirewallRulePojo updateFirewallRule(FirewallRulePojo rule) throws RpcException;
	void deleteFirewallRule(FirewallRulePojo rule) throws RpcException;
	
	// FirewallExceptionRequest
	FirewallExceptionRequestQueryResultPojo getFirewallExceptionRequestsForFilter(FirewallExceptionRequestQueryFilterPojo filter) throws RpcException;
	FirewallExceptionRequestPojo createFirewallExceptionRequest(FirewallExceptionRequestPojo rule) throws RpcException;
	FirewallExceptionRequestPojo updateFirewallExceptionRequest(FirewallExceptionRequestPojo rule) throws RpcException;
	void deleteFirewallExceptionRequest(FirewallExceptionRequestPojo rule) throws RpcException;

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
	List<RoleAssignmentSummaryPojo> getRoleAssignmentsForAccount(String accountId) throws RpcException;
	void removeRoleAssignmentFromAccount(String accountId, RoleAssignmentPojo roleAssignment) throws RpcException;
	List<RoleAssignmentSummaryPojo> getCentralAdmins() throws RpcException;
	
	HashMap<String, List<AWSServicePojo>> getAWSServiceMap() throws RpcException;

}
