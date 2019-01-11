package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.ServiceAssessmentReportView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopServiceAssessmentReport extends ViewImplBase implements ServiceAssessmentReportView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<AWSServicePojo> serviceList = new java.util.ArrayList<AWSServicePojo>();
	String assessmentHTMLPrint;

	private static DesktopServiceAssessmentReportUiBinder uiBinder = GWT
			.create(DesktopServiceAssessmentReportUiBinder.class);

	interface DesktopServiceAssessmentReportUiBinder extends UiBinder<Widget, DesktopServiceAssessmentReport> {
	}

	public DesktopServiceAssessmentReport() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DesktopServiceAssessmentReport(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField VerticalPanel assessmentPanel;
	@UiField HTML assessmentHTML;
	@UiField Button printButton;
	@UiField Button cancelButton;
	
	@UiHandler("printButton")
	void printButtonClicked(ClickEvent e) {
		String htmlTop = 
			"<!DOCTYPE html>\n" + 
			"<html>\n" + 
			"<body>";
		String htmlBottom = 
			"</body>\n" + 
			"</html>";
		String html = htmlTop + assessmentHTML.getElement().getInnerHTML() + htmlBottom;
//		print(assessmentHTMLPrint);
		print(html);
	}
	
	@UiHandler("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		// if the report was opened from the assessment maintenance page,
		// we want to go there instead of to the service list.

		if (presenter.getAssessment() != null) {
			// report was initiated from the service assessment page
			ActionEvent.fire(
				presenter.getEventBus(), 
				ActionNames.MAINTAIN_SECURITY_ASSESSMENT, 
				presenter.getServiceList().get(0), 
				presenter.getAssessment());
		}
		else {
			// report was initiated from the service list page
			ActionEvent.fire(
				presenter.getEventBus(), 
				ActionNames.GO_HOME_SERVICE);
		}
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return printButton;
	}

	@Override
	public void vpcpPromptOkay(String valueEntered) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpPromptCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmOkay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServices(List<AWSServicePojo> services) {
		this.serviceList = services;
	}

	@Override
	public void setAssessmentReportToView(String assessmentReportHTML) {
		assessmentHTML.setHTML(assessmentReportHTML);
//		assessmentHTML.clear();
//		assessmentHTML.add(new HTML(assessmentReportHTML));
	}

	@Override
	public void setAssessmentReportToPrint(String assessmentReportHTML) {
		assessmentHTMLPrint = assessmentReportHTML;
	}

	@Override
	public void clear() {
		String imgString = "<img src=\"images/ajax-loader.gif\" alt=\"Please Wait...\" align=\"middle\">";

		assessmentHTML.setHTML("<table><tr><td><h3>Generating Security Assessment "
			+ "Report...</h2></td><td>" + imgString + "</td></tr></table>");
	}

}
