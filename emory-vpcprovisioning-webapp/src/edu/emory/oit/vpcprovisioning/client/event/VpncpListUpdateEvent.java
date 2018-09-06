package edu.emory.oit.vpcprovisioning.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpncpPojo;

public class VpncpListUpdateEvent extends GwtEvent<VpncpListUpdateEvent.Handler> {

	  /**
	   * Handler for {@link VpncpListUpdateEvent}.
	   */
	  public interface Handler extends EventHandler {
	  
	    /**
	     * Called when the case record list is updated.
	     */
	    void onVpncpListUpdated(VpncpListUpdateEvent event);
	  }

	  public static final Type<VpncpListUpdateEvent.Handler> TYPE = new Type<VpncpListUpdateEvent.Handler>();

	  private final List<VpncpPojo> vpncps;

	  public VpncpListUpdateEvent(List<VpncpPojo> vpncps) {
	    this.vpncps = vpncps;
	  }

	  @Override
	  public Type<VpncpListUpdateEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public List<VpncpPojo> getVpncps() {
	    return vpncps;
	  }

	  @Override
	  protected void dispatch(VpncpListUpdateEvent.Handler handler) {
	    handler.onVpncpListUpdated(this);
	  }
}
