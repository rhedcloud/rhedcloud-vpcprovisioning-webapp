package edu.emory.oit.vpcprovisioning.presenter.finacct;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;

public class ListFinancialAccountsPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix(Constants.LIST_FINANCIAL_ACCOUNTS)
	public static class Tokenizer implements PlaceTokenizer<ListFinancialAccountsPlace> {

		public ListFinancialAccountsPlace getPlace(String token) {
			return new ListFinancialAccountsPlace(true);
		}

		public String getToken(ListFinancialAccountsPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	AccountQueryFilterPojo filter;
	private boolean showBadFinAcctsHTML;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListFinancialAccountsPlace(boolean listStale) {
		this.listStale = listStale;
	}

	public ListFinancialAccountsPlace(boolean b, boolean c) {
		this.listStale = b;
		this.showBadFinAcctsHTML = c;
	}

	/**
	 * Check if the case record list is stale and should be cleared.
	 * 
	 * @return true if stale, false if not
	 */
	public boolean isListStale() {
		return listStale;
	}

	public AccountQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(AccountQueryFilterPojo filter) {
		this.filter = filter;
	}

	public boolean isShowBadFinAcctsHTML() {
		return showBadFinAcctsHTML;
	}

	public void setShowBadFinAcctsHTML(boolean showBadFinAcctsHTML) {
		this.showBadFinAcctsHTML = showBadFinAcctsHTML;
	}
}
