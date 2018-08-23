package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.presenter.PresentsConfirmation;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public interface ListSecurityRiskView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		void selectSecurityRisk(SecurityRiskPojo selected);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		void deleteSecurityRisk(SecurityRiskPojo service);
		public void logMessageOnServer(final String message);
		public void setAssessment(ServiceSecurityAssessmentPojo assessment);
		public ServiceSecurityAssessmentPojo getAssessment();
		public void setUserLoggedIn(UserAccountPojo user);
		public UserAccountPojo getUserLoggedIn();
		public AWSServicePojo getService();
		void refreshList(final UserAccountPojo user);
	}

	/**
	 * Clear the list of case records.
	 */
	void clearList();

	/**
	 * Sets the new presenter, and calls {@link Presenter#stop()} on the previous
	 * one.
	 */
	void setPresenter(Presenter presenter);

	void setSecurityRisks(List<SecurityRiskPojo> securityRisks);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeSecurityRiskFromView(SecurityRiskPojo securityRisks);
}
