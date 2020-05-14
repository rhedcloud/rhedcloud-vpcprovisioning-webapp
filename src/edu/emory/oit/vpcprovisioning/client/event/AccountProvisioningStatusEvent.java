package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;

public class AccountProvisioningStatusEvent extends GwtEvent<AccountProvisioningStatusEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditVpncpEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowAccountProvisioningSummaryStatus(AccountProvisioningStatusEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<AccountProvisioningStatusEvent.Handler> TYPE = new Type<AccountProvisioningStatusEvent.Handler>();

	  private final AccountProvisioningSummaryPojo summary;

	  public AccountProvisioningStatusEvent(AccountProvisioningSummaryPojo summary) {
	    this.summary = summary;
	  }

	  @Override
	  public final Type<AccountProvisioningStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public AccountProvisioningSummaryPojo getAccountProvisioningSummary() {
	    return summary;
	  }

	  @Override
	  protected void dispatch(AccountProvisioningStatusEvent.Handler handler) {
	    handler.onShowAccountProvisioningSummaryStatus(this);
	  }
}
