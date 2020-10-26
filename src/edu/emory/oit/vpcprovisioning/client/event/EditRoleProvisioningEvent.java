package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;

public class EditRoleProvisioningEvent extends GwtEvent<EditRoleProvisioningEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditRoleProvisioningEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onRoleProvisioningEdit(EditRoleProvisioningEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<EditRoleProvisioningEvent.Handler> TYPE = new Type<EditRoleProvisioningEvent.Handler>();

	  private final RoleProvisioningPojo roleProvisioning;

	  public EditRoleProvisioningEvent(RoleProvisioningPojo roleProvisioning) {
	    this.roleProvisioning = roleProvisioning;
	  }

	  @Override
	  public final Type<EditRoleProvisioningEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public RoleProvisioningPojo getRoleProvisioning() {
	    return roleProvisioning;
	  }

	  @Override
	  protected void dispatch(EditRoleProvisioningEvent.Handler handler) {
	    handler.onRoleProvisioningEdit(this);
	  }
}
