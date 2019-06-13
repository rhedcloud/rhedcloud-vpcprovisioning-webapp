package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;

public class GenerateIncidentEvent extends GwtEvent<GenerateIncidentEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link GenerateIncidentEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onIncidentGenerate(GenerateIncidentEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<GenerateIncidentEvent.Handler> TYPE = new Type<GenerateIncidentEvent.Handler>();

	  private final IncidentPojo incident;

	  public GenerateIncidentEvent(IncidentPojo incident) {
	    this.incident = incident;
	  }

	  @Override
	  public final Type<GenerateIncidentEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public IncidentPojo getIncident() {
	    return incident;
	  }

	  @Override
	  protected void dispatch(GenerateIncidentEvent.Handler handler) {
	    handler.onIncidentGenerate(this);
	  }
}
