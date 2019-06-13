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
	List<AWSServicePojo> services = new java.util.ArrayList<AWSServicePojo>();

	public AwsServiceRpcSuggestOracle(String type) {
		this.type = type;

		AsyncCallback<AWSServiceQueryResultPojo> svcCallback = new AsyncCallback<AWSServiceQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("[AwsServiceRpcSuggestOracle.constructor] problem getting services..." + caught.getMessage());
			}

			@Override
			public void onSuccess(AWSServiceQueryResultPojo result) {
				GWT.log("[AwsServiceRpcSuggestOracle.constructor] got " + result.getResults().size() + " services back.");
				services = result.getResults();
			}
		};
		GWT.log("[AwsServiceRpcSuggestOracle.constructor] getting services");
		AWSServiceQueryFilterPojo filter = new AWSServiceQueryFilterPojo();
		VpcProvisioningService.Util.getInstance().getServicesForFilter(filter, svcCallback);
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
		if (services.size() == 0) {
			descList.add(new MultiWordRpcSuggestion("Service Data isn't loaded yet.  Please wait.", "", null));
		}
		else {
			for (AWSServicePojo pojo : services) {
				boolean addIt=false;
				if (pojo.getAwsServiceCode() != null && 
					pojo.getAwsServiceCode().toLowerCase().
					indexOf(request.getQuery().toLowerCase()) >= 0) {
					addIt=true;
				}
				else if (pojo.getAwsServiceCode() != null && 
						pojo.getAwsServiceCode().toLowerCase().
						indexOf(request.getQuery().toLowerCase()) >= 0) {
					addIt=true;
				}
				else if (pojo.getCombinedServiceName() != null && 
						pojo.getCombinedServiceName().toLowerCase().
						indexOf(request.getQuery().toLowerCase()) >= 0) {
					addIt=true;
				}
				else if (pojo.getAlternateServiceName() != null && 
						pojo.getAlternateServiceName().toLowerCase().
						indexOf(request.getQuery().toLowerCase()) >= 0) {
					addIt=true;
				}
				else if (pojo.getDescription() != null && 
						pojo.getDescription().toLowerCase().
						indexOf(request.getQuery().toLowerCase()) >= 0) {
					addIt=true;
				}
				if (addIt) {
					descList.add(new MultiWordRpcSuggestion(pojo.getAwsServiceCode(), 
							pojo.getAwsServiceName(), 
							pojo));
				}
			}
		}
		Response resp =
            new Response(descList);
		callback.onSuggestionsReady(request, resp);
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
