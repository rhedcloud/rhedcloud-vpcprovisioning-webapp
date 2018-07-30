package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;

public class EditServiceTestPlanEvent extends GwtEvent<EditServiceTestPlanEvent.Handler> {
	/**
	 * Implemented by objects that handle {@link EditServiceTestPlanEvent}.
	 */
	public interface Handler extends EventHandler {
		void onServiceTestPlanEdit(EditServiceTestPlanEvent event);
	}

	/**
	 * The event type.
	 */
	public static final Type<EditServiceTestPlanEvent.Handler> TYPE = new Type<EditServiceTestPlanEvent.Handler>();

	private final ServiceTestPlanPojo serviceTestPlan;
	private final ServiceSecurityAssessmentPojo assessment;

	public EditServiceTestPlanEvent(ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo control) {
		this.assessment = assessment;
		this.serviceTestPlan = control;
	}

	@Override
	public final Type<EditServiceTestPlanEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	public ServiceTestPlanPojo getServiceTestPlan() {
		return serviceTestPlan;
	}
	public ServiceSecurityAssessmentPojo getSecurityAssessment() {
		return assessment;
	}

	@Override
	protected void dispatch(EditServiceTestPlanEvent.Handler handler) {
		handler.onServiceTestPlanEdit(this);
	}
}
