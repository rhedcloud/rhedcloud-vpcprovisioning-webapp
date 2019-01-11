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
		getView().showPleaseWaitDialog("Generating Service Assessment Report, please wait...");
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
		// TODO: for each service in the list, get the assessment and generate 
		// the appropriate HTML assessment content and pass it to the view
		
		String htmlTop = "<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<style>\n" + 
				"table {\n" + 
				"  font-family: arial, sans-serif;\n" + 
				"  border-collapse: collapse;\n" + 
				"  width: 100%;\n" + 
				"}\n" + 
				"\n" + 
				"td, th {\n" + 
				"  border: 1px solid #dddddd;\n" + 
				"  text-align: left;\n" + 
				"  padding: 8px;\n" + 
				"}\n" + 
				"\n" + 
				"tr:nth-child(even) {\n" + 
				"  background-color: #ebebeb;\n" + 
				"}\n" + 
				"</style>\n" + 
				"</head>\n" + 
				"<body>";
		final String htmlBottom = "</body>\n" + 
				"</html>";

		final StringBuffer sbView = new StringBuffer();
		final StringBuffer sbPrint = new StringBuffer();
		sbPrint.append(htmlTop);
		sbPrint.append("<h1>Assessment report for " + serviceList.size() + 
				" services for user: " + 
				userLoggedIn.getPublicId() + ".</h1>");
		sbView.append("<h1>Assessment report for " + serviceList.size() + 
				" services for user: " + 
				userLoggedIn.getPublicId() + ".</h1>");
		
//		sbPrint.append("<p>");
//		sbView.append("<p>");
		
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
					sbPrint.append("<h2>Service name: " + svc.getAwsServiceName() + "</h2>");
					sbView.append("<h2>Service name: " + svc.getAwsServiceName() + "</h2>");

					ServiceSecurityAssessmentPojo assessment = sas.getAssessment();
					if (assessment != null) {
						sbPrint.append("<ul style=\"list-style-type:none\">");
						sbView.append("<ul style=\"list-style-type:none\">");
						
						// security risks
						sbPrint.append("<li><h3>Security Risks: " + assessment.getSecurityRisks().size() + "</h3></li>");
						sbView.append("<li><h3>Security Risks: " + assessment.getSecurityRisks().size() + "</h3></li>");
						if (assessment.getSecurityRisks().size() > 0) {
							sbPrint.append("<table>");
							sbPrint.append("<tr><th>Name</th><th>Description</th><th>Risk Level</th><th>Assessor</th><th>Assessment Date</th></tr>");
							sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
							sbView.append("<tr>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Name</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Risk Level</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessor</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessment Date</th>"
									+ "</tr>");
							for (SecurityRiskPojo risk : assessment.getSecurityRisks()) {
								sbPrint.append("<tr>" + 
									"<td>" + risk.getSecurityRiskName() + "</td>" + 
									"<td>" + risk.getDescription() + "</td>" + 
									"<td>" + risk.getRiskLevel() + "</td>" + 
									"<td>" + risk.getAssessorId() + "</td>" + 
									"<td>" + dateFormat_short.format(risk.getAssessmentDate()) + "</td>" + 
									"</tr>");
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getSecurityRiskName() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getDescription() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getRiskLevel() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + risk.getAssessorId() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(risk.getAssessmentDate()) + "</td>" + 
									"</tr>");
								
								// TODO: counter measures
								if (risk.getCouterMeasures().size() > 0) {
									
								}
							}
							sbPrint.append("</table>");
							sbView.append("</table>");
						}
						else {
							sbPrint.append("<h4>No Security Risks Documented</h4>");
							sbView.append("<h4>No Security Risks Documented</h4>");
						}
						
						// security controls
						sbPrint.append("<li><h3>Security Controls: " + assessment.getServiceControls().size() + "</h3></li>");
						sbView.append("<li><h3>Security Controls: " + assessment.getServiceControls().size() + "</h3></li>");
						if (assessment.getServiceControls().size() > 0) {
							sbPrint.append("<table>");
							sbPrint.append("<tr>"
									+ "<th>Name</th><th>Description</th><th>Assessor ID</th><th>Assessment Date</th><th>Verifier ID</th><th>Verification Date</th></tr>");
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
								sbPrint.append("<tr>" + 
									"<td>" + control.getServiceControlName() + "</td>" + 
									"<td>" + control.getDescription() + "</td>" +  
									"<td>" + control.getAssessorId() + "</td>" + 
									"<td>" + dateFormat_short.format(control.getAssessmentDate()) + "</td>" + 
									"<td>" + control.getVerifier() + "</td>" +
									"<td>" + dateFormat_short.format(control.getVerificationDate()) + "</td>" +
									"</tr>");
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getServiceControlName() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getDescription() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getAssessorId() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(control.getAssessmentDate()) + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + control.getVerifier() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(control.getVerificationDate()) + "</td>" + 
									"</tr>");
							}
							sbPrint.append("</table>");
							sbView.append("</table>");
						}
						else {
							sbPrint.append("<h4>No Security Controls Documented</h4>");
							sbView.append("<h4>No Security Controls Documented</h4>");
						}
						
						// security guidelines
						sbPrint.append("<li><h3>Service Guidelines: " + assessment.getServiceGuidelines().size() + "<h3></li>");
						sbView.append("<li><h3>Service Guidelines: " + assessment.getServiceGuidelines().size() + "</h3></li>");
						if (assessment.getServiceGuidelines().size() > 0) {
							sbPrint.append("<table>");
							sbPrint.append("<tr>"
									+ "<th>Name</th><th>Description</th><th>Assessor</th><th>Assessment Date</th></tr>");
							sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
							sbView.append("<tr>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Name</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessor</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Assessment Date</th>"
									+ "</tr>");
							for (ServiceGuidelinePojo guideline : assessment.getServiceGuidelines()) {
								sbPrint.append("<tr>" + 
									"<td>" + guideline.getServiceGuidelineName() + "</td>" + 
									"<td>" + guideline.getDescription() + "</td>" +  
									"<td>" + guideline.getAssessorId() + "</td>" + 
									"<td>" + dateFormat_short.format(guideline.getAssessmentDate()) + "</td>" + 
									"</tr>");
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + guideline.getServiceGuidelineName() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + guideline.getDescription() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + guideline.getAssessorId() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + dateFormat_short.format(guideline.getAssessmentDate()) + "</td>" + 
									"</tr>");
							}
							sbPrint.append("</table>");
							sbView.append("</table>");
						}
						else {
							sbPrint.append("<h4>No Service Guidelines Documented</h4>");
							sbView.append("<h4>No Service Guidelines Documented</h4>");
						}

						// test plan
						sbPrint.append("<li><h3>Test Plan: " + ((assessment.getServiceTestPlan() != null) ? "Yes" : "No") + "</h3></li>");
						sbView.append("<li><h3>Test Plan: " + ((assessment.getServiceTestPlan() != null) ? "Yes" : "No") + "</h3></li>");
						if (assessment.getServiceTestPlan() != null) {
							ServiceTestPlanPojo tp = assessment.getServiceTestPlan();
							
							// requirements
							sbPrint.append("<table>");
							sbPrint.append("<tr>"
									+ "<th>Sequence</th><th>Description</th><th>Tests</th></tr>");
							sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
							sbView.append("<tr>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Sequence</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
									+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Tests</th>"
									+ "</tr>");
							
							for (ServiceTestRequirementPojo str : tp.getServiceTestRequirements()) {
								sbPrint.append("<tr>" + 
									"<td>" + str.getSequenceNumber() + "</td>" + 
									"<td>" + str.getDescription() + "</td>");  
								sbView.append("<tr>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + str.getSequenceNumber() + "</td>" + 
									"<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + str.getDescription() + "</td>"); 

								// tests
								sbPrint.append("<td>");
								sbView.append("<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">"); 
								sbPrint.append("<table>");
								sbPrint.append("<tr>"
										+ "<th>Sequence</th><th>Description</th><th>Expected Result</th><th>Steps</th></tr>");
								sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
								sbView.append("<tr>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Sequence</th>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Expected Result</th>"
										+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Steps</th>"
										+ "</tr>");

								for (ServiceTestPojo test : str.getServiceTests()) {
									sbPrint.append("<tr>" + 
										"<td width=\"5%\">" + test.getSequenceNumber() + "</td>" + 
										"<td width=\"30%\">" + test.getDescription() + "</td>" +  
										"<td width=\"10%\">" + test.getServiceTestExpectedResult() + "</td>");  
									sbView.append("<tr>" + 
										"<td width=\"5%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + test.getSequenceNumber() + "</td>" + 
										"<td width=\"30%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + test.getDescription() + "</td>" + 
										"<td width=\"10%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + test.getServiceTestExpectedResult() + "</td>"); 

									// steps
									sbPrint.append("<td>");
									sbView.append("<td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">"); 
									sbPrint.append("<table>");
									sbPrint.append("<tr>"
											+ "<th>Sequence</th><th>Description</th></tr>");
									sbView.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 100%;\">");
									sbView.append("<tr>"
											+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Sequence</th>"
											+ "<th style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">Description</th>"
											+ "</tr>");

									for (ServiceTestStepPojo step : test.getServiceTestSteps()) {
										sbPrint.append("<tr>" + 
											"<td width=\"5%\">" + step.getSequenceNumber() + "</td>" + 
											"<td width=\"50%\">" + step.getDescription() + "</td>");
										sbView.append("<tr>" + 
											"<td width=\"5%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + step.getSequenceNumber() + "</td>" + 
											"<td width=\"50%\" style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + step.getDescription() + "</td>"); 
									}
									sbPrint.append("</table>");
									sbView.append("</table>");

									sbPrint.append("</td>");
									sbView.append("</td>");

									// end steps

									sbPrint.append("</tr>"); 
									sbView.append("</tr>"); 
								}
								sbPrint.append("</table>");
								sbView.append("</table>");

								sbPrint.append("</td>");
								sbView.append("</td>");
								// end tests

								sbPrint.append("</tr>");
								sbView.append("</tr>");
							}
							sbPrint.append("</table>");
							sbView.append("</table>");
							// end requirements
						}
						
						sbPrint.append("</ul>");
						sbView.append("</ul>");
					}
					else {
						sbPrint.append("<h4>No assessment performed yet</h4>");
						sbView.append("<h4>No assessment performed yet</h4>");
					}
				}
				sbPrint.append(htmlBottom);
				getView().setAssessmentReportToPrint(sbPrint.toString());
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
