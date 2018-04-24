package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class FirewallRulePojo extends SharedObject implements IsSerializable, Comparable<FirewallRulePojo> {

	/*
	<!ELEMENT FirewallRule (ProfileSetting?,  
	Action? 
	LogSetting?)>
	
	<!ELEMENT ProfileSetting (Group)>
	<!ELEMENT Group (Member+)>
	<!ELEMENT To (Member+)>
	<!ELEMENT From (Member+)>
	<!ELEMENT Source (Member+)>
	<!ELEMENT Destination (Member+)>
	<!ELEMENT SourceUser (Member+)>
	<!ELEMENT Category (Member+)>
	<!ELEMENT Application (Member+)>
	<!ELEMENT Service (Member+)>
	<!ELEMENT HipProfiles (Member+)>
	<!ELEMENT Tag (Member+)>
	<!ELEMENT FirewallRuleQuerySpecification (Tag?)>
	
	 */
	
	String name;
	String action;
	String description;
	String logSetting;
	List<String> profileSettings = new java.util.ArrayList<String>();
	List<String> tos = new java.util.ArrayList<String>();
	List<String> froms = new java.util.ArrayList<String>();
	List<String> sources = new java.util.ArrayList<String>();
	List<String> destinations = new java.util.ArrayList<String>();
	List<String> sourceUsers = new java.util.ArrayList<String>();
	List<String> categories = new java.util.ArrayList<String>();
	List<String> applications = new java.util.ArrayList<String>();
	List<String> services = new java.util.ArrayList<String>();
	List<String> hipProfiles = new java.util.ArrayList<String>();
	List<String> tags = new java.util.ArrayList<String>();
	FirewallRulePojo baseline;

	
	public static final ProvidesKey<FirewallRulePojo> KEY_PROVIDER = new ProvidesKey<FirewallRulePojo>() {
		@Override
		public Object getKey(FirewallRulePojo item) {
			return item == null ? null : item.getName();
		}
	};

	public FirewallRulePojo() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public int compareTo(FirewallRulePojo o) {
		return name.compareTo(o.getName());
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<String> getProfileSettings() {
		return profileSettings;
	}


	public void setProfileSettings(List<String> profileSettings) {
		this.profileSettings = profileSettings;
	}


	public List<String> getTos() {
		return tos;
	}


	public void setTos(List<String> tos) {
		this.tos = tos;
	}


	public List<String> getFroms() {
		return froms;
	}


	public void setFroms(List<String> froms) {
		this.froms = froms;
	}


	public List<String> getSources() {
		return sources;
	}


	public void setSources(List<String> sources) {
		this.sources = sources;
	}


	public List<String> getDestinations() {
		return destinations;
	}


	public void setDestinations(List<String> destinations) {
		this.destinations = destinations;
	}


	public List<String> getSourceUsers() {
		return sourceUsers;
	}


	public void setSourceUsers(List<String> sourceUsers) {
		this.sourceUsers = sourceUsers;
	}


	public List<String> getCategories() {
		return categories;
	}


	public void setCategories(List<String> categories) {
		this.categories = categories;
	}


	public List<String> getApplications() {
		return applications;
	}


	public void setApplications(List<String> applications) {
		this.applications = applications;
	}


	public List<String> getServices() {
		return services;
	}


	public void setServices(List<String> services) {
		this.services = services;
	}


	public List<String> getHipProfiles() {
		return hipProfiles;
	}


	public void setHipProfiles(List<String> hipProfiles) {
		this.hipProfiles = hipProfiles;
	}


	public List<String> getTags() {
		return tags;
	}


	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getLogSetting() {
		return logSetting;
	}


	public void setLogSetting(String logSetting) {
		this.logSetting = logSetting;
	}


	public FirewallRulePojo getBaseline() {
		return baseline;
	}


	public void setBaseline(FirewallRulePojo baseline) {
		this.baseline = baseline;
	}

}
