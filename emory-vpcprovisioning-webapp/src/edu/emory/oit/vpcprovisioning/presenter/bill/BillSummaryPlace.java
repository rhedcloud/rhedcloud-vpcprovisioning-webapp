package edu.emory.oit.vpcprovisioning.presenter.bill;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.BillPojo;
import edu.emory.oit.vpcprovisioning.shared.BillQueryFilterPojo;

public class BillSummaryPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("billSummary")
	public static class Tokenizer implements PlaceTokenizer<BillSummaryPlace> {

		public BillSummaryPlace getPlace(String token) {
			return new BillSummaryPlace(true);
		}

		public String getToken(BillSummaryPlace place) {
			return "";
		}
	}

	private final boolean billStale;
	BillPojo bill;
	BillQueryFilterPojo filter;
	AccountPojo account;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public BillSummaryPlace(boolean billStale) {
		this.billStale = billStale;
	}
	private BillSummaryPlace(AccountPojo account) {
		this.billStale = false;
		this.account = account;
	}

	public static BillSummaryPlace createBillSummaryPlace(AccountPojo account) {
		return new BillSummaryPlace(account);
	}

	/**
	 * Check if the case record list is stale and should be cleared.
	 * 
	 * @return true if stale, false if not
	 */
	public boolean isBillStale() {
		return billStale;
	}

	public BillQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(BillQueryFilterPojo filter) {
		this.filter = filter;
	}

	public BillPojo getBill() {
		return bill;
	}

	public void setBill(BillPojo bill) {
		this.bill = bill;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}
}
