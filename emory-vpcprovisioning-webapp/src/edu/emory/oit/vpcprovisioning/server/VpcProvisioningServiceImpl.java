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
package edu.emory.oit.vpcprovisioning.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.openeai.config.AppConfig;
import org.openeai.config.EnterpriseConfigurationObjectException;
import org.openeai.config.EnterpriseFieldException;
import org.openeai.jms.producer.PointToPointProducer;
import org.openeai.jms.producer.ProducerPool;
import org.openeai.moa.ActionableEnterpriseObject;
import org.openeai.moa.EnterpriseObjectCreateException;
import org.openeai.moa.EnterpriseObjectDeleteException;
import org.openeai.moa.EnterpriseObjectGenerateException;
import org.openeai.moa.EnterpriseObjectQueryException;
import org.openeai.moa.EnterpriseObjectUpdateException;
import org.openeai.moa.XmlEnterpriseObject;
import org.openeai.moa.XmlEnterpriseObjectException;
import org.openeai.moa.objects.resources.Datetime;
import org.openeai.moa.objects.resources.v1_0.Parameter;
import org.openeai.moa.objects.resources.v1_0.QueryLanguage;
import org.openeai.threadpool.ThreadPool;
import org.openeai.threadpool.ThreadPoolException;
import org.openeai.transport.RequestService;
import org.openeai.utils.config.AppConfigFactory;
import org.openeai.utils.config.AppConfigFactoryException;
import org.openeai.utils.config.SimpleAppConfigFactory;

import com.amazon.aws.moa.jmsobjects.billing.v1_0.Bill;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.Account;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.AccountNotification;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.AccountProvisioningAuthorization;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.VirtualPrivateCloud;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.VirtualPrivateCloudProvisioning;
import com.amazon.aws.moa.jmsobjects.security.v1_0.SecurityRiskDetection;
import com.amazon.aws.moa.jmsobjects.services.v1_0.ServiceSecurityAssessment;
import com.amazon.aws.moa.jmsobjects.user.v1_0.TermsOfUse;
import com.amazon.aws.moa.jmsobjects.user.v1_0.TermsOfUseAgreement;
import com.amazon.aws.moa.jmsobjects.user.v1_0.UserNotification;
import com.amazon.aws.moa.jmsobjects.user.v1_0.UserProfile;
import com.amazon.aws.moa.objects.resources.v1_0.AccountNotificationQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.AccountProvisioningAuthorizationQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.AccountQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.Annotation;
import com.amazon.aws.moa.objects.resources.v1_0.BillQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.Countermeasure;
import com.amazon.aws.moa.objects.resources.v1_0.DetectedSecurityRisk;
import com.amazon.aws.moa.objects.resources.v1_0.EmailAddress;
import com.amazon.aws.moa.objects.resources.v1_0.LineItem;
import com.amazon.aws.moa.objects.resources.v1_0.ProvisioningStep;
import com.amazon.aws.moa.objects.resources.v1_0.RemediationResult;
import com.amazon.aws.moa.objects.resources.v1_0.SecurityRisk;
import com.amazon.aws.moa.objects.resources.v1_0.SecurityRiskDetectionQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceControl;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceGuideline;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceSecurityAssessmentQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTest;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTestPlan;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTestRequirement;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTestStep;
import com.amazon.aws.moa.objects.resources.v1_0.TermsOfUseAgreementQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.TermsOfUseQuerySpecification;
// TODO: this should be actionable so the aws-moa is currently not correct
import com.amazon.aws.moa.objects.resources.v1_0.UserAction;
import com.amazon.aws.moa.objects.resources.v1_0.UserNotificationQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.UserProfileQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.VirtualPrivateCloudProvisioningQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.VirtualPrivateCloudQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.VirtualPrivateCloudRequisition;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.oracle.peoplesoft.moa.jmsobjects.finance.v1_0.SPEEDCHART;
import com.oracle.peoplesoft.moa.objects.resources.v1_0.SPEEDCHART_QUERY;
import com.paloaltonetworks.moa.jmsobjects.firewall.v1_0.FirewallRule;
import com.paloaltonetworks.moa.objects.resources.v1_0.Application;
import com.paloaltonetworks.moa.objects.resources.v1_0.Category;
import com.paloaltonetworks.moa.objects.resources.v1_0.FirewallRuleQuerySpecification;
import com.paloaltonetworks.moa.objects.resources.v1_0.From;
import com.paloaltonetworks.moa.objects.resources.v1_0.Group;
import com.paloaltonetworks.moa.objects.resources.v1_0.HipProfiles;
import com.paloaltonetworks.moa.objects.resources.v1_0.ProfileSetting;
import com.paloaltonetworks.moa.objects.resources.v1_0.Service;
import com.paloaltonetworks.moa.objects.resources.v1_0.Source;
import com.paloaltonetworks.moa.objects.resources.v1_0.SourceUser;
import com.paloaltonetworks.moa.objects.resources.v1_0.Tag;
import com.paloaltonetworks.moa.objects.resources.v1_0.To;
import com.service_now.moa.jmsobjects.customrequests.v1_0.FirewallExceptionAddRequest;
import com.service_now.moa.jmsobjects.customrequests.v1_0.FirewallExceptionRemoveRequest;
import com.service_now.moa.jmsobjects.servicedesk.v2_0.Incident;
import com.service_now.moa.objects.resources.v1_0.FirewallExceptionAddRequestQuerySpecification;
import com.service_now.moa.objects.resources.v1_0.FirewallExceptionAddRequestRequisition;
import com.service_now.moa.objects.resources.v1_0.FirewallExceptionRemoveRequestQuerySpecification;
import com.service_now.moa.objects.resources.v1_0.FirewallExceptionRemoveRequestRequisition;
import com.service_now.moa.objects.resources.v2_0.AssignedTo;
import com.service_now.moa.objects.resources.v2_0.ConfigurationItem;
import com.service_now.moa.objects.resources.v2_0.IncidentQuerySpecification;
import com.service_now.moa.objects.resources.v2_0.IncidentRequisition;
import com.service_now.moa.objects.resources.v2_0.PersonalName;
import com.service_now.moa.objects.resources.v2_0.ServiceOwner;
import com.service_now.moa.objects.resources.v2_0.SupportGroup;

import edu.emory.moa.jmsobjects.identity.v1_0.DirectoryPerson;
import edu.emory.moa.jmsobjects.identity.v1_0.Role;
import edu.emory.moa.jmsobjects.identity.v1_0.RoleAssignment;
import edu.emory.moa.jmsobjects.identity.v2_0.Employee;
import edu.emory.moa.jmsobjects.identity.v2_0.FullPerson;
import edu.emory.moa.jmsobjects.identity.v2_0.NetworkIdentity;
import edu.emory.moa.jmsobjects.identity.v2_0.Person;
import edu.emory.moa.jmsobjects.identity.v2_0.SponsoredPerson;
import edu.emory.moa.jmsobjects.identity.v2_0.Student;
import edu.emory.moa.jmsobjects.network.v1_0.Cidr;
import edu.emory.moa.jmsobjects.network.v1_0.CidrAssignment;
import edu.emory.moa.jmsobjects.network.v1_0.ElasticIp;
import edu.emory.moa.jmsobjects.network.v1_0.ElasticIpAssignment;
import edu.emory.moa.jmsobjects.network.v1_0.StaticNatDeprovisioning;
import edu.emory.moa.jmsobjects.network.v1_0.StaticNatProvisioning;
import edu.emory.moa.jmsobjects.network.v1_0.VpnConnectionDeprovisioning;
import edu.emory.moa.jmsobjects.network.v1_0.VpnConnectionProfile;
import edu.emory.moa.jmsobjects.network.v1_0.VpnConnectionProfileAssignment;
import edu.emory.moa.jmsobjects.network.v1_0.VpnConnectionProvisioning;
import edu.emory.moa.objects.resources.v1_0.AssociatedCidr;
import edu.emory.moa.objects.resources.v1_0.CidrAssignmentQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.CidrQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.DirectoryPersonQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.ElasticIpAssignmentQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.ElasticIpQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.ElasticIpRequisition;
import edu.emory.moa.objects.resources.v1_0.Email;
import edu.emory.moa.objects.resources.v1_0.ExplicitIdentityDNs;
import edu.emory.moa.objects.resources.v1_0.Property;
import edu.emory.moa.objects.resources.v1_0.RoleAssignmentQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.RoleAssignmentRequisition;
import edu.emory.moa.objects.resources.v1_0.RoleDNs;
import edu.emory.moa.objects.resources.v1_0.StaticNatDeprovisioningQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.StaticNatProvisioningQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.TunnelProfile;
import edu.emory.moa.objects.resources.v1_0.VpnConnectionDeprovisioningQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.VpnConnectionProfileAssignmentQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.VpnConnectionProfileAssignmentRequisition;
import edu.emory.moa.objects.resources.v1_0.VpnConnectionProfileQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.VpnConnectionProvisioningQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.VpnConnectionRequisition;
import edu.emory.moa.objects.resources.v2_0.FullPersonQuerySpecification;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.*;

@Path("vpcpHealthCheck")
@SuppressWarnings("serial")
public class VpcProvisioningServiceImpl extends RemoteServiceServlet implements VpcProvisioningService {
	// for supporting data queries and authorization
	private static final String ELASTIC_IP_SERVICE_NAME = "ElasticIpRequestService";
	private static final String IDM_SERVICE_NAME = "IDMRequestService";
	private static final String DIRECTORY_SERVICE_NAME = "DirectoryRequestService";
	private static final String SERVICE_NOW_SERVICE_NAME = "ServiceNowRequestService";
	private static final String IDENTITY_SERVICE_NAME = "IdentityRequestService";
	private static final String PEOPLE_SOFT_SERVICE_NAME = "PeopleSoftRequestService";
	private static final String AWS_SERVICE_NAME = "AWSRequestService";
//	private static final String CIDR_SERVICE_NAME = "CidrRequestService";
//	private static final String AUTHZ_SERVICE_NAME = "AuthorizationRequestService";
	private static final String FIREWALL_SERVICE_NAME = "FirewallRequestService";
	private static final String SRD_SERVICE_NAME = "SrdRequestService";
	private static final String NETWORK_OPS_SERVICE_NAME = "NetworkOpsRequestService";
	private static final String GENERAL_PROPERTIES = "GeneralProperties";
	private static final String AWS_URL_PROPERTIES = "AWSUrlProperties";
	private static final String AWS_REGION_PROPERTIES = "AWSRegionProperties";
	private static final String ROLE_ASSIGNMENT_PROPERTIES = "RoleAssignmentProperties";
	private static final String AWS_SERVICE_STATUS_PROPERTIES = "AwsServiceStatusProperties";
	private static final String SITE_SERVICE_STATUS_PROPERTIES = "SiteServiceStatusProperties";
	private static final String USER_PROFILE_PROPERTIES = "UserProfileProperties";
//	private static String LOGTAG = "[" + VpcProvisioningServiceImpl.class.getSimpleName()
//			+ "]";
	private Logger log = Logger.getLogger(getClass().getName());
	private boolean useEsbService = true;
	private boolean useShibboleth = true;
	private boolean useAuthzService = true;
	private static AppConfig appConfig = null;
	Properties roleAssignmentProps = null;
	Properties generalProps = null;
	private String configDocPath = null;
	private String appId = null;
	private ProducerPool identityServiceProducerPool = null;
	private ProducerPool idmProducerPool = null;
	private ProducerPool directoryProducerPool = null;
	private ProducerPool awsProducerPool = null;
	private ProducerPool peopleSoftProducerPool = null;
	private ProducerPool cidrProducerPool = null;
//	private ProducerPool authzProducerPool = null;
	private ProducerPool firewallProducerPool = null;
	private ProducerPool serviceNowProducerPool = null;
	private ProducerPool elasticIpProducerPool = null;
	private ProducerPool srdProducerPool = null;
	private ProducerPool networkOpsProducerPool = null;
	private Object lock = new Object();
	static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private boolean initializing = false;
	private int defaultRequestTimeoutInterval = 20000;
	// default, 8 hours in prod
	private int sessionTimeoutIntervalSeconds = 28800;
	private int userCnt=0;
	private boolean manageSessionLocally=false;
//	private String baseLoginURL = null;
	private String applicationEnvironment=null;
	
	boolean fakeVpcpGen = false;
	HashMap<String, VpcpPojo> vpcpMap = new HashMap<String, VpcpPojo>();
	
	private List<BillPojo> masterBills = new java.util.ArrayList<BillPojo>();
	// Key is account id, List is a list of Bills for that account id
	private HashMap<String, List<BillPojo>> billsByAccount = new HashMap<String, List<BillPojo>>();
	AWSServiceSummaryPojo serviceSummary;

	// temporary
	List<UserNotificationPojo> notificationList = new java.util.ArrayList<UserNotificationPojo>();
	List<AWSServicePojo> serviceList = new java.util.ArrayList<AWSServicePojo>();


	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		initAppConfig();

		ReleaseInfo ri = this.getReleaseInfo();
		info(ri.toString() + " initialization complete.");
	}

	private void info(String msg) {
		log.info(msg);
	}

	private void initAppConfig() throws ServletException {
		info("initAppConfig started...");
		synchronized (lock) {
			if (initializing) {
				System.out.println("initialization is already underway...");
				return;
			} else {
				initializing = true;
			}
		}

		try {
			String s = this.getServletConfig()
					.getInitParameter("useEsbService");
			if (s == null) {
				s = "true";
			}
			useEsbService = Boolean.parseBoolean(s);

			configDocPath = this.getServletConfig().getInitParameter(
					"configDocPath");
			if (configDocPath == null) {
				configDocPath = "Unknown configDocPath";
			}

			appId = this.getServletConfig().getInitParameter("appId");
			if (appId == null) {
				appId = "Unknown appId";
			}

			info("useEsbService is: " + useEsbService);
			info("configDocPath is: " + configDocPath);
			info("appId is: " + appId);
		} 
		catch (Throwable e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		if (!useEsbService) {
			System.out.println("Not using AppConfig.");
			return;
		}

		if (getAppConfig() != null) {
			System.out.println("App config is already initialized...");
			return;
		}

		try {
			info("Initializing AppConfig...");
			AppConfigFactory acf = new SimpleAppConfigFactory();
			setAppConfig(acf.makeAppConfig(configDocPath, appId));
			info("AppConfig initialized...");
			peopleSoftProducerPool = (ProducerPool) getAppConfig().getObject(
					PEOPLE_SOFT_SERVICE_NAME);
			identityServiceProducerPool = (ProducerPool) getAppConfig().getObject(
					IDENTITY_SERVICE_NAME);
			awsProducerPool = (ProducerPool) getAppConfig().getObject(
					AWS_SERVICE_NAME);
			firewallProducerPool = (ProducerPool) getAppConfig().getObject(
					FIREWALL_SERVICE_NAME);
			serviceNowProducerPool = (ProducerPool) getAppConfig().getObject(
					SERVICE_NOW_SERVICE_NAME);
			directoryProducerPool = (ProducerPool) getAppConfig().getObject(
					DIRECTORY_SERVICE_NAME);
			idmProducerPool = (ProducerPool) getAppConfig().getObject(
					IDM_SERVICE_NAME);
			elasticIpProducerPool = (ProducerPool) getAppConfig().getObject(
					ELASTIC_IP_SERVICE_NAME);
			srdProducerPool = (ProducerPool) getAppConfig().getObject(
					SRD_SERVICE_NAME);
			networkOpsProducerPool = (ProducerPool) getAppConfig().getObject(
					NETWORK_OPS_SERVICE_NAME);
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			roleAssignmentProps = getAppConfig().getProperties(ROLE_ASSIGNMENT_PROPERTIES);
			
			this.getAWSServiceMap();
			
			String redirect = generalProps.getProperty("manageSessionLocally", "false");
			manageSessionLocally = Boolean.parseBoolean(redirect);
			
			String shib = generalProps.getProperty("useShibboleth", "true");
			useShibboleth = Boolean.parseBoolean(shib);

			String authz = generalProps.getProperty("useAuthzService", "true");
			useAuthzService = Boolean.parseBoolean(authz);
			info("useAuthzService is: " + useAuthzService);
			
			applicationEnvironment = generalProps.getProperty("applicationEnvironment", "DEV");
//			refreshMasterBillData();
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} 
		catch (AppConfigFactoryException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		info("initAppConfig ended...");
	}

	public int getDefaultRequestTimeoutInterval() {
		return defaultRequestTimeoutInterval;
	}

	public void setDefaultRequestTimeoutInterval(
			int defaultRequestTimeoutInterval) {
		this.defaultRequestTimeoutInterval = defaultRequestTimeoutInterval;
	}

	@SuppressWarnings("unchecked")
	private List<ActionableEnterpriseObject> doGenerate(ActionableEnterpriseObject aeo, XmlEnterpriseObject seedXeo, RequestService reqSvc) throws JMSException,
	EnterpriseObjectGenerateException, RpcException {

		String authUserId = this.getAuthUserIdForHALS();
		aeo.getAuthentication().setAuthUserId(authUserId);
		info("[doGenerate] AuthUserId is: " + aeo.getAuthentication().getAuthUserId());

		List<ActionableEnterpriseObject> result = aeo.generate(seedXeo, reqSvc);
		return result;
	}
	private void doCreate(ActionableEnterpriseObject aeo, RequestService reqSvc) throws JMSException,
			EnterpriseObjectCreateException, RpcException {
		
		String authUserId = this.getAuthUserIdForHALS();
		aeo.getAuthentication().setAuthUserId(authUserId);
		info("[doCreate] AuthUserId is: " + aeo.getAuthentication().getAuthUserId());
		
		aeo.create(reqSvc);
	}
    private void doUpdate(ActionableEnterpriseObject aeo, RequestService reqSvc) throws JMSException, EnterpriseObjectUpdateException {
		String authUserId = this.getAuthUserIdForHALS();
		if (authUserId != null) {
			aeo.getAuthentication().setAuthUserId(authUserId);
		}
		info("[doUpdate] AuthUserId is: " + aeo.getAuthentication().getAuthUserId());
        aeo.update(reqSvc);
    }
    private void doDelete(ActionableEnterpriseObject aeo, RequestService reqSvc) throws JMSException, EnterpriseObjectDeleteException {
		String authUserId = this.getAuthUserIdForHALS();
		if (authUserId != null) {
			aeo.getAuthentication().setAuthUserId(authUserId);
		}
		info("[doDelete] AuthUserId is: " + aeo.getAuthentication().getAuthUserId());
        aeo.delete("purge", reqSvc);
    }

	private String getAuthUserIdForHALS(UserAccountPojo userLoggedIn) throws RpcException {
		HttpServletRequest request = this.getThreadLocalRequest();
		String clientIp = request.getRemoteAddr();
		return userLoggedIn.getEppn() + "/" + clientIp;
	}
	private String getAuthUserIdForHALS() throws RpcException {
		try {
			UserAccountPojo user = (UserAccountPojo) Cache.getCache().get(
					Constants.USER_ACCOUNT + getCurrentSessionId());
			if (user == null) {
				// we have a session related issue...
				info("[getAuthUserIdForHALS] Couldn't retrieve user from the current session, "
					+ "trying to determine the user logged in again fresh.");
				// try to get the user from the shibboleth info again but don't refresh the roles
				user = this.getUserLoggedIn(false);
				if (user == null) {
					// we're really screwed now
					throw new RpcException("Could not determine the user logged in after two attempts.  "
						+ "Please close all browsers and restart with a fresh session.  If the problem "
						+ "persists, please contact the Help Desk.");
				}
			}
			return this.getAuthUserIdForHALS(user); 
		}
		catch (RpcException e) {
			if (e.getMessage().equals(Constants.SESSION_TIMEOUT)) {
				throw new RpcException(Constants.SESSION_TIMEOUT);
			}
			else {
				throw e;
			}
		}
	}

	private UserAccountPojo getUserLoggedIn(boolean refreshRoles) {
		// if the user account has been cached, we'll use it
		info("checking cache for existing user...");
		UserAccountPojo user = (UserAccountPojo) Cache.getCache().get(
				Constants.USER_ACCOUNT + getCurrentSessionId());
		
		if (user != null) {
			if (this.isSessionValidForUser(user)) { 
				info("[found user in cache] user logged in is: " + user.getEppn());
				if (useAuthzService) {
					// get roles assigned to this user
					if (refreshRoles) {
						this.getRolesForUser(user);
					}
					if (user.getAccountRoles().size() == 0) {
						if (!refreshRoles) {
							// we'll go ahead and refresh the roles here just in case the user closed
							// their browser and they still have a shib session but no session in the app server
							this.getRolesForUser(user);
						}
						if (user.getAccountRoles().size() == 0) {
							// ERROR, user does not have any required permissions to use
							// this app
							info("user " + user.getEppn() + " is not authorized to use this application.");
							throw new RpcException("The user id being used to log " +
								"in is not authorized to use this application.  " +
								"Please contact your support representative to " +
								"gain access to this application.");
						}
					}
				}
				else {
					info("not configured to use authorization service, anyone "
							+ "who can authenticate will be allowed to use the "
							+ "app.");
				}

				this.createSession(user.getEppn());
				Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
				return user;
			}
			else {
				if (manageSessionLocally) {
					info("[found user in cache] user " + user.getEppn() + 
							" does not have a valid session any more.  Need to " +
							"re-authenticate here.");
					
					getThreadLocalRequest().getSession().setMaxInactiveInterval(0);
					getThreadLocalRequest().getSession().invalidate();
					
					// tell client to do redirect
					throw new RpcException(Constants.SESSION_TIMEOUT);
				}
				else {
					// TEMPORARY UNTIL LAZY SESSION IS TURNED ON:
					// ONCE LAZY SESSIONS ARE TURNED ON, WE'LL RE-DIRECT HERE
					info("[found user in cache] user " + user.getEppn() + 
							" does not have a valid session any more (locally).  " +
							"Temporarily creating a new session until lazy " +
							"sessions is active.");
					this.createSession(user.getEppn());
					Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
					return user;
				}
				
			}
		}
		else {
			info("no user currently logged in, checking for Shibboleth information");
			HttpServletRequest request = this.getThreadLocalRequest();

//			Enumeration<String> attrEnum = request.getAttributeNames();
//			while(attrEnum.hasMoreElements()) {
//				info("HTTP Attribute: " + request.getAttribute(attrEnum.nextElement()));
//			}
//			Enumeration<String> headerEnum = request.getHeaderNames();
//			while(headerEnum.hasMoreElements()) {
//				info("HTTP Header: " + request.getHeader(headerEnum.nextElement()));
//			}
			String eppn = (String) request.getHeader("eduPersonPrincipalName");
			if (eppn == null) {
				eppn = (String) request.getAttribute("eduPersonPrincipalName");
			}
			
			if (eppn != null) {
				info("found eppn in Shibboleth attributes.  eppn is: '" + eppn + "'");
				if (eppn.trim().length() == 0) {
					info("found shibboleth eppn attribute but value is blank.  This is an error.");
					throw new RpcException("There appears to be a problem with " +
						"your shibboleth session.  The required attribute " +
						"(eduPersonPrincipalName) was found in the session " +
						"information but the value is blank.  This is an error " +
						"and application use cannot proceed at this time.  " +
						"If this issue persists, please contact your application support team.");
				}
				user = new UserAccountPojo();
				user.setEppn(eppn);
				// 9/19/2018 - create session immediately even before we get their roles
//				Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
//				this.createSession(eppn);

				if (useAuthzService) {
					// get roles for the user
					if (refreshRoles) {
						this.getRolesForUser(user);
					}
					if (user.getAccountRoles().size() == 0) {
						if (!refreshRoles) {
							// we'll go ahead and refresh the roles here just in case the user closed
							// their browser and they still have a shib session but no session in the app server
							this.getRolesForUser(user);
						}
						if (user.getAccountRoles().size() == 0) {
							// ERROR, user does not have any required permissions to use
							// this app
							info("user " + user.getEppn() + " is not authorized to use this application.");
							throw new RpcException("The user id being used to log " +
								"in is not authorized to use this application.  " +
								"Please contact your support representative to " +
								"gain access to this application.");
						}
					}
				}
				else {
					info("not configured to use authorization service, anyone "
							+ "who can authenticate will be allowed to use the "
							+ "app.");
				}
				
				if (!this.hasSession(user.getEppn())) {
					info("[shib] user " + user.getEppn() + " has not " +
							"established a session yet but they have " +
							"authenticated with Shibboleth.  Creating local session.");
					this.createSession(eppn);
					Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
				}
				else {
					if (this.isSessionValidForUser(user)) { 
						info("[shib] user ' " + user.getEppn() + 
								" still has a valid session.  Processing can continue.");
						this.createSession(eppn);
						Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
					}
					else {
						if (manageSessionLocally) {
							info("[shib] user " + user.getEppn() + 
								" does not have a valid session any more.  " +
								"Need to re-direct here.");
							
							// tell client to do redirect
							throw new RpcException(Constants.SESSION_TIMEOUT);
						}
						else {
							info("[shib] user " + user.getEppn() + 
									" does not have a valid session any more (locally).  " +
									"Temporarily creating the session " +
									"until lazy sessions are turned on.  Once lazy " +
									"sessions are active, we'll re-direct here.");
								
							// TEMPORARY UNTIL LAZY SESSION IS TURNED ON:
							// ONCE LAZY SESSIONS ARE TURNED ON, WE'LL RE-DIRECT HERE
							this.createSession(eppn);
							Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
						}
					}
				}
			} 
			else {
				info("no eppn found in Shibboleth attributes");
				try {
					// do this here so config doc property changes are
					// reflected automatically
					generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
				} catch (EnterpriseConfigurationObjectException e) {
					e.printStackTrace();
				}
				String shib = generalProps.getProperty("useShibboleth", "true");
				info("useShibboleth from config doc: " + shib);
				useShibboleth = Boolean.parseBoolean(shib);
				String testUserEppn = generalProps.getProperty("testUserEppn", "jtjacks@emory.edu");
				String testUserPpid = generalProps.getProperty("testUserPpid", "P7247525");
				String testUserFirstName = generalProps.getProperty("testUserFirstName", "Tod");
				String testUserLastName = generalProps.getProperty("testUserLastName", "Jackson");

				if (useShibboleth) {
					// error condition, somehow they've got to the app
					// without going through Shibboleth
					info("application is configured to use Shibboleth " +
						"but no eppn attribute was found, this is an error");
					throw new RpcException(
							"No valid user logged in.  Please login.");
				} 
				else {
					// LOCAL TESTING:  create a dummy user account
					info("application is NOT configured to use Shibboleth.  " +
						"Using testUserEppn for development purposes");
					user = new UserAccountPojo();
					user.setEppn(testUserEppn);
					user.setPublicId(testUserPpid);
					PersonalNamePojo pnp = new PersonalNamePojo();
					pnp.setFirstName(testUserFirstName);
					pnp.setLastName(testUserLastName);
					user.setPersonalName(pnp);

					if (useAuthzService) {
						// get permissions
						user.setSuperUser(false);
						if (refreshRoles) {
							this.getRolesForUser(user);
						}
					}
					else {
						// give them all permissions
						user.setSuperUser(true);
					}


					boolean forceError = false;
					if (forceError) {
						info("user " + user.getEppn() + " is not authorized to use this application.");
						throw new RpcException("The user id being used to log " +
								"in is not authorized to use this application.  " +
								"Please contact your support representative to " +
								"gain access to this application.");
					}
					else {
						if (user.getAccountRoles().size() == 0) {
							// ERROR, user does not have any required permissions to use
							// this app
							info("user " + user.getEppn() + " is not authorized to use this application.");
							throw new RpcException("The user id being used to log " +
								"in is not authorized to use this application.  " +
								"Please contact your support representative to " +
								"gain access to this application.");
						}
					}
					
					if (userCnt == 0) {
						this.createSession(user.getEppn());
						Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
						userCnt++;
					}
					else {
						if (this.isSessionValidForUser(user)) { 
							info("[no-shib-testing] user ' " + user.getEppn() + " still has a valid session.");
							this.createSession(eppn);
							Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
							userCnt++;
						}
						else {
							if (manageSessionLocally) {
								// tell client to do redirect
								info("[no-shib-testing] user " + user.getEppn() + 
									" does not have a valid session any more.  Need " +
									"to re-authenticate here.");
								throw new RpcException(Constants.SESSION_TIMEOUT);
							}
							else {
								info("[no-shib-testing] user " + user.getEppn() + 
										" does not have a valid session any more.  Need " +
										"to re-authenticate here.  Processing will " +
										"continue since this section of code is just " +
										"for local testing.");
								this.createSession(eppn);
								Cache.getCache().put(Constants.USER_ACCOUNT + getCurrentSessionId(), user);
							}
						}
					}
				}
			}
		}

		return user;
	}
	@Override
	public UserAccountPojo getUserLoggedIn() throws RpcException {
		return this.getUserLoggedIn(true);
	}

	@Override
	public boolean isSessionValidForUser(UserAccountPojo user) {
		if (manageSessionLocally) {
			// check to see if the session has expired
			long currentTime = new java.util.Date().getTime();
			if (this.hasSession(user.getEppn())) {
				if (currentTime > 
					getThreadLocalRequest().getSession().getLastAccessedTime() + 
					(sessionTimeoutIntervalSeconds * 1000)) {
			
					info(user.getEppn() + " 's session has expired.");
					return false;
				}
				else {
					info(user.getEppn() + " 's session has NOT expired.");
					return true;
				}
			}
			else {
				info(user.getEppn() + " does not have a session.  Can't validate it.");
				return false;
			}
		}
		else {
			return hasSession(user.getEppn());
		}
	}

	private String getCurrentSessionId() {
		HttpServletRequest request = this.getThreadLocalRequest();
		if (request != null) {
			HttpSession session = request.getSession();
			if (session != null) {
				info("session is not null...");
				return session.getId();
			}
			else {
				info("session is null...");
				return UUID.uuid();
			}
		} 
		else {
			// this may happen if createUser is called outside of an HTTP
			// request
			info("request is null...");
			return UUID.uuid();
		}
	}

	void createSession(String userId) {
		// (12 hours)
//		info("Creating session for user " + userId + 
//			" with an expiration time of " + sessionTimeoutIntervalSeconds + 
//			" seconds.");
//		getThreadLocalRequest().getSession().setMaxInactiveInterval(sessionTimeoutIntervalSeconds);
		
		// never expires
		info("Creating session for user " + userId + 
				" with an expiration time of -1 (never expires)");
		getThreadLocalRequest().getSession().setMaxInactiveInterval(-1);
		getThreadLocalRequest().getSession().setAttribute("UserID", userId);
	}

	boolean hasSession(String Username) {
		// TODO: does this need to check the value of the UserID attribute and 
		// make sure it matches the Username passed in??
		if (getThreadLocalRequest().getSession().getAttribute("UserID") != null) {
			info(Username + " 's session does exist.");
			return true;
		} 
		else {
			info(Username + "'s session does not exist.");
			return false;
		}
	}

	@Override
	public void invalidateSessionForUser(String userId) {
		Cache.getCache().remove(userId);
		Cache.getCache().remove(Constants.USER_ACCOUNT + getCurrentSessionId());
		getThreadLocalRequest().getSession().removeAttribute("UserID");
		getThreadLocalRequest().getSession().invalidate();
	}

	protected void getRolesForUser(UserAccountPojo user) throws RpcException {
		// get the fullperson object for the user.principal
		// set the user's publicId
		// get the roleassignments for the user.publicId
		// for each roleassignment:
		//	- get the aws account id
		//  - TODO: verify the account exists in this series (environment)
		//	- get the role name associated to that account for this user
		//	- create a new AccountRolePojo, populate it with account id and role name
		//	- add the AccountRolePojo to the user passed in
		
		if (user.getPublicId() == null) {
			FullPersonQueryFilterPojo fp_filter = new FullPersonQueryFilterPojo();
			fp_filter.setNetId(user.getPrincipal());
			fp_filter.setUserLoggedIn(user);
			FullPersonQueryResultPojo fp_result = this.getFullPersonsForFilter(fp_filter);
			if (fp_result != null) {
				info("[getRolesForUser] got " + fp_result.getResults().size() + 
					" FullPerson objects back for net id: " + fp_filter.getNetId());
			}
			else {
				// error
				info("[getRolesForUser] null FullPersonQueryResultPojo.  This is bad.");
				throw new RpcException("Null FullPerson returned from ESB for NetId: " + user.getPrincipal());
			}
			FullPersonPojo fullPerson = fp_result.getResults().get(0);
			user.setPublicId(fullPerson.getPublicId());
			user.setPersonalName(fullPerson.getPerson().getPersonalName());
			info("[getRolesForUser] User public id is: " + user.getPublicId());
		}
		else {
			info("[getRolesForUser] no need to get full person object.");
		}
		
		try {
			roleAssignmentProps = getAppConfig().getProperties(ROLE_ASSIGNMENT_PROPERTIES);
			RoleAssignmentQueryFilterPojo ra_filter = new RoleAssignmentQueryFilterPojo();
			
			String idDn = roleAssignmentProps.getProperty("IdentityDN", "cn=PUBLIC_ID,ou=Users,ou=Data,o=EmoryDev");
			idDn = idDn.replaceAll(Constants.REPLACEMENT_VAR_PUBLIC_ID, user.getPublicId());
			ra_filter.setUserDN(idDn);
			ra_filter.setIdentityType("USER");
			ra_filter.setDirectAssignOnly(true);
			ra_filter.setUserLoggedIn(user);

			user.setAccountRoles(new java.util.ArrayList<AccountRolePojo>());
			RoleAssignmentQueryResultPojo ra_result = this.getRoleAssignmentsForFilter(ra_filter);
			if (ra_result != null) {
//				info("[getRolesForUser] got " + ra_result.getResults().size() + 
//					" RoleAssignment objects back for userDN: " + ra_filter.getUserDN());
			}
			else {
				// error
				info("[getRolesForUser] null RoleAssignmentQueryResultPojo.  This is bad.");
				return;
			}
			user.setAccountRoles(new java.util.ArrayList<AccountRolePojo>());

			for (RoleAssignmentPojo roleAssignment : ra_result.getResults()) {
				String roleDn = roleAssignment.getRoleDN();
//				info("[getRolesForUser] checking roldDN: " + roleDn);
				if (roleDn != null) {
					if (roleDn.indexOf(Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS) >= 0) {
						AccountRolePojo arp = new AccountRolePojo();
						arp.setRoleName(Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS);
						info("[getRolesForUser] adding AccountRolePojo " + arp.toString() + " to UserAccount logged in.");
						user.addAccountRole(arp);
						continue;
					}
					if (roleDn.indexOf(Constants.ROLE_NAME_EMORY_NETWORK_ADMINS) >= 0) {
						AccountRolePojo arp = new AccountRolePojo();
						arp.setRoleName(Constants.ROLE_NAME_EMORY_NETWORK_ADMINS);
						info("[getRolesForUser] adding AccountRolePojo " + arp.toString() + " to UserAccount logged in.");
						user.addAccountRole(arp);
						continue;
					}
					if (roleDn.indexOf("RGR_AWS") >= 0) {
						String[] cns = roleDn.split(",");
						String acctCn = cns[0];
						String[] idRoles = acctCn.split("-");
						if (idRoles.length != 3) {
							continue;
						}
						String acctId = idRoles[1];
						String roleName = idRoles[2];
						AccountPojo verifiedAcct = this.getAccountById(acctId); 
						if (acctId != null && verifiedAcct != null) {
							AccountRolePojo arp = new AccountRolePojo();
							arp.setAccountId(acctId);
							arp.setAccountName(verifiedAcct.getAccountName());
							arp.setRoleName(roleName);
							info("[getRolesForUser] adding AccountRolePojo " + arp.toString() + " to UserAccount logged in.");
							user.addAccountRole(arp);
						}
						else if (acctId == null && 
								(roleName.indexOf(Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS) >= 0 || 
								 roleName.indexOf(Constants.ROLE_NAME_RHEDCLOUD_AWS_CENTRAL_ADMIN) >= 0)) {
							
							AccountRolePojo arp = new AccountRolePojo();
							arp.setRoleName(roleName);
							info("[getRolesForUser] adding AccountRolePojo " + arp.toString() + " to UserAccount logged in.");
							user.addAccountRole(arp);
						}
						else {
							continue;
						}
					}
				}
			}
			
			info("[getRoleForUser] added " + user.getAccountRoles().size() + " AccountRoles to User");
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}
	
//	@SuppressWarnings("unchecked")
//	protected void getPermissionsForUser(UserAccountPojo user) throws RpcException {
//		info("getting permissions for " + user.getEppn());
//		user.getPermissions().clear();
//		try {
//			// preferred approach (1 query)...
//			AuthorizationQuerySpecification queryObject = (AuthorizationQuerySpecification) getAppConfig().getObject(Constants.MOA_AUTHORIZATION_QUERY_SPEC);
//			Authorization actionable = (Authorization) getAppConfig().getObject(Constants.MOA_AUTHORIZATION);
//			queryObject.setPrincipal(user.getEppn());
//			
//			for (String permission : Constants.PERMISSIONS) {
//				info("adding permission " + permission + " to the query spec");
//				queryObject.addPermission(permission);
//			}
//			
//			info("queryObject.toXmlString: " + queryObject.toXmlString());
//			List<Authorization> list = actionable.query(queryObject, getAuthzRequestService());
//			info("there were " + list.size() + " Authorization objects returned.");
//
//			if (list.size() > 0) {
//				for (Authorization authorization : (List<Authorization>) list) {
//					info("authorization has " + authorization.getPermissionLength() + " permissions in it.");
//					info("isAuthorized = " + authorization.getIsAuthorized());
//					info("Authorization.toXmlString: " + authorization.toXmlString());
//					if (authorization.getIsAuthorized().equals("true")) {
//						for (String perm : (List<String>)authorization.getPermission()) {
//							info("adding permission " + perm + " to UserAccountPojo...");
//							user.getPermissions().add(perm);
//						}
//					}
//					else {
//						for (String perm : (List<String>)authorization.getPermission()) {
//							info(user.getEppn() + " is NOT authorized for permission " + perm);
//						}
//					}
//				}
//			}

//			// work around (multiple queries, one for each permission)...
//			for (String permission : Constants.PERMISSIONS) {
//				AuthorizationQuerySpecification queryObject = (AuthorizationQuerySpecification) getAppConfig().getObject(Constants.MOA_AUTHORIZATION_QUERY_SPEC);
//				Authorization actionable = (Authorization) getAppConfig().getObject(Constants.MOA_AUTHORIZATION);
//				queryObject.setPrincipal(user.getEppn());
//				queryObject.addPermission(permission);
//				
////				info("queryObject.toXmlString: " + queryObject.toXmlString());
//				List<Authorization> list = actionable.query(queryObject, getAuthzRequestService());
////				info("there were " + list.size() + " Authorization objects returned.");
//
//				if (list.size() > 0) {
//					for (Authorization authorization : (List<Authorization>) list) {
////						info("authorization has " + authorization.getPermissionLength() + " permissions in it.");
////						info("isAuthorized = " + authorization.getIsAuthorized());
////						info("Authorization.toXmlString: " + authorization.toXmlString());
//						if (authorization.getIsAuthorized().equals("true")) {
//							for (String perm : (List<String>)authorization.getPermission()) {
//								info("adding permission " + perm + " to UserAccountPojo...");
//								user.getPermissions().add(perm);
//							}
//						}
//						else {
//							for (String perm : (List<String>)authorization.getPermission()) {
//								info(user.getEppn() + " is NOT authorized for permission " + perm);
//							}
//						}
//					}
//				}
//			}
//		} 
//		catch (Throwable t) {
//			t.printStackTrace();
//			throw new RpcException(t);
//		}
//	}

	@SuppressWarnings("unused")
	private void populateBillMoa(BillPojo pojo, Bill moa) throws EnterpriseFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		/*
		<!ELEMENT Bill (BillId?, PayerAccountId, BillDate, Type, LineItem+, 
		CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?)>
		 */
		moa.setBillId(pojo.getBillId());
		moa.setPayerAccountId(pojo.getPayerAccountId());
		
		org.openeai.moa.objects.resources.Date billDate = moa.newBillDate();
		this.populateDate(billDate, pojo.getBillDate());
		moa.setBillDate(billDate);
		
		moa.setType(pojo.getType());

		for (LineItemPojo pojoLi : pojo.getLineItems()) {
			LineItem moaLi = moa.newLineItem();
			this.populateLineItemMoa(pojoLi, moaLi);
			moa.addLineItem(moaLi);
		}
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}
	@SuppressWarnings("unchecked")
	private void populateBillPojo(Bill moa, BillPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setBillId(moa.getBillId());
		pojo.setPayerAccountId(moa.getPayerAccountId());
		
		pojo.setBillDate(this.toDateFromDate(moa.getBillDate()));
		
		pojo.setType(pojo.getType());

		for (LineItem moaLi : (List<LineItem>) moa.getLineItem()) {
			LineItemPojo pojoLi = new LineItemPojo();
			this.populateLineItemPojo(moaLi, pojoLi);
			pojo.getLineItems().add(pojoLi);
		}
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}
	private void populateLineItemMoa(LineItemPojo pojo, LineItem moa) throws EnterpriseFieldException {
		/*
		<!ELEMENT LineItem (InvoiceId, PayerAccountId, LinkedAccountId?, RecordType, 
			RecordId, BillingPeriodStartDatetime, BillingPeriodEndDatetime, 
			InvoiceDatetime, PayerAccountName, LinkedAccountName?, TaxationAddress, 
			PayerPONumber?, ProductCode, ProductName, SellerOfRecord, UsageType, 
			Operation, RateId, ItemDescription, UsageStartDatetime, UsageEndDatetime, 
			UsageQuantity, BlendedRate?, CurrencyCode, CostBeforeTax, Credits, 
			TaxAmount, TaxType, TotalCost)>
		 */
		
		moa.setInvoiceId(pojo.getInvoiceId());
		moa.setPayerAccountId(pojo.getPayerAccountId());
		moa.setLinkedAccountId(pojo.getLinkedAccountId());
		moa.setRecordType(pojo.getRecordType());
		moa.setRecordId(pojo.getRecordId());
		
		Datetime billingPeriodStartDatetime = moa.newBillingPeriodStartDatetime();
		this.populateDatetime(billingPeriodStartDatetime, pojo.getBillingPeriodStartDatetime());
		moa.setBillingPeriodStartDatetime(billingPeriodStartDatetime);
		
		Datetime billingPeriodEndDatetime = moa.newBillingPeriodEndDatetime();
		this.populateDatetime(billingPeriodEndDatetime, pojo.getBillingPeriodEndDatetime());
		moa.setBillingPeriodEndDatetime(billingPeriodEndDatetime);
		
		Datetime invoiceDatetime = moa.newInvoiceDatetime();
		this.populateDatetime(invoiceDatetime, pojo.getInvoiceDatetime());
		moa.setInvoiceDatetime(invoiceDatetime);
		
		moa.setPayerAccountName(pojo.getPayerAccountName());
		moa.setLinkedAccountName(pojo.getLinkedAccountName());
		moa.setTaxationAddress(pojo.getTaxationAddress());
		moa.setPayerPONumber(pojo.getPayerPONumber());
		moa.setProductCode(pojo.getProductCode());
		moa.setProductName(pojo.getProductName());
		moa.setSellerOfRecord(pojo.getSellerOfRecord());
		moa.setUsageType(pojo.getUsageType());
		moa.setOperation(pojo.getOperation());
		moa.setRateId(pojo.getRateId());
		moa.setItemDescription(pojo.getItemDescription());
		
		Datetime usageStartDatetime = moa.newUsageStartDatetime();
		this.populateDatetime(usageStartDatetime, pojo.getUsageStartDatetime());
		moa.setUsageStartDatetime(usageStartDatetime);

		Datetime usageEndDatetime = moa.newUsageEndDatetime();
		this.populateDatetime(usageEndDatetime, pojo.getUsageEndDatetime());
		moa.setUsageEndDatetime(usageEndDatetime);

		moa.setUsageQuantity(this.toStringFromDouble(pojo.getUsageQuantity()));
		moa.setBlendedRate(this.toStringFromDouble(pojo.getBlendedRate()));
		moa.setCurrencyCode(pojo.getCurrencyCode());
		moa.setCostBeforeTax(this.toStringFromDouble(pojo.getCostBeforeTax()));
		moa.setCredits(this.toStringFromDouble(pojo.getCredits()));
		moa.setTaxAmount(this.toStringFromDouble(pojo.getTaxAmount()));
		moa.setTaxType(pojo.getTaxType());
		moa.setTotalCost(this.toStringFromDouble(pojo.getTotalCost()));
	}
	private void populateLineItemPojo(LineItem moa, LineItemPojo pojo) {
		pojo.setInvoiceId(moa.getInvoiceId());
		pojo.setPayerAccountId(moa.getPayerAccountId());
		pojo.setLinkedAccountId(moa.getLinkedAccountId());
		pojo.setRecordType(moa.getRecordType());
		pojo.setRecordId(moa.getRecordId());
		
		pojo.setBillingPeriodStartDatetime(this.toDateFromDatetime(moa.getBillingPeriodStartDatetime()));		
		pojo.setBillingPeriodEndDatetime(this.toDateFromDatetime(moa.getBillingPeriodEndDatetime()));
		pojo.setInvoiceDatetime(this.toDateFromDatetime(moa.getInvoiceDatetime()));
		
		pojo.setPayerAccountName(moa.getPayerAccountName());
		pojo.setLinkedAccountName(moa.getLinkedAccountName());
		pojo.setTaxationAddress(moa.getTaxationAddress());
		pojo.setPayerPONumber(moa.getPayerPONumber());
		pojo.setProductCode(moa.getProductCode());
		pojo.setProductName(moa.getProductName());
		pojo.setSellerOfRecord(moa.getSellerOfRecord());
		pojo.setUsageType(moa.getUsageType());
		pojo.setOperation(moa.getOperation());
		pojo.setRateId(moa.getRateId());
		pojo.setItemDescription(moa.getItemDescription());
		
		pojo.setUsageStartDatetime(this.toDateFromDatetime(moa.getUsageStartDatetime()));
		pojo.setUsageEndDatetime(this.toDateFromDatetime(moa.getUsageEndDatetime()));

		pojo.setUsageQuantity(this.toDoubleFromString(moa.getUsageQuantity()));
		pojo.setBlendedRate(this.toDoubleFromString(moa.getBlendedRate()));
		pojo.setCurrencyCode(moa.getCurrencyCode());
		pojo.setCostBeforeTax(this.toDoubleFromString(moa.getCostBeforeTax()));
		pojo.setCredits(this.toDoubleFromString(moa.getCredits()));
		pojo.setTaxAmount(this.toDoubleFromString(moa.getTaxAmount()));
		pojo.setTaxType(moa.getTaxType());
		pojo.setTotalCost(this.toDoubleFromString(moa.getTotalCost()));
	}
	
	private void populateVpcMoa(VpcPojo pojo,
			VirtualPrivateCloud moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getAccountId() != null) {
			moa.setAccountId(pojo.getAccountId());
		}
		if (pojo.getVpcId() != null) {
			moa.setVpcId(pojo.getVpcId());
		}
		else {
			moa.setVpcId(UUID.uuid());
			pojo.setVpcId(moa.getVpcId());
		}
//		moa.setComplianceClass(pojo.getComplianceClass());
		moa.setType(pojo.getType());
		// owner net ids
//        for (String p : pojo.getCustomerAdminNetIdList()) {
//            moa.addCustomerAdminNetId(p);
//        }

		moa.setRegion(pojo.getRegion());
		moa.setCidr(pojo.getCidr());
		moa.setVpnConnectionProfileId(pojo.getVpnConnectionProfileId());
		moa.setPurpose(pojo.getPurpose());
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateVpcPojo(VirtualPrivateCloud moa,
			VpcPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setAccountId(moa.getAccountId());
		AccountPojo account = this.getAccountById(pojo.getAccountId());
		if (account != null) {
			pojo.setAccountName(account.getAccountName());
		}
		else {
			pojo.setAccountName("Unknown");
		}
		pojo.setRegion(moa.getRegion());
		pojo.setVpcId(moa.getVpcId());
		pojo.setType(moa.getType());
		pojo.setCidr(moa.getCidr());
		pojo.setVpnConnectionProfileId(moa.getVpnConnectionProfileId());
		pojo.setPurpose(moa.getPurpose());
//		pojo.setComplianceClass(moa.getComplianceClass());

//		for (String netId : (List<String>) moa.getCustomerAdminNetId()) {
//			pojo.getCustomerAdminNetIdList().add(netId);
//		}
		
		pojo.setCidr(moa.getCidr());
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateVpcRequisitionMoa(VpcRequisitionPojo pojo,
			VirtualPrivateCloudRequisition moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getAccountId() != null) {
			moa.setAccountId(pojo.getAccountId());
		}
		moa.setRegion(pojo.getRegion());
		moa.setAccountOwnerUserId(pojo.getAccountOwnerUserId());
		moa.setFinancialAccountNumber(pojo.getSpeedType());
		moa.setType(pojo.getType());
		// admin user ids
        for (String p : pojo.getCustomerAdminUserIdList()) {
            moa.addCustomerAdminUserId(p);
        }
//        moa.setTicketId(pojo.getTicketId());
        moa.setAuthenticatedRequestorUserId(pojo.getAuthenticatedRequestorUserId());
        moa.setComplianceClass(pojo.getComplianceClass());
        moa.setNotifyAdmins(this.toStringFromBoolean(pojo.isNotifyAdmins()));
        moa.setPurpose(pojo.getPurpose());
        for (String s : pojo.getSensitiveDataList()) {
        	moa.addSensitiveDataType(s);
        }
	}

	@SuppressWarnings({ "unchecked" })
	private void populateVpcRequisitionPojo(VirtualPrivateCloudRequisition moa,
			VpcRequisitionPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {

		pojo.setRegion(moa.getRegion());
		pojo.setAccountId(moa.getAccountId());
		pojo.setAccountOwnerUserId(moa.getAccountOwnerUserId());
		pojo.setSpeedType(moa.getFinancialAccountNumber());
		pojo.setType(moa.getType());
		if (moa.getCustomerAdminUserId() != null) {
			for (String netId : (List<String>) moa.getCustomerAdminUserId()) {
				pojo.getCustomerAdminUserIdList().add(netId);
			}
		}
//		pojo.setTicketId(moa.getTicketId());
		pojo.setAuthenticatedRequestorUserId(moa.getAuthenticatedRequestorUserId());
		pojo.setComplianceClass(moa.getComplianceClass());
		pojo.setNotifyAdmins(this.toBooleanFromString(moa.getNotifyAdmins()));
		pojo.setPurpose(moa.getPurpose());
		if (moa.getSensitiveDataType() != null) {
			for (String sensitiveDataType : (List<String>) moa.getSensitiveDataType()) {
				pojo.getSensitiveDataList().add(sensitiveDataType);
			}
		}
		
//		this.setPojoCreateInfo(pojo, moa);
//		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateAccountMoa(AccountPojo pojo,
			Account moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getAccountId() != null) {
			moa.setAccountId(pojo.getAccountId());
		}
		moa.setAccountName(pojo.getAccountName());
		moa.setPasswordLocation(pojo.getPasswordLocation());
//		moa.setAccountOwnerUserId(pojo.getAccountOwnerDirectoryMetaData().getPublicId());
		moa.setAccountOwnerId(pojo.getAccountOwnerDirectoryMetaData().getPublicId());
		moa.setFinancialAccountNumber(pojo.getSpeedType());
		moa.setComplianceClass(pojo.getComplianceClass());
		// email
        for (EmailPojo p : pojo.getEmailList()) {
            EmailAddress email = moa.newEmailAddress();
            email.setType(p.getType());
            email.setEmail(p.getEmailAddress());
            moa.addEmailAddress(email);
        }
        
//        for (String s : pojo.getSensitiveDataList()) {
//        	moa.addSensitiveDataType(s);
//        }

        for (PropertyPojo p : pojo.getProperties()) {
        	com.amazon.aws.moa.objects.resources.v1_0.Property mp = moa.newProperty();
        	mp.setKey(p.getName());
        	mp.setValue(p.getValue());
        	moa.addProperty(mp);
        }
        this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings("unchecked")
	private void populateAccountPojo(Account moa,
			AccountPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setAccountId(moa.getAccountId());
		pojo.setAccountName(moa.getAccountName());
		// need a clean meta data object because if we use the one from the cache
		// we'll run into issues when we go to do an update later (baseline missmatch)
		// so we'll use the data from the cache (if present) but just populate 
		// a clean meta data object with that data so we don't end up with a 
		// reference to the cached object which would lead to issues during update
		DirectoryMetaDataPojo ownerDmdp = new DirectoryMetaDataPojo();
		DirectoryMetaDataPojo cachedDmdp = this.getDirectoryMetaDataForPublicId(moa.getAccountOwnerId());
		if (cachedDmdp != null) {
			ownerDmdp.setPublicId(cachedDmdp.getPublicId());
			ownerDmdp.setNetId(cachedDmdp.getNetId());
			ownerDmdp.setFirstName(cachedDmdp.getFirstName());
			ownerDmdp.setLastName(cachedDmdp.getLastName());
		}
		pojo.setAccountOwnerDirectoryMetaData(ownerDmdp);
		pojo.setPasswordLocation(moa.getPasswordLocation());
		pojo.setSpeedType(moa.getFinancialAccountNumber());
		pojo.setComplianceClass(moa.getComplianceClass());
		for (EmailAddress email : (List<EmailAddress>) moa.getEmailAddress()) {
			EmailPojo emp = new EmailPojo();
			emp.setType(email.getType());
			emp.setEmailAddress(email.getEmail());
			pojo.getEmailList().add(emp);
		}
		
//		if (moa.getSensitiveDataType() != null) {
//			for (String sensitiveDataType : (List<String>) moa.getSensitiveDataType()) {
//				pojo.getSensitiveDataList().add(sensitiveDataType);
//			}
//		}

		for (com.amazon.aws.moa.objects.resources.v1_0.Property mp : (List<com.amazon.aws.moa.objects.resources.v1_0.Property>) moa.getProperty()) {
			PropertyPojo p = new PropertyPojo();
			p.setName(mp.getKey());
			p.setValue(mp.getValue());
			pojo.getProperties().add(p);
		}
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateElasticIpMoa(ElasticIpPojo pojo,
			ElasticIp moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getElasticIpId() != null) {
			moa.setElasticIpId(pojo.getElasticIpId());
		}
//		else {
//			moa.setElasticIpId(UUID.uuid());
//			pojo.setElasticIpId(moa.getElasticIpId());
//		}
		moa.setElasticIpAddress(pojo.getElasticIpAddress());
		moa.setAssociatedIpAddress(pojo.getAssociatedIpAddress());

		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateElasticIpPojo(ElasticIp moa,
			ElasticIpPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setElasticIpId(moa.getElasticIpId());
		pojo.setElasticIpAddress(moa.getElasticIpAddress());
		pojo.setAssociatedIpAddress(moa.getAssociatedIpAddress());
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateElasticIpAssignmentMoa(ElasticIpAssignmentPojo pojo,
			ElasticIpAssignment moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getAssignmentId() != null) {
			moa.setElasticIpAssignmentId(pojo.getAssignmentId());
		}
		else {
			moa.setElasticIpAssignmentId(UUID.uuid());
			pojo.setAssignmentId(moa.getElasticIpAssignmentId());
		}
		moa.setOwnerId(pojo.getOwnerId());
		moa.setDecription(pojo.getDescription());
		moa.setPurpose(pojo.getPurpose());
		if (pojo.getElasticIp() != null) {
			ElasticIp eip = moa.newElasticIp();
			this.populateElasticIpMoa(pojo.getElasticIp(), eip);
			moa.setElasticIp(eip);
		}

		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateElasticIpAssignmentPojo(ElasticIpAssignment moa,
			ElasticIpAssignmentPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setAssignmentId(moa.getElasticIpAssignmentId());
		pojo.setOwnerId(moa.getOwnerId());
		pojo.setDescription(moa.getDecription());
		pojo.setPurpose(moa.getPurpose());
		if (moa.getElasticIp() != null) {
			ElasticIpPojo eip = new ElasticIpPojo();
			this.populateElasticIpPojo(moa.getElasticIp(), eip);
			pojo.setElasticIp(eip);
		}
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateCidrMoa(CidrPojo pojo,
			Cidr moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getCidrId() != null) {
			moa.setCidrId(pojo.getCidrId());
		}
		else {
			moa.setCidrId(UUID.uuid());
			pojo.setCidrId(moa.getCidrId());
		}
		moa.setNetwork(pojo.getNetwork());
		moa.setBits(pojo.getBits());
		
		if (pojo.getAssociatedCidrs() != null && pojo.getAssociatedCidrs().size() > 0) {
			for (AssociatedCidrPojo acp : pojo.getAssociatedCidrs()) {
				AssociatedCidr ac = moa.newAssociatedCidr();
				ac.setType(acp.getType());
				ac.setBits(acp.getBits());
				ac.setNetwork(acp.getNetwork());
				moa.addAssociatedCidr(ac);
			}
		}
		if (pojo.getProperties() != null && pojo.getProperties().size() > 0) {
			for (PropertyPojo pp : pojo.getProperties()) {
				Property p = moa.newProperty();
				p.setPropertyName(pp.getName());
				p.setPropertyValue(pp.getValue());
				moa.addProperty(p);
			}
		}

		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings("unchecked")
	private void populateCidrPojo(Cidr moa,
			CidrPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setCidrId(moa.getCidrId());
		pojo.setNetwork(moa.getNetwork());
		pojo.setBits(moa.getBits());
		if (moa.getAssociatedCidr() != null && moa.getAssociatedCidrLength() > 0) {
			for (AssociatedCidr ac : (List<AssociatedCidr>)moa.getAssociatedCidr()) {
				AssociatedCidrPojo acp = new AssociatedCidrPojo();
				acp.setType(ac.getType());
				acp.setBits(ac.getBits());
				acp.setNetwork(ac.getNetwork());
				pojo.getAssociatedCidrs().add(acp);
			}
		}
		if (moa.getProperty() != null && moa.getPropertyLength() > 0) {
			for (Property p : (List<Property>)moa.getProperty()) {
				PropertyPojo pp = new PropertyPojo();
				pp.setName(p.getPropertyName());
				pp.setValue(p.getPropertyValue());
			}
		}
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateCidrAssignmentMoa(CidrAssignmentPojo pojo,
			CidrAssignment moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getCidrAssignmentId() != null) {
			moa.setCidrAssignmentId(pojo.getCidrAssignmentId());
		}
		else {
			moa.setCidrAssignmentId(UUID.uuid());
		}
		moa.setOwnerId(pojo.getOwnerId());
		moa.setDecription(pojo.getDescription());
		moa.setPurpose(pojo.getPurpose());
		if (pojo.getCidr() != null) {
			Cidr cidr = moa.newCidr();
			this.populateCidrMoa(pojo.getCidr(), cidr);
			moa.setCidr(cidr);
		}

		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateCidrAssignmentPojo(CidrAssignment moa,
			CidrAssignmentPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setCidrAssignmentId(moa.getCidrAssignmentId());
		pojo.setOwnerId(moa.getOwnerId());
		pojo.setDescription(moa.getDecription());
		pojo.setPurpose(moa.getPurpose());
		if (moa.getCidr() != null && !moa.getCidr().isEmpty()) {
			CidrPojo cidr = new CidrPojo();
			this.populateCidrPojo(moa.getCidr(), cidr);
			pojo.setCidr(cidr);
		}
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public String getClientInfoForUser(UserAccountPojo user) {
        String clientIp = this.getThreadLocalRequest().getRemoteAddr();
        java.util.Date now = new java.util.Date();
        String userInfo = user.getEppn() + "/" + clientIp + " @ " + now;
        return userInfo;
	}

	@Override
	public String getClientInfo() {
        UserAccountPojo user = this.getUserLoggedIn(false);
        return this.getClientInfoForUser(user);
	}

	@Override
	public CidrQueryResultPojo getCidrsForFilter(CidrQueryFilterPojo filter) throws RpcException {
		CidrQueryResultPojo result = new CidrQueryResultPojo();
		List<CidrSummaryPojo> summaries = new java.util.ArrayList<CidrSummaryPojo>();
		try {
			CidrQuerySpecification queryObject = (CidrQuerySpecification) getObject(Constants.MOA_CIDR_QUERY_SPEC);
			Cidr actionable = (Cidr) getObject(Constants.MOA_CIDR);

			if (filter != null) {
				queryObject.setRequestorNetId(filter.getRequestorNetId());
				queryObject.setPurpose(filter.getPurpose());
				queryObject.setDescription(filter.getDescription());
				queryObject.setNetwork(filter.getNetwork());
				queryObject.setBits(this.toStringFromInt(filter.getBits()));
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getCidrsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			info("[getCidrsForFilter] query spec is: " + queryObject.toXmlString());
			@SuppressWarnings("unchecked")
			List<Cidr> moas = actionable.query(queryObject,
					this.getCidrRequestService());
			info("[getCidrsForFilter] got " + moas.size() + " CIDRs back from ESB");
			for (Cidr moa : moas) {
				CidrSummaryPojo summary = new CidrSummaryPojo();
				CidrPojo pojo = new CidrPojo();
				CidrPojo baseline = new CidrPojo();
				this.populateCidrPojo(moa, pojo);
				this.populateCidrPojo(moa, baseline);
				pojo.setBaseline(baseline);
				summary.setCidr(pojo);
				summaries.add(summary);
			}

			Collections.sort(summaries);
			result.setResults(summaries);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public List<CidrPojo> getCidrsForUserLoggedIn() throws RpcException {
		UserAccountPojo user = getUserLoggedIn(false);
		if (user == null) {
			info("no user currently logged in, this shouldn't happen...");
		}
		else {
			info("User logged in for this session is " + user.getEppn());
		}

		if (!useEsbService) {
//			return mockService.getMtpCaseRecordsForPhysician(physicianUserId);
		} 
		else {
			List<CidrPojo> pojos = new java.util.ArrayList<CidrPojo>();
			try {
				CidrQuerySpecification queryObject = (CidrQuerySpecification) getObject(Constants.MOA_CIDR_QUERY_SPEC);
				Cidr actionable = (Cidr) getObject(Constants.MOA_CIDR);

				queryObject.setRequestorNetId(user.getEppn().substring(0, user.getEppn().indexOf("@")));

				String authUserId = this.getAuthUserIdForHALS();
				actionable.getAuthentication().setAuthUserId(authUserId);
				info("[getCidrsForUserLoggedIn] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
				
				@SuppressWarnings("unchecked")
				List<Cidr> moas = actionable.query(queryObject,
						this.getCidrRequestService());
				for (Cidr moa : moas) {
					CidrPojo pojo = new CidrPojo();
					CidrPojo baseline = new CidrPojo();
					this.populateCidrPojo(moa, pojo);
					this.populateCidrPojo(moa, baseline);
					pojo.setBaseline(baseline);
					pojos.add(pojo);
				}

				Collections.sort(pojos);
				return pojos;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectQueryException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (XmlEnterpriseObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (ParseException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}

		return null;
	}

	@Override
	public CidrPojo createCidr(CidrPojo cidr) throws RpcException {
		
		cidr.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("creating CIDR record on the server...");
				Cidr moa = (Cidr) getObject(Constants.MOA_CIDR);
				info("populating moa");
				this.populateCidrMoa(cidr, moa);

				
				info("doing the Cidr.create...");
				this.doCreate(moa, getCidrRequestService());
				info("Cidr.create is complete...");

//				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
				return cidr;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectCreateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public CidrPojo storeCidrInCacheForUser(String eppn, CidrPojo cidr) {
		info("Storing Cidr in cache for " + eppn);
		Cache.getCache().put(Constants.CIDR + eppn, cidr);
		
		return cidr;
	}

	@Override
	public CidrPojo getCidrFromCacheForUser(String eppn) {
		info("Getting CIDR from cache for " + eppn);
		return (CidrPojo) Cache.getCache().get(Constants.CIDR + eppn);
	}

	@Override
	public List<CidrAssignmentPojo> getCidrAssignmentsForUserLoggedIn() throws RpcException {
		UserAccountPojo user = getUserLoggedIn(false);
		if (user == null) {
			info("no user currently logged in, this shouldn't happen...");
		}
		else {
			info("User logged in for this session is " + user.getEppn());
		}

		if (!useEsbService) {
//			return mockService.getMtpCaseRecordsForPhysician(physicianUserId);
		} 
		else {
			List<CidrAssignmentPojo> pojos = new java.util.ArrayList<CidrAssignmentPojo>();
			try {
				CidrAssignmentQuerySpecification queryObject = (CidrAssignmentQuerySpecification) getObject(Constants.MOA_CIDR_ASSIGNMENT_QUERY_SPEC);
				CidrAssignment actionable = (CidrAssignment) getObject(Constants.MOA_CIDR_ASSIGNMENT);

				queryObject.setOwnerId(user.getEppn().substring(0, user.getEppn().indexOf("@")));

				String authUserId = this.getAuthUserIdForHALS();
				actionable.getAuthentication().setAuthUserId(authUserId);
				info("[getCidrAssignmentsForUserLoggedIn] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
				
				@SuppressWarnings("unchecked")
				List<CidrAssignment> moas = actionable.query(queryObject,
						this.getCidrRequestService());
				for (CidrAssignment moa : moas) {
					CidrAssignmentPojo pojo = new CidrAssignmentPojo();
					this.populateCidrAssignmentPojo(moa, pojo);
					pojos.add(pojo);
				}

				Collections.sort(pojos);
				return pojos;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectQueryException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (XmlEnterpriseObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (ParseException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}

		return null;
	}

	@Override
	public CidrAssignmentPojo createCidrAssignment(CidrAssignmentPojo cidrAssignment) throws RpcException {
		
		cidrAssignment.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("creating CIDR Assignment record on the server...");
				CidrAssignment moa = (CidrAssignment) getObject(Constants.MOA_CIDR_ASSIGNMENT);
				info("populating moa");
				this.populateCidrAssignmentMoa(cidrAssignment, moa);

				
				info("doing the CidrAssignment.create...");
				this.doCreate(moa, getCidrRequestService());
				info("CidrAssignment.create is complete...");

//				Cache.getCache().remove(Constants.CIDR_ASSIGNMENT + this.getUserLoggedIn().getEppn());
				return cidrAssignment;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectCreateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public CidrAssignmentPojo storeCidrAssignmentInCacheForUser(String eppn, CidrAssignmentPojo cidrAssignment) {
		info("Storing CidrAssignment in cache for " + eppn);
		Cache.getCache().put(Constants.CIDR_ASSIGNMENT + eppn, cidrAssignment);
		
		return cidrAssignment;
	}

	@Override
	public CidrAssignmentPojo getCidrAssignmentFromCacheForUser(String eppn) {
		info("Getting CIDR Assignment from cache for " + eppn);
		return (CidrAssignmentPojo) Cache.getCache().get(Constants.CIDR_ASSIGNMENT + eppn);
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public boolean isUseEsbService() {
		return useEsbService;
	}

	public void setUseEsbService(boolean useEsbService) {
		this.useEsbService = useEsbService;
	}

	public boolean isUseShibboleth() {
		return useShibboleth;
	}

	public void setUseShibboleth(boolean useShibboleth) {
		this.useShibboleth = useShibboleth;
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		VpcProvisioningServiceImpl.appConfig = appConfig;
	}

	public Properties getGeneralProps() {
		return generalProps;
	}

	public void setGeneralProps(Properties generalProps) {
		this.generalProps = generalProps;
	}

	public String getConfigDocPath() {
		return configDocPath;
	}

	public void setConfigDocPath(String configDocPath) {
		this.configDocPath = configDocPath;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public ProducerPool getCidrProducerPool() {
		return cidrProducerPool;
	}

	public void setCidrProducerPool(ProducerPool cidrProducerPool) {
		this.cidrProducerPool = cidrProducerPool;
	}

//	public ProducerPool getAuthzProducerPool() {
//		return authzProducerPool;
//	}
//
//	public void setAuthzProducerPool(ProducerPool authzProducerPool) {
//		this.authzProducerPool = authzProducerPool;
//	}

	public Object getLock() {
		return lock;
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}

	public static SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}

	public static void setDateFormatter(SimpleDateFormat dateFormatter) {
		VpcProvisioningServiceImpl.dateFormatter = dateFormatter;
	}

	public boolean isInitializing() {
		return initializing;
	}

	public void setInitializing(boolean initializing) {
		this.initializing = initializing;
	}

	public int getUserCnt() {
		return userCnt;
	}

	public void setUserCnt(int userCnt) {
		this.userCnt = userCnt;
	}

	public boolean isManageSessionLocally() {
		return manageSessionLocally;
	}

	public void setManageSessionLocally(boolean manageSessionLocally) {
		this.manageSessionLocally = manageSessionLocally;
	}

	private RequestService getNetworkOpsRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) networkOpsProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getSrdRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) srdProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getElasticIpRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) elasticIpProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getIdentityServiceRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) identityServiceProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getPeopleSoftRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) peopleSoftProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getAWSRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) awsProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getCidrRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) cidrProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
//	private RequestService getAuthzRequestService() throws JMSException {
//		RequestService reqSvc = (RequestService) authzProducerPool.getProducer();
//		((PointToPointProducer) reqSvc)
//				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
//		return reqSvc;
//	}
	private RequestService getFirewallRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) firewallProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getServiceNowRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) serviceNowProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getDirectoryRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) directoryProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}
	private RequestService getIDMRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) idmProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		return reqSvc;
	}

    private java.util.Date toDateFromDate(org.openeai.moa.objects.resources.Date moa) {
        if (moa == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Integer.parseInt(moa.getMonth()) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(moa.getDay()));
        c.set(Calendar.YEAR, Integer.parseInt(moa.getYear()));
        return c.getTime();
    }

    private java.util.Date toDateFromDatetime(
			org.openeai.moa.objects.resources.Datetime moa) {
		if (moa == null) {
			return null;
		}

		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(moa.getTimezone()));

		c.set(Calendar.MONTH, Integer.parseInt(moa.getMonth()) - 1);
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(moa.getDay()));
		c.set(Calendar.YEAR, Integer.parseInt(moa.getYear()));
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(moa.getHour()));
		c.set(Calendar.MINUTE, Integer.parseInt(moa.getMinute()));
		c.set(Calendar.SECOND, Integer.parseInt(moa.getSecond()));
		c.set(Calendar.MILLISECOND, Integer.parseInt(moa.getSubSecond()));
		return c.getTime();
	}

	private void setMoaCreateInfo(XmlEnterpriseObject xeo, SharedObject pojo)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		Method createUserSetter = xeo.getClass().getMethod("setCreateUser",
				new Class[] { String.class });
		createUserSetter.invoke(xeo, new Object[] { pojo.getCreateUser() });

		Datetime dt = new Datetime("CreateDatetime");
		populateDatetime(dt, pojo.getCreateTime());
		Method createDatetimeSetter = xeo
				.getClass()
				.getMethod(
						"setCreateDatetime",
						new Class[] { org.openeai.moa.objects.resources.Datetime.class });
		createDatetimeSetter.invoke(xeo, new Object[] { dt });
	}

	private void setMoaUpdateInfo(XmlEnterpriseObject xeo, SharedObject pojo)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		
		Method updateUserSetter = xeo.getClass().getMethod("setLastUpdateUser",
				new Class[] { String.class });
		updateUserSetter.invoke(xeo, new Object[] { pojo.getUpdateUser() });

		Datetime dt = new Datetime("LastUpdateDatetime");
		if (pojo.getUpdateTime() != null) { // without this condition, it set a
											// time to the moa even when
											// pojo.getUpdateTime()==null
			populateDatetime(dt, pojo.getUpdateTime());
			Method createDatetimeSetter = xeo
					.getClass()
					.getMethod(
							"setLastUpdateDatetime",
							new Class[] { org.openeai.moa.objects.resources.Datetime.class });
			createDatetimeSetter.invoke(xeo, new Object[] { dt });
		}
	}

    private void populateDate(org.openeai.moa.objects.resources.Date moaDate, java.util.Date javaDate)
            throws EnterpriseFieldException {
        if (javaDate == null) {
            return;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(javaDate);
        c.get(Calendar.DAY_OF_MONTH);
        moaDate.setMonth(new Integer(c.get(Calendar.MONTH) + 1).toString());
        moaDate.setDay(new Integer(c.get(Calendar.DAY_OF_MONTH)).toString());
        moaDate.setYear(new Integer(c.get(Calendar.YEAR)).toString());
    }

    private void populateDatetime(
			org.openeai.moa.objects.resources.Datetime moaDateTime,
			java.util.Date javaDate) {
		
		if (javaDate == null) {
			return;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(javaDate);
		// info("[populateDatetime] setting calendar's TimeZone to: " +
		// moaDateTime.getTimezone());
		c.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		// c.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
		moaDateTime.setMonth(new Integer(c.get(Calendar.MONTH) + 1).toString());
		moaDateTime
				.setDay(new Integer(c.get(Calendar.DAY_OF_MONTH)).toString());
		moaDateTime.setYear(new Integer(c.get(Calendar.YEAR)).toString());
		moaDateTime
				.setHour(new Integer(c.get(Calendar.HOUR_OF_DAY)).toString());
		moaDateTime.setMinute(new Integer(c.get(Calendar.MINUTE)).toString());
		moaDateTime.setSecond(new Integer(c.get(Calendar.SECOND)).toString());
		moaDateTime.setSubSecond(new Integer(c.get(Calendar.MILLISECOND))
				.toString());
		moaDateTime.setTimezone(c.getTimeZone().getID());
		// moaDateTime.setTimezone(TimeZone.getDefault().getID());
	}

	private void setPojoCreateInfo(SharedObject pojo, XmlEnterpriseObject xeo)
			throws XmlEnterpriseObjectException {
		
		java.util.Date d = toDateFromDatetime(((org.openeai.moa.objects.resources.Datetime) xeo
				.getValueFromObject("CreateDatetime")));
		pojo.setCreateInfo((String) xeo.getValueFromObject("CreateUser"), d);
	}

	private void setPojoUpdateInfo(SharedObject pojo, XmlEnterpriseObject xeo)
			throws XmlEnterpriseObjectException {
		
		java.util.Date d = toDateFromDatetime(((org.openeai.moa.objects.resources.Datetime) xeo
				.getValueFromObject("LastUpdateDatetime")));
		pojo.setUpdateInfo((String) xeo.getValueFromObject("LastUpdateUser"), d);
	}

	private Object getObject(String objectName)
			throws EnterpriseConfigurationObjectException {
		
		return getAppConfig().getObject(objectName);
	}

	@SuppressWarnings("unused")
	private String toStringFromBoolean(boolean validated) {
		return Boolean.toString(validated);
	}

	private String toStringFromInt(int i) {
		return Integer.toString(i);
	}

	@SuppressWarnings("unused")
	private String toStringFromLong(long l) {
		return Long.toString(l);
	}

	@SuppressWarnings("unused")
	private String toStringFromFloat(float f) {
		return Float.toString(f);
	}

	@SuppressWarnings("unused")
	private String toStringFromDouble(double d) {
		return Double.toString(d);
	}

	@SuppressWarnings("unused")
	private double toDoubleFromString(String s) {
		if (s == null) {
			return 0;
		} 
		else {
			return Double.parseDouble(s);
		}
	}

	@SuppressWarnings("unused")
	private float toFloatFromString(String s) {
		if (s == null) {
			return 0;
		} 
		else {
			return Float.parseFloat(s);
		}
	}

	@SuppressWarnings("unused")
	private int toIntFromString(String s) {
		if (s == null) {
			return 0;
		} 
		else {
			return Integer.parseInt(s);
		}
	}

	@SuppressWarnings("unused")
	private long toLongFromString(String s) {
		if (s == null) {
			return 0;
		} 
		else {
			return Long.parseLong(s);
		}
	}

	@SuppressWarnings("unused")
	private boolean toBooleanFromString(String validated) {
		if (validated != null) {
			return Boolean.parseBoolean(validated);
		}

		return false;
	}

	@Override
	public void deleteCidrSummary(CidrSummaryPojo cidrSummary) throws RpcException {
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				// TODO: need to see if there are any assignments in this summary and if so, delete those too
				info("deleting CIDR record on the server...");
				Cidr moa = (Cidr) getObject(Constants.MOA_CIDR);
				info("populating moa");
				this.populateCidrMoa(cidrSummary.getCidr(), moa);

				
				info("doing the Cidr.delete...");
				this.doDelete(moa, getCidrRequestService());
				info("Cidr.delete is complete...");

//				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public CidrPojo updateCidr(CidrPojo cidr) throws RpcException {
		cidr.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating Cidr on the server...");
            Cidr newData = (Cidr) getObject(Constants.MOA_CIDR);
            Cidr baselineData = (Cidr) getObject(Constants.MOA_CIDR);

            info("populating newData...");
            populateCidrMoa(cidr, newData);

            info("populating baselineData...");
            populateCidrMoa(cidr.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getCidrRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return cidr;
	}

	@Override
	public CidrAssignmentPojo updateCidrAssignment(CidrAssignmentPojo cidrAssignment) throws RpcException {
		cidrAssignment.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating CidrAssignment on the server...");
            CidrAssignment newData = (CidrAssignment) getObject(Constants.MOA_CIDR_ASSIGNMENT);
            CidrAssignment baselineData = (CidrAssignment) getObject(Constants.MOA_CIDR_ASSIGNMENT);

            info("populating newData...");
            populateCidrAssignmentMoa(cidrAssignment, newData);

            info("populating baselineData...");
            populateCidrAssignmentMoa(cidrAssignment.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getCidrRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return cidrAssignment;
	}

	@Override
	public void deleteCidrAssignment(CidrAssignmentPojo cidrAssignment) throws RpcException {
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting CIDR Assignment record on the server...");
				CidrAssignment moa = (CidrAssignment) getObject(Constants.MOA_CIDR_ASSIGNMENT);
				info("populating moa");
				this.populateCidrAssignmentMoa(cidrAssignment, moa);
				
				info("doing the CidrAssignment.delete...");
				this.doDelete(moa, getCidrRequestService());
				info("CidrAssignment.delete is complete...");

//				Cache.getCache().remove(Constants.CIDR_ASSIGNMENT + this.getUserLoggedIn().getEppn());
				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public List<AccountPojo> getAccountsForUserLoggedIn() throws RpcException {
		return null;
	}

	@Override
	public AccountPojo getAccountById(String accountId) throws RpcException {
		
		try {
			AccountQuerySpecification queryObject = (AccountQuerySpecification) getObject(Constants.MOA_ACCOUNT_QUERY_SPEC);
			Account actionable = (Account) getObject(Constants.MOA_ACCOUNT);
	
			queryObject.setAccountId(accountId);
			info("[getAccountById] querying for account: " + accountId);
			@SuppressWarnings("unchecked")
			List<Account> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			
			info("[getAccountById] got " + moas.size() + " accounts from ESB service for account id: " + accountId);
			// there should only ever be one returned
			if (moas.size() != 1) {
				// error
				String errorMsg = "[getAccountById] got " + moas.size() + " accounts from ESB service, "
						+ "for account: " + accountId
						+ " expected exactly 1.  This MAY an issue."; 
				info(errorMsg);
				return null;
//				throw new RpcException(errorMsg);
			}
			else {
				for (Account moa : moas) {
					AccountPojo pojo = new AccountPojo();
					AccountPojo baseline = new AccountPojo();
					this.populateAccountPojo(moa, pojo);
					this.populateAccountPojo(moa, baseline);
					pojo.setBaseline(baseline);
					return pojo;
				}
			}
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
		return null;
	}
	@Override
	public AccountQueryResultPojo getAccountsForFilter(AccountQueryFilterPojo filter) throws RpcException {
		AccountQueryResultPojo result = new AccountQueryResultPojo();
		List<AccountPojo> pojos = new java.util.ArrayList<AccountPojo>();
		if (filter != null) {
			if (filter.getUserLoggedIn() != null) {
				if (!filter.getUserLoggedIn().isCentralAdmin()) {
					info("[getAccountsForFilter] " + filter.getUserLoggedIn().getEppn() + " IS NOT a Central Admin.");
					// iterate over all filter.AccountRoles and do a query for each account 
					// in the list.  Then, need to add each account to the result 
					for (AccountRolePojo arp : filter.getUserLoggedIn().getAccountRoles()) {
						if (arp.getAccountId() == null) {
							continue;
						}
						// check cache, if present, get account from cache
						// otherwise, get it from the account service and store it in the cache
						info("[getAccountsForFilter] getting account: " + arp.getAccountId());
						boolean inCache = false;
						AccountPojo cached_acct = (AccountPojo) Cache.getCache().get(
								Constants.ACCOUNT + arp.getAccountId());
						if (cached_acct == null) {
							cached_acct = this.getAccountById(arp.getAccountId());
							if (cached_acct == null) {
								info("No account found for ID " + arp.getAccountId() + " this may be okay.");
								continue;
							}
							else {
								info("[getAccountsForFilter] got account from account service...");
							}
						}
						else {
							info("[getAccountsForFilter] got account from cache...");
							inCache = true;
						}
						
						if (cached_acct != null) {
							// currently, they can only filter by account id so this
							// should work for now.  May need to re-visit if we ever do 
							// other types of filtering
							if (filter.getAccountId() != null) {
								if (cached_acct.getAccountId().equalsIgnoreCase(filter.getAccountId())) {
									info("[getAccountsForFilter-withAccountIdFilter] adding account " + cached_acct.getAccountId() + 
											" to list of accounts for the user logged in" );
									pojos.add(cached_acct);
									if (!inCache) {
										Cache.getCache().put(Constants.ACCOUNT + cached_acct.getAccountId(), cached_acct);
									}
								}
							}
							else {
								info("[getAccountsForFilter] adding account " + cached_acct.getAccountId() + 
										" to list of accounts for the user logged in" );
								pojos.add(cached_acct);
								if (!inCache) {
									Cache.getCache().put(Constants.ACCOUNT + cached_acct.getAccountId(), cached_acct);
								}
							}
						}
					}
					Collections.sort(pojos);
					result.setResults(pojos);
					result.setFilterUsed(filter);
					info("[getAccountsForFilter] returning " + pojos.size() + " accounts for the user logged in" );
					return result;
				}
				else {
					info("[getAccountsForFilter] " + filter.getUserLoggedIn().getEppn() + " IS a Central Admin.");
				}
			}
			else {
				info("[getAccountsForFilter] filter.userLoggedIn is null");
			}
		}
		
		// if filter.userLoggedIn isn't null and the user isn't a Central Admin, we won't get this far
		try {
			AccountQuerySpecification queryObject = (AccountQuerySpecification) getObject(Constants.MOA_ACCOUNT_QUERY_SPEC);
			Account actionable = (Account) getObject(Constants.MOA_ACCOUNT);

			if (filter != null) {
				queryObject.setAccountId(filter.getAccountId());
				queryObject.setAccountName(filter.getAccountName());
				// TODO may have to make email a list in the filter and query spec...
				if (filter.getEmail() != null) {
					EmailAddress emailMoa = actionable.newEmailAddress();
					emailMoa.setType(filter.getEmail().getType());
					emailMoa.setEmail(filter.getEmail().getEmailAddress());
					queryObject.setEmailAddress(emailMoa);
				}
				queryObject.setAccountOwnerId(filter.getAccountOwnerId());
				queryObject.setFinancialAccountNumber(filter.getSpeedType());
				queryObject.setCreateUser(filter.getCreateUser());
				queryObject.setLastUpdateUser(filter.getLastUpdateUser());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getAccountsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<Account> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getAccountsForFilter] got " + moas.size() + " accounts from ESB service");
			for (Account moa : moas) {
				AccountPojo pojo = new AccountPojo();
				AccountPojo baseline = new AccountPojo();
				this.populateAccountPojo(moa, pojo);
				this.populateAccountPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public AccountPojo createAccount(AccountPojo account) throws RpcException {
		account.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("creating Account on the server...");
				Account moa = (Account) getObject(Constants.MOA_ACCOUNT);
				info("populating moa");
				this.populateAccountMoa(account, moa);

				
				info("doing the Account.create...");
				this.doCreate(moa, getAWSRequestService());
				info("Account.create is complete...");

//				Cache.getCache().remove(Constants.ACCOUNT + this.getUserLoggedIn().getEppn());
				return account;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectCreateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public AccountPojo updateAccount(AccountPojo account) throws RpcException {
		account.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating Account on the server...");
            Account newData = (Account) getObject(Constants.MOA_ACCOUNT);
            Account baselineData = (Account) getObject(Constants.MOA_ACCOUNT);

            info("populating newData...");
            populateAccountMoa(account, newData);

            info("populating baselineData...");
            populateAccountMoa(account.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getAWSRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return account;
	}

	@Override
	public void deleteAccount(AccountPojo account) throws RpcException {
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting CIDR record on the server...");
				Account moa = (Account) getObject(Constants.MOA_ACCOUNT);
				info("populating moa");
				this.populateAccountMoa(account, moa);

				
				info("doing the Account.delete...");
				this.doDelete(moa, getAWSRequestService());
				info("Account.delete is complete...");

//				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public List<VpcPojo> getVpcsForUserLoggedIn() throws RpcException {
		return null;
	}

	private List<VpcPojo> getVpcsForAccountId(String accountId) throws RpcException {
		List<VpcPojo> pojos = new java.util.ArrayList<VpcPojo>();

		try {
			VirtualPrivateCloudQuerySpecification queryObject = (VirtualPrivateCloudQuerySpecification) getObject(Constants.MOA_VPC_QUERY_SPEC);
			VirtualPrivateCloud actionable = (VirtualPrivateCloud) getObject(Constants.MOA_VPC);
	
			queryObject.setAccountId(accountId);
			
			@SuppressWarnings("unchecked")
			List<VirtualPrivateCloud> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			
			info("[getVpcForAccountId] got " + moas.size() + " VPCs from ESB service");
			for (VirtualPrivateCloud moa : moas) {
				VpcPojo pojo = new VpcPojo();
				VpcPojo baseline = new VpcPojo();
				this.populateVpcPojo(moa, pojo);
				this.populateVpcPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
		return pojos;
	}
	@Override
	public VpcQueryResultPojo getVpcsForFilter(VpcQueryFilterPojo filter) throws RpcException {
		VpcQueryResultPojo result = new VpcQueryResultPojo();
		List<VpcPojo> pojos = new java.util.ArrayList<VpcPojo>();
		
		if (filter != null) {
			if (filter.getUserLoggedIn() != null) {
				if (!filter.getUserLoggedIn().isCentralAdmin()) {
					// iterate over all filter.AccountRoles and do a query for each account 
					// in the list.  Then, need to add each account to the result 
					for (AccountRolePojo arp : filter.getUserLoggedIn().getAccountRoles()) {
						List<VpcPojo> vpcs = this.getVpcsForAccountId(arp.getAccountId());
						if (vpcs != null) {
							info("[getVpcsForFilter] adding account " + arp.getAccountId() + 
								" to list of accounts for the user logged in" );
							pojos.addAll(vpcs);
						}
						else {
							String errorMsg = "[getAccountsForFilter] null result from getAccountById, this is an issue.";
							info(errorMsg);
							throw new RpcException(errorMsg);
						}
					}
					Collections.sort(pojos);
					result.setResults(pojos);
					result.setFilterUsed(filter);
					info("[getAccountsForFilter] returning " + pojos.size() + " accounts for the user logged in" );
					return result;
				}
			}
		}

		VpnConnectionProfileAssignmentQueryResultPojo eia_result = new VpnConnectionProfileAssignmentQueryResultPojo();
		if (filter != null && filter.isExcludeVpcsAssignedToVpnConnectionProfiles()) {
			// get all VpnConnectionProfileAssignments so we can weed out any VPCs that have
			// already been used in an existing assignment
			VpnConnectionProfileAssignmentQueryFilterPojo eia_filter = new VpnConnectionProfileAssignmentQueryFilterPojo();
			eia_result = this.getVpnConnectionProfileAssignmentsForFilter(eia_filter);
		}

		try {
			VirtualPrivateCloudQuerySpecification queryObject = (VirtualPrivateCloudQuerySpecification) getObject(Constants.MOA_VPC_QUERY_SPEC);
			VirtualPrivateCloud actionable = (VirtualPrivateCloud) getObject(Constants.MOA_VPC);

			if (filter != null) {
				queryObject.setAccountId(filter.getAccountId());
				queryObject.setVpcId(filter.getVpcId());
				queryObject.setType(filter.getType());
				queryObject.setCustomerAdminUserId(filter.getCustomerAdminUserId());
				queryObject.setCreateUser(filter.getCreateUser());
				queryObject.setLastUpdateUser(filter.getUpdateUser());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getVpcsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<VirtualPrivateCloud> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			for (VirtualPrivateCloud moa : moas) {
				VpcPojo pojo = new VpcPojo();
				VpcPojo baseline = new VpcPojo();
				this.populateVpcPojo(moa, pojo);
				this.populateVpcPojo(moa, baseline);
				pojo.setBaseline(baseline);

				boolean isAssigned = false;
				if (eia_result.getResults().size() > 0) {
					// weed out any VPCs that have already been used in a profile assignment
					assignmentLoop: for (VpnConnectionProfileAssignmentPojo assignment : eia_result.getResults()) {
						if (assignment.getOwnerId().equalsIgnoreCase(pojo.getVpcId())) {
							isAssigned = true;
							break assignmentLoop;
						}
					}
				}

				// only add the VPC to the list IF that VPC has not already been
				// used in a VpnConnectionprofileAssignment
				if (!isAssigned) {
					pojos.add(pojo);
				}
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public VpcPojo registerVpc(VpcPojo vpc) throws RpcException {
		vpc.setCreateInfo(this.getUserLoggedIn(false).getEppn(),
				new java.util.Date());

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("creating vpc record on the server...");
				VirtualPrivateCloud moa = (VirtualPrivateCloud) getObject(Constants.MOA_VPC);
				info("populating moa");
				this.populateVpcMoa(vpc, moa);

				
				info("doing the Vpc.create...");
				this.doCreate(moa, getAWSRequestService());
				info("Vpc.create is complete...");

//				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
				return vpc;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectCreateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public VpcPojo updateVpc(VpcPojo vpc) throws RpcException {
		vpc.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating Vpc on the server...");
            VirtualPrivateCloud newData = (VirtualPrivateCloud) getObject(Constants.MOA_VPC);
            VirtualPrivateCloud baselineData = (VirtualPrivateCloud) getObject(Constants.MOA_VPC);

            info("populating newData...");
            populateVpcMoa(vpc, newData);

            info("populating baselineData...");
            populateVpcMoa(vpc.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getAWSRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return vpc;
	}

	@Override
	public void deleteVpc(VpcPojo vpc) throws RpcException {
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting VPC record on the server...");
				VirtualPrivateCloud moa = (VirtualPrivateCloud) getObject(Constants.MOA_VPC);
				info("populating moa");
				this.populateVpcMoa(vpc, moa);

				
				info("doing the Vpc.delete...");
				this.doDelete(moa, getAWSRequestService());
				info("Vpc.delete is complete...");
				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public List<String> getEmailTypeItems() {
		List<String> emailTypes = new java.util.ArrayList<String>();
		//primary, billing, security or operations
		emailTypes.add("billing");
		emailTypes.add("operations");
		emailTypes.add("primary");
		emailTypes.add("security");
		return emailTypes;
	}

	@Override
	public List<String> getVpcTypeItems() throws RpcException {
		List<String> vpcTypes = new java.util.ArrayList<String>();
		vpcTypes.add("1");
		vpcTypes.add("2");
		return vpcTypes;
	}

	@Override
	public CidrAssignmentQueryResultPojo getCidrAssignmentsForFilter(CidrAssignmentQueryFilterPojo filter)
			throws RpcException {

		CidrAssignmentQueryResultPojo result = new CidrAssignmentQueryResultPojo();
		List<CidrAssignmentPojo> pojos = new java.util.ArrayList<CidrAssignmentPojo>();
		try {
			CidrAssignmentQuerySpecification queryObject = (CidrAssignmentQuerySpecification) getObject(Constants.MOA_CIDR_ASSIGNMENT_QUERY_SPEC);
			CidrAssignment actionable = (CidrAssignment) getObject(Constants.MOA_CIDR_ASSIGNMENT);

			if (filter != null) {
				queryObject.setCidrAssignmentId(filter.getCidrAssignmentId());
				queryObject.setOwnerId(filter.getOwnerId());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getCidrsAssignmentForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<CidrAssignment> moas = actionable.query(queryObject,
					this.getCidrRequestService());
			for (CidrAssignment moa : moas) {
				CidrAssignmentPojo pojo = new CidrAssignmentPojo();
				CidrAssignmentPojo baseline = new CidrAssignmentPojo();
				this.populateCidrAssignmentPojo(moa, pojo);
				this.populateCidrAssignmentPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	public CidrAssignmentSummaryQueryResultPojo getCidrAssignmentSummaryForCidr(CidrAssignmentSummaryQueryFilterPojo filter) {
		CidrAssignmentSummaryQueryResultPojo result = new CidrAssignmentSummaryQueryResultPojo();
		result.setFilterUsed(filter);
		result.setResults(new java.util.ArrayList<CidrAssignmentSummaryPojo>());

		CidrAssignmentQueryResultPojo cidrAssignmentResult;
		if (filter != null) {
			CidrAssignmentQueryFilterPojo cidrAssignmentFilter = new CidrAssignmentQueryFilterPojo();
			cidrAssignmentFilter.setCidrAssignmentId(filter.getCidrAssignmentId());
			cidrAssignmentFilter.setOwnerId(filter.getOwnerId());
			cidrAssignmentResult = this.getCidrAssignmentsForFilter(cidrAssignmentFilter);
		}
		else {
			cidrAssignmentResult = this.getCidrAssignmentsForFilter(null);
		}
		info("getCidrAssignmentSummaryForCidr: There were " + cidrAssignmentResult.getResults().size() + " CidrAssignments returned from the ESB service.");

		for (CidrAssignmentPojo cidrAssignment : cidrAssignmentResult.getResults()) {
			if (cidrAssignment.getCidr().getNetwork().equalsIgnoreCase(filter.getCidr().getNetwork()) && 
					cidrAssignment.getCidr().getBits().equalsIgnoreCase(filter.getCidr().getBits())) {
				
				CidrAssignmentSummaryPojo casp = new CidrAssignmentSummaryPojo();
				casp.setCidrAssignment(cidrAssignment);
				
				VpcQueryFilterPojo vpcFilter = new VpcQueryFilterPojo();
				vpcFilter.setVpcId(cidrAssignment.getOwnerId());
				// should only be one
				VpcQueryResultPojo vpcResult = this.getVpcsForFilter(vpcFilter);
				info("getCidrAssignmentSummaryForCidr: There were " + vpcResult.getResults().size() + " VPCs returned "
						+ "from the ESB service for CidrAssignemtn.ownerId:  '" + 
						cidrAssignment.getOwnerId() + "'");
				for (VpcPojo vpc : vpcResult.getResults()) {
					casp.setVpc(vpc);
					
					AccountQueryFilterPojo acctFilter = new AccountQueryFilterPojo();
					acctFilter.setAccountId(vpc.getAccountId());
					// should only be one
					AccountQueryResultPojo acctResult = this.getAccountsForFilter(acctFilter);
					info("getCidrAssignmentSummaryForCidr: There were " + acctResult.getResults().size() + " Accounts returned "
							+ "from the ESB service for VPC.accountId:  '" + 
							cidrAssignment.getOwnerId() + "'");
					for (AccountPojo acct : acctResult.getResults()) {
						casp.setAccount(acct);
					}
				}
				result.getResults().add(casp);
			}
		}
		info("getCidrAssignmentSummaryForCidr: Returning " + result.getResults().size() + " CidrAssignmentSummary objects.");
		return result;
	}
	@Override
	public CidrAssignmentSummaryQueryResultPojo getCidrAssignmentSummariesForFilter(
			CidrAssignmentSummaryQueryFilterPojo filter) throws RpcException {
		
		CidrAssignmentSummaryQueryResultPojo result = new CidrAssignmentSummaryQueryResultPojo();
		result.setFilterUsed(filter);
		result.setResults(new java.util.ArrayList<CidrAssignmentSummaryPojo>());
		
		CidrAssignmentQueryResultPojo cidrAssignmentResult;
		if (filter != null) {
			CidrAssignmentQueryFilterPojo cidrAssignmentFilter = new CidrAssignmentQueryFilterPojo();
			cidrAssignmentFilter.setCidrAssignmentId(filter.getCidrAssignmentId());
			cidrAssignmentFilter.setOwnerId(filter.getOwnerId());
			cidrAssignmentResult = this.getCidrAssignmentsForFilter(cidrAssignmentFilter);
		}
		else {
			cidrAssignmentResult = this.getCidrAssignmentsForFilter(null);
		}
		info("getCidrAssignmentSummariesForFilter: There were " + cidrAssignmentResult.getResults().size() + " CidrAssignments returned from the ESB service.");
		
		for (CidrAssignmentPojo cidrAssignment : cidrAssignmentResult.getResults()) {
			CidrAssignmentSummaryPojo casp = new CidrAssignmentSummaryPojo();
			casp.setCidrAssignment(cidrAssignment);
			
			VpcQueryFilterPojo vpcFilter = new VpcQueryFilterPojo();
			vpcFilter.setVpcId(cidrAssignment.getOwnerId());
			// should only be one
			VpcQueryResultPojo vpcResult = this.getVpcsForFilter(vpcFilter);
			info("getCidrAssignmentSummariesForFilter: There were " + vpcResult.getResults().size() + " VPCs returned "
					+ "from the ESB service for CidrAssignemtn.ownerId:  '" + 
					cidrAssignment.getOwnerId() + "'");
			for (VpcPojo vpc : vpcResult.getResults()) {
				casp.setVpc(vpc);
				
				AccountQueryFilterPojo acctFilter = new AccountQueryFilterPojo();
				acctFilter.setAccountId(vpc.getAccountId());
				// should only be one
				AccountQueryResultPojo acctResult = this.getAccountsForFilter(acctFilter);
				info("getCidrAssignmentSummariesForFilter: There were " + acctResult.getResults().size() + " Accounts returned "
						+ "from the ESB service for VPC.accountId:  '" + 
						cidrAssignment.getOwnerId() + "'");
				for (AccountPojo acct : acctResult.getResults()) {
					casp.setAccount(acct);
				}
			}
			result.getResults().add(casp);
		}
		info("getCidrAssignmentSummariesForFilter: Returning " + result.getResults().size() + " CidrAssignmentSummary objects.");
		info(result.getResults().toString());
		return result;
	}

	@Override
	public String testPropertyReload() throws RpcException {
		try {
			// get properties here so refresh will work
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			return generalProps.getProperty("refreshableProperty", "'refreshableProperty' propery not found");
//			return "Static content";
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e.getMessage());
		}
	}

	@Override
	public List<VpcPojo> getVpcsForAccount(String accountId) throws RpcException {
		VpcQueryFilterPojo filter = new VpcQueryFilterPojo();
		filter.setAccountId(accountId);
		VpcQueryResultPojo results = this.getVpcsForFilter(filter); 
		return results.getResults();
	}

	@Override
	public List<CidrPojo> getUnassignedCidrs() throws RpcException {
		List<CidrPojo> unassignedCidrs = new java.util.ArrayList<CidrPojo>();
		CidrQueryResultPojo cidrResult = this.getCidrsForFilter(null);
//		CidrAssignmentQueryResultPojo cidrAssignmentResult = this.getCidrAssignmentsForFilter(null);
		
		for (CidrSummaryPojo cidrSummary : cidrResult.getResults()) {
			if (cidrSummary.getCidr() != null) {
//				if (!hasAssignment(cidr, cidrAssignmentResult.getResults())) {
//					unassignedCidrs.add(cidr);
//				}
				unassignedCidrs.add(cidrSummary.getCidr());
			}
		}
		return unassignedCidrs;
	}

	private boolean hasAssignment(CidrPojo cidr, List<CidrAssignmentPojo> cidrAssignments) {
		for (CidrAssignmentPojo cidrAssignment : cidrAssignments) {
			if (cidrAssignment.getCidr().getNetwork().equalsIgnoreCase(cidr.getNetwork()) && 
					cidrAssignment.getCidr().getBits().equalsIgnoreCase(cidr.getBits())) {
				
				return true;
			}
//			if (cidrAssignment.getCidr().getCidrId().equalsIgnoreCase(cidr.getCidrId())) {
//				return true;
//			}
		}
		
		return false;
	}

	public ProducerPool getIdentityServiceProducerPool() {
		return identityServiceProducerPool;
	}

	public void setIdentityServiceProducerPool(ProducerPool identityServiceProducerPool) {
		this.identityServiceProducerPool = identityServiceProducerPool;
	}

	@Override
	public DirectoryMetaDataPojo getDirectoryMetaDataForPublicId(String publicId) throws RpcException {
		// - check cache
		// - if cache exists
		// 	- return DirectoryMetaDataPojo from cache
		// - else:
		// 	- do a FullPerson.Query passing netid in a FullPersonQuerySpecification
		// 	- populate DirectoryMetaDataPojo with FullPerson data
		//  - add DirectoryMetaDataPojo to cache
		//  - return pojo
		DirectoryMetaDataPojo dmd = (DirectoryMetaDataPojo) Cache.getCache().get(
				Constants.NET_ID + publicId);
		if (dmd!= null) {
			return dmd;
		}
		else {
			dmd = new DirectoryMetaDataPojo();
			try {
				FullPersonQuerySpecification queryObject = (FullPersonQuerySpecification) getObject(Constants.MOA_FULL_PERSON_QUERY_SPEC);
				FullPerson actionable = (FullPerson) getObject(Constants.MOA_FULL_PERSON);

				queryObject.setPublicId(publicId);

//				String authUserId = this.getAuthUserIdForHALS();
//				actionable.getAuthentication().setAuthUserId(authUserId);
//				info("[getDirectoryMetaDataForNetId] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
				
				@SuppressWarnings("unchecked")
				List<FullPerson> moas = actionable.query(queryObject,
						this.getIdentityServiceRequestService());
				
				// should only get one back
				info("[getDirectoryMetaDataForNetId] got " + moas.size() + " FullPerson moas back from ESB for PublicID '" + publicId + "'");
				for (FullPerson moa : moas) {
					dmd.setFirstName(moa.getPerson().getPersonalName().getFirstName());
					dmd.setLastName(moa.getPerson().getPersonalName().getLastName());
					dmd.setPublicId(moa.getPublicId());
				}

				Cache.getCache().put(Constants.NET_ID + publicId, dmd);
				return dmd;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectQueryException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
		}
	}

	@Override
	public boolean isCidrAssigned(CidrPojo cidr) throws RpcException {
		CidrAssignmentQueryResultPojo cidrAssignmentResult = this.getCidrAssignmentsForFilter(null);
		return hasAssignment(cidr, cidrAssignmentResult.getResults());
	}

	@Override
	public CidrAssignmentStatus getCidrAssignmentStatusForCidr(CidrPojo cidr) throws RpcException {
		CidrAssignmentStatus cas = new CidrAssignmentStatus();
		cas.setAssigned(false);
		if (cidr == null) {
			return cas;
		}
		CidrAssignmentQueryResultPojo cidrAssignmentResult = this.getCidrAssignmentsForFilter(null);
		for (CidrAssignmentPojo cidrAssignment : cidrAssignmentResult.getResults()) {
			if (cidrAssignment.getCidr().getNetwork().equalsIgnoreCase(cidr.getNetwork()) && 
					cidrAssignment.getCidr().getBits().equalsIgnoreCase(cidr.getBits())) {

				cas.setAssigned(true);
				cas.setCidrAssignment(cidrAssignment);
				return cas;
			}
		}
		return cas;
	}

	@Override
	public String getAwsAccountsURL() throws RpcException {
		try {
			Properties props = getAppConfig().getProperties(AWS_URL_PROPERTIES);
			return props.getProperty("awsAccountsURL", "'awsAccountsURL' propery not found");
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public String getAwsBillingManagementURL() throws RpcException {
		try {
			Properties props = getAppConfig().getProperties(AWS_URL_PROPERTIES);
			return props.getProperty("awsBillingManagementURL", "'awsBillingManagementURL' propery not found");
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public VpcpQueryResultPojo getVpcpsForFilter(VpcpQueryFilterPojo filter) throws RpcException {
		VpcpQueryResultPojo result = new VpcpQueryResultPojo();
		List<VpcpPojo> pojos = new java.util.ArrayList<VpcpPojo>();
		if (fakeVpcpGen) {
			if (filter != null) {
				VpcpPojo vpcp = vpcpMap.get(filter.getProvisioningId());
				if (vpcp != null) {
					pojos.add(vpcp);
					Collections.sort(pojos);
				}
			}
			else {
				Iterator<String> keys = vpcpMap.keySet().iterator();
				while (keys.hasNext()) {
					pojos.add(vpcpMap.get(keys.next()));
				}
			}
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		}
		try {
			VirtualPrivateCloudProvisioningQuerySpecification queryObject = (VirtualPrivateCloudProvisioningQuerySpecification) getObject(Constants.MOA_VPCP_QUERY_SPEC);
			VirtualPrivateCloudProvisioning actionable = (VirtualPrivateCloudProvisioning) getObject(Constants.MOA_VPCP);

			if (filter != null) {
				if (filter.isDefaultMaxVpcps()) {
					info("[getVpcpsForFilter] using 'maxVpcps' query language to get VPCPs");
					QueryLanguage ql = queryObject.newQueryLanguage();
					ql.setName("maxVpcps");
					ql.setType("hql");
					ql.setMax(this.toStringFromInt(filter.getMaxRows()));
					queryObject.setQueryLanguage(ql);
				}
				else if (filter.isAllVpcps()) {
					info("[getVpcpsForFilter] using 'allVpcps' query language to get VPCPs");
					QueryLanguage ql = queryObject.newQueryLanguage();
					ql.setName("allVpcps");
					ql.setType("hql");
					queryObject.setQueryLanguage(ql);
				}
				else {
					queryObject.setProvisioningId(filter.getProvisioningId());
					queryObject.setType(filter.getType());
					queryObject.setComplianceClass(filter.getComplianceClass());
					queryObject.setCreateUser(filter.getCreateUser());
					queryObject.setLastUpdateUser(filter.getUpdateUser());
					info("[getVpcpsForFilter] getting VPCPs for filter: " + queryObject.toXmlString());
				}
			}
			else {
				info("[getVpcpsForFilter] no filter passed in.  Getting all VPCPs");
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getVpcpsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String s_interval = generalProps.getProperty("vpcpListTimeoutMillis", "30000");
			int interval = Integer.parseInt(s_interval);

			RequestService reqSvc = this.getAWSRequestService();
			info("setting RequestService's timeout to: " + interval + " milliseconds");
			((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(interval);

			@SuppressWarnings("unchecked")
			List<VirtualPrivateCloudProvisioning> moas = 
				actionable.query(queryObject, reqSvc);
			
			info("[getVpcpsForFilter] got " + moas.size() + " VPCPs back from the server.");
			for (VirtualPrivateCloudProvisioning moa : moas) {
				VpcpPojo pojo = new VpcpPojo();
				VpcpPojo baseline = new VpcpPojo();
				this.populateVpcpPojo(moa, pojo);
				this.populateVpcpPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public void deleteVpcp(VpcpPojo vpc) throws RpcException {
		// TODO Auto-generated method stub
		
	}

	private void populateProvisioningStepMoa(ProvisioningStepPojo pojo, ProvisioningStep moa) throws EnterpriseFieldException {
		moa.setProvisioningId(pojo.getProvisioningId());
		moa.setProvisioningStepId(pojo.getProvisioningStepId());
		moa.setStepId(pojo.getStepId());
		moa.setType(pojo.getType());
		moa.setDescription(pojo.getDescription());
		moa.setStatus(pojo.getStatus());
		moa.setStepResult(pojo.getStepResult());
		moa.setActualTime(pojo.getActualTime());
		moa.setAnticipatedTime(pojo.getAnticipatedTime());
		if (pojo.getProperties() != null) {
			Iterator<String> keys = pojo.getProperties().keySet().iterator();
	        while (keys.hasNext()) {
	        	com.amazon.aws.moa.objects.resources.v1_0.Property stepProps = moa.newProperty();
	        	stepProps.setKey(keys.next());
	        	stepProps.setValue((String) pojo.getProperties().get(stepProps.getKey()));
	        	moa.addProperty(stepProps);
	        }
		}
	}
	@SuppressWarnings("unchecked")
	private void populateProvisioningStepPojo(ProvisioningStep moa, ProvisioningStepPojo pojo) {
		pojo.setProvisioningId(moa.getProvisioningId());
		pojo.setProvisioningStepId(moa.getProvisioningStepId());
		pojo.setStepId(moa.getStepId());
		pojo.setType(moa.getType());
		pojo.setDescription(moa.getDescription());
		pojo.setStatus(moa.getStatus());
		pojo.setStepResult(moa.getStepResult());
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getProperty() != null) {
			for (com.amazon.aws.moa.objects.resources.v1_0.Property stepProps : (List<com.amazon.aws.moa.objects.resources.v1_0.Property>) moa.getProperty()) {
				pojo.getProperties().put(stepProps.getKey(), stepProps.getValue());
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void populateVpcpMoa(VpcpPojo pojo,
			VirtualPrivateCloudProvisioning moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getProvisioningId() != null) {
			moa.setProvisioningId(pojo.getProvisioningId());
		}
		moa.setStatus(pojo.getStatus());
		moa.setProvisioningResult(pojo.getProvisioningResult());
		moa.setActualTime(pojo.getActualTime());
		moa.setAnticipatedTime(pojo.getAnticipatedTime());
		if (pojo.getVpcRequisition() != null) {
			VirtualPrivateCloudRequisition vpcr = moa.newVirtualPrivateCloudRequisition();
			this.populateVpcRequisitionMoa(pojo.getVpcRequisition(), vpcr);
			moa.setVirtualPrivateCloudRequisition(vpcr);
		}

		// provisioningsteps
		for (ProvisioningStepPojo psp : pojo.getProvisioningSteps()) {
			ProvisioningStep ps = moa.newProvisioningStep();
			this.populateProvisioningStepMoa(psp, ps);
			moa.addProvisioningStep(ps);
		}
		
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings("unchecked")
	private void populateVpcpPojo(VirtualPrivateCloudProvisioning moa,
			VpcpPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {

		pojo.setProvisioningId(moa.getProvisioningId());
		pojo.setStatus(moa.getStatus());
		pojo.setProvisioningResult(moa.getProvisioningResult());
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getVirtualPrivateCloudRequisition() != null) {
			VpcRequisitionPojo vpcr = new VpcRequisitionPojo();
			this.populateVpcRequisitionPojo(moa.getVirtualPrivateCloudRequisition(), vpcr);
			pojo.setVpcRequisition(vpcr);
		}

		// provisioningsteps
		List<ProvisioningStepPojo> pspList = new java.util.ArrayList<ProvisioningStepPojo>();
		for (ProvisioningStep ps : (List<ProvisioningStep>) moa.getProvisioningStep()) {
			ProvisioningStepPojo psp = new ProvisioningStepPojo();
			this.populateProvisioningStepPojo(ps, psp);
			pspList.add(psp);
//			pojo.getProvisioningSteps().add(psp);
		}
		Collections.sort(pspList);
		pojo.setProvisioningSteps(pspList);

		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public VpcpPojo generateVpcp(VpcRequisitionPojo vpcRequisition) throws RpcException {
		try {
			if (fakeVpcpGen) {
				VpcpPojo vpcpPojo = new VpcpPojo();
				vpcpPojo.setProvisioningId(UUID.uuid());
				vpcpPojo.setVpcRequisition(vpcRequisition);
				info("VPC Type is: " + vpcpPojo.getVpcRequisition().getType());
				vpcpPojo.setStatus(Constants.VPCP_STATUS_PENDING);
				vpcpPojo.setProvisioningResult(Constants.VPCP_RESULT_FAILURE);
				for (int i=1; i<21; i++) {
					ProvisioningStepPojo psp = new ProvisioningStepPojo();
					psp.setStepId(this.toStringFromInt(i));
					if (i==1) {
						psp.setStatus(Constants.PROVISIONING_STEP_STATUS_COMPLETED);
						psp.setStepResult(Constants.VPCP_STEP_RESULT_SUCCESS);
					}
					else {
						psp.setStatus(Constants.VPCP_STEP_STATUS_PENDING);
						psp.setStepResult(Constants.VPCP_STEP_RESULT_FAILURE);
					}
					vpcpPojo.getProvisioningSteps().add(psp);
				}
				vpcpMap.put(vpcpPojo.getProvisioningId(), vpcpPojo);
				return vpcpPojo;
			}
			info("generating Vpcp on the server...");
			VirtualPrivateCloudProvisioning actionable = (VirtualPrivateCloudProvisioning) getObject(Constants.MOA_VPCP);
			VirtualPrivateCloudRequisition seed = (VirtualPrivateCloudRequisition) getObject(Constants.MOA_VPC_REQUISITION);
			info("populating moa");
			this.populateVpcRequisitionMoa(vpcRequisition, seed);

			
			info("doing the Vpcp.generate...");
			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("Vpcp.generate seed data is: " + seed.toXmlString());
			@SuppressWarnings("unchecked")
			List<VirtualPrivateCloudProvisioning> result = actionable.generate(seed, getAWSRequestService());
			// TODO if more than one returned, it's an error...
			VpcpPojo vpcpPojo = new VpcpPojo();
			for (VirtualPrivateCloudProvisioning vpcp : result) {
				info("generated VPCP is: " + vpcp.toXmlString());
				this.populateVpcpPojo(vpcp, vpcpPojo);
			}
			info("Vpcp.generate is complete...");

			return vpcpPojo;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseObjectGenerateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public VpcpPojo updateVpcp(VpcpPojo vpc) throws RpcException {
		// TODO Auto-generated method stub
		vpc.setUpdateInfo(this.getCachedUser().getPublicId());
		return null;
	}

	@Override
	public List<String> getComplianceClassItems() throws RpcException {
		List<String> types = new java.util.ArrayList<String>();
		types.add("HIPAA");
		types.add("Standard");
		return types;
	}

	@Override
	public BillQueryResultPojo getBillsForFilter(BillQueryFilterPojo filter) throws RpcException {
		BillQueryResultPojo result = new BillQueryResultPojo();
		List<BillPojo> pojos = new java.util.ArrayList<BillPojo>();
		try {
			BillQuerySpecification queryObject = (BillQuerySpecification) getObject(Constants.MOA_BILL_QUERY_SPEC);
			Bill actionable = (Bill) getObject(Constants.MOA_BILL);

			if (filter != null) {
				queryObject.setPayerAccountId(filter.getPayerAccountId());
				queryObject.setType(filter.getType());
				
				org.openeai.moa.objects.resources.Date billDate = queryObject.newBillDate();
				this.populateDate(billDate, filter.getBillDate());
				queryObject.setBillDate(billDate);

				org.openeai.moa.objects.resources.Date startDate = queryObject.newStartDate();
				this.populateDate(startDate, filter.getStartDate());
				queryObject.setStartDate(startDate);

				org.openeai.moa.objects.resources.Date endDate = queryObject.newEndDate();
				this.populateDate(endDate, filter.getEndDate());
				queryObject.setEndDate(endDate);
				
				info("[getBillsForFilter] getting Billss for filter: " + queryObject.toXmlString());
			}
			else {
				info("[getBillsForFilter] no filter passed in.  Getting all Bills");
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getBillsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<Bill> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getBillsForFilter] got " + moas.size() + " Bills back from the server.");
			for (Bill moa : moas) {
				BillPojo pojo = new BillPojo();
//				BillPojo baseline = new BillPojo();
				this.populateBillPojo(moa, pojo);
//				this.populateBillPojo(moa, baseline);
//				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@Override
	public void refreshMasterBillData() throws RpcException {
		try {
			defaultRequestTimeoutInterval = 200000;
			ThreadPool tPool = (ThreadPool) getAppConfig().getObject("VPCPThreadPool");
			tPool.addJob(new BillLoadingThread());
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
		} catch (ThreadPoolException e) {
			e.printStackTrace();
		}
	}

	private void retrieveAllMasterBillData() {
		BillQueryResultPojo result = this.getBillsForFilter(null);
		masterBills.clear();
		for (BillPojo bill : result.getResults()) {
			masterBills.add(bill);
		}
		Collections.sort(masterBills);
		info("Added " + masterBills.size() + " master bills to the internal list.");
	}

	private void refreshBillsByAccountMap() {
    	// potential things we might need:
    	// - bills by accounts (including all line items).  create bills
    	// 	 for each linked account and only include the line items for 
    	// 	 that linked account.  so, basically, break the overall bill
    	//   into multiple bills, one for each linked account included 
    	//   in the master bill
		billsByAccount.clear();
		for (BillPojo masterBill : masterBills) {
			for (LineItemPojo masterLineItem : masterBill.getLineItems()) {
				String accountIdKey = null;
				if (masterLineItem.getLinkedAccountId() != null && 
					masterLineItem.getRecordType().equalsIgnoreCase(Constants.LINEITEM_RECORD_TYPE_LINKED)) {
					
					// it's a linked account line item
					accountIdKey = masterLineItem.getLinkedAccountId();
				}
				else if (masterLineItem.getRecordType().equalsIgnoreCase(Constants.LINEITEM_RECORD_TYPE_PAYER)) {
					// it's a payer account line item
					accountIdKey = masterLineItem.getPayerAccountId();
				}
				
				if (accountIdKey != null) {
					List<BillPojo> linkedBills = billsByAccount.get(accountIdKey);
					if (linkedBills == null) {
						// if the list is null, it's a new bill for this account
						BillPojo bill = new BillPojo();
						bill.setBillId(masterBill.getBillId());
						bill.setBillDate(masterLineItem.getInvoiceDatetime());
						bill.setBillingPeriodStartDate(masterLineItem.getBillingPeriodStartDatetime());
						bill.setBillingPeriodEndDate(masterLineItem.getBillingPeriodEndDatetime());
						bill.setPayerAccountId(masterLineItem.getPayerAccountId());
						bill.getLineItems().add(masterLineItem);
						linkedBills = new java.util.ArrayList<BillPojo>();
						linkedBills.add(bill);
						billsByAccount.put(accountIdKey, linkedBills);
						info("added line item to NEW bill (no list found) for account " + accountIdKey);
					}
					else {
						// find a bill in the list that has a matching bill date
						boolean foundBill = false;
						billLoop: for (BillPojo bill : linkedBills) {
							if (bill.getBillId().equals(masterBill.getBillId())) {
								foundBill = true;
								bill.getLineItems().add(masterLineItem);
								info("added line item to EXISTING bill for account " + accountIdKey);
								break billLoop;
							}
						}
						// if none found, create a new bill and add it to the list of linkedBills
						if (!foundBill) {
							BillPojo bill = new BillPojo();
							bill.setBillId(masterBill.getBillId());
							bill.setBillDate(masterLineItem.getInvoiceDatetime());
							bill.setBillingPeriodStartDate(masterLineItem.getBillingPeriodStartDatetime());
							bill.setBillingPeriodEndDate(masterLineItem.getBillingPeriodEndDatetime());
							bill.setPayerAccountId(masterLineItem.getPayerAccountId());
							bill.getLineItems().add(masterLineItem);
							info("added line item to NEW bill (list found) for account " + accountIdKey);
							linkedBills.add(bill);
						}
					}
				}
			}
		}
		info("created " + billsByAccount.size() + " separate bills for all accounts");
		Iterator<String> iter = billsByAccount.keySet().iterator();
		while (iter.hasNext()) {
			String accountId = iter.next();
			List<BillPojo> bills = billsByAccount.get(accountId);
			info("account id: " + accountId + " has " + bills.size() + " bills");
			for (BillPojo bill : bills) {
				info("bill " + bill.getBillId() + " has " + bill.getLineItems().size() + " line items");
			}
		}
	}
	
    private class BillLoadingThread implements java.lang.Runnable {
        public BillLoadingThread() {
        }

        public void run() {
        	info("Refreshing all master bill data...");
        	retrieveAllMasterBillData();
        	info("Done refreshing all master bill data...");

        	// TODO: this is where we could cache LineItem info as well.
        	// this would make the user interface more responsive since it's 
        	// static data and won't change unless/until we refresh the actual
        	// master bill data.  we just don't know yet what structures we really need
        	// so it's something we'll likely do once we understand what the user
        	// interfaces need to do a bit more.
        	info("Refreshing bills by account map...");
        	refreshBillsByAccountMap();
        	defaultRequestTimeoutInterval = 20000;
        	info("Done refreshing bills by account map.");
        }
    }

	@Override
	public List<BillPojo> getCachedBillsForAccount(String accountId) throws RpcException {
		info("retrieving bills for account " + accountId);
		return billsByAccount.get("357163498835");
	}

	@Override
	public ElasticIpQueryResultPojo getElasticIpsForFilter(ElasticIpQueryFilterPojo filter) throws RpcException {
		ElasticIpQueryResultPojo result = new ElasticIpQueryResultPojo();
		List<ElasticIpSummaryPojo> summaries = new java.util.ArrayList<ElasticIpSummaryPojo>();
		try {
			
			// get ALL assignments first and process them in memory instead 
			// of doing individual queries by elastic ip id.
			ElasticIpAssignmentQueryFilterPojo eia_filter = new ElasticIpAssignmentQueryFilterPojo();
			ElasticIpAssignmentQueryResultPojo eia_result = this.getElasticIpAssignmentsForFilter(eia_filter);
			
			ElasticIpQuerySpecification queryObject = (ElasticIpQuerySpecification) getObject(Constants.MOA_ELASTIC_IP_QUERY_SPEC);
			ElasticIp actionable = (ElasticIp) getObject(Constants.MOA_ELASTIC_IP);

			if (filter != null) {
				queryObject.setElasticIpId(filter.getElasticIpId());
				queryObject.setElasticIpAddress(filter.getElasticIpAddress());
				queryObject.setAssociatedIpAddress(filter.getAssociatedIpAddress());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getElasticIpsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<ElasticIp> moas = actionable.query(queryObject,
					this.getElasticIpRequestService());
			for (ElasticIp moa : moas) {
				ElasticIpSummaryPojo summary = new ElasticIpSummaryPojo();
				
				ElasticIpPojo pojo = new ElasticIpPojo();
				ElasticIpPojo baseline = new ElasticIpPojo();
				this.populateElasticIpPojo(moa, pojo);
				this.populateElasticIpPojo(moa, baseline);
				pojo.setBaseline(baseline);
				summary.setElasticIp(pojo);
				
				// just go through the internal list here instead of doing an individual query
				assignmentLoop: for (ElasticIpAssignmentPojo assignment : eia_result.getResults()) {
					if (assignment.getElasticIp().getElasticIpId().equals(pojo.getElasticIpId())) {
						summary.setElasticIpAssignment(assignment);
						// TODO: remove assignment from eia_result.getResults()??
						break assignmentLoop;
					}
				}
				summaries.add(summary);
			}

			Collections.sort(summaries);
			result.setResults(summaries);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public ElasticIpPojo createElasticIp(ElasticIpPojo elasticIp) throws RpcException {
		elasticIp.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("creating ElasticIp record on the server...");
				ElasticIp moa = (ElasticIp) getObject(Constants.MOA_ELASTIC_IP);
				info("populating moa");
				this.populateElasticIpMoa(elasticIp, moa);
				info("Creating ElasticIP: " + moa.toXmlString());
				
				info("doing the ElasticIp.create...");
				this.doCreate(moa, getElasticIpRequestService());
				info("ElasticIp.create is complete...");

				return elasticIp;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectCreateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (XmlEnterpriseObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public ElasticIpPojo deleteElasticIp(ElasticIpPojo elasticIp) throws RpcException {
		if (!useEsbService) {
			return elasticIp;
		} 
		else {
			try {
				// TODO: need to see if there are any assignments in this summary and if so, delete those too
				info("deleting ElasticIp record on the server...");
				ElasticIp moa = (ElasticIp) getObject(Constants.MOA_ELASTIC_IP);
				info("populating moa");
				this.populateElasticIpMoa(elasticIp, moa);

				
				info("doing the ElasticIp.delete...");
				this.doDelete(moa, getElasticIpRequestService());
				info("ElasticIp.delete is complete...");

				return elasticIp;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public ElasticIpPojo updateElasticIp(ElasticIpPojo elasticIp) throws RpcException {
		elasticIp.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating elasticIp on the server...");
            ElasticIp newData = (ElasticIp) getObject(Constants.MOA_ELASTIC_IP);
            ElasticIp baselineData = (ElasticIp) getObject(Constants.MOA_ELASTIC_IP);

            info("populating newData...");
            populateElasticIpMoa(elasticIp, newData);

            info("populating baselineData...");
            populateElasticIpMoa(elasticIp.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getElasticIpRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return elasticIp;
	}

	@Override
	public boolean isElasticIpAssigned(ElasticIpPojo elasticIp) throws RpcException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ElasticIpAssignmentStatusPojo getElasticIpAssignmentStatusForElasticIp(ElasticIpPojo elasticIp)
			throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElasticIpAssignmentPojo> getElasticIpAssignmentsForUserLoggedIn() throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElasticIpAssignmentQueryResultPojo getElasticIpAssignmentsForFilter(
			ElasticIpAssignmentQueryFilterPojo filter) throws RpcException {

		ElasticIpAssignmentQueryResultPojo result = new ElasticIpAssignmentQueryResultPojo();
		List<ElasticIpAssignmentPojo> pojos = new java.util.ArrayList<ElasticIpAssignmentPojo>();
		try {
			ElasticIpAssignmentQuerySpecification queryObject = (ElasticIpAssignmentQuerySpecification) getObject(Constants.MOA_ELASTIC_IP_ASSIGNMENT_QUERY_SPEC);
			ElasticIpAssignment actionable = (ElasticIpAssignment) getObject(Constants.MOA_ELASTIC_IP_ASSIGNMENT);

			if (filter != null) {
				queryObject.setElasticIpAssignmentId(filter.getAssignmentId());
				queryObject.setOwnerId(filter.getOwnerId());
				queryObject.setElasticIpId(filter.getElasticIpId());
				queryObject.setElasticIpAddress(filter.getElasticIpAddress());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getElasticIpAssignmentsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<ElasticIpAssignment> moas = actionable.query(queryObject,
					this.getElasticIpRequestService());
			for (ElasticIpAssignment moa : moas) {
				ElasticIpAssignmentPojo pojo = new ElasticIpAssignmentPojo();
				ElasticIpAssignmentPojo baseline = new ElasticIpAssignmentPojo();
				this.populateElasticIpAssignmentPojo(moa, pojo);
				this.populateElasticIpAssignmentPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public ElasticIpAssignmentPojo generateElasticIpAssignment(ElasticIpAssignmentRequisitionPojo req)
			throws RpcException {

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				ElasticIpRequisition requisition = (ElasticIpRequisition) getObject(Constants.MOA_ELASTIC_IP_REQUISITION);
				requisition.setOwnerId(req.getOwnerId());

				ElasticIpAssignment moa = (ElasticIpAssignment) getObject(Constants.MOA_ELASTIC_IP_ASSIGNMENT);

				info("generating elastic ip Assignment record on the server:  " + requisition.toXmlString());
				List<ActionableEnterpriseObject> results = this.doGenerate(moa, requisition, getElasticIpRequestService());
				if (results.size() != 1) {
					// error
					String msg = "Incorrect number of ElasticIpAssignments generated.  Expected 1, got " + results.size();
					throw new RpcException(msg);
				}
				else {
					ElasticIpAssignment generated = (ElasticIpAssignment)results.get(0);
					info("ElasticIpAssignment returned from the generate: " + generated.toXmlString());
					ElasticIpAssignmentPojo eipAssignment = new ElasticIpAssignmentPojo();
					info("populating pojo");
					this.populateElasticIpAssignmentPojo(generated, eipAssignment);
					info("eipAssignment.generate is complete...");

					return eipAssignment;
				}
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectGenerateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (XmlEnterpriseObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public ElasticIpAssignmentPojo updateElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment)
			throws RpcException {
		
		elasticIpAssignment.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating ElasticIpAssignment on the server...");
            ElasticIpAssignment newData = (ElasticIpAssignment) getObject(Constants.MOA_ELASTIC_IP_ASSIGNMENT);
            ElasticIpAssignment baselineData = (ElasticIpAssignment) getObject(Constants.MOA_ELASTIC_IP_ASSIGNMENT);

            info("populating newData...");
            populateElasticIpAssignmentMoa(elasticIpAssignment, newData);
            info("[updateElasticIpAssignment] moa's associated private ip is: " + newData.getElasticIp().getAssociatedIpAddress());

            info("populating baselineData...");
            populateElasticIpAssignmentMoa(elasticIpAssignment.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String s_interval = generalProps.getProperty("vpcpListTimeoutMillis", "30000");
			int interval = Integer.parseInt(s_interval);

			RequestService reqSvc = this.getElasticIpRequestService();
			info("setting RequestService's timeout to: " + interval + " milliseconds");
			((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(interval);

            info("doing the update...");
            doUpdate(newData, reqSvc);
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return elasticIpAssignment;
	}

	@Override
	public void deleteElasticIpAssignment(ElasticIpAssignmentPojo eiaAssignment) throws RpcException {
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting ElasticIpAssignment record on the server...");
				ElasticIpAssignment moa = (ElasticIpAssignment) getObject(Constants.MOA_ELASTIC_IP_ASSIGNMENT);
				info("populating moa");
				this.populateElasticIpAssignmentMoa(eiaAssignment, moa);

				
				info("doing the ElasticIpAssignment.delete...");
				this.doDelete(moa, getElasticIpRequestService());
				info("ElasticIpAssignment.delete is complete...");

				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public List<ElasticIpPojo> getUnassignedElasticIps() throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElasticIpAssignmentSummaryQueryResultPojo getElasticIpAssignmentSummariesForFilter(
			ElasticIpAssignmentSummaryQueryFilterPojo filter) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AWSServiceSummaryPojo getAWSServiceMap() throws RpcException {
		info("checking cache for existing service summary...");
		serviceSummary = (AWSServiceSummaryPojo) Cache.getCache().get(
				Constants.SERVICE_SUMMARY + getCurrentSessionId());

		if (serviceSummary == null) {
			info("no service summary found in cache, building service summary.");
		    serviceSummary = new AWSServiceSummaryPojo();
		}
		else {
			// check how long it's been there and refresh if it's "stale"
			java.util.Date currentTime = new java.util.Date();
			if (serviceSummary.getUpdateTime() != null) {
				long millisSinceUpdated = currentTime.getTime() - serviceSummary.getUpdateTime().getTime();
				// if it's less than one minute, don't refresh
				if (millisSinceUpdated <= 60000) {
					info("service summary found in cache and it's NOT stale, returning cached service summary");
				    info("[getAWSServiceMap] there are " + serviceSummary.getServiceStatistics().size() + " service stats in the cached summary.");
					return serviceSummary;
				}
				info("service summary was found in cached but it's stale, refreshing service summary");
			}
			info("service summary was found in cache but it has no last updated time, refreshing service summary");
		}

		int svcCnt = 0;

		AWSServiceQueryResultPojo servicesResult = this.getServicesForFilter(null);
		
		// key is category
		HashMap<String, List<AWSServicePojo>> awsServicesMap = 
			new HashMap<String, List<AWSServicePojo>>();
		
		svcCnt = servicesResult.getResults().size();
		for (AWSServicePojo service : servicesResult.getResults()) {
	    	// see if this category is already in the awsServicesMap
			String category = "Unknown";
			if (service.getConsoleCategories() != null && 
				service.getConsoleCategories().size() > 0) {
				
				for (String l_category : service.getConsoleCategories()) {
					this.addServiceToCategory(l_category, service, awsServicesMap);
				}
			}
			else if (service.getAwsCategories() != null && 
					service.getAwsCategories().size() > 0) {
				
				// TODO: have to check all the categories they're in...not just the fist one
				for (String l_category : service.getAwsCategories()) {
					this.addServiceToCategory(l_category, service, awsServicesMap);
				}
			}
			else {
				// add service to the unknown category
			}
		}
	    info("[getAWSServiceMap] returning " + svcCnt +" services in " + awsServicesMap.size() + " categories of services.");
	    serviceSummary.setServiceList(servicesResult.getResults());
	    serviceSummary.setServiceMap(awsServicesMap);
	    serviceSummary.initializeStatistics();
	    serviceSummary.setUpdateTime(new java.util.Date());
		Cache.getCache().put(Constants.SERVICE_SUMMARY + getCurrentSessionId(), serviceSummary);
		return serviceSummary;
	}
	
	private void addServiceToCategory(String category, AWSServicePojo service, HashMap<String, List<AWSServicePojo>> awsServicesMap) {
		List<AWSServicePojo> servicesForCat = awsServicesMap.get(category);
		if (servicesForCat == null) {
			servicesForCat = new java.util.ArrayList<AWSServicePojo>();
			servicesForCat.add(service);
			awsServicesMap.put(category, servicesForCat);
		}
		else {
			// need to see if a service by this combined name, alternate name, aws name 
			// exists and if it does, don't add it
			boolean doAdd = true;
			svcLoop: for (AWSServicePojo existingSvc : servicesForCat) {
				if (existingSvc.getCombinedServiceName() != null && 
   					existingSvc.getCombinedServiceName().length() > 0 &&
					existingSvc.getCombinedServiceName().equalsIgnoreCase(service.getCombinedServiceName())) {
					doAdd = false;
					break svcLoop;
				}
				else if (existingSvc.getAlternateServiceName() != null && 
   					existingSvc.getAlternateServiceName().length() > 0 &&
   					existingSvc.getAlternateServiceName().equalsIgnoreCase(service.getAlternateServiceName())) {
					doAdd = false;
					break svcLoop;
    			}
			}
			if (doAdd) {
    			servicesForCat.add(service);
			}
		}
	}

	private void populateAWSServiceMoa(AWSServicePojo pojo, com.amazon.aws.moa.jmsobjects.services.v1_0.Service moa) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, EnterpriseFieldException {
		moa.setServiceId(pojo.getServiceId());
		moa.setAwsServiceCode(pojo.getAwsServiceCode());
		moa.setAwsServiceName(pojo.getAwsServiceName());
		moa.setCombinedServiceName(pojo.getCombinedServiceName());
		moa.setAlternateServiceName(pojo.getAlternateServiceName());
		moa.setAwsStatus(pojo.getAwsStatus());
		moa.setSiteStatus(pojo.getSiteStatus());
		moa.setAwsServiceLandingPageUrl(pojo.getAwsLandingPageUrl());
		moa.setSiteServiceLandingPageUrl(moa.getSiteServiceLandingPageUrl());
		moa.setDescription(pojo.getDescription());
		moa.setAwsHipaaEligible(this.toStringFromBoolean(pojo.isAwsHipaaEligible()));
		moa.setSiteHipaaEligible(this.toStringFromBoolean(pojo.isSiteHipaaEligible()));
		
		if (pojo.getConsoleCategories().size() > 0) {
			for (String consoleCat : pojo.getConsoleCategories()) {
				moa.addConsoleCategory(consoleCat);
			}
		}
		if (pojo.getAwsCategories().size() > 0) {
			for (String cat : pojo.getAwsCategories()) {
				moa.addCategory(cat);
			}
		}
		if (pojo.getTags().size() > 0) {
			for (AWSTagPojo tag : pojo.getTags()) {
				com.amazon.aws.moa.objects.resources.v1_0.Tag moaTag = moa.newTag();
				moaTag.setKey(tag.getKey());
				moaTag.setValue(tag.getValue());

				moa.addTag(moaTag);
			}
		}
		
        this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}
	
	@SuppressWarnings("unchecked")
	private void populateAWSServicePojo(com.amazon.aws.moa.jmsobjects.services.v1_0.Service moa, AWSServicePojo pojo) throws XmlEnterpriseObjectException {
		pojo.setServiceId(moa.getServiceId());
		pojo.setAwsServiceCode(moa.getAwsServiceCode());
		pojo.setAwsServiceName(moa.getAwsServiceName());
		pojo.setCombinedServiceName(moa.getCombinedServiceName());
		pojo.setAlternateServiceName(moa.getAlternateServiceName());
		pojo.setAwsStatus(moa.getAwsStatus());
		pojo.setSiteStatus(moa.getSiteStatus());
		pojo.setAwsLandingPageUrl(moa.getAwsServiceLandingPageUrl());
		pojo.setSiteLandingPageUrl(moa.getSiteServiceLandingPageUrl());
		pojo.setDescription(moa.getDescription());
		pojo.setAwsHipaaEligible(moa.getAwsHipaaEligible());
		pojo.setSiteHipaaEligible(moa.getSiteHipaaEligible());
		if (moa.getConsoleCategoryLength() > 0) {
			for (String consoleCat : (List<String>)moa.getConsoleCategory()) {
				pojo.getConsoleCategories().add(consoleCat);
			}
		}
		if (moa.getCategoryLength() > 0) {
			for (String cat : (List<String>)moa.getCategory()) {
				pojo.getAwsCategories().add(cat);
			}
		}
		if (moa.getTagLength() > 0) {
			for (com.amazon.aws.moa.objects.resources.v1_0.Tag tag : 
				(List<com.amazon.aws.moa.objects.resources.v1_0.Tag>)moa.getTag()) {
				
				AWSTagPojo ptag = new AWSTagPojo();
				ptag.setKey(tag.getKey());
				ptag.setValue(tag.getValue());
				pojo.getTags().add(ptag);
			}
		}
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AWSServiceQueryResultPojo getServicesForFilter(AWSServiceQueryFilterPojo filter) throws RpcException {
		AWSServiceQueryResultPojo result = new AWSServiceQueryResultPojo();
		result.setFilterUsed(filter);
		List<AWSServicePojo> pojos = new java.util.ArrayList<AWSServicePojo>();

		try {
			com.amazon.aws.moa.jmsobjects.services.v1_0.Service actionable = 
					(com.amazon.aws.moa.jmsobjects.services.v1_0.Service) getObject(Constants.MOA_SERVICE);
			ServiceQuerySpecification queryObject = (ServiceQuerySpecification) getObject(Constants.MOA_SERVICE_QUERY_SPEC);
			
			if (filter != null) {
				queryObject.setServiceId(filter.getServiceId());
				queryObject.setAwsServiceCode(filter.getAwsServiceCode());
				queryObject.setAwsServiceName(filter.getAwsServiceName());
				queryObject.setAwsStatus(filter.getAwsStatus());
				queryObject.setSiteStatus(filter.getSiteStatus());
				queryObject.setAwsHipaaEligible(filter.getAwsHipaaEligible());
				queryObject.setSiteHipaaEligible(filter.getSiteHipaaEligible());
				for (String consoleCat : filter.getConsoleCategories()) {
					info("[getServicesForFilter] adding " + consoleCat + " to the query object.");
					queryObject.addConsoleCategory(consoleCat);
				}
				for (String cat : filter.getCategories()) {
					queryObject.addCategory(cat);
				}
			}

			info("[getServicesForFilter] query object is: " + queryObject.toXmlString());
			List<com.amazon.aws.moa.jmsobjects.services.v1_0.Service> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getServicessForFilter] got " + moas.size() + " services from ESB service");

			for (com.amazon.aws.moa.jmsobjects.services.v1_0.Service service : moas) {
				AWSServicePojo pojo = new AWSServicePojo();
				AWSServicePojo baseline = new AWSServicePojo();
				this.populateAWSServicePojo(service, pojo);
				this.populateAWSServicePojo(service, baseline);
				pojo.setBaseline(baseline);

				pojos.add(pojo);
			}
			
			Collections.sort(pojos);
			result.setResults(pojos);
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}

		info("returning " + result.getResults().size() + " services.");
		return result;
	}

	@Override
	public AWSServicePojo createService(AWSServicePojo service) throws RpcException {
		service.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());
        try {
            info("creating AWS Service on the server...");
            com.amazon.aws.moa.jmsobjects.services.v1_0.Service newData = 
            	(com.amazon.aws.moa.jmsobjects.services.v1_0.Service) getObject(Constants.MOA_SERVICE);

            info("populating newData...");
            populateAWSServiceMoa(service, newData);

            info("doing the update...");
            doCreate(newData, getAWSRequestService());
            info("create is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return service;

		// TEMPORARY
//		service.setCreateInfo(this.getCachedUser().getPublicId(),
//				new java.util.Date());
//		service.setServiceId(UUID.uuid());
//		serviceList.add(service);
//		return service;
	}

	@Override
	public AWSServicePojo updateService(AWSServicePojo service) throws RpcException {
		service.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating UserNotification on the server...");
            com.amazon.aws.moa.jmsobjects.services.v1_0.Service newData = 
            	(com.amazon.aws.moa.jmsobjects.services.v1_0.Service) getObject(Constants.MOA_SERVICE);
            com.amazon.aws.moa.jmsobjects.services.v1_0.Service baselineData = 
            	(com.amazon.aws.moa.jmsobjects.services.v1_0.Service) getObject(Constants.MOA_SERVICE);

            info("populating newData...");
            populateAWSServiceMoa(service, newData);

            info("populating baselineData...");
            populateAWSServiceMoa(service.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getAWSRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return service;
	}

	@Override
	public void deleteService(AWSServicePojo service) throws RpcException {
		try {
			info("deleting AWS Service on the server...");
            com.amazon.aws.moa.jmsobjects.services.v1_0.Service moa = 
                	(com.amazon.aws.moa.jmsobjects.services.v1_0.Service) getObject(Constants.MOA_SERVICE);
			info("populating moa");
			this.populateAWSServiceMoa(service, moa);
			
			info("doing the Service.delete...");
			this.doDelete(moa, getAWSRequestService());
			info("Service.delete is complete...");

			return;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseObjectDeleteException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public List<String> getAwsServiceStatusItems() {
		List<String> statusItems = new java.util.ArrayList<String>();
		try {
			Properties props = 	getAppConfig().getProperties(AWS_SERVICE_STATUS_PROPERTIES);
			Iterator<Object> keys = props.keySet().iterator();
			while (keys.hasNext()) {
				Object key = keys.next();
				String value = props.getProperty((String)key);
				statusItems.add(value);
			}
			
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}

//		statusItems.add("active");
//		statusItems.add("deprecated");
		return statusItems;
	}

	@Override
	public List<String> getSiteServiceStatusItems() {
		List<String> statusItems = new java.util.ArrayList<String>();
		
		try {
			Properties props = 	getAppConfig().getProperties(SITE_SERVICE_STATUS_PROPERTIES);
			Iterator<Object> keys = props.keySet().iterator();
			while (keys.hasNext()) {
				Object key = keys.next();
				String value = props.getProperty((String)key);
				statusItems.add(value);
			}
			
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}

//		statusItems.add("Available");
//		statusItems.add("Available with Countermeasures");
//		statusItems.add("Blocked");
//		statusItems.add("Blocked Pending Review");
//		statusItems.add("Fully Available");
		return statusItems;
	}

	@Override
	public UserNotificationQueryResultPojo getUserNotificationsForFilter(UserNotificationQueryFilterPojo filter)
			throws RpcException {

		UserNotificationQueryResultPojo result = new UserNotificationQueryResultPojo();
		List<UserNotificationPojo> pojos = new java.util.ArrayList<UserNotificationPojo>();
		try {
			UserNotificationQuerySpecification queryObject = (UserNotificationQuerySpecification) getObject(Constants.MOA_USER_NOTIFICATION_QUERY_SPEC);
			UserNotification actionable = (UserNotification) getObject(Constants.MOA_USER_NOTIFICATION);

			boolean increaseReqSvcTimeout = true;
			if (filter != null) {
				if (filter.isUseQueryLanguage()) {
					increaseReqSvcTimeout = false;
					QueryLanguage ql = queryObject.newQueryLanguage();
					if (filter.getReadStr() != null) {
						if (filter.isRead() == false) {
							if (filter.getMaxRows() > 5) {
								ql.setName("unreadMaxByUserId");
							}
							else {
								ql.setName("unreadByUserId");
							}
						}
						else {
							ql.setName("byUserId");
						}
					}
					else {
						ql.setName("byUserId");
					}
					ql.setType("hql");
					ql.setMax(this.toStringFromInt(filter.getMaxRows()));

					Parameter userId = ql.newParameter();
					userId.setValue(filter.getUserId());
					userId.setName("UserId");
					ql.addParameter(userId);
					queryObject.setQueryLanguage(ql);
				}
				else {
					increaseReqSvcTimeout = true;
					queryObject.setUserId(filter.getUserId());
					queryObject.setPriority(filter.getPriority());
					queryObject.setType(filter.getType());
					queryObject.setUserNotificationId(filter.getUserNotificationId());
					queryObject.setAccountId(filter.getAccountId());
					queryObject.setAccountNotificationId(filter.getAccountNotificationId());
					if (filter.getReadStr() != null) {
						queryObject.setRead(this.toStringFromBoolean(filter.isRead()));
					}
				}
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getUserNotificationsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			RequestService reqSvc = this.getAWSRequestService();
			// if they're asking for ALL notifications, we'll have to bump 
			// the timeout interval WAY up
			if (increaseReqSvcTimeout) {
				info("[getUserNotificationsForFilter] potentially big UserNotification.Query, "
						+ "increasing RequestService timeout interval."); 
				((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval() * 10);
			}
			
			@SuppressWarnings("unchecked")
			List<UserNotification> moas = actionable.query(queryObject,
					reqSvc);
			info("[getUserNotificationsForFilter] got " + moas.size() + 
					" UserNotifications from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (UserNotification moa : moas) {
				UserNotificationPojo pojo = new UserNotificationPojo();
				UserNotificationPojo baseline = new UserNotificationPojo();

				if (filter.getSearchString() != null && filter.getSearchString().length() > 0) {
					// only add notifications that contain the search string
					if (moa.getSubject().toLowerCase().indexOf(filter.getSearchString().toLowerCase()) >= 0) {
						this.populateUserNotificationPojo(moa, pojo);
						this.populateUserNotificationPojo(moa, baseline);
						pojo.setBaseline(baseline);
						pojos.add(pojo);
					}
					else if (moa.getText().toLowerCase().indexOf(filter.getSearchString().toLowerCase()) >= 0) {
						this.populateUserNotificationPojo(moa, pojo);
						this.populateUserNotificationPojo(moa, baseline);
						pojo.setBaseline(baseline);
						pojos.add(pojo);
					}
				}
				else {
					// add them all
					this.populateUserNotificationPojo(moa, pojo);
					this.populateUserNotificationPojo(moa, baseline);
					pojo.setBaseline(baseline);
					pojos.add(pojo);
				}
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		
//		if (notificationList.isEmpty()) {
//			UserNotificationPojo n1 = new UserNotificationPojo();
//			n1.setUserNotificationId("1");
//			n1.setText("Simple notification.");
//			n1.setCreateTime(new java.util.Date());
//			notificationList.add(n1);
//			UserNotificationPojo n2 = new UserNotificationPojo();
//			n2.setUserNotificationId("2");
//			n2.setText("Simple notification.");
//			n2.setCreateTime(new java.util.Date());
//			notificationList.add(n2);
//			UserNotificationPojo n3 = new UserNotificationPojo();
//			n3.setUserNotificationId("3");
//			n3.setText("Simple notification.");
//			notificationList.add(n3);
//			UserNotificationPojo n4 = new UserNotificationPojo();
//			n4.setUserNotificationId("4");
//			n4.setText("Simple notification.");
//			n4.setCreateTime(new java.util.Date());
//			notificationList.add(n4);
//			UserNotificationPojo n5 = new UserNotificationPojo();
//			n5.setUserNotificationId("5");
//			n5.setText("Simple notification.");
//			n5.setCreateTime(new java.util.Date());
//			notificationList.add(n5);
//			UserNotificationPojo n6 = new UserNotificationPojo();
//			n6.setUserNotificationId("6");
//			n6.setText("Simple notification.");
//			n6.setCreateTime(new java.util.Date());
//			notificationList.add(n6);
//			UserNotificationPojo n7 = new UserNotificationPojo();
//			n7.setUserNotificationId("7");
//			n7.setText("Simple notification.");
//			notificationList.add(n7);
//			UserNotificationPojo n8 = new UserNotificationPojo();
//			n8.setUserNotificationId("8");
//			n8.setText("Simple notification.");
//			n8.setCreateTime(new java.util.Date());
//			notificationList.add(n8);
//			UserNotificationPojo n9 = new UserNotificationPojo();
//			n9.setUserNotificationId("9");
//			n9.setText("Simple notification.");
//			n9.setCreateTime(new java.util.Date());
//			notificationList.add(n9);
//			UserNotificationPojo n10 = new UserNotificationPojo();
//			n10.setUserNotificationId("10");
//			n10.setText("Simple notification.");
//			notificationList.add(n10);
//		}
		
//		result.setResults(notificationList);
//		return result;
	}

	private void populateUserNotificationPojo(UserNotification moa, UserNotificationPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setUserNotificationId(moa.getUserNotificationId());
		pojo.setReferenceId(moa.getReferenceId());
		pojo.setAccountNotificationId(moa.getAccountNotificationId());
		pojo.setUserId(moa.getUserId());
		pojo.setType(moa.getType());
		pojo.setPriority(moa.getPriority());
		pojo.setSubject(moa.getSubject());
		pojo.setText(moa.getText());
		pojo.setRead(this.toBooleanFromString(moa.getRead()));
		pojo.setSentToEmailAddress(moa.getSentToEmailAddress());
		if (moa.getReadDatetime() != null) {
			pojo.setReadDateTime(this.toDateFromDatetime(moa.getReadDatetime()));
		}
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateUserNotificationMoa(UserNotificationPojo pojo, UserNotification moa) throws EnterpriseFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		moa.setUserNotificationId(pojo.getUserNotificationId());
		moa.setReferenceId(pojo.getReferenceId());
		moa.setAccountNotificationId(pojo.getAccountNotificationId());
		moa.setUserId(pojo.getUserId());
		moa.setType(pojo.getType());
		moa.setPriority(pojo.getPriority());
		moa.setSubject(pojo.getSubject());
		moa.setText(pojo.getText());
		moa.setRead(this.toStringFromBoolean(pojo.isRead()));
		moa.setSentToEmailAddress(pojo.getSentToEmailAddress());
		if (pojo.getReadDateTime() != null) {
			Datetime dt = new Datetime("ReadDatetime");
			populateDatetime(dt, pojo.getReadDateTime());
			moa.setReadDatetime(dt);
		}
		
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@Override
	public UserNotificationPojo createUserNotification(UserNotificationPojo notification) throws RpcException {
		notification.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());
        try {
            info("updating UserNotification on the server...");
            UserNotification newData = (UserNotification) getObject(Constants.MOA_USER_NOTIFICATION);

            info("populating newData...");
            populateUserNotificationMoa(notification, newData);

            info("doing the create...");
            doCreate(newData, getAWSRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return notification;
	}

	@Override
	public UserNotificationPojo updateUserNotification(UserNotificationPojo notification) throws RpcException {
		notification.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating UserNotification on the server...");
            UserNotification newData = (UserNotification) getObject(Constants.MOA_USER_NOTIFICATION);
            UserNotification baselineData = (UserNotification) getObject(Constants.MOA_USER_NOTIFICATION);

            info("populating newData...");
            populateUserNotificationMoa(notification, newData);

            info("populating baselineData...");
            populateUserNotificationMoa(notification.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getAWSRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return notification;
	}

	@Override
	public void deleteUserNotification(UserNotificationPojo notification) throws RpcException {
		
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting UserNotification record on the server...");
				UserNotification moa = (UserNotification) getObject(Constants.MOA_USER_NOTIFICATION);
				info("populating moa");
				this.populateUserNotificationMoa(notification, moa);

				
				info("doing the UserNotification.delete...");
				this.doDelete(moa, getAWSRequestService());
				info("UserNotification.delete is complete...");

//				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public SpeedChartPojo getSpeedChartForFinancialAccountNumber(String accountNumber) throws RpcException {
		SpeedChartCachedItem cachedScp = SpeedChartCache.getCache().get(accountNumber);
		if (cachedScp == null) {
			cachedScp = new SpeedChartCachedItem();
		}
		if (SpeedChartCache.isExpired(cachedScp)) {
			info("refreshing cached item from ESB service");
			SpeedChartQueryFilterPojo filter = new SpeedChartQueryFilterPojo();
			List<String> keys = new java.util.ArrayList<String>();
			keys.add(accountNumber);
			filter.setSpeedChartKeys(keys);
			SpeedChartQueryResultPojo result = this.getSpeedChartsForFilter(filter);
			if (result.getResults().size() > 0) {
				SpeedChartPojo scp = result.getResults().get(0);
				cachedScp.setLastUpdated(new Date());
				cachedScp.setSpeedChartKey(accountNumber);
				cachedScp.setSpeedChart(scp);
				SpeedChartCache.getCache().put(accountNumber, cachedScp);
			}
			else {
				return null;
			}
		}
		else {
			info("returning SpeedChart from cache");
		}
		return cachedScp.getSpeedChart();
	}

	@Override
	public SpeedChartQueryResultPojo getSpeedChartsForFilter(SpeedChartQueryFilterPojo filter) throws RpcException {
		SpeedChartQueryResultPojo result = new SpeedChartQueryResultPojo();
		List<SpeedChartPojo> pojos = new java.util.ArrayList<SpeedChartPojo>();
		try {
			SPEEDCHART_QUERY queryObject = (SPEEDCHART_QUERY) getObject(Constants.MOA_SPEEDCHART_QUERY_SPEC);
			SPEEDCHART actionable = (SPEEDCHART) getObject(Constants.MOA_SPEEDCHART);

			if (filter != null) {
				for (String key : filter.getSpeedChartKeys()) {
					queryObject.addSPEEDCHART_KEY(key);
				}
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getSpeedChartsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			@SuppressWarnings("unchecked")
			List<SPEEDCHART> moas = actionable.query(queryObject,
					this.getPeopleSoftRequestService());
			info("got " + moas.size() + " speed chart objects back from ESB");
			for (SPEEDCHART moa : moas) {
				info("SPEEDCHART: " + moa.toXmlString());
				SpeedChartPojo pojo = new SpeedChartPojo();
//				SpeedChartPojo baseline = new SpeedChartPojo();
				this.populateSpeedChartPojo(moa, pojo);
//				this.populateSpeedChartPojo(moa, baseline);
//				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}
	
	@SuppressWarnings("unused")
	private void populateSpeedChartMoa(SpeedChartPojo pojo,
			SPEEDCHART moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		if (pojo.getSpeedChartKey() != null) {
			moa.setSPEEDCHART_KEY(pojo.getSpeedChartKey());
		}
		else {
		}
		moa.setDESCR(pojo.getDescription());
		moa.setVALID_CODE(pojo.getValidCode());

//		this.setMoaCreateInfo(moa, pojo);
//		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateSpeedChartPojo(SPEEDCHART moa,
			SpeedChartPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		/*
		<!ELEMENT SPEEDCHART (SPEEDCHART_KEY, DESCR?, VALID_CODE?, EU_VALIDITY_DESCR?, BUSINESS_UNIT_GL?, 
		BU_DESCR?, OPERATING_UNIT?, OPER_UNIT_DESCR?, FUND_CODE?, DEPTID?, DEPT_DESCR?, BUSINESS_UNIT_PC?, 
		PROJECT_ID?, PROJECT_ID_DESCR?, EFF_STATUS?, EU_PROJ_END_DT?, EU_PC_ACT_STAT_END?, 
		EU_PC_ACT_STAT_CLS?, EU_PC_ACT_STAT_FIN?)>
		 */
		pojo.setSpeedChartKey(moa.getSPEEDCHART_KEY());
		pojo.setDescription(moa.getDESCR());
		if (moa.getVALID_CODE() != null) {
			info("VALID_CODE: " + moa.getVALID_CODE());
			pojo.setValidCode(moa.getVALID_CODE());
		}
		pojo.setEuValidityDescription(moa.getEU_VALIDITY_DESCR());
		pojo.setBusinessUnitGL(moa.getBUSINESS_UNIT_GL());
		pojo.setBusinessDescription(moa.getBU_DESCR());
		pojo.setOperatingUnit(moa.getOPERATING_UNIT());
		pojo.setOperatingUnitDescription(moa.getOPER_UNIT_DESCR());
		pojo.setFundCode(moa.getFUND_CODE());
		pojo.setDepartmentId(moa.getDEPTID());
		pojo.setDepartmentDescription(moa.getDEPT_DESCR());
		pojo.setBusinessUnitPC(moa.getBUSINESS_UNIT_PC());
		pojo.setProjectId(moa.getPROJECT_ID());
		pojo.setProjectIdDescription(moa.getPROJECT_ID_DESCR());
		pojo.setEffectiveStatus(moa.getEFF_STATUS());
		if (moa.getEU_PROJ_END_DT() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			pojo.setEuProjectEndDate(df.parse(moa.getEU_PROJ_END_DT()));
		}
		pojo.setEuPcActStatEnd(moa.getEU_PC_ACT_STAT_END());
		pojo.setEuPcActStatClose(moa.getEU_PC_ACT_STAT_CLS());
		pojo.setEuPcActStatFin(moa.getEU_PC_ACT_STAT_FIN());
		
//		this.setPojoCreateInfo(pojo, moa);
//		this.setPojoUpdateInfo(pojo, moa);
	}

	@SuppressWarnings("unused")
	private void populateFirewallRuleMoa(FirewallRulePojo pojo,
			FirewallRule moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		moa.setName(pojo.getName());
		moa.setVsys(pojo.getVsys());
		moa.setAction(pojo.getAction());
		moa.setDescription(pojo.getDescription());
		moa.setLogSetting(pojo.getLogSetting());
		if (pojo.getProfileSettings().size() > 0) {
			ProfileSetting ps = moa.newProfileSetting();
			Group container = ps.newGroup();
			for (String member : pojo.getProfileSettings()) {
				container.addMember(member);
			}
		}
		if (pojo.getTos().size() > 0) {
			To container = moa.newTo();
			for (String member : pojo.getTos()) {
				container.addMember(member);
			}
		}
		if (pojo.getFroms().size() > 0) {
			From container = moa.newFrom();
			for (String member : pojo.getFroms()) {
				container.addMember(member);
			}
		}
		if (pojo.getSources().size() > 0) {
			Source container = moa.newSource();
			for (String member : pojo.getSources()) {
				container.addMember(member);
			}
		}
		if (pojo.getSourceUsers().size() > 0) {
			SourceUser container = moa.newSourceUser();
			for (String member : pojo.getSourceUsers()) {
				container.addMember(member);
			}
		}
		if (pojo.getCategories().size() > 0) {
			Category container = moa.newCategory();
			for (String member : pojo.getCategories()) {
				container.addMember(member);
			}
		}
		if (pojo.getApplications().size() > 0) {
			Application container = moa.newApplication();
			for (String member : pojo.getApplications()) {
				container.addMember(member);
			}
		}
		if (pojo.getServices().size() > 0) {
			Service container = moa.newService();
			for (String member : pojo.getServices()) {
				container.addMember(member);
			}
		}
		if (pojo.getHipProfiles().size() > 0) {
			HipProfiles container = moa.newHipProfiles();
			for (String member : pojo.getHipProfiles()) {
				container.addMember(member);
			}
		}
		if (pojo.getTags().size() > 0) {
			Tag container = moa.newTag();
			for (String member : pojo.getTags()) {
				container.addMember(member);
			}
		}
		
//		this.setMoaCreateInfo(moa, pojo);
//		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings("unchecked")
	private void populateFirewallRulePojo(FirewallRule moa,
			FirewallRulePojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
	
		pojo.setName(moa.getName());
		pojo.setVsys(moa.getVsys());
		pojo.setAction(moa.getAction());
		pojo.setDescription(moa.getDescription());
		pojo.setLogSetting(moa.getLogSetting());
		if (moa.getProfileSetting() != null) {
			if (moa.getProfileSetting().getGroup() != null) {
				for (String member : (List<String>)moa.getProfileSetting().getGroup().getMember()) {
					pojo.getProfileSettings().add(member);
				}
			}
		}
		if (moa.getTo() != null) {
			for (String member : (List<String>)moa.getTo().getMember()) {
				pojo.getTos().add(member);
			}
		}
		if (moa.getFrom() != null) {
			for (String member : (List<String>)moa.getFrom().getMember()) {
				pojo.getFroms().add(member);
			}
		}
		if (moa.getSource() != null) {
			for (String member : (List<String>)moa.getSource().getMember()) {
				pojo.getSources().add(member);
			}
		}
		if (moa.getDestination() != null) {
			for (String member : (List<String>)moa.getDestination().getMember()) {
				pojo.getDestinations().add(member);
			}
		}
		if (moa.getSourceUser() != null) {
			for (String member : (List<String>)moa.getSourceUser().getMember()) {
				pojo.getSourceUsers().add(member);
			}
		}
		if (moa.getCategory() != null) {
			for (String member : (List<String>)moa.getCategory().getMember()) {
				pojo.getCategories().add(member);
			}
		}
		if (moa.getApplication() != null) {
			for (String member : (List<String>)moa.getApplication().getMember()) {
				pojo.getApplications().add(member);
			}
		}
		if (moa.getService() != null) {
			for (String member : (List<String>)moa.getService().getMember()) {
				pojo.getServices().add(member);
			}
		}
		if (moa.getHipProfiles() != null) {
			for (String member : (List<String>)moa.getHipProfiles().getMember()) {
				pojo.getHipProfiles().add(member);
			}
		}
		if (moa.getTag() != null) {
			for (String member : (List<String>)moa.getTag().getMember()) {
				pojo.getTags().add(member);
			}
		}
	}
	
	@Override
	public FirewallRuleQueryResultPojo getFirewallRulesForFilter(FirewallRuleQueryFilterPojo filter)
			throws RpcException {

		FirewallRuleQueryResultPojo result = new FirewallRuleQueryResultPojo();
		List<FirewallRulePojo> pojos = new java.util.ArrayList<FirewallRulePojo>();
		try {
			FirewallRuleQuerySpecification queryObject = (FirewallRuleQuerySpecification) getObject(Constants.MOA_FIREWALL_RULE_QUERY_SPEC);
			FirewallRule actionable = (FirewallRule) getObject(Constants.MOA_FIREWALL_RULE);

			if (filter != null) {
				if (filter.getTags().size() > 0) {
					Tag queryTag = queryObject.newTag();
					for (String tag : filter.getTags()) {
						queryTag.addMember(tag);
					}
					queryObject.setTag(queryTag);
				}
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getAccountsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<FirewallRule> moas = actionable.query(queryObject,
					this.getFirewallRequestService());
			for (FirewallRule moa : moas) {
				info("FirewallRule returned from ESB: " + moa.toXmlString());
				FirewallRulePojo pojo = new FirewallRulePojo();
				this.populateFirewallRulePojo(moa, pojo);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public FirewallRulePojo createFirewallRule(FirewallRulePojo rule) throws RpcException {
		// TODO Auto-generated method stub
		
		// this will be a FirewallExceptionRequest request sent to service now
		return null;
	}

	@Override
	public FirewallRulePojo updateFirewallRule(FirewallRulePojo rule) throws RpcException {
		// TODO Auto-generated method stub
		
		// this will be a request sent to service now
		return null;
	}

	@Override
	public void deleteFirewallRule(FirewallRulePojo rule) throws RpcException {
		// TODO: this will be a request to service now (FirewallRuleExceptionRequest.Create)
		FirewallExceptionRemoveRequestRequisitionPojo pojo_req = new FirewallExceptionRemoveRequestRequisitionPojo();
		// TODO: populate the fer with stuff from the rule passed in.
		/*
			<!ELEMENT FirewallExceptionRequest (
				UserNetID, 
				ApplicationName, 
				SourceOutsideEmory, 
				TimeRule, 
				SourceIpAddresses, 
				DestinationIpAddresses, 
				Ports, 
				BusinessReason, 
				Patched, 
				DefaultPasswdChanged, 
				AppConsoleACLed, 
				Hardened, 
				PatchingPlan, 
				SensitiveDataDesc, 
				LocalFirewallRules, 
				DefaultDenyZone, 
				Tag+, 
		 */
		UserAccountPojo user = (UserAccountPojo) Cache.getCache().get(
				Constants.USER_ACCOUNT + getCurrentSessionId());

//		fer.setUserNetId(user.getPrincipal());
//		fer.setApplicationName("VPCP");
//		fer.setTimeRule("Indefinitely");
//		StringBuffer sources = new StringBuffer();
//		for (int i=0; i<rule.getSources().size(); i++) {
//			String source = rule.getSources().get(i);
//			sources.append(source);
//		}
//		for (String tag : rule.getTags()) {
//			fer.getTags().add(tag);
//		}
		
		try {
			// TODO: should be a FirewallExceptionRemoveRequestRequisition now I believe
			FirewallExceptionRemoveRequestPojo pojo = this.generateFirewallExceptionRemoveRequest(pojo_req);

			return;
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@Override
	public void logMessage(String message) throws RpcException {
		UserAccountPojo user = (UserAccountPojo) Cache.getCache().get(
				Constants.USER_ACCOUNT + getCurrentSessionId());
		if (user != null) {
			info("[FromClient] for user: " + user.toString() + ": " + message);
		}
		else {
			info("[FromClient-NO_CACHED_USER] " + message);
		}
	}

	@SuppressWarnings("unused")
	private void populateRoleMoa(RolePojo pojo,
			Role moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {


		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings("unused")
	private void populateRolePojo(Role moa,
			RolePojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateRoleAssignmentMoa(RoleAssignmentPojo pojo,
			RoleAssignment moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {
		
		/*
		<!ELEMENT RoleAssignment (RoleAssignmentActionType?, RoleAssignmentType?, 
			CorrelationId?, CauseIdentities?, EffectiveDatetime?, ExpirationDatetime?,
			IdentityDN?,OriginatorDN?,Reason?,RoleDNs?,SodJustification*,
			ExplicitIdentityDNs?, RoleDN?)>

		<RoleAssignment>
			<RoleAssignmentActionType>grant</RoleAssignmentActionType>
			<RoleAssignmentType>USER_TO_ROLE</RoleAssignmentType>
			<IdentityDN>cn=P4877359,ou=Users,ou=Data,o=EmoryDev</IdentityDN>
			<Reason>For Testing</Reason>
			<RoleDNs>
				<DistinguishedName>cn=RGR_AWS-158058157672-Administrator,cn=Level10,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=UserApplication,cn=DRIVERSET01,ou=Servers,o=EmoryDev</DistinguishedName>
			</RoleDNs>
		</RoleAssignment>
		 */

		moa.setRoleAssignmentActionType(pojo.getActionType());
		moa.setRoleAssignmentType(pojo.getType());
		moa.setIdentityDN(pojo.getIdentityDN());
		moa.setReason(pojo.getReason());
		if (pojo.getRoleDNs() != null) {
			RoleDNs roleDns = moa.newRoleDNs();
			for (String dn : pojo.getRoleDNs().getDistinguishedNames()) {
				roleDns.addDistinguishedName(dn);
			}
			moa.setRoleDNs(roleDns);
		}
		ExplicitIdentityDNs eid = moa.newExplicitIdentityDNs();
		for (String eid_dn : pojo.getExplicityIdentitiyDNs().getDistinguishedNames()) {
			eid.addDistinguishedName(eid_dn);
		}
		moa.setExplicitIdentityDNs(eid);
		moa.setRoleDN(pojo.getRoleDN());
		
		Datetime expirationDatetime = moa.newExpirationDatetime();
		this.populateDatetime(expirationDatetime, pojo.getExpirationDate());
		moa.setExpirationDatetime(expirationDatetime);
		
		Datetime effectiveDatetime = moa.newEffectiveDatetime();
		this.populateDatetime(effectiveDatetime, pojo.getEffectiveDate());
		moa.setEffectiveDatetime(effectiveDatetime);
		
//		this.setMoaCreateInfo(moa, pojo);
//		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings("unchecked")
	private void populateRoleAssignmentPojo(RoleAssignment moa,
			RoleAssignmentPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setActionType(moa.getRoleAssignmentActionType());
		pojo.setType(moa.getRoleAssignmentType());
		pojo.setIdentityDN(moa.getIdentityDN());
		pojo.setReason(moa.getReason());
		RoleDNsPojo rdns = new RoleDNsPojo();
		if (moa.getRoleDNs() != null) {
			for (String dn : (List<String>)moa.getRoleDNs().getDistinguishedName()) {
				rdns.getDistinguishedNames().add(dn);
			}
			pojo.setRoleDNs(rdns);
		}
		pojo.setRoleDN(moa.getRoleDN());
		
		if (moa.getExplicitIdentityDNs() != null) {
			pojo.setExplicityIdentitiyDNs(new ExplicitIdentityDNsPojo());
			for (String moa_dn : (List<String>)moa.getExplicitIdentityDNs().getDistinguishedName()) {
				pojo.getExplicityIdentitiyDNs().getDistinguishedNames().add(moa_dn);
			}
		}
		pojo.setExpirationDate(this.toDateFromDatetime(moa.getExpirationDatetime()));
		pojo.setEffectiveDate(this.toDateFromDatetime(moa.getEffectiveDatetime()));
	}

	@SuppressWarnings("unused")
	private void populateDirectoryPersonMoa(DirectoryPersonPojo pojo,
			DirectoryPerson moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {


		//DepartmentName?, Email?, Fax?, FirstMiddle?, FullName, Key, LastName?, DirectoryLocation?, 
		// MailStop?, DirectoryPhone?, SchoolDivision?, StudentPhone?, Suffix?, Title?, Type)>
		moa.setDepartmentName(pojo.getDepartmentName());
		if (pojo.getEmail() != null) {
			Email email = moa.newEmail();
			email.setType(pojo.getEmail().getType());
			email.setEmailAddress(pojo.getEmail().getEmailAddress());
			moa.setEmail(email);
		}
		moa.setFax(pojo.getFax());
		moa.setFirstMiddle(pojo.getFirstMiddle());
		moa.setFullName(pojo.getFullName());
		moa.setKey(pojo.getKey());
		moa.setLastName(pojo.getLastName());
		moa.setDirectoryLocation(pojo.getDirectoryLocation());
		moa.setMailStop(pojo.getMailStop());
		moa.setDirectoryPhone(pojo.getDirectoryPhone());
		moa.setSchoolDivision(pojo.getSchoolDivision());
		moa.setStudentPhone(pojo.getStudentPhone());
		moa.setSuffix(pojo.getSuffix());
		moa.setTitle(pojo.getTitle());
		moa.setType(pojo.getType());
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateDirectoryPersonPojo(DirectoryPerson moa,
			DirectoryPersonPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		//DepartmentName?, Email?, Fax?, FirstMiddle?, FullName, Key, LastName?, DirectoryLocation?, 
		// MailStop?, DirectoryPhone?, SchoolDivision?, StudentPhone?, Suffix?, Title?, Type)>
		pojo.setDepartmentName(moa.getDepartmentName());
		if (moa.getEmail() != null) {
			EmailPojo emailPojo = new EmailPojo();
			emailPojo.setType(moa.getEmail().getType());
			emailPojo.setEmailAddress(moa.getEmail().getEmailAddress());
			pojo.setEmail(emailPojo);
		}
		pojo.setFax(moa.getFax());
		pojo.setFirstMiddle(moa.getFirstMiddle());
		pojo.setFullName(moa.getFullName());
		// Key is their publicId
		pojo.setKey(moa.getKey());
		pojo.setLastName(moa.getLastName());
		pojo.setDirectoryLocation(moa.getDirectoryLocation());
		pojo.setMailStop(moa.getMailStop());
		pojo.setDirectoryPhone(moa.getDirectoryPhone());
		pojo.setSchoolDivision(moa.getSchoolDivision());
		pojo.setStudentPhone(moa.getStudentPhone());
		pojo.setSuffix(moa.getSuffix());
		pojo.setTitle(moa.getTitle());
		pojo.setType(moa.getType());
//		this.setPojoCreateInfo(pojo, moa);
//		this.setPojoUpdateInfo(pojo, moa);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DirectoryPersonQueryResultPojo getDirectoryPersonsForFilter(DirectoryPersonQueryFilterPojo filter)
			throws RpcException {

		DirectoryPersonQueryResultPojo result = new DirectoryPersonQueryResultPojo();
		List<DirectoryPersonPojo> pojos = new java.util.ArrayList<DirectoryPersonPojo>();
		try {
			DirectoryPersonQuerySpecification queryObject = (DirectoryPersonQuerySpecification) getObject(Constants.MOA_DIRECTORY_PERSON_QUERY_SPEC);
			DirectoryPerson actionable = (DirectoryPerson) getObject(Constants.MOA_DIRECTORY_PERSON);

			if (filter != null) {
				queryObject.setKey(filter.getKey());
				queryObject.setSearchString(filter.getSearchString());
				queryObject.setTestRequest(filter.getTestRequest());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getDirectoryPersonsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			List<DirectoryPerson> moas = null;
			try {
				info("Query spec is: " + queryObject.toXmlString());
				moas = actionable.query(queryObject,
						this.getDirectoryRequestService());
			} catch (EnterpriseObjectQueryException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
			if (moas != null) {
				info("[service layer] got " + moas.size() + " DirectoryPerson objects back from ESB.");
				for (DirectoryPerson moa : moas) {
					DirectoryPersonPojo pojo = new DirectoryPersonPojo();
	//				DirectoryPersonPojo baseline = new DirectoryPersonPojo();
					this.populateDirectoryPersonPojo(moa, pojo);
	//				this.populateDirectoryPersonPojo(moa, baseline);
	//				pojo.setBaseline(baseline);
					pojos.add(pojo);
				}
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public FullPersonQueryResultPojo getFullPersonsForFilter(FullPersonQueryFilterPojo filter) throws RpcException {
		FullPersonQueryResultPojo result = new FullPersonQueryResultPojo();
		List<FullPersonPojo> pojos = new java.util.ArrayList<FullPersonPojo>();
		try {
			FullPersonQuerySpecification queryObject = (FullPersonQuerySpecification) getObject(Constants.MOA_FULL_PERSON_QUERY_SPEC);
			FullPerson actionable = (FullPerson) getObject(Constants.MOA_FULL_PERSON);
			String authUserId = null;
			
			if (filter != null) {
				queryObject.setPublicId(filter.getPublicId());
				queryObject.setNetId(filter.getNetId());
				queryObject.setEmplId(filter.getEmplId());
				queryObject.setPrsni(filter.getPrsni());
				queryObject.setCode(filter.getCode());
				if (filter.getUserLoggedIn() != null) {
					authUserId = this.getAuthUserIdForHALS(filter.getUserLoggedIn());
				}
			}

			if (authUserId == null) {
				authUserId = this.getAuthUserIdForHALS();
			}
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getFullPersonsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<FullPerson> moas = actionable.query(queryObject,
					this.getIdentityServiceRequestService());
			if (moas != null) {
				info("[service layer] got " + moas.size() + " FullPerson objects back from ESB.");
			}
			for (FullPerson moa : moas) {
				FullPersonPojo pojo = new FullPersonPojo();
				this.populateFullPersonPojo(moa, pojo);
				pojos.add(pojo);
			}

			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void populateFullPersonPojo(FullPerson moa,
			FullPersonPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		/*
	String publicId;
	PersonPojo person;
	List<NetworkIdentityPojo> networkIdentities = new ArrayList<NetworkIdentityPojo>();
	EmployeePojo employee;
	StudentPojo student;
	List<SponsoredPersonPojo> sponsoredPersons = new ArrayList<SponsoredPersonPojo>();
		 */
		pojo.setPublicId(moa.getPublicId());
		
		if (moa.getPerson() != null) {
			PersonPojo personPojo = new PersonPojo();
			this.populatePersonPojo(moa.getPerson(), personPojo);
			pojo.setPerson(personPojo);
		}

		if (moa.getEmployee() != null) {
			EmployeePojo employeePojo = new EmployeePojo();
			this.populateEmployeePojo(moa.getEmployee(), employeePojo);
			pojo.setEmployee(employeePojo);
		}

		if (moa.getStudent() != null) {
			StudentPojo studentPojo = new StudentPojo();
			this.populateStudentPojo(moa.getStudent(), studentPojo);
			pojo.setStudent(studentPojo);
		}
		
		if (moa.getNetworkIdentity() != null & moa.getNetworkIdentityLength() > 0) {
			for (NetworkIdentity niMoa : (List<NetworkIdentity>) moa.getNetworkIdentity()) {
				NetworkIdentityPojo niPojo = new NetworkIdentityPojo();
				this.populateNetworkIdentityPojo(niMoa, niPojo);
				pojo.getNetworkIdentities().add(niPojo);
			}
		}
		
		if (moa.getSponsoredPerson() != null & moa.getSponsoredPersonLength() > 0) {
			for (SponsoredPerson spMoa : (List<SponsoredPerson>) moa.getSponsoredPerson()) {
				SponsoredPersonPojo spPojo = new SponsoredPersonPojo();
				this.populateSponsoredPersonPojo(spMoa, spPojo);
				pojo.getSponsoredPersons().add(spPojo);
			}
		}
	}
	private void populatePersonPojo(Person moa,
			PersonPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {

		//PublicId, PersonalName, LocalizedNameList?, SSN?, BirthDay?, BirthMonth?, BirthYear?, 
		// DriversLicense?, NationalIdCard*, Gender?, Association*, Eligibility*, Tag*, 
		// FallbackContact*,Phone*,Email*, Prsni, Location?, DirectoryTitle?, EmailRestricted?, 
		// PhoneRestricted?, LocationRestricted?)>

		pojo.setPublicId(moa.getPublicId());
		if (moa.getPersonalName() != null) {
			/*
			<!ELEMENT PersonalName (LastName, FirstName, MiddleName?, NameSuffix?, Prefix?, Suffix?, CompositeName?)>
			<!ATTLIST PersonalName
				type CDATA #REQUIRED
				order (east | west) #IMPLIED
				noFirstName (true | false) #IMPLIED
			>
			*/
			PersonalNamePojo pn = new PersonalNamePojo();
			pn.setLastName(moa.getPersonalName().getLastName());
			pn.setFirstName(moa.getPersonalName().getFirstName());
			pn.setCompositeName(moa.getPersonalName().getCompositeName());
			pojo.setPersonalName(pn);
		}
	}
	private void populateNetworkIdentityPojo(NetworkIdentity moa,
			NetworkIdentityPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		/*
		<!ELEMENT NetworkIdentity (Value, PublicId?, CreateDate, EndDate?, Domain?, Tag*, InitialPassword?)>
		<!ATTLIST NetworkIdentity
			type CDATA #REQUIRED
			status (reserved | active | inactive) #REQUIRED
			ancillary (true | false) "false"
		>
		 */
		pojo.setValue(moa.getValue());
		pojo.setPublicId(moa.getPublicId());
		pojo.setDomain(moa.getDomain());
		pojo.setType(moa.getType());
		pojo.setStatus(moa.getStatus());
		pojo.setAncillary(this.toBooleanFromString(moa.getAncillary()));
	}
	private void populateEmployeePojo(Employee moa,
			EmployeePojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
	}
	private void populateStudentPojo(Student moa,
			StudentPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
	}
	private void populateSponsoredPersonPojo(SponsoredPerson moa,
			SponsoredPersonPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
	}

	public Properties getRoleAssignmentProps() {
		return roleAssignmentProps;
	}

	public void setRoleAssignmentProps(Properties roleAssignmentProps) {
		this.roleAssignmentProps = roleAssignmentProps;
	}

	@Override
	public RoleAssignmentPojo createRoleAssignmentForPersonInAccount(String publicId, 
			String accountId, String roleName) throws RpcException {

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				roleAssignmentProps = getAppConfig().getProperties(ROLE_ASSIGNMENT_PROPERTIES);
				RoleAssignmentRequisition requisition = (RoleAssignmentRequisition) getObject(Constants.MOA_ROLE_ASSIGNMENT_REQUISITION);
				requisition.setRoleAssignmentActionType("grant");
				requisition.setRoleAssignmentType("USER_TO_ROLE");
				requisition.setReason("Added using VPCP by (" + getUserLoggedIn(false).getEppn() +")");
				
				String idDn = roleAssignmentProps.getProperty("IdentityDN", "cn=PUBLIC_ID,ou=Users,ou=Data,o=EmoryDev");
				idDn = idDn.replaceAll(Constants.REPLACEMENT_VAR_PUBLIC_ID, publicId);
				requisition.setIdentityDN(idDn);
				
				String distName = roleAssignmentProps.getProperty("RoleDNDistinguishedName", "cn=RGR_AWS-AWS_ACCOUNT_NUMBER-EMORY_ROLE_NAME,cn=Level10,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=UserApplication,cn=DRIVERSET01,ou=Servers,o=EmoryDev");
				distName = distName.replaceAll(Constants.REPLACEMENT_VAR_AWS_ACCOUNT_NUMBER, accountId);
				distName = distName.replaceAll(Constants.REPLACEMENT_VAR_EMORY_ROLE_NAME, roleName);
				RoleDNs roleDns = requisition.newRoleDNs();
				roleDns.addDistinguishedName(distName);
				requisition.setRoleDNs(roleDns);

				RoleAssignment moa = (RoleAssignment) getObject(Constants.MOA_ROLE_ASSIGNMENT);

				info("generating ROLE Assignment record on the server:  " + requisition.toXmlString());
				List<ActionableEnterpriseObject> results = this.doGenerate(moa, requisition, getIDMRequestService());
				if (results.size() != 1) {
					// error
					String msg = "Incorrect number of RoleAssignments generated.  Expected 1, got " + results.size();
					throw new RpcException(msg);
				}
				else {
					RoleAssignment generated = (RoleAssignment)results.get(0);
					info("RoleAssignment returned from the generate: " + generated.toXmlString());
					RoleAssignmentPojo roleAssignment = new RoleAssignmentPojo();
					info("populating pojo");
					this.populateRoleAssignmentPojo(generated, roleAssignment);
					info("RoleAssignment.generate is complete...");

					return roleAssignment;
				}

			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectGenerateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (XmlEnterpriseObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public RoleAssignmentQueryResultPojo getRoleAssignmentsForFilter(RoleAssignmentQueryFilterPojo filter)
			throws RpcException {

		RoleAssignmentQueryResultPojo result = new RoleAssignmentQueryResultPojo();
		List<RoleAssignmentPojo> pojos = new java.util.ArrayList<RoleAssignmentPojo>();
		try {
			RoleAssignmentQuerySpecification queryObject = (RoleAssignmentQuerySpecification) getObject(Constants.MOA_ROLE_ASSIGNMENT_QUERY_SPEC);
			RoleAssignment actionable = (RoleAssignment) getObject(Constants.MOA_ROLE_ASSIGNMENT);
			String authUserId = null;
			
			if (filter != null) {
				queryObject.setRoleDN(filter.getRoleDN());
				queryObject.setIdentityType(filter.getIdentityType());
				queryObject.setDirectAssignOnly(this.toStringFromBoolean(filter.isDirectAssignOnly()));
				queryObject.setUserDN(filter.getUserDN());
				
				if (filter.getUserLoggedIn() != null) {
					authUserId = this.getAuthUserIdForHALS(filter.getUserLoggedIn());
				}
			}

			if (authUserId == null) {
				authUserId = this.getAuthUserIdForHALS();
			}
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getRoleAssignmentsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			info("[getRoleAssignmentsForFilter] QuerySpec is: " + queryObject.toXmlString());
			@SuppressWarnings("unchecked")
			List<RoleAssignment> moas = actionable.query(queryObject,
					this.getIDMRequestService());
//			info("[getRoleAssignmentsForFilter] got " + moas.size() + " RoleAssignments back from ESB service.");
			for (RoleAssignment moa : moas) {
//				info("[getRoleAssignmentsForFilter] RoleAssignment.toXmlString: " + moa.toXmlString());
				String roleDn = moa.getRoleDN();
				if (roleDn != null) {
					if (roleDn.indexOf("RGR_AWS") >= 0 || 
						roleDn.indexOf(Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS) >= 0 ||
						roleDn.indexOf(Constants.ROLE_NAME_EMORY_NETWORK_ADMINS) >=0) {
						
						RoleAssignmentPojo pojo = new RoleAssignmentPojo();
						this.populateRoleAssignmentPojo(moa, pojo);
						pojos.add(pojo);
					}
				}
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public List<RoleAssignmentSummaryPojo> getCentralAdmins() throws RpcException {
		List<RoleAssignmentSummaryPojo> results = new java.util.ArrayList<RoleAssignmentSummaryPojo>();
		List<RoleAssignmentPojo> pojos = new java.util.ArrayList<RoleAssignmentPojo>();

		try {
			roleAssignmentProps = getAppConfig().getProperties(ROLE_ASSIGNMENT_PROPERTIES);
			String roleDN = roleAssignmentProps.getProperty("RoleDNDistinguishedName", "cn=RGR_AWS-AWS_ACCOUNT_NUMBER-EMORY_ROLE_NAME,cn=Level10,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=UserApplication,cn=DRIVERSET01,ou=Servers,o=EmoryDev");
			roleDN = roleDN.replaceAll("RGR_AWS", "");
			roleDN = roleDN.replaceAll("-" + Constants.REPLACEMENT_VAR_AWS_ACCOUNT_NUMBER, "");
			roleDN = roleDN.replaceAll("-" + Constants.REPLACEMENT_VAR_EMORY_ROLE_NAME, Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS);
			RoleAssignmentQueryFilterPojo filter = new RoleAssignmentQueryFilterPojo();
			info("[getCentralAdmins] filter.roleDN: " + roleDN);
			filter.setRoleDN(roleDN);
			filter.setIdentityType("USER");
			filter.setDirectAssignOnly(true);
			
//			RoleAssignmentQueryResultPojo ra_result = this.getRoleAssignmentsForFilter(filter);
			RoleAssignmentQueryResultPojo ra_result = new RoleAssignmentQueryResultPojo();
			try {
				RoleAssignmentQuerySpecification queryObject = (RoleAssignmentQuerySpecification) getObject(Constants.MOA_ROLE_ASSIGNMENT_QUERY_SPEC);
				RoleAssignment actionable = (RoleAssignment) getObject(Constants.MOA_ROLE_ASSIGNMENT);
	
				if (filter != null) {
					queryObject.setRoleDN(filter.getRoleDN());
					queryObject.setIdentityType(filter.getIdentityType());
					queryObject.setDirectAssignOnly(this.toStringFromBoolean(filter.isDirectAssignOnly()));
					queryObject.setUserDN(filter.getUserDN());
					info("[getCentralAdmins] query spec: " + queryObject.toXmlString());
				}
	
				@SuppressWarnings("unchecked")
				List<RoleAssignment> moas = actionable.query(queryObject,
						this.getIDMRequestService());
				info("[getCentralAdmins] got " + moas.size() + " RoleAssignments back from ESB service.");
				
				for (RoleAssignment moa : moas) {
					String roleDn = moa.getRoleDN();
					if (roleDn != null) {
						if (roleDn.indexOf(Constants.ROLE_NAME_EMORY_AWS_CENTRAL_ADMINS) >= 0) {
							
							info("[getCentralAdmins] RoleAssignment.toXmlString: " + moa.toXmlString());
							RoleAssignmentPojo pojo = new RoleAssignmentPojo();
							this.populateRoleAssignmentPojo(moa, pojo);
							pojos.add(pojo);
						}
					}
				}
	
				Collections.sort(pojos);
				ra_result.setResults(pojos);
				ra_result.setFilterUsed(filter);
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectQueryException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (XmlEnterpriseObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (ParseException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
			
			if (ra_result.getResults().size() > 0) {
				for (RoleAssignmentPojo ra : ra_result.getResults()) {
					if (ra.getExplicityIdentitiyDNs() != null) {
						for (String dn : ra.getExplicityIdentitiyDNs().getDistinguishedNames()) {
							info("[getCentralAdmins] dn: " + dn);
							String[] cns = dn.split(",");
							info("[getCentralAdmins] cns: " + cns);
							String publicIdCn = cns[0];
							info("[getCentralAdmins] publicIdCn: " + publicIdCn);
							String publicId = publicIdCn.substring(publicIdCn.indexOf("=") + 1);
							info("[getCentralAdmins] publicId: " + publicId);
							
							// get directoryperson for publicid
							// check cache to see if the DirectoryPerson is already in the cache
							// if not, go get it.
							DirectoryPersonPojo cached_dp = (DirectoryPersonPojo) Cache.getCache().get(
									Constants.DIRECTORY_PERSON + publicId);
							if (cached_dp == null) {
								DirectoryPersonQueryFilterPojo dp_filter = new DirectoryPersonQueryFilterPojo();
								dp_filter.setKey(publicId);
								try {
									DirectoryPersonQueryResultPojo dp_result = this.getDirectoryPersonsForFilter(dp_filter);
									info("[getCentralAdmins] got " + dp_result.getResults().size() + 
											" DirectoryPerson objects back for key=" + publicId);
									if (dp_result.getResults().size() > 0) {
										cached_dp = dp_result.getResults().get(0);
										info("[getCentralAdmins] " + cached_dp.getFullName() + " IS a Central Admin");
										// add to cache
										Cache.getCache().put(Constants.DIRECTORY_PERSON + publicId, cached_dp);
									}
									else {
										throw new RpcException("Could not find a DirectoryPerson for the public id: " + publicId);
									}
								}
								catch (RpcException e) {
									info("Error retrieving directory information for " + publicId + " processing will continue without it.");
									cached_dp = new DirectoryPersonPojo();
									cached_dp.setKey(publicId);
								}
							}
							else {
								info("[getCentralAdmins] " + cached_dp.getFullName() + " IS a Central Admin");
							}
							
							// create RoleAssignmentSummaryPojo and add it to the results
							RoleAssignmentSummaryPojo ra_summary = new RoleAssignmentSummaryPojo();
							ra_summary.setDirectoryPerson(cached_dp);
							ra_summary.setRoleAssignment(ra);
							info("[getCentralAdmins] adding RoleAssignmentSummary: " + ra_summary.toString());
							results.add(ra_summary);
						}
					}
				}
			}
			else {
				// error
				info("[getCentralAdmins] Could not find a RoleAssignment for the filter: " + filter.toString());
			}
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}

		return results;
	}

	@Override
	public List<RoleAssignmentSummaryPojo> getRoleAssignmentsForAccount(String accountId) throws RpcException {

		// get admin role assignments for this account
		// for each role assignment
		//	- get the public id from the ExplicitIdentitiesDN/distinguishedname
		//	- get directoryperson for the public id
		//	- create a RoleAssignmentSummaryPojo using the roleassignment and directory person
		//	- add that to the results list

		List<RoleAssignmentSummaryPojo> results = new java.util.ArrayList<RoleAssignmentSummaryPojo>();
		if (accountId == null) {
			return results;
		}
		try {
			roleAssignmentProps = getAppConfig().getProperties(ROLE_ASSIGNMENT_PROPERTIES);
			for (String roleName : Constants.ACCOUNT_ROLE_NAMES) {
				String roleDN = roleAssignmentProps.getProperty("RoleDNDistinguishedName", "cn=RGR_AWS-AWS_ACCOUNT_NUMBER-EMORY_ROLE_NAME,cn=Level10,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=UserApplication,cn=DRIVERSET01,ou=Servers,o=EmoryDev");
				roleDN = roleDN.replaceAll(Constants.REPLACEMENT_VAR_AWS_ACCOUNT_NUMBER, accountId);
				roleDN = roleDN.replaceAll(Constants.REPLACEMENT_VAR_EMORY_ROLE_NAME, roleName);
				RoleAssignmentQueryFilterPojo filter = new RoleAssignmentQueryFilterPojo();
				filter.setRoleDN(roleDN);
				filter.setIdentityType("USER");
				filter.setDirectAssignOnly(true);
				RoleAssignmentQueryResultPojo ra_result = this.getRoleAssignmentsForFilter(filter);
				if (ra_result != null) {
					if (ra_result.getResults().size() > 0) {
						for (RoleAssignmentPojo ra : ra_result.getResults()) {
							if (ra.getExplicityIdentitiyDNs() != null) {
								for (String dn : ra.getExplicityIdentitiyDNs().getDistinguishedNames()) {
									String[] cns = dn.split(",");
									String publicIdCn = cns[0];
									String publicId = publicIdCn.substring(publicIdCn.indexOf("=") + 1);
									
									// get directoryperson for publicid
									// check cache to see if the DirectoryPerson is already in the cache
									// if not, go get it.
									DirectoryPersonPojo cached_dp = (DirectoryPersonPojo) Cache.getCache().get(
											Constants.DIRECTORY_PERSON + publicId);
									if (cached_dp == null) {
										DirectoryPersonQueryFilterPojo dp_filter = new DirectoryPersonQueryFilterPojo();
										dp_filter.setKey(publicId);
										DirectoryPersonQueryResultPojo dp_result = this.getDirectoryPersonsForFilter(dp_filter);
										if (dp_result.getResults().size() > 0) {
											cached_dp = dp_result.getResults().get(0);
											// add to cache
											Cache.getCache().put(Constants.DIRECTORY_PERSON + publicId, cached_dp);
										}
										else {
											throw new RpcException("Could not find a DirectoryPerson for the public id: " + publicId);
										}
									}
									else {
//										info("[getAdminRoleAssignmentsForAccount] got DirectoryPerson (" + publicId + ") from cache.");
									}
									
									// create RoleAssignmentSummaryPojo and add it to the results
									RoleAssignmentSummaryPojo ra_summary = new RoleAssignmentSummaryPojo();
									ra_summary.setDirectoryPerson(cached_dp);
									ra_summary.setRoleAssignment(ra);
									info("[getRoleAssignmentsForAccount] adding RoleAssignmentSummary: " + ra_summary.toString());
									results.add(ra_summary);
								}
							}
						}
					}
					else {
						// error but not a show stopper
						info("Could not find a RoleAssignment for the filter: " + filter.toString());
					}
				}
				else {
					// error
					info("NULL RoleAssignment for the filter: " + filter.toString());
					throw new RpcException("NULL RoleAssignment returned for the filter: " + filter.toString());
				}
			}
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
		info("[getAdminRoleAssignmentsForAccount] returning " + results.size() + " RoleAssignmentSummary objects "
				+ "for account: " + accountId);
		return results;
	}

	@Override
	public void removeRoleAssignmentFromAccount(String accountId, RoleAssignmentPojo roleAssignment)
			throws RpcException {

		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting ROLE Assignment record on the server...");
				RoleAssignment moa = (RoleAssignment) getObject(Constants.MOA_ROLE_ASSIGNMENT);
				info("populating moa");
				this.populateRoleAssignmentMoa(roleAssignment, moa);
				
				/*
				<DeleteData>
					<DeleteAction type="Delete"/>
					<RoleAssignment>
						<RoleAssignmentActionType>revoke</RoleAssignmentActionType>
						<RoleAssignmentType>USER_TO_ROLE</RoleAssignmentType>
						<IdentityDN>cn=P4877359,ou=Users,ou=Data,o=EmoryDev</IdentityDN>
						<Reason>For Testing</Reason>
						<RoleDNs>
							<DistinguishedName>cn=RGR_AWS-308833937534-Administrator,cn=Level10,cn=RoleDefs,cn=RoleConfig,cn=AppConfig,cn=UserApplication,cn=DRIVERSET01,ou=Servers,o=EmoryDev</DistinguishedName>
						</RoleDNs>
					</RoleAssignment>
				</DeleteData>
				 */
				moa.setRoleAssignmentActionType("revoke");
				moa.setReason("Removed using VPCP App by (" + getUserLoggedIn(false).getEppn() +")");

				info("doing the RoleAssignment.delete...");
				this.doDelete(moa, getIDMRequestService());
				info("RoleAssignment.delete is complete...");

				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	/*
	private void populateFirewallExceptionRequestMoa(FirewallExceptionRequestPojo pojo,
			FirewallExceptionRequest moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		moa.setSystemId(pojo.getSystemId());
		moa.setUserNetID(pojo.getUserNetId());
		moa.setApplicationName(pojo.getApplicationName());
		moa.setSourceOutsideEmory(pojo.getIsSourceOutsideEmory());
		moa.setTimeRule(pojo.getTimeRule());
		if (pojo.getValidUntilDate() != null) {
			org.openeai.moa.objects.resources.Date validUntilDate = moa.newValidUntilDate();
			this.populateDate(validUntilDate, pojo.getValidUntilDate());
			moa.setValidUntilDate(validUntilDate);
		}
		moa.setSourceIpAddresses(pojo.getSourceIp());
		moa.setDestinationIpAddresses(pojo.getDestinationIp());
		moa.setPorts(pojo.getPorts());
		moa.setBusinessReason(pojo.getBusinessReason());
		moa.setPatched(pojo.getIsPatched());
		moa.setDefaultPasswdChanged(pojo.getIsDefaultPasswdChanged());
		moa.setAppConsoleACLed(pojo.getIsAppConsoleACLed());
		moa.setHardened(pojo.getIsHardened());
		moa.setPatchingPlan(pojo.getPatchingPlan());
		if (pojo.getCompliance().size() > 0) {
			for (String compliance : pojo.getCompliance()) {
				moa.addCompliance(compliance);
			}
		}
		moa.setOtherCompliance(pojo.getOtherCompliance());
		moa.setSensitiveDataDesc(pojo.getSensitiveDataDesc());
		moa.setLocalFirewallRules(pojo.getLocalFirewallRules());
		moa.setDefaultDenyZone(pojo.getIsDefaultDenyZone());
		for (String tag : pojo.getTags()) {
			moa.addTag(tag);
		}
		moa.setRequestNumber(pojo.getRequestNumber());
		moa.setRequestState(pojo.getRequestState());
		moa.setRequestItemNumber(pojo.getRequestItemNumber());
		moa.setRequestItemState(pojo.getRequestItemState());
		moa.setTechContact(pojo.getTechnicalContact());
//		this.setMoaCreateInfo(moa, pojo);
//		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings("unchecked")
	private void populateFirewallExceptionRequestPojo(FirewallExceptionRequest moa,
			FirewallExceptionRequestPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
	
		pojo.setSystemId(moa.getSystemId());
		pojo.setUserNetId(moa.getUserNetID());
		pojo.setApplicationName(moa.getApplicationName());
		pojo.setIsSourceOutsideEmory(moa.getSourceOutsideEmory());
		pojo.setTimeRule(moa.getTimeRule());
		if (moa.getValidUntilDate() != null) {
			pojo.setValidUntilDate(this.toDateFromDate(moa.getValidUntilDate()));
		}
		pojo.setSourceIp(moa.getSourceIpAddresses());
		pojo.setDestinationIp(moa.getDestinationIpAddresses());
		pojo.setPorts(moa.getPorts());
		pojo.setBusinessReason(moa.getBusinessReason());
		pojo.setIsPatched(moa.getPatched());
		pojo.setIsDefaultPasswdChanged(moa.getDefaultPasswdChanged());
		pojo.setIsAppConsoleACLed(moa.getAppConsoleACLed());
		pojo.setIsHardened(moa.getHardened());
		pojo.setPatchingPlan(moa.getPatchingPlan());
		for (String compliance : (List<String>) moa.getCompliance()) {
			pojo.getCompliance().add(compliance);
		}
		pojo.setOtherCompliance(moa.getOtherCompliance());
		pojo.setSensitiveDataDesc(moa.getSensitiveDataDesc());
		pojo.setLocalFirewallRules(moa.getLocalFirewallRules());
		pojo.setIsDefaultDenyZone(moa.getDefaultDenyZone());
		for (String tag : (List<String>) moa.getTag()) {
			pojo.getTags().add(tag);
		}
		pojo.setRequestNumber(moa.getRequestNumber());
		pojo.setRequestState(moa.getRequestState());
		pojo.setRequestItemNumber(moa.getRequestItemNumber());
		pojo.setRequestItemState(moa.getRequestItemState());
		pojo.setTechnicalContact(moa.getTechContact());
	}

	@Override
	public FirewallExceptionRequestQueryResultPojo getFirewallExceptionRequestsForFilter(
			FirewallExceptionRequestQueryFilterPojo filter) throws RpcException {

		FirewallExceptionRequestQueryResultPojo result = new FirewallExceptionRequestQueryResultPojo();
		List<FirewallExceptionRequestPojo> pojos = new java.util.ArrayList<FirewallExceptionRequestPojo>();
		try {
			FirewallExceptionRequestQuerySpecification queryObject = (FirewallExceptionRequestQuerySpecification) getObject(Constants.MOA_FIREWALL_EXCEPTION_REQUEST_QUERY_SPEC);
			FirewallExceptionRequest actionable = (FirewallExceptionRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REQUEST);

			if (filter != null) {
				queryObject.setSystemId(filter.getSystemId());
				queryObject.setUserNetID(filter.getUserNetId());
				queryObject.setApplicationName(filter.getApplicationName());
				queryObject.setSourceOutsideEmory(filter.getIsSourceOutsideEmory());
				queryObject.setTimeRule(filter.getTimeRule());
				if (filter.getValidUntilDate() != null) {
					org.openeai.moa.objects.resources.Date validUntilDate = queryObject.newValidUntilDate();
					this.populateDate(validUntilDate, filter.getValidUntilDate());
					queryObject.setValidUntilDate(validUntilDate);
				}
				queryObject.setSourceIpAddresses(filter.getSourceIp());
				queryObject.setDestinationIpAddresses(filter.getDestinationIp());
				queryObject.setPorts(filter.getPorts());
				queryObject.setBusinessReason(filter.getBusinessReason());
				queryObject.setPatched(filter.getIsPatched());
				queryObject.setDefaultPasswdChanged(filter.getIsDefaultPasswdChanged());
				queryObject.setAppConsoleACLed(filter.getIsAppConsoleACLed());
				queryObject.setHardened(filter.getIsHardened());
				queryObject.setPatchingPlan(filter.getPatchingPlan());
				if (filter.getCompliance().size() > 0) {
					for (String compliance : filter.getCompliance()) {
						queryObject.addCompliance(compliance);
					}
				}
				queryObject.setOtherCompliance(filter.getOtherCompliance());
				queryObject.setSensitiveDataDesc(filter.getSensitiveDataDesc());
				queryObject.setLocalFirewallRules(filter.getLocalFirewallRules());
				queryObject.setDefaultDenyZone(filter.getIsDefaultDenyZone());
				for (String tag : filter.getTags()) {
					queryObject.addTag(tag);
				}
				queryObject.setRequestNumber(filter.getRequestNumber());
				queryObject.setRequestState(filter.getRequestState());
				queryObject.setRequestItemNumber(filter.getRequestItemNumber());
				queryObject.setRequestItemState(filter.getRequestItemState());
				queryObject.setTechContact(filter.getTechnicalContact());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getFirewallExceptionRequestsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			info("[getFirewallExceptionRequestsForFilter] query spec is: " + queryObject.toXmlString());
			
			@SuppressWarnings("unchecked")
			List<FirewallExceptionRequest> moas = actionable.query(queryObject,
					this.getServiceNowRequestService());
			if (moas != null) {
				info("[getFirewallExceptionRequestsForFilter] got " + moas.size() + " FirewallExceptionRequest objects back from ESB.");
			}
			for (FirewallExceptionRequest moa : moas) {
				FirewallExceptionRequestPojo pojo = new FirewallExceptionRequestPojo();
				FirewallExceptionRequestPojo baseline = new FirewallExceptionRequestPojo();
				this.populateFirewallExceptionRequestPojo(moa, pojo);
				this.populateFirewallExceptionRequestPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public FirewallExceptionRequestPojo createFirewallExceptionRequest(FirewallExceptionRequestPojo rule)
			throws RpcException {

		rule.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("creating FirewallExceptionRequest record on the server...");
				FirewallExceptionRequest moa = (FirewallExceptionRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REQUEST);
				info("populating moa");
				this.populateFirewallExceptionRequestMoa(rule, moa);

				
				info("doing the FirewallExceptionRequest.create...");
				info("FirewallRuleException moa is: " + moa.toXmlString());
				this.doCreate(moa, getServiceNowRequestService());
				info("FirewallExceptionRequest.create is complete...");

//				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
				return rule;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseObjectCreateException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (XmlEnterpriseObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}

	@Override
	public FirewallExceptionRequestPojo updateFirewallExceptionRequest(FirewallExceptionRequestPojo rule)
			throws RpcException {

		rule.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating FirewallExceptionRequest on the server...");
            FirewallExceptionRequest newData = (FirewallExceptionRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REQUEST);
            FirewallExceptionRequest baselineData = (FirewallExceptionRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REQUEST);

            info("populating newData...");
            populateFirewallExceptionRequestMoa(rule, newData);

            info("populating baselineData...");
            populateFirewallExceptionRequestMoa(rule.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getServiceNowRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return rule;
	}

	@Override
	public void deleteFirewallExceptionRequest(FirewallExceptionRequestPojo rule) throws RpcException {
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting FirewallExceptionRequest record on the server...");
				FirewallExceptionRequest moa = (FirewallExceptionRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REQUEST);
				info("populating moa");
				this.populateFirewallExceptionRequestMoa(rule, moa);

				
				info("doing the FirewallExceptionRequest.delete...");
				this.doDelete(moa, getServiceNowRequestService());
				info("FirewallExceptionRequest.delete is complete...");

				return;
			} 
			catch (EnterpriseConfigurationObjectException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (EnterpriseFieldException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} 
			catch (JMSException e) {
				e.printStackTrace();
				throw new RpcException(e);
			} catch (EnterpriseObjectDeleteException e) {
				e.printStackTrace();
				throw new RpcException(e);
			}
		}
	}
	*/
	
	@Override
	public ReleaseInfo getReleaseInfo() throws RpcException {
		ReleaseInfo ri = new ReleaseInfo();
		ri.setApplicationEnvironment(applicationEnvironment);
		return ri;
	}

//	@GET
//	@Path("/ping")
//	public String ping() {
//		return "VpcpServiceImpl-PING";
//	}
	
	@GET
//	@Path("/check")
	public String getVpcpHealthCheck() {
		info("getVpcpHealthCheck...");
		
		try {
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException("Error");
		}
		AWSServiceSummaryPojo svcSummary = this.getAWSServiceMap();
		applicationEnvironment = generalProps.getProperty("applicationEnvironment", "DEV");

		info("appId: " + this.getAppId());
		info("AWS service map: " + svcSummary.getServiceMap());
		info("appConfig is: " + appConfig);
		info("AppConfig stats: " + this.getAppConfig().dumpStats());
		

		try {
			AccountQuerySpecification queryObject = (AccountQuerySpecification) getObject(Constants.MOA_ACCOUNT_QUERY_SPEC);
			Account actionable = (Account) getObject(Constants.MOA_ACCOUNT);
			ProducerPool l_awsProducerPool = (ProducerPool) getAppConfig().getObject(
					AWS_SERVICE_NAME);
			RequestService reqSvc = (RequestService) l_awsProducerPool.getProducer();
			@SuppressWarnings("unchecked")
			List<Account> moas = actionable.query(queryObject,reqSvc);
			info("[getVpcpHealthCheck] got " + moas.size() + " accounts from ESB service");
			
			if (applicationEnvironment != null && 
					svcSummary.getServiceMap() != null && 
					svcSummary.getServiceMap().size() > 0) {
			
					return "GOOD";
				}
				else {
					throw new RpcException("Error");
				}
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public String getEsbServiceStatusURL() throws RpcException {
		try {
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String esbServiceStatusURL = generalProps.getProperty("esbServiceStatusURL", null);
			if (esbServiceStatusURL == null) {
				throw new RpcException("Null 'esbServiceStatusURL' property.  This application is not configured correctly.");
			}
			return esbServiceStatusURL;
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e.getMessage());
		}
	}

	@Override
	public String getAccountSeriesText() throws RpcException {
		try {
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String l_awsConsoleInfoText = generalProps.getProperty("awsConsoleInfoText", "Unknown");
			return l_awsConsoleInfoText;
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e.getMessage());
		}
	}

	@Override
	public String getMyNetIdURL() throws RpcException {
		try {
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String myNetIdURL = generalProps.getProperty("myNetIdURL", "https://dev.mynetid.emory.edu/");
			return myNetIdURL;
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e.getMessage());
		}
	}

	@Override
	public AccountNotificationQueryResultPojo getAccountNotificationsForFilter(
			AccountNotificationQueryFilterPojo filter) throws RpcException {
		
		AccountNotificationQueryResultPojo result = new AccountNotificationQueryResultPojo();
		List<AccountNotificationPojo> pojos = new java.util.ArrayList<AccountNotificationPojo>();
		try {
			AccountNotificationQuerySpecification queryObject = (AccountNotificationQuerySpecification) getObject(Constants.MOA_ACCOUNT_NOTIFICATION_QUERY_SPEC);
			AccountNotification actionable = (AccountNotification) getObject(Constants.MOA_ACCOUNT_NOTIFICATION);

			if (filter != null) {
				if (filter.isUseQueryLanguage()) {
					QueryLanguage ql = queryObject.newQueryLanguage();
					ql.setName("byAccountId");
					ql.setType("hql");
					info("setting QueryLanguage max rows to: " + filter.getMaxRows());
					ql.setMax(this.toStringFromInt(filter.getMaxRows()));

					Parameter accountId = ql.newParameter();
					accountId.setValue(filter.getAccountId());
					accountId.setName("AccountId");
					ql.addParameter(accountId);
					queryObject.setQueryLanguage(ql);
				}
				else {
					queryObject.setAccountId(filter.getAccountId());
					queryObject.setAccountNotificationId(filter.getAccountNotificationId());
				}
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getAccountNotificationForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			RequestService reqSvc = this.getAWSRequestService();
			// TODO: if they're asking for ALL notifications, we'll have to bump 
			// the timeout interval WAY up
//			((PointToPointProducer) reqSvc)
//			.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval() * 2);

			@SuppressWarnings("unchecked")
			List<AccountNotification> moas = actionable.query(queryObject,
					reqSvc);
			info("[getAccountNotificationsForFilter] got " + moas.size() + 
					" UserNotifications from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (AccountNotification moa : moas) {
				AccountNotificationPojo pojo = new AccountNotificationPojo();
				AccountNotificationPojo baseline = new AccountNotificationPojo();
				if (filter.getSearchString() != null && filter.getSearchString().length() > 0) {
					// only add notifications that contain the search string
					if (moa.getSubject().toLowerCase().indexOf(filter.getSearchString().toLowerCase()) >= 0) {
						this.populateAccountNotificationPojo(moa, pojo);
						this.populateAccountNotificationPojo(moa, baseline);
						pojo.setBaseline(baseline);
						pojos.add(pojo);
					}
					else if (moa.getText().toLowerCase().indexOf(filter.getSearchString().toLowerCase()) >= 0) {
						this.populateAccountNotificationPojo(moa, pojo);
						this.populateAccountNotificationPojo(moa, baseline);
						pojo.setBaseline(baseline);
						pojos.add(pojo);
					}
				}
				else {
					// add them all
					this.populateAccountNotificationPojo(moa, pojo);
					this.populateAccountNotificationPojo(moa, baseline);
					pojo.setBaseline(baseline);
					pojos.add(pojo);
				}
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@SuppressWarnings("unchecked")
	private void populateAccountNotificationPojo(AccountNotification moa, AccountNotificationPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setAccountNotificationId(moa.getAccountNotificationId());
		pojo.setAccountId(moa.getAccountId());
		pojo.setType(moa.getType());
		pojo.setPriority(moa.getPriority());
		pojo.setSubject(moa.getSubject());
		pojo.setText(moa.getText());
		pojo.setReferenceid(moa.getReferenceId());
		if (moa.getAnnotationLength() > 0) {
			for (Annotation annotation : (List<Annotation>) moa.getAnnotation()) {
				AnnotationPojo ap = new AnnotationPojo();
				ap.setText(annotation.getText());
				pojo.getAnnotations().add(ap);
			}
		}
		
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateAccountNotificationMoa(AccountNotificationPojo pojo, AccountNotification moa) throws EnterpriseFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		moa.setAccountNotificationId(pojo.getAccountNotificationId());
		moa.setAccountId(pojo.getAccountId());
		moa.setType(pojo.getType());
		moa.setPriority(pojo.getPriority());
		moa.setSubject(pojo.getSubject());
		moa.setText(pojo.getText());
		moa.setReferenceId(moa.getReferenceId());
		for (AnnotationPojo annotation : pojo.getAnnotations()) {
			Annotation ap = moa.newAnnotation();
			ap.setText(annotation.getText());
			moa.addAnnotation(ap);
		}
		
        this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@Override
	public AccountNotificationPojo createAccountNotification(AccountNotificationPojo notification) throws RpcException {
        try {
            info("create AccountNotification on the server...");
            AccountNotification newData = (AccountNotification) getObject(Constants.MOA_ACCOUNT_NOTIFICATION);

            info("populating newData...");
            populateAccountNotificationMoa(notification, newData);

            info("doing the create...");
            doCreate(newData, getAWSRequestService());
            info("create is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return notification;
	}

	@Override
	public AccountNotificationPojo updateAccountNotification(AccountNotificationPojo notification) throws RpcException {
		// TODO temporary until notifications can be edited.
		notification.setUpdateInfo(this.getCachedUser().getPublicId());
		return notification;
	}

	@Override
	public void deleteAccountNotification(AccountNotificationPojo notification) throws RpcException {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unused")
	private void populateUserProfileMoa(UserProfilePojo pojo,
			UserProfile moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {


		moa.setUserId(pojo.getUserId());
		Datetime dt = new Datetime("LastLoginDatetime");
		populateDatetime(dt, pojo.getLastLoginTime());
		moa.setLastLoginDatetime(dt);

		for (PropertyPojo prop : pojo.getProperties()) {
			com.amazon.aws.moa.objects.resources.v1_0.Property m_prop = moa.newProperty();
			m_prop.setKey(prop.getName());
			m_prop.setValue(prop.getValue());
			moa.addProperty(m_prop);
		}
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void populateUserProfilePojo(UserProfile moa,
			UserProfilePojo pojo) throws XmlEnterpriseObjectException,
			ParseException, EnterpriseConfigurationObjectException {
		
		pojo.setUserId(moa.getUserId());
		if (moa.getLastLoginDatetime() != null) {
			pojo.setLastLoginTime(this.toDateFromDatetime(moa.getLastLoginDatetime()));
		}
		if (moa.getProperty() != null) {
			for (com.amazon.aws.moa.objects.resources.v1_0.Property m_prop : (List<com.amazon.aws.moa.objects.resources.v1_0.Property>) moa.getProperty()) {
				PropertyPojo prop = new PropertyPojo();
				prop.setName(m_prop.getKey());
				prop.setValue(m_prop.getValue());
				Properties props = getAppConfig().getProperties(USER_PROFILE_PROPERTIES);
				String prettyName = props.getProperty(prop.getName(), null);
				if (prettyName == null) {
					prop.setPrettyName(prop.getName());
				}
				else {
					prop.setPrettyName(prettyName);
				}

				pojo.getProperties().add(prop);
			}
		}

		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public UserProfileQueryResultPojo getUserProfilesForFilter(UserProfileQueryFilterPojo filter) throws RpcException {
		UserProfileQueryResultPojo result = new UserProfileQueryResultPojo();
		List<UserProfilePojo> pojos = new java.util.ArrayList<UserProfilePojo>();
		try {
			UserProfileQuerySpecification queryObject = (UserProfileQuerySpecification) getObject(Constants.MOA_USER_PROFILE_QUERY_SPEC);
			UserProfile actionable = (UserProfile) getObject(Constants.MOA_USER_PROFILE);

			if (filter != null) {
				queryObject.setUserId(filter.getUserId());
			}

			String authUserId = null;
			if (filter != null && filter.getUserAccount() != null) {
				authUserId = this.getAuthUserIdForHALS(filter.getUserAccount());
			}
			else {
				authUserId = this.getAuthUserIdForHALS();
			}
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getUserProfilesForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			@SuppressWarnings("unchecked")
			List<UserProfile> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getUserProfilesForFilter] got " + moas.size() + 
					" UserProfiles from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (UserProfile moa : moas) {
				UserProfilePojo pojo = new UserProfilePojo();
				UserProfilePojo baseline = new UserProfilePojo();
				this.populateUserProfilePojo(moa, pojo);
				this.populateUserProfilePojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@Override
	public UserProfilePojo createUserProfile(UserProfilePojo profile) throws RpcException {
		profile.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());
        try {
            info("creating UserProfile on the server...");
            UserProfile newData = (UserProfile) getObject(Constants.MOA_USER_PROFILE);

            info("populating newData...");
            populateUserProfileMoa(profile, newData);

            info("doing the update...");
            doCreate(newData, getAWSRequestService());
            info("create is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return profile;
	}

	@Override
	public UserProfilePojo updateUserProfile(UserProfilePojo profile) throws RpcException {
		if (this.getCachedUser() != null) {
			profile.setUpdateInfo(this.getCachedUser().getPublicId());
		}
		else {
			profile.setUpdateInfo(profile.getUserId());
		}
        try {
            info("updating UserProfile on the server...");
            UserProfile newData = (UserProfile) getObject(Constants.MOA_USER_PROFILE);
            UserProfile baselineData = (UserProfile) getObject(Constants.MOA_USER_PROFILE);

            info("populating newData...");
            populateUserProfileMoa(profile, newData);

            info("populating baselineData...");
            populateUserProfileMoa(profile.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getAWSRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return profile;
	}

	@Override
	public void deleteUserProfile(UserProfilePojo notification) throws RpcException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserActionQueryResultPojo getUserActionsForFilter(UserActionQueryFilterPojo filter) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserActionPojo createUserAction(UserActionPojo ua) throws RpcException {
		ua.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());
		
        try {
            info("creating UserAction on the server...");
            UserAction newData = (UserAction) getObject(Constants.MOA_USER_ACTION);

            info("populating newData...");
            populateUserActionMoa(ua, newData);

//            info("doing the create...");
            // TODO: UserAction that is imported is NOT actionable so this won't work 
            // as the aws-moa stands right now.
            // The MOA will need to be updated and then I can support this function.
//            doCreate(newData, getAWSRequestService());
//            info("create is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return ua;
	}

	@SuppressWarnings("unused")
	private void populateUserActionMoa(UserActionPojo pojo,
			UserAction moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		moa.setUserId(pojo.getUserId());
		moa.setAction(pojo.getAction());
		moa.setDescription(pojo.getDescription());
		moa.setDetail(pojo.getDetail());
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@Override
	public TermsOfUseQueryResultPojo getTermsOfUseForFilter(TermsOfUseQueryFilterPojo filter) throws RpcException {
		TermsOfUseQueryResultPojo result = new TermsOfUseQueryResultPojo();
		result.setFilterUsed(filter);
		List<TermsOfUsePojo> pojos = new java.util.ArrayList<TermsOfUsePojo>();
		try {
			TermsOfUseQuerySpecification queryObject = (TermsOfUseQuerySpecification) getObject(Constants.MOA_TERMS_OF_USE_QUERY_SPECIFICATION);
			TermsOfUse actionable = (TermsOfUse) getObject(Constants.MOA_TERMS_OF_USE);

			if (filter != null) {
				queryObject.setTermsOfUseId(filter.getTermsOfUseId());
				queryObject.setRevision(filter.getRevision());

				if (filter.getEffectiveDate() != null) {
					org.openeai.moa.objects.resources.Date effectiveDate = queryObject.newEffectiveDate();
					this.populateDate(effectiveDate, filter.getEffectiveDate());
					queryObject.setEffectiveDate(effectiveDate);
				}
				if (filter.getExpirationDate() != null) {
					org.openeai.moa.objects.resources.Date expirationDate = queryObject.newExpirationDate();
					this.populateDate(expirationDate, filter.getExpirationDate());
					queryObject.setExpirationDate(expirationDate);
				}

				if (filter.isEffectiveTerms()) {
					// get the terms of use that are currently in effect
					QueryLanguage ql = queryObject.newQueryLanguage();
					ql.setName("inEffectTermsOfUse");
					ql.setType("hql");

					Parameter p_currentDate = ql.newParameter();
					SimpleDateFormat l_dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					p_currentDate.setValue(l_dateFormatter.format(new Date()));
					p_currentDate.setName("currentDate");
					ql.addParameter(p_currentDate);
					queryObject.setQueryLanguage(ql);
				}
			}

			String authUserId = null;
			if (filter != null && filter.getUserAccount() != null) {
				authUserId = this.getAuthUserIdForHALS(filter.getUserAccount());
			}
			else {
				authUserId = this.getAuthUserIdForHALS();
			}
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getTermsOfUseForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			@SuppressWarnings("unchecked")
			List<TermsOfUse> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getTermsOfUseForFilter] got " + moas.size() + 
					" Terms of Use objects from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (TermsOfUse moa : moas) {
				TermsOfUsePojo pojo = new TermsOfUsePojo();
//				TermsOfUsePojo baseline = new TermsOfUsePojo();
				this.populateTermsOfUsePojo(moa, pojo);
//				this.populateTermsOfUsePojo(moa, baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	private void populateTermsOfUsePojo(TermsOfUse moa, TermsOfUsePojo pojo) throws XmlEnterpriseObjectException {
		pojo.setTermsOfUseId(moa.getTermsOfUseId());
		pojo.setRevision(moa.getRevision());
		pojo.setEffectiveDate(this.toDateFromDate(moa.getEffectiveDate()));
		pojo.setExpirationDate(this.toDateFromDate(moa.getExpirationDate()));
		pojo.setText(moa.getText());
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public TermsOfUseAgreementQueryResultPojo getTermsOfUseAgreementsForFilter(
			TermsOfUseAgreementQueryFilterPojo filter) throws RpcException {

		TermsOfUseAgreementQueryResultPojo result = new TermsOfUseAgreementQueryResultPojo();
		result.setFilterUsed(filter);
		List<TermsOfUseAgreementPojo> pojos = new java.util.ArrayList<TermsOfUseAgreementPojo>();
		try {
			TermsOfUseAgreementQuerySpecification queryObject = (TermsOfUseAgreementQuerySpecification) getObject(Constants.MOA_TERMS_OF_USE_AGREEMENT_QUERY_SPECIFICATION);
			TermsOfUseAgreement actionable = (TermsOfUseAgreement) getObject(Constants.MOA_TERMS_OF_USE_AGREEMENT);

			if (filter != null) {
				queryObject.setTermsOfUseId(filter.getTermsOfUseId());
				queryObject.setTermsOfUseAgreementId(filter.getTermsOfUseAgreementId());
				queryObject.setUserId(filter.getUserId());
			}

			String authUserId = null;
			if (filter != null && filter.getUserAccount() != null) {
				authUserId = this.getAuthUserIdForHALS(filter.getUserAccount());
			}
			else {
				authUserId = this.getAuthUserIdForHALS();
			}
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getTermsOfUseAgreementForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			@SuppressWarnings("unchecked")
			List<TermsOfUseAgreement> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getTermsOfUseAgreementForFilter] got " + moas.size() + 
					" Terms of Use Agreement objects from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (TermsOfUseAgreement moa : moas) {
				TermsOfUseAgreementPojo pojo = new TermsOfUseAgreementPojo();
				TermsOfUseAgreementPojo baseline = new TermsOfUseAgreementPojo();
				this.populateTermsOfUseAgreementPojo(moa, pojo);
				this.populateTermsOfUseAgreementPojo(moa, baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	private void populateTermsOfUseAgreementPojo(TermsOfUseAgreement moa, TermsOfUseAgreementPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setTermsOfUseAgreementId(moa.getTermsOfUseAgreementId());
		pojo.setTermsOfUseId(moa.getTermsOfUseId());
		pojo.setUserId(moa.getUserId());
		pojo.setStatus(moa.getStatus());
		pojo.setPresentedDate(this.toDateFromDatetime(moa.getPresentedDatetime()));
		pojo.setAgreedDate(this.toDateFromDatetime(moa.getAgreedDatetime()));
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public TermsOfUseAgreementPojo createTermsOfUseAgreement(TermsOfUseAgreementPojo toua) throws RpcException {
		toua.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());
		
        try {
            info("creating TermsOfUseAgreement on the server...");
            TermsOfUseAgreement newData = (TermsOfUseAgreement) getObject(Constants.MOA_TERMS_OF_USE_AGREEMENT);

            info("populating newData...");
            populateTermsOfUseAgreementMoa(toua, newData);

            info("doing the create...");
            doCreate(newData, getAWSRequestService());
            info("create is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return toua;
	}

	private void populateTermsOfUseAgreementMoa(TermsOfUseAgreementPojo pojo, TermsOfUseAgreement moa) throws EnterpriseFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		moa.setTermsOfUseAgreementId(pojo.getTermsOfUseAgreementId());
		moa.setTermsOfUseId(pojo.getTermsOfUseId());
		moa.setUserId(pojo.getUserId());
		moa.setStatus(pojo.getStatus());
		
		org.openeai.moa.objects.resources.Datetime presentedDT = moa.newPresentedDatetime();
		this.populateDatetime(presentedDT, pojo.getPresentedDate());
		moa.setPresentedDatetime(presentedDT);
		
		org.openeai.moa.objects.resources.Datetime agreedDT = moa.newAgreedDatetime();
		this.populateDatetime(agreedDT, pojo.getAgreedDate());
		moa.setAgreedDatetime(agreedDT);
		
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	@Override
	public ServiceSecurityAssessmentQueryResultPojo getSecurityAssessmentsForFilter(
			ServiceSecurityAssessmentQueryFilterPojo filter) throws RpcException {
		
		ServiceSecurityAssessmentQueryResultPojo result = new ServiceSecurityAssessmentQueryResultPojo();
		result.setFilterUsed(filter);
		List<ServiceSecurityAssessmentPojo> pojos = new java.util.ArrayList<ServiceSecurityAssessmentPojo>();
		try {
			ServiceSecurityAssessmentQuerySpecification queryObject = (ServiceSecurityAssessmentQuerySpecification) getObject(Constants.MOA_SVC_SECURITY_ASSESSMENT_QUERY_SPEC);
			ServiceSecurityAssessment actionable = (ServiceSecurityAssessment) getObject(Constants.MOA_SVC_SECURITY_ASSESSMENT);

			if (filter != null) {
				queryObject.setServiceId(filter.getServiceId());
				queryObject.setServiceSecurityAssessmentId(filter.getAssessmentId());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getSecurityAssessmentsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			@SuppressWarnings("unchecked")
			List<ServiceSecurityAssessment> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getSecurityAssessmentsForFilter] got " + moas.size() + 
					" Security Assessments from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (ServiceSecurityAssessment moa : moas) {
				info("[query] assessment as xml string: " + moa.toXmlString());
				ServiceSecurityAssessmentPojo pojo = new ServiceSecurityAssessmentPojo();
				ServiceSecurityAssessmentPojo baseline = new ServiceSecurityAssessmentPojo();
				this.populateSecurityAssessmentPojo(moa, pojo);
				this.populateSecurityAssessmentPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@SuppressWarnings("unchecked")
	private void populateSecurityAssessmentPojo(ServiceSecurityAssessment moa, ServiceSecurityAssessmentPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setServiceSecurityAssessmentId(moa.getServiceSecurityAssessmentId());
		pojo.setStatus(moa.getStatus());
		if (moa.getServiceId() != null) {
			for (String svcid : (List<String>)moa.getServiceId()) {
				pojo.getServiceIds().add(svcid);
			}
		}
		if (moa.getSecurityRisk() != null) {
			for (SecurityRisk risk : (List<SecurityRisk>)moa.getSecurityRisk()) {
				SecurityRiskPojo rp = new SecurityRiskPojo();
				rp.setSecurityRiskId(risk.getSecurityRiskId());
				rp.setServiceId(risk.getServiceId());
				rp.setSequenceNumber(this.toIntFromString(risk.getSequenceNumber()));
				rp.setSecurityRiskName(risk.getServiceRiskName());
				rp.setRiskLevel(risk.getRiskLevel());
				rp.setDescription(risk.getDescription());
				rp.setAssessorId(risk.getAssessorId());
				rp.setAssessmentDate(this.toDateFromDatetime(risk.getAssessmentDatetime()));
				if (risk.getCountermeasure() != null) {
					for (Countermeasure cm : (List<Countermeasure>)risk.getCountermeasure()) {
						CounterMeasurePojo cmp = new CounterMeasurePojo();
						cmp.setSecurityRiskId(cm.getSecurityRiskId());
						cmp.setStatus(cm.getStatus());
						cmp.setDescription(cm.getDescription());
						cmp.setVerifier(cm.getVerifier());
						cmp.setVerificationDate(this.toDateFromDatetime(cm.getVerificationDatetime()));
						risk.getCountermeasure().add(cmp);
					}
				}
				pojo.getSecurityRisks().add(rp);
			}
		}
		if (moa.getServiceControl() != null) {
			for (ServiceControl sc : (List<ServiceControl>)moa.getServiceControl()) {
				ServiceControlPojo scp = new ServiceControlPojo();
				scp.setServiceId(sc.getServiceId());
				scp.setServiceControlId(sc.getServiceControlId());
				scp.setSequenceNumber(this.toIntFromString(sc.getSequenceNumber()));
				scp.setServiceControlName(sc.getServiceControlName());
				scp.setDescription(sc.getDescription());
				scp.setAssessorId(sc.getAssessorId());
				scp.setAssessmentDate(this.toDateFromDatetime(sc.getAssessmentDatetime()));
				scp.setVerifier(sc.getVerifier());
				scp.setVerificationDate(this.toDateFromDatetime(sc.getVerificationDatetime()));
				pojo.getServiceControls().add(scp);
			}
		}
		if (moa.getServiceGuideline() != null) {
			for (ServiceGuideline sg : (List<ServiceGuideline>)moa.getServiceGuideline()) {
				ServiceGuidelinePojo sgp = new ServiceGuidelinePojo();
				sgp.setServiceGuidelineName(sg.getServiceId());
				sgp.setSequenceNumber(this.toIntFromString(sg.getSequenceNumber()));
				sgp.setServiceGuidelineName(sg.getServiceGuidelineName());
				sgp.setDescription(sg.getDescription());
				sgp.setAssessorId(sg.getAssessorId());
				sgp.setAssessmentDate(this.toDateFromDatetime(sg.getAssessmentDatetime()));
				pojo.getServiceGuidelines().add(sgp);
			}
		}
		if (moa.getServiceTestPlan() != null) {
			ServiceTestPlan stpm = moa.getServiceTestPlan();
			ServiceTestPlanPojo stpp = new ServiceTestPlanPojo();
			stpp.setServiceId(stpm.getServiceId());
			// test plan requirement list
			if (stpm.getServiceTestRequirement() != null) {
//				List<ServiceTestRequirementPojo> strPojos = new java.util.ArrayList<ServiceTestRequirementPojo>();
				for (ServiceTestRequirement strm : (List<ServiceTestRequirement>)stpm.getServiceTestRequirement()) {
					ServiceTestRequirementPojo strp = new ServiceTestRequirementPojo();
					strp.setServiceTestRequirementId(strm.getServiceTestRequirementId());
					strp.setSequenceNumber(this.toIntFromString(strm.getSequenceNumber()));
					strp.setDescription(strm.getDescription());
					if (strm.getServiceTest() != null) {
						for (ServiceTest stm : (List<ServiceTest>)strm.getServiceTest()) {
							ServiceTestPojo stp = new ServiceTestPojo();
							stp.setServiceTestId(stm.getServiceTestId());
							stp.setSequenceNumber(this.toIntFromString(stm.getSequenceNumber()));
							stp.setDescription(stm.getDescription());
							stp.setServiceTestExpectedResult(stm.getServiceTestExpectedResult());
							if (stm.getServiceTestStep() != null) {
								for (ServiceTestStep stsm : (List<ServiceTestStep>)stm.getServiceTestStep()) {
									ServiceTestStepPojo stsp = new ServiceTestStepPojo();
									stsp.setServiceTestId(stsm.getServiceTestId());
									stsp.setServiceTestStepId(stsm.getServiceTestStepId());
									stsp.setSequenceNumber(this.toIntFromString(stsm.getSequenceNumber()));
									stsp.setDescription(stsm.getDescription());
									stp.getServiceTestSteps().add(stsp);
								}
							}
							strp.getServiceTests().add(stp);
						}
					}
//					strPojos.add(strp);
					stpp.getServiceTestRequirements().add(strp);
				}
//				Collections.sort(strPojos);
//				stpp.setServiceTestRequirements(strPojos);
			}
			pojo.setServiceTestPlan(stpp);
		}
	}

	private void populateSecurityAssessmentMoa(ServiceSecurityAssessmentPojo pojo,
			ServiceSecurityAssessment moa) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, EnterpriseFieldException {
		
		moa.setServiceSecurityAssessmentId(pojo.getServiceSecurityAssessmentId());
		moa.setStatus(pojo.getStatus());
		for (String svcid : pojo.getServiceIds()) {
			moa.addServiceId(svcid);
		}
		for (SecurityRiskPojo risk : pojo.getSecurityRisks()) {
			SecurityRisk moa_risk = moa.newSecurityRisk();
			moa_risk.setSecurityRiskId(risk.getSecurityRiskId());
			moa_risk.setServiceId(risk.getServiceId());
			moa_risk.setSequenceNumber(this.toStringFromInt(risk.getSequenceNumber()));
			moa_risk.setServiceRiskName(risk.getSecurityRiskName());
			moa_risk.setRiskLevel(risk.getRiskLevel());
			moa_risk.setDescription(risk.getDescription());
			moa_risk.setAssessorId(risk.getAssessorId());
			
			org.openeai.moa.objects.resources.Datetime assessDT = moa_risk.newAssessmentDatetime();
			this.populateDatetime(assessDT, risk.getAssessmentDate());
			moa_risk.setAssessmentDatetime(assessDT);
			
			for (CounterMeasurePojo cm : risk.getCouterMeasures()) {
				Countermeasure moa_cm = moa_risk.newCountermeasure();
				moa_cm.setSecurityRiskId(cm.getSecurityRiskId());
				moa_cm.setStatus(cm.getStatus());
				moa_cm.setDescription(cm.getDescription());
				moa_cm.setVerifier(cm.getVerifier());
				
				org.openeai.moa.objects.resources.Datetime verifyDT = moa_cm.newVerificationDatetime();
				this.populateDatetime(verifyDT, cm.getVerificationDate());
				moa_cm.setVerificationDatetime(verifyDT);
				
				moa_risk.addCountermeasure(moa_cm);
			}
			moa.addSecurityRisk(moa_risk);
		}
		
		for (ServiceControlPojo sc : pojo.getServiceControls()) {
			ServiceControl moa_scp = moa.newServiceControl();
			moa_scp.setServiceId(sc.getServiceId());
			moa_scp.setServiceControlId(sc.getServiceControlId());
			moa_scp.setSequenceNumber(this.toStringFromInt(sc.getSequenceNumber()));
			moa_scp.setServiceControlName(sc.getServiceControlName());
			moa_scp.setDescription(sc.getDescription());
			moa_scp.setAssessorId(sc.getAssessorId());
			
			org.openeai.moa.objects.resources.Datetime assessDT = moa_scp.newAssessmentDatetime();
			this.populateDatetime(assessDT, sc.getAssessmentDate());
			moa_scp.setAssessmentDatetime(assessDT);
			
			moa_scp.setVerifier(sc.getVerifier());
			
			org.openeai.moa.objects.resources.Datetime verifyDT = moa_scp.newVerificationDatetime();
			this.populateDatetime(verifyDT, sc.getVerificationDate());
			moa_scp.setVerificationDatetime(verifyDT);
			
			moa.addServiceControl(moa_scp);
		}

		for (ServiceGuidelinePojo sg : pojo.getServiceGuidelines()) {
			ServiceGuideline moa_sg = moa.newServiceGuideline();
			moa_sg.setServiceGuidelineName(sg.getServiceId());
			moa_sg.setSequenceNumber(this.toStringFromInt(sg.getSequenceNumber()));
			moa_sg.setServiceGuidelineName(sg.getServiceGuidelineName());
			moa_sg.setDescription(sg.getDescription());
			moa_sg.setAssessorId(sg.getAssessorId());
			
			org.openeai.moa.objects.resources.Datetime assessDT = moa_sg.newAssessmentDatetime();
			this.populateDatetime(assessDT, sg.getAssessmentDate());
			moa_sg.setAssessmentDatetime(assessDT);

			moa.addServiceGuideline(moa_sg);
		}
			
		if (pojo.getServiceTestPlan() != null) {
			ServiceTestPlanPojo stpp = pojo.getServiceTestPlan();
			ServiceTestPlan moa_stp = moa.newServiceTestPlan();
			moa_stp.setServiceId(pojo.getServiceTestPlan().getServiceId());
			// test plan requirement list
			for (ServiceTestRequirementPojo strp : stpp.getServiceTestRequirements()) {
				ServiceTestRequirement strm = moa_stp.newServiceTestRequirement();
				strm.setServiceTestRequirementId(strp.getServiceTestRequirementId());
				strm.setSequenceNumber(this.toStringFromInt(strp.getSequenceNumber()));
				strm.setDescription(strp.getDescription());
				for (ServiceTestPojo stp : strp.getServiceTests()) {
					ServiceTest stm = strm.newServiceTest();
					stm.setServiceTestId(stp.getServiceTestId());
					stm.setSequenceNumber(this.toStringFromInt(stp.getSequenceNumber()));
					stm.setDescription(stp.getDescription());
					stm.setServiceTestExpectedResult(stp.getServiceTestExpectedResult());
					for (ServiceTestStepPojo stsp : stp.getServiceTestSteps()) {
						ServiceTestStep stsm = stm.newServiceTestStep();
						stsm.setServiceTestId(stsp.getServiceTestId());
						stsm.setServiceTestStepId(stsp.getServiceTestStepId());
						stsm.setSequenceNumber(this.toStringFromInt(stsp.getSequenceNumber()));
						stsm.setDescription(stsp.getDescription());
						stm.addServiceTestStep(stsm);
					}
					strm.addServiceTest(stm);
				}
				moa_stp.addServiceTestRequirement(strm);
			}
			moa.setServiceTestPlan(moa_stp);
		}
	}

	@Override
	public ServiceSecurityAssessmentPojo createSecurityAssessment(ServiceSecurityAssessmentPojo assessment)
			throws RpcException {
		assessment.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());
		
        try {
            info("creating UserNotification on the server...");
            ServiceSecurityAssessment newData = (ServiceSecurityAssessment) getObject(Constants.MOA_SVC_SECURITY_ASSESSMENT);

            info("populating newData...");
            populateSecurityAssessmentMoa(assessment, newData);

            info("doing the update...");
            doCreate(newData, getAWSRequestService());
            info("create is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return assessment;
	}

	@Override
	public ServiceSecurityAssessmentPojo updateSecurityAssessment(ServiceSecurityAssessmentPojo assessment)
			throws RpcException {
		// TODO Auto-generated method stub
		assessment.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating UserNotification on the server...");
            ServiceSecurityAssessment newData = (ServiceSecurityAssessment) getObject(Constants.MOA_SVC_SECURITY_ASSESSMENT);
            ServiceSecurityAssessment baselineData = (ServiceSecurityAssessment) getObject(Constants.MOA_SVC_SECURITY_ASSESSMENT);

            info("populating newData...");
            populateSecurityAssessmentMoa(assessment, newData);

            info("populating baselineData...");
            populateSecurityAssessmentMoa(assessment.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            info("[update] assessment as xml: " + newData.toXmlString());
            doUpdate(newData, getAWSRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return assessment;
	}

	@Override
	public void deleteSecurityAssessment(ServiceSecurityAssessmentPojo service) throws RpcException {
		try {
			info("deleting Security Assessment record on the server...");
			ServiceSecurityAssessment moa = (ServiceSecurityAssessment) getObject(Constants.MOA_SVC_SECURITY_ASSESSMENT);
			info("populating moa");
			this.populateSecurityAssessmentMoa(service, moa);

			
			info("doing the SecurityAssessment.delete...");
			this.doDelete(moa, getAWSRequestService());
			info("SecurityAssessment.delete is complete...");

			return;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseObjectDeleteException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public List<String> getAssessmentStatusTypeItems() {
		List<String> types = new java.util.ArrayList<String>();
		types.add("Complete");
		types.add("In Progress");
		types.add("Not Started");
		return types;
	}

	@Override
	public boolean userHasUnreadNotifications(UserAccountPojo user) throws RpcException {
		if (user == null) {
			return false;
		}
		UserAccountPojo cachedUser = (UserAccountPojo) Cache.getCache().get(
				Constants.USER_ACCOUNT + getCurrentSessionId());
		if (cachedUser == null) {
			return false;
		}

		UserNotificationQueryFilterPojo filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(user.getPublicId());
		filter.setReadStr("false");
		filter.setRead(false);
		filter.setUseQueryLanguage(true);
		filter.setMaxRows(5);
//		// get notifications created in the last hour
//		Date now = new Date();
//		Date yesterday = new Date(now.getTime() - Constants.MILLIS_PER_HR);
//		filter.setStartDate(yesterday);
//		filter.setEndDate(now);
		UserNotificationQueryResultPojo result = this.getUserNotificationsForFilter(filter);
		info("User " + user.getPublicId() + " has " + 
				result.getResults().size() + " un-read User Notifications in the last day");
		if (result.getResults().size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int getNotificationCheckIntervalMillis() throws RpcException {
		try {
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String s_interval = generalProps.getProperty("notificationCheckIntervalMillis", "10000");
			int interval = Integer.parseInt(s_interval);
			return interval;
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e.getMessage());
		}
	}

	@Override
	public List<String> getRiskLevelTypeItems() {
		// TEMP
		List<String> l = new java.util.ArrayList<String>();
		l.add("Low");
		l.add("Medium");
		l.add("High");
		l.add("Critical");
		return l;
	}

	@Override
	public List<String> getCounterMeasureStatusTypeItems() {
		// TEMP
		List<String> l = new java.util.ArrayList<String>();
		l.add("Unknown");
		l.add("Pending");
		l.add("In-progress");
		l.add("Complete");
		return l;
	}
	
	private UserAccountPojo getCachedUser() {
		UserAccountPojo user = (UserAccountPojo) Cache.getCache().get(
				Constants.USER_ACCOUNT + getCurrentSessionId());
		
		if (user == null) {
			user = this.getUserLoggedIn(false);
		}
		return user;
	}

	@Override
	public SecurityRiskDetectionQueryResultPojo getSecurityRiskDetectionsForFilter(
			SecurityRiskDetectionQueryFilterPojo filter) throws RpcException {

		SecurityRiskDetectionQueryResultPojo result = new SecurityRiskDetectionQueryResultPojo();
		result.setFilterUsed(filter);
		List<SecurityRiskDetectionPojo> pojos = new java.util.ArrayList<SecurityRiskDetectionPojo>();
		try {
			SecurityRiskDetectionQuerySpecification queryObject = (SecurityRiskDetectionQuerySpecification) getObject(Constants.MOA_SECURITY_RISK_DETECTION_QUERY_SPEC);
			SecurityRiskDetection actionable = (SecurityRiskDetection) getObject(Constants.MOA_SECURITY_RISK_DETECTION);

			if (filter != null) {
				queryObject.setSecurityRiskDetectionId(filter.getSecurityRiskDetectionId());
				queryObject.setAccountId(filter.getAccountId());
				queryObject.setSecurityRiskDetector(filter.getSecurityRiskDetector());
				queryObject.setSecurityRiskRemediator(filter.getSecurityRiskRemediator());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getSecurityRiskDetectionsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			@SuppressWarnings("unchecked")
			List<SecurityRiskDetection> moas = actionable.query(queryObject,
					this.getSrdRequestService());
			info("[getSecurityRiskDetectionsForFilter] got " + moas.size() + 
					" objects from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (SecurityRiskDetection moa : moas) {
				info("[getSecurityRiskDetectionsForFilter] moa is: " + moa.toXmlString());
				SecurityRiskDetectionPojo pojo = new SecurityRiskDetectionPojo();
				SecurityRiskDetectionPojo baseline = new SecurityRiskDetectionPojo();
				this.populateSecurityRiskDetectionPojo(moa, pojo);
				this.populateSecurityRiskDetectionPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@SuppressWarnings("unchecked")
	private void populateSecurityRiskDetectionPojo(SecurityRiskDetection moa, SecurityRiskDetectionPojo pojo) throws XmlEnterpriseObjectException {
		/*
		<!ELEMENT SecurityRiskDetection (SecurityRiskDetectionId, AccountId, SecurityRiskDetector, 
		SecurityRiskRemediator, DetectedSecurityRisk*, CreateUser, CreateDatetime, 
		LastUpdateUser?, LastUpdateDatetime?)>
			 */
		pojo.setSecurityRiskDetectionId(moa.getSecurityRiskDetectionId());
		pojo.setSecurityRiskDetector(moa.getSecurityRiskDetector());
		pojo.setSecurityRiskRemediator(moa.getSecurityRiskRemediator());
		pojo.setAccountId(moa.getAccountId());
		if (moa.getDetectedSecurityRisk() != null) {
			for (DetectedSecurityRisk risk : (List<DetectedSecurityRisk>)moa.getDetectedSecurityRisk()) {
				DetectedSecurityRiskPojo dsr = new DetectedSecurityRiskPojo();
				dsr.setType(risk.getType());
				dsr.setAmazonResourceName(risk.getAmazonResourceName());
				if (risk.getRemediationResult() != null) {
					RemediationResult rrm = risk.getRemediationResult();
					RemediationResultPojo rrp = new RemediationResultPojo();
					rrp.setStatus(rrm.getStatus());
					rrp.setDescription(rrm.getDescription());
					if (rrm.getError() != null) {
						for (String error : (List<String>)rrm.getError()) {
							ErrorPojo ep = new ErrorPojo();
//							ep.setErrorNumber(error.getErrorNumber());
							ep.setType("SRD");
							ep.setDescription(error);
							rrp.getErrors().add(ep);
						}
					}
					dsr.setRemediationResult(rrp);
				}
				pojo.getDetectedSecurityRisks().add(dsr);
			}
		}
		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	public ProducerPool getSrdProducerPool() {
		return srdProducerPool;
	}

	public void setSrdProducerPool(ProducerPool srdProducerPool) {
		this.srdProducerPool = srdProducerPool;
	}

	@Override
	public void markAllUnreadNotificationsForUserAsRead(UserAccountPojo user) throws RpcException {
		UserNotificationQueryFilterPojo filter = new UserNotificationQueryFilterPojo();
		filter.setUseQueryLanguage(true);
		filter.setReadStr("false");
		filter.setRead(false);
		filter.setUserId(user.getPublicId());
		info("[markAllUnreadNotificationsForUserAsRead] getting all un-read "
				+ "notifications for user " + user.getPublicId());
		boolean keepChecking = true;
		int totalUpdates = 0;
		notificationLoop: while (keepChecking) {
			UserNotificationQueryResultPojo result = this.getUserNotificationsForFilter(filter);
			if (result.getResults().size() == 0) {
				keepChecking = false;
				break notificationLoop;
			}
			else {
				totalUpdates += result.getResults().size();
			}
			info("[markAllUnreadNotificationsForUserAsRead] marking " + 
					result.getResults().size() + " unread UserNotifications for user " + 
					user.getPublicId() + " to 'read'");
			for (UserNotificationPojo unp : result.getResults()) {
				unp.setRead(true);
				unp.setReadDateTime(new Date());
				this.updateUserNotification(unp);
			}
			info("[markAllUnreadNotificationsForUserAsRead] " + totalUpdates + 
					" total updates so far...");
		}
		info("[markAllUnreadNotificationsForUserAsRead] DONE marking " + totalUpdates + 
				" unread UserNotifications for user " + 
				user.getPublicId() + " to 'read'");
	}

	@Override
	public TermsOfUseSummaryPojo getTermsOfUseSummaryForUser(UserAccountPojo user) throws RpcException {
		TermsOfUseSummaryPojo summary = new TermsOfUseSummaryPojo();
		summary.setHasValidTermsOfUseAgreement(false);
		
		TermsOfUseQueryFilterPojo tou_filter = new TermsOfUseQueryFilterPojo();
		tou_filter.setEffectiveTerms(true);
		tou_filter.setUserAccount(user);
		TermsOfUseQueryResultPojo tou_result = this.getTermsOfUseForFilter(tou_filter);
		
		TermsOfUseAgreementQueryFilterPojo toua_filter = new TermsOfUseAgreementQueryFilterPojo();
		toua_filter.setUserId(user.getPublicId());
		toua_filter.setUserAccount(user);
		TermsOfUseAgreementQueryResultPojo toua_result = this.getTermsOfUseAgreementsForFilter(toua_filter);
		
		if (tou_result.getResults().size() == 1) {
			summary.setLatestTerms(tou_result.getResults().get(0));
			for (TermsOfUseAgreementPojo toua : toua_result.getResults()) {
				summary.getTermsOfUseAgreements().add(toua);
				if (toua.getTermsOfUseId().equals(summary.getLatestTerms().getTermsOfUseId())) {
					summary.setHasValidTermsOfUseAgreement(true);
				}
			}
		}
		else {
			// TODO: error, unexpected number of effective TermsOfUse objects.
		}
		return summary;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AccountProvisioningAuthorizationQueryResultPojo getAccountProvisioningAuthorizationsForFilter(
			AccountProvisioningAuthorizationQueryFilterPojo filter) throws RpcException {

		AccountProvisioningAuthorizationQueryResultPojo result = new AccountProvisioningAuthorizationQueryResultPojo();
		result.setFilterUsed(filter);
		List<AccountProvisioningAuthorizationPojo> pojos = new java.util.ArrayList<AccountProvisioningAuthorizationPojo>();
		try {
			AccountProvisioningAuthorizationQuerySpecification queryObject = (AccountProvisioningAuthorizationQuerySpecification) getObject(Constants.MOA_ACCOUNT_PROVISIONING_AUTHORIZATION_QUERY_SPECIFICATION);
			AccountProvisioningAuthorization actionable = (AccountProvisioningAuthorization) getObject(Constants.MOA_ACCOUNT_PROVISIONING_AUTHORIZATION);

			if (filter != null) {
				queryObject.setUserId(filter.getUserId());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getAccountProvisioningAuthorizationsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());

			List<AccountProvisioningAuthorization> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getAccountProvisioningAuthorizationsForFilter] got " + moas.size() + 
					" Provisioing Authorization objects from ESB service" + 
					(filter != null ? " for filter: " + filter.toString() : ""));
			for (AccountProvisioningAuthorization moa : moas) {
				info("AccountProvisioningAuthorization: " + moa.toXmlString());
				AccountProvisioningAuthorizationPojo pojo = new AccountProvisioningAuthorizationPojo();
				pojo.setUserId(moa.getUserId());
				pojo.setAuthorized(this.toBooleanFromString(moa.getIsAuthorized()));
				pojo.setAuthorizedUserDescription(moa.getAuthorizedUserDescription());
				for (String desc : (List<String>)moa.getUnauthorizedReason()) {
					pojo.getUnauthorizedReasons().add(desc);
				}
				pojos.add(pojo);
			}

//			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@Override
	public PersonInfoSummaryPojo getPersonInfoSummaryForPublicId(String publicId) throws RpcException {
		PersonInfoSummaryPojo summary = new PersonInfoSummaryPojo();
		
		FullPersonQueryFilterPojo fp_filter = new FullPersonQueryFilterPojo();
		fp_filter.setPublicId(publicId);
		FullPersonQueryResultPojo fp_result = this.getFullPersonsForFilter(fp_filter);
		if (fp_result.getResults().size() >= 0) {
			summary.setFullPerson(fp_result.getResults().get(0));
		}
		
		AccountProvisioningAuthorizationQueryFilterPojo apa_filter = new AccountProvisioningAuthorizationQueryFilterPojo();
		apa_filter.setUserId(publicId);
		AccountProvisioningAuthorizationQueryResultPojo apa_result = this.getAccountProvisioningAuthorizationsForFilter(apa_filter);
		if (apa_result.getResults().size() >= 0) {
			summary.setAccountProvisioningAuthorization(apa_result.getResults().get(0));
		}
		
		return summary;
	}

	@Override
	public IncidentQueryResultPojo getIncidentsForFilter(IncidentQueryFilterPojo filter) throws RpcException {
		IncidentQueryResultPojo result = new IncidentQueryResultPojo();
		List<IncidentPojo> pojos = new java.util.ArrayList<IncidentPojo>();
		try {
			IncidentQuerySpecification queryObject = (IncidentQuerySpecification) getObject(Constants.MOA_INCIDENT_QUERY_SPEC);
			Incident actionable = (Incident) getObject(Constants.MOA_INCIDENT);

			if (filter != null) {
				queryObject.setNumber(filter.getNumber());
				queryObject.setAssignmentGroup(filter.getAssignmentGroup());
				queryObject.setIncidentAssignmentGroup(filter.getIncidentAssignedTo());
				queryObject.setNocAlarmId(filter.getNocAlarmId());
				queryObject.setCallerId(filter.getCallerId());
				queryObject.setCategory(filter.getCategory());
				queryObject.setSubCategory(filter.getSubCategory());
				queryObject.setRecordType(filter.getRecordType());
				queryObject.setCmdbCi(filter.getCmdbCi());
				queryObject.setIncidentState(filter.getIncidentState());
				if (filter.getConfigurationItem() != null) {
					ConfigurationItem ci = queryObject.newConfigurationItem();
					ci.setAmcomEventId(filter.getConfigurationItem().getAmcomEventId());
					ci.setName(filter.getConfigurationItem().getName());
					ci.setServiceOwnerEventId(filter.getConfigurationItem().getServiceOwnerEventId());
					if (filter.getConfigurationItem().getServiceOwner() != null) {
						ServiceOwner so = ci.newServiceOwner();
						so.setPublicId(filter.getConfigurationItem().getServiceOwner().getPublicId());
						if (filter.getConfigurationItem().getServiceOwner().getPersonalName() != null) {
							PersonalName pn = so.newPersonalName();
							pn.setFirstName(filter.getConfigurationItem().getServiceOwner().getPersonalName().getFirstName());
							pn.setLastName(filter.getConfigurationItem().getServiceOwner().getPersonalName().getLastName());
							pn.setMiddleName(filter.getConfigurationItem().getServiceOwner().getPersonalName().getMiddleName());
							so.setPersonalName(pn);
						}
						ci.setServiceOwner(so);
					}
					if (filter.getConfigurationItem().getSupportGroup() != null) {
						SupportGroup sg = ci.newSupportGroup();
						sg.setName(filter.getConfigurationItem().getSupportGroup().getName());
						ci.setSupportGroup(sg);
					}
				}
				info("[getIncidentssForFilter] getting Incidents for filter: " + queryObject.toXmlString());
			}
			else {
				info("[getIncidentssForFilter] no filter passed in.  Getting all Incidents");
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getIncidentssForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			RequestService reqSvc = this.getServiceNowRequestService();

			@SuppressWarnings("unchecked")
			List<Incident> moas = 
				actionable.query(queryObject, reqSvc);
			
			info("[getIncidentssForFilter] got " + moas.size() + " Incidents back from the server.");
			for (Incident moa : moas) {
				IncidentPojo pojo = new IncidentPojo();
				IncidentPojo baseline = new IncidentPojo();
				this.populateIncidentPojo(moa, pojo);
				this.populateIncidentPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@SuppressWarnings("unchecked")
	private void populateIncidentPojo(Incident moa, IncidentPojo pojo) throws XmlEnterpriseObjectException {
		/*
		<!ELEMENT Incident (Number?, ConfigurationItem?, AssignmentGroup?, AssignedTo?, NocAlarmId?, 
			ShortDescription?, Description?, WorkNotes*, Urgency?, Priority?, Impact?, BusinessService?, 
			Category?, SubCategory?, RecordType?, ContactType?, CallerId?, FunctionalEscalation?,CmdbCi?,
			SystemId?,IncidentState?)>
			 */
		pojo.setNumber(moa.getNumber());
		if (moa.getConfigurationItem() != null) {
			ConfigurationItemPojo cip = new ConfigurationItemPojo();
			cip.setAmcomEventId(moa.getConfigurationItem().getAmcomEventId());
			cip.setName(moa.getConfigurationItem().getName());
			cip.setServiceOwnerEventId(moa.getConfigurationItem().getServiceOwnerEventId());
			if (moa.getConfigurationItem().getServiceOwner() != null) {
				ServiceOwnerPojo sop = new ServiceOwnerPojo();
				sop.setPublicId(moa.getConfigurationItem().getServiceOwner().getPublicId());
				if (moa.getConfigurationItem().getServiceOwner().getPersonalName() != null) {
					PersonalNamePojo snp = new PersonalNamePojo();
					snp.setFirstName(moa.getConfigurationItem().getServiceOwner().getPersonalName().getFirstName());
					snp.setLastName(moa.getConfigurationItem().getServiceOwner().getPersonalName().getLastName());
					snp.setMiddleName(moa.getConfigurationItem().getServiceOwner().getPersonalName().getMiddleName());
					sop.setPersonalName(snp);
				}
				cip.setServiceOwner(sop);
			}
		}
		pojo.setAssignmentGroup(moa.getAssignmentGroup());
		if (moa.getAssignedTo() != null) {
			AssignedToPojo atp = new AssignedToPojo();
			atp.setPublicId(moa.getAssignedTo().getPublicId());
			if (moa.getAssignedTo().getPersonalName() != null) {
				PersonalNamePojo snp = new PersonalNamePojo();
				snp.setFirstName(moa.getAssignedTo().getPersonalName().getFirstName());
				snp.setLastName(moa.getAssignedTo().getPersonalName().getLastName());
				snp.setMiddleName(moa.getAssignedTo().getPersonalName().getMiddleName());
				atp.setPersonalName(snp);
			}
			pojo.setAssignedTo(atp);
		}
		pojo.setNocAlarmId(moa.getNocAlarmId());
		pojo.setShortDescription(moa.getShortDescription());
		pojo.setDescription(moa.getDescription());
		if (moa.getWorkNotes() != null && moa.getWorkNotesLength() > 0) {
			for (String note : (List<String>) moa.getWorkNotes()) {
				pojo.getWorkNotes().add(note);
			}
		}
		pojo.setUrgency(moa.getUrgency());
		pojo.setPriority(moa.getPriority());
		pojo.setImpact(moa.getImpact());
		pojo.setBusinessService(moa.getBusinessService());
		pojo.setCategory(moa.getCategory());
		pojo.setSubCategory(moa.getSubCategory());
		pojo.setRecordType(moa.getRecordType());
		pojo.setContactType(moa.getContactType());
		pojo.setCallerId(moa.getCallerId());
		pojo.setFunctionalEscalation(moa.getFunctionalEscalation());
		pojo.setCmdbCi(moa.getCmdbCi());
		pojo.setSystemId(moa.getSystemId());
		pojo.setIncidentState(moa.getIncidentState());
		
//		this.setPojoCreateInfo(pojo, moa);
//		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public void deleteIncident(IncidentPojo incident) throws RpcException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IncidentPojo generateIncident(IncidentRequisitionPojo incidentRequisition) throws RpcException {
		try {
			info("generating Incident on the server...");
			Incident actionable = (Incident) getObject(Constants.MOA_INCIDENT);
			IncidentRequisition seed = (IncidentRequisition) getObject(Constants.MOA_INCIDENT_REQUISITION);
			info("populating moa");
			this.populateIncidentRequisitionMoa(incidentRequisition, seed);

			
			info("doing the Incident.generate...");
			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("Incident.generate seed data is: " + seed.toXmlString());
			@SuppressWarnings("unchecked")
			List<Incident> result = actionable.generate(seed, this.getServiceNowRequestService());
			// TODO if more than one returned, it's an error...
			IncidentPojo pojo = new IncidentPojo();
			for (Incident moa : result) {
				info("generated Incident is: " + moa.toXmlString());
				this.populateIncidentPojo(moa, pojo);
			}
			info("Incident.generate is complete...");

			return pojo;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectGenerateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	private void populateIncidentRequisitionMoa(IncidentRequisitionPojo pojo, IncidentRequisition moa) throws EnterpriseFieldException {
		if (pojo.getConfigurationItem() != null) {
			ConfigurationItem cip = moa.newConfigurationItem();
			cip.setAmcomEventId(pojo.getConfigurationItem().getAmcomEventId());
			cip.setName(pojo.getConfigurationItem().getName());
			cip.setServiceOwnerEventId(pojo.getConfigurationItem().getServiceOwnerEventId());
			if (pojo.getConfigurationItem().getServiceOwner() != null) {
				ServiceOwner sop = cip.newServiceOwner();
				sop.setPublicId(pojo.getConfigurationItem().getServiceOwner().getPublicId());
				if (pojo.getConfigurationItem().getServiceOwner().getPersonalName() != null) {
					PersonalName snp = sop.newPersonalName();
					snp.setFirstName(pojo.getConfigurationItem().getServiceOwner().getPersonalName().getFirstName());
					snp.setLastName(pojo.getConfigurationItem().getServiceOwner().getPersonalName().getLastName());
					snp.setMiddleName(pojo.getConfigurationItem().getServiceOwner().getPersonalName().getMiddleName());
					sop.setPersonalName(snp);
				}
				cip.setServiceOwner(sop);
			}
		}
		moa.setAssignmentGroup(pojo.getAssignmentGroup());
		if (pojo.getAssignedTo() != null) {
			AssignedTo atp = moa.newAssignedTo();
			atp.setPublicId(pojo.getAssignedTo().getPublicId());
			if (pojo.getAssignedTo().getPersonalName() != null) {
				PersonalName snp = atp.newPersonalName();
				snp.setFirstName(pojo.getAssignedTo().getPersonalName().getFirstName());
				snp.setLastName(pojo.getAssignedTo().getPersonalName().getLastName());
				snp.setMiddleName(pojo.getAssignedTo().getPersonalName().getMiddleName());
				atp.setPersonalName(snp);
			}
			moa.setAssignedTo(atp);
		}
		moa.setShortDescription(pojo.getShortDescription());
		moa.setDescription(pojo.getDescription());
//		if (pojo.getWorkNotes() != null && pojo.getWorkNotes().size() > 0) {
//			for (String note : (List<String>) pojo.getWorkNotes()) {
//				moa.addWorkNotes(note);
//			}
//		}
		moa.setUrgency(pojo.getUrgency());
		moa.setImpact(pojo.getImpact());
		moa.setBusinessService(pojo.getBusinessService());
		moa.setCategory(pojo.getCategory());
		moa.setSubCategory(pojo.getSubCategory());
		moa.setRecordType(pojo.getRecordType());
		moa.setContactType(pojo.getContactType());
		moa.setCallerId(pojo.getCallerId());
		moa.setFunctionalEscalation(pojo.getFunctionalEscalation());
		moa.setCmdbCi(pojo.getCmdbCi());
		
//		this.setPojoCreateInfo(pojo, moa);
//		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public IncidentPojo updateIncident(IncidentPojo indident) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StaticNatProvisioningSummaryQueryResultPojo getStaticNatProvisioningSummariesForFilter(
			StaticNatProvisioningSummaryQueryFilterPojo filter) throws RpcException {
		
		StaticNatProvisioningSummaryQueryResultPojo result = new StaticNatProvisioningSummaryQueryResultPojo();
		if (filter != null) {
			result.setProvisionedFilterUsed(filter.getProvisionedFilter());
			result.setDeProvisionedFilterUsed(filter.getDeProvisionedFilter());
		}

		List<StaticNatProvisioningSummaryPojo> pojos = new java.util.ArrayList<StaticNatProvisioningSummaryPojo>();
		
		StaticNatProvisioningQueryResultPojo p_result = 
			this.getStaticNatProvisioningsForFilter(filter == null ? null : filter.getProvisionedFilter());
		for (StaticNatProvisioningPojo snp : p_result.getResults()) {
			StaticNatProvisioningSummaryPojo snps = new StaticNatProvisioningSummaryPojo();
			snps.setProvisioned(snp);
			pojos.add(snps);
		}
		
		StaticNatDeprovisioningQueryResultPojo dp_result = 
			this.getStaticNatDeprovisioningsForFilter(filter == null ? null : filter.getDeProvisionedFilter());
		for (StaticNatDeprovisioningPojo snd : dp_result.getResults()) {
			StaticNatProvisioningSummaryPojo snps = new StaticNatProvisioningSummaryPojo();
			snps.setDeprovisioned(snd);
			pojos.add(snps);
		}
		
		Collections.sort(pojos);
		result.setResults(pojos);
		return result;
	}

	@Override
	public StaticNatProvisioningQueryResultPojo getStaticNatProvisioningsForFilter(
			StaticNatProvisioningQueryFilterPojo filter) throws RpcException {

		StaticNatProvisioningQueryResultPojo result = new StaticNatProvisioningQueryResultPojo();
		List<StaticNatProvisioningPojo> pojos = new java.util.ArrayList<StaticNatProvisioningPojo>();
		try {
			StaticNatProvisioningQuerySpecification queryObject = (StaticNatProvisioningQuerySpecification) getObject(Constants.MOA_STATIC_NAT_PROVISIONING_QUERY_SPEC);
			StaticNatProvisioning actionable = (StaticNatProvisioning) getObject(Constants.MOA_STATIC_NAT_PROVISIONING);

			if (filter != null) {
				queryObject.setProvisioningId(filter.getProvisioningId());
				queryObject.setType(filter.getType());
				queryObject.setCreateUser(filter.getCreateUser());
				queryObject.setLastUpdateUser(filter.getUpdateUser());
				info("[getStaticNatProvisioningsForFilter] getting items for filter: " + queryObject.toXmlString());
			}
			else {
				info("[getStaticNatProvisioningsForFilter] no filter passed in.  Getting all StaticNat Provisioning objects");
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getStaticNatProvisioningsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String s_interval = generalProps.getProperty("staticNatProvisioningListTimeoutMillis", "300000");
			int interval = Integer.parseInt(s_interval);

			RequestService reqSvc = this.getNetworkOpsRequestService();
			info("setting RequestService's timeout to: " + interval + " milliseconds");
			((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(interval);

			@SuppressWarnings("unchecked")
			List<StaticNatProvisioning> moas = 
				actionable.query(queryObject, reqSvc);
			
			info("[getStaticNatProvisioningsForFilter] got " + moas.size() + " Static Nat Provisioning objects back from the server.");
			for (StaticNatProvisioning moa : moas) {
				StaticNatProvisioningPojo pojo = new StaticNatProvisioningPojo();
				this.populateStaticNatProvisioningPojo(moa, pojo);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@SuppressWarnings("unchecked")
	private void populateStaticNatProvisioningPojo(StaticNatProvisioning moa, StaticNatProvisioningPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setProvisioningId(moa.getProvisioningId());
		pojo.setStatus(moa.getStatus());
		pojo.setProvisioningResult(moa.getProvisioningResult());
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getStaticNat() != null) {
			StaticNatPojo snp = new StaticNatPojo();
			snp.setPrivateIp(moa.getStaticNat().getPrivateIp());
			snp.setPublicIp(moa.getStaticNat().getPublicIp());
			pojo.setStaticNat(snp);
		}

		// provisioningsteps
		List<ProvisioningStepPojo> pspList = new java.util.ArrayList<ProvisioningStepPojo>();
		for (edu.emory.moa.objects.resources.v1_0.ProvisioningStep ps : (List<edu.emory.moa.objects.resources.v1_0.ProvisioningStep>) moa.getProvisioningStep()) {
			ProvisioningStepPojo psp = new ProvisioningStepPojo();
			this.populateProvisioningStepPojoFromEmoryMoa(ps, psp);
			pspList.add(psp);
		}
		Collections.sort(pspList);
		pojo.setProvisioningSteps(pspList);

		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@SuppressWarnings("unchecked")
	private void populateProvisioningStepPojoFromEmoryMoa(edu.emory.moa.objects.resources.v1_0.ProvisioningStep moa,
			ProvisioningStepPojo pojo) {
		
		pojo.setProvisioningId(moa.getProvisioningId());
		pojo.setProvisioningStepId(moa.getProvisioningStepId());
		pojo.setStepId(moa.getStepId());
		pojo.setType(moa.getType());
		pojo.setDescription(moa.getDescription());
		pojo.setStatus(moa.getStatus());
		pojo.setStepResult(moa.getStepResult());
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getProperty() != null) {
			for (edu.emory.moa.objects.resources.v1_0.Property stepProps : (List<edu.emory.moa.objects.resources.v1_0.Property>) moa.getProperty()) {
				pojo.getProperties().put(stepProps.getPropertyName(), stepProps.getPropertyValue());
			}
		}
	}

	@Override
	public StaticNatDeprovisioningQueryResultPojo getStaticNatDeprovisioningsForFilter(
			StaticNatDeprovisioningQueryFilterPojo filter) throws RpcException {

		StaticNatDeprovisioningQueryResultPojo result = new StaticNatDeprovisioningQueryResultPojo();
		List<StaticNatDeprovisioningPojo> pojos = new java.util.ArrayList<StaticNatDeprovisioningPojo>();
		try {
			StaticNatDeprovisioningQuerySpecification queryObject = (StaticNatDeprovisioningQuerySpecification) getObject(Constants.MOA_STATIC_NAT_DEPROVISIONING_QUERY_SPEC);
			StaticNatDeprovisioning actionable = (StaticNatDeprovisioning) getObject(Constants.MOA_STATIC_NAT_DEPROVISIONING);

			if (filter != null) {
				queryObject.setProvisioningId(filter.getProvisioningId());
				queryObject.setType(filter.getType());
				queryObject.setCreateUser(filter.getCreateUser());
				queryObject.setLastUpdateUser(filter.getUpdateUser());
				info("[getStaticNatDeprovisioningsForFilter] getting items for filter: " + queryObject.toXmlString());
			}
			else {
				info("[getStaticNatDeprovisioningsForFilter] no filter passed in.  Getting all StaticNat Provisioning objects");
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getStaticNatDeprovisioningsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String s_interval = generalProps.getProperty("staticNatProvisioningListTimeoutMillis", "300000");
			int interval = Integer.parseInt(s_interval);

			RequestService reqSvc = this.getNetworkOpsRequestService();
			info("setting RequestService's timeout to: " + interval + " milliseconds");
			((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(interval);

			@SuppressWarnings("unchecked")
			List<StaticNatDeprovisioning> moas = 
				actionable.query(queryObject, reqSvc);
			
			info("[getStaticNatDeprovisioningsForFilter] got " + moas.size() + " Static Nat Provisioning objects back from the server.");
			for (StaticNatDeprovisioning moa : moas) {
				StaticNatDeprovisioningPojo pojo = new StaticNatDeprovisioningPojo();
				this.populateStaticNatDeprovisioningPojo(moa, pojo);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@SuppressWarnings("unchecked")
	private void populateStaticNatDeprovisioningPojo(StaticNatDeprovisioning moa, StaticNatDeprovisioningPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setProvisioningId(moa.getProvisioningId());
		pojo.setStatus(moa.getStatus());
		pojo.setProvisioningResult(moa.getProvisioningResult());
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getStaticNat() != null) {
			StaticNatPojo snp = new StaticNatPojo();
			snp.setPrivateIp(moa.getStaticNat().getPrivateIp());
			snp.setPublicIp(moa.getStaticNat().getPublicIp());
			pojo.setStaticNat(snp);
		}

		// provisioningsteps
		List<ProvisioningStepPojo> pspList = new java.util.ArrayList<ProvisioningStepPojo>();
		for (edu.emory.moa.objects.resources.v1_0.ProvisioningStep ps : (List<edu.emory.moa.objects.resources.v1_0.ProvisioningStep>) moa.getProvisioningStep()) {
			ProvisioningStepPojo psp = new ProvisioningStepPojo();
			this.populateProvisioningStepPojoFromEmoryMoa(ps, psp);
			pspList.add(psp);
		}
		Collections.sort(pspList);
		pojo.setProvisioningSteps(pspList);

		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@SuppressWarnings("unchecked")
	@Override
	public VpnConnectionProfileQueryResultPojo getVpnConnectionProfilesForFilter(
			VpnConnectionProfileQueryFilterPojo filter) throws RpcException {
		
		VpnConnectionProfileQueryResultPojo result = new VpnConnectionProfileQueryResultPojo();
		List<VpnConnectionProfileSummaryPojo> summaries = new java.util.ArrayList<VpnConnectionProfileSummaryPojo>();
		
		// get ALL VpnConnectionProfileAssignments and go through them in memory
		// instead of doing an individual query by profile id.
		// this speeds things up significantly but could potentially lead to other resource issues down the road
		VpnConnectionProfileAssignmentQueryFilterPojo eia_filter = new VpnConnectionProfileAssignmentQueryFilterPojo();
		VpnConnectionProfileAssignmentQueryResultPojo eia_result = this.getVpnConnectionProfileAssignmentsForFilter(eia_filter);
		
		// check the cache for cached VpnConnectionProfiles (by current session id)
		// if they've already been cached, just get them from there
		// and pull back any assignments for those
		List<VpnConnectionProfilePojo> cached_profiles = (List<VpnConnectionProfilePojo>) Cache.getCache().get(
				Constants.VPN_CONNECTION_PROFILES + getCurrentSessionId());
		if (cached_profiles != null && cached_profiles.size() > 0) {
			info("[getVpnConnectionProfilesForFilter] using cached VpnConnectionProfile objects");
			for (VpnConnectionProfilePojo profile : cached_profiles) {
				VpnConnectionProfileSummaryPojo summary = new VpnConnectionProfileSummaryPojo();
				summary.setProfile(profile);
				assignmentLoop: for (VpnConnectionProfileAssignmentPojo assignment : eia_result.getResults()) {
					if (assignment.getVpnConnectionProfileId().equals(profile.getVpnConnectionProfileId())) {
						profile.setAssigned(true);
						// go through the tunnel profiles in the profile and remove the "(AVAILABLE)" string?
						for (TunnelProfilePojo tunnel : profile.getTunnelProfiles()) {
							String newDesc = tunnel.getTunnelDescription().replaceAll(Constants.TUNNEL_AVAILABLE, assignment.getOwnerId());
							tunnel.setTunnelDescription(newDesc);
						}
						summary.setAssignment(assignment);
						// TODO: remove assignment from eia_result.getResults()??
						break assignmentLoop;
					}
				}
				summaries.add(summary);
			}
			Collections.sort(summaries);
			result.setResults(summaries);
			result.setFilterUsed(filter);
			return result;
		}
		else {
			info("[getVpnConnectionProfilesForFilter] getting VpnConnectionProfile objects from the service");
		}
		
		try {
			VpnConnectionProfileQuerySpecification queryObject = (VpnConnectionProfileQuerySpecification) getObject(Constants.MOA_VPN_CONNECTION_PROFILE_QUERY_SPEC);
			VpnConnectionProfile actionable = (VpnConnectionProfile) getObject(Constants.MOA_VPN_CONNECTION_PROFILE);

			if (filter != null) {
				// TODO: query language probably

				queryObject.setVpnConnectionProfileId(filter.getVpnConnectionProfileId());
				queryObject.setVpcNetwork(filter.getVpcNetwork());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getVpnConnectionProfilesForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			List<VpnConnectionProfile> moas = actionable.query(queryObject,
					this.getNetworkOpsRequestService());
			info("[getVpnConnectionProfilesForFilter] got " + moas.size() + " VPN COnnection profiles back from the ESB.");
			
			List<VpnConnectionProfilePojo> profiles_to_cache = new java.util.ArrayList<VpnConnectionProfilePojo>();
			
			for (VpnConnectionProfile moa : moas) {
				VpnConnectionProfileSummaryPojo summary = new VpnConnectionProfileSummaryPojo();
				
				VpnConnectionProfilePojo pojo = new VpnConnectionProfilePojo();
				VpnConnectionProfilePojo baseline = new VpnConnectionProfilePojo();
				this.populateVpnConnectionProfilePojo(moa, pojo);
				this.populateVpnConnectionProfilePojo(moa, baseline);
				pojo.setBaseline(baseline);
				summary.setProfile(pojo);
				profiles_to_cache.add(pojo);
				
				assignmentLoop: for (VpnConnectionProfileAssignmentPojo assignment : eia_result.getResults()) {
					if (assignment.getVpnConnectionProfileId().equals(pojo.getVpnConnectionProfileId())) {
						summary.setAssignment(assignment);
						// TODO: remove assignment from eia_result.getResults()??
						break assignmentLoop;
					}
				}
				summaries.add(summary);
			}
			// cache the profiles for later since they don't change often
			// they'll be cached for this session only
			Cache.getCache().put(Constants.VPN_CONNECTION_PROFILES + getCurrentSessionId(), profiles_to_cache);

			Collections.sort(summaries);
			result.setResults(summaries);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@SuppressWarnings("unchecked")
	private void populateVpnConnectionProfilePojo(VpnConnectionProfile moa, VpnConnectionProfilePojo pojo) throws XmlEnterpriseObjectException {
		pojo.setVpnConnectionProfileId(moa.getVpnConnectionProfileId());
		pojo.setVpcNetwork(moa.getVpcNetwork());
		for (TunnelProfile tp_moa : (List<TunnelProfile>) moa.getTunnelProfile()) {
			TunnelProfilePojo tp_pojo = new TunnelProfilePojo();
			tp_pojo.setTunnelId(tp_moa.getTunnelId());
			tp_pojo.setCryptoKeyringName(tp_moa.getCryptoKeyringName());
			tp_pojo.setIsakampProfileName(tp_moa.getIsakampProfileName());
			tp_pojo.setIpsecProfileName(tp_moa.getIpsecProfileName());
			tp_pojo.setIpsecTransformSetName(tp_moa.getIpsecTransformSetName());
			tp_pojo.setTunnelDescription(tp_moa.getTunnelDescription());
			tp_pojo.setCustomerGatewayIp(tp_moa.getCustomerGatewayIp());
			tp_pojo.setVpnInsideIpCidr1(tp_moa.getVpnInsideIpCidr1());
			tp_pojo.setVpnInsideIpCidr2(tp_moa.getVpnInsideIpCidr2());
			
			pojo.getTunnelProfiles().add(tp_pojo);
		}
		
//		this.setPojoCreateInfo(pojo, moa);
//		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public VpnConnectionProfilePojo createVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile)
			throws RpcException {
		vpnConnectionProfile.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());

		try {
			info("creating VpnConnectionProfile record on the server...");
			VpnConnectionProfile moa = (VpnConnectionProfile) getObject(Constants.MOA_VPN_CONNECTION_PROFILE);
			info("populating moa");
			this.populateVpnConnectionProfileMoa(vpnConnectionProfile, moa);
			info("Creating VpnConnectionProfile: " + moa.toXmlString());
			
			info("doing the VpnConnectionProfile.create...");
			this.doCreate(moa, getNetworkOpsRequestService());
			info("VpnConnectionProfile.create is complete...");

			return vpnConnectionProfile;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectCreateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	private void populateVpnConnectionProfileMoa(VpnConnectionProfilePojo pojo,
			VpnConnectionProfile moa) throws EnterpriseFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		
		moa.setVpnConnectionProfileId(pojo.getVpnConnectionProfileId());
		moa.setVpcNetwork(pojo.getVpcNetwork());
		for (TunnelProfilePojo tp_pojo : pojo.getTunnelProfiles()) {
			TunnelProfile tp_moa = moa.newTunnelProfile();
			tp_moa.setTunnelId(tp_pojo.getTunnelId());
			tp_moa.setCryptoKeyringName(tp_pojo.getCryptoKeyringName());
			tp_moa.setIsakampProfileName(tp_pojo.getIsakampProfileName());
			tp_moa.setIpsecProfileName(tp_pojo.getIpsecProfileName());
			tp_moa.setIpsecTransformSetName(tp_pojo.getIpsecTransformSetName());
			tp_moa.setTunnelDescription(tp_pojo.getTunnelDescription());
			tp_moa.setCustomerGatewayIp(tp_pojo.getCustomerGatewayIp());
			tp_moa.setVpnInsideIpCidr1(tp_pojo.getVpnInsideIpCidr1());
			tp_moa.setVpnInsideIpCidr2(tp_pojo.getVpnInsideIpCidr2());
			
			moa.addTunnelProfile(tp_moa);
		}
	}

	@Override
	public VpnConnectionProfilePojo deleteVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile) throws RpcException {
		try {
			// TODO: need to see if there are any assignments to this profile and if so, delete those too
			info("deleting VpnConnectionProfile record on the server...");
			VpnConnectionProfile moa = (VpnConnectionProfile) getObject(Constants.MOA_VPN_CONNECTION_PROFILE);
			info("populating moa");
			this.populateVpnConnectionProfileMoa(vpnConnectionProfile, moa);

			
			info("doing the VpnConnectionProfilePojo.delete...");
			this.doDelete(moa, getNetworkOpsRequestService());
			info("VpnConnectionProfilePojo.delete is complete...");

			return vpnConnectionProfile;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseObjectDeleteException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public VpnConnectionProfilePojo updateVpnConnectionProfile(VpnConnectionProfilePojo vpnConnectionProfile)
			throws RpcException {

        try {
            info("updating VpnConnectionProfile on the server...");
            VpnConnectionProfile newData = (VpnConnectionProfile) getObject(Constants.MOA_VPN_CONNECTION_PROFILE);
            VpnConnectionProfile baselineData = (VpnConnectionProfile) getObject(Constants.MOA_VPN_CONNECTION_PROFILE);

            info("populating newData...");
            populateVpnConnectionProfileMoa(vpnConnectionProfile, newData);

            info("populating baselineData...");
            populateVpnConnectionProfileMoa(vpnConnectionProfile.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the update...");
            doUpdate(newData, getNetworkOpsRequestService());
            info("update is complete...");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return vpnConnectionProfile;
	}

	@Override
	public VpnConnectionProfileAssignmentQueryResultPojo getVpnConnectionProfileAssignmentsForFilter(
			VpnConnectionProfileAssignmentQueryFilterPojo filter) throws RpcException {
		
		VpnConnectionProfileAssignmentQueryResultPojo result = new VpnConnectionProfileAssignmentQueryResultPojo();
		List<VpnConnectionProfileAssignmentPojo> pojos = new java.util.ArrayList<VpnConnectionProfileAssignmentPojo>();
		try {
			VpnConnectionProfileAssignmentQuerySpecification queryObject = (VpnConnectionProfileAssignmentQuerySpecification) getObject(Constants.MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT_QUERY_SPEC);
			VpnConnectionProfileAssignment actionable = (VpnConnectionProfileAssignment) getObject(Constants.MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT);

			if (filter != null) {
				// TODO: query language probably

				queryObject.setVpnConnectionProfileId(filter.getVpnConnectionProfileId());
				queryObject.setOwnerId(filter.getOwnerId());
				queryObject.setVpnConnectionProfileAssignmentId(filter.getVpnConnectionProfileAssignmentId());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getVpnConnectionProfileAssignmentsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<VpnConnectionProfileAssignment> moas = actionable.query(queryObject,
					this.getNetworkOpsRequestService());
			info("[getVpnConnectionProfileAssignmentsForFilter] got " + moas.size() + " VPN Connection profile assignments back from the ESB.");
			
			for (VpnConnectionProfileAssignment moa : moas) {
				VpnConnectionProfileAssignmentPojo pojo = new VpnConnectionProfileAssignmentPojo();
				VpnConnectionProfileAssignmentPojo baseline = new VpnConnectionProfileAssignmentPojo();
				this.populateVpnConnectionProfileAssignmentPojo(moa, pojo);
				this.populateVpnConnectionProfileAssignmentPojo(moa, baseline);
				pojo.setBaseline(baseline);
				
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	private void populateVpnConnectionProfileAssignmentMoa(VpnConnectionProfileAssignmentPojo pojo,
			VpnConnectionProfileAssignment moa) throws EnterpriseFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {

		moa.setVpnConnectionProfileAssignmentId(pojo.getVpnConnectionProfileAssignmentId());
		moa.setVpnConnectionProfileId(pojo.getVpnConnectionProfileId());
		moa.setOwnerId(pojo.getOwnerId());
		moa.setDescription(pojo.getDescription());
		moa.setPurpose(pojo.getPurpose());
		moa.setDeleteUser(pojo.getDeleteUser());
		if (pojo.getDeleteTime() != null) {
			Datetime dt = new Datetime("DeleteDatetime");
			populateDatetime(dt, pojo.getDeleteTime());
			moa.setDeleteDatetime(dt);
		}
		
		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}
	private void populateVpnConnectionProfileAssignmentPojo(VpnConnectionProfileAssignment moa,
			VpnConnectionProfileAssignmentPojo pojo) {

		pojo.setVpnConnectionProfileAssignmentId(moa.getVpnConnectionProfileAssignmentId());
		pojo.setVpnConnectionProfileId(moa.getVpnConnectionProfileId());
		pojo.setOwnerId(moa.getOwnerId());
		pojo.setDescription(moa.getDescription());
		pojo.setPurpose(moa.getPurpose());
		pojo.setDeleteUser(moa.getDeleteUser());
		if (moa.getDeleteDatetime() != null) {
			pojo.setDeleteTime(this.toDateFromDatetime(moa.getDeleteDatetime()));
		}
	}

	@Override
	public VpnConnectionProvisioningQueryResultPojo getVpncpSummariesForFilter(VpnConnectionProvisioningQueryFilterPojo filter)
			throws RpcException {

		VpnConnectionProvisioningQueryResultPojo result = new VpnConnectionProvisioningQueryResultPojo();
//		List<VpnConnectionProvisioningPojo> pojos = new java.util.ArrayList<VpnConnectionProvisioningPojo>();
		List<VpnConnectionProvisioningSummaryPojo> summaries = new java.util.ArrayList<VpnConnectionProvisioningSummaryPojo>();
		try {
			VpnConnectionProvisioningQuerySpecification queryObject = (VpnConnectionProvisioningQuerySpecification) getObject(Constants.MOA_VPNCP_QUERY_SPEC);
			VpnConnectionProvisioning actionable = (VpnConnectionProvisioning) getObject(Constants.MOA_VPNCP);

			if (filter != null) {
				if (filter.isDefaultMaxVpncps()) {
					info("[getVpncpSummariesForFilter] using 'maxVpncps' query language to get VPCPs");
//					QueryLanguage ql = queryObject.newQueryLanguage();
//					ql.setName("maxVpncps");
//					ql.setType("hql");
//					ql.setMax(this.toStringFromInt(filter.getMaxRows()));
//					queryObject.setQueryLanguage(ql);
				}
				else if (filter.isAllVpncps()) {
					info("[getVpncpSummariesForFilter] using 'allVpncps' query language to get VPCPs");
//					QueryLanguage ql = queryObject.newQueryLanguage();
//					ql.setName("allVpncps");
//					ql.setType("hql");
//					queryObject.setQueryLanguage(ql);
				}
				else {
					queryObject.setProvisioningId(filter.getProvisioningId());
					queryObject.setType(filter.getType());
					queryObject.setCreateUser(filter.getCreateUser());
					queryObject.setLastUpdateUser(filter.getUpdateUser());
					info("[getVpncpSummariesForFilter] getting VPNCPs for filter: " + queryObject.toXmlString());
				}
			}
			else {
				info("[getVpncpSummariesForFilter] no filter passed in.  Getting all VPNCPs");
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getVpncpSummariesForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			String s_interval = generalProps.getProperty("vpcpListTimeoutMillis", "30000");
			int interval = Integer.parseInt(s_interval);

			RequestService reqSvc = this.getNetworkOpsRequestService();
			info("setting RequestService's timeout to: " + interval + " milliseconds");
			((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(interval);

			info("VpnConnectionProvisioningQuerySpec: " + queryObject.toXmlString());
			@SuppressWarnings("unchecked")
			List<VpnConnectionProvisioning> moas = 
				actionable.query(queryObject, reqSvc);
			
			info("[getVpncpSummariesForFilter] got " + moas.size() + " VPNCPs back from the server.");
			for (VpnConnectionProvisioning moa : moas) {
				VpnConnectionProvisioningPojo pojo = new VpnConnectionProvisioningPojo();
				VpnConnectionProvisioningPojo baseline = new VpnConnectionProvisioningPojo();
				this.populateVpncpPojo(moa, pojo);
				this.populateVpncpPojo(moa, baseline);
				pojo.setBaseline(baseline);
				VpnConnectionProvisioningSummaryPojo summary = new VpnConnectionProvisioningSummaryPojo();
				summary.setProvisioning(pojo);
				summaries.add(summary);
//				pojos.add(pojo);
			}
			
			// TODO: now get the VPNCDPs (deprovisioning objects)
			VpnConnectionDeprovisioningQuerySpecification deprov_queryObject = (VpnConnectionDeprovisioningQuerySpecification) getObject(Constants.MOA_VPNC_DEPROVISIONING_QUERY_SPEC);
			VpnConnectionDeprovisioning deprov_actionable = (VpnConnectionDeprovisioning) getObject(Constants.MOA_VPNC_DEPROVISIONING);

			if (filter != null) {
				if (filter.isDefaultMaxVpncps()) {
					info("[getVpncpSummariesForFilter] using 'maxVpncps' query language to get VPCDPs");
				}
				else if (filter.isAllVpncps()) {
					info("[getVpncpSummariesForFilter] using 'allVpncps' query language to get VPCDPs");
				}
				else {
					deprov_queryObject.setProvisioningId(filter.getProvisioningId());
					deprov_queryObject.setType(filter.getType());
					deprov_queryObject.setCreateUser(filter.getCreateUser());
					deprov_queryObject.setLastUpdateUser(filter.getUpdateUser());
					info("[getVpncpSummariesForFilter] getting VPNCDPs for filter: " + deprov_queryObject.toXmlString());
				}
			}
			else {
				info("[getVpncpSummariesForFilter] no filter passed in.  Getting all VPNCDPs");
			}

			info("VpnConnectionDeprovisioningQuerySpec: " + deprov_queryObject.toXmlString());
			@SuppressWarnings("unchecked")
			List<VpnConnectionDeprovisioning> deprov_moas = 
				deprov_actionable.query(deprov_queryObject, reqSvc);
			
			info("[getVpncpSummariesForFilter] got " + deprov_moas.size() + " VPNCDPs back from the server.");
			for (VpnConnectionDeprovisioning moa : deprov_moas) {
				VpnConnectionDeprovisioningPojo pojo = new VpnConnectionDeprovisioningPojo();
				VpnConnectionDeprovisioningPojo baseline = new VpnConnectionDeprovisioningPojo();
				this.populateVpnDeprovisioningPojo(moa, pojo);
				this.populateVpnDeprovisioningPojo(moa, baseline);
				pojo.setBaseline(baseline);
				VpnConnectionProvisioningSummaryPojo summary = new VpnConnectionProvisioningSummaryPojo();
				summary.setDeprovisioning(pojo);
				summaries.add(summary);
			}

			Collections.sort(summaries);
			result.setResults(summaries);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@Override
	public VpnConnectionDeprovisioningPojo generateVpnConnectionDeprovisioning(VpnConnectionRequisitionPojo requisition)
			throws RpcException {
		
		try {
			info("generating VpnConnectionDeprovisioning on the server...");
			VpnConnectionDeprovisioning actionable = (VpnConnectionDeprovisioning) getObject(Constants.MOA_VPNC_DEPROVISIONING);
			VpnConnectionRequisition seed = (VpnConnectionRequisition) getObject(Constants.MOA_VPN_CONNECTION_REQUISITION);
			info("populating moa");
			this.populateVpncpRequisitionMoa(requisition, seed);

			
			info("doing the VpnConnectionDeprovisioning.generate...");
			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("VpnConnectionDeprovisioning.generate seed data is: " + seed.toXmlString());
			@SuppressWarnings("unchecked")
			List<VpnConnectionDeprovisioning> result = actionable.generate(seed, this.getNetworkOpsRequestService());
			// TODO if more than one returned, it's an error...
			VpnConnectionDeprovisioningPojo pojo = new VpnConnectionDeprovisioningPojo();
			for (VpnConnectionDeprovisioning moa : result) {
				info("generated VpnConnectionDeprovisioning is: " + moa.toXmlString());
				this.populateVpnDeprovisioningPojo(moa, pojo);
			}
			info("VpnConnectionDeprovisioning.generate is complete...");

			return pojo;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectGenerateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void populateVpnDeprovisioningPojo(VpnConnectionDeprovisioning moa, VpnConnectionDeprovisioningPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setProvisioningId(moa.getProvisioningId());
		pojo.setStatus(moa.getStatus());
		pojo.setProvisioningResult(moa.getProvisioningResult());
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getVpnConnectionRequisition() != null) {
			VpnConnectionRequisitionPojo vpcr = new VpnConnectionRequisitionPojo();
			this.populateVpncpRequisitionPojo(moa.getVpnConnectionRequisition(), vpcr);
			pojo.setRequisition(vpcr);
		}

		// provisioningsteps
		List<ProvisioningStepPojo> pspList = new java.util.ArrayList<ProvisioningStepPojo>();
		for (edu.emory.moa.objects.resources.v1_0.ProvisioningStep ps : (List<edu.emory.moa.objects.resources.v1_0.ProvisioningStep>) moa.getProvisioningStep()) {
			ProvisioningStepPojo psp = new ProvisioningStepPojo();
			this.populateProvisioningStepPojoFromEmoryMoa(ps, psp);
			pspList.add(psp);
		}
		Collections.sort(pspList);
		pojo.setProvisioningSteps(pspList);

		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	@Override
	public VpnConnectionProfileAssignmentPojo createVpnConnectionProfileAssignment(
			VpnConnectionProfileAssignmentPojo profileAssignment) throws RpcException {

		profileAssignment.setCreateInfo(this.getCachedUser().getPublicId(),
				new java.util.Date());
		try {
			info("creating VpnConnectionProfileAssignment on the server...");
			VpnConnectionProfileAssignment moa = (VpnConnectionProfileAssignment) getObject(Constants.MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT);
			info("populating moa");
			this.populateVpnConnectionProfileAssignmentMoa(profileAssignment, moa);
			info("Creating VpnConnectionProfileAssignment: " + moa.toXmlString());
			
			info("doing the VpnConnectionProfileAssignment.create...");
			this.doCreate(moa, getNetworkOpsRequestService());
			info("VpnConnectionProfileAssignment.create is complete...");

			return profileAssignment;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectCreateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public VpnConnectionProfileAssignmentPojo generateVpnConnectionProfileAssignment(
			VpnConnectionProfileAssignmentRequisitionPojo requisition) throws RpcException {

		try {
			info("generating VpnConnectionProfileAssignment on the server...");
			VpnConnectionProfileAssignment actionable = (VpnConnectionProfileAssignment) getObject(Constants.MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT_GENERATE);
			VpnConnectionProfileAssignmentRequisition seed = (VpnConnectionProfileAssignmentRequisition) getObject(Constants.MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT_REQUISITION);
			info("populating moa");
			seed.setOwnerId(requisition.getOwnerId());

			
			info("doing the VpnConnectionProfileAssignment.generate...");
			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("VpnConnectionProfileAssignment.generate seed data is: " + seed.toXmlString());
			@SuppressWarnings("unchecked")
			List<VpnConnectionProfileAssignment> result = actionable.generate(seed, this.getNetworkOpsRequestService());
			// TODO if more than one returned, it's an error...
			VpnConnectionProfileAssignmentPojo pojo = new VpnConnectionProfileAssignmentPojo();
			for (VpnConnectionProfileAssignment moa : result) {
				info("generated VpnConnectionProfileAssignment is: " + moa.toXmlString());
				this.populateVpnConnectionProfileAssignmentPojo(moa, pojo);
			}
			info("VpnConnectionProfileAssignment.generate is complete...");

			return pojo;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectGenerateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
	}

	@Override
	public VpnConnectionProfileAssignmentPojo deleteVpnConnectionProfileAssignment(
			VpnConnectionProfileAssignmentPojo vpnConnectionProfileAssignment) throws RpcException {
		
		try {
			info("deleting VpnConnectionProfileAssignment record on the server...");
			VpnConnectionProfileAssignment moa = (VpnConnectionProfileAssignment) getObject(Constants.MOA_VPN_CONNECTION_PROFILE_ASSIGNMENT);
			info("populating moa");
			this.populateVpnConnectionProfileAssignmentMoa(vpnConnectionProfileAssignment, moa);

			
			info("doing the VpnConnectionProfileAssignmentPojo.delete...");
			this.doDelete(moa, getNetworkOpsRequestService());
			info("VpnConnectionProfileAssignmentPojo.delete is complete...");

			return vpnConnectionProfileAssignment;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseObjectDeleteException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public VpnConnectionProvisioningPojo generateVpncp(VpnConnectionRequisitionPojo vpncpRequisition)
			throws RpcException {
		
		try {
			info("generating VpnConnectionProvisioning on the server...");
			VpnConnectionProvisioning actionable = (VpnConnectionProvisioning) getObject(Constants.MOA_VPNCP);
			VpnConnectionRequisition seed = (VpnConnectionRequisition) getObject(Constants.MOA_VPN_CONNECTION_REQUISITION);
			info("populating moa");
			this.populateVpncpRequisitionMoa(vpncpRequisition, seed);

			
			info("doing the VpnConnectionProvisioning.generate...");
			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("VpnConnectionProvisioning.generate seed data is: " + seed.toXmlString());
			@SuppressWarnings("unchecked")
			List<VpnConnectionProvisioning> result = actionable.generate(seed, this.getNetworkOpsRequestService());
			// TODO if more than one returned, it's an error...
			VpnConnectionProvisioningPojo pojo = new VpnConnectionProvisioningPojo();
			for (VpnConnectionProvisioning moa : result) {
				info("generated VpnConnectionProvisioning is: " + moa.toXmlString());
				this.populateVpncpPojo(moa, pojo);
			}
			info("VpnConnectionProvisioning.generate is complete...");

			return pojo;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectGenerateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void populateVpncpPojo(VpnConnectionProvisioning moa, VpnConnectionProvisioningPojo pojo) throws XmlEnterpriseObjectException {
		pojo.setProvisioningId(moa.getProvisioningId());
		pojo.setStatus(moa.getStatus());
		pojo.setProvisioningResult(moa.getProvisioningResult());
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getVpnConnectionRequisition() != null) {
			VpnConnectionRequisitionPojo vpcr = new VpnConnectionRequisitionPojo();
			this.populateVpncpRequisitionPojo(moa.getVpnConnectionRequisition(), vpcr);
			pojo.setRequisition(vpcr);
		}

		// provisioningsteps
		List<ProvisioningStepPojo> pspList = new java.util.ArrayList<ProvisioningStepPojo>();
		for (edu.emory.moa.objects.resources.v1_0.ProvisioningStep ps : (List<edu.emory.moa.objects.resources.v1_0.ProvisioningStep>) moa.getProvisioningStep()) {
			ProvisioningStepPojo psp = new ProvisioningStepPojo();
			this.populateProvisioningStepPojoFromEmoryMoa(ps, psp);
			pspList.add(psp);
		}
		Collections.sort(pspList);
		pojo.setProvisioningSteps(pspList);

		this.setPojoCreateInfo(pojo, moa);
		this.setPojoUpdateInfo(pojo, moa);
	}

	private void populateVpncpRequisitionPojo(VpnConnectionRequisition moa,
			VpnConnectionRequisitionPojo pojo) throws XmlEnterpriseObjectException {

		pojo.setOwnerId(moa.getOwnerId());
		pojo.setRemoteVpnIpAddress(moa.getRemoteVpnIpAddress());
		pojo.setPresharedKey(moa.getPresharedKey());
		if (moa.getVpnConnectionProfile() != null) {
			VpnConnectionProfilePojo vcp = new VpnConnectionProfilePojo();
			this.populateVpnConnectionProfilePojo(moa.getVpnConnectionProfile(), vcp);
			pojo.setProfile(vcp);
		}
	}

	private void populateVpncpRequisitionMoa(VpnConnectionRequisitionPojo pojo,
			VpnConnectionRequisition moa) throws XmlEnterpriseObjectException, EnterpriseFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {

		moa.setOwnerId(pojo.getOwnerId());
		moa.setRemoteVpnIpAddress(pojo.getRemoteVpnIpAddress());
		moa.setPresharedKey(pojo.getPresharedKey());
		if (pojo.getProfile() != null) {
			VpnConnectionProfile vcp = moa.newVpnConnectionProfile();
			this.populateVpnConnectionProfileMoa(pojo.getProfile(), vcp);
			moa.setVpnConnectionProfile(vcp);
		}
	}

	@Override
	public FirewallExceptionAddRequestQueryResultPojo getFirewallExceptionAddRequestsForFilter(
			FirewallExceptionAddRequestQueryFilterPojo filter) throws RpcException {

		FirewallExceptionAddRequestQueryResultPojo result = new FirewallExceptionAddRequestQueryResultPojo();
		List<FirewallExceptionAddRequestPojo> pojos = new java.util.ArrayList<FirewallExceptionAddRequestPojo>();
		try {
			FirewallExceptionAddRequestQuerySpecification queryObject = (FirewallExceptionAddRequestQuerySpecification) getObject(Constants.MOA_FIREWALL_EXCEPTION_ADD_REQUEST_QUERY_SPEC);
			FirewallExceptionAddRequest actionable = (FirewallExceptionAddRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_ADD_REQUEST);

			if (filter != null) {
				queryObject.setSystemId(filter.getSystemId());
				queryObject.setUserNetID(filter.getUserNetId());
				queryObject.setApplicationName(filter.getApplicationName());
				queryObject.setSourceOutsideEmory(filter.getIsSourceOutsideEmory());
				queryObject.setTimeRule(filter.getTimeRule());
				if (filter.getValidUntilDate() != null) {
					org.openeai.moa.objects.resources.Date validUntilDate = queryObject.newValidUntilDate();
					this.populateDate(validUntilDate, filter.getValidUntilDate());
					queryObject.setValidUntilDate(validUntilDate);
				}
				queryObject.setSourceIpAddresses(filter.getSourceIp());
				queryObject.setDestinationIpAddresses(filter.getDestinationIp());
				queryObject.setPorts(filter.getPorts());
				queryObject.setBusinessReason(filter.getBusinessReason());
				queryObject.setPatched(filter.getIsPatched());
				queryObject.setDefaultPasswdChanged(filter.getIsDefaultPasswdChanged());
				queryObject.setAppConsoleACLed(filter.getIsAppConsoleACLed());
				queryObject.setHardened(filter.getIsHardened());
				queryObject.setPatchingPlan(filter.getPatchingPlan());
				if (filter.getCompliance().size() > 0) {
					for (String compliance : filter.getCompliance()) {
						queryObject.addCompliance(compliance);
					}
				}
				queryObject.setOtherCompliance(filter.getOtherCompliance());
				queryObject.setSensitiveDataDesc(filter.getSensitiveDataDesc());
				queryObject.setLocalFirewallRules(filter.getLocalFirewallRules());
				queryObject.setDefaultDenyZone(filter.getIsDefaultDenyZone());
				for (String tag : filter.getTags()) {
					queryObject.addTag(tag);
				}
				queryObject.setRequestState(filter.getRequestState());
				queryObject.setRequestItemNumber(filter.getRequestItemNumber());
				queryObject.setRequestItemState(filter.getRequestItemState());
				queryObject.setWillTraverseVPN(filter.getWillTraverseVPN());
				queryObject.setAccessAwsVPC(filter.getAccessAwsVPC());
				queryObject.setVPNName(filter.getVpnName());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getFirewallExceptionRequestsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			info("[getFirewallExceptionRequestsForFilter] query spec is: " + queryObject.toXmlString());
			
			@SuppressWarnings("unchecked")
			List<FirewallExceptionAddRequest> moas = actionable.query(queryObject,
					this.getServiceNowRequestService());
			if (moas != null) {
				info("[getFirewallExceptionAddRequestsForFilter] got " + moas.size() + " FirewallExceptionAddRequest objects back from ESB.");
			}
			for (FirewallExceptionAddRequest moa : moas) {
				FirewallExceptionAddRequestPojo pojo = new FirewallExceptionAddRequestPojo();
				FirewallExceptionAddRequestPojo baseline = new FirewallExceptionAddRequestPojo();
				this.populateFirewallExceptionAddRequestPojo(moa, pojo);
				this.populateFirewallExceptionAddRequestPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public FirewallExceptionAddRequestPojo generateFirewallExceptionAddRequest(FirewallExceptionAddRequestRequisitionPojo pojo_req)
			throws RpcException {
		
		try {
			info("generating FirewallExceptionAddRequest on the server...");
			FirewallExceptionAddRequest actionable = (FirewallExceptionAddRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_ADD_REQUEST);
			FirewallExceptionAddRequestRequisition seed = (FirewallExceptionAddRequestRequisition) getObject(Constants.MOA_FIREWALL_EXCEPTION_ADD_REQUEST_REQUISITION);
			info("populating moa");
			this.populateFirewallExceptionAddRequestRequisitionMoa(pojo_req, seed);

			
			info("doing the FirewallExceptionAddRequest.generate...");
			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("FirewallExceptionAddRequest.generate seed data is: " + seed.toXmlString());
			@SuppressWarnings("unchecked")
			List<FirewallExceptionAddRequest> result = actionable.generate(seed, this.getServiceNowRequestService());
			// TODO if more than one returned, it's an error...
			FirewallExceptionAddRequestPojo pojo = new FirewallExceptionAddRequestPojo();
			for (FirewallExceptionAddRequest moa : result) {
				info("generated FirewallExceptionAddRequest is: " + moa.toXmlString());
				this.populateFirewallExceptionAddRequestPojo(moa, pojo);
			}
			info("FirewallExceptionAddRequest.generate is complete...");

			return pojo;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectGenerateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public FirewallExceptionAddRequestPojo updateFirewallExceptionAddRequest(FirewallExceptionAddRequestPojo rule)
			throws RpcException {

		rule.setUpdateInfo(this.getCachedUser().getPublicId());
        try {
            info("updating FirewallExceptionAddRequest on the server...");
            FirewallExceptionAddRequest newData = (FirewallExceptionAddRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_ADD_REQUEST);
            FirewallExceptionAddRequest baselineData = (FirewallExceptionAddRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_ADD_REQUEST);

            info("populating newData...");
            populateFirewallExceptionAddRequestMoa(rule, newData);

            info("populating baselineData...");
            populateFirewallExceptionAddRequestMoa(rule.getBaseline(), baselineData);
            newData.setBaseline(baselineData);

            info("doing the FirewallExceptionAddRequest.update...");
            doUpdate(newData, getServiceNowRequestService());
            info("update is FirewallExceptionAddRequest.update complete...");
        } 
        catch (Throwable t) {
            t.printStackTrace();
            throw new RpcException(t);
        }
		return rule;
	}

	@Override
	public void deleteFirewallExceptionAddRequest(FirewallExceptionAddRequestPojo rule) throws RpcException {
		try {
			info("deleting FirewallExceptionAddRequest record on the server...");
			FirewallExceptionAddRequest moa = (FirewallExceptionAddRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_ADD_REQUEST);
			info("populating moa");
			this.populateFirewallExceptionAddRequestMoa(rule, moa);

			
			info("doing the FirewallExceptionAddRequest.delete...");
			this.doDelete(moa, getServiceNowRequestService());
			info("FirewallExceptionAddRequest.delete is complete...");

			return;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseObjectDeleteException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}
	
	private void populateFirewallExceptionAddRequestMoa(FirewallExceptionAddRequestPojo pojo,
			FirewallExceptionAddRequest moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		moa.setSystemId(pojo.getSystemId());
		moa.setUserNetID(pojo.getUserNetId());
		moa.setApplicationName(pojo.getApplicationName());
		moa.setSourceOutsideEmory(pojo.getIsSourceOutsideEmory());
		moa.setTimeRule(pojo.getTimeRule());
		if (pojo.getValidUntilDate() != null) {
			org.openeai.moa.objects.resources.Date validUntilDate = moa.newValidUntilDate();
			this.populateDate(validUntilDate, pojo.getValidUntilDate());
			moa.setValidUntilDate(validUntilDate);
		}
		moa.setSourceIpAddresses(pojo.getSourceIp());
		moa.setDestinationIpAddresses(pojo.getDestinationIp());
		moa.setPorts(pojo.getPorts());
		moa.setBusinessReason(pojo.getBusinessReason());
		moa.setPatched(pojo.getIsPatched());
		moa.setNotPatchedJustification(pojo.getNotPatchedJustification());
		moa.setDefaultPasswdChanged(pojo.getIsDefaultPasswdChanged());
		moa.setPasswdNotChangedJustification(pojo.getPasswdNotChangedJustification());
		moa.setAppConsoleACLed(pojo.getIsAppConsoleACLed());
		moa.setNotACLedJustification(pojo.getNotACLedJustification());
		moa.setHardened(pojo.getIsHardened());
		moa.setNotHardenedJustification(pojo.getNotHardenedJustification());
		moa.setPatchingPlan(pojo.getPatchingPlan());
		if (pojo.getCompliance().size() > 0) {
			for (String compliance : pojo.getCompliance()) {
				moa.addCompliance(compliance);
			}
		}
		moa.setOtherCompliance(pojo.getOtherCompliance());
		moa.setSensitiveDataDesc(pojo.getSensitiveDataDesc());
		moa.setLocalFirewallRules(pojo.getLocalFirewallRules());
		moa.setDefaultDenyZone(pojo.getIsDefaultDenyZone());
		for (String tag : pojo.getTags()) {
			moa.addTag(tag);
		}
		moa.setRequestState(pojo.getRequestState());
		moa.setRequestItemNumber(pojo.getRequestItemNumber());
		moa.setRequestItemState(pojo.getRequestItemState());
		moa.setWillTraverseVPN(pojo.getWillTraverseVPN());
		moa.setVPNName(pojo.getVpnName());
		moa.setAccessAwsVPC(pojo.getAccessAwsVPC());
	}

	@SuppressWarnings("unchecked")
	private void populateFirewallExceptionAddRequestPojo(FirewallExceptionAddRequest moa,
			FirewallExceptionAddRequestPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
	
		pojo.setSystemId(moa.getSystemId());
		pojo.setUserNetId(moa.getUserNetID());
		pojo.setApplicationName(moa.getApplicationName());
		pojo.setIsSourceOutsideEmory(moa.getSourceOutsideEmory());
		pojo.setTimeRule(moa.getTimeRule());
		if (moa.getValidUntilDate() != null) {
			pojo.setValidUntilDate(this.toDateFromDate(moa.getValidUntilDate()));
		}
		pojo.setSourceIp(moa.getSourceIpAddresses());
		pojo.setDestinationIp(moa.getDestinationIpAddresses());
		pojo.setPorts(moa.getPorts());
		pojo.setBusinessReason(moa.getBusinessReason());
		pojo.setIsPatched(moa.getPatched());
		pojo.setNotPatchedJustification(moa.getNotPatchedJustification());
		pojo.setIsDefaultPasswdChanged(moa.getDefaultPasswdChanged());
		pojo.setPasswdNotChangedJustification(moa.getPasswdNotChangedJustification());
		pojo.setIsAppConsoleACLed(moa.getAppConsoleACLed());
		pojo.setNotACLedJustification(moa.getNotACLedJustification());
		pojo.setIsHardened(moa.getHardened());
		pojo.setNotHardenedJustification(moa.getNotHardenedJustification());
		pojo.setPatchingPlan(moa.getPatchingPlan());
		for (String compliance : (List<String>) moa.getCompliance()) {
			pojo.getCompliance().add(compliance);
		}
		pojo.setOtherCompliance(moa.getOtherCompliance());
		pojo.setSensitiveDataDesc(moa.getSensitiveDataDesc());
		pojo.setLocalFirewallRules(moa.getLocalFirewallRules());
		pojo.setIsDefaultDenyZone(moa.getDefaultDenyZone());
		for (String tag : (List<String>) moa.getTag()) {
			pojo.getTags().add(tag);
		}
		pojo.setRequestState(moa.getRequestState());
		pojo.setRequestItemNumber(moa.getRequestItemNumber());
		pojo.setRequestItemState(moa.getRequestItemState());
		pojo.setWillTraverseVPN(moa.getWillTraverseVPN());
		pojo.setVpnName(moa.getVPNName());
		pojo.setAccessAwsVPC(moa.getAccessAwsVPC());
	}

	private void populateFirewallExceptionAddRequestRequisitionMoa(FirewallExceptionAddRequestRequisitionPojo pojo,
			FirewallExceptionAddRequestRequisition moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		moa.setUserNetID(pojo.getUserNetId());
		moa.setApplicationName(pojo.getApplicationName());
		moa.setSourceOutsideEmory(pojo.getIsSourceOutsideEmory());
		moa.setTimeRule(pojo.getTimeRule());
		if (pojo.getValidUntilDate() != null) {
			org.openeai.moa.objects.resources.Date validUntilDate = moa.newValidUntilDate();
			this.populateDate(validUntilDate, pojo.getValidUntilDate());
			moa.setValidUntilDate(validUntilDate);
		}
		moa.setSourceIpAddresses(pojo.getSourceIp());
		moa.setDestinationIpAddresses(pojo.getDestinationIp());
		moa.setPorts(pojo.getPorts());
		moa.setBusinessReason(pojo.getBusinessReason());
		moa.setPatched(pojo.getIsPatched());
		moa.setNotPatchedJustification(pojo.getNotPatchedJustification());
		moa.setDefaultPasswdChanged(pojo.getIsDefaultPasswdChanged());
		moa.setPasswdNotChangedJustification(pojo.getPasswdNotChangedJustification());
		moa.setAppConsoleACLed(pojo.getIsAppConsoleACLed());
		moa.setNotACLedJustification(pojo.getNotACLedJustification());
		moa.setHardened(pojo.getIsHardened());
		moa.setNotHardenedJustification(pojo.getNotHardenedJustification());
		moa.setPatchingPlan(pojo.getPatchingPlan());
		if (pojo.getCompliance().size() > 0) {
			for (String compliance : pojo.getCompliance()) {
				moa.addCompliance(compliance);
			}
		}
		moa.setOtherCompliance(pojo.getOtherCompliance());
		moa.setSensitiveDataDesc(pojo.getSensitiveDataDesc());
		moa.setLocalFirewallRules(pojo.getLocalFirewallRules());
		moa.setDefaultDenyZone(pojo.getIsDefaultDenyZone());
		for (String tag : pojo.getTags()) {
			moa.addTag(tag);
		}
		moa.setWillTraverseVPN(pojo.getWillTraverseVPN());
		moa.setVPNName(pojo.getVpnName());
		moa.setAccessAwsVPC(pojo.getAccessAwsVPC());
	}

	@Override
	public FirewallExceptionRemoveRequestQueryResultPojo getFirewallExceptionRemoveRequestsForFilter(
			FirewallExceptionRemoveRequestQueryFilterPojo filter) throws RpcException {
		
		FirewallExceptionRemoveRequestQueryResultPojo result = new FirewallExceptionRemoveRequestQueryResultPojo();
		List<FirewallExceptionRemoveRequestPojo> pojos = new java.util.ArrayList<FirewallExceptionRemoveRequestPojo>();
		try {
			FirewallExceptionRemoveRequestQuerySpecification queryObject = (FirewallExceptionRemoveRequestQuerySpecification) getObject(Constants.MOA_FIREWALL_EXCEPTION_REMOVE_REQUEST_QUERY_SPEC);
			FirewallExceptionRemoveRequest actionable = (FirewallExceptionRemoveRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REMOVE_REQUEST);

			if (filter != null) {
				queryObject.setSystemId(filter.getSystemId());
				queryObject.setUserNetID(filter.getUserNetId());
				queryObject.setRequestState(filter.getRequestState());
				queryObject.setRequestItemNumber(filter.getRequestItemNumber());
				queryObject.setRequestItemState(filter.getRequestItemState());
				queryObject.setRequestDetails(filter.getRequestDetails());
				for (String tag : filter.getTags()) {
					queryObject.addTag(tag);
				}
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getFirewallExceptionRemoveRequestsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			info("[getFirewallExceptionRemoveRequestsForFilter] query spec is: " + queryObject.toXmlString());
			
			@SuppressWarnings("unchecked")
			List<FirewallExceptionRemoveRequest> moas = actionable.query(queryObject,
					this.getServiceNowRequestService());
			if (moas != null) {
				info("[getFirewallExceptionRemoveRequestsForFilter] got " + moas.size() + " FirewallExceptionRemoveRequest objects back from ESB.");
			}
			for (FirewallExceptionRemoveRequest moa : moas) {
				FirewallExceptionRemoveRequestPojo pojo = new FirewallExceptionRemoveRequestPojo();
				FirewallExceptionRemoveRequestPojo baseline = new FirewallExceptionRemoveRequestPojo();
				this.populateFirewallExceptionRemoveRequestPojo(moa, pojo);
				this.populateFirewallExceptionRemoveRequestPojo(moa, baseline);
				pojo.setBaseline(baseline);
				pojos.add(pojo);
			}

			Collections.sort(pojos);
			result.setResults(pojos);
			result.setFilterUsed(filter);
			return result;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectQueryException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public FirewallExceptionRemoveRequestPojo generateFirewallExceptionRemoveRequest(
			FirewallExceptionRemoveRequestRequisitionPojo pojo_req) throws RpcException {
		
		try {
			info("generating FirewallExceptionRemoveRequest on the server...");
			FirewallExceptionRemoveRequest actionable = (FirewallExceptionRemoveRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REMOVE_REQUEST);
			FirewallExceptionRemoveRequestRequisition seed = (FirewallExceptionRemoveRequestRequisition) getObject(Constants.MOA_FIREWALL_EXCEPTION_REMOVE_REQUEST_REQUISITION);
			info("populating moa");
			this.populateFirewallExceptionRemoveRequestRequisitionMoa(pojo_req, seed);

			
			info("doing the FirewallExceptionRemoveRequest.generate...");
			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("FirewallExceptionRemoveRequest.generate seed data is: " + seed.toXmlString());
			@SuppressWarnings("unchecked")
			List<FirewallExceptionRemoveRequest> result = actionable.generate(seed, this.getServiceNowRequestService());
			// TODO if more than one returned, it's an error...
			FirewallExceptionRemoveRequestPojo pojo = new FirewallExceptionRemoveRequestPojo();
			for (FirewallExceptionRemoveRequest moa : result) {
				info("generated FirewallExceptionRemoveRequest is: " + moa.toXmlString());
				this.populateFirewallExceptionRemoveRequestPojo(moa, pojo);
			}
			info("FirewallExceptionRemoveRequest.generate is complete...");

			return pojo;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseObjectGenerateException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (XmlEnterpriseObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	@Override
	public FirewallExceptionRemoveRequestPojo updateFirewallExceptionRemoveRequest(
			FirewallExceptionRemoveRequestPojo rule) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFirewallExceptionRemoveRequest(FirewallExceptionRemoveRequestPojo rule) throws RpcException {
		try {
			info("deleting FirewallExceptionRemoveRequest record on the server...");
			FirewallExceptionRemoveRequest moa = (FirewallExceptionRemoveRequest) getObject(Constants.MOA_FIREWALL_EXCEPTION_REMOVE_REQUEST);
			info("populating moa");
			this.populateFirewallExceptionRemoveRequestMoa(rule, moa);

			
			info("doing the FirewallExceptionRemoveRequest.delete...");
			this.doDelete(moa, getServiceNowRequestService());
			info("FirewallExceptionRemoveRequest.delete is complete...");

			return;
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (EnterpriseFieldException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} 
		catch (JMSException e) {
			e.printStackTrace();
			throw new RpcException(e);
		} catch (EnterpriseObjectDeleteException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}
	}

	private void populateFirewallExceptionRemoveRequestMoa(FirewallExceptionRemoveRequestPojo pojo,
			FirewallExceptionRemoveRequest moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		moa.setSystemId(pojo.getSystemId());
		moa.setUserNetID(pojo.getUserNetId());
		moa.setRequestState(pojo.getRequestState());
		moa.setRequestItemNumber(pojo.getRequestItemNumber());
		moa.setRequestItemState(pojo.getRequestItemState());
		moa.setRequestDetails(pojo.getRequestDetails());
		for (String tag : pojo.getTags()) {
			moa.addTag(tag);
		}
	}

	@SuppressWarnings("unchecked")
	private void populateFirewallExceptionRemoveRequestPojo(FirewallExceptionRemoveRequest moa,
			FirewallExceptionRemoveRequestPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
	
		pojo.setSystemId(moa.getSystemId());
		pojo.setUserNetId(moa.getUserNetID());
		pojo.setRequestState(moa.getRequestState());
		pojo.setRequestItemNumber(moa.getRequestItemNumber());
		pojo.setRequestItemState(moa.getRequestItemState());
		pojo.setRequestDetails(moa.getRequestDetails());
		for (String tag : (List<String>) moa.getTag()) {
			pojo.getTags().add(tag);
		}
	}

	private void populateFirewallExceptionRemoveRequestRequisitionMoa(FirewallExceptionRemoveRequestRequisitionPojo pojo,
			FirewallExceptionRemoveRequestRequisition moa) throws EnterpriseFieldException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, EnterpriseConfigurationObjectException {

		moa.setUserNetID(pojo.getUserNetId());
		moa.setRequestDetails(pojo.getRequestDetails());
		for (String tag : pojo.getTags()) {
			moa.addTag(tag);
		}
	}

	@Override
	public FirewallExceptionRequestSummaryQueryResultPojo getFirewallExceptionRequestSummariesForFilter(
			FirewallExceptionRequestSummaryQueryFilterPojo filter) throws RpcException {

		FirewallExceptionRequestSummaryQueryResultPojo result = new FirewallExceptionRequestSummaryQueryResultPojo();
		if (filter != null) {
			filter.setAddRequestFilter(new FirewallExceptionAddRequestQueryFilterPojo());
			filter.getAddRequestFilter().getTags().add(filter.getVpcId());
			result.setAddRequestFilterUsed(filter.getAddRequestFilter());
			
			filter.setRemoveRequestFilter(new FirewallExceptionRemoveRequestQueryFilterPojo());
			filter.getRemoveRequestFilter().getTags().add(filter.getVpcId());
			result.setRemoveRequestFilterUsed(filter.getRemoveRequestFilter());
		}

		List<FirewallExceptionRequestSummaryPojo> pojos = new java.util.ArrayList<FirewallExceptionRequestSummaryPojo>();
		
		FirewallExceptionAddRequestQueryResultPojo p_result = 
			this.getFirewallExceptionAddRequestsForFilter(filter == null ? null : filter.getAddRequestFilter());
		for (FirewallExceptionAddRequestPojo snp : p_result.getResults()) {
			FirewallExceptionRequestSummaryPojo snps = new FirewallExceptionRequestSummaryPojo();
			snps.setAddRequest(snp);
			pojos.add(snps);
		}
		
		FirewallExceptionRemoveRequestQueryResultPojo dp_result = 
			this.getFirewallExceptionRemoveRequestsForFilter(filter == null ? null : filter.getRemoveRequestFilter());
		for (FirewallExceptionRemoveRequestPojo snd : dp_result.getResults()) {
			FirewallExceptionRequestSummaryPojo snps = new FirewallExceptionRequestSummaryPojo();
			snps.setRemoveRequest(snd);
			pojos.add(snps);
		}
		
		Collections.sort(pojos);
		result.setResults(pojos);
		return result;
	}

	@Override
	public List<AWSRegionPojo> getAwsRegionItems() throws RpcException {
		List<AWSRegionPojo> results = new java.util.ArrayList<AWSRegionPojo>();
		try {
			Properties regionProps = getAppConfig().getProperties(AWS_REGION_PROPERTIES);
			Iterator<Object> keys = regionProps.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String)keys.next();
				String value = (String)regionProps.get(key);
				AWSRegionPojo region = new AWSRegionPojo();
				region.setCode(key);
				region.setValue(value);
				results.add(region);
			}
		} 
		catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new RpcException(e);
		}

		return results;
	}
}
