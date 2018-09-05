package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;

public class StaticNatStatusEvent extends GwtEvent<StaticNatStatusEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditStaticNatProvisioningEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowStaticNatStatus(StaticNatStatusEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<StaticNatStatusEvent.Handler> TYPE = new Type<StaticNatStatusEvent.Handler>();

	  private final StaticNatProvisioningSummaryPojo summary;

	  public StaticNatStatusEvent(StaticNatProvisioningSummaryPojo summary) {
	    this.summary = summary;
	  }

	  @Override
	  public final Type<StaticNatStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public StaticNatProvisioningSummaryPojo getStaticNatProvisioningSummary() {
	    return summary;
	  }

	  @Override
	  protected void dispatch(StaticNatStatusEvent.Handler handler) {
	    handler.onShowStaticNatStatus(this);
	  }
}
