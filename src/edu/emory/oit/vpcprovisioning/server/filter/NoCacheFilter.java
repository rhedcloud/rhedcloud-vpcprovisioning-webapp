package edu.emory.oit.vpcprovisioning.server.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public final class NoCacheFilter implements Filter {
	private static final int DAYS_PER_YEAR = 365;
	private static final long MILLISECONDS_PER_DAY = 86400000L;
	private final Logger logger = Logger.getLogger(NoCacheFilter.class);
	private String welcomeFileName;
	public String getWelcomeFileName() {
		return welcomeFileName;
	}

	/**
	 * Define the name of the welcome file so this filter can turn
	 * off caching of the file.
	 * @param welcomeFileName The name of the welcome-file from web.xml
	 */
	public void setWelcomeFileName(final String welcomeFileName) {
		this.welcomeFileName = welcomeFileName;
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException {
		final String fileName = arg0.getInitParameter("welcomeFileName");
		this.setWelcomeFileName(fileName);
		logger.info("initialized with welcome file " + fileName);
	}

	@Override
	public void destroy() {
	}


	@Override
	public void doFilter(
			final ServletRequest arg0,
			final ServletResponse response,
			final FilterChain arg2) throws IOException, ServletException {
		
		final HttpServletRequest httpRequest = (HttpServletRequest) arg0;
		final String requestURI = httpRequest.getRequestURI();
		final String contextPath = httpRequest.getContextPath();
		logger.debug("Request for " + requestURI);

		if (requestURI.endsWith(contextPath)
				|| requestURI.endsWith(contextPath + "/")
				|| requestURI.endsWith(welcomeFileName)
				|| requestURI.endsWith(".nocache.js")) {
			
			logger.trace(
				"Filtering a request for the default document, welcome file, or nocache.js file. Disabling browser cache of this document");
			final HttpServletResponse httpResponse = (HttpServletResponse) response;
			final long now = Calendar.getInstance().getTimeInMillis();
			httpResponse.setDateHeader("Date", now);
			httpResponse.setDateHeader("Expires", now - MILLISECONDS_PER_DAY);
			httpResponse.setHeader("Pragma", "no-cache");
			httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		} 
		else if (requestURI.endsWith(".cache.html") || requestURI.endsWith(".cache.js")) {
			//Cache GWT generated cacheable resources for a year
			final Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, DAYS_PER_YEAR);
			//HTTP header date format: Thu, 01 Dec 1994 16:00:00 GMT
			final String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
			((HttpServletResponse) response).setHeader("Expires", o);
			logger.trace("Set a far future expires header on resource " + requestURI + " to " + o);
		}

		arg2.doFilter(arg0, response);
	}
}