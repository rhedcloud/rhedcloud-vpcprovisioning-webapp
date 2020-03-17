package edu.emory.oit.vpcprovisioning.presenter.resourcetagging;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListResourceTaggingProfileView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		void selectResourceTaggingProfile(ResourceTaggingProfilePojo selected);
		public EventBus getEventBus();
		public ResourceTaggingProfileQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		// NOTE: this one should delete ALL revisions of of the selected profile
		void deleteResourceTaggingProfile(ResourceTaggingProfilePojo selected);
		public void logMessageOnServer(final String message);
		
		void filterByNamespace(String namespace);
		void filterByProfileName(String profileName);
		void filterByNamespaceAndProfileName(String filterString);
		void filterByManagedTagNameAndValue(String tagNameAndValue);
		void clearFilter();
		void refreshList(final UserAccountPojo user);
	}

	/**
	 * Sets the new presenter, and calls {@link Presenter#stop()} on the previous
	 * one.
	 */
	void setPresenter(Presenter presenter);
	void clearList();

	void setResourceTaggingProfiles(List<ResourceTaggingProfilePojo> list);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeResourceTaggingProfileFromView(ResourceTaggingProfilePojo selected);
	void initPage();
	void setFilterTypeItems(List<String> filterTypes);


}
