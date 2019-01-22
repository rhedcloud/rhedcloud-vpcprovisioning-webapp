package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.CounterMeasurePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityAssessmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityAssessmentSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityAssessmentSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestRequirementPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestStepPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ServiceAssessmentReportPresenter extends PresenterBase implements ServiceAssessmentReportView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private List<AWSServicePojo> serviceList = new java.util.ArrayList<AWSServicePojo>();
	private UserAccountPojo userLoggedIn;
	private ServiceSecurityAssessmentPojo assessment;

	/**
	 * For creating a new service.
	 */
	public ServiceAssessmentReportPresenter(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		clientFactory.getServiceAssessmentReportView().setPresenter(this);
	}

	/**
	 * For editing an existing service.
	 */
	public ServiceAssessmentReportPresenter(ClientFactory clientFactory, List<AWSServicePojo> services) {
		this.clientFactory = clientFactory;
		this.serviceList = services;
		clientFactory.getServiceAssessmentReportView().setPresenter(this);
	}

	public ServiceAssessmentReportPresenter(ClientFactory clientFactory, List<AWSServicePojo> services, ServiceSecurityAssessmentPojo assessment) {
		this.clientFactory = clientFactory;
		this.serviceList = services;
		this.assessment = assessment;
		clientFactory.getServiceAssessmentReportView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().clear();
		getView().showPleaseWaitDialog("Generating Service Assessment Report, please wait...(potential long running task)");
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception determining user logged in", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server determining the user logged in.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				// Request the service assessment list now.
				refreshReport(user);
				getView().initPage();
				getView().setInitialFocus();
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void refreshReport(final UserAccountPojo user) {
		// for each service in the list, get the assessment and generate 
		// the appropriate HTML assessment content and pass it to the view
		
		final StringBuffer sbView = new StringBuffer();
		String personInfo = userLoggedIn.getFullName() + " (" + userLoggedIn.getPublicId() + ")";
		sbView.append("<h2>AWS Service Inventory and Security Risk Assessment Report</h2>");
		
		// add some header content that describes what's in the report
		sbView.append("<h3>The following report describes each AWS Service and provides security risk "
				+ "assessment details.  The assessment may include:<ul><li>Security Risks and any counter "
				+ "measures associated to that risk</li><li>Controls that have been put in place to "
				+ "mitigate the risk</li><li>Guidelines that describe how the service should be used to avoid the "
				+ "known risks</li><li>Test Plans that have been used to test any counter measures or "
				+ "controls associated to the risks for a given service</li></ul>"
				+ "The content in this report will change as more analysis and assessment data is entered.</h3>");
		
		List<String> serviceIds = new java.util.ArrayList<String>();
		for (final AWSServicePojo svc : serviceList) {
			serviceIds.add(svc.getServiceId());
		}
		
		AsyncCallback<SecurityAssessmentSummaryQueryResultPojo> callback = new AsyncCallback<SecurityAssessmentSummaryQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				GWT.log("Exception Retrieving Assessment", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the list of Security Assessments associated to this Service.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(SecurityAssessmentSummaryQueryResultPojo result) {
				for (SecurityAssessmentSummaryPojo sas : result.getResults()) {
					AWSServicePojo svc = sas.getService();
					ServiceSecurityAssessmentPojo assessment = sas.getAssessment();
					String serviceName;
					if (svc.getCombinedServiceName() != null && 
							svc.getCombinedServiceName().length() > 0) {
							serviceName = svc.getCombinedServiceName();
						}
						else if (svc.getAlternateServiceName() != null && 
								svc.getAlternateServiceName().length() > 0 ) {
							serviceName = svc.getAlternateServiceName();
						}
						else {
							serviceName = svc.getAwsServiceName();
						}

					sbView.append("<table>");
					
					// service name
					String serviceAnchorString = "<a href=\"" + svc.getAwsLandingPageUrl() + "\">" + serviceName + "</a>";
					sbView.append("<tr><td>");
					sbView.append("<b>Service name:  " + serviceAnchorString + "</b>");
					sbView.append("</td></tr>");
					
					// assessment status
					sbView.append("<tr><td>");
					sbView.append("<b>Assessment Status:  " + ((assessment != null ? assessment.getStatus() : "Not Started")) + "</b>");
					sbView.append("</td></tr>");

					String imgString;
					if (!svc.isBlocked()) {
						imgString = "<img src=\"images/green-checkbox-icon-15.jpg\" alt=\"Smiley face\" height=\"16\" width=\"16\">";
					}
					else {
						imgString = "<img src=\"images/red-circle-white-x.png\" alt=\"Smiley face\" height=\"16\" width=\"16\">";
					}
					sbView.append("<tr><td>");
					sbView.append("<b>STATUS:</b>  " + svc.getSiteStatus());
					sbView.append("</td><td>" + imgString + "</td>");
					sbView.append("</tr>");
					
					if (svc.isSiteHipaaEligible()) {
						imgString = "<img src=\"images/green-checkbox-icon-15.jpg\" alt=\"Smiley face\" height=\"16\" width=\"16\">";
					}
					else {
						imgString = "<img src=\"images/red-circle-white-x.png\" alt=\"Smiley face\" height=\"16\" width=\"16\">";
					}
					sbView.append("<tr><td>");
					sbView.append("<b>Emory HIPPA Eligibility:</b>  " + svc.getSiteHipaaEligible());
					sbView.append("</td><td>" + imgString + "</td>");
					sbView.append("</tr>");

					sbView.append("</table>");
					
					sbView.append("<p>" + ((svc.getDescription()) != null ? svc.getDescription() : "No service description") + "</p>");
					
					if (assessment != null) {
						sbView.append("<ul style=\"list-style-type:none\">");
						
						// security risks
						sbView.append("<li><h3>Security Risks: " + assessment.getSecurityRisks().size() + "</h3></li>");
						if (assessment.getSecurityRisks().size() > 0) {
							sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
							sbView.append("<tr>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Name</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Risk Level</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessor</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessment Date</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Counter Measures</th>"
									+ "</tr>");
							for (SecurityRiskPojo risk : assessment.getSecurityRisks()) {
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getSecurityRiskName() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getDescription() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getRiskLevel() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getAssessorId() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(risk.getAssessmentDate()) + "</td>");
								
									if (risk.getCouterMeasures().size() > 0) {
										// counter measures go here (nested table)
										sbView.append("<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">"); 
										sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
										sbView.append("<tr>"
												+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Status</th>"
												+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
												+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Verifier</th>"
												+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Verification Date</th>"
												+ "</tr>");
									
										for (CounterMeasurePojo cm : risk.getCouterMeasures()) {
											sbView.append("<tr>" + 
													"<td width=\"5%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + cm.getStatus() + "</td>" + 
													"<td width=\"30%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + cm.getDescription() + "</td>" + 
													"<td width=\"10%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + cm.getVerifier() + "</td>" +  
													"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(cm.getVerificationDate()) + "</td>" +
													"</tr>");
										}
										sbView.append("</table>");
										sbView.append("</td>");
									}
									else {
										sbView.append("<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">No Counter Measures documented.  NOTE:  These may be documented as Security Controls.</td>");
									}
									// end counter measures
									
									sbView.append("</tr>");
							}
							sbView.append("</table>");
						}
						else {
							sbView.append("<h4>No Security Risks Documented</h4>");
						}
						
						// security controls
						sbView.append("<li><h3>Security Controls: " + assessment.getServiceControls().size() + "</h3></li>");
						if (assessment.getServiceControls().size() > 0) {
							sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
							sbView.append("<tr>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Name</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessor</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessment Date</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Verifier</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Verification Date</th>"
									+ "</tr>");
							for (ServiceControlPojo control : assessment.getServiceControls()) {
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getServiceControlName() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getDescription() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getAssessorId() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(control.getAssessmentDate()) + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getVerifier() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(control.getVerificationDate()) + "</td>" + 
									"</tr>");
							}
							sbView.append("</table>");
						}
						else {
							sbView.append("<h4>No Security Controls Documented.  NOTE:  These may be documented as Security Risk Counter Measures.</h4>");
						}
						
						// security guidelines
						sbView.append("<li><h3>Service Guidelines: " + assessment.getServiceGuidelines().size() + "</h3></li>");
						if (assessment.getServiceGuidelines().size() > 0) {
							sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
							sbView.append("<tr>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Name</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessor</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessment Date</th>"
									+ "</tr>");
							for (ServiceGuidelinePojo guideline : assessment.getServiceGuidelines()) {
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + guideline.getServiceGuidelineName() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + guideline.getDescription() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + guideline.getAssessorId() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(guideline.getAssessmentDate()) + "</td>" + 
									"</tr>");
							}
							sbView.append("</table>");
						}
						else {
							sbView.append("<h4>No Service Guidelines Documented</h4>");
						}

						// test plan
						sbView.append("<li><h3>Test Plan: " + ((assessment.getServiceTestPlan() != null) ? "Yes" : "No") + "</h3></li>");
						if (assessment.getServiceTestPlan() != null) {
							ServiceTestPlanPojo tp = assessment.getServiceTestPlan();
							
							// requirements
							sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
							sbView.append("<tr>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Sequence</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Tests</th>"
									+ "</tr>");
							
							for (ServiceTestRequirementPojo str : tp.getServiceTestRequirements()) {
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + str.getSequenceNumber() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + str.getDescription() + "</td>"); 

								// tests
								sbView.append("<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">"); 
								sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
								sbView.append("<tr>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Sequence</th>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Expected Result</th>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Steps</th>"
										+ "</tr>");

								for (ServiceTestPojo test : str.getServiceTests()) {
									sbView.append("<tr>" + 
										"<td width=\"5%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + test.getSequenceNumber() + "</td>" + 
										"<td width=\"30%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + test.getDescription() + "</td>" + 
										"<td width=\"10%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + test.getServiceTestExpectedResult() + "</td>"); 

									// steps
									sbView.append("<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">"); 
									sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
									sbView.append("<tr>"
											+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Sequence</th>"
											+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
											+ "</tr>");

									for (ServiceTestStepPojo step : test.getServiceTestSteps()) {
										sbView.append("<tr>" + 
											"<td width=\"5%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + step.getSequenceNumber() + "</td>" + 
											"<td width=\"50%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + step.getDescription() + "</td>"); 
									}
									sbView.append("</table>");
									sbView.append("</td>");
									// end steps

									sbView.append("</tr>"); 
								}
								sbView.append("</table>");
								sbView.append("</td>");
								// end tests

								sbView.append("</tr>");
							}
							sbView.append("</table>");
							// end requirements
						}
						
						sbView.append("</ul>");
					}
					else {
						sbView.append("<h4>No assessment performed yet</h4>");
					}
					sbView.append("<hr>");
				}
				getView().setAssessmentReportToView(sbView.toString());
		        getView().hidePleaseWaitDialog();
			}
		};
		SecurityAssessmentSummaryQueryFilterPojo filter = new SecurityAssessmentSummaryQueryFilterPojo();
		filter.setServiceIds(serviceIds);
		VpcProvisioningService.Util.getInstance().getSecurityAssessmentSummariesForFilter(filter, callback);
	}

	@Override
	public void stop() {
		eventBus = null;
//		clientFactory.getServiceAssessmentReportView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	public ServiceAssessmentReportView getView() {
		return clientFactory.getServiceAssessmentReportView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public List<AWSServicePojo> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<AWSServicePojo> serviceList) {
		this.serviceList = serviceList;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public ServiceSecurityAssessmentPojo getAssessment() {
		return assessment;
	}

	public void setAssessment(ServiceSecurityAssessmentPojo assessment) {
		this.assessment = assessment;
	}
}
