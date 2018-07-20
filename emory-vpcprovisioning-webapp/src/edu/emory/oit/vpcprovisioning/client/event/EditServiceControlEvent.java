package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class EditServiceControlEvent extends GwtEvent<EditServiceControlEvent.Handler> {
	/**
	 * Implemented by objects that handle {@link EditServiceControlEvent}.
	 */
	public interface Handler extends EventHandler {
		void onServiceControlEdit(EditServiceControlEvent event);
	}

	/**
	 * The event type.
	 */
	public static final Type<EditServiceControlEvent.Handler> TYPE = new Type<EditServiceControlEvent.Handler>();

	private final ServiceControlPojo serviceControl;
	private final ServiceSecurityAssessmentPojo assessment;

	public EditServiceControlEvent(ServiceSecurityAssessmentPojo assessment, ServiceControlPojo control) {
		this.assessment = assessment;
		this.serviceControl = control;
	}

	@Override
	public final Type<EditServiceControlEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	public ServiceControlPojo getServiceControl() {
		return serviceControl;
	}
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return assessment;
	}

	@Override
	protected void dispatch(EditServiceControlEvent.Handler handler) {
		handler.onServiceControlEdit(this);
	}
}
