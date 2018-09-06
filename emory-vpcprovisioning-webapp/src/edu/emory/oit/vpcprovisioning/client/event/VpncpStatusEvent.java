package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpncpPojo;

public class VpncpStatusEvent extends GwtEvent<VpncpStatusEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditVpncpEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowVpncpStatus(VpncpStatusEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<VpncpStatusEvent.Handler> TYPE = new Type<VpncpStatusEvent.Handler>();

	  private final VpncpPojo vpncp;

	  public VpncpStatusEvent(VpncpPojo vpncp) {
	    this.vpncp = vpncp;
	  }

	  @Override
	  public final Type<VpncpStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public VpncpPojo getVpncp() {
	    return vpncp;
	  }

	  @Override
	  protected void dispatch(VpncpStatusEvent.Handler handler) {
	    handler.onShowVpncpStatus(this);
	  }
}
