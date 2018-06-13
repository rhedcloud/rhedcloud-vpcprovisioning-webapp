package edu.emory.oit.vpcprovisioning.client.common;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryResultPojo;

public class DirectoryPersonRpcSuggestOracle extends SuggestOracle {
    private Logger log = Logger.getLogger(getClass().getName());
	String type=null;

	public DirectoryPersonRpcSuggestOracle(String type) {
		this.type = type;
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		AsyncCallback<DirectoryPersonQueryResultPojo> srvrCallback = new AsyncCallback<DirectoryPersonQueryResultPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("[MultiWordRpcSuggestOracle.requestSuggestions] Failure: " + caught);
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				if (caught.getMessage().toLowerCase().indexOf("more than 100 matches found") >= 0) {
					descList.add(new MultiWordRpcSuggestion("Too many matches, keep typing...", "", null));
				}
				else {
					descList.add(new MultiWordRpcSuggestion("An Error Occurred: " + caught.getMessage(), "", null));
				}
				Response resp =
			            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}

			@Override
			public void onSuccess(DirectoryPersonQueryResultPojo result) {
				List<String> ppids = new java.util.ArrayList<String>();
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				for (DirectoryPersonPojo pojo : result.getResults()) {
					if (!ppids.contains(pojo.getKey())) {
						ppids.add(pojo.getKey());
						descList.add(new MultiWordRpcSuggestion(pojo.getFullName(), pojo.getFullName(), pojo));
					}
				}
				Response resp =
		            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}
			
		};
		if (type.equals(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME)) {
			DirectoryPersonQueryFilterPojo filter = new DirectoryPersonQueryFilterPojo();
			filter.setSearchString(request.getQuery());
			VpcProvisioningService.Util.getInstance().getDirectoryPersonsForFilter(filter, srvrCallback);
		}
		else {
			// invalid type...
			log.info("Invalid suggestion type");
		}
	}

	private class MultiWordRpcSuggestion implements DirectoryPersonSuggestion {
		String displayString;
		String replacementString;
		DirectoryPersonPojo directoryPerson;
		
		public MultiWordRpcSuggestion(String displayString, String replacementString, DirectoryPersonPojo directoryPerson) {
			super();
			this.displayString = displayString;
			this.replacementString = replacementString;
			this.directoryPerson = directoryPerson;
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
		public DirectoryPersonPojo getDirectoryPerson() {
			return directoryPerson;
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
