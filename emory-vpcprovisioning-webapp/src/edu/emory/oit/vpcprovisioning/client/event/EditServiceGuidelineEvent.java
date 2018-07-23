package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class EditServiceGuidelineEvent extends GwtEvent<EditServiceGuidelineEvent.Handler> {
	/**
	 * Implemented by objects that handle {@link EditServiceGuidelineEvent}.
	 */
	public interface Handler extends EventHandler {
		void onServiceGuidelineEdit(EditServiceGuidelineEvent event);
	}

	/**
	 * The event type.
	 */
	public static final Type<EditServiceGuidelineEvent.Handler> TYPE = new Type<EditServiceGuidelineEvent.Handler>();

	private final ServiceGuidelinePojo serviceGuideline;
	private final ServiceSecurityAssessmentPojo assessment;

	public EditServiceGuidelineEvent(ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo control) {
		this.assessment = assessment;
		this.serviceGuideline = control;
	}

	@Override
	public final Type<EditServiceGuidelineEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	public ServiceGuidelinePojo getServiceGuideline() {
		return serviceGuideline;
	}
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return assessment;
	}

	@Override
	protected void dispatch(EditServiceGuidelineEvent.Handler handler) {
		handler.onServiceGuidelineEdit(this);
	}
}
