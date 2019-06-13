package edu.emory.oit.vpcprovisioning.client.common;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;

public class VpcRpcSuggestOracle extends SuggestOracle {
	String type=null;

	public VpcRpcSuggestOracle(String type) {
		this.type = type;
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		AsyncCallback<VpcQueryResultPojo> acct_callback = new AsyncCallback<VpcQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("problem getting vpcs..." + caught.getMessage());
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				descList.add(new MultiWordRpcSuggestion("An Error Occurred: " + caught.getMessage(), "", null));
				Response resp =
			            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}

			@Override
			public void onSuccess(VpcQueryResultPojo result) {
				GWT.log("got " + result.getResults().size() + " vpcs back.");
				List<String> ids = new java.util.ArrayList<String>();
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				if (result.getResults().size() == 0) {
					descList.add(new MultiWordRpcSuggestion("No matches yet...", "", null));
				}
				for (VpcPojo pojo : result.getResults()) {
					if (!ids.contains(pojo.getVpcId())) {
						ids.add(pojo.getVpcId());
						if (pojo.getVpcId().toLowerCase().contains(request.getQuery().toString())) {
							descList.add(new MultiWordRpcSuggestion(pojo.getVpcId() + " - " + pojo.getAccountName(), pojo.getVpcId(), pojo));
						}
					}
				}
				Response resp =
		            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}
		};
		VpcQueryFilterPojo filter = new VpcQueryFilterPojo();
		// TODO: may just have to get all VPCs and only return the ones that contain the search string.
//		filter.setVpcId(request.getQuery());
		VpcProvisioningService.Util.getInstance().getVpcsForFilter(filter, acct_callback);
	}

	private class MultiWordRpcSuggestion implements VpcSuggestion {
		String displayString;
		String replacementString;
		VpcPojo vpc;
		
		public MultiWordRpcSuggestion(String displayString, String replacementString, VpcPojo vpc) {
			super();
			this.displayString = displayString;
			this.replacementString = replacementString;
			this.vpc = vpc;
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
		public VpcPojo getVpc() {
			return vpc;
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
