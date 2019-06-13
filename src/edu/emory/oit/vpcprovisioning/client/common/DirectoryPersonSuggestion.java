package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;

public interface DirectoryPersonSuggestion extends Suggestion {
	DirectoryPersonPojo getDirectoryPerson();
}
