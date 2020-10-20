package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;

public class RoleProvisioningStatusEvent extends GwtEvent<RoleProvisioningStatusEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditRoleProvisioningEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowRoleProvisioningSummaryStatus(RoleProvisioningStatusEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<RoleProvisioningStatusEvent.Handler> TYPE = new Type<RoleProvisioningStatusEvent.Handler>();

	  private final RoleProvisioningSummaryPojo roleProvisioningSummary;

	  public RoleProvisioningStatusEvent(RoleProvisioningSummaryPojo roleProvisioningSummary) {
	    this.roleProvisioningSummary = roleProvisioningSummary;
	  }

	  @Override
	  public final Type<RoleProvisioningStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public RoleProvisioningSummaryPojo getRoleProvisioningSummary() {
	    return roleProvisioningSummary;
	  }

	  @Override
	  protected void dispatch(RoleProvisioningStatusEvent.Handler handler) {
	    handler.onShowRoleProvisioningSummaryStatus(this);
	  }
}
