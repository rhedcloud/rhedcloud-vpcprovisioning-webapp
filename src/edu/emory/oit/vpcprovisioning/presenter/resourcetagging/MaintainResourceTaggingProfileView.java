package edu.emory.oit.vpcprovisioning.presenter.resourcetagging;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.ManagedTagPojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainResourceTaggingProfileView extends Editor<ResourceTaggingProfilePojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteAccount();

		/**
		 * Create a new account or save the current account based on the values in the
		 * inputs.
		 */
		void saveResourceTaggingProfile(boolean isSimulation);
		ResourceTaggingProfilePojo getResourceTaggingProfile();
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setDirectoryMetaDataTitleOnWidget(String netId, Widget w);
		public void setSpeedChartStatusForKeyOnWidget(String key, Widget w, boolean confirmSpeedType);
		public void setSpeedChartStatusForKey(String key, Label label, boolean confirmSpeedType);
		public boolean didConfirmSpeedType();
		public void logMessageOnServer(final String message);
		public void setDirectoryPerson(DirectoryPersonPojo pojo);
		public DirectoryPersonPojo getDirectoryPerson();
		public MaintainResourceTaggingProfileView getView();
		public void setResourceTaggingProfileFilter(ResourceTaggingProfileQueryFilterPojo filter);
		public ResourceTaggingProfileQueryFilterPojo getResourceTaggingProfileFilter();
		public boolean isNewRevision();

		public void setSelectedManagedTag(ManagedTagPojo tag);
		public ManagedTagPojo getSelectedManagedTag();
		public void updateManagedTag(ManagedTagPojo tag);
		public void addManagedTag(ManagedTagPojo tag);
		public void setSelectedResourceTaggingProfile(ResourceTaggingProfilePojo selected);
		public void updateResourceTaggingProfiles(boolean isSimulation, List<ResourceTaggingProfilePojo> profiles);
		public void generateSrdWithCurrentRTP(List<String> accountIds);
		public void getAccounts();
		void filterByNamespace(String namespace);
		void filterByProfileName(String profileName);
		void filterByNamespaceAndProfileName(String filterString);
		void filterByManagedTagNameAndValue(String tagNameAndValue);
		void clearFilter();
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
	void setSpeedTypeStatus(String status);
	void setSpeedTypeColor(String color);
	Widget getSpeedTypeWidget();
	void setSpeedTypeConfirmed(boolean confirmed);
	boolean isSpeedTypeConfirmed();
	void enableAdminMaintenance();
	void disableAdminMaintenance();
	
	void showFilteredStatus();
	void hideFilteredStatus();
	void setFilterTypeItems(List<String> filterTypes);
	public void setFinancialAccountFieldLabel(String label);
	void setResourceTaggingProfiles(List<ResourceTaggingProfilePojo> profiles);
	void displaySRDs(List<SecurityRiskDetectionPojo> srds);
	void displayAccountSelectionDialogWithAccounts(List<AccountPojo> accounts);
}
