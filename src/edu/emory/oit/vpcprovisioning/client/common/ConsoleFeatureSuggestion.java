package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import edu.emory.oit.vpcprovisioning.shared.ConsoleFeaturePojo;

public interface ConsoleFeatureSuggestion extends Suggestion {
	ConsoleFeaturePojo getService();
}
