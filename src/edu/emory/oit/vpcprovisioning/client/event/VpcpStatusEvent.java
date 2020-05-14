package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpcpSummaryPojo;

public class VpcpStatusEvent extends GwtEvent<VpcpStatusEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditVpcpEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowVpcpStatus(VpcpStatusEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<VpcpStatusEvent.Handler> TYPE = new Type<VpcpStatusEvent.Handler>();

	  private final VpcpSummaryPojo vpcpSummary;

	  public VpcpStatusEvent(VpcpSummaryPojo vpcpSummary) {
	    this.vpcpSummary = vpcpSummary;
	  }

	  @Override
	  public final Type<VpcpStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public VpcpSummaryPojo getVpcpSummary() {
	    return vpcpSummary;
	  }

	  @Override
	  protected void dispatch(VpcpStatusEvent.Handler handler) {
	    handler.onShowVpcpStatus(this);
	  }
}
