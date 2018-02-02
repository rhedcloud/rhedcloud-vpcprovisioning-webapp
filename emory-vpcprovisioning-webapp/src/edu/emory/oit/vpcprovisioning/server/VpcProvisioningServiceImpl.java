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

import org.any_openeai_enterprise.moa.jmsobjects.services.v2_0.Authorization;
import org.any_openeai_enterprise.moa.objects.resources.v2_0.AuthorizationQuerySpecification;
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
import org.openeai.threadpool.ThreadPool;
import org.openeai.threadpool.ThreadPoolException;
import org.openeai.transport.RequestService;
import org.openeai.utils.config.AppConfigFactory;
import org.openeai.utils.config.AppConfigFactoryException;
import org.openeai.utils.config.SimpleAppConfigFactory;

import com.amazon.aws.moa.jmsobjects.billing.v1_0.Bill;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.Account;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.VirtualPrivateCloud;
import com.amazon.aws.moa.jmsobjects.provisioning.v1_0.VirtualPrivateCloudProvisioning;
import com.amazon.aws.moa.objects.resources.v1_0.AccountQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.BillQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.EmailAddress;
import com.amazon.aws.moa.objects.resources.v1_0.LineItem;
import com.amazon.aws.moa.objects.resources.v1_0.ProvisioningStep;
import com.amazon.aws.moa.objects.resources.v1_0.VirtualPrivateCloudProvisioningQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.VirtualPrivateCloudQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.VirtualPrivateCloudRequisition;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.emory.moa.jmsobjects.identity.v2_0.FullPerson;
import edu.emory.moa.jmsobjects.network.v1_0.Cidr;
import edu.emory.moa.jmsobjects.network.v1_0.CidrAssignment;
import edu.emory.moa.objects.resources.v1_0.CidrAssignmentQuerySpecification;
import edu.emory.moa.objects.resources.v1_0.CidrQuerySpecification;
import edu.emory.moa.objects.resources.v2_0.FullPersonQuerySpecification;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.BillPojo;
import edu.emory.oit.vpcprovisioning.shared.BillQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.BillQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Cache;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentStatus;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
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
import edu.emory.oit.vpcprovisioning.shared.EmailPojo;
import edu.emory.oit.vpcprovisioning.shared.LineItemPojo;
import edu.emory.oit.vpcprovisioning.shared.ProvisioningStepPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.RpcException;
import edu.emory.oit.vpcprovisioning.shared.SharedObject;
import edu.emory.oit.vpcprovisioning.shared.UUID;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryResultPojo;

@SuppressWarnings("serial")
public class VpcProvisioningServiceImpl extends RemoteServiceServlet implements VpcProvisioningService {
	// for supporting data queries and authorization
	private static final String IDENTITY_SERVICE_NAME = "IdentityRequestService";
	private static final String AWS_SERVICE_NAME = "AWSRequestService";
	private static final String CIDR_SERVICE_NAME = "CidrRequestService";
	private static final String AUTHZ_SERVICE_NAME = "AuthorizationRequestService";
	private static final String GENERAL_PROPERTIES = "GeneralProperties";
	private static final String AWS_URL_PROPERTIES = "AWSUrlProperties";
	private static String LOGTAG = "[" + VpcProvisioningServiceImpl.class.getSimpleName()
			+ "]";
	private Logger log = Logger.getLogger(getClass().getName());
	private boolean useEsbService = true;
	private boolean useShibboleth = true;
	private boolean useAuthzService = true;
	private AppConfig appConfig = null;
	Properties generalProps = null;
	private String configDocPath = null;
	private String appId = null;
	private ProducerPool identityServiceProducerPool = null;
	private ProducerPool awsProducerPool = null;
	private ProducerPool cidrProducerPool = null;
	private ProducerPool authzProducerPool = null;
	private Object lock = new Object();
	static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private boolean initializing = false;
	private int defaultRequestTimeoutInterval = 20000;
	// default, 8 hours in prod
	private int sessionTimeoutIntervalSeconds = 28800;
	private int userCnt=0;
	private boolean manageSessionLocally=true;
	private String baseLoginURL = null;
	
	boolean fakeVpcpGen = false;
	HashMap<String, VpcpPojo> vpcpMap = new HashMap<String, VpcpPojo>();
	
	private List<BillPojo> masterBills = new java.util.ArrayList<BillPojo>();
	// Key is account id, List is a list of Bills for that account id
	private HashMap<String, List<BillPojo>> billsByAccount = new HashMap<String, List<BillPojo>>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		initAppConfig();

		ReleaseInfo ri = new ReleaseInfo();
		info("VPC Provisioning WebApp Version: " + ri.getVersion() + " Build: " + ri.getBuild()
				+ " initialization complete.");
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
			identityServiceProducerPool = (ProducerPool) getAppConfig().getObject(
					IDENTITY_SERVICE_NAME);
			awsProducerPool = (ProducerPool) getAppConfig().getObject(
					AWS_SERVICE_NAME);
			cidrProducerPool = (ProducerPool) getAppConfig().getObject(
					CIDR_SERVICE_NAME);
			authzProducerPool = (ProducerPool) getAppConfig().getObject(
					AUTHZ_SERVICE_NAME);
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			
			baseLoginURL = generalProps.getProperty("baseLoginURL", null);
			if (baseLoginURL == null) {
				info("'baseLoginURL' property from config doc is null, defaulting to DEV IDP URL.");
				baseLoginURL = "nothing";
			}
			
			String redirect = generalProps.getProperty("manageSessionLocally", "true");
			manageSessionLocally = Boolean.parseBoolean(redirect);
			
			String shib = generalProps.getProperty("useShibboleth", "true");
			useShibboleth = Boolean.parseBoolean(shib);

			String authz = generalProps.getProperty("useAuthzService", "true");
			useAuthzService = Boolean.parseBoolean(authz);
			info("useAuthzService is: " + useAuthzService);
			
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

	@SuppressWarnings({ "unchecked", "unused" })
	private List<ActionableEnterpriseObject> doQuery(
			XmlEnterpriseObject queryObject, ActionableEnterpriseObject aeo)
			throws EnterpriseObjectQueryException, JMSException {

		RequestService reqSvc = (RequestService) cidrProducerPool.getProducer();
		((PointToPointProducer) reqSvc)
				.setRequestTimeoutInterval(getDefaultRequestTimeoutInterval());
		
		String authUserId = this.getAuthUserIdForHALS();
		aeo.getAuthentication().setAuthUserId(authUserId);
		info("[doQuery] AuthUserId is: " + aeo.getAuthentication().getAuthUserId());
		
		List<ActionableEnterpriseObject> results = aeo.query(queryObject,
				reqSvc);
		return results;
	}

	@SuppressWarnings("unchecked")
	private List<ActionableEnterpriseObject> doGenerate(ActionableEnterpriseObject aeo, XmlEnterpriseObject seedXeo, RequestService reqSvc) throws JMSException,
	EnterpriseObjectGenerateException, RpcException {

		String authUserId = this.getAuthUserIdForHALS();
		aeo.getAuthentication().setAuthUserId(authUserId);
		info("[doCreate] AuthUserId is: " + aeo.getAuthentication().getAuthUserId());

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

	
	private String getAuthUserIdForHALS() throws RpcException {
		HttpServletRequest request = this.getThreadLocalRequest();
		String clientIp = request.getRemoteAddr();
		try {
			UserAccountPojo user = this.getUserLoggedIn();
			return user.getEppn() + "/" + clientIp;
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

	@Override
	public String getLoginURL() throws RpcException {
		HttpServletRequest r = this.getThreadLocalRequest();
		String referer = r.getHeader("Referer");
		info("referrer is; " + referer);
		info("baseLoginURL is: " + getBaseLoginURL());

		String loginURL = getBaseLoginURL() + "?target=" + referer;
		info("returning a loginURL of: '" + loginURL + "'");
		
		return loginURL;
	}

	@Override
	public UserAccountPojo getUserLoggedIn() throws RpcException {
		// if the user account has been cached, we'll use it
		info("checking cache for existing user...");
		UserAccountPojo user = (UserAccountPojo) Cache.getCache().get(
				Constants.USER_ACCOUNT + getCurrentSessionId());
		
		if (user != null) {
			if (this.isSessionValidForUser(user)) { 
				info("[found user in cache] user logged in is: " + user.getEppn());
				if (useAuthzService) {
					// get permissions for user so they can be checked against later
					// only do this once per session.
					this.getPermissionsForUser(user);
					if (user.getPermissions().size() == 0) {
						// ERROR, user does not have any required permissions to use
						// this app
						info("user " + user.getEppn() + " is not authorized to use this application.");
						throw new RpcException("The user id being used to log " +
							"in is not authorized to use this application.  " +
							"Please contact your support representative to " +
							"gain access to this application.");
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

			String eppn = (String) request.getHeader("eduPersonPrincipalName");
			
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
				
				if (useAuthzService) {
					// get permissions for user so they can be checked against later
					// only do this once per session.
					this.getPermissionsForUser(user);
					if (user.getPermissions().size() == 0) {
						// ERROR, user does not have any required permissions to use
						// this app
						info("user " + eppn + " is not authorized to use this application.");
						throw new RpcException("The user id being used to log " +
							"in is not authorized to use this application.  " +
							"Please contact your support representative to " +
							"gain access to this application.");
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
				useShibboleth = Boolean.parseBoolean(shib);
				String testUserEppn = generalProps.getProperty("testUserEppn", "jtjacks@emory.edu");

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

					if (useAuthzService) {
						// get permissions
						this.getPermissionsForUser(user);
					}
					else {
						// give them all permissions
						for (String permission : Constants.PERMISSIONS) {
							user.getPermissions().add(permission);
						}
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
						if (user.getPermissions().size() == 0) {
							info("user " + eppn + " is not authorized to use this application.");
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

	@SuppressWarnings("unchecked")
	protected void getPermissionsForUser(UserAccountPojo user) throws RpcException {
		info("getting permissions for " + user.getEppn());
		user.getPermissions().clear();
		try {
			// preferred approach (1 query)...
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

			// work around (multiple queries, one for each permission)...
			for (String permission : Constants.PERMISSIONS) {
				AuthorizationQuerySpecification queryObject = (AuthorizationQuerySpecification) getAppConfig().getObject(Constants.MOA_AUTHORIZATION_QUERY_SPEC);
				Authorization actionable = (Authorization) getAppConfig().getObject(Constants.MOA_AUTHORIZATION);
				queryObject.setPrincipal(user.getEppn());
				queryObject.addPermission(permission);
				
//				info("queryObject.toXmlString: " + queryObject.toXmlString());
				List<Authorization> list = actionable.query(queryObject, getAuthzRequestService());
//				info("there were " + list.size() + " Authorization objects returned.");

				if (list.size() > 0) {
					for (Authorization authorization : (List<Authorization>) list) {
//						info("authorization has " + authorization.getPermissionLength() + " permissions in it.");
//						info("isAuthorized = " + authorization.getIsAuthorized());
//						info("Authorization.toXmlString: " + authorization.toXmlString());
						if (authorization.getIsAuthorized().equals("true")) {
							for (String perm : (List<String>)authorization.getPermission()) {
								info("adding permission " + perm + " to UserAccountPojo...");
								user.getPermissions().add(perm);
							}
						}
						else {
							for (String perm : (List<String>)authorization.getPermission()) {
								info(user.getEppn() + " is NOT authorized for permission " + perm);
							}
						}
					}
				}
			}
		} 
		catch (Throwable t) {
			t.printStackTrace();
			throw new RpcException(t);
		}
	}

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
			pojo.setVpcId(moa.getVpcId());
		}
		else {
			moa.setVpcId(UUID.uuid());
			pojo.setVpcId(moa.getVpcId());
		}
		moa.setType(pojo.getType());
		// owner net ids
//        for (String p : pojo.getCustomerAdminNetIdList()) {
//            moa.addCustomerAdminNetId(p);
//        }

		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateVpcPojo(VirtualPrivateCloud moa,
			VpcPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setAccountId(moa.getAccountId());
		pojo.setVpcId(moa.getVpcId());
		pojo.setType(moa.getType());
//		for (String netId : (List<String>) moa.getCustomerAdminNetId()) {
//			pojo.getCustomerAdminNetIdList().add(netId);
//		}
		
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
		moa.setAccountOwnerNetId(pojo.getAccountOwnerNetId());
		moa.setFinancialAccountNumber(pojo.getFinancialAccountNumber());
		moa.setType(pojo.getType());
		// admin net ids
        for (String p : pojo.getCustomerAdminNetIdList()) {
            moa.addCustomerAdminNetId(p);
        }
        moa.setTicketId(pojo.getTicketId());
        moa.setAuthenticatedRequestorNetId(pojo.getAuthenticatedRequestorNetId());
        moa.setComplianceClass(pojo.getComplianceClass());
        moa.setNotifyAdmins(this.toStringFromBoolean(pojo.isNotifyAdmins()));

//		this.setMoaCreateInfo(moa, pojo);
//		this.setMoaUpdateInfo(moa, pojo);
	}

	@SuppressWarnings({ "unchecked" })
	private void populateVpcRequisitionPojo(VirtualPrivateCloudRequisition moa,
			VpcRequisitionPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {

		pojo.setAccountId(moa.getAccountId());
		pojo.setAccountOwnerNetId(moa.getAccountOwnerNetId());
		pojo.setFinancialAccountNumber(moa.getFinancialAccountNumber());
		pojo.setType(moa.getType());
		for (String netId : (List<String>) moa.getCustomerAdminNetId()) {
			pojo.getCustomerAdminNetIdList().add(netId);
		}
		pojo.setTicketId(moa.getTicketId());
		pojo.setAuthenticatedRequestorNetId(moa.getAuthenticatedRequestorNetId());
		pojo.setComplianceClass(moa.getComplianceClass());
		pojo.setNotifyAdmins(this.toBooleanFromString(moa.getNotifyAdmins()));
		
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
		moa.setAccountOwnerNetId(pojo.getAccountOwnerDirectoryMetaData().getNetId());
		moa.setFinancialAccountNumber(pojo.getFinancialAccountNumber());
		// email
        for (EmailPojo p : pojo.getEmailList()) {
            EmailAddress email = moa.newEmailAddress();
            email.setType(p.getType());
            email.setEmail(p.getEmail());
            moa.addEmailAddress(email);
        }

		// owner net ids
        for (String p : pojo.getCustomerAdminNetIdList()) {
            moa.addCustomerAdminNetId(p);
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
		DirectoryMetaDataPojo cachedDmdp = this.getDirectoryMetaDataForNetId(moa.getAccountOwnerNetId());
		if (cachedDmdp != null) {
			ownerDmdp.setNetId(cachedDmdp.getNetId());
			ownerDmdp.setFirstName(cachedDmdp.getFirstName());
			ownerDmdp.setLastName(cachedDmdp.getLastName());
		}
		pojo.setAccountOwnerDirectoryMetaData(ownerDmdp);
		pojo.setPasswordLocation(moa.getPasswordLocation());
		pojo.setFinancialAccountNumber(moa.getFinancialAccountNumber());
		for (EmailAddress email : (List<EmailAddress>) moa.getEmailAddress()) {
			EmailPojo emp = new EmailPojo();
			emp.setType(email.getType());
			emp.setEmail(email.getEmail());
			pojo.getEmailList().add(emp);
		}
		
		for (String netId : (List<String>) moa.getCustomerAdminNetId()) {
			pojo.getCustomerAdminNetIdList().add(netId);
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

		this.setMoaCreateInfo(moa, pojo);
		this.setMoaUpdateInfo(moa, pojo);
	}

	private void populateCidrPojo(Cidr moa,
			CidrPojo pojo) throws XmlEnterpriseObjectException,
			ParseException {
		
		pojo.setCidrId(moa.getCidrId());
		pojo.setNetwork(moa.getNetwork());
		pojo.setBits(moa.getBits());
		
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
        UserAccountPojo user = this.getUserLoggedIn();
        return this.getClientInfoForUser(user);
	}

	@Override
	public CidrQueryResultPojo getCidrsForFilter(CidrQueryFilterPojo filter) throws RpcException {
		CidrQueryResultPojo result = new CidrQueryResultPojo();
		List<CidrPojo> pojos = new java.util.ArrayList<CidrPojo>();
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
	public List<CidrPojo> getCidrsForUserLoggedIn() throws RpcException {
		UserAccountPojo user = getUserLoggedIn();
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
		
		cidr.setCreateInfo(this.getUserLoggedIn().getEppn(),
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

				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
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
		UserAccountPojo user = getUserLoggedIn();
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
		
		cidrAssignment.setCreateInfo(this.getUserLoggedIn().getEppn(),
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

				Cache.getCache().remove(Constants.CIDR_ASSIGNMENT + this.getUserLoggedIn().getEppn());
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
		this.appConfig = appConfig;
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

	public ProducerPool getAuthzProducerPool() {
		return authzProducerPool;
	}

	public void setAuthzProducerPool(ProducerPool authzProducerPool) {
		this.authzProducerPool = authzProducerPool;
	}

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

	public String getBaseLoginURL() {
		return baseLoginURL;
	}

	public void setBaseLoginURL(String baseLoginURL) {
		this.baseLoginURL = baseLoginURL;
	}

	private RequestService getIdentityServiceRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) identityServiceProducerPool.getProducer();
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
	private RequestService getAuthzRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) authzProducerPool.getProducer();
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
	public void deleteCidr(CidrPojo cidr) throws RpcException {
		if (!useEsbService) {
			return;
		} 
		else {
			try {
				info("deleting CIDR record on the server...");
				Cidr moa = (Cidr) getObject(Constants.MOA_CIDR);
				info("populating moa");
				this.populateCidrMoa(cidr, moa);

				
				info("doing the Cidr.delete...");
				this.doDelete(moa, getCidrRequestService());
				info("Cidr.delete is complete...");

				Cache.getCache().remove(Constants.CIDR + this.getUserLoggedIn().getEppn());
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
		cidr.setUpdateInfo(this.getUserLoggedIn().getPrincipal());
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
		cidrAssignment.setUpdateInfo(this.getUserLoggedIn().getPrincipal());
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

				Cache.getCache().remove(Constants.CIDR_ASSIGNMENT + this.getUserLoggedIn().getEppn());
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
	public AccountQueryResultPojo getAccountsForFilter(AccountQueryFilterPojo filter) throws RpcException {
		AccountQueryResultPojo result = new AccountQueryResultPojo();
		List<AccountPojo> pojos = new java.util.ArrayList<AccountPojo>();
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
					emailMoa.setEmail(filter.getEmail().getEmail());
					queryObject.setEmailAddress(emailMoa);
				}
				queryObject.setAccountOwnerNetId(filter.getAccountOwnerNetId());
				queryObject.setFinancialAccountNumber(filter.getFinancialAccountNumber());
				queryObject.setCreateUser(filter.getCreateUser());
				queryObject.setLastUpdateUser(filter.getLastUpdateUser());
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getAccountsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<Account> moas = actionable.query(queryObject,
					this.getAWSRequestService());
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
		account.setCreateInfo(this.getUserLoggedIn().getEppn(),
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
		account.setUpdateInfo(this.getUserLoggedIn().getPrincipal());
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

	@Override
	public VpcQueryResultPojo getVpcsForFilter(VpcQueryFilterPojo filter) throws RpcException {
		VpcQueryResultPojo result = new VpcQueryResultPojo();
		List<VpcPojo> pojos = new java.util.ArrayList<VpcPojo>();
		try {
			VirtualPrivateCloudQuerySpecification queryObject = (VirtualPrivateCloudQuerySpecification) getObject(Constants.MOA_VPC_QUERY_SPEC);
			VirtualPrivateCloud actionable = (VirtualPrivateCloud) getObject(Constants.MOA_VPC_MAINTAIN);

			if (filter != null) {
				queryObject.setAccountId(filter.getAccountId());
				queryObject.setVpcId(filter.getVpcId());
				queryObject.setType(filter.getType());
				queryObject.setCustomerAdminNetId(filter.getCustomerAdminNetId());
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
	public VpcPojo generateVpc(VpcRequisitionPojo vpcRequisition) throws RpcException {
		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("generating Vpc on the server...");
				VirtualPrivateCloud actionable = (VirtualPrivateCloud) getObject(Constants.MOA_VPC_GENERATE);
				VirtualPrivateCloudRequisition seed = (VirtualPrivateCloudRequisition) getObject(Constants.MOA_VPC_REQUISITION);
				info("populating moa");
				this.populateVpcRequisitionMoa(vpcRequisition, seed);

				
				info("doing the Vpc.generate...");
				String authUserId = this.getAuthUserIdForHALS();
				actionable.getAuthentication().setAuthUserId(authUserId);
				@SuppressWarnings("unchecked")
				List<VirtualPrivateCloud> result = actionable.generate(seed, getAWSRequestService());
				// TODO if more than one returned, it's an error...
				VpcPojo vpcPojo = new VpcPojo();
				for (VirtualPrivateCloud vpc : result) {
					info("generated VPC is: " + vpc.toXmlString());
					this.populateVpcPojo(vpc, vpcPojo);
				}
				info("Vpc.generate is complete...");

//				Cache.getCache().remove(Constants.ACCOUNT + this.getUserLoggedIn().getEppn());
				return vpcPojo;
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
	}

	@Override
	public VpcPojo registerVpc(VpcPojo vpc) throws RpcException {
		vpc.setCreateInfo(this.getUserLoggedIn().getEppn(),
				new java.util.Date());

		if (!useEsbService) {
			return null;
		} 
		else {
			try {
				info("creating vpc record on the server...");
				VirtualPrivateCloud moa = (VirtualPrivateCloud) getObject(Constants.MOA_VPC_MAINTAIN);
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
		vpc.setUpdateInfo(this.getUserLoggedIn().getPrincipal());
        try {
            info("updating Vpc on the server...");
            VirtualPrivateCloud newData = (VirtualPrivateCloud) getObject(Constants.MOA_VPC_MAINTAIN);
            VirtualPrivateCloud baselineData = (VirtualPrivateCloud) getObject(Constants.MOA_VPC_MAINTAIN);

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
				VirtualPrivateCloud moa = (VirtualPrivateCloud) getObject(Constants.MOA_VPC_MAINTAIN);
				info("populating moa");
				this.populateVpcMoa(vpc, moa);

				
				info("doing the Vpc.delete...");
				this.doDelete(moa, getAWSRequestService());
				info("Vpc.delete is complete...");

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
		CidrAssignmentQueryResultPojo cidrAssignmentResult = this.getCidrAssignmentsForFilter(null);
		
		for (CidrPojo cidr : cidrResult.getResults()) {
			if (!hasAssignment(cidr, cidrAssignmentResult.getResults())) {
				unassignedCidrs.add(cidr);
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
	public DirectoryMetaDataPojo getDirectoryMetaDataForNetId(String netId) throws RpcException {
		// - check cache
		// - if cache exists
		// 	- return DirectoryMetaDataPojo from cache
		// - else:
		// 	- do a FullPerson.Query passing netid in a FullPersonQuerySpecification
		// 	- populate DirectoryMetaDataPojo with FullPerson data
		//  - add DirectoryMetaDataPojo to cache
		//  - return pojo
		netId = netId.toLowerCase().trim();
		DirectoryMetaDataPojo dmd = (DirectoryMetaDataPojo) Cache.getCache().get(
				Constants.NET_ID + netId);
		if (dmd!= null) {
			return dmd;
		}
		else {
			dmd = new DirectoryMetaDataPojo();
			try {
				FullPersonQuerySpecification queryObject = (FullPersonQuerySpecification) getObject(Constants.MOA_FULL_PERSON_QUERY_SPEC);
				FullPerson actionable = (FullPerson) getObject(Constants.MOA_FULL_PERSON);

				queryObject.setNetId(netId);

				String authUserId = this.getAuthUserIdForHALS();
				actionable.getAuthentication().setAuthUserId(authUserId);
				info("[getDirectoryMetaDataForNetId] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
				
				@SuppressWarnings("unchecked")
				List<FullPerson> moas = actionable.query(queryObject,
						this.getIdentityServiceRequestService());
				
				// should only get one back
				info("got " + moas.size() + " FullPerson moas back from ESB for NetId '" + netId + "'");
				for (FullPerson moa : moas) {
					dmd.setNetId(netId);
					dmd.setFirstName(moa.getPerson().getPersonalName().getFirstName());
					dmd.setLastName(moa.getPerson().getPersonalName().getLastName());
				}

				Cache.getCache().put(Constants.NET_ID + netId, dmd);
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
			VirtualPrivateCloudProvisioning actionable = (VirtualPrivateCloudProvisioning) getObject(Constants.MOA_VPCP_MAINTAIN);

			if (filter != null) {
				queryObject.setProvisioningId(filter.getProvisioningId());
				queryObject.setType(filter.getType());
				queryObject.setComplianceClass(filter.getComplianceClass());
				queryObject.setCreateUser(filter.getCreateUser());
				queryObject.setLastUpdateUser(filter.getUpdateUser());
				info("[getVpcpsForFilter] getting VPCPs for filter: " + queryObject.toXmlString());
			}
			else {
				info("[getVpcpsForFilter] no filter passed in.  Getting all VPCPs");
			}

			String authUserId = this.getAuthUserIdForHALS();
			actionable.getAuthentication().setAuthUserId(authUserId);
			info("[getVpcpsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
			@SuppressWarnings("unchecked")
			List<VirtualPrivateCloudProvisioning> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getVpcpsForFilter] got " + moas.size() + " VPCPs back from the server.");
			for (VirtualPrivateCloudProvisioning moa : moas) {
//				info("VPCP returned: " + moa.toXmlString());
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
	        @SuppressWarnings("unchecked")
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
		pojo.setActualTime(moa.getActualTime());
		pojo.setAnticipatedTime(moa.getAnticipatedTime());
		if (moa.getProperty() != null) {
			for (com.amazon.aws.moa.objects.resources.v1_0.Property stepProps : (List<com.amazon.aws.moa.objects.resources.v1_0.Property>) moa.getProperty()) {
				pojo.getProperties().put(stepProps.getKey(), stepProps.getValue());
			}
		}
	}
	
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
						psp.setStatus(Constants.VPCP_STEP_STATUS_COMPLETED);
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
			VirtualPrivateCloudProvisioning actionable = (VirtualPrivateCloudProvisioning) getObject(Constants.MOA_VPCP_GENERATE);
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
		return null;
	}

	@Override
	public List<String> getComplianceClassItems() throws RpcException {
		List<String> types = new java.util.ArrayList<String>();
		types.add("HIPAA");
		types.add("ePHI");
		types.add("none");
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

//			String authUserId = this.getAuthUserIdForHALS();
//			actionable.getAuthentication().setAuthUserId(authUserId);
//			info("[getBillsForFilter] AuthUserId is: " + actionable.getAuthentication().getAuthUserId());
			
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElasticIpPojo createElasticIp(ElasticIpPojo elasticIp) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteElasticIp(ElasticIpPojo vpc) throws RpcException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElasticIpPojo updateElasticIp(ElasticIpPojo vpc) throws RpcException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElasticIpAssignmentPojo createElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment)
			throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElasticIpAssignmentPojo updateElasticIpAssignment(ElasticIpAssignmentPojo elasticIpAssignment)
			throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteElasticIpAssignment(ElasticIpAssignmentPojo cidr) throws RpcException {
		// TODO Auto-generated method stub
		
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
}
