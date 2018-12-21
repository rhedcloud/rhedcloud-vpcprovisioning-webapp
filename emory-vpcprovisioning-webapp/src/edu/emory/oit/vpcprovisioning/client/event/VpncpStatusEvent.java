package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;

public class VpncpStatusEvent extends GwtEvent<VpncpStatusEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditVpncpEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowVpncpSummaryStatus(VpncpStatusEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<VpncpStatusEvent.Handler> TYPE = new Type<VpncpStatusEvent.Handler>();

	  private final VpnConnectionProvisioningSummaryPojo vpncpSummary;

	  public VpncpStatusEvent(VpnConnectionProvisioningSummaryPojo vpncpSummary) {
	    this.vpncpSummary = vpncpSummary;
	  }

	  @Override
	  public final Type<VpncpStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public VpnConnectionProvisioningSummaryPojo getVpncpSummary() {
	    return vpncpSummary;
	  }

	  @Override
	  protected void dispatch(VpncpStatusEvent.Handler handler) {
	    handler.onShowVpncpSummaryStatus(this);
	  }
}
