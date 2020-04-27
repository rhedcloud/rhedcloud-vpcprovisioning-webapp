package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.RiskCalculationPropertiesPojo;
import edu.emory.oit.vpcprovisioning.shared.RiskLevelCalculationPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface CalculateSecurityRiskView extends Editor<AWSServicePojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w);
		public void logMessageOnServer(final String message);
		public AWSServicePojo getService();
		public ServiceSecurityAssessmentPojo getAssessment();
		public SecurityRiskPojo getRisk();
		public RiskLevelCalculationPojo getRiskLevelCalculation();
	}

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void clear();
	void setEditing(boolean isEditing);
	void createWizardQuestionPanel(RiskCalculationPropertiesPojo rcp);
	void createWizardTablePanel(RiskCalculationPropertiesPojo rcp);
	void setInitialWizardPanel();
	void setRiskCalculationProperties(List<RiskCalculationPropertiesPojo> rcps);
}
