package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditServiceTestPlanEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainServiceTestPlanActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainServiceTestPlanPlace place;
	private AWSServicePojo service;
	private ServiceSecurityAssessmentPojo assessment;
	private ServiceTestPlanPojo serviceTestPlan;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainServiceTestPlanActivity(ClientFactory clientFactory, MaintainServiceTestPlanPlace place) {
		this.place = place;
		this.service = place.getService();
		this.assessment = place.getAssessment();
		this.serviceTestPlan = place.getServiceTestPlan();
		this.clientFactory = clientFactory;
	}

	@Override
	public String mayStop() {
		return presenter.mayStop();
	}

	@Override
	public void onCancel() {
		presenter.stop();
	}

	@Override
	public void onStop() {
		childEventBus.removeHandlers();
		presenter.stop();
	}

	public void start(final AcceptsOneWidget container, com.google.gwt.event.shared.EventBus eventBus) {
		this.childEventBus = new ResettableEventBus(eventBus);
		eventBus.addHandler(EditServiceTestPlanEvent.TYPE, new EditServiceTestPlanEvent.Handler() {
			@Override
			public void onServiceTestPlanEdit(EditServiceTestPlanEvent event) {
				onStop();
				presenter = startEdit(service, assessment, event.getServiceTestPlan());
				container.setWidget(presenter);
			}
		});

		if (place.getServiceTestPlan() == null) {
			presenter = startCreate(service, assessment);
		} else {
			presenter = startEdit(place.getService(), place.getAssessment(), place.getServiceTestPlan());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		PresentsWidgets rtn = new MaintainServiceTestPlanPresenter(clientFactory, service, assessment);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceTestPlanPojo serviceTestPlan) {
		PresentsWidgets rtn = new MaintainServiceTestPlanPresenter(clientFactory, service, assessment, serviceTestPlan);
		rtn.start(childEventBus);
		return rtn;
	}

	public ServiceTestPlanPojo getServiceTestPlan() {
		return serviceTestPlan;
	}

	public void setServiceTestPlan(ServiceTestPlanPojo serviceTestPlan) {
		this.serviceTestPlan = serviceTestPlan;
	}
}
