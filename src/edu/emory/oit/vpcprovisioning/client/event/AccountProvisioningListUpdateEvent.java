package edu.emory.oit.vpcprovisioning.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;

public class AccountProvisioningListUpdateEvent extends GwtEvent<AccountProvisioningListUpdateEvent.Handler> {

	  /**
	   * Handler for {@link AccountProvisioningListUpdateEvent}.
	   */
	  public interface Handler extends EventHandler {
	  
	    /**
	     * Called when the case record list is updated.
	     */
	    void onProvisioningSummaryListUpdated(AccountProvisioningListUpdateEvent event);
	  }

	  public static final Type<AccountProvisioningListUpdateEvent.Handler> TYPE = new Type<AccountProvisioningListUpdateEvent.Handler>();

	  private final List<AccountProvisioningSummaryPojo> provisioningSummaries;

	  public AccountProvisioningListUpdateEvent(List<AccountProvisioningSummaryPojo> provisioningSummaries) {
	    this.provisioningSummaries = provisioningSummaries;
	  }

	  @Override
	  public Type<AccountProvisioningListUpdateEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public List<AccountProvisioningSummaryPojo> getProvisioningSummaries() {
	    return provisioningSummaries;
	  }

	  @Override
	  protected void dispatch(AccountProvisioningListUpdateEvent.Handler handler) {
	    handler.onProvisioningSummaryListUpdated(this);
	  }
}
