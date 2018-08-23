package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.presenter.PresentsConfirmation;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public interface MaintainServiceView extends Editor<AWSServicePojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteService();

		/**
		 * Create a new account or save the current account based on the values in the
		 * inputs.
		 */
		void saveService(boolean listServices);
		AWSServicePojo getService();
		public boolean isValidServiceId(String value);
		public boolean isValidServiceName(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w);
		public void logMessageOnServer(final String message);
		void deleteSecurityAssessment(ServiceSecurityAssessmentPojo selected);
	}

	/**
	 * Get the driver used to edit tasks in the view.
	 */
	//	  RequestFactoryEditorDriver<TaskProxy, ?> getEditorDriver();

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
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setServiceIdViolation(String message);
	void setServiceNameViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void setServiceStatusItems(List<String> serviceStatusTypes);
	void setAssessments(List<ServiceSecurityAssessmentPojo> assessments);
	void removeAssessmentFromView(ServiceSecurityAssessmentPojo assessment);
	void clearAssessmentList();

}
