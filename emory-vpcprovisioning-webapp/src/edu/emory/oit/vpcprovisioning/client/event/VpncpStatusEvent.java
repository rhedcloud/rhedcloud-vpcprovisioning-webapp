package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;

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

	  private final VpnConnectionProvisioningPojo vpncp;

	  public VpncpStatusEvent(VpnConnectionProvisioningPojo vpncp) {
	    this.vpncp = vpncp;
	  }

	  @Override
	  public final Type<VpncpStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public VpnConnectionProvisioningPojo getVpncp() {
	    return vpncp;
	  }

	  @Override
	  protected void dispatch(VpncpStatusEvent.Handler handler) {
	    handler.onShowVpncpStatus(this);
	  }
}
