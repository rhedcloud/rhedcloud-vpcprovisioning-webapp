package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ResourceTaggingProfilePojo extends SharedObject implements IsSerializable, Comparable<ResourceTaggingProfilePojo> {
	/*
<!ELEMENT ResourceTaggingProfile (ProfileId?, Namespace, ProfileName, Revision, ManagedTag*)>
	 */

	boolean active;
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	String profileId;
	String namespace;
	String profileName;
	String revision;
	List<ManagedTagPojo> managedTags = new java.util.ArrayList<ManagedTagPojo>();
	ResourceTaggingProfilePojo baseline;

	public ResourceTaggingProfilePojo getBaseline() {
		return baseline;
	}
	public void setBaseline(ResourceTaggingProfilePojo baseline) {
		this.baseline = baseline;
	}
	public static final ProvidesKey<ResourceTaggingProfilePojo> KEY_PROVIDER = new ProvidesKey<ResourceTaggingProfilePojo>() {
		@Override
		public Object getKey(ResourceTaggingProfilePojo item) {
			return item == null ? null : item.getProfileId();
		}
	};
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public List<ManagedTagPojo> getManagedTags() {
		return managedTags;
	}
	public void setManagedTags(List<ManagedTagPojo> managedTags) {
		this.managedTags = managedTags;
	}
	@Override
	public int compareTo(ResourceTaggingProfilePojo o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
