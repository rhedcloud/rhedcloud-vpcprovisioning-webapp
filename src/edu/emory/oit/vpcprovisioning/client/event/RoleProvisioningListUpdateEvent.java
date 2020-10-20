package edu.emory.oit.vpcprovisioning.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;

public class RoleProvisioningListUpdateEvent extends GwtEvent<RoleProvisioningListUpdateEvent.Handler> {

	  /**
	   * Handler for {@link RoleProvisioningListUpdateEvent}.
	   */
	  public interface Handler extends EventHandler {
	  
	    /**
	     * Called when the case record list is updated.
	     */
	    void onRoleProvisioningListUpdated(RoleProvisioningListUpdateEvent event);
	  }

	  public static final Type<RoleProvisioningListUpdateEvent.Handler> TYPE = new Type<RoleProvisioningListUpdateEvent.Handler>();

	  private final List<RoleProvisioningSummaryPojo> vpncps;

	  public RoleProvisioningListUpdateEvent(List<RoleProvisioningSummaryPojo> vpncps) {
	    this.vpncps = vpncps;
	  }

	  @Override
	  public Type<RoleProvisioningListUpdateEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public List<RoleProvisioningSummaryPojo> getRoleProvisionings() {
	    return vpncps;
	  }

	  @Override
	  protected void dispatch(RoleProvisioningListUpdateEvent.Handler handler) {
	    handler.onRoleProvisioningListUpdated(this);
	  }
}
