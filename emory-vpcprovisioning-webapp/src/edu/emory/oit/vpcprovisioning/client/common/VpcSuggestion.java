package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public interface VpcSuggestion extends Suggestion {
	VpcPojo getVpc();
}
