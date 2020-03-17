package edu.emory.oit.vpcprovisioning.presenter.resourcetagging;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.ManagedTagPojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainResourceTaggingProfilePresenter extends PresenterBase implements MaintainResourceTaggingProfileView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String profileId;
	private ResourceTaggingProfilePojo resourceTaggingProfile;
	private ResourceTaggingProfileQueryFilterPojo filter;
	private SpeedChartPojo speedType;
	private UserAccountPojo userLoggedIn;
	private DirectoryPersonPojo directoryPerson;
	private boolean isCimp=false;
	private boolean newRevision;
	private ManagedTagPojo selectedManagedTag;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainResourceTaggingProfilePresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.resourceTaggingProfile = null;
		this.profileId = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainResourceTaggingProfilePresenter(ClientFactory clientFactory, boolean newRevision, ResourceTaggingProfilePojo resourceTaggingProfile) {
		this.isEditing = true;
		this.profileId = resourceTaggingProfile.getProfileId();
		this.clientFactory = clientFactory;
		this.resourceTaggingProfile = resourceTaggingProfile;
		this.newRevision = newRevision;
		getView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		GWT.log("[maintaintrppresenter] newRevision = " + newRevision);
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		getView().hideFilteredStatus();
		this.eventBus = eventBus;
		setReleaseInfo(clientFactory);
		getView().showPleaseWaitPanel("Retrieving ResourceTaggingProfile details, please wait...");
		getView().disableAdminMaintenance();

		if (profileId == null) {
			clientFactory.getShell().setSubTitle("Create ResourceTaggingProfile");
			startCreate();
		} 
		else {
			clientFactory.getShell().setSubTitle("Edit ResourceTaggingProfile");
			startEdit();
		}
	
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().disableAdminMaintenance();
				GWT.log("Exception retrieving user logged in", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				List<String> filterTypeItems = new java.util.ArrayList<String>();
				filterTypeItems.add(Constants.FILTER_MANAGED_TAG_NAME_VALUE);
				getView().setFilterTypeItems(filterTypeItems);
				
				AsyncCallback<ResourceTaggingProfileQueryResultPojo> rtp_cb2 = new AsyncCallback<ResourceTaggingProfileQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Exception retrieving resourceTaggingProfile list", caught);
					}

					@Override
					public void onSuccess(ResourceTaggingProfileQueryResultPojo result) {
						GWT.log("[maintainprofilepresenter] Got " + result.getResults().size() + 
								" profiles back for namespace: " + resourceTaggingProfile.getNamespace() + 
								" profile name: " + resourceTaggingProfile.getProfileName());
						getView().setResourceTaggingProfiles(result.getResults());
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
					}
				};
				// get all rtps with the same namespace/profile name as the one being edited
				if (isEditing) {
					ResourceTaggingProfileQueryFilterPojo filter = new ResourceTaggingProfileQueryFilterPojo();
					filter.setNamespace(resourceTaggingProfile.getNamespace());
					filter.setProfileName(resourceTaggingProfile.getProfileName());
					VpcProvisioningService.Util.getInstance().getResourceTaggingProfilesForFilter(filter, rtp_cb2);
				}
				
				// get latest version of the resourceTaggingProfile from the server
				AsyncCallback<ResourceTaggingProfileQueryResultPojo> rtp_cb = new AsyncCallback<ResourceTaggingProfileQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						getView().hidePleaseWaitPanel();
						getView().disableAdminMaintenance();
						GWT.log("Exception retrieving resourceTaggingProfile details", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving the details for this resourceTaggingProfile.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(ResourceTaggingProfileQueryResultPojo result) {
						GWT.log("[maintainprofilepresenter] Got " + result.getResults().size() + " profiles back for profile id: " + profileId);
						if (result.getResults().size() != 1) {
							// error
							getView().showMessageToUser("There was an exception on the " +
									"server retrieving the details for this resource tagging profile.  "
									+ "More than one record returned for profile id: " + profileId + " "
											+ "this should not be possible.");
							return;
						}
						resourceTaggingProfile = result.getResults().get(0);
						if (isNewRevision()) {
							resourceTaggingProfile.setActive(false);
						}
						getView().initPage();
						getView().setFieldViolations(false);
						getView().setInitialFocus();
					}
				};
				if (isEditing) {
					ResourceTaggingProfileQueryFilterPojo filter = new ResourceTaggingProfileQueryFilterPojo();
					filter.setProfileId(profileId);
					VpcProvisioningService.Util.getInstance().getResourceTaggingProfilesForFilter(filter, rtp_cb);
				}
				else {
					getView().initPage();
					getView().hidePleaseWaitDialog();
					getView().hidePleaseWaitPanel();
					getView().setFieldViolations(false);
					getView().setInitialFocus();
				}
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain resourceTaggingProfile: create");
		isEditing = false;
		getView().setEditing(isEditing);
		resourceTaggingProfile = new ResourceTaggingProfilePojo();
	}

	private void startEdit() {
		GWT.log("Maintain resourceTaggingProfile: edit");
		isEditing = true;
		getView().setEditing(isEditing);
		// Lock the display until the resourceTaggingProfile is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		getView().setLocked(false);
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
	public void deleteAccount() {
		if (isEditing) {
			doDeleteAccount();
		} else {
			doCancelAccount();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelAccount() {
		ActionEvent.fire(eventBus, ActionNames.ACCOUNT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteAccount() {
		if (resourceTaggingProfile == null) {
			return;
		}

		// TODO Delete the resourceTaggingProfile on server then fire onAccountDeleted();
	}

	@Override
	public void saveResourceTaggingProfile() {
		// TODO: i think this will always be a create
		getView().showPleaseWaitDialog("Saving resourceTaggingProfile...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().hidePleaseWaitPanel();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().setFieldViolations(false);
			getView().resetFieldStyles();
		}
		AsyncCallback<ResourceTaggingProfilePojo> callback = new AsyncCallback<ResourceTaggingProfilePojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				GWT.log("Exception saving the ResourceTaggingProfile", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the ResourceTaggingProfile.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ResourceTaggingProfilePojo result) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				ActionEvent.fire(eventBus, ActionNames.RTP_SAVED, resourceTaggingProfile);
			}
		};
		// it's a create
		VpcProvisioningService.Util.getInstance().createResourceTaggingProfile(resourceTaggingProfile, callback);
	}

	@Override
	public ResourceTaggingProfilePojo getResourceTaggingProfile() {
		return this.resourceTaggingProfile;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getAccountId() {
		return profileId;
	}

	public void setResourceTaggingProfileId(String profileId) {
		this.profileId = profileId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setAccount(ResourceTaggingProfilePojo resourceTaggingProfile) {
		this.resourceTaggingProfile = resourceTaggingProfile;
	}

	@Override
	public void setSpeedChartStatusForKeyOnWidget(final String key, final Widget w, final boolean confirmSpeedType) {
		GWT.log("[setSpeedChartStatusForKeyOnWidget] validating speed type: " + key);
		AsyncCallback<SpeedChartPojo> callback = new AsyncCallback<SpeedChartPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server exception validating speedtype", caught);
				w.setTitle("Server exception validating speedtype");
				getView().setSpeedTypeStatus("Server exception validating speedtype");
				getView().setSpeedTypeColor(Constants.COLOR_RED);
			}

			@Override
			public void onSuccess(SpeedChartPojo scp) {
				if (scp == null) {
					w.setTitle("Invalid resourceTaggingProfile number (" + key + "), can't validate this number");
					w.getElement().getStyle().setBackgroundColor("#efbebe");
					getView().setSpeedTypeStatus("<b>Invalid resourceTaggingProfile</b>");
					getView().setSpeedTypeColor(Constants.COLOR_RED);
					getView().setFieldViolations(true);
				}
				else {
//				    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
					GWT.log("[setSpeedChartStatusForKeyOnWidget] got a speed chart.");
					speedType = scp;
					String deptId = scp.getDepartmentId();
					String deptDesc = scp.getDepartmentDescription();
					String desc = scp.getDescription();
				    String euValidityDesc = scp.getEuValidityDescription();
				    String statusDescString = euValidityDesc + "\n" + 
				    		deptId + " | " + deptDesc + "\n" +
				    		desc;
				    String statusDescHTML = "<b>" + euValidityDesc + "<br>" + 
				    		deptId + " | " + deptDesc + "<br>" +
				    		desc + "<b>";
					w.setTitle(statusDescString);
					getView().setSpeedTypeStatus(statusDescHTML);
					GWT.log("[setSpeedChartStatusForKeyOnWidget] set speed type status html.");
					if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_VALID)) {
						getView().setSpeedTypeColor(Constants.COLOR_GREEN);
						w.getElement().getStyle().setBackgroundColor(null);
						getView().setFieldViolations(false);
						if (confirmSpeedType) {
							didConfirmSpeedType();
						}
					}
					else if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_INVALID)) {
						getView().setSpeedTypeColor(Constants.COLOR_RED);
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_INVALID_FIELD);
						getView().setFieldViolations(true);
					}
					else {
						getView().setSpeedTypeColor(Constants.COLOR_ORANGE);
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_FIELD_WARNING);
						if (confirmSpeedType) {
							didConfirmSpeedType();
						}
					}
				}
			}
		};
		if (key != null && key.length() > 0) {
			SpeedChartQueryFilterPojo filter = new SpeedChartQueryFilterPojo();
			filter.getSpeedChartKeys().add(key);
			VpcProvisioningService.Util.getInstance().getSpeedChartForFinancialAccountNumber(key, callback);
		}
		else {
			GWT.log("null key, can't validate yet");
		}
	}

	@Override
	public void setSpeedChartStatusForKey(String key, Label label, boolean confirmSpeedType) {
		GWT.log("[setSpeedChartStatusForKey] validating speed type: " + key);
		// null check / length
		if (key == null || key.length() != 10) {
			getView().setSpeedTypeStatus("<b>Invalid length</b>");
			getView().setSpeedTypeColor(Constants.COLOR_RED);
			getView().setFieldViolations(true);
			return;
		}
		// TODO: numeric characters
		
		setSpeedChartStatusForKeyOnWidget(key, getView().getSpeedTypeWidget(), confirmSpeedType);
	}

	@Override
	public DirectoryPersonPojo getDirectoryPerson() {
		return directoryPerson;
	}

	@Override
	public void setDirectoryPerson(DirectoryPersonPojo directoryPerson) {
		GWT.log("[presenter] setting directory person to: " + directoryPerson.toString());
		this.directoryPerson = directoryPerson;
	}

	@Override
	public MaintainResourceTaggingProfileView getView() {
		return clientFactory.getMaintainResourceTaggingProfileView();
	}

	@Override
	public void setResourceTaggingProfileFilter(ResourceTaggingProfileQueryFilterPojo filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResourceTaggingProfileQueryFilterPojo getResourceTaggingProfileFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean didConfirmSpeedType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNewRevision() {
		return this.newRevision;
	}

	@Override
	public void setSelectedManagedTag(ManagedTagPojo tag) {
		this.selectedManagedTag = tag;
	}

	@Override
	public ManagedTagPojo getSelectedManagedTag() {
		return this.selectedManagedTag;
	}

	@Override
	public void updateManagedTag(ManagedTagPojo tag) {
		int index = -1;
		tagLoop: for (int i=0; i<resourceTaggingProfile.getManagedTags().size(); i++) {
			ManagedTagPojo tpp = resourceTaggingProfile.getManagedTags().get(i);
			if (tpp.getTagName().equalsIgnoreCase(tag.getTagName())) {
				index = i;
				break tagLoop;
			}
		}
		if (index >= 0) {
			GWT.log("Updating managed tag: " + index);
			resourceTaggingProfile.getManagedTags().remove(index);
			resourceTaggingProfile.getManagedTags().add(tag);
		}
		else {
			GWT.log("Counldn't find a managed tag to update...problem");
		}
	}

	@Override
	public void addManagedTag(ManagedTagPojo tag) {
		resourceTaggingProfile.getManagedTags().add(0, tag);
	}

	@Override
	public void setSelectedResourceTaggingProfile(ResourceTaggingProfilePojo selected) {
		resourceTaggingProfile = selected;
		getView().initPage();
		getView().setFieldViolations(false);
		getView().setInitialFocus();
	}

	@Override
	public void updateResourceTaggingProfiles(List<ResourceTaggingProfilePojo> profiles) {
		getView().showPleaseWaitDialog("Updating resourceTaggingProfile...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().hidePleaseWaitPanel();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().setFieldViolations(false);
			getView().resetFieldStyles();
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				GWT.log("Exception updating the ResourceTaggingProfile", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server updating the ResourceTaggingProfile.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				ActionEvent.fire(eventBus, ActionNames.RTP_SAVED, resourceTaggingProfile);
			}
		};
		// it's a create
		VpcProvisioningService.Util.getInstance().updateResourceTaggingProfiles(profiles, callback);
	}
}
