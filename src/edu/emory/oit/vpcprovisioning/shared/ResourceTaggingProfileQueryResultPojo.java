package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ResourceTaggingProfileQueryResultPojo extends SharedObject implements IsSerializable {
	ResourceTaggingProfileQueryFilterPojo filterUsed;
	List<ResourceTaggingProfilePojo> results;
	public ResourceTaggingProfileQueryFilterPojo getFilterUsed() {
		return filterUsed;
	}
	public void setFilterUsed(ResourceTaggingProfileQueryFilterPojo filterUsed) {
		this.filterUsed = filterUsed;
	}
	public List<ResourceTaggingProfilePojo> getResults() {
		return results;
	}
	public void setResults(List<ResourceTaggingProfilePojo> results) {
		this.results = results;
	}

}
