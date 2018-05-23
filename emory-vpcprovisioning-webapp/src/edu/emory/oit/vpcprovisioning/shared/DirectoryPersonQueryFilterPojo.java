package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DirectoryPersonQueryFilterPojo extends SharedObject implements IsSerializable {
	String searchString;
	String key;
	String testRequest;

	public DirectoryPersonQueryFilterPojo() {
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTestRequest() {
		return testRequest;
	}

	public void setTestRequest(String testRequest) {
		this.testRequest = testRequest;
	}

}
