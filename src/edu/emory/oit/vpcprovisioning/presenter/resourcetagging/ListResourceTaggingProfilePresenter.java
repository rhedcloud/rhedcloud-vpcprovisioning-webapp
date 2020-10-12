package edu.emory.oit.vpcprovisioning.presenter.resourcetagging;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ResourceTaggingProfileListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListResourceTaggingProfilePresenter extends PresenterBase implements ListResourceTaggingProfileView.Presenter {
	private static final Logger log = Logger.getLogger(ListResourceTaggingProfilePresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	ResourceTaggingProfileQueryFilterPojo filter;
	ResourceTaggingProfilePojo resourceTaggingProfile;
	ResourceTaggingProfilePojo selectedResourceTaggingProfile;

	/**
	 * The refresh timer used to periodically refresh the list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListResourceTaggingProfilePresenter(ClientFactory clientFactory, boolean clearList, ResourceTaggingProfileQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListResourceTaggingProfileView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListResourceTaggingProfilePresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListResourceTaggingProfilePresenter(ClientFactory clientFactory, ListResourceTaggingProfilePlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListResourceTaggingProfileView getView() {
		return clientFactory.getListResourceTaggingProfileView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;
		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving User Logged In...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				log.log(Level.SEVERE, "Exception Retrieving Accounts", caught);
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the Accounts you're associated to.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {
				getView().enableButtons();
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Accounts");

				// Clear the list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				getView().initPage();

				List<String> filterTypeItems = new java.util.ArrayList<String>();
				filterTypeItems.add(Constants.FILTER_PROFILE_NAMESPACE);
				filterTypeItems.add(Constants.FILTER_PROFILE_NAME);
				filterTypeItems.add(Constants.FILTER_PROFILE_NAMESPACE_AND_NAME);
				filterTypeItems.add(Constants.FILTER_MANAGED_TAG_NAME_VALUE);
				getView().setFilterTypeItems(filterTypeItems);

				// Request the  list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	/**
	 * Refresh the list.
	 */
	public void refreshList(final UserAccountPojo user) {
		// use RPC to get all accounts for the current filter being used
		getView().showPleaseWaitDialog("Retrieving resource tagging profiles from the RTP Service...");
		AsyncCallback<ResourceTaggingProfileQueryResultPojo> callback = new AsyncCallback<ResourceTaggingProfileQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving ResourceTaggingProfiles", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of accounts.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(ResourceTaggingProfileQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " RTPs for " + result.getFilterUsed());
				// pair the list down to ONLY the latest revision of each 
				// unique profile by namespace/profileName
				List<ResourceTaggingProfilePojo> newList = pairDownListToLatestRevisionsOnly(result.getResults());
				GWT.log("Paired the list down to " + newList.size() + " profiles");
				setResourceTaggingProfileList(newList);

				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					boolean isAdmin=false;
					boolean isAuditor=false;
					if (isAdmin) {
						getView().applyAWSAccountAdminMask();
					}
					else if (isAuditor) {
						getView().applyAWSAccountAuditorMask();
					}
					else {
						if (result.getResults().size() > 0) {
							getView().showMessageToUser("An error has occurred.  The user logged in does not "
									+ "appear to be associated to any valid roles for this account.");
							getView().applyAWSAccountAuditorMask();
						}
						// just means no rows were returned.
					}
				}
				
                getView().hidePleaseWaitPanel();
				getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing ResourceTaggingProfile list...");
		if (filter == null) {
			filter = new ResourceTaggingProfileQueryFilterPojo();
		}
		filter.setUserLoggedIn(user);
		VpcProvisioningService.Util.getInstance().getResourceTaggingProfilesForFilter(filter, callback);
	}
	
	private List<ResourceTaggingProfilePojo> pairDownListToLatestRevisionsOnly(List<ResourceTaggingProfilePojo> originalList) {
		List<ResourceTaggingProfilePojo> retList = new java.util.ArrayList<ResourceTaggingProfilePojo>();
		java.util.HashMap<String, ResourceTaggingProfilePojo> rtpMap = new java.util.HashMap<String, ResourceTaggingProfilePojo>();
		
		for (ResourceTaggingProfilePojo rtp : originalList) {
			String key = rtp.getNamespace() + rtp.getProfileName();
			ResourceTaggingProfilePojo existingRtp = rtpMap.get(key);
			if (existingRtp == null) {
				rtpMap.put(key, rtp);
				continue;
			}
			// rtp was found in the map so we have to see if it's revision is less than or greater to
			// the current rtp.  if it's less, we need to replace it with the current rtp
			if (existingRtp.getRevision() != null) {
				int existingRevision = Integer.parseInt(existingRtp.getRevision());
				int currentRevision = Integer.parseInt(rtp.getRevision());
				if (currentRevision > existingRevision) {
					rtpMap.put(key, rtp);
				}
			}
			else {
				// just replace what's in there with this revision
				rtpMap.put(key, rtp);
			}
		}
		java.util.Iterator<String> keys = rtpMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			ResourceTaggingProfilePojo rtp = rtpMap.get(key);
			GWT.log("Latest revision for " + rtp.getNamespace() + "/" + rtp.getProfileName() + " is: " + rtp.getRevision());
			retList.add(rtp);
		}
		
		return retList;
	}

	/**
	 * Set the list of accounts.
	 */
	private void setResourceTaggingProfileList(List<ResourceTaggingProfilePojo> accounts) {
		getView().setResourceTaggingProfiles(accounts);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new ResourceTaggingProfileListUpdateEvent(accounts), this);
		}
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectResourceTaggingProfile(ResourceTaggingProfilePojo selected) {
		this.resourceTaggingProfile = selected;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public ResourceTaggingProfileQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(ResourceTaggingProfileQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteResourceTaggingProfile(final ResourceTaggingProfilePojo rtp) {
		selectedResourceTaggingProfile = rtp;
		VpcpConfirm.confirm(
			ListResourceTaggingProfilePresenter.this, 
			"Confirm Delete ResourceTaggingProfile Metadata", 
			"Delete the metadata for the ALL Resource Tagging Profiles in the namespace '" + 
				selectedResourceTaggingProfile.getNamespace() + 
				"' with a profile name of '" + selectedResourceTaggingProfile.getProfileName() + "'?");
	}

//	@Override
//	public void filterByResourceTaggingProfileId(String accountId) {
//		getView().showPleaseWaitDialog("Filtering accounts");
//		filter = new ResourceTaggingProfileQueryFilterPojo();
//		filter
//		this.getUserAndRefreshList();
//	}

	@Override
	public void clearFilter() {
		getView().showPleaseWaitDialog("Clearing filter");
		filter = null;
		this.getUserAndRefreshList();
	}
	private void getUserAndRefreshList() {
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				getView().setUserLoggedIn(result);
				refreshList(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	@Override
	public void vpcpConfirmOkay() {
		getView().showPleaseWaitDialog("Deleting ResourceTaggingProfile Metadata for " + 
			selectedResourceTaggingProfile.getNamespace() + "/" + 
			selectedResourceTaggingProfile.getProfileName() + "...");
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server deleting the ResourceTaggingProfile metadata.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(Void result) {
				// remove from dataprovider
				getView().removeResourceTaggingProfileFromView(selectedResourceTaggingProfile);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), "ResourceTaggingProfile metadata was deleted.");
			}
		};
		VpcProvisioningService.Util.getInstance().deleteResourceTaggingProfile(true, selectedResourceTaggingProfile, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), 
			"Operation cancelled.  ResourceTaggingProfile metadata for " + 
			selectedResourceTaggingProfile.getNamespace() + "/" + 
			selectedResourceTaggingProfile.getProfileName() + " was not deleted.");
	}

	@Override
	public void filterByProfileName(String name) {
		getView().showPleaseWaitDialog("Filtering resource tagging profiles");
		filter = new ResourceTaggingProfileQueryFilterPojo();
		filter.setFuzzyFilter(true);
		filter.setProfileName(name.toLowerCase());
		this.getUserAndRefreshList();
	}

	@Override
	public void filterByNamespace(String namespace) {
		getView().showPleaseWaitDialog("Filtering resource tagging profiles");
		filter = new ResourceTaggingProfileQueryFilterPojo();
		filter.setFuzzyFilter(true);
		filter.setNamespace(namespace.toLowerCase());
		this.getUserAndRefreshList();
	}

	@Override
	public void filterByNamespaceAndProfileName(String filterString) {
		getView().showPleaseWaitDialog("Filtering resource tagging profiles");
		filter = new ResourceTaggingProfileQueryFilterPojo();
		filter.setFuzzyFilter(true);
		if (filterString.indexOf(",") >= 0) {
			String namespace = filterString.substring(0, filterString.indexOf(","));
			String profileName = filterString.substring(filterString.indexOf(",")+1);
			filter.setNamespace(namespace.toLowerCase());
			filter.setProfileName(profileName.toLowerCase());
		}
		else {
			// just use namespace
			filter.setNamespace(filterString.toLowerCase());
		}
		this.getUserAndRefreshList();
	}

	@Override
	public void filterByManagedTagNameAndValue(String tagNameAndValue) {
		getView().showPleaseWaitDialog("Filtering resource tagging profiles");
		filter = new ResourceTaggingProfileQueryFilterPojo();
		filter.setFuzzyFilter(true);
		if (tagNameAndValue.indexOf("=") >= 0) {
			String tagName = tagNameAndValue.substring(0, tagNameAndValue.indexOf("="));
			String tagValue = tagNameAndValue.substring(tagNameAndValue.indexOf("=")+1);
			filter.setManagedTagName(tagName.toLowerCase());
			filter.setManagedTagValue(tagValue.toLowerCase());
		}
		else {
			// just set the tag name
			filter.setManagedTagName(tagNameAndValue.toLowerCase());
		}
		this.getUserAndRefreshList();
	}

}
