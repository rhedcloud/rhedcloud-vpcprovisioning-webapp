package edu.emory.oit.vpcprovisioning.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openeai.config.AppConfig;
import org.openeai.config.EnterpriseConfigurationObjectException;
import org.openeai.utils.config.AppConfigFactory;
import org.openeai.utils.config.AppConfigFactoryException;
import org.openeai.utils.config.SimpleAppConfigFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.RpcException;

@SuppressWarnings("serial")
public class AmazonS3DownloadServlet extends HttpServlet {
	private static final String DOWLOAD_TYPE_TKICLIENT = "tkiclient";
	private static final String LOG_TAG = " [AmazonS3DownloadServlet] ";
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
		configDocPath = System.getProperty("configDocPath");
		if (configDocPath == null) {
			configDocPath = System.getenv("configDocPath");
		}
		if (configDocPath == null) {
			configDocPath = this.getServletConfig().getInitParameter(
					"configDocPath");
		}
		if (configDocPath == null) {
			configDocPath = "Unknown configDocPath";
		}

		appId = System.getProperty("appId");
		if (appId == null) {
			appId = System.getenv("appId");
		}
		if (appId == null) {
			appId = this.getServletConfig().getInitParameter("appId");
		}
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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String downloadType = req.getParameter( "type" );
		info("download type=" + downloadType);
		if (downloadType == null) {
			throw new ServletException("NULL download type.  Cannot continue.");
		}
		
		String userAgent = req.getHeader("User-Agent");
        info("[doGet] User agent: " + userAgent);
		if (userAgent == null) {
	        info("[doGet] User-Agent is null.  Exception condition.");
			throw new ServletException("User-Agent is null.  Exception condition.");
		}

		if (downloadType.trim().equalsIgnoreCase(DOWLOAD_TYPE_TKICLIENT)) {
			String accessId = this.getS3AccessId();
			String secretKey = this.getS3SecretKey();
			String bucket_name = getS3Props().getProperty("tkiClient-bucketName");
			
			// key_name will have to be OS specific
			String key_name = null;
			
			if (isMacOS(userAgent)) {
		        info("[doGet] Client OS is MAC");
				key_name = getS3Props().getProperty("tkiClient-keyName-mac");
			}
			else if (isWindowsOS(userAgent)) {
		        info("[doGet] Client OS is Windows");
				key_name = getS3Props().getProperty("tkiClient-keyName-windows");
			}
			else if (isLinuxOS(userAgent)) {
		        info("[doGet] Client OS is Linux");
				key_name = getS3Props().getProperty("tkiClient-keyName-linux");
			}
			else {
		        info("[doGet] Client OS is Unknown.  User-Agent is: '" + userAgent + "'  Exception condition.");
				throw new ServletException("Unknown operating system.  User-Agent is: " + userAgent);
			}
			
	        info("[doGet] " + downloadType + " accessId=" + accessId);
	        info("[doGet] " + downloadType  + " secretKey=" + secretKey);
	        info("[doGet] " + downloadType + " bucketName="  + bucket_name);
	        info("[doGet] " + downloadType + " keyName="  + key_name);
	        
	        try {
		        this.downloadTkiClient(resp, accessId, secretKey, bucket_name, key_name);
	        }
	        catch (IOException e) {
	        	e.printStackTrace();
	        	throw e;
	        }
	        return;
		}
		
		throw new ServletException("Unknowns download type.  Cannot continue.");
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
	
	private void downloadTkiClient(HttpServletResponse resp, String accessId, String secretKey, String bucket_name, String key_name) throws IOException {
		AWSCredentials credentials = new BasicAWSCredentials(accessId, secretKey);
		AWSStaticCredentialsProvider credProvider = new AWSStaticCredentialsProvider(credentials);
		AmazonS3ClientBuilder s3Builder = AmazonS3ClientBuilder.standard();
		s3Builder.setCredentials(credProvider);
		final AmazonS3 s3 = s3Builder.build();

		info("getting key: " + key_name + " from the bucket: " + bucket_name);
	    S3Object o = s3.getObject(bucket_name, key_name);
		info("got key: " + key_name + " from the bucket: " + bucket_name);
	    
		info("getting object content.");
	    S3ObjectInputStream s3is = o.getObjectContent();
		info("got object content.");

        int BUFFER = 1024 * 100;
        resp.setContentType( "application/octet-stream" );
        String contentDisposition = " attachment; filename=" + "\"" + key_name + "\"";
        info("Content-Disposition: " + contentDisposition);
        resp.setHeader( "Content-Disposition", contentDisposition);
        Iterator<String> headers = resp.getHeaderNames().iterator();
        info("response headers:");
        while (headers.hasNext()) {
        	String header = headers.next();
        	info(header + "=" + resp.getHeader(header));
        }
        
		info("creating output stream to client (HttpServletResponse).");
        ServletOutputStream outputStream = resp.getOutputStream();
		info("created output stream to client (HttpServletResponse).");

        resp.setContentLength( Long.valueOf( o.getObjectMetadata().getContentLength() ).intValue() );
        resp.setBufferSize( BUFFER );
        
	    byte[] read_buf = new byte[1024];
	    int read_len = 0;
		info("writing to HttpServletResponse output stream....");
	    while ((read_len = s3is.read(read_buf)) > 0) {
	    	outputStream.write(read_buf, 0, read_len);
	    }
		info("done writing to HttpServletResponse output stream....");
		
		info("closing s3 object's input stream");
	    s3is.close();
		info("closed s3 object's input stream");
		
		info("closing HttpServletResponse output stream.");
	    outputStream.close();
		info("closed HttpServletResponse output stream.");
	}

	private void info(String msg) {
		log.info(LOG_TAG + msg);
	}

	public static AppConfig getAppConfig() {
		return appConfig;
	}

	public static void setAppConfig(AppConfig appConfig) {
		AmazonS3DownloadServlet.appConfig = appConfig;
	}
}
