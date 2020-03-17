package edu.emory.oit.vpcprovisioning.client.common;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.ConsoleFeaturePojo;
import edu.emory.oit.vpcprovisioning.shared.ConsoleFeatureQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ConsoleFeatureQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ConsoleFeatureRpcSuggestOracle extends SuggestOracle {
	String type=null;
	List<ConsoleFeaturePojo> services = new java.util.ArrayList<ConsoleFeaturePojo>();

	public ConsoleFeatureRpcSuggestOracle(UserAccountPojo user, String type) {
		this.type = type;

		AsyncCallback<ConsoleFeatureQueryResultPojo> svcCallback = new AsyncCallback<ConsoleFeatureQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("[ConsoleFeatureRpcSuggestOracle.constructor] problem getting features..." + caught.getMessage());
			}

			@Override
			public void onSuccess(ConsoleFeatureQueryResultPojo result) {
				GWT.log("[ConsoleFeatureRpcSuggestOracle.constructor] got " + result.getResults().size() + " features back.");
				if (result != null) {
					if (result.getResults() != null) {
						services = result.getResults();
					}
				}
			}
		};
		GWT.log("[ConsoleFeatureRpcSuggestOracle.constructor] getting services");
		ConsoleFeatureQueryFilterPojo filter = new ConsoleFeatureQueryFilterPojo();
		VpcProvisioningService.Util.getInstance().getConsoleFeaturesForFilter(filter, svcCallback);
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
		if (services.size() == 0) {
			descList.add(new MultiWordRpcSuggestion("Feature Data isn't loaded yet.  Please wait.", "", null));
		}
		else {
			for (ConsoleFeaturePojo pojo : services) {
				boolean addIt=false;
				if (pojo.getName() != null && 
					pojo.getName().toLowerCase().
					indexOf(request.getQuery().toLowerCase()) >= 0) {
					addIt=true;
				}
				else if (pojo.getDescription() != null && 
						pojo.getDescription().toLowerCase().
						indexOf(request.getQuery().toLowerCase()) >= 0) {
					addIt=true;
				}
				if (addIt) {
					descList.add(new MultiWordRpcSuggestion(pojo.getName(), 
							pojo.getName(), 
							pojo));
				}
			}
		}
		Response resp =
            new Response(descList);
		callback.onSuggestionsReady(request, resp);
	}

	private class MultiWordRpcSuggestion implements ConsoleFeatureSuggestion {
		String displayString;
		String replacementString;
		ConsoleFeaturePojo service;
		
		public MultiWordRpcSuggestion(String displayString, String replacementString, ConsoleFeaturePojo service) {
			super();
			this.displayString = displayString;
			this.replacementString = replacementString;
			this.service = service;
		}

		@Override
		public String getDisplayString() {
			return displayString;
		}

		@Override
		public String getReplacementString() {
			return replacementString;
		}

		@Override
		public ConsoleFeaturePojo getService() {
			return service;
		}
		
	}
	
	public static class MWCallback implements SuggestOracle.Callback {

		public MWCallback() {
			super();
		}

		@Override
		public void onSuggestionsReady(Request request, Response response) {
		}
	}
}
