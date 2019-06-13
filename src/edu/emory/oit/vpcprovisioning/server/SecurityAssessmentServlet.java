package edu.emory.oit.vpcprovisioning.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openeai.config.AppConfig;
import org.openeai.config.EnterpriseConfigurationObjectException;
import org.openeai.config.EnterpriseFieldException;
import org.openeai.jms.producer.PointToPointProducer;
import org.openeai.jms.producer.ProducerPool;
import org.openeai.moa.EnterpriseObjectQueryException;
import org.openeai.moa.XmlEnterpriseObject;
import org.openeai.moa.XmlEnterpriseObjectException;
import org.openeai.moa.objects.resources.Datetime;
import org.openeai.transport.RequestService;
import org.openeai.utils.config.AppConfigFactory;
import org.openeai.utils.config.AppConfigFactoryException;
import org.openeai.utils.config.SimpleAppConfigFactory;

import com.amazon.aws.moa.jmsobjects.services.v1_0.ServiceSecurityAssessment;
import com.amazon.aws.moa.objects.resources.v1_0.SecurityRisk;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceControl;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceSecurityAssessmentQuerySpecification;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTest;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTestPlan;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTestRequirement;
import com.amazon.aws.moa.objects.resources.v1_0.ServiceTestStep;

import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSTagPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.RpcException;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestRequirementPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestStepPojo;
import edu.emory.oit.vpcprovisioning.shared.SharedObject;

@SuppressWarnings("serial")
public class SecurityAssessmentServlet extends HttpServlet {
	private static final String DIRECTORY_SERVICE_NAME = "DirectoryRequestService";
	private static final String IDENTITY_SERVICE_NAME = "IdentityRequestService";
	private static final String AWS_SERVICE_NAME = "AWSRequestService";

	private ProducerPool identityServiceProducerPool = null;
	private ProducerPool directoryProducerPool = null;
	private ProducerPool awsProducerPool = null;

	private int defaultRequestTimeoutInterval = 20000;

	private static final String LOG_TAG = " [SecurityAssessmentServlet] ";
	private static final String GENERAL_PROPERTIES = "GeneralProperties";
	private Logger log = Logger.getLogger(getClass().getName());
	private String applicationEnvironment=null;
	private static AppConfig appConfig = null;
	Properties generalProps = null;
	private String configDocPath = null;
	private String appId = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		initAppConfig();

		ReleaseInfo ri = this.getReleaseInfo();
		info(ri.toString() + " initialization complete.");
	}
	
	private void initAppConfig() throws ServletException {
		configDocPath = this.getServletConfig().getInitParameter(
				"configDocPath");
		if (configDocPath == null) {
			configDocPath = "Unknown configDocPath";
		}

		appId = this.getServletConfig().getInitParameter("appId");
		if (appId == null) {
			appId = "Unknown appId";
		}

		info("configDocPath is: " + configDocPath);
		info("appId is: " + appId);

		info("Initializing AppConfig...");
		AppConfigFactory acf = new SimpleAppConfigFactory();
		try {
			setAppConfig(acf.makeAppConfig(configDocPath, appId));
			generalProps = getAppConfig().getProperties(GENERAL_PROPERTIES);
			applicationEnvironment = generalProps.getProperty("applicationEnvironment", "DEV");
			
			identityServiceProducerPool = (ProducerPool) getAppConfig().getObject(
					IDENTITY_SERVICE_NAME);
			awsProducerPool = (ProducerPool) getAppConfig().getObject(
					AWS_SERVICE_NAME);
			directoryProducerPool = (ProducerPool) getAppConfig().getObject(
					DIRECTORY_SERVICE_NAME);
		} 
		catch (AppConfigFactoryException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		info("AppConfig initialized...");
	}

	public ReleaseInfo getReleaseInfo() throws RpcException {
		ReleaseInfo ri = new ReleaseInfo();
		ri.setApplicationEnvironment(applicationEnvironment);
		return ri;
	}

	public int getDefaultRequestTimeoutInterval() {
		return defaultRequestTimeoutInterval;
	}

	public void setDefaultRequestTimeoutInterval(
			int defaultRequestTimeoutInterval) {
		this.defaultRequestTimeoutInterval = defaultRequestTimeoutInterval;
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

	private RequestService getDirectoryRequestService() throws JMSException {
		RequestService reqSvc = (RequestService) directoryProducerPool.getProducer();
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
	
	private String toStringFromBoolean(boolean validated) {
		return Boolean.toString(validated);
	}

	private int toIntFromString(String s) {
		if (s == null) {
			return 0;
		} 
		else {
			return Integer.parseInt(s);
		}
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
	public AWSServiceQueryResultPojo getServicesForFilter(AWSServiceQueryFilterPojo filter) throws RpcException {
		AWSServiceQueryResultPojo result = new AWSServiceQueryResultPojo();
		result.setFilterUsed(filter);
		List<AWSServicePojo> pojos = new java.util.ArrayList<AWSServicePojo>();

		try {
			com.amazon.aws.moa.jmsobjects.services.v1_0.Service actionable = 
					(com.amazon.aws.moa.jmsobjects.services.v1_0.Service) getObject(Constants.MOA_SERVICE);
			ServiceQuerySpecification queryObject = (ServiceQuerySpecification) getObject(Constants.MOA_SERVICE_QUERY_SPEC);
			
			if (filter != null) {
				if (!filter.isFuzzyFilter()) {
					queryObject.setServiceId(filter.getServiceId());
					queryObject.setAwsServiceCode(filter.getAwsServiceCode());
					queryObject.setAwsServiceName(filter.getAwsServiceName());
					queryObject.setAwsStatus(filter.getAwsStatus());
					queryObject.setSiteStatus(filter.getSiteStatus());
					queryObject.setAwsHipaaEligible(filter.getAwsHipaaEligible());
					queryObject.setSiteHipaaEligible(filter.getSiteHipaaEligible());
					for (String consoleCat : filter.getConsoleCategories()) {
						queryObject.addConsoleCategory(consoleCat);
					}
					for (String cat : filter.getCategories()) {
						queryObject.addCategory(cat);
					}
				}
				else {
					info("[getServicesForFilter] performing a fuzzy filter...");
				}
			}

			info("[getServicesForFilter] query object is: " + queryObject.toXmlString());
			List<com.amazon.aws.moa.jmsobjects.services.v1_0.Service> moas = actionable.query(queryObject,
					this.getAWSRequestService());
			info("[getServicessForFilter] got " + moas.size() + " services from ESB service");

			for (com.amazon.aws.moa.jmsobjects.services.v1_0.Service service : moas) {
				if (filter != null && filter.isFuzzyFilter()) {
					boolean includeInList=false;
					if (filter.getAwsServiceName() != null && filter.getAwsServiceName().length() > 0) {
						if (service.getAwsServiceName().toLowerCase().indexOf(filter.getAwsServiceName().toLowerCase()) >= 0) {
							includeInList = true;
						}
					}
					else if (filter.getConsoleCategories().size() > 0) {
						String filterCat = filter.getConsoleCategories().get(0);
						for (String consoleCat : (List<String>)service.getConsoleCategory()) {
							if (consoleCat.toLowerCase().indexOf(filterCat.toLowerCase()) >= 0) {
								includeInList = true;
							}
						}
					}
					if (includeInList) {
						AWSServicePojo pojo = new AWSServicePojo();
						AWSServicePojo baseline = new AWSServicePojo();
						this.populateAWSServicePojo(service, pojo);
						this.populateAWSServicePojo(service, baseline);
						pojo.setBaseline(baseline);

						pojos.add(pojo);
					}
				}
				else {
					AWSServicePojo pojo = new AWSServicePojo();
					AWSServicePojo baseline = new AWSServicePojo();
					this.populateAWSServicePojo(service, pojo);
					this.populateAWSServicePojo(service, baseline);
					pojo.setBaseline(baseline);

					pojos.add(pojo);
				}
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
				if (risk.getServiceControl() != null) {
					for (ServiceControl sc : (List<ServiceControl>)risk.getServiceControl()) {
						ServiceControlPojo scp = new ServiceControlPojo();
						scp.setServiceId(sc.getServiceId());
						scp.setServiceControlId(sc.getServiceControlId());
						scp.setSequenceNumber(this.toIntFromString(sc.getSequenceNumber()));
						scp.setServiceControlName(sc.getServiceControlName());
						scp.setDescription(sc.getDescription());
						scp.setAssessorId(sc.getAssessorId());
						scp.setAssessmentDate(this.toDateFromDatetime(sc.getAssessmentDatetime()));
						scp.setVerifier(sc.getVerifier());
						scp.setImplementationType(sc.getImplementationType());
						scp.setControlType(sc.getControlType());
						if (sc.getDocumentationUrl() != null) {
							for (String docUrl : (List<String>)sc.getDocumentationUrl()) {
								scp.getDocumentationUrls().add(docUrl);
							}
						}
						if (sc.getVerificationDatetime() != null) {
							scp.setVerificationDate(this.toDateFromDatetime(sc.getVerificationDatetime()));
						}
						rp.getServiceControls().add(scp);
					}
				}
				pojo.getSecurityRisks().add(rp);
			}
		}
//		if (moa.getServiceControl() != null) {
//			for (ServiceControl sc : (List<ServiceControl>)moa.getServiceControl()) {
//				ServiceControlPojo scp = new ServiceControlPojo();
//				scp.setServiceId(sc.getServiceId());
//				scp.setServiceControlId(sc.getServiceControlId());
//				scp.setSequenceNumber(this.toIntFromString(sc.getSequenceNumber()));
//				scp.setServiceControlName(sc.getServiceControlName());
//				scp.setDescription(sc.getDescription());
//				scp.setAssessorId(sc.getAssessorId());
//				scp.setAssessmentDate(this.toDateFromDatetime(sc.getAssessmentDatetime()));
//				scp.setVerifier(sc.getVerifier());
//				if (sc.getVerificationDatetime() != null) {
//					scp.setVerificationDate(this.toDateFromDatetime(sc.getVerificationDatetime()));
//				}
//				pojo.getServiceControls().add(scp);
//			}
//		}
//		if (moa.getServiceGuideline() != null) {
//			for (ServiceGuideline sg : (List<ServiceGuideline>)moa.getServiceGuideline()) {
//				ServiceGuidelinePojo sgp = new ServiceGuidelinePojo();
//				sgp.setServiceId(sg.getServiceId());
//				sgp.setSequenceNumber(this.toIntFromString(sg.getSequenceNumber()));
//				sgp.setServiceGuidelineName(sg.getServiceGuidelineName());
//				sgp.setDescription(sg.getDescription());
//				sgp.setAssessorId(sg.getAssessorId());
//				sgp.setAssessmentDate(this.toDateFromDatetime(sg.getAssessmentDatetime()));
//				pojo.getServiceGuidelines().add(sgp);
//			}
//		}
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

//			String authUserId = this.getAuthUserIdForHALS();
//			actionable.getAuthentication().setAuthUserId(authUserId);
//			info("[getSecurityAssessmentsForFilter] query object is: " + queryObject.toXmlString());

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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		String downloadType = req.getParameter( "type" );
		String userId = req.getParameter("userId");	// ppid of user logged into the VPCP app
//		String serviceId = req.getParameter( "serviceId" );
		String serviceIds = req.getParameter( "serviceIds" );
//		info("serviceId=" + serviceId);
		info("serviceIds=" + serviceIds);
//		boolean isSingle=false;
		boolean isMultiple=false;
		boolean isAll=false;
		
//		if (serviceId == null) {
//			isSingle=false;
//		}
//		else {
//			isSingle=true;
//		}
		
		if (serviceIds == null) {
			isMultiple=false;
		}
		else {
			isMultiple=true;
		}
		
		if (!isMultiple) {
			isAll=true;
		}
		else {
			isAll=false;
		}

		String userAgent = req.getHeader("User-Agent");
        info("[doGet] User agent: " + userAgent);
		if (userAgent == null) {
	        info("[doGet] User-Agent is null.  Exception condition.");
			throw new ServletException("User-Agent is null.  Exception condition.");
		}

		List<AWSServicePojo> services = new java.util.ArrayList<AWSServicePojo>();
//		if (isSingle) {
//			// TODO: just get the single service for the id passed in 
//			// and build assessment report off of that.
//			AWSServiceQueryFilterPojo filter = new AWSServiceQueryFilterPojo();
//			filter.setServiceId(serviceId);
//			AWSServiceQueryResultPojo result = this.getServicesForFilter(filter);
//			services.addAll(result.getResults());
//		}
//		else 
		if (isMultiple) {
			// TODO: parse the array of service ids, retrieve each service and 
			// build assessment report off of that resulting service list
			String delimIds = serviceIds.substring(1, serviceIds.length() - 1);
			info("delimIds: " + delimIds);
			String[] id_array = delimIds.split(",");
			info("id_array: " + id_array);
			info("there are " + id_array.length + " service ids passed in.");
			for (String svc_id : id_array) {
				AWSServiceQueryFilterPojo filter = new AWSServiceQueryFilterPojo();
				filter.setServiceId(svc_id);
				AWSServiceQueryResultPojo result = this.getServicesForFilter(filter);
				services.addAll(result.getResults());
			}
		}
		else {
			// TODO: get all services and build assessment report off of that
			AWSServiceQueryResultPojo result = this.getServicesForFilter(null);
			services.addAll(result.getResults());
		}

        try {
        	info("generating security assessment for " + services.size() + " services.");
        	this.generateSecurityAssessment(resp, userId, services);
        }
        catch (IOException e) {
        	e.printStackTrace();
        	throw e;
        }
        return;
	}
	
	private void generateSecurityAssessment(HttpServletResponse resp, String userId, List<AWSServicePojo> services) throws IOException {
        int BUFFER = 1024 * 100;
        resp.setContentType( "text/html; charset=utf-8" );
//        String contentDisposition = " attachment; filename=" + "\"" + key_name + "\"";
//        info("Content-Disposition: " + contentDisposition);
//        resp.setHeader( "Content-Disposition", contentDisposition);
        Iterator<String> headers = resp.getHeaderNames().iterator();
        info("response headers:");
        while (headers.hasNext()) {
        	String header = headers.next();
        	info(header + "=" + resp.getHeader(header));
        }
        
		info("creating output stream to client (HttpServletResponse).");
        ServletOutputStream outputStream = resp.getOutputStream();
		info("created output stream to client (HttpServletResponse).");

//        resp.setContentLength( Long.valueOf( o.getObjectMetadata().getContentLength() ).intValue() );
//        resp.setBufferSize( BUFFER );
        
//	    byte[] read_buf = new byte[1024];
//	    int read_len = 0;
		info("writing to HttpServletResponse output stream....");
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("<HTML><b>Assessment report for " + services.size() + " services for user: " + userId + ".</b></HTML>");
		outputStream.write(sbuf.toString().getBytes());
//	    while ((read_len = s3is.read(read_buf)) > 0) {
//	    	outputStream.write(read_buf, 0, read_len);
//	    }
//		info("done writing to HttpServletResponse output stream....");
//		
//		info("closing s3 object's input stream");
//	    s3is.close();
//		info("closed s3 object's input stream");
		
		info("closing HttpServletResponse output stream.");
	    outputStream.close();
		info("closed HttpServletResponse output stream.");
	}

	private boolean isMacOS(String userAgent) {
		if (userAgent.toLowerCase().indexOf("mac") >= 0) {
			return true;
		}
		return false;
	}
	private boolean isWindowsOS(String userAgent) {
		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
			return true;
		}
		return false;
	}
	private boolean isLinuxOS(String userAgent) {
		if (userAgent.toLowerCase().indexOf("linux") >= 0) {
			return true;
		}
		return false;
	}
	private Properties getS3Props() throws ServletException {
		Properties s3Props;
		try {
			s3Props = getAppConfig().getProperties("AWSS3Properties");
			return s3Props;
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	private String getS3AccessId() throws ServletException {
		Properties awsCredsProps;
		try {
			awsCredsProps = getAppConfig().getProperties("AWSCredentials");
			String accessId = awsCredsProps.getProperty("s3-accessId");
			return accessId;
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	private String getS3SecretKey() throws ServletException {
		Properties awsCredsProps;
		try {
			awsCredsProps = getAppConfig().getProperties("AWSCredentials");
			String secretKey = awsCredsProps.getProperty("s3-secretKey");
			return secretKey;
		} catch (EnterpriseConfigurationObjectException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

//	private void downloadTkiClient(HttpServletResponse resp, String accessId, String secretKey, String bucket_name, String key_name) throws IOException {
//		AWSCredentials credentials = new BasicAWSCredentials(accessId, secretKey);
//		AWSStaticCredentialsProvider credProvider = new AWSStaticCredentialsProvider(credentials);
//		AmazonS3ClientBuilder s3Builder = AmazonS3ClientBuilder.standard();
//		s3Builder.setCredentials(credProvider);
//		final AmazonS3 s3 = s3Builder.build();
//
//		info("getting key: " + key_name + " from the bucket: " + bucket_name);
//	    S3Object o = s3.getObject(bucket_name, key_name);
//		info("got key: " + key_name + " from the bucket: " + bucket_name);
//	    
//		info("getting object content.");
//	    S3ObjectInputStream s3is = o.getObjectContent();
//		info("got object content.");
//
//        int BUFFER = 1024 * 100;
//        resp.setContentType( "application/octet-stream" );
//        String contentDisposition = " attachment; filename=" + "\"" + key_name + "\"";
//        info("Content-Disposition: " + contentDisposition);
//        resp.setHeader( "Content-Disposition", contentDisposition);
//        Iterator<String> headers = resp.getHeaderNames().iterator();
//        info("response headers:");
//        while (headers.hasNext()) {
//        	String header = headers.next();
//        	info(header + "=" + resp.getHeader(header));
//        }
//        
//		info("creating output stream to client (HttpServletResponse).");
//        ServletOutputStream outputStream = resp.getOutputStream();
//		info("created output stream to client (HttpServletResponse).");
//
//        resp.setContentLength( Long.valueOf( o.getObjectMetadata().getContentLength() ).intValue() );
//        resp.setBufferSize( BUFFER );
//        
//	    byte[] read_buf = new byte[1024];
//	    int read_len = 0;
//		info("writing to HttpServletResponse output stream....");
//	    while ((read_len = s3is.read(read_buf)) > 0) {
//	    	outputStream.write(read_buf, 0, read_len);
//	    }
//		info("done writing to HttpServletResponse output stream....");
//		
//		info("closing s3 object's input stream");
//	    s3is.close();
//		info("closed s3 object's input stream");
//		
//		info("closing HttpServletResponse output stream.");
//	    outputStream.close();
//		info("closed HttpServletResponse output stream.");
//	}

	private void info(String msg) {
		log.info(LOG_TAG + msg);
	}

	public static AppConfig getAppConfig() {
		return appConfig;
	}

	public static void setAppConfig(AppConfig appConfig) {
		SecurityAssessmentServlet.appConfig = appConfig;
	}
}
