package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningPojo;

public class StaticNatStatusEvent extends GwtEvent<StaticNatStatusEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link EditStaticNatProvisioningEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onShowStaticNatStatus(StaticNatStatusEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<StaticNatStatusEvent.Handler> TYPE = new Type<StaticNatStatusEvent.Handler>();

	  private final StaticNatProvisioningPojo snp;
	  private final StaticNatDeprovisioningPojo sndp;

	  public StaticNatStatusEvent(StaticNatProvisioningPojo snp, StaticNatDeprovisioningPojo sndp) {
	    this.snp = snp;
	    this.sndp = sndp;
	  }

	  @Override
	  public final Type<StaticNatStatusEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public StaticNatProvisioningPojo getStaticNatProvisioning() {
	    return snp;
	  }

	  public StaticNatDeprovisioningPojo getStaticNatDeprovisioning() {
		    return sndp;
		  }

	  @Override
	  protected void dispatch(StaticNatStatusEvent.Handler handler) {
	    handler.onShowStaticNatStatus(this);
	  }
}
