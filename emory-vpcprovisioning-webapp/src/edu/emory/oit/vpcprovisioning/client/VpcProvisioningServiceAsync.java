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
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentStatusPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRulePojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FirewallRuleQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.NotificationQueryResultPojo;
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

public interface VpcProvisioningServiceAsync {
	// START TEMPORARY
	void testPropertyReload(AsyncCallback<String> callback);
	// END TEMPORARY
	
//	void getLoginURL(AsyncCallback<String> callback);
	void logMessage(String message, AsyncCallback<Void> callback);
	
	// Identity services
	void getDirectoryMetaDataForNetId(String netId, AsyncCallback<DirectoryMetaDataPojo> callback);

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
	void generateVpc(VpcRequisitionPojo vpcRequisition, AsyncCallback<VpcPojo> callback);
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
	void deleteElasticIp(ElasticIpPojo cidr, AsyncCallback<Void> callback);
	void isElasticIpAssigned(ElasticIpPojo cidr, AsyncCallback<Boolean> callback);
	void getElasticIpAssignmentStatusForElasticIp(ElasticIpPojo cidr, AsyncCallback<ElasticIpAssignmentStatusPojo> callback);

	// ElasticIpAssignment
	void getElasticIpAssignmentsForUserLoggedIn(AsyncCallback<List<ElasticIpAssignmentPojo>> callback);
	void getElasticIpAssignmentsForFilter(ElasticIpAssignmentQueryFilterPojo filter, AsyncCallback<ElasticIpAssignmentQueryResultPojo> callback);
	void createElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment, AsyncCallback<ElasticIpAssignmentPojo> callback);
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
	
	// notifications
	void getNotificationsForFilter(NotificationQueryFilterPojo filter, AsyncCallback<NotificationQueryResultPojo> callback);
	void createNotification(NotificationPojo notification, AsyncCallback<NotificationPojo> callback);
	void updateNotification(NotificationPojo notification, AsyncCallback<NotificationPojo> callback);
	void deleteNotification(NotificationPojo notification, AsyncCallback<Void> callback);
	
	// FirewallRule
	void getFirewallRulesForFilter(FirewallRuleQueryFilterPojo filter, AsyncCallback<FirewallRuleQueryResultPojo> callback);
	void createFirewallRule(FirewallRulePojo rule, AsyncCallback<FirewallRulePojo> callback);
	void updateFirewallRule(FirewallRulePojo rule, AsyncCallback<FirewallRulePojo> callback);
	void deleteFirewallRule(FirewallRulePojo rule, AsyncCallback<Void> callback);

}
