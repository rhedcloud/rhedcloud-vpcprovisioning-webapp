package edu.emory.oit.vpcprovisioning.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;

public class StaticNatProvisioningSummaryListUpdateEvent extends GwtEvent<StaticNatProvisioningSummaryListUpdateEvent.Handler> {

	  /**
	   * Handler for {@link StaticNatProvisioningSummaryListUpdateEvent}.
	   */
	  public interface Handler extends EventHandler {
	  
	    /**
	     * Called when the case record list is updated.
	     */
	    void onStaticNatProvisioningSummaryListUpdated(StaticNatProvisioningSummaryListUpdateEvent event);
	  }

	  public static final Type<StaticNatProvisioningSummaryListUpdateEvent.Handler> TYPE = new Type<StaticNatProvisioningSummaryListUpdateEvent.Handler>();

	  private final List<StaticNatProvisioningSummaryPojo> snps;

	  public StaticNatProvisioningSummaryListUpdateEvent(List<StaticNatProvisioningSummaryPojo> snps) {
	    this.snps = snps;
	  }

	  @Override
	  public Type<StaticNatProvisioningSummaryListUpdateEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public List<StaticNatProvisioningSummaryPojo> getStaticNatProvisioningSummarys() {
	    return snps;
	  }

	  @Override
	  protected void dispatch(StaticNatProvisioningSummaryListUpdateEvent.Handler handler) {
	    handler.onStaticNatProvisioningSummaryListUpdated(this);
	  }
}
