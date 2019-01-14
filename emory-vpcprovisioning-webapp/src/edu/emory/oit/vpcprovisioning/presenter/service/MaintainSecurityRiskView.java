package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.CounterMeasurePojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface MaintainSecurityRiskView extends Editor<SecurityRiskPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String userId, Widget w);
		public void logMessageOnServer(final String message);
		
		void saveAssessment(boolean closeRiskDialog);
		void setSecurityAssessment(ServiceSecurityAssessmentPojo assessment);
		ServiceSecurityAssessmentPojo getSecurityAssessment();

		AWSServicePojo getService();
		void setService(AWSServicePojo service);

		void deleteSecurityRisk(SecurityRiskPojo selected);
		void deleteCounterMeasure(CounterMeasurePojo selected);
		void deleteCounterMeasures(List<CounterMeasurePojo> selected);
		SecurityRiskPojo getSecurityRisk();
		public void setDirectoryPerson(DirectoryPersonPojo pojo);
		public DirectoryPersonPojo getDirectoryPerson();
		
		void createCounterMeasure();
		void maintainCounterMeasure(CounterMeasurePojo selected);
		void refreshCounterMeasureList(final UserAccountPojo user);
	}

	/**
	 * Specify whether the view is editing an existing account or creating a new
	 * account.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the account is loaded.
	 * 
	 * @param locked true to lock, false to unlock
	 */
	void setLocked(boolean locked);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	
	void setRiskLevelItems(List<String> riskLevels);
	void setCounterMeasureStatusItems(List<String> statuses);
	void setCounterMeasures(List<CounterMeasurePojo> counterMeasures);
	void removeCounterMeasureFromView(CounterMeasurePojo pojo);
	public void showCounterMeasureMaintenanceDialog(final boolean isEdit, final CounterMeasurePojo selected);
}
