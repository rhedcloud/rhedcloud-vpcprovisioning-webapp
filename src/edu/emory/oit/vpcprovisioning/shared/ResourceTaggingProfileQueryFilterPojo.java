package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ResourceTaggingProfileQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter {
	/*
<!ELEMENT ResourceTaggingProfileQuerySpecification (Comparison*, QueryLanguage?, Namespace?, ProfileName?, Revision?)>
	 */
	boolean useQueryLanguage;
	int maxRows;
	String searchString;
	boolean fuzzyFilter=false;
	UserAccountPojo userLoggedIn;

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	String namespace;
	String profileName;
	String revision;
	String managedTagName;
	String managedTagValue;
	String profileId;
	boolean active=false;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getManagedTagName() {
		return managedTagName;
	}

	public void setManagedTagName(String managedTagName) {
		this.managedTagName = managedTagName;
	}

	public String getManagedTagValue() {
		return managedTagValue;
	}

	public void setManagedTagValue(String managedTagValue) {
		this.managedTagValue = managedTagValue;
	}

	public boolean isUseQueryLanguage() {
		return useQueryLanguage;
	}

	public void setUseQueryLanguage(boolean useQueryLanguage) {
		this.useQueryLanguage = useQueryLanguage;
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public boolean isFuzzyFilter() {
		return fuzzyFilter;
	}

	public void setFuzzyFilter(boolean fuzzyFilter) {
		this.fuzzyFilter = fuzzyFilter;
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

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "namespace: " + namespace + ", profileName: " + profileName + ", revision: " + revision;
	}
}
