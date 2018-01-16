package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class RegisterVpcEvent extends GwtEvent<RegisterVpcEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditVpcEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onVpcRegister(RegisterVpcEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<RegisterVpcEvent.Handler> TYPE = new Type<RegisterVpcEvent.Handler>();

	  private final VpcPojo vpc;

	  public RegisterVpcEvent(VpcPojo acct) {
	    this.vpc = acct;
	  }

	  @Override
	  public final Type<RegisterVpcEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public VpcPojo getVpc() {
	    return vpc;
	  }

	  @Override
	  protected void dispatch(RegisterVpcEvent.Handler handler) {
	    handler.onVpcRegister(this);
	  }
}
