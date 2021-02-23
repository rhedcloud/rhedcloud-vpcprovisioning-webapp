package edu.emory.oit.vpcprovisioning.client.common;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class AwsAccountRpcSuggestOracle extends SuggestOracle {
	String type=null;
	UserAccountPojo user;

	public AwsAccountRpcSuggestOracle(String type) {
		this.type = type;
	}
	
	public AwsAccountRpcSuggestOracle(UserAccountPojo user, String type) {
		this.user = user;
		this.type = type;
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		AsyncCallback<AccountQueryResultPojo> acct_callback = new AsyncCallback<AccountQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("problem getting accounts..." + caught.getMessage());
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				descList.add(new MultiWordRpcSuggestion("An Error Occurred: " + caught.getMessage(), "", null));
				Response resp =
			            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}

			@Override
			public void onSuccess(AccountQueryResultPojo result) {
				GWT.log("got " + result.getResults().size() + " accounts back.");
				List<String> accountNames = new java.util.ArrayList<String>();
				List<MultiWordRpcSuggestion> descList = new java.util.ArrayList<MultiWordRpcSuggestion>();
				if (result.getResults().size() == 0) {
					descList.add(new MultiWordRpcSuggestion("No matches yet...", "", null));
				}
				for (AccountPojo pojo : result.getResults()) {
					if (!accountNames.contains(pojo.getAccountName())) {
						accountNames.add(pojo.getAccountName());
						String displayString;
						if (pojo.getAlternateName() != null) {
							displayString = pojo.getAccountId() + " / " + 
									pojo.getAccountName() + " / " + 
									pojo.getAlternateName();
						}
						else {
							displayString = pojo.getAccountId() + " / " + 
									pojo.getAccountName();
						}
						descList.add(new MultiWordRpcSuggestion(displayString, pojo.getAccountName(), pojo));
					}
				}
				Response resp =
		            new Response(descList);
				callback.onSuggestionsReady(request, resp);
			}
		};
		AccountQueryFilterPojo filter = new AccountQueryFilterPojo();
		filter.setSuggestBoxFilter(true);
		filter.setFuzzyFilter(true);
		filter.setAccountName(request.getQuery());
		filter.setAccountId(request.getQuery());
		VpcProvisioningService.Util.getInstance().getAccountsForFilter(filter, acct_callback);
	}

	private class MultiWordRpcSuggestion implements AwsAccountSuggestion {
		String displayString;
		String replacementString;
		AccountPojo account;
		
		public MultiWordRpcSuggestion(String displayString, String replacementString, AccountPojo account) {
			super();
			this.displayString = displayString;
			this.replacementString = replacementString;
			this.account = account;
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
		public AccountPojo getAccount() {
			return account;
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
