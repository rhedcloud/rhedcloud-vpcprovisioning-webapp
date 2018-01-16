package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.AccountPojo;

public class BillSummaryEvent extends GwtEvent<BillSummaryEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditAccountEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowBillSummary(BillSummaryEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<BillSummaryEvent.Handler> TYPE = new Type<BillSummaryEvent.Handler>();

	  private final AccountPojo account;

	  public BillSummaryEvent(AccountPojo acct) {
	    this.account = acct;
	  }

	  @Override
	  public final Type<BillSummaryEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public AccountPojo getAccount() {
	    return account;
	  }

	  @Override
	  protected void dispatch(BillSummaryEvent.Handler handler) {
	    handler.onShowBillSummary(this);
	  }
}
