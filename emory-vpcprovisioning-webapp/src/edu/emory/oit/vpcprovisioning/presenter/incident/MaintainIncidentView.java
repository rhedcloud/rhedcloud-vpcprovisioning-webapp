package edu.emory.oit.vpcprovisioning.presenter.incident;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentRequisitionPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface MaintainIncidentView extends Editor<IncidentPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteIncident();

		/**
		 * Create a new account or save the current account based on the values in the
		 * inputs.
		 */
		void saveIncident();
		IncidentPojo getIncident();
		IncidentRequisitionPojo getIncidentRequisition();
		public boolean isValidIncidentNumber(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w);
		public void logMessageOnServer(final String message);
		
		public void setUrgency(String urgency);
		public void setImpact(String impact);
		public void setBusinessService(String businessService);
		public void setCategory(String category);
		public void setSubCategory(String subCategory);
		public void setRecordType(String recordType);
		public void setContactType(String contactType);
		public void setCmdbCi(String cmdbCi);
		public void setAssignmentGroup(String assignmentGroup);
		public void setShortDescription(String shortDescription);
		
		public void setAccount(AccountPojo account);
		public AccountPojo getAccount();
		void setIncidentType(String incidentType);
		String getIncidentType();
		void setIncidentDialog(DialogBox incidentDialog);
		DialogBox getIncidentDialog();
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
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setIncidentIdViolation(String message);
	void setIncidentNameViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void showGenerateWidgets();
	void showEditWidgets();
}
