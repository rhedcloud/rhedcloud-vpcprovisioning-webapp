package edu.emory.oit.vpcprovisioning.presenter.tou;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainTermsOfUseAgreementView extends Editor<TermsOfUseAgreementPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteTermsOfUseAgreement();

		/**
		 * Create a new account or save the current account based on the values in the
		 * inputs.
		 */
		void saveTermsOfUseAgreement();
		TermsOfUseAgreementPojo getTermsOfUseAgreement();
		public boolean isValidTermsOfUseAgreementId(String value);
		public boolean isValidTermsOfUseAgreementName(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w);
		public void logMessageOnServer(final String message);
		public void setTermsOfUseSummary(TermsOfUseSummaryPojo summary);
		public TermsOfUseSummaryPojo getTermsOfUseSummary();
		public boolean isTermsOfUseAgreementSaved();
		public void setTermsOfUseDialog(DialogBox termsOfUseDialog);
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
	void setTermsOfUseAgreementIdViolation(String message);
	void setTermsOfUseAgreementNameViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);


}
