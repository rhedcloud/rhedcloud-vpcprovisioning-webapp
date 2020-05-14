package edu.emory.oit.vpcprovisioning.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.VpcpSummaryPojo;

public class VpcpListUpdateEvent extends GwtEvent<VpcpListUpdateEvent.Handler> {

	  /**
	   * Handler for {@link VpcpListUpdateEvent}.
	   */
	  public interface Handler extends EventHandler {
	  
	    /**
	     * Called when the case record list is updated.
	     */
	    void onVpcpListUpdated(VpcpListUpdateEvent event);
	  }

	  public static final Type<VpcpListUpdateEvent.Handler> TYPE = new Type<VpcpListUpdateEvent.Handler>();

	  private final List<VpcpSummaryPojo> vpcpSummaries;

	  public VpcpListUpdateEvent(List<VpcpSummaryPojo> vpcpSummaries) {
	    this.vpcpSummaries = vpcpSummaries;
	  }

	  @Override
	  public Type<VpcpListUpdateEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public List<VpcpSummaryPojo> getVpcpSummaries() {
	    return vpcpSummaries;
	  }

	  @Override
	  protected void dispatch(VpcpListUpdateEvent.Handler handler) {
	    handler.onVpcpListUpdated(this);
	  }
}
