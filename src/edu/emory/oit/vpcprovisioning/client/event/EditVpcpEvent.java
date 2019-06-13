package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;

public class EditVpcpEvent extends GwtEvent<EditVpcpEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditVpcpEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onVpcpEdit(EditVpcpEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<EditVpcpEvent.Handler> TYPE = new Type<EditVpcpEvent.Handler>();

	  private final VpcpPojo vpcp;

	  public EditVpcpEvent(VpcpPojo vpcp) {
	    this.vpcp = vpcp;
	  }

	  @Override
	  public final Type<EditVpcpEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public VpcpPojo getVpcp() {
	    return vpcp;
	  }

	  @Override
	  protected void dispatch(EditVpcpEvent.Handler handler) {
	    handler.onVpcpEdit(this);
	  }
}
