package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningPojo;

public class GenerateAccountDeprovisioningEvent extends GwtEvent<GenerateAccountDeprovisioningEvent.Handler> {
	  /**
	   * Implemented by objects that handle {@link GenerateAccountDeprovisioningEvent}.
	   */
	  public interface Handler extends EventHandler {
	    void onAccountDeprovisioningGenerate(GenerateAccountDeprovisioningEvent event);
	  }

	  /**
	   * The event type.
	   */
	  public static final Type<GenerateAccountDeprovisioningEvent.Handler> TYPE = new Type<GenerateAccountDeprovisioningEvent.Handler>();

	  private final AccountDeprovisioningPojo accountDeprovisioning;

	  public GenerateAccountDeprovisioningEvent(AccountDeprovisioningPojo accountDeprovisioning) {
	    this.accountDeprovisioning = accountDeprovisioning;
	  }

	  @Override
	  public final Type<GenerateAccountDeprovisioningEvent.Handler> getAssociatedType() {
	    return TYPE;
	  }

	  public AccountDeprovisioningPojo getAccountDeprovisioning() {
	    return accountDeprovisioning;
	  }

	  @Override
	  protected void dispatch(GenerateAccountDeprovisioningEvent.Handler handler) {
	    handler.onAccountDeprovisioningGenerate(this);
	  }
}
