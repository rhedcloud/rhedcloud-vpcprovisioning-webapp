package edu.emory.oit.vpcprovisioning.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.RoleAssignmentSummaryPojo;

public class CentralAdminListUpdateEvent extends GwtEvent<CentralAdminListUpdateEvent.Handler> {

	  /**
	   * Handler for {@link CentralAdminListUpdateEvent}.
	   */
	  public interface Handler extends EventHandler {
	  
	    /**
	     * Called when the case record list is updated.
	     */
	    void onCentralAdminListUpdated(CentralAdminListUpdateEvent event);
	  }

	  public static final Type<CentralAdminListUpdateEvent.Handler> TYPE = new Type<CentralAdminListUpdateEvent.Handler>();

	  private final List<RoleAssignmentSummaryPojo> centralAdmins;

	  public CentralAdminListUpdateEvent(List<RoleAssignmentSummaryPojo> centralAdmins) {
	    this.centralAdmins = centralAdmins;
	  }

	  @Override
	  public Type<CentralAdminListUpdateEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public List<RoleAssignmentSummaryPojo> getCentralAdmins() {
	    return centralAdmins;
	  }

	  @Override
	  protected void dispatch(CentralAdminListUpdateEvent.Handler handler) {
	    handler.onCentralAdminListUpdated(this);
	  }
}
