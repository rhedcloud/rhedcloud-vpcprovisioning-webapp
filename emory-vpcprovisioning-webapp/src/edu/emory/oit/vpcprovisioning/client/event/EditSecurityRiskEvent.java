package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class EditSecurityRiskEvent extends GwtEvent<EditSecurityRiskEvent.Handler> {
	/**
	 * Implemented by objects that handle {@link EditSecurityRiskEvent}.
	 */
	public interface Handler extends EventHandler {
		void onSecurityRiskEdit(EditSecurityRiskEvent event);
	}

	/**
	 * The event type.
	 */
	public static final Type<EditSecurityRiskEvent.Handler> TYPE = new Type<EditSecurityRiskEvent.Handler>();

	private final SecurityRiskPojo securityRisk;
	private final ServiceSecurityAssessmentPojo assessment;

	public EditSecurityRiskEvent(ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo risk) {
		this.assessment = assessment;
		this.securityRisk = risk;
	}

	@Override
	public final Type<EditSecurityRiskEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	public SecurityRiskPojo getSecurityRisk() {
		return securityRisk;
	}
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return assessment;
	}

	@Override
	protected void dispatch(EditSecurityRiskEvent.Handler handler) {
		handler.onSecurityRiskEdit(this);
	}
}
