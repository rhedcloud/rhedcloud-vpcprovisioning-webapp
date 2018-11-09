package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;

public class EditVpnConnectionProvisioningEvent extends GwtEvent<EditVpnConnectionProvisioningEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditVpnConnectionProvisioningEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onVpnConnectionProvisioningEdit(EditVpnConnectionProvisioningEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<EditVpnConnectionProvisioningEvent.Handler> TYPE = new Type<EditVpnConnectionProvisioningEvent.Handler>();

	  private final VpnConnectionProvisioningPojo vpncp;

	  public EditVpnConnectionProvisioningEvent(VpnConnectionProvisioningPojo vpncp) {
	    this.vpncp = vpncp;
	  }

	  @Override
	  public final Type<EditVpnConnectionProvisioningEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public VpnConnectionProvisioningPojo getVpnConnectionProvisioning() {
	    return vpncp;
	  }

	  @Override
	  protected void dispatch(EditVpnConnectionProvisioningEvent.Handler handler) {
	    handler.onVpnConnectionProvisioningEdit(this);
	  }
}
