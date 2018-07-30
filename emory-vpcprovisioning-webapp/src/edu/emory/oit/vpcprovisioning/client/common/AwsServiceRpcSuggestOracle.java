package edu.emory.oit.vpcprovisioning.client.common;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;

public class AwsServiceRpcSuggestOracle extends SuggestOracle {
	String type=null;

	public AwsServiceRpcSuggestOracle(String type) {
		this.type = type;
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		AsyncCallback<AWSServiceQueryResultPojo> svcCallback = new AsyncCallback<AWSServiceQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("problem getting services..." + caught.getMessage());
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				descList.add(new MultiWordRpcSuggestion("An Error Occurred: " + caught.getMessage(), "", null));
				Response resp =
			            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}

			@Override
			public void onSuccess(AWSServiceQueryResultPojo result) {
				GWT.log("got " + result.getResults().size() + " services back.");
				List<String> svcCodes = new java.util.ArrayList<String>();
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				if (result.getResults().size() == 0) {
					descList.add(new MultiWordRpcSuggestion("No matches yet...", "", null));
				}
				for (AWSServicePojo pojo : result.getResults()) {
					if (!svcCodes.contains(pojo.getAwsServiceCode())) {
						svcCodes.add(pojo.getAwsServiceName());
						descList.add(new MultiWordRpcSuggestion(pojo.getAwsServiceCode(), pojo.getAwsServiceName(), pojo));
					}
				}
				Response resp =
		            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}
		};
		AWSServiceQueryFilterPojo filter = new AWSServiceQueryFilterPojo();
		VpcProvisioningService.Util.getInstance().getServicesForFilter(filter, svcCallback);
		
//		if (type.equals(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME)) {
//			DirectoryPersonQueryFilterPojo filter = new DirectoryPersonQueryFilterPojo();
//			filter.setSearchString(request.getQuery());
//			VpcProvisioningService.Util.getInstance().getDirectoryPersonsForFilter(filter, srvrCallback);
//		}
//		else {
//			// invalid type...
//			log.info("Invalid suggestion type");
//		}
	}

	private class MultiWordRpcSuggestion implements AwsServiceSuggestion {
		String displayString;
		String replacementString;
		AWSServicePojo service;
		
		public MultiWordRpcSuggestion(String displayString, String replacementString, AWSServicePojo service) {
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
		public AWSServicePojo getService() {
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
